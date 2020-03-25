package lmr.randomizer.msd.tilemap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

// TileMap data mutation logic, mostly shared between TileMap and RectangleSelection.
public class TileMapEdit {
    public static <T> List<List<T>> buildData(short width, short height, T fillValue) {
        return buildData(width, height, (x,y) -> fillValue);
    }

    public static <T> List<List<T>> buildData(short width, short height, BiFunction<Short, Short, T> supplier) {
        var res = new ArrayList<List<T>>(width);

        for (short x = 0; x < width; x++) {
            var column = new ArrayList<T>(height);
            for (short y = 0; y < height; y++) {
                var val = supplier.apply(x, y);
                column.add(val);
            }

            res.add(column);
        }

        return res;
    }

    public static <T> short getWidth(List<List<T>> data) {
        return (short) data.size();
    }

    public static <T> short getHeight(List<List<T>> data) {
        return (short) (data.isEmpty() ? 0 : data.get(0).size());
    }

    public static boolean isInMaxBounds(int x, int y) {
        return !(x < 0 || x > 65535 || y < 0 || y > 65535);
    }

    public static void validateBounds(int x, int y) {
        if (isInMaxBounds(x, y)) return;
        throw new IndexOutOfBoundsException();
    }

    public static <T> T getTileBounded(List<List<T>> data, int x, int y) {
        validateBounds(x, y);
        return getTile(data, x, y);
    }

    public static <T> void setTileBounded(List<List<T>> data, int x, int y, T value) {
        validateBounds(x, y);
        setTile(data, x, y, value);
    }

    public static <T> T getTile(List<List<T>> data, int x, int y) {
        return data.get(x).get(y);
    }

    public static <T> void setTile(List<List<T>> data, int x, int y, T value) {
        data.get(x).set(y, value);
    }

    public static <T> void onRect(List<List<T>> data, int x, int y, int width, int height, TileAndCoordsConsumer<T> consumer) {
        validateBounds(x, y);
        validateBounds(x + width, y + height);

        for (short iy = 0; iy < height; iy++) {
            for (short ix = 0; ix < width; ix++) {
                var value = getTile(data, x + ix, y + iy);
                consumer.accept(value, ix, iy);
            }
        }
    }

    public static <T> void resize(List<List<T>> data, short width, short height, T fillValue) {
        // eventually support an overload to specify anchor TOP_LEFT, CENTER, RIGHT etc.
        // it would translate the data content then perform the resize

        var widthChange = width - getWidth(data);
        if (widthChange < 0) {
            for (int i = 0; i < -widthChange; i++) {
                data.remove(data.size() - 1);
            }
        } else if (widthChange > 0) {
            for (int i = 0; i < widthChange; i++) {
                data.add(new ArrayList<>(height));
            }
        }

        var heightChange = height - getHeight(data);
        if (heightChange < 0) {
            for (int c = 0; c < width; c++) {
                for (int i = 0; i < -heightChange; i++) {
                    var column = data.get(c);
                    column.remove(column.size() - 1);
                }
            }
        } else if (heightChange > 0) {
            for (int c = 0; c < width; c++) {
                for (int i = 0; i < heightChange; i++) {
                    var column = data.get(c);
                    column.add(fillValue);
                }
            }
        }
    }

    public static <T> List<Point> testTiles(
        List<List<T>> data, int x, int y, int width, int height, TileAndCoordsPredicate<T> predicate
    ) {
        validateBounds(x, y);
        validateBounds(x + width, y + height);

        var points = new ArrayList<Point>();

        for (short ix = 0; ix < width; ix++) {
            var column = data.get(x + ix);
            for (short iy = 0; iy < height; iy++) {
                var value = column.get(y + iy);
                if (predicate.test(value, ix, iy))
                    points.add(new Point(x + ix, y + iy));
            }
        }

        return points;
    }
}
