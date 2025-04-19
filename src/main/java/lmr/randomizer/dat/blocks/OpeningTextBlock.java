package lmr.randomizer.dat.blocks;

import lmr.randomizer.dat.blocks.contents.entries.TextEntry;

public class OpeningTextBlock extends Block {
    public static final int TextEntry1 = 1;
    public static final int TextEntry2 = 5;
    public static final int TextEntry3 = 7;
    public static final int TextEntry4 = 9;
    public static final int TextEntry5 = 11;
    public static final int TextEntry6 = 13;
    public static final int TextEntry7 = 15;
    public static final int TextEntry8 = 17;
    public static final int TextEntry9 = 19;
    public static final int TextEntry10 = 21;
    public static final int TextEntry11 = 25;

    public OpeningTextBlock(int blockNumber) {
        super(blockNumber);
    }

    public void setTextEntry1(TextEntry textEntry) {
        getBlockContents().set(TextEntry1, textEntry);
    }
    public void setTextEntry2(TextEntry textEntry) {
        getBlockContents().set(TextEntry2, textEntry);
    }
    public void setTextEntry3(TextEntry textEntry) {
        getBlockContents().set(TextEntry3, textEntry);
    }
    public void setTextEntry4(TextEntry textEntry) {
        getBlockContents().set(TextEntry4, textEntry);
    }
    public void setTextEntry5(TextEntry textEntry) {
        getBlockContents().set(TextEntry5, textEntry);
    }
    public void setTextEntry6(TextEntry textEntry) {
        getBlockContents().set(TextEntry6, textEntry);
    }
    public void setTextEntry7(TextEntry textEntry) {
        getBlockContents().set(TextEntry7, textEntry);
    }
    public void setTextEntry8(TextEntry textEntry) {
        getBlockContents().set(TextEntry8, textEntry);
    }
    public void setTextEntry9(TextEntry textEntry) {
        getBlockContents().set(TextEntry9, textEntry);
    }
    public void setTextEntry10(TextEntry textEntry) {
        getBlockContents().set(TextEntry10, textEntry);
    }
    public void setTextEntry11(TextEntry textEntry) {
        getBlockContents().set(TextEntry11, textEntry);
    }
}