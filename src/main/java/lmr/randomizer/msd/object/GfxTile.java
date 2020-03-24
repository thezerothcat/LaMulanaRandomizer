package lmr.randomizer.msd.object;

import java.util.Objects;

public final class GfxTile {
    public static final GfxTile EMPTY = new GfxTile((short) 0);

    public final short gfxIdx;
    public final GfxTileBlendMode blendMode;
    public final boolean flipHorizontal; // TODO: determine if this is unclear after testing all flip + rotation combos
    public final byte rotations;

    public GfxTile(int gfxIdx) {
        this(gfxIdx, GfxTileBlendMode.NORMAL, false, (byte) 0);
    }

    public GfxTile(int gfxIdx, GfxTileBlendMode blendMode, boolean flipHorizontal, byte rotations) {
        this.gfxIdx = (short) gfxIdx;
        this.blendMode = blendMode;
        this.flipHorizontal = flipHorizontal;
        this.rotations = rotations;
    }

    public boolean equals(GfxTile t) {
        return (
            gfxIdx == t.gfxIdx
            && blendMode == t.blendMode
            && flipHorizontal == t.flipHorizontal
            && rotations == t.rotations
        );
    }

    public int hashCode() {
        return Objects.hash(gfxIdx, blendMode, flipHorizontal, rotations);
    }
}
