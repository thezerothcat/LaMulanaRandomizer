package lmr.randomizer.rcd.object;

/**
 * 0x0d Cycle Timer Reversed; Tested
 *     //Performs one update and resets timer
 *     //always performs the next update in sequence.
 *     Update1
 *     Update2
 *     Update3
 *     Update4
 *     0 - seconds
 *     1 - frames
 */
public class CycleTimer extends GameObject{
    public CycleTimer(ObjectContainer objectContainer) {
        super(objectContainer, 2);
        setId(ObjectIdConstants.CycleTimer);
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
