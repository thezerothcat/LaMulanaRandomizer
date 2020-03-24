package lmr.randomizer.msd.object;

public enum GfxTileBlendMode {
    EMPTY(0),
    NORMAL(1),
    ADD(2),
    MULTIPLY(3);

    public final byte value;

    GfxTileBlendMode(int value) {
        this.value = (byte) value;
    }
}
