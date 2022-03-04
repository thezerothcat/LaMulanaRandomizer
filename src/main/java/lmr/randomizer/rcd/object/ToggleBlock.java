package lmr.randomizer.rcd.object;

/**
 * 0xb0 block-megaman
 *     Update0 Equality activates.
 *     0 - HitTileWidth
 *     1 - HitTileHeight
 *     2 - FramesUsed
 *     3 - FramesNoAnimation
 *     4 - Graphic TODO
 *     5 - U
 *     6 - V
 *     7 - Blend Mode
 *     8 - Damage
 *     9 - Activation Sound Effect
 *     10- Deactivation Sound Effect
 */
public class ToggleBlock extends GameObject {
    // Unconfirmed, but likely
    public static int ImageFile_map = 0;
    public static int ImageFile_eveg = 1; // Confirmed
    public static int ImageFile_00prof = 2;
    public static int ImageFile_02comenemy = 3;
    public static int ImageFile_00item = 4;
    public static int ImageFile_01menu = 5;
    public static int ImageFile_00item_alt = 6;
    public static int ImageFile_01effect = 7;

    public ToggleBlock(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 11);
        setId(ObjectIdConstants.ToggleBlock);
        setX(x);
        setY(y);
        setDefaults();
    }

    public void setDefaults() {
        setWidth(4);
        setHeight(4);
        setFramesUsed(5);
        setFramesNoAnimation(4);
        setImageFile(ImageFile_eveg);
        setImagePosition(440, 160);
        setBlendMode(0);
        setDamage(1);
        setActivationSound(41);
        setDeactivationSound(41);
    }

    public void setWidth(int hitTileWidth) {
        getArgs().set(0, (short)hitTileWidth);
    }

    public void setHeight(int hitTileHeight) {
        getArgs().set(1, (short)hitTileHeight);
    }

    public void setFramesUsed(int framesUsed) {
        getArgs().set(2, (short)framesUsed);
    }

    public void setFramesNoAnimation(int framesNoAnimation) {
        getArgs().set(3, (short)framesNoAnimation);
    }

    public void setImageFile(int imageFile) {
        getArgs().set(4, (short)imageFile);
    }

    public void setImageFile(String imageFile) {
        // Image 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        if(imageFile.startsWith("map") && imageFile.endsWith("_1.png")) {
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
            setImageFile(-1);
        }
        else {
            // todo: throw error for invalid option
        }
    }

    public void setImageX(int imageX) {
        // X-component of top left corner for where to begin drawing, on the image file
        getArgs().set(5, (short)imageX);
    }

    public void setImageY(int imageY) {
        // Y-component of top left corner for where to begin drawing, on the image file
        getArgs().set(6, (short)imageY);
    }

    public void setImagePosition(int x, int y) {
        setImageX(x);
        setImageY(y);
    }

    public void setBlendMode(int blendMode) {
        getArgs().set(7, (short)blendMode);
    }

    public void setDamage(int damage) {
        getArgs().set(8, (short)damage);
    }

    public void setActivationSound(int activationSound) {
        getArgs().set(9, (short)activationSound);
    }

    public void setDeactivationSound(int deactivationSound) {
        getArgs().set(10, (short)deactivationSound);
    }
}
