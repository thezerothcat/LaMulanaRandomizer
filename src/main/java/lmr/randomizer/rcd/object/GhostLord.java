package lmr.randomizer.rcd.object;

/**
 * 0x20 - enemy-maus-miniboss-ghostlord (Object)
 * 0- (1) Drop Type
 * 1- (30) Amount
 * 2- (1-3) Speed
 * 3- (15-32) Health
 * 4- (8-10) Damage
 * 5- (20) Soul
 */
public class GhostLord extends GameObject {
    public GhostLord(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 6);
        setId((short)0x20);
        setX(x);
        setY(y);
        setDefaults();
    }

    public void setDefaults() {
        // Reference normal mode params, not hard mode.
        setDrops(DropType.COINS, 30);
        setSpeed(1);
        setHealth(15);
        setDamage(8);
        setSoul(20);
    }

    public void setDropType(DropType dropType) {
        getArgs().set(0, dropType.getValue());
    }

    public void setDropQuantity(int dropQuantity) {
        getArgs().set(1, (short)dropQuantity);
    }

    public void setDrops(DropType dropType, int quantity) {
        setDropType(dropType);
        setDropQuantity(quantity);
    }

    public void setSpeed(int speed) {
        getArgs().set(2, (short)speed);
    }

    public void setHealth(int health) {
        getArgs().set(3, (short)health);
    }

    public void setDamage(int damage) {
        getArgs().set(4, (short)damage);
    }

    public void setSoul(int soul) {
        getArgs().set(5, (short)soul);
    }
}
