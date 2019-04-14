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
                DataFromFile.clearCustomPlacementData();
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

                if(Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart() || Settings.isRandomizeStartingLocation()) {
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
            if(Settings.isRequireFullAccess() && Settings.isRemoveMainWeapons()) {
                JOptionPane.showMessageDialog(this,
                        "The setting \"Require all items to be accessible\" cannot be used when removing Main Weapons",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(Settings.isRandomizeStartingLocation() && !Settings.isRandomizeTransitionGates() && !Settings.getStartingItems().contains("Holy Grail")) {
                JOptionPane.showMessageDialog(this,
                        "Please randomize transitions or enable starting with " + Translations.getText("items.HolyGrail"),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(Settings.isRandomizeStartingLocation() && !ShopRandomizationEnum.EVERYTHING.equals(Settings.getShopRandomization())) {
                JOptionPane.showMessageDialog(this,
                        String.format("Please enable %s %s when using %s", Translations.getText("randomization.randomizeShops"), Translations.getText("randomization.randomizeShops.everything"), Translations.getText("randomization.randomizeStartingLocation")),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(!validateCustomPlacements(this)) {
                // Message created below
                return false;
            }

            Set<String> manuallyRemovedItems = new HashSet<>(DataFromFile.getCustomPlacementData().getRemovedItems());
            if(Settings.isRemoveMainWeapons()) {
                manuallyRemovedItems.addAll(DataFromFile.MAIN_WEAPONS);
                manuallyRemovedItems.remove("Whip"); // Whip gets special treatment.

                if(!Settings.isAlternateMotherAnkh()) {
                    JOptionPane.showMessageDialog(this,
                            String.format("The setting \"%s \" is required when removing Main Weapons", Translations.getText("gameplay.alternateMotherAnkh")),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!Settings.isAllowSubweaponStart()) {
                    JOptionPane.showMessageDialog(this,
                            "Starting with subweapon is required when removing Main Weapons",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if((Settings.getMinRandomRemovedItems() + manuallyRemovedItems.size() > 99)) {
                JOptionPane.showMessageDialog(this,
                        "Minimum removed item count is too high with custom placement settings. A minimum of " + (99 - manuallyRemovedItems.size()) + " will be used instead.",
                        "Randomizer error", JOptionPane.WARNING_MESSAGE);
                Settings.setMinRandomRemovedItems(99 - manuallyRemovedItems.size(), false);
            }
            if((Settings.getMaxRandomRemovedItems() + manuallyRemovedItems.size() > 99)) {
                JOptionPane.showMessageDialog(this,
                        "Maximum removed item count is too high with custom placement settings. A maximum of " + (99 - manuallyRemovedItems.size()) + " will be used instead.",
                        "Randomizer error", JOptionPane.WARNING_MESSAGE);
                Settings.setMaxRandomRemovedItems(99 - manuallyRemovedItems.size(), false);
            }
            return true;
        }

        private boolean validateCustomPlacements(RandomizerUI randomizerUI) {
            CustomPlacementData customPlacementData = DataFromFile.getCustomPlacementData();

            List<String> locations = new ArrayList<>();
            List<String> items = new ArrayList<>();
            if(customPlacementData.getStartingWeapon() != null) {
                if(Settings.getStartingItemsIncludingCustom().contains(customPlacementData.getStartingWeapon())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom starting weapon cannot be the same as starting item" + customPlacementData.getStartingWeapon() + " not valid with current settings for starting item",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if(customPlacementData.getRemovedItems().contains(customPlacementData.getStartingWeapon())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Cannot remove starting weapon",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if(ItemRandomizer.ALL_SUBWEAPONS.contains(customPlacementData.getStartingWeapon())) {
                    if(!Settings.isAllowSubweaponStart()) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom starting weapon not enabled",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                else if(DataFromFile.MAIN_WEAPONS.contains(customPlacementData.getStartingWeapon())) {
                    if("Whip".equals(customPlacementData.getStartingWeapon())) {
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
                            "Invalid starting weapon: " + customPlacementData.getStartingWeapon(),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if(customPlacementData.getStartingLocation() != null) {
                if(!Settings.isRandomizeStartingLocation()) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            String.format("Please enable \"%s\"", Translations.getText("randomization.randomizeStartingLocation")),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!DataFromFile.STARTING_LOCATIONS.contains(customPlacementData.getStartingLocation())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Invalid starting location: " + LocationCoordinateMapper.getStartingZoneName(customPlacementData.getStartingLocation()),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!Settings.isRandomizeTransitionGates() && customPlacementData.getStartingLocation() == 21) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            String.format("Please enable \"%s\"", Translations.getText("randomization.randomizeTransitionGates")),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if(!customPlacementData.getCustomDoorPlacements().isEmpty()) {
                if(!Settings.isRandomizeBacksideDoors()) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Please enable the setting \"" + Translations.getText("randomization.randomizeBacksideDoors") + "\"",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                Map<String, String> placedDoorsAndDestinations = new HashMap<>();
                Map<String, Integer> placedDoorsAndBosses = new HashMap<>();
                for(CustomDoorPlacement customDoorPlacement : customPlacementData.getCustomDoorPlacements()) {
                    if(!customDoorPlacement.getTargetDoor().startsWith("Door F") && !customDoorPlacement.getTargetDoor().startsWith("Door B")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Backside door " + customDoorPlacement.getTargetDoor() + " is invalid",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(!customDoorPlacement.getDestinationDoor().startsWith("Door F") && !customDoorPlacement.getDestinationDoor().startsWith("Door B")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Backside door " + customDoorPlacement.getDestinationDoor() + " is invalid",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(!Settings.isRandomizeNonBossDoors()) {
                        if(isNonBossDoor(customDoorPlacement.getTargetDoor())) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Please enable the setting \"" + Translations.getText("randomization.randomizeNonBossDoors") + "\"",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if(isNonBossDoor(customDoorPlacement.getDestinationDoor())) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Please enable the setting \"" + Translations.getText("randomization.randomizeNonBossDoors") + "\"",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                    if(placedDoorsAndDestinations.keySet().contains(customDoorPlacement.getTargetDoor())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Backside door " + customDoorPlacement.getTargetDoor() + " cannot be assigned multiple destinations",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(placedDoorsAndDestinations.values().contains(customDoorPlacement.getDestinationDoor())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Multiple backside doors cannot lead to destination door " + customDoorPlacement.getDestinationDoor(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(customDoorPlacement.getTargetDoor().equals(customDoorPlacement.getDestinationDoor())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Backside door " + customDoorPlacement.getTargetDoor() + " cannot lead to itself",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(customDoorPlacement.getAssignedBoss() != null
                            && customDoorPlacement.getAssignedBoss() != 9 && (customDoorPlacement.getAssignedBoss() < 1 || customDoorPlacement.getAssignedBoss() > 7)) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Assigned boss for backside door " + customDoorPlacement.getTargetDoor() + " could not be processed; please use boss name or numbers 1-7",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(placedDoorsAndDestinations.values().contains(customDoorPlacement.getTargetDoor())) {
                        if(!customDoorPlacement.getTargetDoor().equals(placedDoorsAndDestinations.get(customDoorPlacement.getDestinationDoor()))) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Support for non-reversible backside door placement does not exist at this time; please update assignment for " + customDoorPlacement.getTargetDoor() + " or " + customDoorPlacement.getDestinationDoor(),
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                    if(placedDoorsAndDestinations.keySet().contains(customDoorPlacement.getDestinationDoor())) {
                        if(!customDoorPlacement.getTargetDoor().equals(placedDoorsAndDestinations.get(customDoorPlacement.getDestinationDoor()))) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Support for non-reversible backside door placement does not exist at this time; please update assignment for " + customDoorPlacement.getTargetDoor() + " or " + customDoorPlacement.getDestinationDoor(),
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                    Integer existingBoss = placedDoorsAndBosses.get(customDoorPlacement.getDestinationDoor());
                    if(existingBoss != null && !existingBoss.equals(customDoorPlacement.getAssignedBoss())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "A pair of backside doors cannot be assigned two different bosses at this time; please update assignment for " + customDoorPlacement.getTargetDoor() + " or " + customDoorPlacement.getDestinationDoor(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if((customDoorPlacement.getAssignedBoss() != null && customDoorPlacement.getAssignedBoss() > 0 && customDoorPlacement.getAssignedBoss() < 9)
                            && (customDoorPlacement.getTargetDoor().endsWith("B8") || customDoorPlacement.getDestinationDoor().endsWith("B8"))) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Dimensional Corridor may not be paired with a Bronze Mirror door",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if((customDoorPlacement.getAssignedBoss() != null && customDoorPlacement.getAssignedBoss() > 0 && customDoorPlacement.getAssignedBoss() < 9)
                            && (customDoorPlacement.getTargetDoor().endsWith("B9") || customDoorPlacement.getDestinationDoor().endsWith("B9"))) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Gate of Time may not be paired with a Bronze Mirror door",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    placedDoorsAndDestinations.put(customDoorPlacement.getTargetDoor(), customDoorPlacement.getDestinationDoor());
                    placedDoorsAndBosses.put(customDoorPlacement.getTargetDoor(), customDoorPlacement.getAssignedBoss());
                    placedDoorsAndBosses.put(customDoorPlacement.getDestinationDoor(), customDoorPlacement.getAssignedBoss());
                }
            }
            if(!customPlacementData.getCustomTransitionPlacements().isEmpty()) {
                if(!Settings.isRandomizeTransitionGates()) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Please enable the setting \"" + Translations.getText("randomization.randomizeTransitionGates") + "\"",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                Map<String, String> placedTargetAndDestination = new HashMap<>();
                for(CustomTransitionPlacement customTransitionPlacement : customPlacementData.getCustomTransitionPlacements()) {
                    if(!isValidTransition(customTransitionPlacement.getTargetTransition())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Transition " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", "") + " is invalid",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(!isValidTransition(customTransitionPlacement.getDestinationTransition())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Transition " + customTransitionPlacement.getDestinationTransition().replaceAll("^Transition:? ", "") + " is invalid",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(!isValidTransitionDirection(customTransitionPlacement.getTargetTransition(), customTransitionPlacement.getDestinationTransition())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Transition " + customTransitionPlacement.getDestinationTransition().replaceAll("^Transition:? ", "") + " is the wrong direction for " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", ""),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(!Settings.isRandomizeOneWayTransitions()) {
                        if(isOneWayTransition(customTransitionPlacement.getTargetTransition())) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Please enable the setting \"" + Translations.getText("randomization.randomizeOneWayTransitions") + "\"",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if(isOneWayTransition(customTransitionPlacement.getDestinationTransition())) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Please enable the setting \"" + Translations.getText("randomization.randomizeOneWayTransitions") + "\"",
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }

                    if(placedTargetAndDestination.keySet().contains(customTransitionPlacement.getTargetTransition())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Transition " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", "") + " cannot be assigned multiple destinations",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(placedTargetAndDestination.values().contains(customTransitionPlacement.getDestinationTransition())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Multiple transitions cannot lead to destination transition " + customTransitionPlacement.getDestinationTransition().replaceAll("^Transition:? ", ""),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(customTransitionPlacement.getTargetTransition().equals(customTransitionPlacement.getDestinationTransition())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Transition " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", "") + " cannot lead to itself",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(placedTargetAndDestination.values().contains(customTransitionPlacement.getTargetTransition())) {
                        if(!customTransitionPlacement.getTargetTransition().equals(placedTargetAndDestination.get(customTransitionPlacement.getDestinationTransition()))) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Support for non-reversible transition placement does not exist at this time; please update assignment for " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", "") + " or " + customTransitionPlacement.getDestinationTransition().replaceAll("^Transition:? ", ""),
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                    if(placedTargetAndDestination.keySet().contains(customTransitionPlacement.getDestinationTransition())) {
                        if(!customTransitionPlacement.getTargetTransition().equals(placedTargetAndDestination.get(customTransitionPlacement.getDestinationTransition()))) {
                            JOptionPane.showMessageDialog(randomizerUI,
                                    "Support for non-reversible backside door placement does not exist at this time; please update assignment for " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", "") + " or " + customTransitionPlacement.getDestinationTransition().replaceAll("^Transition:? ", ""),
                                    "Custom placement error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                    placedTargetAndDestination.put(customTransitionPlacement.getTargetTransition(), customTransitionPlacement.getDestinationTransition());
                }
            }
            if(Settings.isRequireFullAccess() && !customPlacementData.getRemovedItems().isEmpty()) {
                if(Settings.isRequireFullAccess()) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Please disable the setting \"Require all items to be accessible\"",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            for(String customRemovedItem : customPlacementData.getRemovedItems()) {
                if(Settings.getStartingItems().contains(customRemovedItem)) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement of " + customRemovedItem + " not valid with current settings for starting item",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if(customRemovedItem.startsWith("Coin:")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Coin chests cannot be removed items",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(customRemovedItem.startsWith("Trap:")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Traps cannot be removed items",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(Settings.isRequireIceCapeForLava() && customRemovedItem.equals("Ice Cape")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Please disable the setting \"Require Ice Cape for swimming through lava\"",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(Settings.isRequireFlaresForExtinction() && customRemovedItem.equals("Flare Gun")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Please disable the setting \"Require Flare Gun for Chamber of Extinction\"",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!DataFromFile.getRandomRemovableItems().contains(customRemovedItem)
                        && !"Whip".equals(customRemovedItem)) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            customRemovedItem + " cannot be a removed item",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!isValidContents(customRemovedItem)) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Removed item not valid: " + customRemovedItem,
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            for(String customStartingItem : customPlacementData.getStartingItems()) {
                if (!isValidContents(customStartingItem)) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Starting item not valid: " + customStartingItem,
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            for(String cursedChestLocation : customPlacementData.getCursedChests()) {
                if(!Settings.isRandomizeCursedChests()) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement of cursed chest not valid with current settings for cursed chest randomization",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(DataFromFile.FLOATING_ITEM_LOCATIONS.contains(cursedChestLocation)
                        || DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(cursedChestLocation)
                        || "mantra.exe".equals(cursedChestLocation)
                        || "emusic.exe".equals(cursedChestLocation)
                        || "beolamu.exe".equals(cursedChestLocation)) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Non-chest location " + cursedChestLocation + " cannot be cursed",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            for(CustomItemPlacement customItemPlacement : customPlacementData.getCustomItemPlacements()) {
                if(Settings.getStartingItems().contains(customItemPlacement.getContents())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement of " + customItemPlacement.getContents() + " not valid with current settings for starting item",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if(locations.contains(customItemPlacement.getLocation())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Location used for multiple items: " + customItemPlacement.getLocation(),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(items.contains(customItemPlacement.getContents())
                        && !"Weights".equals(customItemPlacement.getContents())
                        && !customItemPlacement.getContents().endsWith(" Ammo")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Item placed in multiple locations: " + customItemPlacement.getContents(),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!isValidLocation(customItemPlacement.getLocation())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Location not valid: " + customItemPlacement.getLocation() + " for item " + customItemPlacement.getContents(),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!isValidContents(customItemPlacement.getContents())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Placed item not valid: " + customItemPlacement.getContents() + " at location " + customItemPlacement.getLocation(),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(DataFromFile.SHOP_ITEMS.contains(customItemPlacement.getLocation())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "To place " + customItemPlacement.getLocation() + " in a shop, please use the shop name and item number instead of the item name",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!Settings.isRandomizeTrapItems()) {
                    if(customItemPlacement.getLocation().startsWith("Trap:")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom placement at location " + customItemPlacement.getLocation() + " not valid with current settings for randomized traps",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(customItemPlacement.getContents().startsWith("Trap:")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom placement of item " + customItemPlacement.getContents() + " not valid with current settings for randomized traps",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                if(!Settings.isRandomizeEscapeChest()) {
                    if(customItemPlacement.getLocation().equals(DataFromFile.ESCAPE_CHEST_NAME) || customItemPlacement.getContents().startsWith(DataFromFile.ESCAPE_CHEST_NAME)) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom placement not valid with current settings for randomized escape chest",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                if(!Settings.isRandomizeCoinChests()) {
                    if(customItemPlacement.getLocation().startsWith("Coin:")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom placement at location " + customItemPlacement.getLocation() + " not valid with current settings for randomized coin chests",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(customItemPlacement.getContents().startsWith("Coin:")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom placement of item " + customItemPlacement.getContents() + " not valid with current settings for randomized coin chests",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                if(!Settings.isRandomizeForbiddenTreasure()) {
                    if(customItemPlacement.getLocation().equals("Provocative Bathing Suit")
                            || customItemPlacement.getContents().equals("Provocative Bathing Suit")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom placement not valid with current settings for Provocative Bathing Suit randomization",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                else if(!Settings.isHTFullRandom()) {
                    if(customItemPlacement.getLocation().equals("Provocative Bathing Suit")
                            && !customItemPlacement.getContents().startsWith("Map (")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom placement not valid with current settings for Provocative Bathing Suit randomization",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                if(customItemPlacement.getContents().startsWith("Coin:") || DataFromFile.EXPLODING_CHEST_NAME.equals(customItemPlacement.getContents())) {
                    if(DataFromFile.FLOATING_ITEM_LOCATIONS.contains(customItemPlacement.getLocation())
                            || DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(customItemPlacement.getLocation())
                            || "mantra.exe".equals(customItemPlacement.getLocation())
                            || "emusic.exe".equals(customItemPlacement.getLocation())
                            || "beolamu.exe".equals(customItemPlacement.getLocation())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Item " + customItemPlacement.getContents() + " cannot be placed at non-chest location " + customItemPlacement.getLocation(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }

                if(!Settings.isRandomizeDracuetShop() && customItemPlacement.getLocation().startsWith("Shop 23 (HT)")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement not valid with current settings for Dracuet shop randomization",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if(ShopRandomizationEnum.CATEGORIZED.equals(Settings.getShopRandomization())
                        && customItemPlacement.getLocation().startsWith("Shop ")) {
                    if(!DataFromFile.CATEGORIZED_SHOP_ITEM_LOCATIONS.contains(customItemPlacement.getLocation())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom placement of item at " + customItemPlacement.getLocation() + " not valid with current settings for shop randomization",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    else if("Weights".equals(customItemPlacement.getContents())
                            || customItemPlacement.getContents().endsWith(" Ammo")) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom placement of " + customItemPlacement.getContents() + " not valid with current settings for shop randomization",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }

                if(!Settings.isRandomizeStartingLocation() && customItemPlacement.getLocation().startsWith("Shop 0")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            String.format("Please enable the setting \"%s\"", Translations.getText("randomization.randomizeStartingLocation")),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                locations.add(customItemPlacement.getLocation());
                items.add(customItemPlacement.getContents());
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
                if(Settings.isRandomizeStartingLocation() && location.startsWith(DataFromFile.CUSTOM_SHOP_NAME)) {
                    return true;
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

        private boolean isValidTransition(String transition) {
            String formattedTransition = transition.replace("Transition ", "Transition: ");
            return "Transition: Goddess W1".equals(formattedTransition)
                    || "Transition: Inferno W1".equals(formattedTransition)
                    || (TransitionGateRandomizer.getTransitionList().contains(formattedTransition)
                    && !formattedTransition.startsWith("Transition: Sun R")
                    && !formattedTransition.startsWith("Transition: Extinction L"));
        }

        private boolean isOneWayTransition(String transition) {
            String formattedTransition = transition.replace("Transition ", "Transition: ");
            return !"Transition: Goddess L1".equals(formattedTransition)
                    && !"Transition: Illusion R1".equals(formattedTransition)
                    && !"Transition: Twin U3".equals(formattedTransition)
                    && !"Transition: Dimensional D1".equals(formattedTransition)
                    && !"Transition: Shrine U1".equals(formattedTransition)
                    && !"Transition: Endless D1".equals(formattedTransition)
                    && !"Transition: Extinction U3".equals(formattedTransition)
                    && !"Transition: Inferno W1".equals(formattedTransition)
                    && !"Transition: Retromausoleum D1".equals(formattedTransition)
                    && !"Transition: Goddess W1".equals(formattedTransition)
                    && !"Transition: Twin U2".equals(formattedTransition)
                    && !"Transition: Shrine D3".equals(formattedTransition)
                    && !"Transition: Endless U1".equals(formattedTransition)
                    && !"Transition: Shrine D2".equals(formattedTransition);
        }

        private boolean isNonBossDoor(String door) {
            return !door.endsWith("8") && !door.endsWith("9");
        }

        private boolean isValidTransitionDirection(String transitionTarget, String transitionDestination) {
            char transitionDirection1 = transitionTarget.charAt(transitionTarget.length() - 2);
            char transitionDirection2 = transitionDestination.charAt(transitionDestination.length() - 2);

            // Handle special transitions
            if(transitionDirection1 == 'W' && transitionTarget.contains("Goddess W1")) {
                transitionDirection1 = 'U';
            }
            if(transitionDirection2 == 'W' && transitionDestination.contains("Goddess W1")) {
                transitionDirection2 = 'U';
            }
            if(transitionDirection1 == 'W' && transitionTarget.contains("Inferno W1")) {
                transitionDirection1 = 'D';
            }
            if(transitionDirection2 == 'W' && transitionDestination.contains("Inferno W1")) {
                transitionDirection2 = 'D';
            }

            // Check direction
            if (transitionDirection1 == 'U') {
                return transitionDirection2 == 'D';
            }
            else if (transitionDirection1 == 'R') {
                return transitionDirection2 == 'L';
            }
            else if (transitionDirection1 == 'D') {
                return transitionDirection2 == 'U';
            }
            else if (transitionDirection1 == 'L') {
                return transitionDirection2 == 'R';
            }
            return false;
        }
    }

    protected static void doTheThing(ProgressDialog dialog) throws Exception {
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
//        if(Settings.isFoolsMode() && DataFromFile.getCustomPlacementData().getMedicineColor() == null) {
//            List<String> medicineColors = Arrays.asList("Red", "Green", "Yellow", null);
//            Settings.setMedicineColor(medicineColors.get(random.nextInt(medicineColors.size())));
//        }

        int totalItemsRemoved = getTotalItemsRemoved(random);
        determineStartingLocation(random);
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

                if(Settings.isRandomizeBacksideDoors()) {
                    backsideDoorRandomizer.updateBacksideDoors();
                    FileUtils.logFlush("Updated backside door data");
                }
                if(Settings.isRandomizeTransitionGates()) {
                    transitionGateRandomizer.updateTransitions();
                    FileUtils.logFlush("Updated transition gate data");
                }
                if(Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart() || Settings.isRandomizeStartingLocation()) {
                    GameDataTracker.updateXelpudIntro(datInfo);
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
                if(Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart() || Settings.isRandomizeStartingLocation()) {
                    backupSaves();
                    writeSaveFile();
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
        if(Settings.isRandomizeTransitionGates()) {
            startingNodes.add("Setting: Random Transitions");
        }
        else {
            startingNodes.add("Setting: Nonrandom Transitions");
        }
        if(!LocationCoordinateMapper.isSurfaceStart()) {
            startingNodes.add("Setting: Alternate Start");
        }
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
                saveData[0x11 + grailFlag] = (byte)1;
            }
            saveData[0x069 * 2 + 0x1011 + 1] = (byte)10;
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
            startingWeapons.add("Key Sword");
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
        }

        CustomPlacementData customPlacementData = DataFromFile.getCustomPlacementData();
        Integer customStartingLocation = customPlacementData.getStartingLocation();
        if(customStartingLocation != null) {
            Settings.setCurrentStartingLocation(customStartingLocation);
            FileUtils.logFlush("Selected custom starting location: " + LocationCoordinateMapper.getStartingZoneName(customStartingLocation));
            return;
        }

        List<Integer> possibleStartingLocations = new ArrayList<>(DataFromFile.STARTING_LOCATIONS);
        if(!Settings.isRandomizeTransitionGates()) {
            possibleStartingLocations.remove(21);
        }
        Settings.setCurrentStartingLocation(possibleStartingLocations.get(random.nextInt(possibleStartingLocations.size())));
        FileUtils.logFlush("Selected starting location: " + LocationCoordinateMapper.getStartingZoneName(Settings.getCurrentStartingLocation()));
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
