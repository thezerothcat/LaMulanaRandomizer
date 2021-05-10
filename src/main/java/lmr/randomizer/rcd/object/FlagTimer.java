package lmr.randomizer.rcd.object;

/**
 * 0x0b Flag Timer Reversed; Tested
 *     // Performs all updates after time specified.
 *     0 - Seconds
 *     1 - Frames
 */
public class FlagTimer extends GameObject {
    public FlagTimer(ObjectContainer objectContainer) {
        super(objectContainer, 2);
        setId((short)0x0b);
        setX(-1);
        setY(-1);
    }

    public void setDelaySeconds(int delaySeconds) {
        getArgs().set(0, (short)delaySeconds);
    }

    public void setDelayFrames(int delayFrames) {
        getArgs().set(1, (short)delayFrames);
    }
}
