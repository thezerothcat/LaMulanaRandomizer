package lmr.randomizer.rcd.object;

/**
 * 0xc4 Field Transition Gate
 *     // object invisible
 *     // one use per screen visit
 *     // always performs black screen wipe
 *     // does not reset screen
 *     // animation continues until lemeza goes off screen
 *     0 - destfield
 *     1 - destRoom
 *     2 - destscreen
 *     3 - destx
 *     4 - desty
 *     5 - direction UDRL (**not** URDL)
 *     6 - no effect? t/f
 */
public class TransitionGate extends GameObject {
    public static final int Up = 0;
    public static final int Down = 1;
    public static final int Right = 2;
    public static final int Left = 3;

    public TransitionGate(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 7);
        setId((short)0xc4);
        setX(x);
        setY(y);
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

    public void setTransitionDirection(int transitionDirection) {
        getArgs().set(5, (short)transitionDirection);
    }

    public void setArg6(int arg6) {
        getArgs().set(6, (short)arg6);
    }
}


