package lmr.randomizer.rcd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/16/2017.
 */
public class Room implements ObjectContainer {
    private List<Screen> screens = new ArrayList<Screen>();
    private List<Object> objects = new ArrayList<>();

    private int zoneIndex;
    private int roomIndex;

    private byte numberOfLayers;
    private byte primeLayerNumber;

    private int numberOfScreens;

    private short hitMaskWidth;
    private short hitMaskHeight;

    private short tileWidth;
    private short tileHeight;

    private short screenWidth;
    private short screenHeight;

    public Room() {
    }

    public int getZoneIndex() {
        return zoneIndex;
    }

    public void setZoneIndex(int zoneIndex) {
        this.zoneIndex = zoneIndex;
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }

    public byte getNumberOfLayers() {
        return numberOfLayers;
    }

    public void setNumberOfLayers(byte numberOfLayers) {
        this.numberOfLayers = numberOfLayers;
    }

    public byte getPrimeLayerNumber() {
        return primeLayerNumber;
    }

    public void setPrimeLayerNumber(byte primeLayerNumber) {
        this.primeLayerNumber = primeLayerNumber;
    }

    public int getNumberOfScreens() {
        return numberOfScreens;
    }

    public void setNumberOfScreens(int numberOfScreens) {
        this.numberOfScreens = numberOfScreens;
    }

    public short getHitMaskWidth() {
        return hitMaskWidth;
    }

    public void setHitMaskWidth(short hitMaskWidth) {
        this.hitMaskWidth = hitMaskWidth;
    }

    public short getHitMaskHeight() {
        return hitMaskHeight;
    }

    public void setHitMaskHeight(short hitMaskHeight) {
        this.hitMaskHeight = hitMaskHeight;
    }

    public short getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(short tileWidth) {
        this.tileWidth = tileWidth;
    }

    public short getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(short tileHeight) {
        this.tileHeight = tileHeight;
    }

    public short getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(short screenWidth) {
        this.screenWidth = screenWidth;
    }

    public short getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(short screenHeight) {
        this.screenHeight = screenHeight;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public List<Object> getObjects() {
        return objects;
    }

    @Override
    public String toString() {
        StringBuilder returnVal = new StringBuilder(String.format("ROOM %02d-%02d", zoneIndex, roomIndex));
        if(!objects.isEmpty()) {
            for(Object obj : objects) {
                returnVal.append('\n').append(obj.toString());
            }
            for(Screen screen : screens) {
                returnVal.append('\n').append(screen.toString());
            }
        }
        return returnVal.toString();
    }
}
