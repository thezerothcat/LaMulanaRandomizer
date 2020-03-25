package lmr.randomizer.msd.object;

import java.util.ArrayList;
import java.util.List;

public final class LayerGroup {
    public final List<Layer> layers; // first in front, last in back
    public short fallbackWidth;
    public short fallbackHeight;

    public LayerGroup() {
        this(new ArrayList<>());
    }

    public LayerGroup(List<Layer> layers) {
        this.layers = layers;
    }

    public short width() {
        return layers.isEmpty() ? fallbackWidth : layers.get(0).width();
    }

    public short height() {
        return layers.isEmpty() ? fallbackHeight : layers.get(0).height();
    }
}
