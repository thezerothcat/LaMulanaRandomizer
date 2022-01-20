package lmr.randomizer.dat.blocks.contents;

import lmr.randomizer.util.BlockDataConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public class BlockColorsData implements BlockContents {
    public static final BlockColorsData COLOR_DEFAULT = new BlockColorsData(0, 0, 0);
    public static final BlockColorsData COLOR_SOFTWARE_YELLOW = new BlockColorsData((short)0, (short)0x32, (short)0x96);
    public static final BlockColorsData COLOR_ITEMS_GREEN = new BlockColorsData((short)0x96, (short)0, (short)0x64);
    public static final BlockColorsData COLOR_PEOPLE_PLACES_BLUE = new BlockColorsData((short)0x96, (short)0x32, (short)0);
    public static final BlockColorsData COLOR_THREATS_RED = new BlockColorsData((short)0, (short)0x78, (short)0x78);
    public static final BlockColorsData COLOR_MANTRAS_DARKRED = new BlockColorsData((short)0, (short)0xc8, (short)0xc8);

    private short data;
    private short color1;
    private short color2;
    private short color3;

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
