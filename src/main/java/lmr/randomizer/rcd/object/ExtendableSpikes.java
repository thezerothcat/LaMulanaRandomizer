package lmr.randomizer.rcd.object;

/**
 * 0x96 Extending Spikes args Partial Reversed; Tested
 *     0 - Layer
 *     1 - Direction URDL
 *     2 - Width-Extended (unused)
 *     3 - Length-Extended
 *     4 - Activation Delay
 *     5 - Vi-extend * 100
 *     6 - dV-extend * 100
 *     7 - Vf-extend * 100
 *     8 - Update1Delay
 *     9 - RetractDelay
 *     10- Vi-retract * 100
 *     11- dV-retract * 100
 *     12- Vf-retract * 100
 *     13- Update2Delay
 *     14- GraphicSheet 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png 7=01effect.png Default=msd Room
 *     15- Room Number
 *     16- ImageX
 *     17- ImageY
 *     18- dx
 *     19- dy
 *     20- ExtendSound
 *     21- RetractSound
 *     22- DamageType 0%, 1hp
 *     23- Damage
 *     24- SideDamage?
 *     25- 0, *20*, 40
 */
public class ExtendableSpikes extends GameObject {
    public static final int Up = 0;
    public static final int Right = 1;
    public static final int Down = 2;
    public static final int Left = 3;

    public ExtendableSpikes(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 26);
        setId((short)0x96);
        setX(x);
        setY(y);
    }

    public void setLayer(int layer) {
        getArgs().set(0, (short)layer);
    }

    public void setDirection(int direction) {
        getArgs().set(1, (short)direction);
    }

    public void setExtendedWidth(int extendedWidth) {
        // Unused
        getArgs().set(2, (short)extendedWidth);
    }

    public void setExtendedLength(int extendedLength) {
        getArgs().set(3, (short)extendedLength);
    }

    public void setActivationDelay(int activationDelay) {
        getArgs().set(4, (short)activationDelay);
    }

    public void setArg5(int arg5) {
        getArgs().set(5, (short)arg5);
    }

    public void setArg6(int arg6) {
        getArgs().set(6, (short)arg6);
    }

    public void setArg7(int arg7) {
        getArgs().set(7, (short)arg7);
    }

    public void setUpdate1Delay(int update1Delay) {
        getArgs().set(8, (short)update1Delay);
    }

    public void setRetractDelay(int retractDelay) {
        getArgs().set(9, (short)retractDelay);
    }

    public void setArg10(int arg10) {
        getArgs().set(10, (short)arg10);
    }

    public void setArg11(int arg11) {
        getArgs().set(11, (short)arg11);
    }

    public void setArg12(int arg12) {
        getArgs().set(12, (short)arg12);
    }

    public void setUpdate2Delay(int update2Delay) {
        getArgs().set(13, (short)update2Delay);
    }

    public void setImageFile(int imageFile) {
        getArgs().set(14, (short)imageFile);
    }

    public void setImageFile(String imageFile) {
        // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png 7=01effect.png Default=msd Room
        // Image 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png

        if(imageFile == null) {
            setImageFile(-1); // msd Room
        }
        else if(imageFile.startsWith("map") && imageFile.endsWith("_1.png")) {
            setImageFile(0);
        }
        else if(imageFile.startsWith("eveg") && imageFile.endsWith(".png")) {
            setImageFile(1);
        }
        else if("00prof.png".equals(imageFile)) {
            setImageFile(2);
        }
        else if("02comenemy.png".equals(imageFile)) {
            setImageFile(3);
        }
        else if("00item.png".equals(imageFile)) {
            setImageFile(4);
        }
        else if("01menu.png".equals(imageFile)) {
            setImageFile(5);
        }
        else if("01effect.png".equals(imageFile)) {
            setImageFile(7);
        }
        else {
            // todo: throw error for invalid option
        }
    }

    public void setRoomNumber(int roomNumber) {
        getArgs().set(15, (short)roomNumber);
    }

    public void setImageX(int imageX) {
        getArgs().set(16, (short)imageX);
    }

    public void setImageY(int imageY) {
        getArgs().set(17, (short)imageY);
    }

    public void setDx(int dx) {
        getArgs().set(18, (short)dx);
    }

    public void setDy(int dy) {
        getArgs().set(19, (short)dy);
    }

    public void setExtendSound(int extendSound) {
        getArgs().set(20, (short)extendSound);
    }

    public void setRetractSound(int retractSound) {
        getArgs().set(21, (short)retractSound);
    }

    public void setDamageType(int damageType) {
        getArgs().set(22, (short)damageType);
    }

    public void setDamage(int damage) {
        getArgs().set(23, (short)damage);
    }

    public void setPercentDamage(int damage) {
        setDamageType(0);
        setDamage(damage);
    }

    public void setFlatDamage(int damage) {
        setDamageType(1);
        setDamage(damage);
    }

    public void setSideDamage(int sideDamage) {
        getArgs().set(24, (short)sideDamage);
    }

    public void setArg25(int arg25) {
        getArgs().set(25, (short)arg25);
    }
}
