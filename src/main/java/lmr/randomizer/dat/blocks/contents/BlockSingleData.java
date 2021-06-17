package lmr.randomizer.dat.blocks.contents;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public class BlockSingleData implements BlockContents {
    private short data;

    public BlockSingleData(short data) {
        this.data = data;
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(data);
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(data);
    }
}
