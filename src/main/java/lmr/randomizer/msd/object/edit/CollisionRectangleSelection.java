package lmr.randomizer.msd.object.edit;

import lmr.randomizer.msd.object.Collision;
import lmr.randomizer.msd.object.CollisionType;
import lmr.randomizer.msd.tilemap.RectangleSelection;

import java.awt.*;

public class CollisionRectangleSelection extends RectangleSelection<
    Collision, CollisionRectangleSelection, CollisionScatteredSelection, Byte
> {
    public CollisionRectangleSelection(Collision collision, Rectangle rect) {
        super(collision, rect);
    }

    public CollisionRectangleSelection moveTiles(int dx, int dy) {
        return moveTiles(dx, dy, CollisionType.EMPTY.value);
    }

    public CollisionRectangleSelection self() {
        return this;
    }
}
