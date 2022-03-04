package lmr.randomizer.rcd.object;

/**
 * 0xb9 TextureDraw Block
 *     Cannot weight block buttons.
 *     0 - Texture
 *     1 - U
 *     2 - V
 *     3 - dX
 *     4 - dY
 *     5 - UNUSED
 *     6 - Damage
 *     7 - When landing,
 *         0 - do nothing
 *         1 - disappear & update
 *         default - break & update
 */
public class PushableGraphic extends GameObject {
    public static final int LandingBehavior_DoNothing = 0;
    public static final int LandingBehavior_DisappearAndUpdate = 1;
    public static final int LandingBehavior_BreakAndUpdate = 2;

    public PushableGraphic(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 8);
        setId(ObjectIdConstants.PushableGraphic);
        setX(x);
        setY(y);
    }

    public void setTexture(int texture) {
        // Seems mostly 0?
        getArgs().set(0, (short)texture);
    }

    public void setImageX(int imageX) {
        // X-component of top left corner for where to begin drawing, on the image file
        getArgs().set(1, (short)imageX);
    }

    public void setImageY(int imageY) {
        // Y-component of top left corner for where to begin drawing, on the image file
        getArgs().set(2, (short)imageY);
    }

    public void setWidth(int pixelWidth) {
        getArgs().set(3, (short)pixelWidth);
    }

    public void setHeight(int pixelHeight) {
        getArgs().set(4, (short)pixelHeight);
    }

    public void setArg5(int arg5) {
        getArgs().set(5, (short)arg5);
    }

    public void setDamage(int damage) {
        getArgs().set(6, (short)damage);
    }

    public void setLandingBehavior(int landingBehavior) {
        getArgs().set(7, (short)landingBehavior);
    }
}
