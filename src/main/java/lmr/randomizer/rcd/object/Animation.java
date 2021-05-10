package lmr.randomizer.rcd.object;

/**
 * 0xa3 Animation
 *     0 - layer
 *     1 -
 *     2 - graphic
 *     3 - mdd select >=72 use internal list from arg22
 *     4 - Entry Effect
 *         1=fade
 *         2=fade from black
 *         3=fade from white
 *         4=fade through black
 *         5=fade through white
 *         default=appear?
 *     5 - Exit Effect
 *         0=nothing ever happens
 *         1=fade
 *         5=fade through black
 *         default=vanish
 *     6 - UseDustTrail
 *     7 - Motion Damage
 *     8 - Finished Damage
 *     9 -
 *     10-
 *     11-
 *     12-
 *     13-
 *     14-
 *     15-
 *     16-
 *     17-
 *     18-
 *     19- AnimationXShift??
 *     20- AnimationYShift??
 *     21-
 *     22-
 */
public class Animation extends GameObject {
    public Animation(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 23);
        setId((short)0xa3);
        setX(x);
        setY(y);
    }

    public void setLayer(int layer) {
        getArgs().set(0, (short)layer);
    }

    public void setArg1(int arg1) {
        getArgs().set(1, (short)arg1);
    }

    public void setGraphic(int graphic) {
        getArgs().set(2, (short)graphic);
    }

    public void setMdd(int mdd) {
        // mdd select >=72 use internal list from arg22
        getArgs().set(3, (short)mdd);
    }

    public void setEntryEffect(int entryEffect) {
        getArgs().set(4, (short)entryEffect);
    }

    public void setExitEffect(int exitEffect) {
        getArgs().set(5, (short)exitEffect);
    }

    public void setUseDustTrail(int useDustTrail) {
        getArgs().set(6, (short)useDustTrail);
    }

    public void setMotionDamage(int motionDamage) {
        getArgs().set(7, (short)motionDamage);
    }

    public void setFinishedDamage(int finishedDamage) {
        getArgs().set(8, (short)finishedDamage);
    }

    public void setArg9(int arg9) {
        getArgs().set(9, (short)arg9);
    }

    public void setArg10(int arg10) {
        getArgs().set(10, (short)arg10);
    }

    public void setArg11(int arg11) {
        getArgs().set(11, (short)arg11);
    }

    public void setArg12(int arg12) {
        getArgs().set(12, (short)arg12);
    }

    public void setArg13(int arg13) {
        getArgs().set(13, (short)arg13);
    }

    public void setArg14(int arg14) {
        getArgs().set(14, (short)arg14);
    }

    public void setArg15(int arg15) {
        getArgs().set(15, (short)arg15);
    }

    public void setArg16(int arg16) {
        getArgs().set(16, (short)arg16);
    }

    public void setArg17(int arg17) {
        getArgs().set(17, (short)arg17);
    }

    public void setArg18(int arg18) {
        getArgs().set(18, (short)arg18);
    }

    public void setAnimationXShift(int animationXShift) {
        getArgs().set(19, (short)animationXShift);
    }

    public void setAnimationYShift(int animationYShift) {
        getArgs().set(20, (short)animationYShift);
    }

    public void setArg21(int arg21) {
        getArgs().set(21, (short)arg21);
    }

    public void setArg22(int arg22) {
        getArgs().set(22, (short)arg22);
    }
}
