package lmr.randomizer.randomization;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.node.CustomItemPlacement;
import lmr.randomizer.node.MoneyChecker;
import lmr.randomizer.randomization.data.ItemPriceCount;
import lmr.randomizer.randomization.data.ShopInventory;
import lmr.randomizer.randomization.data.ShopInventoryData;
import lmr.randomizer.util.LocationCoordinateMapper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 8/17/2017.
 */
public abstract class ShopRandomizer {
    public static final String MSX_SHOP_NAME = "Shop 2 Alt (Surface)";
    public static final String NON_MSX_SHOP_NAME = "Shop 2 (Surface)";
    public static final String FISH_FAIRY_SHOP_NAME = "Shop 12 Alt (Spring)";

    public static final String LITTLE_BROTHER_SHOP_NAME = "Shop 18 (Lil Bro)";

    protected static final String GRAVEYARD_SHOP_NAME = "Shop 7 (Graveyard)";

    protected AccessChecker accessChecker;
    protected ItemRandomizer itemRandomizer; // Not needed for this version of the randomizer?
    protected NpcRandomizer npcRandomizer;
    protected ShopItemPriceCountRandomizer shopItemPriceCountRandomizer;

    protected Map<String, String> mapOfShopInventoryItemToContents = new HashMap<>(); // The thing we're trying to build.
    protected Map<String, ShopInventory> mapOfShopNameToShopInventory = new HashMap<>(); // Storing a shuffled shop inventory, complete with item pricing, for later use.
    protected List<String> unassignedShopItemLocations = new ArrayList<>(); // Shop locations which still need something placed.
    protected List<String> randomizedShops;

    protected List<String> shopsWithTransformations;

    public abstract ShopRandomizer copy();

    public List<String> getUnassignedShopItemLocations() {
        return unassignedShopItemLocations;
    }

    public abstract void placeNonRandomizedItems();

    public abstract void placeSpecialSubweaponAmmo(Random random);

    public List<String> getShopItems(String shopName) {
        List<String> shopItems = new ArrayList<>();
        String shopItem;
        for (int i = 1; i <= 3; i++) {
            shopItem = mapOfShopInventoryItemToContents.get(String.format("%s Item %d", shopName, i));
            if (shopItem != null && !"Weights".equals(shopItem) && !shopItem.startsWith("Hint")) {
                shopItems.add(shopItem);
            }
        }
        return shopItems;
    }

    public ShopInventory getShopInventory(String shopName) {
        if(mapOfShopNameToShopInventory.containsKey(shopName)) {
            return mapOfShopNameToShopInventory.get(shopName);
        }
        String npcName = npcRandomizer.getShopNpcName(shopName);
        ShopInventory shopInventory = new ShopInventory(shopName, npcName, npcName == null ? null : npcRandomizer.getNpcLocation(npcName).replaceAll("NPCL: ", ""));
        ShopInventoryData shopInventoryData;
        String shopItemLocation;
        String shopItem;
        boolean removedItem;
        for (int i = 1; i <= 3; i++) {
            shopItemLocation = String.format("%s Item %d", shopName, i);
            shopItem = Settings.getUpdatedContents(mapOfShopInventoryItemToContents.get(shopItemLocation));
            removedItem = isRemovedItem(shopItem);
            if(removedItem) {
                shopItem = "Weights";
            }

            shopInventoryData = new ShopInventoryData();
            Short customItemGraphic = getCustomItemGraphic(shopItemLocation);
            if(customItemGraphic == null) {
                shopInventoryData.setInventoryArg(ShopInventoryData.getInventoryArg(shopItem));
            }
            else {
                shopInventoryData.setInventoryArg(customItemGraphic);
            }
            shopInventoryData.setWorldFlag(ShopInventoryData.getWorldFlag(shopItem));
            shopInventoryData.setItemPriceCount(getItemPriceCount(shopItem, shopName, i, removedItem));
            if(shopItem.startsWith("Custom")) {
                shopInventoryData.setCustomTextNumber(Integer.parseInt(shopItem.replaceAll("^Custom ?", "")));
            }
            shopInventory.setItem(i, shopInventoryData);
        }
        mapOfShopNameToShopInventory.put(shopName, shopInventory);
        return shopInventory;
    }

    private Short getCustomItemGraphic(String shopItemLocation) {
        for(CustomItemPlacement customItemPlacement : DataFromFile.getCustomPlacementData().getCustomItemPlacements()) {
            if(customItemPlacement.getItemGraphic() != null && customItemPlacement.getLocation().equals(shopItemLocation)) {
                return DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(customItemPlacement.getItemGraphic()).getInventoryArg();
            }
        }
        return null;
    }

    private boolean isRemovedItem(String shopItem) {
        return Settings.getCurrentRemovedItems().contains(shopItem)
                || Settings.getRemovedItems().contains(shopItem)
                || Settings.getStartingItemsIncludingCustom().contains(shopItem)
                || (Settings.isReplaceMapsWithWeights() && shopItem.startsWith("Map (") && !"Map (Shrine of the Mother)".equals(shopItem));
    }

    public ItemPriceCount getItemPriceCount(String shopItem, String shopName, int shopItemNumber, boolean isRemovedItem) {
        if(MSX_SHOP_NAME.equals(shopName) && shopItemNumber != 1
                && mapOfShopNameToShopInventory.containsKey(NON_MSX_SHOP_NAME)) {
            ShopInventoryData shopInventoryData = mapOfShopNameToShopInventory.get(NON_MSX_SHOP_NAME).getShopInventoryData(shopItemNumber);
            if(ShopInventoryData.getInventoryArg(shopItem) == shopInventoryData.getInventoryArg()) {
                return shopInventoryData.getItemPriceCount();
            }
        }
        if(NON_MSX_SHOP_NAME.equals(shopName) && shopItemNumber != 1
                && mapOfShopNameToShopInventory.containsKey(MSX_SHOP_NAME)) {
            ShopInventoryData shopInventoryData = mapOfShopNameToShopInventory.get(MSX_SHOP_NAME).getShopInventoryData(shopItemNumber);
            if(ShopInventoryData.getInventoryArg(shopItem) == shopInventoryData.getInventoryArg()) {
                return shopInventoryData.getItemPriceCount();
            }
        }
        return shopItemPriceCountRandomizer.getItemPriceCount(shopItem, String.format("%s Item %d", shopName, shopItemNumber), isRemovedItem);
    }

    public boolean placeRequiredItem(String item, List<String> shopLocationOptions, int locationIndex) {
        String location = shopLocationOptions.get(locationIndex);
        if(accessChecker.validRequirements(item, location)) {
            shopLocationOptions.remove(location);
            mapOfShopInventoryItemToContents.put(location, item);
            unassignedShopItemLocations.remove(location);
//            if(location.startsWith(MSX_SHOP_NAME) && !location.endsWith("1")) {
//                location = location.replace(MSX_SHOP_NAME, NON_MSX_SHOP_NAME);
//                mapOfShopInventoryItemToContents.put(location, item);
//                unassignedShopItemLocations.remove(location);
//            }
//            else if(location.startsWith(NON_MSX_SHOP_NAME) && !location.endsWith("1")) {
//                location = location.replace(NON_MSX_SHOP_NAME, MSX_SHOP_NAME);
//                mapOfShopInventoryItemToContents.put(location, item);
//                unassignedShopItemLocations.remove(location);
//            }
            return true;
        }
        return false;
    }

    public boolean placeItem(String item, int locationIndex) {
        String location = unassignedShopItemLocations.get(locationIndex);
        if(accessChecker.validRequirements(item, location)) {
            mapOfShopInventoryItemToContents.put(location, item);
            unassignedShopItemLocations.remove(location);
            if(item.contains("Sacred Orb")) {
                shopsWithTransformations.add(location.substring(0, location.indexOf(")") + 1));
            }

//            if(location.startsWith(MSX_SHOP_NAME) && !location.endsWith("1")) {
//                location = location.replace(MSX_SHOP_NAME, NON_MSX_SHOP_NAME);
//                mapOfShopInventoryItemToContents.put(location, item);
//                unassignedShopItemLocations.remove(location);
//            }
//            else if(location.startsWith(NON_MSX_SHOP_NAME) && !location.endsWith("1")) {
//                location = location.replace(NON_MSX_SHOP_NAME, MSX_SHOP_NAME);
//                mapOfShopInventoryItemToContents.put(location, item);
//                unassignedShopItemLocations.remove(location);
//            }
            return true;
        }
        return false;
    }

    public void updateNeburShop() {
        String location = MSX_SHOP_NAME + " Item 2";
        if(!mapOfShopInventoryItemToContents.containsKey(location)) {
            mapOfShopInventoryItemToContents.put(location, mapOfShopInventoryItemToContents.get(NON_MSX_SHOP_NAME + " Item 2"));
        }
        location = MSX_SHOP_NAME + " Item 3";
        if(!mapOfShopInventoryItemToContents.containsKey(location)) {
            mapOfShopInventoryItemToContents.put(location, mapOfShopInventoryItemToContents.get(NON_MSX_SHOP_NAME + " Item 3"));
        }
    }

    public boolean shopHasTransformation(String shopName) {
        return shopsWithTransformations.contains(shopName);
    }

    public List<String> getInitialUnassignedShopItemLocations() {
        List<String> initialUnassigned = new ArrayList<>();
        List<String> initialShops = DataFromFile.getInitialShops();
        for(String shopName : initialShops) {
            for(String unassigned : unassignedShopItemLocations) {
                if(unassigned.contains(shopName)) {
                    initialUnassigned.add(unassigned);
                }
            }
        }
        return initialUnassigned;
    }

    public abstract List<String> getPlacedShopItems();

    public abstract void determineItemTypes(Random random);

    public void outputLocations(BufferedWriter writer, int attemptNumber) throws IOException {
        writer.newLine();
        writer.write(Translations.getText("section.shops"));
        writer.newLine();

        String location;
        boolean addNewline = false;
        for(String shop : randomizedShops) {
            if(LocationCoordinateMapper.isSurfaceStart() && DataFromFile.CUSTOM_SHOP_NAME.equals(shop)) {
                continue;
            }

            if(addNewline) {
                writer.newLine();
            }
            else if(Settings.isRandomizeNpcs()) {
                addNewline = true;
            }

            if(DataFromFile.CUSTOM_SHOP_NAME.equals(shop)) {
                writer.write(Translations.getText("shops.Shop0Default") + ":");
                writer.newLine();
            }
            else {
                String shopNpcName = npcRandomizer.getShopNpcName(shop);
                String npcLocationKey = npcRandomizer.getNpcLocation(shopNpcName);
                if(npcLocationKey != null) {
                    npcLocationKey = npcLocationKey.replaceAll("NPCL: ", "");
                    String npcZone = npcRandomizer.getNpcZone(npcLocationKey);
                    if(MSX_SHOP_NAME.equals(shop)) {
                        shopNpcName = "NeburAlt";
                    }
                    writer.write(Translations.getShopLabel(shopNpcName, npcLocationKey, npcZone));
                    writer.newLine();
                }
            }
            for (int i = 1; i <= 3; i++) {
                location = String.format("%s Item %d", shop, i);
                writer.write("\t" + String.format(Translations.getText("shops.ItemFormat"), i) + ": ");
                if(mapOfShopInventoryItemToContents.containsKey(location)) {
                    String itemName = Settings.getUpdatedContents(mapOfShopInventoryItemToContents.get(location));
                    writer.write(Translations.getItemText(itemName, itemRandomizer.isRemovedItem(itemName)));
                }
                else {
                    writer.write(Translations.getText("shops.unchanged"));
                }
                writer.newLine();
            }
        }
    }

    public void setAccessChecker(AccessChecker accessChecker) {
        this.accessChecker = accessChecker;
    }

    public void setItemRandomizer(ItemRandomizer itemRandomizer) {
        this.itemRandomizer = itemRandomizer;
    }

    public void setNpcRandomizer(NpcRandomizer npcRandomizer) {
        this.npcRandomizer = npcRandomizer;
    }

    public abstract void initShopItemPriceCountRandomizer(boolean subweaponOnly, MoneyChecker moneyChecker, Random random);

    public String findNameOfShopNodeContainingItem(String itemToLookFor) {
        for(Map.Entry<String, String> shopNameAndContents : mapOfShopInventoryItemToContents.entrySet()) {
            if(shopNameAndContents.getValue().equals(itemToLookFor)) {
                return shopNameAndContents.getKey().substring(0, shopNameAndContents.getKey().indexOf(")") + 1);
            }
        }
        return null;
    }
}
