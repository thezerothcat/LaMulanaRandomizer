package lmr.randomizer.dat;

import lmr.randomizer.HolidaySettings;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.util.BlockConstants;

import java.util.*;

public class DatFileData {
    private List<Block> datFileEntries;
    private Map<CustomBlockEnum, Short> mapOfCustomIdToBlockIndex = new HashMap<>();

    public DatFileData(List<Block> datFileEntries) {
        this.datFileEntries = datFileEntries;
    }

    public ItemNameBlock getItemNameBlock() {
        return (ItemNameBlock)datFileEntries.get(BlockConstants.ItemNameBlock);
    }
    public ItemDescriptionBlock getItemDescriptionBlock() {
        return (ItemDescriptionBlock)datFileEntries.get(BlockConstants.ItemDescriptionBlock);
    }
    public MenuBlock getMenuBlock() {
        return (MenuBlock)datFileEntries.get(BlockConstants.MenuBlock);
    }
    public SoftwareBlock getSoftwareBlock() {
        return (SoftwareBlock)datFileEntries.get(BlockConstants.SoftwareBlock);
    }
    public GrailPointBlock getGrailPointBlock() {
        return (GrailPointBlock)datFileEntries.get(BlockConstants.GrailPointBlock);
    }
    public MapNamesLimitedBlock getHTMapNamesLimitedBlock() {
        return (MapNamesLimitedBlock)datFileEntries.get(BlockConstants.HTMapNamesLimitedBlock);
    }
    public ScannableBlock getFootOfFutoScannableBlock() {
        return (ScannableBlock)datFileEntries.get(BlockConstants.FootOfFuto);
    }
    public Block getFairyQueenFirstConversationBlock() {
        return datFileEntries.get(BlockConstants.FairyQueenFirstConversation);
    }
    public Block getFairyQueenWhenTheTimeComesConversationBlock() {
        return datFileEntries.get(BlockConstants.FairyQueenWhenTheTimeComesConversation);
    }
    public Block getMekuriConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationMekuri);
    }
    public Block getMiniDollConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationMiniDoll);
    }
    public Block getPepperConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationPepper);
    }
    public Block getAnchorConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationAnchor);
    }
    public Block getXmailerConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationXmailer);
    }
    public Block getXelpudTalismanConversationBlock() {
        return datFileEntries.get(BlockConstants.XelpudTalismanConversation);
    }
    public Block getXelpudPillarConversationBlock() {
        return datFileEntries.get(BlockConstants.XelpudPillarConversation);
    }
    public Block getMulanaTalismanConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationMulanaTalisman);
    }
    public Block getMulbrukStoneConversationBlock() {
        return datFileEntries.get(BlockConstants.MulbrukStoneConversationBlock);
    }
    public Block getMulbrukWakingUpConversationBlock() {
        return datFileEntries.get(BlockConstants.MulbrukWakingUpConversationBlock);
    }
    public Block getMulbrukIntroConversationBlock() {
        return datFileEntries.get(BlockConstants.MulbrukIntroBlock);
    }
    public Block getMulbrukHTConversationBlock() {
        return datFileEntries.get(BlockConstants.MulbrukHTConversationBlock);
    }
    public Block getRegularEscapeConversationBlock() {
        return datFileEntries.get(BlockConstants.Conversation_MulbrukEscapeRegular);
    }
    public Block getSwimsuitEscapeConversationBlock() {
        return datFileEntries.get(BlockConstants.Conversation_MulbrukEscapeSwimsuit);
    }
    public Block getBookOfTheDeadConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationBookOfTheDead);
    }
    public Block getHinerConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_Hiner);
    }
    public Block getMogerConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_Moger);
    }
    public Block getPriestZarnacConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PriestZarnac);
    }
    public Block getPriestXanadoConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PriestXanado);
    }
    public Block getPriestHidlydaConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PriestHidlyda);
    }
    public Block getPriestMadomonoConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PriestMadomono);
    }
    public Block getPriestGailiousConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PriestGailious);
    }
    public Block getPriestRomancisConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PriestRomancis);
    }
    public Block getPriestAramoConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PriestAramo);
    }
    public Block getPriestTritonConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PriestTriton);
    }
    public Block getPriestJaguarfivConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PriestJaguarfiv);
    }
    public Block getPriestLaydocConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PriestLaydoc);
    }
    public Block getPriestAshgineConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PriestAshgine);
    }
    public Block getGiantThexdeConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_GiantThexde);
    }
    public Block get8BitElderConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_8BitElder);
    }
    public Block getPhilosopherGiltoriyoConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PhilosopherGiltoriyo);
    }
    public Block getPhilosopherAlsedanaConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PhilosopherAlsedana);
    }
    public Block getPhilosopherSamarantaConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PhilosopherSamaranta);
    }
    public Block getPhilosopherFobosLadderConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PhilosopherFobos_Ladder);
    }
    public Block getPhilosopherFobosMedicineConversationBlock() {
        return datFileEntries.get(BlockConstants.NpcConversation_PhilosopherFobos_MedicineCheck);
    }
    public Block getProvocativeBathingSuitConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationProvocativeBathingSuit);
    }
    public Block getSurfaceMapScannableBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationMapSurface);
    }
    public ShopBlock getNeburShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockNebur);
    }
    public ShopBlock getNeburAltShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockNeburAlt);
    }
    public ShopBlock getSidroShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockSidro);
    }
    public ShopBlock getModroShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockModro);
    }
    public ShopBlock getPenadventOfGhostShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockPenadventOfGhost);
    }
    public ShopBlock getGreedyCharlieShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockGreedyCharlie);
    }
    public ShopBlock getShalomIIIShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockShalomIII);
    }
    public ShopBlock getUsasVIShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockUsasVI);
    }
    public ShopBlock getKingvalleyIShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockKingvalleyI);
    }
    public ShopBlock getMrFishmanShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockMrFishman);
    }
    public ShopBlock getMrFishmanAltShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockMrFishmanAlt);
    }
    public ShopBlock getOperatorCombakerShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockOperatorCombaker);
    }
    public ShopBlock getYiegahKungfuShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockYiegahKungfu);
    }
    public ShopBlock getArrogantMetagearShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockArrogantMetagear);
    }
    public ShopBlock getArrogantSturdySnakeShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockArrogantSturdySnake);
    }
    public ShopBlock getYiearKungfuShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockYiearKungfu);
    }
    public ShopBlock getAffectedKnimareShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockAffectedKnimare);
    }
    public ShopBlock getMoverAthlelandShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockMoverAthleland);
    }
    public ShopBlock getGiantMopiranShopBlock() {
        if(HolidaySettings.isFools2020Mode()) {
            return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockGiantMopiranAngelShield);
        }
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockGiantMopiran);
    }
    public ShopBlock getKingvalleyIIShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockKingvalleyII);
    }
    public ShopBlock getEnergeticBelmontShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockEnergeticBelmont);
    }
    public ShopBlock getMechanicalEfspiShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockMechanicalEfspi);
    }
    public ShopBlock getMudManQubertShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockMudManQubert);
    }
    public ShopBlock getHotbloodedNemesistwoShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockHotbloodedNemesistwo);
    }
    public ShopBlock getTailorDracuetShopBlock() {
        return (ShopBlock)datFileEntries.get(BlockConstants.ShopBlockTailorDracuet);
    }
    public ShopBlock getCustomShopBlock() {
        Short customBlockIndex = getCustomBlockIndex(CustomBlockEnum.DefaultShopBlock);
        return customBlockIndex == null ? null : (ShopBlock)datFileEntries.get(customBlockIndex);
    }
    public MasterNpcBlock getHinerReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_Hiner);
    }
    public MasterNpcBlock getMogerReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_Moger);
    }
    public MasterNpcBlock getFormerMekuriMasterReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_FormerMekuriMaster_Mekuri);
    }
    public MasterNpcBlock getPriestZarnacReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestZarnac);
    }
    public MasterNpcBlock getPriestXanadoReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestXanado);
    }
    public MasterNpcBlock getPhilosopherGiltoriyoReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PhilosopherGiltoriyo);
    }
    public MasterNpcBlock getPriestHidlydaReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestHidlyda);
    }
    public MasterNpcBlock getPriestRomancisReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestRomancis);
    }
    public MasterNpcBlock getPriestAramoReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestAramo);
    }
    public MasterNpcBlock getPriestTritonReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestTriton);
    }
    public MasterNpcBlock getPriestJaguarfivReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestJaguarfiv);
    }
    public MasterNpcBlock getFairyQueenWaitingForPendantReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_FairyQueen_WaitingForPendant);
    }
    public MasterNpcBlock getFairyQueenUnlockFairiesReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_FairyQueen_UnlockFairies);
    }
    public MasterNpcBlock getFairyQueenWhenTheTimeComesReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_FairyQueen_WhenTheTimeComes);
    }
    public MasterNpcBlock getFairyQueenTrueShrine1ReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_FairyQueen_TrueShrine1);
    }
    public MasterNpcBlock getFairyQueenTrueShrine2ReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_FairyQueen_TrueShrine2);
    }
    public MasterNpcBlock getMrSlushfundPepperReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_MrSlushfund_Pepper);
    }
    public MasterNpcBlock getMrSlushfundWaitingForTreasuresReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_MrSlushfund_WaitingForTreasures);
    }
    public MasterNpcBlock getMrSlushfundAnchorReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_MrSlushfund_Anchor);
    }
    public MasterNpcBlock getMrSlushfundNeverComeBackReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_MrSlushfund_NeverComeBack);
    }
    public MasterNpcBlock getPriestAlestReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestAlest);
    }
    public MasterNpcBlock getStrayFairyReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_StrayFairy);
    }
    public MasterNpcBlock getGiantThexdeReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_GiantThexde);
    }
    public MasterNpcBlock getPhilosopherAlsedanaReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PhilosopherAlsedana);
    }
    public MasterNpcBlock getPhilosopherSamarantaReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PhilosopherSamaranta);
    }
    public MasterNpcBlock getPriestLaydocReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestLaydoc);
    }
    public MasterNpcBlock getPriestAshgineReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestAshgine);
    }
    public MasterNpcBlock getPhilosopherFobosLadderReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PhilosopherFobos_Ladder);
    }
    public MasterNpcBlock getPhilosopherFobosMedicineReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PhilosopherFobos_MedicineCheck);
    }
    public MasterNpcBlock get8BitElderReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_8BitElder);
    }
    public MasterNpcBlock getDuplexReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_duplex);
    }
    public MasterNpcBlock getSamieruReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_Samieru);
    }
    public MasterNpcBlock getNaramuraReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_Naramura);
    }
    public MasterNpcBlock get8BitFairyReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_8BitFairy);
    }
    public MasterNpcBlock getPriestMadomonoReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestMadomono);
    }
    public MasterNpcBlock getPriestGailiousReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestGailious);
    }
    public MasterNpcBlock getMulbrukStoneConversationReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_Mulbruk_StoneConversation);
    }
    public MasterNpcBlock getPriestAlestNoItemReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_PriestAlest_NoItem);
    }
    public MasterNpcBlock getMulbrukEscapeRegularReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_MulbrukEscapeRegular);
    }
    public MasterNpcBlock getMulbrukEscapeSwimsuitReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_MulbrukEscapeSwimsuit);
    }
    public MasterNpcBlock getMulbrukProvocativeBathingSuitReactionReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(BlockConstants.Master_Mulbruk_ProvocativeBathingSuitReaction);
    }
    public MasterNpcBlock getPhilosopherGiltoriyoStoneReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(getCustomBlockIndex(CustomBlockEnum.RecordableStonePhilosopherGiltoriyo));
    }
    public MasterNpcBlock getPhilosopherAlsedanaStoneReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(getCustomBlockIndex(CustomBlockEnum.RecordableStonePhilosopherAlsedana));
    }
    public MasterNpcBlock getPhilosopherSamarantaStoneReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(getCustomBlockIndex(CustomBlockEnum.RecordableStonePhilosopherSamaranta));
    }
    public MasterNpcBlock getPhilosopherFobosStoneReferenceBlock() {
        return (MasterNpcBlock)datFileEntries.get(getCustomBlockIndex(CustomBlockEnum.RecordableStonePhilosopherFobos));
    }
    public CheckBlock getXelpudFlagCheckBlock() {
        return (CheckBlock)datFileEntries.get(BlockConstants.XelpudFlagCheckBlock);
    }
    public CheckBlock getXelpudScoreCheckBlock() {
        return (CheckBlock)datFileEntries.get(BlockConstants.XelpudScoreCheckBlock);
    }
    public Block getXelpudSpriteBlock() {
        return datFileEntries.get(BlockConstants.Sprite_Xelpud);
    }
    public CheckBlock getMulbrukFlagCheckBlock() {
        return (CheckBlock)datFileEntries.get(BlockConstants.MulbrukFlagCheckBlock);
    }
    public CheckBlock getMulbrukScoreCheckBlock() {
        return (CheckBlock)datFileEntries.get(BlockConstants.MulbrukScoreCheckBlock);
    }
    public List<Block> getMulbrukRandomBlocks() {
        return Arrays.asList(datFileEntries.get(BlockConstants.MulbrukRandomBlock1),
                datFileEntries.get(BlockConstants.MulbrukRandomBlock2),
                datFileEntries.get(BlockConstants.MulbrukRandomBlock3));
    }
    public Block getMulbrukSpriteBlock() {
        return datFileEntries.get(BlockConstants.Sprite_Mulbruk);
    }
    public List<Block> getEmailBlocks() {
        List<Block> emailBlocks = new ArrayList<>();
        emailBlocks.add(datFileEntries.get(BlockConstants.Email00));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email01));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email02));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email03));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email04));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email05));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email06));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email07));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email08));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email09));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email10));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email11));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email12));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email13));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email14));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email15));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email16));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email17));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email18));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email19));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email20));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email21));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email22));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email23));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email24));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email25));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email26));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email27));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email28));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email29));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email30));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email31));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email32));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email33));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email34));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email35));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email36));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email37));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email38));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email39));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email40));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email41));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email42));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email43));
        emailBlocks.add(datFileEntries.get(BlockConstants.Email44));
        return emailBlocks;
    }

    public List<ScannableBlock> getCustomizableTabletBlocks() {
        List<ScannableBlock> tabletBlocks = new ArrayList<>();
        for(int blockNumber : BlockConstants.TABLET_BLOCKS) {
            tabletBlocks.add((ScannableBlock)datFileEntries.get(blockNumber));
        }
        return tabletBlocks;
    }

    public void addCustomBlock(CustomBlockEnum customBlockId, Block block) {
        block.setBlockNumber(datFileEntries.size());
        datFileEntries.add(block);
        mapOfCustomIdToBlockIndex.put(customBlockId, (short)block.getBlockNumber());
    }

    public Short getCustomBlockIndex(CustomBlockEnum customBlockEnum) {
        return customBlockEnum == null ? null : mapOfCustomIdToBlockIndex.get(customBlockEnum);
    }

    public Map<CustomBlockEnum, Short> getMapOfCustomIdToBlockIndex() {
        return mapOfCustomIdToBlockIndex;
    }
}
