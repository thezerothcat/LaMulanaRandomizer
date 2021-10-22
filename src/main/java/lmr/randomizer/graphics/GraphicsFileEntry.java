package lmr.randomizer.graphics;

public class GraphicsFileEntry {
    private int x;
    private int y;
    private int width;
    private int height;

    public GraphicsFileEntry(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
