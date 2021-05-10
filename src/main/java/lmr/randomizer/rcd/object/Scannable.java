package lmr.randomizer.rcd.object;

/**
 * 0x9e tablet:
 *     0 - screenplay card
 *     1 -
 *     2 -
 *     3 -
 *     4 -
 *     5 -
 *     6 -
 *     7 -
 *     8 -
 *     9 -
 *     10- dx
 *     11- dy
 *
 *     Card format:
 *     <text> <endRecord>
 *     <DATA: lang slate> <endRecord>
 *     (if slate):
 *         <DATA: U V dU dV><endRecord>
 *         <DATA: Xpos Ypos>
 *
 *     Language:
 *         0 English
 *         1 La-Mulanese
 *         2 Ancient La-Mulanese
 *         3 Rosetta Stone
 *     Slate:
 *         0 No image.
 *         1 use slate00.png
 *         2 use slate01.png
 *
 *     U V Image position in slate.
 *     dU dV image size in slate.
 *     Xpos Ypos image position in scan.
 */
public class Scannable extends GameObject {
    public Scannable(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 12);
        setId((short)0x9e);
        setX(x);
        setY(y);
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

    public void setArg9(int arg9) {
        getArgs().set(9, (short)arg9);
    }

    public void setWidth(int pixelWidth) {
        getArgs().set(10, (short)pixelWidth);
    }

    public void setHeight(int pixelHeight) {
        getArgs().set(11, (short)pixelHeight);
    }

    public void setDimensions(int pixelWidth, int pixelHeight) {
        setWidth(pixelWidth);
        setHeight(pixelHeight);
    }
}
