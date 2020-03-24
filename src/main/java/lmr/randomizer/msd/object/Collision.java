package lmr.randomizer.msd.object;

import lmr.randomizer.msd.object.tilemap.TileMap;

import java.util.List;

public class Collision extends TileMap<Collision, Byte> {
    public Collision(List<List<Byte>> data) {
        super(data);
    }

    public Collision self() {
        return this;
    }

    public Collision make(List<List<Byte>> data) {
        return new Collision(data);
    }
}
