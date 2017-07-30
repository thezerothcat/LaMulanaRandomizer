package lmr.randomizer.dat;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public interface BlockContents {
    void writeBytes(DataOutputStream dataOutputStream) throws IOException;

    int getSize();
}
