package lmr.randomizer.update;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.BlockContents;
import lmr.randomizer.dat.BlockFlagData;
import lmr.randomizer.dat.BlockItemData;
import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.rcd.object.TestByteOperation;
import lmr.randomizer.rcd.object.WriteByteOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thezerothcat on 7/21/2017.
 */
public final class GameDataTracker {
    private static Map<GameObjectId, List<GameObject>> mapOfChestIdentifyingInfoToGameObject = new HashMap<>();
    private static Map<GameObjectId, List<Block>> mapOfChestIdentifyingInfoToBlock = new HashMap<>();

    private GameDataTracker() { }

    public static void addObject(GameObject gameObject) {
        if (gameObject.getId() == 0x2c) {
            // Chest
            short inventoryArg = (short) (gameObject.getArgs().get(0) - 11);
            int worldFlag = gameObject.getWriteByteOperations().get(0).getIndex();
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

            // todo: Talisman special case
            List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
            if (objects == null) {
                mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
            }
            objects.add(gameObject);
        } else if (gameObject.getId() == 0xb5) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == 209) {
                    // Surface map item give
                    GameObjectId gameObjectId = new GameObjectId((short) 70, 209);
                    List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    if (objects == null) {
                        mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
                        objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
                    }
                    objects.add(gameObject);
                    break;
                }
            }
        } else if (gameObject.getId() == 0x12 || gameObject.getId() == 0x0e) {
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
            }
        } else if (gameObject.getId() == 0x11) {
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
            }
        } else if (gameObject.getId() == 0x9e) {
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
            }
        } else if (gameObject.getId() == 0xa0 || gameObject.getId() == 0x93) {
            for (TestByteOperation flagTest : gameObject.getTestByteOperations()) {
                if (flagTest.getIndex() == 241) {
                    // mekuri conversation
                    GameObjectId gameObjectId = new GameObjectId((short) 100, 241);
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
        else if(block.getBlockNumber() == 397) {
            // Book of the Dead
            short inventoryArg = (short) (54);
            int worldFlag = 183;
            GameObjectId gameObjectId = new GameObjectId(inventoryArg, worldFlag);

            List<Block> blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            if (blocks == null) {
                mapOfChestIdentifyingInfoToBlock.put(gameObjectId, new ArrayList<>());
                blocks = mapOfChestIdentifyingInfoToBlock.get(gameObjectId);
            }
            blocks.add(block);
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
    }

    public static void updateBlock(String chestLocation, String chestContents) {
        Map<String, GameObjectId> nameToDataMap = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData();
        GameObjectId itemNewContentsData = nameToDataMap.get(chestContents);
        GameObjectId itemLocationData = nameToDataMap.get(chestLocation);
        List<Block> blocksToModify = mapOfChestIdentifyingInfoToBlock.get(nameToDataMap.get(chestLocation));
        for(Block block : blocksToModify) {
            for(BlockContents blockContents : block.getBlockContents()) {
                if(blockContents instanceof BlockFlagData) {
                    BlockFlagData flagData = (BlockFlagData) blockContents;
                    if(flagData.getWorldFlag() == itemLocationData.getWorldFlag()) {
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

    public static void writeLocationContents(String chestLocation, String chestContents) {
        Map<String, GameObjectId> nameToDataMap = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData();
        GameObjectId itemNewContentsData = nameToDataMap.get(chestContents);
        GameObjectId itemLocationData = nameToDataMap.get(chestLocation);
        List<GameObject> objectsToModify = mapOfChestIdentifyingInfoToGameObject.get(nameToDataMap.get(chestLocation));
        if(objectsToModify == null) {
            FileUtils.log("Unable to find chest for " + chestLocation);
            return;
        }
        for(GameObject objectToModify : objectsToModify) {
            if(objectToModify.getId() == 0x2c) {
                updateChestContents(objectToModify, itemLocationData, itemNewContentsData);
            }
            else if(objectToModify.getId() == 0xb5) {
                updateInstantItemContents(objectToModify, itemLocationData, itemNewContentsData);
            }
            else if(objectToModify.getId() == 0x2f) {
                updateFloatingItemContents(objectToModify, itemLocationData, itemNewContentsData);
            }
            else {
                updateRelatedObject(objectToModify, itemLocationData, itemNewContentsData);
            }
        }
    }

    private static void updateChestContents(GameObject objectToModify, GameObjectId itemLocationData, GameObjectId itemNewContentsData) {
        objectToModify.getArgs().set(0, (short)(itemNewContentsData.getInventoryArg() + 11));
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
}
