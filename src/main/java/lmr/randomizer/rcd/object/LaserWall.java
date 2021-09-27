package lmr.randomizer.rcd.object;

/**
 * 0xab Laser Wall
 *     //Extend upwards until they reach a wall.
 *     0 - % or HP
 *     1 - Damage
 */
public class LaserWall extends GameObject {
    public LaserWall(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 2);
        setId(ObjectIdConstants.LaserWall);
        setX(x);
        setY(y);
    }

    public void setPercentDamage(int damage) {
        getArgs().set(0, (short)0);
        getArgs().set(1, (short)damage);
    }

    public void setFlatDamage(int damage) {
        getArgs().set(0, (short)1);
        getArgs().set(1, (short)damage);
    }
}
