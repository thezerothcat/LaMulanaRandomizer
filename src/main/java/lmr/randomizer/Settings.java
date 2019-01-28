package lmr.randomizer;

import lmr.randomizer.random.BossDifficulty;
import lmr.randomizer.random.ShopRandomizationEnum;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.math.BigInteger;

/**
 * Created by thezerothcat on 7/20/2017.
 */
public final class Settings {
    public static final int MAX_RANDOM_REMOVED_ITEMS_CURRENTLY_SUPPORTED = 40;

    public static Set<String> currentRemovedItems;
    public static List<String> currentCursedChests;
    public static String currentStartingWeapon;

    private static Settings singleton = new Settings();

    private long startingSeed;

    private boolean changed = false;

    private boolean automaticHardmode;
    private boolean coinChestGraphics;
    private boolean requireSoftwareComboForKeyFairy;
    private boolean requireFullAccess;
    private boolean requireIceCapeForLava;
    private boolean requireFlaresForExtinction;
    private boolean randomizeXmailer;
    private boolean randomizeForbiddenTreasure;
    private boolean htFullRandom;
    private boolean randomizeDracuetShop;
    private boolean randomizeCoinChests;
    private boolean randomizeTrapItems;
    private boolean randomizeEscapeChest;
    private boolean allowWhipStart;
    private boolean allowMainWeaponStart;
    private boolean allowSubweaponStart;
    private boolean subweaponOnlyLogic;
    private boolean removeMainWeapons;
    private boolean randomizeCursedChests;
    private boolean randomizeTransitionGates;
    private boolean randomizeOneWayTransitions;
    private boolean randomizeBacksideDoors;
    private boolean replaceMapsWithWeights;
    private boolean automaticGrailPoints;
    private boolean automaticTranslations;
    private boolean ushumgalluAssist;

    private boolean alternateMotherAnkh;
    private boolean automaticMantras;
    private String medicineColor;

    private boolean removeSpaulder;

    private Integer skipValidation;

    private List<String> enabledGlitches = new ArrayList<>();
    private List<String> enabledDamageBoosts = new ArrayList<>();

    private static final String[] possibleRandomizedItems = {"Holy Grail", "Hand Scanner", "reader.exe",
            "Hermes' Boots", "Grapple Claw", "Feather", "Isis' Pendant", "Bronze Mirror", "mirai.exe", "bunemon.exe", "Ring"};

    private String laMulanaBaseDir;
    private String laMulanaSaveDir;
    private String language;

    private Set<String> nonRandomizedItems = new HashSet<>();
    private Set<String> initiallyAccessibleItems = new HashSet<>();
    private Set<String> startingItems = new HashSet<>();

    private int minRandomRemovedItems;
    private int maxRandomRemovedItems;

    private BossDifficulty bossDifficulty;
    private ShopRandomizationEnum shopRandomization;

    private Settings() {
        startingSeed = new Random().nextInt(Integer.MAX_VALUE);
        laMulanaBaseDir = "Please enter your La-Mulana install directory";
        language = "en";

        requireSoftwareComboForKeyFairy = true;
        requireFullAccess = true;
        requireIceCapeForLava = true;
        requireFlaresForExtinction = true;
        randomizeXmailer = true;
        randomizeForbiddenTreasure = false;
        htFullRandom = false;
        randomizeDracuetShop = false;
        randomizeCoinChests = true;
        randomizeTrapItems = true;
        randomizeEscapeChest = false;
        allowWhipStart = true;
        allowMainWeaponStart = false;
        allowSubweaponStart = false;
        subweaponOnlyLogic = false;
        removeMainWeapons = false;
        randomizeCursedChests = false;
        randomizeTransitionGates = false;
        randomizeOneWayTransitions = false;
        randomizeBacksideDoors = false;
        removeSpaulder = false;
        replaceMapsWithWeights = false;
        automaticHardmode = false;
        coinChestGraphics = false;
        automaticGrailPoints = false;
        automaticTranslations = false;
        ushumgalluAssist = false;

        alternateMotherAnkh = false;
        automaticMantras = false;
        medicineColor = null;

        skipValidation = null;

        bossDifficulty = BossDifficulty.MEDIUM;
        shopRandomization = ShopRandomizationEnum.EVERYTHING;

        minRandomRemovedItems = 0;
        maxRandomRemovedItems = 0;

        for (String filename : Arrays.asList("C:\\Games\\La-Mulana Remake 1.3.3.1", "C:\\GOG Games\\La-Mulana", "C:\\GOG Games\\La-Mulana",
                "C:\\Steam\\steamapps\\common\\La-Mulana", "C:\\Program Files (x86)\\Steam\\steamapps\\common\\La-Mulana",
                "C:\\Program Files\\Steam\\steamapps\\common\\La-Mulana", "C:\\Program Files (x86)\\GOG Galaxy\\Games\\La Mulana",
                "C:\\Program Files (x86)\\GOG.com\\La-Mulana"
                /* Steam on Linux path? */)) {
            if (new File(filename).exists()) {
                laMulanaBaseDir = filename;
                break;
            }
        }
        
        try {
            // Try to find the GOG game on Linux
            // Also honor file system hierachy (local installs supersede global installs)
            for (String menu_entry_file_path : Arrays.asList(
                    "/usr/share/applications/gog_com-La_Mulana_1.desktop",
                    "/usr/local/share/applications/gog_com-La_Mulana_1.desktop",
                    System.getProperty("user.home") + "/.local/share/applications/gog_com-La_Mulana_1.desktop",
                    System.getProperty("user.home") + "/Desktop/gog_com-La_Mulana_1.desktop"
                    /* other valid paths for the .desktop file to be located? */)) {
                
                File menu_entry_file = new File(menu_entry_file_path);
                if (!menu_entry_file.exists()) {
                    continue; // Try next item if file doesn't exist
                }
                
                List<String> menu_file_lines = Files.readAllLines(menu_entry_file.toPath());
                menu_file_lines.removeIf(l -> !l.startsWith("Path="));
                
                if (menu_file_lines.size() != 1) {
                    continue; // File is malformed, there should be exactly one "Path=..." line
                }
                
                laMulanaBaseDir = menu_file_lines.get(0).substring(5);
            }
            
            // The GOG version has some fluff around the *actual* game install, moving it into the
            // "game" subdirectory. If it exists, then just use that, otherwise the rcdReader won't
            // be able to find the necessary files!
            File dir = new File(laMulanaBaseDir, "game");
            if (dir.exists() && dir.isDirectory()) {
                laMulanaBaseDir += "/game";
            }
        } catch (Exception e) {
            // do nothing
        }
    }

    public static boolean isChanged() {
        return singleton.changed;
    }

    public static long getStartingSeed() {
        return singleton.startingSeed;
    }

    public static void setStartingSeed(int startingSeed) {
        singleton.startingSeed = startingSeed;
    }

    public static String getLaMulanaBaseDir() {
        return singleton.laMulanaBaseDir;
    }

    public static void setLaMulanaBaseDir(String laMulanaBaseDir, boolean update) {
        if(update && !laMulanaBaseDir.equals(singleton.laMulanaBaseDir)) {
            singleton.changed = true;
        }
        singleton.laMulanaBaseDir = laMulanaBaseDir;
    }

    public static String getLaMulanaSaveDir() {
        return singleton.laMulanaSaveDir;
    }

    public static void setLaMulanaSaveDir(String laMulanaSaveDir, boolean update) {
        if(update && !laMulanaSaveDir.equals(singleton.laMulanaSaveDir)) {
            singleton.changed = true;
        }
        singleton.laMulanaSaveDir = laMulanaSaveDir;
    }

    public static String getLanguage() {
        return singleton.language;
    }

    public static void setLanguage(String language, boolean update) {
        if(update && !language.equals(singleton.language)) {
            singleton.changed = true;
        }
        singleton.language = language;
    }

    public static String getBackupDatFile() {
        if("en".equals(singleton.language)) {
            return "script_code.dat.bak";
        }
        return "script_code_" + singleton.language + ".dat.bak";
    }

    public static ShopRandomizationEnum getShopRandomization() {
        return singleton.shopRandomization;
    }

    public static void setShopRandomization(String shopRandomization, boolean update) {
        if(update && !shopRandomization.equals(singleton.shopRandomization.toString())) {
            singleton.changed = true;
        }
        singleton.shopRandomization = ShopRandomizationEnum.valueOf(shopRandomization);
    }

    public static BossDifficulty getBossDifficulty() {
        return singleton.bossDifficulty;
    }

    public static void setBossDifficulty(String bossDifficulty, boolean update) {
        if(update && !bossDifficulty.equals(singleton.bossDifficulty.toString())) {
            singleton.changed = true;
        }
        singleton.bossDifficulty = BossDifficulty.valueOf(bossDifficulty);
    }

    public static boolean isRequireSoftwareComboForKeyFairy() {
        return singleton.requireSoftwareComboForKeyFairy;
    }

    public static void setRequireSoftwareComboForKeyFairy(boolean requireSoftwareComboForKeyFairy, boolean update) {
        if(update && requireSoftwareComboForKeyFairy != singleton.requireSoftwareComboForKeyFairy) {
            singleton.changed = true;
        }
        singleton.requireSoftwareComboForKeyFairy = requireSoftwareComboForKeyFairy;
    }

    public static boolean isRequireFullAccess() {
        return singleton.requireFullAccess;
    }

    public static void setRequireFullAccess(boolean requireFullAccess, boolean update) {
        if(update && requireFullAccess != singleton.requireFullAccess) {
            singleton.changed = true;
        }
        singleton.requireFullAccess = requireFullAccess;
    }

    public static boolean isRequireIceCapeForLava() {
        return singleton.requireIceCapeForLava;
    }

    public static void setRequireIceCapeForLava(boolean requireIceCapeForLava, boolean update) {
        if(update && requireIceCapeForLava != singleton.requireIceCapeForLava) {
            singleton.changed = true;
        }
        singleton.requireIceCapeForLava = requireIceCapeForLava;
    }

    public static boolean isRequireFlaresForExtinction() {
        return singleton.requireFlaresForExtinction;
    }

    public static void setRequireFlaresForExtinction(boolean requireFlaresForExtinction, boolean update) {
        if(update && requireFlaresForExtinction != singleton.requireFlaresForExtinction) {
            singleton.changed = true;
        }
        singleton.requireFlaresForExtinction = requireFlaresForExtinction;
    }

    public static boolean isRandomizeXmailer() {
        return singleton.randomizeXmailer;
    }

    public static void setRandomizeXmailer(boolean randomizeXmailer, boolean update) {
        if(update && randomizeXmailer != singleton.randomizeXmailer) {
            singleton.changed = true;
        }
        singleton.randomizeXmailer = randomizeXmailer;
    }

    public static boolean isRandomizeForbiddenTreasure() {
        return singleton.randomizeForbiddenTreasure;
    }

    public static void setRandomizeForbiddenTreasure(boolean randomizeForbiddenTreasure, boolean update) {
        if(update && randomizeForbiddenTreasure != singleton.randomizeForbiddenTreasure) {
            singleton.changed = true;
        }
        singleton.randomizeForbiddenTreasure = randomizeForbiddenTreasure;
    }

    public static boolean isHTFullRandom() {
        return singleton.htFullRandom;
    }

    public static void setHTFullRandom(boolean htFullRandom, boolean update) {
        if(update && htFullRandom != singleton.htFullRandom) {
            singleton.changed = true;
        }
        singleton.htFullRandom = htFullRandom;
    }

    public static boolean isRandomizeDracuetShop() {
        return singleton.randomizeDracuetShop;
    }

    public static void setRandomizeDracuetShop(boolean randomizeDracuetShop, boolean update) {
        if(update && randomizeDracuetShop != singleton.randomizeDracuetShop) {
            singleton.changed = true;
        }
        singleton.randomizeDracuetShop = randomizeDracuetShop;
    }

    public static boolean isRandomizeCoinChests() {
        return singleton.randomizeCoinChests;
    }

    public static void setRandomizeCoinChests(boolean randomizeCoinChests, boolean update) {
        if(update && randomizeCoinChests != singleton.randomizeCoinChests) {
            singleton.changed = true;
        }
        singleton.randomizeCoinChests = randomizeCoinChests;
    }

    public static boolean isRandomizeTrapItems() {
        return singleton.randomizeTrapItems;
    }

    public static void setRandomizeTrapItems(boolean randomizeTrapItems, boolean update) {
        if(update && randomizeTrapItems != singleton.randomizeTrapItems) {
            singleton.changed = true;
        }
        singleton.randomizeTrapItems = randomizeTrapItems;
    }

    public static boolean isRandomizeEscapeChest() {
        return singleton.randomizeEscapeChest;
    }

    public static void setRandomizeEscapeChest(boolean randomizeEscapeChest, boolean update) {
        if(update && randomizeEscapeChest != singleton.randomizeEscapeChest) {
            singleton.changed = true;
        }
        singleton.randomizeEscapeChest = randomizeEscapeChest;
    }

    public static boolean isAllowWhipStart() {
        return singleton.allowWhipStart;
    }

    public static void setAllowWhipStart(boolean allowWhipStart, boolean update) {
        if(update && allowWhipStart != singleton.allowWhipStart) {
            singleton.changed = true;
        }
        singleton.allowWhipStart = allowWhipStart;
    }

    public static boolean isAllowMainWeaponStart() {
        return singleton.allowMainWeaponStart;
    }

    public static void setAllowMainWeaponStart(boolean allowMainWeaponStart, boolean update) {
        if(update && allowMainWeaponStart != singleton.allowMainWeaponStart) {
            singleton.changed = true;
        }
        singleton.allowMainWeaponStart = allowMainWeaponStart;
    }

    public static boolean isAllowSubweaponStart() {
        return singleton.allowSubweaponStart;
    }

    public static void setAllowSubweaponStart(boolean allowSubweaponStart, boolean update) {
        if(update && allowSubweaponStart != singleton.allowSubweaponStart) {
            singleton.changed = true;
        }
        singleton.allowSubweaponStart = allowSubweaponStart;
    }

    public static boolean isSubweaponOnlyLogic() {
        return singleton.subweaponOnlyLogic;
    }

    public static void setSubweaponOnlyLogic(boolean subweaponOnlyLogic, boolean update) {
        if(update && subweaponOnlyLogic != singleton.subweaponOnlyLogic) {
            singleton.changed = true;
        }
        singleton.subweaponOnlyLogic = subweaponOnlyLogic;
    }

    public static boolean isRemoveMainWeapons() {
        return singleton.removeMainWeapons;
    }

    public static void setRemoveMainWeapons(boolean removeMainWeapons, boolean update) {
        if(update && removeMainWeapons != singleton.removeMainWeapons) {
            singleton.changed = true;
        }
        singleton.removeMainWeapons = removeMainWeapons;
    }

    public static boolean isRandomizeCursedChests() {
        return singleton.randomizeCursedChests;
    }

    public static void setRandomizeCursedChests(boolean randomizeCursedChests, boolean update) {
        if(update && randomizeCursedChests != singleton.randomizeCursedChests) {
            singleton.changed = true;
        }
        singleton.randomizeCursedChests = randomizeCursedChests;
    }

    public static boolean isRandomizeTransitionGates() {
        return singleton.randomizeTransitionGates;
    }

    public static void setRandomizeTransitionGates(boolean randomizeTransitionGates, boolean update) {
        if(update && randomizeTransitionGates != singleton.randomizeTransitionGates) {
            singleton.changed = true;
        }
        singleton.randomizeTransitionGates = randomizeTransitionGates;
    }

    public static boolean isRandomizeOneWayTransitions() {
        return singleton.randomizeOneWayTransitions;
    }

    public static void setRandomizeOneWayTransitions(boolean randomizeOneWayTransitions, boolean update) {
        if(update && randomizeOneWayTransitions != singleton.randomizeOneWayTransitions) {
            singleton.changed = true;
        }
        singleton.randomizeOneWayTransitions = randomizeOneWayTransitions;
    }

    public static boolean isRandomizeBacksideDoors() {
        return singleton.randomizeBacksideDoors;
    }

    public static void setRandomizeBacksideDoors(boolean randomizeBacksideDoors, boolean update) {
        if(update && randomizeBacksideDoors != singleton.randomizeBacksideDoors) {
            singleton.changed = true;
        }
        singleton.randomizeBacksideDoors = randomizeBacksideDoors;
    }

    public static boolean isReplaceMapsWithWeights() { return singleton.replaceMapsWithWeights; }

    public static void setReplaceMapsWithWeights(boolean replaceMapsWithWeights, boolean update) {
        if(update && replaceMapsWithWeights != singleton.replaceMapsWithWeights) {
            singleton.changed = true;
        }
        singleton.replaceMapsWithWeights = replaceMapsWithWeights;
    }

    public static boolean isAutomaticHardmode() {
        return singleton.automaticHardmode;
    }

    public static void setAutomaticHardmode(boolean automaticHardmode, boolean update) {
        if(update && automaticHardmode != singleton.automaticHardmode) {
            singleton.changed = true;
        }
        singleton.automaticHardmode = automaticHardmode;
    }

    public static boolean isAutomaticGrailPoints() {
        return singleton.automaticGrailPoints;
    }

    public static void setAutomaticGrailPoints(boolean automaticGrailPoints, boolean update) {
        if(update && automaticGrailPoints != singleton.automaticGrailPoints) {
            singleton.changed = true;
        }
        singleton.automaticGrailPoints = automaticGrailPoints;
    }

    public static boolean isAutomaticTranslations() {
        return singleton.automaticTranslations;
    }

    public static void setAutomaticTranslations(boolean automaticTranslations, boolean update) {
        if(update && automaticTranslations != singleton.automaticTranslations) {
            singleton.changed = true;
        }
        singleton.automaticTranslations = automaticTranslations;
    }

    public static boolean isUshumgalluAssist() {
        return singleton.ushumgalluAssist;
    }

    public static void setUshumgalluAssist(boolean ushumgalluAssist, boolean update) {
        if(update && ushumgalluAssist != singleton.ushumgalluAssist) {
            singleton.changed = true;
        }
        singleton.ushumgalluAssist = ushumgalluAssist;
    }

    public static boolean isCoinChestGraphics() {
        return singleton.coinChestGraphics;
    }

    public static void setCoinChestGraphics(boolean coinChestGraphics, boolean update) {
        if(update && coinChestGraphics != singleton.coinChestGraphics) {
            singleton.changed = true;
        }
        singleton.coinChestGraphics = coinChestGraphics;
    }

    public static Set<String> getNonRandomizedItems() {
        return singleton.nonRandomizedItems;
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

    public static Set<String> getStartingItems() {
        return singleton.startingItems;
    }

    public static Set<String> getStartingItemsIncludingCustom() {
        Set<String> startingItems = new HashSet<>(singleton.startingItems);
        startingItems.addAll(DataFromFile.getCustomPlacementData().getStartingItems());
        return startingItems;
    }

    public static void setStartingItems(Set<String> startingItems, boolean update) {
        if(update && !singleton.changed) {
            if(startingItems.containsAll(singleton.startingItems)) {
                singleton.changed = !singleton.startingItems.containsAll(startingItems);
            }
            else {
                singleton.changed = true;
            }
        }
        singleton.startingItems = startingItems;
    }

    public static Set<String> getInitiallyAccessibleItems() {
        return singleton.initiallyAccessibleItems;
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

    public static List<String> getEnabledGlitches() {
        return singleton.enabledGlitches;
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

    public static List<String> getEnabledDamageBoosts() {
        return singleton.enabledDamageBoosts;
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

    public static int getMinRandomRemovedItems() {
        return singleton.minRandomRemovedItems;
    }

    public static void setMinRandomRemovedItems(int minRandomRemovedItems, boolean update) {
        if(minRandomRemovedItems > MAX_RANDOM_REMOVED_ITEMS_CURRENTLY_SUPPORTED || minRandomRemovedItems < 0) {
            return;
        }
        if(update && minRandomRemovedItems != singleton.minRandomRemovedItems) {
            singleton.changed = true;
        }
        singleton.minRandomRemovedItems = minRandomRemovedItems;
    }

    public static int getMaxRandomRemovedItems() {
        return singleton.maxRandomRemovedItems;
    }

    public static void setMaxRandomRemovedItems(int maxRandomRemovedItems, boolean update) {
        if(maxRandomRemovedItems > MAX_RANDOM_REMOVED_ITEMS_CURRENTLY_SUPPORTED || maxRandomRemovedItems < 0) {
            return;
        }
        if(update && maxRandomRemovedItems != singleton.maxRandomRemovedItems) {
            singleton.changed = true;
        }
        singleton.maxRandomRemovedItems = maxRandomRemovedItems;
    }

    public static Set<String> getRemovedItems() {
        Set<String> removedItems = new HashSet<>();
        if(singleton.removeSpaulder) {
            removedItems.add("Spaulder");
        }
        if(singleton.removeMainWeapons) {
            removedItems.add("Whip");
            removedItems.add("Chain Whip");
            removedItems.add("Flail Whip");
            removedItems.add("Axe");
            removedItems.add("Knife");
            removedItems.add("Katana");
            removedItems.add("Key Sword");
        }
        removedItems.addAll(DataFromFile.getCustomPlacementData().getRemovedItems());
        return removedItems;
    }

    public static void setRemovedItem(String itemName, boolean isRemoved, boolean update) {
        if("Spaulder".equals(itemName)) {
            setRemoveSpaulder(isRemoved, update);
        }
    }

    public static void setRemoveSpaulder(boolean removeSpaulder, boolean update) {
        if(update && removeSpaulder != singleton.removeSpaulder) {
            singleton.changed = true;
        }
        singleton.removeSpaulder = removeSpaulder;
    }

    public static Set<String> getCurrentRemovedItems() {
        if(currentRemovedItems == null) {
            return new HashSet<>(0);
        }
        return currentRemovedItems;
    }

    public static void setCurrentRemovedItems(Set<String> currentRemovedItems) {
        singleton.currentRemovedItems = currentRemovedItems;
    }

    public static String getCurrentStartingWeapon() {
        return singleton.currentStartingWeapon == null ? "Whip" : singleton.currentStartingWeapon;
    }

    public static void setCurrentStartingWeapon(String currentStartingItem) {
        singleton.currentStartingWeapon = currentStartingItem;
    }

    public static List<String> getCurrentCursedChests() {
        if(singleton.randomizeCursedChests) {
            return singleton.currentCursedChests;
        }
        return Arrays.asList("Crystal Skull", "Djed Pillar", "Dimensional Key", "Magatama Jewel");
    }

    public static void setCurrentCursedChests(List<String> currentCursedChests) {
        singleton.currentCursedChests = currentCursedChests;
    }

    public static String getUpdatedContents(String originalContents) {
        if("Vessel".equals(originalContents)) {
            if(Settings.getMedicineColor() != null) {
                return String.format("Medicine of the Mind (%s)", Settings.getMedicineColor());
            }
        }
//        if("Djed Pillar".equals(originalContents)) {
//            newContents = "Ankh Jewel (Extra)";
//        }
        return originalContents;
    }

    public static String getMedicineColor() {
        return DataFromFile.getCustomPlacementData().getMedicineColor();
    }

    public static boolean isAlternateMotherAnkh() {
        return singleton.alternateMotherAnkh || DataFromFile.getCustomPlacementData().isAlternateMotherAnkh();
    }

    public static void setAlternateMotherAnkh(boolean alternateMotherAnkh) {
        singleton.alternateMotherAnkh = alternateMotherAnkh;
    }

    public static boolean isAutomaticMantras() {
        return singleton.automaticMantras || DataFromFile.getCustomPlacementData().isAutomaticMantras();
    }

    public static boolean isGenerationComplete(int attemptNumber) {
        return singleton.skipValidation != null
                && (singleton.skipValidation <= attemptNumber || singleton.skipValidation.equals(-1));
    }

    public static boolean isDetailedLoggingAttempt(Integer attemptNumber) {
        return singleton.skipValidation != null
                && (singleton.skipValidation.equals(attemptNumber) || singleton.skipValidation.equals(-1));
    }

    public static boolean isSkipValidation(int attemptNumber) {
        return singleton.skipValidation != null && attemptNumber < singleton.skipValidation;
    }

    public static void setSkipValidation(int skipValidationAttemptNumber) {
        singleton.skipValidation = skipValidationAttemptNumber;
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

    private static class SettingsInt {
        public static class SettingsIntValueError extends RuntimeException {
            public SettingsIntValueError() { super(); }
            public SettingsIntValueError(String message) { super(message); }
        }

        private BigInteger value = BigInteger.ZERO, range = BigInteger.ONE;

        private final String base32Digits = "CFHJKLMNPRTWXYbcdfgjkpqstwxyz349";

        public void appendSetting(long settingValue, long settingRange) {
            var biRange = BigInteger.valueOf(settingRange);
            assert(settingRange > 1);
            if (settingValue > settingRange)
                throw new SettingsIntValueError("Setting out of range");
            value = value.add(BigInteger.valueOf(settingValue).multiply(range));
            range = range.multiply(BigInteger.valueOf(settingRange));
        }

        public void appendSetting(Enum settingEnum) {
            appendSetting(settingEnum.ordinal(), settingEnum.getClass().getEnumConstants().length);
        }

        public long extractSetting(long settingRange) {
            var biRange = BigInteger.valueOf(settingRange);
            long ret = value.mod(biRange).longValueExact();
            value = value.divide(biRange);
            return ret;
        }

        public <T extends Enum> T extractSetting(Class<T> enumClass) {
            T[] enumConsts = enumClass.getEnumConstants();
            return enumConsts[(int)extractSetting(enumConsts.length)];
        }

        public String toBase32() {
            String base32 = "";
            do {
                BigInteger bits = value.shiftRight(base32.length() * 5).and(BigInteger.valueOf(31));
                base32 = base32 + base32Digits.charAt(bits.intValueExact());
            } while (base32.length() * 5 < value.bitLength());
            return base32;
        }

        // static fromBase32 seems like a pain
        public void setFromBase32(String base32) {
            value = BigInteger.ZERO;
            range = null;
            for (int i = 0; i < base32.length(); i++) {
                int n = base32Digits.indexOf(base32.charAt(i));
                if (n < 0)
                    throw new SettingsIntValueError("Base 32 string contains illegal character");
                value = value.add(BigInteger.valueOf(n).shiftLeft(i * 5));
            }
        }
    }

    public static String generateShortString() {
        var settingsInt = new SettingsInt();

        settingsInt.appendSetting(getStartingSeed(), 1L << 31);
        settingsInt.appendSetting(singleton.randomizeOneWayTransitions ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.randomizeEscapeChest ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.randomizeTransitionGates ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.removeMainWeapons ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.subweaponOnlyLogic ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.allowWhipStart ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.randomizeBacksideDoors ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.ushumgalluAssist ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.allowSubweaponStart ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.requireFullAccess ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.randomizeDracuetShop ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.randomizeXmailer ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.htFullRandom ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.automaticTranslations ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.allowMainWeaponStart ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.randomizeCursedChests ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.automaticHardmode ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.coinChestGraphics ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.requireSoftwareComboForKeyFairy ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.requireIceCapeForLava ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.requireFlaresForExtinction ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.randomizeForbiddenTreasure ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.randomizeCoinChests ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.randomizeTrapItems ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.removeSpaulder ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.replaceMapsWithWeights ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.automaticGrailPoints ? 1 : 0, 2);
        settingsInt.appendSetting(singleton.shopRandomization);

        for (var item : possibleRandomizedItems)
            settingsInt.appendSetting(getInitiallyAccessibleItems().contains(item) ? 1 : getStartingItems().contains(item) ? 2 : 0, 3);
        for (var glitch : DataFromFile.POSSIBLE_GLITCHES)
            settingsInt.appendSetting(getEnabledGlitches().contains(glitch) ? 1 : 0, 2);
        for (var dboost : DataFromFile.POSSIBLE_DBOOSTS)
            settingsInt.appendSetting(getEnabledDamageBoosts().contains(dboost) ? 1 : 0, 2);

        settingsInt.appendSetting(singleton.bossDifficulty);
        settingsInt.appendSetting(singleton.minRandomRemovedItems, MAX_RANDOM_REMOVED_ITEMS_CURRENTLY_SUPPORTED);
        settingsInt.appendSetting(Math.max(0, singleton.maxRandomRemovedItems - singleton.minRandomRemovedItems), MAX_RANDOM_REMOVED_ITEMS_CURRENTLY_SUPPORTED - singleton.minRandomRemovedItems);

        return FileUtils.VERSION + "-" + settingsInt.toBase32();
    }

    public static void importShortString(String text) {
        // Check version compatibility?
        if (!text.startsWith(FileUtils.VERSION + "-")) {
            // Show pop up that the version changed
            int version_mismatch = JOptionPane.showConfirmDialog(null, "These settings were generated with a different version of the randomizer. Do you  still want to try loading them?", "Version Mismatch", JOptionPane.YES_NO_OPTION);

            if(version_mismatch != JOptionPane.OK_OPTION) {
                return;
            }
        }

        var settingsInt = new SettingsInt();

        try {
            settingsInt.setFromBase32(text.split("-", 2)[1]);
        } catch (SettingsInt.SettingsIntValueError ex) {
            JOptionPane.showMessageDialog(null, "Settings string malformed for this version");
            return;
        }

        setStartingSeed((int)settingsInt.extractSetting(1L << 31));
        singleton.randomizeOneWayTransitions = settingsInt.extractSetting(2) != 0;
        singleton.randomizeEscapeChest = settingsInt.extractSetting(2) != 0;
        singleton.randomizeTransitionGates = settingsInt.extractSetting(2) != 0;
        singleton.removeMainWeapons = settingsInt.extractSetting(2) != 0;
        singleton.subweaponOnlyLogic = settingsInt.extractSetting(2) != 0;
        singleton.allowWhipStart = settingsInt.extractSetting(2) != 0;
        singleton.randomizeBacksideDoors = settingsInt.extractSetting(2) != 0;
        singleton.ushumgalluAssist = settingsInt.extractSetting(2) != 0;
        singleton.allowSubweaponStart = settingsInt.extractSetting(2) != 0;
        singleton.requireFullAccess = settingsInt.extractSetting(2) != 0;
        singleton.randomizeDracuetShop = settingsInt.extractSetting(2) != 0;
        singleton.randomizeXmailer = settingsInt.extractSetting(2) != 0;
        singleton.htFullRandom = settingsInt.extractSetting(2) != 0;
        singleton.automaticTranslations = settingsInt.extractSetting(2) != 0;
        singleton.allowMainWeaponStart = settingsInt.extractSetting(2) != 0;
        singleton.randomizeCursedChests = settingsInt.extractSetting(2) != 0;
        singleton.automaticHardmode = settingsInt.extractSetting(2) != 0;
        singleton.coinChestGraphics = settingsInt.extractSetting(2) != 0;
        singleton.requireSoftwareComboForKeyFairy = settingsInt.extractSetting(2) != 0;
        singleton.requireIceCapeForLava = settingsInt.extractSetting(2) != 0;
        singleton.requireFlaresForExtinction = settingsInt.extractSetting(2) != 0;
        singleton.randomizeForbiddenTreasure = settingsInt.extractSetting(2) != 0;
        singleton.randomizeCoinChests = settingsInt.extractSetting(2) != 0;
        singleton.randomizeTrapItems = settingsInt.extractSetting(2) != 0;
        singleton.removeSpaulder = settingsInt.extractSetting(2) != 0;
        singleton.replaceMapsWithWeights = settingsInt.extractSetting(2) != 0;
        singleton.automaticGrailPoints = settingsInt.extractSetting(2) != 0;
        singleton.shopRandomization = settingsInt.extractSetting(ShopRandomizationEnum.class);

        singleton.initiallyAccessibleItems.clear();
        singleton.startingItems.clear();
        for (var item : possibleRandomizedItems) {
            switch ((int)settingsInt.extractSetting(3)) {
                case 1:
                    singleton.initiallyAccessibleItems.add(item);
                    break;
                case 2:
                    singleton.startingItems.add(item);
                    break;
                default:
                    break;
            }
        }
        singleton.enabledGlitches.clear();
        for (var glitch : DataFromFile.POSSIBLE_GLITCHES)
            if (settingsInt.extractSetting(2) != 0)
                singleton.enabledGlitches.add(glitch);
        singleton.enabledDamageBoosts.clear();
        for (var dboost : DataFromFile.POSSIBLE_DBOOSTS)
            if (settingsInt.extractSetting(2) != 0)
                singleton.enabledDamageBoosts.add(dboost);

        singleton.bossDifficulty = settingsInt.extractSetting(BossDifficulty.class);
        singleton.minRandomRemovedItems = (int)settingsInt.extractSetting(MAX_RANDOM_REMOVED_ITEMS_CURRENTLY_SUPPORTED);
        singleton.maxRandomRemovedItems = singleton.minRandomRemovedItems + (int)settingsInt.extractSetting(MAX_RANDOM_REMOVED_ITEMS_CURRENTLY_SUPPORTED - singleton.minRandomRemovedItems);

        singleton.changed = true;

        JOptionPane.showMessageDialog(null, "Settings successfully imported");
    }
}
