package lmr.randomizer.msd.tilemap;

@FunctionalInterface
public interface TileAndCoordsConsumer<T> {
    void accept(T value, Short x, Short y);
}
