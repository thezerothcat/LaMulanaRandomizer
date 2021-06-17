package lmr.randomizer.dat.blocks;

import lmr.randomizer.util.BlockDataConstants;
import lmr.randomizer.dat.blocks.contents.BlockListData;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 8/12/2017.
 */
public class CheckBlock extends Block {
    List<BlockListData> flagCheckReferences = new ArrayList<>();

    public CheckBlock(int blockNumber) {
        super(blockNumber);
    }

    public List<BlockListData> getFlagCheckReferences() {
        return flagCheckReferences;
    }

    @Override
    public int getBlockSize() {
        int size = 0;
        if(!flagCheckReferences.isEmpty()) {
            for(BlockListData flagCheckReference : flagCheckReferences) {
                size += flagCheckReference.getSize();
            }
            size += 2 * (flagCheckReferences.size() - 1);
        }
        return size;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add((short)getBlockSize());
        for(int i = 0; i < flagCheckReferences.size(); i++) {
            rawData.addAll(flagCheckReferences.get(i).getRawData());
            if(i < flagCheckReferences.size() - 1) {
                rawData.add(BlockDataConstants.EndOfEntry);
            }
        }
        return rawData;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(getBlockSize());

        for(int i = 0; i < flagCheckReferences.size(); i++) {
            flagCheckReferences.get(i).writeBytes(dataOutputStream);
            if(i < flagCheckReferences.size() - 1) {
                dataOutputStream.writeShort(0x000a);
            }
        }
    }

    public void removeCheckEntryByBlockIndex(int blockIndex) {
        Integer cmdToRemoveIndex = null;
        for(int i = 0; i < flagCheckReferences.size(); i++) {
            BlockListData blockListData = flagCheckReferences.get(i);
            if(blockListData.getData().get(2) == blockIndex) {
                cmdToRemoveIndex = i;
            }
        }
        if(cmdToRemoveIndex != null) {
            flagCheckReferences.remove((int)cmdToRemoveIndex);
        }
    }
}