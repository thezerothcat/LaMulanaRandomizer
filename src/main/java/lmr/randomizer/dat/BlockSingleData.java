package lmr.randomizer.dat;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public class BlockSingleData implements BlockContents {
    private short data;

    public BlockSingleData(short data) {
        this.data = data;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(data);
    }
}
