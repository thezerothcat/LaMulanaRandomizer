package lmr.randomizer.update;

import lmr.randomizer.*;
import lmr.randomizer.dat.*;
import lmr.randomizer.dat.shop.BlockStringData;
import lmr.randomizer.dat.shop.ShopBlock;
import lmr.randomizer.node.CustomItemPlacement;
import lmr.randomizer.random.EnemyRandomizer;
import lmr.randomizer.random.NpcRandomizer;
import lmr.randomizer.random.SealRandomizer;
import lmr.randomizer.random.ShopRandomizer;
import lmr.randomizer.rcd.object.*;

import java.util.*;

/**
 * Created by thezerothcat on 7/21/2017.
 */
public final class GameDataTracker {
    public static Map<GameObjectId, List<GameObject>> mapOfChestIdentifyingInfoToGameObject = new HashMap<>();
    private static Map<GameObjectId, List<Block>> mapOfChestIdentifyingInfoToBlock = new HashMap<>();
    private static Map<Integer, List<GameObject>> mapOfShopBlockToShopObjects = new HashMap<>();
    private static Map<String, List<GameObject>> mantraTablets = new HashMap<>();
    private static Map<String, List<GameObject>> mapOfDoorNameToBacksideDoor = new HashMap<>();
    private static Map<String, List<GameObject>> mapOfGateNameToTransitionGate = new HashMap<>();
    private static Map<String, Screen> mapOfTransitionNameToScreen = new HashMap<>();
    private static Map<String, List<GameObject>> mapOfSealNodeToSealObjects = new HashMap<>();
    private static Map<String, GameObject> mapOfNpcLocationToObject = new HashMap<>();
    private static List<GameObject> enemyObjects = new ArrayList<>();
    private static List<GameObject> edenDaises = new ArrayList<>();

    private static GameObject subweaponPot;

    private static Map<Integer, Integer> mapOfWorldFlagToAssignedReplacementFlag = new HashMap<>();
    private static int randomizeGraphicsFlag = DataFromFile.FIRST_AVAILABLE_RANDOM_GRAPHICS_FLAG;

    private GameDataTracker() {
    }

    public static void clearAll() {
        mapOfChestIdentifyingInfoToGameObject.clear();
        mapOfChestIdentifyingInfoToBlock.clear();
        mapOfShopBlockToShopObjects.clear();
        mapOfDoorNameToBacksideDoor.clear();
        mapOfGateNameToTransitionGate.clear();
        mapOfTransitionNameToScreen.clear();
        mapOfNpcLocationToObject.clear();
        mantraTablets.clear();
        enemyObjects.clear();
        edenDaises.clear();

        mapOfWorldFlagToAssignedReplacementFlag.clear();
        randomizeGraphicsFlag = DataFromFile.FIRST_AVAILABLE_RANDOM_GRAPHICS_FLAG;
    }

    public static void setSubweaponPot(GameObject subweaponPot) {
        GameDataTracker.subweaponPot = subweaponPot;
    }

    public static void trackDefaultShop(GameObject defaultShop) {
        mapOfShopBlockToShopObjects.put((int)defaultShop.getArgs().get(4), new ArrayList<>(Arrays.asList(defaultShop)));
    }

    public static void putTransitionScreen(String transitionName, Screen screen) {
        GameDataTracker.mapOfTransitionNameToScreen.put(transitionName, screen);
    }

    public static void trackBlock(Block block) {
        if(block.getBlockNumber() == BlockConstants.ItemConversationMekuri) {
            GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.SOFTWARE_MEKURI, FlagConstants.WF_SOFTWARE_MEKURI);
            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == BlockConstants.ItemConversationPepper) {
            // Conversation to receive Pepper
            GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.PEPPER, FlagConstants.WF_PEPPER);
            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == BlockConstants.ItemConversationAnchor) {
            // Conversation to give Treasures and receive Anchor
            GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.ANCHOR, FlagConstants.WF_ANCHOR);
            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == BlockConstants.ItemConversationMiniDoll) {
            // Mini Doll
            GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MINI_DOLL, FlagConstants.WF_MINI_DOLL);
            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == BlockConstants.ItemConversationXmailer) {
            GameObjectId gameObjectId = new GameObjectId(ItemConstants.SOFTWARE_XMAILER, FlagConstants.WF_SOFTWARE_XMAILER);
            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == BlockConstants.ItemConversationMulanaTalisman) {
            GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MULANA_TALISMAN, FlagConstants.WF_MULANA_TALISMAN);
            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == BlockConstants.ItemConversationBookOfTheDead) {
            GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.BOOK_OF_THE_DEAD, FlagConstants.WF_BOOK_OF_THE_DEAD);
            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == BlockConstants.ItemConversationMapSurface) {
            GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MAP, FlagConstants.WF_MAP_SURFACE);
            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == BlockConstants.ItemConversationProvocativeBathingSuit) {
            if(!(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs())) {
                GameObjectId gameObjectId = new GameObjectId(ItemConstants.PROVOCATIVE_BATHING_SUIT, FlagConstants.WF_PROVOCATIVE_BATHING_SUIT);
                List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
                if (blocks == null) {
                    mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                    blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
                }
                blocks.add(block);
            }
        }
    }

    private static void replaceBacksideDoorFlags(GameObject gameObject, Integer bossNumber, Integer gateFlag, boolean motherCheck) {
        gameObject.getTestByteOperations().clear();

        TestByteOperation testByteOperation;

        Integer bossFlag = getBossFlag(bossNumber);
        if(bossFlag != null) {
            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(bossFlag);
            testByteOperation.setValue((byte)(bossNumber == 9 ? 1 : 3));
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            gameObject.getTestByteOperations().add(testByteOperation);
        }

        if(gateFlag != null) {
            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(gateFlag);
            testByteOperation.setValue((byte)1);
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            gameObject.getTestByteOperations().add(testByteOperation);
        }

        if(motherCheck && !Settings.isRandomizeTransitionGates()) {
            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(FlagConstants.MOTHER_STATE);
            testByteOperation.setValue((byte) 3);
            testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
            gameObject.getTestByteOperations().add(testByteOperation);
        }
    }

    private static void replaceBacksideDoorArgs(GameObject gameObject, String doorWithCoordinatesData) {
        gameObject.getArgs().clear();
        gameObject.getArgs().add((short)0);

        if("Door: F1".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)10);
            gameObject.getArgs().add((short)3);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)40);
            gameObject.getArgs().add((short)80);
        }
        else if("Door: F2".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)11);
            gameObject.getArgs().add((short)3);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)80);
            gameObject.getArgs().add((short)160);
        }
        else if("Door: F3".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)12);
            gameObject.getArgs().add((short)4);
            gameObject.getArgs().add((short)2);
            gameObject.getArgs().add((short)200);
            gameObject.getArgs().add((short)240);
        }
        else if("Door: F4".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)14);
            gameObject.getArgs().add((short)2);
            gameObject.getArgs().add((short)1);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)240);
        }
        else if("Door: F5".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)13);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)1);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)400);
        }
        else if("Door: F6".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)15);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)1);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)400);
        }
        else if("Door: F7".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)14);
            gameObject.getArgs().add((short)8);
            gameObject.getArgs().add((short)1);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)120);
        }
        else if("Door: F8".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)17);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)320);
        }
        else if("Door: F9".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)19);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)1);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)320);
        }
        else if("Door: B1".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)5);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)180);
            gameObject.getArgs().add((short)160);
        }
        else if("Door: B2".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)2);
            gameObject.getArgs().add((short)4);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)80);
            gameObject.getArgs().add((short)320);
        }
        else if("Door: B3".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)3);
            gameObject.getArgs().add((short)6);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)460);
            gameObject.getArgs().add((short)160);
        }
        else if("Door: B4".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)5);
            gameObject.getArgs().add((short)8);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)140);
            gameObject.getArgs().add((short)160);
        }
        else if("Door: B5".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)(Settings.isHalloweenMode() ? 22 : 1));
            gameObject.getArgs().add((short)8);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)200);
            gameObject.getArgs().add((short)320);
        }
        else if("Door: B6".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)6);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)40);
            gameObject.getArgs().add((short)80);
        }
        else if("Door: B7".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)5);
            gameObject.getArgs().add((short)9);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)60);
            gameObject.getArgs().add((short)80);
        }
        else if("Door: B8".equals(doorWithCoordinatesData)) {
            if(Settings.isFools2021Mode()) {
                gameObject.getArgs().add((short)0);
                gameObject.getArgs().add((short)0);
                gameObject.getArgs().add((short)0);
                gameObject.getArgs().add((short)320);
                gameObject.getArgs().add((short)152);
            }
            else {
                gameObject.getArgs().add((short)8);
                gameObject.getArgs().add((short)0);
                gameObject.getArgs().add((short)1);
                gameObject.getArgs().add((short)400);
                gameObject.getArgs().add((short)180);
            }
        }
        else if("Door: B9".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)6);
            gameObject.getArgs().add((short)7);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)80);
        }
    }

    private static void replaceBacksideDoorTimerFlags(GameObject gameObject, int bossFlag, int gateFlag, boolean useSpecialFlag) {
        gameObject.getTestByteOperations().clear();

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(bossFlag);
        testByteOperation.setValue((byte)3);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        gameObject.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(FlagConstants.WF_BRONZE_MIRROR);
        testByteOperation.setValue((byte)2);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        gameObject.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(gateFlag);
        testByteOperation.setValue((byte)0);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        gameObject.getTestByteOperations().add(testByteOperation);

        gameObject.getWriteByteOperations().clear();

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(gateFlag);
        writeByteOperation.setValue((byte) 1);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        gameObject.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(useSpecialFlag ? FlagConstants.SCREEN_FLAG_C : FlagConstants.SCREEN_FLAG_29);
        writeByteOperation.setValue((byte) 1);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        gameObject.getWriteByteOperations().add(writeByteOperation);
    }

    private static void replaceMirrorCoverTimerFlags(GameObject gameObject, int mirrorCoverFlag) {
        gameObject.getTestByteOperations().clear();

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(FlagConstants.WF_BRONZE_MIRROR);
        testByteOperation.setValue((byte)2);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        gameObject.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(mirrorCoverFlag);
        testByteOperation.setValue((byte)0);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        gameObject.getTestByteOperations().add(testByteOperation);

        gameObject.getWriteByteOperations().clear();

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(mirrorCoverFlag);
        writeByteOperation.setValue((byte) 1);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        gameObject.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(FlagConstants.SCREEN_FLAG_28);
        writeByteOperation.setValue((byte) 1);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        gameObject.getWriteByteOperations().add(writeByteOperation);
    }

    private static void replaceBacksideDoorCoverFlags(GameObject gameObject, int gateFlag) {
        gameObject.getTestByteOperations().clear();

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(gateFlag);
        testByteOperation.setValue((byte)0);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        gameObject.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(FlagConstants.WF_BRONZE_MIRROR);
        testByteOperation.setValue((byte)2);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        gameObject.getTestByteOperations().add(testByteOperation);

        gameObject.getWriteByteOperations().clear();

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(gateFlag);
        writeByteOperation.setValue((byte) 1);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        gameObject.getWriteByteOperations().add(writeByteOperation);
    }

    private static void replaceMirrorCoverGraphicFlags(GameObject gameObject, int mirrorCoverFlag) {
        gameObject.getTestByteOperations().clear();

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(mirrorCoverFlag);
        testByteOperation.setValue((byte)0);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        gameObject.getTestByteOperations().add(testByteOperation);
    }

    public static void replaceTabletText(List<BlockContents> xelpudBlockContents, String replacement) {
        xelpudBlockContents.clear();
        List<Short> stringCharacters = FileUtils.stringToData(replacement);
        for (Short shortCharacter : stringCharacters) {
            xelpudBlockContents.add(new BlockSingleData(shortCharacter));
        }

        xelpudBlockContents.add(new BlockSingleData((short)10)); // Leaving this off breaks the tablet/prevents it from being translated to La-Mulanese if applicable.

        BlockListData blockListData = new BlockListData((short)78, (short)2);
        blockListData.getData().add((short)1); // 0 = English, 1 = La-Mulanese, 2 = ancient La-Mulanese, 3 = Rosetta Stone
        blockListData.getData().add((short)0); // 0 = no picture, 1 = picture

        xelpudBlockContents.add(blockListData);
    }

    public static void updateBlock(GameObjectId itemLocationData, GameObjectId itemNewContentsData) {
        List<Block> blocksToModify = mapOfChestIdentifyingInfoToBlock.get(itemLocationData);
        for(Block block : blocksToModify) {
            for(BlockContents blockContents : block.getBlockContents()) {
                if(blockContents instanceof BlockFlagData) {
                    BlockFlagData flagData = (BlockFlagData) blockContents;
                    if(flagData.getWorldFlag() == itemLocationData.getWorldFlag()) {
                        if(flagData.getWorldFlag() == FlagConstants.WF_MAP_SURFACE) {
                            // Surface map
                            flagData.setFlagValue((short)1);
                        }
                        else {
                            flagData.setFlagValue((short)2);
                        }
                        flagData.setWorldFlag((short)itemNewContentsData.getWorldFlag());
                    }
                    else if(flagData.getWorldFlag() == FlagConstants.MR_SLUSHFUND_CONVERSATION) {
                        // The flag for the Pepper/Treasures/Anchor sequence is being replaced with custom world flags,
                        // but it won't show up in the block as a thing that matches the world flag, so special case!
                        // Note: There is no case where we see a flag 0x228 and want to keep it.
                        flagData.setWorldFlag((short)itemNewContentsData.getWorldFlag());
                        flagData.setFlagValue((short)2);
                    }
                }
                else if(blockContents instanceof BlockItemData) {
                    BlockItemData itemData = (BlockItemData) blockContents;
                    if(itemData.getItemData() == itemLocationData.getInventoryArg()) {
                        itemData.setItemData(itemNewContentsData.getInventoryArg());
                    }
                }
            }
        }
    }

    public static void writeShopInventory(ShopBlock shopBlock, String shopItem1, String shopItem2, String shopItem3, List<Block> blocks,
                                          ItemPriceCount itemPriceAndCount1, ItemPriceCount itemPriceAndCount2, ItemPriceCount itemPriceAndCount3,
                                          String shopName, boolean recursive, NpcRandomizer npcRandomizer, Random random) {
        short shopItem1Flag = getFlag(shopItem1);
        short shopItem2Flag = getFlag(shopItem2);
        short shopItem3Flag = getFlag(shopItem3);

        // NOTE: only tolerates one sacred orb per shop
        if(shopItem1.contains("Sacred Orb") || shopItem2.contains("Sacred Orb") || shopItem3.contains("Sacred Orb")) {
            ShopBlock noOrbShopBlock = new ShopBlock(shopBlock, blocks.size());
            blocks.add(noOrbShopBlock);
            String newShopItem1 = shopItem1;
            String newShopItem2 = shopItem2;
            String newShopItem3 = shopItem3;
            ItemPriceCount newItemPriceAndCount1 = itemPriceAndCount1;
            ItemPriceCount newItemPriceAndCount2 = itemPriceAndCount2;
            ItemPriceCount newItemPriceAndCount3 = itemPriceAndCount3;
            short sacredOrbItemFlag;
            if(shopItem1.contains("Sacred Orb")) {
                newShopItem1 = "Weights";
                newItemPriceAndCount1 = new ItemPriceCount((short)10, (short)5);
                sacredOrbItemFlag = shopItem1Flag;
            }
            else if(shopItem2.contains("Sacred Orb")) {
                newShopItem2 = "Weights";
                newItemPriceAndCount2 = new ItemPriceCount((short)10, (short)5);
                sacredOrbItemFlag = shopItem2Flag;
            }
            else { // if(shopItem3.contains("Sacred Orb")) {
                newShopItem3 = "Weights";
                newItemPriceAndCount3 = new ItemPriceCount((short)10, (short)5);
                sacredOrbItemFlag = shopItem3Flag;
            }
            writeShopInventory(noOrbShopBlock, newShopItem1, newShopItem2, newShopItem3, blocks,
                    newItemPriceAndCount1, newItemPriceAndCount2, newItemPriceAndCount3, shopName, true, npcRandomizer, random);

            String shuffledNpcLocation = Settings.isRandomizeNpcs() ? npcRandomizer.getShopNpcLocation(shopName) : null;
            GameObject originalShopObject = shuffledNpcLocation == null ? null : mapOfNpcLocationToObject.get(shuffledNpcLocation);
            if(originalShopObject == null) {
                // Use old path for computing shop object.
                for(GameObject shopObject : mapOfShopBlockToShopObjects.get(shopBlock.getBlockNumber())) {
                    GameObject shopWithoutOrb = new GameObject(shopObject);
                    shopObject.getObjectContainer().getObjects().add(shopWithoutOrb);

                    shopWithoutOrb.getArgs().set(4, (short)(blocks.size() - 1));
                    shopWithoutOrb.getTestByteOperations().add(new TestByteOperation(sacredOrbItemFlag, ByteOp.FLAG_EQUALS, 2));

                    shopObject.getTestByteOperations().add(new TestByteOperation(sacredOrbItemFlag, ByteOp.FLAG_LT, 2));
                }
            }
            else {
                GameObject shopWithoutOrb = new GameObject(originalShopObject);
                originalShopObject.getObjectContainer().getObjects().add(shopWithoutOrb);

                shopWithoutOrb.getArgs().set(4, (short)(blocks.size() - 1));
                shopWithoutOrb.getTestByteOperations().add(new TestByteOperation(sacredOrbItemFlag, ByteOp.FLAG_EQUALS, 2));

                originalShopObject.getTestByteOperations().add(new TestByteOperation(sacredOrbItemFlag, ByteOp.FLAG_LT, 2));
            }
        }

        shopBlock.getInventoryItemArgsList().getData().clear();
        shopBlock.getInventoryItemArgsList().getData().add(getInventoryItemArg(shopItem1));
        shopBlock.getInventoryItemArgsList().getData().add(getInventoryItemArg(shopItem2));
        shopBlock.getInventoryItemArgsList().getData().add(getInventoryItemArg(shopItem3));

        List<Short> newCounts  = new ArrayList<>();
        List<Short> newPrices  = new ArrayList<>();
        updatePriceAndCount(newPrices, newCounts, random, shopItem1, itemPriceAndCount1,
                shopBlock.getInventoryPriceList().getData().get(0), shopBlock.getInventoryCountList().getData().get(0));
        updatePriceAndCount(newPrices, newCounts, random, shopItem2, itemPriceAndCount2,
                shopBlock.getInventoryPriceList().getData().get(1), shopBlock.getInventoryCountList().getData().get(1));
        updatePriceAndCount(newPrices, newCounts, random, shopItem3, itemPriceAndCount3,
                shopBlock.getInventoryPriceList().getData().get(2), shopBlock.getInventoryCountList().getData().get(2));

        shopBlock.getInventoryPriceList().getData().clear();
        shopBlock.getInventoryPriceList().getData().addAll(newPrices);

        shopBlock.getInventoryCountList().getData().clear();
        shopBlock.getInventoryCountList().getData().addAll(newCounts);

        shopBlock.getFlagList().getData().clear();
        shopBlock.getFlagList().getData().add(shopItem1Flag);
        shopBlock.getFlagList().getData().add(shopItem2Flag);
        shopBlock.getFlagList().getData().add(shopItem3Flag);

        if(ShopRandomizer.LITTLE_BROTHER_SHOP_NAME.equals(shopName)) {
            // Little Brother's shop
            if(!"Weights".equals(shopItem1) && !shopItem1.endsWith("Ammo")) {
                AddObject.addLittleBrotherShopTimer(shopItem1Flag);
            }
            if(!"Weights".equals(shopItem2) && !shopItem2.endsWith("Ammo")) {
                AddObject.addLittleBrotherShopTimer(shopItem2Flag);
            }
            if(!"Weights".equals(shopItem3) && !shopItem3.endsWith("Ammo")) {
                AddObject.addLittleBrotherShopTimer(shopItem3Flag);
            }
        }
        else {
            shopBlock.getExitFlagList().getData().clear();
            shopBlock.getExitFlagList().getData().add(shopItem1Flag);
            shopBlock.getExitFlagList().getData().add(shopItem2Flag);
            shopBlock.getExitFlagList().getData().add(shopItem3Flag);
        }

        if(ShopRandomizer.MSX_SHOP_NAME.equals(shopName)) {
            // MSX2 shop
            BlockStringData blockStringData = shopBlock.getString(6);
            blockStringData.getData().clear();
            blockStringData.getData().addAll(Arrays.asList((short)70, (short)8, (short)297, (short)315, (short)308, (short)321,
                    (short)318, (short)326, (short)32, (short)320, (short)328, (short)310, (short)315, (short)264));
        }

        if(!recursive && Settings.isAutomaticMantras()) {
            if("Key Sword".equals(shopItem1) || "Key Sword".equals(shopItem2) || "Key Sword".equals(shopItem3)) {
                AddObject.addAutomaticMantrasTimer(mapOfShopBlockToShopObjects.get(shopBlock.getBlockNumber()).get(0).getObjectContainer());
            }
        }

        updateAskItemName(shopBlock.getString(3), shopItem1);
        updateAskItemName(shopBlock.getString(4), shopItem2);
        updateAskItemName(shopBlock.getString(5), shopItem3);

        List<Short> bunemonData = shopBlock.getBunemonText().getData();
        bunemonData.clear();
        updateBunemonText(bunemonData, shopItem1, shopBlock.getInventoryPriceList().getData().get(0));
        bunemonData.add((short)32);
        bunemonData.add((short)262);
        bunemonData.add((short)32);
        updateBunemonText(bunemonData, shopItem2, shopBlock.getInventoryPriceList().getData().get(1));
        bunemonData.add((short)32);
        bunemonData.add((short)262);
        bunemonData.add((short)32);
        updateBunemonText(bunemonData, shopItem3, shopBlock.getInventoryPriceList().getData().get(2));
    }

    public static void updatePriceAndCount(List<Short> newPrices, List<Short> newCounts, Random random,
                                           String shopItem, ItemPriceCount existingItemPriceAndCount,
                                           Short originalLocationPrice, Short originalLocationCount) {
        if(existingItemPriceAndCount == null) {
            if(!Settings.isFools2019Mode()) {
                newPrices.add(originalLocationPrice);
            }

            if ("Weights".equals(shopItem)) {
                if(Settings.isFools2019Mode()) {
                    newCounts.add((short)(random.nextInt(10) + 1));
                    newPrices.add((short)1);
                }
                else {
                    newCounts.add((short) 5);
                }
            }
            else if (shopItem.endsWith("Ammo")) {
                newCounts.add(originalLocationCount);
                if(Settings.isFools2019Mode()) {
                    newPrices.add(originalLocationPrice);
                }
            } else {
                newCounts.add((short) 1);
                if(Settings.isFools2019Mode()) {
                    newPrices.add(originalLocationPrice);
                }
            }
        }
        else {
            if(existingItemPriceAndCount.getPrice() == null) {
                newPrices.add(originalLocationPrice);
            }
            else {
                newPrices.add(existingItemPriceAndCount.getPrice());
            }

            if(existingItemPriceAndCount.getCount() == null) {
                newCounts.add(originalLocationCount);
            }
            else {
                newCounts.add(existingItemPriceAndCount.getCount());
            }
        }
    }

    private static void updateAskItemName(BlockStringData blockStringData, String shopItem) {
        if(blockStringData.getItemNameStartIndex() == null || blockStringData.getItemNameEndIndex() == null) {
            return;
        }

        List<Short> newBlockData = new ArrayList<>(blockStringData.getData().subList(0, blockStringData.getItemNameStartIndex()));
        if(Settings.isFools2020Mode()) {
            if("Scriptures".equals(shopItem)) {
                newBlockData.addAll(FileUtils.stringToData(Translations.getText("items.HeatproofCase")));
            }
            else if("Heatproof Case".equals(shopItem)) {
                newBlockData.addAll(FileUtils.stringToData(Translations.getText("items.Scriptures")));
            }
            else {
                if("Map (Shrine of the Mother)".equals(shopItem)) {
                    if("jp".equals(Settings.getLanguage())) {
                        // 聖母の祠の
                        newBlockData.add((short)1027);
                        newBlockData.add((short)882);
                        newBlockData.add((short)386);
                        newBlockData.add((short)975);
                        newBlockData.add((short)386);
                    } else {
                        newBlockData.add((short)296);
                        newBlockData.add((short)315);
                        newBlockData.add((short)325);
                        newBlockData.add((short)316);
                        newBlockData.add((short)321);
                        newBlockData.add((short)312);
                        newBlockData.add((short)32);
                    }
                }
                newBlockData.add((short)77);
                newBlockData.add(getInventoryItemArg(shopItem));
            }
        }
        else {
            if("Map (Shrine of the Mother)".equals(shopItem)) {
                if("jp".equals(Settings.getLanguage())) {
                    // 聖母の祠の
                    newBlockData.add((short)1027);
                    newBlockData.add((short)882);
                    newBlockData.add((short)386);
                    newBlockData.add((short)975);
                    newBlockData.add((short)386);
                } else {
                    newBlockData.add((short)296);
                    newBlockData.add((short)315);
                    newBlockData.add((short)325);
                    newBlockData.add((short)316);
                    newBlockData.add((short)321);
                    newBlockData.add((short)312);
                    newBlockData.add((short)32);
                }
            }
            newBlockData.add((short)77);
            newBlockData.add(getInventoryItemArg(shopItem));
        }
        newBlockData.addAll(blockStringData.getData().subList(blockStringData.getItemNameEndIndex(), blockStringData.getData().size()));
        blockStringData.getData().clear();
        blockStringData.getData().addAll(newBlockData);
    }

    private static void addShrinePrefixIfNeeded(List<Short> bunemonData, String shopItem) {
        if("Map (Shrine of the Mother)".equals(shopItem)) {
            if("jp".equals(Settings.getLanguage())) {
                // 聖母の祠の
                bunemonData.add((short)1027);
                bunemonData.add((short)882);
                bunemonData.add((short)386);
                bunemonData.add((short)975);
                bunemonData.add((short)386);
            } else {
                bunemonData.add((short)296);
                bunemonData.add((short)315);
                bunemonData.add((short)325);
                bunemonData.add((short)316);
                bunemonData.add((short)321);
                bunemonData.add((short)312);
                bunemonData.add((short)32);
            }
        }
    }

    private static void updateBunemonText(List<Short> bunemonData, String shopItem, Short itemPrice) {
        if(Settings.isFools2020Mode()) {
            if("Scriptures".equals(shopItem)) {
                bunemonData.addAll(FileUtils.stringToData(Translations.getText("items.HeatproofCase")));
            }
            else if("Heatproof Case".equals(shopItem)) {
                bunemonData.addAll(FileUtils.stringToData(Translations.getText("items.Scriptures")));
            }
            else {
                addShrinePrefixIfNeeded(bunemonData, shopItem);
                bunemonData.add((short)77);
                bunemonData.add(getInventoryItemArg(shopItem));
            }
        }
        else {
            addShrinePrefixIfNeeded(bunemonData, shopItem);
            bunemonData.add((short)77);
            bunemonData.add(getInventoryItemArg(shopItem));
        }

        bunemonData.add((short)32);
        for(char c : Short.toString(itemPrice).toCharArray()) {
            if(c == '0') {
                bunemonData.add((short)266);
            }
            else if(c == '1') {
                bunemonData.add((short)267);
            }
            else if(c == '2') {
                bunemonData.add((short)268);
            }
            else if(c == '3') {
                bunemonData.add((short)269);
            }
            else if(c == '4') {
                bunemonData.add((short)270);
            }
            else if(c == '5') {
                bunemonData.add((short)271);
            }
            else if(c == '6') {
                bunemonData.add((short)272);
            }
            else if(c == '7') {
                bunemonData.add((short)273);
            }
            else if(c == '8') {
                bunemonData.add((short)274);
            }
            else if(c == '9') {
                bunemonData.add((short)275);
            }
        }
    }

    public static void writeTransitionGate(String gateToUpdate, String gateDestination, boolean isEndlesL1Destination) {
        List<GameObject> objectsToModify = mapOfGateNameToTransitionGate.get(gateToUpdate);
        if(objectsToModify != null) {
            boolean firstObject = true;
            boolean updateFirstObject = false; // This is to address a problem where firstObject was updated too soon and we failed to check independent if cases - https://github.com/thezerothcat/LaMulanaRandomizer/issues/100
            for (GameObject gameObject : objectsToModify) {
                replaceTransitionGateArgs(gameObject, gateDestination); // First update the transitions so we can make a correct copy of the gate if needed.
                if(gateDestination.startsWith("Transition: Shrine")
                        && !"Transition: Shrine D3".equals(gateDestination)
                        && gameObject.getTestByteOperations().get(0).getValue() != 1) {
                    // Copy and add true shrine gate before updating flags, since flag update will complicate escape gate vs not-escape gate.
                    AddObject.addTrueShrineGate(gameObject);
                }
                replaceTransitionGateFlags(gameObject, gateToUpdate, gateDestination); // Update flags on the gate, as needed.

                if(!"Transition: Sun R2".equals(gateToUpdate) && !"Transition: Extinction L2".equals(gateToUpdate)) {
                    // Both of these screen edges have more than one door; the screen transition is based on Sun R1 and Extinction L1, respectively
                    updateScreenTransition(gameObject, gateDestination);
                }
                if(firstObject && "Transition: Illusion R2".equals(gateToUpdate)) {
                    // Block leading out of Illusion
                    AddObject.addIllusionFruitBlockHorizontal(gameObject, true);
                    updateFirstObject = true;
                }
                if(firstObject
                        && ("Transition: Illusion R1".equals(gateDestination)
                        || "Transition: Illusion R2".equals(gateDestination))) {
                    // Block leading into Illusion
                    AddObject.addIllusionFruitBlockHorizontal(gameObject, "Transition: Illusion R2".equals(gateDestination));
                    updateFirstObject = true;
                }
                if(firstObject && "Transition: Illusion D1".equals(gateDestination)) {
                    // Block leading into Illusion
                    AddObject.addIllusionFruitBlockVertical(gameObject);
                    updateFirstObject = true;
                }
                if(firstObject && Settings.isFools2021Mode() && "Transition: Guidance L1".equals(gateDestination)) {
                    // Add timer for fools' rando 2021 to ensure a flag sequence behaves as intended.
                    // Ensure the process of falling blocks triggered by Pepper is reset if unfinished.
                    AddObject.addFramesTimer(gameObject.getObjectContainer(), 0,
                            Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_GT, 0),
                                    new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_LT, 5)),
                            Arrays.asList(new WriteByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.ASSIGN_FLAG, 1)));
                    updateFirstObject = true;
                }
                if(firstObject && "Transition: Sun L1".equals(gateDestination)) {
                    // Timer on the other side of the gate leading into Buer's room, to mark that the ceiling can now be broken (not allowed during the boss fight).
                    AddObject.addIsisRoomCeilingTimer(gameObject.getObjectContainer());
                    updateFirstObject = true;
                }
                if(firstObject && "Transition: Goddess L2".equals(gateDestination)) {
                    // Timer on the other side of the gate leading into the lower of the left Goddess exits, to mark that the statue should be removed.
                    AddObject.addGoddessStatueLemezaDetector(gameObject);
                    updateFirstObject = true;
                }
                if(firstObject && "Transition: Goddess D1".equals(gateDestination)) {
                    // Timer on the other side of the gate leading into the shield statue's room in Goddess, marking that the shield has been thrown and the statue should not exist upon returning.
                    AddObject.addGoddessShieldTimer(gameObject.getObjectContainer());
                    updateFirstObject = true;
                }
                if(firstObject && "Transition: Birth R1".equals(gateDestination)) {
                    // Block leading into Skanda's room.
                    AddObject.addSkandaBlock(gameObject);
                    updateFirstObject = true;
                }
                if(firstObject && gateDestination.contains("Transition: Twin ") && !gateToUpdate.equals("Transition: Twin U2")) {
                    // For every transition gate leading into Twin Labyrinths, the poison timer (but not the puzzle itself) will be reset,
                    // UNLESS this is a transition from the screen with the first dais into some other screen of Twin Labyrinths.
                    AddObject.addTwinLabsPoisonTimerRemoval(gameObject.getObjectContainer(), false);
                    updateFirstObject = true;
                }
                if(firstObject && isEndlesL1Destination) {
                    // Detector on the other side of the gate coming out of Endless corridor, to open the Map chest in Endless Corridor.
                    AddObject.addEndlessCorridorLeftExitLemezaDetector(gameObject);
                    updateFirstObject = true;
                }
                if(firstObject && !LocationCoordinateMapper.isSurfaceStart() && "Transition: Surface R1".equals(gateToUpdate)) {
                    // Transition gate leading from Surface into some other location. If non-random start, the player may be trapped in the un-opened entryway
                    // and forcibly raindropped somewhere unintended if a warp is not provided before the passageway has been opened on the Surface side.
                    GameObject warp = AddObject.addWarp((Screen)gameObject.getObjectContainer(), 1220, 340, 4, 7, 0, 0, 0, 20, 312);
                    replaceTransitionGateArgs(warp, gateDestination); // First update the transitions so we can make a correct copy of the gate if needed.

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
        Screen screen = mapOfTransitionNameToScreen.get(gateToUpdate);
        if(screen != null) {
            updateScreenTransition(screen, gateDestination);
        }
    }

    private static ScreenExit getScreenExit(String gateDestination) {
        ScreenExit screenExit = new ScreenExit();
        if("Transition: Surface R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)(Settings.isHalloweenMode() ? 22 : 1));
            screenExit.setRoomIndex((byte)11);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Surface D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)(Settings.isHalloweenMode() ? 22 : 1));
            screenExit.setRoomIndex((byte)4);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Surface D2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)(Settings.isHalloweenMode() ? 22 : 1));
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Guidance L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)0);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Guidance U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)0);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Guidance D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)0);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Guidance D2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)0);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Mausoleum L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)2);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Mausoleum U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)2);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Mausoleum D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)2);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Sun L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)3);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Sun R1".equals(gateDestination) || "Transition: Sun R2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)3);
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Sun U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)3);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Spring D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)4);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Inferno R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)5);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Inferno U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)5);
            screenExit.setRoomIndex((byte)1);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Inferno U2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)5);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Inferno W1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)5);
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Extinction L1".equals(gateDestination) || "Transition: Extinction L2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)6);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Extinction U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)6);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Extinction U2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)6);
            screenExit.setRoomIndex((byte)1);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Extinction U3".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)6);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Twin U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)7);
            screenExit.setRoomIndex((byte)3);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Twin U2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)7);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Twin U3".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)7);
            screenExit.setRoomIndex((byte)10);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Twin D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)7);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Twin D2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)7);
            screenExit.setRoomIndex((byte)16);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Endless R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)8);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Endless U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)8);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)3);
        }
        else if("Transition: Endless D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)8);
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)3);
        }
        else if("Transition: Shrine U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)9);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Shrine D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)9);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Shrine D2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)9);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Shrine D3".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)9);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Illusion R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)10);
            screenExit.setRoomIndex((byte)1);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Illusion R2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)10);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Illusion D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)10);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Illusion D2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)10);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Graveyard L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)11);
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Graveyard R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)11);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Graveyard U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)11);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Graveyard U2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)11);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Graveyard D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)11);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Moonlight L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)12);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Moonlight U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)12);
            screenExit.setRoomIndex((byte)1);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Moonlight U2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)12);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Goddess L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Goddess L2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Goddess U1".equals(gateDestination) || "Transition: Goddess W1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)4);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Goddess D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Ruin L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)14);
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Ruin R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)14);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Ruin R2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)14);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Birth L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)15);
            screenExit.setRoomIndex((byte)3);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Birth R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)16);
            screenExit.setRoomIndex((byte)3);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Birth U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)15);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Birth D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)16);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Dimensional D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)17);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Retromausoleum U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)19);
            screenExit.setRoomIndex((byte)4);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Retromausoleum D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)19);
            screenExit.setRoomIndex((byte)1);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Retroguidance L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)20);
            screenExit.setRoomIndex((byte)4);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Retroguidance D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)20);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Retrosurface R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)21);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Pipe L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Pipe R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)0);
        }
        return screenExit;
    }

    private static void replaceTransitionGateArgs(GameObject gameObject, String gateDestination) {
        if("Transition: Surface R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)(Settings.isHalloweenMode() ? 22 : 1));
            gameObject.getArgs().set(1, (short)11);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Surface D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)(Settings.isHalloweenMode() ? 22 : 1));
            gameObject.getArgs().set(1, (short)4);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)440);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Surface D2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)(Settings.isHalloweenMode() ? 22 : 1));
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)440);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Guidance L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)0);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Guidance U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)0);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Guidance D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)0);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Guidance D2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)0);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Mausoleum L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)2);
            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Mausoleum U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)2);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Mausoleum D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)2);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)480);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Sun L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)3);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Sun R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)3);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)72);
        }
        else if("Transition: Sun R2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)3);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)232);
        }
        else if("Transition: Sun U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)3);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Spring D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)4);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Inferno R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)5);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Inferno U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)5);
            gameObject.getArgs().set(1, (short)1);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Inferno U2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)5);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)440);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Inferno W1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)5);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)40);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Extinction L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)6);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)72);
        }
        else if("Transition: Extinction L2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)6);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)232);
        }
        else if("Transition: Extinction U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)6);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Extinction U2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)6);
            gameObject.getArgs().set(1, (short)1);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)440);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Extinction U3".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)6);
            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)40);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Twin U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)7);
            gameObject.getArgs().set(1, (short)3);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)480);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Twin U2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)7);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Twin U3".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)7);
            gameObject.getArgs().set(1, (short)10);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)120);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Twin D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)7);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Twin D2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)7);
            gameObject.getArgs().set(1, (short)16);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Endless R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)8);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Endless U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)8);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)3);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Endless D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)8);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)3);
            gameObject.getArgs().set(3, (short)420);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Shrine U1".equals(gateDestination)) {
            if(gameObject.getTestByteOperations().get(0).getValue() == 1) {
                // Escape door
                gameObject.getArgs().set(0, (short)18);
            }
            else {
                // Non-escape door
                gameObject.getArgs().set(0, (short)9);
            }

            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)420);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Shrine D1".equals(gateDestination)) {
            if(gameObject.getTestByteOperations().get(0).getValue() == 1) {
                // Escape door
                gameObject.getArgs().set(0, (short)18);
            }
            else {
                // Non-escape door
                gameObject.getArgs().set(0, (short)9);
            }

            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Shrine D2".equals(gateDestination)) {
            if(gameObject.getTestByteOperations().get(0).getValue() == 1) {
                // Escape door
                gameObject.getArgs().set(0, (short)18);
            }
            else {
                // Non-escape door
                gameObject.getArgs().set(0, (short)9);
            }

            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Shrine D3".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)9);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Illusion R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)10);
            gameObject.getArgs().set(1, (short)1);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Illusion R2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)10);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Illusion D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)10);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Illusion D2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)10);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)120);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Graveyard L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)11);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Graveyard R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)11);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)232);
        }
        else if("Transition: Graveyard U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)11);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Graveyard U2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)11);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Graveyard D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)11);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)500);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Moonlight L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)12);
            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)232);
        }
        else if("Transition: Moonlight U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)12);
            gameObject.getArgs().set(1, (short)1);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)120);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Moonlight U2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)12);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Goddess L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Goddess L2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Goddess U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)4);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)80);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Goddess D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Goddess W1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)4);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)500);
            gameObject.getArgs().set(4, (short)40);
        }
        else if("Transition: Ruin L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)14);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Ruin R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)14);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Ruin R2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)14);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Birth L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)15);
            gameObject.getArgs().set(1, (short)3);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Birth R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)16);
            gameObject.getArgs().set(1, (short)3);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Birth U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)15);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)500);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Birth D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)16);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)80);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Dimensional D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)17);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)120);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Retromausoleum U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)19);
            gameObject.getArgs().set(1, (short)4);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Retromausoleum D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)19);
            gameObject.getArgs().set(1, (short)1);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)480);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Retroguidance L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)20);
            gameObject.getArgs().set(1, (short)4);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Retroguidance D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)20);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Retrosurface R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)21);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Pipe L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)232);
        }
        else if("Transition: Pipe R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)232);
        }
    }

    private static void updateScreenTransition(GameObject transitionGate, String gateDestination) {
        int screenExitIndex = 0;
        if(gateDestination.equals("Transition: Goddess W1")) {
            screenExitIndex = 2;
        }
        else if(gateDestination.equals("Transition: Inferno W1")) {
            screenExitIndex = 0;
        }
        else {
            char transitionDirection = gateDestination.charAt(gateDestination.length() - 2);
            if (transitionDirection == 'U') {
                screenExitIndex = 2;
            } else if (transitionDirection == 'R') {
                screenExitIndex = 3;
            } else if (transitionDirection == 'D') {
                screenExitIndex = 0;
            } else if (transitionDirection == 'L') {
                screenExitIndex = 1;
            }
        }

        ScreenExit screenExit = new ScreenExit();
        byte zoneIndex = transitionGate.getArgs().get(0).byteValue();
        if(zoneIndex == 18) {
            screenExit.setZoneIndex((byte)9);
        }
        else if(Settings.isHalloweenMode() && zoneIndex == 1) {
            // Night Surface, not normal Surface. Not necessary if the gate is already up to date, but checking for safety anyway.
            screenExit.setZoneIndex((byte)22);
        }
        else {
            screenExit.setZoneIndex(zoneIndex);
        }
        screenExit.setRoomIndex(transitionGate.getArgs().get(1).byteValue());
        screenExit.setScreenIndex(transitionGate.getArgs().get(2).byteValue());
        ((Screen)transitionGate.getObjectContainer()).getScreenExits().set(screenExitIndex, screenExit);
    }

    private static void replaceTransitionGateFlags(GameObject gameObject, String gateToUpdate, String gateDestination) {
        if(gateToUpdate.equals("Transition: Illusion R2")) {
            // Add extra check for Fruit of Eden placed.
            gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.EDEN_UNLOCKED, ByteOp.FLAG_NOT_EQUAL, 0));
        }

        if(gateDestination.startsWith("Transition: Shrine") && !"Transition: Shrine D3".equals(gateDestination)) {
            TestByteOperation testByteOperation = gameObject.getTestByteOperations().get(0);
            if(testByteOperation.getValue() != 1) {
                // Non-escape door
                testByteOperation.setIndex(FlagConstants.BOSSES_SHRINE_TRANSFORM);
                testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                testByteOperation.setValue((byte)9);

                // Add extra check for not during escape, since escape door is different.
                gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_NOT_EQUAL, 1));
            }
        }
        else if(gateDestination.equals("Transition: Illusion R1")
                || gateDestination.equals("Transition: Illusion R2")) {
            // Add extra check for Fruit of Eden placed.
            gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.EDEN_UNLOCKED, ByteOp.FLAG_NOT_EQUAL, 0));
        }
    }

    private static void updateScreenTransition(Screen screen, String gateDestination) {
        int screenExitIndex = 0;
        if(gateDestination.equals("Transition: Goddess W1")) {
            screenExitIndex = 2;
        }
        else if(gateDestination.equals("Transition: Inferno W1")) {
            screenExitIndex = 0;
        }
        else {
            char transitionDirection = gateDestination.charAt(gateDestination.length() - 2);
            if (transitionDirection == 'U') {
                screenExitIndex = 2;
            } else if (transitionDirection == 'R') {
                screenExitIndex = 3;
            } else if (transitionDirection == 'D') {
                screenExitIndex = 0;
            } else if (transitionDirection == 'L') {
                screenExitIndex = 1;
            }
        }

        screen.getScreenExits().set(screenExitIndex, getScreenExit(gateDestination));
    }

    public static void writeBacksideDoor(String doorToReplace, String doorWithCoordinateData, int bossNumber) {
        int bossFlag = getBossFlag(bossNumber);
        int gateFlag = getGateFlag(bossNumber);
        int mirrorCoverFlag = getMirrorCoverFlag(bossNumber);
        List<GameObject> objectsToModify = mapOfDoorNameToBacksideDoor.get(doorToReplace);
        for(GameObject gameObject : objectsToModify) {
            if(gameObject.getId() == 0x98) {
                replaceBacksideDoorFlags(gameObject, bossFlag, gateFlag, "Door: B5".equals(doorToReplace));
                replaceBacksideDoorArgs(gameObject, doorWithCoordinateData);
                AddObject.addBossNumberGraphic(gameObject, bossNumber, mirrorCoverFlag);
            }
            else if(gameObject.getId() == 0x0b) {
                if(isMirrorFlagObject(gameObject)) {
                    replaceMirrorCoverTimerFlags(gameObject, mirrorCoverFlag);
                }
                else {
                    replaceBacksideDoorTimerFlags(gameObject, bossFlag, gateFlag, "Door: B4".equals(doorToReplace));
                }
            }
            else if(gameObject.getId() == 0x93) {
                if(isMirrorFlagObject(gameObject)) {
                    replaceMirrorCoverGraphicFlags(gameObject, mirrorCoverFlag);
                }
                else {
                    replaceBacksideDoorCoverFlags(gameObject, gateFlag);
                }
            }
        }
    }

    private static boolean isMirrorFlagObject(GameObject gameObject) {
        for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
            if(testByteOperation.getIndex() == FlagConstants.AMPHISBAENA_GATE_MIRROR_COVER || testByteOperation.getIndex() == FlagConstants.SAKIT_GATE_MIRROR_COVER
                    || testByteOperation.getIndex() == FlagConstants.ELLMAC_GATE_MIRROR_COVER || testByteOperation.getIndex() == FlagConstants.BAHAMUT_GATE_MIRROR_COVER
                    || testByteOperation.getIndex() == FlagConstants.VIY_GATE_MIRROR_COVER || testByteOperation.getIndex() == FlagConstants.PALENQUE_GATE_MIRROR_COVER
                    || testByteOperation.getIndex() == FlagConstants.BAPHOMET_GATE_MIRROR_COVER) {
                return true;
            }
        }
        return false;
    }

    public static void writeBacksideDoorV2(String doorToReplace, String doorWithCoordinateData, Integer bossNumber) {
        Integer gateFlag = getGateFlag(bossNumber);
        List<GameObject> objectsToModify = mapOfDoorNameToBacksideDoor.get(doorToReplace);
        if(objectsToModify == null) {
            if("Door: F8".equals(doorWithCoordinateData)) {
                AddObject.addGrailToggle(null, true);
            }
        }
        else {
            for(GameObject gameObject : objectsToModify) {
                replaceBacksideDoorFlags(gameObject, bossNumber, gateFlag, isDoorDisabledForEscape(doorToReplace));
                replaceBacksideDoorArgs(gameObject, doorWithCoordinateData);
                if("Door: F8".equals(doorWithCoordinateData)) {
                    AddObject.addGrailToggle(gameObject.getObjectContainer(), true);
                }
                AddObject.addNumberlessBacksideDoorGraphic(gameObject);
                if(bossNumber != null) {
                    if(bossNumber == 9) {
                        AddObject.addKeyFairyDoorTimerAndSounds(gameObject.getObjectContainer());
                        AddObject.addBacksideDoorKeyFairyPoint(gameObject);
                        AddObject.addAnimatedDoorCover(gameObject, FlagConstants.KEY_FAIRY_DOOR_UNLOCKED, false);
                        AddObject.addBossNumberGraphicV2(gameObject, bossNumber, null);
                    }
                    else {
                        int mirrorCoverFlag = getMirrorCoverFlag(bossNumber);
                        AddObject.addAnimatedDoorTimerAndSound(gameObject.getObjectContainer(), getBossFlag(bossNumber), gateFlag);
                        AddObject.addMirrorCoverTimerAndSound(gameObject.getObjectContainer(), mirrorCoverFlag);
                        AddObject.addMirrorCoverGraphic(gameObject, mirrorCoverFlag);
                        AddObject.addAnimatedDoorCover(gameObject, gateFlag, true);
                        AddObject.addBossNumberGraphicV2(gameObject, bossNumber, mirrorCoverFlag);
                    }
                }
            }
        }
    }

    public static void writeSeals(String sealNode, short sealNumber) {
        if(mapOfSealNodeToSealObjects.containsKey(sealNode)) {
            List<GameObject> seals = mapOfSealNodeToSealObjects.get(sealNode);
            for (GameObject sealToUpdate : seals) {
                sealToUpdate.getArgs().set(0, sealNumber);
            }
        }
        else {
            FileUtils.logFlush("Unable to update seal objects for " + sealNode);
        }
    }

    public static void writeNpcDoor(String npcDoorLocation, String npc) {
        GameObject doorToUpdate = mapOfNpcLocationToObject.get(npcDoorLocation);
        NpcObjectUpdates.updateDoor(doorToUpdate, npcDoorLocation, npc);
    }

    private static Integer getBossFlag(Integer bossNumber) {
        if(bossNumber == null) {
            return null;
        }
        if(bossNumber == 1) {
            return FlagConstants.AMPHISBAENA_STATE;
        }
        if(bossNumber == 2) {
            return FlagConstants.SAKIT_STATE;
        }
        if(bossNumber == 3) {
            return FlagConstants.ELLMAC_STATE;
        }
        if(bossNumber == 4) {
            return FlagConstants.BAHAMUT_STATE;
        }
        if(bossNumber == 5) {
            return FlagConstants.VIY_STATE;
        }
        if(bossNumber == 6) {
            return FlagConstants.PALENQUE_STATE;
        }
        if(bossNumber == 7) {
            return FlagConstants.BAPHOMET_STATE;
        }
//        if(bossNumber == 8) {
//            return FlagConstants.TIAMAT_STATE;
//        }
        if(bossNumber == 9) {
            return FlagConstants.KEY_FAIRY_DOOR_UNLOCKED;
        }
        return null;
    }

    private static Integer getGateFlag(Integer bossNumber) {
        if(bossNumber == null) {
            return null;
        }
        if(bossNumber == 1) {
            return FlagConstants.AMPHISBAENA_GATE_OPEN;
        }
        if(bossNumber == 2) {
            return FlagConstants.SAKIT_GATE_OPEN;
        }
        if(bossNumber == 3) {
            return FlagConstants.ELLMAC_GATE_OPEN;
        }
        if(bossNumber == 4) {
            return FlagConstants.BAHAMUT_GATE_OPEN;
        }
        if(bossNumber == 5) {
            return FlagConstants.VIY_GATE_OPEN;
        }
        if(bossNumber == 6) {
            return FlagConstants.PALENQUE_GATE_OPEN;
        }
        if(bossNumber == 7) {
            return FlagConstants.BAPHOMET_GATE_OPEN;
        }
        return null;
    }

    private static int getMirrorCoverFlag(Integer bossNumber) {
        if(bossNumber == 1) {
            return FlagConstants.AMPHISBAENA_GATE_MIRROR_COVER;
        }
        if(bossNumber == 2) {
            return FlagConstants.SAKIT_GATE_MIRROR_COVER;
        }
        if(bossNumber == 3) {
            return FlagConstants.ELLMAC_GATE_MIRROR_COVER;
        }
        if(bossNumber == 4) {
            return FlagConstants.BAHAMUT_GATE_MIRROR_COVER;
        }
        if(bossNumber == 5) {
            return FlagConstants.VIY_GATE_MIRROR_COVER;
        }
        if(bossNumber == 6) {
            return FlagConstants.PALENQUE_GATE_MIRROR_COVER;
        }
        if(bossNumber == 7) {
            return FlagConstants.BAPHOMET_GATE_MIRROR_COVER;
        }
        return 0;
    }

    private static boolean isDoorDisabledForEscape(String doorToReplace) {
        return "Door: B5".equals(doorToReplace) || "Door: F9".equals(doorToReplace);
    }

    private static short getInventoryItemArg(String item) {
        if("Weights".equals(item)) {
            return 105;
        }
        if("Shuriken Ammo".equals(item)) {
            return 107;
        }
        if("Rolling Shuriken Ammo".equals(item)) {
            return 108;
        }
        if("Earth Spear Ammo".equals(item)) {
            return 109;
        }
        if("Flare Gun Ammo".equals(item)) {
            return 110;
        }
        if("Bomb Ammo".equals(item)) {
            return 111;
        }
        if("Chakram Ammo".equals(item)) {
            return 112;
        }
        if("Caltrops Ammo".equals(item)) {
            return 113;
        }
        if("Pistol Ammo".equals(item)) {
            return 114;
        }
        if("Coin".equals(item)) {
            return 0x06a;
        }
        return DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(item).getInventoryArg();
    }

    private static short getFlag(String item) {
        if("Weights".equals(item)) {
            return 0;
        }
        if(item.endsWith("Ammo")) {
            return 0;
        }
        if("Coin".equals(item)) {
            return 0;
        }
        return (short)DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(item).getWorldFlag();
    }

    public static void writeLocationContents(String chestLocation, String chestContents,
                                             GameObjectId itemLocationData, GameObjectId itemNewContentsData,
                                             int newWorldFlag, Random random, boolean isRandomizeGraphicsAllowed) {
        List<GameObject> objectsToModify = mapOfChestIdentifyingInfoToGameObject.get(itemLocationData);
        if(objectsToModify == null) {
            if(!AddObject.addSpecialItemObjects(chestLocation, chestContents)) {
                FileUtils.log("Unable to find objects related to " + chestLocation);
            }
            return;
        }
        boolean isRemovedItem = itemNewContentsData.getWorldFlag() != newWorldFlag;
        boolean isTrapItem = Settings.isRandomizeTrapItems() && (newWorldFlag == FlagConstants.WF_TRAP_GRAVEYARD || newWorldFlag == FlagConstants.WF_TRAP_INFERNO || newWorldFlag == FlagConstants.WF_TRAP_TWIN);
        boolean randomizeGraphics = isRandomizeGraphicsAllowed && !isRemovedItem && !isTrapItem && !chestContents.startsWith("Coin:")
                && (getCustomItemGraphic(chestContents) != 0 || (isRandomGraphicsAvailable() && random.nextBoolean()));
        Integer itemRandomizeGraphicsFlag = randomizeGraphics ? getRandomizeGraphicsFlag(newWorldFlag) : null;

        for(GameObject objectToModify : objectsToModify) {
            if(objectToModify.getId() == 0x2c) {
                updateChestContents(objectToModify, itemLocationData, itemNewContentsData, chestContents, newWorldFlag,
                        itemRandomizeGraphicsFlag, Settings.getCurrentCursedChests().contains(chestLocation), random);
                AddObject.addSpecialItemObjects(objectToModify.getObjectContainer(), chestContents);
            }
            else if(objectToModify.getId() == 0x2f) {
                // Note: Floating maps don't get removed.
                updateFloatingItemContents(objectToModify, itemLocationData, itemNewContentsData, chestContents, newWorldFlag, itemRandomizeGraphicsFlag, random);
                AddObject.addSpecialItemObjects(objectToModify.getObjectContainer(), chestContents);
            }
            else if(objectToModify.getId() == 0xc3) {
                updateSnapshotsItemContents(objectToModify, itemLocationData, itemNewContentsData);
                AddObject.addSpecialItemObjects(objectToModify.getObjectContainer(), chestContents);
            }
            else if(objectToModify.getId() == 0xb5) {
                updateInstantItemContents(objectToModify, itemLocationData, itemNewContentsData);
                AddObject.addSpecialItemObjects(objectToModify.getObjectContainer(), chestContents);
            }
            else if(objectToModify.getId() == 0xa0) {
                updateRelatedObject(objectToModify, itemLocationData, randomizeGraphics ? itemRandomizeGraphicsFlag : newWorldFlag);
                short blockRef = objectToModify.getArgs().get(4);
                if(blockRef == 673 || blockRef == 689 || blockRef == 691 || blockRef == 693) {
                    // 673 = mekuri.exe
                    // 689 = Pepper
                    // 691 = Anchor
                    // 693 = Mini Doll
                    AddObject.addSpecialItemObjects(objectToModify.getObjectContainer(), chestContents);
                }
            }
            else {
                updateRelatedObject(objectToModify, itemLocationData, randomizeGraphics ? itemRandomizeGraphicsFlag : newWorldFlag);
            }
        }
    }

    private static void updateChestContents(GameObject objectToModify, GameObjectId itemLocationData, GameObjectId itemNewContentsData,
                                            String newChestContentsItemName, int newWorldFlag, Integer itemRandomizeGraphicsFlag, boolean cursed, Random random) {
        if(Settings.isFools2021Mode()) {
            if(itemLocationData.getWorldFlag() == FlagConstants.WF_ANKH_JEWEL_SUN) {
                // Ankh Jewel (Temple of the Sun)
                itemNewContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Spaulder");
                newChestContentsItemName = "Spaulder";
                newWorldFlag = 0xabe;
            }
            if(itemLocationData.getWorldFlag() == FlagConstants.WF_BRONZE_MIRROR) {
                // Bronze Mirror
                itemNewContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Spaulder");
                newChestContentsItemName = "Spaulder";
                newWorldFlag = 0xabf;
            }
            if(itemLocationData.getWorldFlag() == FlagConstants.WF_ISIS_PENDANT) {
                // Isis' Pendant
                itemNewContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Spaulder");
                newChestContentsItemName = "Spaulder";
                newWorldFlag = 0xac0;
            }
            if(itemLocationData.getWorldFlag() == FlagConstants.WF_MAP_SUN) {
                // Map (Temple of the Sun)
                itemNewContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Spaulder");
                newChestContentsItemName = "Spaulder";
                newWorldFlag = 0xac2;
            }
            if(itemLocationData.getWorldFlag() == FlagConstants.WF_SACRED_ORB_SUN) {
                // Sacred Orb (Temple of the Sun)
                itemNewContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Spaulder");
                newChestContentsItemName = "Spaulder";
                newWorldFlag = 0xac3;
            }
            if(itemLocationData.getWorldFlag() == FlagConstants.WF_COIN_SUN) {
                // Coin: Sun (Pyramid)
                itemNewContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Spaulder");
                newChestContentsItemName = "Spaulder";
                newWorldFlag = 0xac5;
            }
            if(itemLocationData.getWorldFlag() == FlagConstants.WF_MAP_SHRINE) {
                // Map (Shrine of the Mother)
                itemNewContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Spaulder");
                newChestContentsItemName = "Spaulder";
                newWorldFlag = 0xac6;
            }
        }

        WriteByteOperation puzzleFlag = objectToModify.getWriteByteOperations().get(1);
        objectToModify.getWriteByteOperations().clear();

        if(newChestContentsItemName.startsWith("Coin:")) {
            objectToModify.getArgs().set(0, (short)1); // Coins
            objectToModify.getArgs().set(1, itemNewContentsData.getInventoryArg()); // Re-purposing inventory arg to track coin amount
            if(Settings.isRandomizeGraphics()) {
                objectToModify.getArgs().set(2, (short)random.nextInt(2));
            }
            else {
                objectToModify.getArgs().set(2, (short)0); // Brown chest
            }

            for (TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
                if (flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                    flagTest.setIndex(itemNewContentsData.getWorldFlag());
                }
            }

            WriteByteOperation updateFlag = new WriteByteOperation();
            updateFlag.setOp(ByteOp.ASSIGN_FLAG);
            updateFlag.setIndex(itemNewContentsData.getWorldFlag());
            updateFlag.setValue(2);
            objectToModify.getWriteByteOperations().add(updateFlag);

            objectToModify.getWriteByteOperations().add(puzzleFlag);

            updateFlag = new WriteByteOperation();
            updateFlag.setOp(ByteOp.ASSIGN_FLAG);
            updateFlag.setIndex(itemNewContentsData.getWorldFlag());
            updateFlag.setValue(2);
            objectToModify.getWriteByteOperations().add(updateFlag);

            updateFlag = new WriteByteOperation();
            updateFlag.setOp(ByteOp.ADD_FLAG);
            updateFlag.setIndex(FlagConstants.TOTAL_COIN_CHESTS); // Coin chest tracking
            updateFlag.setValue(1);
            objectToModify.getWriteByteOperations().add(updateFlag);
        }
        else if(newChestContentsItemName.startsWith("Trap:")) {
            if(Settings.isRandomizeGraphics()) {
                objectToModify.getArgs().set(2, (short)random.nextInt(2)); // Random graphics
            }
            else if(Settings.isCoinChestGraphics()) {
                objectToModify.getArgs().set(2, (short)0);
            }
            else {
                objectToModify.getArgs().set(2, (short)1);
            }

            for (TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
                if (flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                    flagTest.setIndex(itemNewContentsData.getWorldFlag());
                }
            }

            WriteByteOperation updateFlag = new WriteByteOperation();
            updateFlag.setOp(ByteOp.ASSIGN_FLAG);
            updateFlag.setIndex(newWorldFlag);
            updateFlag.setValue(newWorldFlag == FlagConstants.WF_TRAP_ILLUSION ? 2 : 1);
            objectToModify.getWriteByteOperations().add(updateFlag);

            objectToModify.getWriteByteOperations().add(puzzleFlag);

            if(newWorldFlag == FlagConstants.WF_TRAP_ILLUSION) {
                objectToModify.getArgs().set(0, (short)0); // Nothing
                objectToModify.getArgs().set(1, (short)1); // Quantity is irrelevant
                if(Settings.isRandomizeGraphics()) {
                    objectToModify.getArgs().set(2, (short)random.nextInt(2)); // Random graphics
                }
                else if(Settings.isCoinChestGraphics()) {
                    objectToModify.getArgs().set(2, (short)0);
                }
                else {
                    objectToModify.getArgs().set(2, (short)1);
                }

                updateFlag = new WriteByteOperation();
                updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                updateFlag.setIndex(newWorldFlag);
                updateFlag.setValue(1);
                objectToModify.getWriteByteOperations().add(updateFlag);

                updateFlag = new WriteByteOperation();
                updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                updateFlag.setIndex(newWorldFlag);
                updateFlag.setValue(1);
                objectToModify.getWriteByteOperations().add(updateFlag);

                // Exploding trap chest
                if(Settings.isFools2020Mode()) {
                    AddObject.addFoolsExplosion(objectToModify.getObjectContainer(), objectToModify.getX(), objectToModify.getY(), newWorldFlag);
                }
                else {
                    AddObject.addExplosion(objectToModify.getObjectContainer(), objectToModify.getX(), objectToModify.getY(), newWorldFlag, 60, true);
                }
            }
            else {
                short newContentsGraphic = getCustomItemGraphic(newChestContentsItemName);
                if(newContentsGraphic == 0) {
                    // Nothing
                    objectToModify.getArgs().set(0, newContentsGraphic);
                    objectToModify.getArgs().set(1, (short)1); // Quantity is irrelevant
                }
                else {
                    // Fake item graphic
                    objectToModify.getArgs().set(0, (short)(newContentsGraphic + 11));
                    objectToModify.getArgs().set(1, (short)0);
                }
                if(Settings.isRandomizeGraphics()) {
                    objectToModify.getArgs().set(2, (short)random.nextInt(2)); // Random graphics
                }
                else if(Settings.isCoinChestGraphics()) {
                    objectToModify.getArgs().set(2, (short)0);
                }
                else {
                    objectToModify.getArgs().set(2, (short)1);
                }

                updateFlag = new WriteByteOperation();
                updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                updateFlag.setIndex(newWorldFlag);
                updateFlag.setValue(1);
                objectToModify.getWriteByteOperations().add(updateFlag);

                updateFlag = new WriteByteOperation();
                updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                updateFlag.setIndex(FlagConstants.SCREEN_FLAG_2E);
                updateFlag.setValue(1);
                objectToModify.getWriteByteOperations().add(updateFlag);

                int xPos = objectToModify.getX();
                int yPos = objectToModify.getY();
                AddObject.addBat(objectToModify.getObjectContainer(), xPos - 20, yPos, FlagConstants.SCREEN_FLAG_2E);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos + 20, yPos, FlagConstants.SCREEN_FLAG_2E);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos - 20, FlagConstants.SCREEN_FLAG_2E);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos + 20, FlagConstants.SCREEN_FLAG_2E);
            }

            AddObject.addNoItemSoundEffect(objectToModify.getObjectContainer(), newWorldFlag, FlagConstants.SCREEN_FLAG_2E);
        }
        else {
            if(itemNewContentsData.getWorldFlag() == newWorldFlag || (Settings.isFools2021Mode() && newWorldFlag >= 0xabe && newWorldFlag <= 0xac6)) {
                // Actual items
                if(itemRandomizeGraphicsFlag == null) {
                    objectToModify.getArgs().set(0, (short)(itemNewContentsData.getInventoryArg() + 11)); // Item arg to indicate what the chest drops
                    objectToModify.getArgs().set(1, (short)1); // Real item, not fake (or 1 weight, because the game won't allow multiple)

                    if(Settings.isRandomizeGraphics()) {
                        objectToModify.getArgs().set(2, (short)random.nextInt(2)); // Random graphics
                    }
                    else if(Settings.isCoinChestGraphics()) {
                        objectToModify.getArgs().set(2, (short)0);
                    }
                    else {
                        objectToModify.getArgs().set(2, (short)1);
                    }

                    for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
                        if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                            flagTest.setIndex(newWorldFlag);
                        }
                    }

                    WriteByteOperation updateFlag = new WriteByteOperation();
                    updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                    updateFlag.setIndex(newWorldFlag);
                    updateFlag.setValue(2);
                    objectToModify.getWriteByteOperations().add(updateFlag);

                    objectToModify.getWriteByteOperations().add(puzzleFlag);

                    updateFlag = new WriteByteOperation();
                    updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                    updateFlag.setIndex(newWorldFlag);
                    updateFlag.setValue(1);
                    objectToModify.getWriteByteOperations().add(updateFlag);

                    updateFlag = new WriteByteOperation();
                    updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                    updateFlag.setIndex(newWorldFlag);
                    updateFlag.setValue(2);
                    objectToModify.getWriteByteOperations().add(updateFlag);
                }
                else {
                    short graphic = getCustomItemGraphic(newChestContentsItemName);
                    if(graphic == 0) {
                        // No custom graphic
                        graphic = getRandomItemGraphic(random);
                    }
                    objectToModify.getArgs().set(0, (short)(graphic + 11)); // Item arg to indicate what the chest drops
                    objectToModify.getArgs().set(1, (short)0); // Fake item

                    if(Settings.isRandomizeGraphics()) {
                        objectToModify.getArgs().set(2, (short)random.nextInt(2)); // Random graphics
                    }
                    else if(Settings.isCoinChestGraphics()) {
                        objectToModify.getArgs().set(2, (short)0);
                    }
                    else {
                        objectToModify.getArgs().set(2, (short)1);
                    }

                    for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
                        if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                            flagTest.setIndex(itemRandomizeGraphicsFlag);
                        }
                    }

                    WriteByteOperation updateFlag = new WriteByteOperation();
                    updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                    updateFlag.setIndex(itemRandomizeGraphicsFlag);
                    updateFlag.setValue(2);
                    objectToModify.getWriteByteOperations().add(updateFlag);

                    objectToModify.getWriteByteOperations().add(puzzleFlag);

                    updateFlag = new WriteByteOperation();
                    updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                    updateFlag.setIndex(itemRandomizeGraphicsFlag);
                    updateFlag.setValue(1);
                    objectToModify.getWriteByteOperations().add(updateFlag);

                    updateFlag = new WriteByteOperation();
                    updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                    updateFlag.setIndex(itemRandomizeGraphicsFlag);
                    updateFlag.setValue(2);
                    objectToModify.getWriteByteOperations().add(updateFlag);

                    if(puzzleFlag.getIndex() != 0x032) {
                        AddObject.addItemGive(objectToModify, itemNewContentsData.getInventoryArg(), itemRandomizeGraphicsFlag, newWorldFlag);
                    }
                }
            }
            else {
                // Removed items
                if(random.nextBoolean()) {
                    objectToModify.getArgs().set(0, (short)1); // Coins
                    objectToModify.getArgs().set(1, (short)10); // 10 coins, the equivalent of a pot
                    if(Settings.isRandomizeGraphics()) {
                        objectToModify.getArgs().set(2, (short)random.nextInt(2)); // Random graphics
                    }
                    else {
                        objectToModify.getArgs().set(2, (short)0); // Removed items use coin chest graphics.
                    }
                }
                else {
                    objectToModify.getArgs().set(0, (short)2); // Weights
                    objectToModify.getArgs().set(1, (short)1); // The game won't allow multiple weights, so just give 1
                    if(Settings.isRandomizeGraphics()) {
                        objectToModify.getArgs().set(2, (short)random.nextInt(2)); // Random graphics
                    }
                    else {
                        objectToModify.getArgs().set(2, (short)0); // Removed items use coin chest graphics.
                    }
                }

                for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
                    if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                        flagTest.setIndex(newWorldFlag);
                    }
                }

                WriteByteOperation updateFlag = new WriteByteOperation();
                updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                updateFlag.setIndex(newWorldFlag);
                updateFlag.setValue(2);
                objectToModify.getWriteByteOperations().add(updateFlag);

                objectToModify.getWriteByteOperations().add(puzzleFlag);

                updateFlag = new WriteByteOperation();
                updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                updateFlag.setIndex(newWorldFlag);
                if(itemNewContentsData.getWorldFlag() == newWorldFlag) {
                    updateFlag.setValue(1);
                }
                else {
                    updateFlag.setValue(2);
                }
                objectToModify.getWriteByteOperations().add(updateFlag);

                updateFlag = new WriteByteOperation();
                updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                updateFlag.setIndex(newWorldFlag);
                updateFlag.setValue(2);
                objectToModify.getWriteByteOperations().add(updateFlag);
            }
        }

        if(cursed) {
            objectToModify.getArgs().set(3, (short)1);
            objectToModify.getArgs().set(4, (short)1);
            objectToModify.getArgs().set(5, (short)50);
        }
        else {
            objectToModify.getArgs().set(3, (short)0);
            objectToModify.getArgs().set(4, (short)1);
            objectToModify.getArgs().set(5, (short)50);
        }
    }

    private static void updateInstantItemContents(GameObject objectToModify, GameObjectId itemLocationData, GameObjectId itemNewContentsData) {
        objectToModify.getArgs().set(0, itemNewContentsData.getInventoryArg());
        for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
            if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                flagTest.setIndex(itemNewContentsData.getWorldFlag());
            }
        }
        int lastWorldFlagUpdateIndex = -1;
        for(int i = 0; i < objectToModify.getWriteByteOperations().size(); i++) {
            WriteByteOperation flagUpdate = objectToModify.getWriteByteOperations().get(i);
            if(flagUpdate.getIndex() == itemLocationData.getWorldFlag()) {
                lastWorldFlagUpdateIndex = i;
                flagUpdate.setIndex(itemNewContentsData.getWorldFlag());
            }
        }

        if(lastWorldFlagUpdateIndex != -1) {
            WriteByteOperation flagUpdate = objectToModify.getWriteByteOperations().get(lastWorldFlagUpdateIndex);
            if(flagUpdate.getValue() != 2) {
                flagUpdate.setValue(2);
            }
        }
    }

    private static void updateSnapshotsItemContents(GameObject objectToModify, GameObjectId itemLocationData, GameObjectId itemNewContentsData) {
        if(objectToModify.getArgs().get(3) != 0) {
            objectToModify.getArgs().set(3, itemNewContentsData.getInventoryArg());
        }
        for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
            if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                flagTest.setIndex(itemNewContentsData.getWorldFlag());
                if(flagTest.getValue() == 1) {
                    flagTest.setValue((byte) 2);
                }
            }
        }
        for(int i = 0; i < objectToModify.getWriteByteOperations().size(); i++) {
            WriteByteOperation flagUpdate = objectToModify.getWriteByteOperations().get(i);
            if(flagUpdate.getIndex() == itemLocationData.getWorldFlag()) {
                flagUpdate.setIndex(itemNewContentsData.getWorldFlag());
                if(flagUpdate.getValue() == 1) {
                    flagUpdate.setValue((byte) 2);
                }
            }
        }
    }

    private static void updateFloatingItemContents(GameObject objectToModify, GameObjectId itemLocationData, GameObjectId itemNewContentsData,
                                                   String newChestContentsItemName, int newWorldFlag, Integer itemRandomizeGraphicsFlag, Random random) {
        short inventoryArg = itemNewContentsData.getInventoryArg();
        boolean isRemovedItem = itemNewContentsData.getWorldFlag() != newWorldFlag;
        boolean isTrapItem = !isRemovedItem && Settings.isRandomizeTrapItems()
                && (newWorldFlag == FlagConstants.WF_TRAP_GRAVEYARD || newWorldFlag == FlagConstants.WF_TRAP_INFERNO || newWorldFlag == FlagConstants.WF_TRAP_TWIN);

        if(Settings.isFools2021Mode()) {
            if(itemLocationData.getWorldFlag() == FlagConstants.WF_KNIFE) {
                // Knife
                inventoryArg = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Spaulder").getInventoryArg();
                isRemovedItem = false;
                newWorldFlag = 0xac1;
            }
            if(itemLocationData.getWorldFlag() == FlagConstants.WF_TALISMAN) {
                // Talisman
                inventoryArg = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Spaulder").getInventoryArg();
                isRemovedItem = false;
                newWorldFlag = 0xac4;
            }
        }

        if(isRemovedItem) {
            // Add handling for removed items.
            objectToModify.getArgs().set(1, inventoryArg);
            objectToModify.getArgs().set(2, (short)0);

            for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
                if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                    flagTest.setIndex(newWorldFlag);
                }
            }

            int lastWorldFlagUpdateIndex = -1;
            for(int i = 0; i < objectToModify.getWriteByteOperations().size(); i++) {
                WriteByteOperation flagUpdate = objectToModify.getWriteByteOperations().get(i);
                if(flagUpdate.getIndex() == itemLocationData.getWorldFlag()) {
                    lastWorldFlagUpdateIndex = i;
                    flagUpdate.setIndex(newWorldFlag);
                }
            }

            if(lastWorldFlagUpdateIndex != -1) {
                WriteByteOperation flagUpdate = objectToModify.getWriteByteOperations().get(lastWorldFlagUpdateIndex);
                if(flagUpdate.getValue() != 2) {
                    flagUpdate.setValue(2);
                }
            }

            WriteByteOperation updateFlag = new WriteByteOperation();
            updateFlag.setIndex(FlagConstants.SCREEN_FLAG_2B);
            updateFlag.setOp(ByteOp.ASSIGN_FLAG);
            updateFlag.setValue(1);
            objectToModify.getWriteByteOperations().add(updateFlag);

            if(Settings.isFools2020Mode()) {
                int xPos = objectToModify.getX();
                int yPos = objectToModify.getY();
                AddObject.addBat(objectToModify.getObjectContainer(), xPos - 20, yPos, FlagConstants.SCREEN_FLAG_2B);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos + 20, yPos, FlagConstants.SCREEN_FLAG_2B);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos - 20, FlagConstants.SCREEN_FLAG_2B);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos + 20, FlagConstants.SCREEN_FLAG_2B);
            }
            AddObject.addNoItemSoundEffect(objectToModify.getObjectContainer(), newWorldFlag, FlagConstants.SCREEN_FLAG_2B);
        }
        else if(isTrapItem) {
            // Trap items
            short graphic = getCustomItemGraphic(newChestContentsItemName);
            if(graphic == 0) {
                // No custom graphic
                graphic = getRandomItemGraphic(random);
            }
            objectToModify.getArgs().set(1, graphic);
            objectToModify.getArgs().set(2, (short)0);

            for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
                if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                    flagTest.setIndex(newWorldFlag);
                }
            }

            int lastWorldFlagUpdateIndex = -1;
            for(int i = 0; i < objectToModify.getWriteByteOperations().size(); i++) {
                WriteByteOperation flagUpdate = objectToModify.getWriteByteOperations().get(i);
                if(flagUpdate.getIndex() == itemLocationData.getWorldFlag()) {
                    lastWorldFlagUpdateIndex = i;
                    flagUpdate.setIndex(newWorldFlag);
                }
            }

            if(lastWorldFlagUpdateIndex != -1) {
                WriteByteOperation flagUpdate = objectToModify.getWriteByteOperations().get(lastWorldFlagUpdateIndex);
                if(flagUpdate.getValue() != 2) {
                    flagUpdate.setValue(2);
                }
            }

            WriteByteOperation writeByteOperation;
            if(objectToModify.getWriteByteOperations().size() > 1) {
                writeByteOperation = objectToModify.getWriteByteOperations().get(1);
            }
            else {
                writeByteOperation = new WriteByteOperation();
                objectToModify.getWriteByteOperations().add(writeByteOperation);
            }
            writeByteOperation.setIndex(FlagConstants.SCREEN_FLAG_2B);
            writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
            writeByteOperation.setValue((byte)1);

            int xPos = objectToModify.getX();
            int yPos = objectToModify.getY();
            AddObject.addBat(objectToModify.getObjectContainer(), xPos - 20, yPos, FlagConstants.SCREEN_FLAG_2B);
            AddObject.addBat(objectToModify.getObjectContainer(), xPos + 20, yPos, FlagConstants.SCREEN_FLAG_2B);
            AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos - 20, FlagConstants.SCREEN_FLAG_2B);
            AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos + 20, FlagConstants.SCREEN_FLAG_2B);

            AddObject.addNoItemSoundEffect(objectToModify.getObjectContainer(), newWorldFlag, FlagConstants.SCREEN_FLAG_2B);
        }
        else if(itemRandomizeGraphicsFlag == null) {
            objectToModify.getArgs().set(1, inventoryArg);
            objectToModify.getArgs().set(2, (short)1); // Real item until determined otherwise.

            for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
                if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                    flagTest.setIndex(newWorldFlag);
                }
            }

            int lastWorldFlagUpdateIndex = -1;
            for(int i = 0; i < objectToModify.getWriteByteOperations().size(); i++) {
                WriteByteOperation flagUpdate = objectToModify.getWriteByteOperations().get(i);
                if(flagUpdate.getIndex() == itemLocationData.getWorldFlag()) {
                    lastWorldFlagUpdateIndex = i;
                    flagUpdate.setIndex(newWorldFlag);
                }
            }

            if(lastWorldFlagUpdateIndex != -1) {
                WriteByteOperation flagUpdate = objectToModify.getWriteByteOperations().get(lastWorldFlagUpdateIndex);
                if(flagUpdate.getValue() != 2) {
                    flagUpdate.setValue(2);
                }
            }
        }
        else {
            short graphic = getCustomItemGraphic(newChestContentsItemName);
            if(graphic == 0) {
                // No custom graphic
                graphic = getRandomItemGraphic(random);
            }
            objectToModify.getArgs().set(1, graphic);
            objectToModify.getArgs().set(2, (short)0);

            for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
                if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                    flagTest.setIndex(itemRandomizeGraphicsFlag);
                }
            }

            int lastWorldFlagUpdateIndex = -1;
            for(int i = 0; i < objectToModify.getWriteByteOperations().size(); i++) {
                WriteByteOperation flagUpdate = objectToModify.getWriteByteOperations().get(i);
                if(flagUpdate.getIndex() == itemLocationData.getWorldFlag()) {
                    lastWorldFlagUpdateIndex = i;
                    flagUpdate.setIndex(itemRandomizeGraphicsFlag);
                }
            }

            if(lastWorldFlagUpdateIndex != -1) {
                WriteByteOperation flagUpdate = objectToModify.getWriteByteOperations().get(lastWorldFlagUpdateIndex);
                if(flagUpdate.getValue() != 2) {
                    flagUpdate.setValue(2);
                }
            }

            AddObject.addItemGive(objectToModify, inventoryArg, itemRandomizeGraphicsFlag, newWorldFlag);
        }

        if(itemLocationData.getInventoryArg() == 11) {
            if(Settings.isFools2020Mode()) {
                objectToModify.getTestByteOperations().add(new TestByteOperation(FlagConstants.INFERNO_PUZZLE_FLARE_GUN, ByteOp.FLAG_GTEQ, 2));
            }
            if(Settings.isFools2021Mode()) {
                objectToModify.getTestByteOperations().add(new TestByteOperation(FlagConstants.INFERNO_PUZZLE_ICE_CAPE_CHEST, ByteOp.FLAG_GTEQ, 2));
            }
        }
    }

    private static void updateRelatedObject(GameObject objectToModify, GameObjectId itemLocationData, int newWorldFlag) {
        for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
            if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                flagTest.setIndex(newWorldFlag);
            }
        }
        for(WriteByteOperation flagUpdate : objectToModify.getWriteByteOperations()) {
            if(flagUpdate.getIndex() == itemLocationData.getWorldFlag()) {
                flagUpdate.setIndex(newWorldFlag);
            }
        }
    }

    private static boolean isRandomGraphicsAvailable() {
        return Settings.isRandomizeGraphics() && randomizeGraphicsFlag < DataFromFile.LAST_AVAILABLE_RANDOM_GRAPHICS_FLAG;
    }

    private static int getRandomizeGraphicsFlag(int newWorldFlag) {
        Integer newItemWorldFlag = mapOfWorldFlagToAssignedReplacementFlag.get(newWorldFlag);
        if (newItemWorldFlag == null) {
            newItemWorldFlag = randomizeGraphicsFlag++;
            mapOfWorldFlagToAssignedReplacementFlag.put(newWorldFlag, newItemWorldFlag);
        }
        return newItemWorldFlag;
    }

    public static void updateSubweaponPot(String weapon) {
        if("Shuriken".equals(weapon)) {
            subweaponPot.getArgs().set(0, (short)3);
        }
        else if("Rolling Shuriken".equals(weapon)) {
            subweaponPot.getArgs().set(0, (short)4);
        }
        else if("Earth Spear".equals(weapon)) {
            subweaponPot.getArgs().set(0, (short)5);
        }
        else if("Flare Gun".equals(weapon)) {
            subweaponPot.getArgs().set(0, (short)6);
        }
        else if("Bomb".equals(weapon)) {
            subweaponPot.getArgs().set(0, (short)7);
        }
        else if("Chakram".equals(weapon)) {
            subweaponPot.getArgs().set(0, (short)8);
        }
        else if("Caltrops".equals(weapon)) {
            subweaponPot.getArgs().set(0, (short)9);
        }
        else if("Pistol".equals(weapon)) {
            subweaponPot.getArgs().set(0, (short)10);
        }
    }

    public static void updateEdenDaises(Random random) {
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

    public static void randomizeEnemies(Random random) {
        EnemyRandomizer enemyRandomizer = new EnemyRandomizer(random);
        for(GameObject enemy : enemyObjects) {
            enemyRandomizer.randomizeEnemy(enemy);
        }
    }

    private static short getCustomItemGraphic(String itemName) {
        for(CustomItemPlacement customItemPlacement : DataFromFile.getCustomPlacementData().getCustomItemPlacements()) {
            if(customItemPlacement.getItemGraphic() != null && customItemPlacement.getContents().equals(itemName)) {
                return DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(customItemPlacement.getItemGraphic()).getInventoryArg();
            }
        }
        return 0;
    }

    private static short getRandomItemGraphic(Random random) {
        int itemGraphicListSize = DataFromFile.RANDOM_ITEM_GRAPHICS.size();
        if(Settings.isRandomizeForbiddenTreasure()) {
            itemGraphicListSize += 1;
        }
        int itemArgIndex = random.nextInt(itemGraphicListSize);
        if(itemArgIndex == DataFromFile.RANDOM_ITEM_GRAPHICS.size()) {
            return (short)74;
        }
        return DataFromFile.RANDOM_ITEM_GRAPHICS.get(itemArgIndex).shortValue();
    }

//    public static void randomizeMantras(Random random) {
//        List<String> mantrasToAssign = new ArrayList<>(Arrays.asList("LAMULANA", "ABUTO", "WEDJET", "BAHRUN", "VIY", "MU", "SABBAT", "MARDUK"));
//        List<String> mantrasToReplace = new ArrayList<>(mantrasToAssign);
//
//        int index;
//        GameObjectId mantraInfo;
//        String mantraToReplace;
//        while(!mantrasToAssign.isEmpty()) {
//            index = random.nextInt(mantrasToAssign.size());
//            mantraInfo = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(mantrasToAssign.get(index));
//            mantrasToAssign.remove(index);
//
//            index = random.nextInt(mantrasToReplace.size());
//            mantraToReplace = mantrasToReplace.get(index);
//
//            for(GameObject objectToReplace : mantraTablets.get(mantraToReplace)) {
//                if(objectToReplace.getId() == 0x9e) {
//                    // Replace mantra tablet block
//                    objectToReplace.getArgs().set(0, mantraInfo.getInventoryArg());
//                }
//                updateRelatedObject(objectToReplace, DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(mantraToReplace), mantraInfo);
//            }
//        }
//    }

    public static void addObject(GameObject gameObject) {
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
                else if(flagTest.getIndex() == FlagConstants.WF_MATERNITY_STATUE) {
                    // Timer to play Shell Horn sound when being given Maternity Statue equivalent
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MATERNITY_STATUE, FlagConstants.WF_MATERNITY_STATUE);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                }
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
                if(DataFromFile.getMapOfShopNameToShopBlock().values().contains(blockNumber)
                        || (Settings.isFools2020Mode() && blockNumber == 273)) {
                    List<GameObject> objects = mapOfShopBlockToShopObjects.get(blockNumber);
                    if (objects == null) {
                        mapOfShopBlockToShopObjects.put(blockNumber, new ArrayList<>());
                        objects = mapOfShopBlockToShopObjects.get(blockNumber);
                    }
                    objects.add(gameObject);
                }

                if(blockNumber == 34) {
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
                else if(blockNumber == 35) {
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Sidro", gameObject);
                    }
                }
                else if(blockNumber == 36) {
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Modro", gameObject);
                    }
                }
                else if(blockNumber == 39) {
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Penadvent of ghost", gameObject);
                    }
                }
                else if(blockNumber == 74) {
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Greedy Charlie", gameObject);
                    }
                }
                else if(blockNumber == 100) {
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Shalom III", gameObject);
                    }
                }
                else if(blockNumber == 102) {
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Usas VI", gameObject);
                    }
                }
                else if(blockNumber == 103) {
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Kingvalley I", gameObject);
                    }
                }
                else if(blockNumber == 132){
                    // Untransformed Mr. Fishman shop
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Mr. Fishman (Original)", gameObject);
                    }
                }
                else if(blockNumber == 133){
                    // Transformed Mr. Fishman shop
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Mr. Fishman (Alt)", gameObject);
                    }
                }
                else if(blockNumber == 167){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Operator Combaker", gameObject);
                    }
                }
                else if(blockNumber == 185){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Yiegah Kungfu", gameObject);
                    }
                }
                else if(blockNumber == 187){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Arrogant Metagear", gameObject);
                    }
                }
                else if(blockNumber == 204){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Arrogant Sturdy Snake", gameObject);
                    }
                }
                else if(blockNumber == 205){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Yiear Kungfu", gameObject);
                    }
                }
                else if(blockNumber == 220){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Affected Knimare", gameObject);
                    }
                }
                else if(blockNumber == 244){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Mover Athleland", gameObject);
                    }
                }
                else if(blockNumber == 272){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Giant Mopiran", gameObject);
                    }
                }
                else if(blockNumber == 290){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Kingvalley II", gameObject);
                    }
                }
                else if(blockNumber == 303){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Energetic Belmont", gameObject);
                    }
                }
                else if(blockNumber == 321){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Mechanical Efspi", gameObject);
                    }
                }
                else if(blockNumber == 337){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Mud Man Qubert", gameObject);
                    }
                }
                else if(blockNumber == 470){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Hot-blooded Nemesistwo", gameObject);
                    }
                }
                else if(blockNumber == 490) {
                    GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MSX2, FlagConstants.WF_MSX2);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                }
                else if(blockNumber == 1008){
                    if(Settings.isRandomizeNpcs()) {
                        mapOfNpcLocationToObject.put("NPCL: Tailor Dracuet", gameObject);
                    }
                }
            }
            else if(blockNumber == BlockConstants.Master_Hiner) {
                // Hiner - Surface NPC, 01-00-02
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Hiner", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_Moger) {
                // Moger - Surface NPC, 01-02-00
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Moger", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_FormerMekuriMaster_Mekuri) {
                // Former Mekuri Master - Surface NPC, 01-07-00
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Former Mekuri Master", gameObject);
                }

                GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.SOFTWARE_MEKURI, FlagConstants.WF_SOFTWARE_MEKURI);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == BlockConstants.Master_PriestZarnac) {
                // Priest Zarnac - Guidance NPC, 00-04-01
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Zarnac", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PriestXanado) {
                // Priest Xanado - Mausoleum NPC, 02-02-00
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Xanado", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PhilosopherGiltoriyo) {
                // Philosopher Giltoriyo - Spring NPC, 04-00-01
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Philosopher Giltoriyo", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PriestHidlyda) {
                // Priest Hidlyda - Spring NPC, 04-06-01
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Hidlyda", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PriestRomancis) {
                // Priest Romancis - Inferno NPC, 05-03-02
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Romancis", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PriestAramo) {
                // Priest Aramo - Extinction NPC, 06-06-00
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Aramo", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PriestTriton) {
                // Priest Triton - Extinction NPC, 06-09-01
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Triton", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PriestJaguarfiv) {
                // Priest Jaguarfiv - Twin Labs NPC, 07-10-01
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Jaguarfiv", gameObject);
                }
            }
            else if(blockNumber == 686) {
                // The Fairy Queen - Endless NPC, 08-01-00
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: The Fairy Queen", gameObject);
                }
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
            if(blockNumber == BlockConstants.Master_PriestAlest) {
                // Priest Alest - Illusion NPC, 10-08-01
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Alest", gameObject);
                }

                GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MINI_DOLL, FlagConstants.WF_MINI_DOLL);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == BlockConstants.Master_StrayFairy) {
                // Stray fairy - Illusion NPC, 10-00-01
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Stray fairy", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_GiantThexde) {
                // Giant Thexde - Graveyard NPC, 11-07-00
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Giant Thexde", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PhilosopherAlsedana) {
                // Philosopher Alsedana - Moonlight NPC, 12-06-00
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Philosopher Alsedana", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PhilosopherSamaranta) {
                // Philosopher Samaranta - Goddess NPC, 13-05-01
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Philosopher Samaranta", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PriestLaydoc) {
                // Priest Laydoc - Ruin NPC, 14-00-01
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Laydoc", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PriestAshgine) {
                // Priest Ashgine - Birth NPC, 16-01-00
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Ashgine", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_Fobos_MedicineCheck) {
                // Philosopher Fobos - Dimensional NPC, 17-02-00
                // Post-Medicine version of Fobos
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Philosopher Fobos", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_8BitElder) {
                // 8bit Elder - Gate of Time NPC, 21-00-00
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: 8bit Elder", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_duplex) {
                // duplex - Illusion NPC, 10-02-02
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: duplex", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_Samieru) {
                // Samieru - Moonlight NPC, 12-03-00
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Samieru", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_Naramura) {
                // Naramura - Goddess NPC, 13-06-03
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Naramura", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_8BitFairy) {
                // 8bit Fairy - Gate of Time NPC, 20-00-01
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: 8bit Fairy", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PriestMadomono) {
                // Priest Madomono - Sun NPC, 03-04-02
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Madomono", gameObject);
                }
            }
            else if(blockNumber == BlockConstants.Master_PriestGailious) {
                // Priest Gailious - Inferno NPC, 05-02-01
                if(Settings.isRandomizeNpcs()) {
                    mapOfNpcLocationToObject.put("NPCL: Priest Gailious", gameObject);
                }
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
            else if(itemArg == ItemConstants.MATERNITY_STATUE) {
                GameObjectId gameObjectId = new GameObjectId((short)ItemConstants.MATERNITY_STATUE, FlagConstants.WF_MATERNITY_STATUE);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
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
