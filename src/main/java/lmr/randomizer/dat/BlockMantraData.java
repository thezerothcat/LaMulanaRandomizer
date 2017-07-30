package lmr.randomizer.dat;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public class BlockMantraData implements BlockContents {
    private short data;
    private short mantraData;

    public BlockMantraData(short data, short mantraData) {
        this.data = data;
        this.mantraData = mantraData;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(data);
        dataOutputStream.writeShort(mantraData);
    }
}
