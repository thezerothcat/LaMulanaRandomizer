package lmr.randomizer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 7/9/2017.
 */
public class Main {
    private static boolean zeroRequirementGrail = true;
    private static boolean zeroRequirementHandScannerAndReaderExe = true;
    private static boolean guaranteeSubweapon = true; // Ensure at least one subweapon drop within initial item set.
    private static int zeroRequirementSubweapons = 3; // Preserving vanilla initial subweapon ratio for the sake of fun and sanity, although this should eventually be configurable.
    private static int zeroRequirementAnkhJewels = 4; // Preserving vanilla number of ankh jewels to reduce risk of ankh jewel locks, at least for v1.

    public static void main(String[] args) {
        long startingSeed = getSeed(args);
        Random random = new Random(startingSeed);

        ItemRandomizer itemRandomizer = new ItemRandomizer();
        String initialSubweapon = null;
        if(guaranteeSubweapon) {
            initialSubweapon = ItemRandomizer.ALL_SUBWEAPONS.get(random.nextInt(ItemRandomizer.ALL_SUBWEAPONS.size()));
        }
        ShopRandomizer shopRandomizer = new ShopRandomizer(itemRandomizer.getTotalShopItems());
        shopRandomizer.determineItemTypes(random, initialSubweapon);

        Requirements requirements = new Requirements();
        FileUtils.populateRequirements(requirements, "requirement/location_reqs.txt");

        try {
            outputLocations(startingSeed, itemRandomizer, shopRandomizer);

            requirements.outputRequirements();
        } catch (Exception ex) {
            return;
            // No exception handling in v1
        }
    }

    private static void outputLocations(long startingSeed, ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer) throws IOException {
//        itemRandomizer.outputLocations(writer);
        shopRandomizer.outputLocations(startingSeed);
    }

    private static long getSeed(String[] args) {
        try {
            return Long.parseLong(args[0]);
        } catch (Exception ex) {
            return 0L;
        }
    }
}
