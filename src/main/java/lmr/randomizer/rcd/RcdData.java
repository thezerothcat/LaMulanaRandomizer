package lmr.randomizer.rcd;

import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.rcd.object.Room;
import lmr.randomizer.rcd.object.Screen;
import lmr.randomizer.rcd.object.Zone;

import java.util.ArrayList;
import java.util.List;

public class RcdData {
    private List<Zone> zones;

    public RcdData(List<Zone> zones) {
        this.zones = zones;
    }

    public Zone getZone(int zoneIndex) {
        for(Zone zone : zones) {
            if(zone.getZoneIndex() == zoneIndex) {
                return zone;
            }
        }
        return null;
    }

    public Room getRoom(int zoneIndex, int roomIndex) {
        return getZone(zoneIndex).getRoom(roomIndex);
    }

    public Screen getScreen(int zoneIndex, int roomIndex, int screenIndex) {
        return getZone(zoneIndex).getRoom(roomIndex).getScreen(screenIndex);
    }

    public List<GameObject> getObjectsById(int id) {
        List<GameObject> objects = new ArrayList<>();
        for(Zone zone : zones) {
            objects.addAll(zone.getObjectsById(id));
            for(Room room : zone.getRooms()) {
                objects.addAll(room.getObjectsById(id));
                for(Screen screen : room.getScreens()) {
                    objects.addAll(screen.getObjectsById(id));
                }
            }
        }
        return objects;
    }

    public List<Zone> getZones() {
        return zones;
    }
}
