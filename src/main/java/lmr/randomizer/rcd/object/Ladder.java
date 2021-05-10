package lmr.randomizer.rcd.object;

/**
 * 0x07 - Ladder Spawner (Object) Not Reversed
 *     0 - (0-1) 0 = From Top, 1 = From Bottom
 *     1 - (4-18) Height in graphical tiles
 *     2 - (0-2) Graphic <=1 Map >1 eveg
 *     3 - (0) (unused?)
 *     4 - (360-900) ImageX
 *     5 - (0-720) ImageY
 *     6 - (0-1) 0 = Standard, 1 = Philosopher
 *     7 - (0-1)
 */
public class Ladder extends GameObject {
    public static final int FromTop = 0;
    public static final int FromBottom = 1;

    public Ladder(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 8);
        setId((short)0x07);
        setX(x);
        setY(y);
    }

    public void setExtendDirection(int direction) {
        getArgs().set(0, (short)direction);
    }

    public void setHeight(int gTileHeight) {
        getArgs().set(1, (short)gTileHeight);
    }

    public void setGraphicsFromMapPng() {
        getArgs().set(2, (short)0);
    }

    public void setGraphicsFromEvegPng() {
        getArgs().set(2, (short)2);
    }

    public void setArg3(int arg3) {
        getArgs().set(3, (short)arg3);
    }

    public void setImageX(int imageX) {
        getArgs().set(4, (short)imageX);
    }

    public void setImageY(int imageY) {
        getArgs().set(5, (short)imageY);
    }

    public void setStandardLadder() {
        getArgs().set(6, (short)0);
    }

    public void setPhilosopherLadder() {
        getArgs().set(6, (short)1);
    }

    public void setArg7(int arg7) {
        getArgs().set(7, (short)arg7);
    }
}
