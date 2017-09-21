package lmr.randomizer.random;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.node.AccessChecker;
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

    private ShopRandomizer shopRandomizer;
    private AccessChecker accessChecker;

    public ItemRandomizer() {
        allItems = new ArrayList<>(DataFromFile.getAllNonShopItemsPlusAllRandomizedShopItemsPlusAllRandomizedCoinChests());
        unplacedItems = new ArrayList<>(allItems);

        nonShopItemLocations = new ArrayList<>(DataFromFile.getNonShopItemLocations());
        unassignedNonShopItemLocations = new ArrayList<>(nonShopItemLocations);
    }

    public int getTotalShopItems() {
        return unplacedItems.size() - unassignedNonShopItemLocations.size();
    }

    public List<String> getAllItems() {
        return allItems;
    }

    public String getItem(String location) {
        return mapOfItemLocationToItem.get(location);
    }

    public void removeItemFromUnplacedItems(String originalShopItem) {
        unplacedItems.remove(originalShopItem);
    }

    public void randomizeForbiddenTreasure(Random random) {
        List<String> uselessMaps = Arrays.asList("Map (Surface)", "Map (Gate of Guidance)", "Map (Mausoleum of the Giants)", "Map (Temple of the Sun)",
                "Map (Spring in the Sky)", "Map (Inferno Cavern)", "Map (Chamber of Extinction)", "Map (Twin Labyrinths)", "Map (Endless Corridor)", "Map (Gate of Illusion)", "Map (Graveyard of the Giants)",
                "Map (Temple of Moonlight)", "Map (Tower of the Goddess)", "Map (Tower of Ruin)", "Map (Chamber of Birth)", "Map (Dimensional Corridor)");

        String uselessMap = uselessMaps.get(random.nextInt(uselessMaps.size()));
        String uselessMapLocation = null;
        for(Map.Entry<String, String> itemLocationAndContents : mapOfItemLocationToItem.entrySet()) {
            if(uselessMap.equals(itemLocationAndContents.getValue())) {
                uselessMapLocation = itemLocationAndContents.getKey();
            }
        }
        if(uselessMapLocation == null) {
            shopRandomizer.randomizeForbiddenTreasure(uselessMap, true);
        }
        else {
            mapOfItemLocationToItem.put(uselessMapLocation, "Forbidden Treasure");
            shopRandomizer.randomizeForbiddenTreasure(uselessMap, false);
        }
    }

    public void placeNonRandomizedItems() {
//        mapOfItemLocationToItem.put("xmailer.exe", "Book of the Dead");
//        unassignedNonShopItemLocations.remove("xmailer.exe");
//        unplacedItems.remove("Book of the Dead");
//        mapOfItemLocationToItem.put("Coin: Surface (Waterfall)", "Shell Horn");
//        unassignedNonShopItemLocations.remove("Coin: Surface (Waterfall)");
//        unplacedItems.remove("Shell Horn");
//        mapOfItemLocationToItem.put("Shell Horn", "Coin: Surface (Waterfall)");
//        unassignedNonShopItemLocations.remove("Shell Horn");
//        unplacedItems.remove("Coin: Surface (Waterfall)");
//        mapOfItemLocationToItem.put("Sacred Orb (Gate of Guidance)", "Flail Whip");
//        unassignedNonShopItemLocations.remove("Sacred Orb (Gate of Guidance)");
//        unplacedItems.remove("Flail Whip");

//        mapOfItemLocationToItem.put("Anchor", "Ankh Jewel (Gate of Guidance)");
//        unassignedNonShopItemLocations.remove("Anchor");
//        unplacedItems.remove("Ankh Jewel (Gate of Guidance)");
//
//        mapOfItemLocationToItem.put("Pepper", "Ankh Jewel (Temple of the Sun)");
//        unassignedNonShopItemLocations.remove("Pepper");
//        unplacedItems.remove("Ankh Jewel (Temple of the Sun)");
//
//        mapOfItemLocationToItem.put("Mini Doll", "Ankh Jewel (Mausoleum of the Giants)");
//        unassignedNonShopItemLocations.remove("Mini Doll");
//        unplacedItems.remove("Ankh Jewel (Mausoleum of the Giants)");
//
//        mapOfItemLocationToItem.put("Ankh Jewel (Temple of the Sun)", "Ankh Jewel (Spring in the Sky)");
//        unassignedNonShopItemLocations.remove("Ankh Jewel (Temple of the Sun)");
//        unplacedItems.remove("Ankh Jewel (Spring in the Sky)");
//
//        mapOfItemLocationToItem.put("Book of the Dead", "Ankh Jewel (Tower of Ruin)");
//        unassignedNonShopItemLocations.remove("Book of the Dead");
//        unplacedItems.remove("Ankh Jewel (Tower of Ruin)");

        for(String item : DataFromFile.getNonRandomizedItems()) {
            mapOfItemLocationToItem.put(item, item);
            unassignedNonShopItemLocations.remove(item);
            unplacedItems.remove(item);
        }
        if(Settings.isRandomizeCoinChests()) {
            for(String coinChest : DataFromFile.getNonRandomizedCoinChests()) {
                mapOfItemLocationToItem.put(coinChest, coinChest);
                unassignedNonShopItemLocations.remove(coinChest);
                unplacedItems.remove(coinChest);
            }
        }
    }

    public boolean placeRequiredItems(List<String> items, Random random) {
        List<String> initialUnassignedNonShopLocations = new ArrayList<>(DataFromFile.getInitialNonShopItemLocations());
        if(Settings.isRandomizeCoinChests()) {
            initialUnassignedNonShopLocations.addAll(DataFromFile.getInitialCoinChestLocations());
        }
        initialUnassignedNonShopLocations.removeAll(DataFromFile.getNonRandomizedItems());
        initialUnassignedNonShopLocations.removeAll(DataFromFile.getNonRandomizedCoinChests());
        List<String> initialUnassignedShopItemLocations = shopRandomizer.getInitialUnassignedShopItemLocations();
        int locationIndexIndex;

        List<String> itemsToPlace = new ArrayList<>(items);
        itemsToPlace.removeAll(DataFromFile.getNonRandomizedItems());

        int size = itemsToPlace.size();
        for(int i = 0; i < size; i++) {
            String item = getRandomItem(itemsToPlace, random);
            int availableLocations = initialUnassignedNonShopLocations.size() + initialUnassignedShopItemLocations.size();
            List<Integer> availableLocationIndices = buildIndices(availableLocations);

            while(true) {
                if(availableLocationIndices.size() == 0) {
                    return false;
                }
                locationIndexIndex = random.nextInt(availableLocationIndices.size());
                int locationIndex = availableLocationIndices.get(locationIndexIndex);
                if(locationIndex < initialUnassignedNonShopLocations.size()) {
                    // todo: check for exceptions
                    String location = initialUnassignedNonShopLocations.get(locationIndex);
                    if(accessChecker.validRequirements(item, location)) {
                        mapOfItemLocationToItem.put(location, item);
                        itemsToPlace.remove(item);
                        initialUnassignedNonShopLocations.remove(location);
                        unassignedNonShopItemLocations.remove(location);
                        unplacedItems.remove(item);
                        break;
                    }
                    else {
                        availableLocationIndices.remove(locationIndexIndex);
                    }
                }
                else {
                    if(shopRandomizer.placeRequiredItem(item, initialUnassignedShopItemLocations, locationIndex - initialUnassignedNonShopLocations.size())) {
                        itemsToPlace.remove(item);
                        unplacedItems.remove(item);
                        break;
                    }
                    else {
                        availableLocationIndices.remove(locationIndexIndex);
                    }
                }
            }
        }
        return true;
    }

    public boolean placeCoinChests(Random random) {
        List<String> coinChests = new ArrayList<>();
        for(String coinChest : DataFromFile.getAllCoinChests()) {
            if(unplacedItems.contains(coinChest)) {
                coinChests.add(coinChest);
            }
        }
        if(coinChests.isEmpty()) {
            return true;
        }

        int locationIndexIndex;

        List<String> possibleLocations = new ArrayList<>();
        for(String itemChestLocation : DataFromFile.getChestOnlyLocations()) {
            if(unassignedNonShopItemLocations.contains(itemChestLocation)) {
                possibleLocations.add(itemChestLocation);
            }
        }
        for(String coinChest : DataFromFile.getAllCoinChests()) {
            if(unassignedNonShopItemLocations.contains(coinChest)) {
                possibleLocations.add(coinChest);
            }
        }
        int size = coinChests.size();
        for(int i = 0; i < size; i++) {
            String item = getRandomItem(coinChests, random);
            int availableLocations = possibleLocations.size();
            List<Integer> availableLocationIndices = buildIndices(availableLocations);

            while(true) {
                if(availableLocationIndices.isEmpty()) {
                    return false;
                }
                locationIndexIndex = random.nextInt(availableLocationIndices.size());
                int locationIndex = availableLocationIndices.get(locationIndexIndex);
                String location = possibleLocations.get(locationIndex);
                if(accessChecker.validRequirements(item, location)) {
                    mapOfItemLocationToItem.put(location, item);
                    coinChests.remove(item);
                    possibleLocations.remove(location);
                    unassignedNonShopItemLocations.remove(location);
                    unplacedItems.remove(item);
                    break;
                }
                else {
                    availableLocationIndices.remove(locationIndexIndex);
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
            if(!placeItem(getRandomItem(unplacedItems, random), availableLocations, random)) {
                return false;
            }
        }
        return true;
    }

    private boolean placeItem(String item, int availableLocations, Random random) {
        List<Integer> availableLocationIndices = buildIndices(availableLocations);
        int locationIndexIndex;

        while(true) {
            if(availableLocationIndices.size() == 0) {
                return false;
            }
            locationIndexIndex = random.nextInt(availableLocationIndices.size());
            int locationIndex = availableLocationIndices.get(locationIndexIndex);
            if(locationIndex < unassignedNonShopItemLocations.size()) {
                String location = unassignedNonShopItemLocations.get(locationIndex);
                if (accessChecker.validRequirements(item, location)) {
                    mapOfItemLocationToItem.put(location, item);
                    unassignedNonShopItemLocations.remove(location);
                    unplacedItems.remove(item);
                    return true;
                }
                else {
                    availableLocationIndices.remove(locationIndexIndex);
                }
            }
            else {
                if(shopRandomizer.placeItem(item, locationIndex - unassignedNonShopItemLocations .size())) {
                    unplacedItems.remove(item);
                    return true;
                }
                else {
                    availableLocationIndices.remove(locationIndexIndex);
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
        BufferedWriter writer = FileUtils.getFileWriter(String.format("%d/items.txt", Settings.getStartingSeed()));
        if (writer == null) {
            return;
        }

        for (String location : mapOfItemLocationToItem.keySet()) {
            writer.write(mapOfItemLocationToItem.get(location) + ": " + location + " location");
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

    public void updateFiles() throws Exception{
        for(Map.Entry<String, String> locationAndItem : mapOfItemLocationToItem.entrySet()) {
            if(!locationAndItem.getKey().equals(locationAndItem.getValue())) {
                if(DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(locationAndItem.getKey())) {
                    GameDataTracker.updateBlock(locationAndItem.getKey(), locationAndItem.getValue());
                }
                GameDataTracker.writeLocationContents(locationAndItem.getKey(), locationAndItem.getValue());
            }
        }
    }

    public String findNameOfNodeContainingItem(String itemToLookFor) {
        if(!mapOfItemLocationToItem.containsValue(itemToLookFor)) {
            return shopRandomizer.findNameOfShopNodeContainingItem(itemToLookFor);
        }
        for(Map.Entry<String, String> itemLocationAndContents : mapOfItemLocationToItem.entrySet()) {
            if(itemLocationAndContents.getValue().equals(itemToLookFor)) {
                return itemLocationAndContents.getKey();
            }
        }
        return null;
    }
}
