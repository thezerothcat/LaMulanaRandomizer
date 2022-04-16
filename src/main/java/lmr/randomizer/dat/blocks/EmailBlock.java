package lmr.randomizer.dat.blocks;

import lmr.randomizer.dat.blocks.contents.BlockContents;
import lmr.randomizer.util.BlockDataConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmailBlock extends Block {
    public EmailBlock(int blockNumber) {
        super(blockNumber);
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add((short)getBlockSize());
        for(int i = 0; i < getBlockContents().size(); i++) {
            rawData.addAll(getBlockContents().get(i).getRawData());
            if(i < getBlockContents().size() - 1) {
                rawData.add(BlockDataConstants.EndOfEntry);
            }
        }
        return rawData;
    }

    @Override
    public int getBlockSize() {
        int size = 0;
        for(BlockContents emailEntry : getBlockContents()) {
            size += emailEntry.getSize() + 2;
        }
        size -= 2; // The final BlockDataConstants.EndOfEntry is omitted
        return size;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(getBlockSize());

        for(int i = 0; i < getBlockContents().size(); i++) {
            getBlockContents().get(i).writeBytes(dataOutputStream);
            if(i < getBlockContents().size() - 1) {
                dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);
            }
        }
    }
}
