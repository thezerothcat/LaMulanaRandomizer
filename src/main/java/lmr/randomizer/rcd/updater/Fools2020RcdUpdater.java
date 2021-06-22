package lmr.randomizer.rcd.updater;

import lmr.randomizer.BossDifficulty;
import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.rcd.RcdFileData;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.AddObject;
import lmr.randomizer.util.BlockConstants;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.ItemConstants;
import lmr.randomizer.util.ZoneConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fools2020RcdUpdater extends RcdUpdater {
    public Fools2020RcdUpdater(RcdFileData rcdFileData, DatFileData datFileData) {
        super(rcdFileData, datFileData);
    }

    @Override
    boolean updateFlagTimer(GameObject flagTimer) {
        ObjectContainer objectContainer = flagTimer.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 2) {
            for (int i = 0; i < flagTimer.getTestByteOperations().size(); i++) {
                TestByteOperation flagTest = flagTimer.getTestByteOperations().get(i);
                if (flagTest.getIndex() == FlagConstants.LITTLE_BROTHER_PURCHASES) {
                    flagTest.setValue((byte)4);
                    break;
                }
            }
        }
        return true;
    }

    @Override
    boolean updateWeaponCover(GameObject weaponCover) {
        ObjectContainer objectContainer = weaponCover.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        // Remove weapon cover from Flare puzzle room
        if (screen.getZoneIndex() == 5 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
            return false;
        }
        return true;
    }

    @Override
    boolean updateTrapdoor(GameObject trapdoor) {
        ObjectContainer objectContainer = trapdoor.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 1) {
            return false;
        }
        return true;
    }

    @Override
    boolean updateVimana(GameObject vimana) {
        List<TestByteOperation> testsToUse = new ArrayList<>();
        for (TestByteOperation flagTest : vimana.getTestByteOperations()) {
            if (flagTest.getIndex() == FlagConstants.WF_PLANE_MODEL) {
                // Don't stop spawning after you get the chest.
                continue;
            }
            if(flagTest.getIndex() == vimana.getWriteByteOperations().get(0).getIndex()) {
                // Don't stop spawning if you've killed one already.
                continue;
            }
            testsToUse.add(flagTest);
        }
        vimana.getTestByteOperations().clear();
        vimana.getTestByteOperations().addAll(testsToUse);
        vimana.getArgs().set(3, (short)10); // Increase speed
        vimana.getArgs().set(4, (short)1); // Set HP to 1
        return true;
    }

    @Override
    boolean updateFog(GameObject fog) {
        for (TestByteOperation flagTest : fog.getTestByteOperations()) {
            if(flagTest.getIndex() == FlagConstants.EXTINCTION_TEMP_LIGHT) {
                flagTest.setValue((byte)(flagTest.getValue() == 0 ? 1 : 0));
            }
        }
        return true;
    }

    @Override
    boolean updateGraphicsTextureDraw(GameObject graphicsTextureDraw) {
        ObjectContainer objectContainer = graphicsTextureDraw.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
            // Graphic for closed Mulbruk door should be removed since the door won't be closed.
            return false;
        }
        return true;
    }

    @Override
    boolean updateSoundEffect(GameObject soundEffect) {
        ObjectContainer objectContainer = soundEffect.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == 7 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 1) {
            return false; // Remove shell horn puzzle solve sound
        }
        return true;
    }

    @Override
    boolean updateConversationDoor(GameObject conversationDoor) {
        if(conversationDoor.getArgs().get(3) == 1) {
            if(conversationDoor.getArgs().get(4) == 272) {
                conversationDoor.getArgs().set(4, (short)273);
            }
        }
        return true;
    }

    @Override
    boolean updatePushableBlock(GameObject pushableBlock) {
        ObjectContainer objectContainer = pushableBlock.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == 5 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
            if(pushableBlock.getX() == 360) {
                pushableBlock.setX(pushableBlock.getX() + 60);
            }
            else {
                return false;
            }
        }
        else if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 1) {
            if(pushableBlock.getY() == 160) {
                pushableBlock.setX(pushableBlock.getX() - 40);
            }
        }
        return true;
    }

    @Override
    boolean updateBlockButton(GameObject blockButton) {
        ObjectContainer objectContainer = blockButton.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 1) {
            if(blockButton.getX() == 1060) {
                blockButton.setX(blockButton.getX() - 40);
            }
        }
        return true;
    }

    @Override
    void addUntrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == 5) {
            if(roomIndex == 4) {
                if(screenIndex == 0) {
                    AddObject.addInfernoFakeWeaponCover(screen,
                            Arrays.asList(new TestByteOperation(FlagConstants.INFERNO_PUZZLE_FLARE_GUN, ByteOp.FLAG_LT, 2)));
                }
            }
            else if(roomIndex == 9) {
                // Add fools 2020 spaulders
                if(screenIndex == 0) {
                    int spaulderCollectFlag = 2730;
                    for(int i = 0; i < 640; i += 40) {
                        AddObject.addFloatingItem(screen, i, 400, ItemConstants.SPAULDER,
                                true, Arrays.asList(new TestByteOperation(spaulderCollectFlag, ByteOp.FLAG_EQUALS, 0)),
                                Arrays.asList(new WriteByteOperation(spaulderCollectFlag, ByteOp.ASSIGN_FLAG, 1)));
                        ++spaulderCollectFlag;
                    }
                }
                else if(screenIndex == 1) {
                    int spaulderCollectFlag = 2746;
                    for(int i = 640; i < 1280; i += 40) {
                        AddObject.addFloatingItem(screen, i, 400, ItemConstants.SPAULDER,
                                true, Arrays.asList(new TestByteOperation(spaulderCollectFlag, ByteOp.FLAG_EQUALS, 0)),
                                Arrays.asList(new WriteByteOperation(spaulderCollectFlag, ByteOp.ASSIGN_FLAG, 1)));
                    }
                    AddObject.addAutosave(screen, 1080, 80, 0x06b,
                            Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
                }
            }
        }
        else if(zoneIndex == 7) {
            if(roomIndex == 3 && screenIndex == 2) {
                AddObject.addLittleBrotherWeightWaster(screen);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addTwinPuzzleFeatherlessPlatform(screen);
                AddObject.addFloatingItem(screen, 1200, 80, ItemConstants.SECRET_TREASURE_OF_LIFE, false, Arrays.asList(new TestByteOperation(FlagConstants.TWIN_UNSOLVABLE_PUZZLE, ByteOp.FLAG_EQUALS, 0)), new ArrayList<>(0));
                AddObject.addNoItemSoundEffect(screen, FlagConstants.TWIN_UNSOLVABLE_PUZZLE, FlagConstants.SCREEN_FLAG_B);
                AddObject.addTwinPuzzleBlockFix(screen);
            }
        }
        else if(zoneIndex == 10) {
            if(roomIndex == 0 && screenIndex == 0) {
                // Ladder attack!
                AddObject.addPot(screen, 580, 280, PotGraphic.ILLUSION, DropType.NOTHING, 0, new ArrayList<>(), new ArrayList<>(0));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                // Ladder attack!
                AddObject.addPot(screen, 580, 280, PotGraphic.ILLUSION, DropType.NOTHING, 0, new ArrayList<>(), new ArrayList<>(0));
            }
        }
        else if(zoneIndex == 14) {
            if(roomIndex == 8) {
                if(screenIndex == 1) {
                    // Troll pot on the way to Nuwa
                    AddObject.addPot(screen, 940, 280, PotGraphic.RUIN, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2),
                            new TestByteOperation(FlagConstants.RUIN_LADDER_NUWA, ByteOp.FLAG_GTEQ, 1),
                            new TestByteOperation(FlagConstants.RUIN_LADDER_NUWA_V2, ByteOp.FLAG_GTEQ, 1),
                            new TestByteOperation(FlagConstants.RUIN_PUZZLE_NUWA, ByteOp.FLAG_GTEQ, 1)), new ArrayList<>(0));
                }
            }
        }
        else if(zoneIndex == 17 && roomIndex == 9 && screenIndex == 0) {
            if(HolidaySettings.isUpdatedVersion()) {
                AddObject.addPot(screen, 300, 400, PotGraphic.DIMENSIONAL,
                        BossDifficulty.EASY.equals(Settings.getBossDifficulty()) ? DropType.FLARE_GUN_AMMO : DropType.NOTHING, 80, Arrays.asList(new TestByteOperation(FlagConstants.ROOM_FLAG_40, ByteOp.FLAG_EQUALS, 1)),
                        new ArrayList<>(0));

            }
        }
    }

    @Override
    void addUntrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == 5) {
            if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addSecondsTimer(screen, 0,
                        Arrays.asList(new TestByteOperation(FlagConstants.SCREEN_FLAG_13, ByteOp.FLAG_GTEQ, 1), new TestByteOperation(FlagConstants.INFERNO_PUZZLE_FLARE_GUN, ByteOp.FLAG_EQUALS, 1)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.INFERNO_PUZZLE_FLARE_GUN, ByteOp.ASSIGN_FLAG, 2), new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1)));
            }
        }
    }

    @Override
    void doUntrackedPostUpdates(){
        convertLampDetectors();
    }

    @Override
    public void doPostShuffleUpdates() {
        updateFlareGunItem();
        updateFeatherChest();
        if(HolidaySettings.isUpdatedVersion()) {
            updateFeatherChestSequence();
        }
        updateMulbrukConversations();
    }

    private void updateFlareGunItem() {
        Zone infernoCavern = rcdFileData.getZone(ZoneConstants.INFERNO);
        Room flarePuzzleRoom = infernoCavern.getRoom(4);
        Screen flarePuzzleScreen = flarePuzzleRoom.getScreen(0);

        for(GameObject gameObject : flarePuzzleScreen.getObjectsById(ObjectIdConstants.FloatingItem)) {
            gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.INFERNO_PUZZLE_FLARE_GUN, ByteOp.FLAG_GTEQ, 2));
        }
    }

    private void updateFeatherChest() {
        Zone surface = rcdFileData.getZone(ZoneConstants.SURFACE);
        Room argusRoom = surface.getRoom(0);
        Screen featherScreen = argusRoom.getScreen(0);

        for(GameObject gameObject : featherScreen.getObjects()) {
            if(gameObject.getId() == 0x2c) {
                gameObject.getArgs().set(0, (short)(ItemConstants.SECRET_TREASURE_OF_LIFE + 11));

                WriteByteOperation puzzleFlag = gameObject.getWriteByteOperations().get(1);
                gameObject.getWriteByteOperations().clear();

                gameObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.ASSIGN_FLAG, 2));
                gameObject.getWriteByteOperations().add(puzzleFlag);
                gameObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.ASSIGN_FLAG, 1));
                gameObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.ASSIGN_FLAG, 2));
            }
        }
    }

    private void updateFeatherChestSequence() {
        Zone surface = rcdFileData.getZone(1);
        Room argusRoom = surface.getRoom(0);
        Screen featherScreen = argusRoom.getScreen(0);

        AddObject.addGrailToggle(featherScreen, false, new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2));
        AddObject.addFramesTimer(featherScreen, 0,
                Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_SPAULDER, ByteOp.FLAG_EQUALS, 0),
                        new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)),
                Arrays.asList(new WriteByteOperation(FlagConstants.CUSTOM_WF_SPAULDER, ByteOp.ASSIGN_FLAG, 1),
                        new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1)));
        AddObject.addPot(featherScreen, 220, 20, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));
        AddObject.addSuccessSound(featherScreen,  Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2),
                new TestByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.FLAG_EQUALS, 1)));

        Screen argusScreen = argusRoom.getScreen(1);

        AddObject.addPot(argusScreen, 900, 320, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));
        AddObject.addPot(argusScreen, 940, 320, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));
        AddObject.addPot(argusScreen, 980, 320, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));
        AddObject.addPot(argusScreen, 1020, 320, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));
        AddObject.addPot(argusScreen, 1060, 320, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));

        AddObject.addPot(argusScreen, 920, 280, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));
        AddObject.addPot(argusScreen, 960, 280, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));
        AddObject.addPot(argusScreen, 1000, 280, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));
        AddObject.addPot(argusScreen, 1040, 280, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));

        AddObject.addPot(argusScreen, 940, 240, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));
        AddObject.addPot(argusScreen, 980, 240, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));
        AddObject.addPot(argusScreen, 1020, 240, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));

        AddObject.addPot(argusScreen, 960, 200, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));
        AddObject.addPot(argusScreen, 1000, 200, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));

        AddObject.addPot(argusScreen, 980, 160, PotGraphic.SURFACE, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)), new ArrayList<>(0));

        AddObject.addFloatingItem(argusScreen, 980, 0, 53, false,
                Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(FlagConstants.CUSTOM_WF_SPAULDER, ByteOp.FLAG_EQUALS, 1)),
                Arrays.asList(new WriteByteOperation(FlagConstants.CUSTOM_WF_SPAULDER, ByteOp.ASSIGN_FLAG, 2),
                        new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1)));
        AddObject.addNoItemSoundEffect(argusScreen, FlagConstants.CUSTOM_WF_SPAULDER, FlagConstants.SCREEN_FLAG_B);

        Screen nextToArgusScreen = argusRoom.getScreen(2);
        AddObject.addGrailToggle(nextToArgusScreen, true);
    }

    private void updateMulbrukConversations() {
        Zone mulbrukZone = rcdFileData.getZone(3);
        Room mulbrukRoom = mulbrukZone.getRoom(3);
        Screen mulbrukScreen = mulbrukRoom.getScreen(0);

        // Find existing objects
        GameObject escapeConversationNormal = null;
        List<GameObject> keptObjects = new ArrayList<>();
        for(GameObject gameObject : mulbrukScreen.getObjects()) {
            if(gameObject.getId() == 0xa0) {
                if(gameObject.getArgs().get(4) == BlockConstants.MulbrukEscapeRegular) {
                    escapeConversationNormal = gameObject;
                    keptObjects.add(gameObject);
                }
                else if(gameObject.getArgs().get(4) == BlockConstants.MulbrukEscapeSwimsuit) {
                    keptObjects.add(gameObject);
                }
            }
            else {
                keptObjects.add(gameObject);
            }
        }

        GameObject bookOfTheDeadConversation = new GameObject(escapeConversationNormal);
        bookOfTheDeadConversation.getArgs().set(4, getCustomBlockIndex(CustomBlockEnum.Fools2020ReferenceBlock_MulbrukBookOfTheDead));
        bookOfTheDeadConversation.getTestByteOperations().clear();
        bookOfTheDeadConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_NOT_EQUAL, 1));
        bookOfTheDeadConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.MULBRUK_CONVERSATION_BOOK, ByteOp.FLAG_EQUALS, 1));
        bookOfTheDeadConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.MULBRUK_CONVERSATION_AWAKE, ByteOp.FLAG_GTEQ, 1));

        GameObject optionConversation = new GameObject(escapeConversationNormal);
        optionConversation.getArgs().set(4, getCustomBlockIndex(CustomBlockEnum.Fools2020ReferenceBlock_MulbrukEarlyExitPrompt));
        optionConversation.getTestByteOperations().clear();
        optionConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_NOT_EQUAL, 1)); // Option to quit
        optionConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.MULBRUK_CONVERSATION_BOOK, ByteOp.FLAG_NOT_EQUAL, 1));
        optionConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.MULBRUK_CONVERSATION_AWAKE, ByteOp.FLAG_GTEQ, 1));

        mulbrukScreen.getObjects().clear();
        mulbrukScreen.getObjects().addAll(keptObjects);
        mulbrukScreen.getObjects().add(bookOfTheDeadConversation);
        mulbrukScreen.getObjects().add(optionConversation);
    }

    @Override
    void updateScreenExits(Screen screen) {
        if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 1) {
            // Prevent raindropping to the Lamp of Time shop
            screen.getScreenExit(ScreenExit.DOWN).setDestination(7, 4, 1);
        }
        if(screen.getZoneIndex() == 1 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
            // In case the falling pot does weird stuff
            screen.getScreenExit(ScreenExit.DOWN).setDestination(1, 0, 0);
        }
    }

    private void convertLampDetectors() {
        for(GameObject lemezaDetector : rcdFileData.getObjectsById(ObjectIdConstants.LemezaDetector)) {
            boolean lampOfTimeDetector = false;
            for(WriteByteOperation writeByteOperation : lemezaDetector.getWriteByteOperations()) {
                if(writeByteOperation.getIndex() == FlagConstants.LAMP_OF_TIME_STATE && ByteOp.ASSIGN_FLAG.equals(writeByteOperation.getOp()) && writeByteOperation.getValue() == 1) {
                    // Lemeza detector for Lamp of Time light
                    lampOfTimeDetector = true;
                    break;
                }
            }
            if(lampOfTimeDetector) {
                lemezaDetector.getWriteByteOperations().clear();
                lemezaDetector.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2A, ByteOp.ASSIGN_FLAG, 1));
                AddObject.addExplosion(lemezaDetector.getObjectContainer(), lemezaDetector.getX(), lemezaDetector.getY(), FlagConstants.SCREEN_FLAG_2A, 15, true);
            }
        }
    }
}
