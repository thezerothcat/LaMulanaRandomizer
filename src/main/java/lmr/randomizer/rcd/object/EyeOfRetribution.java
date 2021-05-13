package lmr.randomizer.rcd.object;

import lmr.randomizer.FlagConstants;

/**
 * 0x95 Eye of Divine Retribution args Not Reversed, Tested
 *     0 - 10  - flag
 *     1 - 0   - 0:arg2 is %hp 1:arg2 is hp
 *     2 - 30  - damage
 */
public class EyeOfRetribution extends GameObject {
    public EyeOfRetribution(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 3);
        setId((short)0x95);
        setX(x);
        setY(y);
        setDefaults();
    }

    public void setDefaults() {
        setTriggerFlag(FlagConstants.SCREEN_FLAG_A);
        setPercentDamage(30);
    }

    public void setTriggerFlag(int triggerFlag) {
        getArgs().set(0, (short)triggerFlag);
    }

    public void setPercentDamage(int damage) {
        getArgs().set(1, (short)0);
        getArgs().set(2, (short)damage);
    }

    public void setFlatDamage(int damage) {
        getArgs().set(1, (short)1);
        getArgs().set(2, (short)damage);
    }
}
