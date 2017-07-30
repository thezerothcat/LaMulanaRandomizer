package lmr.randomizer.random;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.DatReader;
import lmr.randomizer.dat.DatWriter;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.rcd.RcdReader;
import lmr.randomizer.rcd.RcdWriter;
import lmr.randomizer.rcd.object.Zone;
import lmr.randomizer.update.GameDataTracker;

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
        allItems = new ArrayList<>(DataFromFile.getAllItems());
        unplacedItems = new ArrayList<>(allItems);

        nonShopItemLocations = new ArrayList<>(DataFromFile.getNonShopItemLocations());
        unassignedNonShopItemLocations = new ArrayList<>(nonShopItemLocations);
        totalShopItems = Settings.randomizeShops ? allItems.size() - nonShopItemLocations.size() : 0;
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

    public void placeNonRandomizedItems() {
        for(String item : DataFromFile.getNonRandomizedItems()) {
            mapOfItemLocationToItem.put(item, item);
            if(!unassignedNonShopItemLocations.contains(item)) {
                FileUtils.log("Item not included in locations: " + item);
            }
            unassignedNonShopItemLocations.remove(item);
            unplacedItems.remove(item);
        }
    }

    public boolean placeRequiredItems(List<String> items, Random random) {
        List<String> initialUnassignedNonShopLocations = new ArrayList<>(DataFromFile.getInitialNonShopItemLocations());
        List<String> initialUnassignedShopItemLocations = shopRandomizer.getInitialUnassignedShopItemLocations();

        int size = items.size();
        for(int i = 0; i < size; i++) {
            String item = getRandomItem(items, random);
            int availableLocations = initialUnassignedNonShopLocations.size() + initialUnassignedShopItemLocations.size();
            List<Integer> availableLocationIndices = buildIndices(availableLocations);

            while(true) {
                if(availableLocationIndices.size() == 0) {
                    return false;
                }
                int locationIndex = availableLocationIndices.get(random.nextInt(availableLocationIndices.size()));
                if(locationIndex < initialUnassignedNonShopLocations.size()) {
                    // todo: check for exceptions
                    String location = initialUnassignedNonShopLocations.get(locationIndex);
                    mapOfItemLocationToItem.put(location, item);
                    if(accessChecker.validRequirements(item, location)) {
                        items.remove(item);
                        initialUnassignedNonShopLocations.remove(location);
                        unassignedNonShopItemLocations.remove(location);
                        unplacedItems.remove(item);
                        break;
                    }
                    else {
                        availableLocationIndices.remove(locationIndex);
                    }
                }
                else {
                    if(shopRandomizer.placeRequiredItem(item, initialUnassignedShopItemLocations, locationIndex - initialUnassignedNonShopLocations.size())) {
                        items.remove(item);
                        unplacedItems.remove(item);
                        break;
                    }
                    else {
                        availableLocationIndices.remove(locationIndex);
                    }
                }
            }
        }
        return true;
    }

    private String getRandomItem(List<String> items, Random random) {
        return items.get(random.nextInt(items.size()));
    }

    public boolean placeAllItems(Random random) {
        while(!unplacedItems.isEmpty()) {
            int availableLocations = unassignedNonShopItemLocations.size() + shopRandomizer.getUnassignedShopItemLocations().size();
            if(!placeItem(unplacedItems.get(random.nextInt(unplacedItems.size())), availableLocations, random)) {
                return false;
            }
        }
        return true;
    }

    private boolean placeItem(String item, int availableLocations, Random random) {
        List<Integer> availableLocationIndices = buildIndices(availableLocations);

        while(true) {
            if(availableLocationIndices.size() == 0) {
                return false;
            }
            int locationIndex = availableLocationIndices.get(random.nextInt(availableLocationIndices.size()));
            if(locationIndex < unassignedNonShopItemLocations.size()) {
                String location = unassignedNonShopItemLocations.get(locationIndex);
                if (accessChecker.validRequirements(item, location)) {
                    mapOfItemLocationToItem.put(location, item);
                    unassignedNonShopItemLocations.remove(location);
                    unplacedItems.remove(item);
                    return true;
                }
                else {
                    availableLocationIndices.remove(locationIndex);
                }
            }
            else {
                if(shopRandomizer.placeItem(item, locationIndex - unassignedNonShopItemLocations .size())) {
                    unplacedItems.remove(item);
                    return true;
                }
                else {
                    availableLocationIndices.remove(locationIndex);
                }
            }
        }
    }

    private List<Integer> buildIndices(int size) {
        List<Integer> indices = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            indices.add(i);
        }
        return indices;
    }

    public void outputLocations(int attemptNumber) throws IOException {
//        BufferedWriter writer = FileUtils.getFileWriter(String.format("target/items%d_%d.txt", startingSeed, attemptNumber));
        BufferedWriter writer = FileUtils.getFileWriter(String.format("%d/items.txt", Settings.startingSeed));
        if (writer == null) {
            return;
        }

        for (String location : mapOfItemLocationToItem.keySet()) {
            if(!location.equals(mapOfItemLocationToItem.get(location))) {
                writer.write(location + " => " + mapOfItemLocationToItem.get(location));
                writer.newLine();
            }
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

    public void updateFiles() throws Exception{
        List<String> locationsRelatedToBlocks = Arrays.asList("Map (Surface)", "mekuri.exe", "Mini Doll"); // todo: not hardcode this, eventually

        for(Map.Entry<String, String> locationAndItem : mapOfItemLocationToItem.entrySet()) {
            if(!locationAndItem.getKey().equals(locationAndItem.getValue())) {
                if(locationsRelatedToBlocks.contains(locationAndItem.getKey())) {
                    GameDataTracker.updateBlock(locationAndItem.getKey(), locationAndItem.getValue());
                }
                GameDataTracker.writeLocationContents(locationAndItem.getKey(), locationAndItem.getValue());
            }
        }
    }
}
