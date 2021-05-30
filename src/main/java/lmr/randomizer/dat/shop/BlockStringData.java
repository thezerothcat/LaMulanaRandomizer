package lmr.randomizer.dat.shop;

import lmr.randomizer.dat.BlockContents;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 8/1/2017.
 */
public class BlockStringData implements BlockContents {
    private List<Short> data = new ArrayList<>();
    private Integer itemNameStartIndex;
    private Integer itemNameEndIndex;

    public BlockStringData() {
    }

    public BlockStringData(BlockStringData objectToCopy) {
        this.itemNameStartIndex = objectToCopy.itemNameStartIndex;
        this.itemNameEndIndex = objectToCopy.itemNameEndIndex;
        this.data = new ArrayList<>(objectToCopy.data);
    }

    public List<Short> getData() {
        return data;
    }

    public Integer getItemNameStartIndex() {
        return itemNameStartIndex;
    }

    public void setItemNameStartIndex(Integer itemNameStartIndex) {
        this.itemNameStartIndex = itemNameStartIndex;
    }

    public Integer getItemNameEndIndex() {
        return itemNameEndIndex;
    }

    public void setItemNameEndIndex(Integer itemNameEndIndex) {
        this.itemNameEndIndex = itemNameEndIndex;
    }

    @Override
    public int getSize() {
        return data.size() * 2;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        for(int i = 0; i < data.size(); i++) {
            rawData.add(data.get(i));
        }
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        for(Short dataShort : data) {
            dataOutputStream.writeShort(dataShort);
        }
    }
}
