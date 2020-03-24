package lmr.randomizer.msd.object;

import java.util.ArrayList;
import java.util.List;

public final class AnimatedTile {
    public boolean animateInBoss;
    public final List<AnimatedTileFrame> frames;

    public AnimatedTile() {
        this(new ArrayList<>(), false);
    }

    public AnimatedTile(List<AnimatedTileFrame> frames, boolean animateInBoss) {
        this.frames = frames;
        this.animateInBoss = animateInBoss;
    }
}
