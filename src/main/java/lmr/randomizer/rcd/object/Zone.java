package lmr.randomizer.rcd.object;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/16/2017.
 */
public class Zone implements ObjectContainer {
    private List<Room> rooms = new ArrayList<Room>();
    private int zoneIndex;
    private String name;

    private List<GameObject> objects = new ArrayList<>();

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

    public Room getRoom(int roomIndex) {
        for(Room room : rooms) {
            if(room.getRoomIndex() == roomIndex) {
                return room;
            }
        }
        return null;
    }

    public List<GameObject> getObjectsById(int id) {
        List<GameObject> objectsById = new ArrayList<>();
        for(GameObject object : objects) {
            if(object.getId() == id) {
                objectsById.add(object);
            }
        }
        return objectsById;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    @Override
    public String toString() {
        StringBuilder returnVal = new StringBuilder(String.format("ZONE %02d \"%s\"", zoneIndex, name));
        if(!objects.isEmpty()) {
            for(GameObject obj : objects) {
                returnVal.append('\n').append(obj.toString());
            }
            for(Room room : rooms) {
                returnVal.append('\n').append(room.toString());
            }
        }
        return returnVal.toString();
    }

    public String getContainerString() {
        return String.format("ZONE %02d \"%s\"", zoneIndex, name);
    }
}
