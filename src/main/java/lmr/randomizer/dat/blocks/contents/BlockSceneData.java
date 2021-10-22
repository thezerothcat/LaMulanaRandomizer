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
    private short sceneData;

    public BlockSceneData(short data, short sceneData) {
        this.data = data;
        this.sceneData = sceneData;
    }

    /**
     * The only scene in the game (escape/credits)
     */
    public BlockSceneData() {
        this.data = BlockDataConstants.Anime;
        this.sceneData = (short)0;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(data);
        rawData.add(sceneData);
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(data);
        dataOutputStream.writeShort(sceneData);
    }
}
