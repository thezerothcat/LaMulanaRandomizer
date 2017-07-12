package lmr.randomizer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 7/10/2017.
 */
public class ItemRandomizer {
    public static final List<String> ALL_SUBWEAPONS = Arrays.asList("Shuriken", "Caltrops", "Rolling Shuriken", "Bomb", "Flare Gun", "Chakram", "Earth Spear", "Pistol");

    private Map<String, String> mapOfItemLocationToItem; // The map we're trying to build.

    private List<String> allItems; // All possible items.
    private List<String> unplacedItems; // Items that haven't been placed yet.

    private List<String> nonShopItemLocations;
    private int totalShopItems;


    public ItemRandomizer() {
        allItems = FileUtils.getList("all/all_items.txt");
        unplacedItems = new ArrayList<>(allItems);

        nonShopItemLocations = FileUtils.getList("all/non_shop_items.txt");
        totalShopItems = allItems.size() - nonShopItemLocations.size();
    }

    public int getTotalShopItems() {
        return totalShopItems;
    }

    public void outputLocations(BufferedWriter writer) throws IOException {
        // todo: implement properly?
        for (String location : mapOfItemLocationToItem.keySet()) {
            writer.write(location + " => " + mapOfItemLocationToItem.get(location));
            writer.newLine();
        }
    }
}
