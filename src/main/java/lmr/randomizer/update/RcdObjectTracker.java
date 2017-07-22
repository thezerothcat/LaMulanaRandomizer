package lmr.randomizer.update;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.rcd.object.WriteByteOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thezerothcat on 7/21/2017.
 */
public final class RcdObjectTracker {
    private static Map<GameObjectId, List<GameObject>> mapOfChestIdentifyingInfoToGameObject = new HashMap<>();

    private RcdObjectTracker() { }

    public static void addChest(GameObject gameObject) {
        short chestArg = gameObject.getArgs().get(0);
        int worldFlag = gameObject.getWriteByteOperations().get(0).getIndex();
        GameObjectId gameObjectId = new GameObjectId(chestArg, worldFlag);

        List<GameObject> objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
        if(objects == null) {
            mapOfChestIdentifyingInfoToGameObject.put(gameObjectId, new ArrayList<>());
            objects = mapOfChestIdentifyingInfoToGameObject.get(gameObjectId);
        }
        objects.add(gameObject);
    }

    public static void writeChest(String chestLocation, String chestContents) {
        Map<String, GameObjectId> nameToDataMap = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData();
        GameObjectId chestNewContentsData = nameToDataMap.get(chestContents);
        GameObjectId chestLocationData = nameToDataMap.get(chestLocation);
        List<GameObject> objectsToModify = mapOfChestIdentifyingInfoToGameObject.get(nameToDataMap.get(chestLocation));
        if(objectsToModify == null) {
            FileUtils.log("Unable to find chest for " + chestLocation);
            return;
        }
        for(GameObject objectToModify : objectsToModify) {
            objectToModify.getArgs().set(0, chestNewContentsData.getChestArg());
            for(WriteByteOperation flagUpdate : objectToModify.getWriteByteOperations()) {
                if(flagUpdate.getIndex() == chestLocationData.getWorldFlag()) {
                    flagUpdate.setIndex(chestNewContentsData.getWorldFlag());
                }
            }
        }
    }
}
