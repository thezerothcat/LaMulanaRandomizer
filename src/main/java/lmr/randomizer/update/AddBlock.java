package lmr.randomizer.update;

import lmr.randomizer.FileUtils;
import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.blocks.Block;
import lmr.randomizer.dat.blocks.MasterNpcBlock;
import lmr.randomizer.dat.blocks.contents.BlockFlagData;
import lmr.randomizer.dat.blocks.contents.BlockSingleData;
import lmr.randomizer.dat.blocks.contents.BlockStringData;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.util.BlockDataConstants;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.ValueConstants;

import java.util.List;

public final class AddBlock {
    private AddBlock() { }

    public static Block buildXelpudIntroBlock() {
        Block introBlock = new Block();
        introBlock.getBlockContents().add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText(HolidaySettings.isFools2019Mode() ? "fools.xelpudText" : "text.xelpudWarn"));
        for(Short shortCharacter : stringCharacters) {
            introBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        introBlock.getBlockContents().add(new BlockFlagData((short)FlagConstants.RANDOMIZER_SAVE_LOADED, (short)2));
        introBlock.getBlockContents().add(new BlockFlagData((short)FlagConstants.XELPUD_CONVERSATION_INTRO, (short)1));
        introBlock.getBlockContents().add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)0));
        return introBlock;
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
}
