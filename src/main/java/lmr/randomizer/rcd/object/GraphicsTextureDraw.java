package lmr.randomizer.rcd.object;

/**
 * 0x93 args: Texture Draw Partially Reversed Tested
 *      0 - Layer
 *      1 - Image 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
 *      2 - Imagex
 *      3 - Imagey
 *      4 - dx
 *      5 - dy
 *      6 - 0:act as if animation already played
 *          1:allow animation
 *          2:..?
 *      7 - Animation Frames
 *      8 - Pause Frames
 *      9 - Repeat Count (<1 is forever)
 *     10 - Hittile to fill with
 *     11 - Entry Effect
 *          0-static (default)
 *          1-Fade
 *          2-Animate; show LAST frame.
 *
 *     12 - Exit Effect
 *          0-disallow animation
 *          1-Fade
 *          2-Default
 *          3-Large Break on Completion/Failure
 *          4-Default
 *          5-Animate on failure, frame 1 on success
 *          6-Break Glass on Completion/Failure
 *          Default: Disappear Instantly
 *     13 - Cycle Colors t/f
 *     14 - Alpha/frame
 *     15 - Max Alpha
 *     16 - R/frame
 *     17 - Max R
 *     18 - G/frame
 *     19 - Max G
 *     20 - B/frame
 *     21 - Max B
 *     22 - blend. 0=Normal 1=add 2= ... 14=
 *     23 - not0?
 *
 *
 *     arg13 != 0:
 *         (255-arg15)/arg14   A
 *         arg17/arg16         R
 *         arg19/arg18         G
 *         arg21/arg20         B
 *         if second is 0, then 0 is the result
 */
public class GraphicsTextureDraw extends GameObject {
    public static int EntryEffect_FadeIn = 1;
    public static int ExitEffect_FadeOut = 1;
    public static int ExitEffect_Animate = 5;

    public GraphicsTextureDraw(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 24);
        setId((short)0x93);
        setX(x);
        setY(y);
    }

    public void setLayer(int layer) {
        getArgs().set(0, (short)layer);
    }

    public void setImageFile(int imageFile) {
        getArgs().set(1, (short)imageFile);
    }

    public void setImageFile(String imageFile) {
        // Image 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        if(imageFile.startsWith("map") && imageFile.endsWith("_1.png")) {
            getArgs().set(1, (short)0);
        }
        else if(imageFile.startsWith("eveg") && imageFile.endsWith(".png")) {
            getArgs().set(1, (short)1);
        }
        else if("00prof.png".equals(imageFile)) {
            getArgs().set(1, (short)2);
        }
        else if("02comenemy.png".equals(imageFile)) {
            getArgs().set(1, (short)3);
        }
        else if("00item.png".equals(imageFile)) {
            getArgs().set(1, (short)4);
        }
        else if("01menu.png".equals(imageFile)) {
            getArgs().set(1, (short)5);
        }
        else if("01effect.png".equals(imageFile)) {
            getArgs().set(1, (short)-1);
        }
        else {
            // todo: throw error for invalid option
        }
    }

    public void setImageX(int imageX) {
        // X-component of top left corner for where to begin drawing, on the image file
        getArgs().set(2, (short)imageX);
    }

    public void setImageY(int imageY) {
        // Y-component of top left corner for where to begin drawing, on the image file
        getArgs().set(3, (short)imageY);
    }

    public void setImagePosition(int x, int y) {
        setImageX(x);
        setImageY(y);
    }

    public void setImageWidth(int imageWidthPixels) {
        getArgs().set(4, (short)imageWidthPixels);
    }

    public void setImageHeight(int imageHeightPixels) {
        getArgs().set(5, (short)imageHeightPixels);
    }

    public void setImageSize(int imageWidthPixels, int imageHeightPixels) {
        setImageWidth(imageWidthPixels);
        setImageHeight(imageHeightPixels);
    }

    public void setAnimationMode(int animationMode) {
        // 0:act as if animation already played
        // 1:allow animation
        // 2:..?
        getArgs().set(6, (short)animationMode);
    }

    public void setAnimationFrames(int animationFrames) {
        getArgs().set(7, (short)animationFrames);
    }

    public void setPauseFrames(int pauseFrames) {
        getArgs().set(8, (short)pauseFrames);
    }

    public void setRepeatCount(int repeatCount) {
        // <1 is forever
        getArgs().set(9, (short)repeatCount);
    }

    public void setAnimation(int animationMode, int animationFrames, int pauseFrames, int repeatCount) {
        setAnimationMode(animationMode);
        setAnimationFrames(animationFrames);
        setPauseFrames(pauseFrames);
        setRepeatCount(repeatCount);
    }

    public void setCollision(HitTile hitTile) {
        // Hittile to fill with
        getArgs().set(10, hitTile.getId());
    }

    public void setEntryEffect(int entryEffect) {
        // 0-static (default)
        // 1-Fade
        // 2-Animate; show LAST frame.
        getArgs().set(11, (short)entryEffect);
    }

    public void setExitEffect(int exitEffect) {
        // 0-disallow animation
        // 1-Fade
        // 2-Default
        // 3-Large Break on Completion/Failure
        // 4-Default
        // 5-Animate on failure, frame 1 on success
        // 6-Break Glass on Completion/Failure
        // Default: Disappear Instantly
        getArgs().set(12, (short)exitEffect);
    }

    public void setCycleColors(boolean cycleColors) {
        // arg13 != 0:
        //     (255-arg15)/arg14   A
        //     arg17/arg16         R
        //     arg19/arg18         G
        //     arg21/arg20         B
        //     if second is 0, then 0 is the result
        getArgs().set(13, (short)(cycleColors ? 1 : 0));
    }

    public void setAlphaPerFrame(int alphaPerFrame) {
        getArgs().set(14, (short)alphaPerFrame);
    }

    public void setMaxAlpha(int maxAlpha) {
        getArgs().set(15, (short)maxAlpha);
    }

    public void setRedPerFrame(int redPerFrame) {
        getArgs().set(16, (short)redPerFrame);
    }

    public void setMaxRed(int maxRed) {
        getArgs().set(17, (short)maxRed);
    }

    public void setGreenPerFrame(int greenPerFrame) {
        getArgs().set(18, (short)greenPerFrame);
    }

    public void setMaxGreen(int maxGreen) {
        getArgs().set(19, (short)maxGreen);
    }

    public void setRGBAPerFrame(int red, int green, int blue, int alpha) {
        setRedPerFrame(red);
        setGreenPerFrame(green);
        setBluePerFrame(blue);
        setAlphaPerFrame(alpha);
    }

    public void setRGBAMax(int red, int green, int blue, int alpha) {
        setMaxRed(red);
        setMaxGreen(green);
        setMaxBlue(blue);
        setMaxAlpha(alpha);
    }

    public void setBluePerFrame(int bluePerFrame) {
        getArgs().set(20, (short)bluePerFrame);
    }

    public void setMaxBlue(int maxBlue) {
        getArgs().set(21, (short)maxBlue);
    }

    public void setBlendMode(int blendMode) {
        // 0=Normal 1=add 2= ... 14=
        getArgs().set(22, (short)blendMode);
    }

    public void setArg23(int arg23) {
        // not0?
        getArgs().set(23, (short)arg23);
    }
}
