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
    public Block getMulbrukIntroConversationBlock() {
        return datFileEntries.get(BlockConstants.MulbrukIntroBlock);
    }
    public Block getBookOfTheDeadConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationBookOfTheDead);
    }
    public Block getSurfaceMapScannableBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationMapSurface);
    }
    public Block getProvocativeBathingSuitConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationProvocativeBathingSuit);
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
    public CheckBlock getXelpudFlagCheckBlock() {
        return (CheckBlock)datFileEntries.get(BlockConstants.XelpudFlagCheckBlock);
    }
    public CheckBlock getXelpudScoreCheckBlock() {
        return (CheckBlock)datFileEntries.get(BlockConstants.XelpudScoreCheckBlock);
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
