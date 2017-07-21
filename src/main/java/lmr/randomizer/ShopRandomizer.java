package lmr.randomizer;

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
    private List<String> unassignedShopLocations = new ArrayList<>(); // Shop locations which still need something placed.
    private List<String> unassignedSubweapons = new ArrayList<>(ItemRandomizer.ALL_SUBWEAPONS);

    private AccessChecker accessChecker;

    private int totalUniqueShopItems; // Unique items that can be purchased in shops. In order to fill all chests, only a limited amount can be placed.

    public ShopRandomizer(int totalUniqueShopItems) {
        this.totalUniqueShopItems = totalUniqueShopItems;
        allShops = new ArrayList<>(DataFromFile.getAllShops());

        if(!allShops.contains(MSX_SHOP_NAME)) {
            allShops.add(MSX_SHOP_NAME);
        }

        for(String shop : allShops) {
            if(SHIELD_SHOP_NAME.equals(shop)) {
                unassignedShopLocations.add(String.format("%s Item %d", shop, 1));
                unassignedShopLocations.add(String.format("%s Item %d", shop, 3));
            }
            else if(FISH_SHOP_NAME.equals(shop)) {
                unassignedShopLocations.add(String.format("%s Item %d", shop, 3));
            }
            else {
                for (int i = 1; i <= 3; i++) {
                    unassignedShopLocations.add(String.format("%s Item %d", shop, i));
                }
            }
        }
    }

    public List<String> getUnassignedShopItemLocations() {
        return unassignedShopLocations;
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
            return true;
        }
        return false;
    }

    public boolean placeItem(String item, int locationIndex) {
        String location = unassignedShopLocations.get(locationIndex);
        if(accessChecker.validRequirements(item, location)) {
            mapOfShopInventoryItemToContents.put(location, item);
            unassignedShopLocations.remove(location);
            return true;
        }
        return false;
    }

    public List<String> getInitialUnassignedShopItemLocations() {
        List<String> initialUnassigned = new ArrayList<>();
        List<String> initialShops = FileUtils.getList("initial/initial_shops.txt");
        for(String unassigned : unassignedShopLocations) {
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
            unassignedShopLocations.remove(firstAmmoLocation);
        }
        assignWeights(random);
        assignSubweaponAmmoLocations(random);
    }

    public void placeSurfaceWeights() {
        String surfaceWeightsLocation = String.format("%s Item %d", MSX_SHOP_NAME, 1);
        mapOfShopInventoryItemToContents.put(surfaceWeightsLocation, "Weights");
        unassignedShopLocations.remove(surfaceWeightsLocation);
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
                if(unassignedShopLocations.contains(location)) {
                    mapOfShopInventoryItemToContents.put(location, "Weights");
                    unassignedShopLocations.remove(location);
                    break;
                }
            }
            shopsWithNoWeights.remove(shop);
        }
    }

    private int getWeightCount(Random random) {
        int maxAdditionalWeights = Math.min(allShops.size() - 1, unassignedShopLocations.size() - ItemRandomizer.ALL_SUBWEAPONS.size() - totalUniqueShopItems); // Must have enough room for all shop items plus one of each ammo type plus one weight. The remaining ammo to weights ratio can be random.
        if(maxAdditionalWeights < 0) {
            maxAdditionalWeights = 0;
        }
        return random.nextInt(maxAdditionalWeights);
    }

    // todo: current logic could theoretically generate a shop where all 3 items are shurikens
    private void assignSubweaponAmmoLocations(Random random) {
        int totalSubweaponLocations = unassignedShopLocations.size() - totalUniqueShopItems;

        String location;
        String subweapon;
        for(int i = 0; i < totalSubweaponLocations; i++) {
            location = unassignedShopLocations.get(random.nextInt(unassignedShopLocations.size()));
            if(unassignedSubweapons.isEmpty()) {
                subweapon = ItemRandomizer.ALL_SUBWEAPONS.get(random.nextInt(ItemRandomizer.ALL_SUBWEAPONS.size()));
            }
            else {
                subweapon = unassignedSubweapons.get(random.nextInt(unassignedSubweapons.size()));
            }
            mapOfShopInventoryItemToContents.put(location, subweapon + " Ammo");
            unassignedShopLocations.remove(location);
        }
    }

    public void outputLocations(long startingSeed, int attemptNumber) throws IOException {
//        BufferedWriter writer = FileUtils.getFileWriter(String.format("target/shops%s_%s.txt", startingSeed, attemptNumber));
        BufferedWriter writer = FileUtils.getFileWriter(String.format("target/shops%s.txt", startingSeed, attemptNumber));
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
}
