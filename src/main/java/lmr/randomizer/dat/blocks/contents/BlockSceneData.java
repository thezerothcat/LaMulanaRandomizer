package lmr.randomizer.dat.blocks.contents;

import lmr.randomizer.util.BlockDataConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public class BlockSceneData implements BlockContents {
    private short data;

    public BlockSceneData(short data, short sceneData) {
        this.data = sceneData;
    }

    public BlockSceneData(int data) {
        this.data = (short)data;
    }

    /**
     * The only scene in the game (escape/credits)
     */
    public BlockSceneData() {
        this.data = (short)0;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(BlockDataConstants.Anime);
        rawData.add(data);
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(BlockDataConstants.Anime);
        dataOutputStream.writeShort(data);
    }
}
