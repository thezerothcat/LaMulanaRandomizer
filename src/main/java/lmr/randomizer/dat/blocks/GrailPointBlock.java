package lmr.randomizer.dat.blocks;

import lmr.randomizer.dat.blocks.contents.entries.DefaultGrailPointEntry;
import lmr.randomizer.dat.blocks.contents.entries.GrailPointEntry;

public class GrailPointBlock extends Block {
    public GrailPointBlock(int blockNumber) {
        super(blockNumber);
    }

    public DefaultGrailPointEntry getDefaultGrailPointEntry() {
        return ((DefaultGrailPointEntry)getBlockContents().get(0));
    }

    /**
     * Gets existing GrailPointEntry, adds a new one if the index is too high
     * @param index nonzero index for warp data
     * @return GrailPointEntry
     */
    public GrailPointEntry getGrailPointEntry(int index) {
        while(getBlockContents().size() <= index) {
            getBlockContents().add(new GrailPointEntry());
        }
        return ((GrailPointEntry)getBlockContents().get(index));
    }
}
