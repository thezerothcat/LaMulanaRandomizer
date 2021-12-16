package lmr.randomizer.dat.update;

import lmr.randomizer.*;
import lmr.randomizer.dat.*;
import lmr.randomizer.dat.blocks.Block;
import lmr.randomizer.dat.blocks.MasterNpcBlock;
import lmr.randomizer.dat.blocks.contents.*;
import lmr.randomizer.dat.blocks.ShopBlock;
import lmr.randomizer.randomization.ItemRandomizer;
import lmr.randomizer.randomization.NpcRandomizer;
import lmr.randomizer.randomization.ShopRandomizer;
import lmr.randomizer.randomization.data.*;
import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.util.*;

import java.util.*;

public class RandomizationDatUpdater extends DatUpdater {
    private Map<Integer, List<GameObject>> mapOfShopBlockToShopObjects = new HashMap<>();
    private Map<GameObjectId, List<Block>> mapOfChestIdentifyingInfoToBlock = new HashMap<>();

    private ItemRandomizer itemRandomizer;
    private ShopRandomizer shopRandomizer;
    private NpcRandomizer npcRandomizer;
    private FlagManager flagManager;

//    private Map<GameObjectId, GameObjectId> mapOfItemLocationToNewContents = new HashMap<>();

    public RandomizationDatUpdater(DatFileData datFileData, ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer, NpcRandomizer npcRandomizer, FlagManager flagManager) {
        super(datFileData);
        this.itemRandomizer = itemRandomizer;
        this.shopRandomizer = shopRandomizer;
        this.npcRandomizer = npcRandomizer;
        this.flagManager = flagManager;
    }

    @Override
    public void updateMekuriConversationBlock(Block conversationBlock) {
        updateRegularConversationBlock(conversationBlock, "mekuri.exe", FlagConstants.WF_SOFTWARE_MEKURI, ItemConstants.SOFTWARE_MEKURI);
    }

    @Override
    public void updateMiniDollConversationBlock(Block conversationBlock) {
        String newContents = itemRandomizer.getNewContents("Mini Doll");
        boolean isCoinChest = newContents.startsWith("Coin:");
        boolean isRemovedItem = itemRandomizer.isRemovedItem(newContents);

        GameObjectId newContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(newContents);
        short newWorldFlag = isRemovedItem ? flagManager.getNewWorldFlag(FlagConstants.WF_MINI_DOLL) : (short)newContentsData.getWorldFlag();

        Integer blockContentIndex = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                BlockFlagData flagData = (BlockFlagData)blockContents;
                if(flagData.getWorldFlag() == FlagConstants.WF_MINI_DOLL) {
                    flagData.setWorldFlag(newWorldFlag);
                    flagData.setFlagValue((short)2);
                }
            }
            else if(blockContents instanceof BlockItemData) {
                BlockItemData itemData = (BlockItemData)blockContents;
                if(itemData.getItemData() == ItemConstants.MINI_DOLL) {
                    if(isRemovedItem) {
                        itemData.setItemData(ItemConstants.WEIGHT);
                    }
                    else if(isCoinChest) {
                        itemData.setItemData(ItemConstants.COIN);
                    }
                    else {
                        itemData.setItemData(newContentsData.getInventoryArg());
                    }
                }
                blockContentIndex = i;
            }
        }

        if(isCoinChest && blockContentIndex != null) {
            for(int i = 1; i < newContentsData.getInventoryArg(); i++) {
                conversationBlock.getBlockContents().add(blockContentIndex, new BlockItemData(ItemConstants.COIN));
            }
        }
    }

    @Override
    void updatePepperConversationBlock(Block conversationBlock) {
        String newContents = itemRandomizer.getNewContents("Pepper");
        boolean isCoinChest = newContents.startsWith("Coin:");
        boolean isRemovedItem = itemRandomizer.isRemovedItem(newContents);

        GameObjectId newContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(newContents);
        short newWorldFlag = isRemovedItem ? flagManager.getNewWorldFlag(FlagConstants.WF_PEPPER) : (short)newContentsData.getWorldFlag();

        Integer blockContentIndex = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                BlockFlagData flagData = (BlockFlagData)blockContents;
                if(flagData.getWorldFlag() == FlagConstants.WF_PEPPER) {
                    flagData.setWorldFlag(newWorldFlag);
                    flagData.setFlagValue((short)2);
                }
                else if(flagData.getWorldFlag() == FlagConstants.MR_SLUSHFUND_CONVERSATION) {
                    // The flag for the Pepper/Treasures/Anchor sequence is being replaced with custom world flags,
                    // but it won't show up in the block as a thing that matches the world flag, so special case!
                    // Note: There is no case where we see a flag 0x228 and want to keep it.
                    flagData.setWorldFlag(newWorldFlag);
                    flagData.setFlagValue((short)2);
                }
            }
            else if(blockContents instanceof BlockItemData) {
                BlockItemData itemData = (BlockItemData)blockContents;
                if(itemData.getItemData() == ItemConstants.PEPPER) {
                    if(isRemovedItem) {
                        itemData.setItemData(ItemConstants.WEIGHT);
                    }
                    else if(isCoinChest) {
                        itemData.setItemData(ItemConstants.COIN);
                    }
                    else {
                        itemData.setItemData(newContentsData.getInventoryArg());
                    }
                }
                blockContentIndex = i;
            }
        }

        if(isCoinChest && blockContentIndex != null) {
            for(int i = 1; i < newContentsData.getInventoryArg(); i++) {
                conversationBlock.getBlockContents().add(blockContentIndex, new BlockItemData(ItemConstants.COIN));
            }
        }
    }

    @Override
    void updateAnchorConversationBlock(Block conversationBlock) {
        String newContents = itemRandomizer.getNewContents("Anchor");
        boolean isCoinChest = newContents.startsWith("Coin:");
        boolean isRemovedItem = itemRandomizer.isRemovedItem(newContents);

        GameObjectId newContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(newContents);
        short newWorldFlag = isRemovedItem ? flagManager.getNewWorldFlag(FlagConstants.WF_ANCHOR) : (short)newContentsData.getWorldFlag();

        Integer blockContentIndex = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                BlockFlagData flagData = (BlockFlagData)blockContents;
                if(flagData.getWorldFlag() == FlagConstants.WF_ANCHOR) {
                    flagData.setWorldFlag(newWorldFlag);
                    flagData.setFlagValue((short)2);
                }
                else if(flagData.getWorldFlag() == FlagConstants.MR_SLUSHFUND_CONVERSATION) {
                    // The flag for the Pepper/Treasures/Anchor sequence is being replaced with custom world flags,
                    // but it won't show up in the block as a thing that matches the world flag, so special case!
                    // Note: There is no case where we see a flag 0x228 and want to keep it.
                    flagData.setWorldFlag(newWorldFlag);
                    flagData.setFlagValue((short)2);
                }
            }
            else if(blockContents instanceof BlockItemData) {
                BlockItemData itemData = (BlockItemData)blockContents;
                if(itemData.getItemData() == ItemConstants.ANCHOR) {
                    if(isRemovedItem) {
                        itemData.setItemData(ItemConstants.WEIGHT);
                    }
                    else if(isCoinChest) {
                        itemData.setItemData(ItemConstants.COIN);
                    }
                    else {
                        itemData.setItemData(newContentsData.getInventoryArg());
                    }
                }
                blockContentIndex = i;
            }
        }

        if(isCoinChest && blockContentIndex != null) {
            for(int i = 1; i < newContentsData.getInventoryArg(); i++) {
                conversationBlock.getBlockContents().add(blockContentIndex, new BlockItemData(ItemConstants.COIN));
            }
        }
    }

    @Override
    void updateXmailerConversationBlock(Block conversationBlock) {
        updateRegularConversationBlock(conversationBlock, "xmailer.exe", FlagConstants.WF_SOFTWARE_XMAILER, ItemConstants.SOFTWARE_XMAILER);
    }

    @Override
    void updateMulanaTalismanConversationBlock(Block conversationBlock) {
        updateRegularConversationBlock(conversationBlock, "Mulana Talisman", FlagConstants.WF_MULANA_TALISMAN, ItemConstants.MULANA_TALISMAN);
    }

    @Override
    void updateBookOfTheDeadConversationBlock(Block conversationBlock) {
        updateRegularConversationBlock(conversationBlock, "Book of the Dead", FlagConstants.WF_BOOK_OF_THE_DEAD, ItemConstants.BOOK_OF_THE_DEAD);
    }

    @Override
    void updateSurfaceMapScannableBlock(Block scannableBlock) {
        String newContents = itemRandomizer.getNewContents("Map (Surface)");
        boolean isCoinChest = newContents.startsWith("Coin:");
        boolean isRemovedItem = itemRandomizer.isRemovedItem(newContents);

        GameObjectId newContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(newContents);
        short newWorldFlag = isRemovedItem ? flagManager.getNewWorldFlag(FlagConstants.WF_MAP_SURFACE) : (short)newContentsData.getWorldFlag();

        Integer blockContentIndex = null;
        for(int i = 0; i < scannableBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = scannableBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                BlockFlagData flagData = (BlockFlagData)blockContents;
                if(flagData.getWorldFlag() == FlagConstants.WF_MAP_SURFACE) {
                    flagData.setWorldFlag(newWorldFlag);
                    flagData.setFlagValue((short)1); // Set to 1, will be set to 2 by an ItemGive on this screen.
                }
            }
            else if(blockContents instanceof BlockItemData) {
                BlockItemData itemData = (BlockItemData)blockContents;
                if(itemData.getItemData() == ItemConstants.MAP) {
                    if(isRemovedItem) {
                        itemData.setItemData(ItemConstants.WEIGHT);
                    }
                    else if(isCoinChest) {
                        itemData.setItemData(ItemConstants.COIN);
                    }
                    else {
                        itemData.setItemData(newContentsData.getInventoryArg());
                    }
                }
                blockContentIndex = i;
            }
        }

        if(isCoinChest && blockContentIndex != null) {
            for(int i = 1; i < newContentsData.getInventoryArg(); i++) {
                scannableBlock.getBlockContents().add(blockContentIndex, new BlockItemData(ItemConstants.COIN));
            }
        }
    }

    @Override
    void updateProvocativeBathingSuitConversationBlock(Block conversationBlock) {
        updateRegularConversationBlock(conversationBlock, "Provocative Bathing Suit", FlagConstants.WF_PROVOCATIVE_BATHING_SUIT, ItemConstants.PROVOCATIVE_BATHING_SUIT);
    }

    private void updateRegularConversationBlock(Block conversationBlock, String itemLocation, int itemLocationWorldFlag, int itemLocationItemArg) {
        String newContents = itemRandomizer.getNewContents(itemLocation);
        boolean isCoinChest = newContents.startsWith("Coin:");
        boolean isRemovedItem = itemRandomizer.isRemovedItem(newContents);

        GameObjectId newContentsData = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(newContents);
        short newWorldFlag = isRemovedItem ? flagManager.getNewWorldFlag(itemLocationWorldFlag) : (short)newContentsData.getWorldFlag();

        Integer blockContentIndex = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                BlockFlagData flagData = (BlockFlagData)blockContents;
                if(flagData.getWorldFlag() == itemLocationWorldFlag) {
                    flagData.setWorldFlag(newWorldFlag);
                    flagData.setFlagValue((short)2);
                }
            }
            else if(blockContents instanceof BlockItemData) {
                BlockItemData itemData = (BlockItemData)blockContents;
                if(itemData.getItemData() == itemLocationItemArg) {
                    if(isRemovedItem) {
                        itemData.setItemData(ItemConstants.WEIGHT);
                    }
                    else if(isCoinChest) {
                        itemData.setItemData(ItemConstants.COIN);
                    }
                    else {
                        itemData.setItemData(newContentsData.getInventoryArg());
                    }
                }
                blockContentIndex = i;
            }
        }

        if(isCoinChest && blockContentIndex != null) {
            for(int i = 1; i < newContentsData.getInventoryArg(); i++) {
                conversationBlock.getBlockContents().add(blockContentIndex, new BlockItemData(ItemConstants.COIN));
            }
        }
    }

    @Override
    void updateNeburShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 2 (Surface)"));
    }

    @Override
    void updateNeburAltShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 2 Alt (Surface)"));
    }

    @Override
    void updateSidroShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 3 (Surface)"));
    }

    @Override
    void updateModroShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 1 (Surface)"));
    }

    @Override
    void updatePenadventOfGhostShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 4 (Guidance)"));
    }

    @Override
    void updateGreedyCharlieShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 6 (Mausoleum)"));
    }

    @Override
    void updateShalomIIIShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 8 (Sun)"));
    }

    @Override
    void updateUsasVIShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 9 (Sun)"));
    }

    @Override
    void updateKingvalleyIShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 10 (Sun)"));
    }

    @Override
    void updateMrFishmanShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 12 (Spring)"));
    }

    @Override
    void updateMrFishmanAltShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 12 Alt (Spring)"));
    }

    @Override
    void updateOperatorCombakerShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 16 (Extinction)"));
    }

    @Override
    void updateYiegahKungfuShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 18 (Lil Bro)"));
    }

    @Override
    void updateArrogantMetagearShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 21 (Unsolvable)"));
    }

    @Override
    void updateArrogantSturdySnakeShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 20 (Twin Labs)"));
    }

    @Override
    void updateYiearKungfuShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 19 (Big Bro)"));
    }

    @Override
    void updateAffectedKnimareShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 22 (Endless)"));
    }

    @Override
    void updateMoverAthlelandShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 5 (Illusion)"));
    }

    @Override
    void updateGiantMopiranShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 7 (Graveyard)"));
    }

    @Override
    void updateKingvalleyIIShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 11 (Moonlight)"));
    }

    @Override
    void updateEnergeticBelmontShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 13 (Goddess)"));
    }

    @Override
    void updateMechanicalEfspiShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 15 (Ruin)"));
    }

    @Override
    void updateMudManQubertShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 17 (Birth)"));
    }

    @Override
    void updateHotbloodedNemesistwoShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 14 (Inferno)"));
    }

    @Override
    void updateTailorDracuetShopBlock(ShopBlock shopBlock) {
        if(Settings.isRandomizeDracuetShop()) {
            updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 23 (HT)"));
        }
    }

    @Override
    void updateCustomShopBlock(ShopBlock shopBlock) {
        updateShopBlock(shopBlock, shopRandomizer.getShopInventory("Shop 0 (Default)"));
    }

    @Override
    void updateMulbrukStoneConversationBlock(Block conversationBlock) {
        if(Settings.isRandomizeNpcs()) {
            String npcKey = npcRandomizer.getNpc("NPCL: Mulbruk").replaceAll("NPC: ", "").replaceAll("[ )('-.]", "");
            if(npcKey.startsWith("MrFishman")) {
                npcKey = "MrFishman"; // Avoid awkward naming in conversation.
            }
            String translatedNpcName = Translations.getText("npc." + npcKey);
            replaceText(conversationBlock.getBlockContents(), Translations.getText("npc.Mulbruk"), translatedNpcName);
        }
    }

    @Override
    void updateHinerReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Hiner");
    }

    @Override
    void updateMogerReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Moger");
    }

    @Override
    void updateFormerMekuriMasterReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Former Mekuri Master");
    }

    @Override
    void updatePriestZarnacReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Zarnac");
    }

    @Override
    void updatePriestXanadoReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Xanado");
    }

    @Override
    void updatePhilosopherGiltoriyoReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Philosopher Giltoriyo");
    }

    @Override
    void updatePriestHidlydaReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Hidlyda");
    }

    @Override
    void updatePriestRomancisReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Romancis");
    }

    @Override
    void updatePriestAramoReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Aramo");
    }

    @Override
    void updatePriestTritonReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Triton");
    }

    @Override
    void updatePriestJaguarfivReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Jaguarfiv");
    }

    @Override
    void updateFairyQueenWaitingForPendantReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "The Fairy Queen");
    }

    @Override
    void updateFairyQueenUnlockFairiesReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "The Fairy Queen");
    }

    @Override
    void updateFairyQueenWhenTheTimeComesReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "The Fairy Queen");
    }

    @Override
    void updateFairyQueenTrueShrine1ReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "The Fairy Queen");
    }

    @Override
    void updateFairyQueenTrueShrine2ReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "The Fairy Queen");
    }

    @Override
    void updateMrSlushfundPepperReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Mr. Slushfund");
    }

    @Override
    void updateMrSlushfundWaitingForTreasuresReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Mr. Slushfund");
    }

    @Override
    void updateMrSlushfundAnchorReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Mr. Slushfund");
    }

    @Override
    void updateMrSlushfundNeverComeBackReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Mr. Slushfund");
    }

    @Override
    void updatePriestAlestReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Alest");
    }

    @Override
    void updateGiantThexdeReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Giant Thexde");
    }

    @Override
    void updatePhilosopherAlsedanaReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Philosopher Alsedana");
    }

    @Override
    void updatePhilosopherSamarantaReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Philosopher Samaranta");
    }

    @Override
    void updatePriestLaydocReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Laydoc");
    }

    @Override
    void updatePriestAshgineReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Ashgine");
    }

    @Override
    void updatePhilosopherFobosLadderReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Philosopher Fobos");
    }

    @Override
    void updatePhilosopherFobosMedicineReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Philosopher Fobos");
    }

    @Override
    void update8BitElderReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "8bit Elder");
    }

    @Override
    void updateDuplexReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "duplex");
    }

    @Override
    void updateSamieruReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Samieru");
    }

    @Override
    void updateNaramuraReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Naramura");
    }

    @Override
    void updatePriestMadomonoReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Madomono");
    }

    @Override
    void updatePriestGailiousReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Gailious");
    }

    @Override
    void updateMulbrukStoneConversationReferenceBlock(MasterNpcBlock referenceBlock) {
        if(Settings.isRandomizeNpcs()) {
            if(!"NPC: Mulbruk".equals(npcRandomizer.getNpc("NPCL: Mulbruk"))) {
                referenceBlock.setBackground(new BlockCmdSingle(ValueConstants.DAT_CONVERSATION_BACKGROUND_DRACUET));
                updateBunemonLocation(referenceBlock, npcRandomizer.getNpc("NPCL: Mulbruk").replaceAll("NPC: ", ""));
            }
        }
    }

    @Override
    void updatePriestAlestNoItemReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Priest Alest");
    }

    @Override
    void updateMulbrukEscapeRegularReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Mulbruk");
    }

    @Override
    void updateMulbrukEscapeSwimsuitReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Mulbruk");
    }

    @Override
    void updateMulbrukProvocativeBathingSuitReactionReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Mulbruk");
    }

    @Override
    void updatePhilosopherGiltoriyoStoneReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Philosopher Giltoriyo");
    }

    @Override
    void updatePhilosopherAlsedanaStoneReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Philosopher Alsedana");
    }

    @Override
    void updatePhilosopherSamarantaStoneReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Philosopher Samaranta");
    }

    @Override
    void updatePhilosopherFobosStoneReferenceBlock(MasterNpcBlock referenceBlock) {
        updateBunemonLocation(referenceBlock, "Philosopher Fobos");
    }

    @Override
    void updateXelpudSpriteBlock(Block spriteBlock) {
        updateBunemonLocation(spriteBlock.getBlockContents(), "Elder Xelpud");
    }

    @Override
    void updateMulbrukSpriteBlock(Block spriteBlock) {
        if(Settings.isRandomizeNpcs()) {
            String npcLocationKey = npcRandomizer.getNpcLocation("Mulbruk").replaceAll("NPCL: ", "").replaceAll("[ )('-.]", "");
            String translatedNpcLocationName = Translations.getText("npcl." + npcLocationKey);
            replaceText(spriteBlock.getBlockContents(), Translations.getText("npcl.Mulbruk"), translatedNpcLocationName);
        }
    }

    private void updateShopBlock(ShopBlock shopBlock, ShopInventory shopInventory) {
        ShopInventory transformedShopInventory = getTransformedShopInventory(shopInventory);
        if(transformedShopInventory != null) {
            ShopBlock noOrbShopBlock = new ShopBlock(shopBlock);
            updateShopBlock(noOrbShopBlock, transformedShopInventory);

            datFileData.addCustomBlock(getCustomBlockEnumForTransformedShop(shopInventory.getNpcName()), noOrbShopBlock);
        }

        shopBlock.getInventoryItemArgsList().getData().clear();
        shopBlock.getInventoryItemArgsList().getData().add(shopInventory.getItem1().getInventoryArg());
        shopBlock.getInventoryItemArgsList().getData().add(shopInventory.getItem2().getInventoryArg());
        shopBlock.getInventoryItemArgsList().getData().add(shopInventory.getItem3().getInventoryArg());

        updatePriceAndCount(shopBlock, shopInventory);
        updateShopFlags(shopBlock, shopInventory);

        updateAskItemName(shopBlock.getString(3), shopInventory.getItem1());
        updateAskItemName(shopBlock.getString(4), shopInventory.getItem2());
        updateAskItemName(shopBlock.getString(5), shopInventory.getItem3());

        List<Short> bunemonData = shopBlock.getBunemonText().getData();
        bunemonData.clear();
        updateBunemonText(bunemonData, shopInventory.getItem1(), shopBlock.getItem1Price());
        bunemonData.addAll(FileUtils.stringToData(" , "));
        updateBunemonText(bunemonData, shopInventory.getItem2(), shopBlock.getItem2Price());
        bunemonData.addAll(FileUtils.stringToData(" , "));
        updateBunemonText(bunemonData, shopInventory.getItem3(), shopBlock.getItem3Price());

        shopBlock.setBunemonLocation(new BlockStringData(FileUtils.stringToData(
                Translations.getLocationAndNpc(shopInventory.getNpcLocation(), shopInventory.getNpcName()))));
    }

    private CustomBlockEnum getCustomBlockEnumForTransformedShop(String npcName) {
        if(npcName == null) {
            return CustomBlockEnum.TransformedShopBlock_Default;
        }
        return CustomBlockEnum.valueOf("TransformedShopBlock_" + npcName.replaceAll("[ )(-.]", ""));
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

    private void updateBunemonLocation(List<BlockContents> blockContents, String npcName) {
        if(Settings.isRandomizeNpcs()) {
            String npcLocationKey = npcRandomizer.getNpcLocation(npcName).replaceAll("NPCL: ", "").replaceAll("[ )('-.]", "");
            String translatedNpcLocationName = Translations.getText("npcl." + npcLocationKey);
            replaceText(blockContents, Translations.getText("npcl." + npcName.replaceAll("[ )('-.]", "")), translatedNpcLocationName);
        }
    }

    private void updateBunemonLocation(MasterNpcBlock referenceBlock, String npcName) {
        if(Settings.isRandomizeNpcs()) {
            referenceBlock.setNpcName(new BlockStringData(FileUtils.stringToData(
                    Translations.getLocationAndNpc(npcRandomizer.getNpcLocation(npcName).replaceAll("NPCL: ", ""), npcName))));
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

    private ShopInventory getTransformedShopInventory(ShopInventory shopInventory) {
        // NOTE: only tolerates one sacred orb per shop
        if(shopInventory.getItem1().getInventoryArg() == ItemConstants.SACRED_ORB) {
            ShopInventory transformedShopInventory = new ShopInventory(shopInventory);
            transformedShopInventory.setItem1(new ShopInventoryData("Weights", shopRandomizer.getItemPriceCount("Weights", shopInventory.getShopName(), 1, true)));
            return transformedShopInventory;
        }
        else if(shopInventory.getItem2().getInventoryArg() == ItemConstants.SACRED_ORB) {
            ShopInventory transformedShopInventory = new ShopInventory(shopInventory);
            transformedShopInventory.setItem2(new ShopInventoryData("Weights", shopRandomizer.getItemPriceCount("Weights", shopInventory.getShopName(), 2, true)));
            return transformedShopInventory;
        }
        else if(shopInventory.getItem3().getInventoryArg() == ItemConstants.SACRED_ORB) {
            ShopInventory transformedShopInventory = new ShopInventory(shopInventory);
            transformedShopInventory.setItem3(new ShopInventoryData("Weights", shopRandomizer.getItemPriceCount("Weights", shopInventory.getShopName(), 3, true)));
            return transformedShopInventory;
        }
        return null;
    }

    private void addShrinePrefixIfNeeded(List<Short> bunemonData, ShopInventoryData shopInventoryData) {
        if(shopInventoryData.getWorldFlag() == FlagConstants.WF_MAP_SHRINE) {
            bunemonData.addAll(FileUtils.stringToData(Translations.getText("shop.shrinePrefix")));
        }
    }

//    private static void addShrinePrefixIfNeeded(List<Short> bunemonData, String shopItem) {
//        if("Map (Shrine of the Mother)".equals(shopItem)) {
//            bunemonData.addAll(FileUtils.stringToData(Translations.getText("shop.shrinePrefix")));
//        }
//    }
}
