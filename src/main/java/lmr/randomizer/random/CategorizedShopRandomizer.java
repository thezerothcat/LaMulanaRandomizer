package lmr.randomizer.random;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.shop.ShopBlock;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.update.GameDataTracker;
import lmr.randomizer.update.GameObjectId;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 8/2/2017.
 *
 * Currently only randomizes unique items. Should probably eventually randomize ammo options also.
 */
public class CategorizedShopRandomizer implements ShopRandomizer {
    private static final String MSX_SHOP_NAME = "Shop 2 Alt (Surface)";
    private static final String NON_MSX_SHOP_NAME = "Shop 2 (Surface)";
    private static final String FISH_FAIRY_SHOP_NAME = "Shop 12 Alt (Spring)";

    private AccessChecker accessChecker;
    private ItemRandomizer itemRandomizer; // Not needed for this version of the randomizer?

    private Map<String, String> mapOfShopInventoryItemToContents = new HashMap<>(); // The thing we're trying to build.
    private List<String> unassignedShopItemLocations = new ArrayList<>(); // Shop locations which still need something placed.
    private List<String> randomizedShops;
    private List<String> unassignedSubweapons = new ArrayList<>(ItemRandomizer.ALL_SUBWEAPONS);

    public CategorizedShopRandomizer() {
        randomizedShops = new ArrayList<>(DataFromFile.getAllShops());
        randomizedShops.removeAll(DataFromFile.getNonRandomizedShops());

        List<String> originalShopContents;
        String originalShopItem;
        for(String shop : DataFromFile.getAllShops()) {
            if(MSX_SHOP_NAME.equals(shop)) {
                unassignedShopItemLocations.add(String.format("%s Item 1", shop));
                continue;
            }
            else if(FISH_FAIRY_SHOP_NAME.equals(shop)) {
                unassignedShopItemLocations.add(String.format("%s Item 3", shop));
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
    }

    public List<String> getUnassignedShopItemLocations() {
        return unassignedShopItemLocations;
    }

    public void placeNonRandomizedItems() {
        List<String> originalShopContents;
        String originalShopItem;
        String shopItemLocation;

        for(String shop : DataFromFile.getNonRandomizedShops()) {
            originalShopContents = DataFromFile.getMapOfShopNameToShopOriginalContents().get(shop);
            for (int i = 0; i < 3; i++) {
                shopItemLocation = String.format("%s Item %d", shop, i + 1);
                originalShopItem = originalShopContents.get(i);
                mapOfShopInventoryItemToContents.put(shopItemLocation, originalShopItem);
                unassignedShopItemLocations.remove(shopItemLocation);
                if((MSX_SHOP_NAME.equals(shop) && "Mobile Super X2".equals(originalShopItem))) {
                    itemRandomizer.removeItemFromUnplacedItems(originalShopItem);
                }
                else if(FISH_FAIRY_SHOP_NAME.equals(shop) && !"Shell Horn".equals(originalShopItem) && !"guild.exe".equals(originalShopItem)) {
                    itemRandomizer.removeItemFromUnplacedItems(originalShopItem);
                }
                else if(!"Weights".equals(originalShopItem) && !originalShopItem.endsWith("Ammo")){
                    itemRandomizer.removeItemFromUnplacedItems(originalShopItem);
                }
            }
        }

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
//        mapOfShopInventoryItemToContents.put("Shop 3 (Surface) Item 1", "Sacred Orb (Gate of Guidance)");
//        unassignedShopItemLocations.remove("Shop 3 (Surface) Item 1");
//        itemRandomizer.removeItemFromUnplacedItems("Sacred Orb (Gate of Guidance)");
//        mapOfShopInventoryItemToContents.put("Shop 2 Alt (Surface) Item 1", "Diary");
//        unassignedShopItemLocations.remove("Shop 2 Alt (Surface) Item 1");
//        itemRandomizer.removeItemFromUnplacedItems("Diary");
//        mapOfShopInventoryItemToContents.put("Shop 18 (Lil Bro) Item 1", "Fruit of Eden");
//        unassignedShopItemLocations.remove("Shop 18 (Lil Bro) Item 1");
//        itemRandomizer.removeItemFromUnplacedItems("Fruit of Eden");
    }

    public List<String> getShopItems(String shopName) {
        List<String> shopItems = new ArrayList<>();
        String shopItem;
        for (int i = 1; i <= 3; i++) {
            shopItem = mapOfShopInventoryItemToContents.get(String.format("%s Item %d", shopName, i));
            if (shopItem != null && !"Weights".equals(shopItem) && !shopItem.contains("Ammo")) {
                shopItems.add(shopItem);
            }
        }
        return shopItems;
    }

    public boolean placeRequiredItem(String item, List<String> shopLocationOptions, int locationIndex) {
        String location = shopLocationOptions.get(locationIndex);
        if(accessChecker.validRequirements(item, location)) {
            mapOfShopInventoryItemToContents.put(location, item);
            shopLocationOptions.remove(location);
            unassignedShopItemLocations.remove(location);
            return true;
        }
        return false;
    }

    public boolean placeItem(String item, int locationIndex) {
        String location = unassignedShopItemLocations.get(locationIndex);
        if(accessChecker.validRequirements(item, location)) {
            mapOfShopInventoryItemToContents.put(location, item);
            unassignedShopItemLocations.remove(location);
            return true;
        }
        return false;
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

    public void determineItemTypes(Random random, String firstSubweapon) {
        // Item types remain the same with this mode of randomization.
    }

    @Override
    public void randomizeForbiddenTreasure(String uselessMap, boolean placeForbiddenTreasure) {
        if(placeForbiddenTreasure) {
            String uselessMapLocation = null;
            for(Map.Entry<String, String> shopItemLocationAndContents : mapOfShopInventoryItemToContents.entrySet()) {
                if(uselessMap.equals(shopItemLocationAndContents.getValue())) {
                    uselessMapLocation = shopItemLocationAndContents.getKey();
                }
            }
            mapOfShopInventoryItemToContents.put(uselessMapLocation, "Forbidden Treasure");
        }
        mapOfShopInventoryItemToContents.put("Shop 12 Alt (Spring) Item 2", uselessMap);
    }

    public void outputLocations(int attemptNumber) throws IOException {
        BufferedWriter writer = FileUtils.getFileWriter(String.format("%d/shops.txt", Settings.getStartingSeed()));
        if (writer == null) {
            return;
        }


        String location;
        for(String shop : randomizedShops) {
            for (int i = 1; i <= 3; i++) {
                location = String.format("%s Item %d", shop, i);
                if(mapOfShopInventoryItemToContents.containsKey(location)) {
                    writer.write(location + " => " + mapOfShopInventoryItemToContents.get(location));
                    writer.newLine();
                }
                else {
                    writer.write(location + " => (unchanged)");
                    writer.newLine();
                }
            }
        }

        writer.flush();
        writer.close();
    }

    public void updateFiles(List<Block> blocks, Random random) {
        String shopItem1;
        String shopItem2;
        String shopItem3;
        ShopBlock shopBlock;
        for(String shopName : randomizedShops) {
            shopBlock = (ShopBlock) blocks.get(DataFromFile.getMapOfShopNameToShopBlock().get(shopName));

            if(MSX_SHOP_NAME.equals(shopName)) {
                shopItem1 = mapOfShopInventoryItemToContents.get(String.format("%s Item 1", shopName));
                shopItem2 = mapOfShopInventoryItemToContents.get(String.format("%s Item 2", NON_MSX_SHOP_NAME));
                shopItem3 = mapOfShopInventoryItemToContents.get(String.format("%s Item 3", NON_MSX_SHOP_NAME));

                GameDataTracker.writeLocationContents("Mobile Super X2", shopItem1);
            }
            else if(FISH_FAIRY_SHOP_NAME.equals(shopName)) {
                shopItem1 = "Shell Horn";
                shopItem2 = "guild.exe";
                shopItem3 = mapOfShopInventoryItemToContents.get(String.format("%s Item 3", shopName));
            }
            else {
                shopItem1 = mapOfShopInventoryItemToContents.get(String.format("%s Item 1", shopName));
                shopItem2 = mapOfShopInventoryItemToContents.get(String.format("%s Item 2", shopName));
                shopItem3 = mapOfShopInventoryItemToContents.get(String.format("%s Item 3", shopName));
            }
            GameDataTracker.writeShopInventory(shopBlock, shopItem1, shopItem2, shopItem3, null);
        }
    }

    public void setAccessChecker(AccessChecker accessChecker) {
        this.accessChecker = accessChecker;
    }

    public void setItemRandomizer(ItemRandomizer itemRandomizer) {
        this.itemRandomizer = itemRandomizer;
    }
}
