package lmr.randomizer.dat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public class BlockPoseData implements BlockContents {
    private short data;
    private short poseData;

    public BlockPoseData(short data, short poseData) {
        this.data = data;
        this.poseData = poseData;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(data);
        rawData.add(poseData);
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(data);
        dataOutputStream.writeShort(poseData);
    }
}
