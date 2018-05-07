package lmr.randomizer;

import lmr.randomizer.node.NodeWithRequirements;
import lmr.randomizer.random.BossDifficulty;
import lmr.randomizer.random.ShopRandomizationEnum;
import lmr.randomizer.update.GameObjectId;

import java.util.*;

/**
 * Created by thezerothcat on 7/20/2017.
 */
public final class DataFromFile {
    public static final List<String> FLOATING_ITEM_LOCATIONS = Arrays.asList("deathv.exe", "Shuriken",
            "Rolling Shuriken", "Knife", "Talisman", "Caltrops", "Chain Whip", "Flare Gun", "bunplus.com",
            "Chakram", "Ring", "Katana", "Key Sword", "Silver Shield", "Bomb", "Axe", "Philosopher's Ocarina",
            "Flail Whip", "Earth Spear", "Angel Shield");
    public static List<String> LOCATIONS_RELATED_TO_BLOCKS = Arrays.asList("Map (Surface)", "mekuri.exe",
            "Mini Doll", "Pepper", "Anchor", "Mulana Talisman", "xmailer.exe", "Book of the Dead");
    public static List<String> TRAP_ITEMS = Arrays.asList("Trap: Graveyard", "Trap: Exploding",
            "Trap: Inferno Orb", "Trap: Twin Ankh");
    public static List<Integer> RANDOM_ITEM_GRAPHICS = Arrays.asList(1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14, 15,
            18, 19, 20, 21, 24, 25, 26, 27, 28, 29, 32, 33, 38, 39, 41, 42, 43, 44, 45, 47, 48, 49, 51, 52, 53, 54,
            55, 56, 5, 58, 59, 60, 61, 63, 64, 65, 66, 67, 68, 69, 73, 76, 91, 96, 97, 98, 99, 100, 102, 103, 104);

    public static String EXPLODING_CHEST_NAME = "Trap: Exploding";
    public static String GRAVEYARD_TRAP_CHEST_NAME = "Trap: Graveyard";

    private static List<String> allShops;
    private static List<String> allItems;
    private static List<String> allCoinChests;
    private static List<String> allNonShopItemsPlusAllRandomizedShopItems;
    private static List<String> nonRandomizedItems;
    private static List<String> nonRandomizedShops;
    private static List<String> randomizedShopItems;
    private static List<String> randomRemovableItems;
    private static List<String> nonShopItemLocations;
    private static List<String> nonRandomizedCoinChests;
    private static List<String> initialNonShopItemLocations;
    private static List<String> initialCoinChestLocations;
    private static List<String> bannedTrapLocations;
    private static Map<String, GameObjectId> mapOfItemToUsefulIdentifyingRcdData;
    private static Map<String, Integer> mapOfShopNameToShopBlock;
    private static Map<String, List<String>> mapOfShopNameToShopOriginalContents;
    private static Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject;
    private static Map<String, NodeWithRequirements> mapOfNodeNameToExitRequirementsObject;
    private static Map<String, List<String>> mapOfExitRequirementNodeToAccessibleNodes;
    private static List<String> initialShops;
    private static List<String> availableGlitches;
    private static List<String> winRequirements;
    private static List<String> chestOnlyLocations;

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
            allItems = FileUtils.getList("all/all_items.txt");
            if(allItems == null) {
                allItems = new ArrayList<>(0);
            }
        }
        return allItems;
    }

    public static List<String> getAllNonShopItemsPlusAllRandomizedShopItemsPlusAllRandomizedCoinChests() {
        if(allNonShopItemsPlusAllRandomizedShopItems == null) {
            allNonShopItemsPlusAllRandomizedShopItems = new ArrayList<>(getNonShopItemLocations());
            if(!ShopRandomizationEnum.NONE.equals(Settings.getShopRandomization())) {
                for(String item : getRandomizedShopItems()) {
                    if(!allNonShopItemsPlusAllRandomizedShopItems.contains(item)) {
                        allNonShopItemsPlusAllRandomizedShopItems.add(item);
                    }
                }
            }
            if(allNonShopItemsPlusAllRandomizedShopItems == null) {
                allNonShopItemsPlusAllRandomizedShopItems = new ArrayList<>(0); // todo: NPE more likely
            }
        }
        return allNonShopItemsPlusAllRandomizedShopItems;
    }

    public static List<String> getNonShopItemLocations() {
        if(nonShopItemLocations == null) {
            nonShopItemLocations = FileUtils.getList("all/non_shop_items.txt");
            if(Settings.isRandomizeCoinChests()) {
                nonShopItemLocations.addAll(getAllCoinChests());
                nonShopItemLocations.removeAll(getNonRandomizedCoinChests());
            }
            if(Settings.isRandomizeTrapItems()) {
                nonShopItemLocations.addAll(DataFromFile.TRAP_ITEMS);
            }
            if(nonShopItemLocations == null) {
                nonShopItemLocations = new ArrayList<>(0);
            }
        }
        return nonShopItemLocations;
    }

    public static List<String> getNonRandomizedItems() {
        if(nonRandomizedItems == null) {
            nonRandomizedItems = FileUtils.getList("min/non_randomized_items.txt");
            for(String item : Settings.getNonRandomizedItems()) {
                if(!nonRandomizedItems.contains(item)) {
                    nonRandomizedItems.add(item);
                }
            }
            if(nonRandomizedItems == null) {
                nonRandomizedItems = new ArrayList<>(0);
            }
        }
        return nonRandomizedItems;
    }

    public static List<String> getNonRandomizedShops() {
        if(nonRandomizedShops == null) {
            if(!ShopRandomizationEnum.NONE.equals(Settings.getShopRandomization())) {
                nonRandomizedShops = FileUtils.getList("min/non_randomized_shops.txt");
            }
            if(nonRandomizedShops == null) {
                nonRandomizedShops = new ArrayList<>(0);
            }
        }
        return nonRandomizedShops;
    }

    public static List<String> getNonRandomizedCoinChests() {
        if(nonRandomizedCoinChests == null) {
            if (Settings.isRandomizeCoinChests()) {
                nonRandomizedCoinChests = FileUtils.getList("min/non_randomized_coin_chests.txt");
            }
            if (nonRandomizedCoinChests == null) {
                nonRandomizedCoinChests = new ArrayList<>(0);
            }
        }
        return nonRandomizedCoinChests;
    }

    public static List<String> getInitialShops() {
        if(initialShops == null) {
            initialShops = FileUtils.getList("initial/initial_shops.txt");
            if(initialShops == null) {
                initialShops = new ArrayList<>(0);
            }
        }
        return initialShops;
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

    public static List<String> getInitialCoinChestLocations() {
        if(initialCoinChestLocations == null) {
            initialCoinChestLocations = FileUtils.getList("initial/initial_coin_chests.txt");
            if(initialCoinChestLocations == null) {
                initialCoinChestLocations = new ArrayList<>(0);
            }
            else {
                initialCoinChestLocations.removeAll(getNonRandomizedCoinChests());
            }
        }
        return initialCoinChestLocations;
    }

    public static List<String> getInitialTrapItemLocations() {
        return Arrays.asList("Trap: Inferno Orb", "Trap: Twin Ankh");
    }

    public static List<String> getBannedTrapLocations() {
        return bannedTrapLocations == null ? new ArrayList<>(0) : bannedTrapLocations;
    }

    public static void setBannedTrapLocations(Random random) {
        if(bannedTrapLocations == null) {
            bannedTrapLocations = new ArrayList<>(0);
            bannedTrapLocations.add(random.nextBoolean() ? "Coin: Guidance (One)" : "Coin: Guidance (Two)");
            bannedTrapLocations.add(random.nextBoolean() ? "Coin: Illusion (Katana)" : "Fairy Clothes");
            bannedTrapLocations.add(random.nextBoolean() ? "Map (Gate of Illusion)" : "Trap: Exploding");
            bannedTrapLocations.add(random.nextBoolean() ? "Map (Chamber of Extinction)" : "Coin: Extinction");
        }
    }

    public static List<String> getAvailableGlitches() {
        if(availableGlitches == null) {
            availableGlitches = FileUtils.getList("all/available_glitches.txt");
            if(availableGlitches == null) {
                availableGlitches = new ArrayList<>(0);
            }
        }
        return availableGlitches;
    }

    public static Map<String, GameObjectId> getMapOfItemToUsefulIdentifyingRcdData() {
        if(mapOfItemToUsefulIdentifyingRcdData == null) {
            mapOfItemToUsefulIdentifyingRcdData = FileUtils.getRcdDataIdMap("rcd/item_args.txt");
            if(mapOfItemToUsefulIdentifyingRcdData == null) {
                mapOfItemToUsefulIdentifyingRcdData = new HashMap<>(0);
            }
        }
        return mapOfItemToUsefulIdentifyingRcdData;
    }

    public static Map<String, Integer> getMapOfShopNameToShopBlock() {
        if(mapOfShopNameToShopBlock == null) {
            mapOfShopNameToShopBlock = FileUtils.getShopBlockMap("rcd/shop_args.txt");
            if(mapOfShopNameToShopBlock == null) {
                mapOfShopNameToShopBlock = new HashMap<>(0);
            }
        }
        return mapOfShopNameToShopBlock;
    }

    public static List<String> getRandomizedShopItems() {
        if(randomizedShopItems == null) {
            if(!ShopRandomizationEnum.NONE.equals(Settings.getShopRandomization())) {
                randomizedShopItems = new ArrayList<>();
                for(String shopName : getAllShops()) {
                    if(!getNonRandomizedShops().contains(shopName)) {
                        for(String shopItem : getMapOfShopNameToShopOriginalContents().get(shopName)) {
                            if(!shopItem.equals("Weights") && !shopItem.endsWith("Ammo") && !"Shell Horn".equals(shopItem) && !randomizedShopItems.contains(shopItem)) {
                                // Don't count weights, ammo, or the backup copies of Shell Horn or guild.exe
                                randomizedShopItems.add(shopItem);
                            }
                        }
                    }
                }
            }
            else {
                randomizedShopItems = new ArrayList<>(0);
            }
        }
        return randomizedShopItems;
    }

    public static List<String> getRandomRemovableItems() {
        if(randomRemovableItems == null) {
            if(Settings.getMaxRandomRemovedItems() < 1) {
                randomRemovableItems = new ArrayList<>(0);
            }
            else {
                randomRemovableItems = new ArrayList<>();
                boolean requireSerpentStaffAndChakrams = !Settings.getEnabledGlitches().contains("Cat Pause") && !Settings.getEnabledGlitches().contains("Object Zip") && !Settings.getEnabledGlitches().contains("Raindrop");
                boolean requireFruitOfEden = !Settings.getEnabledGlitches().contains("Lamp Glitch");
                boolean requirePlaneModelAndTwinStatue = !Settings.getEnabledGlitches().contains("Raindrop");
                boolean requireEarthSpearAndBronzeMirror = !Settings.getEnabledGlitches().contains("Lamp Glitch") && !Settings.getEnabledGlitches().contains("Raindrop");
                for(String itemName : getAllItems()) {
                    if(itemName.startsWith("Ankh Jewel")) {
                        continue; // Never remove an ankh jewel.
                    }
                    if(getWinRequirements().contains(itemName) || "Hand Scanner".equals(itemName)
                            || "reader.exe".equals(itemName) || "mantra.exe".equals(itemName)
                            || "Djed Pillar".equals(itemName) || "Dimensional Key".equals(itemName)
                            || "Crystal Skull".equals(itemName) || "Pochette Key".equals(itemName)
                            || "Philosopher's Ocarina".equals(itemName) || "Isis' Pendant".equals(itemName)
                            || "Helmet".equals(itemName)) {
                        continue; // Things that should never be removed.
                    }
                    if(Settings.isRequireFlaresForExtinction() && "Flare Gun".equals(itemName)) {
                        continue; // Can't get Extinction grail without flares according to this logic.
                    }
                    if(Settings.isRequireIceCapeForLava() && "Ice Cape".equals(itemName)) {
                        continue; // Needed for Viy
                    }
                    if(requireFruitOfEden && "Fruit of Eden".equals(itemName)) {
                        continue; // Can't get Illusion grail without this.
                    }
                    if(requireSerpentStaffAndChakrams && ("Chakram".equals(itemName) || "Serpent Staff".equals(itemName))) {
                        continue; // Can't get Birth grail without these.
                    }
                    if(requirePlaneModelAndTwinStatue && ("Plane Model".equals(itemName) || "Twin Statue".equals(itemName))) {
                        continue; // Can't get to Birth grail area without Plane Model, Dimensional Corridor without Twin Statue.
                    }
                    if(requireEarthSpearAndBronzeMirror && ("Earth Spear".equals(itemName) || "Bronze Mirror".equals(itemName))) {
                        continue; // Earth Spear needed for Viy access. Bronze Mirror for VIY mantra statue.
                    }
                    if(Settings.getRemovedItems().contains(itemName)) {
                        continue; // Items removed by configuration are counted separately.
                    }
                    if(Settings.isReplaceMapsWithWeights() && itemName.startsWith("Map (") && !"Map (Shrine of the Mother)".equals(itemName)) {
                        continue; // Don't count the maps that will already be replaced.
                    }
                    if(itemName.equals(Settings.getXmailerItem())) {
                        continue; // If the user wanted this item from Xelpud, they probably don't want it gone.
                    }
                    if(Settings.getNonRandomizedItems().contains(itemName)) {
                        continue; // If the user wanted this item in its original location, they probably don't want it gone.
                    }
                    if(Settings.getStartingItems().contains(itemName)) {
                        continue; // If the user wanted this item in a specific location, they probably don't want it gone.
                    }
                    if(Settings.getInitiallyAccessibleItems().contains(itemName)) {
                        continue; // If the user wanted this item early, they probably don't want it gone.
                    }
                    if(BossDifficulty.MEDIUM.equals(Settings.getBossDifficulty())) {
                        if(itemName.startsWith("Sacred Orb")
                                && !"Sacred Orb (Gate of Guidance)".equals(itemName) && !"Sacred Orb (Surface)".equals(itemName)) {
                            continue;
                        }
                    }
                    randomRemovableItems.add(itemName);
                }
            }
        }
        return randomRemovableItems;
    }

    public static Map<String, List<String>> getMapOfShopNameToShopOriginalContents() {
        if(mapOfShopNameToShopOriginalContents == null) {
            mapOfShopNameToShopOriginalContents = FileUtils.getShopOriginalContentsMap("initial/non_randomized_shop_contents.txt");
            if(mapOfShopNameToShopOriginalContents == null) {
                mapOfShopNameToShopOriginalContents = new HashMap<>(0);
            }
        }
        return mapOfShopNameToShopOriginalContents;
    }

    public static Map<String, List<String>> getMapOfExitRequirementNodeToAccessibleNodes() {
        if(mapOfExitRequirementNodeToAccessibleNodes == null) {
            mapOfExitRequirementNodeToAccessibleNodes = FileUtils.getAccessibleLocations("requirement/accessible_from_area.txt");
            if(mapOfExitRequirementNodeToAccessibleNodes == null) {
                mapOfExitRequirementNodeToAccessibleNodes = new HashMap<>(0);
            }
        }
        return mapOfExitRequirementNodeToAccessibleNodes;
    }

    public static Map<String, NodeWithRequirements> getMapOfNodeNameToRequirementsObject() {
        if(mapOfNodeNameToRequirementsObject == null) {
            mapOfNodeNameToRequirementsObject = new HashMap<>();
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/location_reqs.txt");
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/item_reqs.txt");
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/event_reqs.txt");
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/shop_reqs.txt");
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/attack_reqs.txt");
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/dead_ends.txt"); // todo: remove this when dead ends are handled better
            if(!Settings.getEnabledGlitches().isEmpty()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/glitch_reqs.txt");
            }
            if(!Settings.getEnabledDamageBoosts().isEmpty()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/dboost/location_reqs.txt");
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/dboost/item_reqs.txt");
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/dboost/shop_reqs.txt");
            }
            if(Settings.isRandomizeCoinChests()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/coin_chest_reqs.txt");
            }
            if(Settings.isRandomizeTrapItems()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/trap_item_reqs.txt");
            }
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject,
                    String.format("requirement/bosses/%s_reqs.txt", Settings.getBossDifficulty().name().toLowerCase()));
            if(!Settings.isRequireSoftwareComboForKeyFairy()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/special/no_software_combo_for_key_fairy_reqs.txt");
            }
            if(!Settings.isRequireIceCapeForLava()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/special/no_ice_cape_for_lava_reqs.txt");
            }
            if(!Settings.isRequireFlaresForExtinction()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/special/no_flares_for_extinction_reqs.txt");
            }
        }
        return mapOfNodeNameToRequirementsObject;
    }

    public static Map<String, NodeWithRequirements> getMapOfNodeNameToExitRequirementsObject() {
        if (mapOfNodeNameToExitRequirementsObject == null) {
            mapOfNodeNameToExitRequirementsObject = new HashMap<>();
            FileUtils.populateRequirements(mapOfNodeNameToExitRequirementsObject, "requirement/dead_ends.txt");
        }
        return mapOfNodeNameToExitRequirementsObject;
    }


    public static List<String> getWinRequirements() {
        if(winRequirements == null && (Settings.getMinRandomRemovedItems() > 0 || Settings.getMaxRandomRemovedItems() > 0)) {
            winRequirements = FileUtils.getList("requirement/win_reqs.txt");
        }
        return winRequirements;
    }

    public static List<String> getChestOnlyLocations() {
        if(chestOnlyLocations == null ) {
            chestOnlyLocations = FileUtils.getList("all/chest_only_locations.txt");
        }
        return chestOnlyLocations;
    }

    public static List<String> getAllCoinChests() {
        if(allCoinChests == null ) {
            allCoinChests = FileUtils.getList("all/coin_chests.txt");
        }
        return allCoinChests;
    }

    public static void clearAllData() {
        if(Settings.isChanged()) {
            allNonShopItemsPlusAllRandomizedShopItems = null;
            nonRandomizedItems = null;
            nonRandomizedShops = null;
            initialNonShopItemLocations = null;
            randomizedShopItems = null;
            randomRemovableItems = null;
            nonShopItemLocations = null;
            nonRandomizedCoinChests = null;
            bannedTrapLocations = null;
            mapOfNodeNameToRequirementsObject = null;
            mapOfNodeNameToExitRequirementsObject = null;
            winRequirements = null;
            mapOfExitRequirementNodeToAccessibleNodes = null;
        }
    }
}
