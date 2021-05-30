package lmr.randomizer.dat.blocks;

import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.blocks.entries.MapMusicLimitedEntry;
import lmr.randomizer.dat.blocks.entries.TextEntry;

public class MapNamesLimitedBlock extends Block {
    public MapNamesLimitedBlock(int blockNumber) {
        super(blockNumber);
    }

    public void setMusic(int music) {
        ((MapMusicLimitedEntry)getBlockContents()).setMusic(music);
    }

    public void setJapaneseFieldName(TextEntry textEntry) {
        getBlockContents().set(1, textEntry);
    }

    public void setFieldName(TextEntry textEntry) {
        getBlockContents().set(2, textEntry);
    }

    public void setScreenName(int index, TextEntry textEntry) {
        getBlockContents().set(3 + index, textEntry);
    }
}
