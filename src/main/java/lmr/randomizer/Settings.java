package lmr.randomizer;

import java.util.*;

/**
 * Created by thezerothcat on 7/20/2017.
 */
public final class Settings {
    private Settings() { }

    public static long startingSeed = new Random().nextInt(Integer.MAX_VALUE);

    public static boolean allowGlitches = false;
    public static boolean randomizeShops = false;
    public static boolean guaranteeSubweapon = false; // Ensure at least one subweapon drop within initial item set. // todo: restore this; it's broken in cases where no subweapons are included in randomization

    public static String laMulanaBaseDir = null;
    public static String rcdFileLocation = null;
    public static String datFileLocation = null;

    public static Set<String> nonRandomizedItems = new HashSet<>();
    public static Set<String> initiallyAvailableItems = getDefaultInitiallyAvailableItems();

    public static Set<String> getDefaultInitiallyAvailableItems() {
        return new HashSet<>(Arrays.asList("Ankh Jewel (Gate of Guidance)", "Ankh Jewel (Mausoleum of the Giants)",
                "Ankh Jewel (Temple of the Sun)", "Ankh Jewel (Spring in the Sky)"));
    }

//    public static boolean zeroRequirementHandScannerAndReaderExe = true;
//    public static int zeroRequirementAnkhJewels = 4; // Preserving vanilla number of ankh jewels to reduce risk of ankh jewel locks, at least for v1.
}
