package lmr.randomizer.rcd.object;

/**
 * 0x11 MovingTextureTileFill (crusher lel)
 *
 *     0 - UNUSED
 *     1 - Direction URDL
 *     2 - Width (dX for U,D. dY for R,L.) Measured in gtiles.
 *     3 - Length (dY for U,D. dX for R,L.) Measured in gtiles.
 *     4 - Activation Delay -- frames after update 0 is equal before extend
 *     5 - Vi-extend * 100 -- initial velocity (px/frame)
 *     6 - dV-extend * 100 -- acceleration (px/frame/frame)
 *     7 - Vf-extend * 100 -- maximum velocity
 *     8 - Update1Delay -- frames after fully extending before performing update 1
 *     9 - RetractDelay -- frames after update 0 doesn't pass before retract
 *     10- Vi-retract * 100
 *     11- dV-retract * 100
 *     12- Vf-retract * 100
 *     13- Update2Delay -- frames after fully retracting before performing update 2
 *     14- GraphicSheet
 *         <2=mapxx_1.png
 *         2=3=evegxx.png
 *         >4=msd Room
 *     15- Room Number
 *     16- Image X
 *     17- Image Y
 *     18- Image dX
 *     19- Image dY
 *     20- Tile Fill switch
 *         0: 0x80 wall + dynamic object
 *         1: 0x20 waterfall
 *         2: 0x05 water
 *         3: 0x06 water flow UP
 *         4: 0x08 water flow DOWN
 *         5: 0x09 water flow LEFT
 *         6: 0x07 water flow RIGHT
 *         7: 0x20 waterfall
 *         8: 0x10 lava
 *         9: 0x11 lava flow UP
 *         10:0x13 lava flow DOWN
 *         11:0x14 lava flow LEFT
 *         default:0x12 lava flow RIGHT
 *     21- layer
 *     22- Min Length
 *     23- Active Sound    (as sfx file number)
 *     24- RepeatDelay     (number of frames before playing sound again)
 *     25- Volume          (0-127)
 *     26- Pitch           (sample rate. Sound effect is played at 48 000Hz + pitch * 10Hz. Negative is ok.)
 *     27- Retract Sound   (as above.)
 *     28- Repeat Delay    (as above.)
 *     29- Volume          (as above.)
 *     30- Pitch           (as above.)
 */
public class Crusher extends GameObject {
    public static final int ExtendDirection_Up = 0;
    public static final int ExtendDirection_Right = 1;
    public static final int ExtendDirection_Down = 2;
    public static final int ExtendDirection_Left = 3;

    public static final short ImageFile_map = 0;
    public static final short ImageFile_eveg = 2;

    public static final short HitTile_Solid = 0;
    public static final short HitTile_Waterfall = 1;
    public static final short HitTile_Water = 2;
    public static final short HitTile_Water_FlowUp = 3;
    public static final short HitTile_Water_FlowDown = 4;
    public static final short HitTile_Water_FlowLeft = 5;
    public static final short HitTile_Water_FlowRight = 6;
//    public static final short HitTile_Waterfall = 7;
    public static final short HitTile_Lava = 8;
    public static final short HitTile_Lava_FlowUp = 9;
    public static final short HitTile_Lava_FlowDown = 10;
    public static final short HitTile_Lava_FlowLeft = 11;
    public static final short HitTile_Lava_FlowRight = 12; // Anything > 12 will do

    public Crusher(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 31);
        setId(ObjectIdConstants.Crusher);
        setX(x);
        setY(y);
    }

    public Crusher(GameObject gameObject) {
        super(gameObject);
    }

    public void setArg0(int visual) {
        getArgs().set(0, (short)visual);
    }

    public void setExtendDirection(int extendDirection) {
        getArgs().set(1, (short)extendDirection);
    }

    public void setWidth(int gTileWidth) {
        // The dimension that doesn't change.
        getArgs().set(2, (short)gTileWidth);
    }

    public void setExtendedLength(int gTileWidth) {
        getArgs().set(3, (short)gTileWidth);
    }

    public void setExtendDelayFrames(int extendDelayFrames) {
        getArgs().set(4, (short)extendDelayFrames);
    }

    public void setExtendInitialVelocity(int extendInitialVelocity) {
        getArgs().set(5, (short)extendInitialVelocity);
    }

    public void setExtendAcceleration(int extendAcceleration) {
        getArgs().set(6, (short)extendAcceleration);
    }

    public void setExtendMaxVelocity(int maxVelocity) {
        getArgs().set(7, (short)maxVelocity);
    }

    public void setExtend(int extendDelayFrames, int extendInitialVelocity, int extendAcceleration, int extendMaxVelocity) {
        setExtendDelayFrames(extendDelayFrames);
        setExtendInitialVelocity(extendInitialVelocity);
        setExtendAcceleration(extendAcceleration);
        setExtendMaxVelocity(extendMaxVelocity);
    }

    public void setUpdate1DelayFrames(int update1DelayFrames) {
        getArgs().set(8, (short)update1DelayFrames);
    }

    public void setRetractDelayFrames(int retractDelayFrames) {
        getArgs().set(9, (short)retractDelayFrames);
    }

    public void setRetractInitialVelocity(int retractInitialVelocity) {
        getArgs().set(10, (short)retractInitialVelocity);
    }

    public void setRetractAcceleration(int retractAcceleration) {
        getArgs().set(11, (short)retractAcceleration);
    }

    public void setRetractMaxVelocity(int retractMaxVelocity) {
        getArgs().set(12, (short)retractMaxVelocity);
    }

    public void setRetract(int retractDelayFrames, int retractInitialVelocity, int retractAcceleration, int retractMaxVelocity) {
        setRetractDelayFrames(retractDelayFrames);
        setRetractInitialVelocity(retractInitialVelocity);
        setRetractAcceleration(retractAcceleration);
        setRetractMaxVelocity(retractMaxVelocity);
    }

    public void setUpdate2DelayFrames(int update2DelayFrames) {
        getArgs().set(13, (short)update2DelayFrames);
    }

    public void setImageFile(int imageFile) {
        getArgs().set(14, (short)imageFile);
    }

    public void setRoomNumber(int msdRoomNumber) {
        getArgs().set(15, (short)msdRoomNumber);
    }

    public void setImageX(int imageX) {
        // X-component of top left corner for where to begin drawing, on the image file
        getArgs().set(16, (short)imageX);
    }

    public void setImageY(int imageY) {
        // Y-component of top left corner for where to begin drawing, on the image file
        getArgs().set(17, (short)imageY);
    }

    public void setImagePosition(int x, int y) {
        setImageX(x);
        setImageY(y);
    }

    public void setImageWidth(int imageWidthPixels) {
        // NOTE: Draws upward
        getArgs().set(18, (short)imageWidthPixels);
    }

    public void setImageHeight(int imageHeightPixels) {
        getArgs().set(19, (short)imageHeightPixels);
    }

    public void setImageSize(int imageWidthPixels, int imageHeightPixels) {
        setImageWidth(imageWidthPixels);
        setImageHeight(imageHeightPixels);
    }

    public void setCrusherCollision(int crusherCollisionId) {
        getArgs().set(20, (short)crusherCollisionId);
    }

    public void setLayer(int layer) {
        getArgs().set(21, (short)layer);
    }

    public void setMinLength(int minLength) {
        getArgs().set(22, (short)minLength);
    }

    public void setExtendSound(int extendSound) {
        getArgs().set(23, (short)extendSound);
    }

    public void setExtendSoundRepeatFrames(int extendSoundRepeatFrames) {
        getArgs().set(24, (short)extendSoundRepeatFrames);
    }

    public void setExtendSoundVolume(int extendSoundVolume) {
        getArgs().set(25, (short)extendSoundVolume);
    }

    public void setExtendSoundPitch(int extendSoundPitch) {
        getArgs().set(26, (short)extendSoundPitch);
    }

    public void setExtendSound(int extendSound, int extendSoundRepeatFrames, int extendSoundVolume, int extendSoundPitch) {
        setExtendSound(extendSound);
        setExtendSoundRepeatFrames(extendSoundRepeatFrames);
        setExtendSoundVolume(extendSoundVolume);
        setExtendSoundPitch(extendSoundPitch);
    }

    public void setRetractSound(int retractSound) {
        getArgs().set(27, (short)retractSound);
    }

    public void setRetractSoundRepeatFrames(int retractSoundRepeatFrames) {
        getArgs().set(28, (short)retractSoundRepeatFrames);
    }

    public void setRetractSoundVolume(int retractSoundVolume) {
        getArgs().set(29, (short)retractSoundVolume);
    }

    public void setRetractSoundPitch(int retractSoundPitch) {
        getArgs().set(30, (short)retractSoundPitch);
    }

    public void setRetractSound(int retractSound, int retractSoundRepeatFrames, int retractSoundVolume, int retractSoundPitch) {
        setRetractSound(retractSound);
        setRetractSoundRepeatFrames(retractSoundRepeatFrames);
        setRetractSoundVolume(retractSoundVolume);
        setRetractSoundPitch(retractSoundPitch);
    }
}
