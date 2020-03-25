package lmr.randomizer.msd.object.edit;

import lmr.randomizer.msd.object.GfxTile;
import lmr.randomizer.msd.object.Layer;
import lmr.randomizer.msd.tilemap.RectangleSelection;

import java.awt.*;

public class LayerRectangleSelection extends RectangleSelection<
        Layer, LayerRectangleSelection, LayerScatteredSelection, GfxTile
> {
    public LayerRectangleSelection(Layer layer, Rectangle rect) {
        super(layer, rect);
    }

    public LayerRectangleSelection moveTiles(int dx, int dy) {
        return moveTiles(dx, dy, GfxTile.EMPTY);
    }

    public LayerRectangleSelection self() {
        return this;
    }
}
