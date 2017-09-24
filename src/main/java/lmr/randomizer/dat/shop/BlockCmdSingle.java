package lmr.randomizer.dat.shop;

import lmr.randomizer.dat.BlockContents;

import java.io.DataOutputStream;
import java.io.IOException;

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
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(0x004e);
        dataOutputStream.writeShort(1);
        dataOutputStream.writeShort(data);
        dataOutputStream.writeShort(0x000a);
    }
}
