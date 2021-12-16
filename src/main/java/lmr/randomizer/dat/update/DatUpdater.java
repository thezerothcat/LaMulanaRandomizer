package lmr.randomizer.dat.update;

import lmr.randomizer.FileUtils;
import lmr.randomizer.dat.blocks.Block;
import lmr.randomizer.dat.blocks.contents.*;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.blocks.contents.entries.TextEntry;
import lmr.randomizer.dat.blocks.CheckBlock;
import lmr.randomizer.dat.blocks.ShopBlock;
import lmr.randomizer.util.BlockDataConstants;

import java.util.ArrayList;
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
        updateMulbrukStoneConversationBlock(datFileData.getMulbrukStoneConversationBlock());
        updateMulbrukWakingUpConversationBlock(datFileData.getMulbrukWakingUpConversationBlock());
        updateMulbrukIntroConversationBlock(datFileData.getMulbrukIntroConversationBlock());
        updateMulbrukHTConversationBlock(datFileData.getMulbrukHTConversationBlock());
        updateRegularEscapeConversationBlock(datFileData.getRegularEscapeConversationBlock());
        updateSwimsuitEscapeConversationBlock(datFileData.getSwimsuitEscapeConversationBlock());

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

        updateHinerReferenceBlock(datFileData.getHinerReferenceBlock());
        updateMogerReferenceBlock(datFileData.getMogerReferenceBlock());
        updateFormerMekuriMasterReferenceBlock(datFileData.getFormerMekuriMasterReferenceBlock());
        updatePriestZarnacReferenceBlock(datFileData.getPriestZarnacReferenceBlock());
        updatePriestXanadoReferenceBlock(datFileData.getPriestXanadoReferenceBlock());
        updatePhilosopherGiltoriyoReferenceBlock(datFileData.getPhilosopherGiltoriyoReferenceBlock());
        updatePriestHidlydaReferenceBlock(datFileData.getPriestHidlydaReferenceBlock());
        updatePriestRomancisReferenceBlock(datFileData.getPriestRomancisReferenceBlock());
        updatePriestAramoReferenceBlock(datFileData.getPriestAramoReferenceBlock());
        updatePriestTritonReferenceBlock(datFileData.getPriestTritonReferenceBlock());
        updatePriestJaguarfivReferenceBlock(datFileData.getPriestJaguarfivReferenceBlock());
        updateFairyQueenWaitingForPendantReferenceBlock(datFileData.getFairyQueenWaitingForPendantReferenceBlock());
        updateFairyQueenUnlockFairiesReferenceBlock(datFileData.getFairyQueenUnlockFairiesReferenceBlock());
        updateFairyQueenWhenTheTimeComesReferenceBlock(datFileData.getFairyQueenWhenTheTimeComesReferenceBlock());
        updateFairyQueenTrueShrine1ReferenceBlock(datFileData.getFairyQueenTrueShrine1ReferenceBlock());
        updateFairyQueenTrueShrine2ReferenceBlock(datFileData.getFairyQueenTrueShrine2ReferenceBlock());
        updateMrSlushfundPepperReferenceBlock(datFileData.getMrSlushfundPepperReferenceBlock());
        updateMrSlushfundWaitingForTreasuresReferenceBlock(datFileData.getMrSlushfundWaitingForTreasuresReferenceBlock());
        updateMrSlushfundAnchorReferenceBlock(datFileData.getMrSlushfundAnchorReferenceBlock());
        updateMrSlushfundNeverComeBackReferenceBlock(datFileData.getMrSlushfundNeverComeBackReferenceBlock());
        updatePriestAlestReferenceBlock(datFileData.getPriestAlestReferenceBlock());
        updateStrayFairyReferenceBlock(datFileData.getStrayFairyReferenceBlock());
        updateGiantThexdeReferenceBlock(datFileData.getGiantThexdeReferenceBlock());
        updatePhilosopherAlsedanaReferenceBlock(datFileData.getPhilosopherAlsedanaReferenceBlock());
        updatePhilosopherSamarantaReferenceBlock(datFileData.getPhilosopherSamarantaReferenceBlock());
        updatePriestLaydocReferenceBlock(datFileData.getPriestLaydocReferenceBlock());
        updatePriestAshgineReferenceBlock(datFileData.getPriestAshgineReferenceBlock());
        updatePhilosopherFobosLadderReferenceBlock(datFileData.getPhilosopherFobosLadderReferenceBlock());
        updatePhilosopherFobosMedicineReferenceBlock(datFileData.getPhilosopherFobosMedicineReferenceBlock());
        update8BitElderReferenceBlock(datFileData.get8BitElderReferenceBlock());
        updateDuplexReferenceBlock(datFileData.getDuplexReferenceBlock());
        updateSamieruReferenceBlock(datFileData.getSamieruReferenceBlock());
        updateNaramuraReferenceBlock(datFileData.getNaramuraReferenceBlock());
        update8BitFairyReferenceBlock(datFileData.get8BitFairyReferenceBlock());
        updatePriestMadomonoReferenceBlock(datFileData.getPriestMadomonoReferenceBlock());
        updatePriestGailiousReferenceBlock(datFileData.getPriestGailiousReferenceBlock());
        updateMulbrukStoneConversationReferenceBlock(datFileData.getMulbrukStoneConversationReferenceBlock());
        updatePriestAlestNoItemReferenceBlock(datFileData.getPriestAlestNoItemReferenceBlock());
        updateMulbrukEscapeRegularReferenceBlock(datFileData.getMulbrukEscapeRegularReferenceBlock());
        updateMulbrukEscapeSwimsuitReferenceBlock(datFileData.getMulbrukEscapeSwimsuitReferenceBlock());
        updateMulbrukProvocativeBathingSuitReactionReferenceBlock(datFileData.getMulbrukProvocativeBathingSuitReactionReferenceBlock());
        updatePhilosopherGiltoriyoStoneReferenceBlock(datFileData.getPhilosopherGiltoriyoStoneReferenceBlock());
        updatePhilosopherAlsedanaStoneReferenceBlock(datFileData.getPhilosopherAlsedanaStoneReferenceBlock());
        updatePhilosopherSamarantaStoneReferenceBlock(datFileData.getPhilosopherSamarantaStoneReferenceBlock());
        updatePhilosopherFobosStoneReferenceBlock(datFileData.getPhilosopherFobosStoneReferenceBlock());

        updateXelpudFlagCheckBlock(datFileData.getXelpudFlagCheckBlock());
        updateXelpudScoreCheckBlock(datFileData.getXelpudScoreCheckBlock());
        updateXelpudSpriteBlock(datFileData.getXelpudSpriteBlock());
        updateMulbrukFlagCheckBlock(datFileData.getMulbrukFlagCheckBlock());
        updateMulbrukScoreCheckBlock(datFileData.getMulbrukScoreCheckBlock());
        updateMulbrukRandomBlocks(datFileData.getMulbrukRandomBlocks());
        updateMulbrukSpriteBlock(datFileData.getMulbrukSpriteBlock());
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

    protected void replaceText(List<BlockContents> blockContentsList, String textToReplace, String replacement) {
        List<BlockContents> keptBlockContents = new ArrayList<>();
        String temp = "";
        for(BlockContents blockContents : blockContentsList) {
            if(blockContents instanceof BlockColorsData
                    || blockContents instanceof BlockCmdSingle
                    || blockContents instanceof BlockFlagData
                    || blockContents instanceof BlockItemData
                    || blockContents instanceof BlockListData
                    || blockContents instanceof BlockMantraData
                    || blockContents instanceof BlockPoseData
                    || blockContents instanceof BlockSceneData
                    || blockContents instanceof BlockStringData) {
                keptBlockContents.addAll(buildBlockContents(temp));
                keptBlockContents.add(blockContents);
                temp = "";
            }
            else if(blockContents instanceof BlockSingleData) {
                if(BlockDataConstants.Cls == blockContents.getRawData().get(0)) {
                    keptBlockContents.addAll(buildBlockContents(temp));
                    keptBlockContents.add(blockContents);
                }
                else if(BlockDataConstants.EndOfEntry == blockContents.getRawData().get(0)) {
                    keptBlockContents.addAll(buildBlockContents(temp));
                    keptBlockContents.add(blockContents);
                }
                else {
                    String text = FileUtils.dataToString(blockContents.getRawData());
                    if(textToReplace.startsWith(temp + text)) {
                        temp = temp + text;
                        if(textToReplace.equals(temp)) {
                            keptBlockContents.addAll(buildBlockContents(replacement));
                            temp = "";
                        }
                    }
                    else {
                        keptBlockContents.addAll(buildBlockContents(temp));
                        keptBlockContents.add(blockContents);
                        temp = "";
                    }
                }
            }
        }
        blockContentsList.clear();
        blockContentsList.addAll(keptBlockContents);
    }

    protected static List<BlockContents> buildBlockContents(String text) {
        List<BlockContents> blockContents = new ArrayList<>();
        for (Short shortCharacter : FileUtils.stringToData(text)) {
            blockContents.add(new BlockSingleData(shortCharacter));
        }
        return blockContents;
    }

    protected static List<BlockContents> buildBlockContentsWithColor(String text, String subsection, BlockColorsData color) {
        List<BlockContents> blockContents = new ArrayList<>();
        String[] texts = text.split("%s");
        if(texts.length > 0) {
            blockContents.addAll(buildBlockContents(texts[0]));
        }
        blockContents.add(color);
        blockContents.addAll(buildBlockContents(subsection));
        blockContents.add(BlockColorsData.COLOR_DEFAULT);
        blockContents.addAll(buildBlockContents(texts[texts.length > 0 ? 1 : 0]));
        return blockContents;
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
    void updateMulbrukStoneConversationBlock(Block conversationBlock) { }
    void updateMulbrukWakingUpConversationBlock(Block conversationBlock) { }
    void updateMulbrukIntroConversationBlock(Block conversationBlock) { }
    void updateMulbrukHTConversationBlock(Block conversationBlock) { }
    void updateRegularEscapeConversationBlock(Block conversationBlock) { }
    void updateSwimsuitEscapeConversationBlock(Block conversationBlock) { }

    void updateHinerReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateMogerReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateFormerMekuriMasterReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestZarnacReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestXanadoReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePhilosopherGiltoriyoReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestHidlydaReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestRomancisReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestAramoReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestTritonReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestJaguarfivReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateFairyQueenWaitingForPendantReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateFairyQueenUnlockFairiesReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateFairyQueenWhenTheTimeComesReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateFairyQueenTrueShrine1ReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateFairyQueenTrueShrine2ReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateMrSlushfundPepperReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateMrSlushfundWaitingForTreasuresReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateMrSlushfundAnchorReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateMrSlushfundNeverComeBackReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestAlestReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateStrayFairyReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateGiantThexdeReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePhilosopherAlsedanaReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePhilosopherSamarantaReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestLaydocReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestAshgineReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePhilosopherFobosLadderReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePhilosopherFobosMedicineReferenceBlock(MasterNpcBlock referenceBlock) { }
    void update8BitElderReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateDuplexReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateSamieruReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateNaramuraReferenceBlock(MasterNpcBlock referenceBlock) { }
    void update8BitFairyReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestMadomonoReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestGailiousReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateMulbrukStoneConversationReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePriestAlestNoItemReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateMulbrukEscapeRegularReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateMulbrukEscapeSwimsuitReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updateMulbrukProvocativeBathingSuitReactionReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePhilosopherGiltoriyoStoneReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePhilosopherAlsedanaStoneReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePhilosopherSamarantaStoneReferenceBlock(MasterNpcBlock referenceBlock) { }
    void updatePhilosopherFobosStoneReferenceBlock(MasterNpcBlock referenceBlock) { }

    void updateXelpudFlagCheckBlock(CheckBlock flagCheckBlock) { }
    void updateXelpudScoreCheckBlock(CheckBlock scoreCheckBlock) { }
    void updateXelpudSpriteBlock(Block spriteBlock) { }
    void updateMulbrukFlagCheckBlock(CheckBlock flagCheckBlock) { }
    void updateMulbrukScoreCheckBlock(CheckBlock scoreCheckBlock) { }
    void updateMulbrukRandomBlocks(List<Block> randomBlocks) { }
    void updateMulbrukSpriteBlock(Block spriteBlock) { }

    void updateEmailBlock(Block emailBlock) { }
}
