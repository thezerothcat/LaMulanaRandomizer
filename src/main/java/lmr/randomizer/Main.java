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

    static class RandomizerUI extends JFrame implements ActionListener {
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

            tabbedPanel = new TabbedPanel();
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

            mainPanel.rerollRandomSeed();

            progressDialog.updateProgress(10, Translations.getText("setup.backup"));

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

    static class TabbedPanel extends JTabbedPane {
        private RandomizationPanel randomizationPanel;
        private LogicPanel logicPanel;
        private ChallengePanel challengePanel;
        private DboostPanel dboostPanel;
        private GlitchPanel glitchPanel;

        public TabbedPanel() {
            randomizationPanel = new RandomizationPanel();
            addTab(Translations.getText("settings.randomization"), randomizationPanel);

            logicPanel = new LogicPanel();
            addTab(Translations.getText("settings.logic"), logicPanel);

            challengePanel = new ChallengePanel();
            addTab(Translations.getText("settings.challenge"), challengePanel);

//            dboostPanel = new DboostPanel();
//            addTab(Translations.getText("settings.dboost"), dboostPanel);

            glitchPanel = new GlitchPanel();
            addTab(Translations.getText("settings.glitches"), glitchPanel);
        }

        public void updateSettings() {
            randomizationPanel.updateSettings();
            glitchPanel.updateSettings();
            logicPanel.updateSettings();
            challengePanel.updateSettings();
        }

        public void updateTranslations() {
            randomizationPanel.updateTranslations();
//            glitchPanel.updateTranslations();
            logicPanel.updateTranslations();
            challengePanel.updateTranslations();
        }
    }

    static class ButtonPanel extends JPanel {
        JButton applyButton;
        JButton restoreButton;

        public ButtonPanel(RandomizerUI randomizerUI) {
            super(new FlowLayout());

            applyButton = new JButton(Translations.getText("button.apply"));
            applyButton.addActionListener(randomizerUI);
            applyButton.setActionCommand("apply");
            add(applyButton);

            restoreButton = new JButton(Translations.getText("button.restore"));
            restoreButton.addActionListener(randomizerUI);
            restoreButton.setActionCommand("restore");
            add(restoreButton);
        }

        public void updateTranslations() {
            applyButton.setText(Translations.getText("button.apply"));
            restoreButton.setText(Translations.getText("button.restore"));
        }
    }

    static class MainPanel extends JPanel {
        private JTextField laMulanaDirectory;
        private JTextField seedNumber;
        private JComboBox language;

        public MainPanel() {
            super(new MigLayout("fillx", "[][sg fields, fill, grow 80]", "[]"));
            setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.main")));

            seedNumber = new JTextField(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
            add(new JLabel(Translations.getText("settings.seed")), "gap related");
            add(seedNumber);

            language = new JComboBox(new String[]{"English", "日本語"});
            language.setSelectedIndex("en".equals(Settings.getLanguage()) ? 0 : 1);
            add(language, "grow 50, wrap");

            laMulanaDirectory = new JTextField(Settings.getLaMulanaBaseDir());
            add(new JLabel(Translations.getText("settings.dir")), "gap related");
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
            seedNumber.setText(Translations.getText("settings.seed"));
            laMulanaDirectory.setText(Translations.getText("settings.dir"));
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
        private JRadioButton initialItem;
        private JRadioButton nonrandomItem;

        public GameItemRadio(String item) {
            super(new GridLayout(0, 1));

            itemLabel = new JLabel(getItemText(item), JLabel.LEFT);
            add(itemLabel);

            itemRandomization = new ButtonGroup();
            randomItem = new JRadioButton(Translations.getText("randomization.random"));
            randomItem.setActionCommand("RANDOM");
            initialItem = new JRadioButton(Translations.getText("randomization.initial"));
            initialItem.setActionCommand("INITIAL");
            nonrandomItem = new JRadioButton(Translations.getText("randomization.nonrandom"));
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

        public void updateTranslations() {
            itemLabel.setText(getItemText(itemName));
            randomItem.setText(Translations.getText("randomization.random"));
            initialItem.setText(Translations.getText("randomization.initial"));
            nonrandomItem.setText(Translations.getText("randomization.nonrandom"));
        }
    }

    static String getItemText(String itemName) {
        return Translations.getText("items." + itemName.replaceAll("[ ']", ""));
    }

    static class LogicPanel extends JPanel {
        private JCheckBox requireSoftwareComboForKeyFairy;
        private JCheckBox requireIceCapeForLava;
        private JCheckBox requireFlaresForExtinction;
        private JCheckBox enableDamageBoostRequirements;

        public LogicPanel() {
            super(new MigLayout("fillx, wrap"));

            requireIceCapeForLava = new JCheckBox();
            requireIceCapeForLava.setSelected(Settings.isRequireIceCapeForLava());

            requireFlaresForExtinction = new JCheckBox();
            requireFlaresForExtinction.setSelected(Settings.isRequireFlaresForExtinction());

            requireSoftwareComboForKeyFairy = new JCheckBox();
            requireSoftwareComboForKeyFairy.setSelected(Settings.isRequireSoftwareComboForKeyFairy());

            enableDamageBoostRequirements = new JCheckBox();
            enableDamageBoostRequirements.setSelected(Settings.isEnableDamageBoostRequirements());

            CheckboxContainer checkboxContainer = new CheckboxContainer(2);
            checkboxContainer.add(requireIceCapeForLava);
            checkboxContainer.add(requireFlaresForExtinction);
            checkboxContainer.add(requireSoftwareComboForKeyFairy);
            checkboxContainer.add(enableDamageBoostRequirements);
            add(checkboxContainer, "growx, wrap");

            updateTranslations();
        }

        public void updateTranslations() {
            requireIceCapeForLava.setText(Translations.getText("logic.requireIceCapeForLava"));
            requireFlaresForExtinction.setText(Translations.getText("logic.requireFlaresForExtinction"));
            requireSoftwareComboForKeyFairy.setText(Translations.getText("logic.requireSoftwareComboForKeyFairy"));
            enableDamageBoostRequirements.setText(Translations.getText("logic.enableDamageBoostRequirements"));
        }

        public void updateSettings() {
            Settings.setEnableDamageBoostRequirements(enableDamageBoostRequirements.isSelected(), true);
            Settings.setRequireIceCapeForLava(requireIceCapeForLava.isSelected(), true);
            Settings.setRequireFlaresForExtinction(requireFlaresForExtinction.isSelected(), true);
            Settings.setRequireSoftwareComboForKeyFairy(requireSoftwareComboForKeyFairy.isSelected(), true);
        }
    }

    static class ChallengePanel extends JPanel {
        private JCheckBox automaticHardmode;
        private JCheckBox excludedItems;

        private DifficultyPanel difficultyPanel;

        public ChallengePanel() {
            super(new MigLayout("fillx, wrap"));

            excludedItems = new JCheckBox();
            excludedItems.setSelected(!Settings.isFullItemAccess());

            automaticHardmode = new JCheckBox();
            automaticHardmode.setSelected(Settings.isAutomaticHardmode());

            CheckboxContainer checkboxContainer = new CheckboxContainer(2);
            checkboxContainer.add(excludedItems);
            checkboxContainer.add(automaticHardmode);
            add(checkboxContainer, "growx, wrap");

            difficultyPanel = new DifficultyPanel();
            add(difficultyPanel, "growx, aligny, wrap");

            updateTranslations();
        }

        public void updateTranslations() {
            excludedItems.setText(Translations.getText("challenge.excludedItems"));
            automaticHardmode.setText(Translations.getText("challenge.automaticHardmode"));
            difficultyPanel.updateTranslations();
        }

        public void updateSettings() {
            Settings.setAutomaticHardmode(automaticHardmode.isSelected(), true);
            Settings.setFullItemAccess(!excludedItems.isSelected(), true);
            difficultyPanel.updateSettings();
        }
    }

    static class RandomizationPanel extends JPanel {
        private RadioPanel radioPanel;

        private JCheckBox randomizeCoinChests;
        private JCheckBox randomizeForbiddenTreasure;
        private JCheckBox replaceMapsWithWeights;

        private ShopRandomizationRadio shopRandomization;

        public RandomizationPanel() {
            super(new MigLayout("fillx, wrap"));

            radioPanel = new RadioPanel();
            add(radioPanel, "growx");

            randomizeCoinChests = new JCheckBox();
            randomizeCoinChests.setSelected(Settings.isRandomizeCoinChests());

            randomizeForbiddenTreasure = new JCheckBox();
            randomizeForbiddenTreasure.setSelected(Settings.isRandomizeForbiddenTreasure());

            replaceMapsWithWeights = new JCheckBox();
            replaceMapsWithWeights.setSelected(Settings.isReplaceMapsWithWeights());

            CheckboxContainer checkboxContainer = new CheckboxContainer(1);
            checkboxContainer.add(randomizeCoinChests);
            checkboxContainer.add(randomizeForbiddenTreasure);
//            checkboxContainer.add(replaceMapsWithWeights);
            add(checkboxContainer, "growx, wrap");

            shopRandomization = new ShopRandomizationRadio();
            add(shopRandomization, "growx, aligny, wrap");

            updateTranslations();
        }

        public void updateTranslations() {
            radioPanel.updateTranslations();
            randomizeForbiddenTreasure.setText(Translations.getText("randomization.randomizeForbiddenTreasure"));
            randomizeCoinChests.setText(Translations.getText("randomization.randomizeCoinChests"));
            replaceMapsWithWeights.setText(Translations.getText("randomization.replaceMapsWithWeights"));
            shopRandomization.updateTranslations();
        }

        public void updateSettings() {
            radioPanel.updateSettings();
            Settings.setRandomizeForbiddenTreasure(randomizeForbiddenTreasure.isSelected(), true);
            Settings.setRandomizeCoinChests(randomizeCoinChests.isSelected(), true);
            shopRandomization.updateSettings();
        }
    }

    static class CheckboxContainer extends JPanel {
        public CheckboxContainer(int checkboxesPerRow) {
            super(new MigLayout(String.format("wrap %d", checkboxesPerRow),
                    "[sizegroup checkboxes]",
                    String.format("[]%d[]", checkboxesPerRow)));
        }

        public void add(JCheckBox jCheckBox) {
            super.add(jCheckBox);
        }
    }

    static class DboostPanel extends JPanel {
        private JCheckBox itemBased;
        private JCheckBox easyEnemyBased;

        public DboostPanel() {
            super(new MigLayout("fillx, wrap 2", "[sizegroup checkboxes]", "[]2[]"));

            itemBased = new JCheckBox(Translations.getText("dboost.item"));
            itemBased.setSelected(Settings.isEnableDamageBoostRequirements());
            itemBased.setActionCommand("item");

            easyEnemyBased = new JCheckBox(Translations.getText("dboost.enemy.easy"));
            easyEnemyBased.setSelected(Settings.isEnableDamageBoostRequirements());
            easyEnemyBased.setActionCommand("easyenemy");

            CheckboxContainer checkboxContainer = new CheckboxContainer(2);
            checkboxContainer.add(itemBased);
            checkboxContainer.add(easyEnemyBased);
            add(checkboxContainer, "growx, wrap");
        }

        public void updateSettings() {

        }
    }

    static class GlitchPanel extends JPanel {
        private List<JCheckBox> glitchOptions = new ArrayList<>();

        public GlitchPanel() {
            super(new MigLayout("fillx, wrap 4", "[sizegroup checkboxes]", "[]4[]"));

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
            setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.randomization.items")));

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
                gameItemRadio.updateTranslations();
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
//            super(new MigLayout("growx"));
            super(new FlowLayout(FlowLayout.LEFT));

            shopRandomizationLabel = new JLabel(Translations.getText("randomization.randomizeShops"), JLabel.LEFT);
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
            shopRandomizationLabel.setText(Translations.getText("randomization.randomizeShops"));
        }
    }

    static class DifficultyPanel extends JPanel {
        private ButtonGroup difficultySetting;
        private JLabel label;
        private JRadioButton medium;
        private JRadioButton hard;

        public DifficultyPanel() {
            super(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            label = new JLabel(Translations.getText("logic.bossDifficulty"), JLabel.LEFT);
            add(label);

            difficultySetting = new ButtonGroup();
//            JRadioButton easy = new JRadioButton("Easy");
//            easy.setActionCommand("EASY");
            medium = new JRadioButton(Translations.getText("logic.bossDifficulty.lower"));
            medium.setActionCommand("MEDIUM");
            hard = new JRadioButton(Translations.getText("logic.bossDifficulty.higher"));
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

        public void updateTranslations() {
            label.setText(Translations.getText("logic.bossDifficulty"));
            medium.setText(Translations.getText("logic.bossDifficulty.lower"));
            hard.setText(Translations.getText("logic.bossDifficulty.higher"));
        }
    }

    static class ProgressDialog extends JDialog {
        JProgressBar progressBar;
        JLabel statusText;

        public ProgressDialog(Frame owner) {
            super(owner, Translations.getText("progress"), true);
            setLayout(new MigLayout("wrap 1", "", "align center"));
            setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            //setSize(400, 100);

            progressBar = new JProgressBar(0,100);
            statusText = new JLabel("");

            add(statusText, "growx, width 300!");
            add(progressBar, "growx, height 1.5*pref");
            pack();
        }

        public void updateProgress(int percentage, String progressTextKey) {
             statusText.setText(Translations.getText(progressTextKey));
             progressBar.setValue(percentage);
        }

        public void updateTranslations() {
            setTitle(Translations.getText("progress"));
        }
    }

    private static void doTheThing(ProgressDialog dialog) {
        Random random = new Random(Settings.getStartingSeed());
        Set<String> initiallyAvailableItems = getInitiallyAvailableItems();

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
