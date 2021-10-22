package lmr.randomizer.update;

import lmr.randomizer.HolidaySettings;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.dat.update.*;
import lmr.randomizer.randomization.*;
import lmr.randomizer.rcd.RcdFileData;
import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.rcd.object.Room;
import lmr.randomizer.rcd.object.Screen;
import lmr.randomizer.rcd.object.Zone;
import lmr.randomizer.rcd.updater.*;
import lmr.randomizer.util.FlagManager;

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

    public void updateDat(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer, NpcRandomizer npcRandomizer) {
        datUpdaters.add(new BaseDatUpdater(datFileData));
//        if(HolidaySettings.isFools2019Mode()) {
//            updaters.add(new Fools2019DatUpdater(datFileData));
//        }
        if(HolidaySettings.isFools2020Mode()) {
            datUpdaters.add(new Fools2020DatUpdater(datFileData));
        }
        if(HolidaySettings.isFools2021Mode()) {
            datUpdaters.add(new Fools2021DatUpdater(datFileData));
        }
        if(HolidaySettings.isFools2022Mode()) {
            datUpdaters.add(new Fools2022DatUpdater(datFileData));
        }
        if(HolidaySettings.isHalloween2019Mode()) {
            datUpdaters.add(new Halloween2019DatUpdater(datFileData));
        }
        if(HolidaySettings.isHalloween2021Mode()) {
            datUpdaters.add(new Halloween2021DatUpdater(datFileData));
        }
        datUpdaters.add(new RandomizationDatUpdater(datFileData, itemRandomizer, shopRandomizer, npcRandomizer, flagManager));

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
        if(HolidaySettings.isFools2019Mode()) {
            rcdUpdaters.add(new Fools2019RcdUpdater(rcdFileData, datFileData));
        }
        if(HolidaySettings.isFools2020Mode()) {
            rcdUpdaters.add(new Fools2020RcdUpdater(rcdFileData, datFileData));
        }
        if(HolidaySettings.isFools2021Mode()) {
            rcdUpdaters.add(new Fools2021RcdUpdater(rcdFileData, datFileData));
        }
        if(HolidaySettings.isFools2022Mode()) {
            rcdUpdaters.add(new Fools2022RcdUpdater(rcdFileData, datFileData));
        }
        if(HolidaySettings.isHalloween2019Mode()) {
            rcdUpdaters.add(new Halloween2019RcdUpdater(rcdFileData, datFileData));
        }
        if(HolidaySettings.isHalloween2021Mode()) {
            rcdUpdaters.add(new Halloween2021RcdUpdater(rcdFileData, datFileData));
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
