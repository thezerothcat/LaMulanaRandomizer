package lmr.randomizer.update;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.ZoneConstants;

public class FlagTimerUpdates {
    private FlagTimerUpdates() { }

    public static boolean updateFlagTimer(GameObject flagTimer) {
        if(isVanillaShopItemTimer(flagTimer)) {
            return false;
        }
        if(isVanillaBacksideDoorTimer(flagTimer)) {
            return false;
        }
        if(isXelpudTentTimer(flagTimer)) {
            return false;
        }
        if(isMulanaTalismanPuzzleTimer(flagTimer)) {
            return false;
        }
        if(isMSX2XelpudConversationTimer(flagTimer)) {
            return false;
        }
        ObjectContainer objectContainer = flagTimer.getObjectContainer();
        if(flagTimer.getObjectContainer() instanceof Screen) {
            if(isUnwantedScreenTimer(flagTimer, (Screen)objectContainer)) {
                return false;
            }
        }
        if(!(objectContainer instanceof Zone)) {
            if(isUnwantedNonZoneTimer(flagTimer)) {
                return false;
            }
        }

        updateTimerUpdates(flagTimer);
        updateTimerTests(flagTimer);
        updateTrueShrineTransformationTimer(flagTimer);
        return true;
    }

    private static boolean isVanillaShopItemTimer(GameObject flagTimer) {
        // Get rid of timer objects related to purchasing the pre-randomized item
        for (WriteByteOperation flagUpdate : flagTimer.getWriteByteOperations()) {
            if (isRandomizedShopItem(flagUpdate.getIndex())) {
                for (TestByteOperation flagTest : flagTimer.getTestByteOperations()) {
                    if (flagTest.getIndex() == flagUpdate.getIndex() && flagTest.getValue() == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isVanillaBacksideDoorTimer(GameObject flagTimer) {
        // Remove timers for backside doors
        if(Settings.isRandomizeNonBossDoors()) {
            for (WriteByteOperation flagUpdate : flagTimer.getWriteByteOperations()) {
                if(flagUpdate.getIndex() == FlagConstants.AMPHISBAENA_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.AMPHISBAENA_GATE_OPEN
                        || flagUpdate.getIndex() == FlagConstants.SAKIT_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.SAKIT_GATE_OPEN
                        || flagUpdate.getIndex() == FlagConstants.ELLMAC_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.ELLMAC_GATE_OPEN
                        || flagUpdate.getIndex() == FlagConstants.BAHAMUT_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.BAHAMUT_GATE_OPEN
                        || flagUpdate.getIndex() == FlagConstants.VIY_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.VIY_GATE_OPEN
                        || flagUpdate.getIndex() == FlagConstants.PALENQUE_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.PALENQUE_GATE_OPEN
                        || flagUpdate.getIndex() == FlagConstants.BAPHOMET_GATE_MIRROR_COVER || flagUpdate.getIndex() == FlagConstants.BAPHOMET_GATE_OPEN
                        || flagUpdate.getIndex() == FlagConstants.KEY_FAIRY_DOOR_UNLOCKED_V2) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isXelpudTentTimer(GameObject flagTimer) {
        for (WriteByteOperation flagUpdate : flagTimer.getWriteByteOperations()) {
            if(flagUpdate.getIndex() == FlagConstants.XELPUD_TENT_OPEN
                    || flagUpdate.getIndex() == FlagConstants.XELPUD_READY_TO_TALK) {
                return true;
            }
        }
        return false;
    }

    private static boolean isMulanaTalismanPuzzleTimer(GameObject flagTimer) {
        for (WriteByteOperation flagUpdate : flagTimer.getWriteByteOperations()) {
            if(flagUpdate.getIndex() == FlagConstants.SHRINE_PUZZLE_DIARY_CHEST) {
                return true;
            }
        }
        return false;
    }

    private static boolean isMSX2XelpudConversationTimer(GameObject flagTimer) {
        for (TestByteOperation flagTest : flagTimer.getTestByteOperations()) {
            if(flagTest.getIndex() == FlagConstants.WF_MSX2) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRandomizedShopItem(int worldFlag) {
        for(String shopItem : DataFromFile.getRandomizedShopItems()) {
            if(DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(shopItem).getWorldFlag() == worldFlag) {
                FileUtils.log(String.format("Removing timer object for item %s with world flag %s", shopItem, worldFlag));
                return true;
            }
        }
        return false;
    }

    private static boolean isUnwantedScreenTimer(GameObject flagTimer, Screen screen) {
        int zoneIndex = screen.getZoneIndex();
        if(zoneIndex == ZoneConstants.TWIN_FRONT && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 2) {
            for(WriteByteOperation flagUpdate : flagTimer.getWriteByteOperations()) {
                if(flagUpdate.getIndex() == FlagConstants.BIG_BROTHER_UNLOCKED) {
                    // Remove unneeded timer on original Little Brother screen
                    return true;
                }
            }
        }
        else if(zoneIndex == ZoneConstants.SPRING || zoneIndex == ZoneConstants.SHRINE_BACK) {
            for(WriteByteOperation flagUpdate : flagTimer.getWriteByteOperations()) {
                if(flagUpdate.getIndex() == FlagConstants.MANTRA_MARDUK) {
                    // Timer for MARDUK mantra update
                    return true;
                }
            }
        }
        else if(zoneIndex == ZoneConstants.SURFACE) {
            for(WriteByteOperation flagUpdate : flagTimer.getWriteByteOperations()) {
                if(flagUpdate.getIndex() == FlagConstants.XELPUD_CONVERSATION_GENERAL) {
                    // Remove Xelpud screen timers
                    return true;
                }
            }
        }

        if(Settings.isRandomizeTransitionGates()) {
            if(zoneIndex != ZoneConstants.SUN && zoneIndex != ZoneConstants.TWIN_FRONT) {
                for(TestByteOperation testByteOperation : flagTimer.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == FlagConstants.TWINS_POISON) {
                        return true;
                    }
                }
            }

            if(zoneIndex == ZoneConstants.SURFACE) {
                // Surface timer for resetting HT unlock
                for(TestByteOperation testByteOperation : flagTimer.getTestByteOperations()) {
                    if (testByteOperation.getIndex() == FlagConstants.HT_UNLOCK_CHAIN_PRIMARY
                            && ByteOp.FLAG_GTEQ.equals(testByteOperation.getOp())
                            && testByteOperation.getValue() == 3) {
                        return true;
                    }
                }
            }
            else if(zoneIndex == ZoneConstants.INFERNO) {
                for(TestByteOperation testByteOperation : flagTimer.getTestByteOperations()) {
                    if (testByteOperation.getIndex() == FlagConstants.BUER_STATE
                            && testByteOperation.getValue() == 1) {
                        return true;
                    }
                }
            }
            else if(zoneIndex == ZoneConstants.GRAVEYARD) {
                for(TestByteOperation testByteOperation : flagTimer.getTestByteOperations()) {
                    if (testByteOperation.getIndex() == FlagConstants.GODDESS_STATUE_SHIELD_ANIMATION
                            && testByteOperation.getValue() == 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isUnwantedNonZoneTimer(GameObject flagTimer) {
        for (int i = 0; i < flagTimer.getWriteByteOperations().size(); i++) {
            WriteByteOperation updateFlag = flagTimer.getWriteByteOperations().get(i);
            if (updateFlag.getIndex() == FlagConstants.SACRED_ORB_COUNT) {
                // Sacred orb flag for heal rate
                return true;
            }
            else if(updateFlag.getIndex() == FlagConstants.WF_ANGEL_SHIELD) {
                // Get rid of Angel Shield shop timer on Graveyard alt shop screen (alt shop has been removed).
                return true;
            }
            else if(updateFlag.getIndex() == FlagConstants.WF_VANILLA_DRAGON_BONE) {
                // Get rid of Dragon Bone shop timer. We're using a different flag for Dragon Bone
                // since the base game's flag was bugged and fixing it could have unexpected effects,
                // but it still doesn't hurt to get rid of this thing.
                return true;
            }
            else if(updateFlag.getIndex() == FlagConstants.WF_MULANA_TALISMAN) {
                // Get rid of Mulana Talisman timer related to Xelpud conversation. We're using different flags for that.
                return true;
            }
            else if(updateFlag.getIndex() == FlagConstants.SHRINE_DIARY_CHEST && updateFlag.getValue() == 1) {
                // With changed event flow for Xelpud pillar/Diary chest, this timer isn't needed
                return true;
            }
        }
        return false;
    }

    private static void updateTimerTests(GameObject flagTimer) {
        for(int i = 0; i < flagTimer.getTestByteOperations().size(); i++) {
            TestByteOperation flagTest = flagTimer.getTestByteOperations().get(i);
            if(flagTest.getIndex() == FlagConstants.SCORE && flagTest.getValue() == 56) {
                // Mulbruk score check timer - if you don't have the right score, the timer won't set the flag to
                // spawn the Mulbruk conversation object that would let you get Book of the Dead.
                flagTest.setValue((byte)0);
            }
            else if (flagTest.getIndex() == FlagConstants.MANTRA_MARDUK) {
                // Timers related to MARDUK mantra. Some other timers have been removed in RcdReader.
                if(flagTimer.getObjectContainer() instanceof Screen && ((Screen)flagTimer.getObjectContainer()).getZoneIndex() == ZoneConstants.ENDLESS) {
                    flagTest.setOp(ByteOp.FLAG_LTEQ);

                    // Add test for Giltoriyo conversation
                    flagTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.MANTRAS_UNLOCKED, ByteOp.FLAG_EQUALS, 1));
                    break;
                }
            }
            else if (flagTest.getIndex() == FlagConstants.MANTRA_SABBAT) {
                // Timers related to SABBAT mantra. Some other timers have been removed in RcdReader.
                if(flagTimer.getObjectContainer() instanceof Screen && ((Screen)flagTimer.getObjectContainer()).getZoneIndex() == 7) {
                    flagTest.setOp(ByteOp.FLAG_LTEQ);

                    // Add test for Giltoriyo conversation
                    flagTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.MANTRAS_UNLOCKED, ByteOp.FLAG_EQUALS, 1));
                    break;
                }
            }
            else if (flagTest.getIndex() == FlagConstants.MANTRA_MU) {
                // Timers related to MU mantra.
                if(flagTimer.getObjectContainer() instanceof Screen && ((Screen)flagTimer.getObjectContainer()).getZoneIndex() == 6) {
                    flagTest.setOp(ByteOp.FLAG_LTEQ);

                    // Add test for Giltoriyo conversation
                    flagTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.MANTRAS_UNLOCKED, ByteOp.FLAG_EQUALS, 1));
                    break;
                }
            }
            else if (flagTest.getIndex() == FlagConstants.MANTRA_BAHRUN) {
                // Timers related to BAHRUN mantra.
                if(flagTimer.getObjectContainer() instanceof Screen && ((Screen)flagTimer.getObjectContainer()).getZoneIndex() == 13) {
                    flagTest.setOp(ByteOp.FLAG_LTEQ);

                    // Add test for Giltoriyo conversation
                    flagTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.MANTRAS_UNLOCKED, ByteOp.FLAG_EQUALS, 1));
                    break;
                }
            }
            else if (flagTest.getIndex() == FlagConstants.MANTRA_WEDJET) {
                // Timers related to WEDJET mantra.
                if(flagTimer.getObjectContainer() instanceof Screen && ((Screen)flagTimer.getObjectContainer()).getZoneIndex() == 3) {
                    flagTest.setOp(ByteOp.FLAG_LTEQ);

                    // Add test for Giltoriyo conversation
                    flagTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.MANTRAS_UNLOCKED, ByteOp.FLAG_EQUALS, 1));
                    break;
                }
            }
            else if (flagTest.getIndex() == FlagConstants.MANTRA_ABUTO) {
                // Timers related to ABUTO mantra.
                if(flagTimer.getObjectContainer() instanceof Screen && ((Screen)flagTimer.getObjectContainer()).getZoneIndex() == 11) {
                    flagTest.setOp(ByteOp.FLAG_LTEQ);

                    // Add test for Giltoriyo conversation
                    flagTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.MANTRAS_UNLOCKED, ByteOp.FLAG_EQUALS, 1));
                    break;
                }
            }
            else if (flagTest.getIndex() == FlagConstants.MANTRA_FINAL) {
                // Timers related to LAMULANA mantra.
                if(flagTest.getValue() == 1) {
                    if(flagTimer.getObjectContainer() instanceof Screen && ((Screen)flagTimer.getObjectContainer()).getZoneIndex() == 0) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);

                        // Add test for Giltoriyo conversation
                        flagTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.MANTRAS_UNLOCKED, ByteOp.FLAG_EQUALS, 1));
                        break;
                    }
                }
                else if(flagTest.getValue() == 4) {
                    flagTest.setIndex(FlagConstants.MANTRA_LAMULANA);
                    flagTest.setValue((byte)1);
                }
            }
            else if(flagTest.getIndex() == FlagConstants.COG_MUDMEN_STATE) {
                // Timer to update Cog of the Soul puzzle.
                flagTest.setIndex(FlagConstants.ILLUSION_PUZZLE_COG_CHEST);
                for(WriteByteOperation writeByteOperation : flagTimer.getWriteByteOperations()) {
                    if(writeByteOperation.getIndex() == FlagConstants.COG_MUDMEN_STATE) {
                        writeByteOperation.setIndex(FlagConstants.ILLUSION_PUZZLE_COG_CHEST);
                        break;
                    }
                }
                break;
            }
        }
    }

    private static void updateTimerUpdates(GameObject flagTimer) {
        for (WriteByteOperation flagUpdate : flagTimer.getWriteByteOperations()) {
            if (flagUpdate.getIndex() == FlagConstants.PROVE_THOU_ART_SMALL && flagUpdate.getValue() == 3) {
                // Becoming small
                for(TestByteOperation flagTest : flagTimer.getTestByteOperations()) {
                    if (flagTest.getIndex() == FlagConstants.PROVE_THOU_ART_SMALL && ByteOp.FLAG_EQUALS.equals(flagTest.getOp()) && flagTest.getValue() == 2) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);
                        break;
                    }
                }
            }
            else if(flagUpdate.getIndex() == FlagConstants.HT_UNLOCK_PROGRESS_EARLY) {
                if(flagUpdate.getValue() == 1) {
                    if((Settings.isRandomizeForbiddenTreasure() && Settings.isHTFullRandom())
                            || Settings.isRandomizeDracuetShop()) {
                        // Get rid of 8-boss requirement on HT.
                        Integer flagToRemoveIndex = null;
                        for (int i = 0; i < flagTimer.getTestByteOperations().size(); i++) {
                            if (flagTimer.getTestByteOperations().get(i).getIndex() == FlagConstants.BOSSES_SHRINE_TRANSFORM) {
                                flagToRemoveIndex = i;
                                break;
                            }
                        }
                        if (flagToRemoveIndex != null) {
                            flagTimer.getTestByteOperations().remove((int) flagToRemoveIndex);
                        }
                    }
                }
            }
        }
    }

    private static void updateTrueShrineTransformationTimer(GameObject flagTimer) {
        if(Settings.isFoolsGameplay() && Settings.getCurrentBossCount() != 8) {
            // Timers for unlocking true shrine, normally set value from 8 to 9
            boolean addTest = false;
            for(TestByteOperation testByteOperation : flagTimer.getTestByteOperations()) {
                if (testByteOperation.getIndex() == FlagConstants.BOSSES_SHRINE_TRANSFORM
                        && ByteOp.FLAG_EQUALS.equals(testByteOperation.getOp())
                        && testByteOperation.getValue() == 8) {
                    addTest = true;
                    testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                    testByteOperation.setValue((byte)9);
                    break;
                }
            }
            if(addTest) {
                flagTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_GTEQ, Settings.getCurrentBossCount()));
            }
        }
    }
}
