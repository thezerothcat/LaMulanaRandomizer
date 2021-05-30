package lmr.randomizer.dat;

import lmr.randomizer.FileUtils;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.blocks.entries.TextEntry;
import lmr.randomizer.dat.conversation.CheckBlock;

import java.util.List;

public abstract class DatUpdater {
    protected DatFileData datFileData;

    public DatUpdater(DatFileData datFileData) {
        this.datFileData = datFileData;
    }

    public void updateBlocks() {
        updateItemNames(datFileData.getItemNameBlock());
        updateItemDescriptions(datFileData.getItemDescriptionBlock());
        updateMenuData(datFileData.getMenuBlock());
        updateSoftwareData(datFileData.getSoftwareBlock());
        updateGrailPoints(datFileData.getGrailPointBlock());
        updateHTMapNamesLimitedBlock(datFileData.getHTMapNamesLimitedBlock());
        updateFootOfFutoScannableBlock(datFileData.getFootOfFutoScannableBlock());

        updateFairyQueenFirstConversationBlock(datFileData.getFairyQueenFirstConversationBlock());
        updateFairyQueenWhenTheTimeComesConversationBlock(datFileData.getFairyQueenWhenTheTimeComesConversationBlock());
        updateMulbrukIntroConversationBlock(datFileData.getMulbrukIntroConversationBlock());

        updateMiniDollConversationBlock(datFileData.getMiniDollConversationBlock());
        updatePepperConversationBlock(datFileData.getPepperConversationBlock());
        updateXmailerConversationBlock(datFileData.getXmailerConversationBlock());
        updateXelpudTalismanConversationBlock(datFileData.getXelpudTalismanConversationBlock());
        updateXelpudPillarConversationBlock(datFileData.getXelpudPillarConversationBlock());
        updateMulanaTalismanConversationBlock(datFileData.getMulanaTalismanConversationBlock());
        updateBookOfTheDeadConversationBlock(datFileData.getBookOfTheDeadConversationBlock());
        updateProvocativeBathingSuitConversationBlock(datFileData.getProvocativeBathingSuitConversationBlock());

        updateXelpudFlagCheckBlock(datFileData.getXelpudFlagCheckBlock());
        updateXelpudScoreCheckBlock(datFileData.getXelpudScoreCheckBlock());
        updateMulbrukFlagCheckBlock(datFileData.getMulbrukFlagCheckBlock());
        updateMulbrukScoreCheckBlock(datFileData.getMulbrukScoreCheckBlock());
        updateMulbrukRandomBlocks(datFileData.getMulbrukRandomBlocks());
        for(Block emailBlock : datFileData.getEmailBlocks()) {
            updateEmailBlock(emailBlock);
        }
    }

    protected Short getCustomBlockIndex(CustomBlockEnum customBlockEnum) {
        return datFileData.getCustomBlockIndex(customBlockEnum);
    }

    protected TextEntry buildTextEntry(String textToUse) {
        TextEntry textEntry = new TextEntry();
        textEntry.getData().addAll(FileUtils.stringToData(textToUse));
        return textEntry;
    }

    abstract void updateItemNames(ItemNameBlock itemNameBlock);
    abstract void updateItemDescriptions(ItemDescriptionBlock itemDescriptionBlock);
    abstract void updateMenuData(MenuBlock menuBlock);
    abstract void updateSoftwareData(SoftwareBlock softwareBlock);
    abstract void updateGrailPoints(GrailPointBlock grailPointBlock);

    abstract void updateHTMapNamesLimitedBlock(MapNamesLimitedBlock mapNamesLimitedBlock);

    abstract void updateFootOfFutoScannableBlock(ScannableBlock scannableBlock);

    abstract void updatePepperConversationBlock(Block conversationBlock);
    abstract void updateXmailerConversationBlock(Block conversationBlock);
    abstract void updateXelpudTalismanConversationBlock(Block conversationBlock);
    abstract void updateXelpudPillarConversationBlock(Block conversationBlock);
    abstract void updateMulanaTalismanConversationBlock(Block conversationBlock);
    abstract void updateBookOfTheDeadConversationBlock(Block conversationBlock);
    abstract void updateProvocativeBathingSuitConversationBlock(Block conversationBlock);

    abstract void updateFairyQueenFirstConversationBlock(Block conversationBlock);
    abstract void updateFairyQueenWhenTheTimeComesConversationBlock(Block conversationBlock);
    abstract void updateMulbrukIntroConversationBlock(Block conversationBlock);

    abstract void updateMiniDollConversationBlock(Block conversationBlock);

    abstract void updateXelpudFlagCheckBlock(CheckBlock flagCheckBlock);
    abstract void updateXelpudScoreCheckBlock(CheckBlock scoreCheckBlock);
    abstract void updateMulbrukFlagCheckBlock(CheckBlock flagCheckBlock);
    abstract void updateMulbrukScoreCheckBlock(CheckBlock scoreCheckBlock);
    abstract void updateMulbrukRandomBlocks(List<Block> randomBlocks);

    abstract void updateEmailBlock(Block emailBlock);

    public abstract void addCustomBlocks(DatFileData datFileData);
}
