package lmr.randomizer.rcd;

import lmr.randomizer.*;
import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.DatReader;
import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.node.CustomPlacementData;
import lmr.randomizer.random.*;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.GameDataTracker;
import lmr.randomizer.update.GameObjectId;
import lmr.randomizer.update.LocationCoordinateMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.*;

public class RcdUpdaterTest {
    @Test
    public void testThing() {
        int testSeed = 223;
        Settings.setLaMulanaBaseDir(new File("src/test/resources").getAbsolutePath(), false);
        Settings.setRandomizeCoinChests(true, false);
        Settings.setRandomizeTransitionGates(true, false);
        Settings.setRandomizeOneWayTransitions(true, false);
        Settings.setRandomizeBacksideDoors(true, false);
        Settings.setRandomizeNonBossDoors(true, false);
        Settings.setRandomizeTrapItems(true, false);
        Settings.setRandomizeNpcs(false, false);
        HolidayModePlacements.applyCustomPlacements();
        try {
//            if(!"Whip".equals(Settings.getCurrentStartingWeapon())) {
//                removedItems.add("Whip");
//            }
//            if(totalItemsRemoved < 1) {
//                Settings.setCurrentRemovedItems(removedItems);
//                return;
//            }

            Random random = new Random(testSeed);
            determineStartingLocation(random);
            determineStartingWeapon(random);
            determineFoolsGameplay(random);
            determineGiant(random);

            BacksideDoorRandomizer backsideDoorRandomizer = new BacksideDoorRandomizer();
            backsideDoorRandomizer.determineDoorDestinations(random);

            TransitionGateRandomizer transitionGateRandomizer = new TransitionGateRandomizer(backsideDoorRandomizer);

            SealRandomizer sealRandomizer = new SealRandomizer();
            NpcRandomizer npcRandomizer = new NpcRandomizer();

            transitionGateRandomizer.determineGateDestinations(random);
            backsideDoorRandomizer.determineDoorBosses(random, 1);
            sealRandomizer.assignSeals(random);
            npcRandomizer.determineNpcLocations(random);

            ItemRandomizer itemRandomizer = new ItemRandomizer();
            ShopRandomizer shopRandomizer = buildShopRandomizer(itemRandomizer, npcRandomizer);
            AccessChecker accessChecker = buildAccessChecker(itemRandomizer, shopRandomizer, backsideDoorRandomizer, transitionGateRandomizer, sealRandomizer, npcRandomizer);

            List<String> startingNodes = getStartingNodes();
            if(transitionGateRandomizer.isEndlessL1Open(null)) {
                startingNodes.add("State: Endless L1 Open");
            }

            DataFromFile.clearInitialLocations();
            AccessChecker initiallyAccessibleLocationFinder = new AccessChecker(accessChecker, true);
            initiallyAccessibleLocationFinder.computeStartingLocationAccess(false, null);
            for (String startingNode : startingNodes) {
                initiallyAccessibleLocationFinder.computeAccessibleNodes(startingNode, false, null);
            }
            while (!initiallyAccessibleLocationFinder.getQueuedUpdates().isEmpty()) {
                // Get some additional access based on glitch logic/settings access
                initiallyAccessibleLocationFinder.computeAccessibleNodes(initiallyAccessibleLocationFinder.getQueuedUpdates().iterator().next(), false, null);
            }

            itemRandomizer.placeNonRandomizedItems();
            shopRandomizer.placeNonRandomizedItems();
            if(ItemRandomizer.ALL_SUBWEAPONS.contains(Settings.getCurrentStartingWeapon())) {
                shopRandomizer.placeSpecialSubweaponAmmo(random);
            }
            if(ShopRandomizationEnum.EVERYTHING.equals(Settings.getShopRandomization())) {
                ((EverythingShopRandomizer)shopRandomizer).placeGuaranteedWeights(random);
            }
            if(Settings.isRandomizeForbiddenTreasure()) {
                itemRandomizer.placeForbiddenTreasureItem(random);
            }
            shopRandomizer.determineItemTypes(random);
            accessChecker.determineCursedChests(random);
            if(Settings.isRandomizeCoinChests() || Settings.isRandomizeTrapItems()) {
                if(!itemRandomizer.placeChestOnlyItems(random)) {
                    Assert.fail("bad random seed, lazy fix");
                }
            }

            if(!itemRandomizer.placeNoRequirementItems(new ArrayList<>(new ArrayList<>()), random)) {
                Assert.fail("bad random seed, lazy fix");
            }

            if(!itemRandomizer.placeAllItems(random)) {
                Assert.fail("placeAllItems bad random seed, lazy fix");
            }

//            MoneyChecker moneyChecker;
//            if(Settings.isRandomizeTransitionGates()) {
//                moneyChecker = new MoneyChecker(itemRandomizer, shopRandomizer, backsideDoorRandomizer, transitionGateRandomizer);
//                backsideDoorRandomizer.rebuildRequirementsMap();
//                moneyChecker.computeStartingLocationAccess(1);
//                for (String startingNode : startingNodes) {
//                    moneyChecker.computeAccessibleNodes(startingNode, 1);
//                }
//                while(!moneyChecker.getQueuedUpdates().isEmpty()) {
//                    moneyChecker.computeAccessibleNodes(moneyChecker.getQueuedUpdates().iterator().next(), 1);
//                }
//
//                transitionGateRandomizer.placeTowerOfTheGoddessPassthroughPipe(random);
//            }
//            else {
//                moneyChecker = null;
//            }

//            List<Block> datInfo = DatReader.getDatScriptInfo();

            Translations.initTranslations();
            List<Block> datInfo = DatReader.getDatScriptInfo();

            RcdData rcdDataOld = new RcdData(RcdReader.getRcdScriptInfo_Old());
            Map<GameObjectId, List<GameObject>> oldMap = new HashMap<>(GameDataTracker.mapOfChestIdentifyingInfoToGameObject);
            random = new Random(testSeed);
            itemRandomizer.updateFiles(random);
            GameDataTracker.mapOfChestIdentifyingInfoToGameObject.clear();

            RcdData rcdData = new RcdData(RcdReader.getRcdScriptInfo());
            GameUpdater.update(rcdData);
            random = new Random(testSeed);
            itemRandomizer.updateFiles(random);

            Map<GameObjectId, List<GameObject>> newMap = new HashMap<>(GameDataTracker.mapOfChestIdentifyingInfoToGameObject);

//            if(Settings.isRandomizeNpcs()) {
//                // This must happen before shop data randomized in order to get the correct shop screen for little brother
//                npcRandomizer.updateNpcs();
//            }
//
//            boolean subweaponOnly = isSubweaponOnly();
//            shopRandomizer.updateFiles(datInfo, subweaponOnly, moneyChecker, random);
//
//            List<String> availableSubweapons = new ArrayList<>(ItemRandomizer.ALL_SUBWEAPONS);
//            availableSubweapons.removeAll(Settings.getRemovedItems());
//            availableSubweapons.removeAll(Settings.getCurrentRemovedItems());
//            if(!availableSubweapons.isEmpty()) {
//                GameDataTracker.updateSubweaponPot(availableSubweapons.get(random.nextInt(availableSubweapons.size())));
//            }
//            if(Settings.isRandomizeEnemies()) {
//                GameDataTracker.randomizeEnemies(random);
//            }
//            if(Settings.isFools2021Mode()) {
//                GameDataTracker.updateEdenDaises(random);
//            }
//
//            if(Settings.isRandomizeSeals()) {
//                sealRandomizer.updateSeals();
//            }
//
//            if(Settings.isRandomizeBacksideDoors()) {
//                backsideDoorRandomizer.updateBacksideDoors();
//            }
//            if(Settings.isRandomizeTransitionGates()) {
//                transitionGateRandomizer.updateTransitions();
//            }
//            if(Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart() || Settings.isRandomizeStartingLocation() || Settings.isFools2020Mode()) {
//                GameDataTracker.updateXelpudIntro(datInfo);
//            }
////                if(Settings.isRandomizeMantras()) {
////                    GameDataTracker.randomizeMantras(random);
////                }
//            if(Settings.isHalloweenMode()) {
//                int shopBlockNumber = AddObject.addSecretShopBlock(datInfo).getBlockNumber();
//                int danceBlockNumber = AddObject.addDanceBlock(datInfo).getBlockNumber();
//                GameDataTracker.replaceNightSurfaceWithSurface(rcdData, danceBlockNumber, shopBlockNumber);
//                if(Settings.isIncludeHellTempleNPCs()) {
//                    GameDataTracker.addHTSkip(rcdData, datInfo);
//                }
//                GameDataTracker.fixTransitionGates(rcdData);
//            }
//            else if(Settings.isFools2020Mode()) {
//                GameDataTracker.updateWorldForFools2020(rcdData, datInfo);
//            }

            for(Zone zone : rcdDataOld.getZones()) {
                for(Room room : zone.getRooms()) {
                    for(Screen oldRcdDataScreen : room.getScreens()) {
                        Screen newRcdDataScreen = rcdData.getScreen(oldRcdDataScreen.getZoneIndex(), oldRcdDataScreen.getRoomIndex(), oldRcdDataScreen.getScreenIndex());
                        for(int i = 0; i < 4; i++) {
                            ScreenExit oldScreenExit = oldRcdDataScreen.getScreenExit(i);
                            ScreenExit newScreenExit = newRcdDataScreen.getScreenExit(i);
                            Assert.assertEquals(newScreenExit.getZoneIndex(), oldScreenExit.getZoneIndex(), String.format("New exit (%d, %d, %d) for direction %d does not match old exit (%d, %d, %d) on screen %s", newScreenExit.getZoneIndex(), newScreenExit.getRoomIndex(), newScreenExit.getScreenIndex(), i, oldScreenExit.getZoneIndex(), oldScreenExit.getRoomIndex(), oldScreenExit.getScreenIndex(), oldRcdDataScreen.getContainerString()));
                            Assert.assertEquals(newScreenExit.getRoomIndex(), oldScreenExit.getRoomIndex(), String.format("New exit (%d, %d, %d) for direction %d does not match old exit (%d, %d, %d) on screen %s", newScreenExit.getZoneIndex(), newScreenExit.getRoomIndex(), newScreenExit.getScreenIndex(), i, oldScreenExit.getZoneIndex(), oldScreenExit.getRoomIndex(), oldScreenExit.getScreenIndex(), oldRcdDataScreen.getContainerString()));
                            Assert.assertEquals(newScreenExit.getScreenIndex(), oldScreenExit.getScreenIndex(), String.format("New exit (%d, %d, %d) for direction %d does not match old exit (%d, %d, %d) on screen %s", newScreenExit.getZoneIndex(), newScreenExit.getRoomIndex(), newScreenExit.getScreenIndex(), i, oldScreenExit.getZoneIndex(), oldScreenExit.getRoomIndex(), oldScreenExit.getScreenIndex(), oldRcdDataScreen.getContainerString()));
                        }
                    }
                }
            }

            testPot(rcdData);

//            short inventoryArg = 0;
//            short worldFlag = FlagConstants.WF_TRAP_GRAVEYARD;
//            Assert.assertEquals(newMap.get(new GameObjectId(inventoryArg, worldFlag)).toString(), oldMap.get(new GameObjectId(inventoryArg, worldFlag)).toString());
//
//            inventoryArg = 0;
//            worldFlag = FlagConstants.WF_TRAP_ILLUSION;
//            Assert.assertEquals(newMap.get(new GameObjectId(inventoryArg, worldFlag)).toString(), oldMap.get(new GameObjectId(inventoryArg, worldFlag)).toString());

            for(GameObjectId key : oldMap.keySet()) {
                Assert.assertTrue(newMap.containsKey(key), "New map missing key:" + key.toString());
                List<GameObject> oldObjectsTracked = oldMap.get(key);
                List<GameObject> newObjectsTracked = newMap.get(key);
                for(GameObject oldObject : oldObjectsTracked) {
                    boolean objNotFound = true;
                    for(GameObject newObject : newObjectsTracked) {
                        if(sameObject(newObject, oldObject)) {
                            objNotFound = false;
                        }
                    }
                    if(objNotFound) {
                        Assert.fail("Old object not found with new tracked objects: " + oldObject.toString() + "\nOld objects:\n" + oldObjectsTracked.toString() + "\nNew objects:\n" + newObjectsTracked.toString());
                    }
                }
                for(GameObject newObject : newObjectsTracked) {
                    boolean objNotFound = true;
                    for(GameObject oldObject : oldObjectsTracked) {
                        if(sameObject(oldObject, newObject)) {
                            objNotFound = false;
                        }
                    }
                    if(objNotFound) {
                        Assert.fail("New object not found with old tracked objects: " + newObject.toString() + "\nNew objects:\n" + newObjectsTracked.toString() + "\nOld objects:\n" + oldObjectsTracked.toString());
                    }
                }
            }

            for(Zone oldRcdDataZone : rcdDataOld.getZones()) {
                for(GameObject gameObject : oldRcdDataZone.getObjects()) {
                    Assert.assertTrue(containsObject(rcdData.getZone(oldRcdDataZone.getZoneIndex()), gameObject), "Old object not found on new screen: " + gameObject.toString() + "\nContainer:\n" + rcdData.getZone(oldRcdDataZone.getZoneIndex()).toString());
                }
                for(Room oldRcdDataRoom : oldRcdDataZone.getRooms()) {
                    for(GameObject gameObject : oldRcdDataRoom.getObjects()) {
                        Assert.assertTrue(containsObject(rcdData.getRoom(oldRcdDataRoom.getZoneIndex(), oldRcdDataRoom.getRoomIndex()), gameObject), "Old object not found on new screen: " + gameObject.toString() + "\nContainer:\n" + rcdData.getRoom(oldRcdDataRoom.getZoneIndex(), oldRcdDataRoom.getRoomIndex()).toString());
                    }
                    for(Screen oldRcdDataScreen : oldRcdDataRoom.getScreens()) {
                        for(GameObject gameObject : oldRcdDataScreen.getObjects()) {
                            Assert.assertTrue(containsObject(rcdData.getScreen(oldRcdDataScreen.getZoneIndex(), oldRcdDataScreen.getRoomIndex(), oldRcdDataScreen.getScreenIndex()), gameObject), "Old object not found on new screen: " + gameObject.toString() + "\nContainer:\n" + rcdData.getScreen(oldRcdDataScreen.getZoneIndex(), oldRcdDataScreen.getRoomIndex(), oldRcdDataScreen.getScreenIndex()).toString());
                        }
                    }
                }
            }
            for(Zone newRcdDataZone : rcdData.getZones()) {
                for(GameObject gameObject : newRcdDataZone.getObjects()) {
                    Assert.assertTrue(containsObject(rcdDataOld.getZone(newRcdDataZone.getZoneIndex()), gameObject), "New object not found on old screen: " + gameObject.toString() + "\nContainer:\n" + rcdDataOld.getZone(newRcdDataZone.getZoneIndex()).toString());
                }
                for(Room newRcdDataRoom : newRcdDataZone.getRooms()) {
                    for(GameObject gameObject : newRcdDataRoom.getObjects()) {
                        Assert.assertTrue(containsObject(rcdDataOld.getRoom(newRcdDataRoom.getZoneIndex(), newRcdDataRoom.getRoomIndex()), gameObject), "New object not found on old screen: " + gameObject.toString() + "\nContainer:\n" + rcdDataOld.getRoom(newRcdDataRoom.getZoneIndex(), newRcdDataRoom.getRoomIndex()).toString());
                    }
                    for(Screen newRcdDataScreen : newRcdDataRoom.getScreens()) {
                        for(GameObject gameObject : newRcdDataScreen.getObjects()) {
                            Assert.assertTrue(containsObject(rcdDataOld.getScreen(newRcdDataScreen.getZoneIndex(), newRcdDataScreen.getRoomIndex(), newRcdDataScreen.getScreenIndex()), gameObject), "New object not found on old screen: " + gameObject.toString() + "\nContainer:\n" + rcdDataOld.getScreen(newRcdDataScreen.getZoneIndex(), newRcdDataScreen.getRoomIndex(), newRcdDataScreen.getScreenIndex()).toString());
                        }
                    }
                }
            }

//            for(int i = 0; i < rcdData.getZones().size(); i++) {
//                Assert.assertEquals(rcdData.getZones().get(i).toString(), rcdDataOld.getZones().get(i).toString());
//            }
        }
        catch(Exception ex) {
            Assert.fail("Unable to find resource file");
        }
    }

    private static void determineStartingLocation(Random random) {
        if(!Settings.isRandomizeStartingLocation()) {
            Settings.setCurrentStartingLocation(1);
            FileUtils.logFlush("Selected starting location: " + LocationCoordinateMapper.getStartingZoneName(1));
            return;
        }

        CustomPlacementData customPlacementData = DataFromFile.getCustomPlacementData();
        Integer customStartingLocation = customPlacementData.getStartingLocation();
        if(customStartingLocation != null) {
            Settings.setCurrentStartingLocation(customStartingLocation);
            FileUtils.logFlush("Selected custom starting location: " + LocationCoordinateMapper.getStartingZoneName(customStartingLocation));
            return;
        }

        List<Integer> possibleStartingLocations = new ArrayList<>(DataFromFile.STARTING_LOCATIONS);
        if(!Settings.getStartingItemsIncludingCustom().contains("Holy Grail")) {
            // Tower of Ruin will be unable to get back to the grail tablet easily/will have very limited options without grail/feather/boots/ice cape, so just ban it.
            possibleStartingLocations.remove((Integer)14);
        }
        if(!Settings.isRandomizeTransitionGates()) {
            // Most backside fields aren't an option unless random transitions help keep you from getting stuck on one side of the ruins.
            possibleStartingLocations.remove((Integer)11);
            possibleStartingLocations.remove((Integer)13);
            possibleStartingLocations.remove((Integer)14);
            possibleStartingLocations.remove((Integer)16);
            possibleStartingLocations.remove((Integer)21);
        }
        Settings.setCurrentStartingLocation(possibleStartingLocations.get(random.nextInt(possibleStartingLocations.size())));
        FileUtils.logFlush("Selected starting location: " + LocationCoordinateMapper.getStartingZoneName(Settings.getCurrentStartingLocation()));
    }

    private static void determineStartingWeapon(Random random) {
        CustomPlacementData customPlacementData = DataFromFile.getCustomPlacementData();
        String customStartingWeapon = customPlacementData.getStartingWeapon();
        if(customStartingWeapon != null) {
            Settings.setCurrentStartingWeapon(customStartingWeapon);
            FileUtils.logFlush("Selected custom starting weapon: " + customStartingWeapon);
            return;
        }

        List<String> startingWeapons = new ArrayList<>();
        if(Settings.isAllowWhipStart()) {
            startingWeapons.add("Whip");
        }
        if(Settings.isAllowMainWeaponStart()) {
            startingWeapons.add("Knife");
            if(!Settings.isFools2021Mode()) {
                startingWeapons.add("Key Sword");
            }
            startingWeapons.add("Axe");
            startingWeapons.add("Katana");
        }
        if(Settings.isAllowSubweaponStart()) {
            startingWeapons.addAll(ItemRandomizer.ALL_SUBWEAPONS);
        }

        startingWeapons.removeAll(customPlacementData.getRemovedItems());
        startingWeapons.removeAll(customPlacementData.getStartingItems());
        startingWeapons.removeAll(Settings.getRemovedItems());
        Settings.setCurrentStartingWeapon(startingWeapons.get(random.nextInt(startingWeapons.size())));
        FileUtils.logFlush("Selected starting weapon: " + Settings.getCurrentStartingWeapon());
    }

    private static void determineFoolsGameplay(Random random) {
        if(!Settings.isFoolsGameplay()) {
            Settings.setCurrentBossCount(8);
            FileUtils.logFlush("Using default boss count: 8");
            return;
        }

//        int ankhJewelsRemoved = 0;
//        for (String item : DataFromFile.getCustomPlacementData().getRemovedItems()) {
//            if(item.contains("Ankh Jewel")) {
//                ankhJewelsRemoved += 1;
//            }
//        }

//        Settings.setCurrentBossCount(random.nextInt((Settings.isFools2021Mode() ? 4 : 5) - ankhJewelsRemoved) + 4);
        Settings.setCurrentBossCount(7);
        FileUtils.logFlush("Using random boss count: " + Settings.getCurrentBossCount());
    }

    private static void determineGiant(Random random) {
        if(Settings.isFools2021Mode()) {
            List<String> giants = Arrays.asList("Zebu", "Bado", "Migela", "Ledo", "Abuto", "Ji", "Ribu", "Sakit"); // not Futo
            Settings.setCurrentGiant(giants.get(random.nextInt(giants.size())));
        }
    }

    private static ShopRandomizer buildShopRandomizer(ItemRandomizer itemRandomizer, NpcRandomizer npcRandomizer) {
        ShopRandomizer shopRandomizer;
        if(ShopRandomizationEnum.CATEGORIZED.equals(Settings.getShopRandomization())) {
            shopRandomizer = new CategorizedShopRandomizer();
        }
        else {
            shopRandomizer = new EverythingShopRandomizer();
        }

        itemRandomizer.setShopRandomizer(shopRandomizer);
        shopRandomizer.setItemRandomizer(itemRandomizer);
        shopRandomizer.setNpcRandomizer(npcRandomizer);
        return shopRandomizer;
    }

    private static AccessChecker buildAccessChecker(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer,
                                                    BacksideDoorRandomizer backsideDoorRandomizer,
                                                    TransitionGateRandomizer transitionGateRandomizer,
                                                    SealRandomizer sealRandomizer, NpcRandomizer npcRandomizer) {
        AccessChecker accessChecker = new AccessChecker();
        accessChecker.setItemRandomizer(itemRandomizer);
        accessChecker.setShopRandomizer(shopRandomizer);
        accessChecker.setBacksideDoorRandomizer(backsideDoorRandomizer);
        accessChecker.setTransitionGateRandomizer(transitionGateRandomizer);
        accessChecker.setSealRandomizer(sealRandomizer);
        accessChecker.setNpcRandomizer(npcRandomizer);
        itemRandomizer.setAccessChecker(accessChecker);
        shopRandomizer.setAccessChecker(accessChecker);
        return accessChecker;
    }

    private static List<String> getStartingNodes() {
        List<String> startingNodes = new ArrayList<>();
        startingNodes.add(Settings.getCurrentStartingWeapon());
        startingNodes.add("State: Pre-Escape");
        if(ItemRandomizer.ALL_SUBWEAPONS.contains(Settings.getCurrentStartingWeapon())) {
            startingNodes.add(Settings.getCurrentStartingWeapon() + " Ammo");
            if("Pistol".equals(Settings.getCurrentStartingWeapon())) {
                startingNodes.add("Attack: Pistol"); // This one normally requires Isis' Pendant
            }
        }
        for(String startingItem : Settings.getStartingItemsIncludingCustom()) {
            startingNodes.add(startingItem);
        }
        for(String enabledGlitch : Settings.getEnabledGlitches()) {
            startingNodes.add("Setting: " + enabledGlitch);
        }
        for(String enabledDamageBoost : Settings.getEnabledDamageBoosts()) {
            startingNodes.add("Boost: " + enabledDamageBoost);
        }
        if(!Settings.isAutomaticHardmode()) {
            startingNodes.add("Mode: Normal");
        }
        if(!Settings.isRequireSoftwareComboForKeyFairy()) {
            startingNodes.add("Setting: No Combo Key Fairy");
        }
        if(!Settings.isRequireIceCapeForLava()) {
            startingNodes.add("Setting: Lava HP");
        }
        if(Settings.isAutomaticGrailPoints()) {
            startingNodes.add("Setting: Autoread Grail");
        }
        if(Settings.isAutomaticMantras()) {
            startingNodes.add("Setting: Skip Mantras");
        }
        if(Settings.isAutomaticTranslations()) {
            startingNodes.add("Setting: La-Mulanese");
        }
        if(Settings.isUshumgalluAssist()) {
            startingNodes.add("Setting: Ushumgallu Assist");
        }
        if(!Settings.isRequireFlaresForExtinction()) {
            startingNodes.add("Setting: Flareless Extinction");
        }
        if(Settings.isFeatherlessMode()) {
            startingNodes.add("Setting: Featherless");
        }

        if(Settings.isFools2021Mode()) {
            startingNodes.add("Setting: Fools2021");
        } else {
            startingNodes.add("Setting: Not Fools2021");
        }

        startingNodes.add("Setting: " + Settings.getCurrentBossCount() + " Bosses");

        if(!Settings.getEnabledGlitches().contains("Raindrop")) {
            startingNodes.add("Setting: No Raindrop");
        }
        if(Settings.isRandomizeTransitionGates()) {
            startingNodes.add("Setting: Random Transitions");
        }
        else {
            startingNodes.add("Setting: Nonrandom Transitions");
        }
        if(!LocationCoordinateMapper.isSurfaceStart() && Settings.getCurrentStartingLocation() != 23 && Settings.getCurrentStartingLocation() != 24) {
            startingNodes.add("Setting: Alternate Start");
        }
        startingNodes.add(LocationCoordinateMapper.isFrontsideStart() ? "Setting: Frontside Start" : "Setting: Backside Start");
        startingNodes.add(Settings.isRandomizeBosses() ? "Setting: Abnormal Boss" : "Setting: Normal Boss");
        startingNodes.add(Settings.isAlternateMotherAnkh() ? "Setting: Alternate Mother" : "Setting: Standard Mother");
        startingNodes.add(Settings.isBossSpecificAnkhJewels() ? "Setting: Fixed Jewels" : "Setting: Variable Jewels");

        if(Settings.isSubweaponOnlyLogic() || isSubweaponOnly()) {
            startingNodes.add("Setting: Subweapon Only");
        }
        return startingNodes;
    }

    private static boolean isSubweaponOnly() {
        if(DataFromFile.MAIN_WEAPONS.contains(Settings.getCurrentStartingWeapon())) {
            return false;
        }

        List<String> availableMainWeapons = new ArrayList<>(DataFromFile.MAIN_WEAPONS);
        availableMainWeapons.remove("Whip");
        availableMainWeapons.removeAll(Settings.getCurrentRemovedItems());
        availableMainWeapons.removeAll(Settings.getRemovedItems());
        return availableMainWeapons.isEmpty();
    }

    private void testPot(RcdData rcdData) {
        Screen goddessRemovedPotScreen = rcdData.getScreen(13, 7, 2);
        GameObject towerOfTheGoddessRemovedPot = new GameObject(goddessRemovedPotScreen);
        towerOfTheGoddessRemovedPot.setId((short)0x00);
        towerOfTheGoddessRemovedPot.setX(1500);
        towerOfTheGoddessRemovedPot.setY(400);

        towerOfTheGoddessRemovedPot.getArgs().add((short)0);
        towerOfTheGoddessRemovedPot.getArgs().add((short)0);
        towerOfTheGoddessRemovedPot.getArgs().add((short)-1);
        towerOfTheGoddessRemovedPot.getArgs().add((short)1);
        towerOfTheGoddessRemovedPot.getArgs().add((short)13);
        towerOfTheGoddessRemovedPot.getArgs().add((short)105);
        towerOfTheGoddessRemovedPot.getArgs().add((short)35);
        towerOfTheGoddessRemovedPot.getArgs().add((short)17);
        towerOfTheGoddessRemovedPot.getArgs().add((short)0);

        Screen moonlightRemovedPotScreen = rcdData.getScreen(12, 1, 0);
        GameObject templeOfMoonlightRemovedPot = new GameObject(moonlightRemovedPotScreen);
        templeOfMoonlightRemovedPot.setId((short)0x00);
        templeOfMoonlightRemovedPot.setX(540);
        templeOfMoonlightRemovedPot.setY(240);

        templeOfMoonlightRemovedPot.getArgs().add((short)0);
        templeOfMoonlightRemovedPot.getArgs().add((short)0);
        templeOfMoonlightRemovedPot.getArgs().add((short)-1);
        templeOfMoonlightRemovedPot.getArgs().add((short)1);
        templeOfMoonlightRemovedPot.getArgs().add((short)12);
        templeOfMoonlightRemovedPot.getArgs().add((short)105);
        templeOfMoonlightRemovedPot.getArgs().add((short)35);
        templeOfMoonlightRemovedPot.getArgs().add((short)17);
        templeOfMoonlightRemovedPot.getArgs().add((short)0);

        Assert.assertTrue(containsObject(goddessRemovedPotScreen, towerOfTheGoddessRemovedPot));
        Assert.assertTrue(containsObject(moonlightRemovedPotScreen, templeOfMoonlightRemovedPot));
    }

    private boolean containsObject(ObjectContainer objectContainer, GameObject gameObject) {
        for(GameObject object : objectContainer.getObjects()) {
            if(sameObject(object, gameObject)) {
                return true;
            }
        }
        return false;
    }

    private boolean sameObject(GameObject obj1, GameObject obj2) {
        if(obj1 == null) {
            return obj2 == null;
        }
        if(obj2 == null) {
            return false;
        }
        if(obj1.getId() != obj2.getId()) {
            return false;
        }
        if(obj1.getX() != obj2.getX()) {
            return false;
        }
        if(obj1.getY() != obj2.getY()) {
            return false;
        }
        if(obj1.getArgs().size() != obj2.getArgs().size()) {
            return false;
        }
        for(int i = 0; i < obj1.getArgs().size(); i++) {
            if(!obj1.getArgs().get(i).equals(obj2.getArgs().get(i))) {
                return false;
            }
        }
        if(obj1.getTestByteOperations().size() != obj2.getTestByteOperations().size()) {
            return false;
        }
        for(int i = 0; i < obj1.getTestByteOperations().size(); i++) {
            TestByteOperation obj1Test = obj1.getTestByteOperations().get(i);
            if(!containsTest(obj2, obj1Test)) {
                return false;
            }
        }
        for(int i = 0; i < obj1.getWriteByteOperations().size(); i++) {
            WriteByteOperation obj1Update = obj1.getWriteByteOperations().get(i);
            if(!containsUpdate(obj2, obj1Update)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsTest(GameObject gameObject, TestByteOperation match) {
        for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
            if(testByteOperation.getIndex() == match.getIndex()
                    && testByteOperation.getOp().equals(match.getOp())
                    && testByteOperation.getValue() == match.getValue()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsUpdate(GameObject gameObject, WriteByteOperation match) {
        for(WriteByteOperation writeByteOperation : gameObject.getWriteByteOperations()) {
            if(writeByteOperation.getIndex() == match.getIndex()
                    && writeByteOperation.getOp().equals(match.getOp())
                    && writeByteOperation.getValue() == match.getValue()) {
                return true;
            }
        }
        return false;
    }
}
