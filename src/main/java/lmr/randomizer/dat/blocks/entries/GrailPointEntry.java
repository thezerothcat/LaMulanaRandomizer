package lmr.randomizer.dat.blocks.entries;

/**
 * Special case of BlockListData for non-default grail warp
 */
public class GrailPointEntry extends ListEntry {
    public GrailPointEntry() {
        super(8);
    }

    public void setRequireMirai(boolean requireMirai) {
        getData().set(0, (short)(requireMirai ? 1 : 0));
    }

    public void setFlag(int flagIndex) {
        getData().set(1, (short)flagIndex);
    }

    public void setWarpImage(int image) {
        getData().set(2, (short)image);
    }

    public void setDestination(int zone, int room, int screen, int x, int y) {
        getData().set(3, (short)zone);
        getData().set(4, (short)room);
        getData().set(5, (short)screen);
        getData().set(6, (short)x);
        getData().set(7, (short)y);
    }
}
