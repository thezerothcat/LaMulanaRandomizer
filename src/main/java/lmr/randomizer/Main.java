package lmr.randomizer;

import lmr.randomizer.node.AccessChecker;

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
        ShopRandomizer shopRandomizer = buildShopRandomizer(random, itemRandomizer);
        AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer);

        itemRandomizer.placeRequiredItem("Holy Grail", random);
        itemRandomizer.placeRequiredItem("Hand Scanner", random);
        itemRandomizer.placeRequiredItem("reader.exe", random);
        itemRandomizer.placeRequiredItem("Ankh Jewel (Gate of Guidance)", random);
        itemRandomizer.placeRequiredItem("Ankh Jewel (Mausoleum of the Giants)", random);
        itemRandomizer.placeRequiredItem("Ankh Jewel (Temple of the Sun)", random);
        itemRandomizer.placeRequiredItem("Ankh Jewel (Spring in the Sky)", random);

        itemRandomizer.placeAllItems(random);

        try {
            outputLocations(startingSeed, itemRandomizer, shopRandomizer);

            accessChecker.outputRequirements();
        } catch (Exception ex) {
            return;
            // No exception handling in v1
        }
    }

    private static ShopRandomizer buildShopRandomizer(Random random, ItemRandomizer itemRandomizer) {
        String initialSubweapon = null;
        if(guaranteeSubweapon) {
            initialSubweapon = ItemRandomizer.ALL_SUBWEAPONS.get(random.nextInt(ItemRandomizer.ALL_SUBWEAPONS.size()));
        }
        ShopRandomizer shopRandomizer = new ShopRandomizer(itemRandomizer.getTotalShopItems());
        shopRandomizer.determineItemTypes(random, initialSubweapon);

        itemRandomizer.setShopRandomizer(shopRandomizer);
        return shopRandomizer;
    }

    private static AccessChecker buildAccessChecker(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer) {
        AccessChecker accessChecker = new AccessChecker();
        FileUtils.populateRequirements(accessChecker, "requirement/location_reqs.txt", "Location: ");
        FileUtils.populateRequirements(accessChecker, "requirement/item_reqs.txt", null);
        FileUtils.populateRequirements(accessChecker, "requirement/event_reqs.txt", null);
        FileUtils.populateRequirements(accessChecker, "requirement/shop_reqs.txt", null);
        accessChecker.setItemRandomizer(itemRandomizer);
        accessChecker.setShopRandomizer(shopRandomizer);
        itemRandomizer.setAccessChecker(accessChecker);
        return accessChecker;
    }

    private static void outputLocations(long startingSeed, ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer) throws IOException {
        itemRandomizer.outputLocations(startingSeed);
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
