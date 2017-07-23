package lmr.randomizer;

import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.random.ItemRandomizer;
import lmr.randomizer.random.ShopNonRandomizer;
import lmr.randomizer.random.ShopRandomizer;
import lmr.randomizer.rcd.RcdReader;
import lmr.randomizer.rcd.RcdWriter;
import lmr.randomizer.rcd.object.Zone;
import lmr.randomizer.update.RcdObjectTracker;

import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 7/9/2017.
 */
public class Main {
    public static void main(String[] args) {
        parseSettings(args);
        try {
            doTheThing();
        } catch (Exception ex) {
            FileUtils.log("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        FileUtils.closeAll();
    }

    private static void doTheThing() {
        Random random = new Random(Settings.startingSeed);
        List<String> noRequirementItems = getNoRequirementItems();

        int attempt = 1;
        while(true) {
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
            if(!itemRandomizer.placeRequiredItems(noRequirementItems, random)) {
                continue;
            }
            if(!itemRandomizer.placeAllItems(random)) {
                continue;
            }

            accessChecker.computeAccessibleNodes("None");
            while(!accessChecker.getQueuedUpdates().isEmpty()) {
                accessChecker.computeAccessibleNodes(accessChecker.getQueuedUpdates().iterator().next());
            }

            if(accessChecker.isSuccess()) {
                try {
                    outputLocations(itemRandomizer, shopRandomizer, attempt);
                    itemRandomizer.updateRcd();

//                    accessChecker.outputRemaining(Settings.startingSeed, attempt);
                } catch (Exception ex) {
                    return;
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
        if(Settings.allowGlitches) {
            FileUtils.populateRequirements(accessChecker, "requirement/glitch/location_reqs.txt", "Location: ");
            FileUtils.populateRequirements(accessChecker, "requirement/glitch/item_reqs.txt", null);
            FileUtils.populateRequirements(accessChecker, "requirement/glitch/shop_reqs.txt", null);
        }
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
//        noRequirementItems.add("Feather");
//        noRequirementItems.add("Grapple Claw");
//        noRequirementItems.add("Origin Seal");
        noRequirementItems.removeAll(DataFromFile.getNonRandomizedItems());
        return noRequirementItems;
    }

    private static void outputLocations(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer, int attempt) throws IOException {
        itemRandomizer.outputLocations(attempt);
        shopRandomizer.outputLocations(attempt);
    }

    private static void parseSettings(String[] args) {
        for(String arg : args) {
            if(arg.startsWith("-s")) {
                try {
                    Settings.startingSeed = Long.parseLong(arg.substring(2));
                } catch (Exception ex) {
                    Settings.startingSeed = 0L;
                }
            }
            else if(arg.equals("-g")) {
                Settings.allowGlitches = true;
            }
            else if (arg.startsWith("-dir")) {
                Settings.laMulanaBaseDir = arg.substring(4);
            }
        }
    }
}
