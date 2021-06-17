package lmr.randomizer.dat.blocks.contents.entries;

import lmr.randomizer.dat.blocks.contents.BlockContents;
import lmr.randomizer.util.BlockDataConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basically BlockListData, but the 0x000a is handled automatically
 */
public class ListEntry implements BlockContents {
    private short listSize;
    private List<Short> data = new ArrayList<>();

    private boolean includeEndRecordIndicator = true;

    public ListEntry(int listSize) {
        this.listSize = (short)listSize;
        for(int i = 0; i < listSize; i++) {
            data.add((short)0);
        }
    }

    public ListEntry(ListEntry objectToCopy) {
        this.listSize = objectToCopy.listSize;
        data = new ArrayList<>(objectToCopy.data);
    }

    public List<Short> getData() {
        return data;
    }

    public void setIncludeEndRecordIndicator(boolean includeEndRecordIndicator) {
        this.includeEndRecordIndicator = includeEndRecordIndicator;
    }

    @Override
    public int getSize() {
        return data.size() * 2 + 4 + (includeEndRecordIndicator ? 2 : 0); // CMD, list length, then the list itself, then the end of entry marker
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(BlockDataConstants.DataList);
        rawData.add(listSize);
        for(int i = 0; i < data.size(); i++) {
            rawData.add(data.get(i));
        }
        if(includeEndRecordIndicator) {
            rawData.add(BlockDataConstants.EndOfEntry);
        }
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(BlockDataConstants.DataList);
        dataOutputStream.writeShort(listSize);
        for(int i = 0; i < data.size(); i++) {
            dataOutputStream.writeShort(data.get(i));
        }
        if(includeEndRecordIndicator) {
            dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);
        }
    }
}
