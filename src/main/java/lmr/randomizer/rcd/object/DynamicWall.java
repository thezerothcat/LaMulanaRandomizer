package lmr.randomizer.rcd.object;

import lmr.randomizer.util.ZoneConstants;

/**
 * 0x09 - Wall Spawner (Object)
 * 0- (0)
 * 1- (3-4) Height (in graphical tiles?)
 * 2- (2) Sheet?
 * 3- (0)
 * 4- (420-800) Tile x?
 * 5- (0-240) Tile y?
 */
public class DynamicWall extends GameObject {
    public DynamicWall(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 6);
        setId(ObjectIdConstants.DynamicWall);
        setX(x);
        setY(y);
        setDefaults();
    }

    public void setDefaults() {
        setArg2(2); // eveg?
        setImageX(540);
        setImageY(0);
    }

    public void setArg0(int arg0) {
        getArgs().set(0, (short)arg0);
    }

    public void setHeight(int height) {
        getArgs().set(1, (short)height);
    }

    public void setArg2(int arg2) {
        getArgs().set(2, (short)arg2); // probably image file; base game uses 2 and pulls from eveg; could also be width
    }

    public void setArg3(int arg3) {
        getArgs().set(3, (short)arg3);
    }

    public void setImageX(int imageX) {
        // X-component of top left corner for where to begin drawing, on the image file
        getArgs().set(4, (short)imageX);
    }

    public void setImageY(int imageY) {
        // Y-component of top left corner for where to begin drawing, on the image file
        getArgs().set(5, (short)imageY);
    }

    public void setGraphicsFromZone(int zoneIndex) {
        if(zoneIndex == ZoneConstants.ILLUSION) {
            setImageX(540);
            setImageY(0);
        }
        if(zoneIndex == ZoneConstants.MOONLIGHT) {
            setImageX(540);
            setImageY(0);
        }
    }
}
