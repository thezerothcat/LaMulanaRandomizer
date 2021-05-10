package lmr.randomizer.rcd.object;

/**
 * 0x9f Grail Point:
 *     //arguments have identical usage to 9e, but last 3 are not present
 *     0 - screenplay card
 *     1 -
 *     2 -
 *     3 -
 *     4 -
 *     5 -
 *     6 -
 *     7 - seems to always be 506, but unclear what it does
 *     8 - seems to always be 280, but unclear what it does
 */
public class Autosave extends GameObject {
    public Autosave(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 9);
        setId((short)0x9f);
        setX(x);
        setY(y);
        setDefaults();
    }

    public void setDefaults() {
        setArg1(0);
        setArg2(0);
        setArg3(1);
        setArg4(1);
        setArg5(1);
        setArg6(1);
        setArg7(506);
        setArg8(280);
    }

    public void setTextBlock(int textBlock) {
        getArgs().set(0, (short)textBlock);
    }

    public void setArg1(int arg1) {
        getArgs().set(1, (short)arg1);
    }

    public void setArg2(int arg2) {
        getArgs().set(2, (short)arg2);
    }

    public void setArg3(int arg3) {
        getArgs().set(3, (short)arg3);
    }

    public void setArg4(int arg4) {
        getArgs().set(4, (short)arg4);
    }

    public void setArg5(int arg5) {
        getArgs().set(5, (short)arg5);
    }

    public void setArg6(int arg6) {
        getArgs().set(6, (short)arg6);
    }

    public void setArg7(int arg7) {
        getArgs().set(7, (short)arg7);
    }

    public void setArg8(int arg8) {
        getArgs().set(8, (short)arg8);
    }
}
