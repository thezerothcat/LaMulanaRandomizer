package lmr.randomizer.dat.blocks.contents.entries;

/**
 * Special case of BlockListData for MapNamesLimitedBlock entry
 */
public class MapMusicLimitedEntry extends ListEntry {
    public MapMusicLimitedEntry() {
        super(1);
    }

    public MapMusicLimitedEntry(GrailPointEntry objectToCopy) {
        super(objectToCopy);
    }


    public void setMusic(int music) {
        getData().set(0, (short)music);
    }
}
