package lmr.randomizer.update;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.rcd.object.WriteByteOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thezerothcat on 7/21/2017.
 */
public final class RcdObjectTracker {
    private static Map<GameObjectId, GameObject> mapOfChestIdentifyingInfoToGameObject = new HashMap<>();

    private RcdObjectTracker() { }

    public static void addChest(GameObject obj) {
        short chestArg = obj.getArgs().get(0);
        int worldFlag = obj.getWriteByteOperations().get(0).getIndex();
        mapOfChestIdentifyingInfoToGameObject.put(new GameObjectId(chestArg, worldFlag), obj);
    }

    public static void writeChest(String chestLocation, String chestContents) {
        Map<String, GameObjectId> nameToDataMap = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData();
        GameObjectId chestNewContentsData = nameToDataMap.get(chestContents);
        GameObjectId chestLocationData = nameToDataMap.get(chestLocation);
        GameObject objectToModify = mapOfChestIdentifyingInfoToGameObject.get(nameToDataMap.get(chestLocation));
        if(objectToModify == null) {
            FileUtils.log("Unable to find chest for " + chestLocation);
            return;
        }
        objectToModify.getArgs().set(0, chestNewContentsData.getChestArg());
        for(WriteByteOperation flagUpdate : objectToModify.getWriteByteOperations()) {
            if(flagUpdate.getIndex() == chestLocationData.getWorldFlag()) {
                flagUpdate.setIndex(chestNewContentsData.getWorldFlag());
            }
        }
    }
}
