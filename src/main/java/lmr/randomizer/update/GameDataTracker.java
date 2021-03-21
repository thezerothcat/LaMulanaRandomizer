package lmr.randomizer.update;

import lmr.randomizer.*;
import lmr.randomizer.dat.*;
import lmr.randomizer.dat.conversation.CheckBlock;
import lmr.randomizer.dat.shop.BlockStringData;
import lmr.randomizer.dat.shop.ShopBlock;
import lmr.randomizer.node.CustomItemPlacement;
import lmr.randomizer.random.EnemyRandomizer;
import lmr.randomizer.random.SealRandomizer;
import lmr.randomizer.rcd.object.*;

import java.util.*;

/**
 * Created by thezerothcat on 7/21/2017.
 */
public final class GameDataTracker {
    private static Map<GameObjectId, List<GameObject>> mapOfChestIdentifyingInfoToGameObject = new HashMap<>();
    private static Map<GameObjectId, List<Block>> mapOfChestIdentifyingInfoToBlock = new HashMap<>();
    private static Map<Integer, List<GameObject>> mapOfShopBlockToShopObjects = new HashMap<>();
    private static Map<String, List<GameObject>> mantraTablets = new HashMap<>();
    private static Map<String, List<GameObject>> mapOfDoorNameToBacksideDoor = new HashMap<>();
    private static Map<String, List<GameObject>> mapOfGateNameToTransitionGate = new HashMap<>();
    private static Map<String, Screen> mapOfTransitionNameToScreen = new HashMap<>();
    private static Map<String, List<GameObject>> mapOfSealNodeToSealObjects = new HashMap<>();
    private static List<GameObject> enemyObjects = new ArrayList<>();
    private static List<GameObject> npcObjects = new ArrayList<>();

    private static GameObject subweaponPot;
    private static GameObject customShop;

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
        mantraTablets.clear();
        enemyObjects.clear();
        npcObjects.clear();
        customShop = null;

        mapOfWorldFlagToAssignedReplacementFlag.clear();
        randomizeGraphicsFlag = DataFromFile.FIRST_AVAILABLE_RANDOM_GRAPHICS_FLAG;
    }

    public static void setSubweaponPot(GameObject subweaponPot) {
        GameDataTracker.subweaponPot = subweaponPot;
    }

    public static void setCustomShop(GameObject customShop) {
        GameDataTracker.customShop = customShop;
    }

    public static void setCustomShopBlock(int customShopBlock) {
        if(customShop != null) {
            customShop.getArgs().set(4, (short)customShopBlock);
        }
        mapOfShopBlockToShopObjects.put(customShopBlock, new ArrayList<>(Arrays.asList(customShop)));
    }

    public static void putTransitionScreen(String transitionName, Screen screen) {
        GameDataTracker.mapOfTransitionNameToScreen.put(transitionName, screen);
    }

    public static void addObject(GameObject gameObject) {
        if (gameObject.getId() == 0x2c) {
            int worldFlag;
            short inventoryArg;

            if(gameObject.getArgs().get(0) == 1) {
                // Coin chest
                inventoryArg = gameObject.getArgs().get(1); // Use coin amount as item arg
            }
            else if(gameObject.getArgs().get(0) == 7) {
                // Bomb chest, will be replaced by an 80-coin chest.
                inventoryArg = 80;
            }
            else {
                // Item chest
                inventoryArg = (short)(gameObject.getArgs().get(0) - 11);

                if(inventoryArg == 24) {
                    // Cog of the Soul chest
                    TestByteOperation cogChestTest = gameObject.getTestByteOperations().get(0);
                    cogChestTest.setIndex(2799);
                    WriteByteOperation cogPuzzleFlag = gameObject.getWriteByteOperations().get(1);
                    cogPuzzleFlag.setIndex(2799);
                }
                else if(inventoryArg == 72) {
                    // Diary chest
                    for(TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                        if(flagTest.getIndex() == 539) {
                            // Require skull walls removed (normally required for Shawn to appear) instead of directly requiring Shawn's appearance
                            flagTest.setIndex(536);
                            flagTest.setOp(ByteOp.FLAG_EQUALS);
                            flagTest.setValue((byte)1);
                            break;
                        }
                    }
                    // Require showing Talisman to Xelpud (normally required for Shawn to appear)
                    TestByteOperation flagTest = new TestByteOperation();
                    flagTest.setIndex(2796);
                    flagTest.setOp(ByteOp.FLAG_GTEQ);
                    flagTest.setValue((byte)2);
                    gameObject.getTestByteOperations().add(flagTest);
                }
            }

            WriteByteOperation flagUpdate = gameObject.getWriteByteOperations().get(0);
            if(flagUpdate.getIndex() == 333) {
                // Replace world flag for Life Seal coin chest, which is a bit special
                worldFlag = 2707;
            }
            else if(Settings.isRandomizeTrapItems() && flagUpdate.getIndex() == 60) {
                // Replace world flag for Graveyard trap chest, and add a full set of update flags since it only has one.
                inventoryArg = 0;
                worldFlag = 2777;

                gameObject.getWriteByteOperations().clear();

                flagUpdate = new WriteByteOperation();
                flagUpdate.setIndex(2777);
                flagUpdate.setOp(ByteOp.ASSIGN_FLAG);
                flagUpdate.setValue(2);
                gameObject.getWriteByteOperations().add(flagUpdate);

                flagUpdate = new WriteByteOperation();
                flagUpdate.setIndex(2776);
                flagUpdate.setOp(ByteOp.ASSIGN_FLAG);
                flagUpdate.setValue(1);
                gameObject.getWriteByteOperations().add(flagUpdate);

                flagUpdate = new WriteByteOperation();
                flagUpdate.setIndex(2777);
                flagUpdate.setOp(ByteOp.ASSIGN_FLAG);
                flagUpdate.setValue(2);
                gameObject.getWriteByteOperations().add(flagUpdate);

                flagUpdate = new WriteByteOperation();
                flagUpdate.setIndex(2777);
                flagUpdate.setOp(ByteOp.ASSIGN_FLAG);
                flagUpdate.setValue(2);
                gameObject.getWriteByteOperations().add(flagUpdate);
            }
            else if(Settings.isRandomizeTrapItems() && flagUpdate.getIndex() == 522) {
                // Replace world flag for Illusion trap chest
                inventoryArg = 0;
                worldFlag = 2778;
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
        else if (gameObject.getId() == 0xa9) {
            if(Settings.isBlockPushingRequiresGlove()) {
                Screen screen = (Screen)gameObject.getObjectContainer();
                if(screen.getZoneIndex() == 5 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                    AddObject.addInfernoPushableBlockReplacements(gameObject);
                }
                else if(screen.getZoneIndex() != 12 || screen.getRoomIndex() != 7 || screen.getScreenIndex() != 0) {
                    AddObject.addPushableBlockBlockage(gameObject);
                }
            }
            if(Settings.isFools2020Mode()) {
                if(gameObject.getObjectContainer() instanceof Screen) {
                    Screen screen = (Screen) gameObject.getObjectContainer();
                    if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 1) {
                        if(gameObject.getY() == 160) {
                            gameObject.setX(gameObject.getX() - 40);
                        }
                    }
                }
            }
        }
        else if (gameObject.getId() == 0xaa) {
            if(Settings.isFools2020Mode()) {
                if(gameObject.getObjectContainer() instanceof Screen) {
                    Screen screen = (Screen) gameObject.getObjectContainer();
                    if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 1) {
                        if(gameObject.getX() == 1060) {
                            gameObject.setX(gameObject.getX() - 40);
                        }
                    }
                }
            }
        }
        else if (gameObject.getId() == 0xb9) {
            if(Settings.isBlockPushingRequiresGlove()) {
                Screen screen = (Screen)gameObject.getObjectContainer();
                int zone = screen.getZoneIndex();
                if(zone == 1) {
                    AddObject.addPushableBlockBlockage(gameObject);
                }
                else if(zone == 3 || zone == 10) {
                    AddObject.addPushableBlockBlockage(gameObject);
                }
                else if(zone == 14 && screen.getRoomIndex() == 2) {
                    TestByteOperation pushableBlockTest = gameObject.getTestByteOperations().get(0);
                    pushableBlockTest.setIndex(0xacc);

                    AddObject.addRuinGloveTimer(gameObject.getObjectContainer());
                }
            }
        }
        else if (gameObject.getId() == 0x2e) {
            Screen screen = (Screen)gameObject.getObjectContainer();
            if(Settings.isBossSpecificAnkhJewels()) {
                int ankhFlag = getAnkhFlag(screen.getZoneIndex());

                for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == 0x16a && ByteOp.FLAG_EQUALS.equals(testByteOperation.getOp())) {
                        AddObject.addBossSpecificAnkhCover(gameObject, ankhFlag);
                    }
                }
                gameObject.getTestByteOperations().add(new TestByteOperation(ankhFlag, ByteOp.FLAG_NOT_EQUAL, (byte) 0)); // Don't spawn ankh without jewel collected.
            }
//            if(Settings.isFoolsMode()) {
//                Screen screen = (Screen) gameObject.getObjectContainer();
//                if(screen.getZoneIndex() == 4) {
//                    gameObject.getArgs().set(24, (short)4);
//                    gameObject.getArgs().set(25, (short)400);
//                    gameObject.getArgs().set(26, (short)2316);
//                    gameObject.getArgs().set(27, (short)0);
//                    gameObject.getArgs().set(28, (short)4);
//                    gameObject.getArgs().set(29, (short)400);
//                    gameObject.getArgs().set(30, (short)2316);
//                }
//            }
//            if(Settings.isHalloweenMode()) {
//                // Bahamut to Night Surface
//                Screen screen = (Screen) gameObject.getObjectContainer();
//                if(screen.getZoneIndex() == 4) {
//                    gameObject.getArgs().set(24, (short)22);
//                    gameObject.getArgs().set(28, (short)22);
//                }
//            }
            if(screen.getZoneIndex() == 0) {
                // Amphisbaena ankh
                if(Settings.isRandomizeBosses()) {
                    for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                        if(testByteOperation.getIndex() == 0x133) {
                            testByteOperation.setIndex(0x1b4);
                            testByteOperation.setValue((byte)4);
                        }
                    }
                    for(WriteByteOperation writeByteOperation : gameObject.getWriteByteOperations()) {
                        if(writeByteOperation.getIndex() == 0x133) {
                            writeByteOperation.setIndex(0x1b4);
                            writeByteOperation.setValue(5);
                        }
                    }
                }
            }
            else if(screen.getZoneIndex() == 3) {
                if(Settings.isRandomizeBosses()) {
                    gameObject.getArgs().set(24, (short)3);
                    gameObject.getArgs().set(25, (short)402);
                    gameObject.getArgs().set(26, (short)508);
                    gameObject.getArgs().set(28, (short)3);
                    gameObject.getArgs().set(29, (short)402);
                    gameObject.getArgs().set(30, (short)508);
                }
            }
            else if(screen.getZoneIndex() == 5) {
                if(Settings.isRandomizeBosses()) {
                    for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                        if(testByteOperation.getIndex() == 0x1b4) {
                            testByteOperation.setIndex(0x133);
                            testByteOperation.setValue((byte)5);
                        }
                    }
                    for(WriteByteOperation writeByteOperation : gameObject.getWriteByteOperations()) {
                        if(writeByteOperation.getIndex() == 0x1b4) {
                            writeByteOperation.setIndex(0x133);
                            writeByteOperation.setValue(6);
                        }
                    }
                }
            }
            else if(screen.getZoneIndex() == 6) {
                if(Settings.isRandomizeBosses()) {
                    gameObject.getArgs().set(24, (short)7);
                    gameObject.getArgs().set(25, (short)0);
                    gameObject.getArgs().set(26, (short)1500);
                    gameObject.getArgs().set(27, (short)2);

                    for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                        if(testByteOperation.getIndex() == 0x16a && ByteOp.FLAG_EQUALS.equals(testByteOperation.getOp())) {
                            AddObject.addTwinLabsDoor(gameObject);
                        }
                    }
                }
            }
        }
        else if (gameObject.getId() == 0x01) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x02) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }

            if(Settings.isRandomizeBosses()) {
                Screen screen = (Screen) gameObject.getObjectContainer();
                if(screen.getZoneIndex() == 0 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                    for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                        if(testByteOperation.getIndex() == 0x133) {
                            testByteOperation.setIndex(0x1b4);
                            testByteOperation.setValue((byte)4);
                        }
                    }
                }
            }
        }
        else if (gameObject.getId() == 0x03) {
            if(Settings.isRandomizeEnemies()) {
//                if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
//                    // Fix flag for looping punishment room enemies
//                    if(gameObject.getObjectContainer() instanceof Screen) {
//                        Screen screen = (Screen) gameObject.getObjectContainer();
//                        if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
//                            for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
//                                WriteByteOperation flagUpdate = gameObject.getWriteByteOperations().get(i);
//                                if (flagUpdate.getIndex() == 0x000) {
//                                    flagUpdate.setIndex(0xaad);
//                                }
//                            }
//                        }
//                    }
//                }
                enemyObjects.add(gameObject);
            }

            if(Settings.isRandomizeBosses()) {
                Screen screen = (Screen) gameObject.getObjectContainer();
                if(screen.getZoneIndex() == 5 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                    for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                        if(testByteOperation.getIndex() == 0x1b4) {
                            testByteOperation.setIndex(0x133);
                            testByteOperation.setValue((byte)5);
                        }
                    }
                }
            }
        }
        else if (gameObject.getId() == 0x05) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x06) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x16) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x17) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x18) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x1b) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x1c) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x1d) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x1e) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x21) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x26) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x27) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x28) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x29) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x2f) {
            // Floating item
            short chestArg = gameObject.getArgs().get(1);
            int worldFlag = gameObject.getWriteByteOperations().get(0).getIndex();
            GameObjectId gameObjectId = new GameObjectId(chestArg, worldFlag);

            if(chestArg == 4) {
                // Remove LAMULANA mantra check on the Key Sword floating item
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                    if (gameObject.getTestByteOperations().get(i).getIndex() == 292) {
                        flagToRemoveIndex = i;
                        break;
                    }
                }
                gameObject.getTestByteOperations().remove((int)flagToRemoveIndex);
            }

            List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
            if (objects == null) {
                mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
            }
            objects.add(gameObject);
        }
        else if (gameObject.getId() == 0x34) {
            if(Settings.isFoolsLogic()) {
                String sealNode = SealRandomizer.getSealNode(gameObject);
                List<GameObject> seals = mapOfSealNodeToSealObjects.get(sealNode);
                if(seals == null) {
                    seals = new ArrayList<>();
                    mapOfSealNodeToSealObjects.put(sealNode, seals);
                }
                seals.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x35) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x37) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x38) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x3b) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x3c) {
            if(Settings.isRandomizeBosses()) {
                Screen screen = (Screen) gameObject.getObjectContainer();
                if(screen.getZoneIndex() == 5 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                    for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                        if(testByteOperation.getIndex() == 0x1b4) {
                            testByteOperation.setIndex(0x133);
                            testByteOperation.setValue((byte)5);
                        }
                    }
                }
            }
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x3e) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x41) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x42) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x43) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
//        else if (gameObject.getId() == 0x44) {
//            // Hand enemies in Extinction
//            if(Settings.isHalloweenMode() && Settings.isRandomizeEnemies()) {
//                enemyObjects.add(gameObject);
//            }
//        }
//        else if (gameObject.getId() == 0x45) {
//            // Centimani
//            if(Settings.isHalloweenMode() && Settings.isRandomizeEnemies()) {
//                enemyObjects.add(gameObject);
//            }
//        }
        else if (gameObject.getId() == 0x48) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x49) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x4a) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x4b) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x4c) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
//        else if (gameObject.getId() == 0x4f) {
//            if(Settings.isRandomizeEnemies()) {
//                enemyObjects.add(gameObject);
//            }
//        }
        else if (gameObject.getId() == 0x50) {
            if(Settings.isRandomizeEnemies()) {
//                // Fix flag for looping punishment room enemies
//                if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
//                    if(gameObject.getObjectContainer() instanceof Screen) {
//                        Screen screen = (Screen) gameObject.getObjectContainer();
//                        if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
//                            for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
//                                WriteByteOperation flagUpdate = gameObject.getWriteByteOperations().get(i);
//                                if (flagUpdate.getIndex() == 0x000) {
//                                    flagUpdate.setIndex(0xaad);
//                                }
//                            }
//                        }
//                    }
//                }
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x51) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x52) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x53) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x55) {
            // Twin labs - witch
//            Screen screen = (Screen)gameObject.getObjectContainer();
//            if(!(screen.getZoneIndex() == 7
//                    && ((screen.getRoomIndex() == 13 && screen.getScreenIndex() == 0) || (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 1)))) {
//                // All witches except the ones connected to Baphomet
//                enemyObjects.add(gameObject);
//            }
            enemyObjects.add(gameObject);
        }
        else if (gameObject.getId() == 0x56) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x57) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x58) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x59) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x5c) {
            if(Settings.isRandomizeEnemies()) {
                Screen screen = (Screen)gameObject.getObjectContainer();
                if(screen.getZoneIndex() != 10 || screen.getRoomIndex() != 5 || screen.getScreenIndex() != 1) {
                    // All lizards except lizard puzzle guy
                    enemyObjects.add(gameObject);
                }
            }
        }
        else if (gameObject.getId() == 0x5d) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x5e) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x62) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x63) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x64) {
            if(Settings.isRandomizeEnemies()) {
//                if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
//                    // Fix flag for looping punishment room enemies
//                    if(gameObject.getObjectContainer() instanceof Screen) {
//                        Screen screen = (Screen) gameObject.getObjectContainer();
//                        if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
//                            for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
//                                WriteByteOperation flagUpdate = gameObject.getWriteByteOperations().get(i);
//                                if (flagUpdate.getIndex() == 0x000) {
//                                    flagUpdate.setIndex(0xaad);
//                                }
//                            }
//                        }
//                    }
//                }
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x65) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x66) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x68) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x69) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x6a) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x6d) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x6e) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x70) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x73) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x74) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x7d) {
            if(Settings.isRandomizeEnemies()) {
//                if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
//                    // Fix flag for looping punishment room enemies
//                    if(gameObject.getObjectContainer() instanceof Screen) {
//                        Screen screen = (Screen) gameObject.getObjectContainer();
//                        if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
//                            for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
//                                WriteByteOperation flagUpdate = gameObject.getWriteByteOperations().get(i);
//                                if (flagUpdate.getIndex() == 0x000) {
//                                    flagUpdate.setIndex(0xaad);
//                                }
//                            }
//                        }
//                    }
//                }
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x7e) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x81) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x82) {
            if(Settings.isRandomizeEnemies()) {
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0x83) {
            if(Settings.isRandomizeEnemies()) {
//                if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
//                    // Fix flag for looping punishment room enemies
//                    if(gameObject.getObjectContainer() instanceof Screen) {
//                        Screen screen = (Screen) gameObject.getObjectContainer();
//                        if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
//                            for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
//                                WriteByteOperation flagUpdate = gameObject.getWriteByteOperations().get(i);
//                                if (flagUpdate.getIndex() == 0x000) {
//                                    flagUpdate.setIndex(0xaad);
//                                }
//                            }
//                        }
//                    }
//                }
                enemyObjects.add(gameObject);
            }
        }
//        else if (gameObject.getId() == 0x87) {
//            // Kuusarikku
//            if(Settings.isHalloweenMode() && Settings.isRandomizeEnemies()) {
//                if(gameObject.getObjectContainer() instanceof Screen) {
//                    Screen screen = (Screen) gameObject.getObjectContainer();
//                    if(screen.getZoneIndex() == 24) {
//                        enemyObjects.add(gameObject);
//                    }
//                }
//            }
//        }
//        else if (gameObject.getId() == 0x88) {
//            // Girtablilu
//            if(Settings.isHalloweenMode() && Settings.isRandomizeEnemies()) {
//                if(gameObject.getObjectContainer() instanceof Screen) {
//                    Screen screen = (Screen) gameObject.getObjectContainer();
//                    if(screen.getZoneIndex() == 24) {
//                        enemyObjects.add(gameObject);
//                    }
//                }
//            }
//        }
//        else if (gameObject.getId() == 0x8d) {
//            // Ushum
//            if(Settings.isHalloweenMode() && Settings.isRandomizeEnemies()) {
//                if(gameObject.getObjectContainer() instanceof Screen) {
//                    Screen screen = (Screen) gameObject.getObjectContainer();
//                    if(screen.getZoneIndex() == 24) {
//                        Integer flagToRemoveIndex = null;
//                        for (int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
//                            if (gameObject.getTestByteOperations().get(i).getIndex() == 0x016) {
//                                flagToRemoveIndex = i;
//                                break;
//                            }
//                        }
//                        if(flagToRemoveIndex != null) {
//                            gameObject.getTestByteOperations().remove((int)flagToRemoveIndex);
//                        }
//
//                        enemyObjects.add(gameObject);
//                    }
//                }
//            }
//        }
        else if (gameObject.getId() == 0x8f) {
            if(Settings.isRandomizeEnemies()) {
//                if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
//                    // Fix flag for looping punishment room enemies
//                    if(gameObject.getObjectContainer() instanceof Screen) {
//                        Screen screen = (Screen) gameObject.getObjectContainer();
//                        if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
//                            for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
//                                WriteByteOperation flagUpdate = gameObject.getWriteByteOperations().get(i);
//                                if (flagUpdate.getIndex() == 0x000) {
//                                    flagUpdate.setIndex(0xaad);
//                                }
//                            }
//                        }
//                    }
//                }
                enemyObjects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0xb5) {
            short itemArg = gameObject.getArgs().get(0);
            if(itemArg == 70) {
                // Surface map item give
                GameObjectId gameObjectId = new GameObjectId((short) 70, 209);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(itemArg == 81) {
                // Maternity Doll item give
                GameObjectId gameObjectId = new GameObjectId((short) 81, 267);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
        } else if(gameObject.getId() == 0xc3) {
            // Items given by torude.
            short itemArg = gameObject.getArgs().get(3);
            if(itemArg == 93 || itemArg == 94 || itemArg == 95) {
                short worldFlag;
                if(itemArg == 93) {
                    worldFlag = 234;
                }
                else if(itemArg == 94) {
                    worldFlag = 235;
                }
                else {
                    worldFlag = 236;
                }
                GameObjectId gameObjectId = new GameObjectId((short) itemArg, worldFlag);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
        }
        else if (gameObject.getId() == 0xc4) {
            if(Settings.isRandomizeTransitionGates()) {
                Screen screen = (Screen)gameObject.getObjectContainer();
//                FileUtils.logFlush(String.format("Gate on screen [%d, %d, %d] leads to screen [%d, %d, %d] with position (%d, %d)",
//                        screen.getZoneIndex(), screen.getRoomIndex(), screen.getScreenIndex(),
//                        gameObject.getArgs().get(0), gameObject.getArgs().get(1), gameObject.getArgs().get(2),
//                        gameObject.getArgs().get(3), gameObject.getArgs().get(4)));

                String gateName = null;
                boolean needEscapeDoor = false;
                if(screen.getZoneIndex() == 0) {
                    // Gate of Guidance
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Guidance L1";
                        needEscapeDoor = true;
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
                        needEscapeDoor = true;
                    }
                    else if (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Surface D1";
                        needEscapeDoor = true;
                    }
                    else if (screen.getRoomIndex() == 5 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Surface D2";
                        needEscapeDoor = true;
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
                        needEscapeDoor = true;
                    }
                }
                else if(screen.getZoneIndex() == 6) {
                    // Extinction
                    if (screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Extinction U2";
                    }
                    else if (screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Extinction U1";
                        needEscapeDoor = true;
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
                        needEscapeDoor = true;
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
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Endless R1";
                    }
                    else if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 3) {
                        gateName = "Transition: Endless U1";
                        needEscapeDoor = true;
                    }
                    else if (screen.getRoomIndex() == 5 && screen.getScreenIndex() == 3) {
                        gateName = "Transition: Endless D1";
                        needEscapeDoor = true;
                    }
                }
                else if(screen.getZoneIndex() == 9) {
                    // Shrine
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Shrine U1";
                        needEscapeDoor = true;
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
                        needEscapeDoor = true;
                    }
                    else if (screen.getRoomIndex() == 1 && screen.getScreenIndex() == 2) {
                        gateName = "Transition: Retromausoleum D1";
                        needEscapeDoor = true;
                    }
                }
                else if(screen.getZoneIndex() == 20) {
                    // Gate of Time (Gate of Guidance)
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Retroguidance D1";
                        needEscapeDoor = true;
                    }
                    else if (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                        gateName = "Transition: Retroguidance L1";
                        needEscapeDoor = true;
                    }
                }
                else if(screen.getZoneIndex() == 21) {
                    // Gate of Time (Surface)
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                        gateName = "Transition: Retrosurface R1";
                        needEscapeDoor = true;
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

                    if(needEscapeDoor) {
                        GameObject escapeDoor = AddObject.addEscapeGate(gameObject);
                        transitionGates.add(escapeDoor);
                    }
                }
            }
        } else if (gameObject.getId() == 0x12) {
            Integer flagIndexToRemove = null;
            TestByteOperation flagTest;
            for (int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                flagTest = gameObject.getTestByteOperations().get(i);
                if (flagTest.getIndex() == 335) {
                    // deathv stuff
                    GameObjectId gameObjectId = new GameObjectId((short) 96, 335);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if (flagTest.getIndex() == 219) {
                    // Gate of Illusion Map stuff
                    GameObjectId gameObjectId = new GameObjectId((short) 70, 219);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if (flagTest.getIndex() == 377) {
                    // Turning on the lights in Temple of the Sun; moved in versions after 1.3 and should be restored
                    gameObject.setX(400);
                    break;
                }
                else if (flagTest.getIndex() == 435) {
                    // Breakable wall to Flare Gun room; hitbox is inside the wall and makes subweapon breaking a bit awkward
                    gameObject.getArgs().set(6, (short)4); // Extend wall hitbox
                }
                else if (flagTest.getIndex() == 501) {
                    // Breakable ceiling to Isis' Pendant room
                    flagIndexToRemove = i;
                    break;
                }
                else if(flagTest.getIndex() == 591) {
                    // emusic wall - not requiring precise hitbox
                    gameObject.getArgs().set(3, (short)4);
                }
                else if (flagTest.getIndex() == 610) {
                    // Moon-Gazing Pit faces - extend hitbox to make things a bit easier
                    gameObject.setX(gameObject.getX() - 20);
                    gameObject.getArgs().set(6, (short)(gameObject.getArgs().get(6) + 1)); // Extend wall hitbox
                }
                else if(flagTest.getIndex() == 695) {
                    // Main-weapon-only tentacle blocking access to Mother ankh
                    gameObject.getArgs().set(4, (short)18);
                    break;
                }
                else if(flagTest.getIndex() == 877) {
                    // Main-weapon-only wall in Tower of the Goddess
                    gameObject.getArgs().set(4, (short)18);
                    break;
                }
                else if(flagTest.getIndex() == 0x23f) {
                    // Main-weapon-only blockage of the ice block in Graveyard of the Giants, needed to access the up-ladder
                    gameObject.getArgs().set(4, (short)18);
                    break;
                }
                else if(flagTest.getIndex() == 0x256) {
                    // Moonlight dev room floor
                    gameObject.getArgs().set(3, (short)4); // Any direction, to be more bomb-friendly.
                    break;
                }
                else if(flagTest.getIndex() == 2033) {
                    // Trigger for The Boss ankh, coded as a wall that triggers stuff when broken
                    gameObject.getArgs().set(4, (short)18);
                    break;
                }
                else if(flagTest.getIndex() == 296) {
                    // Breakable snake statue in Inferno Cavern spike area
                    gameObject.getArgs().set(6, (short)2); // Extend wall hitbox
                    flagTest.setIndex(2795);
                    flagTest.setValue((byte)1);
                }
            }
            if(flagIndexToRemove != null) {
                gameObject.getTestByteOperations().remove((int)flagIndexToRemove);
            }

            if(Settings.isBlockPushingRequiresGlove()) {
                Screen screen = (Screen)gameObject.getObjectContainer();
                if (screen.getZoneIndex() == 14 && screen.getRoomIndex() == 2 && screen.getScreenIndex() == 0) {
                    for(WriteByteOperation writeByteOperation: gameObject.getWriteByteOperations()) {
                        if (writeByteOperation.getIndex() == 0x28d) {
                            AddObject.addWallCopy(gameObject, 2);
                            AddObject.addWallCopy(gameObject, 3);
                            break;
                        }
                    }
                }
            }
        } else if (gameObject.getId() == 0x0e) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == 335) {
                    // deathv stuff
                    GameObjectId gameObjectId = new GameObjectId((short) 96, 335);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if (flagTest.getIndex() == 219) {
                    // Gate of Illusion Map stuff
                    GameObjectId gameObjectId = new GameObjectId((short) 70, 219);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == 299 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 8) {
                        // MARDUK tablet effect
                        List<GameObject> objects = mantraTablets.get("MARDUK");
                        if (objects == null) {
                            mantraTablets.put("MARDUK", new ArrayList<>());
                            objects = mantraTablets.get("MARDUK");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 298 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 7) {
                        // SABBAT tablet effect
                        List<GameObject> objects = mantraTablets.get("SABBAT");
                        if (objects == null) {
                            mantraTablets.put("SABBAT", new ArrayList<>());
                            objects = mantraTablets.get("SABBAT");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 297 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 6) {
                        // MU tablet effect
                        List<GameObject> objects = mantraTablets.get("MU");
                        if (objects == null) {
                            mantraTablets.put("MU", new ArrayList<>());
                            objects = mantraTablets.get("MU");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 296 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 5) {
                        // VIY tablet effect
                        List<GameObject> objects = mantraTablets.get("VIY");
                        if (objects == null) {
                            mantraTablets.put("VIY", new ArrayList<>());
                            objects = mantraTablets.get("VIY");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 295 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 13) {
                        // BAHRUN tablet effect
                        List<GameObject> objects = mantraTablets.get("BAHRUN");
                        if (objects == null) {
                            mantraTablets.put("BAHRUN", new ArrayList<>());
                            objects = mantraTablets.get("BAHRUN");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 294 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 3) {
                        // WEDJET tablet effect
                        List<GameObject> objects = mantraTablets.get("WEDJET");
                        if (objects == null) {
                            mantraTablets.put("WEDJET", new ArrayList<>());
                            objects = mantraTablets.get("WEDJET");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 293 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 11) {
                        // ABUTO tablet effect
                        List<GameObject> objects = mantraTablets.get("ABUTO");
                        if (objects == null) {
                            mantraTablets.put("ABUTO", new ArrayList<>());
                            objects = mantraTablets.get("ABUTO");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 292) {
                    if(flagTest.getValue() == 2) {
                        if (gameObject.getObjectContainer() instanceof Screen
                                && ((Screen) gameObject.getObjectContainer()).getZoneIndex() == 0) {
                            // LAMULANA tablet effect
                            List<GameObject> objects = mantraTablets.get("LAMULANA");
                            if (objects == null) {
                                mantraTablets.put("LAMULANA", new ArrayList<>());
                                objects = mantraTablets.get("LAMULANA");
                            }
                            objects.add(gameObject);
                            break;
                        }
                    }
                    else if (flagTest.getValue() == 3) {
                        flagTest.setIndex(2794);
                        flagTest.setOp(ByteOp.FLAG_LT);
                        flagTest.setValue((byte)1);
                        break;
                    }
                    else if (flagTest.getValue() == 4) {
                        flagTest.setIndex(2794);
                        flagTest.setValue((byte)1);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 570) {
                    if(flagTest.getValue() == 3) {
                        if(ByteOp.FLAG_EQUALS.equals(flagTest.getOp())) {
                            flagTest.setOp(ByteOp.FLAG_LTEQ);
                            gameObject.setX(gameObject.getX() - 60);
                            break;
                        }
                        if(ByteOp.FLAG_LTEQ.equals(flagTest.getOp())) {
                            flagTest.setValue((byte)4);
                            flagTest.setOp(ByteOp.FLAG_LT);
                            break;
                        }
                    }
                    else if(flagTest.getValue() != 4) {
                        flagTest.setIndex(2799);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 0x14c) {
                    gameObject.getArgs().set(4, (short)2);
                }
            }
        } else if (gameObject.getId() == 0x9b) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == 218) {
                    // Shrine of the Mother Map stuff
                    GameObjectId gameObjectId = new GameObjectId((short) 70, 218);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == 267) {
                    // Timer to play Shell Horn sound when being given Maternity Statue equivalent
                    GameObjectId gameObjectId = new GameObjectId((short) 81, 267);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                }
                else if(flagTest.getIndex() == 292) {
                    // This is based on the LAMULANA mantra being recited, but we have a new flag for that.
                    flagTest.setIndex(2794);
                    flagTest.setValue((byte)1);
                    break;
                }
            }
        } else if (gameObject.getId() == 0x9c) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == 552) {
                    // Using Pepper to spawn Treasures chest
                    flagTest.setIndex(259);
                    flagTest.setOp(ByteOp.FLAG_LTEQ);
                    flagTest.setValue((byte)1);

                    GameObjectId gameObjectId = new GameObjectId((short) 71, 259);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == 570) {
                    if(flagTest.getValue() == 3) {
                        if(ByteOp.FLAG_EQUALS.equals(flagTest.getOp())) {
                            flagTest.setOp(ByteOp.FLAG_LTEQ);
                            gameObject.setX(gameObject.getX() - 60);
                            break;
                        }
                        if(ByteOp.FLAG_LTEQ.equals(flagTest.getOp())) {
                            flagTest.setValue((byte)4);
                            flagTest.setOp(ByteOp.FLAG_LT);
                            break;
                        }
                    }
                    else if(flagTest.getValue() != 4) {
                        flagTest.setIndex(2799);
                        break;
                    }
                }
            }
        }
        else if (gameObject.getId() == 0x98) {
//            if(Settings.isHalloweenMode()) {
//                int destinationZoneIndex = gameObject.getArgs().get(1);
//                if(destinationZoneIndex == 1) {
//                    // Night Surface, not normal Surface.
//                    gameObject.getArgs().set(1, (short)22);
//                }
//            }

            if(gameObject.getArgs().get(0) == 0) {
                if(Settings.isRandomizeBacksideDoors()) {
                    Screen screen = (Screen)gameObject.getObjectContainer();
                    String doorName = null;
                    int zone = screen.getZoneIndex();
                    if(zone == 0) {
                        // Gate of Guidance => Gate of Illusion
                        if(screen.getRoomIndex() != 1) {
                            doorName = "Door: F1";
                        }
                    }
                    else if(zone == 1) {
                        // Surface => Tower of the Goddess
                        doorName = "Door: F5";

                        if(!Settings.isRandomizeNonBossDoors()) {
                            GameObject added = AddObject.addMissingBacksideDoorCover(gameObject, 0x153);
                            List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                            if(backsideDoors == null) {
                                backsideDoors = new ArrayList<>();
                                mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                            }
                            backsideDoors.add(added);
                        }
                    }
                    else if(zone == 2) {
                        // Mausoleum of the Giants => Graveyard of the Giants
                        doorName = "Door: F2";
                    }
                    else if(zone == 3) {
                        // Temple of the Sun => Temple of Moonlight
                        doorName = "Door: F3";
                    }
                    else if(zone == 5) {
                        if(screen.getRoomIndex() == 8) {
                            // Inferno Cavern [Viy] => Tower of Ruin [Southwest]
                            doorName = "Door: F4";
                        }
                        else {
                            // Inferno Cavern [Spikes] => Tower of Ruin [Top]
                            doorName = "Door: F7";
                        }
                    }
                    else if(zone == 6) {
                        if(screen.getRoomIndex() == 7) {
                            if(Settings.isRandomizeTransitionGates()) {
                                Integer flagIndexToRemove = null;
                                for(int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                                    TestByteOperation testByteOperation = gameObject.getTestByteOperations().get(i);
                                    if(testByteOperation.getIndex() == 0x382) {
                                        flagIndexToRemove = i;
                                        break;
                                    }
                                }
                                if(flagIndexToRemove != null) {
                                    gameObject.getTestByteOperations().remove((int)flagIndexToRemove);
                                }
                            }
                            if(Settings.isRandomizeNonBossDoors()) {
                                doorName = "Door: F9";
                                replaceBacksideDoorFlags(gameObject, 0x0fb, 0x1d0, false);
                            }
                        }
                        else {
                            // Chamber of Extinction [Magatama Left] => Chamber of Birth [Northeast]
                            doorName = "Door: F6";
                            replaceBacksideDoorFlags(gameObject, 0x0fb, 0x1d0, false);

                            if(!Settings.isRandomizeNonBossDoors()) {
                                GameObject added = AddObject.addMissingBacksideDoorTimerAndSound(screen, 0x0fb, 0x1d0);
                                List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                                if(backsideDoors == null) {
                                    backsideDoors = new ArrayList<>();
                                    mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                                }
                                backsideDoors.add(added);

                                added = AddObject.addMissingBacksideDoorCover(gameObject, 0x1d0);
                                backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                                if(backsideDoors == null) {
                                    backsideDoors = new ArrayList<>();
                                    mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                                }
                                backsideDoors.add(added);

                                added = AddObject.addMissingBacksideMirrorTimerAndSound(gameObject.getObjectContainer(), 0x2b9);
                                backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                                if(backsideDoors == null) {
                                    backsideDoors = new ArrayList<>();
                                    mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                                }
                                backsideDoors.add(added);

                                added = AddObject.addMissingBacksideDoorMirrorCoverGraphic(gameObject, 0x2b9, true);
                                backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                                if(backsideDoors == null) {
                                    backsideDoors = new ArrayList<>();
                                    mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                                }
                                backsideDoors.add(added);
                            }
                        }
                    }
                    else if(zone == 10) {
                        // Gate of Illusion [Grail] => Gate of Guidance
                        doorName = "Door: B1";
                    }
                    else if(zone == 11) {
                        // Graveyard of the Giants [West] => Mausoleum of the Giants
                        doorName = "Door: B2";
                    }
                    else if(zone == 12) {
                        // Temple of Moonlight [Lower] => Temple of the Sun [Main]
                        doorName = "Door: B3";
                    }
                    else if(zone == 13) {
                        // Tower of the Goddess [Lower] => Surface [Main]
                        if(screen.getRoomIndex() == 0) {
                            doorName = "Door: B5";
                        }
                    }
                    else if(zone == 14) {
                        if(screen.getRoomIndex() == 2) {
                            // Tower of Ruin [Southwest] => Inferno Cavern [Viy]
                            doorName = "Door: B4";
                        }
                        else {
                            // Tower of Ruin [Top] => Inferno Cavern [Spikes]
                            doorName = "Door: B7";
                            replaceBacksideDoorFlags(gameObject, 0x0fc, 0x1c0, false);

                            if(!Settings.isRandomizeNonBossDoors()) {
                                GameObject added = AddObject.addMissingBacksideDoorTimerAndSound(screen, 0x0fc, 0x1c0);
                                List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                                if(backsideDoors == null) {
                                    backsideDoors = new ArrayList<>();
                                    mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                                }
                                backsideDoors.add(added);

                                added = AddObject.addMissingBacksideDoorCover(gameObject, 0x1c0);
                                backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                                if(backsideDoors == null) {
                                    backsideDoors = new ArrayList<>();
                                    mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                                }
                                backsideDoors.add(added);

                                added = AddObject.addMissingBacksideMirrorTimerAndSound(gameObject.getObjectContainer(), 0x3b7);
                                backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                                if(backsideDoors == null) {
                                    backsideDoors = new ArrayList<>();
                                    mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                                }
                                backsideDoors.add(added);

                                added = AddObject.addMissingBacksideDoorMirrorCoverGraphic(gameObject, 0x3b7, false);
                                backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                                if(backsideDoors == null) {
                                    backsideDoors = new ArrayList<>();
                                    mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                                }
                                backsideDoors.add(added);
                            }
                        }
                    }
                    else if(zone == 15) {
                        // Chamber of Birth [Northeast] => Chamber of Extinction [Magatama Left]
                        doorName = "Door: B6";
                    }
                    else if(Settings.isRandomizeNonBossDoors()) {
                        if(zone == 17) {
                            // Dimensional Corridor [Grail] => Endless Corridor [1F]
                            doorName = "Door: B8";
                        }
                        else if(zone == 19) {
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
                else if(Settings.isRandomizeTransitionGates()) {
                    // Re-enable backside door during the escape
                    Integer flagTestToRemove = null;
                    for (int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                        if (gameObject.getTestByteOperations().get(i).getIndex() == 0x0fe) {
                            flagTestToRemove = i;
                            break;
                        }
                    }
                    if(flagTestToRemove != null) {
                        gameObject.getTestByteOperations().remove((int)flagTestToRemove);
                    }
                }

//                if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
//                    // Add escape door in place of normal HT to Guidance door
//                    Screen screen = (Screen)gameObject.getObjectContainer();
//                    if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
//                        if(gameObject.getArgs().get(1) == 0) {
//                            GameObject replacementDoor = new GameObject(gameObject);
//                            replacementDoor.setId((short)0xa0);
//                            replacementDoor.getArgs().clear();
//                            replacementDoor.getArgs().add((short)0);
//                            replacementDoor.getArgs().add((short)0);
//                            replacementDoor.getArgs().add((short)0);
//                            replacementDoor.getArgs().add((short)0);
//                            replacementDoor.getArgs().add((short)926);
//                            replacementDoor.getArgs().add((short)0);
//                            replacementDoor.getArgs().add((short)1);
//                            replacementDoor.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1));
//                            gameObject.getObjectContainer().getObjects().add(replacementDoor);
//
//                            gameObject.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0));
//                        }
//                    }
//                }
            }
        }
        else if(gameObject.getId() == 0x6b) {
            // Fighting Anubis shouldn't prevent Mulbruk from giving you Book of the Dead.
            Integer flagUpdateToRemove = null;
            for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
                if (gameObject.getWriteByteOperations().get(i).getIndex() == 810) {
                    flagUpdateToRemove = i;
                    break;
                }
            }
            if(flagUpdateToRemove != null) {
                gameObject.getWriteByteOperations().remove((int)flagUpdateToRemove);
            }
        }
        else if (gameObject.getId() == 0x11) {
            for (WriteByteOperation flagUpdate : gameObject.getWriteByteOperations()) {
                if (flagUpdate.getIndex() == 218) {
                    // Shrine of the Mother Map stuff
                    GameObjectId gameObjectId = new GameObjectId((short) 70, 218);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagUpdate.getIndex() == 433) {
                    // Chain Whip puzzle crusher
                    flagUpdate.setIndex(46);
                    break;
                }
                else if(flagUpdate.getIndex() == 434) {
                    // Chain Whip puzzle crusher
                    flagUpdate.setIndex(47);
                    break;
                }
            }
        }
        else if (gameObject.getId() == 0x14) {
            // Temple of the Sun Map chest ladder stuff
            ObjectContainer objectContainer = gameObject.getObjectContainer();
            if(objectContainer instanceof Screen) {
                Screen screen = (Screen)objectContainer;
                if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                    if(gameObject.getTestByteOperations().get(0).getIndex() == 387) {
                        gameObject.getWriteByteOperations().remove(0);
                    }
                    else if(gameObject.getTestByteOperations().get(0).getIndex() == 392) {
                        WriteByteOperation writeByteOperation = new WriteByteOperation();
                        writeByteOperation.setIndex(387);
                        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
                        writeByteOperation.setValue(1);
                        gameObject.getWriteByteOperations().add(writeByteOperation);
                    }
                }
            }
            if(Settings.isFools2020Mode()) {
                boolean lampOfTimeDetector = false;
                for(WriteByteOperation writeByteOperation : gameObject.getWriteByteOperations()) {
                    if(writeByteOperation.getIndex() == 0x34d && ByteOp.ASSIGN_FLAG.equals(writeByteOperation.getOp()) && writeByteOperation.getValue() == 1) {
                        // Lemeza detector for Lamp of Time light
                        lampOfTimeDetector = true;
                        break;
                    }
                }
                if(lampOfTimeDetector) {
                    gameObject.getWriteByteOperations().clear();
                    gameObject.getWriteByteOperations().add(new WriteByteOperation(42, ByteOp.ASSIGN_FLAG, 1));
                    AddObject.addExplosion(gameObject.getObjectContainer(), gameObject.getX(), gameObject.getY(), 42, 15, true);
                }
            }
        } else if (gameObject.getId() == 0x33) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == 180) {
                    // Plane Model stuff
                    GameObjectId gameObjectId = new GameObjectId((short) 51, 180);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
            }
        } else if (gameObject.getId() == 0x71) {
            // Vimana
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == 180) {
                    // Plane Model stuff
                    GameObjectId gameObjectId = new GameObjectId((short) 51, 180);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
            }
            if(Settings.isFools2020Mode()) {
                List<TestByteOperation> testsToUse = new ArrayList<>();
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 0x0b4) {
                        // Don't stop spawning after you get the chest.
                        continue;
                    }
                    if(flagTest.getIndex() == gameObject.getWriteByteOperations().get(0).getIndex()) {
                        // Don't stop spawning if you've killed one already.
                        continue;
                    }
                    testsToUse.add(flagTest);
                }
                gameObject.getTestByteOperations().clear();
                gameObject.getTestByteOperations().addAll(testsToUse);
                gameObject.getArgs().set(3, (short)10); // Increase speed
                gameObject.getArgs().set(4, (short)1); // Set HP to 1
            }
        } else if (gameObject.getId() == 0x08) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == 229) {
                    // yagostr dais
                    GameObjectId gameObjectId = new GameObjectId((short) 88, 229);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == 144) {
                    // Temple of the Sun Ankh Jewel trap dais
                    GameObjectId gameObjectId = new GameObjectId((short) 19, 144);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == 433) {
                    // Chain Whip puzzle crusher
                    flagTest.setIndex(46);

                    gameObject.getWriteByteOperations().get(0).setIndex(46);
                    break;
                }
                else if(flagTest.getIndex() == 434) {
                    // Chain Whip puzzle crusher
                    flagTest.setIndex(47);

                    gameObject.getWriteByteOperations().get(0).setIndex(47);
                    break;
                }
                else if(Settings.isRandomizeTrapItems()) {
                    if(gameObject.getObjectContainer() instanceof Screen) {
                        Screen screen = (Screen) gameObject.getObjectContainer();
                        if (screen.getZoneIndex() == 11 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 3) {
                            // Graveyard trap chest dais
                            gameObject.getTestByteOperations().get(0).setIndex(2776);
                            gameObject.getWriteByteOperations().get(0).setIndex(2776);
                            gameObject.getWriteByteOperations().remove(1);

                            GameObjectId gameObjectId = new GameObjectId((short) 0, 2777);
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
            }
        } else if (gameObject.getId() == 0xb6) {
            if(LocationCoordinateMapper.getStartingZone() == 16) {
                // Move Chamber of Birth grail point when starting in that area.
                Screen screen = (Screen) gameObject.getObjectContainer();
                if(screen.getZoneIndex() == 16) {
                    gameObject.setX(200);
                    gameObject.setY(380);
                }
            }
        } else if (gameObject.getId() == 0x9f) {
            if(!LocationCoordinateMapper.isSurfaceStart()) {
                Screen screen = (Screen) gameObject.getObjectContainer();
                int zoneIndex = screen.getZoneIndex();
                if(zoneIndex == 1) {
                    gameObject.getWriteByteOperations().clear();

                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(0xad3);
                    testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                    testByteOperation.setValue((byte)1);
                    gameObject.getTestByteOperations().add(testByteOperation);

                    WriteByteOperation writeByteOperation = new WriteByteOperation();
                    writeByteOperation.setIndex(0xad3);
                    writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
                    writeByteOperation.setValue((byte)1);
                    gameObject.getWriteByteOperations().add(writeByteOperation);
                }
                if(zoneIndex == 16 && LocationCoordinateMapper.getStartingZone() == 16) {
                    // Move Chamber of Birth grail point when starting in that area.
                    gameObject.setX(200);
                    gameObject.setY(380);
                }
                if(zoneIndex <= 18
                        && LocationCoordinateMapper.getStartingZone() == zoneIndex
                        && LocationCoordinateMapper.getStartingRoom() == screen.getRoomIndex()
                        && LocationCoordinateMapper.getStartingScreen() == screen.getScreenIndex()) {
                    AddObject.addHotspring(gameObject);
                }
            }

            if(gameObject.getObjectContainer() instanceof Screen) {
                if (Settings.isFools2021Mode()) {
                    int languageBlock = gameObject.getArgs().get(0);
                    int zoneIndex = ((Screen)gameObject.getObjectContainer()).getZoneIndex();
                    boolean front = (languageBlock == 41 || languageBlock == 75 || languageBlock == 104 || languageBlock == 136
                            || languageBlock == 149 || languageBlock == 170 || languageBlock == 188 || languageBlock == 221)
                            || (languageBlock == 231 && zoneIndex == 9);
                    // Swap out original for custom flags
                    for (TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                        if (testByteOperation.getIndex() == LocationCoordinateMapper.getOriginalGrailFlag(zoneIndex, front)) {
                            testByteOperation.setIndex(LocationCoordinateMapper.getGrailFlag(zoneIndex, front));
                        }
                    }
                    for (WriteByteOperation writeByteOperation : gameObject.getWriteByteOperations()) {
                        if (writeByteOperation.getIndex() == LocationCoordinateMapper.getOriginalGrailFlag(zoneIndex, front)) {
                            writeByteOperation.setIndex(LocationCoordinateMapper.getGrailFlag(zoneIndex, front));
                        }
                    }
                }
            }
        } else if (gameObject.getId() == 0x9e) {
            if(LocationCoordinateMapper.getStartingZone() == 16) {
                Screen screen = (Screen) gameObject.getObjectContainer();
                if(screen.getZoneIndex() == 16
                        && screen.getRoomIndex() == LocationCoordinateMapper.getStartingRoom()
                        && screen.getScreenIndex() == LocationCoordinateMapper.getStartingScreen()
                        && gameObject.getX() == 340 && gameObject.getY() == 320) {
                    // Move Chamber of Birth grail point when starting in that area.
                    gameObject.setX(200);
                    gameObject.setY(380);
                }
            }

            int languageBlock = gameObject.getArgs().get(0);
            if(gameObject.getObjectContainer() instanceof Screen) {
                int zoneIndex = ((Screen)gameObject.getObjectContainer()).getZoneIndex();
                boolean front = (languageBlock == 41 || languageBlock == 75 || languageBlock == 104 || languageBlock == 136
                        || languageBlock == 149 || languageBlock == 170 || languageBlock == 188 || languageBlock == 221)
                        || (languageBlock == 231 && zoneIndex == 9);

                if(Settings.isAutomaticGrailPoints()) {
                    AddObject.addGrailDetector(gameObject, LocationCoordinateMapper.getGrailFlag(zoneIndex, front));
                }
                if (Settings.isFools2021Mode()) {
                    // Swap out original for custom flags
                    for (TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                        if (testByteOperation.getIndex() == LocationCoordinateMapper.getOriginalGrailFlag(zoneIndex, front)) {
                            testByteOperation.setIndex(LocationCoordinateMapper.getGrailFlag(zoneIndex, front));
                        }
                    }
                    for (WriteByteOperation writeByteOperation : gameObject.getWriteByteOperations()) {
                        if (writeByteOperation.getIndex() == LocationCoordinateMapper.getOriginalGrailFlag(zoneIndex, front)) {
                            writeByteOperation.setIndex(LocationCoordinateMapper.getGrailFlag(zoneIndex, front));
                        }
                    }
                }
                return;
            }

            if(languageBlock == 648) {
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 292 && flagTest.getValue() == 4) {
                        flagTest.setIndex(2794);
                        flagTest.setValue((byte)1);
                        break;
                    }
                }
            }
            else {
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 209) {
                        // Surface map scan effect?
                        GameObjectId gameObjectId = new GameObjectId((short) 70, 209);
                        List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        if (objects == null) {
                            mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                            objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        }
                        objects.add(gameObject);
                        break;
                    }
                    else if(flagTest.getIndex() == 570) {
                        if(flagTest.getValue() == 3) {
                            if(ByteOp.FLAG_EQUALS.equals(flagTest.getOp())) {
                                flagTest.setOp(ByteOp.FLAG_LTEQ);
                                gameObject.setX(gameObject.getX() - 60);
                                break;
                            }
                            if(ByteOp.FLAG_LTEQ.equals(flagTest.getOp())) {
                                flagTest.setValue((byte)4);
                                flagTest.setOp(ByteOp.FLAG_LT);
                                break;
                            }
                        }
                        else if(flagTest.getValue() != 4) {
                            flagTest.setIndex(2799);
                            break;
                        }
                    }
                }
            }
        } else if (gameObject.getId() == 0xa0) {
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
                        if (flagTest.getIndex() == 742) {
                            GameObjectId gameObjectId = new GameObjectId((short) 76, 742);
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
                else if(blockNumber == 490) {
                    // MSX2 shop
                    GameObjectId gameObjectId = new GameObjectId((short) 76, 742);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    objects.add(AddObject.addAltSurfaceShopItemTimer(gameObject.getObjectContainer()));
                }
                else if(blockNumber == 132){
                   // Untransformed Gyonin fish shop
                   GameObject backupFishShop = AddObject.addBackupGyoninFishShop(gameObject);
                   List<GameObject> objects = mapOfShopBlockToShopObjects.get(blockNumber);
                   if (objects == null) {
                       mapOfShopBlockToShopObjects.put(blockNumber, new ArrayList<>());
                       objects = mapOfShopBlockToShopObjects.get(blockNumber);
                   }
                   objects.add(backupFishShop);
               }
            }
            else if(blockNumber == 915) {
                // Mini Doll conversation
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 554) {
                        flagTest.setIndex(152); // Normally this goes off a flag related to proving yourself small, but we'd rather check if we have the Mini Doll for safer shuffling.

                        GameObjectId gameObjectId = new GameObjectId((short) 22, 152);
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
            else if(blockNumber == 671) {
                // Hiner - Surface NPC, 01-00-02
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 672) {
                // Moger - Surface NPC, 01-02-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 673) {
                // Former Mekuri Master - Surface NPC, 01-07-00
                npcObjects.add(gameObject);

                GameObjectId gameObjectId = new GameObjectId((short) 100, 241);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == 674) {
                // Priest Zarnac - Guidance NPC, 00-04-01
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 675) {
                // Priest Xanado - Mausoleum NPC, 02-02-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 677) {
                // Philosopher Giltoriyo - Spring NPC, 04-00-01
                npcObjects.add(gameObject);

                // Giltoriyo mantra conversation
                for (WriteByteOperation flagUpdate : gameObject.getWriteByteOperations()) {
                    if(flagUpdate.getIndex() == 299) {
                        flagUpdate.setIndex(2795);
                    }
                }
            }
            else if(blockNumber == 678) {
                // Priest Hidlyda - Spring NPC, 04-06-01
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 679) {
                // Priest Romancis - Inferno NPC, 05-03-02
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 680) {
                // Priest Aramo - Extinction NPC, 06-06-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 681) {
                // Priest Triton - Extinction NPC, 06-09-01
                npcObjects.add(gameObject);

                if(Settings.isHalloweenMode()) {
                    for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                        if (flagTest.getIndex() == 0x1c3) {
                            // Fix conversation to be based on whether the Palenque fight is active, rather than whether the ankh is present.
                            flagTest.setIndex(0x0fb);
                            flagTest.setOp(ByteOp.FLAG_NOT_EQUAL);
                            flagTest.setValue((byte)2);
                            break;
                        }
                    }
                }
            }
            else if(blockNumber == 683) {
                // Priest Jaguarfiv - Twin Labs NPC, 07-10-01
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 684) {
                // Fairy Queen - Endless NPC, 08-01-00
                if(Settings.isHalloweenMode()) {
                    Integer flagToRemoveIndex = null;
                    for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
                        if (gameObject.getWriteByteOperations().get(i).getIndex() == 0x1f5) {
                            flagToRemoveIndex = i;
                            break;
                        }
                    }
                    if(flagToRemoveIndex != null) {
                        gameObject.getWriteByteOperations().remove((int)flagToRemoveIndex);
                    }
                }
            }
            else if(blockNumber == 685) {
                if(!Settings.isHalloweenMode()) {
                    for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                        if (flagTest.getIndex() == 501) {
                            // The first conversation with the Fairy Queen is removed. Subsequent conversations must
                            // therefore not require the first one to have happened, so we'll make the conversation
                            // check for flag value <= 1 instead of == 1
                            flagTest.setOp(ByteOp.FLAG_LTEQ);
                            break;
                        }
                    }
                }
            }
            else if(blockNumber == 686) {
                if(Settings.isHalloweenMode()) {
                    gameObject.getTestByteOperations().clear();
                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(0x1f5);
                    testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                    testByteOperation.setValue((byte)2);
                    gameObject.getTestByteOperations().add(testByteOperation);

                    testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(0x0aa);
                    testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                    testByteOperation.setValue((byte)2);
                    gameObject.getTestByteOperations().add(testByteOperation);

                    testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(0x0fe);
                    testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                    testByteOperation.setValue((byte)3);
                    gameObject.getTestByteOperations().add(testByteOperation);

                    for(WriteByteOperation writeByteOperation : gameObject.getWriteByteOperations()) {
                        if(writeByteOperation.getIndex() == 0x1f5) {
                            writeByteOperation.setValue(3);
                            break;
                        }
                    }
                }
                else {
                    for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                        if (flagTest.getIndex() == 501) {
                            // The first conversation with the Fairy Queen is removed. Subsequent conversations must
                            // therefore not require the first one to have happened, so we'll make the conversation
                            // check for flag value <= 1 instead of == 1
                            flagTest.setOp(ByteOp.FLAG_LTEQ);
                            break;
                        }
                    }
                }
            }
            else if(blockNumber == 687) {
                if(Settings.isHalloweenMode()) {
                    gameObject.getTestByteOperations().clear();
                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(0x1f5);
                    testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                    testByteOperation.setValue((byte)2);
                    gameObject.getTestByteOperations().add(testByteOperation);

                    testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(0x0aa);
                    testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                    testByteOperation.setValue((byte)2);
                    gameObject.getTestByteOperations().add(testByteOperation);

                    testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(0x0fe);
                    testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                    testByteOperation.setValue((byte)3);
                    gameObject.getTestByteOperations().add(testByteOperation);
                }
            }
            else if(blockNumber == 688) {
                if(Settings.isHalloweenMode()) {
                    gameObject.getArgs().set(4, (short)687);

                    gameObject.getTestByteOperations().clear();
                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(0x1f5);
                    testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                    testByteOperation.setValue((byte)3);
                    gameObject.getTestByteOperations().add(testByteOperation);

                    testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(0x0fe);
                    testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                    testByteOperation.setValue((byte)3);
                    gameObject.getTestByteOperations().add(testByteOperation);

                    gameObject.getWriteByteOperations().clear();
                }
            }
            else if(blockNumber == 689) {
                // Mr. Slushfund - Illusion NPC, 10-08-00
                // Conversation to receive Pepper
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 552) {
                        flagTest.setIndex(2784);
                        flagTest.setOp(ByteOp.FLAG_EQUALS);
                        flagTest.setValue((byte)0);
                        break;
                    }
                }

                GameObjectId gameObjectId = new GameObjectId((short) 30, 2702);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);

                npcObjects.add(gameObject);
            }
            else if(blockNumber == 690) {
                // Conversation after receiving Pepper if you don't have Treasures
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 552) {
                        // Swap out the Pepper/Treasures/Anchor combo flag with Pepper received flag
                        flagTest.setIndex(2784);
                        flagTest.setOp(ByteOp.FLAG_GT);
                        flagTest.setValue((byte)0);
                        break;
                    }
                }

                if(Settings.isHalloweenMode()) {
                    // Mr. Slushfund - Illusion NPC, 10-08-00
                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(getNpcConversationFlag((short)689));
                    testByteOperation.setOp(ByteOp.FLAG_GT);
                    testByteOperation.setValue((byte)0);
                    gameObject.getTestByteOperations().add(testByteOperation);
                }
            }
            else if(blockNumber == 691) {
                // Conversation to give Treasures and receive Anchor
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 552) {
                        // Swap out the Pepper/Treasures/Anchor combo flag with Anchor custom world flag
                        flagTest.setIndex(2706);
                        flagTest.setOp(ByteOp.FLAG_LT);
                        flagTest.setValue((byte)2);

                        GameObjectId gameObjectId = new GameObjectId((short) 50, 2706);
                        List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        if (objects == null) {
                            mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                            objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        }
                        objects.add(gameObject);
                        break;
                    }
                }

                if(Settings.isHalloweenMode()) {
                    // Mr. Slushfund - Illusion NPC, 10-08-00
                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(getNpcConversationFlag((short)689));
                    testByteOperation.setOp(ByteOp.FLAG_GT);
                    testByteOperation.setValue((byte)0);
                    gameObject.getTestByteOperations().add(testByteOperation);
                }
            }
            else if(blockNumber == 692) {
                // Conversation after receiving both Pepper and Anchor
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 552) {
                        // Swap out the Pepper/Treasures/Anchor combo flag with Anchor custom world flag
                        flagTest.setIndex(2706);
                        flagTest.setOp(ByteOp.FLAG_GTEQ);
                        flagTest.setValue((byte)2);

                        GameObjectId gameObjectId = new GameObjectId((short) 50, 2706);
                        List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        if (objects == null) {
                            mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                            objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                // Add a check for Pepper conversation (otherwise we could get this conversation before being given Pepper).
                TestByteOperation pepperCheck = new TestByteOperation();
                pepperCheck.setIndex(2784);
                pepperCheck.setOp(ByteOp.FLAG_GT);
                pepperCheck.setValue((byte)0);

                gameObject.getTestByteOperations().add(pepperCheck);

                if(Settings.isHalloweenMode()) {
                    // Mr. Slushfund - Illusion NPC, 10-08-00
                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(getNpcConversationFlag((short)689));
                    testByteOperation.setOp(ByteOp.FLAG_GT);
                    testByteOperation.setValue((byte)0);
                    gameObject.getTestByteOperations().add(testByteOperation);
                }
            }
            if(blockNumber == 693) {
                // Priest Alest - Illusion NPC, 10-08-01
                npcObjects.add(gameObject);

                // Mini Doll conversation
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 554) {
                        flagTest.setIndex(152); // Normally this goes off a flag related to proving yourself small, but we'd rather check if we have the Mini Doll for safer shuffling.

                        GameObjectId gameObjectId = new GameObjectId((short) 22, 152);
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
            else if(blockNumber == 694) {
                // Stray fairy - Illusion NPC, 10-00-01
                npcObjects.add(gameObject);

                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 570) {
                        // Swap out the original Cog of the Soul puzzle flag for the custom flag
                        flagTest.setIndex(2799);
                    }
                }
                for(WriteByteOperation flagUpdate : gameObject.getWriteByteOperations()) {
                    if (flagUpdate.getIndex() == 570) {
                        // Swap out the original Cog of the Soul puzzle flag for the custom flag
                        flagUpdate.setIndex(2799);
                    }
                }
            }
            else if(blockNumber == 695) {
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 570) {
                        // Swap out the original Cog of the Soul puzzle flag for the custom flag
                        flagTest.setIndex(2799);
                    }
                }
                for(WriteByteOperation flagUpdate : gameObject.getWriteByteOperations()) {
                    if (flagUpdate.getIndex() == 570) {
                        // Swap out the original Cog of the Soul puzzle flag for the custom flag
                        flagUpdate.setIndex(2799);
                    }
                }
            }
            else if(blockNumber == 696) {
                // Giant Thexde - Graveyard NPC, 11-07-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 698) {
                // Philosopher Alsedana - Moonlight NPC, 12-06-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 700) {
                // Philosopher Samaranta - Goddess NPC, 13-05-01
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 701) {
                // Priest Laydoc - Ruin NPC, 14-00-01
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 702) {
                // Priest Ashgine - Birth NPC, 16-01-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 704) {
                // Philosopher Fobos - Dimensional NPC, 17-02-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 705) {
                // Philosopher Fobos - Dimensional NPC, 17-02-00
                // Post-Medicine version of Fobos
                if(Settings.isHalloweenMode()) {
                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(getNpcConversationFlag((short)704));
                    testByteOperation.setOp(ByteOp.FLAG_GT);
                    testByteOperation.setValue((byte)0);
                    gameObject.getTestByteOperations().add(testByteOperation);
                }
            }
            else if(blockNumber == 706) {
                // 8bit Elder - Gate of Time NPC, 21-00-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 707) {
                // duplex - Illusion NPC, 10-02-02
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 708) {
                // Samieru - Moonlight NPC, 12-03-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 709) {
                // Naramura - Goddess NPC, 13-06-03
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 710) {
                // 8bit Fairy - Gate of Time NPC, 20-00-01
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 714) {
                if(Settings.isHalloweenMode()) {
                    // 8bit Fairy - conversation needs added test
                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(getNpcConversationFlag((short)710));
                    testByteOperation.setOp(ByteOp.FLAG_GT);
                    testByteOperation.setValue((byte)0);
                    gameObject.getTestByteOperations().add(testByteOperation);
                }
            }
            else if(blockNumber == 718) {
                // Priest Madomono - Sun NPC, 03-04-02
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 723) {
                // Priest Gailious - Inferno NPC, 05-02-01
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 726) {
                // Tailor Dracuet - Guidance NPC, 00-06-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 991) {
                // Tailor Dracuet - Illusion NPC, 10-07-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 993) {
                // Tailor Dracuet - Gate of Time NPC, 20-03-01
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 998) {
                if(Settings.isIncludeHellTempleNPCs()) {
                    // Fairy - Night Surface NPC, 22-07-00
                    npcObjects.add(gameObject);
                }
            }
            else if(blockNumber == 1000) {
                // Tailor Dracuet - Goddess NPC, 13-01-00
                npcObjects.add(gameObject);
            }
            else if(blockNumber == 484 || blockNumber == 1019
                    || blockNumber == 1080 || blockNumber == 1081) {
                // Remove the flags that prevent normal Xelpud convos if he leaves for the Diary puzzle
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                    if (gameObject.getTestByteOperations().get(i).getIndex() == 537) {
                        flagToRemoveIndex = i;
                        break;
                    }
                }
                if(flagToRemoveIndex != null) {
                    gameObject.getTestByteOperations().remove((int)flagToRemoveIndex);
                }
            }
            else if(blockNumber == 485) {
                // Remove the flags that prevent normal Mulbruk convos if you have Forbidden Treasure/Provocative Bathing Suit.
                // Also remove score requirement on Mulbruk conversation.
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                    TestByteOperation flagTest = gameObject.getTestByteOperations().get(i);
                    if (flagTest.getIndex() == 874) {
                        flagToRemoveIndex = i;
                    }
                    else if(flagTest.getIndex() == 123 && flagTest.getValue() == 56) {
                        flagTest.setValue((byte)0);
                    }
                }
                if(flagToRemoveIndex != null) {
                    gameObject.getTestByteOperations().remove((int)flagToRemoveIndex);
                }
            }
            else if(blockNumber == 990) {
                // Mulbruk misc conversation priority below Book of the Dead
                TestByteOperation testByteOperation = new TestByteOperation();
                testByteOperation.setIndex(810);
                testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                testByteOperation.setValue((byte)1);
                gameObject.getTestByteOperations().add(testByteOperation);
            }
            else if(blockNumber == 1011) {
                // Dracuet Provocative Bathing Suit conversation - needs to depend on HT item instead.
                GameObjectId gameObjectId = new GameObjectId((short) 74, 262);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == 1013) {
                // Mulbruk Provocative Bathing Suit conversation - needs to depend on HT item instead.
                GameObjectId gameObjectId = new GameObjectId((short) 74, 262);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == 1082 || blockNumber == 1083 || blockNumber == 924) {
                // Remove the flags that prevent normal Mulbruk convos if you have Forbidden Treasure/Provocative Bathing Suit
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                    if (gameObject.getTestByteOperations().get(i).getIndex() == 874) {
                        flagToRemoveIndex = i;
                        break;
                    }
                }
                if(flagToRemoveIndex != null) {
                    gameObject.getTestByteOperations().remove((int)flagToRemoveIndex);
                }
            }
        } else if (gameObject.getId() == 0x92) {
            if(Settings.isFools2020Mode()) {
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if(flagTest.getIndex() == 0x1cd) {
                        flagTest.setValue((byte)(flagTest.getValue() == 0 ? 1 : 0));
                    }
                }
            }
        } else if (gameObject.getId() == 0x93) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if(!Settings.isRandomizeNonBossDoors()) {
                    if(flagTest.getIndex() == 0x15c || flagTest.getIndex() == 0x15d) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 0
                                ? "Door: F1" : "Door: B1";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == 0x16d || flagTest.getIndex() == 0x16e) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 2
                                ? "Door: F2" : "Door: B2";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == 0x175 || flagTest.getIndex() == 0x176) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 3
                                ? "Door: F3" : "Door: B3";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == 0x1bd || flagTest.getIndex() == 0x1be) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 5
                                ? "Door: F4" : "Door: B4";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == 0x152 || flagTest.getIndex() == 0x153) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 1
                                ? "Door: F5" : "Door: B5";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == 0x2b9 || flagTest.getIndex() == 0x1d0) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 6
                                ? "Door: F6" : "Door: B6";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagTest.getIndex() == 0x3b7 || flagTest.getIndex() == 0x1c0) {
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

                if(Settings.isBlockPushingRequiresGlove()) {
                    if(flagTest.getIndex() == 0x28d) {
                        if (gameObject.getArgs().get(2) != 360 || gameObject.getArgs().get(3) != 520) {
                            // The blockage on the Ruin pushblock can disappear as normal, but the pushability needs to be modified.
                            if(flagTest.getValue() == 0 || flagTest.getValue() == 2) {
                                flagTest.setIndex(0xacc);
                            }
                        }
                    }
                }

                if (flagTest.getIndex() == 241) {
                    // mekuri tent-closing effect
                    GameObjectId gameObjectId = new GameObjectId((short) 100, 241);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == 171) {
                    // Crucifix puzzle lit torches
                    GameObjectId gameObjectId = new GameObjectId((short) 42, 171);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == 262) {
                    // HT Dracuet stuff
                    GameObjectId gameObjectId = new GameObjectId((short) 74, 262);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
                else if(flagTest.getIndex() == 299 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 8) {
                        // MARDUK tablet effect
                        List<GameObject> objects = mantraTablets.get("MARDUK");
                        if (objects == null) {
                            mantraTablets.put("MARDUK", new ArrayList<>());
                            objects = mantraTablets.get("MARDUK");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 298 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 7) {
                        // SABBAT tablet effect
                        List<GameObject> objects = mantraTablets.get("SABBAT");
                        if (objects == null) {
                            mantraTablets.put("SABBAT", new ArrayList<>());
                            objects = mantraTablets.get("SABBAT");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 297 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 6) {
                        // MU tablet effect
                        List<GameObject> objects = mantraTablets.get("MU");
                        if (objects == null) {
                            mantraTablets.put("MU", new ArrayList<>());
                            objects = mantraTablets.get("MU");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 296 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 5) {
                        // VIY tablet effect
                        List<GameObject> objects = mantraTablets.get("VIY");
                        if (objects == null) {
                            mantraTablets.put("VIY", new ArrayList<>());
                            objects = mantraTablets.get("VIY");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 295 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 13) {
                        // BAHRUN tablet effect
                        List<GameObject> objects = mantraTablets.get("BAHRUN");
                        if (objects == null) {
                            mantraTablets.put("BAHRUN", new ArrayList<>());
                            objects = mantraTablets.get("BAHRUN");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 294 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 3) {
                        // WEDJET tablet effect
                        List<GameObject> objects = mantraTablets.get("WEDJET");
                        if (objects == null) {
                            mantraTablets.put("WEDJET", new ArrayList<>());
                            objects = mantraTablets.get("WEDJET");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 293 && flagTest.getValue() == 2) {
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 11) {
                        // ABUTO tablet effect
                        List<GameObject> objects = mantraTablets.get("ABUTO");
                        if (objects == null) {
                            mantraTablets.put("ABUTO", new ArrayList<>());
                            objects = mantraTablets.get("ABUTO");
                        }
                        objects.add(gameObject);
                        break;
                    }
                }
                else if(flagTest.getIndex() == 292) {
                    if(flagTest.getValue() == 2) {
                        if(gameObject.getObjectContainer() instanceof Screen
                                && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 0) {
                            // LAMULANA tablet effect
                            List<GameObject> objects = mantraTablets.get("LAMULANA");
                            if (objects == null) {
                                mantraTablets.put("LAMULANA", new ArrayList<>());
                                objects = mantraTablets.get("LAMULANA");
                            }
                            objects.add(gameObject);
                            break;
                        }
                    }
                    else if(flagTest.getValue() == 4) {
                        flagTest.setIndex(2794);
                        flagTest.setValue((byte)1);
                    }
                }
                else if(flagTest.getIndex() == 570) {
                    if(flagTest.getValue() == 3) {
                        if(ByteOp.FLAG_EQUALS.equals(flagTest.getOp())) {
                            flagTest.setOp(ByteOp.FLAG_LTEQ);
                            gameObject.setX(gameObject.getX() - 60);
                            break;
                        }
                        if(ByteOp.FLAG_LTEQ.equals(flagTest.getOp())) {
                            flagTest.setValue((byte)4);
                            flagTest.setOp(ByteOp.FLAG_LT);
                            break;
                        }
                    }
                    else if(flagTest.getValue() != 4) {
                        flagTest.setIndex(2799);
                        break;
                    }
                }
            }
        } else if (gameObject.getId() == 0x0b) {
            if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
                if(gameObject.getObjectContainer() instanceof Screen) {
                    Screen screen = (Screen) gameObject.getObjectContainer();
                    if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
                        for (int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                            TestByteOperation flagTest = gameObject.getTestByteOperations().get(i);
                            if (flagTest.getIndex() == 0x000) {
                                flagTest.setIndex(0xaad);
                                break;
                            }
                        }
                    }
                }
            }
            if(Settings.isFools2020Mode()) {
                if(gameObject.getObjectContainer() instanceof Screen) {
                    Screen screen = (Screen) gameObject.getObjectContainer();
                    if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 2) {
                        for (int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                            TestByteOperation flagTest = gameObject.getTestByteOperations().get(i);
                            if (flagTest.getIndex() == 0x1ea) {
                                flagTest.setValue((byte)4);
                                break;
                            }
                        }
                    }
                }
            }
            // Timer objects
            for (WriteByteOperation flagUpdate : gameObject.getWriteByteOperations()) {
                if (flagUpdate.getIndex() == 554 && flagUpdate.getValue() == 3) {
                    // Becoming small
                    for(TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                        if (flagTest.getIndex() == 554 && ByteOp.FLAG_EQUALS.equals(flagTest.getOp()) && flagTest.getValue() == 2) {
                            flagTest.setOp(ByteOp.FLAG_LTEQ);
                            return;
                        }
                    }
                }
                else if(flagUpdate.getIndex() == 844) {
                    if(flagUpdate.getValue() == 8) {
                        // Mulbruk swimsuit conversation timer.
                        GameObjectId gameObjectId = new GameObjectId((short) 74, 262);
                        List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        if (objects == null) {
                            mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                            objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                        }
                        objects.add(gameObject);
                        return;
                    }
                    else if(flagUpdate.getValue() == 1) {
                        if((Settings.isRandomizeForbiddenTreasure() && Settings.isHTFullRandom())
                                || Settings.isRandomizeDracuetShop()) {
                            // Get rid of 8-boss requirement on HT.
                            Integer flagToRemoveIndex = null;
                            for (int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                                if (gameObject.getTestByteOperations().get(i).getIndex() == 258) {
                                    flagToRemoveIndex = i;
                                    break;
                                }
                            }
                            if (flagToRemoveIndex != null) {
                                gameObject.getTestByteOperations().remove((int) flagToRemoveIndex);
                            }
                            return;
                        }
                    }
                }
                else if(!Settings.isRandomizeNonBossDoors()) {
                    if(flagUpdate.getIndex() == 0x15c || flagUpdate.getIndex() == 0x15d) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 0
                                ? "Door: F1" : "Door: B1";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == 0x16d || flagUpdate.getIndex() == 0x16e) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 2
                                ? "Door: F2" : "Door: B2";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == 0x175 || flagUpdate.getIndex() == 0x176) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 3
                                ? "Door: F3" : "Door: B3";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == 0x1bd || flagUpdate.getIndex() == 0x1be) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 5
                                ? "Door: F4" : "Door: B4";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == 0x152 || flagUpdate.getIndex() == 0x153) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 1
                                ? "Door: F5" : "Door: B5";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == 0x2b9 || flagUpdate.getIndex() == 0x1d0) {
                        String doorName = ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 6
                                ? "Door: F6" : "Door: B6";
                        List<GameObject> backsideDoors = mapOfDoorNameToBacksideDoor.get(doorName);
                        if(backsideDoors == null) {
                            backsideDoors = new ArrayList<>();
                            mapOfDoorNameToBacksideDoor.put(doorName, backsideDoors);
                        }
                        backsideDoors.add(gameObject);
                    }
                    else if(flagUpdate.getIndex() == 0x3b7 || flagUpdate.getIndex() == 0x1c0) {
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
                else if(Settings.isBlockPushingRequiresGlove()) {
                    if(flagUpdate.getIndex() == 0x28d) {
                        flagUpdate.setIndex(0xacc);
                    }
                }
            }
            for(int i = 0; i < gameObject.getTestByteOperations().size(); i++) {
                TestByteOperation flagTest = gameObject.getTestByteOperations().get(i);
                if(flagTest.getIndex() == 260) {
                    // Timers related to Diary puzzle
                    GameObjectId gameObjectId = new GameObjectId((short) 72, 260);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    return;
                }
                else if(flagTest.getIndex() == 742) {
                    // Timer related to MSX2 shop
                    GameObjectId gameObjectId = new GameObjectId((short) 76, 742);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    return;
                }
                else if(flagTest.getIndex() == 123 && flagTest.getValue() == 56) {
                    // Mulbruk score check timer - if you don't have the right score, the timer won't set the flag to
                    // spawn the Mulbruk conversation object that would let you get Book of the Dead.
                    flagTest.setValue((byte)0);
                }
                else if(flagTest.getIndex() == 0x2b8 && flagTest.getValue() == 0) {
                    // 8bit Fairy timer test for some conversations
                    if(Settings.isHalloweenMode()) {
                        // 8bit Fairy - conversation needs added test
                        TestByteOperation testByteOperation = new TestByteOperation();
                        testByteOperation.setIndex(getNpcConversationFlag((short)710));
                        testByteOperation.setOp(ByteOp.FLAG_GT);
                        testByteOperation.setValue((byte)0);
                        gameObject.getTestByteOperations().add(testByteOperation);
                    }
                }
                else if (flagTest.getIndex() == 299) {
                    // Timers related to MARDUK mantra. Some other timers have been removed in RcdReader.
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 8) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);

                        // Add test for Giltoriyo conversation
                        TestByteOperation testByteOperation = new TestByteOperation();
                        testByteOperation.setIndex(2795);
                        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                        testByteOperation.setValue((byte)1);

                        gameObject.getTestByteOperations().add(testByteOperation);
                        break;
                    }

                    List<GameObject> objects = mantraTablets.get("MARDUK");
                    if (objects == null) {
                        mantraTablets.put("MARDUK", new ArrayList<>());
                        objects = mantraTablets.get("MARDUK");
                    }
                    objects.add(gameObject);
                }
                else if (flagTest.getIndex() == 298) {
                    // Timers related to SABBAT mantra. Some other timers have been removed in RcdReader.
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 7) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);

                        // Add test for Giltoriyo conversation
                        TestByteOperation testByteOperation = new TestByteOperation();
                        testByteOperation.setIndex(2795);
                        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                        testByteOperation.setValue((byte)1);

                        gameObject.getTestByteOperations().add(testByteOperation);
                        break;
                    }

                    List<GameObject> objects = mantraTablets.get("SABBAT");
                    if (objects == null) {
                        mantraTablets.put("SABBAT", new ArrayList<>());
                        objects = mantraTablets.get("SABBAT");
                    }
                    objects.add(gameObject);
                }
                else if (flagTest.getIndex() == 297) {
                    // Timers related to MU mantra.
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 6) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);

                        // Add test for Giltoriyo conversation
                        TestByteOperation testByteOperation = new TestByteOperation();
                        testByteOperation.setIndex(2795);
                        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                        testByteOperation.setValue((byte)1);

                        gameObject.getTestByteOperations().add(testByteOperation);
                        break;
                    }

                    List<GameObject> objects = mantraTablets.get("MU");
                    if (objects == null) {
                        mantraTablets.put("MU", new ArrayList<>());
                        objects = mantraTablets.get("MU");
                    }
                    objects.add(gameObject);
                }
                else if (flagTest.getIndex() == 425) {
                    // Timer related to VIY mantra/statue combo.
//                    if(gameObject.getObjectContainer() instanceof Screen
//                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 5) {
//                        // Add test for Giltoriyo conversation
//                        TestByteOperation testByteOperation = new TestByteOperation();
//                        testByteOperation.setIndex(2795);
//                        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
//                        testByteOperation.setValue((byte)1);
//
//                        gameObject.getTestByteOperations().add(testByteOperation);
//                        break;
//                    }

                    // This needs to be randomized on account of the update flag.
                    List<GameObject> objects = mantraTablets.get("VIY");
                    if (objects == null) {
                        mantraTablets.put("VIY", new ArrayList<>());
                        objects = mantraTablets.get("VIY");
                    }
                    objects.add(gameObject);
                }
                else if (flagTest.getIndex() == 295) {
                    // Timers related to BAHRUN mantra.
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 13) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);

                        // Add test for Giltoriyo conversation
                        TestByteOperation testByteOperation = new TestByteOperation();
                        testByteOperation.setIndex(2795);
                        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                        testByteOperation.setValue((byte)1);

                        gameObject.getTestByteOperations().add(testByteOperation);
                        break;
                    }

                    List<GameObject> objects = mantraTablets.get("BAHRUN");
                    if (objects == null) {
                        mantraTablets.put("BAHRUN", new ArrayList<>());
                        objects = mantraTablets.get("BAHRUN");
                    }
                    objects.add(gameObject);
                }
                else if (flagTest.getIndex() == 294) {
                    // Timers related to WEDJET mantra.
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 3) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);

                        // Add test for Giltoriyo conversation
                        TestByteOperation testByteOperation = new TestByteOperation();
                        testByteOperation.setIndex(2795);
                        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                        testByteOperation.setValue((byte)1);

                        gameObject.getTestByteOperations().add(testByteOperation);
                        break;
                    }

                    List<GameObject> objects = mantraTablets.get("WEDJET");
                    if (objects == null) {
                        mantraTablets.put("WEDJET", new ArrayList<>());
                        objects = mantraTablets.get("WEDJET");
                    }
                    objects.add(gameObject);
                }
                else if (flagTest.getIndex() == 293) {
                    // Timers related to ABUTO mantra.
                    if(gameObject.getObjectContainer() instanceof Screen
                            && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 11) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);

                        // Add test for Giltoriyo conversation
                        TestByteOperation testByteOperation = new TestByteOperation();
                        testByteOperation.setIndex(2795);
                        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                        testByteOperation.setValue((byte)1);

                        gameObject.getTestByteOperations().add(testByteOperation);
                        break;
                    }

                    List<GameObject> objects = mantraTablets.get("ABUTO");
                    if (objects == null) {
                        mantraTablets.put("ABUTO", new ArrayList<>());
                        objects = mantraTablets.get("ABUTO");
                    }
                    objects.add(gameObject);
                }
                else if (flagTest.getIndex() == 292) {
                    // Timers related to LAMULANA mantra.
                    if(flagTest.getValue() == 1) {
                        if(gameObject.getObjectContainer() instanceof Screen
                                && ((Screen)gameObject.getObjectContainer()).getZoneIndex() == 0) {
                            flagTest.setOp(ByteOp.FLAG_LTEQ);

                            // Add test for Giltoriyo conversation
                            TestByteOperation testByteOperation = new TestByteOperation();
                            testByteOperation.setIndex(2795);
                            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                            testByteOperation.setValue((byte)1);

                            gameObject.getTestByteOperations().add(testByteOperation);

                            List<GameObject> objects = mantraTablets.get("LAMULANA");
                            if (objects == null) {
                                mantraTablets.put("LAMULANA", new ArrayList<>());
                                objects = mantraTablets.get("LAMULANA");
                            }
                            objects.add(gameObject);
                            break;
                        }
                    }
                    else if(flagTest.getValue() == 4) {
                        flagTest.setIndex(2794);
                        flagTest.setValue((byte)1);
                    }
                }
                else if(flagTest.getIndex() == 570) {
                    // Timer to update Cog of the Soul puzzle.
                    flagTest.setIndex(2799);
                    for(WriteByteOperation writeByteOperation : gameObject.getWriteByteOperations()) {
                        if(writeByteOperation.getIndex() == 570) {
                            writeByteOperation.setIndex(2799);
                            break;
                        }
                    }
                    break;
                }
                else if(flagTest.getIndex() == 0x28d) {
                    if(Settings.isBlockPushingRequiresGlove()) {
                        flagTest.setIndex(0xacc);
                    }
                }
//                else if(flagTest.getIndex() == 267 && flagTest.getValue() == 1) {
//                    // Timer to track wait time with Woman Statue and give Maternity Statue
//                    GameObjectId gameObjectId = new GameObjectId((short) 81, 267);
//                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
//                    if (objects == null) {
//                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
//                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
//                    }
//                    objects.add(gameObject);
//                }
            }
        } else if (gameObject.getId() == 0xbb) {
            for(TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if(flagTest.getIndex() == 260) {
                    // Diary puzzle pillar
                    GameObjectId gameObjectId = new GameObjectId((short) 72, 260);
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
        else if(gameObject.getId() == 0x0a) {
            for(WriteByteOperation flagUpdate : gameObject.getWriteByteOperations()) {
                if(flagUpdate.getIndex() == 143) {
                    // Mausoleum Ankh Jewel chest trap
                    GameObjectId gameObjectId = new GameObjectId((short) 19, 143);
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
//        else if(gameObject.getId() == 0x90) {
//            // The Boss ankh
//            if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
//                gameObject.getArgs().set(3, (short)160); // HP
//                gameObject.getArgs().set(7, (short)1); // Max number of slimes
//                gameObject.getArgs().set(12, (short)240); // Delay between bombs
//            }
//        }
        else if(gameObject.getId() == 0xc2) {
            // Mantra detectors
            short mantraNumber = gameObject.getArgs().get(0);
            if(mantraNumber > 1) {
                // Don't mess with birth/death
                if(mantraNumber == 2) {
                    // Reciting MARDUK won't un-recite SABBAT
                    Integer flagToRemoveIndex = null;
                    for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
                        if (gameObject.getWriteByteOperations().get(i).getIndex() == 298) {
                            flagToRemoveIndex = i;
                            break;
                        }
                    }
                    if(flagToRemoveIndex != null) {
                        gameObject.getWriteByteOperations().remove((int)flagToRemoveIndex);
                    }
                }
                else if(mantraNumber == 3) {
                    // Reciting SABBAT won't un-recite MU
                    Integer flagToRemoveIndex = null;
                    for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
                        if (gameObject.getWriteByteOperations().get(i).getIndex() == 297) {
                            flagToRemoveIndex = i;
                            break;
                        }
                    }
                    if(flagToRemoveIndex != null) {
                        gameObject.getWriteByteOperations().remove((int)flagToRemoveIndex);
                    }
                }
                else if(mantraNumber == 4) {
                    // Reciting MU won't un-recite VIY, and statue won't be dependent on it either.
                    Integer flagToRemoveIndex = null;
                    for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
                        if (gameObject.getWriteByteOperations().get(i).getIndex() == 296) {
                            flagToRemoveIndex = i;
                            break;
                        }
                    }
                    if(flagToRemoveIndex != null) {
                        gameObject.getWriteByteOperations().remove((int)flagToRemoveIndex);
                    }
                }
                else if(mantraNumber == 5) {
                    // Reciting VIY won't un-recite BAHRUN
                    Integer flagToRemoveIndex = null;
                    for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
                        if (gameObject.getWriteByteOperations().get(i).getIndex() == 295) {
                            flagToRemoveIndex = i;
                            break;
                        }
                    }
                    if(flagToRemoveIndex != null) {
                        gameObject.getWriteByteOperations().remove((int)flagToRemoveIndex);
                    }
                }
                else if(mantraNumber == 6) {
                    // Reciting BAHRUN won't un-recite WEDJET
                    Integer flagToRemoveIndex = null;
                    for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
                        if (gameObject.getWriteByteOperations().get(i).getIndex() == 294) {
                            flagToRemoveIndex = i;
                            break;
                        }
                    }
                    if(flagToRemoveIndex != null) {
                        gameObject.getWriteByteOperations().remove((int)flagToRemoveIndex);
                    }
                }
                else if(mantraNumber == 7) {
                    // Reciting WEDJET won't un-recite ABUTO
                    Integer flagToRemoveIndex = null;
                    for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
                        if (gameObject.getWriteByteOperations().get(i).getIndex() == 293) {
                            flagToRemoveIndex = i;
                            break;
                        }
                    }
                    if(flagToRemoveIndex != null) {
                        gameObject.getWriteByteOperations().remove((int)flagToRemoveIndex);
                    }
                }
                else if(mantraNumber == 8) {
                    // Reciting ABUTO won't un-recite LAMULANA
                    Integer flagToRemoveIndex = null;
                    for (int i = 0; i < gameObject.getWriteByteOperations().size(); i++) {
                        if (gameObject.getWriteByteOperations().get(i).getIndex() == 292) {
                            flagToRemoveIndex = i;
                            break;
                        }
                    }
                    if(flagToRemoveIndex != null) {
                        gameObject.getWriteByteOperations().remove((int)flagToRemoveIndex);
                    }
                }
                else if(mantraNumber == 9) {
                    // Reciting LAMULANA won't update the flag that upgrades the Key Sword until we're ready.
                    for (WriteByteOperation writeByteOperation : gameObject.getWriteByteOperations()) {
                        if (writeByteOperation.getIndex() == 292) {
                            writeByteOperation.setIndex(2794);
                            writeByteOperation.setValue(1);
                            break;
                        }
                    }
                }

                // Add mantra count timer
                GameObject mantraCountTimer = new GameObject(gameObject.getObjectContainer());
                mantraCountTimer.setId((short)0x0b);
                mantraCountTimer.getArgs().add((short) 0);
                mantraCountTimer.getArgs().add((short) 0);
                mantraCountTimer.setX(-1);
                mantraCountTimer.setY(-1);

                TestByteOperation testByteOperation = new TestByteOperation();
                testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                if(mantraNumber == 9) {
                    // We've swapped out this flag, so use the replaced one.
                    testByteOperation.setIndex(2794);
                    testByteOperation.setValue((byte)1);
                }
                else {
                    // First index on mantra activation object is always the mantra recited flag.
                    testByteOperation.setIndex(gameObject.getTestByteOperations().get(0).getIndex());
                    testByteOperation.setValue((byte)4);
                }
                mantraCountTimer.getTestByteOperations().add(testByteOperation);

                // Check to ensure that the timer hasn't already done its job (so each mantra recited can only increment the timer once)
                testByteOperation = new TestByteOperation();
                testByteOperation.setIndex(2792 - (299 - gameObject.getTestByteOperations().get(0).getIndex()));
                testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                testByteOperation.setValue((byte)0);
                mantraCountTimer.getTestByteOperations().add(testByteOperation);

                WriteByteOperation writeByteOperation = new WriteByteOperation();
                writeByteOperation.setIndex(2793);
                writeByteOperation.setOp(ByteOp.ADD_FLAG);
                writeByteOperation.setValue(1);
                mantraCountTimer.getWriteByteOperations().add(writeByteOperation);

                writeByteOperation = new WriteByteOperation();
                writeByteOperation.setIndex(2792 - (299 - gameObject.getTestByteOperations().get(0).getIndex()));
                writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
                writeByteOperation.setValue(1);
                mantraCountTimer.getWriteByteOperations().add(writeByteOperation);

                gameObject.getObjectContainer().getObjects().add(mantraCountTimer);

                // Add LAMULANA mantra timer
                GameObject mantraTimer = new GameObject(gameObject.getObjectContainer());
                mantraTimer.setId((short)0x0b);
                mantraTimer.getArgs().add((short) 0);
                mantraTimer.getArgs().add((short) 0);
                mantraTimer.setX(-1);
                mantraTimer.setY(-1);

                testByteOperation = new TestByteOperation();
                testByteOperation.setIndex(2793);
                testByteOperation.setOp(ByteOp.FLAG_GTEQ);
                if(Settings.getEnabledGlitches().contains("Lamp Glitch")) {
                    testByteOperation.setValue((byte)5);
                }
                else {
                    testByteOperation.setValue((byte)8);
                }
                mantraTimer.getTestByteOperations().add(testByteOperation);

                testByteOperation = new TestByteOperation();
                testByteOperation.setIndex(292);
                testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                testByteOperation.setValue((byte)4);
                mantraTimer.getTestByteOperations().add(testByteOperation);

                writeByteOperation = new WriteByteOperation();
                writeByteOperation.setIndex(292);
                writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
                writeByteOperation.setValue(4);
                mantraTimer.getWriteByteOperations().add(writeByteOperation);

                gameObject.getObjectContainer().getObjects().add(mantraTimer);
            }
        }
    }

    private static int getAnkhFlag(int zoneIndex) {
        if(zoneIndex == 0) {
            return 0x08e;
        }
        if(zoneIndex == 2) {
            return 0x08f;
        }
        if(zoneIndex == 3) {
            return 0x090;
        }
        if(zoneIndex == 4) {
            return 0x091;
        }
        if(zoneIndex == 5) {
            return 0x092;
        }
        if(zoneIndex == 6) {
            return 0x093;
        }
        if(zoneIndex == 7) {
            return 0x094;
        }
        if(zoneIndex == 17) {
            return 0x095;
        }
        return 0;
    }

    private static int getGrailFlag(int languageBlock) {
        if(Settings.isFools2021Mode()) {
            if(languageBlock == 41) {
                return LocationCoordinateMapper.getGrailFlag(0, true);
            }
            else if(languageBlock == 75) {
                return LocationCoordinateMapper.getGrailFlag(2, true);
            }
            else if(languageBlock == 104) {
                return LocationCoordinateMapper.getGrailFlag(3, true);
            }
            else if(languageBlock == 136) {
                return LocationCoordinateMapper.getGrailFlag(4, true);
            }
            else if(languageBlock == 149) {
                return LocationCoordinateMapper.getGrailFlag(5, true);
            }
            else if(languageBlock == 170) {
                return LocationCoordinateMapper.getGrailFlag(6, true);
            }
            else if(languageBlock == 188) {
                return LocationCoordinateMapper.getGrailFlag(7, true);
            }
            else if(languageBlock == 221) {
                return LocationCoordinateMapper.getGrailFlag(8, true);
            }
            else if(languageBlock == 250) {
                return LocationCoordinateMapper.getGrailFlag(10, false);
            }
            else if(languageBlock == 275) {
                return LocationCoordinateMapper.getGrailFlag(11, false);
            }
            else if(languageBlock == 291) {
                return LocationCoordinateMapper.getGrailFlag(12, false);
            }
            else if(languageBlock == 305) {
                return LocationCoordinateMapper.getGrailFlag(13, false);
            }
            else if(languageBlock == 323) {
                return LocationCoordinateMapper.getGrailFlag(14, false);
            }
            else if(languageBlock == 339) {
                return LocationCoordinateMapper.getGrailFlag(15, false); // 16 also works, the outcome is the same right now.
            }
            else if(languageBlock == 206) {
                return LocationCoordinateMapper.getGrailFlag(7, false);
            }
            else if(languageBlock == 358) {
                return LocationCoordinateMapper.getGrailFlag(17, false);
            }
            else if(languageBlock == 231) {
                return LocationCoordinateMapper.getGrailFlag(18, false);
            }
        }
        throw new RuntimeException();
    }

    private static int getOriginalGrailFlag(int languageBlock) {
        if(Settings.isFools2021Mode()) {
            if(languageBlock == 41) {
                return LocationCoordinateMapper.getOriginalGrailFlag(0, true);
            }
            else if(languageBlock == 75) {
                return LocationCoordinateMapper.getOriginalGrailFlag(2, true);
            }
            else if(languageBlock == 104) {
                return LocationCoordinateMapper.getOriginalGrailFlag(3, true);
            }
            else if(languageBlock == 136) {
                return LocationCoordinateMapper.getOriginalGrailFlag(4, true);
            }
            else if(languageBlock == 149) {
                return LocationCoordinateMapper.getOriginalGrailFlag(5, true);
            }
            else if(languageBlock == 170) {
                return LocationCoordinateMapper.getOriginalGrailFlag(6, true);
            }
            else if(languageBlock == 188) {
                return LocationCoordinateMapper.getOriginalGrailFlag(7, true);
            }
            else if(languageBlock == 221) {
                return LocationCoordinateMapper.getOriginalGrailFlag(8, true);
            }
            else if(languageBlock == 250) {
                return LocationCoordinateMapper.getOriginalGrailFlag(10, false);
            }
            else if(languageBlock == 275) {
                return LocationCoordinateMapper.getOriginalGrailFlag(11, false);
            }
            else if(languageBlock == 291) {
                return LocationCoordinateMapper.getOriginalGrailFlag(12, false);
            }
            else if(languageBlock == 305) {
                return LocationCoordinateMapper.getOriginalGrailFlag(13, false);
            }
            else if(languageBlock == 323) {
                return LocationCoordinateMapper.getOriginalGrailFlag(14, false);
            }
            else if(languageBlock == 339) {
                return LocationCoordinateMapper.getOriginalGrailFlag(15, false); // 16 also works, the outcome is the same right now.
            }
            else if(languageBlock == 206) {
                return LocationCoordinateMapper.getOriginalGrailFlag(7, false);
            }
            else if(languageBlock == 358) {
                return LocationCoordinateMapper.getOriginalGrailFlag(17, false);
            }
            else if(languageBlock == 231) {
                return LocationCoordinateMapper.getOriginalGrailFlag(18, false);
            }
        }
        throw new RuntimeException();
    }

    public static void addBlock(Block block) {
        if(block.getBlockNumber() == 37) {
            // mekuri.exe
            short inventoryArg = (short) (100);
            int worldFlag = 241;
            GameObjectId gameObjectId = new GameObjectId(inventoryArg, worldFlag);

            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == 245) {
            // Conversation to receive Pepper
            short inventoryArg = (short) (30);
            int worldFlag = 2702;
            GameObjectId gameObjectId = new GameObjectId(inventoryArg, worldFlag);

            // Add custom conversation flag so we don't have to worry about order of operations for
            Integer blockContentIndex = null;
            for(int i = 0; i < block.getBlockContents().size(); i++) {
                BlockContents blockContents = block.getBlockContents().get(i);
                if(blockContents instanceof BlockFlagData) {
                    BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                    if(blockFlagData.getWorldFlag() == 552) {
                        blockContentIndex = i;
                    }
                }
            }

            block.getBlockContents().add(blockContentIndex,
                    new BlockFlagData((short)0x0040, (short)2784, (short)1));

            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == 247) {
            // Conversation to give Treasures and receive Anchor
            short inventoryArg = (short) (50);
            int worldFlag = 2706;
            GameObjectId gameObjectId = new GameObjectId(inventoryArg, worldFlag);

            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == 249) {
            // Mini Doll
            short inventoryArg = (short) (22);
            int worldFlag = 152;
            GameObjectId gameObjectId = new GameObjectId(inventoryArg, worldFlag);

            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == 364) {
            // xmailer.exe
            short inventoryArg = (short) (86);
            int worldFlag = 227;
            GameObjectId gameObjectId = new GameObjectId(inventoryArg, worldFlag);

            updateXmailerBlock(block.getBlockContents());

            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == 369) {
            // Talisman conversation (allows Diary chest access)
            Integer blockContentIndex = null;
            for(int i = 0; i < block.getBlockContents().size(); i++) {
                BlockContents blockContents = block.getBlockContents().get(i);
                if(blockContents instanceof BlockFlagData) {
                    BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                    if(blockFlagData.getWorldFlag() == 740) {
                        blockContentIndex = i;
                    }
                }
            }

            block.getBlockContents().add(blockContentIndex,
                    new BlockFlagData((short)0x0040, (short)2796, (short)2));
            block.getBlockContents().add(blockContentIndex,
                    new BlockFlagData((short)0x0040, (short)807, (short)1));
        }
        else if(block.getBlockNumber() == 370) {
            // Diary chest conversation (allows Diary chest access)
            Integer flagIndexToRemove = null;
            for(int i = 0; i < block.getBlockContents().size(); i++) {
                BlockContents blockContents = block.getBlockContents().get(i);
                if(blockContents instanceof BlockFlagData) {
                    BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                    if(blockFlagData.getWorldFlag() == 537) {
                        flagIndexToRemove = i;
                    }
                }
            }
            // Get rid of Diary puzzle flag.
            if(flagIndexToRemove != null) {
                block.getBlockContents().remove((int)flagIndexToRemove);
            }

            Integer blockContentIndex = null;
            for(int i = 0; i < block.getBlockContents().size(); i++) {
                BlockContents blockContents = block.getBlockContents().get(i);
                if(blockContents instanceof BlockFlagData) {
                    BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                    if(blockFlagData.getWorldFlag() == 740) {
                        blockContentIndex = i;
                    }
                }
            }

            block.getBlockContents().add(blockContentIndex,
                    new BlockFlagData((short)0x0040, (short)2796, (short)3));
        }
        else if(block.getBlockNumber() == 371) {
            // Mulana Talisman
            short inventoryArg = (short) (73);
            int worldFlag = 261;
            GameObjectId gameObjectId = new GameObjectId(inventoryArg, worldFlag);

            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);

            BlockFlagData blockFlagData;

            // Handle Mulana Talisman conversation flag changes.
            Integer flagIndexOfMulanaTalisman = null;
            for(int i = 0; i < block.getBlockContents().size(); i++) {
                BlockContents blockContents = block.getBlockContents().get(i);
                if(blockContents instanceof BlockFlagData) {
                    blockFlagData = (BlockFlagData) blockContents;
                    if(blockFlagData.getWorldFlag() == 261) {
                        blockFlagData.setWorldFlag((short)2797);
                        blockFlagData.setFlagValue((short)2);
                        flagIndexOfMulanaTalisman = i;
                    }
                }
            }
            blockFlagData = new BlockFlagData((short)0x0040, (short)261, (short)2);
            block.getBlockContents().add(flagIndexOfMulanaTalisman, blockFlagData);

            // Get rid of Diary puzzle flag.
            Integer flagIndexToRemove = null;
            for(int i = 0; i < block.getBlockContents().size(); i++) {
                BlockContents blockContents = block.getBlockContents().get(i);
                if(blockContents instanceof BlockFlagData) {
                    blockFlagData = (BlockFlagData) blockContents;
                    if(blockFlagData.getWorldFlag() == 530) {
                        flagIndexToRemove = i;
                    }
                }
            }
            if(flagIndexToRemove != null) {
                block.getBlockContents().remove((int)flagIndexToRemove);
            }
        }
        else if(block.getBlockNumber() == 397) {
            // Book of the Dead
            short inventoryArg = (short) (54);
            int worldFlag = 2703;
            GameObjectId gameObjectId = new GameObjectId(inventoryArg, worldFlag);

            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);

            BlockFlagData blockFlagData;

            // Handle Book of the Dead conversation flag changes.
            Integer flagIndexOfConversationFlagUpdate = null;
            for(int i = 0; i < block.getBlockContents().size(); i++) {
                BlockContents blockContents = block.getBlockContents().get(i);
                if(blockContents instanceof BlockFlagData) {
                    blockFlagData = (BlockFlagData) blockContents;
                    if(blockFlagData.getWorldFlag() == 810) {
                        flagIndexOfConversationFlagUpdate = i;
                    }
                }
            }
            blockFlagData = new BlockFlagData((short)0x0040, (short)2703, (short)2);
            block.getBlockContents().add(flagIndexOfConversationFlagUpdate, blockFlagData);
        }
        else if(block.getBlockNumber() == 480) {
            // Xelpud flag check reference block
            CheckBlock checkBlock = (CheckBlock)block;

            Integer cmdToRemoveIndex1 = null;
            for(int i = 0; i < checkBlock.getFlagCheckReferences().size(); i++) {
                BlockListData blockListData = checkBlock.getFlagCheckReferences().get(i);
                if(blockListData.getData().get(2) == 1049) {
                    cmdToRemoveIndex1 = i;
                }
            }
            checkBlock.getFlagCheckReferences().remove((int)cmdToRemoveIndex1);

            Integer cmdToRemoveIndex2 = null;
            for(int i = 0; i < checkBlock.getFlagCheckReferences().size(); i++) {
                BlockListData blockListData = checkBlock.getFlagCheckReferences().get(i);
                if(blockListData.getData().get(2) == 371) {
                    cmdToRemoveIndex2 = i;
                }
            }
            checkBlock.getFlagCheckReferences().remove((int)cmdToRemoveIndex2);

            Integer cmdToRemoveIndex3 = null;
            for(int i = 0; i < checkBlock.getFlagCheckReferences().size(); i++) {
                BlockListData blockListData = checkBlock.getFlagCheckReferences().get(i);
                if(blockListData.getData().get(2) == 370) {
                    cmdToRemoveIndex3 = i;
                }
            }
            checkBlock.getFlagCheckReferences().remove((int)cmdToRemoveIndex3);

            // Remove xmailer conversation so we can re-build it at the front.
            Integer cmdToRemoveIndex4 = null;
            for(int i = 0; i < checkBlock.getFlagCheckReferences().size(); i++) {
                BlockListData blockListData = checkBlock.getFlagCheckReferences().get(i);
                if(blockListData.getData().get(2) == 364) {
                    cmdToRemoveIndex4 = i;
                }
            }
            checkBlock.getFlagCheckReferences().remove((int)cmdToRemoveIndex4);

            BlockListData blockListData = new BlockListData((short)78, (short)4);
            blockListData.getData().add((short)2797);
            blockListData.getData().add((short)1);
            blockListData.getData().add((short)371);
            blockListData.getData().add((short)0); // Disabling repeat for Mulana Talisman in case it's an ankh jewel or something.
            checkBlock.getFlagCheckReferences().add(0, blockListData);

            // Xelpud pillar conversation possible as soon as Talisman conversation
            blockListData = new BlockListData((short)78, (short)4);
            blockListData.getData().add((short)2796);
            blockListData.getData().add((short)2);
            blockListData.getData().add((short)370);
            blockListData.getData().add((short)0);
            checkBlock.getFlagCheckReferences().add(0, blockListData);

            // Changing Talisman conversation to use a custom flag instead of a held item check
            blockListData = new BlockListData((short)78, (short)4);
            blockListData.getData().add((short)2796);
            blockListData.getData().add((short)1);
            blockListData.getData().add((short)369);
            blockListData.getData().add((short)0);
            checkBlock.getFlagCheckReferences().add(0, blockListData);

            // Changing xmailer conversation to use a custom flag
            blockListData = new BlockListData((short)78, (short)4);
            blockListData.getData().add((short)0xad0);
            blockListData.getData().add((short)0);
            blockListData.getData().add((short)364);
            blockListData.getData().add((short)0);
            checkBlock.getFlagCheckReferences().add(0, blockListData);
        }
        else if(block.getBlockNumber() == 482) {
            // Xelpud point check reference block
            CheckBlock checkBlock = (CheckBlock)block;

            Integer cmdToRemoveIndex = null;
            for(int i = 0; i < checkBlock.getFlagCheckReferences().size(); i++) {
                BlockListData blockListData = checkBlock.getFlagCheckReferences().get(i);
                if(blockListData.getData().get(2) == 373) {
                        cmdToRemoveIndex = i;
                    }
                }
            if(cmdToRemoveIndex != null) {
                checkBlock.getFlagCheckReferences().remove((int)cmdToRemoveIndex);
            }
        }
        else if(block.getBlockNumber() == 486) {
            // Mulbruk flag check reference block
            CheckBlock checkBlock = (CheckBlock)block;

            for(BlockListData blockListData : checkBlock.getFlagCheckReferences()) {
                blockListData.getData().remove((int)blockListData.getData().size() - 1); // Remove last object
                blockListData.getData().add((short)0); // Disable conversation repeats to reduce annoyance factor
            }

            List<BlockListData> flagChecks = new ArrayList<>(checkBlock.getFlagCheckReferences().size());
            flagChecks.add(checkBlock.getFlagCheckReferences().get(3));
            flagChecks.addAll(checkBlock.getFlagCheckReferences().subList(13, checkBlock.getFlagCheckReferences().size()));
            flagChecks.addAll(checkBlock.getFlagCheckReferences().subList(0, 3));
            flagChecks.addAll(checkBlock.getFlagCheckReferences().subList(4, 13));
            checkBlock.getFlagCheckReferences().clear();
            checkBlock.getFlagCheckReferences().addAll(flagChecks);
        }
        else if(block.getBlockNumber() == 716) {
            // Surface map
            short inventoryArg = (short) (70);
            int worldFlag = 209;
            GameObjectId gameObjectId = new GameObjectId(inventoryArg, worldFlag);

            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
        }
        else if(block.getBlockNumber() == 1012) {
            // Provocative Bathing Suit
            if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
                updateSwimsuitBlock(block.getBlockContents());
            }
            else {
                short inventoryArg = (short) (74);
                int worldFlag = 262;
                GameObjectId gameObjectId = new GameObjectId(inventoryArg, worldFlag);

                List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
                if (blocks == null) {
                    mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                    blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
                }
                blocks.add(block);
            }
        }
    }

    public static void addCustomBlocks(List<Block> datBlocks) {
        if(Settings.isHalloweenMode()) {
            int noCandyBlockNum = AddObject.addNoCandyBlock(datBlocks);
            for(GameObject npcObject : npcObjects) {
                short npcBlock = npcObject.getArgs().get(4);
                if(npcBlock == 726) {
                    // Dracuet (Guidance)
                    npcObject.getArgs().set(4, (short)AddObject.addNoCandyMasterBlock(datBlocks, npcBlock, noCandyBlockNum));
                    continue;
                }
                else if(npcBlock == 991) {
                    // Dracuet (Illusion)
                    npcObject.getArgs().set(4, (short)AddObject.addNoCandyMasterBlock(datBlocks, npcBlock, noCandyBlockNum));
                    continue;
                }
                else if(npcBlock == 993) {
                    // Dracuet (Gate of Time)
                    npcObject.getArgs().set(4, (short)AddObject.addNoCandyMasterBlock(datBlocks, npcBlock, noCandyBlockNum));
                    continue;
                }
                else if(npcBlock == 1000) {
                    // Dracuet (Goddess)
                    npcObject.getArgs().set(4, (short)AddObject.addNoCandyMasterBlock(datBlocks, npcBlock, noCandyBlockNum));
                    continue;
                }

                GameObject newNpcObject = new GameObject(npcObject);

                int npcConversationFlag = getNpcConversationFlag(npcBlock);
                if(npcConversationFlag > 0) {
                    if(npcBlock == 673) {
                        // Former Mekuri Master (Surface)
                        Integer flagIndexToRemove = null;
                        for(int i = 0; i < newNpcObject.getTestByteOperations().size(); i++) {
                            TestByteOperation testByteOperation = newNpcObject.getTestByteOperations().get(i);
                            if(testByteOperation.getIndex() == 0x0f1) {
                                flagIndexToRemove = i;
                                break;
                            }
                        }
                        if(flagIndexToRemove != null) {
                            newNpcObject.getTestByteOperations().remove((int)flagIndexToRemove);
                        }
                        newNpcObject.getWriteByteOperations().clear();
                    }
                    else if(npcBlock == 689 || npcBlock == 693) {
                        // Mr. Slushfund, Priest Alest
                        Integer flagIndexToRemove = null;
                        for(int i = 0; i < newNpcObject.getTestByteOperations().size(); i++) {
                            TestByteOperation testByteOperation = newNpcObject.getTestByteOperations().get(i);
                            if(testByteOperation.getIndex() != 0x0fe) {
                                // The other flag might be shuffled, but there are only two.
                                flagIndexToRemove = i;
                                break;
                            }
                        }
                        if(flagIndexToRemove != null) {
                            newNpcObject.getTestByteOperations().remove((int)flagIndexToRemove);
                        }
                    }
                    else if(npcBlock == 704) {
                        // Fobos
                        Integer flagIndexToRemove = null;
                        for(int i = 0; i < newNpcObject.getTestByteOperations().size(); i++) {
                            TestByteOperation testByteOperation = newNpcObject.getTestByteOperations().get(i);
                            if(testByteOperation.getIndex() == 0x34f) {
                                flagIndexToRemove = i;
                                break;
                            }
                        }
                        if(flagIndexToRemove != null) {
                            newNpcObject.getTestByteOperations().remove((int)flagIndexToRemove);
                        }
                    }
                    else if(npcBlock == 710) {
                        // 8bit Fairy
                        Integer flagIndexToRemove = null;
                        for(int i = 0; i < newNpcObject.getTestByteOperations().size(); i++) {
                            TestByteOperation testByteOperation = newNpcObject.getTestByteOperations().get(i);
                            if(testByteOperation.getIndex() == 0x2b8) {
                                flagIndexToRemove = i;
                                break;
                            }
                        }
                        if(flagIndexToRemove != null) {
                            newNpcObject.getTestByteOperations().remove((int)flagIndexToRemove);
                        }

                        flagIndexToRemove = null;
                        for(int i = 0; i < newNpcObject.getTestByteOperations().size(); i++) {
                            TestByteOperation testByteOperation = newNpcObject.getTestByteOperations().get(i);
                            if(testByteOperation.getIndex() == 0x0fd) {
                                flagIndexToRemove = i;
                                break;
                            }
                        }
                        if(flagIndexToRemove != null) {
                            newNpcObject.getTestByteOperations().remove((int)flagIndexToRemove);
                        }
                    }
                    else if(npcBlock == 998) {
                        // Fairy (Night Surface)
                        Integer flagIndexToRemove = null;
                        for(int i = 0; i < newNpcObject.getTestByteOperations().size(); i++) {
                            TestByteOperation testByteOperation = newNpcObject.getTestByteOperations().get(i);
                            if(testByteOperation.getIndex() == 0x3ba) {
                                flagIndexToRemove = i;
                                break;
                            }
                        }
                        if(flagIndexToRemove != null) {
                            newNpcObject.getTestByteOperations().remove((int)flagIndexToRemove);
                        }
                        newNpcObject.getWriteByteOperations().clear();
                    }

                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(npcConversationFlag);
                    testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                    testByteOperation.setValue((byte)0);
                    newNpcObject.getTestByteOperations().add(testByteOperation);

                    testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(npcConversationFlag);
                    testByteOperation.setOp(ByteOp.FLAG_GT);
                    testByteOperation.setValue((byte)0);
                    npcObject.getTestByteOperations().add(testByteOperation);

                    newNpcObject.getArgs().set(4, (short)AddObject.addNpcBlock(datBlocks, npcObject.getArgs().get(4), npcConversationFlag));

                    newNpcObject.getWriteByteOperations().clear();

                    npcObject.getObjectContainer().getObjects().add(newNpcObject);
                }
            }
            // Update Xelpud with conversations for NPC counts
            CheckBlock checkBlock = (CheckBlock)datBlocks.get(480);
            final int totalNpcCount = Settings.isIncludeHellTempleNPCs() ? 29 : 28;
            for(int i = 28; i >= 0; i--) {
                BlockListData blockListData = new BlockListData((short)78, (short)4);
                blockListData.getData().add((short)0xaca);
                blockListData.getData().add((short)i);
                blockListData.getData().add((short)AddObject.addNpcCountBlock(datBlocks, i, totalNpcCount));
                blockListData.getData().add((short)0);
                checkBlock.getFlagCheckReferences().add(4, blockListData);
            }
            if(Settings.isIncludeHellTempleNPCs()) {
                BlockListData blockListData = new BlockListData((short)78, (short)4);
                blockListData.getData().add((short)0xaca);
                blockListData.getData().add((short)29);
                blockListData.getData().add((short)AddObject.addAllNpcsBlock(datBlocks));
                blockListData.getData().add((short)0);
                checkBlock.getFlagCheckReferences().add(4, blockListData);
            }

            // Update Mulbruk with Helloween Temple conversation
            checkBlock = (CheckBlock)datBlocks.get(486);
            List<BlockListData> flagCheckReferences = new ArrayList<>();
            for(BlockListData blockListData : checkBlock.getFlagCheckReferences()) {
                short blockNum = blockListData.getData().get(2);
                if(blockNum == 0x18d) {
                    flagCheckReferences.add(blockListData);
                }
                else if(blockNum == 0x19e || blockNum == 0x19f || blockNum == 0x1a0 || blockNum == 0x1a1
                        || blockNum == 0x188 || blockNum == 0x18c || blockNum == 0x18e || blockNum == 0x18f
                        || blockNum == 0x194 || blockNum == 0x195 || blockNum == 0x196 || blockNum == 0x190
                        || blockNum == 0x191 || blockNum == 0x193 || blockNum == 0x199) {
                    // Unnecessary conversations
                    continue;
                }
                else if(blockNum == 0x189) {
                    updateMulbrukIntroBlock(datBlocks.get(0x189).getBlockContents(), 0x189);
                    BlockListData mulbrukBlockListData = new BlockListData((short)78, (short)4);
                    mulbrukBlockListData.getData().add((short)0xaac);
                    mulbrukBlockListData.getData().add((short)0);
                    mulbrukBlockListData.getData().add((short)0x189);
                    mulbrukBlockListData.getData().add((short)1);
                    flagCheckReferences.add(0, mulbrukBlockListData);
                }
                else {
                    flagCheckReferences.add(blockListData);
                }
            }
            checkBlock.getFlagCheckReferences().clear();
            checkBlock.getFlagCheckReferences().addAll(flagCheckReferences);

            final int npcHintCount = 17;

            if(Settings.isIncludeHellTempleNPCs()) {
                BlockListData blockListData = new BlockListData((short)78, (short)4);
                blockListData.getData().add((short)0xaac);
                blockListData.getData().add((short)2);
                blockListData.getData().add((short)AddObject.addMulbrukHTBlock(datBlocks, npcHintCount + 1));
                blockListData.getData().add((short)0);
                checkBlock.getFlagCheckReferences().add(blockListData);
            }

            // Update Mulbruk with hints for NPCs
            Block randomBlock1 = datBlocks.get(0x436);
            Block randomBlock2 = datBlocks.get(0x437);
            Block randomBlock3 = datBlocks.get(0x1e9);
            randomBlock1.getBlockContents().clear();
            randomBlock2.getBlockContents().clear();
            randomBlock3.getBlockContents().clear();
            for(int i = 1; i <= npcHintCount; i++) {
                // Add to flag-based.
                short hintBlock = (short)AddObject.addNpcHintBlock(datBlocks, i, true);
                BlockListData blockListData = new BlockListData((short)78, (short)4);
                blockListData.getData().add((short)0xace);
                blockListData.getData().add((short)(i - 1));
                blockListData.getData().add(hintBlock);
                blockListData.getData().add((short)0);
                checkBlock.getFlagCheckReferences().add(blockListData);

                // Add to random
                hintBlock = (short)AddObject.addNpcHintBlock(datBlocks, i, false);
                blockListData = new BlockListData((short)78, (short)2);
                blockListData.getData().add(hintBlock);
                blockListData.getData().add((short)0);
                randomBlock1.getBlockContents().add(blockListData);
                randomBlock2.getBlockContents().add(blockListData);
                randomBlock3.getBlockContents().add(blockListData);
                randomBlock1.getBlockContents().add(new BlockSingleData((short)10));
                randomBlock2.getBlockContents().add(new BlockSingleData((short)10));
                randomBlock3.getBlockContents().add(new BlockSingleData((short)10));
            }
            short hintBlock = (short)AddObject.addDevRoomHintBlock(datBlocks, true);
            BlockListData blockListData = new BlockListData((short)78, (short)4);
            blockListData.getData().add((short)0xace);
            blockListData.getData().add((short)npcHintCount);
            blockListData.getData().add(hintBlock);
            blockListData.getData().add((short)0);
            checkBlock.getFlagCheckReferences().add(blockListData);

            hintBlock = (short)AddObject.addDevRoomHintBlock(datBlocks, false);
            blockListData = new BlockListData((short)78, (short)2);
            blockListData.getData().add(hintBlock);
            blockListData.getData().add((short)0);
            randomBlock1.getBlockContents().add(blockListData);
            randomBlock2.getBlockContents().add(blockListData);
            randomBlock3.getBlockContents().add(blockListData);

            // Update Mulbruk to not have score conversations so the random ones will take precedence
            Block pointsBlock = datBlocks.get(0x1e8);
            pointsBlock.getBlockContents().clear();
        }
    }

    private static int getNpcConversationFlag(short conversationBlockNumber) {
        if(conversationBlockNumber == 671) {
            return 0xac9;
        }
        if(conversationBlockNumber == 672) {
            return 0xac8;
        }
        if(conversationBlockNumber == 673) {
            return 0xac7;
        }
        if(conversationBlockNumber == 674) {
            return 0xac6;
        }
        if(conversationBlockNumber == 675) {
            return 0xac5;
        }
        if(conversationBlockNumber == 677) {
            return 0xac4;
        }
        if(conversationBlockNumber == 678) {
            return 0xac3;
        }
        if(conversationBlockNumber == 679) {
            return 0xac2;
        }
        if(conversationBlockNumber == 680) {
            return 0xac1;
        }
        if(conversationBlockNumber == 681) {
            return 0xac0;
        }
        if(conversationBlockNumber == 683) {
            return 0xabf;
        }
        if(conversationBlockNumber == 684) {
            return 0x1f5;
        }
        if(conversationBlockNumber == 689) {
            return 0xabe;
        }
        if(conversationBlockNumber == 693) {
            return 0xabd;
        }
        if(conversationBlockNumber == 694) {
            return 0xabc;
        }
        if(conversationBlockNumber == 696) {
            return 0xabb;
        }
        if(conversationBlockNumber == 698) {
            return 0xaba;
        }
        if(conversationBlockNumber == 700) {
            return 0xab9;
        }
        if(conversationBlockNumber == 701) {
            return 0xab8;
        }
        if(conversationBlockNumber == 702) {
            return 0xab7;
        }
        if(conversationBlockNumber == 704) {
            return 0xab6;
        }
        if(conversationBlockNumber == 706) {
            return 0xab5;
        }
        if(conversationBlockNumber == 707) {
            return 0xab4;
        }
        if(conversationBlockNumber == 708) {
            return 0xab3;
        }
        if(conversationBlockNumber == 709) {
            return 0xab2;
        }
        if(conversationBlockNumber == 710) {
            return 0xab1;
        }
        if(conversationBlockNumber == 718) {
            return 0xab0;
        }
        if(conversationBlockNumber == 723) {
            return 0xaaf;
        }
        if(conversationBlockNumber == 998) {
            return 0xaab;
        }
        return 0;
    }

    public static void updateXmailerBlock(List<BlockContents> xelpudBlockContents) {
        if(Settings.isHalloweenMode()) {
            xelpudBlockContents.clear();
            xelpudBlockContents.add(new BlockFlagData((short) 0x0040, (short) 740, (short) 1));
            xelpudBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0xaa7, (short) 1));
            List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.intro1"));
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }
            xelpudBlockContents.add(new BlockSingleData((short) 0x0044)); // {CLS}

            stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.intro2"));
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }
            xelpudBlockContents.add(new BlockItemData((short) 0x0042, (short) 86)); // xmailer, to be replaced with 74=swimsuit
            xelpudBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0x0e3, (short) 2)); // xmailer flag
            xelpudBlockContents.add(new BlockSingleData((short) 0x0044)); // {CLS}

            String text = Translations.getText("event.halloween.intro3");
            String[] texts = text.split("%s");
            if(texts.length > 0) {
                stringCharacters = FileUtils.stringToData(texts[0]);
                for (Short shortCharacter : stringCharacters) {
                    xelpudBlockContents.add(new BlockSingleData(shortCharacter));
                }
            }

            xelpudBlockContents.add(new BlockColorsData((short)0x004a, (short)0, (short)0x32, (short)0x96));
            stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.graphics"));
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }
            xelpudBlockContents.add(new BlockColorsData((short)0x004a, (short)0, (short)0, (short)0));

            stringCharacters = FileUtils.stringToData(texts[texts.length > 0 ? 1 : 0]);
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }
            xelpudBlockContents.add(new BlockSingleData((short) 0x0044)); // {CLS}

            stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.intro4"));
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }
            xelpudBlockContents.add(new BlockSingleData((short) 0x0044)); // {CLS}

            text = Translations.getText("event.halloween.intro5");
            texts = text.split("%s");
            if(texts.length > 0) {
                stringCharacters = FileUtils.stringToData(texts[0]);
                for (Short shortCharacter : stringCharacters) {
                    xelpudBlockContents.add(new BlockSingleData(shortCharacter));
                }
            }

            xelpudBlockContents.add(new BlockColorsData((short)0x004a, (short)0x96, (short)0x32, (short)0));
            stringCharacters = FileUtils.stringToData(Translations.getText("locations.TempleoftheSun"));
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }
            xelpudBlockContents.add(new BlockColorsData((short)0x004a, (short)0, (short)0, (short)0));

            stringCharacters = FileUtils.stringToData(texts[texts.length > 0 ? 1 : 0]);
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }

            xelpudBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0xad0, (short) 1)); // Talked to xelpud flag
            xelpudBlockContents.add(new BlockFlagData((short) 0x0040, (short) 740, (short) 0)); // Can-exit flag
        }
        else if(Settings.isFools2020Mode()) {
            xelpudBlockContents.clear();
            xelpudBlockContents.add(new BlockFlagData((short) 0x0040, (short) 740, (short) 1));
            xelpudBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0xaa7, (short) 1));
            List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.intro1"));
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }
            xelpudBlockContents.add(new BlockItemData((short) 0x0042, (short) 86)); // xmailer, to be replaced with 74=swimsuit
            xelpudBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0x0e3, (short) 2)); // xmailer flag
            xelpudBlockContents.add(new BlockSingleData((short) 0x0044)); // {CLS}

            String text = Translations.getText("event.fools2020.intro2");
            String[] texts = text.split("%s");
            if(texts.length > 0) {
                stringCharacters = FileUtils.stringToData(texts[0]);
                for (Short shortCharacter : stringCharacters) {
                    xelpudBlockContents.add(new BlockSingleData(shortCharacter));
                }
            }

            xelpudBlockContents.add(new BlockColorsData((short)0x004a, (short)0, (short)0x32, (short)0x96));
            stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.graphics"));
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }
            xelpudBlockContents.add(new BlockColorsData((short)0x004a, (short)0, (short)0, (short)0));

            stringCharacters = FileUtils.stringToData(texts[texts.length > 0 ? 1 : 0]);
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }

            xelpudBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0xad0, (short) 1)); // Talked to xelpud flag
            xelpudBlockContents.add(new BlockFlagData((short) 0x0040, (short) 740, (short) 0)); // Can-exit flag
        }
        else {
            // Set value of world flag to 2 instead of 1
            for(int i = 0; i < xelpudBlockContents.size(); i++) {
                BlockContents blockContents = xelpudBlockContents.get(i);
                if(blockContents instanceof BlockFlagData) {
                    BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                    if(blockFlagData.getWorldFlag() == 0x0e3) {
                        blockFlagData.setFlagValue((short)2);
                    }
                    else if(blockFlagData.getWorldFlag() == 0x07c) {
                        blockFlagData.setWorldFlag((short)0xad0);
                        blockFlagData.setFlagValue((short)1);
                    }
                }
            }
        }
    }

    public static void updateSwimsuitBlock(List<BlockContents> swimsuitBlockContents) {
        swimsuitBlockContents.clear();
        swimsuitBlockContents.add(new BlockFlagData((short) 0x0040, (short) 740, (short) 1));
        swimsuitBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0xaa7, (short) 1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.dracuet1"));
        for (Short shortCharacter : stringCharacters) {
            swimsuitBlockContents.add(new BlockSingleData(shortCharacter));
        }
        swimsuitBlockContents.add(new BlockItemData((short) 0x0042, (short) 74)); // Swimsuit
        swimsuitBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0x7ea, (short) 1)); // Solve room 27 so you can get out
        swimsuitBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0x7f8, (short) 1)); // Solve room 27 so you can get out
        swimsuitBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0x36a, (short) 1)); // Mulbruk Halloween costume
        swimsuitBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0x106, (short) 1)); // Mulbruk Halloween costume
        swimsuitBlockContents.add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.dracuet2"));
        for (Short shortCharacter : stringCharacters) {
            swimsuitBlockContents.add(new BlockSingleData(shortCharacter));
        }

        swimsuitBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0xaae, (short) 1)); // Dracuet flag
        swimsuitBlockContents.add(new BlockFlagData((short) 0x0040, (short) 740, (short) 0)); // Can-exit flag
    }

    public static void updateMulbrukIntroBlock(List<BlockContents> mulbrukBlockContents, int repeatBlock) {
        mulbrukBlockContents.clear();
        mulbrukBlockContents.add(new BlockFlagData((short) 0x0040, (short) 740, (short) 1));
        mulbrukBlockContents.add(new BlockFlagData((short) 0x0040, (short) 0xaac, (short) 1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.mulbruk1"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.mulbruk2"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.mulbruk3"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData((short) 0x0044)); // {CLS}

        String text = Translations.getText("event.halloween.mulbruk4");
        String[] texts = text.split("%s");
        if(texts.length > 0) {
            stringCharacters = FileUtils.stringToData(texts[0]);
            for (Short shortCharacter : stringCharacters) {
                mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
            }
        }

        mulbrukBlockContents.add(new BlockColorsData((short)0x004a, (short)0x96, (short)0, (short)0x64));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.halloweenCandy"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockColorsData((short)0x004a, (short)0, (short)0, (short)0));

        stringCharacters = FileUtils.stringToData(texts[texts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData((short) 0x0044)); // {CLS}
        mulbrukBlockContents.add(new BlockFlagData((short) 0x0040, (short) 740, (short) 0)); // Can-exit flag

        // Allow repeat
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.repeat"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData((short)0x000a));

        BlockListData repeatCmd = new BlockListData((short)0x004e, (short)1);
        repeatCmd.getData().add((short)repeatBlock); // Re-use the same block, since this doesn't really affect much.
        mulbrukBlockContents.add(repeatCmd);
        mulbrukBlockContents.add(new BlockSingleData((short)0x000a));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.repeat.yes"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData((short)0x000a));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.repeat.no"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData((short)0x000a));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.mulbruk5"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData((short)0x000a));
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
            testByteOperation.setIndex(0x0fe);
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
            gameObject.getArgs().add((short)8);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)1);
            gameObject.getArgs().add((short)400);
            gameObject.getArgs().add((short)180);
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
        testByteOperation.setIndex(0x0ae);
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
        writeByteOperation.setIndex(useSpecialFlag ? 0x00c : 0x029);
        writeByteOperation.setValue((byte) 1);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        gameObject.getWriteByteOperations().add(writeByteOperation);
    }

    private static void replaceMirrorCoverTimerFlags(GameObject gameObject, int mirrorCoverFlag) {
        gameObject.getTestByteOperations().clear();

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x0ae);
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
        writeByteOperation.setIndex(0x028);
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
        testByteOperation.setIndex(0x0ae);
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
                        if(flagData.getWorldFlag() == 209) {
                            // Surface map
                            flagData.setFlagValue((short)1);
                        }
                        else {
                            flagData.setFlagValue((short)2);
                        }
                        flagData.setWorldFlag((short)itemNewContentsData.getWorldFlag());
                    }
                    else if(flagData.getWorldFlag() == 552) {
                        // The flag for the Pepper/Treasures/Anchor sequence is being replaced with custom world flags,
                        // but it won't show up in the block as a thing that matches the world flag, so special case!
                        // Note: There is no case where we see a flag 552 and want to keep it.
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
                                          boolean littleBrotherShop, boolean msxShop, boolean recursive, Random random) {
        short shopItem1Flag = getFlag(shopItem1);
        short shopItem2Flag = getFlag(shopItem2);
        short shopItem3Flag = getFlag(shopItem3);

        // NOTE: only tolerates one sacred orb per shop
        if(shopItem1.contains("Sacred Orb")) {
            ShopBlock noOrbShopBlock = new ShopBlock(shopBlock, blocks.size());
            blocks.add(noOrbShopBlock);
            writeShopInventory(noOrbShopBlock, "Weights", shopItem2, shopItem3, blocks,
                    new ItemPriceCount((short)10, (short)5), itemPriceAndCount2, itemPriceAndCount3, littleBrotherShop, msxShop, true, random);

            TestByteOperation testByteOperation;
            for(GameObject shopObject : mapOfShopBlockToShopObjects.get(shopBlock.getBlockNumber())) {
                GameObject shopWithoutOrb = new GameObject(shopObject);
                shopObject.getObjectContainer().getObjects().add(shopWithoutOrb);

                testByteOperation = new TestByteOperation();
                testByteOperation.setIndex(shopItem1Flag);
                testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                testByteOperation.setValue((byte)2);
                shopWithoutOrb.getTestByteOperations().add(testByteOperation);

                shopWithoutOrb.getArgs().set(4, (short)(blocks.size() - 1));

                testByteOperation = new TestByteOperation();
                testByteOperation.setIndex(shopItem1Flag);
                testByteOperation.setOp(ByteOp.FLAG_LT);
                testByteOperation.setValue((byte)2);
                shopObject.getTestByteOperations().add(testByteOperation);
            }
        }
        else if(shopItem2.contains("Sacred Orb")) {
            ShopBlock noOrbShopBlock = new ShopBlock(shopBlock, blocks.size());
            blocks.add(noOrbShopBlock);
            writeShopInventory(noOrbShopBlock, shopItem1, "Weights", shopItem3, blocks,
                    itemPriceAndCount1, new ItemPriceCount((short)10, (short)5), itemPriceAndCount3, littleBrotherShop, msxShop, true, random);

            TestByteOperation testByteOperation;
            for(GameObject shopObject : mapOfShopBlockToShopObjects.get(shopBlock.getBlockNumber())) {
                GameObject shopWithoutOrb = new GameObject(shopObject);
                shopObject.getObjectContainer().getObjects().add(shopWithoutOrb);

                testByteOperation = new TestByteOperation();
                testByteOperation.setIndex(shopItem2Flag);
                testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                testByteOperation.setValue((byte)2);
                shopWithoutOrb.getTestByteOperations().add(testByteOperation);

                shopWithoutOrb.getArgs().set(4, (short)(blocks.size() - 1));

                testByteOperation = new TestByteOperation();
                testByteOperation.setIndex(shopItem2Flag);
                testByteOperation.setOp(ByteOp.FLAG_LT);
                testByteOperation.setValue((byte)2);
                shopObject.getTestByteOperations().add(testByteOperation);
            }
        }
        else if(shopItem3.contains("Sacred Orb")) {
            ShopBlock noOrbShopBlock = new ShopBlock(shopBlock, blocks.size());
            blocks.add(noOrbShopBlock);
            writeShopInventory(noOrbShopBlock, shopItem1, shopItem2, "Weights", blocks,
                    itemPriceAndCount1, itemPriceAndCount2, new ItemPriceCount((short)10, (short)5), littleBrotherShop, msxShop, true, random);

            TestByteOperation testByteOperation;
            for(GameObject shopObject : mapOfShopBlockToShopObjects.get(shopBlock.getBlockNumber())) {
                GameObject shopWithoutOrb = new GameObject(shopObject);
                shopObject.getObjectContainer().getObjects().add(shopWithoutOrb);

                testByteOperation = new TestByteOperation();
                testByteOperation.setIndex(shopItem3Flag);
                testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                testByteOperation.setValue((byte)2);
                shopWithoutOrb.getTestByteOperations().add(testByteOperation);

                shopWithoutOrb.getArgs().set(4, (short)(blocks.size() - 1));

                testByteOperation = new TestByteOperation();
                testByteOperation.setIndex(shopItem3Flag);
                testByteOperation.setOp(ByteOp.FLAG_LT);
                testByteOperation.setValue((byte)2);
                shopObject.getTestByteOperations().add(testByteOperation);
            }
        }

        shopBlock.getInventoryItemArgsList().getData().clear();
        shopBlock.getInventoryItemArgsList().getData().add(getInventoryItemArg(shopItem1));
        shopBlock.getInventoryItemArgsList().getData().add(getInventoryItemArg(shopItem2));
        shopBlock.getInventoryItemArgsList().getData().add(getInventoryItemArg(shopItem3));

        List<Short> newCounts  = new ArrayList<>();
        List<Short> newPrices  = new ArrayList<>();
        if(itemPriceAndCount1 == null) {
//            if(!Settings.isFoolsMode()) {
                newPrices.add(shopBlock.getInventoryPriceList().getData().get(0));
//            }
            if ("Weights".equals(shopItem1)) {
//                if(Settings.isFoolsMode()) {
//                    newCounts.add((short)(random.nextInt(10) + 1));
//                    newPrices.add((short)1);
//                }
//                else {
                    newCounts.add((short) 5);
//                }
            }
            else if (shopItem1.endsWith("Ammo")) {
                newCounts.add(shopBlock.getInventoryCountList().getData().get(0));
//                if(Settings.isFoolsMode()) {
//                    newPrices.add(shopBlock.getInventoryPriceList().getData().get(0));
//                }
            } else {
                newCounts.add((short) 1);
//                if(Settings.isFoolsMode()) {
//                    newPrices.add(shopBlock.getInventoryPriceList().getData().get(0));
//                }
            }
        }
        else {
            if(itemPriceAndCount1.getPrice() == null) {
                newPrices.add(shopBlock.getInventoryPriceList().getData().get(0));
            }
            else {
                newPrices.add(itemPriceAndCount1.getPrice());
            }

            if(itemPriceAndCount1.getCount() == null) {
                newCounts.add(shopBlock.getInventoryCountList().getData().get(0));
            }
            else {
                newCounts.add(itemPriceAndCount1.getCount());
            }
        }

        if(itemPriceAndCount2 == null) {
//            if(!Settings.isFoolsMode()) {
                newPrices.add(shopBlock.getInventoryPriceList().getData().get(1));
//            }

            if ("Weights".equals(shopItem2)) {
//                if(Settings.isFoolsMode()) {
//                    newCounts.add((short)(random.nextInt(10) + 1));
//                    newPrices.add((short)1);
//                }
//                else {
                    newCounts.add((short) 5);
//                }
            } else if (shopItem2.endsWith("Ammo")) {
                newCounts.add(shopBlock.getInventoryCountList().getData().get(1));
//                if(Settings.isFoolsMode()) {
//                    newPrices.add(shopBlock.getInventoryPriceList().getData().get(1));
//                }
            } else {
                newCounts.add((short) 1);
//                if(Settings.isFoolsMode()) {
//                    newPrices.add(shopBlock.getInventoryPriceList().getData().get(1));
//                }
            }
        }
        else {
            if(itemPriceAndCount2.getPrice() == null) {
                newPrices.add(shopBlock.getInventoryPriceList().getData().get(1));
            }
            else {
                newPrices.add(itemPriceAndCount2.getPrice());
            }

            if(itemPriceAndCount2.getCount() == null) {
                newCounts.add(shopBlock.getInventoryCountList().getData().get(1));
            }
            else {
                newCounts.add(itemPriceAndCount2.getCount());
            }
        }

        if(itemPriceAndCount3 == null) {
//            if(!Settings.isFoolsMode()) {
                newPrices.add(shopBlock.getInventoryPriceList().getData().get(2));
//            }
            if ("Weights".equals(shopItem3)) {
//                if(Settings.isFoolsMode()) {
//                    newCounts.add((short)(random.nextInt(10) + 1));
//                    newPrices.add((short)1);
//                }
//                else {
                    newCounts.add((short) 5);
//                }
            } else if (shopItem3.endsWith("Ammo")) {
                newCounts.add(shopBlock.getInventoryCountList().getData().get(2));
//                if(Settings.isFoolsMode()) {
//                    newPrices.add(shopBlock.getInventoryPriceList().getData().get(2));
//                }
            } else {
                newCounts.add((short) 1);
//                if(Settings.isFoolsMode()) {
//                    newPrices.add(shopBlock.getInventoryPriceList().getData().get(2));
//                }
            }
        }
        else {
            if(itemPriceAndCount3.getPrice() == null) {
                newPrices.add(shopBlock.getInventoryPriceList().getData().get(2));
            }
            else {
                newPrices.add(itemPriceAndCount3.getPrice());
            }

            if(itemPriceAndCount3.getCount() == null) {
                newCounts.add(shopBlock.getInventoryCountList().getData().get(2));
            }
            else {
                newCounts.add(itemPriceAndCount3.getCount());
            }
        }

        shopBlock.getInventoryPriceList().getData().clear();
        shopBlock.getInventoryPriceList().getData().addAll(newPrices);

        shopBlock.getInventoryCountList().getData().clear();
        shopBlock.getInventoryCountList().getData().addAll(newCounts);

        shopBlock.getFlagList().getData().clear();
        shopBlock.getFlagList().getData().add(shopItem1Flag);
        shopBlock.getFlagList().getData().add(shopItem2Flag);
        shopBlock.getFlagList().getData().add(shopItem3Flag);

        if(littleBrotherShop) {
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

        if(msxShop) {
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

    public static void updateXelpudIntro(List<Block> blocks) {
        Block introBlock = new Block(blocks.size());
        introBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("text.xelpudWarn"));
        for (Short shortCharacter : stringCharacters) {
            introBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        introBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 0xad1, (short)2));
        introBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 0xad0, (short)1));
        introBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 0));

        blocks.add(introBlock);

        CheckBlock checkBlock = (CheckBlock)blocks.get(480);

        // Changing xmailer conversation to use a custom flag instead of a held item check
        BlockListData blockListData = new BlockListData((short)78, (short)4);
        blockListData.getData().add((short)0xad1);
        blockListData.getData().add((short)0);
        blockListData.getData().add((short)introBlock.getBlockNumber());
        blockListData.getData().add((short)0);
        checkBlock.getFlagCheckReferences().add(0, blockListData);
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

    public static void writeTransitionGate(String gateToUpdate, String gateDestination) {
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
                    updateScreenTransition(gameObject, gateDestination);
                }
                if(firstObject && "Transition: Illusion R2".equals(gateToUpdate)) {
                    AddObject.addIllusionFruitBlockHorizontal(gameObject, true);
                    updateFirstObject = true;
                }
                if(firstObject
                        && ("Transition: Illusion R1".equals(gateDestination)
                        || "Transition: Illusion R2".equals(gateDestination))) {
                    AddObject.addIllusionFruitBlockHorizontal(gameObject, "Transition: Illusion R2".equals(gateDestination));
                    updateFirstObject = true;
                }
                if(firstObject && "Transition: Illusion D1".equals(gateDestination)) {
                    AddObject.addIllusionFruitBlockVertical(gameObject);
                    updateFirstObject = true;
                }
                if(firstObject && "Transition: Sun L1".equals(gateDestination)) {
                    AddObject.addIsisRoomCeilingTimer(gameObject.getObjectContainer());
                    updateFirstObject = true;
                }
                if(firstObject && "Transition: Goddess L2".equals(gateDestination)) {
                    AddObject.addGoddessStatueLemezaDetector(gameObject);
                    updateFirstObject = true;
                }
                if(firstObject && "Transition: Goddess D1".equals(gateDestination)) {
                    AddObject.addGoddessShieldTimer(gameObject.getObjectContainer());
                    updateFirstObject = true;
                }
                if(firstObject && "Transition: Birth R1".equals(gateDestination)) {
                    AddObject.addSkandaBlock(gameObject);
                    updateFirstObject = true;
                }
                if(firstObject && gateDestination.contains("Transition: Twin ") && !gateToUpdate.equals("Transition: Twin U2")) {
                    AddObject.addTwinLabsPoisonTimerRemoval(gameObject.getObjectContainer(), false);
                    updateFirstObject = true;
                }
                if(firstObject && !LocationCoordinateMapper.isSurfaceStart() && "Transition: Surface R1".equals(gateToUpdate)) {
                    GameObject warp = AddObject.addWarp((Screen)gameObject.getObjectContainer(), 1220, 340, 4, 7, 0, 0, 0, 20, 312);
                    replaceTransitionGateArgs(warp, gateDestination); // First update the transitions so we can make a correct copy of the gate if needed.

                    TestByteOperation warpTest = new TestByteOperation();
                    warpTest.setIndex(0x414);
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
            TestByteOperation testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(0x226);
            testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
            testByteOperation.setValue((byte)0);
            gameObject.getTestByteOperations().add(testByteOperation);
        }

        if(gateDestination.startsWith("Transition: Shrine") && !"Transition: Shrine D3".equals(gateDestination)) {
            TestByteOperation testByteOperation = gameObject.getTestByteOperations().get(0);
            if(testByteOperation.getValue() != 1) {
                // Non-escape door
                testByteOperation.setIndex(0x102);
                testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                testByteOperation.setValue((byte)9);

                // Add extra check for not during escape, since escape door is different.
                testByteOperation = new TestByteOperation();
                testByteOperation.setIndex(0x382);
                testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                testByteOperation.setValue((byte)1);
                gameObject.getTestByteOperations().add(testByteOperation);
            }
        }
        else if(gateDestination.equals("Transition: Illusion R1")
                || gateDestination.equals("Transition: Illusion R2")) {
            // Add extra check for Fruit of Eden placed.
            TestByteOperation testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(0x226);
            testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
            testByteOperation.setValue((byte)0);
            gameObject.getTestByteOperations().add(testByteOperation);
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
            if(testByteOperation.getIndex() == 0x15c || testByteOperation.getIndex() == 0x16d
                    || testByteOperation.getIndex() == 0x175 || testByteOperation.getIndex() == 0x1bd
                    || testByteOperation.getIndex() == 0x152 || testByteOperation.getIndex() == 0x2b9
                    || testByteOperation.getIndex() == 0x3b7) {
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
                AddObject.addGrailToggle(null);
            }
        }
        else {
            for(GameObject gameObject : objectsToModify) {
                replaceBacksideDoorFlags(gameObject, bossNumber, gateFlag, isDoorDisabledForEscape(doorToReplace));
                replaceBacksideDoorArgs(gameObject, doorWithCoordinateData);
                if("Door: F8".equals(doorWithCoordinateData)) {
                    AddObject.addGrailToggle(gameObject.getObjectContainer());
                }
                AddObject.addNumberlessBacksideDoorGraphic(gameObject);
                if(bossNumber != null) {
                    if(bossNumber == 9) {
                        AddObject.addKeyFairyDoorTimerAndSounds(gameObject.getObjectContainer());
                        AddObject.addBacksideDoorKeyFairyPoint(gameObject);
                        AddObject.addAnimatedDoorCover(gameObject, 0x1c9, false);
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
        List<GameObject> seals = mapOfSealNodeToSealObjects.get(sealNode);
        for (GameObject sealToUpdate : seals) {
            sealToUpdate.getArgs().set(0, sealNumber);
        }
    }

    private static Integer getBossFlag(Integer bossNumber) {
        if(bossNumber == null) {
            return null;
        }
        if(bossNumber == 1) {
            return 0x0f6;
        }
        if(bossNumber == 2) {
            return 0x0f7;
        }
        if(bossNumber == 3) {
            return 0x0f8;
        }
        if(bossNumber == 4) {
            return 0x0f9;
        }
        if(bossNumber == 5) {
            return 0x0fa;
        }
        if(bossNumber == 6) {
            return 0x0fb;
        }
        if(bossNumber == 7) {
            return 0x0fc;
        }
//        if(bossNumber == 8) {
//            return 0x0fd;
//        }
        if(bossNumber == 9) {
            return 0x1c9;
        }
        return null;
    }

    private static Integer getGateFlag(Integer bossNumber) {
        if(bossNumber == null) {
            return null;
        }
        if(bossNumber == 1) {
            return 0x15d;
        }
        if(bossNumber == 2) {
            return 0x16e;
        }
        if(bossNumber == 3) {
            return 0x176;
        }
        if(bossNumber == 4) {
            return 0x1be;
        }
        if(bossNumber == 5) {
            return 0x153;
        }
        if(bossNumber == 6) {
            return 0x1d0;
        }
        if(bossNumber == 7) {
            return 0x1c0;
        }
        return null;
    }

    private static int getMirrorCoverFlag(Integer bossNumber) {
        if(bossNumber == 1) {
            return 0x15c;
        }
        if(bossNumber == 2) {
            return 0x16d;
        }
        if(bossNumber == 3) {
            return 0x175;
        }
        if(bossNumber == 4) {
            return 0x1bd;
        }
        if(bossNumber == 5) {
            return 0x152;
        }
        if(bossNumber == 6) {
            return 0x2b9;
        }
        if(bossNumber == 7) {
            return 0x3b7;
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
        boolean isTrapItem = Settings.isRandomizeTrapItems() && (newWorldFlag == 2777 || newWorldFlag == 2779 || newWorldFlag == 2780);
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

    public static void replaceNightSurfaceWithSurface(List<Zone> rcdInfo, int danceBlockNumber, int secretShopBlockNumber) {
        Zone surface = getZone(rcdInfo, 1);
        Zone nightSurface = getZone(rcdInfo, 22);
        for(GameObject gameObject : surface.getObjects()) {
            nightSurface.getObjects().add(new GameObject(gameObject));
        }
        for(Room surfaceRoom : surface.getRooms()) {
            Room nightSurfaceRoom = getRoom(nightSurface.getRooms(), surfaceRoom.getRoomIndex());
            nightSurfaceRoom.getObjects().clear();
            for(GameObject gameObject : surfaceRoom.getObjects()) {
                nightSurfaceRoom.getObjects().add(new GameObject(gameObject));
            }
            for(Screen surfaceScreen : surfaceRoom.getScreens()) {
                Screen nightSurfaceScreen = getScreen(nightSurfaceRoom.getScreens(), surfaceScreen.getScreenIndex());
                List<GameObject> positionalObjectsToPreserve = new ArrayList<>();
                if(nightSurfaceScreen.getRoomIndex() == 7 && nightSurfaceScreen.getScreenIndex() == 0) {
                    for(GameObject gameObject : nightSurfaceScreen.getObjects()) {
                        if(gameObject.getId() == 0xa0) {
                            positionalObjectsToPreserve.add(gameObject);
                        }
                    }
                }
                nightSurfaceScreen.getObjects().clear();
                boolean isXelpudStatueScreen = surfaceScreen.getRoomIndex() == 4 && surfaceScreen.getScreenIndex() == 2;
                for(GameObject gameObject : surfaceScreen.getObjects()) {
                    if(isXelpudStatueScreen && gameObject.getId() == 0x14 && gameObject.getArgs().size() > 3) {
                        int detectorType = gameObject.getArgs().get(3);
                        if(detectorType < 0 || detectorType > 4) {
                            // Sleep detector - exclude this so we don't mess with HT unlock state
                            continue;
                        }
                    }
                    nightSurfaceScreen.getObjects().add(new GameObject(gameObject));
                }
                nightSurfaceScreen.getObjects().addAll(positionalObjectsToPreserve);
                if(nightSurfaceScreen.getRoomIndex() == 2 && nightSurfaceScreen.getScreenIndex() == 1) {
                    AddObject.addStartingItems(nightSurfaceScreen);
                    if (Settings.isAutomaticHardmode()) {
                        AddObject.addAutomaticHardmodeTimer(nightSurfaceScreen);
                    }
                    if (Settings.isAutomaticTranslations()) {
                        AddObject.addAutomaticTranslationsTimer(nightSurfaceScreen);
                    }
                }
                if(Settings.isIncludeHellTempleNPCs()) {
                    if(nightSurfaceScreen.getRoomIndex() == 7 && nightSurfaceScreen.getScreenIndex() == 0) {
                        // Fairy - 998
                        // No escape timer since this only happens for HT.
                        // Not in RcdReader because we need this to happen to Night Surface specifically.
                        AddObject.addNpcConversationTimer(nightSurfaceScreen, 0xaab);
                    }
                }
                if(nightSurfaceScreen.getRoomIndex() == 9 && nightSurfaceScreen.getScreenIndex() == 0) {
                    AddObject.addDanceDetector(nightSurfaceScreen, danceBlockNumber);
                    AddObject.addSecretShop(nightSurfaceScreen, secretShopBlockNumber);
                }

                nightSurfaceScreen.getScreenExits().clear();
                for(ScreenExit surfaceScreenExit : surfaceScreen.getScreenExits()) {
                    ScreenExit nightSurfaceScreenExit = new ScreenExit();
                    nightSurfaceScreenExit.setZoneIndex(surfaceScreenExit.getZoneIndex() == 1 ? 22 : surfaceScreenExit.getZoneIndex());
                    nightSurfaceScreenExit.setRoomIndex(surfaceScreenExit.getRoomIndex());
                    nightSurfaceScreenExit.setScreenIndex(surfaceScreenExit.getScreenIndex());
                    nightSurfaceScreen.getScreenExits().add(nightSurfaceScreenExit);
                }
            }
        }
    }

    public static void fixTransitionGates(List<Zone> rcdInfo) {
        for(Zone zone : rcdInfo) {
            for(Room room : zone.getRooms()) {
                for(Screen screen : room.getScreens()) {
                    for(GameObject gameObject : screen.getObjects()) {
                        if(gameObject.getId() == 0xc4) {
                            if(Settings.isHalloweenMode()) {
                                if(gameObject.getArgs().get(0) == 1) {
                                    gameObject.getArgs().set(0, (short)22);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void addHTSkip(List<Zone> rcdInfo, List<Block> datInfo) {
        Zone htZone = getZone(rcdInfo, 23);
        Room htRoom = getRoom(htZone.getRooms(), 0);
        Screen htScreen = getScreen(htRoom.getScreens(), 0);
        AddObject.addHTSkip(htScreen, datInfo);
        if(!Settings.getStartingItemsIncludingCustom().contains("Holy Grail")) {
            AddObject.addHTWarning(htScreen, datInfo);
        }
    }

    public static void updateWorldForFools2020(List<Zone> rcdInfo, List<Block> datInfo) {
        Zone surface = getZone(rcdInfo, 1);
        Room argusRoom = getRoom(surface.getRooms(), 0);
        Screen featherScreen = getScreen(argusRoom.getScreens(), 0);

        for(GameObject gameObject : featherScreen.getObjects()) {
            if(gameObject.getId() == 0x2c) {
                gameObject.getArgs().set(0, (short)95); // Item arg to indicate what the chest drops

                WriteByteOperation puzzleFlag = gameObject.getWriteByteOperations().get(1);
                gameObject.getWriteByteOperations().clear();

                WriteByteOperation updateFlag = new WriteByteOperation();
                updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                updateFlag.setIndex(0xacf);
                updateFlag.setValue(2);
                gameObject.getWriteByteOperations().add(updateFlag);

                gameObject.getWriteByteOperations().add(puzzleFlag);

                updateFlag = new WriteByteOperation();
                updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                updateFlag.setIndex(0xacf);
                updateFlag.setValue(1);
                gameObject.getWriteByteOperations().add(updateFlag);

                updateFlag = new WriteByteOperation();
                updateFlag.setOp(ByteOp.ASSIGN_FLAG);
                updateFlag.setIndex(0xacf);
                updateFlag.setValue(2);
                gameObject.getWriteByteOperations().add(updateFlag);
            }
        }

        Zone mulbrukZone = getZone(rcdInfo, 3);
        Room mulbrukRoom = getRoom(mulbrukZone.getRooms(), 3);
        Screen mulbrukScreen = getScreen(mulbrukRoom.getScreens(), 0);
        AddObject.addFoolsMulbrukBlocks(mulbrukScreen, datInfo);
    }

    private static Zone getZone(List<Zone> zones, int zoneIndex) {
        for(Zone zone : zones) {
            if(zone.getZoneIndex() == zoneIndex) {
                return zone;
            }
        }
        return null;
    }

    private static Room getRoom(List<Room> rooms, int roomIndex) {
        for(Room room : rooms) {
            if(room.getRoomIndex() == roomIndex) {
                return room;
            }
        }
        return null;
    }

    private static Screen getScreen(List<Screen> screens, int screenIndex) {
        for(Screen screen : screens) {
            if(screen.getScreenIndex() == screenIndex) {
                return screen;
            }
        }
        return null;
    }

    private static void updateChestContents(GameObject objectToModify, GameObjectId itemLocationData, GameObjectId itemNewContentsData,
                                            String newChestContentsItemName, int newWorldFlag, Integer itemRandomizeGraphicsFlag, boolean cursed, Random random) {
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
            updateFlag.setIndex(119); // Coin chest tracking
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
            updateFlag.setValue(newWorldFlag == 2778 ? 2 : 1);
            objectToModify.getWriteByteOperations().add(updateFlag);

            objectToModify.getWriteByteOperations().add(puzzleFlag);

            if(newWorldFlag == 2778) {
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
                updateFlag.setIndex(46);
                updateFlag.setValue(1);
                objectToModify.getWriteByteOperations().add(updateFlag);

                int xPos = objectToModify.getX();
                int yPos = objectToModify.getY();
                AddObject.addBat(objectToModify.getObjectContainer(), xPos - 20, yPos, 46);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos + 20, yPos, 46);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos - 20, 46);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos + 20, 46);
            }

            AddObject.addNoItemSoundEffect(objectToModify.getObjectContainer(), newWorldFlag, 46);
        }
        else {
            if(itemNewContentsData.getWorldFlag() == newWorldFlag) {
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
                && (newWorldFlag == 2777 || newWorldFlag == 2779 || newWorldFlag == 2780);
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
            updateFlag.setIndex(43);
            updateFlag.setOp(ByteOp.ASSIGN_FLAG);
            updateFlag.setValue(1);
            objectToModify.getWriteByteOperations().add(updateFlag);

            if(Settings.isFools2020Mode()) {
                int xPos = objectToModify.getX();
                int yPos = objectToModify.getY();
                AddObject.addBat(objectToModify.getObjectContainer(), xPos - 20, yPos, 43);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos + 20, yPos, 43);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos - 20, 43);
                AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos + 20, 43);
            }
            AddObject.addNoItemSoundEffect(objectToModify.getObjectContainer(), newWorldFlag, 43);
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
            writeByteOperation.setIndex(43);
            writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
            writeByteOperation.setValue((byte)1);

            int xPos = objectToModify.getX();
            int yPos = objectToModify.getY();
            AddObject.addBat(objectToModify.getObjectContainer(), xPos - 20, yPos, 43);
            AddObject.addBat(objectToModify.getObjectContainer(), xPos + 20, yPos, 43);
            AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos - 20, 43);
            AddObject.addBat(objectToModify.getObjectContainer(), xPos, yPos + 20, 43);

            AddObject.addNoItemSoundEffect(objectToModify.getObjectContainer(), newWorldFlag, 43);
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

        if(Settings.isFools2020Mode()) {
            if(itemLocationData.getInventoryArg() == 11) {
                objectToModify.getTestByteOperations().add(new TestByteOperation(0x1b3, ByteOp.FLAG_GTEQ, 2));
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
}
