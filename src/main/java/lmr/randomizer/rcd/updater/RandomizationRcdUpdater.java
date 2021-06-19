package lmr.randomizer.rcd.updater;

import lmr.randomizer.*;
import lmr.randomizer.randomization.*;
import lmr.randomizer.randomization.data.*;
import lmr.randomizer.update.AddObject;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.rcd.RcdFileData;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.*;
import lmr.randomizer.util.*;

import java.util.*;

public class RandomizationRcdUpdater extends RcdUpdater {
    private FlagManager flagManager;
    private ItemRandomizer itemRandomizer;
    private ShopRandomizer shopRandomizer;
    private SealRandomizer sealRandomizer;
    private NpcRandomizer npcRandomizer;
    private TransitionGateRandomizer transitionGateRandomizer;
    private BacksideDoorRandomizer backsideDoorRandomizer;

    public Map<GameObjectId, List<GameObject>> mapOfChestIdentifyingInfoToGameObject = new HashMap<>();

    private Map<String, List<GameObject>> mapOfDoorNameToBacksideDoor = new HashMap<>();
    private Map<String, List<GameObject>> mapOfGateNameToTransitionGate = new HashMap<>();
    private Map<String, List<GameObject>> mapOfSealNodeToSealObjects = new HashMap<>();
    private Map<String, ConversationDoor> mapOfNpcLocationToObject = new HashMap<>();
    private Map<Integer, List<GameObject>> mapOfShopBlockToShopObjects = new HashMap<>();

    private List<GameObject> enemyObjects = new ArrayList<>();
    private List<GameObject> edenDaises = new ArrayList<>();

    public RandomizationRcdUpdater(RcdFileData rcdFileData, DatFileData datFileData, FlagManager flagManager,
                                   ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer,
                                   NpcRandomizer npcRandomizer, SealRandomizer sealRandomizer,
                                   TransitionGateRandomizer transitionGateRandomizer,
                                   BacksideDoorRandomizer backsideDoorRandomizer) {
        super(rcdFileData, datFileData);
        this.flagManager = flagManager;
        this.itemRandomizer = itemRandomizer;
        this.shopRandomizer = shopRandomizer;
        this.sealRandomizer = sealRandomizer;
        this.npcRandomizer = npcRandomizer;
        this.transitionGateRandomizer = transitionGateRandomizer;
        this.backsideDoorRandomizer = backsideDoorRandomizer;
    }

    @Override
    public void doShuffleUpdates(Random random) {
        updateItems(random);
        updateNpcDoors();
        updateSubweaponPot();
//        FileUtils.logFlush("Updated subweapon pot data");

        if(Settings.isFools2021Mode()){
            updateEdenDaises(random);
//            FileUtils.logFlush("Updated puzzle flags");
        }
        if(Settings.isRandomizeEnemies()) {
            updateEnemies(random);
//            FileUtils.logFlush("Updated enemy data");
        }
        if(Settings.isRandomizeSeals()) {
            updateSeals();
//            FileUtils.logFlush("Updated seal data");
        }
        if(Settings.isRandomizeTransitionGates()) {
            updateTransitionGates();
//            FileUtils.logFlush("Updated transition gate data");
        }
        if(Settings.isRandomizeBacksideDoors()) {
            updateBacksideDoors();
//            FileUtils.logFlush("Updated backside door data");
        }
        addStartingShop();
    }

    private void addStartingShop() {
        Screen startingScreen = rcdFileData.getScreen(LocationCoordinateMapper.getStartingZone(),
                LocationCoordinateMapper.getStartingRoom(), LocationCoordinateMapper.getStartingScreen());
        ShopInventory shopInventory = shopRandomizer.getShopInventory(DataFromFile.CUSTOM_SHOP_NAME);
        AddStartingShop.addStartingShop(startingScreen, shopInventory,
                getCustomBlockIndex(CustomBlockEnum.DefaultShopBlock),
                getCustomBlockIndex(getCustomBlockEnumForTransformedShop(null)));
    }

    private void updateItems(Random random) {
        LocationContentsData locationContentsData;
        Map<String, GameObjectId> nameToDataMap = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData();

        String newContents;
        for(String itemLocation : itemRandomizer.getItemLocations()) {
            newContents = itemRandomizer.getNewContents(itemLocation);
            GameObjectId itemLocationData = nameToDataMap.get(itemLocation);
            List<GameObject> objectsToModify = mapOfChestIdentifyingInfoToGameObject.get(itemLocationData);
            if(objectsToModify == null) {
                if(!AddObject.addSpecialItemObjects(itemLocation, newContents)) {
                    FileUtils.log("Unable to find objects related to " + itemLocation);
                }
                continue;
            }
            GameObjectId newContentsData = nameToDataMap.get(newContents);

            locationContentsData = new LocationContentsData(itemLocation, newContents);
            locationContentsData.setCoinChest(itemRandomizer.isCoinChest(newContents));
            locationContentsData.setTrap(itemRandomizer.isTrapItem(newContents));
            locationContentsData.setExplodingChest(itemRandomizer.isExplodingChest(newContents));
            locationContentsData.setRemovedItem(itemRandomizer.isRemovedItem(newContents));
            locationContentsData.setLocationWorldFlag((short)itemLocationData.getWorldFlag());
            if(locationContentsData.isRemovedItem()) {
                locationContentsData.setItemInventoryArg(newContentsData.getInventoryArg());
                locationContentsData.setItemWorldFlag(flagManager.getNewWorldFlag(itemLocationData.getWorldFlag()));
                locationContentsData.setNewWorldFlag(flagManager.getNewWorldFlag(itemLocationData.getWorldFlag()));
            }
            else if(locationContentsData.isTrap()) {
                locationContentsData.setItemInventoryArg(DropType.NOTHING.getValue());
                locationContentsData.setItemWorldFlag(newContentsData.getWorldFlag());
                locationContentsData.setNewWorldFlag(newContentsData.getWorldFlag());
                locationContentsData.setCustomItemGraphic(itemRandomizer.getGraphic(itemLocation));
            }
            else {
                locationContentsData.setItemInventoryArg(newContentsData.getInventoryArg());
                locationContentsData.setItemWorldFlag(newContentsData.getWorldFlag());

                Short itemGraphic = itemRandomizer.getGraphic(itemLocation);
                if(itemGraphic == null) {
                    locationContentsData.setNewWorldFlag(newContentsData.getWorldFlag());
                }
                else {
                    locationContentsData.setNewWorldFlag(flagManager.getNewWorldFlag(itemLocationData.getWorldFlag()));
                    locationContentsData.setCustomItemGraphic(itemGraphic);
                }
            }

            updateLocationContents(objectsToModify, locationContentsData, random);
        }
    }

    private void updateLocationContents(List<GameObject> objectsToModify, LocationContentsData locationContentsData, Random random) {
        for(GameObject objectToModify : objectsToModify) {
            if(objectToModify.getId() == ObjectIdConstants.Chest) {
                updateChestContents((Chest)objectToModify, locationContentsData, random);
                AddObject.addSpecialItemObjects(objectToModify.getObjectContainer(), locationContentsData.getItemName());
            }
            else if(objectToModify.getId() == ObjectIdConstants.FloatingItem) {
                // Note: Floating maps don't get removed.
                updateFloatingItemContents((FloatingItem)objectToModify, locationContentsData);
                AddObject.addSpecialItemObjects(objectToModify.getObjectContainer(), locationContentsData.getItemName());
            }
            else if(objectToModify.getId() == ObjectIdConstants.ItemGive) {
                updateItemGiveContents((ItemGive)objectToModify, locationContentsData);
                AddObject.addSpecialItemObjects(objectToModify.getObjectContainer(), locationContentsData.getItemName());
            }
            else if(objectToModify.getId() == ObjectIdConstants.SnapshotsScan) {
                updateSnapshotsScanContents((SnapshotsScan)objectToModify, locationContentsData);
                AddObject.addSpecialItemObjects(objectToModify.getObjectContainer(), locationContentsData.getItemName());
            }
            else {
                updateRelatedObject(objectToModify, locationContentsData);
                if(objectToModify.getId() == ObjectIdConstants.ConversationDoor) {
                    short blockRef = objectToModify.getArgs().get(4);
                    if(blockRef == BlockConstants.Master_FormerMekuriMaster_Mekuri
                            || blockRef == BlockConstants.Master_MrSlushfund_Pepper
                            || blockRef == BlockConstants.Master_MrSlushfund_Anchor
                            || blockRef == BlockConstants.Master_PriestAlest) {
                        AddObject.addSpecialItemObjects(objectToModify.getObjectContainer(), locationContentsData.getItemName());
                    }
                }
            }
        }
    }

    private void updateChestContents(Chest chest, LocationContentsData locationContentsData, Random random) {
        if(locationContentsData.isCoinChest()) {
            chest.setDrops(DropType.COINS.getValue(), locationContentsData.getItemInventoryArg()); // Re-purposing inventory arg to track coin amount
            updateChestGraphic(chest, false, random);

            updateWorldFlagTests(chest, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag(), false);
            chest.setEmptyCheck(new WriteByteOperation(locationContentsData.getNewWorldFlag(), ByteOp.ASSIGN_FLAG, 2));
            chest.setUpdateWhenOpened(new WriteByteOperation(locationContentsData.getNewWorldFlag(), ByteOp.ASSIGN_FLAG, 2));
            chest.setUpdateWhenCollected(new WriteByteOperation(FlagConstants.TOTAL_COIN_CHESTS, ByteOp.ADD_FLAG, 1)); // Track coin chest count
        }
        else if(locationContentsData.isExplodingChest()) {
            // Exploding trap chest
            chest.setDrops(DropType.NOTHING.getValue(), 1); // Quantity is irrelevant
            updateChestGraphic(chest, true, random);

            updateWorldFlagTests(chest, locationContentsData.getLocationWorldFlag(), locationContentsData.getItemWorldFlag(), false);
            chest.setEmptyCheck(new WriteByteOperation(locationContentsData.getItemWorldFlag(), ByteOp.ASSIGN_FLAG, 2));
            chest.setUpdateWhenOpened(new WriteByteOperation(locationContentsData.getItemWorldFlag(), ByteOp.ASSIGN_FLAG, 1));
            chest.setUpdateWhenCollected(new WriteByteOperation(locationContentsData.getItemWorldFlag(), ByteOp.ASSIGN_FLAG, 1));

            if(Settings.isFools2020Mode()) {
                AddObject.addFoolsExplosion(chest.getObjectContainer(), chest.getX(), chest.getY(), locationContentsData.getItemWorldFlag());
            }
            else {
                AddObject.addExplosion(chest.getObjectContainer(), chest.getX(), chest.getY(), locationContentsData.getItemWorldFlag(), 60, true);
            }
            AddObject.addNoItemSoundEffect(chest.getObjectContainer(), locationContentsData.getItemWorldFlag(), (int)FlagConstants.SCREEN_FLAG_2E);
        }
        else if(locationContentsData.isTrap()) {
            // Non-exploding trap chest
            if(locationContentsData.getCustomItemGraphic() != null) {
                chest.setDrops(locationContentsData.getCustomItemGraphic() + 11, 0);
            }
            else {
                chest.setDrops(DropType.NOTHING.getValue(), 1); // Quantity is irrelevant
            }
            updateChestGraphic(chest, true, random);

            updateWorldFlagTests(chest, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag(), false);
            chest.setEmptyCheck(new WriteByteOperation(locationContentsData.getNewWorldFlag(), ByteOp.ASSIGN_FLAG, 1));
            chest.setUpdateWhenOpened(new WriteByteOperation(locationContentsData.getNewWorldFlag(), ByteOp.ASSIGN_FLAG, 1));
            chest.setUpdateWhenCollected(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2E, ByteOp.ASSIGN_FLAG, 1));

            AddObject.addTrapItemBats(chest, FlagConstants.SCREEN_FLAG_2E);
            AddObject.addNoItemSoundEffect(chest.getObjectContainer(), locationContentsData.getNewWorldFlag(), (int)FlagConstants.SCREEN_FLAG_2E);
        }
        else if(locationContentsData.isRemovedItem()) {
            if(random.nextBoolean()) {
                chest.setDrops(DropType.COINS.getValue(), 10);
            }
            else {
                chest.setDrops(DropType.WEIGHTS.getValue(), 1); // The game won't allow multiple weights, so just give 1
            }
            updateChestGraphic(chest, true, random);
            updateChestFlags(chest, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag(), 2);
        }
        else if(locationContentsData.getCustomItemGraphic() != null) {
            chest.setDrops(locationContentsData.getCustomItemGraphic() + 11, 0);
            updateChestGraphic(chest, true, random);
            updateChestFlags(chest, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag(), 1);

            if(chest.getUnlockedCheck().getIndex() != 0x032) {
                AddObject.addItemGive(chest, locationContentsData.getItemInventoryArg(), locationContentsData.getNewWorldFlag(), locationContentsData.getItemWorldFlag());
            }
        }
        else {
            // Actual items
            chest.setDrops(locationContentsData.getItemInventoryArg() + 11, 1);
            updateChestGraphic(chest, true, random);
            updateChestFlags(chest, locationContentsData.getLocationWorldFlag(), locationContentsData.getItemWorldFlag(), 1);
        }

        chest.setCursed(Settings.getCurrentCursedChests().contains(locationContentsData.getLocationName()));
        chest.setPercentCurseDamage(50);
    }

    private void updateFloatingItemContents(FloatingItem floatingItem, LocationContentsData locationContentsData) {
        if(locationContentsData.isRemovedItem()) {
            floatingItem.setInventoryWord(locationContentsData.getItemInventoryArg());
            floatingItem.setRealItem(false);

            updateWorldFlagTests(floatingItem, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag(), false);
            updateWorldFlagUpdates(floatingItem, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag());

            floatingItem.addUpdates(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2B, ByteOp.ASSIGN_FLAG, 1));

            if(Settings.isFools2020Mode()) {
                AddObject.addTrapItemBats(floatingItem, FlagConstants.SCREEN_FLAG_2B);
                AddObject.addNoItemSoundEffect(floatingItem.getObjectContainer(), locationContentsData.getNewWorldFlag(), (int)FlagConstants.SCREEN_FLAG_2B);
            }
            else {
                AddObject.addItemGive(floatingItem, ItemConstants.WEIGHT).addTests(
                        new TestByteOperation(locationContentsData.getNewWorldFlag(), ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(FlagConstants.SCREEN_FLAG_2B, ByteOp.FLAG_EQUALS, 1));
            }
        }
        else if(locationContentsData.isTrap()) {
            // Non-exploding trap chest
            floatingItem.setInventoryWord(locationContentsData.getCustomItemGraphic());
            floatingItem.setRealItem(false);

            updateWorldFlagTests(floatingItem, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag(), false);
            updateWorldFlagUpdates(floatingItem, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag());

            if(floatingItem.getWriteByteOperations().size() > 1) {
                floatingItem.getWriteByteOperations().set(1, new WriteByteOperation(FlagConstants.SCREEN_FLAG_2B, ByteOp.ASSIGN_FLAG, 1));
            }
            else {
                floatingItem.addUpdates(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2B, ByteOp.ASSIGN_FLAG, 1));
            }

            AddObject.addTrapItemBats(floatingItem, FlagConstants.SCREEN_FLAG_2B);
            AddObject.addNoItemSoundEffect(floatingItem.getObjectContainer(), locationContentsData.getNewWorldFlag(), (int)FlagConstants.SCREEN_FLAG_2B);
        }
        else if(locationContentsData.getCustomItemGraphic() != null) {
            floatingItem.setInventoryWord(locationContentsData.getCustomItemGraphic());
            floatingItem.setRealItem(false);

            updateWorldFlagTests(floatingItem, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag(), false);
            updateWorldFlagUpdates(floatingItem, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag());

            AddObject.addItemGive(floatingItem, locationContentsData.getItemInventoryArg(), locationContentsData.getNewWorldFlag(), locationContentsData.getItemWorldFlag());
        }
        else {
            floatingItem.setInventoryWord(locationContentsData.getItemInventoryArg());
            floatingItem.setRealItem(true);

            updateWorldFlagTests(floatingItem, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag(), false);
            updateWorldFlagUpdates(floatingItem, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag());
        }
    }

    private void updateItemGiveContents(ItemGive itemGive, LocationContentsData locationContentsData) {
        itemGive.setInventoryWord(locationContentsData.isRemovedItem() ? ItemConstants.WEIGHT : locationContentsData.getItemInventoryArg());
        updateWorldFlagTests(itemGive, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag(), false);
        updateWorldFlagUpdates(itemGive, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag());
    }

    private void updateSnapshotsScanContents(SnapshotsScan snapshotsScan, LocationContentsData locationContentsData) {
        if(snapshotsScan.getInventoryWord() != DropType.NOTHING.getValue()) {
            snapshotsScan.setInventoryWord(locationContentsData.getItemInventoryArg());
        }

        updateWorldFlagTests(snapshotsScan, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag(), true);
        for(int i = 0; i < snapshotsScan.getWriteByteOperations().size(); i++) {
            WriteByteOperation flagUpdate = snapshotsScan.getWriteByteOperations().get(i);
            if(flagUpdate.getIndex() == locationContentsData.getLocationWorldFlag()) {
                flagUpdate.setIndex(locationContentsData.getNewWorldFlag());
                flagUpdate.setValue(2);
            }
        }
    }

    private void updateRelatedObject(GameObject gameObject, LocationContentsData locationContentsData) {
        updateWorldFlagTests(gameObject, locationContentsData.getLocationWorldFlag(), locationContentsData.getNewWorldFlag(), false);
        for(WriteByteOperation flagUpdate : gameObject.getWriteByteOperations()) {
            if(flagUpdate.getIndex() == locationContentsData.getLocationWorldFlag()) {
                flagUpdate.setIndex(locationContentsData.getNewWorldFlag());
            }
        }
    }

    private void updateChestGraphic(Chest chest, boolean isItem, Random random) {
        if(Settings.isRandomizeGraphics()) {
            chest.setChestGraphic(random.nextBoolean() ? Chest.COIN_CHEST : Chest.ITEM_CHEST);
        }
        else if(isItem) {
            chest.setChestGraphic(Settings.isCoinChestGraphics() ? Chest.COIN_CHEST : Chest.ITEM_CHEST);
        }
        else {
            chest.setChestGraphic(Chest.COIN_CHEST);
        }
    }

    private void updateChestFlags(Chest chest, int originalWorldFlag, int newWorldFlag, int newFlagValueWhenOpened) {
        updateWorldFlagTests(chest, originalWorldFlag, newWorldFlag, false);

        chest.setEmptyCheck(new WriteByteOperation(newWorldFlag, ByteOp.ASSIGN_FLAG, 2));
        chest.setUpdateWhenOpened(new WriteByteOperation(newWorldFlag, ByteOp.ASSIGN_FLAG, newFlagValueWhenOpened));
        chest.setUpdateWhenCollected(new WriteByteOperation(newWorldFlag, ByteOp.ASSIGN_FLAG, 2));
    }

    private void updateWorldFlagTests(GameObject gameObject, int originalWorldFlag, int newWorldFlag, boolean updateFlagValue) {
        for(TestByteOperation flagTest : gameObject.getTestByteOperations()) {
            if(flagTest.getIndex() == originalWorldFlag) {
                flagTest.setIndex(newWorldFlag);
                if(updateFlagValue && flagTest.getValue() == 1) {
                    flagTest.setValue(2);
                }
            }
        }
    }

    private void updateWorldFlagUpdates(GameObject gameObject, int originalWorldFlag, int newWorldFlag) {
        int lastWorldFlagUpdateIndex = -1;
        for(int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
            WriteByteOperation flagUpdate = gameObject.getWriteByteOperations().get(i);
            if(flagUpdate.getIndex() == originalWorldFlag) {
                lastWorldFlagUpdateIndex = i;
                flagUpdate.setIndex(newWorldFlag);
            }
        }

        if(lastWorldFlagUpdateIndex != -1) {
            WriteByteOperation flagUpdate = gameObject.getWriteByteOperations().get(lastWorldFlagUpdateIndex);
            if(flagUpdate.getValue() != 2) {
                flagUpdate.setValue(2);
            }
        }
    }

    private void updateEnemies(Random random) {
        EnemyRandomizer enemyRandomizer = new EnemyRandomizer(random);
        for(GameObject enemy : enemyObjects) {
            enemyRandomizer.randomizeEnemy(enemy);
        }
    }

    private void updateNpcDoors() {
        ConversationDoor doorToUpdate;
        for (String npcDoorLocation : npcRandomizer.getNpcDoors()) {
            doorToUpdate = mapOfNpcLocationToObject.get(npcDoorLocation);
            updateConversationDoorContents(doorToUpdate, npcDoorLocation, npcRandomizer.getNpc(npcDoorLocation));
        }
    }

    private void updateTransitionGates() {
        for(TransitionGateData transitionGateData : transitionGateRandomizer.getTransitionGateData()) {
            List<GameObject> objectsToModify = mapOfGateNameToTransitionGate.get(transitionGateData.getGateName());
            if(objectsToModify != null) {
                boolean firstObject = true;
                boolean updateFirstObject = false; // This is to address a problem where firstObject was updated too soon and we failed to check independent if cases - https://github.com/thezerothcat/LaMulanaRandomizer/issues/100
                for (GameObject gameObject : objectsToModify) {
                    TransitionGateUpdates.replaceTransitionGateArgs(gameObject, transitionGateData.getGateDestination()); // First update the transitions so we can make a correct copy of the gate if needed.
                    if(transitionGateData.getGateDestination().startsWith("Transition: Shrine")
                            && !"Transition: Shrine D3".equals(transitionGateData.getGateDestination())
                            && gameObject.getTestByteOperations().get(0).getValue() != 1) {
                        // Copy and add true shrine gate before updating flags, since flag update will complicate escape gate vs not-escape gate.
                        AddObject.addTrueShrineGate(gameObject);
                    }
                    TransitionGateUpdates.replaceTransitionGateFlags(gameObject, transitionGateData.getGateName(), transitionGateData.getGateDestination()); // Update flags on the gate, as needed.

                    if(!"Transition: Sun R2".equals(transitionGateData.getGateName()) && !"Transition: Extinction L2".equals(transitionGateData.getGateName())) {
                        // Both of these screen edges have more than one door; the screen transition is based on Sun R1 and Extinction L1, respectively
                        TransitionGateUpdates.updateScreenTransition(gameObject, transitionGateData.getGateDestination());
                    }
                    if(firstObject && "Transition: Illusion R2".equals(transitionGateData.getGateName())) {
                        // Block leading out of Illusion
                        AddObject.addIllusionFruitBlockHorizontal(gameObject, true);
                        updateFirstObject = true;
                    }
                    if(firstObject
                            && ("Transition: Illusion R1".equals(transitionGateData.getGateDestination())
                            || "Transition: Illusion R2".equals(transitionGateData.getGateDestination()))) {
                        // Block leading into Illusion
                        AddObject.addIllusionFruitBlockHorizontal(gameObject, "Transition: Illusion R2".equals(transitionGateData.getGateDestination()));
                        updateFirstObject = true;
                    }
                    if(firstObject && "Transition: Illusion D1".equals(transitionGateData.getGateDestination())) {
                        // Block leading into Illusion
                        AddObject.addIllusionFruitBlockVertical(gameObject);
                        updateFirstObject = true;
                    }
                    if(firstObject && Settings.isFools2021Mode() && "Transition: Guidance L1".equals(transitionGateData.getGateDestination())) {
                        // Add timer for fools' rando 2021 to ensure a flag sequence behaves as intended.
                        // Ensure the process of falling blocks triggered by Pepper is reset if unfinished.
                        AddObject.addFramesTimer(gameObject.getObjectContainer(), 0,
                                Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_GT, 0),
                                        new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_LT, 5)),
                                Arrays.asList(new WriteByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.ASSIGN_FLAG, 1)));
                        updateFirstObject = true;
                    }
                    if(firstObject && "Transition: Sun L1".equals(transitionGateData.getGateDestination())) {
                        // Timer on the other side of the gate leading into Buer's room, to mark that the ceiling can now be broken (not allowed during the boss fight).
                        AddObject.addIsisRoomCeilingTimer(gameObject.getObjectContainer());
                        updateFirstObject = true;
                    }
                    if(firstObject && "Transition: Goddess L2".equals(transitionGateData.getGateDestination())) {
                        // Timer on the other side of the gate leading into the lower of the left Goddess exits, to mark that the statue should be removed.
                        AddObject.addGoddessStatueLemezaDetector(gameObject);
                        updateFirstObject = true;
                    }
                    if(firstObject && "Transition: Goddess D1".equals(transitionGateData.getGateDestination())) {
                        // Timer on the other side of the gate leading into the shield statue's room in Goddess, marking that the shield has been thrown and the statue should not exist upon returning.
                        AddObject.addGoddessShieldTimer(gameObject.getObjectContainer());
                        updateFirstObject = true;
                    }
                    if(firstObject && "Transition: Birth R1".equals(transitionGateData.getGateDestination())) {
                        // Block leading into Skanda's room.
                        AddObject.addSkandaBlock(gameObject);
                        updateFirstObject = true;
                    }
                    if(firstObject && transitionGateData.getGateDestination().contains("Transition: Twin ") && !transitionGateData.getGateName().equals("Transition: Twin U2")) {
                        // For every transition gate leading into Twin Labyrinths, the poison timer (but not the puzzle itself) will be reset,
                        // UNLESS this is a transition from the screen with the first dais into some other screen of Twin Labyrinths.
                        AddObject.addTwinLabsPoisonTimerRemoval(gameObject.getObjectContainer(), false);
                        updateFirstObject = true;
                    }
                    if(firstObject && transitionGateData.isEndlessL1Reverse()) {
                        // Detector on the other side of the gate coming out of Endless corridor, to open the Map chest in Endless Corridor.
                        AddObject.addEndlessCorridorLeftExitLemezaDetector(gameObject);
                        updateFirstObject = true;
                    }
                    if(firstObject && !LocationCoordinateMapper.isSurfaceStart() && "Transition: Surface R1".equals(transitionGateData.getGateName())) {
                        // Transition gate leading from Surface into some other location. If non-random start, the player may be trapped in the un-opened entryway
                        // and forcibly raindropped somewhere unintended if a warp is not provided before the passageway has been opened on the Surface side.
                        GameObject warp = AddObject.addWarp((Screen)gameObject.getObjectContainer(), 1220, 340, 4, 7, 0, 0, 0, 20, 312);
                        TransitionGateUpdates.replaceTransitionGateArgs(warp, transitionGateData.getGateDestination()); // First update the transitions so we can make a correct copy of the gate if needed.

                        TestByteOperation warpTest = new TestByteOperation();
                        warpTest.setIndex(FlagConstants.SURFACE_RUINS_FRONT_DOOR_OPEN);
                        warpTest.setValue((byte) 0);
                        warpTest.setOp(ByteOp.FLAG_EQUALS);
                        warp.getTestByteOperations().add(warpTest);
                        updateFirstObject = true;
                    }

                    if(updateFirstObject) {
                        firstObject = false;
                    }
                }
            }

            if("Transition: Dimensional D1".equals(transitionGateData.getGateName())) {
                TransitionGateUpdates.updateScreenTransition(rcdFileData.getScreen(17, 0, 1), transitionGateData.getGateDestination());
            }
        }
    }

    public void updateBacksideDoors() {
        for(BacksideDoorData backsideDoorData : backsideDoorRandomizer.getBacksideDoorData()) {
            List<GameObject> objectsToModify = mapOfDoorNameToBacksideDoor.get(backsideDoorData.getDoorName());
            if(objectsToModify == null) {
                if(Settings.isRandomizeNonBossDoors()) {
                    if("Door: F8".equals(backsideDoorData.getDoorDestination())) {
                        AddObject.addGrailToggle(rcdFileData.getScreen(8, 0, 1), true);
                    }
                }
            }
            else {
                if(Settings.isRandomizeNonBossDoors()) {
                    BacksideDoorUpdates.updateBacksideDoorV2(backsideDoorData, objectsToModify);
                }
                else if(!backsideDoorData.getDoorName().endsWith("8") && !backsideDoorData.getDoorName().endsWith("9")) {
                    BacksideDoorUpdates.updateBacksideDoorV1(backsideDoorData, objectsToModify);
                }
            }
        }
    }

    public void updateSubweaponPot() {
        for(GameObject pot : rcdFileData.getScreen(10, 7, 2).getObjectsById(ObjectIdConstants.Pot)) {
            if(pot.getArgs().get(0) == 3) {
                pot.getArgs().set(0, itemRandomizer.getSubweaponPotContents().getValue());
            }
        }
    }

    public void updateConversationDoorContents(ConversationDoor conversationDoor, String npcDoorLocation, String npcAssigned) {
        // Args 0-2 and 5-6 seem to just be 0 for the conversations referenced so far
        if(conversationDoor != null) {
            conversationDoor.setDoorType(ConversationDoor.getDoorType(npcAssigned));
            conversationDoor.setBlockNumber(ConversationDoor.getConversationArg(npcAssigned));

            conversationDoor.getTestByteOperations().clear();
            conversationDoor.getWriteByteOperations().clear();

            updateConversationDoorByLocation(conversationDoor, npcDoorLocation);
            updateConversationDoorByNpcAssigned(conversationDoor, npcAssigned);

            if(ConversationDoor.getDoorType(npcAssigned) == ConversationDoor.Shop) {
                ShopInventory shopInventory = shopRandomizer.getShopInventory(npcRandomizer.getShopName(npcAssigned));
                Short customBlockIndex = shopInventory == null ? null : getCustomBlockIndex(
                        getCustomBlockEnumForTransformedShop(shopInventory.getNpcName()));
                AddObject.addShopObjects(conversationDoor, shopInventory, customBlockIndex);
            }
        }
    }

    private CustomBlockEnum getCustomBlockEnumForTransformedShop(String npcName) {
        if(npcName == null) {
            return CustomBlockEnum.TransformedShopBlock_Default;
        }
        return CustomBlockEnum.valueOf("TransformedShopBlock_" + npcName.replaceAll("[ )(-.]", ""));
    }

    private void updateConversationDoorByLocation(ConversationDoor conversationDoor, String npcDoorLocation) {
        // Handle special cases for location of door
        if("NPCL: Sidro".equals(npcDoorLocation) || "NPCL: Modro".equals(npcDoorLocation) || "NPCL: Hiner".equals(npcDoorLocation) || "NPCL: Moger".equals(npcDoorLocation)) {
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.XELPUD_CONVERSATION_INTRO, ByteOp.FLAG_EQUALS, 1)); // Flag for having talked to Xelpud to open Surface tents
        }
        if("NPCL: Mr. Fishman (Original)".equals(npcDoorLocation)) {
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.FISH_SHOP_UNLOCKS, ByteOp.FLAG_GTEQ, 2));
        }
        if("NPCL: Mr. Fishman (Alt)".equals(npcDoorLocation)) {
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.FISH_SHOP_UNLOCKS, ByteOp.FLAG_EQUALS, 3));
        }
        if("NPCL: Yiear Kungfu".equals(npcDoorLocation)) {
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.BIG_BROTHER_UNLOCKED, ByteOp.FLAG_GTEQ, 1));
        }
        if("NPCL: Mud Man Qubert".equals(npcDoorLocation)) {
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.COG_MUDMEN_STATE, ByteOp.FLAG_EQUALS, 4));
        }
        if("NPCL: Priest Triton".equals(npcDoorLocation)) {
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.PALENQUE_STATE, ByteOp.FLAG_NOT_EQUAL, 2)); // Test whether the Palenque fight is active, not whether the ankh is present.
        }
        if("NPCL: Naramura".equals(npcDoorLocation) || "NPCL: duplex".equals(npcDoorLocation) || "NPCL: Samieru".equals(npcDoorLocation)) {
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.DEV_ROOM_COMBO, ByteOp.FLAG_EQUALS, 1)); // Flag automatically set when dev room software combo is activated.
        }
    }

    private void updateConversationDoorByNpcAssigned(ConversationDoor conversationDoor, String npcAssigned) {
        // Handle special cases for door contents
        if("NPC: Yiegah Kungfu".equals(npcAssigned)) {
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.BIG_BROTHER_UNLOCKED, ByteOp.FLAG_NOT_EQUAL, 1));
        }
        if("NPC: Philosopher Giltoriyo".equals(npcAssigned)) {
            ConversationDoorUpdates.addPhilosopherStoneDoor(conversationDoor); // Do this before adding tests, so we can carry over any tests based on the location.
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_PHILOSOPHERS_OCARINA, ByteOp.FLAG_EQUALS, 2));
            conversationDoor.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.GILTORIYO_LADDER, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning Endless Corridor Philosopher ladder
            conversationDoor.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MANTRAS_UNLOCKED, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning MARDUK mantra, changed in randomizer from 0x12b
        }
        if("NPC: Philosopher Alsedana".equals(npcAssigned)) {
            ConversationDoorUpdates.addPhilosopherStoneDoor(conversationDoor); // Do this before adding tests, so we can carry over any tests based on the location.
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_PHILOSOPHERS_OCARINA, ByteOp.FLAG_EQUALS, 2));
            conversationDoor.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.ALSEDANA_LADDER, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning Nuwa's pyramid Philosopher ladder
        }
        if("NPC: Philosopher Samaranta".equals(npcAssigned)) {
            ConversationDoorUpdates.addPhilosopherStoneDoor(conversationDoor); // Do this before adding tests, so we can carry over any tests based on the location.
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_PHILOSOPHERS_OCARINA, ByteOp.FLAG_EQUALS, 2));
            conversationDoor.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SAMARANTA_LADDER, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning Scales of the Heart puzzle room Philosopher ladder
        }
        if("NPC: Philosopher Fobos".equals(npcAssigned)) {
            ConversationDoorUpdates.addFobosDoors(conversationDoor); // Do this before adding tests, so we can carry over any tests based on the location.
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_PHILOSOPHERS_OCARINA, ByteOp.FLAG_EQUALS, 2));
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.MEDICINE_SOLVED, ByteOp.FLAG_EQUALS, 1)); // Medicine puzzle solved
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.FOBOS_LADDER, ByteOp.FLAG_NOT_EQUAL, 0)); // Fobos spoken to
        }
        if("NPC: The Fairy Queen".equals(npcAssigned)) {
            ConversationDoorUpdates.addFairyQueenDoors(conversationDoor); // Do this before adding tests, so we can carry over any tests based on the location.
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.FLAG_LTEQ, 1)); // Fairy Queen conversation progress
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_ISIS_PENDANT, ByteOp.FLAG_EQUALS, 2)); // Isis' Pendant collected
            conversationDoor.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.ASSIGN_FLAG, 2)); // Fairy Queen conversation progress
            conversationDoor.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.FAIRY_POINTS_ACTIVE, ByteOp.ASSIGN_FLAG, 1)); // Fairy points active
        }
        if("NPC: Naramura".equals(npcAssigned)) {
            conversationDoor.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.NARAMURA_SPOKEN, ByteOp.ASSIGN_FLAG, 1)); // Flag indicating Naramura has been spoken to
        }
        if("NPC: duplex".equals(npcAssigned)) {
            conversationDoor.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.DUPLEX_SPOKEN, ByteOp.ASSIGN_FLAG, 1)); // Flag indicating duplex has been spoken to
        }
        if("NPC: Samieru".equals(npcAssigned)) {
            conversationDoor.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SAMIERU_SPOKEN, ByteOp.ASSIGN_FLAG, 1)); // Flag indicating Samieru has been spoken to
        }
    }

    private void updateSeals() {
        short sealNumber;
        for(String sealNode : mapOfSealNodeToSealObjects.keySet()) {
            sealNumber = sealRandomizer.getSealNumber(sealNode);
            List<GameObject> seals = mapOfSealNodeToSealObjects.get(sealNode);
            for (GameObject sealToUpdate : seals) {
                sealToUpdate.getArgs().set(0, sealNumber);
            }
        }
    }

    private void updateEdenDaises(Random random) {
        if (edenDaises.size() == 4) {
            List<GameObject> updatedDaises = new ArrayList<>(edenDaises);
            for (int i = 0; i < 4; i++) {
                GameObject dais = updatedDaises.remove(random.nextInt(updatedDaises.size()));
                if (i == 0) {
                    dais.setX(160);
                    dais.setY(180);
                    for(TestByteOperation testByteOperation : dais.getTestByteOperations()) {
                        if (testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_DANCING_MAN || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_HANDS
                                || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_TRAP || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_FACE) {
                            testByteOperation.setIndex(FlagConstants.MOONLIGHT_SCAN_DANCING_MAN);
                            break;
                        }
                    }
                }
                else if (i == 1) {
                    dais.setX(320);
                    dais.setY(180);
                    for(TestByteOperation testByteOperation : dais.getTestByteOperations()) {
                        if (testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_DANCING_MAN || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_HANDS
                                || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_TRAP || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_FACE) {
                            testByteOperation.setIndex(FlagConstants.MOONLIGHT_SCAN_HANDS);
                            break;
                        }
                    }
                }
                else if (i == 2) {
                    dais.setX(320);
                    dais.setY(340);
                    for(TestByteOperation testByteOperation : dais.getTestByteOperations()) {
                        if (testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_DANCING_MAN || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_HANDS
                                || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_TRAP || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_FACE) {
                            testByteOperation.setIndex(FlagConstants.MOONLIGHT_SCAN_TRAP);
                            break;
                        }
                    }
                }
                else { // if (i == 3)
                    dais.setX(320);
                    dais.setY(260);
                    for(TestByteOperation testByteOperation : dais.getTestByteOperations()) {
                        if (testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_DANCING_MAN || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_HANDS
                                || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_TRAP || testByteOperation.getIndex() == FlagConstants.MOONLIGHT_SCAN_FACE) {
                            testByteOperation.setIndex(FlagConstants.MOONLIGHT_SCAN_FACE);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void trackObject(GameObject gameObject) {
        if (gameObject.getId() == ObjectIdConstants.Enemy_Antlion) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Bat) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Skeleton) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Snouter) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_KodamaRat) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Dais) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == FlagConstants.WF_SOFTWARE_YAGOSTR) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.SOFTWARE_YAGOSTR, FlagConstants.WF_SOFTWARE_YAGOSTR);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == FlagConstants.WF_ANKH_JEWEL_SUN) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.ANKH_JEWEL, FlagConstants.WF_ANKH_JEWEL_SUN);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                if (Settings.isFools2021Mode()) {
                    if(flagTest.getIndex() == FlagConstants.MOONLIGHT_SCAN_DANCING_MAN) {
                        // Eden chest 1
                        edenDaises.add(gameObject);
                    }
                    else if(flagTest.getIndex() == FlagConstants.MOONLIGHT_SCAN_HANDS) {
                        // Eden chest 2
                        edenDaises.add(gameObject);
                    }
                    else if(flagTest.getIndex() == FlagConstants.MOONLIGHT_SCAN_TRAP) {
                        // Eden chest 3
                        edenDaises.add(gameObject);
                    }
                    else if(flagTest.getIndex() == FlagConstants.MOONLIGHT_SCAN_FACE) {
                        // Eden chest 4
                        edenDaises.add(gameObject);
                    }
                }
            }

            if(gameObject.getObjectContainer() instanceof Screen) {
                Screen screen = (Screen) gameObject.getObjectContainer();
                if(Settings.isRandomizeTrapItems()) {
                    if (screen.getZoneIndex() == 11 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 3) {
                        // Graveyard trap chest dais
                        GameObjectId gameObjectId = new GameObjectId(DropType.NOTHING.getValue(), FlagConstants.WF_TRAP_GRAVEYARD);
                        List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        if (objects == null) {
                            mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                            objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        }
                        objects.add(gameObject);
                    }
                }
            }
        }
        else if(gameObject.getId() == ObjectIdConstants.MovingRoomSpawner) {
            for(WriteByteOperation flagUpdate : gameObject.getWriteByteOperations()) {
                if(flagUpdate.getIndex() == FlagConstants.WF_ANKH_JEWEL_MAUSOLEUM) {
                    // Mausoleum Ankh Jewel chest trap
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.ANKH_JEWEL, FlagConstants.WF_ANKH_JEWEL_MAUSOLEUM);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    return;
                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.FlagTimer) {
            // Timer objects
            for (WriteByteOperation flagUpdate : gameObject.getWriteByteOperations()) {
                if(flagUpdate.getIndex() == FlagConstants.HT_UNLOCK_PROGRESS_EARLY) {
                    if(flagUpdate.getValue() == 8) {
                        // Mulbruk swimsuit conversation timer.
                        //OBJECT Type=0xb
                        //TEST:
                        //[0106] == 2
                        //[034c] <= 7
                        //UPDATE:
                        //[034c]  = 8
                        //ARG 0: 0
                        //ARG 1: 0
                        GameObjectId gameObjectId = new GameObjectId(ItemConstants.PROVOCATIVE_BATHING_SUIT, FlagConstants.WF_PROVOCATIVE_BATHING_SUIT);
                        List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        if (objects == null) {
                            mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                            objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        }
                        objects.add(gameObject);
                        return;
                    }
                }
                else if(!Settings.isRandomizeNonBossDoors()) {
                    if(flagUpdate.getIndex() == FlagConstants.AMPHISBAENA_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.AMPHISBAENA_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 0
                                ? "Door: F1" : "Door: B1";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == FlagConstants.SAKIT_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.SAKIT_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 2
                                ? "Door: F2" : "Door: B2";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == FlagConstants.ELLMAC_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.ELLMAC_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 3
                                ? "Door: F3" : "Door: B3";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == FlagConstants.BAHAMUT_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.BAHAMUT_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 5
                                ? "Door: F4" : "Door: B4";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == FlagConstants.VIY_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.VIY_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 1
                                ? "Door: F5" : "Door: B5";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == FlagConstants.PALENQUE_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.PALENQUE_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 6
                                ? "Door: F6" : "Door: B6";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == FlagConstants.BAPHOMET_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.BAPHOMET_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 5
                                ? "Door: F7" : "Door: B7";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                }
            }
            for(int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                TestByteOperation flagTest = gameObject.getTestByteOperations().get(i);
                if(flagTest.getIndex() == FlagConstants.WF_DIARY) {
                    // Timers related to Diary puzzle
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.DIARY, FlagConstants.WF_DIARY);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    return;
                }
                else if(flagTest.getIndex() == FlagConstants.WF_MSX2) {
                    // Timer related to MSX2 shop
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MSX2, FlagConstants.WF_MSX2);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    return;
                }
//                else if(flagTest.getIndex() == FlagConstants.WF_MATERNITY_STATUE && flagTest.getValue() == 1) {
//                    // Timer to track wait time with Woman Statue and give Maternity Statue
//                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MATERNITY_STATUE, FlagConstants.WF_MATERNITY_STATUE);
//                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
//                    if (objects == null) {
//                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
//                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
//                    }
//                    objects.add(gameObject);
//                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.RoomSpawner) {
            for(TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if(flagTest.getIndex() == FlagConstants.WF_SOFTWARE_DEATHV) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.SOFTWARE_DEATHV, FlagConstants.WF_SOFTWARE_DEATHV);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if(objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == FlagConstants.WF_MAP_ILLUSION) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MAP, FlagConstants.WF_MAP_ILLUSION);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if(objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
//                else if(flagTest.getIndex() == FlagConstants.MANTRA_MARDUK && flagTest.getValue() == 2) {
//                    if(gameObject.getObjectContainer() instanceof Screen
//                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 8) {
//                        // MARDUK tablet effect
//                        List<GameObject> objects = mantraTablets.get("MARDUK");
//                        if(objects == null) {
//                            mantraTablets.put("MARDUK", new ArrayList<>());
//                            objects = mantraTablets.get("MARDUK");
//                        }
//                        objects.add(gameObject);
//                        break;
//                    }
//                }
//                else if(flagTest.getIndex() == FlagConstants.MANTRA_SABBAT && flagTest.getValue() == 2) {
//                    if(gameObject.getObjectContainer() instanceof Screen
//                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 7) {
//                        // SABBAT tablet effect
//                        List<GameObject> objects = mantraTablets.get("SABBAT");
//                        if(objects == null) {
//                            mantraTablets.put("SABBAT", new ArrayList<>());
//                            objects = mantraTablets.get("SABBAT");
//                        }
//                        objects.add(gameObject);
//                        break;
//                    }
//                }
//                else if(flagTest.getIndex() == FlagConstants.MANTRA_MU && flagTest.getValue() == 2) {
//                    if(gameObject.getObjectContainer() instanceof Screen
//                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 6) {
//                        // MU tablet effect
//                        List<GameObject> objects = mantraTablets.get("MU");
//                        if(objects == null) {
//                            mantraTablets.put("MU", new ArrayList<>());
//                            objects = mantraTablets.get("MU");
//                        }
//                        objects.add(gameObject);
//                        break;
//                    }
//                }
//                else if(flagTest.getIndex() == FlagConstants.MANTRA_VIY && flagTest.getValue() == 2) {
//                    if(gameObject.getObjectContainer() instanceof Screen
//                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 5) {
//                        // VIY tablet effect
//                        List<GameObject> objects = mantraTablets.get("VIY");
//                        if(objects == null) {
//                            mantraTablets.put("VIY", new ArrayList<>());
//                            objects = mantraTablets.get("VIY");
//                        }
//                        objects.add(gameObject);
//                        break;
//                    }
//                }
//                else if(flagTest.getIndex() == FlagConstants.MANTRA_BAHRUN && flagTest.getValue() == 2) {
//                    if(gameObject.getObjectContainer() instanceof Screen
//                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 13) {
//                        // BAHRUN tablet effect
//                        List<GameObject> objects = mantraTablets.get("BAHRUN");
//                        if(objects == null) {
//                            mantraTablets.put("BAHRUN", new ArrayList<>());
//                            objects = mantraTablets.get("BAHRUN");
//                        }
//                        objects.add(gameObject);
//                        break;
//                    }
//                }
//                else if(flagTest.getIndex() == FlagConstants.MANTRA_WEDJET && flagTest.getValue() == 2) {
//                    if(gameObject.getObjectContainer() instanceof Screen
//                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 3) {
//                        // WEDJET tablet effect
//                        List<GameObject> objects = mantraTablets.get("WEDJET");
//                        if(objects == null) {
//                            mantraTablets.put("WEDJET", new ArrayList<>());
//                            objects = mantraTablets.get("WEDJET");
//                        }
//                        objects.add(gameObject);
//                        break;
//                    }
//                }
//                else if(flagTest.getIndex() == FlagConstants.MANTRA_ABUTO && flagTest.getValue() == 2) {
//                    if(gameObject.getObjectContainer() instanceof Screen
//                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 11) {
//                        // ABUTO tablet effect
//                        List<GameObject> objects = mantraTablets.get("ABUTO");
//                        if(objects == null) {
//                            mantraTablets.put("ABUTO", new ArrayList<>());
//                            objects = mantraTablets.get("ABUTO");
//                        }
//                        objects.add(gameObject);
//                        break;
//                    }
//                }
//                else if(flagTest.getIndex() == FlagConstants.MANTRA_FINAL) {
//                    if(flagTest.getValue() == 2) {
//                        if(gameObject.getObjectContainer() instanceof Screen
//                                && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 0) {
//                            // LAMULANA tablet effect
//                            List<GameObject> objects = mantraTablets.get("LAMULANA");
//                            if(objects == null) {
//                                mantraTablets.put("LAMULANA", new ArrayList<>());
//                                objects = mantraTablets.get("LAMULANA");
//                            }
//                            objects.add(gameObject);
//                            break;
//                        }
//                    }
//                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Crusher) {
            for (WriteByteOperation flagUpdate : gameObject.getWriteByteOperations()) {
                if (flagUpdate.getIndex() == FlagConstants.WF_MAP_SHRINE) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MAP, FlagConstants.WF_MAP_SHRINE);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Hitbox) {
            TestByteOperation flagTest;
            for (int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                flagTest = gameObject.getTestByteOperations().get(i);
                if (flagTest.getIndex() == FlagConstants.WF_SOFTWARE_DEATHV) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.SOFTWARE_DEATHV, FlagConstants.WF_SOFTWARE_DEATHV);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if (flagTest.getIndex() == FlagConstants.WF_MAP_ILLUSION) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MAP, FlagConstants.WF_MAP_ILLUSION);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Snake) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Cockatrice) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Condor) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_MirrorGhosts) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_MaskedMan) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Nozuchi) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Fist) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.RedSkeleton) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Sonic) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_CatBall) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Bennu) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_PharaohHead) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if(gameObject.getId() == ObjectIdConstants.Chest) {
            int worldFlag;
            short inventoryArg;

            if(gameObject.getArgs().get(0) == DropType.COINS.getValue()) {
                // Coin chest
                inventoryArg = gameObject.getArgs().get(1); // Use coin amount as item arg
            }
            else if(gameObject.getArgs().get(0) == DropType.BOMB_AMMO.getValue()) {
                // Bomb chest, will be replaced by an 80-coin chest.
                inventoryArg = 80;
            }
            else {
                // Item chest
                inventoryArg = (short)(gameObject.getArgs().get(0) - 11);
            }

            WriteByteOperation flagUpdate = gameObject.getWriteByteOperations().get(0);
            if(flagUpdate.getIndex() == FlagConstants.SURFACE_PUZZLE_SEAL_COIN_CHEST) {
                // Replace world flag for Life Seal coin chest, which is a bit special
                worldFlag = FlagConstants.WF_COIN_SURFACE_SEAL;
            }
            else if(Settings.isRandomizeTrapItems() && flagUpdate.getIndex() == FlagConstants.WF_TRAP_GRAVEYARD) {
                // Replace world flag for Graveyard trap chest, and add a full set of update flags since it only has one.
                inventoryArg = 0;
                worldFlag = FlagConstants.WF_TRAP_GRAVEYARD;
            }
            else if(Settings.isRandomizeTrapItems() && flagUpdate.getIndex() == FlagConstants.ILLUSION_PUZZLE_EXPLODING_CHEST) {
                // Replace world flag for Illusion trap chest
                inventoryArg = 0;
                worldFlag = FlagConstants.WF_TRAP_ILLUSION;
            }
            else {
                worldFlag = gameObject.getWriteByteOperations().get(0).getIndex();
            }

            GameObjectId gameObjectId = new GameObjectId(inventoryArg, worldFlag);

            List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
            if (objects == null) {
                mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
            }
            objects.add(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.FloatingItem) {
            short chestArg = gameObject.getArgs().get(1);
            int worldFlag = gameObject.getWriteByteOperations().get(0).getIndex();
            GameObjectId gameObjectId = new GameObjectId(chestArg, worldFlag);

            List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
            if (objects == null) {
                mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
            }
            objects.add(gameObject);
        }
        else if (gameObject.getId() == ObjectIdConstants.CounterweightElevator) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == FlagConstants.WF_PLANE_MODEL) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.PLANE_MODEL, FlagConstants.WF_PLANE_MODEL);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Seal) {
            if(Settings.isRandomizeSeals()) {
                String sealNode = SealRandomizer.getSealNode(gameObject);
                List<GameObject> seals = mapOfSealNodeToSealObjects.get(sealNode);
                if(seals == null) {
                    seals = new ArrayList<>();
                    mapOfSealNodeToSealObjects.put(sealNode, seals);
                }
                seals.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Gyonin) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Hippocamp) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Kraken) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_ExplodeRock) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Slime) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Kakoujuu) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Mandrake) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Naga) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Garuda) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Blob) {
            // Hand enemies in Extinction
            if(Settings.isHalloweenMode() && Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
//        else if (gameObject.getId() == ObjectIdConstants.Hekatonkheires) {
//            if(Settings.isHalloweenMode() && Settings.isRandomizeEnemies()) {
//                enemyObjects.add(gameObject);
//            }
//        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Bonnacon) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_FlowerFacedSnouter) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Monocoli) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_JiangShi) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_RongXuanwangCorpse) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
//        else if (gameObject.getId() == ObjectIdConstants.Enemy_Hundun) {
//            if(Settings.isRandomizeEnemies()) {
//                enemyObjects.add(gameObject);
//            }
//        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Pan) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Hanuman) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Enkidu) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Marchosias) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Witch) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Siren) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_XingTian) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_ZaoChi) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Leucrotta) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_LizardMan) {
            if(Settings.isRandomizeEnemies()) {
                Screen screen = (Screen)gameObject.getObjectContainer();
                if(screen.getZoneIndex() != 10 || screen.getRoomIndex() != 5 || screen.getScreenIndex() != 1) {
                    // All lizards except lizard puzzle guy
                    enemyObjects.add(gameObject);
                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Asp) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Kui) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Toujin) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_DiJiang) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_IceWizard) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Keseran) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_BaiZe) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Anubis) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Yowie) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Troll) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_ABaoAQu) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Andras) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Cyclops) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Vimana) {
            // Would normally check for Plane Model flag, but there are no Vimana objects that don't have it, except the Fools2020 ones.
//            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
//                if (flagTest.getIndex() == FlagConstants.WF_PLANE_MODEL) {
            GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.PLANE_MODEL, FlagConstants.WF_PLANE_MODEL);
            List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
            if (objects == null) {
                mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
            }
            objects.add(gameObject);
//                    break;
//                }
//            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_BlackDog) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Salamander) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_SwordBird) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Elephant) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Amon) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Satan) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_Devil) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
//        else if (gameObject.getId() == ObjectIdConstants.Kuusarikku) {
//            if(Settings.isHalloweenMode() && Settings.isRandomizeEnemies()) {
//                if(gameObject.getObjectContainer() instanceof Screen) {
//                    Screen screen = (Screen) gameObject.getObjectContainer();
//                    if(screen.getZoneIndex() == 24) {
//                        enemyObjects.add(gameObject);
//                    }
//                }
//            }
//        }
        else if (gameObject.getId() == ObjectIdConstants.Girtablilu) {
            if(Settings.isHalloweenMode()) {
                if(gameObject.getObjectContainer() instanceof Screen) {
                    Screen screen = (Screen) gameObject.getObjectContainer();
                    if(screen.getZoneIndex() == 24) {
                        enemyObjects.add(gameObject);
                    }
                }
            }
        }
//        else if (gameObject.getId() == ObjectIdConstants.Ushum) {
//            if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs() && Settings.isRandomizeEnemies()) {
//                if(gameObject.getObjectContainer() instanceof Screen) {
//                    Screen screen = (Screen) gameObject.getObjectContainer();
//                    if(screen.getZoneIndex() == 24) {
//                        enemyObjects.add(gameObject);
//                    }
//                }
//            }
//        }
        else if (gameObject.getId() == ObjectIdConstants.Enemy_MiniBoss) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.GraphicsTextureDraw) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if(!Settings.isRandomizeNonBossDoors()) {
                    if(flagTest.getIndex() == FlagConstants.AMPHISBAENA_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.AMPHISBAENA_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 0
                                ? "Door: F1" : "Door: B1";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == FlagConstants.SAKIT_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.SAKIT_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 2
                                ? "Door: F2" : "Door: B2";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == FlagConstants.ELLMAC_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.ELLMAC_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 3
                                ? "Door: F3" : "Door: B3";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == FlagConstants.BAHAMUT_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.BAHAMUT_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 5
                                ? "Door: F4" : "Door: B4";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == FlagConstants.VIY_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.VIY_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 1
                                ? "Door: F5" : "Door: B5";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == FlagConstants.PALENQUE_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.PALENQUE_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 6
                                ? "Door: F6" : "Door: B6";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == FlagConstants.BAPHOMET_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.BAPHOMET_GATE_OPEN) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 5
                                ? "Door: F7" : "Door: B7";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                }

                if (flagTest.getIndex() == FlagConstants.WF_SOFTWARE_MEKURI) {
                    // mekuri tent-closing effect
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.SOFTWARE_MEKURI, FlagConstants.WF_SOFTWARE_MEKURI);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == FlagConstants.WF_CRUCIFIX) {
                    // Crucifix puzzle lit torches
                    GameObjectId gameObjectId = new GameObjectId((short) ItemConstants.CRUCIFIX, FlagConstants.WF_CRUCIFIX);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == FlagConstants.WF_PROVOCATIVE_BATHING_SUIT) {
                    // HT Dracuet stuff
                    GameObjectId gameObjectId = new GameObjectId(ItemConstants.PROVOCATIVE_BATHING_SUIT, FlagConstants.WF_PROVOCATIVE_BATHING_SUIT);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.WarpDoor) {
            if(gameObject.getArgs().get(0) == 0) {
                if(Settings.isRandomizeBacksideDoors()) {
                    Screen screen = (Screen)gameObject.getObjectContainer();
                    String doorName = null;
                    int zone = screen.getZoneIndex();
                    if(zone == ZoneConstants.GUIDANCE) {
                        // Gate of Guidance => Gate of Illusion
                        if(screen.getRoomIndex() != 1) {
                            doorName = "Door: F1";
                        }
                    }
                    else if(zone == ZoneConstants.SURFACE) {
                        // Surface => Tower of the Goddess
                        doorName = "Door: F5";
                    }
                    else if(zone == ZoneConstants.MAUSOLEUM) {
                        // Mausoleum of the Giants => Graveyard of the Giants
                        doorName = "Door: F2";
                    }
                    else if(zone == ZoneConstants.SUN) {
                        // Temple of the Sun => Temple of Moonlight
                        doorName = "Door: F3";
                    }
                    else if(zone == ZoneConstants.INFERNO) {
                        if(screen.getRoomIndex() == 8) {
                            // Inferno Cavern [Viy] => Tower of Ruin [Southwest]
                            doorName = "Door: F4";
                        }
                        else {
                            // Inferno Cavern [Spikes] => Tower of Ruin [Top]
                            doorName = "Door: F7";
                        }
                    }
                    else if(zone == ZoneConstants.EXTINCTION) {
                        if(screen.getRoomIndex() == 7) {
                            if(Settings.isRandomizeNonBossDoors()) {
                                doorName = "Door: F9";
                            }
                        }
                        else {
                            // Chamber of Extinction [Magatama Left] => Chamber of Birth [Northeast]
                            doorName = "Door: F6";
                        }
                    }
                    else if(zone == ZoneConstants.ILLUSION) {
                        // Gate of Illusion [Grail] => Gate of Guidance
                        doorName = "Door: B1";
                    }
                    else if(zone == ZoneConstants.GRAVEYARD) {
                        // Graveyard of the Giants [West] => Mausoleum of the Giants
                        doorName = "Door: B2";
                    }
                    else if(zone == ZoneConstants.MOONLIGHT) {
                        // Temple of Moonlight [Lower] => Temple of the Sun [Main]
                        doorName = "Door: B3";
                    }
                    else if(zone == ZoneConstants.GODDESS) {
                        // Tower of the Goddess [Lower] => Surface [Main]
                        if(screen.getRoomIndex() == 0) {
                            doorName = "Door: B5";
                        }
                    }
                    else if(zone == ZoneConstants.RUIN) {
                        if(screen.getRoomIndex() == 2) {
                            // Tower of Ruin [Southwest] => Inferno Cavern [Viy]
                            doorName = "Door: B4";
                        }
                        else {
                            // Tower of Ruin [Top] => Inferno Cavern [Spikes]
                            doorName = "Door: B7";
                        }
                    }
                    else if(zone == ZoneConstants.BIRTH_SWORDS) {
                        // Chamber of Birth [Northeast] => Chamber of Extinction [Magatama Left]
                        doorName = "Door: B6";
                    }
                    else if(zone == 17) {
                        if(Settings.isRandomizeNonBossDoors()) {
                            // Dimensional Corridor [Grail] => Endless Corridor [1F]
                            doorName = "Door: B8";
                        }
                    }
                    else if(zone == 19) {
                        if(Settings.isRandomizeNonBossDoors()) {
                            // Gate of Time [Mausoleum] =>
                            doorName = "Door: B9";
                        }
                    }

                    if(doorName != null) {
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.SoundEffect) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == FlagConstants.WF_MAP_SHRINE) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MAP, FlagConstants.WF_MAP_SHRINE);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
//                else if(flagTest.getIndex() == FlagConstants.WF_MATERNITY_STATUE) {
//                    // Timer to play Shell Horn sound when being given Maternity Statue equivalent
//                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MATERNITY_STATUE, FlagConstants.WF_MATERNITY_STATUE);
//                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
//                    if (objects == null) {
//                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
//                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
//                    }
//                    objects.add(gameObject);
//                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.UseItemDetector) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == FlagConstants.WF_TREASURES) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.TREASURES, FlagConstants.WF_TREASURES);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.Scannable) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == FlagConstants.WF_MAP_SURFACE) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MAP, FlagConstants.WF_MAP_SURFACE);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.ConversationDoor) {
            int blockNumber = gameObject.getArgs().get(4);
            if(gameObject.getArgs().get(3) == 1) {
                // Any shop
                Short defaultShopBlock = getCustomBlockIndex(CustomBlockEnum.DefaultShopBlock);
                if(DataFromFile.getMapOfShopNameToShopBlock().values().contains(blockNumber)
                        || (defaultShopBlock != null && blockNumber == defaultShopBlock)
                        || (Settings.isFools2020Mode() && blockNumber == BlockConstants.ShopBlockGiantMopiranAngelShield)) {
                    List<GameObject> objects = mapOfShopBlockToShopObjects.get(blockNumber);
                    if (objects == null) {
                        mapOfShopBlockToShopObjects.put(blockNumber, new ArrayList<>());
                        objects = mapOfShopBlockToShopObjects.get(blockNumber);
                    }
                    objects.add(gameObject);
                }

                if(blockNumber == BlockConstants.ShopBlockNebur) {
                    // Shop before/after buying the MSX2
                    for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                        if (flagTest.getIndex() == FlagConstants.WF_MSX2) {
                            GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MSX2, FlagConstants.WF_MSX2);
                            List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                            if (objects == null) {
                                mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                                objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                            }
                            objects.add(gameObject);
                            break;
                        }
                    }
                }
                else if(blockNumber == BlockConstants.ShopBlockSidro) {
                    mapOfNpcLocationToObject.put("NPCL: Sidro", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockModro) {
                    mapOfNpcLocationToObject.put("NPCL: Modro", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockPenadventOfGhost) {
                    mapOfNpcLocationToObject.put("NPCL: Penadvent of ghost", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockGreedyCharlie) {
                    mapOfNpcLocationToObject.put("NPCL: Greedy Charlie", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockShalomIII) {
                    mapOfNpcLocationToObject.put("NPCL: Shalom III", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockUsasVI) {
                    mapOfNpcLocationToObject.put("NPCL: Usas VI", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockKingvalleyI) {
                    mapOfNpcLocationToObject.put("NPCL: Kingvalley I", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockMrFishman){
                    mapOfNpcLocationToObject.put("NPCL: Mr. Fishman (Original)", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockMrFishmanAlt){
                    mapOfNpcLocationToObject.put("NPCL: Mr. Fishman (Alt)", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockOperatorCombaker){
                    mapOfNpcLocationToObject.put("NPCL: Operator Combaker", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockYiegahKungfu){
                    mapOfNpcLocationToObject.put("NPCL: Yiegah Kungfu", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockArrogantMetagear){
                    mapOfNpcLocationToObject.put("NPCL: Arrogant Metagear", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockArrogantSturdySnake){
                    mapOfNpcLocationToObject.put("NPCL: Arrogant Sturdy Snake", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockYiearKungfu){
                    mapOfNpcLocationToObject.put("NPCL: Yiear Kungfu", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockAffectedKnimare){
                    mapOfNpcLocationToObject.put("NPCL: Affected Knimare", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockMoverAthleland){
                    mapOfNpcLocationToObject.put("NPCL: Mover Athleland", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockGiantMopiran){
                    mapOfNpcLocationToObject.put("NPCL: Giant Mopiran", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockKingvalleyII){
                    mapOfNpcLocationToObject.put("NPCL: Kingvalley II", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockEnergeticBelmont){
                    mapOfNpcLocationToObject.put("NPCL: Energetic Belmont", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockMechanicalEfspi){
                    mapOfNpcLocationToObject.put("NPCL: Mechanical Efspi", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockMudManQubert){
                    mapOfNpcLocationToObject.put("NPCL: Mud Man Qubert", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockHotbloodedNemesistwo){
                    mapOfNpcLocationToObject.put("NPCL: Hot-blooded Nemesistwo", (ConversationDoor)gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockNeburAlt) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MSX2, FlagConstants.WF_MSX2);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                }
                else if(blockNumber == BlockConstants.ShopBlockTailorDracuet){
                    mapOfNpcLocationToObject.put("NPCL: Tailor Dracuet", (ConversationDoor)gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_Hiner) {
                mapOfNpcLocationToObject.put("NPCL: Hiner", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_Moger) {
                mapOfNpcLocationToObject.put("NPCL: Moger", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_FormerMekuriMaster_Mekuri) {
                mapOfNpcLocationToObject.put("NPCL: Former Mekuri Master", (ConversationDoor)gameObject);

                GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.SOFTWARE_MEKURI, FlagConstants.WF_SOFTWARE_MEKURI);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestZarnac) {
                mapOfNpcLocationToObject.put("NPCL: Priest Zarnac", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestXanado) {
                mapOfNpcLocationToObject.put("NPCL: Priest Xanado", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PhilosopherGiltoriyo) {
                mapOfNpcLocationToObject.put("NPCL: Philosopher Giltoriyo", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestHidlyda) {
                mapOfNpcLocationToObject.put("NPCL: Priest Hidlyda", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestRomancis) {
                mapOfNpcLocationToObject.put("NPCL: Priest Romancis", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestAramo) {
                mapOfNpcLocationToObject.put("NPCL: Priest Aramo", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestTriton) {
                mapOfNpcLocationToObject.put("NPCL: Priest Triton", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestJaguarfiv) {
                mapOfNpcLocationToObject.put("NPCL: Priest Jaguarfiv", (ConversationDoor)gameObject);
            }
            else if(blockNumber == 686) {
                // The Fairy Queen - Endless NPC, 08-01-00
                mapOfNpcLocationToObject.put("NPCL: The Fairy Queen", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_MrSlushfund_Pepper) {
                // Mr. Slushfund - Illusion NPC, 10-08-00
                // Conversation to receive Pepper
                GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.PEPPER, FlagConstants.WF_PEPPER);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == BlockConstants.Master_MrSlushfund_Anchor) {
                // Conversation to give Treasures and receive Anchor
                GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.ANCHOR, FlagConstants.WF_ANCHOR);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == BlockConstants.Master_MrSlushfund_NeverComeBack) {
                // Conversation after receiving both Pepper and Anchor
                GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.ANCHOR, FlagConstants.WF_ANCHOR);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestAlest) {
//                mapOfNpcLocationToObject.put("NPCL: Priest Alest", (ConversationDoor)gameObject);

                GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MINI_DOLL, FlagConstants.WF_MINI_DOLL);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
//            else if(blockNumber == BlockConstants.Master_StrayFairy) {
//                mapOfNpcLocationToObject.put("NPCL: Stray fairy", (ConversationDoor)gameObject);
//            }
            else if(blockNumber == BlockConstants.Master_GiantThexde) {
                mapOfNpcLocationToObject.put("NPCL: Giant Thexde", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PhilosopherAlsedana) {
                mapOfNpcLocationToObject.put("NPCL: Philosopher Alsedana", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PhilosopherSamaranta) {
                mapOfNpcLocationToObject.put("NPCL: Philosopher Samaranta", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestLaydoc) {
                mapOfNpcLocationToObject.put("NPCL: Priest Laydoc", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestAshgine) {
                mapOfNpcLocationToObject.put("NPCL: Priest Ashgine", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_Fobos_MedicineCheck) {
                // Post-Medicine version of Fobos
                mapOfNpcLocationToObject.put("NPCL: Philosopher Fobos", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_8BitElder) {
                mapOfNpcLocationToObject.put("NPCL: 8bit Elder", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_duplex) {
                mapOfNpcLocationToObject.put("NPCL: duplex", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_Samieru) {
                mapOfNpcLocationToObject.put("NPCL: Samieru", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_Naramura) {
                mapOfNpcLocationToObject.put("NPCL: Naramura", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_8BitFairy) {
                mapOfNpcLocationToObject.put("NPCL: 8bit Fairy", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestMadomono) {
                mapOfNpcLocationToObject.put("NPCL: Priest Madomono", (ConversationDoor)gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestGailious) {
                mapOfNpcLocationToObject.put("NPCL: Priest Gailious", (ConversationDoor)gameObject);
            }
            else if(blockNumber == 915) {
                GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MINI_DOLL, FlagConstants.WF_MINI_DOLL);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == BlockConstants.Master_Dracuet_ProvocativeBathingSuit) {
                // Dracuet Provocative Bathing Suit conversation - needs to depend on HT item instead.
                GameObjectId gameObjectId = new GameObjectId(ItemConstants.PROVOCATIVE_BATHING_SUIT, FlagConstants.WF_PROVOCATIVE_BATHING_SUIT);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == 1013) {
                // Mulbruk Provocative Bathing Suit conversation - needs to depend on HT item instead.
                GameObjectId gameObjectId = new GameObjectId(ItemConstants.PROVOCATIVE_BATHING_SUIT, FlagConstants.WF_PROVOCATIVE_BATHING_SUIT);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.ItemGive) {
            short itemArg = gameObject.getArgs().get(0);
            if(itemArg == ItemConstants.MAP) {
                GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MAP, FlagConstants.WF_MAP_SURFACE);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
//            else if(itemArg == ItemConstants.MATERNITY_STATUE) {
//                GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MATERNITY_STATUE, FlagConstants.WF_MATERNITY_STATUE);
//                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
//                if (objects == null) {
//                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
//                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
//                }
//                objects.add(gameObject);
//            }
        }
        else if (gameObject.getId() == ObjectIdConstants.XelpudPillar) {
            for(TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if(flagTest.getIndex() == FlagConstants.WF_DIARY) {
                    // Diary puzzle pillar
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.DIARY, FlagConstants.WF_DIARY);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    return;
                }
            }
        }
        else if(gameObject.getId() == ObjectIdConstants.SnapshotsScan) {
            short itemArg = gameObject.getArgs().get(3);
            if(itemArg == ItemConstants.SOFTWARE_MANTRA
                    || itemArg == ItemConstants.SOFTWARE_EMUSIC
                    || itemArg == ItemConstants.SOFTWARE_BEOLAMU) {
                short worldFlag;
                if(itemArg == ItemConstants.SOFTWARE_MANTRA) {
                    worldFlag = FlagConstants.WF_SOFTWARE_MANTRA;
                }
                else if(itemArg == ItemConstants.SOFTWARE_EMUSIC) {
                    worldFlag = FlagConstants.WF_SOFTWARE_EMUSIC;
                }
                else {
                    worldFlag = FlagConstants.WF_SOFTWARE_BEOLAMU;
                }

                GameObjectId gameObjectId = new GameObjectId(itemArg, worldFlag);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
        }
        else if (gameObject.getId() == ObjectIdConstants.TransitionGate) {
            if(Settings.isRandomizeTransitionGates()) {
                Screen screen = (Screen)gameObject.getObjectContainer();
//                FileUtils.logFlush(String.format("Gate on screen [%d, %d, %d] leads to screen [%d, %d, %d] with position (%d, %d)",
//                        screen.getZoneIndex(), screen.getRoomIndex(), screen.getScreenIndex(),
//                        gameObject.getArgs().get(0), gameObject.getArgs().get(1), gameObject.getArgs().get(2),
//                        gameObject.getArgs().get(3), gameObject.getArgs().get(4)));

                String gateName = null;
                if(screen.getZoneIndex() == 0) {
                    // Gate of Guidance
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Guidance L1";
                    }
                    else if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Guidance U1";
                    }
                    else if (screen.getRoomIndex() == 9 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Guidance D1";
                    }
                    else if (screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Guidance D2";
                    }
                }
                else if(screen.getZoneIndex() == 1) {
                    // Surface
                    if (screen.getRoomIndex() == 11 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Surface R1";
                    }
                    else if (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Surface D1";
                    }
                    else if (screen.getRoomIndex() == 5 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Surface D2";
                    }
                }
                else if(screen.getZoneIndex() == 2) {
                    // Mausoleum
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Mausoleum U1";
                    }
                    else if (screen.getRoomIndex() == 7 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Mausoleum D1";
                    }
                    else if (screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Mausoleum L1";
                    }
                }
                else if(screen.getZoneIndex() == 3) {
                    // Sun
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Sun U1";
                    }
                    else if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Sun L1";
                    }
                    else if (screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
                        if(gameObject.getY() == 80) {
                            gateName = "Transition: Sun R1";
                        }
                        else {
                            gateName = "Transition: Sun R2";
                        }
                    }
                }
                else if(screen.getZoneIndex() == 4) {
                    // Spring
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Spring D1";
                    }
                }
                else if(screen.getZoneIndex() == 5) {
                    // Inferno
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Inferno R1";
                    }
                    else if (screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Inferno U1";
                    }
                    else if (screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Inferno U2";
                    }
                }
                else if(screen.getZoneIndex() == 6) {
                    // Extinction
                    if (screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Extinction U2";
                    }
                    else if (screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Extinction U1";
                    }
                    else if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 0) {
                        if(gameObject.getY() == 80) {
                            gateName = "Transition: Extinction L1";
                        }
                        else {
                            gateName = "Transition: Extinction L2";
                        }
                    }
                    else if (screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Extinction U3";
                    }
                }
                else if(screen.getZoneIndex() == 7) {
                    // Twin Labyrinths
                    if (screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
                        if(gameObject.getArgs().get(0) != 7) {
                            gateName = "Transition: Twin U1";
                        }
                    }
                    else if (screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Twin D1";
                    }
                    else if (screen.getRoomIndex() == 16 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Twin D2";
                    }
                    else if (screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Twin U2";
                    }
                    else if (screen.getRoomIndex() == 10 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Twin U3";
                    }
//                    else if (screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
//                        gateName = "Transition: Twin U4";
//                    }
                }
                else if(screen.getZoneIndex() == 8) {
                    // Endless
                    if (screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Endless L1";
                    }
                    else if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Endless R1";
                    }
                    else if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 3) {
                        gateName = "Transition: Endless U1";
                    }
                    else if (screen.getRoomIndex() == 5 && screen.getScreenIndex() == 3) {
                        gateName = "Transition: Endless D1";
                    }
                }
                else if(screen.getZoneIndex() == 9) {
                    // Shrine
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Shrine U1";
                    }
                    else if (screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                        // Escape gate gets added elsewhere.
                        gateName = "Transition: Shrine D1";
                    }
                    else if (screen.getRoomIndex() == 9 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Shrine D2";
                    }
                    else if (screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                        // Escape gate gets added elsewhere.
                        gateName = "Transition: Shrine D3";
                    }
                }
                else if(screen.getZoneIndex() == 10) {
                    // Illusion
                    if (screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Illusion D1";
                    }
                    else if (screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Illusion D2";
                    }
                    else if (screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Illusion R1";
                    }
                    else if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Illusion R2";
                    }
                }
                else if(screen.getZoneIndex() == 11) {
                    // Graveyard
                    if (screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Graveyard L1";
                    }
                    else if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Graveyard R1";
                    }
                    else if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Graveyard U1";
                    }
                    else if (screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Graveyard U2";
                    }
                    else if (screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Graveyard D1";
                    }
                }
                else if(screen.getZoneIndex() == 12) {
                    // Moonlight
                    if (screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Moonlight U1";
                    }
                    else if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Moonlight U2";
                    }
                    else if (screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Moonlight L1";
                    }
                }
                else if(screen.getZoneIndex() == 13) {
                    // Goddess
                    if (screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Goddess L1";
                    }
                    else if (screen.getRoomIndex() == 7 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Goddess D1";
                    }
                    else if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Goddess L2";
                    }
                    else if (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Goddess U1";
                    }
                    else if (screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                        if(gameObject.getArgs().get(0) == 11) {
                            gateName = "Transition: Pipe L1";
                        }
                        else if(gameObject.getArgs().get(0) == 12) {
                            gateName = "Transition: Pipe R1";
                        }
                    }
                }
                else if(screen.getZoneIndex() == 14) {
                    // Ruin
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Ruin R2";
                    }
                    else if (screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Ruin R1";
                    }
                    else if (screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Ruin L1";
                    }
                }
                else if(screen.getZoneIndex() == 15) {
                    // Birth (East)
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Birth U1";
                    }
                    else if (screen.getRoomIndex() == 3 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Birth L1";
                    }
                }
                else if(screen.getZoneIndex() == 16) {
                    // Birth (West)
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Birth D1";
                    }
                    else if (screen.getRoomIndex() == 3 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Birth R1";
                    }
                }
                else if(screen.getZoneIndex() == 18) {
                    // True Shrine
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Shrine U1";
                    }
                    else if (screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Shrine D1";
                    }
                    else if (screen.getRoomIndex() == 9 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Shrine D2";
                    }
                    else if (screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Shrine D3";
                    }
                }
                else if(screen.getZoneIndex() == 19) {
                    // Gate of Time (Mausoleum of the Giants)
                    if (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Retromausoleum U1";
                    }
                    else if (screen.getRoomIndex() == 1 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Retromausoleum D1";
                    }
                }
                else if(screen.getZoneIndex() == 20) {
                    // Gate of Time (Gate of Guidance)
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Retroguidance D1";
                    }
                    else if (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Retroguidance L1";
                    }
                }
                else if(screen.getZoneIndex() == 21) {
                    // Gate of Time (Surface)
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Retrosurface R1";
                    }
                }
                else if(screen.getZoneIndex() == 22) {
                    // Night Surface
                    if (screen.getRoomIndex() == 11 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Surface R1";
                    }
                }

                if(gateName != null) {
                    List<GameObject> transitionGates = mapOfGateNameToTransitionGate.get(gateName);
                    if(transitionGates == null) {
                        transitionGates = new ArrayList<>();
                        mapOfGateNameToTransitionGate.put(gateName, transitionGates);
                    }
                    transitionGates.add(gameObject);
                }
            }
        }
    }
}
