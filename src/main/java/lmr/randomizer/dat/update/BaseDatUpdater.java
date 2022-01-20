package lmr.randomizer.dat.update;

import lmr.randomizer.*;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.blocks.contents.BlockContents;
import lmr.randomizer.dat.blocks.contents.BlockFlagData;
import lmr.randomizer.dat.blocks.contents.BlockListData;
import lmr.randomizer.dat.blocks.contents.BlockStringData;
import lmr.randomizer.dat.blocks.contents.entries.DefaultGrailPointEntry;
import lmr.randomizer.dat.blocks.contents.entries.GrailPointEntry;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.update.AddBlock;
import lmr.randomizer.update.AddObject;
import lmr.randomizer.util.BlockConstants;
import lmr.randomizer.util.BlockDataConstants;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.LocationCoordinateMapper;

import java.util.ArrayList;
import java.util.List;

public class BaseDatUpdater extends DatUpdater {
    public BaseDatUpdater(DatFileData datFileData) {
        super(datFileData);
    }

    @Override
    public void updateItemDescriptions(ItemDescriptionBlock itemDescriptionBlock) {
        String versionString = " Randomizer version " + FileUtils.VERSION;
        String settingsString = "Settings " + Settings.generateShortString();
        String msxDescription = Translations.getText("items.MobileSuperX") + "：" + versionString + "\n" + settingsString;
        String msx2Description = Translations.getText("items.MobileSuperX2") + "：" + versionString + "\n" + settingsString;

        itemDescriptionBlock.setDescription(ItemDescriptionBlock.MSX1, buildTextEntry(msxDescription));
        itemDescriptionBlock.setDescription(ItemDescriptionBlock.MSX2, buildTextEntry(msx2Description));
    }

    @Override
    public void updateSoftwareData(SoftwareBlock softwareBlock) {
        softwareBlock.setSoftwareCost(SoftwareBlock.SoftwareMirai, 200);
        softwareBlock.setSoftwareCostText(SoftwareBlock.SoftwareMirai, buildTextEntry(Translations.getText("software.cost.mirai")));
    }

    @Override
    public void updateGrailPoints(GrailPointBlock grailPointBlock) {
        if(!LocationCoordinateMapper.isSurfaceStart() || HolidaySettings.isFools2021Mode() || Settings.getCurrentStartingLocation() == 22) {
            boolean front = LocationCoordinateMapper.isFrontsideStart();

            DefaultGrailPointEntry defaultGrailPointEntry = grailPointBlock.getDefaultGrailPointEntry();
            defaultGrailPointEntry.setWarpImage(LocationCoordinateMapper.getImageIndex(LocationCoordinateMapper.getStartingZone(), front));
            defaultGrailPointEntry.setDestination(LocationCoordinateMapper.getStartingZone(),
                    LocationCoordinateMapper.getStartingRoom(),
                    LocationCoordinateMapper.getStartingScreen(),
                    LocationCoordinateMapper.getStartingX() % 640,
                    LocationCoordinateMapper.getStartingY() % 480);

            List<Short> warpsUpdated = new ArrayList<>();
            warpsUpdated.add((short)LocationCoordinateMapper.getStartingZone());
            short nextWarp = LocationCoordinateMapper.getNextWarpZone(LocationCoordinateMapper.getStartingZone(), front);
            int grailPointIndex = 1;
            while(!warpsUpdated.contains(nextWarp)) {
                GrailPointEntry grailPointEntry = grailPointBlock.getGrailPointEntry(grailPointIndex++);
                grailPointEntry.setRequireMirai(false);
                grailPointEntry.setFlag(LocationCoordinateMapper.getGrailFlag(nextWarp, front));
                grailPointEntry.setWarpImage(LocationCoordinateMapper.getImageIndex(nextWarp, front));
                grailPointEntry.setDestination(nextWarp,
                        LocationCoordinateMapper.getStartingRoom(nextWarp, front),
                        LocationCoordinateMapper.getStartingScreen(nextWarp),
                        LocationCoordinateMapper.getStartingX(nextWarp, front) % 640,
                        LocationCoordinateMapper.getStartingY(nextWarp, front) % 480);

                warpsUpdated.add(nextWarp);
                nextWarp = LocationCoordinateMapper.getNextWarpZone(nextWarp, front);
            }
            front = !front;
            nextWarp = LocationCoordinateMapper.getNextWarpZone(-1, front);
            warpsUpdated.clear(); // Necessary since Twin Labyrinths is on both sides.
            while(!warpsUpdated.contains(nextWarp)) {
                GrailPointEntry grailPointEntry = grailPointBlock.getGrailPointEntry(grailPointIndex++);
                grailPointEntry.setRequireMirai(true);
                grailPointEntry.setFlag(LocationCoordinateMapper.getGrailFlag(nextWarp, front));
                grailPointEntry.setWarpImage(LocationCoordinateMapper.getImageIndex(nextWarp, front));
                grailPointEntry.setDestination(nextWarp,
                        LocationCoordinateMapper.getStartingRoom(nextWarp, front),
                        LocationCoordinateMapper.getStartingScreen(nextWarp),
                        LocationCoordinateMapper.getStartingX(nextWarp, front) % 640,
                        LocationCoordinateMapper.getStartingY(nextWarp, front) % 480);

                warpsUpdated.add(nextWarp);
                nextWarp = LocationCoordinateMapper.getNextWarpZone(nextWarp, front);
            }
        }
    }

    @Override
    void updateMulbrukWakingUpConversationBlock(Block conversationBlock) {
        // Move flag update from ConversationDoor into conversation
        Integer blockContentIndex = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                if(blockFlagData.getWorldFlag() == FlagConstants.MULBRUK_CONVERSATIONS_EARLY) {
                    blockContentIndex = i;
                }
            }
        }
        conversationBlock.getBlockContents().add(blockContentIndex,
                new BlockFlagData(FlagConstants.MULBRUK_CONVERSATION_AWAKE, 1));
    }

    @Override
    public void updateMiniDollConversationBlock(Block conversationBlock) {
        // Remove Mini Doll's becoming small flag from conversation.
        Integer becomingSmallFlagIndex = null;
        BlockContents blockContents;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData && ((BlockFlagData)blockContents).getWorldFlag() == FlagConstants.PROVE_THOU_ART_SMALL) {
                becomingSmallFlagIndex = i;
            }
        }
        if(becomingSmallFlagIndex != null) {
            conversationBlock.getBlockContents().remove((int)becomingSmallFlagIndex);
        }
    }

    @Override
    void updatePepperConversationBlock(Block conversationBlock) {
        // Add custom conversation flag to separate the Pepper and Anchor checks
        Integer blockContentIndex = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                if(blockFlagData.getWorldFlag() == FlagConstants.MR_SLUSHFUND_CONVERSATION) {
                    blockContentIndex = i;
                }
            }
        }
        conversationBlock.getBlockContents().add(blockContentIndex,
                new BlockFlagData(FlagConstants.MR_SLUSHFUND_CONVERSATION_PEPPER, 1));
    }

    @Override
    void updateXmailerConversationBlock(Block conversationBlock) {
        // Note: This may still get overwritten by holiday mode changes (Halloween, Fools2020)
        List<BlockContents> xelpudBlockContents = conversationBlock.getBlockContents();

        // Set value of world flag to 2 instead of 1
        for(int i = 0; i < xelpudBlockContents.size(); i++) {
            BlockContents blockContents = xelpudBlockContents.get(i);
            if(blockContents instanceof BlockFlagData) {
                BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                if(blockFlagData.getWorldFlag() == FlagConstants.WF_SOFTWARE_XMAILER) {
                    blockFlagData.setFlagValue((short)2);
                }
                else if(blockFlagData.getWorldFlag() == FlagConstants.XELPUD_CONVERSATION_GENERAL) {
                    blockFlagData.setWorldFlag((short)FlagConstants.XELPUD_CONVERSATION_INTRO);
                    blockFlagData.setFlagValue((short)1);
                }
            }
        }
    }

    @Override
    void updateXelpudTalismanConversationBlock(Block conversationBlock) {
        // Talisman conversation (allows Diary chest access)
        Integer blockContentIndex = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                if(blockFlagData.getWorldFlag() == FlagConstants.CONVERSATION_CANT_LEAVE) {
                    blockContentIndex = i;
                }
            }
        }
        conversationBlock.getBlockContents().add(blockContentIndex,
                new BlockFlagData((short)FlagConstants.XELPUD_CONVERSATION_TALISMAN_FOUND, (short)2));
        conversationBlock.getBlockContents().add(blockContentIndex, new BlockFlagData((short)807, (short)1));
    }

    @Override
    void updateXelpudPillarConversationBlock(Block conversationBlock) {
        // Diary chest conversation (allows Diary chest access)
        Integer flagIndexToRemove = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                if(blockFlagData.getWorldFlag() == FlagConstants.SHRINE_DIARY_CHEST) {
                    flagIndexToRemove = i;
                }
            }
        }
        // Get rid of Diary puzzle flag.
        if(flagIndexToRemove != null) {
            conversationBlock.getBlockContents().remove((int)flagIndexToRemove);
        }

        Integer blockContentIndex = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                BlockFlagData blockFlagData = (BlockFlagData) blockContents;
                if(blockFlagData.getWorldFlag() == FlagConstants.CONVERSATION_CANT_LEAVE) {
                    blockContentIndex = i;
                }
            }
        }
        conversationBlock.getBlockContents().add(blockContentIndex,
                new BlockFlagData((short)FlagConstants.XELPUD_CONVERSATION_TALISMAN_FOUND, (short)3));
    }

    @Override
    void updateMulanaTalismanConversationBlock(Block conversationBlock) {
        BlockFlagData blockFlagData;

        // Handle Mulana Talisman conversation flag changes.
        Integer flagIndexOfMulanaTalisman = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                blockFlagData = (BlockFlagData) blockContents;
                if(blockFlagData.getWorldFlag() == FlagConstants.WF_MULANA_TALISMAN) {
                    blockFlagData.setWorldFlag((short)FlagConstants.XELPUD_CONVERSATION_DIARY_FOUND);
                    blockFlagData.setFlagValue((short)2);
                    flagIndexOfMulanaTalisman = i;
                }
            }
        }
        blockFlagData = new BlockFlagData((short)FlagConstants.WF_MULANA_TALISMAN, (short)2);
        conversationBlock.getBlockContents().add(flagIndexOfMulanaTalisman, blockFlagData);

        // Get rid of Diary puzzle flag.
        Integer flagIndexToRemove = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                blockFlagData = (BlockFlagData)blockContents;
                if(blockFlagData.getWorldFlag() == FlagConstants.SHRINE_PUZZLE_DIARY_CHEST) {
                    flagIndexToRemove = i;
                }
            }
        }
        if(flagIndexToRemove != null) {
            conversationBlock.getBlockContents().remove((int)flagIndexToRemove);
        }
    }

    @Override
    void updateBookOfTheDeadConversationBlock(Block conversationBlock) {
        // Handle Book of the Dead conversation flag changes.
        BlockFlagData blockFlagData;
        Integer flagIndexOfConversationFlagUpdate = null;
        for(int i = 0; i < conversationBlock.getBlockContents().size(); i++) {
            BlockContents blockContents = conversationBlock.getBlockContents().get(i);
            if(blockContents instanceof BlockFlagData) {
                blockFlagData = (BlockFlagData)blockContents;
                if(blockFlagData.getWorldFlag() == FlagConstants.MULBRUK_CONVERSATION_BOOK) {
                    flagIndexOfConversationFlagUpdate = i;
                }
            }
        }
        blockFlagData = new BlockFlagData(FlagConstants.WF_BOOK_OF_THE_DEAD, 2);
        conversationBlock.getBlockContents().add(flagIndexOfConversationFlagUpdate, blockFlagData);
    }

    @Override
    void updateNeburAltShopBlock(ShopBlock shopBlock) {
        BlockStringData oldBlockStringData = shopBlock.getString(6);
        BlockStringData newBlockStringData = new BlockStringData();
        newBlockStringData.getData().addAll(oldBlockStringData.getData().subList(0, oldBlockStringData.getData().size() - 3));
        shopBlock.setString(newBlockStringData, 6);
    }

    @Override
    public void updateXelpudFlagCheckBlock(CheckBlock flagCheckBlock) {
        flagCheckBlock.removeCheckEntryByBlockIndex(BlockConstants.Removed_WindHowlingSwimsuit);
        flagCheckBlock.removeCheckEntryByBlockIndex(BlockConstants.ItemConversationMulanaTalisman);
        flagCheckBlock.removeCheckEntryByBlockIndex(BlockConstants.XelpudPillarConversation); // Remove Xelpud pillar conversation so we can re-build it at the front.
        flagCheckBlock.removeCheckEntryByBlockIndex(BlockConstants.ItemConversationXmailer); // Remove xmailer conversation so we can re-build it at the front.

        BlockListData blockListData = new BlockListData(BlockDataConstants.DataList, (short)4);
        blockListData.getData().add((short)FlagConstants.XELPUD_CONVERSATION_DIARY_FOUND);
        blockListData.getData().add((short)1);
        blockListData.getData().add((short)BlockConstants.ItemConversationMulanaTalisman);
        blockListData.getData().add((short)0); // Disabling repeat for Mulana Talisman in case it's an ankh jewel or something.
        flagCheckBlock.getFlagCheckReferences().add(0, blockListData);

        // Xelpud pillar conversation possible as soon as Talisman conversation
        blockListData = new BlockListData(BlockDataConstants.DataList, (short)4);
        blockListData.getData().add((short)FlagConstants.XELPUD_CONVERSATION_TALISMAN_FOUND);
        blockListData.getData().add((short)2);
        blockListData.getData().add((short)BlockConstants.XelpudPillarConversation);
        blockListData.getData().add((short)0);
        flagCheckBlock.getFlagCheckReferences().add(0, blockListData);

        // Changing Talisman conversation to use a custom flag instead of a held item check
        blockListData = new BlockListData(BlockDataConstants.DataList, (short)4);
        blockListData.getData().add((short)FlagConstants.XELPUD_CONVERSATION_TALISMAN_FOUND);
        blockListData.getData().add((short)1);
        blockListData.getData().add((short)BlockConstants.XelpudTalismanConversation);
        blockListData.getData().add((short)0);
        flagCheckBlock.getFlagCheckReferences().add(0, blockListData);

        // Changing xmailer conversation to use a custom flag
        blockListData = new BlockListData(BlockDataConstants.DataList, (short)4);
        blockListData.getData().add((short)FlagConstants.XELPUD_CONVERSATION_INTRO);
        blockListData.getData().add((short)0);
        blockListData.getData().add((short)BlockConstants.ItemConversationXmailer);
        blockListData.getData().add((short)0);
        flagCheckBlock.getFlagCheckReferences().add(0, blockListData);

        if(Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart() || Settings.isRandomizeStartingLocation() || HolidaySettings.isFools2019Mode() || HolidaySettings.isFools2020Mode()) {
            // Changing xmailer conversation to use a custom flag instead of a held item check
            blockListData = new BlockListData((short)4);
            blockListData.getData().add((short)FlagConstants.RANDOMIZER_SAVE_LOADED);
            blockListData.getData().add((short)0);
            blockListData.getData().add(getCustomBlockIndex(CustomBlockEnum.CustomXelpudIntro));
            blockListData.getData().add((short)0);
            flagCheckBlock.getFlagCheckReferences().add(0, blockListData);
        }
    }

    @Override
    public void updateXelpudScoreCheckBlock(CheckBlock scoreCheckBlock) {
        Integer cmdToRemoveIndex = null;
        for(int i = 0; i < scoreCheckBlock.getFlagCheckReferences().size(); i++) {
            BlockListData blockListData = scoreCheckBlock.getFlagCheckReferences().get(i);
            if(blockListData.getData().get(2) == BlockConstants.Removed_XelpudScoreWindHowling) {
                cmdToRemoveIndex = i;
            }
        }
        if(cmdToRemoveIndex != null) {
            scoreCheckBlock.getFlagCheckReferences().remove((int)cmdToRemoveIndex);
        }
    }

    @Override
    public void updateMulbrukFlagCheckBlock(CheckBlock flagCheckBlock) {
        for(BlockListData blockListData : flagCheckBlock.getFlagCheckReferences()) {
            blockListData.getData().remove((int)blockListData.getData().size() - 1); // Remove existing value for conversation repeats
            blockListData.getData().add((short)0); // Disable conversation repeats to reduce annoyance factor
        }

        List<BlockListData> flagChecks = new ArrayList<>(flagCheckBlock.getFlagCheckReferences().size());
        flagChecks.add(flagCheckBlock.getFlagCheckReferences().get(3)); // Book of the Dead conversation
        flagChecks.addAll(flagCheckBlock.getFlagCheckReferences().subList(13, flagCheckBlock.getFlagCheckReferences().size())); // HT conversations

        // Mulbruk waking up conversation
        BlockListData blockListData = new BlockListData((short)4);
        blockListData.getData().add((short)FlagConstants.MULBRUK_CONVERSATION_AWAKE);
        blockListData.getData().add((short)0);
        blockListData.getData().add((short)BlockConstants.MulbrukWakingUpConversationBlock);
        blockListData.getData().add((short)0);
        flagChecks.add(blockListData);

        flagChecks.addAll(flagCheckBlock.getFlagCheckReferences().subList(1, 3)); // MULBRUK_CONVERSATIONS_EARLY
        flagChecks.addAll(flagCheckBlock.getFlagCheckReferences().subList(4, 13)); // Misc Mulbruk conversations
        flagCheckBlock.getFlagCheckReferences().clear();
        flagCheckBlock.getFlagCheckReferences().addAll(flagChecks);
    }

    @Override
    void updateScannableBlock(ScannableBlock scannableBlock) {
        String scannableId = getScannableId(scannableBlock.getBlockNumber());
        String customText = getCustomScanText(scannableId);
        if(customText != null) {
            scannableBlock.setScanText(buildTextEntryWithCommands(customText));
        }
    }

    protected String getCustomScanText(String scannableId) {
        if(scannableId == null) {
            return null;
        }
        return Translations.getText(scannableId + ".Text");
    }

    private String getScannableId(int blockNumber) {
        if(blockNumber == BlockConstants.Tablet_Guidance_DeathAwaitsThePowerless) {
            return "Tablet_Guidance_DeathAwaitsThePowerless";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_Entrance) {
            return "Tablet_Guidance_Entrance";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Guidance_Guild) {
            return "DamagedTablet_Guidance_Guild";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_HolyGround) {
            return "Tablet_Guidance_HolyGround";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_ProveThineCourage) {
            return "Tablet_Guidance_ProveThineCourage";
        }
        if(blockNumber == BlockConstants.Tablet_Spring_ChildWasBorn) {
            return "Tablet_Spring_ChildWasBorn";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_Offer3Lights) {
            return "Tablet_Guidance_Offer3Lights";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_8Ankhs) {
            return "Tablet_Guidance_8Ankhs";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_GiantsHistory) {
            return "Tablet_Guidance_GiantsHistory";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_ChiYou) {
            return "Tablet_Guidance_ChiYou";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_StatueOfTheGiant) {
            return "Tablet_Sun_StatueOfTheGiant";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_CrossTheSacredLake) {
            return "Tablet_Guidance_CrossTheSacredLake";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_HolyGrailMemories) {
            return "Tablet_Guidance_HolyGrailMemories";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_Watchtower) {
            return "Tablet_Guidance_Watchtower";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_ItShallConnect) {
            return "Tablet_Guidance_ItShallConnect";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_CelestialBodies) {
            return "Tablet_Mausoleum_CelestialBodies";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_Abuto) {
            return "Tablet_Mausoleum_Abuto";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_ThouArtTiny) {
            return "Tablet_Mausoleum_ThouArtTiny";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_Ribu) {
            return "Tablet_Mausoleum_Ribu";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Mausoleum_GhostLord) {
            return "DamagedTablet_Mausoleum_GhostLord";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_Migela) {
            return "Tablet_Mausoleum_Migela";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_Ledo) {
            return "Tablet_Mausoleum_Ledo";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_Ji) {
            return "Tablet_Mausoleum_Ji";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_Bado) {
            return "Tablet_Mausoleum_Bado";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_MigelaLeftHanded) {
            return "Tablet_Mausoleum_MigelaLeftHanded";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_AbutoJiRibuSakit) {
            return "Tablet_Mausoleum_AbutoJiRibuSakit";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_Zebu) {
            return "Tablet_Mausoleum_Zebu";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Mausoleum_RedLight) {
            return "DamagedTablet_Mausoleum_RedLight";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_ZebuUnableToMove) {
            return "Tablet_Mausoleum_ZebuUnableToMove";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_Numbers) {
            return "Tablet_Mausoleum_Numbers";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_BadoMigelaLedoFuto) {
            return "Tablet_Mausoleum_BadoMigelaLedoFuto";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_NineBrothers) {
            return "Tablet_Mausoleum_NineBrothers";
        }
        if(blockNumber == BlockConstants.Tablet_Mausoleum_Sakit) {
            return "Tablet_Mausoleum_Sakit";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_JumpIntoTheSun) {
            return "Tablet_Sun_JumpIntoTheSun";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_ChallengeTheInfernoCavern) {
            return "Tablet_Sun_ChallengeTheInfernoCavern";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_CastALight) {
            return "Tablet_Sun_CastALight";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_Scalesphere) {
            return "Tablet_Sun_Scalesphere";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_PathThatConnectsTwoPyramids) {
            return "Tablet_Sun_PathThatConnectsTwoPyramids";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_MoonGazingPit) {
            return "Tablet_Sun_MoonGazingPit";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_FirstMantra) {
            return "Tablet_Sun_FirstMantra";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_SecondMantra) {
            return "Tablet_Sun_SecondMantra";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_ThirdMantra) {
            return "Tablet_Sun_ThirdMantra";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_MantraOrder) {
            return "Tablet_Sun_MantraOrder";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_FourthMantra) {
            return "Tablet_Sun_FourthMantra";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_FifthMantra) {
            return "Tablet_Sun_FifthMantra";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_SeventhMantra) {
            return "Tablet_Sun_SeventhMantra";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_LastMantra) {
            return "Tablet_Sun_LastMantra";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_ManWomanWeapon) {
            return "Tablet_Sun_ManWomanWeapon";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_WomanWithChild) {
            return "Tablet_Sun_WomanWithChild";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Sun_FillThisPlaceWithWater) {
            return "DamagedTablet_Sun_FillThisPlaceWithWater";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_NoLightUpAhead) {
            return "Tablet_Sun_NoLightUpAhead";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_ThoseThatFlyShallBeCastDown) {
            return "Tablet_Sun_ThoseThatFlyShallBeCastDown";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_MeditateUnderWedjet) {
            return "Tablet_Sun_MeditateUnderWedjet";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_PowerOfTheTwins) {
            return "Tablet_Sun_PowerOfTheTwins";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_SolvedAllPuzzles) {
            return "Tablet_Sun_SolvedAllPuzzles";
        }
        if(blockNumber == BlockConstants.Tablet_Spring_AboveTheSun) {
            return "Tablet_Spring_AboveTheSun";
        }
        if(blockNumber == BlockConstants.Tablet_Spring_Fairy) {
            return "Tablet_Spring_Fairy";
        }
        if(blockNumber == BlockConstants.Tablet_Spring_OpenTheFloodgate) {
            return "Tablet_Spring_OpenTheFloodgate";
        }
        if(blockNumber == BlockConstants.Tablet_Spring_OannesFormOfAFish) {
            return "Tablet_Spring_OannesFormOfAFish";
        }
        if(blockNumber == BlockConstants.Tablet_Spring_SailAway) {
            return "Tablet_Spring_SailAway";
        }
        if(blockNumber == BlockConstants.Tablet_Spring_CollectTheFourSigils) {
            return "Tablet_Spring_CollectTheFourSigils";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Spring_WaterMovesTheTower) {
            return "DamagedTablet_Spring_WaterMovesTheTower";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Spring_MothersWrath) {
            return "DamagedTablet_Spring_MothersWrath";
        }
        if(blockNumber == BlockConstants.Tablet_Spring_ThoseSeekingEden) {
            return "Tablet_Spring_ThoseSeekingEden";
        }
        if(blockNumber == BlockConstants.Tablet_Inferno_SnakesAreEnraged) {
            return "Tablet_Inferno_SnakesAreEnraged";
        }
        if(blockNumber == BlockConstants.Tablet_Inferno_Gatekeepers) {
            return "Tablet_Inferno_Gatekeepers";
        }
        if(blockNumber == BlockConstants.Tablet_Inferno_StickToTheWalls) {
            return "Tablet_Inferno_StickToTheWalls";
        }
        if(blockNumber == BlockConstants.Tablet_Inferno_ErrJustOnce) {
            return "Tablet_Inferno_ErrJustOnce";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Inferno_PhilosophersStone) {
            return "DamagedTablet_Inferno_PhilosophersStone";
        }
        if(blockNumber == BlockConstants.Tablet_Inferno_TreadADifferentPath) {
            return "Tablet_Inferno_TreadADifferentPath";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Inferno_CloakMadeFromIce) {
            return "DamagedTablet_Inferno_CloakMadeFromIce";
        }
        if(blockNumber == BlockConstants.Tablet_Inferno_TuneOfThineOcarina) {
            return "Tablet_Inferno_TuneOfThineOcarina";
        }
        if(blockNumber == BlockConstants.Tablet_Inferno_BottomOfThisLand) {
            return "Tablet_Inferno_BottomOfThisLand";
        }
        if(blockNumber == BlockConstants.Tablet_Inferno_ThoseSeekingEden) {
            return "Tablet_Inferno_ThoseSeekingEden";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_SeekTheRighteousSpirit) {
            return "Tablet_Extinction_SeekTheRighteousSpirit";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_TwoAngelShield) {
            return "Tablet_Extinction_TwoAngelShield";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_ShellInTheLeftHandPoints) {
            return "Tablet_Extinction_ShellInTheLeftHandPoints";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_LetTheLightBurnHere) {
            return "Tablet_Extinction_LetTheLightBurnHere";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_SprigganStatue) {
            return "Tablet_Extinction_SprigganStatue";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_DragonBone) {
            return "Tablet_Extinction_DragonBone";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_CogOfTheSoulGivesLife) {
            return "Tablet_Extinction_CogOfTheSoulGivesLife";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_LargeCrucifixSummonPalenque) {
            return "Tablet_Extinction_LargeCrucifixSummonPalenque";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_LetTheMapGuideItsPlacement) {
            return "Tablet_Extinction_LetTheMapGuideItsPlacement";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_ProceedThroughTheWall) {
            return "Tablet_Extinction_ProceedThroughTheWall";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_EveryHeartHasAColor) {
            return "Tablet_Extinction_EveryHeartHasAColor";
        }
        if(blockNumber == BlockConstants.Tablet_Inferno_LastJewelLiesWithin) {
            return "Tablet_Inferno_LastJewelLiesWithin";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_NuwaSleeps) {
            return "Tablet_Extinction_NuwaSleeps";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_BlackOnyxPurpleAmethyst) {
            return "Tablet_Twin_BlackOnyxPurpleAmethyst";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Twin_FrontAndBack) {
            return "DamagedTablet_Twin_FrontAndBack";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_EdenSearchOutThePlace) {
            return "Tablet_Moonlight_EdenSearchOutThePlace";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_IdignaBuranunHermes) {
            return "Tablet_Twin_IdignaBuranunHermes";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_HeroOfTheThirdChild) {
            return "Tablet_Twin_HeroOfTheThirdChild";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Twin_FootOfTheFootlessNeptune) {
            return "DamagedTablet_Twin_FootOfTheFootlessNeptune";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_ChantASpellToTheSpirit) {
            return "Tablet_Twin_ChantASpellToTheSpirit";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Twin_BeyondTheSkull) {
            return "DamagedTablet_Twin_BeyondTheSkull";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_DanceOfLife) {
            return "Tablet_Twin_DanceOfLife";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_UnsolvablePuzzle) {
            return "Tablet_Twin_UnsolvablePuzzle";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_FiveWitchesPlusOneMore) {
            return "Tablet_Twin_FiveWitchesPlusOneMore";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_Beelzebub) {
            return "Tablet_Twin_Beelzebub";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_RevealTheLight) {
            return "Tablet_Twin_RevealTheLight";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_HatchSkuldVerdandi) {
            return "Tablet_Twin_HatchSkuldVerdandi";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_PriestsBecamePhilosophers) {
            return "Tablet_Twin_PriestsBecamePhilosophers";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_AnkhIsAlwaysInTheFront) {
            return "Tablet_Twin_AnkhIsAlwaysInTheFront";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_InnocentGirl) {
            return "Tablet_Twin_InnocentGirl";
        }
        if(blockNumber == BlockConstants.DamagedTablet_Twin_ZuPeryton) {
            return "DamagedTablet_Twin_ZuPeryton";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_PowerToStopTime) {
            return "Tablet_Twin_PowerToStopTime";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_BrotherShops) {
            return "Tablet_Twin_BrotherShops";
        }
        if(blockNumber == BlockConstants.Tablet_Twin_DestroyedIn2015) {
            return "Tablet_Twin_DestroyedIn2015";
        }
        if(blockNumber == BlockConstants.Tablet_Endless_Dragon) {
            return "Tablet_Endless_Dragon";
        }
        if(blockNumber == BlockConstants.Tablet_Endless_BornLivesReproduceAscension) {
            return "Tablet_Endless_BornLivesReproduceAscension";
        }
        if(blockNumber == BlockConstants.Tablet_Endless_ToilsForNaught) {
            return "Tablet_Endless_ToilsForNaught";
        }
        if(blockNumber == BlockConstants.Tablet_Endless_MightOrWisdom) {
            return "Tablet_Endless_MightOrWisdom";
        }
        if(blockNumber == BlockConstants.Tablet_Endless_EndureTrials) {
            return "Tablet_Endless_EndureTrials";
        }
        if(blockNumber == BlockConstants.Tablet_Extinction_ThoseSeekingEden) {
            return "Tablet_Extinction_ThoseSeekingEden";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_ThoseSeekingEden) {
            return "Tablet_Graveyard_ThoseSeekingEden";
        }
        if(blockNumber == BlockConstants.Tablet_Shrine_EndlessnessAndDimensions) {
            return "Tablet_Shrine_EndlessnessAndDimensions";
        }
        if(blockNumber == BlockConstants.Tablet_Shrine_WallOfYourCalling) {
            return "Tablet_Shrine_WallOfYourCalling";
        }
        if(blockNumber == BlockConstants.Tablet_Shrine_PhilosopherChosenOne) {
            return "Tablet_Shrine_PhilosopherChosenOne";
        }
        if(blockNumber == BlockConstants.Tablet_Shrine_MushussuCombo) {
            return "Tablet_Shrine_MushussuCombo";
        }
        if(blockNumber == BlockConstants.Tablet_Shrine_EightSpirits) {
            return "Tablet_Shrine_EightSpirits";
        }
//        if(blockNumber == BlockConstants.Tablet_Shrine_Rosetta) {
//            return "Tablet_Shrine_Rosetta";
//        }
        if(blockNumber == BlockConstants.Tablet_Shrine_SecretTreasureOfLife) {
            return "Tablet_Shrine_SecretTreasureOfLife";
        }
        if(blockNumber == BlockConstants.Tablet_Shrine_PhilosopherImmortal) {
            return "Tablet_Shrine_PhilosopherImmortal";
        }
        if(blockNumber == BlockConstants.Tablet_Shrine_SeventhChildren) {
            return "Tablet_Shrine_SeventhChildren";
        }
        if(blockNumber == BlockConstants.Tablet_Shrine_SleepWithinTheWoman) {
            return "Tablet_Shrine_SleepWithinTheWoman";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_KeyToEndlessCorridor) {
            return "Tablet_Illusion_KeyToEndlessCorridor";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_DeviceThatCreatesLife) {
            return "Tablet_Illusion_DeviceThatCreatesLife";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_VirtualWiseMan) {
            return "Tablet_Illusion_VirtualWiseMan";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_TiamatGrailPowerless) {
            return "Tablet_Illusion_TiamatGrailPowerless";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_PossessTheWisdomOfAWiseMan) {
            return "Tablet_Illusion_PossessTheWisdomOfAWiseMan";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_FreeThyselfOfDoubtAndIllusion) {
            return "Tablet_Illusion_FreeThyselfOfDoubtAndIllusion";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_GiveUp) {
            return "Tablet_Illusion_GiveUp";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_FoolDeservesNaughtButDeath) {
            return "Tablet_Illusion_FoolDeservesNaughtButDeath";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_TheFairiesUnlockIt) {
            return "Tablet_Illusion_TheFairiesUnlockIt";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_WanderTheLabyrinthForEternity) {
            return "Tablet_Illusion_WanderTheLabyrinthForEternity";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_UnderstandingIsImpressive) {
            return "Tablet_Illusion_UnderstandingIsImpressive";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_FoolPuzzle) {
            return "Tablet_Illusion_FoolPuzzle";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_FoolWillNeverFindHisWay) {
            return "Tablet_Illusion_FoolWillNeverFindHisWay";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_ExtinguishTheWhole) {
            return "Tablet_Illusion_ExtinguishTheWhole";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_SearchOnBendedKnee) {
            return "Tablet_Illusion_SearchOnBendedKnee";
        }
        if(blockNumber == BlockConstants.Tablet_Illusion_EnterEden) {
            return "Tablet_Illusion_EnterEden";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_Toujin) {
            return "Tablet_Graveyard_Toujin";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_MuIsTheNamelessOne) {
            return "Tablet_Graveyard_MuIsTheNamelessOne";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_EyesAreNotHollowedOutHoles) {
            return "Tablet_Graveyard_EyesAreNotHollowedOutHoles";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_MoneyCantBuyTheRealThing) {
            return "Tablet_Graveyard_MoneyCantBuyTheRealThing";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_MeetingOfManAndWoman) {
            return "Tablet_Graveyard_MeetingOfManAndWoman";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_DemonEndlesslyConsumesLife) {
            return "Tablet_Graveyard_DemonEndlesslyConsumesLife";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_MeaningsAreInfusedIntoTheGemstones) {
            return "Tablet_Graveyard_MeaningsAreInfusedIntoTheGemstones";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_NewWeaponTakeDownTheWall) {
            return "Tablet_Graveyard_NewWeaponTakeDownTheWall";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_SpaulderGoddess) {
            return "Tablet_Graveyard_SpaulderGoddess";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_CursedTreasureRedStone) {
            return "Tablet_Graveyard_CursedTreasureRedStone";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_JewelShopWhereLifeSpringsForth) {
            return "Tablet_Graveyard_JewelShopWhereLifeSpringsForth";
        }
        if(blockNumber == BlockConstants.Tablet_Graveyard_WeShallAwaitTheeAhead) {
            return "Tablet_Graveyard_WeShallAwaitTheeAhead";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_EveryHeartHasAWeight) {
            return "Tablet_Moonlight_EveryHeartHasAWeight";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_MindThyManners) {
            return "Tablet_Moonlight_MindThyManners";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_AtopTheNavelOfNeptune) {
            return "Tablet_Moonlight_AtopTheNavelOfNeptune";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_UltimateSacrificeForPower) {
            return "Tablet_Moonlight_UltimateSacrificeForPower";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_OcarinaWomanWithChild) {
            return "Tablet_Moonlight_OcarinaWomanWithChild";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_WedgesGiveFormToTheSoul) {
            return "Tablet_Moonlight_WedgesGiveFormToTheSoul";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_WaterWillProvideThePower) {
            return "Tablet_Moonlight_WaterWillProvideThePower";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_BookOfTheDead) {
            return "Tablet_Moonlight_BookOfTheDead";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_GrindDownThePyramid) {
            return "Tablet_Moonlight_GrindDownThePyramid";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_UnderworldPalace) {
            return "Tablet_Moonlight_UnderworldPalace";
        }
        if(blockNumber == BlockConstants.Tablet_Moonlight_SigilOfSpiritThatControlsLife) {
            return "Tablet_Moonlight_SigilOfSpiritThatControlsLife";
        }
        if(blockNumber == BlockConstants.Tablet_Goddess_HunkOfFlyingIron) {
            return "Tablet_Goddess_HunkOfFlyingIron";
        }
        if(blockNumber == BlockConstants.Tablet_Goddess_SecretOfLifePowerOfDestruction) {
            return "Tablet_Goddess_SecretOfLifePowerOfDestruction";
        }
        if(blockNumber == BlockConstants.Tablet_Goddess_CannotGrantTheMothersWish) {
            return "Tablet_Goddess_CannotGrantTheMothersWish";
        }
        if(blockNumber == BlockConstants.Tablet_Goddess_ABaoAQu) {
            return "Tablet_Goddess_ABaoAQu";
        }
        if(blockNumber == BlockConstants.Tablet_Goddess_ChallengeTheChamberOfBirth) {
            return "Tablet_Goddess_ChallengeTheChamberOfBirth";
        }
        if(blockNumber == BlockConstants.Tablet_Goddess_FlyWithTheGoldenWings) {
            return "Tablet_Goddess_FlyWithTheGoldenWings";
        }
        if(blockNumber == BlockConstants.Tablet_Goddess_MagatamaJewel) {
            return "Tablet_Goddess_MagatamaJewel";
        }
        if(blockNumber == BlockConstants.Tablet_Goddess_BalanceTheSpirits) {
            return "Tablet_Goddess_BalanceTheSpirits";
        }
        if(blockNumber == BlockConstants.Tablet_Goddess_AfterThineAscensionToTheTower) {
            return "Tablet_Goddess_AfterThineAscensionToTheTower";
        }
        if(blockNumber == BlockConstants.Tablet_Goddess_RusaliiYaksiDakini) {
            return "Tablet_Goddess_RusaliiYaksiDakini";
        }
        if(blockNumber == BlockConstants.Tablet_Goddess_IllusionOfGoddessStatues) {
            return "Tablet_Goddess_IllusionOfGoddessStatues";
        }
        if(blockNumber == BlockConstants.Tablet_Ruin_IdentifyTheSeductress) {
            return "Tablet_Ruin_IdentifyTheSeductress";
        }
        if(blockNumber == BlockConstants.Tablet_Ruin_TheOneWhoChallengesNuwa) {
            return "Tablet_Ruin_TheOneWhoChallengesNuwa";
        }
        if(blockNumber == BlockConstants.Tablet_Ruin_FaceTheLastTrial) {
            return "Tablet_Ruin_FaceTheLastTrial";
        }
        if(blockNumber == BlockConstants.Tablet_Ruin_GlowingRedCrucifixBeacon) {
            return "Tablet_Ruin_GlowingRedCrucifixBeacon";
        }
        if(blockNumber == BlockConstants.Tablet_Ruin_TheOnesThatCreatedNuwa) {
            return "Tablet_Ruin_TheOnesThatCreatedNuwa";
        }
        if(blockNumber == BlockConstants.Tablet_Ruin_GreatBirdMercyCrushingThatHand) {
            return "Tablet_Ruin_GreatBirdMercyCrushingThatHand";
        }
        if(blockNumber == BlockConstants.Tablet_Ruin_TooQuickForTheEyes) {
            return "Tablet_Ruin_TooQuickForTheEyes";
        }
        if(blockNumber == BlockConstants.Tablet_Ruin_Skanda) {
            return "Tablet_Ruin_Skanda";
        }
//        if(blockNumber == BlockConstants.Tablet_Ruin_Rosetta) {
//            return "Tablet_Ruin_Rosetta";
//        }
        if(blockNumber == BlockConstants.Tablet_Ruin_UnintentionalMischief) {
            return "Tablet_Ruin_UnintentionalMischief";
        }
        if(blockNumber == BlockConstants.Tablet_Ruin_FollowTheNameThatMeansUnnamed) {
            return "Tablet_Ruin_FollowTheNameThatMeansUnnamed";
        }
        if(blockNumber == BlockConstants.Tablet_Ruin_WhiteDiamondRepresentsPurity) {
            return "Tablet_Ruin_WhiteDiamondRepresentsPurity";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_BrightLightWithinTheDarkness) {
            return "Tablet_Birth_BrightLightWithinTheDarkness";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_GoldenGlowThinlyStretchedLight) {
            return "Tablet_Birth_GoldenGlowThinlyStretchedLight";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_ShallNotMakeItWithThinePower) {
            return "Tablet_Birth_ShallNotMakeItWithThinePower";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_BePreparedForDeath) {
            return "Tablet_Birth_BePreparedForDeath";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_ClayDollsBecomeHuman) {
            return "Tablet_Birth_ClayDollsBecomeHuman";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_BecameHumanAndProspered) {
            return "Tablet_Birth_BecameHumanAndProspered";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_PalenqueSlumbers) {
            return "Tablet_Birth_PalenqueSlumbers";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_StrikeTheWedgeDisturbTheMother) {
            return "Tablet_Birth_StrikeTheWedgeDisturbTheMother";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_PraiseLifeClayDollGoldenKey) {
            return "Tablet_Birth_PraiseLifeClayDollGoldenKey";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_BeyondTheManyCorpsesAWomanWaits) {
            return "Tablet_Birth_BeyondTheManyCorpsesAWomanWaits";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_ContinuesWithoutEndBirthDeath) {
            return "Tablet_Birth_ContinuesWithoutEndBirthDeath";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_ChallengePalenque) {
            return "Tablet_Birth_ChallengePalenque";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_VishnuBeheadedAsura) {
            return "Tablet_Birth_VishnuBeheadedAsura";
        }
//        if(blockNumber == BlockConstants.Tablet_Birth_Rosetta) {
//            return "Tablet_Birth_Rosetta";
//        }
        if(blockNumber == BlockConstants.Tablet_Birth_WhiteOpalRepresentsJoy) {
            return "Tablet_Birth_WhiteOpalRepresentsJoy";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_CrystalSkullSuppressesTiamat) {
            return "Tablet_Birth_CrystalSkullSuppressesTiamat";
        }
        if(blockNumber == BlockConstants.Tablet_Birth_DanceInThisPlace) {
            return "Tablet_Birth_DanceInThisPlace";
        }
        if(blockNumber == BlockConstants.Tablet_Dimensional_BeyondTheBoundariesOfTime) {
            return "Tablet_Dimensional_BeyondTheBoundariesOfTime";
        }
        if(blockNumber == BlockConstants.Tablet_Dimensional_RoadToTreasure) {
            return "Tablet_Dimensional_RoadToTreasure";
        }
        if(blockNumber == BlockConstants.Tablet_Dimensional_TwoVessels) {
            return "Tablet_Dimensional_TwoVessels";
        }
        if(blockNumber == BlockConstants.Tablet_Dimensional_LivesThe11ChildrenHave) {
            return "Tablet_Dimensional_LivesThe11ChildrenHave";
        }
        if(blockNumber == BlockConstants.Tablet_Dimensional_ThrustInfinityThere) {
            return "Tablet_Dimensional_ThrustInfinityThere";
        }
        if(blockNumber == BlockConstants.Tablet_Surface_RuinsEntrance) {
            return "Tablet_Surface_RuinsEntrance";
        }
        if(blockNumber == BlockConstants.Tablet_Inferno_SwingYourWeapon) {
            return "Tablet_Inferno_SwingYourWeapon";
        }
        if(blockNumber == BlockConstants.Tablet_Guidance_PushTheWhiteBox) {
            return "Tablet_Guidance_PushTheWhiteBox";
        }
        if(blockNumber == BlockConstants.Tablet_Endless_MapColor) {
            return "Tablet_Endless_MapColor";
        }
        if(blockNumber == BlockConstants.Tablet_Shrine_ChosenOnly) {
            return "Tablet_Shrine_ChosenOnly";
        }
        if(blockNumber == BlockConstants.Tablet_Dimensional_OnlyOnePathToSurvival) {
            return "Tablet_Dimensional_OnlyOnePathToSurvival";
        }
        if(blockNumber == BlockConstants.Tablet_Sun_AimAndShoot) {
            return "Tablet_Sun_AimAndShoot";
        }
        if(blockNumber == BlockConstants.Tablet_Eden) {
            return "Tablet_Eden";
        }
        if(blockNumber == BlockConstants.Tablet_HT_Sleep) {
            return "Tablet_HT_Sleep";
        }
        if(blockNumber == BlockConstants.Tablet_HT_IExistHere) {
            return "Tablet_HT_IExistHere";
        }
        if(blockNumber == BlockConstants.Tablet_HT_ClothToMakeTheTreasure) {
            return "Tablet_HT_ClothToMakeTheTreasure";
        }
        return null;
    }

    @Override
    public void addCustomBlocks(DatFileData datFileData) {
        if(!LocationCoordinateMapper.isSurfaceStart() && Settings.getCurrentStartingLocation() != 23 && Settings.getCurrentStartingLocation() != 24) {
            Block block = AddObject.buildShopBlock();
            datFileData.addCustomBlock(CustomBlockEnum.DefaultShopBlock, block);
            if(!DataFromFile.getMapOfShopNameToShopBlock().containsKey(DataFromFile.CUSTOM_SHOP_NAME)) {
                DataFromFile.getMapOfShopNameToShopBlock().put(DataFromFile.CUSTOM_SHOP_NAME, block.getBlockNumber());
            }
        }

        if(Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart() || Settings.isRandomizeStartingLocation() || HolidaySettings.isFools2019Mode() || HolidaySettings.isFools2020Mode()) {
            datFileData.addCustomBlock(CustomBlockEnum.CustomXelpudIntro, AddBlock.buildXelpudIntroBlock());
        }
        datFileData.addCustomBlock(CustomBlockEnum.RecordableStoneConversation,
                AddBlock.buildRecordableStoneConversationBlock());
        int recordableStoneConversationBlockIndex = getCustomBlockIndex(CustomBlockEnum.RecordableStoneConversation);
        datFileData.addCustomBlock(CustomBlockEnum.RecordableStonePhilosopherGiltoriyo,
                AddBlock.buildRecordableStoneConversationMasterBlock(CustomBlockEnum.RecordableStonePhilosopherGiltoriyo,
                        recordableStoneConversationBlockIndex));
        datFileData.addCustomBlock(CustomBlockEnum.RecordableStonePhilosopherAlsedana,
                AddBlock.buildRecordableStoneConversationMasterBlock(CustomBlockEnum.RecordableStonePhilosopherAlsedana,
                        recordableStoneConversationBlockIndex));
        datFileData.addCustomBlock(CustomBlockEnum.RecordableStonePhilosopherSamaranta,
                AddBlock.buildRecordableStoneConversationMasterBlock(CustomBlockEnum.RecordableStonePhilosopherSamaranta,
                        recordableStoneConversationBlockIndex));
        datFileData.addCustomBlock(CustomBlockEnum.RecordableStonePhilosopherFobos,
                AddBlock.buildRecordableStoneConversationMasterBlock(CustomBlockEnum.RecordableStonePhilosopherFobos,
                        recordableStoneConversationBlockIndex));
    }
}
