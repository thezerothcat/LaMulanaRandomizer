package lmr.randomizer.update;

import lmr.randomizer.randomization.*;
import lmr.randomizer.rcd.RcdFileData;
import lmr.randomizer.util.FlagManager;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.*;
import lmr.randomizer.dat.update.*;
import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.rcd.object.Room;
import lmr.randomizer.rcd.object.Screen;
import lmr.randomizer.rcd.object.Zone;
import lmr.randomizer.rcd.updater.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUpdater {
    private RcdFileData rcdFileData;
    private DatFileData datFileData;
    private FlagManager flagManager;

    private List<RcdUpdater> rcdUpdaters = new ArrayList<>();
    private List<DatUpdater> datUpdaters = new ArrayList<>();

    public GameUpdater(RcdFileData rcdFileData, DatFileData datFileData, FlagManager flagManager) {
        this.rcdFileData = rcdFileData;
        this.datFileData = datFileData;
        this.flagManager = flagManager;
    }

    public void updateDat(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer) {
        datUpdaters.add(new BaseDatUpdater(datFileData));
//        if(Settings.isFools2019Mode()) {
//            updaters.add(new Fools2019DatUpdater(datFileData));
//        }
        if(Settings.isFools2020Mode()) {
            datUpdaters.add(new Fools2020DatUpdater(datFileData));
        }
        if(Settings.isFools2021Mode()) {
            datUpdaters.add(new Fools2021DatUpdater(datFileData));
        }
        if(Settings.isFools2022Mode()) {
            datUpdaters.add(new Fools2022DatUpdater(datFileData));
        }
        if(Settings.isHalloweenMode()) {
            datUpdaters.add(new HalloweenDatUpdater(datFileData));
        }
        datUpdaters.add(new RandomizationDatUpdater(datFileData, itemRandomizer, shopRandomizer, flagManager));

        for(DatUpdater datUpdater : datUpdaters) {
            datUpdater.addCustomBlocks(datFileData);
        }
        for(DatUpdater datUpdater : datUpdaters) {
            datUpdater.updateBlocks();
        }
    }

    public void updateRcd(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer, NpcRandomizer npcRandomizer,
                          SealRandomizer sealRandomizer, TransitionGateRandomizer transitionGateRandomizer,
                          BacksideDoorRandomizer backsideDoorRandomizer, Random random) {
        rcdUpdaters.add(new BaseRcdUpdater(rcdFileData, datFileData));
        if(Settings.isFools2019Mode()) {
            rcdUpdaters.add(new Fools2019RcdUpdater(rcdFileData, datFileData));
        }
        if(Settings.isFools2020Mode()) {
            rcdUpdaters.add(new Fools2020RcdUpdater(rcdFileData, datFileData));
        }
        if(Settings.isFools2021Mode()) {
            rcdUpdaters.add(new Fools2021RcdUpdater(rcdFileData, datFileData));
        }
        if(Settings.isFools2022Mode()) {
            rcdUpdaters.add(new Fools2022RcdUpdater(rcdFileData, datFileData));
        }
        if(Settings.isHalloweenMode()) {
            rcdUpdaters.add(new HalloweenRcdUpdater(rcdFileData, datFileData));
        }
        rcdUpdaters.add(new RandomizationRcdUpdater(rcdFileData, datFileData, flagManager,
                itemRandomizer, shopRandomizer, npcRandomizer, sealRandomizer,
                transitionGateRandomizer, backsideDoorRandomizer));

        for(RcdUpdater rcdUpdater : rcdUpdaters) {
            rcdUpdater.updateObjects();
        }
        trackObjects();
        for(RcdUpdater rcdUpdater : rcdUpdaters) {
            rcdUpdater.addUntrackedObjects();
        }
        for(RcdUpdater rcdUpdater : rcdUpdaters) {
            rcdUpdater.doShuffleUpdates(random);
        }
        for(RcdUpdater rcdUpdater : rcdUpdaters) {
            rcdUpdater.doPostShuffleUpdates();
        }
    }

    private void trackObjects() {
        for(Zone zone : rcdFileData.getZones()) {
            for(GameObject gameObject : zone.getObjects()) {
                for(RcdUpdater rcdUpdater : rcdUpdaters) {
                    rcdUpdater.trackObject(gameObject);
                }
            }
            for(Room room : zone.getRooms()) {
                for(GameObject gameObject : room.getObjects()) {
                    for(RcdUpdater rcdUpdater : rcdUpdaters) {
                        rcdUpdater.trackObject(gameObject);
                    }
                }
                for(Screen screen : room.getScreens()) {
                    for(GameObject gameObject : screen.getObjects()) {
                        for(RcdUpdater rcdUpdater : rcdUpdaters) {
                            rcdUpdater.trackObject(gameObject);
                        }
                    }
                }
            }
        }
    }
}
