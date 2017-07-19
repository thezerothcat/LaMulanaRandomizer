package lmr.randomizer.rcd;

/**
 * Created by thezerothcat on 7/18/2017.
 */
public class ScreenExit {
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
