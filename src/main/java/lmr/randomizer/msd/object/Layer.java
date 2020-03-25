package lmr.randomizer.msd.object;

import lmr.randomizer.msd.object.edit.LayerRectangleSelection;
import lmr.randomizer.msd.object.edit.LayerScatteredSelection;
import lmr.randomizer.msd.tilemap.TileMap;

import java.awt.*;
import java.util.List;

public class Layer extends TileMap<
    Layer,
    LayerRectangleSelection,
    LayerScatteredSelection,
    GfxTile
> {
    public Layer(List<List<GfxTile>> data) {
        super(data);
    }

    public Layer self() {
        return this;
    }

    // TODO: pasteGraphic(int x, int y, int gfxX, int gfxY, int gfxWidth, int gfxHeight)

    public LayerRectangleSelection select(Rectangle rect) {
        return new LayerRectangleSelection(this, rect);
    }

    public LayerScatteredSelection select(List<Point> points) {
        return new LayerScatteredSelection(this, points);
    }

    public Layer make(List<List<GfxTile>> data) {
        return new Layer(data);
    }
}
