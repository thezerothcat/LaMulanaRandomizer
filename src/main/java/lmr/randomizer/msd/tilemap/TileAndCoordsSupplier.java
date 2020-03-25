package lmr.randomizer.msd.tilemap;

@FunctionalInterface
public interface TileAndCoordsSupplier<T> {
    T get(T value, Short x, Short y);
}
