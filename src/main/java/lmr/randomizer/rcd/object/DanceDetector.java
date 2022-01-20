package lmr.randomizer.rcd.object;

/**
 * 0xb8 - detection-dance (Object) screenplay ref
 * 0 - (983-984) screenplay seg
 * 1 - (6-14) gtile dx
 * 2 - (3-7) gtile dy
 */
public class DanceDetector extends GameObject {
    public DanceDetector(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 3);
        setId(ObjectIdConstants.DanceDetector);
        setX(x);
        setY(y);
    }

    public void setBlockNumber(int blockNumber) {
        getArgs().set(0, (short)blockNumber);
    }

    public void setWidth(int gTileWidth) {
        getArgs().set(1, (short)gTileWidth);
    }

    public void setHeight(int gTileHeight) {
        getArgs().set(2, (short)gTileHeight);
    }
}
