package lmr.randomizer;

import lmr.randomizer.random.BossDifficulty;
import lmr.randomizer.random.ShopRandomizationEnum;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Created by thezerothcat on 7/20/2017.
 */
public final class Settings {
    private static Settings singleton = new Settings();

    private long startingSeed;

    private boolean changed = false;

    private boolean fullItemAccess;
    private boolean automaticHardmode;
    private boolean coinChestGraphics;
    private boolean requireSoftwareComboForKeyFairy;
    private boolean requireIceCapeForLava;
    private boolean requireFlaresForExtinction;
    private boolean randomizeForbiddenTreasure;
    private boolean randomizeCoinChests;
    private boolean replaceMapsWithWeights;
    private boolean automaticGrailPoints;

    private List<String> enabledGlitches = new ArrayList<>();
    private List<String> enabledDamageBoosts = new ArrayList<>();

    private List<String> possibleGlitches = Arrays.asList("Lamp Glitch", "Cat Pause",
            "Raindrop", "Ice Raindrop", "Pot Clip", "Object Zip");
    private List<String> possibleDboosts = Arrays.asList("Item", "Environment", "Enemy");

    private List<String> possibleRandomizedItems = Arrays.asList("Holy Grail", "Hand Scanner", "reader.exe", "Hermes'" +
            " Boots", "Grapple Claw", "Feather", "Isis' Pendant", "Bronze Mirror", "mirai.exe", "bunemon.exe" , "Random", "xmailer.exe");

    private String laMulanaBaseDir;
    private String language;

    private Set<String> nonRandomizedItems = new HashSet<>();
    private Set<String> initiallyAccessibleItems = new HashSet<>();
    private Set<String> surfaceItems = new HashSet<>();

    private String xmailerItem;

    private BossDifficulty bossDifficulty;
    private ShopRandomizationEnum shopRandomization;

    private Settings() {
        startingSeed = new Random().nextInt(Integer.MAX_VALUE);
        laMulanaBaseDir = "Please enter your La-Mulana install directory";
        language = "en";

        requireSoftwareComboForKeyFairy = true;
        requireIceCapeForLava = true;
        requireFlaresForExtinction = true;
        randomizeForbiddenTreasure = true;
        randomizeCoinChests = true;
        replaceMapsWithWeights = true;
        fullItemAccess = true;
        automaticHardmode = false;
        coinChestGraphics = false;
        automaticGrailPoints = false;

        bossDifficulty = BossDifficulty.MEDIUM;
        shopRandomization = ShopRandomizationEnum.EVERYTHING;

        xmailerItem = null;

        for (String filename : Arrays.asList("C:\\Games\\La-Mulana Remake 1.3.3.1", "C:\\GOG Games\\La-Mulana", "C:\\GOG Games\\La-Mulana",
                "C:\\Steam\\steamapps\\common\\La-Mulana", "C:\\Program Files (x86)\\Steam\\steamapps\\common\\La-Mulana",
                "C:\\Program Files\\Steam\\steamapps\\common\\La-Mulana", "C:\\Program Files (x86)\\GOG Galaxy\\Games\\La Mulana",
                "C:\\Program Files (x86)\\GOG.com\\La-Mulana")) {
            if (new File(filename).exists()) {
                laMulanaBaseDir = filename;
            }
        }
    }

    public static boolean isChanged() {
        return singleton.changed;
    }

    public static long getStartingSeed() {
        return singleton.startingSeed;
    }

    public static boolean isRequireSoftwareComboForKeyFairy() {
        return singleton.requireSoftwareComboForKeyFairy;
    }

    public static boolean isRequireIceCapeForLava() {
        return singleton.requireIceCapeForLava;
    }

    public static boolean isRequireFlaresForExtinction() {
        return singleton.requireFlaresForExtinction;
    }

    public static boolean isRandomizeForbiddenTreasure() {
        return singleton.randomizeForbiddenTreasure;
    }

    public static boolean isRandomizeCoinChests() {
        return singleton.randomizeCoinChests;
    }

    public static boolean isReplaceMapsWithWeights() {
        return singleton.replaceMapsWithWeights;
    }

    public static boolean isCoinChestGraphics() {
        return singleton.coinChestGraphics;
    }

    public static String getLaMulanaBaseDir() {
        return singleton.laMulanaBaseDir;
    }

    public static String getLanguage() {
        return singleton.language;
    }

    public static String getXmailerItem() {
        return singleton.xmailerItem;
    }

    public static boolean getCoinChestGraphics() {
        return singleton.coinChestGraphics;
    }
    public static boolean getAutomaticHardmode() {
        return singleton.automaticHardmode;
    }

    public static boolean getFullItemAccess() {
        return singleton.fullItemAccess;
    }

    public static String getBackupDatFile() {
        if("en".equals(singleton.language)) {
            return "script_code.dat.bak";
        }
        return "script_code_" + singleton.language + ".dat.bak";
    }


    public static boolean getAutomaticGrailPoints() {
        return singleton.automaticGrailPoints;
    }

    public static Set<String> getNonRandomizedItems() {
        return singleton.nonRandomizedItems;
    }

    public static Set<String> getSurfaceItems() {
        return singleton.surfaceItems;
    }

    public static Set<String> getInitiallyAccessibleItems() {
        return singleton.initiallyAccessibleItems;
    }

    public static ShopRandomizationEnum getShopRandomization() {
        return singleton.shopRandomization;
    }

    public static BossDifficulty getBossDifficulty() {
        return singleton.bossDifficulty;
    }

    public static boolean getRandomizeCoinChests() {
        return singleton.randomizeCoinChests;
    }

    public static boolean isFullItemAccess() {
        return singleton.fullItemAccess;
    }

    public static boolean isAutomaticHardmode() {
        return singleton.automaticHardmode;
    }

    public static boolean isAutomaticGrailPoints() {
        return singleton.automaticGrailPoints;
    }

    public static List<String> getEnabledGlitches() {
        return singleton.enabledGlitches;
    }

    public static List<String> getEnabledDamageBoosts() {
        return singleton.enabledDamageBoosts;
    }

    public static boolean getRequireSoftwareComboForKeyFairy() { return singleton.requireSoftwareComboForKeyFairy; }

    public static void setRequireSoftwareComboForKeyFairy(boolean requireSoftwareComboForKeyFairy, boolean update) {
        if(update && requireSoftwareComboForKeyFairy != singleton.requireSoftwareComboForKeyFairy) {
            singleton.changed = true;
        }
        singleton.requireSoftwareComboForKeyFairy = requireSoftwareComboForKeyFairy;
    }
    public static boolean getRequireIceCapeForLava() { return singleton.requireIceCapeForLava; }

    public static void setRequireIceCapeForLava(boolean requireIceCapeForLava, boolean update) {
        if(update && requireIceCapeForLava != singleton.requireIceCapeForLava) {
            singleton.changed = true;
        }
        singleton.requireIceCapeForLava = requireIceCapeForLava;
    }

    public static boolean getRequireFlaresForExtinction() { return singleton.requireFlaresForExtinction; }

    public static void setRequireFlaresForExtinction(boolean requireFlaresForExtinction, boolean update) {
        if(update && requireFlaresForExtinction != singleton.requireFlaresForExtinction) {
            singleton.changed = true;
        }
        singleton.requireFlaresForExtinction = requireFlaresForExtinction;
    }

    public static boolean getRandomizeForbiddenTreasure() { return singleton.randomizeForbiddenTreasure; }

    public static void setRandomizeForbiddenTreasure(boolean randomizeForbiddenTreasure, boolean update) {
        if(update && randomizeForbiddenTreasure != singleton.randomizeForbiddenTreasure) {
            singleton.changed = true;
        }
        singleton.randomizeForbiddenTreasure = randomizeForbiddenTreasure;
    }

    public static boolean setRandomizeCoinChests() { return singleton.randomizeCoinChests; }

    public static void setRandomizeCoinChests(boolean randomizeCoinChests, boolean update) {
        if(update && randomizeCoinChests != singleton.randomizeCoinChests) {
            singleton.changed = true;
        }
        singleton.randomizeCoinChests = randomizeCoinChests;
    }
    public static boolean getReplaceMapsWithWeights() { return singleton.replaceMapsWithWeights; }

    public static void setReplaceMapsWithWeights(boolean replaceMapsWithWeights, boolean update) {
        if(update && replaceMapsWithWeights != singleton.replaceMapsWithWeights) {
            singleton.changed = true;
        }
        singleton.replaceMapsWithWeights = replaceMapsWithWeights;
    }

    public static void setInitiallyAccessibleItems(Set<String> initiallyAccessibleItems, boolean update) {
        if(update && !singleton.changed) {
            if(initiallyAccessibleItems.containsAll(singleton.initiallyAccessibleItems)) {
                singleton.changed = !singleton.initiallyAccessibleItems.containsAll(initiallyAccessibleItems);
            }
            else {
                singleton.changed = true;
            }
        }
        singleton.initiallyAccessibleItems = initiallyAccessibleItems;
    }

    public static void setNonRandomizedItems(Set<String> nonRandomizedItems, boolean update) {
        if(update && !singleton.changed) {
            if(nonRandomizedItems.containsAll(singleton.nonRandomizedItems)) {
                singleton.changed = !singleton.nonRandomizedItems.containsAll(nonRandomizedItems);
            }
            else {
                singleton.changed = true;
            }
        }

        singleton.nonRandomizedItems = nonRandomizedItems;
    }

    public static void setSurfaceItems(Set<String> surfaceItems, boolean update) {
        if(update && !singleton.changed) {
            if(surfaceItems.containsAll(singleton.surfaceItems)) {
                singleton.changed = !singleton.surfaceItems.containsAll(surfaceItems);
            }
            else {
                singleton.changed = true;
            }
        }

        singleton.surfaceItems = surfaceItems;
    }

    public static void setXmailerItem(String xmailerItem, boolean update) {
        if(update) {
            if(xmailerItem == null && singleton.xmailerItem != null
                    || xmailerItem != null && xmailerItem.equals(singleton.xmailerItem)) {
                singleton.changed = true;
            }
        }
        singleton.xmailerItem = xmailerItem;
    }

    public static void setEnabledGlitches(List<String> enabledGlitches, boolean update) {
        if(update && !singleton.changed) {
            if (enabledGlitches.containsAll(singleton.enabledGlitches)) {
                singleton.changed = !singleton.enabledGlitches.containsAll(enabledGlitches);
            } else {
                singleton.changed = true;
            }
        }
        singleton.enabledGlitches = enabledGlitches;
    }

    public static void setEnabledDamageBoosts(List<String> enabledDamageBoosts, boolean update) {
        if(update && !singleton.changed) {
            if (enabledDamageBoosts.containsAll(singleton.enabledDamageBoosts)) {
                singleton.changed = !singleton.enabledDamageBoosts.containsAll(enabledDamageBoosts);
            } else {
                singleton.changed = true;
            }
        }
        singleton.enabledDamageBoosts = enabledDamageBoosts;
    }

    public static void setStartingSeed(int startingSeed) {
        singleton.startingSeed = startingSeed;
    }

    public static void setLaMulanaBaseDir(String laMulanaBaseDir, boolean update) {
        if(update && !laMulanaBaseDir.equals(singleton.laMulanaBaseDir)) {
            singleton.changed = true;
        }
        singleton.laMulanaBaseDir = laMulanaBaseDir;
    }

    public static void setLanguage(String language, boolean update) {
        if(update && !language.equals(singleton.language)) {
            singleton.changed = true;
        }
        singleton.language = language;
    }

    public static void setShopRandomization(String shopRandomization, boolean update) {
        if(update && !shopRandomization.equals(singleton.shopRandomization.toString())) {
            singleton.changed = true;
        }
        singleton.shopRandomization = ShopRandomizationEnum.valueOf(shopRandomization);
    }

    public static void setBossDifficulty(String bossDifficulty, boolean update) {
        if(update && !bossDifficulty.equals(singleton.bossDifficulty.toString())) {
            singleton.changed = true;
        }
        singleton.bossDifficulty = BossDifficulty.valueOf(bossDifficulty);
    }

    public static void setFullItemAccess(boolean fullItemAccess, boolean update) {
        if(update && fullItemAccess != singleton.fullItemAccess) {
            singleton.changed = true;
        }
        singleton.fullItemAccess = fullItemAccess;
    }

    public static void setAutomaticHardmode(boolean automaticHardmode, boolean update) {
        if(update && automaticHardmode != singleton.automaticHardmode) {
            singleton.changed = true;
        }
        singleton.automaticHardmode = automaticHardmode;
    }

    public static void setAutomaticGrailPoints(boolean automaticGrailPoints, boolean update) {
        if(update && automaticGrailPoints != singleton.automaticGrailPoints) {
            singleton.changed = true;
        }
        singleton.automaticGrailPoints = automaticGrailPoints;
    }

    public static void setCoinChestGraphics(boolean coinChestGraphics, boolean update) {
        if(update && coinChestGraphics != singleton.coinChestGraphics) {
            singleton.changed = true;
        }
        singleton.coinChestGraphics = coinChestGraphics;
    }

    public static void saveSettings() {
        if(singleton.changed) {
            try {
                FileUtils.saveSettings();
            } catch (IOException ex) {
                FileUtils.log("Unable to save settings: " + ex.getMessage());
            }
        }
    }

    public static int itemSetToInt(Collection<String> selectedItems, List<String> possibleItems) {
        int value = 0;
        for (String s : selectedItems) {
            int index = possibleItems.indexOf(s);
            if (index >= 0) {
                value |= 1 << index;
            }
        }
        return value;
    }

    public static Collection<String> intToItemSet(int input, List<String> possibleItems) {
        Collection<String> items = new ArrayList<>();

        int index = 0;

        while(input > 0) {
            if((input & 1) == 1) {
                items.add(possibleItems.get(index));
            }

            index++;
            input >>= 1;
        }

        return items;
    }

    public static int boolToInt(boolean b) {
        return b?1:0;
    }

    public static boolean intToBool(int i) {
        return i>0;
    }

    public static String generateShortString() {
        String result = FileUtils.VERSION;

        String seperator = "|";

        //seed

        //boolean fields + shoprandomization enum
        BiFunction<Boolean, Integer, Integer> processBooleanFlag = (Boolean b, Integer flagIndex) -> boolToInt(b) << flagIndex;

        int booleanSettings = 0;
        booleanSettings |= processBooleanFlag.apply(singleton.fullItemAccess, 9);
        booleanSettings |= processBooleanFlag.apply(singleton.automaticHardmode, 8);
        booleanSettings |= processBooleanFlag.apply(singleton.coinChestGraphics, 7);
        booleanSettings |= processBooleanFlag.apply(singleton.requireSoftwareComboForKeyFairy, 6);
        booleanSettings |= processBooleanFlag.apply(singleton.requireIceCapeForLava, 5);
        booleanSettings |= processBooleanFlag.apply(singleton.requireFlaresForExtinction, 4);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeForbiddenTreasure, 3);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeCoinChests, 2);
        booleanSettings |= processBooleanFlag.apply(singleton.replaceMapsWithWeights, 1);
        booleanSettings |= processBooleanFlag.apply(singleton.automaticGrailPoints, 0);
        booleanSettings = booleanSettings << 2 | singleton.shopRandomization.ordinal();

        //glitches
        int glitches = itemSetToInt(getEnabledGlitches(), singleton.possibleGlitches);

        //dboosts
        int dboosts = itemSetToInt(getEnabledDamageBoosts(), singleton.possibleDboosts);

        //nonrandomized items
        int nonRandoItems = itemSetToInt(getNonRandomizedItems(), singleton.possibleRandomizedItems);

        //initially accessible items
        int initItems = itemSetToInt(getInitiallyAccessibleItems(), singleton.possibleRandomizedItems);

        //surface items
        int surfaceItems = itemSetToInt(getSurfaceItems(), singleton.possibleRandomizedItems);

        // xmailer item
        int xmailer = singleton.possibleRandomizedItems.indexOf(singleton.xmailerItem);

        if(singleton.xmailerItem == null || xmailer == -1) {
            xmailer = singleton.possibleRandomizedItems.indexOf("Random");
        }

        // boss difficulty
        int bossDifficulty = singleton.bossDifficulty.ordinal();

        // combine the results of the settings in a string
        long startingSeed = getStartingSeed();
        result += seperator + Long.toHexString(startingSeed);
        result += seperator + Integer.toHexString(booleanSettings);
        result += seperator + Integer.toHexString(glitches);
        result += seperator + Integer.toHexString(dboosts);
        result += seperator + Integer.toHexString(nonRandoItems);
        result += seperator + Integer.toHexString(initItems);
        result += seperator + Integer.toHexString(surfaceItems);
        result += seperator + Integer.toHexString(xmailer);
        result += seperator + Integer.toHexString(bossDifficulty);

        return result;
    }

    public static void importShortString(String text) {
        String[] parts = text.split("\\|");

        // Check version compatibility?
        if(!FileUtils.VERSION.equals(parts[0])) {
            // Show pop up that the version changed
            int version_mismatch = JOptionPane.showConfirmDialog(null, "These settings were generated with a different version of the randomizer. Do you  still want to try loading them?", "Version Mismatch", JOptionPane.YES_NO_OPTION);

            if(version_mismatch != JOptionPane.OK_OPTION) {
                return;
            }
        }

        // Parse seed from string
        int seed = Integer.parseInt(parts[1], 16);

        // Parse boolean settings from string
        int booleanSettingsFlag = Integer.parseInt(parts[2], 16);

        singleton.shopRandomization = ShopRandomizationEnum.values()[booleanSettingsFlag & 0x3];
        booleanSettingsFlag >>= 2;

        BiFunction<Integer, Integer, Boolean> getBoolFlagFromInt = (startingVal, flagIdx) -> intToBool((startingVal >> flagIdx) & 0x1);

        singleton.fullItemAccess = getBoolFlagFromInt.apply(booleanSettingsFlag, 9);
        singleton.automaticHardmode = getBoolFlagFromInt.apply(booleanSettingsFlag, 8);
        singleton.coinChestGraphics = getBoolFlagFromInt.apply(booleanSettingsFlag, 7);
        singleton.requireSoftwareComboForKeyFairy = getBoolFlagFromInt.apply(booleanSettingsFlag, 6);
        singleton.requireIceCapeForLava = getBoolFlagFromInt.apply(booleanSettingsFlag, 5);
        singleton.requireFlaresForExtinction = getBoolFlagFromInt.apply(booleanSettingsFlag, 4);
        singleton.randomizeForbiddenTreasure = getBoolFlagFromInt.apply(booleanSettingsFlag, 3);
        singleton.randomizeCoinChests = getBoolFlagFromInt.apply(booleanSettingsFlag, 2);
        singleton.replaceMapsWithWeights = getBoolFlagFromInt.apply(booleanSettingsFlag, 1);
        singleton.automaticGrailPoints = getBoolFlagFromInt.apply(booleanSettingsFlag, 0);

        Collection<String> glitches = intToItemSet(Integer.parseInt(parts[3],16), singleton.possibleGlitches);
        Collection<String> dboosts = intToItemSet(Integer.parseInt(parts[4],16), singleton.possibleDboosts);
        Set<String> nonRandoItems = new HashSet<>(intToItemSet(Integer.parseInt(parts[5],16), singleton.possibleRandomizedItems));
        Set<String> initItems = new HashSet<>(intToItemSet(Integer.parseInt(parts[6],16), singleton.possibleRandomizedItems));
        Set<String> surfaceItems = new HashSet<>(intToItemSet(Integer.parseInt(parts[7],16), singleton.possibleRandomizedItems));
        String xmailerItem = singleton.possibleRandomizedItems.get(Integer.parseInt(parts[8],16));
        BossDifficulty bossDifficulty = BossDifficulty.values()[Integer.parseInt(parts[9],16)];

        setStartingSeed(seed);
        setEnabledGlitches((List<String>) glitches, true);
        setEnabledDamageBoosts((List<String>) dboosts, true);
        setNonRandomizedItems(nonRandoItems, true);
        setInitiallyAccessibleItems(initItems, true);
        setSurfaceItems(surfaceItems, true);
        setXmailerItem(xmailerItem, true);
        setBossDifficulty(bossDifficulty.toString(), true);

        JOptionPane.showMessageDialog(null, "Settings successfully imported");
    }



}
