package lmr.randomizer.msd.tilemap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

// T should be a Value object, i.e. immutable with equals() performing value equality
public abstract class TileMap<
    M extends TileMap<M, R, S, T>,
    R extends RectangleSelection<M, R, S, T>,
    S extends ScatteredSelection<M, R, S, T>,
    T
> {
    protected List<List<T>> data;

    public TileMap() {
        this(new ArrayList<>());
    }

    public TileMap(List<List<T>> data) {
        this.data = data;
    }

    public TileMap(short width, short height, T defaultValue) {
        this.data = TileMapEdit.buildData(width, height, defaultValue);
    }

    protected abstract M self();

    public List<List<T>> getData() {
        return data;
    }

    public M setData(List<List<T>> data) {
        if (data == null)
            throw new IllegalArgumentException("cannot assign data to null");

        this.data = data;
        return self();
    }

    public short width() {
        return TileMapEdit.getWidth(data);
    }

    public short height() {
        return TileMapEdit.getHeight(data);
    }

    public T getTile(int x, int y) {
        return TileMapEdit.getTile(data, x, y);
    }

    public M setTile(int x, int y, T value) {
        TileMapEdit.setTile(data, x, y, value);
        return self();
    }

    public M forEach(Consumer<T> consumer) {
        return forEach((tile, x, y) -> consumer.accept(tile));
    }

    public M forEach(BiConsumer<Short, Short> consumer) {
        return forEach((tile, x, y) -> consumer.accept(x, y));
    }

    public M forEach(TileAndCoordsConsumer<T> consumer) {
        TileMapEdit.onRect(data, 0, 0, width(), height(), consumer);
        return self();
    }

    public M fill(T value) {
        return setEach((tile, x, y) -> value);
    }

    public M setEach(Supplier<T> supplier) {
        return setEach((tile, x, y) -> supplier.get());
    }

    public M setEach(Function<T, T> supplier) {
        return setEach((tile, x, y) -> supplier.apply(tile));
    }

    public M setEach(BiFunction<Short, Short, T> supplier) {
        return setEach((tile, x, y) -> supplier.apply(x, y));
    }

    public M setEach(TileAndCoordsSupplier<T> supplier) {
        forEach((tile, x, y) -> {
            var value = supplier.get(tile, x, y);
            setTile(x, y, value);
        });

        return self();
    }

    public M stamp(M stamp, int x, int y) {
        if (this == stamp)
            throw new IllegalArgumentException("TileMap cannot stamp itself onto itself");

        select(x, y, stamp.width(), stamp.height())
            .setEach(stamp::getTile);

        return self();
    }

    public M stamp(R stamp, int x, int y) {
        select(x, y, stamp.width(), stamp.height())
                .setEach(stamp::getTile);

        return self();
    }

    public M resize(int width, int height, T fillValue) {
        TileMapEdit.resize(data, (short) width, (short) height, fillValue);
        return self();
    }

    public List<List<T>> cloneData() {
        return TileMapEdit.buildData(width(), height(), this::getTile);
    }

    public M makeCopy() {
        return make(cloneData());
    }

    public R select(int x, int y, int width, int height) {
        return select(new Rectangle(x, y, width, height));
    }

    public abstract R select(Rectangle rect);

    public R selectAll() {
        return select(new Rectangle(0, 0, width(), height()));
    }

    public S select(Predicate<T> condition) {
        return select((tile, x, y) -> condition.test(tile));
    }

    public S select(BiPredicate<Short, Short> condition) {
        return select((tile, x, y) -> condition.test(x, y));
    }

    public S select(TileAndCoordsPredicate<T> condition) {
        var points = TileMapEdit.testTiles(getData(), 0, 0, width(), height(), condition);
        return select(points);
    }

    public abstract S select(List<Point> points);

    public abstract M make(List<List<T>> data);
}
