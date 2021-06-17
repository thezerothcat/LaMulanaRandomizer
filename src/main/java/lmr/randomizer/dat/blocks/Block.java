package lmr.randomizer.dat.blocks;

import lmr.randomizer.dat.blocks.contents.BlockContents;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/26/2017.
 */
public class Block {
    private int blockNumber;
    private List<BlockContents> blockContents = new ArrayList<>();

    public Block() {
    }

    public Block(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int getBlockSize() {
        int size = 0;
        for(BlockContents blockContent : blockContents) {
            size += blockContent.getSize();
        }
        return size;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public List<BlockContents> getBlockContents() {
        return blockContents;
    }

    /**
     * Mostly for testing use, should probably use this when writing the dat file
     * @return list of shorts representing the data of this block
     */
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add((short)getBlockSize());
        for (BlockContents blockData : getBlockContents()) {
            rawData.addAll(blockData.getRawData());
        }
        return rawData;
    }

    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(getBlockSize());
        for (BlockContents blockData : getBlockContents()) {
            blockData.writeBytes(dataOutputStream);
        }
    }
}
