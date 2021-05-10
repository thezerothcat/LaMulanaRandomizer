package lmr.randomizer.rcd.object;

/**
 * 0xc7 effect-escape-screenshake Reversed; Tested.
 *     0: StopFlag
 *     1: StopValue
 *
 *     If StopFlag <= 0, then the screenshake does not end.
 *     Otherwise, when the [StopFlag] == StopValue, then it stops creating new effects, and the screen vibration ends within a few seconds.
 */
public class EscapeScreenShake extends GameObject {
    public EscapeScreenShake(ObjectContainer objectContainer) {
        super(objectContainer, 2);
        setId((short)0xc7);
        setX(-1);
        setY(-1);
    }

    public void setStopFlag(int stopFlag) {
        getArgs().set(0, (short)stopFlag);
    }

    public void setStopValue(int stopValue) {
        getArgs().set(1, (short)stopValue);
    }
}
