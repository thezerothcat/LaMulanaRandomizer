package lmr.randomizer;

import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.DatReader;
import lmr.randomizer.dat.DatWriter;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.random.*;
import lmr.randomizer.rcd.RcdReader;
import lmr.randomizer.rcd.RcdWriter;
import lmr.randomizer.rcd.object.Zone;
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
        }
        catch (Exception ex) {
            FileUtils.log("Unable to read settings: " + ex.getMessage());
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

    static class RandomizerUI extends JFrame implements ActionListener {
        private FieldPanel fieldPanel;
        private RadioPanel radioPanel;
        private RandomizationPanel randomizationPanel;
        private GlitchPanel glitchPanel;
        private ShopRandomizationRadio shopRandomization;
        private DifficultyPanel difficultyPanel;
        private ProgressDialog progressDialog;

        public RandomizerUI() {
            try {
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            setTitle("La-Mulana (Remake) Randomizer");
            setLayout(new MigLayout("fill, aligny top", "[]", "[]"));
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setResizable(true);

            fieldPanel = new FieldPanel();
            add(fieldPanel, "growx, wrap");
            fieldPanel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fieldPanel.updateTranslations();
                    radioPanel.updateTranslations();
                    randomizationPanel.updateTranslations();
                    shopRandomization.updateTranslations();
                }
            });

            radioPanel = new RadioPanel();
            add(radioPanel, "growx, wrap");

            JPanel advancedSettingsPanel = new JPanel(new MigLayout("fill, aligny top, wrap", "[]", "[]"));
            advancedSettingsPanel.setBorder(BorderFactory.createTitledBorder("Advanced Settings"));

            randomizationPanel = new RandomizationPanel();
            advancedSettingsPanel.add(randomizationPanel, "wrap");

            shopRandomization = new ShopRandomizationRadio();
            advancedSettingsPanel.add(shopRandomization, "wrap");
            add(advancedSettingsPanel, "growx, wrap");

            glitchPanel = new GlitchPanel();
            add(glitchPanel, "growx, wrap");

            difficultyPanel = new DifficultyPanel();
            add(difficultyPanel, "growx, aligny, wrap");

            progressDialog = new ProgressDialog(this);

            add(new ButtonPanel(this), "grow");
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
                    this.progressDialog.updateProgress(0, "Restoring files");
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
            fieldPanel.updateSettings();
            radioPanel.updateSettings();
            randomizationPanel.updateSettings();
            glitchPanel.updateSettings();
            shopRandomization.updateSettings();
            difficultyPanel.updateSettings();
            Settings.saveSettings();

            DataFromFile.clearAllData();

            fieldPanel.rerollRandomSeed();

            progressDialog.updateProgress(10, "Backing up game files");

            File rcdFile = new File("script.rcd.bak");
            if(!rcdFile.exists()) {
                File existingRcd = new File(Settings.getLaMulanaBaseDir(), "data/mapdata/script.rcd");
                if (!FileUtils.hashRcdFile(existingRcd)) {
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

            progressDialog.updateProgress(15, "Setting up output directory");

            File directory = new File(Long.toString(Settings.getStartingSeed()));
            directory.mkdir();


            try {
                Frame f = this;
                SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        progressDialog.setLocationRelativeTo(f);
                        doTheThing(progressDialog);

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
                progressDialog.updateProgress(0, "Setting up randomizer");
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
                progressDialog.updateProgress(0, "Restoring script.rcd");

                FileOutputStream fileOutputStream = new FileOutputStream(new File(Settings.getLaMulanaBaseDir() + "/data/mapdata/script.rcd"));
                Files.copy(new File("script.rcd.bak").toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                progressDialog.updateProgress(50, "Restoring script_code.dat");

                fileOutputStream = new FileOutputStream(new File(String.format("%s/data/language/%s/script_code.dat",
                        Settings.getLaMulanaBaseDir(), Settings.getLanguage())));
                Files.copy(new File(Settings.getBackupDatFile()).toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                progressDialog.updateProgress(100, "Files restored!");

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

    static class ButtonPanel extends JPanel {
        public ButtonPanel(RandomizerUI randomizerUI) {
            super(new FlowLayout());

//            JButton generateButton = new JButton("Generate");
//            generateButton.addActionListener(randomizerUI);
//            generateButton.setActionCommand("generate");
//            add(generateButton);

            JButton applyButton = new JButton("Apply");
            applyButton.addActionListener(randomizerUI);
            applyButton.setActionCommand("apply");
            add(applyButton);

            JButton restoreButton = new JButton("Restore");
            restoreButton.addActionListener(randomizerUI);
            restoreButton.setActionCommand("restore");
            add(restoreButton);
        }
    }

    static class FieldPanel extends JPanel {
        private JTextField laMulanaDirectory;
        private JTextField seedNumber;
        private JComboBox language;

        public FieldPanel() {
            super(new MigLayout("fillx", "[][sg fields, fill, grow 80]", "[]"));
            setBorder(BorderFactory.createTitledBorder("Game Settings"));

            seedNumber = new JTextField(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
            add(new JLabel("Seed number"), "gap related");
            add(seedNumber);

            language = new JComboBox(new String[]{"English", "日本語"});
            language.setSelectedIndex("en".equals(Settings.getLanguage()) ? 0 : 1);
            add(language, "grow 50, wrap");

            laMulanaDirectory = new JTextField(Settings.getLaMulanaBaseDir());
            add(new JLabel("La-Mulana install directory "), "gap related");
            add(laMulanaDirectory, "span 2, grow 100");
        }

        public void addActionListener(ActionListener actionListener) {
            language.addActionListener(actionListener);
        }

        public void rerollRandomSeed() {
            seedNumber.setText(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
        }

        public void updateTranslations() {
            Settings.setLanguage(language.getSelectedIndex() == 0 ? "en" : "jp", true);
        }

        public void updateSettings() {
            try {
                Settings.setStartingSeed(Integer.parseInt(seedNumber.getText()));
                Settings.setLaMulanaBaseDir(laMulanaDirectory.getText(), true);
                Settings.setLanguage(language.getSelectedIndex() == 0 ? "en" : "jp", true);
            }
            catch (Exception ex) {
                FileUtils.log("unable to save edit for seedNumber");
            }
        }
    }

    static class GameItemRadio extends JPanel {
        private ButtonGroup itemRandomization;
        private String itemName;
        private JLabel itemLabel;
        private JRadioButton randomItem;
        private JRadioButton nonrandomItem;

        public GameItemRadio(String item) {
            super(new GridLayout(0, 1));

            itemLabel = new JLabel(getText(item), JLabel.LEFT);
            add(itemLabel);

            itemRandomization = new ButtonGroup();
            randomItem = new JRadioButton("jp".equals(Settings.getLanguage()) ? "ランダム" : "Random");
            randomItem.setActionCommand("RANDOM");
            JRadioButton initialItem = new JRadioButton("Initially Accessible");
            initialItem.setActionCommand("INITIAL");
            nonrandomItem = new JRadioButton("jp".equals(Settings.getLanguage()) ? "元の場所" : "Original Location");
            nonrandomItem.setActionCommand("NONRANDOM");
            itemRandomization.add(randomItem);
            itemRandomization.add(initialItem);
            itemRandomization.add(nonrandomItem);

            add(randomItem);
            add(initialItem);
            add(nonrandomItem);

            if(Settings.getInitiallyAvailableItems().contains(item)) {
                initialItem.setSelected(true);
            }
            else if(Settings.getNonRandomizedItems().contains(item)) {
                nonrandomItem.setSelected(true);
            }
            else {
                randomItem.setSelected(true);
            }

            this.itemName = item;
        }

        public String getActionCommand() {
            return itemRandomization.getSelection().getActionCommand();
        }

        public String getItemName() {
            return itemName;
        }
        public void updateText() {
            itemLabel.setText(getText(itemName));
            randomItem.setText("jp".equals(Settings.getLanguage()) ? "ランダム" : "Random");
            nonrandomItem.setText("jp".equals(Settings.getLanguage()) ? "元の場所" : "Original Location");
        }
    }

    static String getText(String itemName) {
        if(!"jp".equals(Settings.getLanguage())) {
            return itemName + ":";
        }

        if("Holy Grail".equals(itemName)) {
            return "聖杯";
        }
        if("mirai.exe".equals(itemName)) {
            return "こちら未来開発宇宙支部";
        }
        if("Hermes' Boots".equals(itemName)) {
            return "ヘルメスの靴";
        }
        if("Feather".equals(itemName)) {
            return "羽";
        }
        if("Grapple Claw".equals(itemName)) {
            return "かぎ爪";
        }
        if("Hand Scanner".equals(itemName)) {
            return "ハンディスキャナ";
        }
        if("reader.exe".equals(itemName)) {
            return "古文書リーダー";
        }
        if("Isis' Pendant".equals(itemName)) {
            return "イシスのペンダント";
        }
        if("Bronze Mirror".equals(itemName)) {
            return "銅鏡";
        }
        if("xmailer.exe".equals(itemName)) {
            return "ｘｅｌｐｕｄ　ｍａｉｌｅｒ";
        }
        return "???";
    }

    static class RandomizationPanel extends JPanel {
        private JCheckBox requireSoftwareComboForKeyFairy;
        private JCheckBox requireIceCapeForLava;
        private JCheckBox requireFlaresForExtinction;

        private JCheckBox randomizeCoinChests;
        private JCheckBox randomizeForbiddenTreasure;
//        private JCheckBox replaceMapsWithWeights;

        private JCheckBox automaticHardmode;
        private JCheckBox fullItemAccess;
        private JCheckBox enableDamageBoostRequirements;

        public RandomizationPanel() {
            super(new MigLayout("wrap 2", "[sizegroup checkboxes]", "[]2[]"));

            requireIceCapeForLava = new JCheckBox();
            requireIceCapeForLava.setSelected(Settings.isRequireIceCapeForLava());
            add(requireIceCapeForLava);

            requireFlaresForExtinction = new JCheckBox();
            requireFlaresForExtinction.setSelected(Settings.isRequireFlaresForExtinction());
            add(requireFlaresForExtinction);

            requireSoftwareComboForKeyFairy = new JCheckBox();
            requireSoftwareComboForKeyFairy.setSelected(Settings.isRequireSoftwareComboForKeyFairy());
            add(requireSoftwareComboForKeyFairy);

            randomizeForbiddenTreasure = new JCheckBox();
            randomizeForbiddenTreasure.setSelected(Settings.isRandomizeForbiddenTreasure());
            add(randomizeForbiddenTreasure);

            randomizeCoinChests = new JCheckBox();
            randomizeCoinChests.setSelected(Settings.isRandomizeCoinChests());
            add(randomizeCoinChests);

//            replaceMapsWithWeights = new JCheckBox();
//            replaceMapsWithWeights.setSelected(Settings.isReplaceMapsWithWeights());
//            add(replaceMapsWithWeights);

            fullItemAccess = new JCheckBox();
            fullItemAccess.setSelected(Settings.isFullItemAccess());
            add(fullItemAccess);

            enableDamageBoostRequirements = new JCheckBox();
            enableDamageBoostRequirements.setSelected(Settings.isEnableDamageBoostRequirements());
            add(enableDamageBoostRequirements);

            automaticHardmode = new JCheckBox();
            automaticHardmode.setSelected(Settings.isAutomaticHardmode());
            add(automaticHardmode);

            updateTranslations();
        }

        public void updateTranslations() {
            if("jp".equals(Settings.getLanguage())) {
                requireIceCapeForLava.setText("Require 氷のマント for swimming through lava");
                requireFlaresForExtinction.setText("Require 発弾筒 for 死滅の碑");
                requireSoftwareComboForKeyFairy.setText("Key Fairy chests/doors expect ミラクルウィッチ and めくり番長");
                randomizeForbiddenTreasure.setText("Replace random non-Shrine 地図 with あぶねぇ水着");
                randomizeCoinChests.setText("Randomize coin chests");
                fullItemAccess.setText("All items must be accessible (100% seed)");
                enableDamageBoostRequirements.setText("Allow damage-boosting requirements");
                automaticHardmode.setText("ＨＡＲＤモード activates automatically at the start"); // 開始
            }
            else {
                requireIceCapeForLava.setText("Require Ice Cape for swimming through lava");
                requireFlaresForExtinction.setText("Require Flare Gun for Chamber of Extinction");
                requireSoftwareComboForKeyFairy.setText("Key Fairy chests/doors expect miracle + mekuri");
                randomizeForbiddenTreasure.setText("Replace random non-Shrine map with Provocative Bathing Suit");
                randomizeCoinChests.setText("Randomize coin chests");
                fullItemAccess.setText("All items must be accessible (100% seed)");
                enableDamageBoostRequirements.setText("Allow damage-boosting requirements");
                automaticHardmode.setText("Hard Mode activates automatically at the start");
            }
        }

        public void updateSettings() {
            Settings.setAutomaticHardmode(automaticHardmode.isSelected(), true);
            Settings.setFullItemAccess(fullItemAccess.isSelected(), true);
            Settings.setEnableDamageBoostRequirements(enableDamageBoostRequirements.isSelected(), true);
            Settings.setRequireIceCapeForLava(requireIceCapeForLava.isSelected(), true);
            Settings.setRequireFlaresForExtinction(requireFlaresForExtinction.isSelected(), true);
            Settings.setRequireSoftwareComboForKeyFairy(requireSoftwareComboForKeyFairy.isSelected(), true);
            Settings.setRandomizeForbiddenTreasure(randomizeForbiddenTreasure.isSelected(), true);
            Settings.setRandomizeCoinChests(randomizeCoinChests.isSelected(), true);
        }
    }

    static class GlitchPanel extends JPanel {
        private List<JCheckBox> glitchOptions = new ArrayList<>();

        public GlitchPanel() {
            super(new MigLayout("fillx, wrap 6", "[sizegroup checkboxes]", "[]4[]"));
            setBorder(BorderFactory.createTitledBorder("Glitch Settings"));

            for(String availableGlitch : DataFromFile.getAvailableGlitches()) {
                JCheckBox glitchCheckbox = new JCheckBox(availableGlitch);
                glitchCheckbox.setSelected(Settings.getEnabledGlitches().contains(availableGlitch));
                glitchCheckbox.setActionCommand(availableGlitch);
                glitchOptions.add(glitchCheckbox);
                add(glitchCheckbox);
            }
        }

        public void updateSettings() {
            List<String> enabledGlitches = new ArrayList<>();
            for(JCheckBox glitchOption : glitchOptions) {
                if(glitchOption.isSelected()) {
                    enabledGlitches.add(glitchOption.getActionCommand());
                }
            }
            Settings.setEnabledGlitches(enabledGlitches, true);
        }
    }

    static class RadioPanel extends JPanel {
        List<GameItemRadio> itemConfigRadioGroupPanels;

        public RadioPanel() {
            super(new GridLayout(0, 5, 0, 15));
            setBorder(BorderFactory.createTitledBorder("Item Settings"));

            itemConfigRadioGroupPanels = new ArrayList<>();
            itemConfigRadioGroupPanels.add(new GameItemRadio("Holy Grail"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("mirai.exe"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Hermes' Boots"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Feather"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Grapple Claw"));

            itemConfigRadioGroupPanels.add(new GameItemRadio("Hand Scanner"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("reader.exe"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Isis' Pendant"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Bronze Mirror"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("xmailer.exe"));

            for(GameItemRadio gameItemRadio : itemConfigRadioGroupPanels) {
                add(gameItemRadio, LEFT_ALIGNMENT);
            }
        }

        public void updateTranslations() {
            for(GameItemRadio gameItemRadio : itemConfigRadioGroupPanels) {
                gameItemRadio.updateText();
            }
        }

        public void updateSettings() {
            Set<String> initiallyAvailableItems = new HashSet<>();
            Set<String> nonRandomizedItems = new HashSet<>();

            for(GameItemRadio itemRadio : itemConfigRadioGroupPanels) {
                String actionCommand = itemRadio.getActionCommand();
                if("INITIAL".equals(actionCommand)) {
                    addArgItemUI(initiallyAvailableItems, itemRadio.getItemName());
                }
                else if("NONRANDOM".equals(actionCommand)) {
                    addArgItemUI(nonRandomizedItems, itemRadio.getItemName());
                }
            }
            Settings.setInitiallyAvailableItems(initiallyAvailableItems, true);
            Settings.setNonRandomizedItems(nonRandomizedItems, true);
        }
    }

    static class ShopRandomizationRadio extends JPanel {
        private ButtonGroup shopRandomization;
        private JLabel shopRandomizationLabel;

        public ShopRandomizationRadio() {
            super(new MigLayout("fillx"));

            shopRandomizationLabel = new JLabel("jp".equals(Settings.getLanguage()) ? "店 Randomization:" : "Shop Randomization:", JLabel.LEFT);
            add(shopRandomizationLabel);

            shopRandomization = new ButtonGroup();

            JRadioButton shopNoRandomization = new JRadioButton("None");
            shopNoRandomization.setActionCommand("NONE");
            shopRandomization.add(shopNoRandomization);

            JRadioButton shopCategorizedRandomization = new JRadioButton("Unique items only");
            shopCategorizedRandomization.setActionCommand("CATEGORIZED");
            shopRandomization.add(shopCategorizedRandomization);

            JRadioButton shopEverythingRandomization = new JRadioButton("All items");
            shopEverythingRandomization.setActionCommand("EVERYTHING");
            shopRandomization.add(shopEverythingRandomization);

            add(shopNoRandomization);
            add(shopCategorizedRandomization);
            add(shopEverythingRandomization);

            if(ShopRandomizationEnum.NONE.equals(Settings.getShopRandomization())) {
                shopNoRandomization.setSelected(true);
            }
            else if(ShopRandomizationEnum.CATEGORIZED.equals(Settings.getShopRandomization())) {
                shopCategorizedRandomization.setSelected(true);
            }
            else {
                shopEverythingRandomization.setSelected(true);
            }
        }

        public void updateSettings() {
            Settings.setShopRandomization(shopRandomization.getSelection().getActionCommand(), true);
        }

        public void updateTranslations() {
            shopRandomizationLabel.setText("jp".equals(Settings.getLanguage()) ? "店 Randomization:" : "Shop Randomization:");
        }
    }

    static class DifficultyPanel extends JPanel {
        private ButtonGroup difficultySetting;

        public DifficultyPanel() {
            super(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(new JLabel("Boss Difficulty: ", JLabel.LEFT));

            difficultySetting = new ButtonGroup();
//            JRadioButton easy = new JRadioButton("Easy");
//            easy.setActionCommand("EASY");
            JRadioButton medium = new JRadioButton("Lower");
            medium.setActionCommand("MEDIUM");
            JRadioButton hard = new JRadioButton("Higher");
            hard.setActionCommand("HARD");
//            difficultySetting.add(easy);
            difficultySetting.add(medium);
            difficultySetting.add(hard);

//            add(easy);
            add(medium);
            add(hard);

            if(BossDifficulty.EASY.equals(Settings.getBossDifficulty())) {
                medium.setSelected(true); // todo: easy is not a setting yet
            }
            else if(BossDifficulty.MEDIUM.equals(Settings.getBossDifficulty())) {
                medium.setSelected(true);
            }
            else {
                hard.setSelected(true);
            }
        }

        public void updateSettings() {
            Settings.setBossDifficulty(difficultySetting.getSelection().getActionCommand(), true);
        }
    }

    static class ProgressDialog extends JDialog {
        JProgressBar progressBar;
        JLabel statusText;

        public ProgressDialog(Frame owner) {
            super(owner, "Progress", true);
            setLayout(new MigLayout("wrap 1", "", "align center"));
            setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            //setSize(400, 100);

            progressBar = new JProgressBar(0,100);
            statusText = new JLabel("Generating seed...");

            add(statusText, "growx, width 300!");
            add(progressBar, "growx, height 1.5*pref");
            pack();
        }

        public void updateProgress(int percentage, String progressText) {
             statusText.setText(progressText);
             progressBar.setValue(percentage);
        }
    }

    private static void doTheThing(ProgressDialog dialog) {
        Random random = new Random(Settings.getStartingSeed());
        Set<String> initiallyAvailableItems = getInitiallyAvailableItems();

        int attempt = 0;
        while(true) {
            ++attempt;
            dialog.updateProgress(20, "Shuffling items for attempt #" + attempt);
            dialog.setTitle("Progress after attempt #"+ attempt);
            dialog.progressBar.setIndeterminate(true);

            ItemRandomizer itemRandomizer = new ItemRandomizer();
            ShopRandomizer shopRandomizer = buildShopRandomizer(itemRandomizer);
            AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer);

            itemRandomizer.placeNonRandomizedItems();
            shopRandomizer.placeNonRandomizedItems();
            shopRandomizer.determineItemTypes(random);
            if(!itemRandomizer.placeRequiredItems(new ArrayList<>(initiallyAvailableItems), random)) {
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
                    dialog.updateProgress(80, "Shuffling done after #" + attempt + " attempts");

                    FileUtils.log(String.format("Successful attempt %s.", attempt));

                    if(Settings.isRandomizeForbiddenTreasure()) {
                        itemRandomizer.randomizeForbiddenTreasure(random);
                    }

                    dialog.updateProgress(85, "Writing spoiler logs");
                    outputLocations(itemRandomizer, shopRandomizer, attempt);
                    if(!Settings.isFullItemAccess()) {
                        accessChecker.outputRemaining(Settings.getStartingSeed(), attempt);
                    }

                    dialog.updateProgress(90, "Reading non-randomized game files");

                    List<Zone> rcdData = RcdReader.getRcdScriptInfo();
                    List<Block> datInfo = DatReader.getDatScriptInfo();

                    dialog.updateProgress(95, "Writing files to game directory");
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

                    dialog.updateProgress(100, "Done!");
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
        if(ShopRandomizationEnum.NONE.equals(Settings.getShopRandomization())) {
            shopRandomizer = new StaticShopRandomizer(itemRandomizer.getTotalShopItems());
        }
        else if(ShopRandomizationEnum.CATEGORIZED.equals(Settings.getShopRandomization())) {
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
        Set<String> noRequirementItems = new HashSet<>(Settings.getInitiallyAvailableItems());
        noRequirementItems.removeAll(DataFromFile.getNonRandomizedItems());
        return noRequirementItems;
    }

    private static void outputLocations(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer, int attempt) throws IOException {
        itemRandomizer.outputLocations(attempt);
        shopRandomizer.outputLocations(attempt);
    }

    private static void addArgItemUI(Set<String> nonRandomizedItems, String input) {
        for(String item : DataFromFile.getAllItems()) {
            if(item.equals(input)) {
                nonRandomizedItems.add(item);
            }
        }
    }
}
