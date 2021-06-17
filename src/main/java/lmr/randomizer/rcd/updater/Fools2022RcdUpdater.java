package lmr.randomizer.rcd.updater;

import lmr.randomizer.update.AddObject;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.rcd.RcdFileData;
import lmr.randomizer.rcd.object.*;

public class Fools2022RcdUpdater extends RcdUpdater {
    public Fools2022RcdUpdater(RcdFileData rcdFileData, DatFileData datFileData) {
        super(rcdFileData, datFileData);
    }

    @Override
    boolean updateItemGive(GameObject itemGive) {
//        itemGive.getArgs().set(0, (short)105);
        return true;
    }

    @Override
    public void doPostShuffleUpdates() {
        for(GameObject itemGive : rcdFileData.getObjectsById(ObjectIdConstants.ItemGive)) {
            Screen screen = (Screen)itemGive.getObjectContainer();
            if(screen.getZoneIndex() == 1 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 2) {
//                itemGive.getArgs().set(0, (short)105); // gives weight
                itemGive.getArgs().set(0, (short)106);
                AddObject.addFramesTimer(screen, 1, itemGive.getTestByteOperations(), itemGive.getWriteByteOperations());
                itemGive.getWriteByteOperations().clear();
                for(int i = 0; i < 4; i++) {
                    ItemGive itemGiveExtra = AddObject.addItemGive(itemGive, 106);
                    itemGiveExtra.addTests(itemGive.getTestByteOperations());
                }
            }
        }
    }
}
