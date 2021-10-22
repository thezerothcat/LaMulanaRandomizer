package lmr.randomizer.dat.update;

import lmr.randomizer.FileUtils;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.blocks.contents.*;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.update.AddObject;
import lmr.randomizer.util.*;

import java.util.ArrayList;
import java.util.List;

public class Halloween2021DatUpdater extends DatUpdater {
    public Halloween2021DatUpdater(DatFileData datFileData) {
        super(datFileData);
    }

    @Override
    public void updateItemNames(ItemNameBlock itemNameBlock) {
        itemNameBlock.setName(ItemNameBlock.Map, buildTextEntry(Translations.getText("event.halloween.candy.name")));
        itemNameBlock.setName(ItemNameBlock.YellowMedicine, buildTextEntry(Translations.getText("event.halloween2021.yellowMedicine.name")));
        itemNameBlock.setName(ItemNameBlock.SecretTreasureOfLife, buildTextEntry(Translations.getText("event.halloween.candy.name")));
        itemNameBlock.setName(ItemNameBlock.SoftwareXmailer, buildTextEntry(Translations.getText("event.halloween2021.xmailer.name")));
    }

    @Override
    public void updateItemDescriptions(ItemDescriptionBlock itemDescriptionBlock) {
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.Map, buildTextEntry(Translations.getText("event.halloween.candy.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.YellowMedicine, buildTextEntry(Translations.getText("event.halloween2021.yellowMedicine.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.SecretTreasureOfLife, buildTextEntry(Translations.getText("event.halloween.candy.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.SoftwareXmailer, buildTextEntry(Translations.getText("event.halloween2021.xmailer.description")));
    }

    @Override
    public void updateMenuData(MenuBlock menuBlock) {
        menuBlock.setXmailerAppHeader(buildTextEntry(Translations.getText("event.halloween2021.xmailer.AppHeader")));
    }

    @Override
    public void updateSoftwareData(SoftwareBlock softwareBlock) {
        softwareBlock.setSoftwareName(SoftwareBlock.SoftwareXmailer, buildTextEntry(Translations.getText("event.halloween2021.xmailer.exe")));
    }

    @Override
    public void updateEmailBlock(Block emailBlock) {
        List<BlockContents> blockContents = emailBlock.getBlockContents();

        if(emailBlock.getBlockNumber() == BlockConstants.Email00) {
            blockContents.clear();
            blockContents.addAll(getEmailData(0));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email01) {
            blockContents.clear();
            blockContents.addAll(getEmailData(1));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email02) {
            blockContents.clear();
            blockContents.addAll(getEmailData(2));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email03) {
            blockContents.clear();
            blockContents.addAll(getEmailData(3));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email04) {
            blockContents.clear();
            blockContents.addAll(getEmailData(4));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email05) {
            blockContents.clear();
            blockContents.addAll(getEmailData(5));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email06) {
            blockContents.clear();
            blockContents.addAll(getEmailData(6));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email07) {
            blockContents.clear();
            blockContents.addAll(getEmailData(7));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email08) {
            blockContents.clear();
            blockContents.addAll(getEmailData(8));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email09) {
            blockContents.clear();
            blockContents.addAll(getEmailData(9));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email10) {
            blockContents.clear();
            blockContents.addAll(getEmailData(10));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email11) {
            blockContents.clear();
            blockContents.addAll(getEmailData(11));
        }
        else if(emailBlock.getBlockNumber() == BlockConstants.Email44) {
            blockContents.clear();
            blockContents.addAll(getEmailData(44));
        }
    }

    @Override
    void updateXmailerConversationBlock(Block conversationBlock) {
        List<BlockContents> xelpudBlockContents = conversationBlock.getBlockContents();
        xelpudBlockContents.clear();
        xelpudBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, 1));

        xelpudBlockContents.addAll(buildBlockContentsWithColor(
                Translations.getText("event.halloween2021.intro1"),
                Translations.getText("event.halloween.halloweenCandy"),
                BlockColorsData.COLOR_ITEMS_GREEN));
        xelpudBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        xelpudBlockContents.addAll(buildBlockContentsWithColor(
                Translations.getText("event.halloween2021.intro2"),
                Translations.getText("event.halloween2021.intro2a"),
                BlockColorsData.COLOR_THREATS_RED));
        xelpudBlockContents.add(new BlockItemData(ItemConstants.SOFTWARE_XMAILER));
        xelpudBlockContents.add(new BlockFlagData(FlagConstants.WF_SOFTWARE_XMAILER, 2));
        xelpudBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        xelpudBlockContents.addAll(buildBlockContentsWithColor(
                Translations.getText("event.halloween2021.intro3"),
                Translations.getText("event.halloween.halloweenCandy"),
                BlockColorsData.COLOR_ITEMS_GREEN));
        xelpudBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        xelpudBlockContents.addAll(buildBlockContents(Translations.getText("event.halloween2021.intro4")));
        xelpudBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        xelpudBlockContents.addAll(buildBlockContentsWithColor(
                Translations.getText("event.halloween2021.intro5"),
                Translations.getText("event.halloween.graphics"),
                BlockColorsData.COLOR_SOFTWARE_YELLOW));

        xelpudBlockContents.add(new BlockFlagData(FlagConstants.XELPUD_CONVERSATION_INTRO, 1)); // Talked to xelpud flag
        xelpudBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, 0)); // Can-exit flag
    }

    @Override
    void updateProvocativeBathingSuitConversationBlock(Block conversationBlock) {
        List<BlockContents> blockContents = conversationBlock.getBlockContents();
        blockContents.clear();
        blockContents.add(new BlockFlagData(FlagConstants.CUSTOM_HALLOWEEN2021_MULBRUK_DRACUET, 1));
        blockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, 1));

        blockContents.addAll(buildBlockContentsWithColor(Translations.getText("event.halloween2021.Dracuet1"),
                Translations.getText("items.AnkhJewel"), BlockColorsData.COLOR_ITEMS_GREEN));
        blockContents.add(new BlockItemData((short)ItemConstants.ANKH_JEWEL));
        blockContents.add(new BlockFlagData(FlagConstants.WF_ANKH_JEWEL_EXTRA, 2));
        blockContents.add(new BlockSingleData(BlockDataConstants.Cls));
        blockContents.addAll(buildBlockContents(Translations.getText("event.halloween2021.Dracuet2")));

        blockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, 0));
    }

    @Override
    void updateMulbrukHTConversationBlock(Block conversationBlock) {
        List<BlockContents> blockContents = conversationBlock.getBlockContents();
        blockContents.clear();
        blockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, 1));
        blockContents.add(new BlockFlagData(FlagConstants.HT_UNLOCK_PROGRESS_EARLY, 9)); // Skip all other Mulbruk HT conversations
        blockContents.add(new BlockFlagData(FlagConstants.CUSTOM_HALLOWEEN2021_MULBRUK_DRACUET, 1));

        blockContents.addAll(buildBlockContents(Translations.getText("event.halloween2021.MulbrukDracuet1")));
        blockContents.add(new BlockSingleData(BlockDataConstants.Cls));
        blockContents.addAll(buildBlockContentsWithColor(Translations.getText("event.halloween2021.MulbrukDracuet2"),
                Translations.getText("items.AnkhJewel"), BlockColorsData.COLOR_ITEMS_GREEN));
        blockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        blockContents.addAll(buildBlockContentsWithColor(Translations.getText("event.halloween2021.MulbrukDracuet3"),
                Translations.getText("locations.GateofGuidance"), BlockColorsData.COLOR_PEOPLE_PLACES_BLUE));

        blockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, 0));
    }

    @Override
    void updateSwimsuitEscapeConversationBlock(Block conversationBlock) {
        List<BlockContents> blockContents = conversationBlock.getBlockContents();
        blockContents.clear();
        blockContents.addAll(buildBlockContents(Translations.getText("event.halloween2021.MulbrukEscapeExtra")));
        blockContents.add(new BlockSingleData(BlockDataConstants.Cls));
        blockContents.add(new BlockSceneData());
    }

    @Override
    void updateXelpudFlagCheckBlock(CheckBlock flagCheckBlock) {
        // Update Xelpud with conversations for NPC counts
        for(int i = 17; i >= 0; i--) {
            BlockListData blockListData = new BlockListData((short)4);
            blockListData.getData().add((short)FlagConstants.ACHIEVEMENT_MAP_COUNT);
            blockListData.getData().add((short)i);
            blockListData.getData().add(getCustomBlockIndex(CustomBlockEnum.valueOf("XelpudConversationBlock_MapCount" + i)));
            blockListData.getData().add((short)0);
            flagCheckBlock.getFlagCheckReferences().add(5, blockListData);
        }
        BlockListData blockListData = new BlockListData((short)4);
        blockListData.getData().add(FlagConstants.CUSTOM_HALLOWEEN2021_XELPUD_BONUS_CANDY_CONVERSATIONS);
        blockListData.getData().add((short)3);
        blockListData.getData().add(getCustomBlockIndex(CustomBlockEnum.Halloween2021ConversationBlock_ExtraCandy2));
        blockListData.getData().add((short)0);
        flagCheckBlock.getFlagCheckReferences().add(5, blockListData);
        blockListData = new BlockListData((short)4);
        blockListData.getData().add(FlagConstants.CUSTOM_HALLOWEEN2021_XELPUD_BONUS_CANDY_CONVERSATIONS);
        blockListData.getData().add((short)1);
        blockListData.getData().add(getCustomBlockIndex(CustomBlockEnum.Halloween2021ConversationBlock_ExtraCandy1));
        blockListData.getData().add((short)0);
        flagCheckBlock.getFlagCheckReferences().add(5, blockListData);
    }

    private List<BlockContents> getEmailData(int mailNumber) {
        List<BlockContents> blockContents = new ArrayList<>();
        for (Short singleCharacter : FileUtils.stringToData(Translations.getText("event.halloween2021.mailTitle" + mailNumber))) {
            blockContents.add(new BlockSingleData(singleCharacter));
        }
        blockContents.add(new BlockSingleData(BlockDataConstants.EndOfEntry));
        for (Short singleCharacter : FileUtils.stringToData(Translations.getText("event.halloween2021.mailText" + mailNumber))) {
            blockContents.add(new BlockSingleData(singleCharacter));
        }
        return blockContents;
    }

    @Override
    public void addCustomBlocks(DatFileData datFileData) {
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenSecretShopBlock, AddObject.buildSecretShopBlock());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenDanceBlock, AddObject.buildDanceBlock());
        datFileData.addCustomBlock(CustomBlockEnum.Halloween2021ConversationBlock_ExtraCandy1, AddObject.buildBonusCandyBlock(1));
        datFileData.addCustomBlock(CustomBlockEnum.Halloween2021ConversationBlock_ExtraCandy2, AddObject.buildBonusCandyBlock(2));

        Block npcCountBlock;
        for(int i = 17; i >= 0; i--) {
            npcCountBlock = new Block();
            npcCountBlock.getBlockContents().addAll(buildBlockContents(String.format(Translations.getText("event.halloween2021.mapCount"), i, ValueConstants.TOTAL_MAPS)));
            datFileData.addCustomBlock(CustomBlockEnum.valueOf("XelpudConversationBlock_MapCount" + i), npcCountBlock);
        }
    }
}
