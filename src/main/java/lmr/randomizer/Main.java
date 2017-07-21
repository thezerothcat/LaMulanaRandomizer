package lmr.randomizer;

import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.rcd.RcdReader;
import lmr.randomizer.rcd.RcdWriter;
import lmr.randomizer.rcd.Zone;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 7/9/2017.
 */
public class Main {
    public static void main(String[] args) {
        generateItemPlacements(args);

//        try {
//            RcdWriter.writeRcd(RcdReader.getRcdScriptInfo());
//        } catch (Exception ex) {
//            FileUtils.log("Rcd script processing failed: " + ex.getMessage());
//            ex.printStackTrace();
//        }
        FileUtils.closeAll();
    }

    private static void generateItemPlacements(String[] args) {
        long startingSeed = getSeed(args);
        Random random = new Random(startingSeed);
        List<String> noRequirementItems = getNoRequirementItems();

        int attempt = 1;
        while(true) {
//        for(int attempt = 1; attempt <= 5; attempt++) {
            ItemRandomizer itemRandomizer = new ItemRandomizer();
            ShopRandomizer shopRandomizer = buildShopRandomizer(itemRandomizer);
            AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer);

            String initialSubweapon = null;
            if(Settings.guaranteeSubweapon) {
                initialSubweapon = ItemRandomizer.ALL_SUBWEAPONS.get(random.nextInt(ItemRandomizer.ALL_SUBWEAPONS.size()));
            }
            shopRandomizer.determineItemTypes(random, initialSubweapon);

            // todo: make initial items based on settings
            itemRandomizer.placeNonRandomizedItems();
            // Note: The line beginning with Feather isn't actually required, but since the entire
            // placement set gets rerolled every time there's a failure, it can take ages to find a
            // valid set of placements. This was an attempt to make things resolve faster.
            itemRandomizer.placeRequiredItems(noRequirementItems, random);

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
        ShopRandomizer shopRandomizer;
        if(Settings.randomizeShops) {
            shopRandomizer = new ShopRandomizer(itemRandomizer.getTotalShopItems());
        }
        else {
            shopRandomizer = new ShopNonRandomizer(itemRandomizer.getTotalShopItems());
        }

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

    private static List<String> getNoRequirementItems() {
        List<String> noRequirementItems = new ArrayList<>();
        noRequirementItems.add("Holy Grail");
        if(Settings.randomizeShops) {
            noRequirementItems.add("Hand Scanner");
            noRequirementItems.add("reader.exe");
            noRequirementItems.add("Hermes' Boots");
            noRequirementItems.add("Helmet");
        }
        noRequirementItems.add("Ankh Jewel (Gate of Guidance)");
        noRequirementItems.add("Ankh Jewel (Mausoleum of the Giants)");
        noRequirementItems.add("Ankh Jewel (Temple of the Sun)");
        noRequirementItems.add("Ankh Jewel (Spring in the Sky)");
        noRequirementItems.add("Feather");
        noRequirementItems.add("Grapple Claw");
        noRequirementItems.add("Origin Seal");
        noRequirementItems.removeAll(DataFromFile.getNonRandomizedItems());
        return noRequirementItems;
    }

    private static void outputLocations(long startingSeed, ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer, int attempt) throws IOException {
        itemRandomizer.outputLocations(startingSeed, attempt);
        shopRandomizer.outputLocations(startingSeed, attempt);
    }

    private static long getSeed(String[] args) {
        try {
            return Long.parseLong(args[0]);
        } catch (Exception ex) {
            return 0L;
        }
    }
}
