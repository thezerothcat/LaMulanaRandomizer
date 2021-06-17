package lmr.randomizer.dat.blocks.contents.entries;

/**
 * Special case of BlockListData for default grail warp
 */
public class DefaultGrailPointEntry extends ListEntry {
    public DefaultGrailPointEntry() {
        super(6);
    }

    public void setWarpImage(int image) {
        getData().set(0, (short)image);
    }

    public void setDestination(int zone, int room, int screen, int x, int y) {
        getData().set(1, (short)zone);
        getData().set(2, (short)room);
        getData().set(3, (short)screen);
        getData().set(4, (short)x);
        getData().set(5, (short)y);
    }
}
