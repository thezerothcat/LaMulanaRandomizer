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
    private boolean requireSoftwareComboForKeyFairy;
    private boolean enableDamageBoostRequirements;
    private boolean randomizeForbiddenTreasure;

    private List<String> enabledGlitches = new ArrayList<>();

    private String laMulanaBaseDir = null;

    private Set<String> nonRandomizedItems = new HashSet<>();
    private Set<String> initiallyAvailableItems = new HashSet<>();

    private BossDifficulty bossDifficulty;
    private ShopRandomizationEnum shopRandomization;

    private Settings() {
        startingSeed = new Random().nextInt(Integer.MAX_VALUE);
        laMulanaBaseDir = "Please enter your La-Mulana install directory";

        fullItemAccess = false;
        randomizeForbiddenTreasure = true;
        enableDamageBoostRequirements = false;
        requireSoftwareComboForKeyFairy = true;

        bossDifficulty = BossDifficulty.HARD;
        shopRandomization = ShopRandomizationEnum.EVERYTHING;

        nonRandomizedItems.add("Holy Grail");
        nonRandomizedItems.add("Hermes' Boots");

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

    public static boolean isRandomizeForbiddenTreasure() {
        return singleton.randomizeForbiddenTreasure;
    }

    public static String getLaMulanaBaseDir() {
        return singleton.laMulanaBaseDir;
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
