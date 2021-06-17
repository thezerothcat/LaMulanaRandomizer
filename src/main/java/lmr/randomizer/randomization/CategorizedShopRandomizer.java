package lmr.randomizer.randomization;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.Settings;
import lmr.randomizer.node.CustomItemPlacement;
import lmr.randomizer.node.MoneyChecker;
import lmr.randomizer.util.LocationCoordinateMapper;

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
                if(Settings.isAlternateMotherAnkh()) {
                    unassignedShopItemLocations.add(String.format("%s Item 2", shop));
                }
                continue;
            }

            originalShopContents = DataFromFile.getMapOfShopNameToShopOriginalContents().get(shop);
            for(int i = 0; i < 3; i++) {
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
            for(int i = 0; i < 3; i++) {
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
                else if(nonRandomizedItems.contains(originalShopItem)) {
                    mapOfShopInventoryItemToContents.put(shopItemLocation, originalShopItem);
                    unassignedShopItemLocations.remove(shopItemLocation);
                    itemRandomizer.removeItemFromUnplacedItems(originalShopItem);
                }
            }
        }

        mapOfShopInventoryItemToContents.put(FISH_FAIRY_SHOP_NAME + " Item 1", "Shell Horn");
        if(!Settings.isAlternateMotherAnkh()) {
            mapOfShopInventoryItemToContents.put(FISH_FAIRY_SHOP_NAME + " Item 2", "guild.exe");
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
    public void initShopItemPriceCountRandomizer(boolean subweaponOnly, MoneyChecker moneyChecker, Random random) {
        shopItemPriceCountRandomizer = new CategorizedShopItemPriceCountRandomizer(subweaponOnly, moneyChecker, random);
    }
}
