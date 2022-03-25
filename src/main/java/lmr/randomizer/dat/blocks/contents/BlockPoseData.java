package lmr.randomizer.dat.blocks.contents;

import lmr.randomizer.util.BlockDataConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public class BlockPoseData implements BlockContents {
    private short poseData;

    public BlockPoseData(int poseData) {
        this.poseData = (short)poseData;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(BlockDataConstants.Pose);
        rawData.add(poseData);
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(BlockDataConstants.Pose);
        dataOutputStream.writeShort(poseData);
    }
}
