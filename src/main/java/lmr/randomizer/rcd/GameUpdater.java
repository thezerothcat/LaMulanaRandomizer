package lmr.randomizer.rcd;

import lmr.randomizer.Settings;
import lmr.randomizer.dat.*;
import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.rcd.object.Room;
import lmr.randomizer.rcd.object.Screen;
import lmr.randomizer.rcd.object.Zone;
import lmr.randomizer.update.GameDataTracker;

import java.util.ArrayList;
import java.util.List;

public class GameUpdater {
    private RcdFileData rcdFileData;
    private DatFileData datFileData;

    private List<RcdUpdater> rcdUpdaters = new ArrayList<>();
    private List<DatUpdater> datUpdaters = new ArrayList<>();

    public GameUpdater(RcdFileData rcdFileData, DatFileData datFileData) {
        this.rcdFileData = rcdFileData;
        this.datFileData = datFileData;
    }

    public void updateRcd() {
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
        if(Settings.isHalloweenMode()) {
            rcdUpdaters.add(new HalloweenRcdUpdater(rcdFileData, datFileData));
        }

        for(RcdUpdater rcdUpdater : rcdUpdaters) {
            rcdUpdater.updateObjects();
        }
        trackObjects();
        for(RcdUpdater rcdUpdater : rcdUpdaters) {
            rcdUpdater.addUntrackedObjects();
        }
    }

    public void updateDat() {
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
        if(Settings.isHalloweenMode()) {
            datUpdaters.add(new HalloweenDatUpdater(datFileData));
        }

        for(DatUpdater datUpdater : datUpdaters) {
            datUpdater.addCustomBlocks(datFileData);
        }
        for(DatUpdater datUpdater : datUpdaters) {
            datUpdater.updateBlocks();
        }
    }

    public void trackObjects() {
        for(Zone zone : rcdFileData.getZones()) {
            for(GameObject gameObject : zone.getObjects()) {
                GameDataTracker.addObject(gameObject);
            }
            for(Room room : zone.getRooms()) {
                for(GameObject gameObject : room.getObjects()) {
                    GameDataTracker.addObject(gameObject);
                }
                for(Screen screen : room.getScreens()) {
                    for(GameObject gameObject : screen.getObjects()) {
                        GameDataTracker.addObject(gameObject);
                    }
                }
            }
        }
    }

    public void doPostShuffleUpdates() {
        for(RcdUpdater rcdUpdater : rcdUpdaters) {
            rcdUpdater.doPostShuffleUpdates();
        }
    }
}
