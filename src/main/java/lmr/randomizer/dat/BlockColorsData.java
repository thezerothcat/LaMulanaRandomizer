package lmr.randomizer.dat;

import java.io.DataOutputStream;
import java.io.IOException;

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

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(data);
        dataOutputStream.writeShort(color1);
        dataOutputStream.writeShort(color2);
        dataOutputStream.writeShort(color3);
    }
}
