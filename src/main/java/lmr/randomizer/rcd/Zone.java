package lmr.randomizer.rcd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/16/2017.
 */
public class Zone {
    private List<Room> rooms = new ArrayList<Room>();
    private int index;
    private String name;

    private List<Object> objects = new ArrayList<>();

    public Zone() {
    }

//    @Override
//    public String toString() {
//        String returnVal = String.format("ZONE %02d \"%s\"", self.idx, self.name);
//        if(self.objs) {
//            ret += '\n' + '\n'.join(map(str, self.objs))
//            ret += '\n' + '\n'.join(map(str, self.rooms))
//        }
//        return returnVal;
//    }


    public List<Room> getRooms() {
        return rooms;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getObjects() {
        return objects;
    }
}
