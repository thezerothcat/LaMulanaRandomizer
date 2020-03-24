package lmr.randomizer.msd.object.tilemap;

@FunctionalInterface
public interface TileAndCoordsConsumer<T> {
    void accept(T value, Short x, Short y);
}
