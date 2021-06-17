package lmr.randomizer.dat.update;

import lmr.randomizer.dat.blocks.Block;
import lmr.randomizer.dat.blocks.contents.BlockContents;
import lmr.randomizer.dat.blocks.contents.BlockItemData;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.dat.blocks.MenuBlock;

public class Fools2022DatUpdater extends DatUpdater {
    public Fools2022DatUpdater(DatFileData datFileData) {
        super(datFileData);
    }

    @Override
    public void updateMenuData(MenuBlock menuBlock) {
//        menuBlock.setMantraName(MantraConstants.BIRTH, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.BIRTH)));
//        menuBlock.setMantraName(MantraConstants.DEATH, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.DEATH)));
//        menuBlock.setMantraName(MantraConstants.MARDUK, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.MARDUK)));
//        menuBlock.setMantraName(MantraConstants.SABBAT, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.SABBAT)));
//        menuBlock.setMantraName(MantraConstants.MU, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.MU)));
//        menuBlock.setMantraName(MantraConstants.VIY, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.VIY)));
//        menuBlock.setMantraName(MantraConstants.BAHRUN, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.BAHRUN)));
//        menuBlock.setMantraName(MantraConstants.WEDJET, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.WEDJET)));
//        menuBlock.setMantraName(MantraConstants.ABUTO, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.ABUTO)));
//        menuBlock.setMantraName(MantraConstants.LAMULANA, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.LAMULANA)));
    }

    @Override
    void updateXmailerConversationBlock(Block conversationBlock) {
        for(BlockContents blockContents : conversationBlock.getBlockContents()) {
            if(blockContents instanceof BlockItemData) {
                BlockItemData itemData = (BlockItemData)blockContents;
//                if(itemData.getItemData() == ItemConstants.SOFTWARE_MEKURI) {
                    itemData.setItemData((short)105); // weight
//                itemData.setItemData((short)106); // coin
//                }
            }
        }
        Integer blockContentIndex = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockItemData) {
                BlockItemData itemData = (BlockItemData)blockContents;
//                if(itemData.getItemData() == ItemConstants.SOFTWARE_MEKURI) {
//                    itemData.setItemData((short)105); // weight
                itemData.setItemData((short)106); // coin
//                }
                blockContentIndex = i;
                break;
            }
        }
        for(int i = 1; i < 300; i++) {
            conversationBlock.getBlockContents().add(blockContentIndex, new BlockItemData((short)106));
        }
    }

    @Override
    void updateMekuriConversationBlock(Block conversationBlock) {
        for(BlockContents blockContents : conversationBlock.getBlockContents()) {
            if(blockContents instanceof BlockItemData) {
                BlockItemData itemData = (BlockItemData)blockContents;
//                if(itemData.getItemData() == ItemConstants.SOFTWARE_MEKURI) {
                    itemData.setItemData((short)106);
//                }
            }
        }
    }
}
