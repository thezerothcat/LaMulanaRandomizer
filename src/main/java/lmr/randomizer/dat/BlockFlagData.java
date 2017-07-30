package lmr.randomizer.dat;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by thezerothcat on 7/27/2017.
 */
public class BlockFlagData implements BlockContents {
    private short data;

    private short worldFlag;
    private short flagValue;

    public BlockFlagData(short data, short worldFlag, short flagValue) {
        this.data = data;
        this.worldFlag = worldFlag;
        this.flagValue = flagValue;
    }

    public short getWorldFlag() {
        return worldFlag;
    }

    public void setWorldFlag(short worldFlag) {
        this.worldFlag = worldFlag;
    }

    public short getFlagValue() {
        return flagValue;
    }

    public void setFlagValue(short flagValue) {
        this.flagValue = flagValue;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(data);
        dataOutputStream.writeShort(worldFlag);
        dataOutputStream.writeShort(flagValue);
    }
}
