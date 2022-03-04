package lmr.randomizer.dat.update;

import lmr.randomizer.*;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.blocks.contents.*;
import lmr.randomizer.dat.blocks.contents.entries.TextEntry;
import lmr.randomizer.randomization.data.*;
import lmr.randomizer.util.BlockDataConstants;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.ItemConstants;
import lmr.randomizer.util.MantraConstants;

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
        updateTowerOfTheGoddessSnapshotsScanBlock(datFileData.getTowerOfTheGoddessSnapshotsScanBlock());
        updateTowerOfRuinSnapshotsScanBlock(datFileData.getTowerOfRuinSnapshotsScanBlock());

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
        updateXelpudHeldItemPepperBlock(datFileData.getXelpudHeldItemPepperBlock());
        updateXelpudTalismanConversationBlock(datFileData.getXelpudTalismanConversationBlock());
        updateXelpudPillarConversationBlock(datFileData.getXelpudPillarConversationBlock());
        updateXelpudMSX2ConversationBlock(datFileData.getXelpudMSX2ConversationBlock());
        updateMulanaTalismanConversationBlock(datFileData.getMulanaTalismanConversationBlock());
        updateBookOfTheDeadConversationBlock(datFileData.getBookOfTheDeadConversationBlock());
        updateBookOfTheDeadRepeatConversationBlock(datFileData.getBookOfTheDeadRepeatConversationBlock());
        updateProvocativeBathingSuitConversationBlock(datFileData.getProvocativeBathingSuitConversationBlock());

        updateHinerConversationBlock(datFileData.getHinerConversationBlock());
        updateMogerConversationBlock(datFileData.getMogerConversationBlock());
        updatePriestZarnacConversationBlock(datFileData.getPriestZarnacConversationBlock());
        updatePriestXanadoConversationBlock(datFileData.getPriestXanadoConversationBlock());
        updatePriestHidlydaConversationBlock(datFileData.getPriestHidlydaConversationBlock());
        updatePriestMadomonoConversationBlock(datFileData.getPriestMadomonoConversationBlock());
        updatePriestGailiousConversationBlock(datFileData.getPriestGailiousConversationBlock());
        updatePriestRomancisConversationBlock(datFileData.getPriestRomancisConversationBlock());
        updatePriestAramoConversationBlock(datFileData.getPriestAramoConversationBlock());
        updatePriestTritonConversationBlock(datFileData.getPriestTritonConversationBlock());
        updatePriestJaguarfivConversationBlock(datFileData.getPriestJaguarfivConversationBlock());
        updatePriestLaydocConversationBlock(datFileData.getPriestLaydocConversationBlock());
        updatePriestAshgineConversationBlock(datFileData.getPriestAshgineConversationBlock());
        updateGiantThexdeConversationBlock(datFileData.getGiantThexdeConversationBlock());
        update8BitElderConversationBlock(datFileData.get8BitElderConversationBlock());
        updatePhilosopherGiltoriyoConversationBlock(datFileData.getPhilosopherGiltoriyoConversationBlock());
        updatePhilosopherAlsedanaConversationBlock(datFileData.getPhilosopherAlsedanaConversationBlock());
        updatePhilosopherSamarantaConversationBlock(datFileData.getPhilosopherSamarantaConversationBlock());
        updatePhilosopherFobosLadderConversationBlock(datFileData.getPhilosopherFobosLadderConversationBlock());
        updatePhilosopherFobosMedicineConversationBlock(datFileData.getPhilosopherFobosMedicineConversationBlock());

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
        for(int mailNumber = 0; mailNumber < datFileData.getOrderedEmailBlocks().size(); mailNumber++) {
            Block emailBlock = datFileData.getOrderedEmailBlocks().get(mailNumber);
            updateEmailBlock(emailBlock, mailNumber);
        }
        for(MapGraphicsBlock mapGraphicsBlock : datFileData.getMapGraphicsBlocks()) {
            updateMapGraphicsBlock(mapGraphicsBlock);
        }

        for(ScannableBlock customizableTabletBlock : datFileData.getCustomizableTabletBlocks()) {
            updateScannableBlock(customizableTabletBlock);
        }
        updateLaptopScannableBlock(datFileData.getLaptopScannableBlock());
        updateSurfaceMapScannableBlock(datFileData.getSurfaceMapScannableBlock());
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

    protected static TextEntry buildTextEntryWithCommands(String textToUse) {
        TextEntry textEntry = new TextEntry();
        for(String section : extractSections(textToUse)) {
            if(section.startsWith("{COLOR=")) {
                textEntry.getData().addAll(
                        getColor(section.replaceAll("\\{COLOR=", "").replaceAll("}", "")).getRawData());
            }
            else if(section.startsWith("{MANTRA=")) {
                textEntry.getData().addAll(
                        getMantra(section.replaceAll("\\{MANTRA=", "").replaceAll("}", "")).getRawData());
            }
            else if(section.equals("{PAGE}")) {
                textEntry.getData().add(BlockDataConstants.Cls);
            }
            else if(section.startsWith("{ITEM=")) {
                BlockItemData itemData = getItem(section.replaceAll("\\{ITEM=", "").replaceAll("}", ""));
                if(itemData != null) {
                    textEntry.getData().addAll(itemData.getRawData());
                }
            }
            else {
                textEntry.getData().addAll(FileUtils.stringToData(section));
            }
        }
        return textEntry;
    }

    protected static List<Short> buildRawDataWithCommands(String textToUse) {
        List<Short> rawData = new ArrayList<>();
        for(String section : extractSections(textToUse)) {
            if(section.startsWith("{COLOR=")) {
                rawData.addAll(
                        getColor(section.replaceAll("\\{COLOR=", "").replaceAll("}", "")).getRawData());
            }
            else if(section.equals("{PAGE}")) {
                rawData.add(BlockDataConstants.Cls);
            }
            else if(section.startsWith("{MANTRA=")) {
                rawData.addAll(
                        getMantra(section.replaceAll("\\{MANTRA=", "").replaceAll("}", "")).getRawData());
            }
            else if(section.startsWith("{ITEM=")) {
                BlockItemData itemData = getItem(section.replaceAll("\\{ITEM=", "").replaceAll("}", ""));
                if(itemData != null) {
                    rawData.addAll(itemData.getRawData());
                }
            }
            else if(section.startsWith("{FLAG ")) {
                BlockFlagData flagData = getFlag(section.replaceAll("\\{FLAG ", "").replaceAll("}", ""));
                if(flagData != null) {
                    rawData.addAll(flagData.getRawData());
                }
            }
            else {
                rawData.addAll(FileUtils.stringToData(section));
            }
        }
        return rawData;
    }

    private static List<String> extractSections(String originalText) {
        List<String> tokens = new ArrayList<>();
        int specialDataStartIndex;
        int specialDataEndIndex;
        while(originalText.contains("{")) {
            specialDataStartIndex = originalText.indexOf('{');
            specialDataEndIndex = originalText.indexOf('}');
            if(specialDataStartIndex > 0) {
                tokens.add(originalText.substring(0, specialDataStartIndex));
            }
            tokens.add(originalText.substring(specialDataStartIndex, specialDataEndIndex + 1));
            originalText = originalText.substring(specialDataEndIndex + 1);
        }
        if(!originalText.isEmpty()) {
            tokens.add(originalText);
        }
        return tokens;
    }

    private static BlockColorsData getColor(String color) {
        if(color != null) {
            color = color.toUpperCase();
            if("MANTRA".equals(color)) {
                return BlockColorsData.COLOR_MANTRAS_DARKRED;
            }
            if(color.contains("THREAT") || color.contains("ENEMY") || "RED".equals(color)) {
                return BlockColorsData.COLOR_THREATS_RED;
            }
            if(color.contains("ITEM") || "GREEN".equals(color)) {
                return BlockColorsData.COLOR_ITEMS_GREEN;
            }
            if(color.contains("PERSON") || color.contains("PLACE") || "BLUE".equals(color)) {
                return BlockColorsData.COLOR_PEOPLE_PLACES_BLUE;
            }
            if(color.contains("SOFTWARE") || "YELLOW".equals(color)) {
                return BlockColorsData.COLOR_SOFTWARE_YELLOW;
            }
            String[] rgb = color.split("-");
            if(rgb.length == 3) {
                return new BlockColorsData(getDecimalOrHex(rgb[0]), getDecimalOrHex(rgb[1]), getDecimalOrHex(rgb[2]));
            }
        }
        return BlockColorsData.COLOR_DEFAULT;
    }

    private static int getDecimalOrHex(String decimalOrHex) {
        if(decimalOrHex.startsWith("0x")) {
            decimalOrHex = decimalOrHex.replaceAll("^0x", "");
            return Integer.parseInt(decimalOrHex, 16);
        }

        try {
            return Integer.parseInt(decimalOrHex);
        }
        catch(NumberFormatException ex) {
            return Integer.parseInt(decimalOrHex, 16);
        }
    }

    private static BlockMantraData getMantra(String mantra) {
        if(mantra != null) {
            mantra = mantra.toUpperCase();
            if("BIRTH".equals(mantra) || "0".equals(mantra)) {
                return new BlockMantraData(MantraConstants.BIRTH);
            }
            if("DEATH".equals(mantra) || "1".equals(mantra)) {
                return new BlockMantraData(MantraConstants.DEATH);
            }
            if("MARDUK".equals(mantra) || "2".equals(mantra)) {
                return new BlockMantraData(MantraConstants.MARDUK);
            }
            if("SABBAT".equals(mantra) || "3".equals(mantra)) {
                return new BlockMantraData(MantraConstants.SABBAT);
            }
            if("MU".equals(mantra) || "4".equals(mantra)) {
                return new BlockMantraData(MantraConstants.MU);
            }
            if("VIY".equals(mantra) || "5".equals(mantra)) {
                return new BlockMantraData(MantraConstants.VIY);
            }
            if("BAHRUN".equals(mantra) || "6".equals(mantra)) {
                return new BlockMantraData(MantraConstants.BAHRUN);
            }
            if("WEDJET".equals(mantra) || "7".equals(mantra)) {
                return new BlockMantraData(MantraConstants.WEDJET);
            }
            if("ABUTO".equals(mantra) || "8".equals(mantra)) {
                return new BlockMantraData(MantraConstants.ABUTO);
            }
            if("LAMULANA".equals(mantra) || "9".equals(mantra)) {
                return new BlockMantraData(MantraConstants.LAMULANA);
            }
        }
        return new BlockMantraData(MantraConstants.BIRTH);
    }

    private static BlockItemData getItem(String item) {
        if(item != null) {
            GameObjectId itemInfo = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(item);
            if(itemInfo != null) {
                return new BlockItemData(itemInfo.getInventoryArg());
            }
        }
        return null;
    }

    private static BlockFlagData getFlag(String flag) {
        if(flag != null) {
            String[] indexAndValue = flag.split("=");
            if(indexAndValue.length == 2) {
                return new BlockFlagData(getDecimalOrHex(indexAndValue[0]), getDecimalOrHex(indexAndValue[1]));
            }
        }
        return null;
    }

    protected static void replaceText(List<BlockContents> blockContentsList, String textToReplace, String replacement) {
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

    protected static List<Short> buildStringDataWithColor(String text, String subsection, BlockColorsData color) {
        List<Short> stringData = new ArrayList<>();
        String[] texts = text.split("%s");
        if(texts.length > 0) {
            stringData.addAll(FileUtils.stringToData(texts[0]));
        }
        stringData.addAll(color.getRawData());
        stringData.addAll(FileUtils.stringToData(subsection));
        stringData.addAll(BlockColorsData.COLOR_DEFAULT.getRawData());
        stringData.addAll(FileUtils.stringToData(texts[texts.length > 0 ? 1 : 0]));
        return stringData;
    }

    void updateItemNames(ItemNameBlock itemNameBlock) { }
    void updateItemDescriptions(ItemDescriptionBlock itemDescriptionBlock) { }
    void updateMenuData(MenuBlock menuBlock) { }
    void updateSoftwareData(SoftwareBlock softwareBlock) { }
    void updateGrailPoints(GrailPointBlock grailPointBlock) { }

    void updateHTMapNamesLimitedBlock(MapNamesLimitedBlock mapNamesLimitedBlock) { }

    void updateFootOfFutoScannableBlock(ScannableBlock scannableBlock) { }
    void updateTowerOfTheGoddessSnapshotsScanBlock(Block snapshotsScanBlock) { }
    void updateTowerOfRuinSnapshotsScanBlock(Block snapshotsScanBlock) { }

    void updateMekuriConversationBlock(Block conversationBlock) { }
    void updateMiniDollConversationBlock(Block conversationBlock) { }
    void updatePepperConversationBlock(Block conversationBlock) { }
    void updateAnchorConversationBlock(Block conversationBlock) { }
    void updateXmailerConversationBlock(Block conversationBlock) { }
    void updateXelpudHeldItemPepperBlock(Block conversationBlock) { }
    void updateXelpudTalismanConversationBlock(Block conversationBlock) { }
    void updateXelpudPillarConversationBlock(Block conversationBlock) { }
    void updateXelpudMSX2ConversationBlock(Block conversationBlock) { }
    void updateMulanaTalismanConversationBlock(Block conversationBlock) { }
    void updateBookOfTheDeadConversationBlock(Block conversationBlock) { }
    void updateBookOfTheDeadRepeatConversationBlock(Block conversationBlock) { }
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

    void updateHinerConversationBlock(Block conversationBlock) { }
    void updateMogerConversationBlock(Block conversationBlock) { }
    void updatePriestZarnacConversationBlock(Block conversationBlock) { }
    void updatePriestXanadoConversationBlock(Block conversationBlock) { }
    void updatePriestHidlydaConversationBlock(Block conversationBlock) { }
    void updatePriestMadomonoConversationBlock(Block conversationBlock) { }
    void updatePriestGailiousConversationBlock(Block conversationBlock) { }
    void updatePriestRomancisConversationBlock(Block conversationBlock) { }
    void updatePriestAramoConversationBlock(Block conversationBlock) { }
    void updatePriestTritonConversationBlock(Block conversationBlock) { }
    void updatePriestJaguarfivConversationBlock(Block conversationBlock) { }
    void updatePriestLaydocConversationBlock(Block conversationBlock) { }
    void updatePriestAshgineConversationBlock(Block conversationBlock) { }
    void updateGiantThexdeConversationBlock(Block conversationBlock) { }
    void update8BitElderConversationBlock(Block conversationBlock) { }
    void updatePhilosopherGiltoriyoConversationBlock(Block conversationBlock) { }
    void updatePhilosopherAlsedanaConversationBlock(Block conversationBlock) { }
    void updatePhilosopherSamarantaConversationBlock(Block conversationBlock) { }
    void updatePhilosopherFobosLadderConversationBlock(Block conversationBlock) { }
    void updatePhilosopherFobosMedicineConversationBlock(Block conversationBlock) { }

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

    void updateEmailBlock(Block emailBlock, int mailNumber) { }
    void updateMapGraphicsBlock(MapGraphicsBlock mapGraphicsBlock) { }
    void updateScannableBlock(ScannableBlock scannableBlock) { }
    void updateLaptopScannableBlock(Block scannableBlock) { }
    void updateSurfaceMapScannableBlock(Block scannableBlock) { }

    protected void updateShopBlock(ShopBlock shopBlock, ShopInventory shopInventory) {
        shopBlock.getInventoryItemArgsList().getData().clear();
        shopBlock.getInventoryItemArgsList().getData().add(shopInventory.getItem1().getInventoryArg());
        shopBlock.getInventoryItemArgsList().getData().add(shopInventory.getItem2().getInventoryArg());
        shopBlock.getInventoryItemArgsList().getData().add(shopInventory.getItem3().getInventoryArg());

        updatePriceAndCount(shopBlock, shopInventory);
        updateShopFlags(shopBlock, shopInventory);

        updateAskItemName(shopBlock.getString(3), shopInventory.getItem1());
        updateAskItemName(shopBlock.getString(4), shopInventory.getItem2());
        updateAskItemName(shopBlock.getString(5), shopInventory.getItem3());

        updateSoldText(shopBlock.getString(6), shopInventory.getItem1());
        updateSoldText(shopBlock.getString(7), shopInventory.getItem2());
        updateSoldText(shopBlock.getString(8), shopInventory.getItem3());

        List<Short> bunemonData = shopBlock.getBunemonText().getData();
        bunemonData.clear();
        updateBunemonText(bunemonData, shopInventory.getItem1(), shopBlock.getItem1Price());
        bunemonData.addAll(FileUtils.stringToData(" , "));
        updateBunemonText(bunemonData, shopInventory.getItem2(), shopBlock.getItem2Price());
        bunemonData.addAll(FileUtils.stringToData(" , "));
        updateBunemonText(bunemonData, shopInventory.getItem3(), shopBlock.getItem3Price());

        shopBlock.setBunemonLocation(new BlockStringData(Translations.getLocationAndNpc(shopInventory.getNpcLocation(), shopInventory.getNpcName())));
    }

    private void updatePriceAndCount(ShopBlock shopBlock, ShopInventory shopInventory) {
        ItemPriceCount itemPriceCount = shopInventory.getItem1().getItemPriceCount();
        if(itemPriceCount != null) {
            if(itemPriceCount.getPrice() != null) {
                shopBlock.setItem1Price(itemPriceCount.getPrice());
            }
            if(itemPriceCount.getCount() != null) {
                shopBlock.setItem1Count(itemPriceCount.getCount());
            }
        }
        itemPriceCount = shopInventory.getItem2().getItemPriceCount();
        if(itemPriceCount != null) {
            if(itemPriceCount.getPrice() != null) {
                shopBlock.setItem2Price(itemPriceCount.getPrice());
            }
            if(itemPriceCount.getCount() != null) {
                shopBlock.setItem2Count(itemPriceCount.getCount());
            }
        }
        itemPriceCount = shopInventory.getItem3().getItemPriceCount();
        if(itemPriceCount != null) {
            if(itemPriceCount.getPrice() != null) {
                shopBlock.setItem3Price(itemPriceCount.getPrice());
            }
            if(itemPriceCount.getCount() != null) {
                shopBlock.setItem3Count(itemPriceCount.getCount());
            }
        }
    }

    public void updateShopFlags(ShopBlock shopBlock, ShopInventory shopInventory) {
        short shopItem1Flag = shopInventory.getItem1().getWorldFlag();
        short shopItem2Flag = shopInventory.getItem2().getWorldFlag();
        short shopItem3Flag = shopInventory.getItem3().getWorldFlag();

        shopBlock.getFlagList().getData().clear();
        shopBlock.getFlagList().getData().add(shopItem1Flag);
        shopBlock.getFlagList().getData().add(shopItem2Flag);
        shopBlock.getFlagList().getData().add(shopItem3Flag);

        if(!"Yiegah Kungfu".equals(shopInventory.getNpcName())) {
            shopBlock.getExitFlagList().getData().clear();
            shopBlock.getExitFlagList().getData().add(shopItem1Flag);
            shopBlock.getExitFlagList().getData().add(shopItem2Flag);
            shopBlock.getExitFlagList().getData().add(shopItem3Flag);
        }
    }

    private void updateAskItemName(BlockStringData blockStringData, ShopInventoryData shopInventoryData) {
        if(shopInventoryData.getCustomTextNumber() != null) {
            blockStringData.getData().clear();
            blockStringData.getData().addAll(buildStringDataWithColor(
                    Translations.getText("CustomShopText.Offer" + shopInventoryData.getCustomTextNumber()),
                    Translations.getText("CustomShopText.Name" + shopInventoryData.getCustomTextNumber()),
                    BlockColorsData.COLOR_SOFTWARE_YELLOW));
            if(shopInventoryData.getCustomTextNumber() == 2) {
                blockStringData.getData().addAll(new BlockMantraData(MantraConstants.LAMULANA).getRawData());
            }
            return;
        }

        if(blockStringData.getItemNameStartIndex() == null || blockStringData.getItemNameEndIndex() == null) {
            return;
        }

        List<Short> newBlockData = new ArrayList<>(blockStringData.getData().subList(0, blockStringData.getItemNameStartIndex()));
        if(HolidaySettings.isFools2020Mode()) {
            if(shopInventoryData.getInventoryArg() == ItemConstants.SCRIPTURES) {
                newBlockData.addAll(FileUtils.stringToData(Translations.getText("items.HeatproofCase")));
            }
            else if(shopInventoryData.getInventoryArg() == ItemConstants.HEATPROOF_CASE) {
                newBlockData.addAll(FileUtils.stringToData(Translations.getText("items.Scriptures")));
            }
            else {
                addShrinePrefixIfNeeded(newBlockData, shopInventoryData);
                newBlockData.add(BlockDataConstants.ItemName);
                newBlockData.add(shopInventoryData.getInventoryArg());
            }
        }
        else {
            addShrinePrefixIfNeeded(newBlockData, shopInventoryData);
            newBlockData.add(BlockDataConstants.ItemName);
            newBlockData.add(shopInventoryData.getInventoryArg());
        }
        newBlockData.addAll(blockStringData.getData().subList(blockStringData.getItemNameEndIndex(), blockStringData.getData().size()));
        blockStringData.getData().clear();
        blockStringData.getData().addAll(newBlockData);
    }

    private void updateSoldText(BlockStringData blockStringData, ShopInventoryData shopInventoryData) {
        if(HolidaySettings.isFools2022Mode() && ItemConstants.HAND_SCANNER == shopInventoryData.getInventoryArg()) {
            blockStringData.getData().clear();
            blockStringData.getData().addAll(buildRawDataWithCommands(Translations.getText("event.fools2022.Purchase.HandScanner")));
            return;
        }

        if(shopInventoryData.getCustomTextNumber() != null) {
            blockStringData.getData().clear();
            String text = Translations.getText("CustomShopText.Purchase" + shopInventoryData.getCustomTextNumber());
            if(text == null) {
                int subIndex = 1;
                boolean multiWindow = false;
                text = Translations.getText("CustomShopText.Purchase" + shopInventoryData.getCustomTextNumber() + "." + subIndex++);
                while(text != null) {
                    if(multiWindow) {
                        blockStringData.getData().add(BlockDataConstants.Cls);
                    }
                    blockStringData.getData().addAll(FileUtils.stringToData(text));
                    text = Translations.getText("CustomShopText.Purchase" + shopInventoryData.getCustomTextNumber() + "." + subIndex++);
                }
            }
            else {
                blockStringData.getData().addAll(FileUtils.stringToData(text));
            }
        }
    }

    private void updateBunemonText(List<Short> bunemonData, ShopInventoryData shopInventoryData, Short itemPrice) {
        if(HolidaySettings.isFools2020Mode()) {
            if(shopInventoryData.getInventoryArg() == ItemConstants.SCRIPTURES) {
                bunemonData.addAll(FileUtils.stringToData(Translations.getText("items.HeatproofCase")));
            }
            else if(shopInventoryData.getInventoryArg() == ItemConstants.HEATPROOF_CASE) {
                bunemonData.addAll(FileUtils.stringToData(Translations.getText("items.Scriptures")));
            }
            else {
                addShrinePrefixIfNeeded(bunemonData, shopInventoryData);
                bunemonData.add(BlockDataConstants.ItemName);
                bunemonData.add(shopInventoryData.getInventoryArg());
            }
        }
        else {
            addShrinePrefixIfNeeded(bunemonData, shopInventoryData);
            bunemonData.add(BlockDataConstants.ItemName);
            bunemonData.add(shopInventoryData.getInventoryArg());
        }

        bunemonData.add(BlockDataConstants.Space);
        bunemonData.addAll(FileUtils.stringToData(Short.toString(itemPrice)));
    }

    private void addShrinePrefixIfNeeded(List<Short> bunemonData, ShopInventoryData shopInventoryData) {
        if(shopInventoryData.getWorldFlag() == FlagConstants.WF_MAP_SHRINE) {
            bunemonData.addAll(FileUtils.stringToData(Translations.getText("shop.shrinePrefix")));
        }
    }
}
