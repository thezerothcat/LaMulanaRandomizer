package lmr.randomizer.rcd.object;

/**
 * 0x91 Fairy Point: Reversed Untested
 *     0 - 0 normal 1 Health 2 Attack 3 Luck 4 Key
 */
public class FairyPoint extends GameObject {
    public static int ANY_FAIRY = 0;
    public static int HEALTH_FAIRY = 1;
    public static int ATTACK_FAIRY = 2;
    public static int TREASURE_FAIRY = 3;
    public static int KEY_FAIRY = 4;

    public FairyPoint(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 1);
        setId(ObjectIdConstants.FairyPoint);
        setX(x);
        setY(y);
    }

    public FairyPoint(GameObject objectToCopy) {
        super(objectToCopy);
    }

    public void setFairyType(int fairyType) {
        getArgs().set(0, (short)fairyType);
    }
}
