package lmr.randomizer.rcd.object;

/**
 * 0- (1-46) Tile-block
 * 1- (-14-9) Destination Layer
 * 2- (0-1) Use Hit Map
 * 3- (0-1) Entry Effect: 0 = Normal, 1 = Fade
 * 4- (0-9) Exit Effect: 0 = Normal, 1 = Fade, 2 = Large Break, 3 = Crack-break, 4 = Fade, 5 = Vanish white, 6 = Vanish black, 7 = Red streaks, 8 = white glow + rise, 9 = Break Glass
 * 5- (0-1) Use ARGB Cycle
 * 6- (0-10) dA
 * 7- (100-255) min A
 * 8- (0) dR
 * 9- (0) minR
 * 10- (0-5) dG
 * 11- (0-50) minG
 * 12- (0-6) dB
 * 13- (0-60) minB
 *
 * 0x0e Room Spawner Not Reversed
 *     0 - Room
 *     1 - Destination layer
 *     2 - UseHitMap
 *     3 - Entry Effect
 *         0:Normal
 *         1:Fade
 *     4 - Exit Effect
 *         0:Normal
 *         1:Fade
 *         2:Large Break
 *         3:Crack-Break
 *         4:Also Fade
 *         5:Go white and vanish in a puff
 *         6:Go Black and vanish in a puff
 *         7:Go Red and do that streak dealie
 *         8:Glow white + rising white pixels
 *         9:Break Glass
 *     5 - Use ARGB Cycle
 *     6 - dA
 *     7 - Min A
 *     8 - dR
 *     9 - Max R
 *     10- dG
 *     11- Max G
 *     12- dB
 *     13- Max B
 */
public class RoomSpawner extends GameObject {
    public RoomSpawner(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 14);
        setId(ObjectIdConstants.RoomSpawner);
        setX(x);
        setY(y);
    }

    public void setRoom(int room) {
        getArgs().set(0, (short)room);
    }

    public void setLayer(int layer) {
        getArgs().set(1, (short)layer);
    }

    public void setUseHitMap(boolean useHitMap) {
        getArgs().set(2, (short)(useHitMap ? 1 : 0));
    }

    public void setEntryEffect(int entryEffect) {
        getArgs().set(3, (short)entryEffect);
    }

    public void setExitEffect(int exitEffect) {
        getArgs().set(4, (short)exitEffect);
    }

    public void setCycleColors(boolean cycleColors) {
        getArgs().set(5, (short)(cycleColors ? 1 : 0));
    }

    public void setAlphaChange(int alphaChange) {
        getArgs().set(6, (short)alphaChange);
    }

    public void setMinAlpha(int minAlpha) {
        getArgs().set(7, (short)minAlpha);
    }

    public void setRedChange(int redChange) {
        getArgs().set(8, (short)redChange);
    }

    public void setMinRed(int minRed) {
        getArgs().set(9, (short)minRed);
    }

    public void setGreenChange(int greenChange) {
        getArgs().set(10, (short)greenChange);
    }

    public void setMinGreen(int minGreen) {
        getArgs().set(11, (short)minGreen);
    }

    public void setBlueChange(int blueChange) {
        getArgs().set(12, (short)blueChange);
    }

    public void setMinBlue(int minBlue) {
        getArgs().set(13, (short)minBlue);
    }
}
