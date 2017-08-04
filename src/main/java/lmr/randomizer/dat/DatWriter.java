package lmr.randomizer.dat;

import lmr.randomizer.Settings;

import java.io.*;
import java.util.List;

/**
 * Created by thezerothcat on 7/26/2017.
 */
public final class DatWriter {
    private DatWriter() {
    }

    public static void writeDat(List<Block> blockInfo) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(String.format("%d/script_code.dat", Settings.startingSeed)));
        dataOutputStream.writeShort(blockInfo.size());

        for(Block block : blockInfo) {
            block.write(dataOutputStream);
        }
        dataOutputStream.flush();
        dataOutputStream.close();
    }
}
