package lmr.randomizer.rcd.object;

/**
 *     state 0 sliding
 *           1 after playing, tests still pass (no slide)
 *           2 after playing, tests stopped passing (no slide)
 *          10 delay before playing (initial)0
 *             tests pass -> 10 -> play after arg[9] frames -> 1 -> tests stop passing -> 2 -> tests start passing -> back to 10
 *             tests pass -> 10 -> play after arg[9] frames with slide -> 0 -> stop
 *             sound effect has slide if any parameter's final value is different
 *     ARGUMENTS
 *     0 -  sound effect    (as the file number)
 *     1 -  volume  initial (0-127)
 *     2 -  balance initial (0-127, 64 is centered)
 *     3 -  pitch   initial (see below)
 *     4 -  volume  final   (see below)
 *     5 -  balance final
 *     6 -  pitch   final
 *     7 -  voice priority (higher is better, most game sound effects get 15)
 *     8 -  UNUSED????
 *     9 -  frames to wait before playing
 *     10-  controller rumble (bool)
 *     11-  ??? rumble strength
 *     12-  volume  slide frames (see below)
 *     13-  balance slide frames (see below)
 *     14-  pitch   slide frames (see below)
 *     INTERNAL VALUES
 *     flt  0  volume  current
 *     flt  1  balance current
 *     flt  2  pitch   current
 *     flt  3  volume  final
 *     flt  4  balance final
 *     flt  5  pitch   final
 *     flt  6  volume  delta
 *     flt  7  balance delta
 *     flt  8  pitch   delta
 *     loc  0 frames waiting to play
 *     loc  1 playing sound index
 *
 *     If the initial and final values for volume, pitch or balance are not the same, then the sound effect starts out at the initial value, then over the course of the relevant number of slide frames, fades to the final value.
 *     For example, if volume initial is 0 and volume final is 120 and volume slide is 60, then every frame for 60 frames, the sound will get 2 louder.
 *
 *     Pitch is the sample rate of the sound effect. That is, the higher the pitch, the faster and higher the sound plays. The lower it is, the slower and lower the sound plays. The sample rate is 48 000Hz + pitch * 10Hz. More practically speaking, compare the sound produced by breaking pots in Tower of Ruin to breaking pots in other locations. The pot sound effects in Ruin have a pitch of (approximately) -500.
 */
public class SoundEffect extends GameObject {
    public static final int HardmodeActivated = 0;
    public static final int ItemCollected = 39;
    public static final int ShellHornSuccess = 30;
    public static final int ShellHornFailure = 80;

    public SoundEffect(ObjectContainer objectContainer) {
        super(objectContainer, 15);
        setId(ObjectIdConstants.SoundEffect);
        setX(-1);
        setY(-1);
    }

    public void setSoundEffect(int fileNumber) {
        // Plays "se**.wav" from La-Mulana/data/sound (install directory), where ** is the two-digit hex value for this arg
        getArgs().set(0, (short)fileNumber);
    }

    public void setInitialVolume(int initialVolume) {
        // 0-127
        getArgs().set(1, (short)initialVolume);
    }

    public void setInitialBalance(int initialBalance) {
        // 0-127, 64 is centered
        getArgs().set(2, (short)initialBalance);
    }

    public void setInitialPitch(int initialPitch) {
        getArgs().set(3, (short)initialPitch);
    }

    public void setFinalVolume(int finalVolume) {
        getArgs().set(4, (short)finalVolume);
    }

    public void setFinalBalance(int finalBalance) {
        getArgs().set(5, (short)finalBalance);
    }

    public void setFinalPitch(int finalPitch) {
        getArgs().set(6, (short)finalPitch);
    }

    public void setPriority(int priority) {
        // "voice priority" according to documentation - higher is better, most game sound effects get 15
        getArgs().set(7, (short)priority);
    }

    public void setArg8(int arg8) {
        // UNUSED????
        getArgs().set(8, (short)arg8);
    }

    public void setFramesDelay(int framesDelay) {
        // frames to wait before playing
        getArgs().set(9, (short)framesDelay);
    }

    public void setControllerRumble(boolean controllerRumble) {
        getArgs().set(10, (short)(controllerRumble ? 1 : 0));
    }

    public void setRumbleStrength(int rumbleStrength) {
        // ??? rumble strength
        getArgs().set(11, (short)rumbleStrength);
    }

    public void setVolumeSlideFrames(int volumeSlideFrames) {
        getArgs().set(12, (short)volumeSlideFrames);
    }

    public void setBalanceSlideFrames(int balanceSlideFrames) {
        getArgs().set(13, (short)balanceSlideFrames);
    }

    public void setPitchSlideFrames(int pitchSlideFrames) {
        getArgs().set(14, (short)pitchSlideFrames);
    }

    public void setVolume(int initialVal, int finalVal, int slideFrames) {
        setInitialVolume(initialVal);
        setFinalVolume(finalVal);
        setVolumeSlideFrames(slideFrames);
    }

    public void setBalance(int initialVal, int finalVal, int slideFrames) {
        setInitialBalance(initialVal);
        setFinalBalance(finalVal);
        setBalanceSlideFrames(slideFrames);
    }

    public void setPitch(int initialVal, int finalVal, int slideFrames) {
        setInitialPitch(initialVal);
        setFinalPitch(finalVal);
        setPitchSlideFrames(slideFrames);
    }

    public void setVolumeBalancePitch(int volume, int balance, int pitch) {
        setVolume(volume, volume, 0);
        setBalance(balance, balance, 0);
        setPitch(pitch, pitch, 0);
    }
}
