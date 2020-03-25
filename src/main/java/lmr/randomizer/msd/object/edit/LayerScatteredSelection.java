package lmr.randomizer.msd.object.edit;

import lmr.randomizer.msd.object.GfxTile;
import lmr.randomizer.msd.object.Layer;
import lmr.randomizer.msd.tilemap.ScatteredSelection;

import java.awt.*;
import java.util.List;

public class LayerScatteredSelection extends ScatteredSelection<
    Layer, LayerRectangleSelection, LayerScatteredSelection, GfxTile
> {
    public LayerScatteredSelection(Layer layer, List<Point> points) {
        super(layer, points);
    }

    public LayerScatteredSelection self() { return this; }
}
