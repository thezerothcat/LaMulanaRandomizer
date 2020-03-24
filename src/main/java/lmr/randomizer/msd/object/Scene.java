package lmr.randomizer.msd.object;

import java.util.List;

public class Scene {
    public boolean useBossGfx;
    public byte primeLayerGroupIdx;
    public final Collision collision;
    public final List<LayerGroup> layerGroups;
    public lmr.randomizer.rcd.object.Room rcdRoom; // for linking convenience

    public Scene(Collision collision, List<LayerGroup> layerGroups) {
        this.collision = collision;
        this.layerGroups = layerGroups;
    }

    public LayerGroup primeGroup() {
        return primeLayerGroupIdx < layerGroups.size() ? layerGroups.get(primeLayerGroupIdx) : null;
    }

    public short width() {
        var primeGroup = primeGroup();
        return (primeGroup == null) ? -1 : primeGroup.width();
    }

    public short height() {
        var primeGroup = primeGroup();
        return (primeGroup == null) ? -1 : primeGroup.height();
    }
}
