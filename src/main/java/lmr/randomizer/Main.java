package lmr.randomizer;

import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.DatReader;
import lmr.randomizer.dat.DatWriter;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.random.*;
import lmr.randomizer.rcd.RcdReader;
import lmr.randomizer.rcd.RcdWriter;
import lmr.randomizer.rcd.object.Zone;
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
        if(false) {
//            Settings.startingSeed = 1246445508;
            Settings.getNonRandomizedItems().add("Holy Grail");
//            parseSettings(args);
            Settings.setLaMulanaBaseDir("C:\\GOG Games\\La-Mulana", true);
            File directory = new File(Long.toString(Settings.getStartingSeed()));
            directory.mkdir();
            try {
                doTheThing();
            } catch (Exception ex) {
                FileUtils.log("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
            FileUtils.closeAll();
        }
        else {
            try {
                FileUtils.readSettings();
            }
            catch (Exception ex) {
                FileUtils.log("Unable to read settings: " + ex.getMessage());
            }
            SwingUtilities.invokeLater(new RandomizerRunnable());
        }
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
        private CheckboxPanel checkboxPanel;
        private GlitchPanel glitchPanel;
        private ShopRandomizationRadio shopRandomization;
//        private DifficultyPanel difficultyPanel;

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

            radioPanel = new RadioPanel();
            add(radioPanel, "growx, wrap");

            JPanel advancedSettingsPanel = new JPanel(new MigLayout("fill, aligny top, wrap", "[]", "[]"));
            advancedSettingsPanel.setBorder(BorderFactory.createTitledBorder("Advanced Settings"));

            checkboxPanel = new CheckboxPanel();
            advancedSettingsPanel.add(checkboxPanel, "wrap");

            shopRandomization = new ShopRandomizationRadio();
            advancedSettingsPanel.add(shopRandomization, "wrap");
            add(advancedSettingsPanel, "growx, wrap");

            glitchPanel = new GlitchPanel();
            add(glitchPanel, "growx, wrap");

//            difficultyPanel = new DifficultyPanel();
//            add(difficultyPanel, "growx, aligny, wrap");

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
                restore();
            }
        }

        private void generateSeed() {
            fieldPanel.updateSettings();
            radioPanel.updateSettings();
            checkboxPanel.updateSettings();
            glitchPanel.updateSettings();
            shopRandomization.updateSettings();
//            difficultyPanel.updateSettings();
            Settings.saveSettings();

            DataFromFile.clearAllData();

            fieldPanel.rerollRandomSeed();

            File rcdFile = new File("script.rcd.bak");
            if(!rcdFile.exists()) {
                File existingRcd = new File(Settings.getLaMulanaBaseDir(), "data/mapdata/script.rcd");
                if(!FileUtils.hashRcdFile(existingRcd)) {
                    FileUtils.log("unable to back up script.rcd - file already modified");
                    FileUtils.closeAll();
                    System.exit(0);
                }

                try {
                    // Make script.rcd backup
                    Files.copy(existingRcd.toPath(),
                            new FileOutputStream(new File("script.rcd.bak")));
                }
                catch (Exception ex) {
                    FileUtils.log("unable to back up script.rcd: " + ex.getMessage());
                    FileUtils.closeAll();
                    System.exit(0);
                }
            }
            File datFile = new File("script_code.dat.bak");
            if(!datFile.exists()) {
                File existingDat = new File(Settings.getLaMulanaBaseDir(), "data/language/en/script_code.dat");
                if(!FileUtils.hashDatFile(existingDat)) {
                    FileUtils.log("unable to back up script_code.dat - file already modified");
                    FileUtils.closeAll();
                    System.exit(0);
                }

                try {
                    // Make script_code.dat backup
                    Files.copy(existingDat.toPath(),
                            new FileOutputStream(new File("script_code.dat.bak")));
                }
                catch (Exception ex) {
                    FileUtils.log("unable to back up script_code.dat: " + ex.getMessage());
                    FileUtils.closeAll();
                    System.exit(0);
                }
            }

            File directory = new File(Long.toString(Settings.getStartingSeed()));
            directory.mkdir();


            try {
                doTheThing();
            } catch (Exception ex) {
                FileUtils.log("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
            FileUtils.closeAll();
        }

        private void generateAndApply() {
            generateSeed();

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(Settings.getLaMulanaBaseDir() + "\\data\\mapdata\\script.rcd"));
                Files.copy(new File(String.format("%s/script.rcd", Settings.getStartingSeed())).toPath(),
                        fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                fileOutputStream = new FileOutputStream(new File(Settings.getLaMulanaBaseDir() + "\\data\\language\\en\\script_code.dat"));
                Files.copy(new File(String.format("%s/script_code.dat", Settings.getStartingSeed())).toPath(),
                        fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            catch (Exception ex) {
                FileUtils.log("unable to copy files to La-Mulana install");
                FileUtils.closeAll();
                System.exit(0);
            }
        }

        private void restore() {
            generateSeed();

            try {
                Files.copy(new File("script.rcd.bak").toPath(),
                        new FileOutputStream(new File(Settings.getLaMulanaBaseDir() + "\\data\\mapdata\\script.rcd")));
                Files.copy(new File(String.format("script_code.dat.bak", Settings.getStartingSeed())).toPath(),
                        new FileOutputStream(new File(Settings.getLaMulanaBaseDir() + "\\data\\language\\en\\script_code.dat")));
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

        public FieldPanel() {
            super(new MigLayout("fillx", "[][sg fields, fill, grow 80]", "[]"));
            setBorder(BorderFactory.createTitledBorder("Game Settings"));

            seedNumber = new JTextField(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
            add(new JLabel("Seed number"), "gap related");
            add(seedNumber, "wrap rel");

            laMulanaDirectory = new JTextField(Settings.getLaMulanaBaseDir());
            add(new JLabel("La-Mulana install directory "), "gap related");
            add(laMulanaDirectory);
        }

        public void rerollRandomSeed() {
            seedNumber.setText(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
        }

        public void updateSettings() {
            try {
                Settings.setStartingSeed(Integer.parseInt(seedNumber.getText()));
                Settings.setLaMulanaBaseDir(laMulanaDirectory.getText(), true);
            }
            catch (Exception ex) {
                FileUtils.log("unable to save edit for seedNumber");
            }
        }
    }

    static class GameItemRadio extends JPanel {
        private ButtonGroup itemRandomization;
        private String itemName;

        public GameItemRadio(String item) {
            super(new GridLayout(0, 1));

            add(new JLabel(item + ": ", JLabel.LEFT));

            itemRandomization = new ButtonGroup();
            JRadioButton randomItem = new JRadioButton("Random");
            randomItem.setActionCommand("RANDOM");
            JRadioButton initialItem = new JRadioButton("Initially Accessible");
            initialItem.setActionCommand("INITIAL");
            JRadioButton nonrandomItem = new JRadioButton("Original Location");
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
    }

    static class CheckboxPanel extends JPanel {
        private JCheckBox fullItemAccess;
        private JCheckBox randomizeForbiddenTreasure;
        private JCheckBox enableDamageBoostRequirements;
        private JCheckBox requireSoftwareComboForKeyFairy;

        public CheckboxPanel() {
            super(new MigLayout("wrap 2", "[sizegroup checkboxes]", "[]2[]"));

            fullItemAccess = new JCheckBox("All items must be accessible (100% seed)");
            fullItemAccess.setSelected(Settings.isFullItemAccess());
            add(fullItemAccess);

            enableDamageBoostRequirements = new JCheckBox("Allow damage-boosting requirements");
            enableDamageBoostRequirements.setSelected(Settings.isRequireSoftwareComboForKeyFairy());
            add(enableDamageBoostRequirements);

            requireSoftwareComboForKeyFairy = new JCheckBox("Key Fairy chests/doors expect miracle + mekuri");
            requireSoftwareComboForKeyFairy.setSelected(Settings.isRequireSoftwareComboForKeyFairy());
            add(requireSoftwareComboForKeyFairy);

            randomizeForbiddenTreasure = new JCheckBox("Replace random non-Shrine map with Forbidden Treasure");
            randomizeForbiddenTreasure.setSelected(Settings.isRandomizeForbiddenTreasure());
            add(randomizeForbiddenTreasure);
        }

        public void updateSettings() {
            Settings.setFullItemAccess(fullItemAccess.isSelected(), true);
            Settings.setRequireSoftwareComboForKeyFairy(requireSoftwareComboForKeyFairy.isSelected(), true);
            Settings.setRandomizeForbiddenTreasure(randomizeForbiddenTreasure.isSelected(), true);
            Settings.setEnableDamageBoostRequirements(enableDamageBoostRequirements.isSelected(), true);
        }
    }

    static class GlitchPanel extends JPanel {
        private List<JCheckBox> glitchOptions = new ArrayList<>();

        public GlitchPanel() {
            super(new MigLayout("fillx, wrap 4", "[sizegroup checkboxes]", "[]4[]"));
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
            super(new GridLayout(0, 4, 0, 20));
            setBorder(BorderFactory.createTitledBorder("Item Settings"));

            itemConfigRadioGroupPanels = new ArrayList<>();
            itemConfigRadioGroupPanels.add(new GameItemRadio("Holy Grail"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("mirai.exe"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Hermes' Boots"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Feather"));

            itemConfigRadioGroupPanels.add(new GameItemRadio("Hand Scanner"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("reader.exe"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Isis' Pendant"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Bronze Mirror"));

            itemConfigRadioGroupPanels.add(new GameItemRadio("Grapple Claw"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("xmailer.exe"));
//            itemConfigRadioGroupPanels.add(new GameItemRadio("Flail Whip"));
//            itemConfigRadioGroupPanels.add(new GameItemRadio("Fairy Clothes"));
            for(GameItemRadio gameItemRadio : itemConfigRadioGroupPanels) {
                add(gameItemRadio, LEFT_ALIGNMENT);
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

        public ShopRandomizationRadio() {
            super(new MigLayout("fillx"));

            add(new JLabel("Shop Randomization: ", JLabel.LEFT));

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
    }

    static class DifficultyPanel extends JPanel {
        private ButtonGroup difficultySetting;

        public DifficultyPanel() {
            super(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(new JLabel("Boss Difficulty: ", JLabel.LEFT));

            difficultySetting = new ButtonGroup();
            JRadioButton easy = new JRadioButton("Easy");
            easy.setActionCommand("EASY");
            JRadioButton medium = new JRadioButton("Medium");
            medium.setActionCommand("MEDIUM");
            JRadioButton hard = new JRadioButton("Hard");
            hard.setActionCommand("HARD");
            difficultySetting.add(easy);
            difficultySetting.add(medium);
            difficultySetting.add(hard);

            add(easy);
            add(medium);
            add(hard);

            if(BossDifficulty.EASY.equals(Settings.getBossDifficulty())) {
                easy.setSelected(true);
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

    private static void doTheThing() {
        Random random = new Random(Settings.getStartingSeed());
        Set<String> initiallyAvailableItems = getInitiallyAvailableItems();

        int attempt = 0;
        while(true) {
            ++attempt;
            ItemRandomizer itemRandomizer = new ItemRandomizer();
            ShopRandomizer shopRandomizer = buildShopRandomizer(itemRandomizer);
            AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer);

            itemRandomizer.placeNonRandomizedItems();
            shopRandomizer.placeNonRandomizedItems();
            shopRandomizer.determineItemTypes(random);
            if(!itemRandomizer.placeRequiredItems(new ArrayList<>(initiallyAvailableItems), random)) {
                continue;
            }
            if(!itemRandomizer.placeAllItems(random)) {
                continue;
            }

            boolean ankhJewelLock = false;
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
                    FileUtils.log(String.format("Successful attempt %s.", attempt));

                    if(Settings.isRandomizeForbiddenTreasure()) {
                        itemRandomizer.randomizeForbiddenTreasure(random);
                    }

                    List<Zone> rcdData = RcdReader.getRcdScriptInfo();
                    List<Block> datInfo = DatReader.getDatScriptInfo();
                    outputLocations(itemRandomizer, shopRandomizer, attempt);
                    itemRandomizer.updateFiles();
                    shopRandomizer.updateFiles(datInfo, random);
                    RcdWriter.writeRcd(rcdData);
                    DatWriter.writeDat(datInfo);

                    if(!Settings.isFullItemAccess()) {
                        accessChecker.outputRemaining(Settings.getStartingSeed(), attempt);
                    }

                    return;
                } catch (Exception ex) {
                    FileUtils.log(ex.getClass().getName() + ": " + ex.getMessage());
                    FileUtils.log("File: " + ex.getStackTrace()[0].getFileName());
                    FileUtils.log("Method: " + ex.getStackTrace()[0].getMethodName());
                    FileUtils.log("Line: " + ex.getStackTrace()[0].getLineNumber());
                    FileUtils.log("File: " + ex.getStackTrace()[1].getFileName());
                    FileUtils.log("Method: " + ex.getStackTrace()[1].getMethodName());
                    FileUtils.log("Line: " + ex.getStackTrace()[1].getLineNumber());
                    return;
                }
            }
            try {
//                accessChecker.outputRemaining(Settings.getStartingSeed(), attempt);
            } catch (Exception ex) {
                FileUtils.log(ex.getClass().getName() + ": " + ex.getMessage());
                FileUtils.log("File: " + ex.getStackTrace()[0].getFileName());
                FileUtils.log("Method: " + ex.getStackTrace()[0].getMethodName());
                FileUtils.log("Line: " + ex.getStackTrace()[0].getLineNumber());
                FileUtils.log("File: " + ex.getStackTrace()[1].getFileName());
                FileUtils.log("Method: " + ex.getStackTrace()[1].getMethodName());
                FileUtils.log("Line: " + ex.getStackTrace()[1].getLineNumber());
                return;
                // No exception handling in v1
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
