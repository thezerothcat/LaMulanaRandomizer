package lmr.randomizer;

import lmr.randomizer.update.GameObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thezerothcat on 7/20/2017.
 */
public final class DataFromFile {
    private static List<String> allShops;
    private static List<String> allItems;
    private static List<String> nonRandomizedItems;
    private static List<String> nonShopItemLocations;
    private static List<String> initialNonShopItemLocations;
    private static Map<String, GameObjectId> mapOfItemToUsefulIdentifyingRcdData;

    private DataFromFile() { }

    public static List<String> getAllShops() {
        if(allShops == null) {
            allShops = FileUtils.getList("all/all_shops.txt");
            if(allShops == null) {
                allShops = new ArrayList<>(0);
            }
        }
        return allShops;
    }

    public static List<String> getAllItems() {
        if(allItems == null) {
            if(Settings.randomizeShops) {
                allItems = FileUtils.getList("all/all_items.txt");
            }
            else {
                allItems = FileUtils.getList("all/non_shop_items.txt");
            }

            if(allItems == null) {
                allItems = new ArrayList<>(0);
            }
        }
        return allItems;
    }

    public static List<String> getNonShopItemLocations() {
        if(nonShopItemLocations == null) {
            nonShopItemLocations = FileUtils.getList("all/non_shop_items.txt");
            if(nonShopItemLocations == null) {
                nonShopItemLocations = new ArrayList<>(0);
            }
        }
        return nonShopItemLocations;
    }

    public static List<String> getNonRandomizedItems() {
        if(nonRandomizedItems == null) {
            nonRandomizedItems = FileUtils.getList("min/non_randomized_items.txt");
            if(nonRandomizedItems == null) {
                nonRandomizedItems = new ArrayList<>(0);
            }
        }
        return nonRandomizedItems;
    }

    public static List<String> getInitialNonShopItemLocations() {
        if(initialNonShopItemLocations == null) {
            initialNonShopItemLocations = FileUtils.getList("initial/initial_chests.txt");
            if(initialNonShopItemLocations == null) {
                initialNonShopItemLocations = new ArrayList<>(0);
            }
            else {
                initialNonShopItemLocations.removeAll(getNonRandomizedItems());
            }
        }
        return initialNonShopItemLocations;
    }

    public static Map<String, GameObjectId> getMapOfItemToUsefulIdentifyingRcdData() {
        if(mapOfItemToUsefulIdentifyingRcdData == null) {
            mapOfItemToUsefulIdentifyingRcdData = FileUtils.getRcdDataIdMap("rcd/chest_args.txt");
            if(mapOfItemToUsefulIdentifyingRcdData == null) {
                mapOfItemToUsefulIdentifyingRcdData = new HashMap<>(0);
            }
        }
        return mapOfItemToUsefulIdentifyingRcdData;
    }
}
