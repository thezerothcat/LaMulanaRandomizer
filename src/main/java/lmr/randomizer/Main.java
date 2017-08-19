package lmr.randomizer;

import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.DatReader;
import lmr.randomizer.dat.DatWriter;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.random.ItemRandomizer;
import lmr.randomizer.random.CategorizedShopRandomizer;
import lmr.randomizer.random.StaticShopRandomizer;
import lmr.randomizer.random.ShopRandomizer;
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
            Settings.setRandomizeShops(true);
            Settings.getNonRandomizedItems().add("Holy Grail");
//            parseSettings(args);
            Settings.setLaMulanaBaseDir("C:\\GOG Games\\La-Mulana");
            Settings.setRcdFileLocation("src/main/resources/lmr/randomizer/rcd/script.rcd.bak");
            Settings.setDatFileLocation("src/main/resources/lmr/randomizer/rcd/script_code.dat.bak");
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

        public RandomizerUI() {
            try {
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            setTitle("La-Mulana (Remake) Randomizer");
            //setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            //setLayout(new GridLayout(4, 0));
            setLayout(new MigLayout("fill, aligny top", "[]", "[]"));

            fieldPanel = new FieldPanel();
            add(fieldPanel, "growx, wrap");

            radioPanel = new RadioPanel();
            add(radioPanel, "growx, wrap");

            checkboxPanel = new CheckboxPanel();
            add(checkboxPanel, "growx, wrap");

//            getContentPane().add(buttonPanel);
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
            Settings.saveSettings();

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

            Settings.setRcdFileLocation("script.rcd.bak");
            Settings.setDatFileLocation("script_code.dat.bak");
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

            //super(new GridLayout(2, 0));
            super(new MigLayout("fillx", "[][sg fields, fill, grow 80]", "[]"));

            //setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
                Settings.setLaMulanaBaseDir(laMulanaDirectory.getText());
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

            setBorder(BorderFactory.createTitledBorder(item));

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
        private JCheckBox enableGlitches;
        private JCheckBox initialSubweapon;
        private JCheckBox randomizeShops;
        private JCheckBox requireSoftwareComboForKeyFairy;
        private JCheckBox randomizeForbiddenTreasure;

        public CheckboxPanel() {
            //super(new GridLayout(3, 2));
            super(new MigLayout("wrap 2", "[sizegroup checkboxes]", "[]2[]"));
            //setPreferredSize(new Dimension(800, 10));
            //setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            setBorder(BorderFactory.createTitledBorder("Advanced Settings"));

            enableGlitches = new JCheckBox("Enable glitched requirements");
            enableGlitches.setSelected(Settings.isAllowGlitches());
            add(enableGlitches);

            initialSubweapon = new JCheckBox("Guarantee initially accessible subweapon");
            initialSubweapon.setSelected(Settings.isGuaranteeSubweapon());
            add(initialSubweapon);

            randomizeShops = new JCheckBox("Enable shop randomization");
            randomizeShops.setSelected(Settings.isRandomizeShops());
            add(randomizeShops);

            randomizeForbiddenTreasure = new JCheckBox("Include Forbidden Treasure in randomized items");
            randomizeForbiddenTreasure.setSelected(Settings.isRandomizeForbiddenTreasure());
            add(randomizeForbiddenTreasure);

            requireSoftwareComboForKeyFairy = new JCheckBox("Require software combo for key fairies");
            requireSoftwareComboForKeyFairy.setSelected(Settings.isRequireSoftwareComboForKeyFairy());
            add(requireSoftwareComboForKeyFairy);
        }

        public void updateSettings() {
            Settings.setAllowGlitches(enableGlitches.isSelected());
            Settings.setGuaranteeSubweapon(initialSubweapon.isSelected());

            if(Settings.isRandomizeShops() != randomizeShops.isSelected()
                    || Settings.isRequireSoftwareComboForKeyFairy() != requireSoftwareComboForKeyFairy.isSelected()) {
                DataFromFile.clearRequirementsData();
            }
            Settings.setRandomizeShops(randomizeShops.isSelected());
            Settings.setRequireSoftwareComboForKeyFairy(requireSoftwareComboForKeyFairy.isSelected());
            Settings.setRandomizeForbiddenTreasure(randomizeForbiddenTreasure.isSelected());
        }
    }

    static class RadioPanel extends JPanel {
        List<GameItemRadio> itemConfigRadioGroupPanels;

        public RadioPanel() {
            //super(new GridLayout(2, 4, 0, 20));
            super(new MigLayout("wrap 4, gap rel, alignx center", "[]", "[]"));
//            setPreferredSize(new Dimension(800, 400));
            setBorder(BorderFactory.createTitledBorder("Item Settings"));

            itemConfigRadioGroupPanels = new ArrayList<>();
            itemConfigRadioGroupPanels.add(new GameItemRadio("Holy Grail"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("mirai.exe"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Grapple Claw"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Feather"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Isis' Pendant"));
            itemConfigRadioGroupPanels.add(new GameItemRadio("Bronze Mirror"));
//            itemConfigRadioGroupPanels.add(new GameItemRadio("Flail Whip"));
//            itemConfigRadioGroupPanels.add(new GameItemRadio("Fairy Clothes"));
            for(GameItemRadio gameItemRadio : itemConfigRadioGroupPanels) {
                add(gameItemRadio);
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
            Settings.setInitiallyAvailableItems(initiallyAvailableItems);
            Settings.setNonRandomizedItems(nonRandomizedItems);
        }
    }


    private static void doTheThing() {
        Random random = new Random(Settings.getStartingSeed());
        Set<String> initiallyAvailableItems = getInitiallyAvailableItems();
        List<String> subweapons = new ArrayList<>(ItemRandomizer.ALL_SUBWEAPONS);
        subweapons.removeAll(DataFromFile.getNonRandomizedItems());

        int attempt = 0;
        while(true) {
            ++attempt;
            ItemRandomizer itemRandomizer = new ItemRandomizer();
            ShopRandomizer shopRandomizer = buildShopRandomizer(itemRandomizer);
            AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer);

            String initialSubweapon = null;
            if(Settings.isGuaranteeSubweapon() && !subweapons.isEmpty()) {
                initialSubweapon = subweapons.get(random.nextInt(subweapons.size())); // todo: if this isn't a thing that can be placed, problems.
            }
            shopRandomizer.determineItemTypes(random, initialSubweapon);

            List<String> initiallyAvailableItemsTemp = new ArrayList<>(initiallyAvailableItems);
            initiallyAvailableItemsTemp.add(initialSubweapon);
            itemRandomizer.placeNonRandomizedItems();
            shopRandomizer.placeNonRandomizedItems();
            if(!itemRandomizer.placeRequiredItems(new ArrayList<>(initiallyAvailableItems), random)) {
                continue;
            }
            if(!itemRandomizer.placeAllItems(random)) {
                continue;
            }

            boolean ankhJewelLock = false;
            accessChecker.computeAccessibleNodes("None");
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
                    // No exception handling in v1
                }

            }
            try {
//                accessChecker.outputRemaining(Settings.startingSeed, attempt);
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
            attempt++;
        }
    }

    private static ShopRandomizer buildShopRandomizer(ItemRandomizer itemRandomizer) {
        ShopRandomizer shopRandomizer;
        if(Settings.isRandomizeShops()) {
//            shopRandomizer = new ShopRandomizer(itemRandomizer.getTotalShopItems());
            shopRandomizer = new CategorizedShopRandomizer();
        }
        else {
            shopRandomizer = new StaticShopRandomizer(itemRandomizer.getTotalShopItems());
        }

        itemRandomizer.setShopRandomizer(shopRandomizer);
        return shopRandomizer;
    }

    private static AccessChecker buildAccessChecker(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer) {
        AccessChecker accessChecker = new AccessChecker();
        accessChecker.setItemRandomizer(itemRandomizer);
        accessChecker.setShopRandomizer(shopRandomizer);
        itemRandomizer.setAccessChecker(accessChecker);
        shopRandomizer.setAccessChecker(accessChecker);
        shopRandomizer.setItemRandomizer(itemRandomizer);
        return accessChecker;
    }

    private static Set<String> getInitiallyAvailableItems() {
        Set<String> noRequirementItems = new HashSet<>(Settings.getInitiallyAvailableItems());
//        if(Settings.randomizeShops) {
//            noRequirementItems.add("Hand Scanner");
//            noRequirementItems.add("reader.exe");
//            noRequirementItems.add("Hermes' Boots");
//            noRequirementItems.add("Helmet");
//        }
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
