package lmr.randomizer.msd.object.tilemap;

@FunctionalInterface
public interface TileAndCoordsSupplier<T> {
    T get(T value, Short x, Short y);
}
