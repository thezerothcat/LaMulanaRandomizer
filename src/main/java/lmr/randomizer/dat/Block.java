package lmr.randomizer.dat;

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

    public List<BlockContents> getBlockContents() {
        return blockContents;
    }

    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(getBlockSize());
        for (BlockContents blockData : getBlockContents()) {
            blockData.writeBytes(dataOutputStream);
        }
    }
}
