package lmr.randomizer.dat;

import lmr.randomizer.MantraConstants;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.conversation.CheckBlock;

import java.util.List;

public class Fools2022DatUpdater extends DatUpdater {
    public Fools2022DatUpdater(DatFileData datFileData) {
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
        menuBlock.setMantraName(MantraConstants.BIRTH, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.BIRTH)));
        menuBlock.setMantraName(MantraConstants.DEATH, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.DEATH)));
        menuBlock.setMantraName(MantraConstants.MARDUK, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.MARDUK)));
        menuBlock.setMantraName(MantraConstants.SABBAT, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.SABBAT)));
        menuBlock.setMantraName(MantraConstants.MU, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.MU)));
        menuBlock.setMantraName(MantraConstants.VIY, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.VIY)));
        menuBlock.setMantraName(MantraConstants.BAHRUN, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.BAHRUN)));
        menuBlock.setMantraName(MantraConstants.WEDJET, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.WEDJET)));
        menuBlock.setMantraName(MantraConstants.ABUTO, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.ABUTO)));
        menuBlock.setMantraName(MantraConstants.LAMULANA, buildTextEntry(Translations.getText("event.fools2022.m" + MantraConstants.LAMULANA)));
    }

    @Override
    void updateSoftwareData(SoftwareBlock softwareBlock) {
    }

    @Override
    void updateGrailPoints(GrailPointBlock grailPointBlock) {
    }

    @Override
    void updateHTMapNamesLimitedBlock(MapNamesLimitedBlock mapNamesLimitedBlock) {
    }

    @Override
    void updateFootOfFutoScannableBlock(ScannableBlock scannableBlock) {
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
    void updateFairyQueenFirstConversationBlock(Block conversationBlock) {
    }

    @Override
    void updateFairyQueenWhenTheTimeComesConversationBlock(Block conversationBlock) {
    }

    @Override
    void updateMulbrukIntroConversationBlock(Block conversationBlock) {
    }

    @Override
    void updateMiniDollConversationBlock(Block conversationBlock) {
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
    void updateEmailBlock(Block emailBlock) {
    }

    @Override
    public void addCustomBlocks(DatFileData datFileData) {
    }
}
