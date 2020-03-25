package lmr.randomizer.msd.tilemap;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.*;

public abstract class ScatteredSelection<
    M extends TileMap<M, R, S, T>,
    R extends RectangleSelection<M, R, S, T>,
    S extends ScatteredSelection<M, R, S, T>,
    T
> {
    public M srcTileMap;
    private List<Point> points;
    private short width;
    private short height;

    public ScatteredSelection(M tileMap, List<Point> points) {
        this.srcTileMap = tileMap;
        this.points = Collections.unmodifiableList(points);
        this.width = calculateWidth(points);
        this.height = calculateHeight(points);
    }

    protected abstract S self();

    public M getSrcTileMap() {
        return srcTileMap;
    }

    public List<Point> getPoints() {
        return points;
    }

    public short width() {
        return width;
    }

    public short height() {
        return height;
    }

    public S forEach(Consumer<T> consumer) {
        return forEach((tile, x, y) -> consumer.accept(tile));
    }

    public S forEach(BiConsumer<Short, Short> consumer) {
        return forEach((tile, x, y) -> consumer.accept(x, y));
    }

    public S forEach(TileAndCoordsConsumer<T> consumer) {
        for (var point : points) {
            var value = TileMapEdit.getTile(srcTileMap.data, point.x, point.y);
            consumer.accept(value, (short) point.x, (short) point.y);
        }

        return self();
    }

    public S setEach(T value) {
        return setEach((tile, x, y) -> value);
    }

    public S setEach(Supplier<T> supplier) {
        return setEach((tile, x, y) -> supplier.get());
    }

    public S setEach(Function<T,T> supplier) {
        return setEach((tile, x, y) -> supplier.apply(tile));
    }

    public S setEach(BiFunction<Short, Short, T> supplier) {
        return setEach((tile, x, y) -> supplier.apply(x, y));
    }

    public S setEach(TileAndCoordsSupplier<T> supplier) {
        forEach((tile, x, y) -> {
            var newValue = supplier.get(tile, x, y);
            srcTileMap.setTile(x, y, newValue);
        });

        return self();
    }

    public S select(Predicate<T> condition) {
        return select((tile, x, y) -> condition.test(tile));
    }

    public S select(BiPredicate<Short, Short> condition) {
        return select((tile, x, y) -> condition.test(x, y));
    }

    public S select(TileAndCoordsPredicate<T> condition) {
        var newPoints = new ArrayList<Point>();
        var data = srcTileMap.getData();

        forEach((tile, x, y) -> {
            if (condition.test(tile, x, y))
                newPoints.add(new Point(x, y));
        });

        return srcTileMap.select(newPoints);
    }

    public static short calculateWidth(List<Point> points) {
        var min = 0;
        var max = 0;

        for (var point : points) {
            if (point.x < min) min = point.x;
            if (point.x > max) max = point.x;
        }

        return (short) (max - min);
    }

    public static short calculateHeight(List<Point> points) {
        var min = points.get(0).y;
        var max = points.get(points.size() - 1).y;

        return (short) (max - min);
    }
}
