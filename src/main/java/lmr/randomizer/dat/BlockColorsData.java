package lmr.randomizer.dat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public class BlockColorsData implements BlockContents {
    private short data;
    private short color1;
    private short color2;
    private short color3;

    public BlockColorsData(short data, short color1, short color2, short color3) {
        this.data = data;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
    }

    public BlockColorsData(int color1, int color2, int color3) {
        this.data = BlockDataConstants.ColorChange;
        this.color1 = (short)color1;
        this.color2 = (short)color2;
        this.color3 = (short)color3;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(data);
        rawData.add(color1);
        rawData.add(color2);
        rawData.add(color3);
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(data);
        dataOutputStream.writeShort(color1);
        dataOutputStream.writeShort(color2);
        dataOutputStream.writeShort(color3);
    }

    @Override
    public int getSize() {
        return 8;
    }
}
