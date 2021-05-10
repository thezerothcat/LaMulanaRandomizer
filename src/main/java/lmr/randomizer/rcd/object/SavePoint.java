package lmr.randomizer.rcd.object;

/**
 0xb6 Save Point
 0 - SE select
 */
public class SavePoint extends GameObject {
    public SavePoint(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 1);
        setId((short)0xb6);
        setX(x);
        setY(y);
        setDefaults();
    }

    public void setDefaults() {
        setSoundEffect(33);
    }

    public void setSoundEffect(int soundEffect) {
        getArgs().set(0, (short)soundEffect);
    }
}
