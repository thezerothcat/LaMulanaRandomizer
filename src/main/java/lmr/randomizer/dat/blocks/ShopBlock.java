package lmr.randomizer.dat.blocks;

import lmr.randomizer.util.BlockDataConstants;
import lmr.randomizer.dat.blocks.contents.BlockListData;
import lmr.randomizer.dat.blocks.contents.BlockCmdSingle;
import lmr.randomizer.dat.blocks.contents.BlockStringData;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/26/2017.
 */
public class ShopBlock extends Block {
    private BlockListData inventoryItemArgsList;
    private BlockListData inventoryCountList;
    private BlockListData inventoryPriceList;
    private BlockListData flagList; // These all get incremented by one when exiting the shop if you bought the item.

    private BlockStringData yesPurchaseString;
    private BlockStringData noPurchaseString;
    private BlockStringData introTextString;

    private BlockStringData askItem1String;
    private BlockStringData askItem2String;
    private BlockStringData askItem3String;

    private BlockStringData boughtItem1String;
    private BlockStringData boughtItem2String;
    private BlockStringData boughtItem3String;

    private BlockStringData soldOutItem1String;
    private BlockStringData soldOutItem2String;
    private BlockStringData soldOutItem3String;

    private BlockStringData cancelItem1String;
    private BlockStringData cancelItem2String;
    private BlockStringData cancelItem3String;

    private BlockStringData noMoneyItem1String;
    private BlockStringData noMoneyItem2String;
    private BlockStringData noMoneyItem3String;

    private BlockCmdSingle background;
    private BlockCmdSingle sprite;

    private BlockListData exitFlagList; // These all get incremented by one when exiting the shop if you bought the item.

    private BlockCmdSingle music;

    private BlockStringData bunemonLocation;
    private BlockStringData bunemonText;
    private BlockStringData bunemonIntroText;

    public ShopBlock() {
        super();
    }

    public ShopBlock(int blockNumber) {
        super(blockNumber);
    }

    public ShopBlock(ShopBlock blockToCopy, int blockNumber) {
        super(blockNumber);

        inventoryItemArgsList = new BlockListData(blockToCopy.getInventoryItemArgsList());
        inventoryCountList = new BlockListData(blockToCopy.getInventoryCountList());
        inventoryPriceList = new BlockListData(blockToCopy.getInventoryPriceList());
        flagList = new BlockListData(blockToCopy.getFlagList());

        for(int i = 0; i < 18; i++) {
            this.setString(new BlockStringData(blockToCopy.getString(i)), i);
        }

        background = new BlockCmdSingle(blockToCopy.getBackground());
        sprite = new BlockCmdSingle(blockToCopy.getSprite());

        exitFlagList = new BlockListData(blockToCopy.getExitFlagList());

        music = new BlockCmdSingle(blockToCopy.getMusic());

        bunemonLocation = new BlockStringData(blockToCopy.getBunemonLocation());
        bunemonText = new BlockStringData(blockToCopy.getBunemonText());
        bunemonIntroText = new BlockStringData(blockToCopy.getBunemonIntroText());
    }

    public ShopBlock(ShopBlock blockToCopy) {
        super();

        inventoryItemArgsList = new BlockListData(blockToCopy.getInventoryItemArgsList());
        inventoryCountList = new BlockListData(blockToCopy.getInventoryCountList());
        inventoryPriceList = new BlockListData(blockToCopy.getInventoryPriceList());
        flagList = new BlockListData(blockToCopy.getFlagList());

        for(int i = 0; i < 18; i++) {
            this.setString(new BlockStringData(blockToCopy.getString(i)), i);
        }

        background = new BlockCmdSingle(blockToCopy.getBackground());
        sprite = new BlockCmdSingle(blockToCopy.getSprite());

        exitFlagList = new BlockListData(blockToCopy.getExitFlagList());

        music = new BlockCmdSingle(blockToCopy.getMusic());

        bunemonLocation = new BlockStringData(blockToCopy.getBunemonLocation());
        bunemonText = new BlockStringData(blockToCopy.getBunemonText());
        bunemonIntroText = new BlockStringData(blockToCopy.getBunemonIntroText());
    }

    public BlockStringData getString(int index) {
        if(index == 0) {
            return yesPurchaseString;
        }
        else if(index == 1) {
            return noPurchaseString;
        }
        else if(index == 2) {
            return introTextString;
        }
        else if(index == 3) {
            return askItem1String;
        }
        else if(index == 4) {
            return askItem2String;
        }
        else if(index == 5) {
            return askItem3String;
        }
        else if(index == 6) {
            return boughtItem1String;
        }
        else if(index == 7) {
            return boughtItem2String;
        }
        else if(index == 8) {
            return boughtItem3String;
        }
        else if(index == 9) {
            return soldOutItem1String;
        }
        else if(index == 10) {
            return soldOutItem2String;
        }
        else if(index == 11) {
            return soldOutItem3String;
        }
        else if(index == 12) {
            return cancelItem1String;
        }
        else if(index == 13) {
            return cancelItem2String;
        }
        else if(index == 14) {
            return cancelItem3String;
        }
        else if(index == 15) {
            return noMoneyItem1String;
        }
        else if(index == 16) {
            return noMoneyItem2String;
        }
        else if(index == 17) {
            return noMoneyItem3String;
        }
        return null;
    }

    public void setString(BlockStringData blockStringData, int index) {
        if(index == 0) {
            yesPurchaseString = blockStringData;
        }
        else if(index == 1) {
            noPurchaseString = blockStringData;
        }
        else if(index == 2) {
            introTextString = blockStringData;
        }
        else if(index == 3) {
            askItem1String = blockStringData;
        }
        else if(index == 4) {
            askItem2String = blockStringData;
        }
        else if(index == 5) {
            askItem3String = blockStringData;
        }
        else if(index == 6) {
            boughtItem1String = blockStringData;
        }
        else if(index == 7) {
            boughtItem2String = blockStringData;
        }
        else if(index == 8) {
            boughtItem3String = blockStringData;
        }
        else if(index == 9) {
            soldOutItem1String = blockStringData;
        }
        else if(index == 10) {
            soldOutItem2String = blockStringData;
        }
        else if(index == 11) {
            soldOutItem3String = blockStringData;
        }
        else if(index == 12) {
            cancelItem1String = blockStringData;
        }
        else if(index == 13) {
            cancelItem2String = blockStringData;
        }
        else if(index == 14) {
            cancelItem3String = blockStringData;
        }
        else if(index == 15) {
            noMoneyItem1String = blockStringData;
        }
        else if(index == 16) {
            noMoneyItem2String = blockStringData;
        }
        else if(index == 17) {
            noMoneyItem3String = blockStringData;
        }
    }

    public BlockListData getInventoryItemArgsList() {
        return inventoryItemArgsList;
    }

    public void setInventoryItemArgsList(BlockListData inventoryItemArgsList) {
        this.inventoryItemArgsList = inventoryItemArgsList;
    }

    public BlockListData getInventoryCountList() {
        return inventoryCountList;
    }

    public void setInventoryCountList(BlockListData inventoryCountList) {
        this.inventoryCountList = inventoryCountList;
    }

    public BlockListData getInventoryPriceList() {
        return inventoryPriceList;
    }

    public void setInventoryPriceList(BlockListData inventoryPriceList) {
        this.inventoryPriceList = inventoryPriceList;
    }

    public BlockListData getFlagList() {
        return flagList;
    }

    public void setFlagList(BlockListData flagList) {
        this.flagList = flagList;
    }

    public BlockCmdSingle getBackground() {
        return background;
    }

    public void setBackground(BlockCmdSingle background) {
        this.background = background;
    }

    public BlockCmdSingle getSprite() {
        return sprite;
    }

    public void setSprite(BlockCmdSingle sprite) {
        this.sprite = sprite;
    }

    public BlockListData getExitFlagList() {
        return exitFlagList;
    }

    public void setExitFlagList(BlockListData exitFlagList) {
        this.exitFlagList = exitFlagList;
    }

    public BlockCmdSingle getMusic() {
        return music;
    }

    public void setMusic(BlockCmdSingle music) {
        this.music = music;
    }

    public BlockStringData getBunemonLocation() {
        return bunemonLocation;
    }

    public void setBunemonLocation(BlockStringData bunemonLocation) {
        this.bunemonLocation = bunemonLocation;
    }

    public BlockStringData getBunemonText() {
        return bunemonText;
    }

    public void setBunemonText(BlockStringData bunemonText) {
        this.bunemonText = bunemonText;
    }

    public BlockStringData getBunemonIntroText() {
        return bunemonIntroText;
    }

    public void setBunemonIntroText(BlockStringData bunemonIntroText) {
        this.bunemonIntroText = bunemonIntroText;
    }

    public short getItem1Price() {
        return inventoryPriceList.getData().get(0);
    }
    public void setItem1Price(int price) {
        inventoryPriceList.getData().set(0, (short)price);
    }
    public short getItem2Price() {
        return inventoryPriceList.getData().get(1);
    }
    public void setItem2Price(int price) {
        inventoryPriceList.getData().set(1, (short)price);
    }
    public short getItem3Price() {
        return inventoryPriceList.getData().get(2);
    }
    public void setItem3Price(int price) {
        inventoryPriceList.getData().set(2, (short)price);
    }

    public short getItem1Count() {
        return inventoryCountList.getData().get(0);
    }
    public void setItem1Count(int count) {
        inventoryCountList.getData().set(0, (short)count);
    }
    public short getItem2Count() {
        return inventoryCountList.getData().get(1);
    }
    public void setItem2Count(int count) {
        inventoryCountList.getData().set(1, (short)count);
    }
    public short getItem3Count() {
        return inventoryCountList.getData().get(2);
    }
    public void setItem3Count(int count) {
        inventoryCountList.getData().set(2, (short)count);
    }

    @Override
    public int getBlockSize() {
        int size = 0;
        size += inventoryItemArgsList.getSize() + 2;
        size += inventoryCountList.getSize() + 2;
        size += inventoryPriceList.getSize() + 2;
        size += flagList.getSize() + 2;

        for(int i = 0; i < 18; i++) {
            size += getString(i).getSize() + 2;
        }

        size += background.getSize();
        size += sprite.getSize();

        size += exitFlagList.getSize() + 2;

        size += music.getSize();

        size += bunemonLocation.getSize() + 2;
        size += bunemonText.getSize() + 2; // Newline
        size += bunemonIntroText.getSize(); // No 0x000a after this one
        return size;
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add((short)getBlockSize());

        rawData.addAll(inventoryItemArgsList.getRawData());
        rawData.add(BlockDataConstants.EndOfEntry);

        rawData.addAll(inventoryPriceList.getRawData());
        rawData.add(BlockDataConstants.EndOfEntry);

        rawData.addAll(inventoryCountList.getRawData());
        rawData.add(BlockDataConstants.EndOfEntry);

        rawData.addAll(flagList.getRawData());
        rawData.add(BlockDataConstants.EndOfEntry);

        for(int i = 0; i < 18; i++) {
            rawData.addAll(getString(i).getRawData());
            rawData.add(BlockDataConstants.EndOfEntry);
        }

        rawData.addAll(background.getRawData());
        rawData.addAll(sprite.getRawData());

        rawData.addAll(exitFlagList.getRawData());
        rawData.add(BlockDataConstants.EndOfEntry);

        rawData.addAll(music.getRawData());

        rawData.addAll(bunemonLocation.getRawData());
        rawData.add(BlockDataConstants.EndOfEntry);

        rawData.addAll(bunemonText.getRawData());
        rawData.add(BlockDataConstants.Newline);
        rawData.addAll(bunemonIntroText.getRawData());
        return rawData;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(getBlockSize());

        inventoryItemArgsList.writeBytes(dataOutputStream);
        dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);

        inventoryPriceList.writeBytes(dataOutputStream);
        dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);

        inventoryCountList.writeBytes(dataOutputStream);
        dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);

        flagList.writeBytes(dataOutputStream);
        dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);

        for(int i = 0; i < 18; i++) {
            getString(i).writeBytes(dataOutputStream);
            dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);
        }

        background.writeBytes(dataOutputStream);
        sprite.writeBytes(dataOutputStream);

        exitFlagList.writeBytes(dataOutputStream);
        dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);

        music.writeBytes(dataOutputStream);

        bunemonLocation.writeBytes(dataOutputStream);
        dataOutputStream.writeShort(BlockDataConstants.EndOfEntry);

        bunemonText.writeBytes(dataOutputStream);
        dataOutputStream.writeShort(BlockDataConstants.Newline);
        bunemonIntroText.writeBytes(dataOutputStream);
    }
}
