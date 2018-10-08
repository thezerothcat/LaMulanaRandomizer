package lmr.randomizer.random;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.node.CustomPlacement;
import lmr.randomizer.update.GameDataTracker;
import lmr.randomizer.update.GameObjectId;

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

    private List<String> unassignedNonShopItemLocations;

    private ShopRandomizer shopRandomizer;
    private AccessChecker accessChecker;

    private Map<Integer, Integer> mapOfWorldFlagToAssignedReplacementFlag;
    private int nextReplacedItemFlag;

    public ItemRandomizer() {
        allItems = new ArrayList<>(DataFromFile.getAllNonShopItemsPlusAllRandomizedShopItemsPlusAllRandomizedCoinChests());
        unplacedItems = new ArrayList<>(allItems);
        if(Settings.isRandomizeMainWeapon()) {
            unplacedItems.add("Whip");
            unplacedItems.remove(Settings.getCurrentStartingWeapon());
        }
        if (Settings.isAlternateMotherAnkh()) {
            unplacedItems.add("Ankh Jewel (Extra)");
        }

        unassignedNonShopItemLocations = new ArrayList<>(DataFromFile.getNonShopItemLocations());

        mapOfWorldFlagToAssignedReplacementFlag = new HashMap<>();
        nextReplacedItemFlag = 2724;
    }

    public ItemRandomizer(ItemRandomizer itemRandomizer) {
        allItems = new ArrayList<>(itemRandomizer.allItems);
        unplacedItems = new ArrayList<>(itemRandomizer.unplacedItems);
        unassignedNonShopItemLocations = new ArrayList<>(itemRandomizer.unassignedNonShopItemLocations);
        mapOfItemLocationToItem = new HashMap<>(itemRandomizer.mapOfItemLocationToItem);
        mapOfWorldFlagToAssignedReplacementFlag = new HashMap<>(itemRandomizer.mapOfWorldFlagToAssignedReplacementFlag);
        nextReplacedItemFlag = itemRandomizer.nextReplacedItemFlag;
    }

    public int getTotalShopItems() {
        return unplacedItems.size() - unassignedNonShopItemLocations.size();
    }

    public String getItem(String location) {
        return mapOfItemLocationToItem.get(location);
    }

    public void removeItemFromUnplacedItems(String originalShopItem) {
        unplacedItems.remove(originalShopItem);
    }

    public void placeNonRandomizedItems() {
        for(String item : DataFromFile.getNonRandomizedItems()) {
            if(!Settings.getStartingItemsIncludingCustom().contains(item)) {
                mapOfItemLocationToItem.put(item, item);
                unassignedNonShopItemLocations.remove(item);
                unplacedItems.remove(item);
            }
        }

        for(CustomPlacement customPlacement : DataFromFile.getCustomItemPlacements()) {
            String customLocation = customPlacement.getLocation();
            if(!customPlacement.isRemoveItem() && !customPlacement.isCurseChest()
                    && !customPlacement.isRemoveLogic()
                    && customLocation != null && !customLocation.startsWith("Shop ")) {
                mapOfItemLocationToItem.put(customLocation, customPlacement.getContents());
                unassignedNonShopItemLocations.remove(customLocation);
                unplacedItems.remove(customPlacement.getContents());
            }
        }
    }

    public boolean placeNoRequirementItems(List<String> items, Random random) {
        List<String> initialUnassignedNonShopLocations = new ArrayList<>(DataFromFile.getInitialNonShopItemLocations());
        initialUnassignedNonShopLocations.removeAll(Settings.getCurrentCursedChests());
        initialUnassignedNonShopLocations.removeAll(mapOfItemLocationToItem.keySet());

        List<String> initialUnassignedShopItemLocations = shopRandomizer.getInitialUnassignedShopItemLocations();
        int locationIndexIndex;

        List<String> itemsToPlace = new ArrayList<>(items);
        itemsToPlace.removeAll(mapOfItemLocationToItem.values());
        itemsToPlace.removeAll(shopRandomizer.getPlacedShopItems());
        items.removeAll(Settings.getStartingItemsIncludingCustom());

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

    public boolean placeForbiddenTreasureItem(Random random) {
        List<String> possibleItems = DataFromFile.getHTItems(unplacedItems);
        String item = possibleItems.get(random.nextInt(possibleItems.size()));
        mapOfItemLocationToItem.put("Provocative Bathing Suit", item);
        unplacedItems.remove(item);
        unassignedNonShopItemLocations.remove("Provocative Bathing Suit");
        return true;
    }

    public boolean placeChestOnlyItems(Random random) {
        List<String> chestOnlyItems = getUnplacedChestOnlyItems();
        if(chestOnlyItems.isEmpty()) {
            return true;
        }

        int locationIndexIndex;

        List<String> possibleLocations = getPossibleChestOnlyItemLocations();
        int size = chestOnlyItems.size();
        for(int i = 0; i < size; i++) {
            String item = getRandomItem(chestOnlyItems, random);
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
                    chestOnlyItems.remove(item);
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

    private List<String> getUnplacedChestOnlyItems() {
        List<String> chestOnlyItems = new ArrayList<>();
        if(Settings.isRandomizeCoinChests()) {
            for(String coinChest : DataFromFile.getAllCoinChests()) {
                if(unplacedItems.contains(coinChest)) {
                    chestOnlyItems.add(coinChest);
                }
            }
        }
        if(Settings.isRandomizeTrapItems() && unplacedItems.contains(DataFromFile.EXPLODING_CHEST_NAME)) {
            chestOnlyItems.add(DataFromFile.EXPLODING_CHEST_NAME);
        }
        return chestOnlyItems;
    }

    private List<String> getPossibleChestOnlyItemLocations() {
        List<String> possibleLocations = new ArrayList<>();
        for(String itemChestLocation : DataFromFile.getChestOnlyLocations()) {
            if(unassignedNonShopItemLocations.contains(itemChestLocation)) {
                possibleLocations.add(itemChestLocation);
            }
        }
        if(Settings.isRandomizeCoinChests()) {
            for(String coinChest : DataFromFile.getAllCoinChests()) {
                if(unassignedNonShopItemLocations.contains(coinChest)) {
                    possibleLocations.add(coinChest);
                }
            }
        }
        if(Settings.isRandomizeTrapItems()) {
            if(unassignedNonShopItemLocations.contains(DataFromFile.EXPLODING_CHEST_NAME)) {
                possibleLocations.add(DataFromFile.EXPLODING_CHEST_NAME);
            }
            if(unassignedNonShopItemLocations.contains(DataFromFile.GRAVEYARD_TRAP_CHEST_NAME)) {
                possibleLocations.add(DataFromFile.GRAVEYARD_TRAP_CHEST_NAME);
            }
        }
        return possibleLocations;
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

        Comparator<String> itemNameComparator = new Comparator<String>() {
            @Override
            public int compare(String itemName1, String itemName2) {
                if(itemName1.startsWith("Coin:") && itemName2.startsWith("Coin:")) {
                    int coinCount1 = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(itemName1).getInventoryArg();
                    int coinCount2 = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(itemName2).getInventoryArg();
                    return Integer.compare(coinCount1, coinCount2);
                }
                String translatedItem1 = Translations.getItemText(Settings.getUpdatedContents(itemName1), false);
                String translatedItem2 = Translations.getItemText(Settings.getUpdatedContents(itemName2), false);
                return translatedItem1.compareToIgnoreCase(translatedItem2);
            }
        };

        writer.write(Translations.getItemText(Settings.getCurrentStartingWeapon(), false) + ": starting weapon");
        writer.newLine();

        List<String> sortedStartingItems = new ArrayList<>(Settings.getStartingItemsIncludingCustom());
        Collections.sort(sortedStartingItems, itemNameComparator);

        for(String itemName : sortedStartingItems) {
            writer.write(Translations.getItemText(itemName, false) + ": starting item");
            writer.newLine();
        }
        writer.newLine();

        Map<String, String> mapOfItemToLocation = buildReverseMap(mapOfItemLocationToItem);
        List<String> itemNames = new ArrayList<>(mapOfItemToLocation.keySet());
        Collections.sort(itemNames, itemNameComparator);

        for (String itemName : itemNames) {
            boolean isRemoved = Settings.getCurrentRemovedItems().contains(itemName)
                    || Settings.getRemovedItems().contains(itemName)
                    || Settings.getStartingItemsIncludingCustom().contains(itemName);
            String location = mapOfItemToLocation.get(itemName);
            itemName = Translations.getItemText(Settings.getUpdatedContents(itemName), isRemoved);
            location = Translations.getLocationText(location, Settings.getCurrentCursedChests().contains(location));
            writer.write(itemName + ": " + location);
            writer.newLine();
        }

        writer.flush();
        writer.close();
    }

    private Map<String, String> buildReverseMap(Map<String, String> originalMap) {
        Map<String, String> reverseMap = new HashMap<>();
        for(String key : originalMap.keySet()) {
            reverseMap.put(originalMap.get(key), key);
        }
        return reverseMap;
    }

    public void setShopRandomizer(ShopRandomizer shopRandomizer) {
        this.shopRandomizer = shopRandomizer;
    }

    public void setAccessChecker(AccessChecker accessChecker) {
        this.accessChecker = accessChecker;
    }

    public void updateFiles(Random random) throws Exception{
        String itemLocation;
        String newContents;
        int newWorldFlag;
        Map<String, GameObjectId> nameToDataMap = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData();
        GameObjectId itemNewContentsData;
        GameObjectId itemLocationData;
        for(Map.Entry<String, String> locationAndItem : mapOfItemLocationToItem.entrySet()) {
            itemLocation = locationAndItem.getKey();
            newContents = Settings.getUpdatedContents(locationAndItem.getValue());
            itemLocationData = nameToDataMap.get(itemLocation);
            itemNewContentsData = nameToDataMap.get(newContents);
            newWorldFlag = getNewWorldFlag(itemLocation, newContents, itemLocationData, itemNewContentsData);
            if(DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(locationAndItem.getKey())) {
                GameDataTracker.updateBlock(itemLocationData, itemNewContentsData);
            }
            GameDataTracker.writeLocationContents(itemLocation, newContents, itemLocationData, itemNewContentsData, newWorldFlag, random);
        }
    }

    private int getNewWorldFlag(String itemLocation, String newContents, GameObjectId itemLocationData, GameObjectId itemNewContentsData) {
        if(itemNewContentsData.getInventoryArg() == 62 && Settings.getRemovedItems().contains("Spaulder")) {
            return 2781;
        }
        if("Whip".equals(newContents)
                && (Settings.getCurrentRemovedItems().contains("Whip") || Settings.getRemovedItems().contains("Whip"))) {
            return 2773;
        }
        if(Settings.isReplaceMapsWithWeights()
                && itemNewContentsData.getInventoryArg() == 70 && itemNewContentsData.getWorldFlag() != 218
                && !DataFromFile.FLOATING_ITEM_LOCATIONS.contains(itemLocation)) {
            if(itemNewContentsData.getWorldFlag() == 209) {
                return 2708; // Surface map
            }
            if(itemNewContentsData.getWorldFlag() == 210) {
                return 2709; // Guidance map
            }
            if(itemNewContentsData.getWorldFlag() == 211) {
                return 2710; // Mausoleum map
            }
            if(itemNewContentsData.getWorldFlag() == 212) {
                return 2711; // Sun map
            }
            if(itemNewContentsData.getWorldFlag() == 213) {
                return 2712; // Spring map
            }
            if(itemNewContentsData.getWorldFlag() == 214) {
                return 2713; // Inferno map
            }
            if(itemNewContentsData.getWorldFlag() == 215) {
                return 2714; // Extinction map
            }
            if(itemNewContentsData.getWorldFlag() == 216) {
                return 2715; // Twin Labyrinths map
            }
            if(itemNewContentsData.getWorldFlag() == 217) {
                return 2716; // Endless map
            }
            if(itemNewContentsData.getWorldFlag() == 219) {
                return 2717; // Illusion map
            }
            if(itemNewContentsData.getWorldFlag() == 220) {
                return 2718; // Graveyard map
            }
            if(itemNewContentsData.getWorldFlag() == 221) {
                return 2719; // Moonlight map
            }
            if(itemNewContentsData.getWorldFlag() == 222) {
                return 2720; // Goddess map
            }
            if(itemNewContentsData.getWorldFlag() == 223) {
                return 2721; // Ruin map
            }
            if(itemNewContentsData.getWorldFlag() == 224) {
                return 2722; // Birth map
            }
            if(itemNewContentsData.getWorldFlag() == 225) {
                return 2723; // Dimensional map
            }
        }
        if(Settings.getCurrentRemovedItems().contains(newContents)
                || Settings.getRemovedItems().contains(newContents)
                || Settings.getStartingItemsIncludingCustom().contains(newContents)) {
            Integer newChestWorldFlag = mapOfWorldFlagToAssignedReplacementFlag.get(itemLocationData.getWorldFlag());
            if (newChestWorldFlag == null) {
                newChestWorldFlag = nextReplacedItemFlag++;
                mapOfWorldFlagToAssignedReplacementFlag.put(itemLocationData.getWorldFlag(), newChestWorldFlag);
            }
            return newChestWorldFlag;
        }
        return itemNewContentsData.getWorldFlag();
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
