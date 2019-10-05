package lmr.randomizer.dat.shop;

import lmr.randomizer.dat.Block;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by thezerothcat on 9/8/2019.
 */
public class MasterNpcBlock extends Block {
    private BlockCmdSingle textCard;
    private BlockCmdSingle background;
    private BlockCmdSingle npcCard;
    private BlockCmdSingle music;

    private BlockStringData npcName;

    public MasterNpcBlock(int blockNumber) {
        super(blockNumber);
    }

    public MasterNpcBlock(MasterNpcBlock blockToCopy, int blockNumber) {
        super(blockNumber);
        this.textCard = new BlockCmdSingle(blockToCopy.getTextCard());
        this.background = new BlockCmdSingle(blockToCopy.getBackground());
        this.npcCard = new BlockCmdSingle(blockToCopy.getNpcCard());
        this.music = new BlockCmdSingle(blockToCopy.getMusic());
        this.npcName = new BlockStringData(blockToCopy.getNpcName());
    }

    public BlockCmdSingle getTextCard() {
        return textCard;
    }

    public void setTextCard(BlockCmdSingle textCard) {
        this.textCard = textCard;
    }

    public BlockCmdSingle getBackground() {
        return background;
    }

    public void setBackground(BlockCmdSingle background) {
        this.background = background;
    }

    public BlockCmdSingle getNpcCard() {
        return npcCard;
    }

    public void setNpcCard(BlockCmdSingle npcCard) {
        this.npcCard = npcCard;
    }

    public BlockCmdSingle getMusic() {
        return music;
    }

    public void setMusic(BlockCmdSingle music) {
        this.music = music;
    }

    public BlockStringData getNpcName() {
        return npcName;
    }

    public void setNpcName(BlockStringData npcName) {
        this.npcName = npcName;
    }

    @Override
    public int getBlockSize() {
        int size = textCard.getSize();
        size += background.getSize();
        size += npcCard.getSize();
        size += music.getSize();
        size += npcName.getSize();
        return size;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(getBlockSize());

        textCard.writeBytes(dataOutputStream);
        background.writeBytes(dataOutputStream);
        npcCard.writeBytes(dataOutputStream);
        music.writeBytes(dataOutputStream);

        npcName.writeBytes(dataOutputStream);
    }
}
