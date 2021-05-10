package lmr.randomizer.rcd.object;

/**
 * 0xb7 Grail Toggle Observed. Not Reversed; Not Tested
 *     // Sets a flag that allows or prevents grail use until changed back.
 *     0 - T/F allow grail
 */
public class GrailToggle extends GameObject {
    public GrailToggle(ObjectContainer objectContainer, boolean enableGrail) {
        super(objectContainer, 1);
        setId((short)0xb7);
        setX(-1);
        setY(-1);
        setEnableGrail(enableGrail);
    }

    public void setEnableGrail(boolean enableGrail) {
        getArgs().set(0, (short)(enableGrail ? 1 : 0));
    }
}
