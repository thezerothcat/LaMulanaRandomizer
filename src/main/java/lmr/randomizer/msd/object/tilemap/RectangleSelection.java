package lmr.randomizer.msd.object.tilemap;

import java.awt.*;
import java.util.List;
import java.util.function.*;

public class RectangleSelection<M extends TileMap<M, T>, T> {
    public Rectangle rect;

    private M srcTileMap;

    public RectangleSelection(M tileMap, Rectangle rect) {
        setSrcTileMap(tileMap);
        setRect(rect);
    }

    public M getSrcTileMap() {
        return srcTileMap;
    }

    public void setSrcTileMap(M tileMap) {
        if (tileMap == null)
            throw new IllegalArgumentException("TileMap cannot be null");
        this.srcTileMap = tileMap;
    }

    public short x() { return (short) rect.x; }

    public short y() { return (short) rect. y; }

    public short right() { return (short) (rect.x + rect.width); }

    public short bottom() { return (short) (rect.y + rect.height); }

    public short width() {
        return (short) rect.width;
    }

    public short height() {
        return (short) rect.height;
    }

    public Rectangle rect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        if (!isInBounds(srcTileMap, rect))
            throw new IllegalArgumentException("Rectangle passed to RectangleSelection must be within tilemap bounds");
        this.rect = rect;
    }

    public T getTile(int x, int y) {
        return TileMapEdit.getTile(srcTileMap.getData(), rect.x + x, rect.y + y);
    }

    public RectangleSelection<M, T> setTile(int x, int y, T value) {
        TileMapEdit.setTile(srcTileMap.getData(), rect.x + x, rect.y + y, value);
        return this;
    }

    public RectangleSelection<M, T> forEach(Consumer<T> consumer) {
        return forEach((tile, x, y) -> consumer.accept(tile));
    }

    public RectangleSelection<M, T> forEach(BiConsumer<Short, Short> consumer) {
        return forEach((tile, x, y) -> consumer.accept(x, y));
    }

    public RectangleSelection<M, T> forEach(TileAndCoordsConsumer<T> consumer) {
        TileMapEdit.onRect(srcTileMap.getData(), rect.x, rect.y, width(), height(), consumer);
        return this;
    }

    public RectangleSelection<M, T> fill(T value) {
        return setEach((tile, x, y) -> value);
    }

    public RectangleSelection<M, T> setEach(Supplier<T> supplier) {
        return setEach((tile, x, y) -> supplier.get());
    }

    public RectangleSelection<M, T> setEach(Function<T, T> supplier) {
        return setEach((tile, x, y) -> supplier.apply(tile));
    }

    public RectangleSelection<M, T> setEach(BiFunction<Short, Short, T> supplier) {
        return setEach((tile, x, y) -> supplier.apply(x, y));
    }

    public RectangleSelection<M, T> setEach(TileAndCoordsSupplier<T> supplier) {
        forEach((tile, x, y) -> {
            var value = supplier.get(tile, x, y);
            setTile(x, y, value);
        });
        return this;
    }

    public RectangleSelection<M, T> select(Rectangle relativeRect) {
        return select(relativeRect.x, relativeRect.y, relativeRect.width, relativeRect.height);
    }

    public RectangleSelection<M, T> select(int x, int y, int width, int height) {
        var newRect = new Rectangle(rect.x + x, rect.y + y, width, height);
        return new RectangleSelection<>(srcTileMap, newRect);
    }

    public ScatteredSelection<M, T> select(Predicate<T> condition) {
        return select((tile, x, y) -> condition.test(tile));
    }

    public ScatteredSelection<M, T> select(BiPredicate<Short, Short> condition) {
        return select((tile, x, y) -> condition.test(x, y));
    }

    public ScatteredSelection<M, T> select(TileAndCoordsPredicate<T> condition) {
        var points = TileMapEdit.testTiles(
            srcTileMap.getData(), rect.x, rect.y, rect.x + width(), rect.y + height(), condition
        );

        return select(points);
    }

    public ScatteredSelection<M, T> select(List<Point> points) {
        return new ScatteredSelection<>(srcTileMap, points);
    }

    public List<List<T>> materializeData() {
        return TileMapEdit.buildData(width(), height(), this::getTile);
    }

    public M asTileMap() {
        return srcTileMap.make(materializeData());
    }

    public static <M extends TileMap<M, T>, T> boolean isInBounds(M tileMap, Rectangle rect) {
        return rect.x >= 0 && rect.y >= 0 && rect.width <= tileMap.width() && rect.height <= tileMap.height();
    }
}
