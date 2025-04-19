package lmr.randomizer.dat.update;

import lmr.randomizer.FileUtils;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.blocks.contents.BlockContents;
import lmr.randomizer.dat.blocks.contents.BlockFlagData;
import lmr.randomizer.dat.blocks.contents.BlockSingleData;
import lmr.randomizer.dat.blocks.contents.entries.TextEntry;
import lmr.randomizer.util.BlockDataConstants;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.ItemConstants;

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
    void updateHardmodeWarningScannableBlock(ScannableBlock scannableBlock) {
        scannableBlock.setLanguage(ScannableBlock.Language_Native);
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
    void updateOpeningText(OpeningTextBlock openingTextBlock) {
        openingTextBlock.setTextEntry1(buildTextEntry(Translations.getText("event.easter2025.opening1")));
        openingTextBlock.setTextEntry2(buildTextEntry(Translations.getText("event.easter2025.opening2")));
        openingTextBlock.setTextEntry3(buildTextEntry(Translations.getText("event.easter2025.opening3")));
        openingTextBlock.setTextEntry4(buildTextEntry(Translations.getText("event.easter2025.opening4")));
        openingTextBlock.setTextEntry5(buildTextEntry(Translations.getText("event.easter2025.opening5")));
        openingTextBlock.setTextEntry6(buildTextEntry(Translations.getText("event.easter2025.opening6")));
        openingTextBlock.setTextEntry7(buildTextEntry(Translations.getText("event.easter2025.opening7")));
        openingTextBlock.setTextEntry8(buildTextEntry(Translations.getText("event.easter2025.opening8")));
        openingTextBlock.setTextEntry9(buildTextEntry(Translations.getText("event.easter2025.opening9")));
        openingTextBlock.setTextEntry10(buildTextEntry(Translations.getText("event.easter2025.opening10")));
        openingTextBlock.setTextEntry11(buildTextEntry(Translations.getText("event.easter2025.opening11")));
    }

    @Override
    public void addItemNames(DatFileData datFileData) {
        ItemNameBlock itemNameBlock = datFileData.getItemNameBlock();
        ((TextEntry)itemNameBlock.getBlockContents().get(itemNameBlock.getBlockContents().size() - 1)).setIncludeEndRecordIndicator(true);

        TextEntry textEntry;
        String name;
        // Add placeholders for unused inventory items
        for (int i = ItemConstants.SOUL_STONE + 1; i < ItemConstants.FIRST_USABLE_CUSTOM_INVENTORY_ITEM; i++) {
            textEntry = new TextEntry();
            name = Translations.getText("event.easter2025.egg.name");
                    textEntry.getData().addAll(FileUtils.stringToData(name));
            textEntry.setIncludeEndRecordIndicator(true);
            itemNameBlock.getBlockContents().add(textEntry);
        }
        // Add custom inventory items
        for (int i = 170; i <= ItemConstants.LAST_USABLE_CUSTOM_INVENTORY_ITEM; i++) {
            textEntry = new TextEntry();
            name = Translations.getText("event.easter2025.egg.name." + i);
            if (name == null) {
                break;
            }
            textEntry.getData().addAll(FileUtils.stringToData(name));
            textEntry.setIncludeEndRecordIndicator(true);
            itemNameBlock.getBlockContents().add(textEntry);
        }
        ((TextEntry)itemNameBlock.getBlockContents().get(itemNameBlock.getBlockContents().size() - 1)).setIncludeEndRecordIndicator(false);
    }
}
