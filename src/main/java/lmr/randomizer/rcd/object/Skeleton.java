package lmr.randomizer.rcd.object;

/**
 * 0x03 - enemy-com-skeleton (Object)
 * 0- (0-1) Facing
 * 1- (1-11) Drop Type
 * 2- (0-3) Speed -- At speed 8 and above the skeletons move too fast to do their ranged attacks
 * 3- (0-1) Start State: 0 = Walking, 1 = Collapsed
 * 4- (0-2) Type: 0 = Basic, 1 = Bone throw, 2 = Tri-shot
 * 5- (3-13) Health
 * 6- (2-6) Contact Damage
 * 7- (2-5) Projectile Damage
 * 8- (3-8) Soul
 * 9- (2-3) Projectile Speed
 */
public class Skeleton extends GameObject {
    public static final int TYPE_SKELETON = 0;
    public static final int TYPE_SKULLETON = 1;
    public static final int TYPE_SKILLETON = 2;

    public Skeleton(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 10);
        setId(ObjectIdConstants.Enemy_Skeleton);
        setX(x);
        setY(y);
    }

    public Skeleton(GameObject gameObject) {
        super(gameObject);
    }

    public void setFacing(int facing) {
        getArgs().set(0, (short)facing);
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

    public void setSpeed(int speed) {
        getArgs().set(2, (short)speed);
    }

    public void setInitiallyWalking(boolean initiallyWalking) {
        getArgs().set(3, (short)(initiallyWalking ? 0 : 1));
    }

    public void setSkeletonType(int skeletonType) {
        getArgs().set(4, (short)skeletonType);
    }

    public void setHealth(int health) {
        getArgs().set(5, (short)health);
    }

    public void setContactDamage(int damage) {
        getArgs().set(6, (short)damage);
    }

    public void setProjectileDamage(int damage) {
        getArgs().set(7, (short)damage);
    }

    public void setSoul(int soul) {
        getArgs().set(8, (short)soul);
    }

    public void setProjectileSpeed(int speed) {
        getArgs().set(9, (short)speed);
    }
}
