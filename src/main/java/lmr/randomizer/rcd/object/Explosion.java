package lmr.randomizer.rcd.object;

/**
 * 0xb4 Explosion
 *     0 - Width & Height
 *     1 -
 *     2 - Frames No Animation
 *     3 -
 *     4 - hp or %
 *     5 - Damage
 *     6 - SE select
 */
public class Explosion extends GameObject {
    public Explosion(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 7);
        setId((short)0xb4);
        setX(x);
        setY(y);
        setDefaults();
    }

    public void setDefaults() {
        // Taken from the Key Fairy shop explosion; the exploding chest in Gate of Illusion has different values
        setSize(200); // 100
        setArg1(1); // 2
        setFramesNoAnimation(6);
        setArg3(6); // 0
        setPercentDamage(60); // 40
        setSoundEffect(85);
    }

    public void setSize(int widthHeight) {
        getArgs().set(0, (short)widthHeight);
    }

    public void setArg1(int arg1) {
        getArgs().set(1, (short)arg1);
    }

    public void setFramesNoAnimation(int framesNoAnimation) {
        getArgs().set(2, (short)framesNoAnimation);
    }

    public void setArg3(int arg3) {
        getArgs().set(3, (short)arg3);
    }

    public void setFlatDamage(int damage) {
        getArgs().set(4, (short)0);
        getArgs().set(5, (short)damage);
    }

    public void setPercentDamage(int damage) {
        getArgs().set(4, (short)1);
        getArgs().set(5, (short)damage);
    }

    public void setSoundEffect(int soundEffect) {
        getArgs().set(6, (short)soundEffect);
    }
}
