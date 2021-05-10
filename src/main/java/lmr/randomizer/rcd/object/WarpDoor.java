package lmr.randomizer.rcd.object;

/**
 * 0x98 Teleport Door/Warp Pot args: Not Reversed; tested
 *     0 - Interaction type: 0 = press up. 1 = press down.
 *     1 - Destination field
 *     2 - Destination room
 *     3 - Destination screen
 *     4 - Destination screen X
 *     5 - Destination screen Y
 */
public class WarpDoor extends GameObject {
    public WarpDoor(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 6);
        setId((short)0x98);
        setX(x);
        setY(y);
    }

    public void setPressDown() {
        // Interaction type: 0 = press up. 1 = press down.
        getArgs().set(0, (short)1);
    }

    public void setDestinationZone(int zone) {
        getArgs().set(1, (short)zone);
    }

    public void setDestinationRoom(int room) {
        getArgs().set(2, (short)room);
    }

    public void setDestinationScreen(int screen) {
        getArgs().set(3, (short)screen);
    }

    public void setDestinationX(int x) {
        getArgs().set(4, (short)x);
    }

    public void setDestinationY(int y) {
        getArgs().set(5, (short)y);
    }

    public void setDestination(int zone, int room, int screen, int x, int y) {
        setDestinationZone(zone);
        setDestinationRoom(room);
        setDestinationScreen(screen);
        setDestinationX(x);
        setDestinationY(y);
    }
}
