package lmr.randomizer.dat.blocks;

import lmr.randomizer.util.BlockDataConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapGraphicsBlock extends Block {
    private List<MapGraphicsEntry> roomGraphics = new ArrayList<>();

    public MapGraphicsBlock(int blockNumber) {
        super(blockNumber);
    }

    public void addRoom(MapGraphicsEntry mapGraphicsEntry) {
        roomGraphics.add(mapGraphicsEntry);
    }

    public List<MapGraphicsEntry> getMapGraphics() {
        return roomGraphics;
    }

    public MapGraphicsEntry getMapGraphics(int roomNumber) {
        return roomGraphics.get(roomNumber);
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add((short)getBlockSize());
        for(int i = 0; i < roomGraphics.size(); i++) {
            rawData.addAll(roomGraphics.get(i).getRawData());
            if(i < roomGraphics.size() - 1) {
                rawData.add(BlockDataConstants.EndOfEntry);
            }
        }
        return rawData;
    }

    @Override
    public int getBlockSize() {
        int size = 0;
        for(MapGraphicsEntry mapGraphicsEntry : roomGraphics) {
            size += mapGraphicsEntry.getSize() + 2;
        }
        size -= 2; // The final BlockDataConstants.EndOfEntry is omitted
        return size;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(getBlockSize());

        for(int i = 0; i < roomGraphics.size(); i++) {
            roomGraphics.get(i).writeBytes(dataOutputStream);
            if(i < roomGraphics.size() - 1) {
                dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);
            }
        }
    }
}
