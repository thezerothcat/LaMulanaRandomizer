package lmr.randomizer.dat.blocks.entries;

public class MapMusicEntry extends ListEntry {
    public MapMusicEntry() {
        super(3);
    }

    public MapMusicEntry(GrailPointEntry objectToCopy) {
        super(objectToCopy);
    }


    public void setFlag(int flag) {
        getData().set(0, (short)flag);
    }

    public void setMusicTrue(int data) {
        getData().set(1, (short)data); // plays if flag is non-zero
    }

    public void setMusicFalse(int data) {
        getData().set(2, (short)data); // plays if the flag is zero
    }
}
