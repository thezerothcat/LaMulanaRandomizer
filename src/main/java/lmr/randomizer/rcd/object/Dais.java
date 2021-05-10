package lmr.randomizer.rcd.object;

/**
 * 0x08 - trigger-dais (Object) Not Reversed
 *     0 - (0-1) Light red dust or pink dust
 *     1 - (1-270) Falling time (in frames?)
 *     2 - (-1-50) RiseFlag -1 Never Rise. 0 Always Rise
 *     3 - (0-2) Image
 *     4 - (0) (unused?)
 *     5 - (180-860) ImageX
 *     6 - (0-100) ImageY
 *     7 - (0-1) Width 0 = Half-width, 1 = Full-width
 *     8 - (0-10) (probably unused height)
 *     9 - (0-60) RiseSpeed
 */
public class Dais extends GameObject {
    public Dais(ObjectContainer objectContainer) {
        super(objectContainer, 10);
    }

    public Dais(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 10);
        setId((short)0x08);
        setX(x);
        setY(y);
    }

    public void setDustAppearance(int dustAppearance) {
        // Light red dust or pink dust
        getArgs().set(0, (short)dustAppearance);
    }

    public void setFallingTime(int fallingTime) {
        // (1-270) Falling time (in frames?)
        getArgs().set(1, (short)fallingTime);
    }

    public void setRise(int rise) {
        // (-1-50) RiseFlag -1 Never Rise. 0 Always Rise
        getArgs().set(2, (short)rise);
    }

    public void setImage(int image) {
        // (0-2) Image
        getArgs().set(3, (short)image);
    }

    public void setArg4(int arg4) {
        // (0) (unused?)
        getArgs().set(4, (short)arg4);
    }

    public void setImageX(int x) {
        getArgs().set(5, (short)x);
    }

    public void setImageY(int y) {
        getArgs().set(6, (short)y);
    }

    public void setFullWidth() {
        // Width 0 = Half-width, 1 = Full-width
        getArgs().set(7, (short)1);
    }

    public void setArg8(int arg8) {
        // (0-10) (probably unused height)
        getArgs().set(8, (short)arg8);
    }

    public void setRiseSpeed(int riseSpeed) {
        // (0-60) RiseSpeed
        getArgs().set(9, (short)riseSpeed);
    }
}
