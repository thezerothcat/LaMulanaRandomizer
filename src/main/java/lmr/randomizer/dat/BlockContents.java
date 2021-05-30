package lmr.randomizer.dat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public interface BlockContents {
    List<Short> getRawData();

    void writeBytes(DataOutputStream dataOutputStream) throws IOException;

    int getSize();
}
