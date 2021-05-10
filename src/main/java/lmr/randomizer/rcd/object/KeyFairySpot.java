package lmr.randomizer.rcd.object;

/**
 * 0xa7 KeyFairy Spot NEEDS RESEARCH
 *     0 - Activation Object
 *         0 - Key Fairy
 *         1 - Lizard Man
 *         (others exist)
 *     1 - dX
 *     2 - dY
 */
public class KeyFairySpot extends GameObject {
    public KeyFairySpot(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 3);
        setId((short)0xa7);
        setX(x);
        setY(y);
    }

    public void setActivationObject(int activationObject) {
        // 0 - Key Fairy
        // 1 - Lizard Man
        // (others exist)
        getArgs().set(0, (short)activationObject);
    }

    public void setWidth(int pixelWidth) {
        getArgs().set(1, (short)pixelWidth);
    }

    public void setHeight(int pixelHeight) {
        getArgs().set(2, (short)pixelHeight);
    }
}
