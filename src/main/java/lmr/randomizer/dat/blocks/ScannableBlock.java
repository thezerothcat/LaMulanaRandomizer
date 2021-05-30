package lmr.randomizer.dat.blocks;

import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.blocks.entries.ListEntry;
import lmr.randomizer.dat.blocks.entries.TextEntry;

public class ScannableBlock extends Block {
    public static final int Language_Native = 0;
    public static final int Language_LaMulanese = 1;
    public static final int Language_AncientLaMulanese = 2;
    public static final int Language_Rosetta = 3;

    public static final int NoImage = 0;
    public static final int slate00 = 1;
    public static final int slate01 = 2;

    public ScannableBlock(int blockNumber) {
        super(blockNumber);
    }

    public void setScanText(TextEntry scanText) {
        getBlockContents().set(0, scanText);
    }

    public void setLanguage(int language) {
        ((ListEntry)getBlockContents().get(1)).getData().set(0, (short)language);
    }

    public void setSlate(int slate) {
        ((ListEntry)getBlockContents().get(1)).getData().set(1, (short)slate);
    }
}
