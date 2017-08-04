package lmr.randomizer.random;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.shop.ShopBlock;
import lmr.randomizer.update.GameDataTracker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 8/2/2017.
 *
 * Currently only randomizes unique items. Should probably eventually randomize ammo options also.
 */
public class CategorizedShopRandomizer extends ShopRandomizer {
    private static final String SHURIKEN_SHOP_NAME = "Shop 1 (Surface)";
    private static final String MSX_SHOP_NAME = "Shop 2 Alt (Surface)"; // todo: shop transformation needs to be handled

    private List<String> randomizedShops;
    private List<String> unassignedSubweapons = new ArrayList<>(ItemRandomizer.ALL_SUBWEAPONS);

    public CategorizedShopRandomizer() {
        super();
        randomizedShops = new ArrayList<>(DataFromFile.getAllShops());
        randomizedShops.removeAll(DataFromFile.getNonRandomizedShops());

        List<String> originalShopContents;
        String originalShopItem;
        for(String shop : DataFromFile.getAllShops()) {
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
                if(!MSX_SHOP_NAME.equals(shop) && !"Shell Horn".equals(originalShopItem) && !"guild.exe".equals(originalShopItem)) {
                    itemRandomizer.removeItemFromUnplacedItems(originalShopItem);
                }
            }
        }
        for(String shop : randomizedShops) { // todo: remove this once we randomize weights/ammo
            originalShopContents = DataFromFile.getMapOfShopNameToShopOriginalContents().get(shop);
            for (int i = 0; i < 3; i++) {
                shopItemLocation = String.format("%s Item %d", shop, i + 1);
                originalShopItem = originalShopContents.get(i);
                if(originalShopItem.equals("Weights") || originalShopItem.endsWith("Ammo")) {
                    mapOfShopInventoryItemToContents.put(shopItemLocation, originalShopItem);
                }
            }
        }
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

    public void placeSurfaceWeights() {
        // Do nothing, because we aren't randomizing weights in this randomizer.
    }

    public void outputLocations(int attemptNumber) throws IOException {
        BufferedWriter writer = FileUtils.getFileWriter(String.format("%d/shops.txt", Settings.startingSeed));
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

    public void updateFiles(List<Block> blocks) {
        String shopItem1;
        String shopItem2;
        String shopItem3;
        ShopBlock shopBlock;
        for(String shopName : randomizedShops) {
            shopBlock = (ShopBlock) blocks.get(DataFromFile.getMapOfShopNameToShopBlock().get(shopName));
            shopItem1 = mapOfShopInventoryItemToContents.get(String.format("%s Item 1", shopName));
            shopItem2 = mapOfShopInventoryItemToContents.get(String.format("%s Item 2", shopName));
            shopItem3 = mapOfShopInventoryItemToContents.get(String.format("%s Item 3", shopName));
            GameDataTracker.writeShopInventory(shopBlock, shopItem1, shopItem2, shopItem3);
        }
    }
}
