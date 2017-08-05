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
            Settings.startingSeed = 0;
            Settings.nonRandomizedItems.add("Holy Grail");
//            parseSettings(args);
            Settings.laMulanaBaseDir = "C:\\GOG Games\\La-Mulana";
            Settings.rcdFileLocation = "src/main/resources/lmr/randomizer/rcd/script.rcd.bak";
            Settings.datFileLocation = "src/main/resources/lmr/randomizer/rcd/script_code.dat.bak";
            File directory = new File(Long.toString(Settings.startingSeed));
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
            setTitle("La-Mulana (Remake) Randomizer");
            setMinimumSize(new Dimension(800, 600));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            setLayout(new GridLayout(4, 0));

            fieldPanel = new FieldPanel();
            getContentPane().add(fieldPanel);

            radioPanel = new RadioPanel();
            add(radioPanel);

            checkboxPanel = new CheckboxPanel();
            add(checkboxPanel);

//            getContentPane().add(buttonPanel);
            add(new ButtonPanel(this));
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
            fieldPanel.rerollRandomSeed();

            File rcdFile = new File("script.rcd.bak");
            if(!rcdFile.exists()) {
                File existingRcd = new File(Settings.laMulanaBaseDir, "data/mapdata/script.rcd");
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
                File existingDat = new File(Settings.laMulanaBaseDir, "data/language/en/script_code.dat");
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

            Settings.rcdFileLocation = "script.rcd.bak";
            Settings.datFileLocation = "script_code.dat.bak";
            File directory = new File(Long.toString(Settings.startingSeed));
            directory.mkdir();


//            parseSettings(args);
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
                FileOutputStream fileOutputStream = new FileOutputStream(new File(Settings.laMulanaBaseDir + "\\data\\mapdata\\script.rcd"));
                Files.copy(new File(String.format("%s/script.rcd", Settings.startingSeed)).toPath(),
                        fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                fileOutputStream = new FileOutputStream(new File(Settings.laMulanaBaseDir + "\\data\\language\\en\\script_code.dat"));
                Files.copy(new File(String.format("%s/script_code.dat", Settings.startingSeed)).toPath(),
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
                        new FileOutputStream(new File(Settings.laMulanaBaseDir + "\\data\\mapdata\\script.rcd")));
                Files.copy(new File(String.format("script_code.dat.bak", Settings.startingSeed)).toPath(),
                        new FileOutputStream(new File(Settings.laMulanaBaseDir + "\\data\\language\\en\\script_code.dat")));
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
            super(new GridLayout(2, 0));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel firstFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            firstFieldPanel.setPreferredSize(new Dimension(800, 60));
            firstFieldPanel.add(new JLabel("Seed number: ", JLabel.LEFT));
            seedNumber = new JTextField(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
            seedNumber.setSize(800, 80);
            firstFieldPanel.add(seedNumber);

            JPanel secondFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            secondFieldPanel.setPreferredSize(new Dimension(800, 60));
            secondFieldPanel.add(new JLabel("La-Mulana install directory: ", JLabel.LEFT));
            laMulanaDirectory = new JTextField(getDefaultLaMulanaBaseDir());
            laMulanaDirectory.setSize(800, 60);
            secondFieldPanel.add(laMulanaDirectory);

            add(firstFieldPanel);
            add(secondFieldPanel);
        }

        public void rerollRandomSeed() {
            seedNumber.setText(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
        }

        private String getDefaultLaMulanaBaseDir() {
            for(String filename : Arrays.asList("C:\\Games\\La-Mulana Remake 1.3.3.1", "C:\\GOG Games\\La-Mulana", "C:\\GOG Games\\La-Mulana",
                    "C:\\Steam\\steamapps\\common\\La-Mulana", "C:\\Program Files (x86)\\Steam\\steamapps\\common\\La-Mulana",
                    "C:\\Program Files\\Steam\\steamapps\\common\\La-Mulana")) {
                if(new File(filename).exists()) {
                    return filename;
                }
            }
            return null;
        }

        public void updateSettings() {
            try {
                Settings.startingSeed = Integer.parseInt(seedNumber.getText());
                Settings.laMulanaBaseDir = laMulanaDirectory.getText();
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
            randomItem.setActionCommand("random");
            randomItem.setSelected(true);
            JRadioButton initialItem = new JRadioButton("Initially Accessible");
            initialItem.setActionCommand("initial");
            JRadioButton nonrandomItem = new JRadioButton("Original Location");
            nonrandomItem.setActionCommand("nonrandom");
            itemRandomization.add(randomItem);
            itemRandomization.add(initialItem);
            itemRandomization.add(nonrandomItem);

            add(randomItem);
            add(initialItem);
            add(nonrandomItem);

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

        public CheckboxPanel() {
            super(new GridLayout(2, 2));
            setPreferredSize(new Dimension(800, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            enableGlitches = new JCheckBox("Enable glitched requirements");
            add(enableGlitches);

            initialSubweapon = new JCheckBox("Guarantee initially accessible subweapon");
            add(initialSubweapon);

            randomizeShops = new JCheckBox("Enable shop randomization");
            add(randomizeShops);
            randomizeShops.setSelected(true);
        }

        public void updateSettings() {
            Settings.allowGlitches = enableGlitches.isSelected();
            Settings.guaranteeSubweapon = initialSubweapon.isSelected();
            Settings.randomizeShops = randomizeShops.isSelected();
        }
    }

    static class RadioPanel extends JPanel {
        List<GameItemRadio> itemConfigRadioGroupPanels;

        public RadioPanel() {
            super(new GridLayout(2, 4, 0, 20));
//            setPreferredSize(new Dimension(800, 400));
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

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
                add(gameItemRadio, LEFT_ALIGNMENT);
            }
        }

        public void updateSettings() {
            Settings.initiallyAvailableItems = Settings.getDefaultInitiallyAvailableItems();
            Settings.nonRandomizedItems = new HashSet<>();

            for(GameItemRadio itemRadio : itemConfigRadioGroupPanels) {
                String actionCommand = itemRadio.getActionCommand();
                if("initial".equals(actionCommand)) {
                    addArgItemUI(Settings.initiallyAvailableItems, itemRadio.getItemName());
                }
                else if("nonrandom".equals(actionCommand)) {
                    addArgItemUI(Settings.nonRandomizedItems, itemRadio.getItemName());
                }
            }
        }
    }


    private static void doTheThing() {
        Random random = new Random(Settings.startingSeed);
        Set<String> initiallyAvailableItems = getInitiallyAvailableItems();
        List<String> subweapons = new ArrayList<>(ItemRandomizer.ALL_SUBWEAPONS);
        subweapons.removeAll(DataFromFile.getNonRandomizedItems());

        int attempt = 1;
        while(true) {
            ItemRandomizer itemRandomizer = new ItemRandomizer();
            ShopRandomizer shopRandomizer = buildShopRandomizer(itemRandomizer);
            AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer);

            String initialSubweapon = null;
            if(Settings.guaranteeSubweapon && !subweapons.isEmpty()) {
                initialSubweapon = subweapons.get(random.nextInt(subweapons.size())); // todo: if this isn't a thing that can be placed, problems.
            }
            shopRandomizer.determineItemTypes(random, initialSubweapon);

            // todo: make initial items based on settings
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

            accessChecker.computeAccessibleNodes("None");
            while(!accessChecker.getQueuedUpdates().isEmpty()) {
                accessChecker.computeAccessibleNodes(accessChecker.getQueuedUpdates().iterator().next());
            }

            if(accessChecker.isSuccess()) {
                try {
                    List<Zone> rcdData = RcdReader.getRcdScriptInfo();
                    List<Block> datInfo = DatReader.getDatScriptInfo();
                    outputLocations(itemRandomizer, shopRandomizer, attempt);
                    itemRandomizer.updateFiles();
                    shopRandomizer.updateFiles(datInfo);
                    RcdWriter.writeRcd(rcdData);
                    DatWriter.writeDat(datInfo);

                    return;
                } catch (Exception ex) {
                    return;
                    // No exception handling in v1
                }

            }
            try {
//                accessChecker.outputRemaining(Settings.startingSeed, attempt);
            } catch (Exception ex) {
                return;
                // No exception handling in v1
            }
            attempt++;
        }
    }

    private static ShopRandomizer buildShopRandomizer(ItemRandomizer itemRandomizer) {
        ShopRandomizer shopRandomizer;
        if(Settings.randomizeShops) {
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
        Set<String> noRequirementItems = new HashSet<>(Settings.initiallyAvailableItems);
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

    private static void parseSettings(String[] args) {
        for(String arg : args) {
            if(arg.startsWith("-s")) {
                try {
                    Settings.startingSeed = Long.parseLong(arg.substring(2));
                } catch (Exception ex) {
                    Settings.startingSeed = 0L;
                }
            }
            else if(arg.equals("-g")) {
                Settings.allowGlitches = true;
            }
            else if (arg.startsWith("-dir")) {
                Settings.laMulanaBaseDir = arg.substring(4);
            }
            else if(arg.equals("-ng") || arg.equals("-ngrail")) {
                Settings.nonRandomizedItems.add("Holy Grail");
            }
            else if (arg.startsWith("-n")) {
                addArgItem(Settings.nonRandomizedItems, arg.substring(2));
            }
            else if(arg.equals("-isw")) {
                Settings.guaranteeSubweapon = true;
            }
            else if(arg.equals("-ig") || arg.equals("-igrail")) {
                Settings.initiallyAvailableItems.add("Holy Grail");
            }
            else if(arg.equals("-igrapple")) {
                Settings.initiallyAvailableItems.add("Grapple Claw");
            }
            else if (arg.startsWith("-i")) {
                addArgItem(Settings.initiallyAvailableItems, arg.substring(2));
            }
        }
    }

    private static void addArgItem(Set<String> nonRandomizedItems, String input) {
        for(String item : DataFromFile.getAllItems()) {
            if(item.replaceAll(" ", "").equalsIgnoreCase(input)) {
                nonRandomizedItems.add(item);
            }
        }
    }

    private static void addArgItemUI(Set<String> nonRandomizedItems, String input) {
        for(String item : DataFromFile.getAllItems()) {
            if(item.equals(input)) {
                nonRandomizedItems.add(item);
            }
        }
    }
}
