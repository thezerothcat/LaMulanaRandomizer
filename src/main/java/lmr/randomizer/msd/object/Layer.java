package lmr.randomizer.msd.object;

import lmr.randomizer.msd.object.tilemap.TileMap;

import java.util.List;

public class Layer extends TileMap<Layer, GfxTile> {
    public Layer(List<List<GfxTile>> data) {
        super(data);
    }

    public Layer self() {
        return this;
    }

    // TODO: pasteGraphic(int x, int y, int gfxX, int gfxY, int gfxWidth, int gfxHeight)

    public Layer make(List<List<GfxTile>> data) {
        return new Layer(data);
    }
}
