package lmr.randomizer.msd.tilemap;

import java.awt.*;
import java.util.List;
import java.util.function.*;

public abstract class RectangleSelection<
    M extends TileMap<M, R, S, T>,
    R extends RectangleSelection<M, R, S, T>,
    S extends ScatteredSelection<M, R, S, T>,
    T
> {
    private M srcTileMap;
    private Rectangle rect; // TODO: make immutable

    public RectangleSelection(M tileMap, Rectangle rect) {
        setSrcTileMap(tileMap);
        setRect(rect);
    }

    protected abstract R self();

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
        if (!isInBounds(srcTileMap.data, rect))
            throw new IllegalArgumentException("Rectangle passed to RectangleSelection must be within tilemap bounds");
        this.rect = rect;
    }

    public R moveSelector(int dx, int dy) {
        setRect(new Rectangle(rect.x + dx, rect.y + dy, width(), height()));
        return self();
    }

    public T getTile(int x, int y) {
        return TileMapEdit.getTile(srcTileMap.getData(), rect.x + x, rect.y + y);
    }

    public R setTile(int x, int y, T value) {
        TileMapEdit.setTile(srcTileMap.getData(), rect.x + x, rect.y + y, value);
        return self();
    }

    public R forEach(Consumer<T> consumer) {
        return forEach((tile, x, y) -> consumer.accept(tile));
    }

    public R forEach(BiConsumer<Short, Short> consumer) {
        return forEach((tile, x, y) -> consumer.accept(x, y));
    }

    public R forEach(TileAndCoordsConsumer<T> consumer) {
        TileMapEdit.onRect(srcTileMap.getData(), rect.x, rect.y, width(), height(), consumer);
        return self();
    }

    public R fill(T value) {
        return setEach((tile, x, y) -> value);
    }

    public R setEach(Supplier<T> supplier) {
        return setEach((tile, x, y) -> supplier.get());
    }

    public R setEach(Function<T, T> supplier) {
        return setEach((tile, x, y) -> supplier.apply(tile));
    }

    public R setEach(BiFunction<Short, Short, T> supplier) {
        return setEach((tile, x, y) -> supplier.apply(x, y));
    }

    public R setEach(TileAndCoordsSupplier<T> supplier) {
        forEach((tile, x, y) -> {
            var value = supplier.get(tile, x, y);
            setTile(x, y, value);
        });
        return self();
    }

    public R moveTiles(int dx, int dy, T fillValue) {
        // TODO: optimize so that materializing the stamp isn't necessary
        var stamp = asTileMap();
        fill(fillValue);
        moveSelector(dx, dy);
        srcTileMap.stamp(stamp, rect.x, rect.y);
        return self();
    }

    public R select(Rectangle relativeRect) {
        return select(relativeRect.x, relativeRect.y, relativeRect.width, relativeRect.height);
    }

    public R select(int x, int y, int width, int height) {
        var newRect = new Rectangle(rect.x + x, rect.y + y, width, height);
        return srcTileMap.select(newRect);
    }

    public S select(Predicate<T> condition) {
        return select((tile, x, y) -> condition.test(tile));
    }

    public S select(BiPredicate<Short, Short> condition) {
        return select((tile, x, y) -> condition.test(x, y));
    }

    public S select(TileAndCoordsPredicate<T> condition) {
        var points = TileMapEdit.testTiles(
            srcTileMap.getData(), rect.x, rect.y, rect.x + width(), rect.y + height(), condition
        );

        return select(points);
    }

    public S select(List<Point> points) {
        return srcTileMap.select(points);
    }

    public List<List<T>> materializeData() {
        return TileMapEdit.buildData(width(), height(), this::getTile);
    }

    public M asTileMap() {
        return srcTileMap.make(materializeData());
    }

    public <X> boolean isInBounds(List<List<X>> data, Rectangle rect) {
        return rect.x >= 0 && rect.y >= 0 && rect.width <= TileMapEdit.getWidth(data) && rect.height <= TileMapEdit.getHeight(data);
    }
}
