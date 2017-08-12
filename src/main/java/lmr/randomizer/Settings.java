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

    private boolean allowGlitches;
    private boolean randomizeShops;
    private boolean guaranteeSubweapon; // Ensure at least one subweapon drop within initial item set. // todo: restore this; it's broken in cases where no subweapons are included in randomization
    private boolean requireSoftwareComboForKeyFairy;
    private boolean randomizeForbiddenTreasure;

    private String laMulanaBaseDir = null;
    private String rcdFileLocation = null;
    private String datFileLocation = null;

    private Set<String> nonRandomizedItems = new HashSet<>();
    private Set<String> initiallyAvailableItems = new HashSet<>();

    private Settings() {
        startingSeed = new Random().nextInt(Integer.MAX_VALUE);
        laMulanaBaseDir = "Please enter your La-Mulana install directory";
        randomizeShops = true;
        requireSoftwareComboForKeyFairy = true;

        for(String filename : Arrays.asList("C:\\Games\\La-Mulana Remake 1.3.3.1", "C:\\GOG Games\\La-Mulana", "C:\\GOG Games\\La-Mulana",
                "C:\\Steam\\steamapps\\common\\La-Mulana", "C:\\Program Files (x86)\\Steam\\steamapps\\common\\La-Mulana",
                "C:\\Program Files\\Steam\\steamapps\\common\\La-Mulana")) {
            if(new File(filename).exists()) {
                laMulanaBaseDir = filename;
            }
        }
    }

    public static long getStartingSeed() {
        return singleton.startingSeed;
    }

    public static boolean isAllowGlitches() {
        return singleton.allowGlitches;
    }

    public static boolean isRandomizeShops() {
        return singleton.randomizeShops;
    }

    public static boolean isGuaranteeSubweapon() {
        return singleton.guaranteeSubweapon;
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

    public static String getRcdFileLocation() {
        return singleton.rcdFileLocation;
    }

    public static String getDatFileLocation() {
        return singleton.datFileLocation;
    }

    public static Set<String> getNonRandomizedItems() {
        return singleton.nonRandomizedItems;
    }

    public static Set<String> getInitiallyAvailableItems() {
        return singleton.initiallyAvailableItems;
    }

    public static void setAllowGlitches(boolean allowGlitches) {
        if(allowGlitches != singleton.allowGlitches) {
            singleton.changed = true;
        }
        singleton.allowGlitches = allowGlitches;
    }

    public static void setGuaranteeSubweapon(boolean guaranteeSubweapon) {
        if(guaranteeSubweapon != singleton.guaranteeSubweapon) {
            singleton.changed = true;
        }
        singleton.guaranteeSubweapon = guaranteeSubweapon;
    }

    public static void setRandomizeShops(boolean randomizeShops) {
        if(randomizeShops != singleton.randomizeShops) {
            singleton.changed = true;
        }
        singleton.randomizeShops = randomizeShops;
    }

    public static void setRequireSoftwareComboForKeyFairy(boolean requireSoftwareComboForKeyFairy) {
        if(requireSoftwareComboForKeyFairy != singleton.requireSoftwareComboForKeyFairy) {
            singleton.changed = true;
        }
        singleton.requireSoftwareComboForKeyFairy = requireSoftwareComboForKeyFairy;
    }

    public static void setRandomizeForbiddenTreasure(boolean randomizeForbiddenTreasure) {
        if(randomizeForbiddenTreasure != singleton.randomizeForbiddenTreasure) {
            singleton.changed = true;
        }
        singleton.randomizeForbiddenTreasure = randomizeForbiddenTreasure;
    }

    public static void setInitiallyAvailableItems(Set<String> initiallyAvailableItems) {
        if(!singleton.changed) {
            if(initiallyAvailableItems.containsAll(singleton.initiallyAvailableItems)) {
                singleton.changed = !singleton.initiallyAvailableItems.containsAll(initiallyAvailableItems);
            }
            else {
                singleton.changed = true;
            }
        }
        singleton.initiallyAvailableItems = initiallyAvailableItems;
    }

    public static void setNonRandomizedItems(Set<String> nonRandomizedItems) {
        if(!singleton.changed) {
            if(nonRandomizedItems.containsAll(singleton.nonRandomizedItems)) {
                singleton.changed = !singleton.nonRandomizedItems.containsAll(nonRandomizedItems);
            }
            else {
                singleton.changed = true;
            }
        }

        singleton.nonRandomizedItems = nonRandomizedItems;
    }

    public static void setStartingSeed(int startingSeed) {
        singleton.startingSeed = startingSeed;
    }

    public static void setLaMulanaBaseDir(String laMulanaBaseDir) {
        if(!laMulanaBaseDir.equals(singleton.laMulanaBaseDir)) {
            singleton.changed = true;
        }
        singleton.laMulanaBaseDir = laMulanaBaseDir;
    }

    public static void setRcdFileLocation(String rcdFileLocation) {
        if(!rcdFileLocation.equals(singleton.rcdFileLocation)) {
            singleton.changed = true;
        }
        singleton.rcdFileLocation = rcdFileLocation;
    }

    public static void setDatFileLocation(String datFileLocation) {
        if(!datFileLocation.equals(singleton.datFileLocation)) {
            singleton.changed = true;
        }
        singleton.datFileLocation = datFileLocation;
    }

    public static void saveSettings() {
        if(singleton.changed) {
            try {
                FileUtils.saveSettings();
            }
            catch (IOException ex) {
                FileUtils.log("Unable to save settings: " + ex.getMessage());
            }
        }
    }

//    public static boolean zeroRequirementHandScannerAndReaderExe = true;
}
