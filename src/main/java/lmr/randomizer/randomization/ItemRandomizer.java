package lmr.randomizer.randomization;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.node.CustomItemPlacement;
import lmr.randomizer.rcd.object.DropType;
import lmr.randomizer.util.ItemConstants;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 7/10/2017.
 */
public class ItemRandomizer {
    public static final List<String> ALL_SUBWEAPONS = Arrays.asList("Shuriken", "Rolling Shuriken", "Earth Spear", "Flare Gun", "Bomb", "Chakram", "Caltrops", "Pistol");

    private Map<String, String> mapOfItemLocationToItem = new HashMap<>(); // The map we're trying to build.
    private Map<String, Short> mapOfItemLocationToItemGraphicInventoryArg = new HashMap<>();
    private DropType subweaponPotContents = DropType.SHURIKEN_AMMO;

    private List<String> allItems; // All possible items.
    private List<String> unplacedItems; // Items that haven't been placed yet.

    private List<String> unassignedNonShopItemLocations;

    private ShopRandomizer shopRandomizer;
    private AccessChecker accessChecker;

    public ItemRandomizer() {
        allItems = new ArrayList<>(DataFromFile.getAllNonShopItemsPlusAllRandomizedShopItemsPlusAllRandomizedCoinChests());
        unplacedItems = new ArrayList<>(allItems);
        if(!"Whip".equals(Settings.getCurrentStartingWeapon())) {
            unplacedItems.add("Whip");
            unplacedItems.remove(Settings.getCurrentStartingWeapon());
        }
        if (Settings.isAlternateMotherAnkh()) {
            unplacedItems.add("Ankh Jewel (Extra)");
        }

        unassignedNonShopItemLocations = new ArrayList<>(DataFromFile.getNonShopItemLocations());
    }

    public ItemRandomizer(ItemRandomizer itemRandomizer) {
        allItems = new ArrayList<>(itemRandomizer.allItems);
        unplacedItems = new ArrayList<>(itemRandomizer.unplacedItems);
        unassignedNonShopItemLocations = new ArrayList<>(itemRandomizer.unassignedNonShopItemLocations);
        mapOfItemLocationToItem = new HashMap<>(itemRandomizer.mapOfItemLocationToItem);
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

        for(CustomItemPlacement customItemPlacement : DataFromFile.getCustomPlacementData().getCustomItemPlacements()) {
            String customLocation = customItemPlacement.getLocation();
            if(customLocation != null && !customLocation.startsWith("Shop ")) {
                mapOfItemLocationToItem.put(customLocation, customItemPlacement.getContents());
                unassignedNonShopItemLocations.remove(customLocation);
                unplacedItems.remove(customItemPlacement.getContents());
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
        if(!mapOfItemLocationToItem.containsKey("Provocative Bathing Suit")) {
            List<String> possibleItems = DataFromFile.getHTItems(unplacedItems);
            String item = possibleItems.get(random.nextInt(possibleItems.size()));
            mapOfItemLocationToItem.put("Provocative Bathing Suit", item);
            unplacedItems.remove(item);
            unassignedNonShopItemLocations.remove("Provocative Bathing Suit");
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
        shopRandomizer.updateNeburShop();
        return true;
    }

    public void placeSubweaponPotContents(Random random) {
        List<String> availableSubweapons = new ArrayList<>(ItemRandomizer.ALL_SUBWEAPONS);
        availableSubweapons.removeAll(Settings.getRemovedItems());
        availableSubweapons.removeAll(Settings.getCurrentRemovedItems());
        subweaponPotContents = getPotContents(availableSubweapons.isEmpty() ? null : availableSubweapons.get(random.nextInt(availableSubweapons.size())));
    }

    private DropType getPotContents(String weapon) {
        if(weapon == null) {
            return DropType.WEIGHTS;
        }
        else if("Shuriken".equals(weapon)) {
            return DropType.SHURIKEN_AMMO;
        }
        else if("Rolling Shuriken".equals(weapon)) {
            return DropType.ROLLING_SHURIKEN_AMMO;
        }
        else if("Earth Spear".equals(weapon)) {
            return DropType.EARTH_SPEAR_AMMO;
        }
        else if("Flare Gun".equals(weapon)) {
            return DropType.FLARE_GUN_AMMO;
        }
        else if("Bomb".equals(weapon)) {
            return DropType.BOMB_AMMO;
        }
        else if("Chakram".equals(weapon)) {
            return DropType.CHAKRAM_AMMO;
        }
        else if("Caltrops".equals(weapon)) {
            return DropType.CALTROPS_AMMO;
        }
        else if("Pistol".equals(weapon)) {
            return DropType.PISTOL_AMMO;
        }
        return DropType.WEIGHTS;
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

    public void outputLocations(BufferedWriter writer, int attemptNumber) throws IOException {
        writer.write(Translations.getText("section.items"));
        writer.newLine();

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

    public Set<String> getItemLocations() {
        Set<String> itemLocations = new HashSet<>(mapOfItemLocationToItem.keySet());
        itemLocations.add("Mobile Super X2"); // Not a traditional item, but it needs to be shuffled with rcd.
        if(!Settings.isRandomizeTrapItems()) {
            itemLocations.removeAll(DataFromFile.TRAP_ITEMS);
        }
        return itemLocations;
    }

    public DropType getSubweaponPotContents() {
        return subweaponPotContents;
    }

    public String getNewContents(String itemLocation) {
        if("Mobile Super X2".equals(itemLocation)) {
            return shopRandomizer.getShopItems(ShopRandomizer.MSX_SHOP_NAME).get(0);
        }
        return Settings.getUpdatedContents(mapOfItemLocationToItem.get(itemLocation));
    }

    public Short getGraphic(String itemLocation) {
        return mapOfItemLocationToItemGraphicInventoryArg.get(itemLocation);
    }

    public void assignRandomGraphics(int availableFlags, Random random) {
        if(availableFlags < 1) {
            return;
        }
        assignCustomGraphics();
        availableFlags -= getTotalRemovedItems();
        availableFlags -= mapOfItemLocationToItemGraphicInventoryArg.size();
        if(availableFlags < 1) {
            return;
        }

        String newContents;
        for(String itemLocation : getShuffledItemLocations(random)) {
            if(isLocationRelatedToBlock(itemLocation)) {
                continue;
            }
            if(isSnapshotsScan(itemLocation)) {
                continue;
            }
            if(isItemGraphicCustom(itemLocation)) {
                continue;
            }
            if("Maternity Statue".equals(itemLocation)) {
                continue;
            }
            newContents = Settings.getUpdatedContents(mapOfItemLocationToItem.get(itemLocation));
            if(isRemovedItem(newContents)) {
                continue;
            }
            if(isCoinChest(newContents)) {
                continue;
            }
            if(isTrapItem(newContents) && !isExplodingChest(newContents)) {
                if(DataFromFile.FLOATING_ITEM_LOCATIONS.contains(itemLocation)) {
                    mapOfItemLocationToItemGraphicInventoryArg.put(itemLocation, getRandomItemGraphic(random));
                }
                continue;
            }
            if(Settings.isRandomizeGraphics() && random.nextBoolean()) {
                mapOfItemLocationToItemGraphicInventoryArg.put(itemLocation, getRandomItemGraphic(random));
                availableFlags -= 1;
            }
            if(availableFlags < 1) {
                return;
            }
        }
    }

    private List<String> getShuffledItemLocations(Random random) {
        // Randomize the order in which locations are assigned, to avoid bias for which items
        // will be fake if item graphics are randomized
        List<String> itemLocations = new ArrayList<>(mapOfItemLocationToItem.keySet());
        itemLocations.removeAll(mapOfItemLocationToItemGraphicInventoryArg.keySet());
        List<String> shuffledLocations = new ArrayList<>(itemLocations.size());
        String location;
        while(!itemLocations.isEmpty()) {
            location = itemLocations.get(random.nextInt(itemLocations.size()));
            shuffledLocations.add(location);
            itemLocations.remove(location);
        }
        return shuffledLocations;
    }

    private static short getRandomItemGraphic(Random random) {
        int itemGraphicListSize = DataFromFile.RANDOM_ITEM_GRAPHICS.size();
        if(Settings.isRandomizeForbiddenTreasure()) {
            itemGraphicListSize += 1;
        }
        int itemArgIndex = random.nextInt(itemGraphicListSize);
        if(itemArgIndex == DataFromFile.RANDOM_ITEM_GRAPHICS.size()) {
            return ItemConstants.PROVOCATIVE_BATHING_SUIT;
        }
        return DataFromFile.RANDOM_ITEM_GRAPHICS.get(itemArgIndex).shortValue();
    }


    private boolean isLocationRelatedToBlock(String itemLocation) {
        return DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(itemLocation);
    }

    private boolean isSnapshotsScan(String itemLocation) {
        return DataFromFile.SNAPSHOTS_SCAN_LOCATIONS.contains(itemLocation);
    }

    private boolean isItemGraphicCustom(String itemLocation) {
        return mapOfItemLocationToItemGraphicInventoryArg.containsKey(itemLocation);
    }

    public boolean isRemovedItem(String newContents) {
        return isRemovedMap(newContents)
                || Settings.getCurrentRemovedItems().contains(newContents)
                || Settings.getRemovedItems().contains(newContents)
                || Settings.getStartingItemsIncludingCustom().contains(newContents);
    }

    private boolean isRemovedMap(String newContents) {
        return Settings.isReplaceMapsWithWeights() && newContents.startsWith("Map (") && !newContents.equals("Map (Shrine of the Mother)");
    }

    public boolean isCoinChest(String newContents) {
        return newContents.startsWith("Coin:");
    }

    public boolean isTrapItem(String newContents) {
        return Settings.isRandomizeTrapItems() && newContents.startsWith("Trap:");
    }

    public boolean isExplodingChest(String newContents) {
        return  newContents.equals(DataFromFile.EXPLODING_CHEST_NAME);
    }

    private void assignCustomGraphics() {
        for(CustomItemPlacement customItemPlacement : DataFromFile.getCustomPlacementData().getCustomItemPlacements()) {
            if(customItemPlacement.getItemGraphic() != null) {
                mapOfItemLocationToItemGraphicInventoryArg.put(customItemPlacement.getLocation(),
                        DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(customItemPlacement.getItemGraphic()).getInventoryArg());
            }
        }
    }

    private int getTotalRemovedItems() {
        int totalRemovedItems = 0;
        for(String itemLocation : mapOfItemLocationToItem.keySet()) {
            if(isRemovedItem(Settings.getUpdatedContents(mapOfItemLocationToItem.get(itemLocation)))) {
                totalRemovedItems += 1;
            }
        }
        return totalRemovedItems;
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
