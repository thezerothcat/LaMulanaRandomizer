package lmr.randomizer.dat.update;

import lmr.randomizer.FileUtils;
import lmr.randomizer.dat.blocks.Block;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.blocks.contents.entries.TextEntry;
import lmr.randomizer.dat.blocks.CheckBlock;
import lmr.randomizer.dat.blocks.ShopBlock;

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

        updateMekuriConversationBlock(datFileData.getMekuriConversationBlock());
        updateMiniDollConversationBlock(datFileData.getMiniDollConversationBlock());
        updatePepperConversationBlock(datFileData.getPepperConversationBlock());
        updateAnchorConversationBlock(datFileData.getAnchorConversationBlock());
        updateXmailerConversationBlock(datFileData.getXmailerConversationBlock());
        updateXelpudTalismanConversationBlock(datFileData.getXelpudTalismanConversationBlock());
        updateXelpudPillarConversationBlock(datFileData.getXelpudPillarConversationBlock());
        updateMulanaTalismanConversationBlock(datFileData.getMulanaTalismanConversationBlock());
        updateBookOfTheDeadConversationBlock(datFileData.getBookOfTheDeadConversationBlock());
        updateSurfaceMapScannableBlock(datFileData.getSurfaceMapScannableBlock());
        updateProvocativeBathingSuitConversationBlock(datFileData.getProvocativeBathingSuitConversationBlock());

        updateNeburShopBlock(datFileData.getNeburShopBlock());
        updateNeburAltShopBlock(datFileData.getNeburAltShopBlock());
        updateSidroShopBlock(datFileData.getSidroShopBlock());
        updateModroShopBlock(datFileData.getModroShopBlock());
        updatePenadventOfGhostShopBlock(datFileData.getPenadventOfGhostShopBlock());
        updateGreedyCharlieShopBlock(datFileData.getGreedyCharlieShopBlock());
        updateShalomIIIShopBlock(datFileData.getShalomIIIShopBlock());
        updateUsasVIShopBlock(datFileData.getUsasVIShopBlock());
        updateKingvalleyIShopBlock(datFileData.getKingvalleyIShopBlock());
        updateMrFishmanShopBlock(datFileData.getMrFishmanShopBlock());
        updateMrFishmanAltShopBlock(datFileData.getMrFishmanAltShopBlock());
        updateOperatorCombakerShopBlock(datFileData.getOperatorCombakerShopBlock());
        updateYiegahKungfuShopBlock(datFileData.getYiegahKungfuShopBlock());
        updateArrogantMetagearShopBlock(datFileData.getArrogantMetagearShopBlock());
        updateArrogantSturdySnakeShopBlock(datFileData.getArrogantSturdySnakeShopBlock());
        updateYiearKungfuShopBlock(datFileData.getYiearKungfuShopBlock());
        updateAffectedKnimareShopBlock(datFileData.getAffectedKnimareShopBlock());
        updateMoverAthlelandShopBlock(datFileData.getMoverAthlelandShopBlock());
        updateGiantMopiranShopBlock(datFileData.getGiantMopiranShopBlock());
        updateKingvalleyIIShopBlock(datFileData.getKingvalleyIIShopBlock());
        updateEnergeticBelmontShopBlock(datFileData.getEnergeticBelmontShopBlock());
        updateMechanicalEfspiShopBlock(datFileData.getMechanicalEfspiShopBlock());
        updateMudManQubertShopBlock(datFileData.getMudManQubertShopBlock());
        updateHotbloodedNemesistwoShopBlock(datFileData.getHotbloodedNemesistwoShopBlock());
        updateTailorDracuetShopBlock(datFileData.getTailorDracuetShopBlock());
        ShopBlock customShopBlock = datFileData.getCustomShopBlock();
        if(customShopBlock != null) {
            updateCustomShopBlock(datFileData.getCustomShopBlock());
        }

        updateXelpudFlagCheckBlock(datFileData.getXelpudFlagCheckBlock());
        updateXelpudScoreCheckBlock(datFileData.getXelpudScoreCheckBlock());
        updateMulbrukFlagCheckBlock(datFileData.getMulbrukFlagCheckBlock());
        updateMulbrukScoreCheckBlock(datFileData.getMulbrukScoreCheckBlock());
        updateMulbrukRandomBlocks(datFileData.getMulbrukRandomBlocks());
        for(Block emailBlock : datFileData.getEmailBlocks()) {
            updateEmailBlock(emailBlock);
        }
    }

    public void addCustomBlocks(DatFileData datFileData) { }

    protected Short getCustomBlockIndex(CustomBlockEnum customBlockEnum) {
        return datFileData.getCustomBlockIndex(customBlockEnum);
    }

    protected TextEntry buildTextEntry(String textToUse) {
        TextEntry textEntry = new TextEntry();
        textEntry.getData().addAll(FileUtils.stringToData(textToUse));
        return textEntry;
    }

    void updateItemNames(ItemNameBlock itemNameBlock) { }
    void updateItemDescriptions(ItemDescriptionBlock itemDescriptionBlock) { }
    void updateMenuData(MenuBlock menuBlock) { }
    void updateSoftwareData(SoftwareBlock softwareBlock) { }
    void updateGrailPoints(GrailPointBlock grailPointBlock) { }

    void updateHTMapNamesLimitedBlock(MapNamesLimitedBlock mapNamesLimitedBlock) { }

    void updateFootOfFutoScannableBlock(ScannableBlock scannableBlock) { }

    void updateMekuriConversationBlock(Block conversationBlock) { }
    void updateMiniDollConversationBlock(Block conversationBlock) { }
    void updatePepperConversationBlock(Block conversationBlock) { }
    void updateAnchorConversationBlock(Block conversationBlock) { }
    void updateXmailerConversationBlock(Block conversationBlock) { }
    void updateXelpudTalismanConversationBlock(Block conversationBlock) { }
    void updateXelpudPillarConversationBlock(Block conversationBlock) { }
    void updateMulanaTalismanConversationBlock(Block conversationBlock) { }
    void updateBookOfTheDeadConversationBlock(Block conversationBlock) { }
    void updateSurfaceMapScannableBlock(Block scannableBlock) { }
    void updateProvocativeBathingSuitConversationBlock(Block conversationBlock) { }

    void updateNeburShopBlock(ShopBlock shopBlock) { }
    void updateNeburAltShopBlock(ShopBlock shopBlock) { }
    void updateSidroShopBlock(ShopBlock shopBlock) { }
    void updateModroShopBlock(ShopBlock shopBlock) { }
    void updatePenadventOfGhostShopBlock(ShopBlock shopBlock) { }
    void updateGreedyCharlieShopBlock(ShopBlock shopBlock) { }
    void updateShalomIIIShopBlock(ShopBlock shopBlock) { }
    void updateUsasVIShopBlock(ShopBlock shopBlock) { }
    void updateKingvalleyIShopBlock(ShopBlock shopBlock) { }
    void updateMrFishmanShopBlock(ShopBlock shopBlock) { }
    void updateMrFishmanAltShopBlock(ShopBlock shopBlock) { }
    void updateOperatorCombakerShopBlock(ShopBlock shopBlock) { }
    void updateYiegahKungfuShopBlock(ShopBlock shopBlock) { }
    void updateArrogantMetagearShopBlock(ShopBlock shopBlock) { }
    void updateArrogantSturdySnakeShopBlock(ShopBlock shopBlock) { }
    void updateYiearKungfuShopBlock(ShopBlock shopBlock) { }
    void updateAffectedKnimareShopBlock(ShopBlock shopBlock) { }
    void updateMoverAthlelandShopBlock(ShopBlock shopBlock) { }
    void updateGiantMopiranShopBlock(ShopBlock shopBlock) { }
    void updateKingvalleyIIShopBlock(ShopBlock shopBlock) { }
    void updateEnergeticBelmontShopBlock(ShopBlock shopBlock) { }
    void updateMechanicalEfspiShopBlock(ShopBlock shopBlock) { }
    void updateMudManQubertShopBlock(ShopBlock shopBlock) { }
    void updateHotbloodedNemesistwoShopBlock(ShopBlock shopBlock) { }
    void updateTailorDracuetShopBlock(ShopBlock shopBlock) { }
    void updateCustomShopBlock(ShopBlock shopBlock) { }

    void updateFairyQueenFirstConversationBlock(Block conversationBlock) { }
    void updateFairyQueenWhenTheTimeComesConversationBlock(Block conversationBlock) { }
    void updateMulbrukIntroConversationBlock(Block conversationBlock) { }

    void updateXelpudFlagCheckBlock(CheckBlock flagCheckBlock) { }
    void updateXelpudScoreCheckBlock(CheckBlock scoreCheckBlock) { }
    void updateMulbrukFlagCheckBlock(CheckBlock flagCheckBlock) { }
    void updateMulbrukScoreCheckBlock(CheckBlock scoreCheckBlock) { }
    void updateMulbrukRandomBlocks(List<Block> randomBlocks) { }

    void updateEmailBlock(Block emailBlock) { }
}
