package lmr.randomizer.dat;

import java.io.DataOutputStream;
import java.io.IOException;

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

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(data);
        dataOutputStream.writeShort(sceneData);
    }
}
