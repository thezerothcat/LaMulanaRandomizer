package lmr.randomizer.dat.blocks.entries;

import lmr.randomizer.dat.BlockContents;
import lmr.randomizer.dat.BlockDataConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basically BlockStringData, but the 0x000a is handled automatically and there's no special handling for item name replacement.
 */
public class TextEntry implements BlockContents {
    private List<Short> data = new ArrayList<>();

    private boolean includeEndRecordIndicator;

    public TextEntry() {
        includeEndRecordIndicator = true;
    }

    public List<Short> getData() {
        return data;
    }

    public void setIncludeEndRecordIndicator(boolean includeEndRecordIndicator) {
        this.includeEndRecordIndicator = includeEndRecordIndicator;
    }

    @Override
    public int getSize() {
        return data.size() * 2 + (includeEndRecordIndicator ? 2 : 0);
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
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
        for(Short dataShort : data) {
            dataOutputStream.writeShort(dataShort);
        }
        if(includeEndRecordIndicator) {
            dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);
        }
    }
}
