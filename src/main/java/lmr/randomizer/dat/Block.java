package lmr.randomizer.dat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/26/2017.
 */
public class Block {
    private int blockNumber;
    private int blockSize;
    private List<BlockContents> blockContents = new ArrayList<>();

    public Block(int blockNumber, int blockSize) {
        this.blockNumber = blockNumber;
        this.blockSize = blockSize;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public List<BlockContents> getBlockContents() {
        return blockContents;
    }
}
