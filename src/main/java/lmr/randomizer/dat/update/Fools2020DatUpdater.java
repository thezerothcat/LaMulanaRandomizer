package lmr.randomizer.dat.update;

import lmr.randomizer.*;
import lmr.randomizer.dat.*;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.blocks.contents.*;
import lmr.randomizer.update.AddObject;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.util.*;

import java.util.List;

public class Fools2020DatUpdater extends DatUpdater {
    public Fools2020DatUpdater(DatFileData datFileData) {
        super(datFileData);
    }

    @Override
    public void updateItemNames(ItemNameBlock itemNameBlock) {
        itemNameBlock.setName(ItemNameBlock.LampOfTime, buildTextEntry(Translations.getText("items.LampofTime")));
        itemNameBlock.setName(ItemNameBlock.HeatproofCase, buildTextEntry(Translations.getText("items.Scriptures")));
        itemNameBlock.setName(ItemNameBlock.Scriptures, buildTextEntry(Translations.getText("items.HeatproofCase")));
        itemNameBlock.setName(ItemNameBlock.Map, buildTextEntry(Translations.getText("items.Nothing")));
        itemNameBlock.setName(ItemNameBlock.LampOfTimeEmpty, buildTextEntry(Translations.getText("items.LampofTime")));
        itemNameBlock.setName(ItemNameBlock.SecretTreasureOfLife, buildTextEntry(Translations.getText("items.Feather")));
        itemNameBlock.setName(ItemNameBlock.SoftwareYagomap, buildTextEntry(Translations.getText("items.YagooStr")));
        itemNameBlock.setName(ItemNameBlock.SoftwareYagostr, buildTextEntry(Translations.getText("items.YagooMap")));
        itemNameBlock.setName(ItemNameBlock.SoftwareTorude, buildTextEntry(Translations.getText("items.MiracleWitch")));
        itemNameBlock.setName(ItemNameBlock.SoftwareGuild, buildTextEntry(Translations.getText("items.LaMulana")));
        itemNameBlock.setName(ItemNameBlock.SoftwareMiracle, buildTextEntry(Translations.getText("items.Snapshots")));
        itemNameBlock.setName(ItemNameBlock.SoftwareLamulana, buildTextEntry(Translations.getText("items.Guild")));
    }

    @Override
    public void updateItemDescriptions(ItemDescriptionBlock itemDescriptionBlock) {
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.LampOfTime, buildTextEntry(Translations.getText("event.fools2020.lamp.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.HeatproofCase, buildTextEntry(Translations.getText("event.fools2020.heatproof.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.Scriptures, buildTextEntry(Translations.getText("event.fools2020.scriptures.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.LampOfTimeEmpty, buildTextEntry(Translations.getText("event.fools2020.lamp.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.SecretTreasureOfLife, buildTextEntry(Translations.getText("event.fools2020.stol.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.SoftwareYagomap, buildTextEntry(Translations.getText("event.fools2020.yagomap.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.SoftwareYagostr, buildTextEntry(Translations.getText("event.fools2020.yagostr.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.SoftwareTorude, buildTextEntry(Translations.getText("event.fools2020.torude.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.SoftwareGuild, buildTextEntry(Translations.getText("event.fools2020.guild.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.SoftwareMiracle, buildTextEntry(Translations.getText("event.fools2020.miracle.description")));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.SoftwareLamulana, buildTextEntry(Translations.getText("event.fools2020.lamulana.description")));
    }

    @Override
    public void updateMenuData(MenuBlock menuBlock) {
        menuBlock.setAngelShieldHp(352 * 10 + 1); // Nerf Angel Shield hp
        menuBlock.setFakeSilverShieldHp(10000); // Buff Fake Silver Shield hp
    }

    @Override
    public void updateSoftwareData(SoftwareBlock softwareBlock) {
        softwareBlock.setSoftwareName(SoftwareBlock.SoftwareYagomap, buildTextEntry(Translations.getText("items.yagostr.exe")));
        softwareBlock.setSoftwareName(SoftwareBlock.SoftwareYagostr, buildTextEntry(Translations.getText("items.yagomap.exe")));
        softwareBlock.setSoftwareName(SoftwareBlock.SoftwareTorude, buildTextEntry(Translations.getText("items.miracle.exe")));
        softwareBlock.setSoftwareName(SoftwareBlock.SoftwareGuild, buildTextEntry(Translations.getText("items.lamulana.exe")));
        softwareBlock.setSoftwareName(SoftwareBlock.SoftwareMiracle, buildTextEntry(Translations.getText("items.torude.exe")));
        softwareBlock.setSoftwareName(SoftwareBlock.SoftwareLamulana, buildTextEntry(Translations.getText("items.guild.exe")));
    }

    @Override
    void updateXmailerConversationBlock(Block conversationBlock) {
        List<BlockContents> xelpudBlockContents = conversationBlock.getBlockContents();
        xelpudBlockContents.clear();
        xelpudBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));
        xelpudBlockContents.add(new BlockFlagData((short)0xaa7, (short)1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.intro1"));
        for(Short shortCharacter : stringCharacters) {
            xelpudBlockContents.add(new BlockSingleData(shortCharacter));
        }
        xelpudBlockContents.add(new BlockItemData(ItemConstants.SOFTWARE_XMAILER)); // xmailer, to be replaced with 74=swimsuit
        xelpudBlockContents.add(new BlockFlagData((short)0x0e3, (short)2)); // xmailer flag
        xelpudBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        String text = Translations.getText("event.fools2020.intro2");
        String[] texts = text.split("%s");
        if(texts.length > 0) {
            stringCharacters = FileUtils.stringToData(texts[0]);
            for(Short shortCharacter : stringCharacters) {
                xelpudBlockContents.add(new BlockSingleData(shortCharacter));
            }
        }

        xelpudBlockContents.add(new BlockColorsData((short)0, (short)0x32, (short)0x96));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.graphics"));
        for(Short shortCharacter : stringCharacters) {
            xelpudBlockContents.add(new BlockSingleData(shortCharacter));
        }
        xelpudBlockContents.add(new BlockColorsData((short)0, (short)0, (short)0));

        stringCharacters = FileUtils.stringToData(texts[texts.length > 0 ? 1 : 0]);
        for(Short shortCharacter : stringCharacters) {
            xelpudBlockContents.add(new BlockSingleData(shortCharacter));
        }

        xelpudBlockContents.add(new BlockFlagData((short)FlagConstants.XELPUD_CONVERSATION_INTRO, (short)1)); // Talked to xelpud flag
        xelpudBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)0)); // Can-exit flag
    }

    @Override
    public void addCustomBlocks(DatFileData datFileData) {
        datFileData.addCustomBlock(CustomBlockEnum.Fools2020ConversationBlock_MulbrukEarlyExit, AddObject.buildFoolsEarlyExitBlock());
        datFileData.addCustomBlock(CustomBlockEnum.Fools2020ConversationBlock_MulbrukEarlyExitPrompt,
                AddObject.buildFoolsExitPromptBlock(getCustomBlockIndex(CustomBlockEnum.Fools2020ConversationBlock_MulbrukEarlyExit)));

        Block masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.Fools2020ReferenceBlock_MulbrukEarlyExitPrompt, getCustomBlockIndex(CustomBlockEnum.Fools2020ConversationBlock_MulbrukEarlyExitPrompt));
        datFileData.addCustomBlock(CustomBlockEnum.Fools2020ReferenceBlock_MulbrukEarlyExitPrompt, masterBlock);

        masterBlock = AddObject.buildMasterBlock(CustomBlockEnum.Fools2020ReferenceBlock_MulbrukBookOfTheDead, BlockConstants.ItemConversationBookOfTheDead);
        datFileData.addCustomBlock(CustomBlockEnum.Fools2020ReferenceBlock_MulbrukBookOfTheDead, masterBlock);
    }
}
