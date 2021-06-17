package lmr.randomizer.dat.update;

import lmr.randomizer.*;
import lmr.randomizer.dat.*;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.blocks.CheckBlock;
import lmr.randomizer.dat.blocks.contents.*;
import lmr.randomizer.update.AddObject;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.util.BlockDataConstants;
import lmr.randomizer.FileUtils;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.ItemConstants;

import java.util.ArrayList;
import java.util.List;

public class HalloweenDatUpdater extends DatUpdater {
    public HalloweenDatUpdater(DatFileData datFileData) {
        super(datFileData);
    }

    @Override
    public void updateItemNames(ItemNameBlock itemNameBlock) {
        itemNameBlock.setName(ItemNameBlock.SecretTreasureOfLife, buildTextEntry(Translations.getText("event.halloween.candy.name")));
    }

    @Override
    public void updateItemDescriptions(ItemDescriptionBlock itemDescriptionBlock) {
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.SecretTreasureOfLife, buildTextEntry(Translations.getText("event.halloween.candy.description")));
    }

    @Override
    public void updateHTMapNamesLimitedBlock(MapNamesLimitedBlock mapNamesLimitedBlock) {
        mapNamesLimitedBlock.setFieldName(buildTextEntry(Translations.getText("event.halloween.HTFieldName")));
    }

    @Override
    public void updateFairyQueenFirstConversationBlock(Block conversationBlock) {
        List<BlockContents> blockContents = conversationBlock.getBlockContents();
        blockContents.clear();
        blockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.text1"));
        for (Short shortCharacter : stringCharacters) {
            blockContents.add(new BlockSingleData(shortCharacter));
        }
        blockContents.add(new BlockItemData(ItemConstants.SECRET_TREASURE_OF_LIFE));
        blockContents.add(new BlockFlagData((short)FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, (short)1));
        blockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.text2"));
        for (Short shortCharacter : stringCharacters) {
            blockContents.add(new BlockSingleData(shortCharacter));
        }
        blockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)0));
    }

    @Override
    public void updateFairyQueenWhenTheTimeComesConversationBlock(Block conversationBlock) {
        List<BlockContents> blockContents = conversationBlock.getBlockContents();
        blockContents.clear();
        blockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.fairyqueen"));
        for (Short shortCharacter : stringCharacters) {
            blockContents.add(new BlockSingleData(shortCharacter));
        }
        blockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)0));
    }

    @Override
    void updateMulbrukIntroConversationBlock(Block conversationBlock) {
        List<BlockContents> mulbrukBlockContents = conversationBlock.getBlockContents();
        mulbrukBlockContents.clear();
        mulbrukBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));
        mulbrukBlockContents.add(new BlockFlagData((short)FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_CONVERSATION, (short) 1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.mulbruk1"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.mulbruk2"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.mulbruk3"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        String text = Translations.getText("event.halloween.mulbruk4");
        String[] texts = text.split("%s");
        if(texts.length > 0) {
            stringCharacters = FileUtils.stringToData(texts[0]);
            for (Short shortCharacter : stringCharacters) {
                mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
            }
        }

        mulbrukBlockContents.add(new BlockColorsData((short)0x96, (short)0, (short)0x64));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.halloweenCandy"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockColorsData((short)0, (short)0, (short)0));

        stringCharacters = FileUtils.stringToData(texts[texts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));
        mulbrukBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)0));

        // Allow repeat
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.repeat"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        BlockListData repeatCmd = new BlockListData((short)1);
        repeatCmd.getData().add((short)conversationBlock.getBlockNumber()); // Re-use the same block, since this doesn't really affect much.
        mulbrukBlockContents.add(repeatCmd);
        mulbrukBlockContents.add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.repeat.yes"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.repeat.no"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.mulbruk5"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukBlockContents.add(new BlockSingleData(BlockDataConstants.EndOfEntry));
    }

    @Override
    void updateXmailerConversationBlock(Block conversationBlock) {
        List<BlockContents> xelpudBlockContents = conversationBlock.getBlockContents();
        xelpudBlockContents.clear();
        xelpudBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));
        xelpudBlockContents.add(new BlockFlagData((short)0xaa7, (short)1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.intro1"));
        for (Short shortCharacter : stringCharacters) {
            xelpudBlockContents.add(new BlockSingleData(shortCharacter));
        }
        xelpudBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.intro2"));
        for (Short shortCharacter : stringCharacters) {
            xelpudBlockContents.add(new BlockSingleData(shortCharacter));
        }
        xelpudBlockContents.add(new BlockItemData(ItemConstants.SOFTWARE_XMAILER));
        xelpudBlockContents.add(new BlockFlagData((short)FlagConstants.WF_SOFTWARE_XMAILER, (short)2));
        xelpudBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        String text = Translations.getText("event.halloween.intro3");
        String[] texts = text.split("%s");
        if(texts.length > 0) {
            stringCharacters = FileUtils.stringToData(texts[0]);
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }
        }

        xelpudBlockContents.add(new BlockColorsData((short)0, (short)0x32, (short)0x96));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.graphics"));
        for (Short shortCharacter : stringCharacters) {
            xelpudBlockContents.add(new BlockSingleData(shortCharacter));
        }
        xelpudBlockContents.add(new BlockColorsData((short)0, (short)0, (short)0));

        stringCharacters = FileUtils.stringToData(texts[texts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            xelpudBlockContents.add(new BlockSingleData(shortCharacter));
        }
        xelpudBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.intro4"));
        for (Short shortCharacter : stringCharacters) {
            xelpudBlockContents.add(new BlockSingleData(shortCharacter));
        }
        xelpudBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        text = Translations.getText("event.halloween.intro5");
        texts = text.split("%s");
        if(texts.length > 0) {
            stringCharacters = FileUtils.stringToData(texts[0]);
            for (Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }
        }

        xelpudBlockContents.add(new BlockColorsData((short)0x96, (short)0x32, (short)0));
        stringCharacters = FileUtils.stringToData(Translations.getText("locations.TempleoftheSun"));
        for (Short shortCharacter : stringCharacters) {
            xelpudBlockContents.add(new BlockSingleData(shortCharacter));
        }
        xelpudBlockContents.add(new BlockColorsData((short)0, (short)0, (short)0));

        stringCharacters = FileUtils.stringToData(texts[texts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            xelpudBlockContents.add(new BlockSingleData(shortCharacter));
        }

        xelpudBlockContents.add(new BlockFlagData((short)FlagConstants.XELPUD_CONVERSATION_INTRO, (short)1)); // Talked to xelpud flag
        xelpudBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)0)); // Can-exit flag
    }

    @Override
    void updateProvocativeBathingSuitConversationBlock(Block conversationBlock) {
        if(Settings.isIncludeHellTempleNPCs()) {
            List<BlockContents> swimsuitBlockContents = conversationBlock.getBlockContents();
            swimsuitBlockContents.clear();
            swimsuitBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));
            swimsuitBlockContents.add(new BlockFlagData((short)0xaa7, (short) 1));
            List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.dracuet1"));
            for (Short shortCharacter : stringCharacters) {
                swimsuitBlockContents.add(new BlockSingleData(shortCharacter));
            }
            swimsuitBlockContents.add(new BlockItemData(ItemConstants.PROVOCATIVE_BATHING_SUIT)); // Swimsuit
            swimsuitBlockContents.add(new BlockFlagData((short)0x7ea, (short) 1)); // Solve room 27 so you can get out
            swimsuitBlockContents.add(new BlockFlagData((short)0x7f8, (short) 1)); // Solve room 27 so you can get out
            swimsuitBlockContents.add(new BlockFlagData((short)FlagConstants.MULBRUK_BIKINI_ENDING, (short)1)); // Mulbruk Halloween costume
            swimsuitBlockContents.add(new BlockFlagData((short)FlagConstants.WF_PROVOCATIVE_BATHING_SUIT, (short)1)); // Mulbruk Halloween costume
            swimsuitBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

            stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.dracuet2"));
            for (Short shortCharacter : stringCharacters) {
                swimsuitBlockContents.add(new BlockSingleData(shortCharacter));
            }
            swimsuitBlockContents.add(new BlockFlagData((short)FlagConstants.CUSTOM_HALLOWEEN_FINAL_DRACUET, (short)1)); // Dracuet flag
            swimsuitBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)0)); // Can-exit flag
        }
    }

    @Override
    void updateXelpudFlagCheckBlock(CheckBlock flagCheckBlock) {
        // Update Xelpud with conversations for NPC counts
        for(int i = 28; i >= 0; i--) {
            BlockListData blockListData = new BlockListData((short)4);
            blockListData.getData().add((short)FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT);
            blockListData.getData().add((short)i);
            blockListData.getData().add(getCustomBlockIndex(CustomBlockEnum.valueOf("HalloweenConversationBlock_NpcCount" + i)));
            blockListData.getData().add((short)0);
            flagCheckBlock.getFlagCheckReferences().add(4, blockListData);
        }
        if(Settings.isIncludeHellTempleNPCs()) {
            BlockListData blockListData = new BlockListData((short)4);
            blockListData.getData().add((short)FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT);
            blockListData.getData().add((short)29);
            blockListData.getData().add(getCustomBlockIndex(CustomBlockEnum.HalloweenConversationBlock_AllNpcs));
            blockListData.getData().add((short)0);
            flagCheckBlock.getFlagCheckReferences().add(4, blockListData);
        }
    }

    @Override
    public void updateMulbrukFlagCheckBlock(CheckBlock flagCheckBlock) {
        // Update Mulbruk with Helloween Temple conversation
        List<BlockListData> halloweenFlagCheckReferences = new ArrayList<>();
        for(BlockListData blockListData : flagCheckBlock.getFlagCheckReferences()) {
            short blockNum = blockListData.getData().get(2);
            if(blockNum == 0x18d) {
                halloweenFlagCheckReferences.add(blockListData);
            }
            else if(blockNum == 0x19e || blockNum == 0x19f || blockNum == 0x1a0 || blockNum == 0x1a1
                    || blockNum == 0x188 || blockNum == 0x18c || blockNum == 0x18e || blockNum == 0x18f
                    || blockNum == 0x194 || blockNum == 0x195 || blockNum == 0x196 || blockNum == 0x190
                    || blockNum == 0x191 || blockNum == 0x193 || blockNum == 0x199) {
                // Unnecessary conversations
                continue;
            }
            else if(blockNum == 393) { // 0x189
                BlockListData mulbrukBlockListData = new BlockListData((short)4);
                mulbrukBlockListData.getData().add((short)FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_CONVERSATION);
                mulbrukBlockListData.getData().add((short)0);
                mulbrukBlockListData.getData().add((short)393);
                mulbrukBlockListData.getData().add((short)1);
                halloweenFlagCheckReferences.add(0, mulbrukBlockListData);
            }
            else {
                halloweenFlagCheckReferences.add(blockListData);
            }
        }
        flagCheckBlock.getFlagCheckReferences().clear();
        flagCheckBlock.getFlagCheckReferences().addAll(halloweenFlagCheckReferences);

        if(Settings.isIncludeHellTempleNPCs()) {
            BlockListData blockListData = new BlockListData((short)4);
            blockListData.getData().add((short)FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_CONVERSATION);
            blockListData.getData().add((short)2);
            blockListData.getData().add(getCustomBlockIndex(CustomBlockEnum.HalloweenConversationBlock_Mulbruk_HT));
            blockListData.getData().add((short)0);
            flagCheckBlock.getFlagCheckReferences().add(blockListData);
        }

        // Update Mulbruk with hints for NPCs
        final int npcHintCount = 17;
        for(int i = 1; i <= npcHintCount; i++) {
            // Add to flag-based.
            BlockListData blockListData = new BlockListData((short)4);
            blockListData.getData().add((short)FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_HINT);
            blockListData.getData().add((short)(i - 1));
            blockListData.getData().add(getCustomBlockIndex(CustomBlockEnum.valueOf("HalloweenConversationBlock_Flag_NpcHint" + i)));
            blockListData.getData().add((short)0);
            flagCheckBlock.getFlagCheckReferences().add(blockListData);
        }
        BlockListData blockListData = new BlockListData((short)4);
        blockListData.getData().add((short)FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_HINT);
        blockListData.getData().add((short)npcHintCount);
        blockListData.getData().add(getCustomBlockIndex(CustomBlockEnum.HalloweenConversationBlock_Flag_DevRoomHint));
        blockListData.getData().add((short)0);
        flagCheckBlock.getFlagCheckReferences().add(blockListData);
    }

    @Override
    public void updateMulbrukScoreCheckBlock(CheckBlock scoreCheckBlock) {
        // Update Mulbruk to not have score conversations so the random ones will take precedence
        scoreCheckBlock.getFlagCheckReferences().clear();
    }

    @Override
    public void updateMulbrukRandomBlocks(List<Block> randomBlocks) {
        // Update Mulbruk with hints for NPCs
        final int npcHintCount = 17;
        BlockListData blockListData;
        for(Block randomBlock : randomBlocks) {
            randomBlock.getBlockContents().clear();

            for(int i = 1; i <= npcHintCount; i++) {
                blockListData = new BlockListData((short)2);
                blockListData.getData().add(getCustomBlockIndex(CustomBlockEnum.valueOf("HalloweenConversationBlock_Random_NpcHint" + i)));
                blockListData.getData().add((short)0);
                randomBlock.getBlockContents().add(blockListData);
                randomBlock.getBlockContents().add(new BlockSingleData((short)10));
            }

            blockListData = new BlockListData((short)2);
            blockListData.getData().add(getCustomBlockIndex(CustomBlockEnum.HalloweenConversationBlock_Random_DevRoomHint));
            blockListData.getData().add((short)0);
            randomBlock.getBlockContents().add(blockListData);
        }
    }

    @Override
    public void addCustomBlocks(DatFileData datFileData) {
        Block noCandyConversationBlock = AddObject.buildNoCandyConversationBlock();
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenNoCandyConversationBlock, noCandyConversationBlock);

        Block candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestZarnac);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestZarnac, candyConversationBlock);
        Block masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestZarnac, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestZarnac, masterBlock);

        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetWaitForNightfall, noCandyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetWaitForNightfall, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_Hiner);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_Hiner, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_Hiner, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_Hiner, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_Moger);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_Moger, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_Moger, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_Moger, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_FormerMekuriMaster);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_FormerMekuriMaster, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_FormerMekuriMaster, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_FormerMekuriMaster, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestXanado);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestXanado, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestXanado, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestXanado, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestMadomono);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestMadomono, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestMadomono, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestMadomono, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherGiltoriyo);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherGiltoriyo, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherGiltoriyo, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherGiltoriyo, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestHidlyda);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestHidlyda, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestHidlyda, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestHidlyda, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestGailious);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestGailious, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestGailious, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestGailious, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestRomancis);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestRomancis, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestRomancis, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestRomancis, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestAramo);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestAramo, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestAramo, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestAramo, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestTriton);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestTriton, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestTriton, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestTriton, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestJaguarfiv);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestJaguarfiv, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestJaguarfiv, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestJaguarfiv, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_StrayFairy);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_StrayFairy, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_StrayFairy, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_StrayFairy, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_duplex);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_duplex, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_duplex, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_duplex, masterBlock);

        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetBackInTime, noCandyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetBackInTime, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_MrSlushfund);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_MrSlushfund, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_MrSlushfund, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_MrSlushfund, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestAlest);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestAlest, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestAlest, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestAlest, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_GiantThexde);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_GiantThexde, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_GiantThexde, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_GiantThexde, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_Samieru);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_Samieru, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_Samieru, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_Samieru, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherAlsedana);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherAlsedana, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherAlsedana, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherAlsedana, masterBlock);

        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetHTUnlocked, noCandyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetHTUnlocked, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherSamaranta);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherSamaranta, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherSamaranta, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherSamaranta, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_Naramura);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_Naramura, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_Naramura, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_Naramura, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestLaydoc);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestLaydoc, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestLaydoc, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestLaydoc, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestAshgine);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PriestAshgine, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestAshgine, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestAshgine, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherFobos);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherFobos, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherFobos, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherFobos, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_8bitFairy);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_8bitFairy, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_8bitFairy, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_8bitFairy, masterBlock);

        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetHugeCasket, noCandyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetHugeCasket, masterBlock);

        candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_8BitElder);
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_8BitElder, candyConversationBlock);
        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_8BitElder, candyConversationBlock.getBlockNumber());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_8BitElder, masterBlock);

        if(Settings.isIncludeHellTempleNPCs()) {
            candyConversationBlock = AddObject.buildHalloweenCandyBlock(CustomBlockEnum.HalloweenCandyConversationBlock_NightSurfaceFairy);
            datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyConversationBlock_NightSurfaceFairy, candyConversationBlock);
            masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_NightSurfaceFairy, candyConversationBlock.getBlockNumber());
            datFileData.addCustomBlock(CustomBlockEnum.HalloweenCandyReferenceBlock_NightSurfaceFairy, masterBlock);
        }

        final int totalNpcCount = Settings.isIncludeHellTempleNPCs() ? 29 : 28;
        final int npcHintCount = 17;
        for(int i = 28; i >= 0; i--) {
            datFileData.addCustomBlock(CustomBlockEnum.valueOf("HalloweenConversationBlock_NpcCount" + i),
                    AddObject.buildNpcCountBlock(i, totalNpcCount));
        }
        if(Settings.isIncludeHellTempleNPCs()) {
            datFileData.addCustomBlock(CustomBlockEnum.HalloweenConversationBlock_AllNpcs,
                    AddObject.buildAllNpcsBlock());
            Block conversationBlock = AddObject.buildMulbrukHTBlock(npcHintCount + 1);
            datFileData.addCustomBlock(CustomBlockEnum.HalloweenConversationBlock_Mulbruk_HT, conversationBlock);
        }

        for(int i = 1; i <= npcHintCount; i++) {
            datFileData.addCustomBlock(CustomBlockEnum.valueOf("HalloweenConversationBlock_Flag_NpcHint" + i),
                    AddObject.buildNpcHintBlock(i, true));
            datFileData.addCustomBlock(CustomBlockEnum.valueOf("HalloweenConversationBlock_Random_NpcHint" + i),
                    AddObject.buildNpcHintBlock(i, false));
        }
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenConversationBlock_Flag_DevRoomHint,
                AddObject.buildDevRoomHintBlock(true));
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenConversationBlock_Random_DevRoomHint,
                AddObject.buildDevRoomHintBlock(false));

        datFileData.addCustomBlock(CustomBlockEnum.HalloweenSecretShopBlock, AddObject.buildSecretShopBlock());
        datFileData.addCustomBlock(CustomBlockEnum.HalloweenDanceBlock, AddObject.buildDanceBlock());
        if(Settings.isIncludeHellTempleNPCs()) {
            datFileData.addCustomBlock(CustomBlockEnum.HalloweenHTSkip, AddObject.buildHTSkipBlock());
            if(!Settings.getStartingItemsIncludingCustom().contains("Holy Grail")) {
                datFileData.addCustomBlock(CustomBlockEnum.HalloweenHTGrailWarning, AddObject.buildHTGrailWarningBlock());
            }
        }
    }
}
