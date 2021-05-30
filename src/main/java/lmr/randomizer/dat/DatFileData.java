package lmr.randomizer.dat;

import lmr.randomizer.BlockConstants;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.conversation.CheckBlock;

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
    public Block getMiniDollConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationMiniDoll);
    }
    public Block getPepperConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationPepper);
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
    public Block getProvocativeBathingSuitConversationBlock() {
        return datFileEntries.get(BlockConstants.ItemConversationProvocativeBathingSuit);
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
        return mapOfCustomIdToBlockIndex.get(customBlockEnum);
    }

    public Map<CustomBlockEnum, Short> getMapOfCustomIdToBlockIndex() {
        return mapOfCustomIdToBlockIndex;
    }
}
