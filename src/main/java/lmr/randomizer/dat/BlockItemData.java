package lmr.randomizer.dat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public class BlockItemData implements BlockContents {
    private short data;
    private short itemData; // World flag in decimal form

    public BlockItemData(short data, short itemData) {
        this.data = data;
        this.itemData = itemData;
    }

    public BlockItemData(short itemData) {
        this.data = BlockDataConstants.Item;
        this.itemData = itemData;
    }

    public short getItemData() {
        return itemData;
    }

    public void setItemData(short itemData) {
        this.itemData = itemData;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(data);
        rawData.add(itemData);
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(data);
        dataOutputStream.writeShort(itemData);
    }
}
