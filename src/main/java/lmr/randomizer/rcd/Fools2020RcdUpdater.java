package lmr.randomizer.rcd;

import lmr.randomizer.FlagConstants;
import lmr.randomizer.ItemConstants;
import lmr.randomizer.dat.AddObject;
import lmr.randomizer.rcd.object.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fools2020RcdUpdater extends RcdUpdater {
    public Fools2020RcdUpdater(RcdData rcdData) {
        super(rcdData);
    }

    @Override
    boolean updatePot(GameObject pot) {
        return true;
    }

    @Override
    boolean updateBat(GameObject bat) {
        return true;
    }

    @Override
    boolean updateSkeleton(GameObject skeleton) {
        return true;
    }

    @Override
    boolean updateLadder(GameObject ladder) {
        return true;
    }

    @Override
    boolean updateDais(GameObject dais) {
        return true;
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
    boolean updateRoomSpawner(GameObject roomSpawner) {
        return true;
    }

    @Override
    boolean updateCrusher(GameObject crusher) {
        return true;
    }

    @Override
    boolean updateHitbox(GameObject hitbox) {
        return true;
    }

    @Override
    boolean updateLemezaDetector(GameObject lemezaDetector) {
        return true;
    }

    @Override
    boolean updateFist(GameObject fist) {
        return true;
    }

    @Override
    boolean updateSteam(GameObject steam) {
        return true;
    }

    @Override
    boolean updateSonic(GameObject sonic) {
        return true;
    }

    @Override
    boolean updateGhostSpawner(GameObject ghostSpawner) {
        return true;
    }

    @Override
    boolean updateChest(GameObject chest) {
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
    boolean updateAnkh(GameObject ankh) {
        return true;
    }

    @Override
    boolean updateFloatingItem(GameObject floatingItem) {
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
    boolean updateSeal(GameObject seal) {
        return true;
    }

    @Override
    boolean updateSlime(GameObject slime) {
        return true;
    }

    @Override
    boolean updateLavaRock(GameObject lavaRock) {
        return true;
    }

    @Override
    boolean updateSpriggan(GameObject spriggan) {
        return true;
    }

    @Override
    boolean updateHundun(GameObject hundun) {
        return true;
    }

    @Override
    boolean updatePan(GameObject pan) {
        return true;
    }

    @Override
    boolean updateHanuman(GameObject hanuman) {
        return true;
    }

    @Override
    boolean updateEnkidu(GameObject enkidu) {
        return true;
    }

    @Override
    boolean updateMarchosias(GameObject marchosias) {
        return true;
    }

    @Override
    boolean updateWitch(GameObject witch) {
        return true;
    }

    @Override
    boolean updateLizardMan(GameObject lizardMan) {
        return true;
    }

    @Override
    boolean updateChiYou(GameObject chiYou) {
        return true;
    }

    @Override
    boolean updateToujin(GameObject toujin) {
        return true;
    }

    @Override
    boolean updateIceWizard(GameObject iceWizard) {
        return true;
    }

    @Override
    boolean updateAnubis(GameObject anubis) {
        return true;
    }

    @Override
    boolean updateNinjaSpawner(GameObject ninjaSpawner) {
        return true;
    }

    @Override
    boolean updateAndras(GameObject andras) {
        return true;
    }

    @Override
    boolean updateChonchonSpawner(GameObject chonchonSpawner) {
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
    boolean updateSwordBird(GameObject swordBird) {
        return true;
    }

    @Override
    boolean updateElephant(GameObject elephant) {
        return true;
    }

    @Override
    boolean updateAmon(GameObject amon) {
        return true;
    }

    @Override
    boolean updateSatan(GameObject satan) {
        return true;
    }

    @Override
    boolean updateDevil(GameObject devil) {
        return true;
    }

    @Override
    boolean updateUmuDabrutu(GameObject umuDabrutu) {
        return true;
    }

    @Override
    boolean updateUrmahlullu(GameObject urmahlullu) {
        return true;
    }

    @Override
    boolean updateMushnahhu(GameObject mushnahhu) {
        return true;
    }

    @Override
    boolean updateUshum(GameObject ushum) {
        return true;
    }

    @Override
    boolean updateMushussu(GameObject mushussu) {
        return true;
    }

    @Override
    boolean updateMiniBoss(GameObject miniBoss) {
        return true;
    }

    @Override
    boolean updateTheBoss(GameObject theBoss) {
        return true;
    }

    @Override
    boolean updateFairyPoint(GameObject fairyPoint) {
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
    boolean updateEyeOfRetribution(GameObject eyeOfRetribution) {
        return true;
    }

    @Override
    boolean updateExtendableSpikes(GameObject extendableSpikes) {
        return true;
    }

    @Override
    boolean updateWarpPortal(GameObject warpPortal) {
        return true;
    }

    @Override
    boolean updateWarpDoor(GameObject warpDoor) {
        return true;
    }

    @Override
    boolean updateFallingRoom(GameObject crusher) {
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
    boolean updateUseItemDetector(GameObject useItemDetector) {
        return true;
    }

    @Override
    boolean updateScannable(GameObject scannable) {
        return true;
    }

    @Override
    boolean updateAutosave(GameObject autosave) {
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
    boolean updateAnimation(GameObject animation) {
        return true;
    }

    @Override
    boolean updateKeyFairySpot(GameObject keyFairySpot) {
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
    boolean updateHotSpring(GameObject hotSpring) {
        return true;
    }

    @Override
    boolean updateExplosion(GameObject explosion) {
        return true;
    }

    @Override
    boolean updateItemGive(GameObject itemGive) {
        return true;
    }

    @Override
    boolean updateSavePoint(GameObject savePoint) {
        return true;
    }

    @Override
    boolean updateGrailToggle(GameObject grailToggle) {
        return true;
    }

    @Override
    boolean updateMotherAnkh(GameObject motherAnkh) {
        return true;
    }

    @Override
    boolean updateMantraDetector(GameObject mantraDetector) {
        return true;
    }

    @Override
    boolean updateSnapshotsScan(GameObject snapshotsScan) {
        return true;
    }

    @Override
    boolean updateTransitionGate(GameObject transitionGate) {
        return true;
    }

    @Override
    boolean updateEscapeTimer(GameObject escapeTimer) {
        return true;
    }

    @Override
    boolean updateEscapeScreenShake(GameObject escapeScreenShake) {
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
    void addUntrackedCustomNoPositionZoneObjects(Zone zone) {
    }

    @Override
    void doUntrackedPostUpdates(){
        convertLampDetectors();
    }

    @Override
    void addTrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
    }

    @Override
    void addTrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
    }

    @Override
    void doTrackedPostUpdates(){
    }

    @Override
    void updateScreenExits(Screen screen) {
        if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 1) {
            // Prevent raindropping to the Lamp of Time shop
            screen.getScreenExit(ScreenExit.DOWN).setDestination(7, 4, 1);
        }
    }

    private void convertLampDetectors() {
        for(GameObject lemezaDetector : rcdData.getObjectsById(ObjectIdConstants.LemezaDetector)) {
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
