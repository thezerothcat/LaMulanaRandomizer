package lmr.randomizer.random;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.Block;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.node.MoneyChecker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 8/17/2017.
 */
public abstract class ShopRandomizer {
    public static final String MSX_SHOP_NAME = "Shop 2 Alt (Surface)";
    public static final String NON_MSX_SHOP_NAME = "Shop 2 (Surface)";
    public static final String FISH_FAIRY_SHOP_NAME = "Shop 12 Alt (Spring)";

    public static final String LITTLE_BROTHER_SHOP_NAME = "Shop 18 (Lil Bro)";

    protected static final String GRAVEYARD_SHOP_NAME = "Shop 7 (Graveyard)";

    protected AccessChecker accessChecker;
    protected ItemRandomizer itemRandomizer; // Not needed for this version of the randomizer?
    protected NpcRandomizer npcRandomizer;

    protected Map<String, String> mapOfShopInventoryItemToContents = new HashMap<>(); // The thing we're trying to build.
    protected List<String> unassignedShopItemLocations = new ArrayList<>(); // Shop locations which still need something placed.
    protected List<String> randomizedShops;

    protected List<String> shopsWithTransformations;

    public abstract ShopRandomizer copy();

    public List<String> getUnassignedShopItemLocations() {
        return unassignedShopItemLocations;
    }

    public abstract void placeNonRandomizedItems();

    public abstract void placeSpecialSubweaponAmmo(Random random);

    public List<String> getShopItems(String shopName) {
        List<String> shopItems = new ArrayList<>();
        String shopItem;
        for (int i = 1; i <= 3; i++) {
            shopItem = mapOfShopInventoryItemToContents.get(String.format("%s Item %d", shopName, i));
            if (shopItem != null && !"Weights".equals(shopItem)) {
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
            if(item.contains("Sacred Orb")) {
                shopsWithTransformations.add(location.substring(0, location.indexOf(")") + 1));
            }
            return true;
        }
        return false;
    }

    public boolean shopHasTransformation(String shopName) {
        return shopsWithTransformations.contains(shopName);
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

    public abstract List<String> getPlacedShopItems();

    public abstract void determineItemTypes(Random random);

    public void outputLocations(int attemptNumber) throws IOException {
        BufferedWriter writer = FileUtils.getFileWriter(String.format("%d/shops.txt", Settings.getStartingSeed()));
        if (writer == null) {
            return;
        }

        String location;
        boolean addNewline = false;
        for(String shop : randomizedShops) {
            if(addNewline) {
                writer.newLine();
            }
            else if(Settings.isRandomizeNpcs()) {
                addNewline = true;
            }

            if(Settings.isRandomizeNpcs()) {
                if(DataFromFile.CUSTOM_SHOP_NAME.equals(shop)) {
                    writer.write(Translations.getText("shops.Shop0Default") + ":");
                    writer.newLine();
                }
                else {
                    String npcLocationKey = npcRandomizer.getShopNpcLocation(shop);
                    if(npcLocationKey != null) {
                        npcLocationKey = npcLocationKey.replaceAll("NPCL: ", "").replaceAll("[ )('-.]", "");
                        writer.write(Translations.getText("npc." + npcLocationKey).replaceAll("：", ":")
                                + Translations.getText("npcl." + npcLocationKey).replaceAll("：", ":"));
                        writer.newLine();
                    }
                }
            }
            for (int i = 1; i <= 3; i++) {
                location = String.format("%s Item %d", shop, i);
                if(mapOfShopInventoryItemToContents.containsKey(location)) {
                    String itemName = Settings.getUpdatedContents(mapOfShopInventoryItemToContents.get(location));
                    boolean removedItem = Settings.getCurrentRemovedItems().contains(itemName)
                            || Settings.getRemovedItems().contains(itemName)
                            || Settings.getStartingItemsIncludingCustom().contains(itemName)
                            || (Settings.isReplaceMapsWithWeights() && itemName.startsWith("Map (") && !"Map (Shrine of the Mother)".equals(itemName));
                    writer.write(Translations.getShopItemText(shop, i) + ": " + Translations.getItemText(itemName, removedItem));
                    writer.newLine();
                }
                else {
                    writer.write(Translations.getShopItemText(shop, i) + ": (unchanged)");
                    writer.newLine();
                }
            }

        }

        writer.flush();
        writer.close();
    }

    public abstract void updateFiles(List<Block> blocks, boolean subweaponOnly, MoneyChecker moneyChecker, Random random);

    public void setAccessChecker(AccessChecker accessChecker) {
        this.accessChecker = accessChecker;
    }

    public void setItemRandomizer(ItemRandomizer itemRandomizer) {
        this.itemRandomizer = itemRandomizer;
    }

    public void setNpcRandomizer(NpcRandomizer npcRandomizer) {
        this.npcRandomizer = npcRandomizer;
    }

    public String findNameOfShopNodeContainingItem(String itemToLookFor) {
        for(Map.Entry<String, String> shopNameAndContents : mapOfShopInventoryItemToContents.entrySet()) {
            if(shopNameAndContents.getValue().equals(itemToLookFor)) {
                return shopNameAndContents.getKey().substring(0, shopNameAndContents.getKey().indexOf(")") + 1);
            }
        }
        return null;
    }
}
