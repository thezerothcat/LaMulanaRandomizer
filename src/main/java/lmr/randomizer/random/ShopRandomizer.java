package lmr.randomizer.random;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.Block;
import lmr.randomizer.node.AccessChecker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 7/9/2017.
 */
public class ShopRandomizer {
    private static final String SHURIKEN_SHOP_NAME = "Shop 1 (Surface)";
    private static final String MSX_SHOP_NAME = "Shop 2 (Surface)"; // todo: shop transformation needs to be handled
    private static final String SHIELD_SHOP_NAME = "Shop 7 (Graveyard)";
    private static final String FISH_SHOP_NAME = "Shop 12 Alt (Spring)";

    protected Map<String, String> mapOfShopInventoryItemToContents = new HashMap<>(); // The thing we're trying to build.
    private List<String> allShops;
    protected List<String> unassignedShopItemLocations = new ArrayList<>(); // Shop locations which still need something placed.
    private List<String> unassignedSubweapons = new ArrayList<>(ItemRandomizer.ALL_SUBWEAPONS);

    protected AccessChecker accessChecker;

    private int totalUniqueShopItems; // Unique items that can be purchased in shops. In order to fill all chests, only a limited amount can be placed.
    protected ItemRandomizer itemRandomizer; // Not needed for this version of the randomizer?

    public ShopRandomizer() { }

    public ShopRandomizer(int totalUniqueShopItems) {
        this.totalUniqueShopItems = totalUniqueShopItems;
        allShops = new ArrayList<>(DataFromFile.getAllShops());

        if(!allShops.contains(MSX_SHOP_NAME)) {
            allShops.add(MSX_SHOP_NAME);
        }

        for(String shop : allShops) {
            if(SHIELD_SHOP_NAME.equals(shop)) {
                unassignedShopItemLocations.add(String.format("%s Item %d", shop, 1));
                unassignedShopItemLocations.add(String.format("%s Item %d", shop, 3));
            }
            else if(FISH_SHOP_NAME.equals(shop)) {
                unassignedShopItemLocations.add(String.format("%s Item %d", shop, 3));
            }
            else {
                for (int i = 1; i <= 3; i++) {
                    unassignedShopItemLocations.add(String.format("%s Item %d", shop, i));
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
                originalShopItem = originalShopContents.get(i);
                shopItemLocation = String.format("%s Item %d", shop, i + 1);
                mapOfShopInventoryItemToContents.put(shopItemLocation, originalShopItem);
                unassignedShopItemLocations.remove(shopItemLocation);
                if(!"Shell Horn".equals(originalShopItem) && !"guild.exe".equals(originalShopItem)) {
                    itemRandomizer.removeItemFromUnplacedItems(originalShopItem);
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
        List<String> initialShops = FileUtils.getList("initial/initial_shops.txt");
        initialShops.removeAll(DataFromFile.getNonRandomizedShops());
        for(String unassigned : unassignedShopItemLocations) {
            for(String shopName : initialShops) {
                if(unassigned.contains(shopName)) {
                    initialUnassigned.add(unassigned);
                }
            }
        }
        return initialUnassigned;
    }

    public void determineItemTypes(Random random, String firstSubweapon) {
        placeSurfaceWeights();
        if(firstSubweapon != null) {
            String firstAmmoLocation = String.format("%s Item %d", SHURIKEN_SHOP_NAME, 3);
            mapOfShopInventoryItemToContents.put(firstAmmoLocation, firstSubweapon + " Ammo");
            unassignedSubweapons.remove(firstSubweapon);
            unassignedShopItemLocations.remove(firstAmmoLocation);
        }
        assignWeights(random);
        assignSubweaponAmmoLocations(random);
    }

    public void placeSurfaceWeights() {
        String surfaceWeightsLocation = String.format("%s Item %d", MSX_SHOP_NAME, 1);
        mapOfShopInventoryItemToContents.put(surfaceWeightsLocation, "Weights");
        unassignedShopItemLocations.remove(surfaceWeightsLocation);
    }

    private void assignWeights(Random random) {
        List<String> shopsWithNoWeights = new ArrayList<>(allShops);
        shopsWithNoWeights.remove(MSX_SHOP_NAME);

        String shop;
        String location;
        List<Integer> shopItems;
        int shopItemIndex;
        int maxAdditionalWeights = getWeightCount(random);
        for(int i = 0; i < maxAdditionalWeights; i++) {
            shop = shopsWithNoWeights.get(random.nextInt(shopsWithNoWeights.size()));
            shopItems = Arrays.asList(1, 2, 3);
            while(!shopItems.isEmpty()) {
                shopItemIndex = random.nextInt(shopItems.size());
                location = String.format("%s Item %d", shop, shopItems.get(shopItemIndex));
                if(unassignedShopItemLocations.contains(location)) {
                    mapOfShopInventoryItemToContents.put(location, "Weights");
                    unassignedShopItemLocations.remove(location);
                    break;
                }
            }
            shopsWithNoWeights.remove(shop);
        }
    }

    private int getWeightCount(Random random) {
        int maxAdditionalWeights = Math.min(allShops.size() - 1, unassignedShopItemLocations.size() - ItemRandomizer.ALL_SUBWEAPONS.size() - totalUniqueShopItems); // Must have enough room for all shop items plus one of each ammo type plus one weight. The remaining ammo to weights ratio can be random.
        if(maxAdditionalWeights < 0) {
            maxAdditionalWeights = 0;
        }
        return random.nextInt(maxAdditionalWeights);
    }

    // todo: current logic could theoretically generate a shop where all 3 items are shurikens
    private void assignSubweaponAmmoLocations(Random random) {
        int totalSubweaponLocations = unassignedShopItemLocations.size() - totalUniqueShopItems;

        String location;
        String subweapon;
        for(int i = 0; i < totalSubweaponLocations; i++) {
            location = unassignedShopItemLocations.get(random.nextInt(unassignedShopItemLocations.size()));
            if(unassignedSubweapons.isEmpty()) {
                subweapon = ItemRandomizer.ALL_SUBWEAPONS.get(random.nextInt(ItemRandomizer.ALL_SUBWEAPONS.size()));
            }
            else {
                subweapon = unassignedSubweapons.get(random.nextInt(unassignedSubweapons.size()));
            }
            mapOfShopInventoryItemToContents.put(location, subweapon + " Ammo");
            unassignedShopItemLocations.remove(location);
        }
    }

    public void outputLocations(int attemptNumber) throws IOException {
//        BufferedWriter writer = FileUtils.getFileWriter(String.format("target/shops%s_%s.txt", startingSeed, attemptNumber));
        BufferedWriter writer = FileUtils.getFileWriter(String.format("%d/shops.txt", Settings.startingSeed));
        if (writer == null) {
            return;
        }


        String location;
        for(String shop : allShops) {
            for (int i = 1; i <= 3; i++) {
                location = String.format("%s Item %d", shop, i);
                if(mapOfShopInventoryItemToContents.containsKey(location)) {
                    writer.write(location + " => " + mapOfShopInventoryItemToContents.get(location));
                    writer.newLine();
                }
                else {
                    writer.write(location + " => ???");
                    writer.newLine();
                }
            }
        }

        writer.flush();
        writer.close();
    }

    public void setAccessChecker(AccessChecker accessChecker) {
        this.accessChecker = accessChecker;
    }

    public void updateFiles(List<Block> blocks, Random random) {
//        List<String> locationsRelatedToBlocks = Arrays.asList("Map (Surface)", "mekuri.exe", "Mini Doll"); // todo: not hardcode this, eventually
//
//        for(String shopName : allShops) {
//            if(!DataFromFile.getNonRandomizedShops().contains(shopName)) {
//                String shopItem1 = mapOfShopInventoryItemToContents.get(String.format("%s Item 1", shopName));
//                String shopItem2 = mapOfShopInventoryItemToContents.get(String.format("%s Item 2", shopName));
//                String shopItem3 = mapOfShopInventoryItemToContents.get(String.format("%s Item 3", shopName));
//                GameDataTracker.writeShopInventory(shopName, shopItem1, shopItem2, shopItem3);
//            }
//        }
    }

    public void setItemRandomizer(ItemRandomizer itemRandomizer) {
        this.itemRandomizer = itemRandomizer;
    }
}
