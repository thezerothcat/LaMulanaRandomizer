package lmr.randomizer.dat.blocks;

import lmr.randomizer.dat.blocks.contents.entries.MapMusicEntry;
import lmr.randomizer.dat.blocks.contents.entries.TextEntry;

public class MapNamesBlock extends Block {
    public MapNamesBlock(int blockNumber) {
        super(blockNumber);
    }

    public void setFlag(int flag) {
        ((MapMusicEntry)getBlockContents()).setFlag(flag);
    }

    public void setMusicTrue(int data) {
        ((MapMusicEntry)getBlockContents()).setMusicTrue(data);
    }

    public void setMusicFalse(int data) {
        ((MapMusicEntry)getBlockContents()).setMusicFalse(data);
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
