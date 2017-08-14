package lmr.randomizer.dat.conversation;

import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.BlockListData;

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
        for (BlockListData flagCheckReference : flagCheckReferences) {
            size += flagCheckReference.getSize();
        }
        size += 2 * (flagCheckReferences.size() - 1);
        return size;
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
}