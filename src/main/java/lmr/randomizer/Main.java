package lmr.randomizer;

import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.random.ItemRandomizer;
import lmr.randomizer.random.ShopNonRandomizer;
import lmr.randomizer.random.ShopRandomizer;

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
            parseSettings(args);
            Settings.rcdFileLocation = "src/main/resources/lmr/randomizer/rcd/script.rcd.bak";
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
        private CheckboxPanel checkboxPanel;


        public RandomizerUI() {
            setTitle("La-Mulana (Remake) Randomizer");
            setMinimumSize(new Dimension(600, 500));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            setLayout(new GridLayout(3, 1));

            fieldPanel = new FieldPanel();
            getContentPane().add(fieldPanel);

            checkboxPanel = new CheckboxPanel();
            add(checkboxPanel);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton generateButton = new JButton("Generate");
            generateButton.addActionListener(this);
            buttonPanel.add(generateButton);
            getContentPane().add(buttonPanel);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fieldPanel.updateSettings();
            checkboxPanel.updateSettings();
            fieldPanel.rerollRandomSeed();

            File directory = new File(Long.toString(Settings.startingSeed));
            directory.mkdir();

            File rcdFile = new File("script.rcd.bak");
            if(!rcdFile.exists()) {
                try {
                    // Make script.rcd backup
                    Files.copy(new File(Settings.laMulanaBaseDir, "data/mapdata/script.rcd").toPath(),
                            new FileOutputStream(new File("script.rcd.bak")));
                }
                catch (Exception ex) {
                    FileUtils.log("unable to back up script.rcd");
                }
            }
            Settings.rcdFileLocation = "script.rcd.bak";

//            parseSettings(args);
            try {
                doTheThing();
            } catch (Exception ex) {
                FileUtils.log("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
            FileUtils.closeAll();
        }
    }

    static class FieldPanel extends JPanel {
        private JTextField laMulanaDirectory;
        private JTextField seedNumber;

        public FieldPanel() {
            super(new GridLayout(2, 0));
            setPreferredSize(new Dimension(600, 100));

            JPanel firstFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            firstFieldPanel.setPreferredSize(new Dimension(600, 60));
            firstFieldPanel.add(new JLabel("Seed number: ", JLabel.LEFT));
            seedNumber = new JTextField(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
            firstFieldPanel.add(seedNumber);

            JPanel secondFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            secondFieldPanel.setPreferredSize(new Dimension(600, 60));
            secondFieldPanel.add(new JLabel("La-Mulana install directory: ", JLabel.LEFT));
            laMulanaDirectory = new JTextField(getDefaultLaMulanaBaseDir());
            secondFieldPanel.add(laMulanaDirectory);

            add(firstFieldPanel);
            add(secondFieldPanel);
        }

        public void rerollRandomSeed() {
            seedNumber.setText(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
        }

        private String getDefaultLaMulanaBaseDir() {
            if(Settings.laMulanaBaseDir != null) {
                return Settings.laMulanaBaseDir;
            }
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

        public GameItemRadio(String item) {
            super(new GridLayout(0, 1));
            setPreferredSize(new Dimension(200, 140));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(new JLabel(item + ": ", JLabel.LEFT));

            itemRandomization = new ButtonGroup();
            JRadioButton randomItem = new JRadioButton("Random");
            randomItem.setActionCommand("random");
            JRadioButton initialItem = new JRadioButton("Initially Accessible");
            initialItem.setActionCommand("initial");
            initialItem.setSelected(true);
            JRadioButton nonrandomItem = new JRadioButton("Original Location");
            nonrandomItem.setActionCommand("nonrandom");
            itemRandomization.add(randomItem);
            itemRandomization.add(initialItem);
            itemRandomization.add(nonrandomItem);

            add(randomItem);
            add(initialItem);
            add(nonrandomItem);
        }

        public String getActionCommand() {
            return itemRandomization.getSelection().getActionCommand();
        }
    }

    static class CheckboxPanel extends JPanel {
        Map<String, GameItemRadio> mapOfItemNameToItemConfigRadioGroupPanel;
        private ButtonGroup grailRandomization;
//        private JCheckBox earlyGrapple;
//        private JCheckBox earlySubweapon;
//
//        private JCheckBox enableGlitches;

        public CheckboxPanel() {
            super(new GridLayout(2, 0));
            setPreferredSize(new Dimension(600, 200));

            mapOfItemNameToItemConfigRadioGroupPanel = new HashMap<>();
            mapOfItemNameToItemConfigRadioGroupPanel.put("Holy Grail", new GameItemRadio("Holy Grail"));
            mapOfItemNameToItemConfigRadioGroupPanel.put("Grapple Claw", new GameItemRadio("Grapple Claw"));
            for(GameItemRadio gameItemRadio : mapOfItemNameToItemConfigRadioGroupPanel.values()) {
                add(gameItemRadio);
            }
        }

        public void updateSettings() {
            Settings.initiallyAvailableItems = Settings.getDefaultInitiallyAvailableItems();
            Settings.nonRandomizedItems = new HashSet<>();

            for(Map.Entry<String, GameItemRadio> entry : mapOfItemNameToItemConfigRadioGroupPanel.entrySet()) {
                String actionCommand = entry.getValue().getActionCommand();
                if("initial".equals(actionCommand)) {
                    addArgItemUI(Settings.initiallyAvailableItems, entry.getKey());
                }
                else if("nonrandom".equals(actionCommand)) {
                    addArgItemUI(Settings.nonRandomizedItems, entry.getKey());
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
                    outputLocations(itemRandomizer, shopRandomizer, attempt);
                    itemRandomizer.updateRcd();

//                    accessChecker.outputRemaining(Settings.startingSeed, attempt);
                } catch (Exception ex) {
                    return;
                    // No exception handling in v1
                }

                return;
            }
            attempt++;
        }
    }

    private static ShopRandomizer buildShopRandomizer(ItemRandomizer itemRandomizer) {
        ShopRandomizer shopRandomizer;
        if(Settings.randomizeShops) {
            shopRandomizer = new ShopRandomizer(itemRandomizer.getTotalShopItems());
        }
        else {
            shopRandomizer = new ShopNonRandomizer(itemRandomizer.getTotalShopItems());
        }

        itemRandomizer.setShopRandomizer(shopRandomizer);
        return shopRandomizer;
    }

    private static AccessChecker buildAccessChecker(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer) {
        AccessChecker accessChecker = new AccessChecker();
        FileUtils.populateRequirements(accessChecker, "requirement/location_reqs.txt", "Location: ");
        FileUtils.populateRequirements(accessChecker, "requirement/item_reqs.txt", null);
        FileUtils.populateRequirements(accessChecker, "requirement/event_reqs.txt", null);
        FileUtils.populateRequirements(accessChecker, "requirement/shop_reqs.txt", null);
        if(Settings.allowGlitches) {
            FileUtils.populateRequirements(accessChecker, "requirement/glitch/location_reqs.txt", "Location: ");
            FileUtils.populateRequirements(accessChecker, "requirement/glitch/item_reqs.txt", null);
            FileUtils.populateRequirements(accessChecker, "requirement/glitch/shop_reqs.txt", null);
        }
        accessChecker.setItemRandomizer(itemRandomizer);
        accessChecker.setShopRandomizer(shopRandomizer);
        itemRandomizer.setAccessChecker(accessChecker);
        shopRandomizer.setAccessChecker(accessChecker);
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
