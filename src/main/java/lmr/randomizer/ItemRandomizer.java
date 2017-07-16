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

    public List<String> getAllItems() {
        return allItems;
    }

    public String getItem(String location) {
        return mapOfItemLocationToItem.get(location);
    }

    public void placeRequiredItems(List<String> items, Random random) {
        List<String> initialUnassignedNonShopLocations = FileUtils.getList("initial/initial_chests.txt");
        List<String> initialUnassignedShopItemLocations = shopRandomizer.getInitialUnassignedShopItemLocations();

        // todo: properly randomize the order in which the list of initial items is placed
        for(String item : items) {
            while(true) {
                int availableLocations = initialUnassignedNonShopLocations.size() + initialUnassignedShopItemLocations.size();

                int locationIndex = random.nextInt(availableLocations);
                if(locationIndex < initialUnassignedNonShopLocations.size()) {
                    // todo: check for exceptions
                    String location = initialUnassignedNonShopLocations.get(locationIndex);
                    mapOfItemLocationToItem.put(location, item);
                    if(accessChecker.validRequirements(item, location)) {
                        initialUnassignedNonShopLocations.remove(location);
                        unassignedNonShopItemLocations.remove(location);
                        unplacedItems.remove(item);
                        break;
                    }
                }
                else if(shopRandomizer.placeRequiredItem(item, initialUnassignedShopItemLocations, locationIndex - initialUnassignedNonShopLocations.size())) {
                    unplacedItems.remove(item);
                    break;
                }
            }
        }
    }

    public void placeAllItems(Random random) {
        while(!unplacedItems.isEmpty()) {
            int availableLocations = unassignedNonShopItemLocations.size() + shopRandomizer.getUnassignedShopItemLocations().size();
            placeItem(unplacedItems.get(random.nextInt(unplacedItems.size())), availableLocations, random);
        }
    }

    private boolean placeItem(String item, int availableLocations, Random random) {
        int locationIndex = random.nextInt(availableLocations);
        if(locationIndex < unassignedNonShopItemLocations.size()) {
            String location = unassignedNonShopItemLocations.get(locationIndex);
            if (accessChecker.validRequirements(item, location)) {
                mapOfItemLocationToItem.put(location, item);
                unassignedNonShopItemLocations.remove(location);
                unplacedItems.remove(item);
                return true;
            }
        }
        else if(shopRandomizer.placeItem(item, locationIndex - unassignedNonShopItemLocations .size())) {
            unplacedItems.remove(item);
            return true;
        }
        return false;
    }

    public void outputLocations(long startingSeed, int attemptNumber) throws IOException {
        BufferedWriter writer = FileUtils.getFileWriter(String.format("target/items%d_%d.txt", startingSeed, attemptNumber));
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
