package lmr.randomizer.update;

import lmr.randomizer.FileUtils;
import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.blocks.Block;
import lmr.randomizer.dat.blocks.MasterNpcBlock;
import lmr.randomizer.dat.blocks.ShopBlock;
import lmr.randomizer.dat.blocks.contents.*;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.util.BlockDataConstants;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.LocationCoordinateMapper;
import lmr.randomizer.util.ValueConstants;

import java.util.List;

public final class AddBlock {
    private AddBlock() { }

    public static Block buildCustomXelpudIntroBlock() {
        Block introBlock = new Block();
        introBlock.getBlockContents().add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText(HolidaySettings.isFools2019Mode() ? "fools.xelpudText" : "text.xelpudWarn"));
        for(Short shortCharacter : stringCharacters) {
            introBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        introBlock.getBlockContents().add(new BlockFlagData((short)FlagConstants.XELPUD_CONVERSATION_INTRO, (short)1));
        introBlock.getBlockContents().add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)0));
        return introBlock;
    }

    public static Block buildLoadSaveFileConversationBlock() {
        Block introBlock = new Block();
        introBlock.getBlockContents().add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("text.xelpudWarn"));
        for(Short shortCharacter : stringCharacters) {
            introBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        introBlock.getBlockContents().add(new BlockFlagData((short)FlagConstants.RANDOMIZER_SAVE_LOADED, (short)2));
        introBlock.getBlockContents().add(new BlockFlagData((short)FlagConstants.XELPUD_CONVERSATION_INTRO, (short)1));
        introBlock.getBlockContents().add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)0));
        return introBlock;
    }

    public static Block buildLoadSaveFileReferenceBlock(short loadSaveFileConversationBlock) {
        return new MasterNpcBlock(loadSaveFileConversationBlock,
                ValueConstants.NPC_BACKGROUND_TENT_CONVERSATION,
                ValueConstants.NPC_SPRITE_ELDER_XELPUD,
                ValueConstants.NPC_MUSIC_ELDER_XELPUD, new BlockStringData(Translations.getLocationAndNpc("ElderXelpud")));
    }

    public static Block buildRecordableStoneConversationBlock() {
        Block introBlock = new Block();
        introBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.Space));
        return introBlock;
    }

    public static MasterNpcBlock buildRecordableStoneConversationMasterBlock(CustomBlockEnum philosopherBlockEnum,
                                                                             int textBlockIndex) {
        BlockStringData blockStringData = new BlockStringData();
        if(CustomBlockEnum.RecordableStonePhilosopherGiltoriyo.equals(philosopherBlockEnum)) {
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PhilosopherGiltoriyo")));
        }
        else if(CustomBlockEnum.RecordableStonePhilosopherAlsedana.equals(philosopherBlockEnum)) {
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PhilosopherAlsedana")));
        }
        else if(CustomBlockEnum.RecordableStonePhilosopherSamaranta.equals(philosopherBlockEnum)) {
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PhilosopherSamaranta")));
        }
        else if(CustomBlockEnum.RecordableStonePhilosopherFobos.equals(philosopherBlockEnum)) {
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PhilosopherFobos")));
        }
        return new MasterNpcBlock(textBlockIndex, ValueConstants.NPC_BACKGROUND_STATUE, ValueConstants.NPC_SPRITE_USE_BACKGROUND, ValueConstants.NPC_MUSIC_STONE_PHILOSOPHER, blockStringData);
    }

    public static ShopBlock buildShopBlock() {
        ShopBlock shopBlock = new ShopBlock();

        BlockListData shopBlockData = new BlockListData((short)3);
        shopBlockData.getData().add((short)105);
        shopBlockData.getData().add((short)105);
        shopBlockData.getData().add((short)105);
        shopBlock.setInventoryItemArgsList(shopBlockData);

        shopBlockData = new BlockListData((short)3);
        shopBlockData.getData().add((short)10);
        shopBlockData.getData().add((short)10);
        shopBlockData.getData().add((short)10);
        shopBlock.setInventoryPriceList(shopBlockData);

        shopBlockData = new BlockListData((short)3);
        shopBlockData.getData().add((short)5);
        shopBlockData.getData().add((short)5);
        shopBlockData.getData().add((short)5);
        shopBlock.setInventoryCountList(shopBlockData);

        shopBlockData = new BlockListData((short)3);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlock.setFlagList(shopBlockData);

        shopBlockData = new BlockListData((short)3);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlock.setExitFlagList(shopBlockData);

        shopBlock.setBackground(new BlockCmdSingle((short)4));
        shopBlock.setSprite(new BlockCmdSingle((short)0x2dc));
        shopBlock.setMusic(new BlockCmdSingle((short)4));

        BlockStringData blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData("Weight , Weight , Weight"));
        shopBlock.setBunemonText(blockStringData);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText(
                "shop0.screenName.zone" + LocationCoordinateMapper.getStartingZone()
                        + (LocationCoordinateMapper.isFrontsideStart() ? ".front" : ".back"))));
        shopBlock.setBunemonLocation(blockStringData);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("text.intro")));
        shopBlock.setBunemonIntroText(blockStringData);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.yesPurchaseString")));
        shopBlock.setString(blockStringData, 0);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.noPurchaseString")));
        shopBlock.setString(blockStringData, 1);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("text.intro")));
        shopBlock.setString(blockStringData, 2);

        blockStringData = new BlockStringData();
        List<Short> data = FileUtils.stringToData(Translations.getText("shop0.askItem1String.1"));
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add(BlockDataConstants.ItemName);
        data.add((short)105);
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0);
        data.add((short)0);
        data.add((short)0);
        blockStringData.getData().addAll(data);
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.askItem1String.2")));
        shopBlock.setString(blockStringData, 3);

        blockStringData = new BlockStringData();
        data = FileUtils.stringToData(Translations.getText("shop0.askItem2String.1"));
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add(BlockDataConstants.ItemName);
        data.add((short)105);
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0);
        data.add((short)0);
        data.add((short)0);
        blockStringData.getData().addAll(data);
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.askItem2String.2")));
        shopBlock.setString(blockStringData, 4);

        blockStringData = new BlockStringData();
        data = FileUtils.stringToData(Translations.getText("shop0.askItem3String.1"));
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add(BlockDataConstants.ItemName);
        data.add((short)105);
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0);
        data.add((short)0);
        data.add((short)0);
        blockStringData.getData().addAll(data);
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.askItem3String.2")));
        shopBlock.setString(blockStringData, 5);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.boughtItem1String")));
        shopBlock.setString(blockStringData, 6);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.boughtItem2String")));
        shopBlock.setString(blockStringData, 7);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.boughtItem3String")));
        shopBlock.setString(blockStringData, 8);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.soldOutItem1String")));
        shopBlock.setString(blockStringData, 9);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.soldOutItem2String")));
        shopBlock.setString(blockStringData, 10);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.soldOutItem3String")));
        shopBlock.setString(blockStringData, 11);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.cancelItem1String")));
        shopBlock.setString(blockStringData, 12);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.cancelItem2String")));
        shopBlock.setString(blockStringData, 13);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.cancelItem3String")));
        shopBlock.setString(blockStringData, 14);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.noMoneyItem1String")));
        shopBlock.setString(blockStringData, 15);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.noMoneyItem2String")));
        shopBlock.setString(blockStringData, 16);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.noMoneyItem3String")));
        shopBlock.setString(blockStringData, 17);
        return shopBlock;
    }

}
