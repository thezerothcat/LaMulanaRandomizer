package lmr.randomizer;

import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.rcd.Zone;

import javax.script.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by thezerothcat on 7/9/2017.
 */
public class Main {
    // todo: actually use settings
    private static boolean zeroRequirementGrail = true;
    private static boolean zeroRequirementHandScannerAndReaderExe = true;
    private static boolean guaranteeSubweapon = true; // Ensure at least one subweapon drop within initial item set.
    private static int zeroRequirementSubweapons = 3; // Preserving vanilla initial subweapon ratio for the sake of fun and sanity, although this should eventually be configurable.
    private static int zeroRequirementAnkhJewels = 4; // Preserving vanilla number of ankh jewels to reduce risk of ankh jewel locks, at least for v1.

    public static void main(String[] args) {
//        generateItemPlacements(args);

        try {
            writeRcd(FileUtils.getRcdScriptInfo());
        } catch (Exception ex) {
            FileUtils.log("Rcd script processing failed: " + ex.getMessage());
            ex.printStackTrace();
        }
        FileUtils.closeAll();
    }

    private static void generateItemPlacements(String[] args) {
        long startingSeed = getSeed(args);
        Random random = new Random(startingSeed);

        int attempt = 1;
        while(true) {
//        for(int attempt = 1; attempt <= 5; attempt++) {
            ItemRandomizer itemRandomizer = new ItemRandomizer();
            ShopRandomizer shopRandomizer = buildShopRandomizer(itemRandomizer);
            AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer);

            String initialSubweapon = null;
            if(guaranteeSubweapon) {
                initialSubweapon = ItemRandomizer.ALL_SUBWEAPONS.get(random.nextInt(ItemRandomizer.ALL_SUBWEAPONS.size()));
            }
            shopRandomizer.determineItemTypes(random, initialSubweapon);

            // todo: make initial items based on settings
            // Note: The line beginning with Feather isn't actually required, but since the entire
            // placement set gets rerolled every time there's a failure, it can take ages to find a
            // valid set of placements. This was an attempt to make things resolve faster.
            itemRandomizer.placeRequiredItems(Arrays.asList("Holy Grail",
                    "Hand Scanner", "reader.exe",
                    "Ankh Jewel (Gate of Guidance)",
                    "Ankh Jewel (Mausoleum of the Giants)",
                    "Ankh Jewel (Temple of the Sun)",
                    "Ankh Jewel (Spring in the Sky)",
                    "Feather", "Hermes' Boots", "Grapple Claw", "Origin Seal", "Helmet",
                    initialSubweapon),
                    random);

            itemRandomizer.placeAllItems(random);

            accessChecker.computeAccessibleNodes("None");
            while(!accessChecker.getQueuedUpdates().isEmpty()) {
                accessChecker.computeAccessibleNodes(accessChecker.getQueuedUpdates().iterator().next());
            }

            if(accessChecker.isSuccess()) {
                try {
                    outputLocations(startingSeed, itemRandomizer, shopRandomizer, attempt);
//                    updateRcd(itemRandomizer, shopRandomizer);
//                    accessChecker.outputRemaining(startingSeed, attempt);
                } catch (Exception ex) {
                    continue;
                    // No exception handling in v1
                }

                return;
            }
            attempt++;
        }
    }

    private static ShopRandomizer buildShopRandomizer(ItemRandomizer itemRandomizer) {
        ShopRandomizer shopRandomizer = new ShopRandomizer(itemRandomizer.getTotalShopItems());
        itemRandomizer.setShopRandomizer(shopRandomizer);
        return shopRandomizer;
    }

    private static AccessChecker buildAccessChecker(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer) {
        AccessChecker accessChecker = new AccessChecker();
        FileUtils.populateRequirements(accessChecker, "requirement/location_reqs.txt", "Location: ");
        FileUtils.populateRequirements(accessChecker, "requirement/item_reqs.txt", null);
        FileUtils.populateRequirements(accessChecker, "requirement/event_reqs.txt", null);
        FileUtils.populateRequirements(accessChecker, "requirement/shop_reqs.txt", null);
//        FileUtils.populateRequirements(accessChecker, "requirement/glitch/location_reqs.txt", "Location: ");
//        FileUtils.populateRequirements(accessChecker, "requirement/glitch/item_reqs.txt", null);
//        FileUtils.populateRequirements(accessChecker, "requirement/glitch/shop_reqs.txt", null);
        accessChecker.setItemRandomizer(itemRandomizer);
        accessChecker.setShopRandomizer(shopRandomizer);
        itemRandomizer.setAccessChecker(accessChecker);
        shopRandomizer.setAccessChecker(accessChecker);
        return accessChecker;
    }

    private static void outputLocations(long startingSeed, ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer, int attempt) throws IOException {
        itemRandomizer.outputLocations(startingSeed, attempt);
        shopRandomizer.outputLocations(startingSeed, attempt);
    }

    private static void writeRcd(List<Zone> rcdInfo) throws IOException {
        try(BufferedWriter writer = FileUtils.getFileWriter("rcdtest.txt")) {
            for(Zone zone : rcdInfo) {
                writer.write(zone.toString());
                writer.newLine();
            }
        }
    }

    private static long getSeed(String[] args) {
        try {
            return Long.parseLong(args[0]);
        } catch (Exception ex) {
            return 0L;
        }
    }
}
