package lmr.randomizer.dat.blocks.contents;

import lmr.randomizer.util.BlockDataConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 8/1/2017.
 */
public class BlockCmdSingle implements BlockContents {
    private short data;

    public BlockCmdSingle(short data) {
        this.data = data;
    }

    public BlockCmdSingle(BlockCmdSingle objectToCopy) {
        this.data = objectToCopy.data;
    }

    @Override
    public int getSize() {
        return 8;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(BlockDataConstants.DataList);
        rawData.add((short)1);
        rawData.add(data);
        rawData.add(BlockDataConstants.EndOfEntry);
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(BlockDataConstants.DataList);
        dataOutputStream.writeShort(1);
        dataOutputStream.writeShort(data);
        dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);
    }
}
