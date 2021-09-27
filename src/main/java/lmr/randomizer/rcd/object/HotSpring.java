package lmr.randomizer.rcd.object;

/**
 * 0xad Hotspring
 *     0 - Width
 *     1 - Height
 *     2 - FramesOfNoHeal
 *     3 - HealAmount
 */
public class HotSpring extends GameObject {
    public HotSpring(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 4);
        setId(ObjectIdConstants.HotSpring);
        setX(x);
        setY(y);
    }

    public HotSpring(GameObject gameObject) {
        super(gameObject);
    }

    public void setWidth(int gTileWidth) {
        getArgs().set(0, (short)gTileWidth);
    }

    public void setHeight(int gTileHeight) {
        getArgs().set(1, (short)gTileHeight);
    }

    public void setFramesOfNoHeal(int framesOfNoHeal) {
        getArgs().set(2, (short)framesOfNoHeal);
    }

    public int getHealAmount() {
        return getArgs().get(3);
    }

    public void setHealAmount(int healAmount) {
        getArgs().set(3, (short)healAmount);
    }
}
