package lmr.randomizer;

import lmr.randomizer.random.BossDifficulty;
import lmr.randomizer.random.ShopRandomizationEnum;
import lmr.randomizer.update.LocationCoordinateMapper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Created by thezerothcat on 7/20/2017.
 */
public final class Settings {
    public static final int MAX_RANDOM_REMOVED_ITEMS_CURRENTLY_SUPPORTED = 99;

    public static Set<String> currentRemovedItems;
    public static List<String> currentCursedChests;
    public static String currentStartingWeapon;
    public static Integer currentStartingLocation;

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
    private boolean includeHellTempleNPCs;
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
    private boolean randomizeNonBossDoors;
    private boolean replaceMapsWithWeights;
    private boolean automaticGrailPoints;
    private boolean automaticTranslations;
    private boolean ushumgalluAssist;
    private boolean bossSpecificAnkhJewels;
    private boolean blockPushingRequiresGlove;
    private boolean screenshakeDisabled;

    private boolean alternateMotherAnkh;
    private boolean automaticMantras;
    private String medicineColor;

    private boolean randomizeStartingLocation;
    private boolean randomizeBosses;
    private boolean randomizeEnemies;
    private boolean randomizeGraphics;


    private boolean removeSpaulder;

    private Integer skipValidation;

    private List<String> enabledGlitches = new ArrayList<>();
    private List<String> enabledDamageBoosts = new ArrayList<>();

    private List<String> possibleRandomizedItems = Arrays.asList("Holy Grail", "Hand Scanner", "reader.exe",
            "Hermes' Boots", "Grapple Claw", "Feather", "Isis' Pendant", "Bronze Mirror", "mirai.exe", "bunemon.exe", "Ring");

    private String laMulanaBaseDir;
    private String laMulanaSaveDir;
    private String graphicsPack;
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
        requireFullAccess = false;
        requireIceCapeForLava = true;
        requireFlaresForExtinction = true;
        randomizeXmailer = true;
        randomizeForbiddenTreasure = false;
        htFullRandom = false;
        randomizeDracuetShop = false;
        includeHellTempleNPCs = false;
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
        randomizeNonBossDoors = false;
        bossSpecificAnkhJewels = false;
        blockPushingRequiresGlove = false;
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

        randomizeStartingLocation = false;
        randomizeBosses = false;
        randomizeEnemies = false;
        randomizeGraphics = false;

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

    public static String getGraphicsPack() {
        if(singleton.graphicsPack == null) {
            return "00";
        }
        return singleton.graphicsPack;
    }

    public static void setGraphicsPack(String graphicsPack, boolean update) {
        if(update && !graphicsPack.equals(singleton.graphicsPack)) {
            singleton.changed = true;
        }
        singleton.graphicsPack = graphicsPack;
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

    public static boolean isRandomizeNonBossDoors() {
        return singleton.randomizeNonBossDoors;
    }

    public static void setRandomizeNonBossDoors(boolean randomizeNonBossDoors, boolean update) {
        if(update && randomizeNonBossDoors != singleton.randomizeNonBossDoors) {
            singleton.changed = true;
        }
        singleton.randomizeNonBossDoors = randomizeNonBossDoors;
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

    public static boolean isBossSpecificAnkhJewels() {
        return singleton.bossSpecificAnkhJewels;
    }

    public static void setBossSpecificAnkhJewels(boolean bossSpecificAnkhJewels, boolean update) {
        if(update && bossSpecificAnkhJewels != singleton.bossSpecificAnkhJewels) {
            singleton.changed = true;
        }
        singleton.bossSpecificAnkhJewels = bossSpecificAnkhJewels;
    }

    public static boolean isBlockPushingRequiresGlove() {
        return singleton.blockPushingRequiresGlove;
    }

    public static void setBlockPushingRequiresGlove(boolean blockPushingRequiresGlove, boolean update) {
        if(update && blockPushingRequiresGlove != singleton.blockPushingRequiresGlove) {
            singleton.changed = true;
        }
        singleton.blockPushingRequiresGlove = blockPushingRequiresGlove;
    }

    public static boolean isScreenshakeDisabled() {
        return singleton.screenshakeDisabled;
    }

    public static void setScreenshakeDisabled(boolean screenshakeDisabled, boolean update) {
        if(update && screenshakeDisabled != singleton.screenshakeDisabled) {
            singleton.changed = true;
        }
        singleton.screenshakeDisabled = screenshakeDisabled;
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
        if(Settings.getCurrentStartingLocation() == 7) {
            startingItems.add("Twin Statue");
        }
        if(LocationCoordinateMapper.getStartingZone() == 13) {
            startingItems.add("Plane Model");
        }
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

    public static int getCurrentStartingLocation() {
        return singleton.currentStartingLocation == null ? 1 : singleton.currentStartingLocation;
    }

    public static void setCurrentStartingLocation(int currentStartingLocation) {
        singleton.currentStartingLocation = currentStartingLocation;
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

//    public static boolean isFoolsMode() {
//        return false;
//    }

    public static boolean isHalloweenMode() {
        return true;
    }

    public static boolean isIncludeHellTempleNPCs() {
        return singleton.includeHellTempleNPCs;
    }

    public static void setIncludeHellTempleNPCs(boolean includeHellTempleNPCs, boolean update) {
        if(update && includeHellTempleNPCs != singleton.includeHellTempleNPCs) {
            singleton.changed = true;
        }
        singleton.includeHellTempleNPCs = includeHellTempleNPCs;
    }

    public static boolean isAlternateMotherAnkh() {
        return singleton.alternateMotherAnkh || DataFromFile.getCustomPlacementData().isAlternateMotherAnkh();
    }

    public static void setAlternateMotherAnkh(boolean alternateMotherAnkh, boolean update) {
        if(update && alternateMotherAnkh != singleton.alternateMotherAnkh) {
            singleton.changed = true;
        }
        singleton.alternateMotherAnkh = alternateMotherAnkh;
    }

    public static String getMedicineColor() {
        String customMedicineColor = DataFromFile.getCustomPlacementData().getMedicineColor();
        return customMedicineColor == null ? singleton.medicineColor : customMedicineColor;
    }

    public static void setMedicineColor(String medicineColor) {
        singleton.medicineColor = medicineColor;
    }

    public static boolean isRandomizeStartingLocation() {
        return singleton.randomizeStartingLocation;
    }

    public static void setRandomizeStartingLocation(boolean randomizeStartingLocation, boolean update) {
        if(update && randomizeStartingLocation != singleton.randomizeStartingLocation) {
            singleton.changed = true;
        }
        singleton.randomizeStartingLocation = randomizeStartingLocation;
    }

    public static boolean isRandomizeBosses() {
        return singleton.randomizeBosses;
    }

    public static void setRandomizeBosses(boolean randomizeBosses, boolean update) {
        if(update && randomizeBosses != singleton.randomizeBosses) {
            singleton.changed = true;
        }
        singleton.randomizeBosses = randomizeBosses;
    }

    public static boolean isRandomizeEnemies() {
        return singleton.randomizeEnemies;
    }

    public static void setRandomizeEnemies(boolean randomizeEnemies, boolean update) {
        if(update && randomizeEnemies != singleton.randomizeEnemies) {
            singleton.changed = true;
        }
        singleton.randomizeEnemies = randomizeEnemies;
    }

    public static boolean isRandomizeGraphics() {
        return singleton.randomizeGraphics;
    }

    public static void setRandomizeGraphics(boolean randomizeGraphics, boolean update) {
        if(update && randomizeGraphics != singleton.randomizeGraphics) {
            singleton.changed = true;
        }
        singleton.randomizeGraphics = randomizeGraphics;
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

        String separator = "-";

        //seed

        //boolean fields + shoprandomization enum
        BiFunction<Boolean, Integer, Integer> processBooleanFlag = (Boolean b, Integer flagIndex) -> boolToInt(b) << flagIndex;

        int booleanSettings = 0;
        booleanSettings |= processBooleanFlag.apply(singleton.alternateMotherAnkh, 28);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeNonBossDoors, 27);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeOneWayTransitions, 26);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeEscapeChest, 25);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeTransitionGates, 24);
        booleanSettings |= processBooleanFlag.apply(singleton.removeMainWeapons, 23);
        booleanSettings |= processBooleanFlag.apply(singleton.subweaponOnlyLogic, 22);
        booleanSettings |= processBooleanFlag.apply(singleton.allowWhipStart, 21);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeBacksideDoors, 20);
        booleanSettings |= processBooleanFlag.apply(singleton.ushumgalluAssist, 19);
        booleanSettings |= processBooleanFlag.apply(singleton.allowSubweaponStart, 18);
        booleanSettings |= processBooleanFlag.apply(singleton.requireFullAccess, 17);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeDracuetShop, 16);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeXmailer, 15);
        booleanSettings |= processBooleanFlag.apply(singleton.htFullRandom, 14);
        booleanSettings |= processBooleanFlag.apply(singleton.automaticTranslations, 13);
        booleanSettings |= processBooleanFlag.apply(singleton.allowMainWeaponStart, 12);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeCursedChests, 11);
        booleanSettings |= processBooleanFlag.apply(singleton.automaticHardmode, 10);
        booleanSettings |= processBooleanFlag.apply(singleton.coinChestGraphics, 9);
        booleanSettings |= processBooleanFlag.apply(singleton.requireSoftwareComboForKeyFairy, 8);
        booleanSettings |= processBooleanFlag.apply(singleton.requireIceCapeForLava, 7);
        booleanSettings |= processBooleanFlag.apply(singleton.requireFlaresForExtinction, 6);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeForbiddenTreasure, 5);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeCoinChests, 4);
        booleanSettings |= processBooleanFlag.apply(singleton.randomizeTrapItems, 3);
        booleanSettings |= processBooleanFlag.apply(singleton.removeSpaulder, 2);
        booleanSettings |= processBooleanFlag.apply(singleton.replaceMapsWithWeights, 1);
        booleanSettings |= processBooleanFlag.apply(singleton.automaticGrailPoints, 0);
        booleanSettings = booleanSettings << 2 | singleton.shopRandomization.ordinal();

        //glitches
        int glitches = itemSetToInt(getEnabledGlitches(), DataFromFile.POSSIBLE_GLITCHES);

        //dboosts
        int dboosts = itemSetToInt(getEnabledDamageBoosts(), DataFromFile.POSSIBLE_DBOOSTS);

        //initially accessible items
        int initItems = itemSetToInt(getInitiallyAccessibleItems(), singleton.possibleRandomizedItems);

        //starting items
        int startingItems = itemSetToInt(getStartingItems(), singleton.possibleRandomizedItems);

        // boss difficulty
        int bossDifficulty = singleton.bossDifficulty.ordinal();

        int booleanSettings2 = 0;
        booleanSettings2 |= processBooleanFlag.apply(singleton.screenshakeDisabled, 6);
        booleanSettings2 |= processBooleanFlag.apply(singleton.includeHellTempleNPCs, 5);
        booleanSettings2 |= processBooleanFlag.apply(singleton.blockPushingRequiresGlove, 4);
        booleanSettings2 |= processBooleanFlag.apply(singleton.randomizeGraphics, 3);
        booleanSettings2 |= processBooleanFlag.apply(singleton.randomizeEnemies, 2);
        booleanSettings2 |= processBooleanFlag.apply(singleton.randomizeBosses, 1);
        booleanSettings2 |= processBooleanFlag.apply(singleton.randomizeStartingLocation, 0);

        // combine the results of the settings in a string
        long startingSeed = getStartingSeed();
        result += separator + Long.toHexString(startingSeed);
        result += separator + Integer.toHexString(booleanSettings);
        result += separator + Integer.toHexString(glitches);
        result += separator + Integer.toHexString(dboosts);
        result += separator + Integer.toHexString(initItems);
        result += separator + Integer.toHexString(startingItems);
        result += separator + Integer.toHexString(bossDifficulty);
        result += separator + Integer.toHexString(singleton.minRandomRemovedItems);
        result += separator + Integer.toHexString(singleton.maxRandomRemovedItems);
        result += separator + Integer.toHexString(booleanSettings2);

        return result;
    }

    public static void importShortString(String text) {
        String[] parts = text.split("-");

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

        singleton.alternateMotherAnkh = getBoolFlagFromInt.apply(booleanSettingsFlag, 28);
        singleton.randomizeNonBossDoors = getBoolFlagFromInt.apply(booleanSettingsFlag, 27);
        singleton.randomizeOneWayTransitions = getBoolFlagFromInt.apply(booleanSettingsFlag, 26);
        singleton.randomizeEscapeChest = getBoolFlagFromInt.apply(booleanSettingsFlag, 25);
        singleton.randomizeTransitionGates = getBoolFlagFromInt.apply(booleanSettingsFlag, 24);
        singleton.removeMainWeapons = getBoolFlagFromInt.apply(booleanSettingsFlag, 23);
        singleton.subweaponOnlyLogic = getBoolFlagFromInt.apply(booleanSettingsFlag, 22);
        singleton.allowWhipStart = getBoolFlagFromInt.apply(booleanSettingsFlag, 21);
        singleton.randomizeBacksideDoors = getBoolFlagFromInt.apply(booleanSettingsFlag, 20);
        singleton.ushumgalluAssist = getBoolFlagFromInt.apply(booleanSettingsFlag, 19);
        singleton.allowSubweaponStart = getBoolFlagFromInt.apply(booleanSettingsFlag, 18);
        singleton.requireFullAccess = getBoolFlagFromInt.apply(booleanSettingsFlag, 17);
        singleton.randomizeDracuetShop = getBoolFlagFromInt.apply(booleanSettingsFlag, 16);
        singleton.randomizeXmailer = getBoolFlagFromInt.apply(booleanSettingsFlag, 15);
        singleton.htFullRandom = getBoolFlagFromInt.apply(booleanSettingsFlag, 14);
        singleton.automaticTranslations = getBoolFlagFromInt.apply(booleanSettingsFlag, 13);
        singleton.allowMainWeaponStart = getBoolFlagFromInt.apply(booleanSettingsFlag, 12);
        singleton.randomizeCursedChests = getBoolFlagFromInt.apply(booleanSettingsFlag, 11);
        singleton.automaticHardmode = getBoolFlagFromInt.apply(booleanSettingsFlag, 10);
        singleton.coinChestGraphics = getBoolFlagFromInt.apply(booleanSettingsFlag, 9);
        singleton.requireSoftwareComboForKeyFairy = getBoolFlagFromInt.apply(booleanSettingsFlag, 8);
        singleton.requireIceCapeForLava = getBoolFlagFromInt.apply(booleanSettingsFlag, 7);
        singleton.requireFlaresForExtinction = getBoolFlagFromInt.apply(booleanSettingsFlag, 6);
        singleton.randomizeForbiddenTreasure = getBoolFlagFromInt.apply(booleanSettingsFlag, 5);
        singleton.randomizeCoinChests = getBoolFlagFromInt.apply(booleanSettingsFlag, 4);
        singleton.randomizeTrapItems = getBoolFlagFromInt.apply(booleanSettingsFlag, 3);
        singleton.removeSpaulder = getBoolFlagFromInt.apply(booleanSettingsFlag, 2);
        singleton.replaceMapsWithWeights = getBoolFlagFromInt.apply(booleanSettingsFlag, 1);
        singleton.automaticGrailPoints = getBoolFlagFromInt.apply(booleanSettingsFlag, 0);

        Collection<String> glitches = intToItemSet(Integer.parseInt(parts[3],16), DataFromFile.POSSIBLE_GLITCHES);
        Collection<String> dboosts = intToItemSet(Integer.parseInt(parts[4],16), DataFromFile.POSSIBLE_DBOOSTS);
        Set<String> initItems = new HashSet<>(intToItemSet(Integer.parseInt(parts[5],16), singleton.possibleRandomizedItems));
        Set<String> startingItems = new HashSet<>(intToItemSet(Integer.parseInt(parts[6],16), singleton.possibleRandomizedItems));
        BossDifficulty bossDifficulty = BossDifficulty.values()[Integer.parseInt(parts[7],16)];
        int minRandomRemovedItems = Integer.parseInt(parts[8],16);
        int maxRandomRemovedItems = Integer.parseInt(parts[9],16);

        int booleanSettingsFlag2 = Integer.parseInt(parts[10], 16);
        singleton.screenshakeDisabled = getBoolFlagFromInt.apply(booleanSettingsFlag2, 6);
        singleton.includeHellTempleNPCs = getBoolFlagFromInt.apply(booleanSettingsFlag2, 5);
        singleton.blockPushingRequiresGlove = getBoolFlagFromInt.apply(booleanSettingsFlag2, 4);
        singleton.randomizeGraphics = getBoolFlagFromInt.apply(booleanSettingsFlag2, 3);
        singleton.randomizeEnemies = getBoolFlagFromInt.apply(booleanSettingsFlag2, 2);
        singleton.randomizeBosses = getBoolFlagFromInt.apply(booleanSettingsFlag2, 1);
        singleton.randomizeStartingLocation = getBoolFlagFromInt.apply(booleanSettingsFlag2, 0);

        setStartingSeed(seed);
        setEnabledGlitches((List<String>) glitches, true);
        setEnabledDamageBoosts((List<String>) dboosts, true);
        setInitiallyAccessibleItems(initItems, true);
        setStartingItems(startingItems, true);
        setBossDifficulty(bossDifficulty.toString(), true);
        setMinRandomRemovedItems(minRandomRemovedItems, true);
        setMaxRandomRemovedItems(maxRandomRemovedItems, true);

        JOptionPane.showMessageDialog(null, "Settings successfully imported");
    }
}
