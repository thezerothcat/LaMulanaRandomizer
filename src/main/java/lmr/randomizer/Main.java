package lmr.randomizer;

import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.DatReader;
import lmr.randomizer.dat.DatWriter;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.node.CustomPlacement;
import lmr.randomizer.random.*;
import lmr.randomizer.rcd.RcdReader;
import lmr.randomizer.rcd.RcdWriter;
import lmr.randomizer.rcd.object.Zone;
import lmr.randomizer.ui.ButtonPanel;
import lmr.randomizer.ui.MainPanel;
import lmr.randomizer.ui.ProgressDialog;
import lmr.randomizer.ui.TabbedPanel;
import lmr.randomizer.update.GameDataTracker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

/**
 * Created by thezerothcat on 7/9/2017.
 */
public class Main {
    public static void main(String[] args) {
        try {
            FileUtils.readSettings();
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
            setTitle(Translations.getText("title"));
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
            progressDialog.updateProgress(0, Translations.getText("progress.generating"));

            buttonPanel = new ButtonPanel(this);
            add(buttonPanel, "grow");
            pack();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mainPanel.updateSettings();
            tabbedPanel.updateSettings();
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
            else if("restore".equals(e.getActionCommand())) {
                if(!validateInstallDir()) {
                    JOptionPane.showMessageDialog(this,
                            "Unable to find La-Mulana install directory",
                            "Randomizer error", JOptionPane.ERROR_MESSAGE);
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
                if(!validateSaveDir()) {
                    JOptionPane.showMessageDialog(this,
                            "Unable to find La-Mulana install directory",
                            "Randomizer error", JOptionPane.ERROR_MESSAGE);
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
                return false;
            }

            progressDialog.updateProgress(0, Translations.getText("progress.generating"));

            Settings.saveSettings();

            DataFromFile.clearAllData();

            progressDialog.updateProgress(10, Translations.getText("setup.backup"));

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
                            doTheThing(progressDialog);
                            mainPanel.rerollRandomSeed();
                        }
                        catch (Exception ex) {
                            FileUtils.logException(ex);
                            JOptionPane.showMessageDialog(f,
                                    "Unknown error. Please see logs for more information.",
                                    "Randomizer error", JOptionPane.ERROR_MESSAGE);
                            SwingUtilities.invokeLater(() -> {
                                progressDialog.setVisible(false);
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

        private void generateAndApply() {
            try {
                progressDialog.updateProgress(0, Translations.getText("setup.start"));
                if(!generateSeed()) {
                    return;
                }

                FileOutputStream fileOutputStream = new FileOutputStream(new File(Settings.getLaMulanaBaseDir() + "/data/mapdata/script.rcd"));
                Files.copy(new File(String.format("%s/script.rcd", Settings.getStartingSeed())).toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                fileOutputStream = new FileOutputStream(new File(String.format("%s/data/language/%s/script_code.dat",
                        Settings.getLaMulanaBaseDir(), Settings.getLanguage())));
                Files.copy(new File(String.format("%s/script_code.dat", Settings.getStartingSeed())).toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                FileUtils.closeAll();
            }
            catch (Exception ex) {
                FileUtils.log("Unable to copy files to La-Mulana install:" + ex.getMessage());
                FileUtils.logException(ex);
                throw new RuntimeException("Unable to back up script.rcd. Please see logs for more information.");
            }
        }

        private void restore() {
            try {
                progressDialog.updateProgress(0, Translations.getText("restore.rcd"));

                FileOutputStream fileOutputStream = new FileOutputStream(new File(Settings.getLaMulanaBaseDir() + "/data/mapdata/script.rcd"));
                Files.copy(new File("script.rcd.bak").toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                progressDialog.updateProgress(50, Translations.getText("restore.dat"));

                fileOutputStream = new FileOutputStream(new File(String.format("%s/data/language/%s/script_code.dat",
                        Settings.getLaMulanaBaseDir(), Settings.getLanguage())));
                Files.copy(new File(Settings.getBackupDatFile()).toPath(), fileOutputStream);
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
            if(!validateInstallDir()) {
                JOptionPane.showMessageDialog(this,
                        "Unable to find La-Mulana install directory",
                        "Randomizer error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(Settings.isRandomizeMainWeapon() && !validateSaveDir()) {
                JOptionPane.showMessageDialog(this,
                        "Unable to find La-Mulana save directory",
                        "Randomizer error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(!validateCustomPlacements(this)) {
                // Message created below
                DataFromFile.clearCustomItemPlacements();
                return false;
            }
            return true;
        }

        private boolean validateCustomPlacements(RandomizerUI randomizerUI) {
            List<CustomPlacement> customPlacements = DataFromFile.getCustomItemPlacements();
            if(customPlacements.isEmpty()) {
                return true;
            }

            List<String> locations = new ArrayList<>();
            List<String> items = new ArrayList<>();
            List<String> removed = new ArrayList<>();
            for(CustomPlacement customPlacement : customPlacements) {
                if(customPlacement.isRemoveItem()) {
                    if(removed.contains(customPlacement.getContents())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Duplicate removed item " + customPlacement.getContents(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(customPlacement.getContents().startsWith("Coin:")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Coin chests cannot be removed items",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(customPlacement.getContents().startsWith("Trap:")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Traps cannot be removed items",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(!DataFromFile.getRandomRemovableItems().contains(customPlacement.getContents())
                            && !"Whip".equals(customPlacement.getContents())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                customPlacement.getContents() + " cannot be a removed item",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    removed.add(customPlacement.getContents());
                }
                else if(customPlacement.isCurseChest()) {
                    if(!Settings.isRandomizeCursedChests()) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement of cursed chest not valid with current settings for cursed chest randomization",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                    }
                }
                else {
                    if(locations.contains(customPlacement.getLocation())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Location used for multiple items: " + customPlacement.getLocation(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(items.contains(customPlacement.getContents())
                            && !"Weights".equals(customPlacement.getContents())
                            && !customPlacement.getContents().endsWith(" Ammo")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Item placed in multiple locations: " + customPlacement.getContents(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(!isValidLocation(customPlacement.getLocation())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Location not valid: " + customPlacement.getLocation() + " for item " + customPlacement.getContents(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(!isValidContents(customPlacement.getContents())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Placed item not valid: " + customPlacement.getContents() + " at location " + customPlacement.getLocation(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(DataFromFile.SHOP_ITEMS.contains(customPlacement.getLocation())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "To place " + customPlacement.getLocation() + " in a shop, please use the shop name and item number instead of the item name",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(!Settings.isRandomizeTrapItems()) {
                        if(customPlacement.getLocation().startsWith("Trap:")) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement at location " + customPlacement.getLocation() + " not valid with current settings for randomized traps",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if(customPlacement.getContents().startsWith("Trap:")) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement of item " + customPlacement.getContents() + " not valid with current settings for randomized traps",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                    if(!Settings.isRandomizeCoinChests()) {
                        if(customPlacement.getLocation().startsWith("Coin:")) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement at location " + customPlacement.getLocation() + " not valid with current settings for randomized coin chests",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if(customPlacement.getContents().startsWith("Coin:")) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement of item " + customPlacement.getContents() + " not valid with current settings for randomized coin chests",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                    if(!Settings.isRandomizeForbiddenTreasure()) {
                        if(customPlacement.getLocation().equals("Provocative Bathing Suit")
                                || customPlacement.getContents().equals("Provocative Bathing Suit")) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement not valid with current settings for Provocative Bathing Suit randomization",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                    else if(!Settings.isHTFullRandom()) {
                        if(customPlacement.getLocation().equals("Provocative Bathing Suit")
                                && !customPlacement.getContents().startsWith("Map (")) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement not valid with current settings for Provocative Bathing Suit randomization",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }

                    if(!Settings.isRandomizeDracuetShop() && customPlacement.getLocation().startsWith("Shop 23 (HT)")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom placement not valid with current settings for Dracuet shop randomization",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    if(ShopRandomizationEnum.CATEGORIZED.equals(Settings.getShopRandomization())
                            && customPlacement.getLocation().startsWith("Shop ")) {
                        if(!DataFromFile.CATEGORIZED_SHOP_ITEM_LOCATIONS.contains(customPlacement.getLocation())) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement of item at " + customPlacement.getLocation() + " not valid with current settings for shop randomization",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        else if("Weights".equals(customPlacement.getContents())
                                || customPlacement.getContents().endsWith(" Ammo")) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement of " + customPlacement.getContents() + " not valid with current settings for shop randomization",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }

                    if(Settings.getInitiallyAccessibleItems().contains(customPlacement.getContents())
                            && !DataFromFile.getInitialNonShopItemLocations().contains(customPlacement.getLocation())
                            && !DataFromFile.getInitialCoinChestLocations().contains(customPlacement.getLocation())
                            && !DataFromFile.getInitialTrapItemLocations().contains(customPlacement.getLocation())) {
                        boolean initialShop = false;
                        for(String shopName : DataFromFile.getInitialShops()) {
                            if(customPlacement.getLocation().startsWith(shopName)) {
                                initialShop = true;
                                break;
                            }
                        }
                        if(!initialShop) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement of " + customPlacement.getContents() + " not valid with current settings for initially accessible item",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }

                    locations.add(customPlacement.getLocation());
                    items.add(customPlacement.getContents());
                }

                if(Settings.getStartingItems().contains(customPlacement.getContents())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement of " + customPlacement.getContents() + " not valid with current settings for starting item",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            return true;
        }

        private boolean validateInstallDir() {
            return Settings.getLaMulanaBaseDir() != null && !Settings.getLaMulanaBaseDir().isEmpty()
                    && new File(Settings.getLaMulanaBaseDir()).exists();
        }

        private boolean validateSaveDir() {
            return Settings.getLaMulanaSaveDir() != null && !Settings.getLaMulanaSaveDir().isEmpty()
                    && new File(Settings.getLaMulanaSaveDir()).exists();
        }

        private boolean isValidLocation(String location) {
            if(DataFromFile.getAllItems().contains(location)) {
                return true;
            }
            if(DataFromFile.getAllCoinChests().contains(location)) {
                return true;
            }
            if(location.equals("Trap: Graveyard") || location.equals("Trap: Exploding")
                    || location.equals("Trap: Inferno Orb") || location.equals("Trap: Twin Ankh")) {
                return true;
            }
            if(location.startsWith("Shop ")
                    && (location.endsWith(" Item 1") || location.endsWith(" Item 2") || location.endsWith(" Item 3"))) {
                for(String shopName : DataFromFile.getAllShops()) {
                    if(location.startsWith(shopName)) {
                        return !location.startsWith("Shop 2 Alt (Surface)") || location.endsWith("1");
                    }
                }
            }
            return false;
        }

        private boolean isValidContents(String contents) {
            if(DataFromFile.getAllItems().contains(contents)) {
                return true;
            }
            if(DataFromFile.getAllCoinChests().contains(contents)) {
                return true;
            }
            if(contents.equals("Trap: Graveyard") || contents.equals("Trap: Exploding")
                    || contents.equals("Trap: Inferno Orb") || contents.equals("Trap: Twin Ankh")) {
                return true;
            }
            if(contents.equals("Weights") || contents.equals("Shuriken Ammo") || contents.equals("Rolling Shuriken Ammo")
                    || contents.equals("Caltrops Ammo") || contents.equals("Chakram Ammo") || contents.equals("Flare Gun Ammo")
                    || contents.equals("Earth Spear Ammo") || contents.equals("Pistol Ammo")) {
                return true;
            }
            return false;
        }
    }

    protected static void doTheThing(ProgressDialog dialog) throws Exception {
        FileUtils.log(String.format("Shuffling items for seed %s", Settings.getStartingSeed()));

        Random random = new Random(Settings.getStartingSeed());
        Set<String> initiallyAccessibleItems = getInitiallyAvailableItems();

        int attempt = 0;
        while(true) {
            ++attempt;
            if(Settings.isRandomizeMainWeapon()) {
                determineMainWeapon(random);
            }
            if(Settings.getMaxRandomRemovedItems() < 1) {
                if(Settings.isRandomizeMainWeapon() && !"Whip".equals(Settings.getCurrentStartingWeapon())) {
                    Settings.setCurrentRemovedItems(new HashSet<>(Arrays.asList("Whip")));
                }
                else {
                    Settings.setCurrentRemovedItems(new HashSet<>(0));
                }
            }
            else {
                dialog.updateProgress(20, Translations.getText("progress.shuffling.removing"));
                while(true) {
                    determineRemovedItems(random);
                    if(BossDifficulty.MEDIUM.equals(Settings.getBossDifficulty())) {
                        // Some optimizations to save larger computations when boss requirements will definitely fail the seed.
                        if(Settings.getCurrentRemovedItems().contains("Chain Whip") && Settings.getCurrentRemovedItems().contains("Flail Whip") && Settings.getCurrentRemovedItems().contains("Axe")) {
                            // Rejected because none of the preferred weapons exists.
                            continue;
                        }
                        if(Settings.getCurrentRemovedItems().contains("Silver Shield") && Settings.getCurrentRemovedItems().contains("Angel Shield")) {
                            // Rejected because no permanent shield.
                            continue;
                        }
                        if(Settings.getCurrentRemovedItems().contains("Rolling Shuriken") && Settings.getCurrentRemovedItems().contains("Chakram")
                                && Settings.getCurrentRemovedItems().contains("Earth Spear") && Settings.getCurrentRemovedItems().contains("Pistol")) {
                            // Rejected because none of the "good" ranged weapons for Palenque are in the seed.
                            continue;
                        }
                    }
                    break;
                }
            }
            if(Settings.isRandomizeTrapItems()) {
                // For flag space reasons, screens which contain more than one chest can have only one of those chests
                // contain a trap item. We'll randomly pick one location from each set to be the valid one for having
                // the trap item.
                DataFromFile.setBannedTrapLocations(random);
            }
            dialog.updateProgress(25, String.format(Translations.getText("progress.shuffling"), attempt));
            dialog.setTitle(String.format(Translations.getText("progress.shuffling.title"), attempt));
            dialog.progressBar.setIndeterminate(true);

            ItemRandomizer itemRandomizer = new ItemRandomizer();
            ShopRandomizer shopRandomizer = buildShopRandomizer(itemRandomizer);
            AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer);

            itemRandomizer.placeNonRandomizedItems();
            shopRandomizer.placeNonRandomizedItems();
            if(Settings.isRandomizeForbiddenTreasure()) {
                itemRandomizer.placeForbiddenTreasureItem(random);
            }
            if(ShopRandomizationEnum.EVERYTHING.equals(Settings.getShopRandomization())) {
                ((EverythingShopRandomizer)shopRandomizer).placeGuaranteedWeights(random);
            }
            shopRandomizer.determineItemTypes(random);
            accessChecker.determineCursedChests(random);
            if(!itemRandomizer.placeNoRequirementItems(new ArrayList<>(initiallyAccessibleItems), random)) {
                continue;
            }
            if(Settings.isRandomizeCoinChests() || Settings.isRandomizeTrapItems()) {
                if(!itemRandomizer.placeChestOnlyItems(random)) {
                    continue;
                }
            }

            if(!itemRandomizer.placeAllItems(random)) {
                continue;
            }

            boolean ankhJewelLock = false;
            accessChecker.initExitRequirements();
            accessChecker.computeAccessibleNodes("None");
            accessChecker.computeAccessibleNodes(Settings.getCurrentStartingWeapon());
            for(String startingItem : Settings.getStartingItems()) {
                accessChecker.computeAccessibleNodes(startingItem);
            }
            for(String enabledGlitch : Settings.getEnabledGlitches()) {
                accessChecker.computeAccessibleNodes("Setting: " + enabledGlitch);
            }
            for(String enabledDamageBoost : Settings.getEnabledDamageBoosts()) {
                accessChecker.computeAccessibleNodes("Boost: " + enabledDamageBoost);
            }
            if(Settings.isAutomaticTranslations()) {
                accessChecker.computeAccessibleNodes("Setting: La-Mulanese");
            }
            if(!Settings.isAutomaticHardmode()) {
                accessChecker.computeAccessibleNodes("Mode: Normal");
            }
            if(accessChecker.updateForBosses(attempt)) {
                while(!accessChecker.getQueuedUpdates().isEmpty()) {
                    accessChecker.computeAccessibleNodes(accessChecker.getQueuedUpdates().iterator().next());
                    if (accessChecker.getQueuedUpdates().isEmpty()) {
                        if (!accessChecker.isEnoughAnkhJewelsToDefeatAllAccessibleBosses()) {
                            ankhJewelLock = true;
                            break;
                        }
                        if (!accessChecker.updateForBosses(attempt)) {
                            ankhJewelLock = true;
                            break;
                        }
                    }
                }
            }
            else {
                ankhJewelLock = true;
            }
            if(ankhJewelLock) {
                FileUtils.log(String.format("Detected ankh jewel lock on attempt %s. Re-shuffling items.", attempt));
                continue;
            }

            if(accessChecker.isSuccess()) {
                dialog.progressBar.setIndeterminate(false);
                dialog.setSafeClose(false);
                dialog.updateProgress(80, String.format(Translations.getText("progress.shuffling.done"), attempt));

                FileUtils.log(String.format("Successful attempt %s.", attempt));
                FileUtils.flush();

                dialog.updateProgress(85, Translations.getText("progress.spoiler"));
                outputLocations(itemRandomizer, shopRandomizer, attempt);

                dialog.updateProgress(90, Translations.getText("progress.read"));

                List<Zone> rcdData = RcdReader.getRcdScriptInfo();
                List<Block> datInfo = DatReader.getDatScriptInfo();

                dialog.updateProgress(95, Translations.getText("progress.write"));
                itemRandomizer.updateFiles(random);
                shopRandomizer.updateFiles(datInfo, random);
//                    if(Settings.isRandomizeMantras()) {
//                        GameDataTracker.randomizeMantras(random);
//                    }
                RcdWriter.writeRcd(rcdData);
                DatWriter.writeDat(datInfo);
                if(Settings.isRandomizeMainWeapon()) {
                    backupSaves();
                    writeSaveFile();
                }

                File settingsFile = new File("randomizer-config.txt");
                if(settingsFile.exists()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            new File(String.format("%s/randomizer-config.txt", Settings.getStartingSeed())));
                    Files.copy(settingsFile.toPath(), fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }

                if(Settings.isRandomizeMainWeapon()) {
                    File saveFile = new File(String.format("%s/lm_00.sav", Settings.getLaMulanaSaveDir()));
                    if(saveFile.exists()) {
                        FileOutputStream fileOutputStream = new FileOutputStream(
                                new File(String.format("%s/lm_00.sav", Settings.getStartingSeed())));
                        Files.copy(saveFile.toPath(), fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }

                dialog.updateProgress(100, Translations.getText("progress.done"));
                GameDataTracker.clearAll();

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

    private static void backupSaves() {
        try {
            // Make lm_00.sav backup
            File existingSave = new File(String.format("%s/lm_00.sav", Settings.getLaMulanaSaveDir()));
            if(existingSave.exists()) {
                File firstBackupSave = new File(String.format("%s/lm_00.sav.lmr.orig.bak", Settings.getLaMulanaSaveDir()));
                if(!firstBackupSave.exists()) {
                    Files.copy(existingSave.toPath(), firstBackupSave.toPath());
                }
                File backupSave = new File(String.format("%s/lm_00.sav.lmr.bak", Settings.getLaMulanaSaveDir()));
                if(backupSave.exists()) {
                    Files.delete(backupSave.toPath());
                }
                Files.copy(existingSave.toPath(), backupSave.toPath());
            }
        }
        catch (Exception ex) {
            FileUtils.log("Unable to back up save file: " + ex.getMessage());
            FileUtils.logException(ex);
            throw new RuntimeException("Unable to back up save file. Please see logs for more information.");
        }
    }

    private static void writeSaveFile() throws IOException {
        byte[] saveData = buildNewSave();
        String startingWeapon = Settings.getCurrentStartingWeapon();
        if(!"Whip".equals(startingWeapon)) {
            saveData[4113] = (byte)-1; // word + 0x1011; remove whip
            saveData[4114] = (byte)-1; // word + 0x1011; remove whip

            if("Knife".equals(startingWeapon)) {
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
        }

        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(
                    String.format("%s/lm_00.sav", Settings.getLaMulanaSaveDir())));
            dataOutputStream.write(saveData);
            dataOutputStream.flush();
            dataOutputStream.close();
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
        saveData[5] = (byte)1;
        saveData[6] = (byte)2;
        saveData[7] = (byte)1;
        saveData[8] = (byte)1;
        saveData[9] = (byte)24;
        saveData[10] = (byte)0;
        saveData[11] = (byte)-104;
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

    private static AccessChecker buildAccessChecker(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer) {
        AccessChecker accessChecker = new AccessChecker();
        accessChecker.setItemRandomizer(itemRandomizer);
        accessChecker.setShopRandomizer(shopRandomizer);
        itemRandomizer.setAccessChecker(accessChecker);
        shopRandomizer.setAccessChecker(accessChecker);
        return accessChecker;
    }

    private static Set<String> getInitiallyAvailableItems() {
        Set<String> noRequirementItems = new HashSet<>(Settings.getInitiallyAccessibleItems());
        noRequirementItems.removeAll(DataFromFile.getNonRandomizedItems());
        return noRequirementItems;
    }

    private static void determineMainWeapon(Random random) {
        List<String> startingWeapons = new ArrayList<>(5);
        startingWeapons.add("Whip");
        startingWeapons.add("Knife");
        startingWeapons.add("Key Sword");
        startingWeapons.add("Axe");
        startingWeapons.add("Katana");

        for(CustomPlacement customPlacement : DataFromFile.getCustomItemPlacements()) {
            // Whether it's a removed item or a custom placed item, we don't want it here.
            startingWeapons.remove(customPlacement.getContents());
        }

        Settings.setCurrentStartingWeapon(startingWeapons.get(random.nextInt(startingWeapons.size())));
    }

    private static void determineRemovedItems(Random random) {
        Set<String> removedItems = new HashSet<>(Settings.getRemovedItems());
        int totalItemsRemoved = Settings.getMinRandomRemovedItems();
        totalItemsRemoved += random.nextInt(Settings.getMaxRandomRemovedItems() - totalItemsRemoved + 1);

        List<String> removableItems = new ArrayList<>(DataFromFile.getRandomRemovableItems());
        removableItems.removeAll(Settings.getRemovedItems());

        boolean objectZipEnabled = Settings.getEnabledGlitches().contains("Object Zip");
        boolean catPauseEnabled = Settings.getEnabledGlitches().contains("Cat Pause");
        boolean lampGlitchEnabled = Settings.getEnabledGlitches().contains("Lamp Glitch");
        boolean requireKeyFairyCombo = Settings.isRequireSoftwareComboForKeyFairy();
        boolean easierBosses = BossDifficulty.MEDIUM.equals(Settings.getBossDifficulty());
        boolean orbRemoved = false;
        int chosenRemovedItems = 0;
        while(chosenRemovedItems < totalItemsRemoved && !removableItems.isEmpty()) {
            int removedItemIndex = random.nextInt(removableItems.size());
            String removedItem = removableItems.get(removedItemIndex);
            if(!removedItems.contains(removedItem)) {
                removedItems.add(removedItem);
                removableItems.remove(removedItem);
                if("Twin Statue".equals(removedItem)) {
                    // Only possible if raindropping enabled
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
                    if(!catPauseEnabled) {
                        removableItems.remove("Chakram");
                        removableItems.remove("Serpent Staff");
                    }
                    if(!lampGlitchEnabled) {
                        removableItems.remove("Bronze Mirror");
                    }
                } else if("Grapple Claw".equals(removedItem)) {
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
                } else if("Plane Model".equals(removedItem)) {
                    // Don't remove alternative means of getting into Chamber of Birth and the Medicine of the Mind area.
                    removableItems.remove("Hermes' Boots");
                    removableItems.remove("Grapple Claw");
                    removableItems.remove("Bronze Mirror");
                    if(requireKeyFairyCombo) {
                        removableItems.remove("miracle.exe");
                        removableItems.remove("mekuri.exe");
                    }
                } else if("Bronze Mirror".equals(removedItem)) {
                    removableItems.remove("Plane Model");
                    if(!lampGlitchEnabled) {
                        removableItems.remove("Hermes' Boots");
                        removableItems.remove("Grapple Claw");
                    }
                } else if(easierBosses) {
                    if("Silver Shield".equals(removedItem)) {
                        removableItems.remove("Angel Shield");
                    } else if ("Angel Shield".equals(removedItem)) {
                        removableItems.remove("Silver Shield");
                    }
                    if(removedItem.startsWith("Sacred Orb")) {
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
                ++chosenRemovedItems;
            }
        }
        if(Settings.isRandomizeMainWeapon() && !"Whip".equals(Settings.getCurrentStartingWeapon())) {
            removedItems.add("Whip");
        }
        Settings.setCurrentRemovedItems(removedItems);
    }

    private static void outputLocations(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer, int attempt) throws IOException {
        itemRandomizer.outputLocations(attempt);
        shopRandomizer.outputLocations(attempt);
        if (!Settings.getCurrentRemovedItems().isEmpty()) {
            BufferedWriter writer = FileUtils.getFileWriter(String.format("%s/removed_items.txt", Settings.getStartingSeed()));
            if (writer == null) {
                return;
            }

            for(String removedItem : Settings.getCurrentRemovedItems()) {
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
