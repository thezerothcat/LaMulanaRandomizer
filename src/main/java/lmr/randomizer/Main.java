package lmr.randomizer;

import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.DatReader;
import lmr.randomizer.dat.DatWriter;
import lmr.randomizer.node.AccessChecker;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        }
        SwingUtilities.invokeLater(new RandomizerRunnable());
    }

    static class RandomizerRunnable implements Runnable {
        @Override
        public void run() {
            RandomizerUI randomizerUI = new RandomizerUI();
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
            progressDialog.updateProgress(0, Translations.getText("progress.generating"));

            buttonPanel = new ButtonPanel(this);
            add(buttonPanel, "grow");
            pack();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if("generate".equals(e.getActionCommand())) {
                generateSeed();
            }
            else if("apply".equals(e.getActionCommand())) {
                generateAndApply();
            }
            else if("restore".equals(e.getActionCommand())) {
                try {
                    this.progressDialog.updateProgress(0, Translations.getText("restore.start"));
                    Frame f = this;
                    SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            progressDialog.setLocationRelativeTo(f);
                            restore();

                            return null;
                        }
                    };
                    swingWorker.execute();
                    progressDialog.setVisible(true);
                } catch (Exception ex) {
                    FileUtils.log("Error: " + ex.getMessage());
                    ex.printStackTrace();
                    throw ex;
                }
            }
        }

        private void generateSeed() {
            progressDialog.updateProgress(0, Translations.getText("progress.generating"));

            mainPanel.updateSettings();
            tabbedPanel.updateSettings();
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
                    FileUtils.closeAll();
                    System.exit(0);
                }
                else if (!FileUtils.hashRcdFile(existingRcd)) {
                    JOptionPane.showMessageDialog(this,
                            "The data/mapdata/script.rcd file in the game directory is not original! Please restore it from a backup / clean install!",
                            "Randomizer error", JOptionPane.ERROR_MESSAGE);
                    FileUtils.closeAll();
                    System.exit(0);
                }

                try {
                    // Make script.rcd backup
                    FileOutputStream fileOutputStream = new FileOutputStream(new File("script.rcd.bak"));
                    Files.copy(existingRcd.toPath(), fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                catch (Exception ex) {
                    FileUtils.log("unable to back up script.rcd: " + ex.getMessage());
                    FileUtils.closeAll();
                    System.exit(0);
                }
            }
            File datFile = new File(Settings.getBackupDatFile());
            if(!datFile.exists()) {
                File existingDat = new File(String.format("%s/data/language/%s/script_code.dat",
                        Settings.getLaMulanaBaseDir(), Settings.getLanguage()));
                if(!FileUtils.hashDatFile(existingDat)) {
                    FileUtils.log("unable to back up script_code.dat - file already modified");
                    FileUtils.closeAll();
                    System.exit(0);
                }

                try {
                    // Make script_code.dat backup
                    Files.copy(existingDat.toPath(),
                            new FileOutputStream(new File(Settings.getBackupDatFile())));
                }
                catch (Exception ex) {
                    FileUtils.log("unable to back up script_code.dat: " + ex.getMessage());
                    FileUtils.closeAll();
                    System.exit(0);
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
                        doTheThing(progressDialog);
                        mainPanel.rerollRandomSeed();
                        return null;
                    }
                };
                swingWorker.execute();
                progressDialog.setVisible(true);
            } catch (Exception ex) {
                FileUtils.log("Error: " + ex.getMessage());
                ex.printStackTrace();
                throw ex;
            }
        }

        private void generateAndApply() {
            try {
                progressDialog.updateProgress(0, Translations.getText("setup.start"));
                generateSeed();

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
                FileUtils.log("unable to copy files to La-Mulana install");
                FileUtils.closeAll();
                System.exit(0);
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
                FileUtils.log("unable to restore files to La-Mulana install");
                FileUtils.closeAll();
                System.exit(0);
            }
        }
    }

    protected static void doTheThing(ProgressDialog dialog) {
        FileUtils.log(String.format("Shuffling items for seed %s", Settings.getStartingSeed()));

        Random random = new Random(Settings.getStartingSeed());
        Set<String> initiallyAccessibleItems = getInitiallyAvailableItems();
        Set<String> surfaceItems = getSurfaceItems();

        int attempt = 0;
        while(true) {
            ++attempt;
            dialog.updateProgress(20, String.format(Translations.getText("progress.shuffling"), attempt));
            dialog.setTitle(String.format(Translations.getText("progress.shuffling.title"), attempt));
            dialog.progressBar.setIndeterminate(true);

            ItemRandomizer itemRandomizer = new ItemRandomizer();
            ShopRandomizer shopRandomizer = buildShopRandomizer(itemRandomizer);
            AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer);

            itemRandomizer.placeNonRandomizedItems();
            shopRandomizer.placeNonRandomizedItems();
            if(ShopRandomizationEnum.EVERYTHING.equals(Settings.getShopRandomization())) {
                ((EverythingShopRandomizer)shopRandomizer).placeGuaranteedWeights(random);
            }
            if(!surfaceItems.isEmpty()) {
                itemRandomizer.placeVeryEarlyItems(new ArrayList<>(surfaceItems), random);
            }
            shopRandomizer.determineItemTypes(random);
            if(!itemRandomizer.placeRequiredItems(new ArrayList<>(initiallyAccessibleItems), random)) {
                continue;
            }
            if(Settings.isRandomizeCoinChests()) {
                if(!itemRandomizer.placeCoinChests(random)) {
                    continue;
                }
            }
            if(!itemRandomizer.placeAllItems(random)) {
                continue;
            }

            boolean ankhJewelLock = false;
            accessChecker.initExitRequirements();
            accessChecker.computeAccessibleNodes("None");
            for(String enabledGlitch : Settings.getEnabledGlitches()) {
                accessChecker.computeAccessibleNodes("Setting: " + enabledGlitch);
            }
            for(String enabledDamageBoost : Settings.getEnabledDamageBoosts()) {
                accessChecker.computeAccessibleNodes("Boost: " + enabledDamageBoost);
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
                try {
                    dialog.progressBar.setIndeterminate(false);
                    dialog.updateProgress(80, String.format(Translations.getText("progress.shuffling.done"), attempt));

                    FileUtils.log(String.format("Successful attempt %s.", attempt));

                    if(Settings.isRandomizeForbiddenTreasure()) {
                        itemRandomizer.randomizeForbiddenTreasure(random);
                    }

                    dialog.updateProgress(85, Translations.getText("progress.spoiler"));
                    outputLocations(itemRandomizer, shopRandomizer, attempt);
                    if(!Settings.isFullItemAccess()) {
                        accessChecker.outputRemaining(Settings.getStartingSeed(), attempt);
                    }

                    dialog.updateProgress(90, Translations.getText("progress.read"));

                    List<Zone> rcdData = RcdReader.getRcdScriptInfo();
                    List<Block> datInfo = DatReader.getDatScriptInfo();

                    dialog.updateProgress(95, Translations.getText("progress.write"));
                    itemRandomizer.updateFiles();
                    shopRandomizer.updateFiles(datInfo, random);
                    if(Settings.isAutomaticHardmode()) {
                        GameDataTracker.addAutomaticHardmode();
                    }
//                    if(Settings.isRandomizeMantras()) {
//                        GameDataTracker.randomizeMantras(random);
//                    }
                    RcdWriter.writeRcd(rcdData);
                    DatWriter.writeDat(datInfo);

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

                    SwingUtilities.invokeLater(() -> {
                        try {
                            Thread.sleep(2000);
                            dialog.setVisible(false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    return;
                } catch (Exception ex) {
                    FileUtils.logException(ex);
                    return;
                }
            }
            try {
//                accessChecker.outputRemaining(Settings.getStartingSeed(), attempt);
            } catch (Exception ex) {
                FileUtils.logException(ex);
                return;
            }
        }
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

    private static Set<String> getSurfaceItems() {
        Set<String> surfaceItems = new HashSet<>(Settings.getSurfaceItems());
        surfaceItems.removeAll(DataFromFile.getNonRandomizedItems());
        return surfaceItems;
    }

    private static void outputLocations(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer, int attempt) throws IOException {
        itemRandomizer.outputLocations(attempt);
        shopRandomizer.outputLocations(attempt);
    }

    public static void addArgItemUI(Set<String> nonRandomizedItems, String input) {
        for(String item : DataFromFile.getAllItems()) {
            if(item.equals(input)) {
                nonRandomizedItems.add(item);
            }
        }
    }
}
