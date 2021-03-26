package lmr.randomizer;

import lmr.randomizer.node.CustomPlacementData;
import lmr.randomizer.node.NodeWithRequirements;
import lmr.randomizer.random.ShopRandomizationEnum;
import lmr.randomizer.update.GameObjectId;
import lmr.randomizer.update.LocationCoordinateMapper;

import java.util.*;

/**
 * Created by thezerothcat on 7/20/2017.
 */
public final class DataFromFile {
    public static final List<String> MAIN_WEAPONS = Arrays.asList("Whip", "Chain Whip", "Flail Whip", "Axe",
            "Knife", "Katana", "Key Sword");
    public static final List<String> FLOATING_ITEM_LOCATIONS = Arrays.asList("deathv.exe", "Shuriken",
            "Rolling Shuriken", "Knife", "Talisman", "Caltrops", "Chain Whip", "Flare Gun", "bunplus.com",
            "Chakram", "Ring", "Katana", "Key Sword", "Silver Shield", "Bomb", "Axe", "Philosopher's Ocarina",
            "Flail Whip", "Earth Spear", "Angel Shield", "Trap: Inferno Orb", "Trap: Twin Ankh");
    public static List<String> LOCATIONS_RELATED_TO_BLOCKS = Arrays.asList("Map (Surface)", "mekuri.exe",
            "Mini Doll", "Pepper", "Anchor", "Mulana Talisman", "xmailer.exe", "Book of the Dead", "Provocative Bathing Suit");
    public static List<String> TRAP_ITEMS = Arrays.asList("Trap: Graveyard", "Trap: Exploding",
            "Trap: Inferno Orb", "Trap: Twin Ankh");
    public static List<String> USELESS_ITEMS = Arrays.asList("Map (Surface)", "Map (Gate of Guidance)", "Map (Mausoleum of the Giants)", "Map (Temple of the Sun)",
            "Map (Spring in the Sky)", "Map (Inferno Cavern)", "Map (Chamber of Extinction)", "Map (Twin Labyrinths)", "Map (Endless Corridor)", "Map (Gate of Illusion)", "Map (Graveyard of the Giants)",
            "Map (Temple of Moonlight)", "Map (Tower of the Goddess)", "Map (Tower of Ruin)", "Map (Chamber of Birth)", "Map (Dimensional Corridor)");
    public static List<String> HT_BANNED_ITEMS = Arrays.asList("Holy Grail", "Hand Scanner", "reader.exe", "Feather",
            "Ice Cape", "Flail Whip", "Lamp of Time", "Bomb", "Ring", "guild.exe", "Grapple Claw",
            "Origin Seal", "Isis' Pendant",
            "Fruit of Eden");
    public static List<String> SHOP_ITEMS = Arrays.asList("Ankh Jewel (Chamber of Birth)", "Bracelet", "Buckler", "bunemon.exe",
            "capstar.exe", "Dragon Bone", "Fake Silver Shield", "guild.exe", "Hand Scanner", "Heatproof Case", "Helmet",
            "Hermes' Boots", "Lamp of Time", "miracle.exe", "Mobile Super X2", "move.exe", "Pistol", "randc.exe", "reader.exe",
            "Scriptures", "torude.exe", "Waterproof Case", "yagomap.exe");
    public static List<String> CATEGORIZED_SHOP_ITEM_LOCATIONS = Arrays.asList("Shop 1 (Surface) Item 1",
            "Shop 2 (Surface) Item 2", "Shop 2 (Surface) Item 3", "Shop 2 Alt (Surface) Item 1",
            "Shop 3 (Surface) Item 1", "Shop 3 (Surface) Item 2", "Shop 3 (Surface) Item 3", "Shop 4 (Guidance) Item 2",
            "Shop 5 (Illusion) Item 1", "Shop 6 (Mausoleum) Item 1", "Shop 7 (Graveyard) Item 2", "Shop 8 (Sun) Item 3",
            "Shop 9 (Sun) Item 1", "Shop 11 (Moonlight) Item 1", "Shop 12 Alt (Spring) Item 3", "Shop 13 (Goddess) Item 1",
            "Shop 14 (Inferno) Item 1", "Shop 15 (Ruin) Item 1", "Shop 17 (Birth) Item 2", "Shop 18 (Lil Bro) Item 1",
            "Shop 19 (Big Bro) Item 1", "Shop 20 (Twin Labs) Item 1", "Shop 21 (Unsolvable) Item 1");
    public static final List<String> GUARDIAN_DEFEATED_EVENTS = Arrays.asList("Event: Amphisbaena Defeated",
            "Event: Sakit Defeated", "Event: Ellmac Defeated", "Event: Bahamut Defeated", "Event: Viy Defeated",
            "Event: Palenque Defeated", "Event: Baphomet Defeated", "Event: Tiamat Defeated");
    public static List<Integer> RANDOM_ITEM_GRAPHICS = Arrays.asList(1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14, 15,
            16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 36, 37, 38, 39, 40, 41, 42, 43,
            44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
            71, 72, 73, 75, 76, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104);
    public static List<Integer> STARTING_LOCATIONS = Arrays.asList(0, 1, 2, 5, 7, -7, 8, 10, 11, 13, 16, 21);
    public static List<String> NPC_LOCATIONS = Arrays.asList("Location: Surface [Main]", "Location: Gate of Guidance [Main]",
            "Location: Mausoleum of the Giants", "Location: Temple of the Sun [Sphinx]", "Location: Spring in the Sky [Main]",
            "Location: Inferno Cavern [Main]", "Location: Chamber of Extinction [Main]", "Location: Chamber of Extinction [Ankh Lower]",
            "Location: Twin Labyrinths [Poseidon]", "Location: Endless Corridor [1F]",
            "Location: Gate of Illusion [Upper]", "Location: Gate of Illusion [Grail]", "Location: Gate of Illusion [Lower]",
            "Location: Graveyard of the Giants [East]", "Location: Temple of Moonlight [Upper]", "Location: Temple of Moonlight [Pyramid]",
            "Location: Tower of the Goddess [Lower]", "Location: Tower of Ruin [Southwest]", "Location: Chamber of Birth [West]",
            "Location: Dimensional Corridor [Grail]", "Location: Gate of Time [Guidance]", "Location: Gate of Time [Surface]");

    public static List<String> POSSIBLE_GLITCHES = Arrays.asList("Lamp Glitch", "Cat Pause",
            "Raindrop", "Ice Raindrop", "Pot Clip", "Object Zip", "Screen Mash");
    public static List<String> POSSIBLE_DBOOSTS = Arrays.asList("Item", "Environment", "Enemy");

    public static String EXPLODING_CHEST_NAME = "Trap: Exploding";
    public static String GRAVEYARD_TRAP_CHEST_NAME = "Trap: Graveyard";
    public static String ESCAPE_CHEST_NAME = "Coin: Twin (Escape)";

    public static String CUSTOM_SHOP_NAME = "Shop 0 (Default)";

    public static int FIRST_AVAILABLE_RANDOM_GRAPHICS_FLAG = (Settings.isFools2020Mode() || Settings.isFools2021Mode()) ? 2762 : 2730;
    public static int LAST_AVAILABLE_RANDOM_GRAPHICS_FLAG = (Settings.isFools2020Mode() || Settings.isFools2021Mode()) ? 2766 : 2760;

    private static List<String> allShops;
    private static List<String> allItems;
    private static List<String> allCoinChests;
    private static List<String> allNonShopItemsPlusAllRandomizedShopItems;
    private static List<String> nonRandomizedItems;
    private static List<String> randomizedShopItems;
    private static List<String> randomRemovableItems;
    private static List<String> nonShopItemLocations;
    private static List<String> initialNonShopItemLocations;
    private static List<String> bannedTrapLocations;
    private static Map<String, GameObjectId> mapOfItemToUsefulIdentifyingRcdData;
    private static Map<String, Integer> mapOfShopNameToShopBlock;
    private static Map<String, List<String>> mapOfShopNameToShopOriginalContents;
    private static Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject;
    private static Map<String, Set<String>> mapOfRequirementsToNodeNameObject;
    private static List<String> initialShops;
    private static List<String> availableGlitches;
    private static List<String> winRequirements;
    private static List<String> chestOnlyLocations;

    private static CustomPlacementData customPlacementData;

    private DataFromFile() { }

    public static List<String> getAllShops() {
        if(allShops == null) {
            allShops = FileUtils.getList("all/all_shops.txt");
            if(Settings.isRandomizeDracuetShop()) {
                allShops.add("Shop 23 (HT)");
            }
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
            if(nonShopItemLocations == null) {
                nonShopItemLocations = new ArrayList<>(0);
            }
            if(Settings.isRandomizeCoinChests()) {
                nonShopItemLocations.addAll(getAllCoinChests());
            }
            if(Settings.isRandomizeTrapItems()) {
                nonShopItemLocations.addAll(DataFromFile.TRAP_ITEMS);
            }
            if(Settings.isRandomizeEscapeChest()) {
                nonShopItemLocations.add(DataFromFile.ESCAPE_CHEST_NAME);
            }
        }
        return nonShopItemLocations;
    }

    public static List<String> getNonRandomizedItems() {
        if(nonRandomizedItems == null) {
            nonRandomizedItems = new ArrayList<>();
            nonRandomizedItems.add("Maternity Statue");
            for(String item : Settings.getNonRandomizedItems()) {
                if(!nonRandomizedItems.contains(item)) {
                    nonRandomizedItems.add(item);
                }
            }
            if(!Settings.isRandomizeForbiddenTreasure()) {
                nonRandomizedItems.add("Provocative Bathing Suit");
            }
            if(!Settings.isRandomizeXmailer()) {
                nonRandomizedItems.add("xmailer.exe");
            }
            if(nonRandomizedItems == null) {
                nonRandomizedItems = new ArrayList<>(0);
            }
        }
        return nonRandomizedItems;
    }

    public static List<String> getInitialShops() {
        if(initialShops == null) {
            initialShops = new ArrayList<>();
        }
        return initialShops;
    }

    public static List<String> getInitialNonShopItemLocations() {
        if(initialNonShopItemLocations == null) {
            initialNonShopItemLocations = new ArrayList<>();
        }
        return initialNonShopItemLocations;
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
                    for(String shopItem : getMapOfShopNameToShopOriginalContents().get(shopName)) {
                        if(!shopItem.equals("Weights") && !shopItem.endsWith("Ammo") && !"Shell Horn".equals(shopItem)
                                && !randomizedShopItems.contains(shopItem)) {
                            // Don't count weights, ammo, or the backup copies of Shell Horn or guild.exe
                            randomizedShopItems.add(shopItem);
                        }
                    }
                }
                if(!LocationCoordinateMapper.isSurfaceStart() && Settings.getCurrentStartingLocation() != 23 && Settings.getCurrentStartingLocation() != 24) {
                    // Random starting location comes with a special shop.
                    for(String shopItem : getMapOfShopNameToShopOriginalContents().get(CUSTOM_SHOP_NAME)) {
                        if(!shopItem.equals("Weights") && !shopItem.endsWith("Ammo") && !"Shell Horn".equals(shopItem)
                                && !randomizedShopItems.contains(shopItem)) {
                            // Don't count weights, ammo, or the backup copies of Shell Horn or guild.exe
                            randomizedShopItems.add(shopItem);
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
            randomRemovableItems = new ArrayList<>();
            boolean requireSerpentStaffAndChakrams = !Settings.getEnabledGlitches().contains("Cat Pause") && !Settings.getEnabledGlitches().contains("Object Zip") && !Settings.getEnabledGlitches().contains("Raindrop");
            boolean requireFruitOfEden = !Settings.isRandomizeBacksideDoors() || (!Settings.getEnabledGlitches().contains("Raindrop") && !Settings.isAutomaticMantras() && !Settings.isAlternateMotherAnkh()); // Deep dive not supported in logic, so the only way to reach upper Illusion grail is the backside door, and raindropping is needed to get to where you'd recite LAMULANA
            boolean requirePlaneModelAndTwinStatueAndLiteracy = !Settings.getEnabledGlitches().contains("Raindrop");
            boolean requireEarthSpearAndBronzeMirror = !Settings.getEnabledGlitches().contains("Lamp Glitch") && !Settings.getEnabledGlitches().contains("Raindrop");
            for(String itemName : getAllItems()) {
                if(!Settings.isFoolsGameplay() && itemName.startsWith("Ankh Jewel")) {
                    continue; // Don't remove ankh jewels unless it's allowed.
                }
                if("Holy Grail".equals(itemName) || "Dimensional Key".equals(itemName)
                        || "Crystal Skull".equals(itemName) || "Pochette Key".equals(itemName)
                        || "Philosopher's Ocarina".equals(itemName)
                        || "Helmet".equals(itemName) || "Vessel".equals(itemName)
                        || "Isis' Pendant".equals(itemName)
                        || "Origin Seal".equals(itemName) || "Birth Seal".equals(itemName)
                        || "Life Seal".equals(itemName) || "Death Seal".equals(itemName)) {
                    continue; // Things that should never be removed.
                }
                if(!Settings.isFeatherlessMode() && "Feather".equals(itemName)) {
                    continue;
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
                if(requirePlaneModelAndTwinStatueAndLiteracy && ("Plane Model".equals(itemName) || "Twin Statue".equals(itemName))) {
                    continue; // Can't get to Birth grail area without Plane Model, Dimensional Corridor without Twin Statue.
                }
                if(requireEarthSpearAndBronzeMirror && ("Earth Spear".equals(itemName) || "Bronze Mirror".equals(itemName))) {
                    continue; // Earth Spear needed for Viy access. Bronze Mirror for VIY mantra statue.
                }
                if(Settings.isReplaceMapsWithWeights() && itemName.startsWith("Map (") && !"Map (Shrine of the Mother)".equals(itemName)) {
                    continue; // Don't count the maps that will already be replaced.
                }
                if(!Settings.isAlternateMotherAnkh() && "Key Sword".equals(itemName)) {
                    continue; // Required to start the Mother fight
                }
                if(Settings.getNonRandomizedItems().contains(itemName)) {
                    continue; // If the user wanted this item in its original location, they probably don't want it gone.
                }
                if(!Settings.isRandomizeXmailer() && "xmailer.exe".equals(itemName)) {
                    continue; // NPCs can't have removed items yet.
                }
                if("Provocative Bathing Suit".equals(itemName)) {
                    continue; // No value in removing this, and if someone wanted it randomized they probably want to actually see it.
                }
                if(Settings.getStartingItems().contains(itemName)) {
                    continue; // If the user wanted this item in a specific location, they probably don't want it gone.
                }
                if(Settings.getInitiallyAccessibleItems().contains(itemName)) {
                    continue; // If the user wanted this item early, they probably don't want it gone.
                }

                if("mantra.exe".equals(itemName) || "Djed Pillar".equals(itemName)) {
                    if(!Settings.isAlternateMotherAnkh() || !"Yellow".equals(Settings.getMedicineColor())) {
                        continue; // Don't remove mantra or Djed Pillar if they're needed for reciting mantras to fight mother.
                    }
                }
                if("Lamp of Time".equals(itemName) && !"Yellow".equals(Settings.getMedicineColor())) {
                    continue; // Don't remove mantra or Djed Pillar if they're needed for reciting mantras to fight mother.
                }
                if("Hand Scanner".equals(itemName) || "reader.exe".equals(itemName)) {
                    if(requirePlaneModelAndTwinStatueAndLiteracy || !Settings.isAutomaticGrailPoints()
                            || !Settings.isAlternateMotherAnkh() || !"Yellow".equals(Settings.getMedicineColor())) {
                        continue; // Don't remove literacy if it's needed for reciting mantras to fight Mother.
                    }
                }

                randomRemovableItems.add(itemName);
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

    public static Map<String, NodeWithRequirements> getMapOfNodeNameToRequirementsObject() {
        if(mapOfNodeNameToRequirementsObject == null) {
            mapOfNodeNameToRequirementsObject = new HashMap<>();
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/location_reqs.txt", true);
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/item_reqs.txt", true);
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/event_reqs.txt", true);
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/shop_reqs.txt", true);
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/attack_reqs.txt", true);
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/dead_ends.txt", true);
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/transition_reqs.txt", true);
            if(Settings.isHalloweenMode()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/npc_reqs.txt", true);
            }
            if(Settings.isFoolsNpc()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/npc_door_reqs.txt", true);
            }
            if(!Settings.getEnabledGlitches().isEmpty()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/glitch_reqs.txt", true);
            }
            if(Settings.isRandomizeCoinChests()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/coin_chest_reqs.txt", true);
            }
            if(Settings.isRandomizeTrapItems()) {
                FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "requirement/trap_item_reqs.txt", true);
            }
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject,
                    String.format("requirement/bosses/%s_reqs.txt", Settings.getBossDifficulty().name().toLowerCase()), true);
            for(String removeNode : getCustomPlacementData().getRemovedLogicNodes()) {
                mapOfNodeNameToRequirementsObject.remove(removeNode);
            }
            if(LocationCoordinateMapper.isSurfaceStart() || Settings.getCurrentStartingLocation() == 23 || Settings.getCurrentStartingLocation() == 24) {
                mapOfNodeNameToRequirementsObject.remove(DataFromFile.CUSTOM_SHOP_NAME);
            }
            FileUtils.populateRequirements(mapOfNodeNameToRequirementsObject, "custom-reqs.txt", false);

            for(NodeWithRequirements nodeWithRequirements : mapOfNodeNameToRequirementsObject.values()) {
                nodeWithRequirements.expandRequirements();
            }
        }
        return mapOfNodeNameToRequirementsObject;
    }

    //Goes through the map of nodes and their requirements, and makes a reverse map, where the keys are the items/areas/etc
    // and the value is a set of all nodes that have that item/area as a requirement.  Call this after building the previous map.
    public static Map<String, Set<String>> getMapOfRequirementsToNodeNameObject() {
        if(mapOfRequirementsToNodeNameObject == null) {
            mapOfRequirementsToNodeNameObject = new HashMap<String, Set<String>>();
            NodeWithRequirements node;
            List<List<String>> listOfRequirementSets;

            for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
                node = mapOfNodeNameToRequirementsObject.get(nodeName);
                listOfRequirementSets = node.getAllRequirements();
                for(List<String> requirementSet : listOfRequirementSets) {
                    for(String requirement : requirementSet) {
                        Set nodeSet = mapOfRequirementsToNodeNameObject.get(requirement);
                        if(nodeSet == null)
                            nodeSet = new HashSet<String>();
                        nodeSet.add(nodeName);
                        mapOfRequirementsToNodeNameObject.put(requirement, nodeSet);
                    }
                }
            }
        }
        return mapOfRequirementsToNodeNameObject;
    }

    public static List<String> getWinRequirements() {
        if(winRequirements == null) {
            if(Settings.isHalloweenMode()) {
                winRequirements = FileUtils.getList("requirement/win/npc_win_reqs.txt");
            }
            else {
                winRequirements = FileUtils.getList("requirement/win/win_reqs.txt");
            }
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

    public static List<String> getHTItems(List<String> possibleItems) {
        List<String> enabledItems = new ArrayList<>(possibleItems.size());
        if(Settings.isHTFullRandom()) {
            for(String item : possibleItems) {
                if(!HT_BANNED_ITEMS.contains(item) && !item.startsWith("Coin:") && !item.startsWith("Trap:")
                        && !Settings.getRemovedItems().contains(item)
                        && !Settings.getCurrentRemovedItems().contains(item)) {
                    enabledItems.add(item);
                }
            }
        }
        else {
            for(String item : possibleItems) {
                if(USELESS_ITEMS.contains(item)) {
                    enabledItems.add(item);
                }
            }
        }
        return enabledItems;
    }

    public static CustomPlacementData getCustomPlacementData() {
        if(customPlacementData == null) {
            customPlacementData = FileUtils.getCustomPlacementData();
        }
        return customPlacementData;
    }

    public static void clearCustomPlacementData() {
        customPlacementData = null;
    }

    public static void clearInitialLocations() {
        initialShops = null;
        initialNonShopItemLocations = null;
    }

    public static void clearAllData() {
        if(Settings.isChanged()) {
            allNonShopItemsPlusAllRandomizedShopItems = null;
            nonRandomizedItems = null;
            allShops = null;
            randomizedShopItems = null;
            randomRemovableItems = null;
            nonShopItemLocations = null;
            mapOfNodeNameToRequirementsObject = null;
            mapOfRequirementsToNodeNameObject = null;
            winRequirements = null;
        }
        initialShops = null;
        initialNonShopItemLocations = null;
        bannedTrapLocations = null;
    }
}
