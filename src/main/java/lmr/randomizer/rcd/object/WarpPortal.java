package lmr.randomizer.rcd.object;

/**
 *  0x97 Warp args: Not Reversed; Tested
 *  0 - Destination field
 *  1 - Destination room
 *  2 - Destination screen
 *  3 - Destination screen X
 *  4 - Destination screen Y
 *  5 - gTile dX
 *  6 - gTile dY
 *  7 - Enter? animation
 *  8 - Exit? animation
 *  Animations: 1=oval move left 3=oval move right 4=circle default=hardlock game
 */
public class WarpPortal extends GameObject {
    public WarpPortal(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 9);
        setId((short)0x97);
        setX(x);
        setY(y);
        setDefaults();
    }

    public void setDefaults() {
        setEnterAnimation(4);
        setExitAnimation(4);
    }

    public void setDestinationZone(int zone) {
        getArgs().set(0, (short)zone);
    }

    public void setDestinationRoom(int room) {
        getArgs().set(1, (short)room);
    }

    public void setDestinationScreen(int screen) {
        getArgs().set(2, (short)screen);
    }

    public void setDestinationX(int x) {
        getArgs().set(3, (short)x);
    }

    public void setDestinationY(int y) {
        getArgs().set(4, (short)y);
    }

    public void setDestination(int zone, int room, int screen, int x, int y) {
        setDestinationZone(zone);
        setDestinationRoom(room);
        setDestinationScreen(screen);
        setDestinationX(x);
        setDestinationY(y);
    }

    public void setWidth(int gTileWidth) {
        getArgs().set(5, (short)gTileWidth);
    }

    public void setHeight(int gTileHeight) {
        getArgs().set(6, (short)gTileHeight);
    }

    public void setDimensions(int gTileWidth, int gTileHeight) {
        setWidth(gTileWidth);
        setHeight(gTileHeight);
    }

    public void setEnterAnimation(int enterAnimation) {
        getArgs().set(7, (short)enterAnimation);
    }

    public void setExitAnimation(int exitAnimation) {
        getArgs().set(8, (short)exitAnimation);
    }
}
