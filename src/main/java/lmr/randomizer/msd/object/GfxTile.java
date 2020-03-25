package lmr.randomizer.msd.object;

import java.util.Objects;

public final class GfxTile {
    public static final GfxTile EMPTY = new GfxTile();

    public final short gfxIdx;
    public final GfxTileBlendMode blendMode;
    public final boolean flipHorizontal; // TODO: determine if this is unclear after testing all flip + rotation combos
    public final byte rotations;

    public GfxTile() { this(0, GfxTileBlendMode.EMPTY, false, 0); }

    public GfxTile(int gfxIdx) {
        this(gfxIdx, GfxTileBlendMode.NORMAL, false, 0);
    }

    public GfxTile(int gfxIdx, GfxTileBlendMode blendMode) { this(gfxIdx, blendMode, false, 0); }

    public GfxTile(int gfxIdx, GfxTileBlendMode blendMode, boolean flipHorizontal, int rotations) {
        this.gfxIdx = (short) gfxIdx;
        this.blendMode = blendMode;
        this.flipHorizontal = flipHorizontal;
        this.rotations = (byte) rotations;
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
