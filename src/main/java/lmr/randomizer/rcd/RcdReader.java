package lmr.randomizer.rcd;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.AddObject;
import lmr.randomizer.random.ShopRandomizationEnum;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.GameDataTracker;
import lmr.randomizer.update.LocationCoordinateMapper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by thezerothcat on 7/18/2017.
 */
public final class RcdReader {
    private RcdReader() {
    }

    private static byte[] getByteArraySlice(byte[] mainArray, int startIndex, int length) {
        byte[] slice = new byte[length];
        for(int i = 0; i < length; i++) {
            slice[i] = mainArray[startIndex + i];
        }
        return slice;
    }

    private static ByteBuffer getField(byte[] mainArray, int startIndex, int length) {
        return ByteBuffer.wrap(getByteArraySlice(mainArray, startIndex, length)).order(ByteOrder.BIG_ENDIAN);
    }

    /**
     * Reads in an object from the rcd file, adds it to the given ObjectContainer (unless we don't want to keep it),
     * and then notifies GameObjectManager of the object.
     * @param objectContainer the container (zone, room, or screen) we're adding this object to
     * @param rcdBytes all the bytes from the rcd file
     * @param rcdByteIndex byte index when starting to read this object
     * @param hasPosition whether or not this object includes position data
     * @return new rcdByteIndex after building this object
     */
    private static int addObject(ObjectContainer objectContainer, byte[] rcdBytes, int rcdByteIndex, boolean hasPosition) {
        GameObject obj = new GameObject(objectContainer);

        obj.setId(getField(rcdBytes, rcdByteIndex, 2).getShort());
        rcdByteIndex += 2;

        byte temp = rcdBytes[rcdByteIndex];
        rcdByteIndex += 1;

        int writeOperationCount = temp & 0xf;
        int testOperationCount = temp >> 4;

        byte argCount = rcdBytes[rcdByteIndex];
        rcdByteIndex += 1;

        if(hasPosition) {
            obj.setX(20 * getField(rcdBytes, rcdByteIndex, 2).getShort());
            rcdByteIndex += 2;
            obj.setY(20 * getField(rcdBytes, rcdByteIndex, 2).getShort());
            rcdByteIndex += 2;
        }
        else {
            obj.setX((short) -1);
            obj.setY((short) -1);
        }

        for(int i = 0; i < testOperationCount; i++) {
            obj.getTestByteOperations().add(getTestByteOperation(rcdBytes, rcdByteIndex));
            rcdByteIndex += 4;
        }
        for(int i = 0; i < writeOperationCount; i++) {
            obj.getWriteByteOperations().add(getWriteByteOperation(rcdBytes, rcdByteIndex));
            rcdByteIndex += 4;
        }

        for(int argIndex = 0; argIndex < argCount; argIndex++) {
            obj.getArgs().add(getField(rcdBytes, rcdByteIndex, 2).getShort());
            rcdByteIndex += 2;
        }

        boolean keepObject = true;
        if (obj.getId() == 0x07) {
            if(Settings.isRandomizeTransitionGates()) {
                if (obj.getObjectContainer() instanceof Screen) {
                    Screen screen = (Screen)obj.getObjectContainer();
                    if (screen.getZoneIndex() == 11 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                        // Remove the Eden flag, since this door may not lead to Eden.
                        Integer flagIndexToRemove = null;
                        for(int i = 0; i < obj.getTestByteOperations().size(); i++) {
                            TestByteOperation testByteOperation = obj.getTestByteOperations().get(i);
                            if(testByteOperation.getIndex() == 0x226) {
                                flagIndexToRemove = i;
                                break;
                            }
                        }
                        if(flagIndexToRemove != null) {
                            obj.getTestByteOperations().remove((int)flagIndexToRemove);
                        }
                    }
                }
            }
        }
        else if (obj.getId() == 0x0b) {
            if(!ShopRandomizationEnum.NONE.equals(Settings.getShopRandomization())) {
                // Get rid of timer objects related to purchasing the pre-randomized item
                for (WriteByteOperation flagUpdate : obj.getWriteByteOperations()) {
                    if (isRandomizedShopItem(flagUpdate.getIndex())) {
                        for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                            if (flagTest.getIndex() == flagUpdate.getIndex() && flagTest.getValue() == 1) {
                                FileUtils.log("Timer object excluded from rcd");
                                return rcdByteIndex;
                            }
                        }
                    }
                }
            }

            if(Settings.isRandomizeTransitionGates()) {
                if(obj.getObjectContainer() instanceof Screen) {
                    int zoneIndex = ((Screen)obj.getObjectContainer()).getZoneIndex();
                    if(zoneIndex != 3 && zoneIndex != 7) {
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if(testByteOperation.getIndex() == 0x1d7) {
                                keepObject = false;
                                break;
                            }
                        }
                    }

                    if(zoneIndex == 1) {
                        // Surface timer for resetting HT unlock
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if (testByteOperation.getIndex() == 0x3ba
                                    && ByteOp.FLAG_GTEQ.equals(testByteOperation.getOp())
                                    && testByteOperation.getValue() == 3) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                    else if(zoneIndex == 5) {
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if (testByteOperation.getIndex() == 0x17a && testByteOperation.getValue() == 1) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                    else if(zoneIndex == 11) {
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if (testByteOperation.getIndex() == 0x284 && testByteOperation.getValue() == 2) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
            }

            if(Settings.isRandomizeNonBossDoors()) {
                for (WriteByteOperation flagUpdate : obj.getWriteByteOperations()) {
                    if(flagUpdate.getIndex() == 0x15c || flagUpdate.getIndex() == 0x15d
                            || flagUpdate.getIndex() == 0x16d || flagUpdate.getIndex() == 0x16e
                            || flagUpdate.getIndex() == 0x175 || flagUpdate.getIndex() == 0x176
                            || flagUpdate.getIndex() == 0x1bd || flagUpdate.getIndex() == 0x1be
                            || flagUpdate.getIndex() == 0x152 || flagUpdate.getIndex() == 0x153
                            || flagUpdate.getIndex() == 0x2b9 || flagUpdate.getIndex() == 0x1d0
                            || flagUpdate.getIndex() == 0x3b7 || flagUpdate.getIndex() == 0x1c0
                            || flagUpdate.getIndex() == 0x38c) {
                        keepObject = false;
                    }
                }
            }

            if(Settings.isFoolsNpc()) {
                if(obj.getObjectContainer() instanceof Screen) {
                    Screen screen = ((Screen)obj.getObjectContainer());
                    if (screen.getZoneIndex() == 7 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 2) {
                        for (WriteByteOperation flagUpdate : obj.getWriteByteOperations()) {
                            if(flagUpdate.getIndex() == 0x1f0) {
                                // Remove unneeded timer on original Little Brother screen
                                keepObject = false;
                                break;
                            }
                        }
                    }
                } else if (obj.getObjectContainer() instanceof Zone) {
                    boolean found = false;
                    for (WriteByteOperation flagUpdate : obj.getWriteByteOperations()) {
                        if(flagUpdate.getIndex() == 0x1cd) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        int seconds = 5;
                        obj.getArgs().set(0, (short)seconds);

                        // Add reverse lighting timer
                        AddObject.addSecondsTimer(obj.getObjectContainer(), seconds,
                                Arrays.asList(new TestByteOperation(0x1c2, ByteOp.FLAG_NOT_EQUAL, 3),
                                        new TestByteOperation(0x1cd, ByteOp.FLAG_EQUALS, 0)),
                                Arrays.asList(new WriteByteOperation(0x1cd, ByteOp.ASSIGN_FLAG, 1)));
                    }
                }
            }

            if(Settings.isFoolsGameplay() && Settings.getCurrentBossCount() != 8) {
                // Timers for unlocking true shrine, normally set value from 8 to 9
                boolean addTest = false;
                for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                    if (testByteOperation.getIndex() == 0x102
                           && ByteOp.FLAG_EQUALS.equals(testByteOperation.getOp())
                           && testByteOperation.getValue() == 8) {
                        addTest = true;
                        testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                        testByteOperation.setValue((byte)9);
                        break;
                    }
                }
                if(addTest) {
                    obj.getTestByteOperations().add(new TestByteOperation(0x102, ByteOp.FLAG_GTEQ, Settings.getCurrentBossCount()));
                }
            }
            if(!(objectContainer instanceof Zone)) {
                for (int i = 0; i < obj.getWriteByteOperations().size(); i++) {
                    WriteByteOperation updateFlag = obj.getWriteByteOperations().get(i);
                    if (updateFlag.getIndex() == 852) {
                        // Sacred orb flag for heal rate
                        keepObject = false;
                        break;
                    }
                    else if(updateFlag.getIndex() == 299) {
                        // Timer for MARDUK mantra update
                        if(objectContainer instanceof Screen) {
                            int zoneIndex = ((Screen) objectContainer).getZoneIndex();
                            if(zoneIndex == 4 || zoneIndex == 18) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                    else if(updateFlag.getIndex() == 141) {
                        // Get rid of Angel Shield shop timer on Graveyard alt shop screen (alt shop has been removed).
                        keepObject = false;
                        break;
                    }
                    else if(updateFlag.getIndex() == 157) {
                        // Get rid of Dragon Bone shop timer. We're using a different flag for Dragon Bone
                        // since the base game's flag was bugged and fixing it could have unexpected effects,
                        // but it still doesn't hurt to get rid of this thing.
                        keepObject = false;
                        break;
                    }
                    else if(updateFlag.getIndex() == 261) {
                        // Get rid of Mulana Talisman timer related to Xelpud conversation. We're using different flags for that.
                        keepObject = false;
                        break;
                    }
                    else if(updateFlag.getIndex() == 537 && updateFlag.getValue() == 1) {
                        // With changed event flow for Xelpud pillar/Diary chest, this timer isn't needed
                        keepObject = false;
                        break;
                    }
                }
            }
        }
        else if (obj.getId() == 0x12) {
            if(Settings.isRandomizeTransitionGates()) {
                if (obj.getObjectContainer() instanceof Screen) {
                    Screen screen = (Screen)obj.getObjectContainer();
                    if (screen.getZoneIndex() == 11 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if (testByteOperation.getIndex() == 0x226) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
            }
            if(Settings.isRandomizeNonBossDoors()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if (screen.getZoneIndex() == 6 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                        keepObject = false;
                    }
                }
            }
            if(Settings.isFools2021Mode()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if (screen.getZoneIndex() == 12) {
                        if (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0
                            || screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0
                            || screen.getRoomIndex() == 5 && screen.getScreenIndex() == 1
                            || screen.getRoomIndex() == 6 && screen.getScreenIndex() == 0) {
                            for (WriteByteOperation writeByteOperation : obj.getWriteByteOperations()) {
                                if (writeByteOperation.getIndex() == 0x00a) {
                                    // Retribution trigger wall in Moonlight pyramid
                                    keepObject = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(obj.getId() == 0x2c) {
            if(Settings.isFools2021Mode()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if (screen.getZoneIndex() == 0 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        keepObject = false; // Remove Treasures chest from Guidance.
                    }
                    else if (screen.getZoneIndex() == 12 && screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
                        keepObject = false; // Remove Philosopher's Ocarina chest from Moonlight.
                    }
                }
            }
        }
        else if(obj.getId() == 0x2d) {
            if(Settings.isFools2020Mode()) {
                // Remove weapon cover from Flare puzzle room
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if (screen.getZoneIndex() == 5 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                        keepObject = false;
                    }
                }
            }
        }
        else if(obj.getId() == 0x2e) {
            if(Settings.isFools2021Mode()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if (screen.getZoneIndex() == 0 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if (testByteOperation.getIndex() == 0x16a && ByteOp.FLAG_EQUALS.equals(testByteOperation.getOp()) && testByteOperation.getValue() == 2) {
                                // Delete the hardmode variant of Amphisbaena ankh
                                keepObject = false;
                                break;
                            }
                        }
                        if(keepObject) {
                            obj.getTestByteOperations().add(new TestByteOperation(0x354, ByteOp.FLAG_EQUALS, 0));

                            // Reduce health
                            obj.getArgs().set(2, (short)4);
                            // Buff damage
                            obj.getArgs().set(3, (short)32);
                            obj.getArgs().set(5, (short)32);
                        }
                    }
                }
            }
        }
        else if(obj.getId() == 0x2f) {
            if(Settings.isFools2021Mode()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if (screen.getZoneIndex() == 12 && screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
                        // Philosopher's Ocarina
                        convertToChest(obj);
                    }
                }
            }

            if(obj.getArgs().get(1) == 7) {
                // Remove empowered Key Sword
                keepObject = false;
            }
        }
        else if(obj.getId() == 0x46) {
            if(Settings.isRandomizeTransitionGates()) {
                // Spriggan statue still reachable during the escape sequence.
                Integer flagIndexToRemove = null;
                for(int i = 0; i < obj.getTestByteOperations().size(); i++) {
                    TestByteOperation testByteOperation = obj.getTestByteOperations().get(i);
                    if(testByteOperation.getIndex() == 0x0fe) {
                        flagIndexToRemove = i;
                        break;
                    }
                }
                if(flagIndexToRemove != null) {
                    obj.getTestByteOperations().remove((int)flagIndexToRemove);
                }
            }
        }
        else if(obj.getId() == 0x0e) {
            if(objectContainer instanceof Screen) {
                Screen screen = (Screen)objectContainer;
                if(screen.getZoneIndex() == 0 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                    if(Settings.isRandomizeTransitionGates()) {
                        // Guidance to Surface escape blockage
                        for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                            if(flagTest.getIndex() == 0x0fe && flagTest.getValue() == 3) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
                else if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                    // Temple of the Sun Map chest ladder stuff
                    TestByteOperation testByteOperation = new TestByteOperation();
                    testByteOperation.setIndex(12);
                    testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                    testByteOperation.setValue((byte)0);
                    obj.getTestByteOperations().add(testByteOperation);
                }
                else if(screen.getZoneIndex() == 4 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 1) {
                    if(Settings.isRandomizeTransitionGates()) {
                        // Spring to Surface escape blockage
                        for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                            if (flagTest.getIndex() == 0x0fe && flagTest.getValue() == 3) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
                else if(screen.getZoneIndex() == 6) {
                    if(screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
                        if (Settings.isRandomizeTransitionGates()) {
                            for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                                if(flagTest.getIndex() == 0x0fe && flagTest.getValue() == 3) {
                                    keepObject = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                        if (Settings.isRandomizeTransitionGates()) {
                            for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                                if (flagTest.getIndex() == 0x382 && flagTest.getValue() == 1) {
                                    keepObject = false;
                                    break;
                                }
                            }
                        }
                        if (Settings.isRandomizeNonBossDoors()) {
                            for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                                if (flagTest.getIndex() == 0x1c9 && flagTest.getValue() == 1) {
                                    keepObject = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                else if(screen.getZoneIndex() == 11 && screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                    if(Settings.isRandomizeTransitionGates()) {
                        for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                            if(flagTest.getIndex() == 0x0fe && flagTest.getValue() == 3) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
                else if(screen.getZoneIndex() == 11 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
                    if(Settings.isFools2021Mode()) {
                        for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                            if(flagTest.getIndex() == 0x3b5 && flagTest.getValue() == 1) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
                else if(screen.getZoneIndex() == 12 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                    if(Settings.isRandomizeTransitionGates()) {
                        for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                            if(flagTest.getIndex() == 0x0fe && flagTest.getValue() == 3) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
                else if(screen.getZoneIndex() == 13 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                    if(Settings.isRandomizeTransitionGates()) {
                        for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                            if(flagTest.getIndex() == 0x0fe && flagTest.getValue() == 3) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        else if (obj.getId() == 0xa0) {
            if(obj.getArgs().get(3) == 1) {
                if(obj.getArgs().get(4) == 272) {
                    Integer testFlagIndex = null;
                    for (int i = 0; i < obj.getTestByteOperations().size(); i++) {
                        TestByteOperation flagTest = obj.getTestByteOperations().get(i);
                        if (flagTest.getIndex() == 748) {
                            // This is the flag that prevents you from getting the original version of the Graveyard shop once you've killed all the guardians.
                            testFlagIndex = i;
                        }
                    }
                    if(testFlagIndex != null) {
                        obj.getTestByteOperations().remove((int)testFlagIndex);
                    }
                    if(Settings.isFools2020Mode()) {
                        obj.getArgs().set(4, (short)273);
                    }
                }
                else if(obj.getArgs().get(4) == 273) {
                    // Get rid of alternate Graveyard shop (with the Angel Shield)
                    keepObject = false;
                }
            }
            else if(obj.getArgs().get(4) == 719) {
                // Low-score version of Mulbruk which could interfere with getting Book of the Dead.
                keepObject = false;
            }
            else if(obj.getArgs().get(4) == 682 && Settings.isFoolsNpc()) {
                // Conversation to inform of unlocking Big Brother's shop, to be removed and re-added if shuffling NPCs for simplicity.
                keepObject = false;
            }
            else if(obj.getArgs().get(4) == 684 && !Settings.isHalloweenMode()) {
                // First Fairy Queen conversation, completely unneeded for randomizer outside of Halloween.
                keepObject = false;
            }
            else if(obj.getArgs().get(4) == 685 && Settings.isHalloweenMode()) {
                // Halloween doesn't need this Fairy Queen conversation.
                keepObject = false;
            }
            else if(obj.getArgs().get(4) == 985 && Settings.isHalloweenMode()) {
                // Halloween doesn't need this Fairy Queen conversation either.
                keepObject = false;
            }
            else if(obj.getArgs().get(4) == 913) {
                // Xelpud conversation after he goes to do the Diary thing.
                keepObject = false;
            }
            else if(obj.getArgs().get(4) == 1011) {
                if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
                    // Dracuet conversation has some unwanted flags
                    Integer flagToRemove = null;
                    for (int i = 0; i < obj.getTestByteOperations().size(); i++) {
                        TestByteOperation flagTest = obj.getTestByteOperations().get(i);
                        if (flagTest.getIndex() == 0x710) {
                            flagToRemove = i;
                        }
                    }
                    if(flagToRemove != null) {
                        obj.getTestByteOperations().remove((int)flagToRemove);
                    }

                    for (int i = 0; i < obj.getTestByteOperations().size(); i++) {
                        TestByteOperation flagTest = obj.getTestByteOperations().get(i);
                        if (flagTest.getIndex() == 0x106) {
                            flagTest.setIndex(0xaae);
                            flagTest.setValue((byte)0);
                        }
                    }
                }
            }
            else if(obj.getArgs().get(4) == 924) {
                // Mulbruk escape
                if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
                    keepObject = false;
                }
            }
            else if(obj.getArgs().get(4) == 926) {
                // Mulbruk escape
                if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
                    keepObject = false;
                }
            }
            else if(obj.getArgs().get(4) == 1014) {
                // Mulbruk conversation after she runs away from the Provocative Bathing Suit.
                keepObject = false;
            }

            if(objectContainer instanceof Screen) {
                Screen screen = (Screen)objectContainer;
                if(screen.getZoneIndex() == 1) {
                    if(screen.getRoomIndex() == 0 && screen.getScreenIndex() == 2
                            || screen.getRoomIndex() == 2 && screen.getScreenIndex() == 0
                            || screen.getRoomIndex() == 2 && screen.getScreenIndex() == 1) {
                        for (int i = 0; i < obj.getTestByteOperations().size(); i++) {
                            TestByteOperation flagTest = obj.getTestByteOperations().get(i);
                            if (flagTest.getIndex() == 0x145) {
                                // Swap Xelpud first-conversation flag with custom
                                flagTest.setIndex(0xad0);
                            }
                        }
                    }
                }
            }
        }
        else if (obj.getId() == 0x00) {
            if(objectContainer instanceof Screen) {
                Screen screen = (Screen)objectContainer;
                if(screen.getZoneIndex() == 10 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 2) {
                    if(obj.getArgs().get(0) == 3) {
                        // Shuriken pot
                        GameDataTracker.setSubweaponPot(obj);
                    }
                }

                if(screen.getZoneIndex() == 17 && screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                    // Dimensional - Umu Dabrutu's room
                    if (Settings.isFools2021Mode()) {
                        if(obj.getY() > 300) {
                            if(obj.getX() == 700) {
                                obj.getArgs().set(4, (short)7);
                                obj.getTestByteOperations().add(new TestByteOperation(0x008, ByteOp.FLAG_EQUALS, 0));
                                obj.getWriteByteOperations().add(new WriteByteOperation(0x008, ByteOp.FLAG_EQUALS, 1));
                            }
                            else { // if(obj.getX() == 1200) {
                                obj.getArgs().set(4, (short)7);
                                obj.getTestByteOperations().add(new TestByteOperation(0x009, ByteOp.FLAG_EQUALS, 0));
                                obj.getWriteByteOperations().add(new WriteByteOperation(0x009, ByteOp.FLAG_EQUALS, 1));
                            }
                        }
                    }
                }

                if(screen.getZoneIndex() == 21 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                    // Pot in retro surface now has coins
                    obj.getArgs().set(0, (short)1);
                    obj.getArgs().set(1, (short)10);
                    obj.getArgs().set(2, (short)277);
                    obj.getArgs().set(3, (short)16);
                }
            }

            if(obj.getTestByteOperations().isEmpty() || obj.getTestByteOperations().get(0).getIndex() != 524) {
                // Pots - update to 1.3 position and then return (no need to add tracking).
                // Note that there is a pot tied to a warp in Illusion which is removed by randomizer (the warp is always active)
                objectContainer.getObjects().add(obj);
                PotMover.updateLocation(obj);
            }
            return rcdByteIndex;
        }
        else if (obj.getId() == 0x96) {
            if(Settings.isFools2021Mode()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if(screen.getZoneIndex() == 2) {
                        if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                            if("Migela".equals(Settings.getCurrentGiant())) {
                                keepObject = false;
                            }
                            else {
                                obj.getArgs().set(4, (short)0); // Activation delay
                            }
                        }
                        else if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                            if("Bado".equals(Settings.getCurrentGiant())) {
                                keepObject = false;
                            }
                            else {
                                obj.getArgs().set(4, (short)0); // Activation delay
                            }
                        }
                        else if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 2) {
                            if("Ledo".equals(Settings.getCurrentGiant())) {
                                keepObject = false;
                            }
                            else {
                                obj.getArgs().set(4, (short)0); // Activation delay
                            }
                        }
                        else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                            if("Abuto".equals(Settings.getCurrentGiant())) {
                                keepObject = false;
                            }
                            else {
                                obj.getArgs().set(4, (short)0); // Activation delay
                            }
                        }
                        else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                            if(obj.getX() == 740) {
                                if("Sakit".equals(Settings.getCurrentGiant())) {
                                    keepObject = false;
                                }
                                else {
                                    obj.getArgs().set(4, (short)0); // Activation delay
                                }
                            }
                            else { //if(obj.getX() == 1120) {
                                if("Ji".equals(Settings.getCurrentGiant())) {
                                    keepObject = false;
                                }
                                else {
                                    obj.getArgs().set(4, (short)0); // Activation delay
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (obj.getId() == 0x08) {
            if(Settings.isFools2021Mode()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if(screen.getZoneIndex() == 2) {
                        if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                            if(obj.getX() == 140) {
                                // Migela
                                if("Migela".equals(Settings.getCurrentGiant())) {
                                    obj.getTestByteOperations().clear();
                                    obj.getTestByteOperations().add(new TestByteOperation(0x165, ByteOp.FLAG_EQUALS, 0));

                                    obj.getWriteByteOperations().clear();
                                    obj.getWriteByteOperations().add(new WriteByteOperation(0x165, ByteOp.ASSIGN_FLAG, 1));
                                    obj.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));
                                }
                                else {
                                    obj.getArgs().set(1, (short)10); // Falling speed
                                }
                            }
                            else {
                                // Futo
                                obj.getArgs().set(1, (short)10); // Falling speed

                                obj.getTestByteOperations().clear();
                                obj.getTestByteOperations().add(new TestByteOperation(0x008, ByteOp.FLAG_EQUALS, 0));

                                obj.getWriteByteOperations().clear();
                                obj.getWriteByteOperations().add(new WriteByteOperation(0x008, ByteOp.ASSIGN_FLAG, 1));

                                AddObject.addExtendingSpikes(obj, 0x008);
                            }
                        }
                        else if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                            if("Bado".equals(Settings.getCurrentGiant())) {
                                obj.getTestByteOperations().clear();
                                obj.getTestByteOperations().add(new TestByteOperation(0x165, ByteOp.FLAG_EQUALS, 0));

                                obj.getWriteByteOperations().clear();
                                obj.getWriteByteOperations().add(new WriteByteOperation(0x165, ByteOp.ASSIGN_FLAG, 1));
                                obj.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));
                            }
                            else {
                                obj.getArgs().set(1, (short)10); // Falling speed
                            }
                        }
                        else if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 2) {
                            if(obj.getY() == 340) {
                                // Make sure not to get the Sakit ankh unlock dais
                                if("Ledo".equals(Settings.getCurrentGiant())) {
                                    obj.getTestByteOperations().clear();
                                    obj.getTestByteOperations().add(new TestByteOperation(0x165, ByteOp.FLAG_EQUALS, 0));

                                    obj.getWriteByteOperations().clear();
                                    obj.getWriteByteOperations().add(new WriteByteOperation(0x165, ByteOp.ASSIGN_FLAG, 1));
                                    obj.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));
                                }
                                else {
                                    obj.getArgs().set(1, (short)10); // Falling speed
                                }
                            }
                        }
                        else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                            if("Abuto".equals(Settings.getCurrentGiant())) {
                                obj.getTestByteOperations().clear();
                                obj.getTestByteOperations().add(new TestByteOperation(0x165, ByteOp.FLAG_EQUALS, 0));

                                obj.getWriteByteOperations().clear();
                                obj.getWriteByteOperations().add(new WriteByteOperation(0x165, ByteOp.ASSIGN_FLAG, 1));
                                obj.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));
                            }
                            else {
                                obj.getArgs().set(1, (short)10); // Falling speed
                            }
                        }
                        else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                            if(obj.getX() == 760) {
                                if("Sakit".equals(Settings.getCurrentGiant())) {
                                    obj.getTestByteOperations().clear();
                                    obj.getTestByteOperations().add(new TestByteOperation(0x165, ByteOp.FLAG_EQUALS, 0));
                                    obj.getTestByteOperations().add(new TestByteOperation(0x164, ByteOp.FLAG_NOT_EQUAL, 1));

                                    obj.getWriteByteOperations().clear();
                                    obj.getWriteByteOperations().add(new WriteByteOperation(0x165, ByteOp.ASSIGN_FLAG, 1));
                                    obj.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));
                                }
                                else {
                                    obj.getArgs().set(1, (short)10); // Falling speed
                                }
                            }
                            else { //if(obj.getX() == 1140) {
                                if("Ji".equals(Settings.getCurrentGiant())) {
                                    obj.getTestByteOperations().clear();
                                    obj.getTestByteOperations().add(new TestByteOperation(0x165, ByteOp.FLAG_EQUALS, 0));
                                    obj.getTestByteOperations().add(new TestByteOperation(0x164, ByteOp.FLAG_NOT_EQUAL, 1));

                                    obj.getWriteByteOperations().clear();
                                    obj.getWriteByteOperations().add(new WriteByteOperation(0x165, ByteOp.ASSIGN_FLAG, 1));
                                    obj.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));
                                }
                                else {
                                    obj.getArgs().set(1, (short)10); // Falling speed
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (obj.getId() == 0x97) {
            if(!obj.getTestByteOperations().isEmpty() && obj.getTestByteOperations().get(0).getIndex() == 524) {
                // Remove broken pot flag check so the warp is just always active.
                obj.getTestByteOperations().remove(0);
            }
            if(objectContainer instanceof Screen) {
                Screen screen = (Screen)objectContainer;
                if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 21 && screen.getScreenIndex() == 0) {
                    if(Settings.isHalloweenMode()) {
                        obj.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0));
                    }
                }
                if(screen.getZoneIndex() == 16 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 2) {
                    if(Settings.isFools2021Mode()) {
                        obj.getArgs().set(0, (short)12);
                        obj.getArgs().set(1, (short)5);
                        obj.getArgs().set(2, (short)1);
                        obj.getArgs().set(3, (short)300);
                        obj.getArgs().set(4, (short)312);
                        obj.getTestByteOperations().add(new TestByteOperation(0x3c8, ByteOp.FLAG_GT, 0));
                    }
                }
            }
        }
        else if (obj.getId() == 0x91) {
            if(Settings.isRandomizeNonBossDoors()) {
                Integer testFlagIndex = null;
                for (int i = 0; i < obj.getTestByteOperations().size(); i++) {
                    TestByteOperation flagTest = obj.getTestByteOperations().get(i);
                    if (flagTest.getIndex() == 0x382) {
                        // Un-disable fairy points during the escape
                        testFlagIndex = i;
                    }
                }
                if(testFlagIndex != null) {
                    obj.getTestByteOperations().remove((int)testFlagIndex);
                }
            }
        }
        else if (obj.getId() == 0x9a) {
            if(Settings.isFools2021Mode()) {
                // Remove Treasures chest falling animation from Guidance.
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if (screen.getZoneIndex() == 0 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        keepObject = false;
                    }
                }
            }
        }
        else if (obj.getId() == 0xb7) {
            // Grail toggle
            if(Settings.isRandomizeNonBossDoors()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if(screen.getZoneIndex() == 8 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                        keepObject = false;
                    }
                }
            }
            if(keepObject && obj.getArgs().get(0) == 1) {
                boolean needEscapeTest = true;
                for (int i = 0; i < obj.getTestByteOperations().size(); i++) {
                    TestByteOperation flagTest = obj.getTestByteOperations().get(i);
                    if (flagTest.getIndex() == 0x382) {
                        needEscapeTest = false;
                        break;
                    }
                }
                if(needEscapeTest) {
                    obj.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0));
                }
            }
        }
        else if (obj.getId() == 0xb4) {
            if(Settings.isRandomizeTrapItems()) {
                if (!obj.getTestByteOperations().isEmpty() && obj.getTestByteOperations().get(0).getIndex() == 522) {
                    // Exploding chest explosion object.
                    keepObject = false;
                }
            }
        }
        else if (obj.getId() == 0x14) {
            if(Settings.isRandomizeTrapItems()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen) objectContainer;
                    if (screen.getZoneIndex() == 5 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
                        // Lemeza Detector part of Inferno Cavern fake Sacred Orb trap
                        if(!Settings.isFools2020Mode()) {
                            keepObject = false;
                        }
                    } else if (screen.getZoneIndex() == 7 && screen.getRoomIndex() == 12 && screen.getScreenIndex() == 0) {
                        // Lemeza Detector part of Twin Labs fake Ankh Jewel trap
                        keepObject = false;
                    }
                }
            }
            if(Settings.isRandomizeTransitionGates()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen) objectContainer;
                    if (screen.getZoneIndex() == 14 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                        for(WriteByteOperation writeByteOperation : obj.getWriteByteOperations()) {
                            if(writeByteOperation.getIndex() == 0x278) {
                                // Lemeza detector for ToG statue
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        else if(obj.getId() == 0x11) {
            // Crusher object
            if(Settings.isHalloweenMode()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen) objectContainer;
                    if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 20 && screen.getScreenIndex() == 0) {
                        keepObject = false;
                    }
                }
            }
        }
        else if(obj.getId() == 0x1f) {
            // Ghost spawner
            if(Settings.isHalloweenMode()) {
                boolean clearFlags = false;
                for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == 0x164) {
                        clearFlags = true;
                        break;
                    }
                }
                if(clearFlags) {
                    obj.getTestByteOperations().clear();
                }
            }
        }
        else if(obj.getId() == 0x30) {
            // Trapdoor
            if(Settings.isFools2020Mode()) {
                if(obj.getObjectContainer() instanceof Screen) {
                    Screen screen = (Screen) obj.getObjectContainer();
                    if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 1) {
                        keepObject = false;
                    }
                }
            }
        }
        else if(obj.getId() == 0x34) {
            // Seal
            if(objectContainer instanceof Screen) {
                Screen screen = (Screen) objectContainer;
                if (screen.getZoneIndex() == 3 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
                    if(Settings.isHalloweenMode()) {
                        keepObject = false; // Don't require Birth Seal for Mulbruk stuff
                    }
                    else {
                        // Seal to wake Mulbruk - set the awake flag so we can skip the conversation that normally sets this flag.
                        WriteByteOperation flagUpdate = new WriteByteOperation();
                        flagUpdate.setIndex(0x391);
                        flagUpdate.setOp(ByteOp.ASSIGN_FLAG);
                        flagUpdate.setValue(1);
                        obj.getWriteByteOperations().add(flagUpdate);
                    }
                }
                if(screen.getZoneIndex() == 18 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
                    if(Settings.isHalloweenMode()) {
                        keepObject = false; // Remove seal for mother ankh
                    }
                    if(Settings.isFeatherlessMode()) {
                        if(obj.getY() == 240) {
                            obj.setY(260);
                            if(obj.getX() == 200) {
                                obj.setX(220);
                            }
                            else {
                                obj.setX(380);
                            }
                        }
                    }
                }
            }
        }
        else if (obj.getId() == 0x02) {
            if(Settings.isRandomizeTrapItems()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen) objectContainer;
                    if (screen.getZoneIndex() == 7 && screen.getRoomIndex() == 12 && screen.getScreenIndex() == 0) {
                        // Bats for fake Ankh Jewel trap
                        keepObject = false;
                    }
                }
            }
            if(Settings.isHalloweenMode()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen) objectContainer;
                    if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 15 && screen.getScreenIndex() == 0) {
                        keepObject = false;
                    }
                }
            }
        }
        else if (obj.getId() == 0x3d) {
            if(Settings.isHalloweenMode()) {
                keepObject = false; // Remove lava rocks in favor of ghosts.
            }
        }
        else if (obj.getId() == 0x4f) {
            if(Settings.isHalloweenMode()) {
                keepObject = false; // Remove Hundun shrine enemy in favor of ghosts.
            }
        }
        else if (obj.getId() == 0xc5) {
            if(Settings.isRandomizeTransitionGates()) {
                obj.getArgs().set(2, (short)10);
            }
        }
        else if (obj.getId() == 0x9b) {
            if(Settings.isFools2020Mode()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if (screen.getZoneIndex() == 7 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 1) {
                        keepObject = false; // Remove shell horn puzzle solve sound
                    }
                }
            }
            if(Settings.isFoolsNpc()) {
                if(obj.getObjectContainer() instanceof Screen) {
                    Screen screen = ((Screen)obj.getObjectContainer());
                    if (screen.getZoneIndex() == 7 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 2) {
                        for (WriteByteOperation flagUpdate : obj.getWriteByteOperations()) {
                            if(flagUpdate.getIndex() == 0x1f0) {
                                // Remove unneeded sound effect on original Little Brother screen
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        else if (obj.getId() == 0x9e) {
            if(Settings.isRandomizeNonBossDoors()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if (screen.getZoneIndex() == 1 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if (testByteOperation.getIndex() == 0x152) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                    if (screen.getZoneIndex() == 6 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if (testByteOperation.getIndex() == 0x1c9) {
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
                for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                    if (testByteOperation.getIndex() == 0x0ae && testByteOperation.getValue() == 0 && ByteOp.FLAG_EQUALS.equals(testByteOperation.getOp())) {
                        keepObject = false;
                        break;
                    }
                }
            }
        }
        else if (obj.getId() == 0x93) {
            if(objectContainer instanceof Screen) {
                Screen screen = (Screen)objectContainer;
                if(screen.getZoneIndex() == 1) {
                    // Graphics for closed surface tents before talking to Xelpud
                    if(screen.getRoomIndex() == 0 && screen.getScreenIndex() == 2
                            || screen.getRoomIndex() == 2 && screen.getScreenIndex() == 0
                            || screen.getRoomIndex() == 2 && screen.getScreenIndex() == 1
                            || screen.getRoomIndex() == 10 && screen.getScreenIndex() == 3) {
                        for (int i = 0; i < obj.getTestByteOperations().size(); i++) {
                            TestByteOperation flagTest = obj.getTestByteOperations().get(i);
                            if (flagTest.getIndex() == 0x145) {
                                // Swap Xelpud first-conversation flag with custom
                                flagTest.setIndex(0xad0);
                            }
                        }
                    }
                }
                else if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
                    if(Settings.isHalloweenMode() || Settings.isFools2020Mode()) {
                        // Graphic for closed Mulbruk door should be removed since the door won't be closed.
                        keepObject = false;
                    }
                }
//                else if(Settings.isFoolsMode() && screen.getZoneIndex() == 4 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
//                    // Bahamut wall graphic that gets removed after the fight.
//                    if(obj.getTestByteOperations().size() == 1) {
//                        TestByteOperation testByteOperation = obj.getTestByteOperations().get(0);
//                        if(testByteOperation.getIndex() == 0x0f9 && ByteOp.FLAG_LTEQ.equals(testByteOperation.getOp())
//                            && testByteOperation.getValue() == (byte)1) {
//                            testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
//                            testByteOperation.setValue((byte)2);
//                        }
//                    }
//                }
            }

            if(Settings.isRandomizeTrapItems()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if (screen.getZoneIndex() == 5 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
                        if(!Settings.isFools2020Mode()) {
                            for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                                if(testByteOperation.getIndex() == 0x1ac) {
                                    // Graphical part of Inferno Cavern fake Sacred Orb trap
                                    obj.setId((short)0x2f);
                                    obj.getArgs().clear();
                                    obj.getArgs().add((short)0); // Interactable any time?
                                    obj.getArgs().add((short)69); // Sacred Orb item
                                    obj.getArgs().add((short)0); // Fake item
                                    obj.getTestByteOperations().get(0).setIndex(2779);

                                    WriteByteOperation writeByteOperation = new WriteByteOperation();
                                    writeByteOperation.setIndex(2779);
                                    writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
                                    writeByteOperation.setValue((byte)1);
                                    obj.getWriteByteOperations().add(writeByteOperation);
                                    break;
                                }
                            }
                        }
                    }
                    else if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 12 && screen.getScreenIndex() == 0) {
                        if(!obj.getTestByteOperations().isEmpty() && obj.getTestByteOperations().get(0).getIndex() == 53) {
                            // Graphical part of Twin Labs fake Ankh Jewel trap
                            obj.setId((short)0x2f);
                            obj.getArgs().clear();
                            obj.getArgs().add((short)0); // Interactable any time?
                            obj.getArgs().add((short)19); // Ankh Jewel item
                            obj.getArgs().add((short)0); // Fake item
                            obj.getTestByteOperations().get(0).setIndex(2780);

                            WriteByteOperation writeByteOperation = new WriteByteOperation();
                            writeByteOperation.setIndex(2780);
                            writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
                            writeByteOperation.setValue((byte)1);
                            obj.getWriteByteOperations().add(writeByteOperation);
                        }
                    }
                }
            }
            if(Settings.isRandomizeTransitionGates()) {
                if (objectContainer instanceof Screen) {
                    Screen screen = (Screen) objectContainer;
                    if (screen.getZoneIndex() == 11 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if(testByteOperation.getIndex() == 0x226) {
                                // Fruit block graphic
                                keepObject = false;
                                break;
                            }
                        }
                    }
                    else if (screen.getZoneIndex() == 13 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if(testByteOperation.getIndex() == 0x226) {
                                // Fruit block graphic
                                keepObject = false;
                                break;
                            }
                        }
                    }
                    else if (screen.getZoneIndex() == 14 && screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if(testByteOperation.getIndex() == 0x226) {
                                // Fruit block graphic
                                keepObject = false;
                                break;
                            }
                        }
                    }
                    else if(screen.getZoneIndex() == 15 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 1) {
                        for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                            if(testByteOperation.getIndex() == 0x2a6) {
                                // Skanda block graphic
                                keepObject = false;
                                break;
                            }
                        }
                    }
                }
            }
            if(Settings.isRandomizeNonBossDoors()) {
                for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                    if(flagTest.getIndex() == 0x15c || flagTest.getIndex() == 0x15d
                            || flagTest.getIndex() == 0x16d || flagTest.getIndex() == 0x16e
                            || flagTest.getIndex() == 0x175 || flagTest.getIndex() == 0x176
                            || flagTest.getIndex() == 0x1bd || flagTest.getIndex() == 0x1be
                            || flagTest.getIndex() == 0x152 || flagTest.getIndex() == 0x153
                            || flagTest.getIndex() == 0x2b9 || flagTest.getIndex() == 0x1d0
                            || flagTest.getIndex() == 0x3b7 || flagTest.getIndex() == 0x1c0) {
                        keepObject = false;
                        break;
                    }
                }
                Screen screen = (Screen) objectContainer;
                if (screen.getZoneIndex() == 6 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                    for (TestByteOperation flagTest : obj.getTestByteOperations()) {
                        if(flagTest.getIndex() == 0x1c9 && flagTest.getValue() == 0) {
                            keepObject = false;
                            break;
                        }
                    }
                }
            }
            if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
                Integer flagToRemove = null;
                for (int i = 0; i < obj.getTestByteOperations().size(); i++) {
                    TestByteOperation flagTest = obj.getTestByteOperations().get(i);
                    if (flagTest.getIndex() == 0x710) {
                        flagToRemove = i;
                    }
                }
                if(flagToRemove != null) {
                    obj.getTestByteOperations().remove((int)flagToRemove);
                }

                for (int i = 0; i < obj.getTestByteOperations().size(); i++) {
                    TestByteOperation flagTest = obj.getTestByteOperations().get(i);
                    if (flagTest.getIndex() == 0x106) {
                        flagTest.setIndex(0xaae);
                        flagTest.setValue((byte)0);
                    }
                }
            }
        }
        else if(obj.getId() == 0x95) {
            // Eye of Divine Retribution
            if(Settings.isFools2021Mode()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen) objectContainer;
                    if (screen.getZoneIndex() == 12) {
                        if(screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                            keepObject = false;
                        }
                        else if(screen.getRoomIndex() == 4 && screen.getScreenIndex() == 1) {
                            keepObject = false;
                        }
                        else if(screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
                            keepObject = false;
                        }
                        else if(screen.getRoomIndex() == 5 && screen.getScreenIndex() == 1) {
                            keepObject = false;
                        }
                        else if(screen.getRoomIndex() == 6 && screen.getScreenIndex() == 0) {
                            keepObject = false;
                        }
                    }
                }
            }
        }
        else if(obj.getId() == 0xa7) {
            if(objectContainer instanceof Screen) {
                Screen screen = (Screen) objectContainer;
                if (screen.getZoneIndex() == 4 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 3) {
                    // Key fairy point for Mr. Fishman shop
                    obj.setX(180);
                    obj.setY(1480);
                }
                else if (screen.getZoneIndex() == 6 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                    keepObject = false;
                }
            }
        }
        else if(obj.getId() == 0xa9) {
            if(Settings.isFools2020Mode()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen) objectContainer;
                    if (screen.getZoneIndex() == 5 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                        if(obj.getX() == 360) {
                            obj.setX(obj.getX() + 60);
                        }
                        else {
                            keepObject = false;
                        }
                    }
                }
            }
        }
        else if(obj.getId() == 0x1e || obj.getId() == 0x50 || obj.getId() == 0x51 || obj.getId() == 0x52 || obj.getId() == 0x53 || obj.getId() == 0x55|| obj.getId() == 0x5c
                || obj.getId() == 0x62  || obj.getId() == 0x64 || obj.getId() == 0x6c || obj.getId() == 0x6e || obj.getId() == 0x7d || obj.getId() == 0x7e
                || obj.getId() == 0x81 || obj.getId() == 0x82 || obj.getId() == 0x83 || obj.getId() == 0x8f) {
            if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen) objectContainer;
                    if(screen.getZoneIndex() == 23) {
                        if(screen.getRoomIndex() == 1) {
                            if(screen.getScreenIndex() == 0) {
                                // HT room 1
                                keepObject = false;
                            }
                        }
                        if(screen.getRoomIndex() == 2) {
                            if(screen.getScreenIndex() == 0 || screen.getScreenIndex() == 1) {
                                // HT rooms 3 and 4
                                keepObject = false;
                            }
                        }
                        if(screen.getRoomIndex() == 5) {
                            if(screen.getScreenIndex() == 0) {
                                // HT room 7
                                keepObject = false;
                            }
                        }
                        if(screen.getRoomIndex() == 6) {
                            if(screen.getScreenIndex() == 0) {
                                // HT room 10
                                keepObject = false;
                            }
                        }
                        if(screen.getRoomIndex() == 8) {
                            if(screen.getScreenIndex() == 0) {
                                // HT room 13
                                keepObject = false;
                            }
                        }
                        if(screen.getRoomIndex() == 9) {
                            if(screen.getScreenIndex() == 0) {
                                // HT room 15
                                keepObject = false;
                            }
                        }
                        if(screen.getRoomIndex() == 10) {
                            if(screen.getScreenIndex() == 1) {
                                // HT room 18
                                keepObject = false;
                            }
                        }
                        if(screen.getRoomIndex() == 15) {
                            if(screen.getScreenIndex() == 0 || screen.getScreenIndex() == 1) {
                                // HT room 25 and 26
                                keepObject = false;
                            }
                        }
                        if(screen.getRoomIndex() == 18) {
                            if(screen.getScreenIndex() == 0) {
                                // HT room 27
                                keepObject = false;
                            }
                        }
                        if(screen.getRoomIndex() == 22) {
                            if(screen.getScreenIndex() == 0) {
                                // HT room 35
                                keepObject = false;
                            }
                        }
                    }
                    else if(screen.getZoneIndex() == 24) {
                        if(obj.getWriteByteOperations().isEmpty()
                                || (screen.getZoneIndex() == 23 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0)) {
                            keepObject = false;
                        }
                    }
                }
            }
        }
        else if (obj.getId() == 0x8e) {
            // Mushussu
            if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
                if(obj.getObjectContainer() instanceof Screen) {
                    Screen screen = (Screen) obj.getObjectContainer();
                    if(screen.getZoneIndex() == 24) {
                        keepObject = false;
                    }
                }
            }
        }
        else if(obj.getId() == 0x6f) {
            if(Settings.isHalloweenMode()) {
                // Ban Medusa heads in favor of ghosts
                keepObject = false;
            }
        }
        else if(obj.getId() == 0xc4) {
            if(objectContainer instanceof Screen) {
                Screen screen = (Screen) objectContainer;
                if (screen.getZoneIndex() == 6 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                    if(Settings.isRandomizeTransitionGates()) {
                        if(obj.getArgs().get(0) == 18) {
                            keepObject = false;
                        }
                        else if(obj.getArgs().get(0) == 9) {
                            TestByteOperation testByteOperation = obj.getTestByteOperations().get(0);
                            testByteOperation.setIndex(0x382);
                            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                            testByteOperation.setValue((byte)0);
                        }
                    }
                }
                else if (screen.getZoneIndex() == 7) {
                    if(screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        // Twin Labyrinths ladder that normally goes to Ellmac's room
                        if(Settings.isRandomizeBosses()) {
                            obj.getArgs().set(0, (short)6);
                            obj.getArgs().set(1, (short)9);
                            obj.getArgs().set(2, (short)1);
                            obj.getArgs().set(3, (short)300);
                            obj.getArgs().set(4, (short)380);
                        }
                    }
                    else if(screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                        if(Settings.isRandomizeTransitionGates()) {
                            if(obj.getArgs().get(0) == 18) {
                                keepObject = false;
                            }
                            else if(obj.getArgs().get(0) == 9) {
                                TestByteOperation testByteOperation = obj.getTestByteOperations().get(0);
                                testByteOperation.setIndex(0x382);
                                testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                                testByteOperation.setValue((byte)0);
                            }
                        }
                        else {
                            // Don't change to true shrine until you have Feather, since the old shrine has more requirement options.
                            if (obj.getTestByteOperations().get(0).getIndex() == 258) {
                                if(ByteOp.FLAG_EQUALS.equals(obj.getTestByteOperations().get(0).getOp())) {
                                    keepObject = false;
                                }
                                else {
                                    obj.getTestByteOperations().clear();
                                }
                            }
                        }
                    }
                }
                else if(screen.getZoneIndex() == 8) {
                    if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 3) {
                        if(Settings.isRandomizeTransitionGates()) {
                            if(obj.getArgs().get(0) == 18) {
                                keepObject = false;
                            }
                            else if(obj.getArgs().get(0) == 9) {
                                TestByteOperation testByteOperation = obj.getTestByteOperations().get(0);
                                testByteOperation.setIndex(0x382);
                                testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                                testByteOperation.setValue((byte)0);
                            }
                        }
                        else {
                            // Don't change to true shrine until you have Feather, since the old shrine has more requirement options.
                            if (ByteOp.FLAG_EQUALS.equals(obj.getTestByteOperations().get(0).getOp())) {
                                TestByteOperation featherCheck = new TestByteOperation();
                                featherCheck.setIndex(182);
                                featherCheck.setOp(ByteOp.FLAG_EQUALS);
                                featherCheck.setValue((byte) 2);
                                obj.getTestByteOperations().add(featherCheck);
                            }
                        }
                    }
                    else if(screen.getRoomIndex() == 5 && screen.getScreenIndex() == 3) {
                        if(Settings.isRandomizeTransitionGates()) {
                            if(obj.getArgs().get(0) == 18) {
                                keepObject = false;
                            }
                            else if(obj.getArgs().get(0) == 9) {
                                TestByteOperation testByteOperation = obj.getTestByteOperations().get(0);
                                testByteOperation.setIndex(0x382);
                                testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                                testByteOperation.setValue((byte)0);
                            }
                        }
                    }
                }
                else if (screen.getZoneIndex() == 13 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                    if(Settings.isRandomizeTransitionGates()) {
                        // Remove the Eden flag, since this door may not lead to Eden.
                        Integer flagIndexToRemove = null;
                        for(int i = 0; i < obj.getTestByteOperations().size(); i++) {
                            TestByteOperation testByteOperation = obj.getTestByteOperations().get(i);
                            if(testByteOperation.getIndex() == 0x226) {
                                flagIndexToRemove = i;
                                break;
                            }
                        }
                        if(flagIndexToRemove != null) {
                            obj.getTestByteOperations().remove((int)flagIndexToRemove);
                        }
                    }
                }
            }
        }
        else if (obj.getId() == 0xa3) {
            if(Settings.isAlternateMotherAnkh()) {
                for (TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                    if (testByteOperation.getIndex() == 0x0fe) {
                        keepObject = false;
                        break;
                    }
                }
            }
        }
        else if (obj.getId() == 0xc0) {
            // Mother ankh
            if(Settings.isHalloweenMode()) {
               keepObject = false;
            }
            else {
                if(Settings.isAlternateMotherAnkh()) {
                    obj.setId((short)0x2e);
                    obj.getArgs().set(0, (short)8);
                    obj.getWriteByteOperations().get(0).setValue((byte) 1);
                    obj.getWriteByteOperations().get(1).setValue((byte) 2);
                    obj.setY(obj.getY() + 60);
                    AddObject.addAutosave((Screen)obj.getObjectContainer(), obj.getX(), obj.getY(), 231,
                            Arrays.asList(new TestByteOperation(0x2e0, ByteOp.FLAG_EQUALS, 3),
                                    new TestByteOperation(0x0fe, ByteOp.FLAG_LTEQ, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                }
                else {
                    AddObject.addAutosave((Screen)obj.getObjectContainer(), obj.getX(), obj.getY() + 60, 231,
                            Arrays.asList(new TestByteOperation(0x2e0, ByteOp.FLAG_EQUALS, 3),
                                    new TestByteOperation(0x0fe, ByteOp.FLAG_LTEQ, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                }
//                for(int i = 1; i <= 23; i++) {
//                    obj.getArgs().set(i, (short) 1);
//                }
            }
        }
        else if (obj.getId() == 0xc7) {
            // Escape screenshake
            if(Settings.isScreenshakeDisabled()) {
               keepObject = false;
            }
        }

        if(keepObject) {
            objectContainer.getObjects().add(obj);
            GameDataTracker.addObject(obj);
        }
//        else {
//            FileUtils.log("Object excluded from rcd");
//        }
        return rcdByteIndex;
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

    private static TestByteOperation getTestByteOperation(byte[] rcdBytes, int rcdByteIndex) {
        TestByteOperation testByteOperation = new TestByteOperation();

        testByteOperation.setIndex(getField(rcdBytes, rcdByteIndex, 2).getShort());
        rcdByteIndex += 2;

        testByteOperation.setValue(rcdBytes[rcdByteIndex]);
        rcdByteIndex += 1;

        testByteOperation.setOp(ByteOp.getTestOp((int)rcdBytes[rcdByteIndex]));
//        rcdByteIndex += 1;

        return testByteOperation;
    }

    private static WriteByteOperation getWriteByteOperation(byte[] rcdBytes, int rcdByteIndex) {
        WriteByteOperation writeByteOperation = new WriteByteOperation();

        writeByteOperation.setIndex(getField(rcdBytes, rcdByteIndex, 2).getShort());
        rcdByteIndex += 2;

        writeByteOperation.setValue(rcdBytes[rcdByteIndex]);
        rcdByteIndex += 1;

        writeByteOperation.setOp(ByteOp.getWriteOp((int)rcdBytes[rcdByteIndex]));
//        rcdByteIndex += 1;

        return writeByteOperation;
    }

    public static List<Zone> getRcdScriptInfo() throws Exception {
        String mapPath = String.format("%s/data/mapdata", Settings.getLaMulanaBaseDir());

        byte[] rcdBytes = FileUtils.getBytes("script.rcd.bak");
        int rcdByteIndex = 2; // Seems we skip the first two bytes?

        List<Zone> zones = new ArrayList<>();
        PotMover.init();
        for (int zoneIndex = 0; zoneIndex < 26; zoneIndex++) {
            Zone zone = new Zone();
            zone.setZoneIndex(zoneIndex);

            byte nameLength = getField(rcdBytes, rcdByteIndex, 1).get();
            rcdByteIndex += 1;
            short zoneObjectCount = getField(rcdBytes, rcdByteIndex, 2).getShort();
            rcdByteIndex += 2;

            zone.setName(new String(getByteArraySlice(rcdBytes, rcdByteIndex, nameLength), "UTF-16BE"));
            rcdByteIndex += (int) nameLength;

            for (int i = 0; i < zoneObjectCount; i++) {
                rcdByteIndex = addObject(zone, rcdBytes, rcdByteIndex, false);
            }


            byte[] msdBytes = FileUtils.getBytes(String.format("%s/map%02d.msd", mapPath, zoneIndex));
            int msdByteIndex = 0;
            while (true) {
                short frames = getField(msdBytes, msdByteIndex, 2).getShort();
                msdByteIndex += 2;
                if (frames == 0) {
                    break;
                }
                msdByteIndex += frames * 2;
            }

            byte rooms = msdBytes[msdByteIndex + 2];
            msdByteIndex += 3;

            for (int roomIndex = 0; roomIndex < rooms; roomIndex++) {
                Room room = new Room();
                room.setZone(zone);
                room.setZoneIndex(zoneIndex);
                room.setRoomIndex(roomIndex);
                short roomObjCount = getField(rcdBytes, rcdByteIndex, 2).getShort();
                rcdByteIndex += 2;

                for (int roomObjectIndex = 0; roomObjectIndex < roomObjCount; roomObjectIndex++) {
                    rcdByteIndex = addObject(room, rcdBytes, rcdByteIndex, false);
                }

                msdByteIndex += 1; // unwanted byte for use boss graphics

                room.setNumberOfLayers(msdBytes[msdByteIndex]);
                msdByteIndex += 1;

                room.setPrimeLayerNumber(msdBytes[msdByteIndex]);
                msdByteIndex += 1;

                room.setHitMaskWidth(getField(msdBytes, msdByteIndex, 2).getShort());
                msdByteIndex += 2;

                room.setHitMaskHeight(getField(msdBytes, msdByteIndex, 2).getShort());
                msdByteIndex += 2;

                if(Settings.isFools2021Mode() && zoneIndex == 13) {
                    for(int i = 0; i < room.getHitMaskWidth() * room.getHitMaskHeight(); i ++) {
                        byte hitMask = msdBytes[msdByteIndex + i];
                        if(hitMask == 0x05) {
                            msdBytes[msdByteIndex + i] = 0x10;
                        }
                        if(hitMask == 0x06) {
                            msdBytes[msdByteIndex + i] = 0x11;
                        }
                        if(hitMask == 0x07) {
                            msdBytes[msdByteIndex + i] = 0x12;
                        }
                        if(hitMask == 0x08) {
                            msdBytes[msdByteIndex + i] = 0x13;
                        }
                        if(hitMask == 0x09) {
                            msdBytes[msdByteIndex + i] = 0x14;
                        }
                        if(hitMask == 0x0a) {
                            msdBytes[msdByteIndex + i] = 0x15;
                        }
                        if(hitMask == 0x0b) {
                            msdBytes[msdByteIndex + i] = 0x16;
                        }
                        if(hitMask == 0x0c) {
                            msdBytes[msdByteIndex + i] = 0x17;
                        }
                        if(hitMask == 0x0d) {
                            msdBytes[msdByteIndex + i] = 0x18;
                        }
                    }
                    msdByteIndex += room.getHitMaskWidth() * room.getHitMaskHeight();
                }
                else {
                    msdByteIndex += room.getHitMaskWidth() * room.getHitMaskHeight();
                }

                for (int layerIndex = 0; layerIndex < room.getNumberOfLayers(); layerIndex++) {
                    short layerWidth = getField(msdBytes, msdByteIndex, 2).getShort();
                    msdByteIndex += 2;

                    short layerHeight = getField(msdBytes, msdByteIndex, 2).getShort();
                    msdByteIndex += 2;

                    byte sublayers = msdBytes[msdByteIndex];
                    msdByteIndex += 1;

                    if (layerIndex == (int) room.getPrimeLayerNumber()) {
                        room.setTileWidth(layerWidth);
                        room.setTileHeight(layerHeight);

                        room.setScreenWidth((int)room.getTileWidth() / 32);
                        room.setScreenHeight((int)room.getTileHeight() / 24);
                        room.setNumberOfScreens(room.getScreenWidth() * room.getScreenHeight());
                    }

                    msdByteIndex += sublayers * layerWidth * layerHeight * 2;
                }

                for (int screenIndex = 0; screenIndex < room.getNumberOfScreens(); screenIndex++) {
                    Screen screen = new Screen();
                    screen.setZone(zone);
                    screen.setZoneIndex(zoneIndex);
                    screen.setRoomIndex(roomIndex);
                    screen.setScreenIndex(screenIndex);
                    registerScreen(screen);

                    byte screenNameLength = rcdBytes[rcdByteIndex];
                    rcdByteIndex += 1;

                    short screenObjectCount = getField(rcdBytes, rcdByteIndex, 2).getShort();
                    rcdByteIndex += 2;

                    byte noPositionScreenObjectCount = rcdBytes[rcdByteIndex];
                    rcdByteIndex += 1;

                    addCustomNoPositionObjects(screen, zoneIndex, roomIndex, screenIndex);
                    for (int noPositionScreenObjectIndex = 0; noPositionScreenObjectIndex < noPositionScreenObjectCount; noPositionScreenObjectIndex++) {
                        rcdByteIndex = addObject(screen, rcdBytes, rcdByteIndex, false);
                    }

                    addCustomPositionObjects(screen, zoneIndex, roomIndex, screenIndex);
                    for (int screenObjectIndex = 0; screenObjectIndex < (screenObjectCount - noPositionScreenObjectCount); screenObjectIndex++) {
                        rcdByteIndex = addObject(screen, rcdBytes, rcdByteIndex, true);
                    }

                    screen.setName(new String(getByteArraySlice(rcdBytes, rcdByteIndex, screenNameLength), "UTF-16BE"));
                    rcdByteIndex += (int) screenNameLength;

                    for (int exitIndex = 0; exitIndex < 4; exitIndex++) {
                        ScreenExit screenExit = new ScreenExit();

                        screenExit.setZoneIndex(rcdBytes[rcdByteIndex]);
                        rcdByteIndex += 1;

                        screenExit.setRoomIndex(rcdBytes[rcdByteIndex]);
                        rcdByteIndex += 1;

                        screenExit.setScreenIndex(rcdBytes[rcdByteIndex]);
                        rcdByteIndex += 1;

                        screen.getScreenExits().add(screenExit);
                    }
                    updateScreenExits(screen);

                    if(zoneIndex == 1) {
                        if(roomIndex == 2 && screenIndex == 1) {
                            AddObject.setXelpudScreen(screen);
                        }
                    }
                    else if(zoneIndex == 3) {
                        if(roomIndex == 3 && screenIndex == 0) {
                            AddObject.setMulbrukScreen(screen);
                        }
                    }
                    else if(zoneIndex == 8) {
                        if(roomIndex == 0 && screenIndex == 1) {
                            AddObject.setDimensionalExitScreen(screen);
                        }
                    }

                    room.getScreens().add(screen);
                }

                zone.getRooms().add(room);
            }
            zones.add(zone);
            if(Settings.isFools2021Mode() && zoneIndex == 13) {
                Settings.goddessMsdBytes = msdBytes;
            }
        }
        PotMover.addRemovedPots();
        return zones;
    }

    private static void registerScreen(Screen screen) {
        if(screen.getZoneIndex() == 17 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
            GameDataTracker.putTransitionScreen("Transition: Dimensional D1", screen);
        }
    }

    private static void updateScreenExits(Screen screen) {
        if(Settings.isRandomizeTransitionGates()
                && (screen.getZoneIndex() == 2 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 2
                || screen.getZoneIndex() == 19 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 1)) {
            ScreenExit screenExit = screen.getScreenExits().get(2);
            screenExit.setZoneIndex((byte)-1);
            screenExit.setRoomIndex((byte)-1);
            screenExit.setScreenIndex((byte)-1);
        }

//        if(Settings.isFoolsMode()) {
//            if(screen.getZoneIndex() == 4 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
//                ScreenExit screenExit = screen.getScreenExits().get(3);
//                screenExit.setZoneIndex((byte)-1);
//                screenExit.setRoomIndex((byte)-1);
//                screenExit.setScreenIndex((byte)-1);
//            }
//        }
        if(Settings.isFools2021Mode()) {
            // Transition to Ellmac
            if(screen.getZoneIndex() == 4 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                ScreenExit screenExit = screen.getScreenExits().get(3);
                screenExit.setZoneIndex((byte)3);
                screenExit.setRoomIndex((byte)8);
                screenExit.setScreenIndex((byte)0);
            }
        }

        if(Settings.isRandomizeBosses()) {
            if(screen.getZoneIndex() == 0) {
                if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                    ScreenExit screenExit = screen.getScreenExits().get(2);
                    screenExit.setZoneIndex((byte)5);
                    screenExit.setRoomIndex((byte)8);
                    screenExit.setScreenIndex((byte)1);
                }
                else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                    ScreenExit screenExit = screen.getScreenExits().get(0);
                    screenExit.setZoneIndex((byte)5);
                    screenExit.setRoomIndex((byte)8);
                    screenExit.setScreenIndex((byte)0);
                }
            }
            else if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                ScreenExit screenExit = screen.getScreenExits().get(2);
                screenExit.setZoneIndex((byte)3);
                screenExit.setRoomIndex((byte)4);
                screenExit.setScreenIndex((byte)2);
            }
            else if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                ScreenExit screenExit = screen.getScreenExits().get(0);
                screenExit.setZoneIndex((byte)6);
                screenExit.setRoomIndex((byte)9);
                screenExit.setScreenIndex((byte)1);
            }
            else if(screen.getZoneIndex() == 5) {
                if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                    ScreenExit screenExit = screen.getScreenExits().get(2);
                    screenExit.setZoneIndex((byte)0);
                    screenExit.setRoomIndex((byte)8);
                    screenExit.setScreenIndex((byte)1);
                }
                else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                    ScreenExit screenExit = screen.getScreenExits().get(0);
                    screenExit.setZoneIndex((byte)0);
                    screenExit.setRoomIndex((byte)8);
                    screenExit.setScreenIndex((byte)0);
                }
            }
        }

        if(!LocationCoordinateMapper.isSurfaceStart() || Settings.getCurrentStartingLocation() == 22) {
            for(ScreenExit screenExit : screen.getScreenExits()) {
                if(screenExit.getZoneIndex() == -1 && screenExit.getRoomIndex() == -1 && screenExit.getScreenIndex() == -1) {
                    screenExit.setZoneIndex(LocationCoordinateMapper.getStartingZone());
                    screenExit.setRoomIndex(LocationCoordinateMapper.getStartingRoom());
                    screenExit.setScreenIndex(LocationCoordinateMapper.getStartingScreen());
                }
            }
        }
        if(Settings.getCurrentStartingLocation() == 22) {
            for(ScreenExit screenExit : screen.getScreenExits()) {
                if(screenExit.getZoneIndex() == 1) {
                    screenExit.setZoneIndex((byte)22);
                }
            }
        }

        if(Settings.isFools2020Mode()) {
            if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 1) {
                // Prevent raindropping to the Lamp of Time shop
                ScreenExit downExit = screen.getScreenExits().get(2);
                downExit.setZoneIndex((byte)7);
                downExit.setRoomIndex((byte)4);
                downExit.setScreenIndex((byte)1);
            }
        }
        if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
            if(screen.getZoneIndex() == 23) {
                if(screen.getRoomIndex() == 0) {
                    if(screen.getScreenIndex() == 0) {
                        // Entrance, ban raindrop skip
                        // H1
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)3);
                        downExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 1) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 1, no change
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 2
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)1);
                        upExit.setScreenIndex((byte)1);

                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)1);
                        downExit.setScreenIndex((byte)1);
                    }
                }
                else if(screen.getRoomIndex() == 2) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 4
                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)2);
                        rightExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)2);
                        leftExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 3
                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)2);
                        rightExit.setScreenIndex((byte)1);

                        // H1
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)3);
                        downExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 3) {
                    if(screen.getScreenIndex() == 0) {
                        // H1
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)1);
                        upExit.setScreenIndex((byte)1);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)3);
                        rightExit.setScreenIndex((byte)0);

                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)3);
                        downExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)3);
                        leftExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 4) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 6
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)4);
                        upExit.setScreenIndex((byte)0);

                        // H1
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)3);
                        downExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 5
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)4);
                        upExit.setScreenIndex((byte)1);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)2);
                        rightExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 5) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 7
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)5);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)5);
                        leftExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 8
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)5);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)5);
                        rightExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)5);
                        leftExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 6) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 10
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)6);
                        upExit.setScreenIndex((byte)1);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)6);
                        leftExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 9
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)6);
                        upExit.setScreenIndex((byte)1);

                        // H2
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)7);
                        downExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 7) {
                    if(screen.getScreenIndex() == 0) {
                        // H2
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)6);
                        upExit.setScreenIndex((byte)1);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)7);
                        rightExit.setScreenIndex((byte)0);

                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)7);
                        downExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)7);
                        leftExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 8) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 13
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)8);
                        upExit.setScreenIndex((byte)0);

                        // H2
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)7);
                        downExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 14
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)8);
                        upExit.setScreenIndex((byte)1);

                        // H3
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)11);
                        downExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 9) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 15
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)9);
                        upExit.setScreenIndex((byte)0);

                        // H3
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)11);
                        downExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 16
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)9);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)9);
                        rightExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 10) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 17
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)9);
                        upExit.setScreenIndex((byte)1);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)9);
                        rightExit.setScreenIndex((byte)1);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)9);
                        leftExit.setScreenIndex((byte)1);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 18
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)10);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)10);
                        rightExit.setScreenIndex((byte)0);

                        // H3
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)11);
                        downExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 11) {
                    if(screen.getScreenIndex() == 0) {
                        // H3
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)8);
                        upExit.setScreenIndex((byte)1);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)11);
                        rightExit.setScreenIndex((byte)0);

                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)11);
                        downExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)11);
                        leftExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 12) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 20, no change
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 22
                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)12);
                        rightExit.setScreenIndex((byte)1);

                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)12);
                        downExit.setScreenIndex((byte)1);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)12);
                        leftExit.setScreenIndex((byte)1);
                    }
                }
                else if(screen.getRoomIndex() == 13) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 21
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)13);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)13);
                        rightExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)13);
                        leftExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 14) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 24
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)14);
                        upExit.setScreenIndex((byte)1);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)14);
                        leftExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 23
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)14);
                        upExit.setScreenIndex((byte)1);

                        // H4
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)16);
                        downExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 15) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 25
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)21);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)14);
                        rightExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)14);
                        leftExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 26
                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)15);
                        rightExit.setScreenIndex((byte)1);

                        // H4
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)15);
                        downExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)14);
                        leftExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 16) {
                    if(screen.getScreenIndex() == 0) {
                        // H4
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)12);
                        upExit.setScreenIndex((byte)1);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)16);
                        rightExit.setScreenIndex((byte)0);

                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)16);
                        downExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)16);
                        leftExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 17) {
                    if(screen.getScreenIndex() == 0) {
                        // H4
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)21);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)17);
                        rightExit.setScreenIndex((byte)0);

                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)17);
                        downExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)17);
                        leftExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 18) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 27
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)18);
                        upExit.setScreenIndex((byte)0);

                        // H5
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)17);
                        downExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 28
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)18);
                        upExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 19) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 29
                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)23);
                        rightExit.setRoomIndex((byte)19);
                        rightExit.setScreenIndex((byte)0);

                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)19);
                        downExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)19);
                        leftExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 20) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 30
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)20);
                        upExit.setScreenIndex((byte)0);

                        // H5
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)17);
                        downExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 21) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 33
                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)24);
                        rightExit.setRoomIndex((byte)2);
                        rightExit.setScreenIndex((byte)1);

                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)24);
                        downExit.setRoomIndex((byte)2);
                        downExit.setScreenIndex((byte)1);
                    }
                }
                else if(screen.getRoomIndex() == 22) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 35
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)21);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)21);
                        downExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)23);
                        leftExit.setRoomIndex((byte)21);
                        leftExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 34
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)21);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)21);
                        downExit.setScreenIndex((byte)0);
                    }
                }
            }
            else if(screen.getZoneIndex() == 24) {
                if(screen.getRoomIndex() == 0) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 11
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)6);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)24);
                        rightExit.setRoomIndex((byte)0);
                        rightExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)24);
                        leftExit.setRoomIndex((byte)0);
                        leftExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 12
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)24);
                        upExit.setRoomIndex((byte)0);
                        upExit.setScreenIndex((byte)0);

                        // H2
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)7);
                        downExit.setScreenIndex((byte)0);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)24);
                        leftExit.setRoomIndex((byte)0);
                        leftExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 1) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 19
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)23);
                        upExit.setRoomIndex((byte)10);
                        upExit.setScreenIndex((byte)1);

                        // H3
                        ScreenExit downExit = screen.getScreenExits().get(2);
                        downExit.setZoneIndex((byte)23);
                        downExit.setRoomIndex((byte)11);
                        downExit.setScreenIndex((byte)0);
                    }
                }
                else if(screen.getRoomIndex() == 2) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 31
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)24);
                        upExit.setRoomIndex((byte)2);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)24);
                        rightExit.setRoomIndex((byte)2);
                        rightExit.setScreenIndex((byte)0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 32
                        ScreenExit upExit = screen.getScreenExits().get(0);
                        upExit.setZoneIndex((byte)24);
                        upExit.setRoomIndex((byte)2);
                        upExit.setScreenIndex((byte)0);

                        ScreenExit rightExit = screen.getScreenExits().get(1);
                        rightExit.setZoneIndex((byte)24);
                        rightExit.setRoomIndex((byte)2);
                        rightExit.setScreenIndex((byte)1);

                        ScreenExit leftExit = screen.getScreenExits().get(3);
                        leftExit.setZoneIndex((byte)24);
                        leftExit.setRoomIndex((byte)2);
                        leftExit.setScreenIndex((byte)1);
                    }
                }
            }
        }
    }

    private static void addCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == LocationCoordinateMapper.getStartingZone()
                && roomIndex == LocationCoordinateMapper.getStartingRoom()
                && screenIndex == LocationCoordinateMapper.getStartingScreen()) {
            if(zoneIndex == 0) {
                GameDataTracker.setCustomShop(AddObject.addGuidanceShop(screen));
            }
            else if(zoneIndex == 2) {
                GameDataTracker.setCustomShop(AddObject.addMausoleumShop(screen));
            }
            else if(zoneIndex == 5) {
                GameDataTracker.setCustomShop(AddObject.addInfernoShop(screen));
            }
            else if(zoneIndex == 7) {
                if(Settings.getCurrentStartingLocation() == -7) {
                    GameDataTracker.setCustomShop(AddObject.addTwinLabsBackShop(screen));
                }
                else {
                    GameDataTracker.setCustomShop(AddObject.addTwinLabsFrontShop(screen));
                }
            }
            else if(zoneIndex == 8) {
                GameDataTracker.setCustomShop(AddObject.addEndlessShop(screen));
            }
            else if(zoneIndex == 10) {
                GameDataTracker.setCustomShop(AddObject.addIllusionShop(screen));
            }
            else if(zoneIndex == 11) {
                GameDataTracker.setCustomShop(AddObject.addGraveyardShop(screen));
            }
            else if(zoneIndex == 13) {
                GameDataTracker.setCustomShop(AddObject.addGoddessShop(screen));
            }
            else if(zoneIndex == 14) {
                GameDataTracker.setCustomShop(AddObject.addRuinShop(screen));
            }
            else if(zoneIndex == 16) {
                GameDataTracker.setCustomShop(AddObject.addBirthStartStuff(screen));
            }
            else if(zoneIndex == 21) {
                // Retro Surface start.
                GameObject grailTablet = AddObject.addSpecialGrailTablet(screen);
                AddObject.addHotspring(grailTablet);
                GameDataTracker.setCustomShop(AddObject.addRetroSurfaceShop(screen));
            }
            AddObject.addStartingItems(screen);
        }

        if(zoneIndex == 0) {
            if(roomIndex == 0 && screenIndex == 0) {
                if(Settings.isFools2021Mode()) {
                    // Treasures room
                    AddObject.addWarpDoor(screen, 320, 160, 3, 6, 0, 460, 152,
                            Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_GTEQ, 1)));
                    AddObject.addPushableBlock(screen, 320, 180, Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 1)));
                    AddObject.addPushableBlock(screen, 320, 180, Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 2)));
                    AddObject.addPushableBlock(screen, 320, 180, Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 3)));
                    AddObject.addPushableBlock(screen, 320, 180, Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 4)));

                    // After the falling block sequence, the blocks can be replaced by stationary ones.
                    AddObject.addPushableBlock(screen, 320, 200, Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 5), new TestByteOperation(0x00d, ByteOp.FLAG_EQUALS, 0)));
                    AddObject.addPushableBlock(screen, 320, 240, Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 5), new TestByteOperation(0x00d, ByteOp.FLAG_EQUALS, 0)));
                    AddObject.addPushableBlock(screen, 320, 280, Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 5), new TestByteOperation(0x00d, ByteOp.FLAG_EQUALS, 0)));
                    AddObject.addPushableBlock(screen, 320, 320, Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 5), new TestByteOperation(0x00d, ByteOp.FLAG_EQUALS, 0)));
                }
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                // Room above Amphisbaena
                if(Settings.isRandomizeBosses()) {
                    // Transition to Viy
                    AddObject.addSpecialTransitionWarp(screen, 5);
                }
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                // Amphisbaena boss room
                if(Settings.isRandomizeBosses()) {
                    AddObject.addSpecialTransitionGate(screen, 5);
                    // todo: test boss swap + boss checkpoint
                    AddObject.addAutosave(screen, 300, 880, 41,
                            Arrays.asList(new TestByteOperation(0x1b4, ByteOp.FLAG_EQUALS, 4),
                                    new TestByteOperation(0x0f6, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                }
                if (Settings.isFools2021Mode()) {
                    AddObject.addEyeOfDivineRetribution(screen, 300, 500);
                    AddObject.addHitbox(screen, 0, 480, 32, 24, new ArrayList<>(0), Arrays.asList(new WriteByteOperation(0x000a, ByteOp.ADD_FLAG, 1)));

                    for (int i = 1; i <= 10; i++) {
                        AddObject.addAmphisbaenaAnkh(screen, 300, 880, 32 * (i + 1),
                                Arrays.asList(new TestByteOperation(0x133, ByteOp.FLAG_EQUALS, 5),
                                        new TestByteOperation(0x354, ByteOp.FLAG_EQUALS, i)));
                    }
                }
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 300, 880, 41,
                            Arrays.asList(new TestByteOperation(0x133, ByteOp.FLAG_EQUALS, 5),
                                    new TestByteOperation(0x0f6, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                }
            }
        }
        else if(zoneIndex == 1) {
            if(roomIndex == 2 && screenIndex == 1) {
                if(!LocationCoordinateMapper.isSurfaceStart()) {
                    AddObject.addSurfaceGrailTablet(screen);
                }
            }

            if(roomIndex == 4 && screenIndex == 2) {
                AddObject.addSurfaceCoverDetector(screen);
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                if(Settings.isAlternateMotherAnkh()) {
                    AddObject.addMotherAnkhJewelItemGive(screen);
                }
            }
            else if(roomIndex == 11 && screenIndex == 1) {
                if(!LocationCoordinateMapper.isSurfaceStart() && !Settings.isRandomizeTransitionGates()) {
                    GameObject warp = AddObject.addWarp(screen, 1220, 340, 4, 7, 0, 0, 0, 20, 312);

                    TestByteOperation warpTest = new TestByteOperation();
                    warpTest.setIndex(0x414);
                    warpTest.setValue((byte) 0);
                    warpTest.setOp(ByteOp.FLAG_EQUALS);
                    warp.getTestByteOperations().add(warpTest);
                }
            }
        }
        else if(zoneIndex == 2) {
            if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addHardmodeToggleWeights(screen);
            }
            if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addZebuDais(screen);
            }
            if(roomIndex == 8 && screenIndex == 1) {
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 900, 120, 75,
                            Arrays.asList(new TestByteOperation(0x164, ByteOp.FLAG_EQUALS, 1),
                                    new TestByteOperation(0x0f7, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                }
            }
        }
        else if(zoneIndex == 3) {
            if(roomIndex == 4 && screenIndex == 3) {
                if(Settings.isFools2021Mode()) {
                    AddObject.addPot(screen, 40, 480, 1, new ArrayList<>(0));
                    AddObject.addPot(screen, 40, 640, 1, new ArrayList<>(0));
                    AddObject.addPot(screen, 40, 800, 1, new ArrayList<>(0));
                }
            }
            else if(roomIndex == 4 && screenIndex == 5) {
                if(Settings.isFools2021Mode()) {
                    AddObject.addWarp(screen, 1280, 480, 32, 24, 12, 8, 0, 20, 232)
                            .getTestByteOperations().add(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, (byte)0));
                }
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                // Ellmac boss room
                if(Settings.isRandomizeBosses()) {
                    GameObject warp = AddObject.addWarp(screen, 0, 420, 32, 4, 3, 4, 2, 100, 160);

                    TestByteOperation warpTest = new TestByteOperation();
                    warpTest.setIndex(0x0f8);
                    warpTest.setValue((byte) 2);
                    warpTest.setOp(ByteOp.FLAG_NOT_EQUAL);
                    warp.getTestByteOperations().add(warpTest);
                }
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 400, 320, 104,
                            Arrays.asList(new TestByteOperation(0x178, ByteOp.FLAG_EQUALS, 5),
                                    new TestByteOperation(0x0f8, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                }
            }
        }
        else if(zoneIndex == 4) {
            if(roomIndex == 4 && screenIndex == 0) {
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 380, 340, 136,
                            Arrays.asList(new TestByteOperation(0x19f, ByteOp.FLAG_EQUALS, 1),
                                    new TestByteOperation(0x199, ByteOp.FLAG_EQUALS, 1),
                                    new TestByteOperation(0x0f9, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                }
            }
            else if (roomIndex == 8 && screenIndex == 1) {
                if (Settings.isFools2021Mode()) {
                    // Transition to Ellmac
                    AddObject.addWarp(screen, 0, 440 + 480, 32, 4, 3, 8, 0, 360, 40)
                            .getTestByteOperations().add(new TestByteOperation(0x0f8, ByteOp.FLAG_NOT_EQUAL, (byte) 2));
                }
            }
        }
        else if(zoneIndex == 5) {
            if(roomIndex == 8 && screenIndex == 0) {
                if(Settings.isRandomizeBosses()) {
                    AddObject.addSpecialTransitionWarp(screen, 0);
                }
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                // Viy boss room
                if(Settings.isRandomizeBosses()) {
                    AddObject.addSpecialTransitionGate(screen, 0);
                    // todo: test boss swap + boss checkpoint
                    AddObject.addAutosave(screen, 460, 560, 149,
                            Arrays.asList(new TestByteOperation(0x133, ByteOp.FLAG_EQUALS, 5),
                                    new TestByteOperation(0x0fa, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                }
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 460, 560, 149,
                            Arrays.asList(new TestByteOperation(0x1b4, ByteOp.FLAG_EQUALS, 4),
                                    new TestByteOperation(0x0fa, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                }
            }
            else if(roomIndex == 4) {
                if(Settings.isFools2020Mode()) {
                    if(screenIndex == 0) {
                        AddObject.addInfernoFakeWeaponCover(screen);
                    }
                }
            }
            else if(roomIndex == 9) {
                if(Settings.isFools2020Mode()) {
                    // Add fools 2020 spaulders
                    if(screenIndex == 0) {
                        int spaulderCollectFlag = 2730;
                        for(int i = 0; i < 640; i += 40) {
                            AddObject.addFloatingItem(screen, i, 400, 62,
                                    true, Arrays.asList(new TestByteOperation(spaulderCollectFlag, ByteOp.FLAG_EQUALS, 0)),
                                    Arrays.asList(new WriteByteOperation(spaulderCollectFlag, ByteOp.ASSIGN_FLAG, 1)));
                            ++spaulderCollectFlag;
                        }
                    }
                    else if(screenIndex == 1) {
                        int spaulderCollectFlag = 2746;
                        for(int i = 640; i < 1280; i += 40) {
                            AddObject.addFloatingItem(screen, i, 400, 62,
                                    true, Arrays.asList(new TestByteOperation(spaulderCollectFlag, ByteOp.FLAG_EQUALS, 0)),
                                    Arrays.asList(new WriteByteOperation(spaulderCollectFlag, ByteOp.ASSIGN_FLAG, 1)));
                        }
                        AddObject.addAutosave(screen, 1080, 80, 0x06b,
                                Arrays.asList(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2),
                                        new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                                new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                    }
                }
            }
        }
        else if(zoneIndex == 6) {
            if(roomIndex == 2 && screenIndex == 0) {
                if(Settings.isRandomizeTransitionGates()) {
                    AddObject.addExtinctionTorch(screen);
                }
            }
            if(Settings.isBossCheckpoints()) {
                if(roomIndex == 9 && screenIndex == 1) {
                    AddObject.addAutosave(screen, 940, 400, 170,
                            Arrays.asList(new TestByteOperation(0x1ca, ByteOp.FLAG_EQUALS, 3),
                                    new TestByteOperation(0x1c3, ByteOp.FLAG_EQUALS, 3),
                                    new TestByteOperation(0x0fb, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                }
            }
            if(Settings.isFeatherlessMode()) {
                if(roomIndex == 1 && screenIndex == 1) {
                    // Land on Spriggan without feather
                    AddObject.addPot(screen, 780, 400, 5,
                            Settings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0));
                }
            }
        }
        else if(zoneIndex == 7) {
            if(roomIndex == 4 && screenIndex == 1) {
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 940, 80, 188,
                            Arrays.asList(new TestByteOperation(0x1e0, ByteOp.FLAG_EQUALS, 2),
                                    new TestByteOperation(0x0fc, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1)); // Text block 206 is backside Twin Labs grail, but they seem to be identical.
                }
            }
            else if(roomIndex == 10 && screenIndex == 1) {
                if(Settings.isFeatherlessMode()) {
                    // Access to Dimensional without Feather
                    AddObject.addPot(screen, 840, 320, 6,
                            Settings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0));
                }
            }
            else if(roomIndex == 3 && screenIndex == 2) {
                if(Settings.isFools2020Mode() || Settings.isFools2021Mode()) {
                    AddObject.addLittleBrotherWeightWaster(screen);
                }
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                if(Settings.isFools2020Mode()) {
                    AddObject.addTwinPuzzleFeatherlessPlatform(screen);
                    AddObject.addFloatingItem(screen, 1200, 80, 84, false, Arrays.asList(new TestByteOperation(0x1e4, ByteOp.FLAG_EQUALS, 0)), new ArrayList<>(0));
                    AddObject.addNoItemSoundEffect(screen, 0x1e4, 0x00b);
                    AddObject.addTwinPuzzleBlockFix(screen);
                }
            }
        }
        else if(zoneIndex == 9) {
            if(roomIndex == 7 && screenIndex == 0) {
                if(Settings.isFeatherlessMode()) {
                    AddObject.addPot(screen, 280, 240, 8, new ArrayList<>(0));
                }
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                GameDataTracker.addObject(AddObject.addUntrueShrineExit(screen, 0));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                GameDataTracker.addObject(AddObject.addUntrueShrineExit(screen, 1));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                GameDataTracker.addObject(AddObject.addUntrueShrineExit(screen, 2));
            }
        }
        else if(zoneIndex == 10) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 8 && screenIndex == 0) {
                    // Mr. Slushfund - 689
                    GameObject warp = AddObject.addWarp(screen, 80, 40, 4, 4, 10, 8, 1, 80, 352);

                    TestByteOperation warpTest = new TestByteOperation();
                    warpTest.setIndex(46);
                    warpTest.setValue((byte)1);
                    warpTest.setOp(ByteOp.FLAG_EQUALS);
                    warp.getTestByteOperations().add(warpTest);
                }
            }
            if(Settings.isFeatherlessMode()) {
                if(roomIndex == 1 && screenIndex == 0) {
                    // For not having to damage boost up Gate of Illusion to Cog of the Soul
                    AddObject.addPot(screen, 580, 280, 10,
                            Settings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0));
                }
            }
            if(Settings.isFools2020Mode()) {
                if(roomIndex == 0 && screenIndex == 0) {
                    // Ladder attack!
                    AddObject.addPot(screen, 580, 280, 10, new ArrayList<>());
                }
                else if(roomIndex == 8 && screenIndex == 0) {
                    // Ladder attack!
                    AddObject.addPot(screen, 580, 280, 10, new ArrayList<>());
                }
            }
        }
        else if(zoneIndex == 12) {
            if(Settings.isFeatherlessMode()) {
                if(roomIndex == 5 && screenIndex == 0) {
                    AddObject.addMoonlightFeatherlessPlatform(screen);
                }
            }
        }
        else if(zoneIndex == 14) {
            if(Settings.isFools2020Mode()) {
                if(roomIndex == 8) {
                    if(screenIndex == 1) {
                        // Troll pot on the way to Nuwa
                        AddObject.addPot(screen, 940, 280, 14, Arrays.asList(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2),
                                new TestByteOperation(0x369, ByteOp.FLAG_GTEQ, 1),
                                new TestByteOperation(0x265, ByteOp.FLAG_GTEQ, 1),
                                new TestByteOperation(0x298, ByteOp.FLAG_GTEQ, 1)));
                    }
                }
            }
            if(Settings.isFeatherlessMode()) {
                if(roomIndex == 8) {
                    if(screenIndex == 2) {
                        // Nuwa assist
                        AddObject.addPot(screen, 1840, 180, 14,
                                Settings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0));
                        AddObject.addPot(screen, 1840, 220, 14,
                                Settings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0));
                    }
                }
            }
        }
        else if(zoneIndex == 15) {
            if(Settings.isFeatherlessMode() && !Settings.getEnabledGlitches().contains("Raindrop")) {
                if(roomIndex == 2 && screenIndex == 1) {
                    // Access to Palenque without Feather
                    AddObject.addPot(screen, 20, 760, 15,
                            Settings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0));
                    AddObject.addPot(screen, 20, 800, 15,
                            Settings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0));
                }
            }
        }
        else if(zoneIndex == 17) {
            if(roomIndex == 10 && screenIndex == 1) {
                if(Settings.isUshumgalluAssist()) {
                    AddObject.addDimensionalOrbLadder(screen);
                }
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 300, 80, 358,
                            Arrays.asList(new TestByteOperation(0x2ed, ByteOp.FLAG_EQUALS, 1),
                                    new TestByteOperation(0x0fd, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                }
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                if(Settings.isFools2021Mode()) {
                    AddObject.addExplosion(screen, 700, 320, 0x008, 50, true);
                    AddObject.addExplosion(screen, 1200, 400, 0x009, 50, true);
                }
            }
            if(Settings.isFeatherlessMode()) {
                if(roomIndex == 0 && screenIndex == 1) {
                    AddObject.addPot(screen, 500, 680, 16,
                            Settings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0));
                    AddObject.addPot(screen, 500, 720, 16,
                            Settings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0));
                }
            }
        }
        else if(zoneIndex == 18) {
            if (roomIndex == 0 && screenIndex == 0) {
                AddObject.addUpperUntrueShrineBackupDoor(screen);
            }
            if (roomIndex == 1 && screenIndex == 1) {
                if(Settings.isFeatherlessMode()) {
                    AddObject.addTrueShrineFeatherlessPlatform(screen, 800, 240);
                    AddObject.addTrueShrineFeatherlessPlatform(screen, 1020, 220);
                }
            }
//            else if (roomIndex == 3 && screenIndex == 0) {
//                if(Settings.isFoolsMode()) {
//                    AddObject.addWarp(screen, 600, 440, 32, 3, 18, 3, 1, 150, 72);
//                }
//            }
            else if (roomIndex == 8 && screenIndex == 1) {
                AddObject.addLowerUntrueShrineBackupDoor(screen);
            }
            else if (roomIndex == 9 && screenIndex == 0) {
                AddObject.addSealUntrueShrineBackupDoor(screen);
            }
        }
        else if(zoneIndex == 23) {
            if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
                if(roomIndex == 1) {
                    if(screenIndex == 0) {
                        // HT room 1
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);

                        GameObject warp = AddObject.addWarp(screen, 40, 440, 6, 2, 23, 5, 0, 40, 152);
                        warp.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1));

                        GameObject laserWall = AddObject.addLaserWall(screen, 120, 200, true, 1);
                        laserWall.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1));
                        laserWall.getTestByteOperations().add(new TestByteOperation(0x7f4, ByteOp.FLAG_GT, 0));

                        AddObject.addHTExitDoor(screen);
                    }
                }
                else if(roomIndex == 2) {
                    if(screenIndex == 0 || screenIndex == 1) {
                        // HT rooms 3 and 4
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                }
                else if(roomIndex == 5) {
                    if(screenIndex == 0) {
                        // HT room 7
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                    else if(screenIndex == 1) {
                        // HT room 8
                        GameObject warp = AddObject.addWarp(screen, 120, 920, 26, 2, 23, 5, 0, 40, 152);
                        warp.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1));
                    }
                }
                else if(roomIndex == 6) {
                    if(screenIndex == 0) {
                        // HT room 10
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                }
                else if(roomIndex == 8) {
                    if(screenIndex == 0) {
                        // HT room 13
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                }
                else if(roomIndex == 9) {
                    if(screenIndex == 0) {
                        // HT room 15
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                }
                else if(roomIndex == 10) {
                    if(screenIndex == 1) {
                        // HT room 18
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                }
                else if(roomIndex == 12) {
                    if(screenIndex == 0) {
                        // HT room 20
                        GameObject warp = AddObject.addWarp(screen, 0, 0, 32, 2, 24, 1, 0, 100, 160);
                        warp.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1));
                    }
                }
//                else if(roomIndex == 14) {
//                    if(screenIndex == 0) {
//                        // HT room 24
//                        AddObject.addInfoTablet(screen);
//                    }
//                }
                else if(roomIndex == 15) {
                    if(screenIndex == 0) {
                        // HT room 25
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                    else if(screenIndex == 1) {
                        // HT room 26
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);

                        AddObject.addWarp(screen, 0, 920, 32, 2, 23, 15, 1, 80, 392);
                        AddObject.addWarp(screen, 600, 840, 2, 8, 23, 14, 0, 80, 212);
//                        AddObject.addLemezaDetector(screen, 600, 840, 2, 8,
//                                Arrays.asList(new TestByteOperation(0xace, ByteOp.FLAG_EQUALS, 0)),
//                                Arrays.asList(new WriteByteOperation(0xace, ByteOp.ASSIGN_FLAG, 1)));
                    }
                }
                else if(roomIndex == 18) {
                    if(screenIndex == 0) {
                        // HT room 27
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);

                        AddObject.addWarp(screen, 0, 360, 2, 8, 23, 15, 1, 80, 392);
                    }
                }
                else if(roomIndex == 20) {
                    if(screenIndex == 0) {
                        // HT room 30
                        AddObject.addLemezaDetector(screen, 0, 280, 3, 4,
                                Arrays.asList(new TestByteOperation(0x005, ByteOp.FLAG_EQUALS, 0),
                                        new TestByteOperation(0x7ed, ByteOp.FLAG_EQUALS, 0),
                                        new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)),
                                Arrays.asList(new WriteByteOperation(0x005, ByteOp.ASSIGN_FLAG, 1)));
                        AddObject.addPunchyFist(screen, 0, 300,
                                Arrays.asList(new WriteByteOperation(0x005, ByteOp.ASSIGN_FLAG, 1),
                                        new WriteByteOperation(0x005, ByteOp.ASSIGN_FLAG, 0)));
                    }
                }
                else if(roomIndex == 22) {
                    if(screenIndex == 0) {
                        // HT room 35
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                        AddObject.addAutosave(screen, 580, 380, 918,
                                Arrays.asList(new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0)),
                                new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                    }
                    else if(screenIndex == 1) {
//                        // HT room 34 (The Boss)
//                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 20, 0).getTestByteOperations().add(new TestByteOperation(0x7f1, ByteOp.FLAG_EQUALS, 2));
//                        AddObject.addLaserWall(screen, 680, 440, false, 30).getTestByteOperations().add(new TestByteOperation(0x7f1, ByteOp.FLAG_EQUALS, 2));
//                        AddObject.addLaserWall(screen, 1220, 440, false, 30).getTestByteOperations().add(new TestByteOperation(0x7f1, ByteOp.FLAG_EQUALS, 2));

//                        AddObject.addMovingPlatforms(screen);
//                        AddObject.addStunWitch(screen,720, 400, true).getTestByteOperations().add(new TestByteOperation(0x7f1, ByteOp.FLAG_EQUALS, 2));
//                        AddObject.addStunWitch(screen,1180, 400, false).getTestByteOperations().add(new TestByteOperation(0x7f1, ByteOp.FLAG_EQUALS, 2));

//                        AddObject.addAutosave(screen, 1220, 400, new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 0),
//                                new WriteByteOperation(0x002, ByteOp.ASSIGN_FLAG, 1));
                    }
                }
            }
        }
        else if(zoneIndex == 24) {
            if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
                if(roomIndex == 1 && screenIndex == 0) {
//                    int wave1Health = 20;
//                    int wave2Health = 40;
//                    int wave3Health = 40;
//                    int wave4Health = 80;
                    int wave1Health = 30;
                    int wave2Health = 48;
                    int wave3Health = 48;
                    int wave4Health = 100;

                    int wave1Speed = 1;
                    int wave2Speed = 2;
                    int wave3Speed = 2;
                    int wave4Speed = 3;

                    int wave1Damage = 10;
                    int wave2Damage = 5;
                    int wave3Damage = 5;
                    int wave4Damage = 20;

                    int delayPerWave = 5; // Can't go much lower or it'll break the ghosts, probably from too many objects.

                    int fromLeft = 40;
                    int fromTop = 40;
                    int fromBottom = 360;
                    int fromRight= 520;
                    int midWidth = 260;
                    int midHeight = 180;

                    // Top ghosts, wave 1
                    GameObject ghostLord = AddObject.addGhostLord(screen, fromLeft, fromTop, wave1Speed, wave1Health, wave1Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x7dd, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x002, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));

                    ghostLord = AddObject.addGhostLord(screen, fromRight, fromTop, wave1Speed, wave1Health, wave1Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x7dd, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x002, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));


                    // Mid ghosts, wave 2
                    AddObject.addSecondsTimer(screen, delayPerWave,
                            Arrays.asList(new TestByteOperation(0x7dd, ByteOp.FLAG_EQUALS, 0),
                                    new TestByteOperation(0x003, ByteOp.FLAG_EQUALS, 0)),
                            Arrays.asList(new WriteByteOperation(0x003, ByteOp.ASSIGN_FLAG, 1)));

                    ghostLord = AddObject.addGhostLord(screen, fromLeft, midHeight, wave2Speed, wave2Health, wave2Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x003, ByteOp.FLAG_EQUALS, 1));
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x7dd, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x002, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));

                    ghostLord = AddObject.addGhostLord(screen, fromRight, midHeight, wave2Speed, wave2Health, wave2Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x003, ByteOp.FLAG_EQUALS, 1));
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x7dd, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x002, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));

                    // Bottom ghosts, wave 3
                    AddObject.addSecondsTimer(screen, delayPerWave,
                            Arrays.asList(new TestByteOperation(0x7dd, ByteOp.FLAG_EQUALS, 0),
                                    new TestByteOperation(0x003, ByteOp.FLAG_EQUALS, 1)),
                            Arrays.asList(new WriteByteOperation(0x003, ByteOp.ASSIGN_FLAG, 2)));
                    ghostLord = AddObject.addGhostLord(screen, fromLeft, fromBottom, wave3Speed, wave3Health, wave3Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x003, ByteOp.FLAG_EQUALS, 2));
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x7dd, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x002, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));

                    ghostLord = AddObject.addGhostLord(screen, fromRight, fromBottom, wave3Speed, wave3Health, wave3Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x003, ByteOp.FLAG_EQUALS, 2));
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x7dd, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x002, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));

                    // Center ghost, final wave, true boss
                    AddObject.addSecondsTimer(screen, delayPerWave,
                            Arrays.asList(new TestByteOperation(0x7dd, ByteOp.FLAG_EQUALS, 0),
                                    new TestByteOperation(0x003, ByteOp.FLAG_EQUALS, 2)),
                            Arrays.asList(new WriteByteOperation(0x003, ByteOp.ASSIGN_FLAG, 3)));
                    ghostLord = AddObject.addGhostLord(screen, midWidth, midHeight, wave4Speed, wave4Health, wave4Damage, 352);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x003, ByteOp.FLAG_EQUALS, 3));
                    ghostLord.getTestByteOperations().add(new TestByteOperation(0x7dd, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x002, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));

                    // Timer to trigger end of fight
                    AddObject.addSecondsTimer(screen,
                            0, Arrays.asList(new TestByteOperation(0x7dd, ByteOp.FLAG_EQUALS, 0), new TestByteOperation(0x002, ByteOp.FLAG_EQUALS, 7)),
                            Arrays.asList(new WriteByteOperation(0x7e2, ByteOp.ASSIGN_FLAG, 1)));

                    // Autosave
                    AddObject.addAutosave(screen, 580, 400, 918,
                            Arrays.asList(new TestByteOperation(0x004, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(0x004, ByteOp.ASSIGN_FLAG, 1));
                }
            }
        }
    }

    private static void addCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == LocationCoordinateMapper.getStartingZone()
                && roomIndex == LocationCoordinateMapper.getStartingRoom()
                && screenIndex == LocationCoordinateMapper.getStartingScreen()) {
            if (Settings.isAutomaticHardmode()) {
                AddObject.addAutomaticHardmodeTimer(screen);
            }
            if (Settings.isAutomaticTranslations()) {
                AddObject.addAutomaticTranslationsTimer(screen);
            }
        }

        if(zoneIndex == 0) {
            if(roomIndex == 0 && screenIndex == 0) {
                // Treasures room
                if(Settings.isFools2021Mode()) {
                    AddObject.addFramesTimer(screen, 24,
                            Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 1)),
                            Arrays.asList(new WriteByteOperation(0x137, ByteOp.ASSIGN_FLAG, 2)));
                    AddObject.addFramesTimer(screen, 20,
                            Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 2)),
                            Arrays.asList(new WriteByteOperation(0x137, ByteOp.ASSIGN_FLAG, 3)));
                    AddObject.addFramesTimer(screen, 12,
                            Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 3)),
                            Arrays.asList(new WriteByteOperation(0x137, ByteOp.ASSIGN_FLAG, 4)));
                    AddObject.addFramesTimer(screen, 6,
                            Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_EQUALS, 4)),
                            Arrays.asList(new WriteByteOperation(0x137, ByteOp.ASSIGN_FLAG, 5)));
                }
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                // Treasures room
                if(Settings.isFools2021Mode()) {
                    // Ensure the process of falling blocks triggered by Pepper is reset if unfinished.
                    AddObject.addFramesTimer(screen, 0,
                            Arrays.asList(new TestByteOperation(0x137, ByteOp.FLAG_GT, 0),
                                    new TestByteOperation(0x137, ByteOp.FLAG_LT, 5)),
                            Arrays.asList(new WriteByteOperation(0x137, ByteOp.ASSIGN_FLAG, 1)));
                }
            }

            if(roomIndex == 4 && screenIndex == 1) {
                if(Settings.isHalloweenMode()) {
                    // Priest Zarnac - 674
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xac6);
                }
                // Ensure you can't lose access to the Guidance elevator. // todo: maybe find a better solution that respects logic
                AddObject.addSecondsTimer(screen, 0,
                        Arrays.asList(new TestByteOperation(0x34c, ByteOp.FLAG_GT, 1), new TestByteOperation(0x134, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(0x134, ByteOp.ASSIGN_FLAG, 1)));
            }
            if(roomIndex == 8 && screenIndex == 0) {
                if(Settings.isFools2021Mode()) {
                    // Make sure Amphisbaena is appropriately buffed.
                    AddObject.addSacredOrbCountTimers(screen);
                }
            }
        }
        else if(zoneIndex == 1) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 0 && screenIndex == 2) {
                    // Hiner - 671
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xac9);
                }
                else if(roomIndex == 2 && screenIndex == 0) {
                    // Moger - 672
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xac8);
                }
                else if(roomIndex == 7 && screenIndex == 0) {
                    // Former Mekuri Master - 673
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xac7);
                }
            }

            if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addSacredOrbCountTimers(screen);
            }
            else if(roomIndex == 2) {
                if(screenIndex == 0) {
                    if (!"Whip".equals(Settings.getCurrentStartingWeapon()) || Settings.isRandomizeStartingLocation() || Settings.isFools2020Mode()) {
                        AddObject.addSurfaceKillTimer(screen, false);
                    }
                }
                if(screenIndex == 1) {
                    AddObject.addXelpudIntroTimer(screen);
                    AddObject.addDiaryTalismanConversationTimers(screen);
                    if (!"Whip".equals(Settings.getCurrentStartingWeapon()) || Settings.isRandomizeStartingLocation() || Settings.isFools2020Mode()) {
                        AddObject.addSurfaceKillTimer(screen, true);
                    }
                }
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                if (!"Whip".equals(Settings.getCurrentStartingWeapon()) || Settings.isRandomizeStartingLocation() || Settings.isFools2020Mode()) {
                    AddObject.addSurfaceKillTimer(screen, false);
                }
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                if(Settings.isAlternateMotherAnkh()) {
                    AddObject.addMotherAnkhJewelRecoveryTimer(screen);
                }
            }
        }
        else if(zoneIndex == 2) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 2 && screenIndex == 0) {
                    // Priest Xanado - - 675
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xac5);
                }
            }
            if(Settings.isFools2021Mode()) {
                if(roomIndex == 7 && screenIndex == 1) {
                    if("Bado".equals(Settings.getCurrentGiant())) {
                        AddObject.addSuccessSound(screen);
                    }
                }
                else if(roomIndex == 7 && screenIndex == 2) {
                    if("Ledo".equals(Settings.getCurrentGiant())) {
                        AddObject.addSuccessSound(screen);
                    }
                }
                else if(roomIndex == 8 && screenIndex == 0) {
                    if("Abuto".equals(Settings.getCurrentGiant())) {
                        AddObject.addSuccessSound(screen);
                    }
                }
                else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                    if("Sakit".equals(Settings.getCurrentGiant()) || "Ji".equals(Settings.getCurrentGiant())) {
                        AddObject.addSuccessSound(screen);
                    }
                }
            }
        }
        else if(zoneIndex == 3) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 3 && screenIndex == 0) {
                    // Mulbruk
                    if(Settings.isIncludeHellTempleNPCs()) {
                        // Timer to set flag for talking about HT
                        AddObject.addSecondsTimer(screen,
                                0, Arrays.asList(new TestByteOperation(0xaca, ByteOp.FLAG_EQUALS, 29),
                                        new TestByteOperation(0xaac, ByteOp.FLAG_EQUALS, 1)),
                                Arrays.asList(new WriteByteOperation(0xaac, ByteOp.ASSIGN_FLAG, 2)));
                    }
                }
                else if(roomIndex == 4 && screenIndex == 2) {
                    // Priest Madomono - 718
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xab0);
                }
            }

            if(Settings.isRandomizeBosses()) {
                if(roomIndex == 4 && screenIndex == 2) {
                    AddObject.addBossTimer(screen, 0x0f8, 0x2d8);
                }
                else if(roomIndex == 8 && screenIndex == 0) {
                    AddObject.addSphinxRemovalTimer(screen);
                }
            }
        }
        else if(zoneIndex == 4) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 0 && screenIndex == 1) {
                    // Philosopher Giltoriyo - 677
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xac4);
                }
                else if(roomIndex == 6 && screenIndex == 1) {
                    // Priest Hidlyda - 678
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xac3);
                }
            }
//            if(roomIndex == 4 && screenIndex == 0) {
//                if(Settings.isFoolsMode()) {
//                    AddObject.addBossTimer(screen, 0x0f9, 0x2d9);
//                }
//            }
        }
        else if(zoneIndex == 5) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 3 && screenIndex == 2) {
                    // Priest Romancis - 679
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xac2);
                }
                else if(roomIndex == 2 && screenIndex == 1) {
                    // Priest Gailious - 723
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xaaf);
                }
            }
            if(Settings.isFools2020Mode()) {
                if(roomIndex == 4 && screenIndex == 0) {
                    AddObject.addSecondsTimer(screen, 0,
                            Arrays.asList(new TestByteOperation(0x13, ByteOp.FLAG_GTEQ, 1), new TestByteOperation(0x1b3, ByteOp.FLAG_EQUALS, 1)),
                            Arrays.asList(new WriteByteOperation(0x1b3, ByteOp.ASSIGN_FLAG, 2), new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1)));
                }
            }
        }
        else if(zoneIndex == 6) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 6 && screenIndex == 0) {
                    // Priest Aramo - 680
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xac1);
                }
                else if(roomIndex == 9 && screenIndex == 1) {
                    // Priest Triton - 681
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xac0);
                }
            }

            if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addPalenqueMSX2Timer(screen);
                if(Settings.isRandomizeBosses()) {
                    AddObject.addTwinLabsPoisonTimerRemoval(screen, true);
                }
            }
        }
        else if(zoneIndex == 7) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 10 && screenIndex == 1) {
                    // Priest Jaguarfiv - 683
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xabf);
                }
            }

            if(Settings.isRandomizeBosses()) {
                if(roomIndex == 0 && screenIndex == 0) {
                    AddObject.addBossTimer(screen, 0x0fb, 0x2db);
                }
            }
        }
        else if(zoneIndex == 8) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 1 && screenIndex == 0) {
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0x1f5);
                }
            }
        }
        else if(zoneIndex == 9 && roomIndex == 2 && screenIndex == 0) {
            AddObject.addDiaryChestConditionTimer(screen);
        }
        else if(zoneIndex == 10) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 8 && screenIndex == 0) {
                    // Mr. Slushfund - 689
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xabe);
                }
                else if(roomIndex == 8 && screenIndex == 1) {
                    // Priest Alest - 693
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xabd);
                }
                else if(roomIndex == 0 && screenIndex == 1) {
                    // Stray fairy - 694
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xabc);
                }
                else if(roomIndex == 2 && screenIndex == 2) {
                    // duplex - 707
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xab4);
                }
            }

            if(Settings.isRandomizeTransitionGates()) {
                if(roomIndex == 1 && screenIndex == 0) {
                    AddObject.addWeightDoorTimer(screen, 0x037);
                }
            }
        }
        else if(zoneIndex == 11) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 7 && screenIndex == 0) {
                    // Giant Thexde - 696
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xabb);
                }
            }
        }
        else if(zoneIndex == 12) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 6 && screenIndex == 0) {
                    // Philosopher Alsedana - 698
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xaba);
                }
                else if(roomIndex == 3 && screenIndex == 0) {
                    // Samieru - 708
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xab3);
                }
            }

            if(roomIndex == 2) {
                if(screenIndex == 0) {
                    AddObject.addMoonlightPassageTimer(screen);
                }
                else if(screenIndex == 1) {
                    AddObject.addWeightDoorTimer(screen, 0x045);
                }
            }
        }
        else if(zoneIndex == 13) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 5 && screenIndex == 1) {
                    // Philosopher Samaranta - 700
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xab9);
                }
                else if(roomIndex == 6 && screenIndex == 3) {
                    // Naramura - 709
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xab2);
                }
            }
            if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addFlailWhipPuzzleTimer(screen);
            }
            else if(roomIndex == 6 && screenIndex == 2) {
                AddObject.addFlailWhipPuzzleTimer(screen);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                if(Settings.isAllowSubweaponStart()) {
                    AddObject.addFloodedTowerShortcutTimer(screen);
                }
            }
        }
        else if(zoneIndex == 14) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 0 && screenIndex == 1) {
                    // Priest Laydoc - 701
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xab8);
                }
            }
        }
        else if(zoneIndex == 16) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 1 && screenIndex == 0) {
                    // Priest Ashgine - 702
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xab7);
                }
            }
        }
        else if(zoneIndex == 17) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 2 && screenIndex == 0) {
                    // Fobos - 704
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xab6);
                }
            }

            if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addAngelShieldPuzzleTimers(screen);
            }
        }
        else if(zoneIndex == 21) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 0 && screenIndex == 0) {
                    // 8bit Elder - 706
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xab5);
                }
            }
        }
        else if(zoneIndex == 20) {
            if(Settings.isHalloweenMode()) {
                if(roomIndex == 0 && screenIndex == 1) {
                    // 8bit Fairy - 710
                    if(!Settings.isIncludeHellTempleNPCs()) {
                        AddObject.addEscapeTimer(screen, 0xaca, 28);
                    }
                    AddObject.addNpcConversationTimer(screen, 0xab1);
                }
            }
        }
        else if(zoneIndex == 23) {
            if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
//                if(roomIndex == 0) {
//                    if(screenIndex == 0) {
//                        AddObject.addSecondsTimer(screen,
//                                Arrays.asList(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0)),
//                                Arrays.asList(new WriteByteOperation(0x382, ByteOp.ASSIGN_FLAG, 1)));
//                    }
//                }
                if(roomIndex == 22) {
                    if(screenIndex == 0) {
                        // Dracuet - 1011
                        AddObject.addEscapeTimer(screen, 0xaae, 1);
                        AddObject.addNpcConversationTimer(screen, 0xaae);
                    }
                }
                else if(roomIndex == 12 && screenIndex == 1) {
                    AddObject.addSecondsTimer(screen,
                            0, Arrays.asList(new TestByteOperation(0xaad, ByteOp.FLAG_GT, 0)),
                            Arrays.asList(new WriteByteOperation(0xaad, ByteOp.ASSIGN_FLAG, 0)));
                }
                else if(roomIndex == 14 && screenIndex == 1) {
                    AddObject.addSecondsTimer(screen,
                            0, Arrays.asList(new TestByteOperation(0xaad, ByteOp.FLAG_GT, 0)),
                            Arrays.asList(new WriteByteOperation(0xaad, ByteOp.ASSIGN_FLAG, 0)));
                }
                else if(roomIndex == 15 && screenIndex == 1) {
                    AddObject.addSecondsTimer(screen,
                            0, Arrays.asList(new TestByteOperation(0xaad, ByteOp.FLAG_GT, 0)),
                            Arrays.asList(new WriteByteOperation(0xaad, ByteOp.ASSIGN_FLAG, 0)));
                }
            }
        }
    }

    private static void convertToChest(GameObject gameObject) {
        int itemFlag = gameObject.getWriteByteOperations().get(0).getIndex();
        TestByteOperation puzzleFlag = gameObject.getTestByteOperations().get(1); // Probably a better approach is to look for flags that don't match the update flag, and that the comparison is ==
        short inventoryWord = gameObject.getArgs().get(1);

        gameObject.setId((short)0x2c);

        while (gameObject.getArgs().size() < 6) {
            // Ensure the correct number of args for chests, regardless of what was present before.
            gameObject.getArgs().add((short) 0);
        }
        gameObject.getArgs().set(0, (short)(inventoryWord + 11));
        gameObject.getArgs().set(1, (short)1); // Real item
        gameObject.getArgs().set(2, (short)1); // Blue chest
        gameObject.getArgs().set(3, (short)0); // Not cursed
        gameObject.getArgs().set(4, (short)1); // Curse percent damage
        gameObject.getArgs().set(5, (short)50); // Curse damage 50%

        gameObject.getTestByteOperations().clear();
        gameObject.getWriteByteOperations().clear();
        gameObject.getWriteByteOperations().add(new WriteByteOperation(itemFlag, ByteOp.ASSIGN_FLAG, 2));
        gameObject.getWriteByteOperations().add(new WriteByteOperation(puzzleFlag.getIndex(), ByteOp.ASSIGN_FLAG, puzzleFlag.getValue()));
        gameObject.getWriteByteOperations().add(new WriteByteOperation(itemFlag, ByteOp.ASSIGN_FLAG, 1));
        gameObject.getWriteByteOperations().add(new WriteByteOperation(itemFlag, ByteOp.ASSIGN_FLAG, 2));
    }
}
