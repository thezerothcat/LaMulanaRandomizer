package lmr.randomizer.random;

import lmr.randomizer.dat.Block;
import lmr.randomizer.node.AccessChecker;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by thezerothcat on 8/17/2017.
 */
public interface ShopRandomizer {
    ShopRandomizer copy();

    List<String> getUnassignedShopItemLocations();

    void placeNonRandomizedItems();

    void placeSpecialSubweaponAmmo(Random random);

    List<String> getShopItems(String shopName);

    boolean placeRequiredItem(String item, List<String> shopLocationOptions, int locationIndex);

    boolean placeItem(String item, int locationIndex);

    boolean shopContainsSacredOrb(String shopName);

    List<String> getInitialUnassignedShopItemLocations();

    List<String> getPlacedShopItems();

    void determineItemTypes(Random random);

    void outputLocations(int attemptNumber) throws IOException;

    void updateFiles(List<Block> blocks, boolean subweaponOnly, Random random);

    void setAccessChecker(AccessChecker accessChecker);

    void setItemRandomizer(ItemRandomizer itemRandomizer);

    String findNameOfShopNodeContainingItem(String itemToLookFor);
}
