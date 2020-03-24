package lmr.randomizer.msd.object.tilemap;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.*;

public class ScatteredSelection<M extends TileMap<M, T>, T> {
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

    public ScatteredSelection<M, T> forEach(Consumer<T> consumer) {
        return forEach((tile, x, y) -> consumer.accept(tile));
    }

    public ScatteredSelection<M, T> forEach(BiConsumer<Short, Short> consumer) {
        return forEach((tile, x, y) -> consumer.accept(x, y));
    }

    public ScatteredSelection<M, T> forEach(TileAndCoordsConsumer<T> consumer) {
        forEach(srcTileMap.getData(), points, consumer);
        return this;
    }

    public ScatteredSelection<M, T> setEach(T value) {
        return setEach((tile, x, y) -> value);
    }

    public ScatteredSelection<M, T> setEach(Supplier<T> supplier) {
        return setEach((tile, x, y) -> supplier.get());
    }

    public ScatteredSelection<M, T> setEach(Function<T,T> supplier) {
        return setEach((tile, x, y) -> supplier.apply(tile));
    }

    public ScatteredSelection<M, T> setEach(BiFunction<Short, Short, T> supplier) {
        return setEach((tile, x, y) -> supplier.apply(x, y));
    }

    public ScatteredSelection<M, T> setEach(TileAndCoordsSupplier<T> supplier) {
        setEach(this, supplier);
        return this;
    }

    public ScatteredSelection<M, T> select(Predicate<T> condition) {
        return select(this, (tile, x, y) -> condition.test(tile));
    }

    public ScatteredSelection<M, T> select(BiPredicate<Short, Short> condition) {
        return select(this, (tile, x, y) -> condition.test(x, y));
    }

    public ScatteredSelection<M, T> select(TileAndCoordsPredicate<T> condition) {
        return select(this, condition);
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

    public static <T> void forEach(List<List<T>> data, List<Point> points, TileAndCoordsConsumer<T> consumer) {
        for (var point : points) {
            var value = TileMapEdit.getTile(data, point.x, point.y);
            consumer.accept(value, (short) point.x, (short) point.y);
        }
    }

    public static <M extends TileMap<M, T>, T> void setEach(ScatteredSelection<M, T> selection, TileAndCoordsSupplier<T> supplier) {
        for (var point : selection.points) {
            var curValue = selection.srcTileMap.getTile(point.x, point.y);
            var newValue = supplier.get(curValue, (short) point.x, (short) point.y);
            selection.srcTileMap.setTile(point.x, point.y, newValue);
        }
    }

    public static <M extends TileMap<M, T>, T> ScatteredSelection<M, T> select(ScatteredSelection<M, T> selection, TileAndCoordsPredicate<T> condition) {
        var newPoints = new ArrayList<Point>();
        var data = selection.getSrcTileMap().getData();

        for (var point : selection.points) {
            var value = TileMapEdit.getTile(data, point.x, point.y);
            if (condition.test(value, (short) point.x, (short) point.y))
                newPoints.add(point);
        }

        return new ScatteredSelection<>(selection.getSrcTileMap(), newPoints);
    }
}
