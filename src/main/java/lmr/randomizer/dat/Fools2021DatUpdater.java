package lmr.randomizer.dat;

import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.conversation.CheckBlock;

import java.util.List;

public class Fools2021DatUpdater extends DatUpdater {
    public Fools2021DatUpdater(DatFileData datFileData) {
        super(datFileData);
    }

    @Override
    public void updateItemNames(ItemNameBlock itemNameBlock) {
    }

    @Override
    public void updateItemDescriptions(ItemDescriptionBlock itemDescriptionBlock) {
    }

    @Override
    public void updateMenuData(MenuBlock menuBlock) {
    }

    @Override
    public void updateSoftwareData(SoftwareBlock softwareBlock) {
    }

    @Override
    public void updateGrailPoints(GrailPointBlock grailPointBlock) {
    }

    @Override
    public void updateHTMapNamesLimitedBlock(MapNamesLimitedBlock mapNamesLimitedBlock) {
    }

    @Override
    public void updateFootOfFutoScannableBlock(ScannableBlock scannableBlock) {
        String hintText = String.format(Translations.getText("event.fools2021.giants"), Settings.getCurrentGiant(), Settings.getCurrentGiant());
        scannableBlock.setScanText(buildTextEntry(hintText));
    }

    @Override
    public void updateFairyQueenFirstConversationBlock(Block conversationBlock) {
    }

    @Override
    public void updateFairyQueenWhenTheTimeComesConversationBlock(Block conversationBlock) {
    }

    @Override
    void updateMulbrukIntroConversationBlock(Block conversationBlock) {
    }

    @Override
    public void updateMiniDollConversationBlock(Block conversationBlock) {
    }

    @Override
    void updatePepperConversationBlock(Block conversationBlock) {
    }

    @Override
    void updateXmailerConversationBlock(Block conversationBlock) {
    }

    @Override
    void updateXelpudTalismanConversationBlock(Block conversationBlock) {
    }

    @Override
    void updateXelpudPillarConversationBlock(Block conversationBlock) {
    }

    @Override
    void updateMulanaTalismanConversationBlock(Block conversationBlock) {
    }

    @Override
    void updateBookOfTheDeadConversationBlock(Block conversationBlock) {
    }

    @Override
    void updateProvocativeBathingSuitConversationBlock(Block conversationBlock) {
    }

    @Override
    void updateXelpudFlagCheckBlock(CheckBlock flagCheckBlock) {
    }

    @Override
    void updateXelpudScoreCheckBlock(CheckBlock scoreCheckBlock) {
    }

    @Override
    void updateMulbrukFlagCheckBlock(CheckBlock flagCheckBlock) {
    }

    @Override
    void updateMulbrukScoreCheckBlock(CheckBlock scoreCheckBlock) {
    }

    @Override
    void updateMulbrukRandomBlocks(List<Block> randomBlocks) {
    }

    @Override
    public void updateEmailBlock(Block emailBlock) {
        List<BlockContents> blockContents = emailBlock.getBlockContents();
        blockContents.clear();
        for (Short singleCharacter : FileUtils.stringToData(Translations.getText("event.fools2021.mailTitle"))) {
            blockContents.add(new BlockSingleData(singleCharacter));
        }
        blockContents.add(new BlockSingleData(BlockDataConstants.EndOfEntry));
        for (Short singleCharacter : FileUtils.stringToData(Translations.getText("event.fools2021.mailText"))) {
            blockContents.add(new BlockSingleData(singleCharacter));
        }
    }

    @Override
    public void addCustomBlocks(DatFileData datFileData) {
    }
}
