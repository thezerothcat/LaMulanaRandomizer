package lmr.randomizer.rcd.object;

/**
 *  0xa9 Block
 *     0 - Push Damage
 *     1 - Fall Damage
 */
public class PushableBlock extends GameObject {
    public PushableBlock(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 2);
        setId((short)0xa9);
        setX(x);
        setY(y);
    }

    public void setPushDamage(int pushDamage) {
        getArgs().set(0, (short)pushDamage);
    }

    public void setFallDamage(int fallDamage) {
        getArgs().set(1, (short)fallDamage);
    }
}
