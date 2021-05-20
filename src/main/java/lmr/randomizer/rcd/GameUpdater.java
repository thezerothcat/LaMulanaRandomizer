package lmr.randomizer.rcd;

import lmr.randomizer.Settings;
import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.rcd.object.Room;
import lmr.randomizer.rcd.object.Screen;
import lmr.randomizer.rcd.object.Zone;
import lmr.randomizer.update.GameDataTracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Currently just rcd updates, may eventually include other files
 */
public final class GameUpdater {
    public static void update(RcdData rcdData) {
        List<RcdUpdater> updaters = new ArrayList<>();
        updaters.add(new BaseRcdUpdater(rcdData));
        if(Settings.isFools2019Mode()) {
            updaters.add(new Fools2019RcdUpdater(rcdData));
        }
        if(Settings.isFools2020Mode()) {
            updaters.add(new Fools2020RcdUpdater(rcdData));
        }
        if(Settings.isFools2021Mode()) {
            updaters.add(new Fools2021RcdUpdater(rcdData));
        }
        if(Settings.isHalloweenMode()) {
            updaters.add(new HalloweenRcdUpdater(rcdData));
        }

        for(RcdUpdater rcdUpdater : updaters) {
            rcdUpdater.updateObjects();
        }
        trackObjects(rcdData);
        for(RcdUpdater rcdUpdater : updaters) {
            rcdUpdater.addUntrackedObjects();
        }
    }

    public static void trackObjects(RcdData rcdData) {
        for(Zone zone : rcdData.getZones()) {
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

    private GameUpdater() { }
}
