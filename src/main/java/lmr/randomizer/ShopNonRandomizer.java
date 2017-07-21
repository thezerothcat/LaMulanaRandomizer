package lmr.randomizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by thezerothcat on 7/20/2017.
 */
public class ShopNonRandomizer extends ShopRandomizer {
    public ShopNonRandomizer(int totalUniqueShopItems) {
        super(totalUniqueShopItems);
        mapOfShopInventoryItemToContents.put("Shop 1 (Surface) Item 1", "Hand Scanner");
        mapOfShopInventoryItemToContents.put("Shop 2 (Surface) Item 1", "reader.exe");
        mapOfShopInventoryItemToContents.put("Shop 2 (Surface) Item 2", "yagomap.exe");
        mapOfShopInventoryItemToContents.put("Shop 3 (Surface) Item 1", "Buckler");
        mapOfShopInventoryItemToContents.put("Shop 3 (Surface) Item 2", "Waterproof Case");
        mapOfShopInventoryItemToContents.put("Shop 3 (Surface) Item 3", "Pistol");
        mapOfShopInventoryItemToContents.put("Shop 4 (Guidance) Item 1", "guild.exe");
        mapOfShopInventoryItemToContents.put("Shop 5 (Illusion) Item 1", "move.exe");
        mapOfShopInventoryItemToContents.put("Shop 6 (Mausoleum) Item 1", "Hermes' Boots");
        mapOfShopInventoryItemToContents.put("Shop 7 (Graveyard) Item 1", "Fake Silver Shield");
        mapOfShopInventoryItemToContents.put("Shop 8 (Sun) Item 1", "bunemon.exe");
        mapOfShopInventoryItemToContents.put("Shop 9 (Sun) Item 1", "Heatproof Case");
        mapOfShopInventoryItemToContents.put("Shop 11 (Moonlight) Item 1", "Scriptures");
        mapOfShopInventoryItemToContents.put("Shop 12 Alt (Spring) Item 1", "Shell Horn");
        mapOfShopInventoryItemToContents.put("Shop 12 Alt (Spring) Item 2", "guild.exe");
        mapOfShopInventoryItemToContents.put("Shop 12 Alt (Spring) Item 3", "randc.exe");
        mapOfShopInventoryItemToContents.put("Shop 13 (Goddess) Item 1", "miracle.exe");
        mapOfShopInventoryItemToContents.put("Shop 14 (Inferno) Item 1", "capstar.exe");
        mapOfShopInventoryItemToContents.put("Shop 15 (Ruin) Item 1", "torude.exe");
        mapOfShopInventoryItemToContents.put("Shop 17 (Birth) Item 1", "Ankh Jewel (Chamber of Birth)");
        mapOfShopInventoryItemToContents.put("Shop 18 (Lil Bro) Item 1", "Helmet");
        mapOfShopInventoryItemToContents.put("Shop 19 (Big Bro) Item 1", "Dragon Bone");
        mapOfShopInventoryItemToContents.put("Shop 20 (Twin Labs) Item 1", "Bracelet");
        mapOfShopInventoryItemToContents.put("Shop 21 (Unsolvable) Item 1", "Lamp of Time");
    }

    @Override
    public List<String> getInitialUnassignedShopItemLocations() {
        return new ArrayList<>(0);
    }

    @Override
    public List<String> getUnassignedShopItemLocations() {
        return new ArrayList<>(0);
    }

    @Override
    public void determineItemTypes(Random random, String firstSubweapon) {
        return; // Don't randomize
    }


    @Override
    public void outputLocations(long startingSeed, int attemptNumber) throws IOException {
        return; // Don't output anything since we didn't randomize anything
    }
}
