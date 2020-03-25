package lmr.randomizer.msd.object;

import lmr.randomizer.msd.object.edit.CollisionRectangleSelection;
import lmr.randomizer.msd.object.edit.CollisionScatteredSelection;
import lmr.randomizer.msd.tilemap.TileMap;

import java.awt.*;
import java.util.List;

public class Collision extends TileMap<
    Collision,
        CollisionRectangleSelection,
        CollisionScatteredSelection,
    Byte
> {
    public Collision(List<List<Byte>> data) {
        super(data);
    }

    public Collision self() {
        return this;
    }

    public CollisionRectangleSelection select(Rectangle rect) {
        return new CollisionRectangleSelection(this, rect);
    }

    public CollisionScatteredSelection select(List<Point> points) {
        return new CollisionScatteredSelection(this, points);
    }

    public Collision make(List<List<Byte>> data) {
        return new Collision(data);
    }
}
