package lmr.randomizer.rcd.object;

/**
 * 0x00 POT
 *     0 - Drop type
 *     1 - Quantity
 *     2 - Byte
 *     3 - Bit (> 8 ok)
 *     4 - Pot Type
 *     5 - Hit Sound
 *     6 - Break Sound
 *     7 - Land Sound
 *     8 - Pitch Shift
 *     Pitch shift is 48 000 Hz + shift * 10 Hz. That is, a shift of -500 will cause the sound effects to play at 43kHz, and a shift of 499 will cause the sound effects to play at 52 990 Hz. For pots, the pitch shift is varied randomly with each time a sound is played.
 */
public class Pot extends GameObject {
    public Pot(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 9);
        setId(ObjectIdConstants.Pot);
        setX(x);
        setY(y);
    }

    public void setDropType(DropType dropType) {
        getArgs().set(0, dropType.getValue());
    }

    public void setDropQuantity(int quantity) {
        getArgs().set(1, (short)quantity);
    }

    public void setDrops(DropType dropType, int quantity) {
        setDropType(dropType);
        setDropQuantity(quantity);
    }

    public void setFlagByte(int flagByte) {
        getArgs().set(2, (short)flagByte);
    }

    public void setFlagBit(int flagBit) {
        getArgs().set(3, (short)flagBit);
    }

    public void setFlag(int flagByte, int flagBit) {
        getArgs().set(2, (short)flagByte);
        getArgs().set(3, (short)flagBit);
    }

    public void setPotGraphic(PotGraphic potGraphic) {
        getArgs().set(4, potGraphic.getGraphic());
    }

    public void setHitSoundEffect(int soundEffect) {
        getArgs().set(5, (short)soundEffect);
    }

    public void setBreakSoundEffect(int soundEffect) {
        getArgs().set(6, (short)soundEffect);
    }

    public void setLandSoundEffect(int soundEffect) {
        getArgs().set(7, (short)soundEffect);
    }

    public void setSoundEffects(int hitSoundEffect, int breakSoundEffect, int landSoundEffect) {
        setHitSoundEffect(hitSoundEffect);
        setBreakSoundEffect(breakSoundEffect);
        setLandSoundEffect(landSoundEffect);
    }

    public void setPitchShift(int pitchShift) {
        getArgs().set(8, (short)pitchShift);
    }
}
