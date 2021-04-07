package lmr.randomizer;

import lmr.randomizer.dat.AddObject;
import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.DatReader;
import lmr.randomizer.dat.DatWriter;
import lmr.randomizer.node.*;
import lmr.randomizer.random.*;
import lmr.randomizer.rcd.RcdReader;
import lmr.randomizer.rcd.RcdWriter;
import lmr.randomizer.rcd.object.Zone;
import lmr.randomizer.ui.ButtonPanel;
import lmr.randomizer.ui.MainPanel;
import lmr.randomizer.ui.ProgressDialog;
import lmr.randomizer.ui.TabbedPanel;
import lmr.randomizer.update.GameDataTracker;
import lmr.randomizer.update.LocationCoordinateMapper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.*;

/**
 * Created by thezerothcat on 7/9/2017.
 */
public class Main {
    public static void main(String[] args) {
        try {
            FileUtils.readSettings();
            if(args.length > 0) {
                Settings.setSkipValidation(Integer.parseInt(args[0]));
            }
            Translations.initTranslations();
        }
        catch (Exception ex) {
            FileUtils.log("Unable to initialize: " + ex.getMessage()); // todo: should probably start the UI with an error
            FileUtils.logException(ex);
        }
        SwingUtilities.invokeLater(new RandomizerRunnable());
    }

    static class RandomizerRunnable implements Runnable {
        @Override
        public void run() {
            RandomizerUI randomizerUI = new RandomizerUI();
            randomizerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    FileUtils.closeAll();
                }
            });
            randomizerUI.setVisible(true);
        }
    }

    public static class RandomizerUI extends JFrame implements ActionListener {
        private MainPanel mainPanel;
        private ProgressDialog progressDialog;
        private TabbedPanel tabbedPanel;
        private ButtonPanel buttonPanel;

        public RandomizerUI() {
            try {
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            setTitle(String.format(Translations.getText("title"), FileUtils.VERSION));
            setLayout(new MigLayout("fill, aligny top", "[]", "[]"));
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setResizable(true);

            mainPanel = new MainPanel();
            add(mainPanel, "growx, wrap");

            tabbedPanel = new TabbedPanel(mainPanel);
            add(tabbedPanel, "growx, wrap");

            mainPanel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainPanel.updateTranslations();
                    tabbedPanel.updateTranslations();
                    buttonPanel.updateTranslations();
                    progressDialog.updateTranslations();
                    pack();
                }
            });

            progressDialog = new ProgressDialog(this);
            JFrame mainWindow = this;
            progressDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if(progressDialog.isSafeClose()) {
                        mainWindow.dispatchEvent(new WindowEvent(mainWindow,WindowEvent.WINDOW_CLOSING));
                    }
                }
            });

            buttonPanel = new ButtonPanel(this);
            add(buttonPanel, "grow");
            pack();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mainPanel.updateSettings();
            tabbedPanel.updateSettings();
            // Any forced temporary settings updates can go here.

            if("generate".equals(e.getActionCommand())) {
                try {
                    generateSeed();
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            ex.getMessage(),
                            "Randomizer error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if("apply".equals(e.getActionCommand())) {
                try {
                    generateAndApply();
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            ex.getMessage(),
                            "Randomizer error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if("importSeed".equals(e.getActionCommand())) {
                Settings.saveSettings();
//                if(validateInstallDir()) {
//                    if(validateSaveDir()) {
                        JFileChooser zipFileChooser = new JFileChooser();
                        if(zipFileChooser.showOpenDialog(this.getParent()) == JFileChooser.APPROVE_OPTION) {
                            Settings.saveSettings();
                            if(FileUtils.importExistingSeed(zipFileChooser.getSelectedFile())) {
                                JOptionPane.showMessageDialog(this,
                                        "La-Mulana has been updated.",
                                        "Import success!", JOptionPane.PLAIN_MESSAGE);
                            }
                            else {
                                JOptionPane.showMessageDialog(this,
                                        "Import failed",
                                        "Randomizer error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
//                    }
//                    else {
//                        JOptionPane.showMessageDialog(this,
//                                "Unable to find La-Mulana save directory",
//                                "Randomizer error", JOptionPane.ERROR_MESSAGE);
//                    }
//                }
//                else {
//                    JOptionPane.showMessageDialog(this,
//                            "Unable to find La-Mulana install directory",
//                            "Randomizer error", JOptionPane.ERROR_MESSAGE);
//                }
            }
            else if("restore".equals(e.getActionCommand())) {
                if(!Validation.validateInstallDir(this)) {
                    return;
                }

                try {
                    this.progressDialog.updateProgress(0, Translations.getText("restore.start"));
                    Frame f = this;
                    SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            progressDialog.setLocationRelativeTo(f);
                            try {
                                restore();
                            }
                            catch (Exception ex) {
                                JOptionPane.showMessageDialog(f,
                                        ex.getMessage(),
                                        "Randomizer error", JOptionPane.ERROR_MESSAGE);
                            }

                            return null;
                        }
                    };
                    swingWorker.execute();
                    progressDialog.setVisible(true);
                } catch (Exception ex) {
                    FileUtils.log("Error: " + ex.getMessage());
                    FileUtils.logException(ex);
                }
            }
            else if("restoreSaves".equals(e.getActionCommand())) {
                if(!Validation.validateSaveDir(this)) {
                    return;
                }

                try {
                    this.progressDialog.updateProgress(0, Translations.getText("restore.start"));
                    Frame f = this;
                    SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            progressDialog.setLocationRelativeTo(f);
                            try {
                                restoreSaves();
                            }
                            catch (Exception ex) {
                                JOptionPane.showMessageDialog(f,
                                        ex.getMessage(),
                                        "Randomizer error", JOptionPane.ERROR_MESSAGE);
                            }

                            return null;
                        }
                    };
                    swingWorker.execute();
                    progressDialog.setVisible(true);
                } catch (Exception ex) {
                    FileUtils.log("Error: " + ex.getMessage());
                    FileUtils.logException(ex);
                }
            }
        }

        private boolean generateSeed() {
            if(!validateSettings()) {
                Settings.setCurrentStartingLocation(1);
                DataFromFile.clearCustomPlacementData();
                DataFromFile.clearAllData();
                return false;
            }

            progressDialog.updateProgress(0, Translations.getText("progress.generating"));

            Settings.saveSettings();

            HolidayModePlacements.applyCustomPlacements();

            // Any forced temporary plando settings can go here.

            progressDialog.updateProgress(10, Translations.getText("setup.backup"));

            if(!backupRcd()) {
                return false; // Error messaging handled within
            }
            if(!backupDat()) {
                return false; // Error messaging handled within
            }
            if(!backupMsd()) {
                return false; // Error messaging handled within
            }

            progressDialog.updateProgress(15, Translations.getText("setup.dir"));

            File directory = new File(Long.toString(Settings.getStartingSeed()));
            directory.mkdir();

            try {
                Frame f = this;
                SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        progressDialog.setLocationRelativeTo(f);
                        try {
                            doTheThing(progressDialog, f);
                            mainPanel.rerollRandomSeed();
                        }
                        catch (Exception ex) {
                            FileUtils.logException(ex);
                            JOptionPane.showMessageDialog(f,
                                    "Unknown error. Please see logs for more information.",
                                    "Randomizer error", JOptionPane.ERROR_MESSAGE);
                            Settings.setCurrentStartingLocation(1);
                            DataFromFile.clearCustomPlacementData();
                            DataFromFile.clearAllData();
                            SwingUtilities.invokeLater(() -> {
                                progressDialog.setVisible(false);
                                progressDialog.setSafeClose(true);
                            });
                        }
                        return null;
                    }
                };
                swingWorker.execute();
                progressDialog.setVisible(true);
            } catch (Exception ex) {
                FileUtils.log("Error: " + ex.getMessage());
                FileUtils.logException(ex);
                throw new RuntimeException("Unknown error. Please see logs for more information.");
            }
            return true;
        }

        private boolean backupRcd() {
            File rcdFile = new File("script.rcd.bak");
            if(!rcdFile.exists()) {
                File existingRcd = new File(Settings.getLaMulanaBaseDir(), "data/mapdata/script.rcd");
                if(!existingRcd.exists()) {
                    JOptionPane.showMessageDialog(this,
                            "Unable to find file " + existingRcd.getAbsolutePath(),
                            "Randomizer error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                else if (!FileUtils.hashRcdFile(existingRcd)) {
                    JOptionPane.showMessageDialog(this,
                            "The data/mapdata/script.rcd file in the game directory is not original! Please restore it from a backup / clean install!",
                            "Randomizer error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                try {
                    // Make script.rcd backup
                    FileOutputStream fileOutputStream = new FileOutputStream(new File("script.rcd.bak"));
                    Files.copy(existingRcd.toPath(), fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                catch (Exception ex) {
                    FileUtils.log("Unable to back up script.rcd: " + ex.getMessage());
                    FileUtils.logException(ex);
                    throw new RuntimeException("Unable to back up script.rcd. Please see logs for more information.");
                }
            }
            return true;
        }

        private boolean backupMsd() {
            File msdFile = new File("map13.msd.bak");
            if(!msdFile.exists()) {
                File existingMsd = new File(Settings.getLaMulanaBaseDir(), "data/mapdata/map13.msd");
                if(!existingMsd.exists()) {
                    JOptionPane.showMessageDialog(this,
                            "Unable to find file " + existingMsd.getAbsolutePath(),
                            "Randomizer error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                try {
                    // Make map13.msd backup
                    FileOutputStream fileOutputStream = new FileOutputStream(new File("map13.msd.bak"));
                    Files.copy(existingMsd.toPath(), fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                catch (Exception ex) {
                    FileUtils.log("Unable to back up map13.msd: " + ex.getMessage());
                    FileUtils.logException(ex);
                    throw new RuntimeException("Unable to back up map13.msd. Please see logs for more information.");
                }
            }
            return true;
        }

        private boolean backupDat() {
            File datFile = new File(Settings.getBackupDatFile());
            if(!datFile.exists()) {
                File existingDat = new File(String.format("%s/data/language/%s/script_code.dat",
                        Settings.getLaMulanaBaseDir(), Settings.getLanguage()));
                if(!FileUtils.hashDatFile(existingDat)) {
                    JOptionPane.showMessageDialog(this,
                            "Unable to back up script_code.dat - file already modified",
                            "Randomizer error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                try {
                    // Make script_code.dat backup
                    Files.copy(existingDat.toPath(),
                            new FileOutputStream(new File(Settings.getBackupDatFile())));
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Unable to back up script.rcd. Please see logs for more information.",
                            "Randomizer error", JOptionPane.ERROR_MESSAGE);
                    FileUtils.log("Unable to back up script_code.dat: " + ex.getMessage());
                    FileUtils.logException(ex);
                    throw new RuntimeException("Unable to back up script_code.dat");
                }
            }
            return true;
        }

        private void generateAndApply() {
            try {
                progressDialog.updateProgress(0, Translations.getText("setup.start"));
                if(!generateSeed()) {
                    return;
                }

                FileUtils.logFlush("Copying rcd file from seed folder to La-Mulana install directory");
                FileOutputStream fileOutputStream = new FileOutputStream(new File(Settings.getLaMulanaBaseDir() + "/data/mapdata/script.rcd"));
                Files.copy(new File(String.format("%s/script.rcd", Settings.getStartingSeed())).toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                FileUtils.logFlush("rcd copy complete");

                FileUtils.logFlush("Copying dat file from seed folder to La-Mulana install directory");
                fileOutputStream = new FileOutputStream(new File(String.format("%s/data/language/%s/script_code.dat",
                        Settings.getLaMulanaBaseDir(), Settings.getLanguage())));
                Files.copy(new File(String.format("%s/script_code.dat", Settings.getStartingSeed())).toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                FileUtils.logFlush("dat copy complete");

                if(Settings.isFools2021Mode()) {
                    FileUtils.logFlush("Copying msd file from seed folder to La-Mulana install directory");
                    fileOutputStream = new FileOutputStream(new File(String.format("%s/data/mapdata/map13.msd",
                            Settings.getLaMulanaBaseDir(), Settings.getLanguage())));
                    Files.copy(new File(String.format("%s/map13.msd", Settings.getStartingSeed())).toPath(), fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    FileUtils.logFlush("msd copy complete");
                }

                if(Settings.isSaveFileNeeded()) {
                    FileUtils.logFlush("Copying save file from seed folder to La-Mulana save directory");
                    File saveFile = new File(String.format("%s/lm_00.sav", Settings.getStartingSeed()));
                    if(saveFile.exists()) {
                        fileOutputStream = new FileOutputStream(
                                new File(String.format("%s/lm_00.sav", Settings.getLaMulanaSaveDir())));
                        Files.copy(saveFile.toPath(), fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }
                FileUtils.logFlush("Save file copy complete");

                FileUtils.closeAll();
            }
            catch (Exception ex) {
                FileUtils.log("Unable to update game files for La-Mulana:" + ex.getMessage());
                FileUtils.logException(ex);
                throw new RuntimeException("Unable to update game files for La-Mulana. Please see logs for more information.");
            }
        }

        private void restore() {
            try {
                progressDialog.updateProgress(0, Translations.getText("restore.rcd"));

                FileOutputStream fileOutputStream = new FileOutputStream(new File(Settings.getLaMulanaBaseDir() + "/data/mapdata/script.rcd"));
                Files.copy(new File("script.rcd.bak").toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                progressDialog.updateProgress(30, Translations.getText("restore.dat"));

                fileOutputStream = new FileOutputStream(new File(String.format("%s/data/language/%s/script_code.dat",
                        Settings.getLaMulanaBaseDir(), Settings.getLanguage())));
                Files.copy(new File(Settings.getBackupDatFile()).toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                progressDialog.updateProgress(60, Translations.getText("restore.msd"));

                fileOutputStream = new FileOutputStream(new File(Settings.getLaMulanaBaseDir() + "/data/mapdata/map13.msd"));
                Files.copy(new File("map13.msd.bak").toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                progressDialog.updateProgress(100, Translations.getText("restore.done"));

                SwingUtilities.invokeLater(() -> {
                    try {
                        Thread.sleep(2000);
                        progressDialog.setVisible(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
            catch (Exception ex) {
                FileUtils.log("Unable to restore files to La-Mulana install: " + ex.getMessage());
                FileUtils.logException(ex);
                FileUtils.flush();
                throw new RuntimeException("Unable to restore files to La-Mulana install. Please see logs for more information.");
            }
        }

        private void restoreSaves() {
            try {
                progressDialog.updateProgress(0, Translations.getText("restore.save"));

                File backupSave = new File(String.format("%s/lm_00.sav.lmr.bak", Settings.getLaMulanaSaveDir()));
                File existingSave = new File(String.format("%s/lm_00.sav", Settings.getLaMulanaSaveDir()));
                FileOutputStream fileOutputStream = new FileOutputStream(existingSave);
                Files.copy(backupSave.toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                progressDialog.updateProgress(100, Translations.getText("restore.done"));

                SwingUtilities.invokeLater(() -> {
                    try {
                        Thread.sleep(2000);
                        progressDialog.setVisible(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
            catch (Exception ex) {
                FileUtils.log("Unable to restore save file: " + ex.getMessage());
                FileUtils.logException(ex);
                throw new RuntimeException("Unable to restore save file. Please see logs for more information.");
            }
        }

        private boolean validateSettings() {
            if(!Validation.validateInstallDir(this)) {
                return false;
            }
            if(Settings.isSaveFileNeeded() && !Validation.validateSaveDir(this)) {
                return false;
            }
            if(!Validation.validateGraphicsPack(this)) {
                return false;
            }
            if(!Validation.validateSettingCombinations(this)) {
                return false;
            }
            if(!Validation.validateHalloween(this)) {
                return false;
            }
            if(!Validation.validateFools2020(this)) {
                return false;
            }
            if(!Validation.validateFools2021(this)) {
                return false;
            }
            if(!Validation.validateCustomPlacements(this)) {
                return false;
            }
            if (!Validation.validateRemovedItems(this)) {
                return false;
            }
            return true;
        }
    }

    protected static void doTheThing(ProgressDialog dialog, Frame f) throws Exception {
        FileUtils.log(String.format("Shuffling items for seed %s", Settings.getStartingSeed()));
        FileUtils.log("Settings string: " + Settings.generateShortString());
        if(DataFromFile.getCustomPlacementData().isCustomized()) {
            FileUtils.log("Custom placement data has been found and applied.");
        }

        DataFromFile.clearAllData();

        Random random = new Random(Settings.getStartingSeed());
        if(Settings.isRandomizeTrapItems()) {
            // For flag space reasons, screens which contain more than one chest can have only one of those chests
            // contain a trap item. We'll randomly pick one location from each set to be the valid one for having
            // the trap item.
            DataFromFile.setBannedTrapLocations(random);
        }
        if(Settings.isFools2019Mode() && DataFromFile.getCustomPlacementData().getMedicineColor() == null) {
            List<String> medicineColors = Arrays.asList("Red", "Green", "Yellow", null);
            Settings.setMedicineColor(medicineColors.get(random.nextInt(medicineColors.size())));
        }

        int totalItemsRemoved = getTotalItemsRemoved(random);
        determineStartingLocation(random);
        determineStartingWeapon(random);
        determineFoolsGameplay(random);
        determineGiant(random);

        BacksideDoorRandomizer backsideDoorRandomizer = new BacksideDoorRandomizer();
        backsideDoorRandomizer.determineDoorDestinations(random);
        backsideDoorRandomizer.logLocations();

        TransitionGateRandomizer transitionGateRandomizer = new TransitionGateRandomizer(backsideDoorRandomizer);

        SealRandomizer sealRandomizer = new SealRandomizer();
        NpcRandomizer npcRandomizer = new NpcRandomizer();

        Set<String> initiallyAccessibleItems = getInitiallyAvailableItems();

        int attempt = 0;
        long startTime = System.nanoTime(), uiUpdateTime = startTime - (long)1e9;
        class ProgressUpdate {
            public long time;
            public int attempt;
            public ProgressUpdate(long time, int attempt) {
                this.time = time;
                this.attempt = attempt;
            }
        }
        int totalFakeAttempts = Settings.isFools2020Mode() ? random.nextInt(4120) : 0; // Use random not from seed, to avoid messing things up.
        var updateHistory = new LinkedList<ProgressUpdate>();
        updateHistory.add(new ProgressUpdate(startTime, 0));
        while(true) {
            ++attempt;
            long now = System.nanoTime();

            determineRemovedItems(totalItemsRemoved, random);

            if (now - uiUpdateTime > (long)100e6) {
                uiUpdateTime = now;
                var progressUpdate = new ProgressUpdate(now, attempt);
                updateHistory.add(progressUpdate);
                double attemptRate = 0.;
                int attempts = attempt - updateHistory.getFirst().attempt;
                long nanosecs = now - updateHistory.getFirst().time;
                if (nanosecs > 0)
                    attemptRate = (double)attempts * 1e9 / nanosecs;
                if (nanosecs > (long)5e9)
                    updateHistory.removeFirst();
                dialog.updateProgress(25, String.format(Translations.getText("progress.shuffling"), attempt, attemptRate));
                dialog.setTitle(String.format(Translations.getText("progress.shuffling.title"), attempt));
                dialog.progressBar.setIndeterminate(true);
            }

            transitionGateRandomizer.determineGateDestinations(random);
            backsideDoorRandomizer.determineDoorBosses(random, attempt);
            sealRandomizer.assignSeals(random);
            npcRandomizer.determineNpcLocations(random);

            ItemRandomizer itemRandomizer = new ItemRandomizer();
            ShopRandomizer shopRandomizer = buildShopRandomizer(itemRandomizer);
            AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer, backsideDoorRandomizer, transitionGateRandomizer, sealRandomizer, npcRandomizer);

            List<String> startingNodes = getStartingNodes();

            if(!initiallyAccessibleItems.isEmpty()) {
                DataFromFile.clearInitialLocations();
                AccessChecker initiallyAccessibleLocationFinder = new AccessChecker(accessChecker, true);
                initiallyAccessibleLocationFinder.computeStartingLocationAccess(false, null);
                for (String startingNode : startingNodes) {
                    initiallyAccessibleLocationFinder.computeAccessibleNodes(startingNode, false, null);
                }
                while (!initiallyAccessibleLocationFinder.getQueuedUpdates().isEmpty()) {
                    // Get some additional access based on glitch logic/settings access
                    initiallyAccessibleLocationFinder.computeAccessibleNodes(initiallyAccessibleLocationFinder.getQueuedUpdates().iterator().next(), false, null);
                }
            }

            itemRandomizer.placeNonRandomizedItems();
            shopRandomizer.placeNonRandomizedItems();
            if(ItemRandomizer.ALL_SUBWEAPONS.contains(Settings.getCurrentStartingWeapon())) {
                shopRandomizer.placeSpecialSubweaponAmmo(random);
            }
            if(ShopRandomizationEnum.EVERYTHING.equals(Settings.getShopRandomization())) {
                ((EverythingShopRandomizer)shopRandomizer).placeGuaranteedWeights(random);
            }
            if(Settings.isRandomizeForbiddenTreasure()) {
                itemRandomizer.placeForbiddenTreasureItem(random);
            }
            shopRandomizer.determineItemTypes(random);
            accessChecker.determineCursedChests(random);
            if(Settings.isRandomizeCoinChests() || Settings.isRandomizeTrapItems()) {
                if(!itemRandomizer.placeChestOnlyItems(random)) {
                    continue;
                }
            }

            if(!itemRandomizer.placeNoRequirementItems(new ArrayList<>(initiallyAccessibleItems), random)) {
                continue;
            }

            if(!itemRandomizer.placeAllItems(random)) {
                continue;
            }

            if(!Settings.isSkipValidation(attempt)) {
                accessChecker.computeStartingLocationAccess(true, attempt);
                for (String startingNode : startingNodes) {
                    accessChecker.computeAccessibleNodes(startingNode, attempt);
                }

                if(Settings.isBossSpecificAnkhJewels()) {
                    while (!accessChecker.getQueuedUpdates().isEmpty()) {
                        accessChecker.computeAccessibleNodes(accessChecker.getQueuedUpdates().iterator().next(), attempt);
                    }
                }
                else {
                    boolean ankhJewelLock = false;
                    if (accessChecker.getQueuedUpdates().isEmpty()) {
                        if (!accessChecker.updateForBosses()) {
                            ankhJewelLock = true;
                        }
                    }

                    if (!ankhJewelLock) {
                        while (!accessChecker.getQueuedUpdates().isEmpty()) {
                            accessChecker.computeAccessibleNodes(accessChecker.getQueuedUpdates().iterator().next(), attempt);
                            if (accessChecker.getQueuedUpdates().isEmpty()) {
                                if (!accessChecker.isEnoughAnkhJewelsToDefeatAllAccessibleBosses()) {
                                    accessChecker.logAnkhJewelLock();
                                    ankhJewelLock = true;
                                    break;
                                }
                                if (!accessChecker.updateForBosses()) {
                                    ankhJewelLock = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (ankhJewelLock) {
                        if(Settings.isGenerationComplete(attempt)) {
                            FileUtils.log(String.format("Detected ankh jewel lock on attempt %s, but validation is skipped. Generating seed anyway.", attempt));
                        }
                        else {
                            FileUtils.log(String.format("Detected ankh jewel lock on attempt %s. Re-shuffling items.", attempt));
                            continue;
                        }
                    }
                }
            }
            if(Settings.isFools2020Mode() && attempt < totalFakeAttempts) {
                continue;
            }
            if(Settings.isGenerationComplete(attempt) || accessChecker.isSuccess(attempt)) {
                dialog.progressBar.setIndeterminate(false);
                dialog.setSafeClose(false);
                dialog.updateProgress(80, String.format(Translations.getText("progress.shuffling.done"), attempt));

                FileUtils.log(String.format("Successful attempt %s.", attempt));
                FileUtils.flush();

                MoneyChecker moneyChecker;
                if(Settings.isRandomizeTransitionGates()) {
                    moneyChecker = new MoneyChecker(itemRandomizer, shopRandomizer, backsideDoorRandomizer, transitionGateRandomizer);
                    backsideDoorRandomizer.rebuildRequirementsMap();
                    moneyChecker.computeStartingLocationAccess(attempt);
                    for (String startingNode : startingNodes) {
                        moneyChecker.computeAccessibleNodes(startingNode, attempt);
                    }
                    while(!moneyChecker.getQueuedUpdates().isEmpty()) {
                        moneyChecker.computeAccessibleNodes(moneyChecker.getQueuedUpdates().iterator().next(), attempt);
                    }

                    transitionGateRandomizer.placeTowerOfTheGoddessPassthroughPipe(random);
                    FileUtils.logDetail("Placed pipe transitions", attempt);
                }
                else {
                    moneyChecker = null;
                }

                dialog.updateProgress(85, Translations.getText("progress.spoiler"));
                outputLocations(itemRandomizer, shopRandomizer, backsideDoorRandomizer, transitionGateRandomizer, attempt);

                dialog.updateProgress(90, Translations.getText("progress.read"));

                List<Zone> rcdData = RcdReader.getRcdScriptInfo();
                FileUtils.logFlush("rcd file successfully read");

                List<Block> datInfo = DatReader.getDatScriptInfo();
                FileUtils.logFlush("dat file successfully read");

                dialog.updateProgress(95, Translations.getText("progress.write"));
                itemRandomizer.updateFiles(random);
                FileUtils.logFlush("Updated item location data");

                if(Settings.isFoolsNpc()) {
                    // This must happen before shop data randomized in order to get the correct shop screen for little brother
                    npcRandomizer.updateNpcs();
                    FileUtils.logFlush("Updated NPCs");
                }

                boolean subweaponOnly = isSubweaponOnly();
                shopRandomizer.updateFiles(datInfo, subweaponOnly, moneyChecker, random);
                FileUtils.logFlush("Updated shop data");

                List<String> availableSubweapons = new ArrayList<>(ItemRandomizer.ALL_SUBWEAPONS);
                availableSubweapons.removeAll(Settings.getRemovedItems());
                availableSubweapons.removeAll(Settings.getCurrentRemovedItems());
                if(!availableSubweapons.isEmpty()) {
                    GameDataTracker.updateSubweaponPot(availableSubweapons.get(random.nextInt(availableSubweapons.size())));
                }
                FileUtils.logFlush("Updated subweapon pot data");
                if(Settings.isRandomizeEnemies()) {
                    GameDataTracker.randomizeEnemies(random);
                    FileUtils.logFlush("Updated enemy data");
                }
                if(Settings.isFools2021Mode()) {
                    GameDataTracker.updateEdenDaises(random);
                    FileUtils.logFlush("Updated puzzle flags");
                }

                if(Settings.isRandomizeSeals()) {
                    sealRandomizer.updateSeals();
                    FileUtils.logFlush("Updated seal data");
                }

                if(Settings.isRandomizeBacksideDoors()) {
                    backsideDoorRandomizer.updateBacksideDoors();
                    FileUtils.logFlush("Updated backside door data");
                }
                if(Settings.isRandomizeTransitionGates()) {
                    transitionGateRandomizer.updateTransitions();
                    FileUtils.logFlush("Updated transition gate data");
                }
                if(Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart() || Settings.isRandomizeStartingLocation() || Settings.isFools2020Mode()) {
                    GameDataTracker.updateXelpudIntro(datInfo);
                }
//                if(Settings.isRandomizeMantras()) {
//                    GameDataTracker.randomizeMantras(random);
//                }
                if(Settings.isHalloweenMode()) {
                    int shopBlockNumber = AddObject.addSecretShopBlock(datInfo).getBlockNumber();
                    int danceBlockNumber = AddObject.addDanceBlock(datInfo).getBlockNumber();
                    GameDataTracker.replaceNightSurfaceWithSurface(rcdData, danceBlockNumber, shopBlockNumber);
                    if(Settings.isIncludeHellTempleNPCs()) {
                        GameDataTracker.addHTSkip(rcdData, datInfo);
                    }
                    GameDataTracker.fixTransitionGates(rcdData);
                }
                else if(Settings.isFools2020Mode()) {
                    GameDataTracker.updateWorldForFools2020(rcdData, datInfo);
                }

                FileUtils.logFlush("Writing rcd file");
                RcdWriter.writeRcd(rcdData);

                FileUtils.log("rcd file successfully written");
                FileUtils.logFlush("Writing dat file");

                DatWriter.writeDat(datInfo);
                FileUtils.logFlush("dat file successfully written");
                FileUtils.logFlush("Writing msd file");

                if(Settings.isFools2021Mode()) {
                    FileUtils.writeMsd();
                }
                FileUtils.logFlush("msd file successfully written");
                if(Settings.isSaveFileNeeded()) {
                    backupSaves();
                    writeSaveFile();
                }

                if(Settings.isHalloweenMode()) {
                    if(!FileUtils.updateGraphicsFilesForHalloween(Settings.getGraphicsPack())) {
                        JOptionPane.showMessageDialog(f,
                                Translations.getText("Unable to create Halloween graphics"),
                                "Randomizer error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if(Settings.isFools2020Mode()) {
                    if(!FileUtils.updateGraphicsFilesForFools2020(Settings.getGraphicsPack())) {
                        JOptionPane.showMessageDialog(f,
                                Translations.getText("Unable to create Fools 2020 graphics"),
                                "Randomizer error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                FileUtils.updateGraphicsFiles(); // Always want to update graphics files, for backup Shrine door and possibly other things.

                FileUtils.logFlush("Copying settings file");
                File settingsFile = new File("randomizer-config.txt");
                if(settingsFile.exists()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            new File(String.format("%s/randomizer-config.txt", Settings.getStartingSeed())));
                    Files.copy(settingsFile.toPath(), fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }

                dialog.updateProgress(100, Translations.getText("progress.done"));
                GameDataTracker.clearAll();
                AddObject.clearObjects();
                DataFromFile.clearCustomPlacementData();

                SwingUtilities.invokeLater(() -> {
                    try {
                        Thread.sleep(2000);
                        dialog.setSafeClose(true);
                        dialog.setVisible(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                return;
            }
//                accessChecker.outputRemaining(Settings.getStartingSeed(), attempt);
        }
    }

    private static int getTotalItemsRemoved(Random random) {
        if(Settings.getMaxRandomRemovedItems() < 1) {
            return 0;
        }
        int totalItemsRemoved = Settings.getMinRandomRemovedItems();
        return totalItemsRemoved + random.nextInt(Settings.getMaxRandomRemovedItems() - totalItemsRemoved + 1);
    }

    private static boolean isSubweaponOnly() {
        if(DataFromFile.MAIN_WEAPONS.contains(Settings.getCurrentStartingWeapon())) {
            return false;
        }

        List<String> availableMainWeapons = new ArrayList<>(DataFromFile.MAIN_WEAPONS);
        availableMainWeapons.remove("Whip");
        availableMainWeapons.removeAll(Settings.getCurrentRemovedItems());
        availableMainWeapons.removeAll(Settings.getRemovedItems());
        return availableMainWeapons.isEmpty();
    }

    private static List<String> getStartingNodes() {
        List<String> startingNodes = new ArrayList<>();
        startingNodes.add(Settings.getCurrentStartingWeapon());
        startingNodes.add("State: Pre-Escape");
        if(ItemRandomizer.ALL_SUBWEAPONS.contains(Settings.getCurrentStartingWeapon())) {
            startingNodes.add(Settings.getCurrentStartingWeapon() + " Ammo");
            if("Pistol".equals(Settings.getCurrentStartingWeapon())) {
                startingNodes.add("Attack: Pistol"); // This one normally requires Isis' Pendant
            }
        }
        for(String startingItem : Settings.getStartingItemsIncludingCustom()) {
            startingNodes.add(startingItem);
        }
        for(String enabledGlitch : Settings.getEnabledGlitches()) {
            startingNodes.add("Setting: " + enabledGlitch);
        }
        for(String enabledDamageBoost : Settings.getEnabledDamageBoosts()) {
            startingNodes.add("Boost: " + enabledDamageBoost);
        }
        if(!Settings.isAutomaticHardmode()) {
            startingNodes.add("Mode: Normal");
        }
        if(!Settings.isRequireSoftwareComboForKeyFairy()) {
            startingNodes.add("Setting: No Combo Key Fairy");
        }
        if(!Settings.isRequireIceCapeForLava()) {
            startingNodes.add("Setting: Lava HP");
        }
        if(Settings.isAutomaticGrailPoints()) {
            startingNodes.add("Setting: Autoread Grail");
        }
        if(Settings.isAutomaticMantras()) {
            startingNodes.add("Setting: Skip Mantras");
        }
        if(Settings.isAutomaticTranslations()) {
            startingNodes.add("Setting: La-Mulanese");
        }
        if(Settings.isUshumgalluAssist()) {
            startingNodes.add("Setting: Ushumgallu Assist");
        }
        if(!Settings.isRequireFlaresForExtinction()) {
            startingNodes.add("Setting: Flareless Extinction");
        }
        if(!Settings.isBlockPushingRequiresGlove()) {
            startingNodes.add("Setting: Normal Pushing");
        }
        if(Settings.isFeatherlessMode()) {
            startingNodes.add("Setting: Featherless");
        }

        if(Settings.isFools2021Mode()) {
            startingNodes.add("Setting: Fools2021");
        } else {
            startingNodes.add("Setting: Not Fools2021");
        }

        startingNodes.add("Setting: " + Settings.getCurrentBossCount() + " Bosses");

        if(!Settings.getEnabledGlitches().contains("Raindrop")) {
            startingNodes.add("Setting: No Raindrop");
        }
        if(Settings.isRandomizeTransitionGates()) {
            startingNodes.add("Setting: Random Transitions");
        }
        else {
            startingNodes.add("Setting: Nonrandom Transitions");
        }
        if(!LocationCoordinateMapper.isSurfaceStart() && Settings.getCurrentStartingLocation() != 23 && Settings.getCurrentStartingLocation() != 24) {
            startingNodes.add("Setting: Alternate Start");
        }
        startingNodes.add(LocationCoordinateMapper.isFrontsideStart() ? "Setting: Frontside Start" : "Setting: Backside Start");
        startingNodes.add(Settings.isRandomizeBosses() ? "Setting: Abnormal Boss" : "Setting: Normal Boss");
        startingNodes.add(Settings.isAlternateMotherAnkh() ? "Setting: Alternate Mother" : "Setting: Standard Mother");
        startingNodes.add(Settings.isBossSpecificAnkhJewels() ? "Setting: Fixed Jewels" : "Setting: Variable Jewels");

        if(Settings.isSubweaponOnlyLogic() || isSubweaponOnly()) {
            startingNodes.add("Setting: Subweapon Only");
        }
        return startingNodes;
    }

    private static void backupSaves() {
        FileUtils.logFlush("Backing up save files");
        try {
            // Make lm_00.sav backup
            File existingSave = new File(String.format("%s/lm_00.sav", Settings.getLaMulanaSaveDir()));
            if(existingSave.exists()) {
                File firstBackupSave = new File(String.format("%s/lm_00.sav.lmr.orig.bak", Settings.getLaMulanaSaveDir()));
                if(!firstBackupSave.exists()) {
                    FileUtils.logFlush("Backing up first save file as lm_00.sav.lmr.orig.bak");
                    Files.copy(existingSave.toPath(), firstBackupSave.toPath());
                }
                File backupSave = new File(String.format("%s/lm_00.sav.lmr.bak", Settings.getLaMulanaSaveDir()));
                if(backupSave.exists()) {
                    FileUtils.logFlush("Deleting existing backup save");
                    Files.delete(backupSave.toPath());
                }
                Files.copy(existingSave.toPath(), backupSave.toPath());
                FileUtils.logFlush("Current backup save created");
            }
        }
        catch (Exception ex) {
            FileUtils.log("Unable to back up save file: " + ex.getMessage());
            FileUtils.logException(ex);
            throw new RuntimeException("Unable to back up save file. Please see logs for more information.");
        }
    }

    private static void writeSaveFile() {
        byte[] saveData = buildNewSave();
        String startingWeapon = Settings.getCurrentStartingWeapon();
        if(!"Whip".equals(startingWeapon)) {
            FileUtils.logFlush("Updating weapon data");
            saveData[4113] = (byte)-1; // word + 0x1011; remove whip
            saveData[4114] = (byte)-1; // word + 0x1011; remove whip

            if("Chain Whip".equals(startingWeapon)) {
                saveData[142] = (byte)2; // byte + 0x11; add chain whip flag
                saveData[4115] = (byte)1; // word + 0x1011; add chain whip
                saveData[4623] = (byte)1; // Held main weapon item number
                saveData[4626] = (byte)0; // Held main weapon slot number
            }
            else if("Flail Whip".equals(startingWeapon)) {
                saveData[143] = (byte)2; // byte + 0x11; add flail whip flag
                saveData[4117] = (byte)1; // word + 0x1011; add flail whip
                saveData[4623] = (byte)2; // Held main weapon item number
                saveData[4626] = (byte)0; // Held main weapon slot number
            }
            else if("Knife".equals(startingWeapon)) {
                saveData[144] = (byte)2; // byte + 0x11; add knife flag
                saveData[4119] = (byte)1; // word + 0x1011; add knife
                saveData[4623] = (byte)3; // Held main weapon item number
                saveData[4626] = (byte)1; // Held main weapon slot number
            }
            else if("Key Sword".equals(startingWeapon)) {
                saveData[145] = (byte)2; // byte + 0x11; add key sword flag
                saveData[4121] = (byte)1; // word + 0x1011; add key sword
                saveData[4623] = (byte)4; // Held main weapon item number
                saveData[4626] = (byte)2; // Held main weapon slot number

                if(Settings.isAutomaticMantras()) {
                    saveData[0x11 + 0x124] = (byte)4;
                }
            }
            else if("Axe".equals(startingWeapon)) {
                saveData[146] = (byte)2; // byte + 0x11; add axe flag
                saveData[4123] = (byte)1; // word + 0x1011; add axe
                saveData[4623] = (byte)5; // Held main weapon item number
                saveData[4626] = (byte)3; // Held main weapon slot number
            }
            else if("Katana".equals(startingWeapon)) {
                saveData[147] = (byte)2; // byte + 0x11; add katana flag
                saveData[4125] = (byte)1; // word + 0x1011; add katana
                saveData[4623] = (byte)6; // Held main weapon item number
                saveData[4626] = (byte)4; // Held main weapon slot number
            }
            else {
                saveData[4623] = (byte)-1; // Held main weapon item number
                saveData[4626] = (byte)-1; // Held main weapon slot number

                if("Shuriken".equals(startingWeapon)) {
                    saveData[148] = (byte)2;
                    saveData[4129] = (byte)1;
                    saveData[4624] = (byte)8; // Held subweapon item number
                    saveData[4627] = (byte)0; // Held subweapon slot number
                    saveData[0x06b * 2 + 0x1011 + 1] = (byte)150; // Ammo
                }
                else if("Rolling Shuriken".equals(startingWeapon)) {
                    saveData[149] = (byte)2;
                    saveData[4131] = (byte)1;
                    saveData[4624] = (byte)9; // Held subweapon item number
                    saveData[4627] = (byte)1; // Held subweapon slot number
                    saveData[0x06c * 2 + 0x1011 + 1] = (byte)100; // Ammo
                }
                else if("Earth Spear".equals(startingWeapon)) {
                    saveData[150] = (byte)2;
                    saveData[4133] = (byte)1;
                    saveData[4624] = (byte)10; // Held subweapon item number
                    saveData[4627] = (byte)2; // Held subweapon slot number
                    saveData[0x06d * 2 + 0x1011 + 1] = (byte)80; // Ammo
                }
                else if("Flare Gun".equals(startingWeapon)) {
                    saveData[151] = (byte)2;
                    saveData[4135] = (byte)1;
                    saveData[4624] = (byte)11; // Held subweapon item number
                    saveData[4627] = (byte)3; // Held subweapon slot number
                    saveData[0x06e * 2 + 0x1011 + 1] = (byte)80; // Ammo
                }
                else if("Bomb".equals(startingWeapon)) {
                    saveData[152] = (byte)2;
                    saveData[4137] = (byte)1;
                    saveData[4624] = (byte)12; // Held subweapon item number
                    saveData[4627] = (byte)4; // Held subweapon slot number
                    saveData[0x06f * 2 + 0x1011 + 1] = (byte)30; // Ammo
                }
                else if("Chakram".equals(startingWeapon)) {
                    saveData[153] = (byte)2;
                    saveData[4139] = (byte)1;
                    saveData[4624] = (byte)13; // Held subweapon item number
                    saveData[4627] = (byte)5; // Held subweapon slot number
                    saveData[0x070 * 2 + 0x1011 + 1] = (byte)10; // Ammo
                }
                else if("Caltrops".equals(startingWeapon)) {
                    saveData[154] = (byte)2;
                    saveData[4141] = (byte)1;
                    saveData[4624] = (byte)14; // Held subweapon item number
                    saveData[4627] = (byte)6; // Held subweapon slot number
                    saveData[0x071 * 2 + 0x1011 + 1] = (byte)80; // Ammo
                }
                else if("Pistol".equals(startingWeapon)) {
                    saveData[155] = (byte)2;
                    saveData[4143] = (byte)1;
                    saveData[4624] = (byte)15; // Held subweapon item number
                    saveData[4627] = (byte)7; // Held subweapon slot number
                    saveData[0x072 * 2 + 0x1011 + 1] = (byte)3; // Ammo
                    saveData[0x074 * 2 + 0x1011 + 1] = (byte)6; // Ammo
                }
            }
        }
        saveData[0x11 + 0xad1] = (byte)1;
        if(!LocationCoordinateMapper.isSurfaceStart()) {
            short grailFlag = LocationCoordinateMapper.getGrailFlag(LocationCoordinateMapper.getStartingZone(), LocationCoordinateMapper.isFrontsideStart());
            if(0x064 <= grailFlag && 0x075 >= grailFlag) {
                saveData[0x11 + grailFlag] = (byte)1; // Set starting grail flag if this is a traditional starting location, so your grail will be empowered.
            }
            saveData[0x069 * 2 + 0x1011 + 1] = (byte)10; // Start with 10 weights

            if(Settings.getCurrentStartingLocation() == 7) {
                // Twin labs front, auto-solve the elevator puzzle
                saveData[0x11 + 0x1db] = (byte)2;
            }
//            if(Settings.getCurrentStartingLocation() == 13) {
//                // Tower of the Goddess, turn on the lights.
//                saveData[0x11 + 0x271] = 3;
//            }
        }
        if(Settings.isHalloweenMode()) {
            // Unlock Mulbruk so you can get Halloween hints.
            saveData[0x11 + 0x079] = (byte)1;
            saveData[0x11 + 0x18e] = (byte)2;
            saveData[0x11 + 0x391] = (byte)1;

//            saveData[0x11 + 0x70e] = (byte)1; // room 20 floor
//            saveData[0x11 + 0x7d1] = (byte)1; // room 2
//            saveData[0x11 + 0x7d4] = (byte)1; // room 5
//            saveData[0x11 + 0x7d5] = (byte)1; // room 6
//            saveData[0x11 + 0x7d6] = (byte)1; // room 7
//            saveData[0x11 + 0x7d7] = (byte)2; // room 8
//            saveData[0x11 + 0x7d8] = (byte)1; // room 9
//            saveData[0x11 + 0x7d9] = (byte)1; // room 10
//            saveData[0x11 + 0x7da] = (byte)1; // room 11
//            saveData[0x11 + 0x7db] = (byte)1; // room 12
//            saveData[0x11 + 0x7dc] = (byte)1; // room 13
//            saveData[0x11 + 0x7de] = (byte)1; // room 15
//            saveData[0x11 + 0x7e0] = (byte)1; // room 17
//            saveData[0x11 + 0x7e1] = (byte)1; // room 18
//            saveData[0x11 + 0x7e2] = (byte)1; // room 19
//            saveData[0x11 + 0x7e5] = (byte)1; // room 22
//            saveData[0x11 + 0x7e6] = (byte)1; // room 20
//            saveData[0x11 + 0x7e7] = (byte)2; // room 24
//            saveData[0x11 + 0x7f7] = (byte)2; // Key fairy room solved
////            saveData[0x11 + 0x7e9] = (byte)1; // room 25
////            saveData[0x11 + 0x7ef] = (byte)1; // room 32
////            saveData[0x11 + 0x7f0] = (byte)1; // room 33
//            saveData[0x11 + 0x70c] = (byte)1; // room 34
        }
        if(Settings.isFeatherlessMode()) {
            // Ice block puzzle forced.
            saveData[0x11 + 0x243] = 3;
            saveData[0x11 + 0x244] = 3;
        }
        if(Settings.isFools2020Mode()) {
            // Unlock Mulbruk so you can have conversations about quitting the game
            saveData[0x11 + 0x079] = (byte)1;
            saveData[0x11 + 0x18e] = (byte)2;
            saveData[0x11 + 0x391] = (byte)1;

            // Default Extinction lighting
            saveData[0x11 + 0x1cd] = (byte)1;
        }
        if(Settings.isFools2021Mode()) {
            saveData[0x11 + 0x178] = (byte)5; // Ellmac ankh puzzle solved

            saveData[0x11 + 0x064] = (byte)1; // guidance
            saveData[0x11 + 0x065] = (byte)1; // maus
            saveData[0x11 + 0x066] = (byte)1; // sun
            saveData[0x11 + 0x067] = (byte)1; // spring
            saveData[0x11 + 0x068] = (byte)1; // inferno
            saveData[0x11 + 0x069] = (byte)1; // extinction
            saveData[0x11 + 0x06a] = (byte)1; // twin-f
            saveData[0x11 + 0x06b] = (byte)1; // endless
            saveData[0x11 + 0x06d] = (byte)1; // illusion
            saveData[0x11 + 0x06e] = (byte)1; // graveyard
            saveData[0x11 + 0x06f] = (byte)1; // moonlight
            saveData[0x11 + 0x070] = (byte)1; // goddess
            saveData[0x11 + 0x071] = (byte)1; // ruin
            saveData[0x11 + 0x072] = (byte)1; // birth
            saveData[0x11 + 0x073] = (byte)1; // twin-b
            saveData[0x11 + 0x074] = (byte)1; // dimensional

            saveData[0x11 + 0x1cd] = (byte)1; // Default Extinction lighting
        }
//        saveData[0x11 + 0x064] = 1;
//        saveData[0x11 + 0x065] = 1;
//        saveData[0x11 + 0x066] = 1;
//        saveData[0x11 + 0x067] = 1;
//        saveData[0x11 + 0x068] = 1;
//        saveData[0x11 + 0x069] = 1;
//        saveData[0x11 + 0x06a] = 1;
//        saveData[0x11 + 0x06b] = 1;
//        saveData[0x11 + 0x06c] = 1;
//        saveData[0x11 + 0x06d] = 1;
//        saveData[0x11 + 0x06e] = 1;
//        saveData[0x11 + 0x06f] = 1;
//        saveData[0x11 + 0x070] = 1;
//        saveData[0x11 + 0x071] = 1;
//        saveData[0x11 + 0x072] = 1;
//        saveData[0x11 + 0x073] = 1;
//        saveData[0x11 + 0x074] = 1;
//        saveData[0x11 + 0x075] = 1;

        FileUtils.logFlush("Writing save file");
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(
                    String.format("%s/lm_00.sav", Settings.getStartingSeed())));
            dataOutputStream.write(saveData);
            dataOutputStream.flush();
            dataOutputStream.close();
            FileUtils.logFlush("Save file created");
        }
        catch (Exception ex) {
            FileUtils.log("Problem modifying save file for randomized main weapons: " + ex.getMessage());
            FileUtils.logException(ex);
            throw new RuntimeException("Problem modifying save file for randomized main weapons. Please see logs for more information.");
        }
    }

    private static byte[] buildNewSave() {
        byte[] saveData = new byte[16384];
        Arrays.fill(saveData, (byte)0);
        saveData[0] = (byte)1;
        saveData[3] = (byte)0;
        saveData[4] = (byte)0;
        saveData[5] = LocationCoordinateMapper.getStartingZone();
        saveData[6] = LocationCoordinateMapper.getStartingRoom();
        saveData[7] = LocationCoordinateMapper.getStartingScreen();
        saveData[8] = (byte)(((LocationCoordinateMapper.getStartingX() % 640) >> 8) & 0xff);
        saveData[9] = (byte)((LocationCoordinateMapper.getStartingX() % 640) & 0xff);
        saveData[10] = (byte)(((LocationCoordinateMapper.getStartingY() % 480) >> 8) & 0xff);
        saveData[11] = (byte)((LocationCoordinateMapper.getStartingY() % 480) & 0xff);
        saveData[12] = (byte)1;
        saveData[13] = (byte)0;
        saveData[14] = (byte)32;
        saveData[841] = (byte)1;
        saveData[859] = (byte)1;
        saveData[4624] = (byte)-1;
        saveData[4625] = (byte)-1;
        saveData[4630] = (byte)46;

        int index = 4633;
        for(int i = 0; i < 46; i++) {
            for(int j = 0; j < 6; j++) {
                saveData[index++] = (byte) 0;
            }
            saveData[index++] = (byte) -1;
            saveData[index++] = (byte) -1;
        }
        index += 13;
        for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 13; j++) {
                saveData[index++] = (byte) 0;
            }
            saveData[index++] = (byte) -1;
        }
//        try {
//            saveData = FileUtils.getBytes(String.format("%s/lm_00.sav", Settings.getLaMulanaSaveDir()));
//        }
//        catch (Exception ex) {
//            return saveData;
//        }
        return saveData;
    }

    private static ShopRandomizer buildShopRandomizer(ItemRandomizer itemRandomizer) {
        ShopRandomizer shopRandomizer;
        if(ShopRandomizationEnum.CATEGORIZED.equals(Settings.getShopRandomization())) {
            shopRandomizer = new CategorizedShopRandomizer();
        }
        else {
            shopRandomizer = new EverythingShopRandomizer();
        }

        itemRandomizer.setShopRandomizer(shopRandomizer);
        shopRandomizer.setItemRandomizer(itemRandomizer);
        return shopRandomizer;
    }

    private static AccessChecker buildAccessChecker(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer,
                                                    BacksideDoorRandomizer backsideDoorRandomizer,
                                                    TransitionGateRandomizer transitionGateRandomizer,
                                                    SealRandomizer sealRandomizer, NpcRandomizer npcRandomizer) {
        AccessChecker accessChecker = new AccessChecker();
        accessChecker.setItemRandomizer(itemRandomizer);
        accessChecker.setShopRandomizer(shopRandomizer);
        accessChecker.setBacksideDoorRandomizer(backsideDoorRandomizer);
        accessChecker.setTransitionGateRandomizer(transitionGateRandomizer);
        accessChecker.setSealRandomizer(sealRandomizer);
        accessChecker.setNpcRandomizer(npcRandomizer);
        itemRandomizer.setAccessChecker(accessChecker);
        shopRandomizer.setAccessChecker(accessChecker);
        return accessChecker;
    }

    private static Set<String> getInitiallyAvailableItems() {
        Set<String> noRequirementItems = new HashSet<>(Settings.getInitiallyAccessibleItems());
        noRequirementItems.removeAll(DataFromFile.getNonRandomizedItems());
        return noRequirementItems;
    }

    private static void determineStartingWeapon(Random random) {
        CustomPlacementData customPlacementData = DataFromFile.getCustomPlacementData();
        String customStartingWeapon = customPlacementData.getStartingWeapon();
        if(customStartingWeapon != null) {
            Settings.setCurrentStartingWeapon(customStartingWeapon);
            FileUtils.logFlush("Selected custom starting weapon: " + customStartingWeapon);
            return;
        }

        List<String> startingWeapons = new ArrayList<>();
        if(Settings.isAllowWhipStart()) {
            startingWeapons.add("Whip");
        }
        if(Settings.isAllowMainWeaponStart()) {
            startingWeapons.add("Knife");
            if(!Settings.isFools2021Mode()) {
                startingWeapons.add("Key Sword");
            }
            startingWeapons.add("Axe");
            startingWeapons.add("Katana");
        }
        if(Settings.isAllowSubweaponStart()) {
            startingWeapons.addAll(ItemRandomizer.ALL_SUBWEAPONS);
        }

        startingWeapons.removeAll(customPlacementData.getRemovedItems());
        startingWeapons.removeAll(customPlacementData.getStartingItems());
        startingWeapons.removeAll(Settings.getRemovedItems());
        Settings.setCurrentStartingWeapon(startingWeapons.get(random.nextInt(startingWeapons.size())));
        FileUtils.logFlush("Selected starting weapon: " + Settings.getCurrentStartingWeapon());
    }

    private static void determineStartingLocation(Random random) {
        if(!Settings.isRandomizeStartingLocation()) {
            Settings.setCurrentStartingLocation(1);
            FileUtils.logFlush("Selected starting location: " + LocationCoordinateMapper.getStartingZoneName(1));
            return;
        }

        CustomPlacementData customPlacementData = DataFromFile.getCustomPlacementData();
        Integer customStartingLocation = customPlacementData.getStartingLocation();
        if(customStartingLocation != null) {
            Settings.setCurrentStartingLocation(customStartingLocation);
            FileUtils.logFlush("Selected custom starting location: " + LocationCoordinateMapper.getStartingZoneName(customStartingLocation));
            return;
        }

        if(Settings.isHalloweenMode()) {
//            Settings.setCurrentStartingLocation(24);
//            Settings.setCurrentStartingLocation(23);
//            Settings.setCurrentStartingLocation(0);
            Settings.setCurrentStartingLocation(22);
        }
        else {
            List<Integer> possibleStartingLocations = new ArrayList<>(DataFromFile.STARTING_LOCATIONS);
            if(!Settings.getStartingItemsIncludingCustom().contains("Holy Grail")) {
                // Tower of Ruin will be unable to get back to the grail tablet easily/will have very limited options without grail/feather/boots/ice cape, so just ban it.
                possibleStartingLocations.remove((Integer)14);
            }
            if(!Settings.isRandomizeTransitionGates()) {
                // Most backside fields aren't an option unless random transitions help keep you from getting stuck on one side of the ruins.
                possibleStartingLocations.remove((Integer)11);
                possibleStartingLocations.remove((Integer)13);
                possibleStartingLocations.remove((Integer)14);
                possibleStartingLocations.remove((Integer)16);
                possibleStartingLocations.remove((Integer)21);
            }
            Settings.setCurrentStartingLocation(possibleStartingLocations.get(random.nextInt(possibleStartingLocations.size())));
            FileUtils.logFlush("Selected starting location: " + LocationCoordinateMapper.getStartingZoneName(Settings.getCurrentStartingLocation()));
        }
    }

    private static void determineFoolsGameplay(Random random) {
        if(!Settings.isFoolsGameplay()) {
            Settings.setCurrentBossCount(8);
            FileUtils.logFlush("Using default boss count: 8");
            return;
        }

        // todo: alt mother ankh??
//        int ankhJewelsRemoved = 0;
//        for (String item : DataFromFile.getCustomPlacementData().getRemovedItems()) {
//            if(item.contains("Ankh Jewel")) {
//                ankhJewelsRemoved += 1;
//            }
//        }

//        Settings.setCurrentBossCount(random.nextInt((Settings.isFools2021Mode() ? 4 : 5) - ankhJewelsRemoved) + 4); // todo: how do ankh jewels removed factor in?
        Settings.setCurrentBossCount(7);
        FileUtils.logFlush("Using random boss count: " + Settings.getCurrentBossCount());
    }

    private static void determineGiant(Random random) {
        if(Settings.isFools2021Mode()) {
            List<String> giants = Arrays.asList("Zebu", "Bado", "Migela", "Ledo", "Abuto", "Ji", "Ribu", "Sakit"); // not Futo
            Settings.setCurrentGiant(giants.get(random.nextInt(giants.size())));
        }
    }

    private static void determineRemovedItems(int totalItemsRemoved, Random random) {
        Set<String> removedItems = new HashSet<>(Settings.getRemovedItems());
        if(!"Whip".equals(Settings.getCurrentStartingWeapon())) {
            removedItems.add("Whip");
        }
        if(totalItemsRemoved < 1) {
            Settings.setCurrentRemovedItems(removedItems);
            return;
        }
        List<String> removableItems = new ArrayList<>(DataFromFile.getRandomRemovableItems());
        removableItems.removeAll(removedItems);
        removableItems.remove(Settings.getCurrentStartingWeapon());
        if(removableItems.isEmpty()) {
            Settings.setCurrentRemovedItems(removedItems);
            return;
        }
        int removedAnkhJewels = 0;
        for(String item : removedItems) {
            if(item.startsWith("Ankh Jewel (")) {
                removedAnkhJewels += 1;
            }
        }
        if(removedAnkhJewels >= (8 - Settings.getCurrentBossCount())) {
            filterAnkhJewels(removableItems);
        }

        boolean objectZipEnabled = Settings.getEnabledGlitches().contains("Object Zip");
        boolean catPauseEnabled = Settings.getEnabledGlitches().contains("Cat Pause");
        boolean lampGlitchEnabled = Settings.getEnabledGlitches().contains("Lamp Glitch");
        boolean requireKeyFairyCombo = Settings.isRequireSoftwareComboForKeyFairy();
        boolean lamulanaMantraRequired = !lampGlitchEnabled && !Settings.isAutomaticMantras() && !Settings.isAlternateMotherAnkh();
        boolean orbRemoved = false;

        boolean easierBosses = BossDifficulty.MEDIUM.equals(Settings.getBossDifficulty());
        List<String> easierSubweaponsForPalenque = easierBosses ? new ArrayList<>(Arrays.asList("Rolling Shuriken", "Chakram", "Earth Spear", "Pistol")) : new ArrayList<>(0);
        List<String> easierWeapons = easierBosses ? new ArrayList<>(Arrays.asList("Chain Whip", "Flail Whip", "Axe")) : new ArrayList<>(0);
        List<String> mainWeapons = new ArrayList<>(DataFromFile.MAIN_WEAPONS);
        mainWeapons.remove("Whip");
        List<String> subweaponOnlyItems = new ArrayList<>(Arrays.asList("Caltrops", "Flare Gun", "Bomb", "Feather", "Ring"));
        List<String> itemsRequiredForBosses = new ArrayList<>(Arrays.asList("Helmet", "Pochette Key"));
        if(Settings.isRequireIceCapeForLava()) {
            itemsRequiredForBosses.add("Ice Cape"); // Viy
        }
        if(!lampGlitchEnabled) {
            itemsRequiredForBosses.add("Bronze Mirror"); // Viy
        }

        boolean subweaponOnly = isSubweaponOnly(); // Preliminary check based on custom placements; currently this cannot happen randomly.
        if(isSubweaponOnly()) {
            removableItems.removeAll(subweaponOnlyItems);
        }

        for(String removedItem : removedItems) {
            orbRemoved = filterRemovableItems(removedItem, removableItems, objectZipEnabled, easierBosses, lampGlitchEnabled, requireKeyFairyCombo, catPauseEnabled, lamulanaMantraRequired, subweaponOnly, orbRemoved, mainWeapons, easierWeapons, subweaponOnlyItems, easierSubweaponsForPalenque, itemsRequiredForBosses);
        }

        if(removableItems.isEmpty()) {
            Settings.setCurrentRemovedItems(removedItems);
            return;
        }

        int chosenRemovedItems = 0;
        while(chosenRemovedItems < totalItemsRemoved && !removableItems.isEmpty()) {
            int removedItemIndex = random.nextInt(removableItems.size());
            String removedItem = removableItems.get(removedItemIndex);
            if(!removedItems.contains(removedItem)) {
                removedItems.add(removedItem);
                removableItems.remove(removedItem);
                orbRemoved = filterRemovableItems(removedItem, removableItems, objectZipEnabled, easierBosses, lampGlitchEnabled, requireKeyFairyCombo, catPauseEnabled, lamulanaMantraRequired, subweaponOnly, orbRemoved, mainWeapons, easierWeapons, subweaponOnlyItems, easierSubweaponsForPalenque, itemsRequiredForBosses);
                if(removedItem.startsWith("Ankh Jewel (")) {
                    removedAnkhJewels += 1;
                    if (removedAnkhJewels >= 8 - Settings.getCurrentBossCount()) {
                        filterAnkhJewels(removableItems);
                    }
                }
                ++chosenRemovedItems;
            }
        }
        Settings.setCurrentRemovedItems(removedItems);
    }

    private static void filterAnkhJewels(List<String> removableItems) {
        removableItems.remove("Ankh Jewel (Gate of Guidance)");
        removableItems.remove("Ankh Jewel (Mausoleum of the Giants)");
        removableItems.remove("Ankh Jewel (Temple of the Sun)");
        removableItems.remove("Ankh Jewel (Spring in the Sky)");
        removableItems.remove("Ankh Jewel (Tower of Ruin)");
        removableItems.remove("Ankh Jewel (Chamber of Birth)");
        removableItems.remove("Ankh Jewel (Twin Labyrinths)");
        removableItems.remove("Ankh Jewel (Dimensional Corridor)");
        removableItems.remove("Ankh Jewel (Extra)");
    }

    private static boolean filterRemovableItems(String removedItem, List<String> removableItems, boolean objectZipEnabled, boolean easierBosses, boolean lampGlitchEnabled, boolean requireKeyFairyCombo, boolean catPauseEnabled, boolean lamulanaMantraRequired, boolean subweaponOnly, boolean orbRemoved, List<String> mainWeapons, List<String> easierWeapons, List<String> subweaponOnlyItems, List<String> easierSubweaponsForPalenque, List<String> itemsRequiredForBosses) {
        if("Twin Statue".equals(removedItem)) {
            // Raindropping needed to reach Dimensional Corridor
            removableItems.remove("Hermes' Boots");
            removableItems.remove("Grapple Claw");
        }
        else if("Hand Scanner".equals(removedItem) || "reader.exe".equals(removedItem)) {
            // Raindropping needed to enter Shrine of the Mother
            removableItems.remove("Hermes' Boots");
            removableItems.remove("Grapple Claw");
        }
        else if("Fruit of Eden".equals(removedItem) && lamulanaMantraRequired) {
            // Raindropping needed to recite LAMULANA
            removableItems.remove("Hermes' Boots");
            removableItems.remove("Grapple Claw");
        }
        else if("Chakram".equals(removedItem)) {
            // Must be able to raindrop unless alternative glitches are a thing.
            if(!catPauseEnabled) {
                removableItems.remove("Hermes' Boots");
                if(!objectZipEnabled) {
                    removableItems.remove("Grapple Claw");
                }
            }

            if(easierBosses) {
                easierSubweaponsForPalenque.remove(removedItem);
                if(easierSubweaponsForPalenque.size() == 1) {
                    removableItems.remove(easierSubweaponsForPalenque.get(0));
                }
            }

            subweaponOnlyItems.remove(removedItem); // Chakram is considered required for subweapon-only.
            if(mainWeapons.size() == 1) {
                removableItems.remove(mainWeapons.get(0));
            }
        }
        else if("Serpent Staff".equals(removedItem)) {
            // Must be able to raindrop unless alternative glitches are a thing.
            if(!catPauseEnabled) {
                removableItems.remove("Hermes' Boots");
                if(!objectZipEnabled) {
                    removableItems.remove("Grapple Claw");
                }
            }
        }
        else if("Hermes' Boots".equals(removedItem)) {
            // Don't remove anything that outright requires raindropping.
            removableItems.remove("Twin Statue");
            removableItems.remove("Plane Model");
            removableItems.remove("Hand Scanner");
            removableItems.remove("reader.exe");
            if(lamulanaMantraRequired) {
                removableItems.remove("Fruit of Eden");
            }
            if(!catPauseEnabled) {
                removableItems.remove("Chakram");
                removableItems.remove("Serpent Staff");
            }
            if(!lampGlitchEnabled) {
                removableItems.remove("Bronze Mirror");
            }
        }
        else if("Grapple Claw".equals(removedItem)) {
            // Don't remove anything that outright requires raindropping.
            removableItems.remove("Twin Statue");
            removableItems.remove("Plane Model");
            if(!objectZipEnabled && !catPauseEnabled) {
                removableItems.remove("Chakram");
                removableItems.remove("Serpent Staff");
            }
            if(!lampGlitchEnabled) {
                removableItems.remove("Bronze Mirror");
            }
        }
        else if("Plane Model".equals(removedItem)) {
            // Don't remove alternative means of getting into Chamber of Birth and the Medicine of the Mind area.
            removableItems.remove("Hermes' Boots");
            removableItems.remove("Grapple Claw");
            removableItems.remove("Bronze Mirror");
            if(requireKeyFairyCombo) {
                removableItems.remove("miracle.exe");
                removableItems.remove("mekuri.exe");
            }
        }
        else if("Bronze Mirror".equals(removedItem)) {
            removableItems.remove("Plane Model");
            if(!lampGlitchEnabled) {
                removableItems.remove("Hermes' Boots");
                removableItems.remove("Grapple Claw");
                if(itemsRequiredForBosses.contains("Bronze Mirror")) {
                    for(String item : itemsRequiredForBosses) {
                        if(!"Ice Cape".equals(item)) {
                            itemsRequiredForBosses.remove(item);
                            removableItems.remove(item);
                        }
                    }
                    if(itemsRequiredForBosses.contains("Ice Cape")) {
                        itemsRequiredForBosses.clear();
                        itemsRequiredForBosses.add("Ice Cape");
                    }
                    else {
                        itemsRequiredForBosses.add("Ice Cape");
                    }
                }
            }
        }
        else if("Ring".equals(removedItem)) {
            subweaponOnlyItems.remove(removedItem);
            if(Settings.isSubweaponOnlyLogic() && mainWeapons.size() == 1) {
                // Can't kill Tiamat, main weapon is now required.
                removableItems.remove(mainWeapons.get(0));
            }
        }
        else if("Ice Cape".equals(removedItem)) {
            if(itemsRequiredForBosses.contains("Ice Cape")) {
                for(String item : itemsRequiredForBosses) {
                    if(!"Bronze Mirror".equals(item)) {
                        removableItems.remove(item);
                    }
                }

                if(itemsRequiredForBosses.contains("Bronze Mirror")) {
                    itemsRequiredForBosses.clear();
                    itemsRequiredForBosses.add("Bronze Mirror");
                }
                else {
                    itemsRequiredForBosses.add("Bronze Mirror");
                }
            }
        }
        else if("Helmet".equals(removedItem)) {
            // todo: deal with boss swap
            for(String item : itemsRequiredForBosses) {
                removableItems.remove(item);
            }
            itemsRequiredForBosses.clear();
        }
        else if("Pochette Key".equals(removedItem)) {
            for(String item : itemsRequiredForBosses) {
                removableItems.remove(item);
            }
            itemsRequiredForBosses.clear();
        }
        else if(DataFromFile.MAIN_WEAPONS.contains(removedItem)) {
            mainWeapons.remove(removedItem);
            if(Settings.isSubweaponOnlyLogic()) {
                if(mainWeapons.isEmpty()) {
                    removableItems.removeAll(subweaponOnlyItems);
                }
                else if(mainWeapons.size() == 1 && subweaponOnlyItems.size() < 5) {
                    // One of the subweapon-only items has been removed, so a main weapon is required.
                    removableItems.remove(mainWeapons.get(0));
                }
            }
            else {
                if(mainWeapons.size() == 1) {
                    removableItems.remove(mainWeapons.get(0));
                }
            }

            if(easierBosses) {
                easierWeapons.remove(removedItem);
                if(easierWeapons.size() == 1) {
                    removableItems.remove(easierWeapons.get(0));
                }
            }
        }
        else if(ItemRandomizer.ALL_SUBWEAPONS.contains(removedItem)) {
            subweaponOnlyItems.remove(removedItem);
            if(Settings.isSubweaponOnlyLogic()) {
                if(mainWeapons.size() == 1 && subweaponOnlyItems.size() < 5) {
                    // One of the subweapon-only items has been removed, so a main weapon is required.
                    removableItems.remove(mainWeapons.get(0));
                }
            }

            if(easierBosses) {
                easierWeapons.remove(removedItem);
                if(easierWeapons.size() == 1) {
                    removableItems.remove(easierWeapons.get(0));
                }

                easierSubweaponsForPalenque.remove(removedItem);
                if(easierSubweaponsForPalenque.size() == 1) {
                    removableItems.remove(easierSubweaponsForPalenque.get(0));
                }
            }
        }
        else if(easierBosses) {
            if("Silver Shield".equals(removedItem)) {
                removableItems.remove("Angel Shield");
            }
            else if ("Angel Shield".equals(removedItem)) {
                removableItems.remove("Silver Shield");
            }
            else if(removedItem.startsWith("Sacred Orb")) {
                if(orbRemoved) {
                    removableItems.remove("Sacred Orb (Surface)");
                    removableItems.remove("Sacred Orb (Gate of Guidance)");
                    removableItems.remove("Sacred Orb (Mausoleum of the Giants)");
                    removableItems.remove("Sacred Orb (Temple of the Sun)");
                    removableItems.remove("Sacred Orb (Spring in the Sky)");
                    removableItems.remove("Sacred Orb (Chamber of Extinction)");
                    removableItems.remove("Sacred Orb (Twin Labyrinths)");
                    removableItems.remove("Sacred Orb (Shrine of the Mother)");
                    removableItems.remove("Sacred Orb (Tower of Ruin)");
                    removableItems.remove("Sacred Orb (Chamber of Extinction)");
                }
                else {
                    orbRemoved = true;
                }
            }
        }
        return orbRemoved;
    }

    private static void outputLocations(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer,
                                        BacksideDoorRandomizer backsideDoorRandomizer,
                                        TransitionGateRandomizer transitionGateRandomizer, int attempt) throws IOException {
        itemRandomizer.outputLocations(attempt);
        shopRandomizer.outputLocations(attempt);
        backsideDoorRandomizer.outputLocations(attempt);
        transitionGateRandomizer.outputLocations(attempt);
        if (!Settings.getCurrentRemovedItems().isEmpty() || !Settings.getRemovedItems().isEmpty()) {
            BufferedWriter writer = FileUtils.getFileWriter(String.format("%s/removed_items.txt", Settings.getStartingSeed()));
            if (writer == null) {
                return;
            }

            Set<String> allRemovedItems = new HashSet<>(Settings.getCurrentRemovedItems());
            for(String removedItem : allRemovedItems) {
                writer.write(Translations.getItemText(removedItem, false));
                writer.newLine();
            }
            writer.flush();
            writer.close();
        }
    }

    public static void addArgItemUI(Set<String> nonRandomizedItems, String input) {
        for(String item : DataFromFile.getAllItems()) {
            if(item.equals(input)) {
                nonRandomizedItems.add(item);
            }
        }
    }
}
