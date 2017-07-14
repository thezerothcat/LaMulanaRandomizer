package lmr.randomizer;

import lmr.randomizer.node.AccessChecker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 7/10/2017.
 */
public class ItemRandomizer {
    public static final List<String> ALL_SUBWEAPONS = Arrays.asList("Shuriken", "Caltrops", "Rolling Shuriken", "Bomb", "Flare Gun", "Chakram", "Earth Spear", "Pistol");

    private Map<String, String> mapOfItemLocationToItem = new HashMap<>(); // The map we're trying to build.

    private List<String> allItems; // All possible items.
    private List<String> unplacedItems; // Items that haven't been placed yet.

    private List<String> nonShopItemLocations;
    private List<String> unassignedNonShopItemLocations;
    private int totalShopItems;

    private ShopRandomizer shopRandomizer;
    private AccessChecker accessChecker;

    public ItemRandomizer() {
        allItems = FileUtils.getList("all/all_items.txt");
        unplacedItems = new ArrayList<>(allItems);

        nonShopItemLocations = FileUtils.getList("all/non_shop_items.txt");
        unassignedNonShopItemLocations = new ArrayList<>(nonShopItemLocations);
        totalShopItems = allItems.size() - nonShopItemLocations.size();
    }

    public int getTotalShopItems() {
        return totalShopItems;
    }

//    public void addLocation(String location) {
//        nonShopItemLocations.add(location);
//    }

    public void placeRequiredItem(String item, Random random) {
        int availableLocations = unassignedNonShopItemLocations.size() + shopRandomizer.getUnassignedShopItemLocations().size();
        placeItem(item, availableLocations, random);
    }

    public void placeAllItems(Random random) {
        while(!unplacedItems.isEmpty()) {
            placeRequiredItem(unplacedItems.get(random.nextInt(unplacedItems.size())), random);
        }
    }
//    // todo: update for shop logic
//    public void placeItem(Random random) {
//        int availableLocations = nonShopItemLocations.size() + shopRandomizer.getUnassignedShopItemLocations().size();
//        if(availableLocations < accessChecker.getMinRequirementsToNextItemLocation()) {
//            // todo: die in a fire - really, we don't want to wind up in this situation
//            // todo: alternatively, maybe check a list of items that don't give progress, and make replacements?
//        }
//        else if(availableLocations == accessChecker.getMinRequirementsToNextItemLocation()) {
//            // todo: logic to select one of the possible sets of progress items that will allow further progress
//        }
//        else {
//            String item = unplacedItems.get(random.nextInt(unplacedItems.size()));
//            int locationIndex = random.nextInt(availableLocations);
//            if(locationIndex < nonShopItemLocations.size()) {
//                // todo: check for exceptions
//                String location = nonShopItemLocations.get(locationIndex);
//                mapOfItemLocationToItem.put(location, item);
//                nonShopItemLocations.remove(location);
//            }
//            else {
//                shopRandomizer.placeItem(item, locationIndex - nonShopItemLocations.size());
//            }
//            unplacedItems.remove(item);
//            accessChecker.updateRequirements(item);
//            while(!accessChecker.getQueuedUpdates().isEmpty()) {
//                accessChecker.updateRequirements(null);
//            }
//        }
//    }

    private void placeItem(String item, int availableLocations, Random random) {
        int locationIndex = random.nextInt(availableLocations);
        if(locationIndex < unassignedNonShopItemLocations.size()) {
            // todo: check for exceptions
            String location = unassignedNonShopItemLocations.get(locationIndex);
            mapOfItemLocationToItem.put(location, item);
            unassignedNonShopItemLocations.remove(location);
        }
        else {
            shopRandomizer.placeItem(item, locationIndex - unassignedNonShopItemLocations .size());
        }
        unplacedItems.remove(item);
    }

    public void outputLocations(long startingSeed) throws IOException {
        BufferedWriter writer = FileUtils.getFileWriter(String.format("items%d.txt", startingSeed));
        if (writer == null) {
            return;
        }

        for (String location : mapOfItemLocationToItem.keySet()) {
            writer.write(location + " => " + mapOfItemLocationToItem.get(location));
            writer.newLine();
        }

        writer.flush();
        writer.close();
    }

    public void setShopRandomizer(ShopRandomizer shopRandomizer) {
        this.shopRandomizer = shopRandomizer;
    }

    public void setAccessChecker(AccessChecker accessChecker) {
        this.accessChecker = accessChecker;
    }
}
