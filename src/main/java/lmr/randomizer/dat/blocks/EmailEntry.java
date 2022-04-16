package lmr.randomizer.dat.blocks;

import lmr.randomizer.dat.blocks.contents.BlockContents;
import lmr.randomizer.util.BlockDataConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmailEntry implements BlockContents {
    private int mailFlag;
    private int mailBlockNumber;

    public EmailEntry(int mailFlag, int mailBlockNumber) {
        this.mailFlag = mailFlag;
        this.mailBlockNumber = mailBlockNumber;
    }

    public int getMailFlag() {
        return mailFlag;
    }

    public void setMailFlag(int mailFlag) {
        this.mailFlag = mailFlag;
    }

    public int getMailBlockNumber() {
        return mailBlockNumber;
    }

    @Override
    public int getSize() {
        return 8; // CMD, list length, then two items
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(BlockDataConstants.DataList);
        rawData.add((short)2);
        rawData.add((short)mailFlag);
        rawData.add((short)mailBlockNumber);
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(BlockDataConstants.DataList);
        dataOutputStream.writeShort(2);
        dataOutputStream.writeShort(mailFlag);
        dataOutputStream.writeShort(mailBlockNumber);
    }
}
