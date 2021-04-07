package lmr.randomizer.rcd.object;

/**
 * Created by thezerothcat on 7/18/2017.
 */
public class ScreenExit {
    public static int UP = 0;
    public static int RIGHT = 1;
    public static int DOWN = 2;
    public static int LEFT = 3;

    byte zoneIndex;
    byte roomIndex;
    byte screenIndex;

    public byte getZoneIndex() {
        return zoneIndex;
    }

    public void setZoneIndex(byte zoneIndex) {
        this.zoneIndex = zoneIndex;
    }

    public byte getRoomIndex() {
        return roomIndex;
    }

    public void setRoomIndex(byte roomIndex) {
        this.roomIndex = roomIndex;
    }

    public byte getScreenIndex() {
        return screenIndex;
    }

    public void setScreenIndex(byte screenIndex) {
        this.screenIndex = screenIndex;
    }
}
