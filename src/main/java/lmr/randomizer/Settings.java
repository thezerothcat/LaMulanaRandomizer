package lmr.randomizer;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 7/20/2017.
 */
public final class Settings {
    private static Settings singleton = new Settings();

    private long startingSeed;

    private boolean changed = false;

    private boolean fullItemAccess;
    private boolean automaticHardmode;
    private boolean requireSoftwareComboForKeyFairy;
    private boolean requireIceCapeForLava;
    private boolean requireFlaresForExtinction;
    private boolean enableDamageBoostRequirements;
    private boolean randomizeForbiddenTreasure;
    private boolean randomizeCoinChests;

    private List<String> enabledGlitches = new ArrayList<>();

    private String laMulanaBaseDir;
    private String language;

    private Set<String> nonRandomizedItems = new HashSet<>();
    private Set<String> initiallyAvailableItems = new HashSet<>();

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
        fullItemAccess = true;
        automaticHardmode = false;
        enableDamageBoostRequirements = false;

        bossDifficulty = BossDifficulty.MEDIUM;
        shopRandomization = ShopRandomizationEnum.EVERYTHING;

        initiallyAvailableItems.add("Holy Grail");
        initiallyAvailableItems.add("Hermes' Boots");

        for(String filename : Arrays.asList("C:\\Games\\La-Mulana Remake 1.3.3.1", "C:\\GOG Games\\La-Mulana", "C:\\GOG Games\\La-Mulana",
                "C:\\Steam\\steamapps\\common\\La-Mulana", "C:\\Program Files (x86)\\Steam\\steamapps\\common\\La-Mulana",
                "C:\\Program Files\\Steam\\steamapps\\common\\La-Mulana")) {
            if(new File(filename).exists()) {
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

    public static String getLaMulanaBaseDir() {
        return singleton.laMulanaBaseDir;
    }

    public static String getLanguage() {
        return singleton.language;
    }

    public static String getBackupDatFile() {
        if("en".equals(singleton.language)) {
            return "script_code.dat.bak";
        }
        return "script_code_" + singleton.language + ".dat.bak";
    }


    public static Set<String> getNonRandomizedItems() {
        return singleton.nonRandomizedItems;
    }

    public static Set<String> getInitiallyAvailableItems() {
        return singleton.initiallyAvailableItems;
    }

    public static ShopRandomizationEnum getShopRandomization() {
        return singleton.shopRandomization;
    }

    public static BossDifficulty getBossDifficulty() {
        return singleton.bossDifficulty;
    }

    public static boolean isFullItemAccess() {
        return singleton.fullItemAccess;
    }

    public static boolean isAutomaticHardmode() {
        return singleton.automaticHardmode;
    }

    public static boolean isEnableDamageBoostRequirements() {
        return singleton.enableDamageBoostRequirements;
    }

    public static List<String> getEnabledGlitches() {
        return singleton.enabledGlitches;
    }

    public static void setRequireSoftwareComboForKeyFairy(boolean requireSoftwareComboForKeyFairy, boolean update) {
        if(update && requireSoftwareComboForKeyFairy != singleton.requireSoftwareComboForKeyFairy) {
            singleton.changed = true;
        }
        singleton.requireSoftwareComboForKeyFairy = requireSoftwareComboForKeyFairy;
    }

    public static void setRequireIceCapeForLava(boolean requireIceCapeForLava, boolean update) {
        if(update && requireIceCapeForLava != singleton.requireIceCapeForLava) {
            singleton.changed = true;
        }
        singleton.requireIceCapeForLava = requireIceCapeForLava;
    }

    public static void setRequireFlaresForExtinction(boolean requireFlaresForExtinction, boolean update) {
        if(update && requireFlaresForExtinction != singleton.requireFlaresForExtinction) {
            singleton.changed = true;
        }
        singleton.requireFlaresForExtinction = requireFlaresForExtinction;
    }

    public static void setEnableDamageBoostRequirements(boolean enableDamageBoostRequirements, boolean update) {
        if(update && enableDamageBoostRequirements != singleton.enableDamageBoostRequirements) {
            singleton.changed = true;
        }
        singleton.enableDamageBoostRequirements = enableDamageBoostRequirements;
    }

    public static void setRandomizeForbiddenTreasure(boolean randomizeForbiddenTreasure, boolean update) {
        if(update && randomizeForbiddenTreasure != singleton.randomizeForbiddenTreasure) {
            singleton.changed = true;
        }
        singleton.randomizeForbiddenTreasure = randomizeForbiddenTreasure;
    }

    public static void setRandomizeCoinChests(boolean randomizeCoinChests, boolean update) {
        if(update && randomizeCoinChests != singleton.randomizeCoinChests) {
            singleton.changed = true;
        }
        singleton.randomizeCoinChests = randomizeCoinChests;
    }

    public static void setInitiallyAvailableItems(Set<String> initiallyAvailableItems, boolean update) {
        if(update && !singleton.changed) {
            if(initiallyAvailableItems.containsAll(singleton.initiallyAvailableItems)) {
                singleton.changed = !singleton.initiallyAvailableItems.containsAll(initiallyAvailableItems);
            }
            else {
                singleton.changed = true;
            }
        }
        singleton.initiallyAvailableItems = initiallyAvailableItems;
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

    public static void saveSettings() {
        if(singleton.changed) {
            try {
                FileUtils.log("Updating setings");
                FileUtils.saveSettings();
            }
            catch (IOException ex) {
                FileUtils.log("Unable to save settings: " + ex.getMessage());
            }
        }
    }
}
