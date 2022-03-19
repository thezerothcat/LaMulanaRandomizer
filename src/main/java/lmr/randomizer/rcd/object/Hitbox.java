package lmr.randomizer.rcd.object;

/**
 *     0 - visual 1:dust >1: star
 *     1 - 0:hp 1:hits
 *     2 - health
 *     3 - direction: 0 - up 1 - right 2 - down 3 - left 4 - any
 *     4 - weapon type: 0-15 same as word, 16 all main 17 all sub 18 all 19 none
 *     5 - Update Type
 *         0-  break: update all 4.
 *             wrongwep: update none.
 *         1-  break: update 0,2
 *             wrongwep: update 1,3
 *     6 - hitbox sizex
 *     7 - hitbox sizey
 *     8 - Hit Success Sound Effect (-1 for silent)
 *     9 - Hit Fail Sound Effect (-1 for silent)
 *     10- dust1 density 1
 *     11- dust2 density 2
 */
public class Hitbox extends GameObject {
    public static final int FromAbove = 0;
    public static final int FromRight = 1;
    public static final int FromBelow = 2;
    public static final int FromLeft = 3;
    public static final int AnyDirection = 4;

    public static final int Whip = 0;
    public static final int ChainWhip = 1;
    public static final int FlailWhip = 2;
    public static final int Knife = 3;
    public static final int KeySword = 4;
    public static final int Axe = 5;
    public static final int Katana = 6;
    public static final int EmpoweredKeySword = 7;
    public static final int Shuriken = 8;
    public static final int RollingShuriken = 9;
    public static final int EarthSpear = 10;
    public static final int FlareGun = 11;
    public static final int Bomb = 12;
    public static final int Chakram = 13;
    public static final int Caltrops = 14;
    public static final int Pistol = 15;
    public static final int AnyMainWeapon = 16;
    public static final int AnySubWeapon = 17;
    public static final int AnyWeapon = 18;
    public static final int NotBreakable = 19;

    public Hitbox(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 12);
        setId((short)0x12);
        setX(x);
        setY(y);
    }

    public Hitbox(GameObject gameObject) {
        super(gameObject);
    }

    public void setVisual(int visual) {
        // visual 1:dust >1: star
        getArgs().set(0, (short)visual);
    }

    public void setHealth(int health) {
        getArgs().set(1, (short)0);
        getArgs().set(2, (short)health);
    }

    public void setHitCount(int health) {
        getArgs().set(1, (short)1);
        getArgs().set(2, (short)health);
    }

    public void setDirection(int direction) {
        getArgs().set(3, (short)direction);
    }

    public int getBreakableBy() {
        return getArgs().get(4);
    }

    public void setBreakableBy(int weaponType) {
        getArgs().set(4, (short)weaponType);
    }

    public void setBreakable(int direction, int weaponType) {
        getArgs().set(3, (short)direction);
        getArgs().set(4, (short)weaponType);
    }

    public void setUpdateOnlyForCorrectWeapon() {
        // 0-  break: update all 4.
        // wrongwep: update none.
        getArgs().set(5, (short)0);
    }

    public void setSplitUpdates() {
        // 1-  break: update 0,2
        // wrongwep: update 1,3
        getArgs().set(5, (short)1);
    }

    public void setHitboxWidth(int gTileWidth) {
        getArgs().set(6, (short)gTileWidth);
    }

    public void setHitboxHeight(int gTileHeight) {
        getArgs().set(7, (short)gTileHeight);
    }

    public void setDimensions(int gTileWidth, int gTileHeight) {
        setHitboxWidth(gTileWidth);
        setHitboxHeight(gTileHeight);
    }

    public void setSuccessSound(int successSound) {
        // Hit Success Sound Effect (-1 for silent)
        getArgs().set(8, (short)successSound);
    }

    public void setFailureSound(int failureSound) {
        // Hit Fail Sound Effect (-1 for silent)
        getArgs().set(9, (short)failureSound);
    }

    public void setSoundEffects(int successSound, int failureSound) {
        // Hit Fail Sound Effect (-1 for silent)
        setSuccessSound(successSound);
        setFailureSound(failureSound);
    }

    public void setDustDensity1(int dustDensity1) {
        // dust1 density 1
        getArgs().set(10, (short)dustDensity1);
    }

    public void setDustDensity2(int dustDensity2) {
        // dust2 density 2
        getArgs().set(11, (short)dustDensity2);
    }
}
