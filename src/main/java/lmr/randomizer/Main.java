package lmr.randomizer;

import lmr.randomizer.dat.AddObject;
import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.DatReader;
import lmr.randomizer.dat.DatWriter;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.node.CustomPlacement;
import lmr.randomizer.node.MoneyChecker;
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
                DataFromFile.clearCustomItemPlacements();
                DataFromFile.clearAllData();
                return false;
            }

            progressDialog.updateProgress(0, Translations.getText("progress.generating"));

            Settings.saveSettings();

            progressDialog.updateProgress(10, Translations.getText("setup.backup"));

            if(!backupRcd()) {
                return false; // Error messaging handled within
            }
            if(!backupDat()) {
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

                if(Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart()) {
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
            if((Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart()) && !validateSaveDir()) {
                JOptionPane.showMessageDialog(this,
                        "Unable to find La-Mulana save directory",
                        "Randomizer error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(!Settings.isAllowWhipStart() && !Settings.isAllowMainWeaponStart() && !Settings.isAllowSubweaponStart()) {
                JOptionPane.showMessageDialog(this,
                        "Starting without a weapon is not currently enabled",
                        "Randomizer error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(!validateCustomPlacements(this)) {
                // Message created below
                return false;
            }

            Set<String> manuallyRemovedItems = new HashSet<>();
            for(CustomPlacement customPlacement : DataFromFile.getCustomItemPlacements()) {
                if(customPlacement.isRemoveItem()) {
                    // Removed item
                    manuallyRemovedItems.add(customPlacement.getContents());
                }
            }
            if(Settings.isRemoveMainWeapons()) {
                manuallyRemovedItems.addAll(DataFromFile.MAIN_WEAPONS);
                manuallyRemovedItems.remove("Whip"); // Whip gets special treatment.

                Settings.setAlternateMotherAnkh(true); // Required to clear the game without Key Sword.
            }
            if((Settings.getMinRandomRemovedItems() + manuallyRemovedItems.size() > 48)) {
                JOptionPane.showMessageDialog(this,
                        "Minimum removed item count is too high with custom placement settings. A minimum of " + (48 - manuallyRemovedItems.size()) + " will be used instead.",
                        "Randomizer error", JOptionPane.WARNING_MESSAGE);
                Settings.setMinRandomRemovedItems(48 - manuallyRemovedItems.size(), false);
            }
            if((Settings.getMaxRandomRemovedItems() + manuallyRemovedItems.size() > 48)) {
                JOptionPane.showMessageDialog(this,
                        "Maximum removed item count is too high with custom placement settings. A maximum of " + (48 - manuallyRemovedItems.size()) + " will be used instead.",
                        "Randomizer error", JOptionPane.WARNING_MESSAGE);
                Settings.setMaxRandomRemovedItems(48 - manuallyRemovedItems.size(), false);
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
                    if(Settings.isRequireIceCapeForLava() && customPlacement.getContents().equals("Ice Cape")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Please disable the setting \"Require Ice Cape for swimming through lava\"",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(Settings.isRequireFlaresForExtinction() && customPlacement.getContents().equals("Flare Gun")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Please disable the setting \"Require Flare Gun for Chamber of Extinction\"",
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
                    if(!isValidContents(customPlacement.getContents())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Removed item not valid: " + customPlacement.getContents(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    removed.add(customPlacement.getContents());
                }
                else if(customPlacement.isStartingWeapon()) {
                    if(removed.contains(customPlacement.getContents())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Cannot remove starting weapon",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    if(ItemRandomizer.ALL_SUBWEAPONS.contains(customPlacement.getContents())) {
                        if(!Settings.isAllowSubweaponStart()) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom starting weapon not enabled",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                    else if(DataFromFile.MAIN_WEAPONS.contains(customPlacement.getContents())) {
                        if("Whip".equals(customPlacement.getContents())) {
                            if(!Settings.isAllowWhipStart()) {
                                JOptionPane.showMessageDialog(randomizerUI,
                                        "Custom starting weapon not enabled",
                                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                        }
                        else if(!Settings.isAllowMainWeaponStart()) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom starting weapon not enabled",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                    }
                    else {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Invalid starting weapon: " + customPlacement.getContents(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                else if(customPlacement.isStartingItem()) {
                    if(!isValidContents(customPlacement.getContents())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Placed item not valid: " + customPlacement.getContents() + " at location " + customPlacement.getLocation(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                else if(customPlacement.isCurseChest()) {
                    if(!Settings.isRandomizeCursedChests()) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement of cursed chest not valid with current settings for cursed chest randomization",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                    }
                    if(DataFromFile.FLOATING_ITEM_LOCATIONS.contains(customPlacement.getLocation())
                            || DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(customPlacement.getLocation())
                            || "mantra.exe".equals(customPlacement.getLocation())
                            || "emusic.exe".equals(customPlacement.getLocation())
                            || "beolamu.exe".equals(customPlacement.getLocation())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Non-chest location " + customPlacement.getLocation() + " cannot be cursed",
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
                    if(!Settings.isRandomizeEscapeChest()) {
                        if(customPlacement.getLocation().equals(DataFromFile.ESCAPE_CHEST_NAME) || customPlacement.getContents().startsWith(DataFromFile.ESCAPE_CHEST_NAME)) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Custom placement not valid with current settings for randomized escape chest",
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
                    if(customPlacement.getContents().startsWith("Coin:") || DataFromFile.EXPLODING_CHEST_NAME.equals(customPlacement.getContents())) {
                        if(DataFromFile.FLOATING_ITEM_LOCATIONS.contains(customPlacement.getLocation())
                                || DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(customPlacement.getLocation())
                                || "mantra.exe".equals(customPlacement.getLocation())
                                || "emusic.exe".equals(customPlacement.getLocation())
                                || "beolamu.exe".equals(customPlacement.getLocation())) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Item " + customPlacement.getContents() + " cannot be placed at non-chest location " + customPlacement.getLocation(),
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
            if(DataFromFile.getAllCoinChests().contains(location)
                    || DataFromFile.ESCAPE_CHEST_NAME.equals(location)) {
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
            if(DataFromFile.getAllItems().contains(contents)
                    || "Whip".equals(contents) || "Ankh Jewel (Extra)".equals(contents)) {
                return true;
            }
            if(DataFromFile.getAllCoinChests().contains(contents)
                    || DataFromFile.ESCAPE_CHEST_NAME.equals(contents)) {
                return true;
            }
            if(contents.equals("Trap: Graveyard") || contents.equals("Trap: Exploding")
                    || contents.equals("Trap: Inferno Orb") || contents.equals("Trap: Twin Ankh")) {
                return true;
            }
            if(contents.equals("Weights") || contents.equals("Shuriken Ammo") || contents.equals("Rolling Shuriken Ammo")
                    || contents.equals("Caltrops Ammo") || contents.equals("Chakram Ammo") || contents.equals("Flare Gun Ammo")
                    || contents.equals("Earth Spear Ammo") || contents.equals("Pistol Ammo") || contents.equals("Bomb Ammo")) {
                return true;
            }
            return false;
        }
    }

    protected static void doTheThing(ProgressDialog dialog) throws Exception {
        FileUtils.log(String.format("Shuffling items for seed %s", Settings.getStartingSeed()));

        DataFromFile.clearAllData();

        Random random = new Random(Settings.getStartingSeed());
        if(Settings.isRandomizeTrapItems()) {
            // For flag space reasons, screens which contain more than one chest can have only one of those chests
            // contain a trap item. We'll randomly pick one location from each set to be the valid one for having
            // the trap item.
            DataFromFile.setBannedTrapLocations(random);
        }

        int totalItemsRemoved = getTotalItemsRemoved(random);
        determineStartingWeapon(random);

        BacksideDoorRandomizer backsideDoorRandomizer = new BacksideDoorRandomizer();
        backsideDoorRandomizer.determineDoorDestinations(random);
        backsideDoorRandomizer.logLocations();

        TransitionGateRandomizer transitionGateRandomizer = new TransitionGateRandomizer(backsideDoorRandomizer);

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

            ItemRandomizer itemRandomizer = new ItemRandomizer();
            ShopRandomizer shopRandomizer = buildShopRandomizer(itemRandomizer);
            AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer, backsideDoorRandomizer, transitionGateRandomizer);
            accessChecker.initExitRequirements();

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
                    FileUtils.log(String.format("Detected ankh jewel lock on attempt %s. Re-shuffling items.", attempt));
                    continue;
                }
            }
            if(Settings.isGenerationComplete(attempt) || accessChecker.isSuccess(attempt)) {
                dialog.progressBar.setIndeterminate(false);
                dialog.setSafeClose(false);
                dialog.updateProgress(80, String.format(Translations.getText("progress.shuffling.done"), attempt));

                FileUtils.log(String.format("Successful attempt %s.", attempt));
                FileUtils.flush();

                MoneyChecker moneyChecker;
                if(Settings.isRandomizeTransitionGates()) {
                    transitionGateRandomizer.placeTowerOfTheGoddessPassthroughPipe(random);
                    FileUtils.logDetail("Placed pipe transitions", attempt);

                    moneyChecker = new MoneyChecker(itemRandomizer, shopRandomizer, transitionGateRandomizer);
                    for (String startingNode : startingNodes) {
                        moneyChecker.computeAccessibleNodes(startingNode, attempt);
                    }
                    while(!moneyChecker.getQueuedUpdates().isEmpty()) {
                        moneyChecker.computeAccessibleNodes(moneyChecker.getQueuedUpdates().iterator().next(), attempt);
                    }
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
                FileUtils.log("Updated item location data");

                shopRandomizer.updateFiles(datInfo, isSubweaponOnly(), moneyChecker, random);
                FileUtils.log("Updated shop data");

                List<String> availableSubweapons = new ArrayList<>(ItemRandomizer.ALL_SUBWEAPONS);
                availableSubweapons.removeAll(Settings.getRemovedItems());
                availableSubweapons.removeAll(Settings.getCurrentRemovedItems());
                if(!availableSubweapons.isEmpty()) {
                    GameDataTracker.updateSubweaponPot(availableSubweapons.get(random.nextInt(availableSubweapons.size())));
                }
                FileUtils.log("Updated subweapon pot data");

                if(Settings.isRandomizeBacksideDoors()) {
                    backsideDoorRandomizer.updateBacksideDoors();
                    FileUtils.log("Updated backside door data");
                }
                if(Settings.isRandomizeTransitionGates()) {
                    transitionGateRandomizer.updateTransitions();
                    FileUtils.log("Updated transition gate data");
                }

//                if(Settings.isRandomizeMantras()) {
//                    GameDataTracker.randomizeMantras(random);
//                }
                FileUtils.logFlush("Writing rcd file");
                RcdWriter.writeRcd(rcdData);

                FileUtils.log("rcd file successfully written");
                FileUtils.logFlush("Writing dat file");

                DatWriter.writeDat(datInfo);
                FileUtils.logFlush("dat file successfully written");
                if(Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart()) {
                    backupSaves();
                    writeSaveFile();
                }

                if(Settings.isRandomizeBacksideDoors()) {
                    FileUtils.updateGraphicsFiles();
                }

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
                DataFromFile.clearCustomItemPlacements();

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
        if(Settings.isRandomizeTransitionGates()) {
            startingNodes.add("Setting: Random Transitions");
        }
        else {
            startingNodes.add("Setting: Nonrandom Transitions");
        }
        startingNodes.add(Settings.isAlternateMotherAnkh() ? "Setting: Alternate Mother" : "Setting: Standard Mother");

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
                    saveData[0x11 + 0x124] = (byte)4; // Held main weapon slot number
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
                                                    BacksideDoorRandomizer backsideDoorRandomizer, TransitionGateRandomizer transitionGateRandomizer) {
        AccessChecker accessChecker = new AccessChecker();
        accessChecker.setItemRandomizer(itemRandomizer);
        accessChecker.setShopRandomizer(shopRandomizer);
        accessChecker.setBacksideDoorRandomizer(backsideDoorRandomizer);
        accessChecker.setTransitionGateRandomizer(transitionGateRandomizer);
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
        List<String> startingWeapons = new ArrayList<>();
        if(Settings.isAllowWhipStart()) {
            startingWeapons.add("Whip");
        }
        if(Settings.isAllowMainWeaponStart()) {
            startingWeapons.add("Knife");
            startingWeapons.add("Key Sword");
            startingWeapons.add("Axe");
            startingWeapons.add("Katana");
        }
        if(Settings.isAllowSubweaponStart()) {
            startingWeapons.addAll(ItemRandomizer.ALL_SUBWEAPONS);
        }

        for(CustomPlacement customPlacement : DataFromFile.getCustomItemPlacements()) {
            if(customPlacement.isStartingWeapon()) {
                FileUtils.log("Selected starting weapon: " + customPlacement.getContents());
                Settings.setCurrentStartingWeapon(customPlacement.getContents());
                return;
            }
            // Whether it's a removed item, starting item, or a custom placed item, we don't want it here.
            startingWeapons.remove(customPlacement.getContents());
        }
        startingWeapons.removeAll(Settings.getRemovedItems());
        Settings.setCurrentStartingWeapon(startingWeapons.get(random.nextInt(startingWeapons.size())));
        FileUtils.logFlush("Selected starting weapon: " + Settings.getCurrentStartingWeapon());
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

        boolean subweaponOnly = isSubweaponOnly(); // Preliminary check based on custom placements; currently this cannot happen randomly.
        if(isSubweaponOnly()) {
            removableItems.removeAll(subweaponOnlyItems);
        }

        for(String removedItem : removedItems) {
            orbRemoved = filterRemovableItems(removedItem, removableItems, objectZipEnabled, easierBosses, lampGlitchEnabled, requireKeyFairyCombo, catPauseEnabled, lamulanaMantraRequired, subweaponOnly, orbRemoved, mainWeapons, easierWeapons, subweaponOnlyItems, easierSubweaponsForPalenque);
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
                orbRemoved = filterRemovableItems(removedItem, removableItems, objectZipEnabled, easierBosses, lampGlitchEnabled, requireKeyFairyCombo, catPauseEnabled, lamulanaMantraRequired, subweaponOnly, orbRemoved, mainWeapons, easierWeapons, subweaponOnlyItems, easierSubweaponsForPalenque);
                ++chosenRemovedItems;
            }
        }
        Settings.setCurrentRemovedItems(removedItems);
    }

    private static boolean filterRemovableItems(String removedItem, List<String> removableItems, boolean objectZipEnabled, boolean easierBosses, boolean lampGlitchEnabled, boolean requireKeyFairyCombo, boolean catPauseEnabled, boolean lamulanaMantraRequired, boolean subweaponOnly, boolean orbRemoved, List<String> mainWeapons, List<String> easierWeapons, List<String> subweaponOnlyItems, List<String> easierSubweaponsForPalenque) {
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
            }
        }
        else if("Ring".equals(removedItem)) {
            subweaponOnlyItems.remove(removedItem);
            if(Settings.isSubweaponOnlyLogic() && mainWeapons.size() == 1) {
                // Can't kill Tiamat, main weapon is now required.
                removableItems.remove(mainWeapons.get(0));
            }
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

            for(String removedItem : Settings.getCurrentRemovedItems()) {
                writer.write(Translations.getItemText(removedItem, false));
                writer.newLine();
            }
            for(String removedItem : Settings.getRemovedItems()) {
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
