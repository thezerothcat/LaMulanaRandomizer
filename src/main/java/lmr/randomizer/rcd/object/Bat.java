package lmr.randomizer.rcd.object;

/**
 * 0x02
 * HP=1 hardcoded
 * 0 - (0-1) 0 start resting | 1 start flying
 * 1 - (1-2) Drop Type
 * 2 - (1-3)
 * 3 - (0-1) Bat type 0 = normal, 1 = cyclops
 * 4 - (2-3) Damage
 */
public class Bat extends GameObject {
    public Bat(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 5);
        setId(ObjectIdConstants.Enemy_Bat);
        setX(x);
        setY(y);
    }

    public Bat(GameObject gameObject) {
        super(gameObject);
    }

    public void setInitiallyFlying(boolean initiallyFlying) {
        getArgs().set(0, (short)(initiallyFlying ? 1 : 0));
    }

    public int getDropType() {
        return getArgs().get(1);
    }

    public void setDropType(DropType dropType) {
        getArgs().set(1, dropType.getValue());
    }

    public void setDropType(int dropType) {
        getArgs().set(1, (short)dropType);
    }

    public void setArg2(int arg2) {
        getArgs().set(2, (short)arg2);
    }

    public void setFrontsideBat(boolean frontsideBat) {
        getArgs().set(3, (short)(frontsideBat ? 0 : 1));
    }

    public int getDamage() {
        return getArgs().get(4);
    }

    public void setDamage(int damage) {
        getArgs().set(4, (short)damage);
    }
}
