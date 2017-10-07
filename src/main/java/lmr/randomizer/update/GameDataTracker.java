package lmr.randomizer.update;

import javafx.util.Pair;
import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.*;
import lmr.randomizer.dat.conversation.CheckBlock;
import lmr.randomizer.dat.shop.BlockStringData;
import lmr.randomizer.dat.shop.ShopBlock;
import lmr.randomizer.random.ShopItemPriceCountRandomizer;
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

    private static ObjectContainer xelpudScreen;
    private static ObjectContainer mulbrukScreen;
    private static ObjectContainer littleBrotherShopScreen;

    private GameDataTracker() { }

    public static void clearAll() {
        mapOfChestIdentifyingInfoToGameObject.clear();
        mapOfChestIdentifyingInfoToBlock.clear();
        mapOfShopBlockToShopObjects.clear();
        mantraTablets.clear();
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
                // Bomb chest, will be replaced by a coin chest anyway.
                inventoryArg = 30;
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
            }

            WriteByteOperation flagUpdate = gameObject.getWriteByteOperations().get(0);
            if(flagUpdate.getIndex() == 333) {
                // Replace world flag for Life Seal coin chest, which is a bit special
                worldFlag = 2707;
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
        } else if (gameObject.getId() == 0x2f) {
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
        } else if (gameObject.getId() == 0xb5) {
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
                else if (flagTest.getIndex() == 501) {
                    // Breakable ceiling to Isis' Pendant room
                    flagIndexToRemove = i;
                    break;
                }
                else if(flagTest.getIndex() == 296) {
                    // Breakable snake statue in Inferno Cavern spike area
                    flagTest.setIndex(2795);
                    flagTest.setValue((byte)1);
                }
            }
            if(flagIndexToRemove != null) {
                gameObject.getTestByteOperations().remove((int)flagIndexToRemove);
            }
        } else if (gameObject.getId() == 0x0e) {
            // Temple of the Sun Map chest ladder stuff
            ObjectContainer objectContainer = gameObject.getObjectContainer();
            if(objectContainer instanceof Screen) {
                Screen screen = (Screen)objectContainer;
                if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(12);
                    testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                    testByteOperation.setValue((byte)0);
                    gameObject.getTestByteOperations().add(testByteOperation);
                }
            }

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
        } else if (gameObject.getId() == 0x71 || gameObject.getId() == 0x33) {
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
            }
        } else if (gameObject.getId() == 0x9e) {
            int languageBlock = gameObject.getArgs().get(0);
            if(languageBlock == 223) {
                // Tablet for MARDUK mantra
                List<GameObject> objects = mantraTablets.get("MARDUK");
                if (objects == null) {
                    mantraTablets.put("MARDUK", new ArrayList<>());
                    objects = mantraTablets.get("MARDUK");
                }
                objects.add(gameObject);
            }
            else if(languageBlock == 200) {
                // Tablet for SABBAT mantra
                List<GameObject> objects = mantraTablets.get("SABBAT");
                if (objects == null) {
                    mantraTablets.put("SABBAT", new ArrayList<>());
                    objects = mantraTablets.get("SABBAT");
                }
                objects.add(gameObject);
            }
            else if(languageBlock == 172) {
                // Tablet for MU mantra
                List<GameObject> objects = mantraTablets.get("MU");
                if (objects == null) {
                    mantraTablets.put("MU", new ArrayList<>());
                    objects = mantraTablets.get("MU");
                }
                objects.add(gameObject);
            }
            else if(languageBlock == 153) {
                // Tablet for VIY mantra
                List<GameObject> objects = mantraTablets.get("VIY");
                if (objects == null) {
                    mantraTablets.put("VIY", new ArrayList<>());
                    objects = mantraTablets.get("VIY");
                }
                objects.add(gameObject);
            }
            else if(languageBlock == 313) {
                // Tablet for BAHRUN mantra
                List<GameObject> objects = mantraTablets.get("BAHRUN");
                if (objects == null) {
                    mantraTablets.put("BAHRUN", new ArrayList<>());
                    objects = mantraTablets.get("BAHRUN");
                }
                objects.add(gameObject);
            }
            else if(languageBlock == 115) {
                // Tablet for WEDJET mantra
                List<GameObject> objects = mantraTablets.get("WEDJET");
                if (objects == null) {
                    mantraTablets.put("WEDJET", new ArrayList<>());
                    objects = mantraTablets.get("WEDJET");
                }
                objects.add(gameObject);
            }
            else if(languageBlock == 282) {
                // Tablet for ABUTO mantra
                List<GameObject> objects = mantraTablets.get("ABUTO");
                if (objects == null) {
                    mantraTablets.put("ABUTO", new ArrayList<>());
                    objects = mantraTablets.get("ABUTO");
                }
                objects.add(gameObject);
            }
            else if(languageBlock == 72) {
                // Tablet for LAMULANA mantra
                List<GameObject> objects = mantraTablets.get("LAMULANA");
                if (objects == null) {
                    mantraTablets.put("LAMULANA", new ArrayList<>());
                    objects = mantraTablets.get("LAMULANA");
                }
                objects.add(gameObject);
            }
            else if(languageBlock == 648) {
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
                    }                }
            }
        } else if (gameObject.getId() == 0xa0) {
            int blockNumber = gameObject.getArgs().get(4);
            if(gameObject.getArgs().get(3) == 1) {
                // Any shop
                if(DataFromFile.getMapOfShopNameToShopBlock().values().contains(blockNumber)) {
                    List<GameObject> objects = mapOfShopBlockToShopObjects.get(blockNumber);
                    if (objects == null) {
                        mapOfShopBlockToShopObjects.put(blockNumber, new ArrayList<>());
                        objects = mapOfShopBlockToShopObjects.get(blockNumber);
                    }
                    objects.add(gameObject);
                }
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
            else if(blockNumber == 185) {
                // Little Brother's shop
                littleBrotherShopScreen = gameObject.getObjectContainer();
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

                addAltSurfaceShopItemTimer(gameObject.getObjectContainer());
            }
            else if(blockNumber == 693 || blockNumber == 915) {
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
            else if(blockNumber == 673) {
                // mekuri conversation
                GameObjectId gameObjectId = new GameObjectId((short) 100, 241);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == 677) {
                // Giltoriyo mantra conversation
                for (WriteByteOperation flagUpdate : gameObject.getWriteByteOperations()) {
                    if(flagUpdate.getIndex() == 299) {
                        flagUpdate.setIndex(2795);
                    }
                }
            }
            else if(blockNumber == 689 || blockNumber == 690) {
                // Conversation to receive Pepper, or conversation after receiving Pepper if you don't have Treasures
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 552) {
                        // Swap out the Pepper/Treasures/Anchor combo flag with Pepper custom world flag
                        flagTest.setIndex(2702);
                        if(flagTest.getValue() > 0 && ByteOp.FLAG_EQUALS.equals(flagTest.getOp())) {
                            flagTest.setOp(ByteOp.FLAG_GTEQ);
                        }

                        GameObjectId gameObjectId = new GameObjectId((short) 30, 2702);
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
            else if(blockNumber == 691) {
                // Conversation to give Treasures and receive Anchor
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 552) {
                        // Swap out the Pepper/Treasures/Anchor combo flag with Anchor custom world flag
                        flagTest.setIndex(2706);
                        if(flagTest.getValue() == 1 && ByteOp.FLAG_EQUALS.equals(flagTest.getOp())) {
                            flagTest.setOp(ByteOp.FLAG_LTEQ);
                        }

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
            }
            else if(blockNumber == 692) {
                // Conversation after receiving both Pepper and Anchor
                for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                    if (flagTest.getIndex() == 552) {
                        // Swap out the Pepper/Treasures/Anchor combo flag with Anchor custom world flag
                        flagTest.setIndex(2706);
                        if(flagTest.getValue() == 1 && ByteOp.FLAG_EQUALS.equals(flagTest.getOp())) {
                            flagTest.setOp(ByteOp.FLAG_LTEQ);
                        }

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
                // Add a check for having Pepper (otherwise we could get this conversation before being given Pepper).
                TestByteOperation pepperCheck = new TestByteOperation();
                pepperCheck.setIndex(2702);
                pepperCheck.setOp(ByteOp.FLAG_GTEQ);
                pepperCheck.setValue((byte)2);
                gameObject.getTestByteOperations().add(pepperCheck);

                // This conversation needs to be linked to Pepper in addition to Anchor, so both flags will be replaced based on randomization.
                GameObjectId gameObjectId = new GameObjectId((short) 30, 2702);
                List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                if (objects == null) {
                    mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                    objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                }
                objects.add(gameObject);
            }
            else if(blockNumber == 484 || blockNumber == 1019
                    || gameObject.getArgs().get(4) == 1080 || gameObject.getArgs().get(4) == 1081) {
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

                // Add Diary updated timer to Xelpud's screen. We're only doing this on one of his conversation objects
                // in order to avoid duplicates, but which conversation object chosen is completely arbitrary.
                // We're also saving a reference to Xelpud's screen in case we need to add timers for an object being
                // randomized into one of his conversations.
                if(gameObject.getArgs().get(4) == 484) {
                    addDiaryTalismanConversationTimers(gameObject.getObjectContainer());
                    xelpudScreen = gameObject.getObjectContainer();
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
                mulbrukScreen = gameObject.getObjectContainer();
            }
            else if(blockNumber == 694 || blockNumber == 695) {
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
            else if(blockNumber == 132){
                // Untransformed Gyonin fish shop
                addBackupGyoninFishShop(gameObject);
            }
        } else if (gameObject.getId() == 0x93) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
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
        else if(gameObject.getId() == 0x4e) {
            // Tai Sui, on the screen of Endless Corridor with the ladder down to Shrine of the Mother
            for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                if(testByteOperation.getIndex() == 362 && ByteOp.FLAG_EQUALS.equals(testByteOperation.getOp())) {
                    // Must pick either hardmode or non-hardmode version, or we'll be adding the door twice.
                    addBackupShrineDoor(gameObject.getObjectContainer());
                }
            }
        }
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
//        else if(gameObject.getId() == 0xc0) {
//            // Mother ankh
//            if(Settings.isAllowMantraSkip()) {
//                addMantraDetector(gameObject.getObjectContainer());
//            }
//        }
    }

    private static void addBackupGyoninFishShop(GameObject untransformedGyoninFishShop) {
        ObjectContainer objectContainer = untransformedGyoninFishShop.getObjectContainer();
        if(objectContainer instanceof Screen) {
            GameObject backupFishShop = new GameObject(untransformedGyoninFishShop.getObjectContainer());
            for (int i = 0; i < untransformedGyoninFishShop.getArgs().size(); i++) {
                backupFishShop.getArgs().add(untransformedGyoninFishShop.getArgs().get(i));
            }
            TestByteOperation testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(407);
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            testByteOperation.setValue((byte) 3);
            backupFishShop.getTestByteOperations().add(testByteOperation);

            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(254);
            testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
            testByteOperation.setValue((byte) 3);
            backupFishShop.getTestByteOperations().add(testByteOperation);

            backupFishShop.setId((short) 0xa0);
            backupFishShop.setX(180);
            backupFishShop.setY(1520);

            untransformedGyoninFishShop.getObjectContainer().getObjects().add(backupFishShop);

            GameObject backupFishNewDoorGraphic = new GameObject(untransformedGyoninFishShop.getObjectContainer());
            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(407);
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            testByteOperation.setValue((byte) 3);
            backupFishNewDoorGraphic.getTestByteOperations().add(testByteOperation);

            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(254);
            testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
            testByteOperation.setValue((byte) 3);
            backupFishNewDoorGraphic.getTestByteOperations().add(testByteOperation);

            backupFishNewDoorGraphic.getArgs().add((short)-1);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)260);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)40);
            backupFishNewDoorGraphic.getArgs().add((short)40);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)1);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)255);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);
            backupFishNewDoorGraphic.getArgs().add((short)0);

            backupFishNewDoorGraphic.setId((short) 0x93);
            backupFishNewDoorGraphic.setX(180);
            backupFishNewDoorGraphic.setY(1520);

            untransformedGyoninFishShop.getObjectContainer().getObjects().add(backupFishNewDoorGraphic);

            int untransformedGyoninFishShopBlockNumber = (int)untransformedGyoninFishShop.getArgs().get(4);

            List<GameObject> objects = mapOfShopBlockToShopObjects.get(untransformedGyoninFishShopBlockNumber);
            if (objects == null) {
                mapOfShopBlockToShopObjects.put(untransformedGyoninFishShopBlockNumber, new ArrayList<>());
                objects = mapOfShopBlockToShopObjects.get(untransformedGyoninFishShopBlockNumber);
            }
            objects.add(backupFishShop);
        }
    }

    private static void addBackupShrineDoor(ObjectContainer objectContainer) {
        // Add actual door to old Shrine of the Mother
        GameObject backupShrineDoor = new GameObject(objectContainer);
        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoor.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(500);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte) 1);
        backupShrineDoor.getTestByteOperations().add(testByteOperation);

        backupShrineDoor.setId((short) 0x98);
        backupShrineDoor.setX(2430);
        backupShrineDoor.setY(320);

        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)9);
        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)420);
        backupShrineDoor.getArgs().add((short)152);

        objectContainer.getObjects().add(backupShrineDoor);

        // Add graphics for door to old Shrine of the Mother
        GameObject backupShrineDoorGraphic = new GameObject(objectContainer);
        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoorGraphic.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(500);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte) 1);
        backupShrineDoorGraphic.getTestByteOperations().add(testByteOperation);

        backupShrineDoorGraphic.getArgs().add((short)-1);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)540);
        backupShrineDoorGraphic.getArgs().add((short)40);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)1);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)255);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);

        backupShrineDoorGraphic.setId((short) 0x93);
        backupShrineDoorGraphic.setX(2410);
        backupShrineDoorGraphic.setY(280);

        objectContainer.getObjects().add(backupShrineDoorGraphic);
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

            // Set value of world flag to 2 instead of 1
            for(int i = 0; i < block.getBlockContents().size(); i++) {
                BlockContents blockContents = block.getBlockContents().get(i);
                if(blockContents instanceof BlockFlagData) {
                    BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                    if(blockFlagData.getWorldFlag() == 227) {
                        blockFlagData.setFlagValue((short)2);
                    }
                }
            }

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

            block.getBlockContents().add(blockContentIndex + 1,
                    new BlockFlagData((short)0x0040, (short)2796, (short)2));
            block.getBlockContents().add(blockContentIndex + 1,
                    new BlockFlagData((short)0x0040, (short)807, (short)1));
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

            BlockListData blockListData = new BlockListData((short)78, (short)4);
            blockListData.getData().add((short)2797);
            blockListData.getData().add((short)1);
            blockListData.getData().add((short)371);
            blockListData.getData().add((short)0); // Disabling repeat for Mulana Talisman in case it's an ankh jewel or something.
            checkBlock.getFlagCheckReferences().add(0, blockListData);

            // Changing Talisman conversation to use a custom flag instead of a held item check
            blockListData = new BlockListData((short)78, (short)4);
            blockListData.getData().add((short)2796);
            blockListData.getData().add((short)1);
            blockListData.getData().add((short)369);
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

    public static void updateBlock(String itemLocation, String randomizedContents) {
        Map<String, GameObjectId> nameToDataMap = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData();
        GameObjectId itemNewContentsData = nameToDataMap.get(randomizedContents);
        GameObjectId itemLocationData = nameToDataMap.get(itemLocation);
        List<Block> blocksToModify = mapOfChestIdentifyingInfoToBlock.get(nameToDataMap.get(itemLocation));
        for(Block block : blocksToModify) {
            for(BlockContents blockContents : block.getBlockContents()) {
                if(blockContents instanceof BlockFlagData) {
                    BlockFlagData flagData = (BlockFlagData) blockContents;
                    if(flagData.getWorldFlag() == itemLocationData.getWorldFlag()) {
                        flagData.setWorldFlag((short)itemNewContentsData.getWorldFlag());
                        flagData.setFlagValue((short)2);
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

    public static void writeShopInventory(ShopBlock shopBlock, String shopItem1, String shopItem2, String shopItem3,
                                          List<Block> blocks, ShopItemPriceCountRandomizer priceCountRandomizer,
                                          boolean littleBrotherShop, boolean msxShop) {
        short shopItem1Flag = getFlag(shopItem1);
        short shopItem2Flag = getFlag(shopItem2);
        short shopItem3Flag = getFlag(shopItem3);

        // NOTE: only tolerates one sacred orb per shop
        if(shopItem1.contains("Sacred Orb")) {
            ShopBlock noOrbShopBlock = new ShopBlock(shopBlock, blocks.size());
            blocks.add(noOrbShopBlock);
            writeShopInventory(noOrbShopBlock, "Weights", shopItem2, shopItem3, blocks, priceCountRandomizer,
                    littleBrotherShop, msxShop);

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
            writeShopInventory(noOrbShopBlock, shopItem1, "Weights", shopItem3, blocks, priceCountRandomizer,
                    littleBrotherShop, msxShop);

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
            writeShopInventory(noOrbShopBlock, shopItem1, shopItem2, "Weights", blocks, priceCountRandomizer,
                    littleBrotherShop, msxShop);

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

        if(priceCountRandomizer == null) {
            List<Short> newCounts = new ArrayList<>();
            if("Weights".equals(shopItem1)) {
                newCounts.add((short)5);
            }
            else if(shopItem1.endsWith("Ammo")) {
                newCounts.add(shopBlock.getInventoryCountList().getData().get(0));
            }
            else {
                newCounts.add((short)1);
            }

            if("Weights".equals(shopItem2)) {
                newCounts.add((short)5);
            }
            else if(shopItem2.endsWith("Ammo")) {
                newCounts.add(shopBlock.getInventoryCountList().getData().get(1));
            }
            else {
                newCounts.add((short)1);
            }

            if("Weights".equals(shopItem3)) {
                newCounts.add((short)5);
            }
            else if(shopItem3.endsWith("Ammo")) {
                newCounts.add(shopBlock.getInventoryCountList().getData().get(2));
            }
            else {
                newCounts.add((short)1);
            }
            shopBlock.getInventoryCountList().getData().clear();
            shopBlock.getInventoryCountList().getData().addAll(newCounts);

//            shopBlock.getInventoryPriceList().getData().clear();
//            shopBlock.getInventoryPriceList().getData().add((short)1);
//            shopBlock.getInventoryPriceList().getData().add((short)1);
//            shopBlock.getInventoryPriceList().getData().add((short)1);
        }
        else {
            shopBlock.getInventoryPriceList().getData().clear();
            shopBlock.getInventoryCountList().getData().clear();

            Pair<Short, Short> itemPriceAndCount = priceCountRandomizer.getItemPriceAndCount(shopItem1);
            shopBlock.getInventoryPriceList().getData().add(itemPriceAndCount.getKey());
            shopBlock.getInventoryCountList().getData().add(itemPriceAndCount.getValue());

            itemPriceAndCount = priceCountRandomizer.getItemPriceAndCount(shopItem2);
            shopBlock.getInventoryPriceList().getData().add(itemPriceAndCount.getKey());
            shopBlock.getInventoryCountList().getData().add(itemPriceAndCount.getValue());

            itemPriceAndCount = priceCountRandomizer.getItemPriceAndCount(shopItem3);
            shopBlock.getInventoryPriceList().getData().add(itemPriceAndCount.getKey());
            shopBlock.getInventoryCountList().getData().add(itemPriceAndCount.getValue());

//            shopBlock.getInventoryPriceList().getData().clear();
//            shopBlock.getInventoryPriceList().getData().add((short)1);
//            shopBlock.getInventoryPriceList().getData().add((short)1);
//            shopBlock.getInventoryPriceList().getData().add((short)1);
        }

        shopBlock.getFlagList().getData().clear();
        shopBlock.getFlagList().getData().add(shopItem1Flag);
        shopBlock.getFlagList().getData().add(shopItem2Flag);
        shopBlock.getFlagList().getData().add(shopItem3Flag);

        if(littleBrotherShop) {
            // Little Brother's shop
            if(!"Weights".equals(shopItem1) || !shopItem1.endsWith("Ammo")) {
                addLittleBrotherShopTimer(shopItem1Flag);
            }
            if(!"Weights".equals(shopItem2) || !shopItem2.endsWith("Ammo")) {
                addLittleBrotherShopTimer(shopItem2Flag);
            }
            if(!"Weights".equals(shopItem3) || !shopItem3.endsWith("Ammo")) {
                addLittleBrotherShopTimer(shopItem3Flag);
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

        updateAskItemName(shopBlock.getString(3), shopItem1);
        updateAskItemName(shopBlock.getString(4), shopItem2);
        updateAskItemName(shopBlock.getString(5), shopItem3);

        List<Short> bunemonData = shopBlock.getBunemonText().getData();
        bunemonData.clear();
        updateBunemonText(bunemonData, shopItem1);
        bunemonData.add((short)32);
        bunemonData.add((short)262);
        bunemonData.add((short)32);
        updateBunemonText(bunemonData, shopItem2);
        bunemonData.add((short)32);
        bunemonData.add((short)262);
        bunemonData.add((short)32);
        updateBunemonText(bunemonData, shopItem3);
    }

    private static void addLittleBrotherShopTimer(short shopItemFlag) {
        // Sets item world flags from 1 to 2, since one of the shop flags is taken up by checking the Big Brother shop trigger.
        GameObject littleBrotherShopItemTimer = new GameObject(littleBrotherShopScreen);
        littleBrotherShopItemTimer.setId((short) 0x0b);
        littleBrotherShopItemTimer.getArgs().add((short) 0);
        littleBrotherShopItemTimer.getArgs().add((short) 0);
        littleBrotherShopItemTimer.setX(-1);
        littleBrotherShopItemTimer.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(shopItemFlag);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)1);
        littleBrotherShopItemTimer.getTestByteOperations().add(testFlag);

        WriteByteOperation updateFlag = new WriteByteOperation();
        updateFlag.setIndex(shopItemFlag);
        updateFlag.setValue((byte) 2);
        updateFlag.setOp(ByteOp.ASSIGN_FLAG);
        littleBrotherShopItemTimer.getWriteByteOperations().add(updateFlag);

        littleBrotherShopScreen.getObjects().add(0, littleBrotherShopItemTimer);
    }

    private static void updateAskItemName(BlockStringData blockStringData, String shopItem) {
        if(blockStringData.getItemNameStartIndex() == null || blockStringData.getItemNameEndIndex() == null) {
            return;
        }

        List<Short> newBlockData = new ArrayList<>(blockStringData.getData().subList(0, blockStringData.getItemNameStartIndex()));
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
        newBlockData.addAll(blockStringData.getData().subList(blockStringData.getItemNameEndIndex(), blockStringData.getData().size()));
        blockStringData.getData().clear();
        blockStringData.getData().addAll(newBlockData);
    }

    private static void updateBunemonText(List<Short> bunemonData, String shopItem) {
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
        bunemonData.add((short)77);
        bunemonData.add(getInventoryItemArg(shopItem));
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
        return DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(item).getInventoryArg();
    }

    private static short getFlag(String item) {
        if("Weights".equals(item)) {
            return 0;
        }
        if(item.endsWith("Ammo")) {
            return 0;
        }
        return (short)DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(item).getWorldFlag();
    }

    public static void writeLocationContents(String chestLocation, String chestContents) {
        Map<String, GameObjectId> nameToDataMap = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData();
        GameObjectId itemNewContentsData = nameToDataMap.get(chestContents);
        GameObjectId itemLocationData = nameToDataMap.get(chestLocation);
        List<GameObject> objectsToModify = mapOfChestIdentifyingInfoToGameObject.get(nameToDataMap.get(chestLocation));
        if(objectsToModify == null) {
            if("xmailer.exe".equals(chestLocation) || "Mulana Talisman".equals(chestLocation)) {
                // Xelpud location, but no object with flags to update.
                if("Map (Shrine of the Mother)".equals(chestContents)) {
                    addShrineMapSoundEffect(xelpudScreen);
                }
            }
            else if("Book of the Dead".equals(chestLocation)) {
                // Mulbruk location, but no object with flags to update.
                addShrineMapSoundEffect(mulbrukScreen);
            }
            else {
                FileUtils.log("Unable to find objects related to " + chestLocation);
            }
            return;
        }
        for(GameObject objectToModify : objectsToModify) {
            if(objectToModify.getId() == 0x2c) {
                updateChestContents(objectToModify, itemLocationData, itemNewContentsData, !chestContents.startsWith("Coin:"));
                if("Map (Shrine of the Mother)".equals(chestContents)) {
                    addShrineMapSoundEffect(objectToModify.getObjectContainer());
                }
            }
            else if(objectToModify.getId() == 0xb5) {
                updateInstantItemContents(objectToModify, itemLocationData, itemNewContentsData);
                if("Map (Shrine of the Mother)".equals(chestContents)) {
                    addShrineMapSoundEffect(objectToModify.getObjectContainer());
                }
            }
            else if(objectToModify.getId() == 0x2f) {
                updateFloatingItemContents(objectToModify, itemLocationData, itemNewContentsData);
                if("Map (Shrine of the Mother)".equals(chestContents)) {
                    addShrineMapSoundEffect(objectToModify.getObjectContainer());
                }
            }
            else if(objectToModify.getId() == 0xc3) {
                updateSnapshotsItemContents(objectToModify, itemLocationData, itemNewContentsData);
                if("Map (Shrine of the Mother)".equals(chestContents)) {
                    addShrineMapSoundEffect(objectToModify.getObjectContainer());
                }
            }
            else if(objectToModify.getId() == 0xa0) {
                updateRelatedObject(objectToModify, itemLocationData, itemNewContentsData);
                short blockRef = objectToModify.getArgs().get(4);
                if(blockRef == 673 || blockRef == 689 || blockRef == 691 || blockRef == 693) {
                    // 673 = mekuri.exe
                    // 689 = Pepper
                    // 691 = Anchor
                    // 693 = Mini Doll
                    if ("Map (Shrine of the Mother)".equals(chestContents)) {
                        addShrineMapSoundEffect(objectToModify.getObjectContainer());
                    }
                }
            }
            else {
                updateRelatedObject(objectToModify, itemLocationData, itemNewContentsData);
            }
        }
    }

    public static void addAutomaticHardmode() {
        GameObject automaticHardmodeTimer = new GameObject(xelpudScreen);
        automaticHardmodeTimer.setId((short) 0x0b);
        automaticHardmodeTimer.getArgs().add((short) 0);
        automaticHardmodeTimer.getArgs().add((short) 0);
        automaticHardmodeTimer.setX(-1);
        automaticHardmodeTimer.setY(-1);

        TestByteOperation automaticHardModeTimerFlagTest = new TestByteOperation();
        automaticHardModeTimerFlagTest.setIndex(362);
        automaticHardModeTimerFlagTest.setValue((byte) 2);
        automaticHardModeTimerFlagTest.setOp(ByteOp.FLAG_NOT_EQUAL);
        automaticHardmodeTimer.getTestByteOperations().add(automaticHardModeTimerFlagTest);

        WriteByteOperation automaticHardModeTimerFlagUpdate = new WriteByteOperation();
        automaticHardModeTimerFlagUpdate.setIndex(362);
        automaticHardModeTimerFlagUpdate.setValue((byte) 2);
        automaticHardModeTimerFlagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        automaticHardmodeTimer.getWriteByteOperations().add(automaticHardModeTimerFlagUpdate);

        xelpudScreen.getObjects().add(0, automaticHardmodeTimer);
    }

    private static void addShrineMapSoundEffect(ObjectContainer objectContainer) {
        GameObject shrineMapSoundEffect = new GameObject(objectContainer);
        shrineMapSoundEffect.setId((short)0x9b);
        shrineMapSoundEffect.getArgs().add((short)30);
        shrineMapSoundEffect.getArgs().add((short)120);
        shrineMapSoundEffect.getArgs().add((short)64);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.getArgs().add((short)120);
        shrineMapSoundEffect.getArgs().add((short)64);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.getArgs().add((short)25);
        shrineMapSoundEffect.getArgs().add((short)1);
        shrineMapSoundEffect.getArgs().add((short)5);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.getArgs().add((short)10);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.setX(-1);
        shrineMapSoundEffect.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(218);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)2);
        shrineMapSoundEffect.getTestByteOperations().add(testFlag);

        testFlag = new TestByteOperation();
        testFlag.setIndex(42);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)1);
        shrineMapSoundEffect.getTestByteOperations().add(testFlag);

        GameObject shrineMapSoundEffectRemovalTimer = new GameObject(objectContainer);
        shrineMapSoundEffectRemovalTimer.setId((short) 0x0b);
        shrineMapSoundEffectRemovalTimer.getArgs().add((short) 0);
        shrineMapSoundEffectRemovalTimer.getArgs().add((short) 0);
        shrineMapSoundEffectRemovalTimer.setX(-1);
        shrineMapSoundEffectRemovalTimer.setY(-1);

        TestByteOperation shrineMapSoundEffectRemovalTimerFlagTest = new TestByteOperation();
        shrineMapSoundEffectRemovalTimerFlagTest.setIndex(2798);
        shrineMapSoundEffectRemovalTimerFlagTest.setValue((byte) 0);
        shrineMapSoundEffectRemovalTimerFlagTest.setOp(ByteOp.FLAG_EQUALS);
        shrineMapSoundEffectRemovalTimer.getTestByteOperations().add(shrineMapSoundEffectRemovalTimerFlagTest);

        shrineMapSoundEffectRemovalTimerFlagTest = new TestByteOperation();
        shrineMapSoundEffectRemovalTimerFlagTest.setIndex(218);
        shrineMapSoundEffectRemovalTimerFlagTest.setValue((byte) 2);
        shrineMapSoundEffectRemovalTimerFlagTest.setOp(ByteOp.FLAG_EQUALS);
        shrineMapSoundEffectRemovalTimer.getTestByteOperations().add(shrineMapSoundEffectRemovalTimerFlagTest);

        WriteByteOperation shrineMapSoundEffectRemovalTimerFlagUpdate = new WriteByteOperation();
        shrineMapSoundEffectRemovalTimerFlagUpdate.setIndex(2798);
        shrineMapSoundEffectRemovalTimerFlagUpdate.setValue((byte) 1);
        shrineMapSoundEffectRemovalTimerFlagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        shrineMapSoundEffectRemovalTimer.getWriteByteOperations().add(shrineMapSoundEffectRemovalTimerFlagUpdate);

        shrineMapSoundEffectRemovalTimerFlagUpdate = new WriteByteOperation();
        shrineMapSoundEffectRemovalTimerFlagUpdate.setIndex(42);
        shrineMapSoundEffectRemovalTimerFlagUpdate.setValue((byte) 1);
        shrineMapSoundEffectRemovalTimerFlagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        shrineMapSoundEffectRemovalTimer.getWriteByteOperations().add(shrineMapSoundEffectRemovalTimerFlagUpdate);

        objectContainer.getObjects().add(0, shrineMapSoundEffect);
        objectContainer.getObjects().add(0, shrineMapSoundEffectRemovalTimer);
    }

    private static void addDiaryTalismanConversationTimers(ObjectContainer objectContainer) {
        // Timer to trigger Xelpud Diary conversation (gives Mulana Talisman) if you enter his screen with the Diary.
        GameObject diaryFlagTimer = new GameObject(objectContainer);
        diaryFlagTimer.setId((short) 0x0b);
        diaryFlagTimer.getArgs().add((short) 0);
        diaryFlagTimer.getArgs().add((short) 0);
        diaryFlagTimer.setX(-1);
        diaryFlagTimer.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(260);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)2);
        diaryFlagTimer.getTestByteOperations().add(testFlag);

        testFlag = new TestByteOperation();
        testFlag.setIndex(2797);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)0);
        diaryFlagTimer.getTestByteOperations().add(testFlag);

        WriteByteOperation updateFlag = new WriteByteOperation();
        updateFlag.setIndex(2797);
        updateFlag.setValue((byte) 1);
        updateFlag.setOp(ByteOp.ASSIGN_FLAG);
        diaryFlagTimer.getWriteByteOperations().add(updateFlag);

        objectContainer.getObjects().add(0, diaryFlagTimer);

        // Timer to trigger Xelpud Talisman conversation (allows Diary chest access) if you enter his screen with the Talisman.
        GameObject xelpudTalismanConversationTimer = new GameObject(objectContainer);
        xelpudTalismanConversationTimer.setId((short) 0x0b);
        xelpudTalismanConversationTimer.getArgs().add((short) 0);
        xelpudTalismanConversationTimer.getArgs().add((short) 0);
        xelpudTalismanConversationTimer.setX(-1);
        xelpudTalismanConversationTimer.setY(-1);

        testFlag = new TestByteOperation();
        testFlag.setIndex(164);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)2);
        xelpudTalismanConversationTimer.getTestByteOperations().add(testFlag);

        testFlag = new TestByteOperation();
        testFlag.setIndex(2796);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)0);
        xelpudTalismanConversationTimer.getTestByteOperations().add(testFlag);

        updateFlag = new WriteByteOperation();
        updateFlag.setIndex(2796);
        updateFlag.setValue((byte) 1);
        updateFlag.setOp(ByteOp.ASSIGN_FLAG);
        xelpudTalismanConversationTimer.getWriteByteOperations().add(updateFlag);

        objectContainer.getObjects().add(0, xelpudTalismanConversationTimer);
    }

    private static void addAltSurfaceShopItemTimer(ObjectContainer objectContainer) {
        // Handles the case where the shop item can be obtained somewhere else and you already have it.
        // Without this timer, the shop could potentially be unable to transform back to its original state.
        GameObject altSurfaceShopTimer = new GameObject(objectContainer);
        altSurfaceShopTimer.setId((short) 0x0b);
        altSurfaceShopTimer.getArgs().add((short) 0);
        altSurfaceShopTimer.getArgs().add((short) 0);
        altSurfaceShopTimer.setX(-1);
        altSurfaceShopTimer.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(742);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)1);
        altSurfaceShopTimer.getTestByteOperations().add(testFlag);

        WriteByteOperation updateFlag = new WriteByteOperation();
        updateFlag.setIndex(742);
        updateFlag.setValue((byte) 2);
        updateFlag.setOp(ByteOp.ASSIGN_FLAG);
        altSurfaceShopTimer.getWriteByteOperations().add(updateFlag);

        objectContainer.getObjects().add(0, altSurfaceShopTimer);

        GameObjectId gameObjectId = new GameObjectId((short) 76, 742);
        List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
        if (objects == null) {
            mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
            objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
        }
        objects.add(altSurfaceShopTimer);
    }

    private static void updateChestContents(GameObject objectToModify, GameObjectId itemLocationData, GameObjectId itemNewContentsData, boolean itemChest) {
        WriteByteOperation puzzleFlag = objectToModify.getWriteByteOperations().get(1);
        objectToModify.getWriteByteOperations().clear();

        if(itemChest) {
//            if(itemNewContentsData.getInventoryArg() == 2) {
//                objectToModify.getArgs().set(0, (short)(itemNewContentsData.getInventoryArg() + 11)); // Item arg to indicate what the chest drops
//                objectToModify.getArgs().set(1, (short)1); // Real item, not fake
//            }
//            if(itemNewContentsData.getInventoryArg() == 70) {
//                objectToModify.getArgs().set(0, (short)2); // Weights
//                objectToModify.getArgs().set(1, (short)1);
//            }
//            else {
                objectToModify.getArgs().set(0, (short)(itemNewContentsData.getInventoryArg() + 11)); // Item arg to indicate what the chest drops
                objectToModify.getArgs().set(1, (short)1); // Real item, not fake
//            }
            objectToModify.getArgs().set(2, (short)1); // Blue chest
            for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
                if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
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
            updateFlag.setValue(1);
            objectToModify.getWriteByteOperations().add(updateFlag);

            updateFlag = new WriteByteOperation();
            updateFlag.setOp(ByteOp.ASSIGN_FLAG);
            updateFlag.setIndex(itemNewContentsData.getWorldFlag());
            updateFlag.setValue(2);
            objectToModify.getWriteByteOperations().add(updateFlag);
        }
        else {
            objectToModify.getArgs().set(0, (short)1); // Coins
            objectToModify.getArgs().set(1, itemNewContentsData.getInventoryArg()); // Re-purposing inventory arg to track coin amount
            objectToModify.getArgs().set(2, (short)0); // Brown chest
            for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
                if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
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
            updateFlag.setIndex(itemNewContentsData.getWorldFlag());
            updateFlag.setValue(1);
            objectToModify.getWriteByteOperations().add(updateFlag);
        }
    }

    private static void updateInstantItemContents(GameObject objectToModify, GameObjectId itemLocationData, GameObjectId itemNewContentsData) {
        objectToModify.getArgs().set(0, (short)(itemNewContentsData.getInventoryArg()));
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

    private static void updateFloatingItemContents(GameObject objectToModify, GameObjectId itemLocationData, GameObjectId itemNewContentsData) {
        objectToModify.getArgs().set(1, itemNewContentsData.getInventoryArg());
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

    private static void updateRelatedObject(GameObject objectToModify, GameObjectId itemLocationData, GameObjectId itemNewContentsData) {
        for(TestByteOperation flagTest : objectToModify.getTestByteOperations()) {
            if(flagTest.getIndex() == itemLocationData.getWorldFlag()) {
                flagTest.setIndex(itemNewContentsData.getWorldFlag());
            }
        }
        for(WriteByteOperation flagUpdate : objectToModify.getWriteByteOperations()) {
            if(flagUpdate.getIndex() == itemLocationData.getWorldFlag()) {
                flagUpdate.setIndex(itemNewContentsData.getWorldFlag());
            }
        }
    }

    public static void randomizeMantras(Random random) {
        List<String> mantrasToAssign = new ArrayList<>(Arrays.asList("LAMULANA", "ABUTO", "WEDJET", "BAHRUN", "VIY", "MU", "SABBAT", "MARDUK"));
        List<String> mantrasToReplace = new ArrayList<>(mantrasToAssign);

        int index;
        GameObjectId mantraInfo;
        String mantraToReplace;
        while(!mantrasToAssign.isEmpty()) {
            index = random.nextInt(mantrasToAssign.size());
            mantraInfo = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(mantrasToAssign.get(index));
            mantrasToAssign.remove(index);

            index = random.nextInt(mantrasToReplace.size());
            mantraToReplace = mantrasToReplace.get(index);

            for(GameObject objectToReplace : mantraTablets.get(mantraToReplace)) {
                if(objectToReplace.getId() == 0x9e) {
                    // Replace mantra tablet block
                    objectToReplace.getArgs().set(0, mantraInfo.getInventoryArg());
                }
                updateRelatedObject(objectToReplace, DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(mantraToReplace), mantraInfo);
            }
        }
    }
}
