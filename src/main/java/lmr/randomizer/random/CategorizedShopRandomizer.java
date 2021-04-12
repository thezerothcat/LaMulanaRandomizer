package lmr.randomizer.random;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.ItemPriceCount;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.shop.ShopBlock;
import lmr.randomizer.node.CustomItemPlacement;
import lmr.randomizer.node.MoneyChecker;
import lmr.randomizer.update.GameDataTracker;
import lmr.randomizer.update.GameObjectId;
import lmr.randomizer.update.LocationCoordinateMapper;

import java.util.*;

/**
 * Created by thezerothcat on 8/2/2017.
 *
 * Currently only randomizes unique items. Should probably eventually randomize ammo options also.
 */
public class CategorizedShopRandomizer extends ShopRandomizer {
    public CategorizedShopRandomizer() {
        randomizedShops = new ArrayList<>(DataFromFile.getAllShops());

        List<String> originalShopContents;
        String originalShopItem;
        for(String shop : DataFromFile.getAllShops()) {
            if(MSX_SHOP_NAME.equals(shop)) {
                unassignedShopItemLocations.add(String.format("%s Item 1", shop));
                continue;
            }
            else if(FISH_FAIRY_SHOP_NAME.equals(shop)) {
                unassignedShopItemLocations.add(String.format("%s Item 3", shop));
                if (Settings.isAlternateMotherAnkh()) {
                    unassignedShopItemLocations.add(String.format("%s Item 2", shop));
                }
                continue;
            }

            originalShopContents = DataFromFile.getMapOfShopNameToShopOriginalContents().get(shop);
            for (int i = 0; i < 3; i++) {
                originalShopItem = originalShopContents.get(i);
                if(!originalShopItem.equals("Weights") && !originalShopItem.endsWith("Ammo")) {
                    unassignedShopItemLocations.add(String.format("%s Item %d", shop, i + 1));
                }
            }
        }

        shopsWithTransformations = new ArrayList<>();
        shopsWithTransformations.add(NON_MSX_SHOP_NAME);
        shopsWithTransformations.add(MSX_SHOP_NAME);
    }

    @Override
    public ShopRandomizer copy() {
        CategorizedShopRandomizer other = new CategorizedShopRandomizer();
        other.randomizedShops = new ArrayList<>(this.randomizedShops);
        other.unassignedShopItemLocations = new ArrayList<>(this.unassignedShopItemLocations);
        other.shopsWithTransformations = new ArrayList<>(this.shopsWithTransformations);
        other.mapOfShopInventoryItemToContents = new HashMap<>(this.mapOfShopInventoryItemToContents);
        return other;
    }

    @Override
    public void placeNonRandomizedItems() {
        List<String> originalShopContents;
        String originalShopItem;
        String shopItemLocation;

        List<String> nonRandomizedItems = DataFromFile.getNonRandomizedItems();
        for(String shop : randomizedShops) {
            originalShopContents = DataFromFile.getMapOfShopNameToShopOriginalContents().get(shop);
            for (int i = 0; i < 3; i++) {
                shopItemLocation = String.format("%s Item %d", shop, i + 1);
                originalShopItem = originalShopContents.get(i);
                if(originalShopItem.equals("Weights") || originalShopItem.endsWith("Ammo")) {
                    // todo: remove/change this once we randomize weights/ammo
                    mapOfShopInventoryItemToContents.put(shopItemLocation, originalShopItem);
                }
                else if(MSX_SHOP_NAME.equals(shop) && "Mobile Super X2".equals(originalShopItem) && nonRandomizedItems.contains(originalShopItem)) {
                    mapOfShopInventoryItemToContents.put(shopItemLocation, originalShopItem);
                    unassignedShopItemLocations.remove(shopItemLocation);
                    itemRandomizer.removeItemFromUnplacedItems(originalShopItem);
                }
                else if(FISH_FAIRY_SHOP_NAME.equals(shop) && !"Shell Horn".equals(originalShopItem) && !"guild.exe".equals(originalShopItem) && nonRandomizedItems.contains(originalShopItem)) {
                    mapOfShopInventoryItemToContents.put(shopItemLocation, originalShopItem);
                    unassignedShopItemLocations.remove(shopItemLocation);
                    itemRandomizer.removeItemFromUnplacedItems(originalShopItem);
                }
                else if(nonRandomizedItems.contains(originalShopItem)){
                    mapOfShopInventoryItemToContents.put(shopItemLocation, originalShopItem);
                    unassignedShopItemLocations.remove(shopItemLocation);
                    itemRandomizer.removeItemFromUnplacedItems(originalShopItem);
                }
            }
        }
        if(LocationCoordinateMapper.isSurfaceStart()) {
            int i = 1;
            for(String shopContents : DataFromFile.getMapOfShopNameToShopOriginalContents().get(DataFromFile.CUSTOM_SHOP_NAME)) {
                shopItemLocation = String.format("%s Item %d", DataFromFile.CUSTOM_SHOP_NAME, i++);
                mapOfShopInventoryItemToContents.put(shopItemLocation, shopContents);
                unassignedShopItemLocations.remove(shopItemLocation);
            }
        }

        for(CustomItemPlacement customItemPlacement : DataFromFile.getCustomPlacementData().getCustomItemPlacements()) {
            String customLocation = customItemPlacement.getLocation();
            if(customLocation != null && customLocation.startsWith("Shop ")) {
                if(!customLocation.startsWith(DataFromFile.CUSTOM_SHOP_NAME) || !LocationCoordinateMapper.isSurfaceStart()) {
                    mapOfShopInventoryItemToContents.put(customLocation, customItemPlacement.getContents());
                    unassignedShopItemLocations.remove(customLocation);
                    itemRandomizer.removeItemFromUnplacedItems(customItemPlacement.getContents());
                }
            }
        }
    }

    @Override
    public void placeSpecialSubweaponAmmo(Random random) {
        mapOfShopInventoryItemToContents.put("Shop 1 (Surface) Item 3", Settings.getCurrentStartingWeapon() + " Ammo");
    }

    @Override
    public List<String> getPlacedShopItems() {
        List<String> placedItems = new ArrayList<>();
        for(Map.Entry<String, String> locationAndItem : mapOfShopInventoryItemToContents.entrySet()) {
            if(locationAndItem.getKey().contains(FISH_FAIRY_SHOP_NAME)) {
                if(locationAndItem.getKey().endsWith("3")) {
                    placedItems.add(locationAndItem.getValue());
                }
            }
            else if(locationAndItem.getKey().contains(MSX_SHOP_NAME)) {
                if(locationAndItem.getKey().endsWith("1")) {
                    placedItems.add(locationAndItem.getValue());
                }
            }
            else {
                placedItems.add(locationAndItem.getValue());
            }
        }
        return placedItems;
    }

    @Override
    public void determineItemTypes(Random random) {
        // Item types remain the same with this mode of randomization.
    }

    @Override
    public void updateFiles(List<Block> blocks, boolean subweaponOnly, MoneyChecker moneyChecker, Random random) {
        String shopItem1;
        String shopItem2;
        String shopItem3;
        ShopBlock shopBlock;
        for(String shopName : randomizedShops) {
            shopBlock = (ShopBlock) blocks.get(DataFromFile.getMapOfShopNameToShopBlock().get(shopName));

            if(MSX_SHOP_NAME.equals(shopName)) {
                shopItem1 = Settings.getUpdatedContents(mapOfShopInventoryItemToContents.get(String.format("%s Item 1", shopName)));
                shopItem2 = Settings.getUpdatedContents(mapOfShopInventoryItemToContents.get(String.format("%s Item 2", NON_MSX_SHOP_NAME)));
                shopItem3 = Settings.getUpdatedContents(mapOfShopInventoryItemToContents.get(String.format("%s Item 3", NON_MSX_SHOP_NAME)));

                // No need to worry about flag replacement because MSX2 can't be a removed item.
                Map<String, GameObjectId> nameToDataMap = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData();
                GameObjectId itemNewContentsData = nameToDataMap.get(shopItem1);
                GameDataTracker.writeLocationContents("Mobile Super X2", shopItem1,
                        nameToDataMap.get("Mobile Super X2"), itemNewContentsData, itemNewContentsData.getWorldFlag(), random, false);
            }
            else if(FISH_FAIRY_SHOP_NAME.equals(shopName)) {
                shopItem1 = "Shell Horn";
                if (Settings.isAlternateMotherAnkh()) {
                    shopItem2 = Settings.getUpdatedContents(mapOfShopInventoryItemToContents.get(String.format("%s Item 2", shopName)));
                }
                else {
                    shopItem2 = "guild.exe";
                }
                shopItem3 = Settings.getUpdatedContents(mapOfShopInventoryItemToContents.get(String.format("%s Item 3", shopName)));
            }
            else {
                shopItem1 = Settings.getUpdatedContents(mapOfShopInventoryItemToContents.get(String.format("%s Item 1", shopName)));
                shopItem2 = Settings.getUpdatedContents(mapOfShopInventoryItemToContents.get(String.format("%s Item 2", shopName)));
                shopItem3 = Settings.getUpdatedContents(mapOfShopInventoryItemToContents.get(String.format("%s Item 3", shopName)));
            }
            if(Settings.getCurrentRemovedItems().contains(shopItem1)
                    || Settings.getRemovedItems().contains(shopItem1)
                    || Settings.getStartingItemsIncludingCustom().contains(shopItem1)
                    || (Settings.isReplaceMapsWithWeights() && shopItem1.startsWith("Map (") && !"Map (Shrine of the Mother)".equals(shopItem1))) {
                shopItem1 = "Weights";
            }
            if(Settings.getCurrentRemovedItems().contains(shopItem2)
                    || Settings.getRemovedItems().contains(shopItem2)
                    || Settings.getStartingItemsIncludingCustom().contains(shopItem2)
                    || (Settings.isReplaceMapsWithWeights() && shopItem2.startsWith("Map (") && !"Map (Shrine of the Mother)".equals(shopItem2))) {
                shopItem2 = "Weights";
            }
            if(Settings.getCurrentRemovedItems().contains(shopItem3)
                    || Settings.getRemovedItems().contains(shopItem3)
                    || Settings.getStartingItemsIncludingCustom().contains(shopItem3)
                    || (Settings.isReplaceMapsWithWeights() && shopItem3.startsWith("Map (") && !"Map (Shrine of the Mother)".equals(shopItem3))) {
                shopItem3 = "Weights";
            }
            GameDataTracker.writeShopInventory(shopBlock, shopItem1, shopItem2, shopItem3, blocks,
                    getItemPriceCount(subweaponOnly, shopItem1, String.format("%s Item 1", shopName), moneyChecker, random),
                    getItemPriceCount(subweaponOnly, shopItem2, String.format("%s Item 2", shopName), moneyChecker, random),
                    getItemPriceCount(subweaponOnly, shopItem3, String.format("%s Item 3", shopName), moneyChecker, random),
                    shopName, false, npcRandomizer, random);
        }
    }

    private ItemPriceCount getItemPriceCount(boolean subweaponOnly, String itemName, String shopInventoryLocation, MoneyChecker moneyChecker, Random random) {
        String startingWeapon = Settings.getCurrentStartingWeapon();
        if((subweaponOnly && itemName.endsWith(" Ammo")) || itemName.equals(startingWeapon + " Ammo")) {
            if("Shuriken".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)150);
            }
            if("Rolling Shuriken".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)100);
            }
            if("Earth Spear".equals(startingWeapon)
                    || "Flare Gun".equals(startingWeapon)
                    || "Caltrops".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)80);
            }
            if("Bomb".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)30);
            }
            if("Chakram".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)10);
            }
            if("Pistol".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)3);
            }
        }

        Integer shopPrice = moneyChecker.getShopPrice(itemName, shopInventoryLocation.replaceAll(" Item \\d", ""));
        if(shopPrice != null) {
            Integer defaultShopPrice = null;
            if("Shop 1 (Surface) Item 1".equals(shopInventoryLocation)) {
                defaultShopPrice = 10;
            }
            else if("Shop 2 (Surface) Item 2".equals(shopInventoryLocation)) {
                defaultShopPrice = 50;
            }
            else if("Shop 2 (Surface) Item 3".equals(shopInventoryLocation)) {
                defaultShopPrice = 20;
            }
            else if("Shop 3 (Surface) Item 1".equals(shopInventoryLocation)) {
                defaultShopPrice = 10;
            }
            else if("Shop 3 (Surface) Item 2".equals(shopInventoryLocation)) {
                defaultShopPrice = 50;
            }
            else if("Shop 3 (Surface) Item 3".equals(shopInventoryLocation)) {
                defaultShopPrice = 100;
            }
            else if("Shop 6 (Mausoleum) Item 1".equals(shopInventoryLocation)) {
                defaultShopPrice = 60;
            }
            else if("Shop 7 (Graveyard) Item 1".equals(shopInventoryLocation)) {
                defaultShopPrice = 60;
            }
            if(defaultShopPrice != null) {
                return new ItemPriceCount((short)(int)(defaultShopPrice < shopPrice ? defaultShopPrice : shopPrice - 5 * random.nextInt(2)), null);
            }
            return new ItemPriceCount((short)(int)(shopPrice - 5 * random.nextInt(2)), null);
        }
        return null;
    }
}
