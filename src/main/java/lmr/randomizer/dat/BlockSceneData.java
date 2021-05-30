package lmr.randomizer.dat;

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
