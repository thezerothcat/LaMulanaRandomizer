package lmr.randomizer.dat.update;

import lmr.randomizer.FileUtils;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.dat.blocks.Block;
import lmr.randomizer.dat.blocks.ItemDescriptionBlock;
import lmr.randomizer.dat.blocks.ItemNameBlock;
import lmr.randomizer.dat.blocks.ScannableBlock;
import lmr.randomizer.dat.blocks.contents.BlockContents;
import lmr.randomizer.dat.blocks.contents.BlockFlagData;
import lmr.randomizer.dat.blocks.contents.BlockSingleData;
import lmr.randomizer.dat.blocks.contents.entries.TextEntry;
import lmr.randomizer.util.BlockDataConstants;
import lmr.randomizer.util.EggConstants;
import lmr.randomizer.util.FlagConstants;

import java.util.List;

public class Easter2025DatUpdater extends DatUpdater {
    public Easter2025DatUpdater(DatFileData datFileData) {
        super(datFileData);
    }

    @Override
    void updateItemNames(ItemNameBlock itemNameBlock) {
        itemNameBlock.setName(ItemDescriptionBlock.WaterproofCase, buildTextEntry(Translations.getText("event.easter2025.egg.name")));
    }

    @Override
    public void updateItemDescriptions(ItemDescriptionBlock itemDescriptionBlock) {
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.WaterproofCase, buildTextEntry(Translations.getText("event.easter2025.egg.description")));
    }

    @Override
    public void updateFootOfFutoScannableBlock(ScannableBlock scannableBlock) {
        String hintText = Translations.getText("event.easter2025.hintText.giants");
        scannableBlock.setScanText(buildTextEntry(hintText));
    }

    @Override
    void updateXmailerConversationBlock(Block conversationBlock) {
        List<BlockContents> blockContents = conversationBlock.getBlockContents();
        blockContents.clear();
        blockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, 1));
        for(Short rawDataEntry : buildRawDataWithCommands(Translations.getText("event.easter2025.XelpudIntro1"))) {
            blockContents.add(new BlockSingleData(rawDataEntry));
        }
        blockContents.add(new BlockSingleData(BlockDataConstants.Cls));
        for(Short rawDataEntry : buildRawDataWithCommands(Translations.getText("event.easter2025.XelpudIntro2"))) {
            blockContents.add(new BlockSingleData(rawDataEntry));
        }
        blockContents.add(new BlockSingleData(BlockDataConstants.Cls));
        for(Short rawDataEntry : buildRawDataWithCommands(Translations.getText("event.easter2025.XelpudIntro3"))) {
            blockContents.add(new BlockSingleData(rawDataEntry));
        }
        blockContents.add(new BlockFlagData(FlagConstants.XELPUD_CONVERSATION_INTRO, 1));
        blockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, 0));
    }

    @Override
    public void addItemNames(DatFileData datFileData) {
        ItemNameBlock itemNameBlock = datFileData.getItemNameBlock();
        TextEntry textEntry;
        String name;
        for (int i = 170; i <= EggConstants.LAST_CUSTOM_INVENTORY_ITEM; i++) {
            textEntry = new TextEntry();
            name = Translations.getText("event.easter2025.egg.name." + i);
            if (name == null) {
                break;
            }
            textEntry.getData().addAll(FileUtils.stringToData(name));
            textEntry.setIncludeEndRecordIndicator(true);
            itemNameBlock.getBlockContents().add(textEntry);
        }
    }
}
