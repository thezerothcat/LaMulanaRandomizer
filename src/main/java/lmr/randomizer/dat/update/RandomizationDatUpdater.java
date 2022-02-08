package lmr.randomizer.dat.update;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.dat.blocks.Block;
import lmr.randomizer.dat.blocks.MasterNpcBlock;
import lmr.randomizer.dat.blocks.ShopBlock;
import lmr.randomizer.dat.blocks.contents.*;
import lmr.randomizer.randomization.ItemRandomizer;
import lmr.randomizer.randomization.NpcRandomizer;
import lmr.randomizer.randomization.ShopRandomizer;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.randomization.data.GameObjectId;
import lmr.randomizer.randomization.data.ShopInventory;
import lmr.randomizer.randomization.data.ShopInventoryData;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.FlagManager;
import lmr.randomizer.util.ItemConstants;
import lmr.randomizer.util.ValueConstants;

import java.util.List;

public class RandomizationDatUpdater extends DatUpdater {
    private ItemRandomizer itemRandomizer;
    private ShopRandomizer shopRandomizer;
    private NpcRandomizer npcRandomizer;
    private FlagManager flagManager;

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
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 2 (Surface)"));
    }

    @Override
    void updateNeburAltShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 2 Alt (Surface)"));
    }

    @Override
    void updateSidroShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 3 (Surface)"));
    }

    @Override
    void updateModroShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 1 (Surface)"));
    }

    @Override
    void updatePenadventOfGhostShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 4 (Guidance)"));
    }

    @Override
    void updateGreedyCharlieShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 6 (Mausoleum)"));
    }

    @Override
    void updateShalomIIIShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 8 (Sun)"));
    }

    @Override
    void updateUsasVIShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 9 (Sun)"));
    }

    @Override
    void updateKingvalleyIShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 10 (Sun)"));
    }

    @Override
    void updateMrFishmanShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 12 (Spring)"));
    }

    @Override
    void updateMrFishmanAltShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 12 Alt (Spring)"));
    }

    @Override
    void updateOperatorCombakerShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 16 (Extinction)"));
    }

    @Override
    void updateYiegahKungfuShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 18 (Lil Bro)"));
    }

    @Override
    void updateArrogantMetagearShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 21 (Unsolvable)"));
    }

    @Override
    void updateArrogantSturdySnakeShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 20 (Twin Labs)"));
    }

    @Override
    void updateYiearKungfuShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 19 (Big Bro)"));
    }

    @Override
    void updateAffectedKnimareShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 22 (Endless)"));
    }

    @Override
    void updateMoverAthlelandShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 5 (Illusion)"));
    }

    @Override
    void updateGiantMopiranShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 7 (Graveyard)"));
    }

    @Override
    void updateKingvalleyIIShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 11 (Moonlight)"));
    }

    @Override
    void updateEnergeticBelmontShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 13 (Goddess)"));
    }

    @Override
    void updateMechanicalEfspiShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 15 (Ruin)"));
    }

    @Override
    void updateMudManQubertShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 17 (Birth)"));
    }

    @Override
    void updateHotbloodedNemesistwoShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 14 (Inferno)"));
    }

    @Override
    void updateTailorDracuetShopBlock(ShopBlock shopBlock) {
        if(Settings.isRandomizeDracuetShop()) {
            updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 23 (HT)"));
        }
    }

    @Override
    void updateCustomShopBlock(ShopBlock shopBlock) {
        updateShopBlockCheckTransformation(shopBlock, shopRandomizer.getShopInventory("Shop 0 (Default)"));
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

    private void updateShopBlockCheckTransformation(ShopBlock shopBlock, ShopInventory shopInventory) {
        ShopInventory transformedShopInventory = getTransformedShopInventory(shopInventory);
        if(transformedShopInventory != null) {
            ShopBlock noOrbShopBlock = new ShopBlock(shopBlock);
            updateShopBlock(noOrbShopBlock, transformedShopInventory);

            datFileData.addCustomBlock(getCustomBlockEnumForTransformedShop(shopInventory.getNpcName()), noOrbShopBlock);
        }
        updateShopBlock(shopBlock, shopInventory);
    }

    private CustomBlockEnum getCustomBlockEnumForTransformedShop(String npcName) {
        if(npcName == null) {
            return CustomBlockEnum.TransformedShopBlock_Default;
        }
        return CustomBlockEnum.valueOf("TransformedShopBlock_" + npcName.replaceAll("[ )(-.]", ""));
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
            referenceBlock.setNpcName(new BlockStringData(
                    Translations.getLocationAndNpc(npcRandomizer.getNpcLocation(npcName).replaceAll("NPCL: ", ""), npcName)));
        }
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

//    private static void addShrinePrefixIfNeeded(List<Short> bunemonData, String shopItem) {
//        if("Map (Shrine of the Mother)".equals(shopItem)) {
//            bunemonData.addAll(FileUtils.stringToData(Translations.getText("shop.shrinePrefix")));
//        }
//    }
}
