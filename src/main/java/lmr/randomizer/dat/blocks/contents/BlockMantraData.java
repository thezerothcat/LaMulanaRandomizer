package lmr.randomizer.dat.blocks.contents;

import lmr.randomizer.util.BlockDataConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public class BlockMantraData implements BlockContents {
    private short mantraData;

    public BlockMantraData(int mantraData) {
        this.mantraData = (short)mantraData;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(BlockDataConstants.Mantra);
        rawData.add(mantraData);
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(BlockDataConstants.Mantra);
        dataOutputStream.writeShort(mantraData);
    }
}
