package lmr.randomizer.rcd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/16/2017.
 */
public class Zone implements ObjectContainer {
    private List<Room> rooms = new ArrayList<Room>();
    private int zoneIndex;
    private String name;

    private List<Object> objects = new ArrayList<>();

    public Zone() {
    }

    public int getZoneIndex() {
        return zoneIndex;
    }

    public void setZoneIndex(int index) {
        this.zoneIndex = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Object> getObjects() {
        return objects;
    }

    @Override
    public String toString() {
        StringBuilder returnVal = new StringBuilder(String.format("ZONE %02d \"%s\"", zoneIndex, name));
        if(!objects.isEmpty()) {
            for(Object obj : objects) {
                returnVal.append('\n').append(obj.toString());
            }
            for(Room room : rooms) {
                returnVal.append('\n').append(room.toString());
            }
        }
        return returnVal.toString();
    }
}
