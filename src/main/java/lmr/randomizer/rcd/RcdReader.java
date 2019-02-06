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
        }
        else if(obj.getId() == 0x2f) {
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
            else if(obj.getArgs().get(4) == 684) {
                // First Fairy Queen conversation, completely unneeded for randomizer.
                keepObject = false;
            }
            else if(obj.getArgs().get(4) == 913) {
                // Xelpud conversation after he goes to do the Diary thing.
                keepObject = false;
            }
            else if(obj.getArgs().get(4) == 1014) {
                // Mulbruk conversation after she runs away from the Provocative Bathing Suit.
                keepObject = false;
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
            }

            if(obj.getTestByteOperations().isEmpty() || obj.getTestByteOperations().get(0).getIndex() != 524) {
                // Pots - update to 1.3 position and then return (no need to add tracking).
                // Note that there is a pot tied to a warp in Illusion which is removed by randomizer (the warp is always active)
                objectContainer.getObjects().add(obj);
                PotMover.updateLocation(obj);
            }
            return rcdByteIndex;
        }
        else if (obj.getId() == 0x97) {
            if(!obj.getTestByteOperations().isEmpty() && obj.getTestByteOperations().get(0).getIndex() == 524) {
                // Remove broken pot flag check so the warp is just always active.
                obj.getTestByteOperations().remove(0);
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
                        keepObject = false;
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
        else if(obj.getId() == 0x34) {
            if(objectContainer instanceof Screen) {
                Screen screen = (Screen) objectContainer;
                if (screen.getZoneIndex() == 3 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
                    // Seal to wake Mulbruk - set the awake flag so we can skip the conversation that normally sets this flag.
                    WriteByteOperation flagUpdate = new WriteByteOperation();
                    flagUpdate.setIndex(913);
                    flagUpdate.setOp(ByteOp.ASSIGN_FLAG);
                    flagUpdate.setValue(1);
                    obj.getWriteByteOperations().add(flagUpdate);
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
        }
        else if (obj.getId() == 0xc5) {
            if(Settings.isRandomizeTransitionGates()) {
                obj.getArgs().set(2, (short)10);
            }
        }
        else if (obj.getId() == 0x93) {
            if(Settings.isRandomizeTrapItems()) {
                if(objectContainer instanceof Screen) {
                    Screen screen = (Screen)objectContainer;
                    if (screen.getZoneIndex() == 5 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
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
                else if (screen.getZoneIndex() == 7 && screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
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
                    if (testByteOperation.getIndex() == 254) {
                        keepObject = false;
                        break;
                    }
                }
            }
        }
        else if (obj.getId() == 0xc0) {
            // Mother ankh
            if(Settings.isAlternateMotherAnkh()) {
                obj.setId((short)0x2e);
                obj.getArgs().set(0, (short)8);
                for(TestByteOperation testByteOperation : obj.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == 254) {
                        testByteOperation.setValue((byte) 2);
                    }
                }
                obj.setY(obj.getY() + 60);
            }

//            for(int i = 1; i <= 23; i++) {
//                obj.getArgs().set(i, (short) 1);
//            }
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

                msdByteIndex += room.getHitMaskWidth() * room.getHitMaskHeight();

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
                    else if(zoneIndex == 7) {
                        if(roomIndex == 3 && screenIndex == 2) {
                            AddObject.setLittleBrotherShopScreen(screen);
                        }
                    }

                    room.getScreens().add(screen);
                }

                zone.getRooms().add(room);
            }
            zones.add(zone);
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

        if(screen.getZoneIndex() == 4 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
            ScreenExit screenExit = screen.getScreenExits().get(2);
            screenExit.setZoneIndex((byte)25);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }

        if(Settings.isRandomizeStartingLocation()) {
            for(ScreenExit screenExit : screen.getScreenExits()) {
                if(screenExit.getZoneIndex() == -1 && screenExit.getRoomIndex() == -1 && screenExit.getScreenIndex() == -1) {
                    screenExit.setZoneIndex(LocationCoordinateMapper.getStartingZone());
                    screenExit.setRoomIndex(LocationCoordinateMapper.getStartingRoom());
                    screenExit.setScreenIndex(LocationCoordinateMapper.getStartingScreen());
                }
            }
        }
    }

    private static void addCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == LocationCoordinateMapper.getStartingZone()
                && roomIndex == LocationCoordinateMapper.getStartingRoom()
                && screenIndex == LocationCoordinateMapper.getStartingScreen()) {
            AddObject.addSpecialGrailTablet(screen);
            AddObject.addStartingItems(screen);
        }

        if(zoneIndex == 1) {
            if(Settings.isRandomizeStartingLocation() && roomIndex == 2 && screenIndex == 1) {
                AddObject.addSurfaceGrailTablet(screen);
            }

            if(roomIndex == 4 && screenIndex == 2) {
                if(Settings.isRandomizeBacksideDoors()) {
                    AddObject.addSurfaceCoverDetector(screen);
                }
            }
        }
        else if(zoneIndex == 2 && roomIndex == 2 && screenIndex == 0) {
            AddObject.addHardmodeToggleWeights(screen);
        }
        else if(zoneIndex == 9 && roomIndex == 8 && screenIndex == 1) {
            GameDataTracker.addObject(AddObject.addUntrueShrineExit(screen, 0));
        }
        else if(zoneIndex == 9 && roomIndex == 9 && screenIndex == 0) {
            GameDataTracker.addObject(AddObject.addUntrueShrineExit(screen, 1));
        }
        else if(zoneIndex == 9 && roomIndex == 9 && screenIndex == 1) {
            GameDataTracker.addObject(AddObject.addUntrueShrineExit(screen, 2));
        }
        else if(zoneIndex == 17 && roomIndex == 10 && screenIndex == 1) {
            if(Settings.isUshumgalluAssist()) {
                AddObject.addDimensionalOrbLadder(screen);
            }
        }
        else if(zoneIndex == 18) {
            if (roomIndex == 0 && screenIndex == 0) {
                AddObject.addUpperUntrueShrineBackupDoor(screen);
            }
            else if (roomIndex == 8 && screenIndex == 1) {
                AddObject.addLowerUntrueShrineBackupDoor(screen);
            }
            else if (roomIndex == 9 && screenIndex == 0) {
                AddObject.addSealUntrueShrineBackupDoor(screen);
            }
        }
    }

    private static void addCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == 1 && roomIndex == 1 && screenIndex == 1) {
            AddObject.addSacredOrbCountTimers(screen);
        }
        else if(zoneIndex == 1) {
            if(roomIndex == 2) {
                if(screenIndex == 0) {
                    if (!"Whip".equals(Settings.getCurrentStartingWeapon())) {
                        AddObject.addRandomWeaponKillTimer(screen, false);
                    }
                }
                if(screenIndex == 1) {
                    AddObject.addDiaryTalismanConversationTimers(screen);
                    if (Settings.isAutomaticHardmode()) {
                        AddObject.addAutomaticHardmode(screen);
                    }
                    if (Settings.isAutomaticTranslations()) {
                        AddObject.addAutomaticTranslations(screen);
                    }
                    if (!"Whip".equals(Settings.getCurrentStartingWeapon())) {
                        AddObject.addRandomWeaponKillTimer(screen, true);
                    }
                }
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                if (!"Whip".equals(Settings.getCurrentStartingWeapon())) {
                    AddObject.addRandomWeaponKillTimer(screen, false);
                }
            }
        }
        else if(zoneIndex == 6 && roomIndex == 9 && screenIndex == 1) {
            AddObject.addPalenqueMSX2Timer(screen);
        }
        else if(zoneIndex == 9 && roomIndex == 2 && screenIndex == 0) {
            AddObject.addDiaryChestConditionTimer(screen);
        }
        else if(zoneIndex == 10 && roomIndex == 1 && screenIndex == 0) {
            if(Settings.isRandomizeTransitionGates()) {
                AddObject.addWeightDoorTimer(screen, 0x037);
            }
        }
        else if(zoneIndex == 12) {
            if(roomIndex == 2) {
                if(screenIndex == 0) {
                    AddObject.addMoonlightPassageTimer(screen);
                }
                else if(screenIndex == 1 && Settings.isRandomizeTransitionGates()) {
                    AddObject.addWeightDoorTimer(screen, 0x045);
                }
            }
        }
        else if(zoneIndex == 13) {
            if((roomIndex == 5 && screenIndex == 1) || (roomIndex == 6 && screenIndex == 2)) {
                AddObject.addFlailWhipPuzzleTimer(screen);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                if(Settings.isAllowSubweaponStart()) {
                    AddObject.addFloodedTowerShortcutTimer(screen);
                }
            }
        }
        else if(zoneIndex == 17 && roomIndex == 8 && screenIndex == 0) {
            AddObject.addAngelShieldPuzzleTimers(screen);
        }
    }
}
