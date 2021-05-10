package lmr.randomizer.rcd.object;

/**
 * 0xc5 Escape Timer
 *     //Ends 1 second early.
 *     //Only updates the flag in argument 10.
 *     0 - XPosition
 *     1 - YPosition
 *     2 - Minutes
 *     3 - Seconds
 *     4 - 0:countdown 1:countup 2:time attack
 *     5 - Red Seconds
 *     6 - Second Sound
 *     7 - Red Second Sound
 *     8 - probably also a sound?
 *     9 - PauseTimerFlag
 *     10- TimerRunOutFlag
 *     11- StopTimerFlag
 */
public class EscapeTimer extends GameObject {
    public static final int CountDown = 0;
    public static final int CountUp = 1;
    public static final int TimeAttack = 2;

    public EscapeTimer(ObjectContainer objectContainer) {
        super(objectContainer, 12);
        setId((short)0xc5);
        setX(-1);
        setY(-1);
    }

    public void setXPosition(int xPosition) {
        getArgs().set(0, (short)xPosition);
    }

    public void setYPosition(int yPosition) {
        getArgs().set(1, (short)yPosition);
    }

    public void setMinutes(int minutes) {
        getArgs().set(2, (short)minutes);
    }

    public void setSeconds(int seconds) {
        getArgs().set(3, (short)seconds);
    }

    public void setTimerMode(int timerMode) {
        getArgs().set(4, (short)timerMode);
    }

    public void setRedSeconds(int redSeconds) {
        getArgs().set(5, (short)redSeconds);
    }

    public void setSecondSound(int secondSound) {
        getArgs().set(6, (short)secondSound);
    }

    public void setRedSecondSound(int redSecondSound) {
        getArgs().set(7, (short)redSecondSound);
    }

    public void setArg8(int arg8) {
        getArgs().set(8, (short)arg8);
    }

    public void setPauseTimerFlag(int pauseTimerFlag) {
        getArgs().set(9, (short)pauseTimerFlag);
    }

    public void setTimerRunOutFlag(int timerRunOutFlag) {
        getArgs().set(10, (short)timerRunOutFlag);
    }

    public void setStopTimerFlag(int stopTimerFlag) {
        getArgs().set(11, (short)stopTimerFlag);
    }
}
