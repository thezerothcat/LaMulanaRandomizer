package lmr.randomizer.msd.object.edit;

import lmr.randomizer.msd.object.Collision;
import lmr.randomizer.msd.tilemap.ScatteredSelection;

import java.awt.*;
import java.util.List;

public class CollisionScatteredSelection extends ScatteredSelection<
        Collision,
        CollisionRectangleSelection,
    CollisionScatteredSelection,
    Byte
> {
    public CollisionScatteredSelection(Collision collision, List<Point> points) {
        super(collision, points);
    }

    public CollisionScatteredSelection self() { return this; }
}
