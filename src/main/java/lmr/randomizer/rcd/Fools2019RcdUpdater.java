package lmr.randomizer.rcd;

import lmr.randomizer.FlagConstants;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.AddObject;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.LocationCoordinateMapper;

import java.util.Arrays;

public class Fools2019RcdUpdater extends RcdUpdater {
    public Fools2019RcdUpdater(RcdData rcdData) {
        super(rcdData);
    }

    @Override
    boolean updatePot(GameObject pot) {
        return true;
    }

    @Override
    boolean updateBat(GameObject bat) {
        ObjectContainer objectContainer = bat.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(Settings.isRandomizeBosses()) {
            if(screen.getZoneIndex() == 0 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                for(TestByteOperation testByteOperation : bat.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == FlagConstants.AMPHISBAENA_ANKH_PUZZLE) {
                        testByteOperation.setIndex(FlagConstants.VIY_ANKH_PUZZLE);
                        testByteOperation.setValue((byte)4);
                    }
                }
            }
        }
        return true;
    }

    @Override
    boolean updateSkeleton(GameObject skeleton) {
        ObjectContainer objectContainer = skeleton.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(Settings.isRandomizeBosses()) {
            if(screen.getZoneIndex() == 5 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                for(TestByteOperation testByteOperation : skeleton.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == FlagConstants.VIY_ANKH_PUZZLE) {
                        testByteOperation.setIndex(FlagConstants.AMPHISBAENA_ANKH_PUZZLE);
                        testByteOperation.setValue((byte)5);
                    }
                }
            }
        }
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
        return true;
    }

    @Override
    boolean updateAnkh(GameObject ankh) {
        ObjectContainer objectContainer = ankh.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 4) {
            ankh.getArgs().set(24, (short)4);
            ankh.getArgs().set(25, (short)400);
            ankh.getArgs().set(26, (short)2316);
            ankh.getArgs().set(27, (short)0);
            ankh.getArgs().set(28, (short)4);
            ankh.getArgs().set(29, (short)400);
            ankh.getArgs().set(30, (short)2316);
        }
        if(Settings.isRandomizeBosses()) {
            if(screen.getZoneIndex() == 0) {
                // Amphisbaena ankh
                for(TestByteOperation testByteOperation : ankh.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == FlagConstants.AMPHISBAENA_ANKH_PUZZLE) {
                        testByteOperation.setIndex(FlagConstants.VIY_ANKH_PUZZLE);
                        testByteOperation.setValue((byte)4);
                    }
                }
                for(WriteByteOperation writeByteOperation : ankh.getWriteByteOperations()) {
                    if(writeByteOperation.getIndex() == FlagConstants.AMPHISBAENA_ANKH_PUZZLE) {
                        writeByteOperation.setIndex(FlagConstants.VIY_ANKH_PUZZLE);
                        writeByteOperation.setValue(5);
                    }
                }
            }
            else if(screen.getZoneIndex() == 3) {
                ankh.getArgs().set(24, (short)3);
                ankh.getArgs().set(25, (short)402);
                ankh.getArgs().set(26, (short)508);
                ankh.getArgs().set(28, (short)3);
                ankh.getArgs().set(29, (short)402);
                ankh.getArgs().set(30, (short)508);
            }
            else if(screen.getZoneIndex() == 5) {
                for(TestByteOperation testByteOperation : ankh.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == FlagConstants.VIY_ANKH_PUZZLE) {
                        testByteOperation.setIndex(FlagConstants.AMPHISBAENA_ANKH_PUZZLE);
                        testByteOperation.setValue((byte)5);
                    }
                }
                for(WriteByteOperation writeByteOperation : ankh.getWriteByteOperations()) {
                    if(writeByteOperation.getIndex() == FlagConstants.VIY_ANKH_PUZZLE) {
                        writeByteOperation.setIndex(FlagConstants.AMPHISBAENA_ANKH_PUZZLE);
                        writeByteOperation.setValue(6);
                    }
                }
            }
            else if(screen.getZoneIndex() == 6) {
                ankh.getArgs().set(24, (short)7);
                ankh.getArgs().set(25, (short)0);
                ankh.getArgs().set(26, (short)1500);
                ankh.getArgs().set(27, (short)2);
            }
        }
        return true;
    }

    @Override
    boolean updateFloatingItem(GameObject floatingItem) {
        return true;
    }

    @Override
    boolean updateTrapdoor(GameObject trapdoor) {
        return true;
    }

    @Override
    boolean updateSeal(GameObject seal) {
        return true;
    }

    @Override
    boolean updateSlime(GameObject slime) {
        ObjectContainer objectContainer = slime.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(Settings.isRandomizeBosses()) {
            if(screen.getZoneIndex() == 5 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                for(TestByteOperation testByteOperation : slime.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == FlagConstants.VIY_ANKH_PUZZLE) {
                        testByteOperation.setIndex(FlagConstants.AMPHISBAENA_ANKH_PUZZLE);
                        testByteOperation.setValue((byte)5);
                    }
                }
            }
        }
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
        return true;
    }

    @Override
    boolean updateGraphicsTextureDraw(GameObject graphicsTextureDraw) {
        ObjectContainer objectContainer = graphicsTextureDraw.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 4 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
            // Bahamut wall graphic that gets removed after the fight.
            if(graphicsTextureDraw.getTestByteOperations().size() == 1) {
                TestByteOperation testByteOperation = graphicsTextureDraw.getTestByteOperations().get(0);
                if(testByteOperation.getIndex() == FlagConstants.BAHAMUT_STATE
                        && ByteOp.FLAG_LTEQ.equals(testByteOperation.getOp())
                        && testByteOperation.getValue() == (byte)1) {
                    testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                    testByteOperation.setValue((byte)2);
                }
            }
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
        return true;
    }

    @Override
    boolean updateBlockButton(GameObject blockButton) {
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
        ObjectContainer objectContainer = transitionGate.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == 7) {
            if(screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                // Twin Labyrinths ladder that normally goes to Ellmac's room
                if(Settings.isRandomizeBosses()) {
                    // Extinction/Palenque's room
                    transitionGate.getArgs().set(0, (short)6);
                    transitionGate.getArgs().set(1, (short)9);
                    transitionGate.getArgs().set(2, (short)1);
                    transitionGate.getArgs().set(3, (short)300);
                    transitionGate.getArgs().set(4, (short)380);
                }
            }
        }
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
        if(zoneIndex == 0) {
            if(roomIndex == 8 && screenIndex == 0) {
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
                            Arrays.asList(new TestByteOperation(FlagConstants.VIY_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 4),
                                    new TestByteOperation(FlagConstants.AMPHISBAENA_STATE, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
                }
            }
        }
        else if(zoneIndex == 3) {
            if(roomIndex == 8 && screenIndex == 0) {
                // Ellmac boss room
                if(Settings.isRandomizeBosses()) {
                    GameObject warp = AddObject.addWarp(screen, 0, 420, 32, 4, 3, 4, 2, 100, 160);

                    TestByteOperation warpTest = new TestByteOperation();
                    warpTest.setIndex(FlagConstants.ELLMAC_STATE);
                    warpTest.setValue((byte) 2);
                    warpTest.setOp(ByteOp.FLAG_NOT_EQUAL);
                    warp.getTestByteOperations().add(warpTest);
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
                            Arrays.asList(new TestByteOperation(FlagConstants.AMPHISBAENA_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 5),
                                    new TestByteOperation(FlagConstants.VIY_STATE, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
                }
            }
        }
        else if(zoneIndex == 6) {
            if(Settings.isRandomizeBosses()) {
                if(roomIndex == 9 && screenIndex == 1) {
                    AddObject.addTwinLabsDoor(screen);
                }
            }
        }
        else if(zoneIndex == 18) {
            if (roomIndex == 3 && screenIndex == 0) {
                AddObject.addWarp(screen, 600, 440, 32, 3, 18, 3, 1, 150, 72);
            }
        }
    }

    @Override
    void addUntrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == 3) {
            if(Settings.isRandomizeBosses()) {
                if(roomIndex == 4 && screenIndex == 2) {
                    AddObject.addBossTimer(screen, FlagConstants.ELLMAC_STATE, 0x2d8);
                }
                else if(roomIndex == 8 && screenIndex == 0) {
                    AddObject.addSphinxRemovalTimer(screen);
                }
            }
        }
        else if(zoneIndex == 4) {
            if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addBossTimer(screen, FlagConstants.BAHAMUT_STATE, 0x2d9);
            }
        }
        else if(zoneIndex == 6) {
            if(roomIndex == 9 && screenIndex == 1) {
                if(Settings.isRandomizeBosses()) {
                    AddObject.addTwinLabsPoisonTimerRemoval(screen, true);
                }
            }
        }
        else if(zoneIndex == 7) {
            if(Settings.isRandomizeBosses()) {
                if(roomIndex == 0 && screenIndex == 0) {
                    AddObject.addBossTimer(screen, FlagConstants.PALENQUE_STATE, 0x2db);
                }
            }
        }
    }

    @Override
    void addUntrackedCustomNoPositionZoneObjects(Zone zone) {
    }

    @Override
    void doUntrackedPostUpdates(){
    }

    @Override
    void addTrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
    }

    @Override
    void addTrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
    }

    @Override
    protected void doTrackedPostUpdates(){
    }

    @Override
    void updateScreenExits(Screen screen) {
        if(screen.getZoneIndex() == 4 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
            screen.getScreenExit(ScreenExit.LEFT).setDestination(LocationCoordinateMapper.getStartingZone(), LocationCoordinateMapper.getStartingRoom(), LocationCoordinateMapper.getStartingScreen());
        }

        if(Settings.isRandomizeBosses()) {
            if(screen.getZoneIndex() == 0) {
                if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                    screen.getScreenExit(ScreenExit.DOWN).setDestination(5, 8, 1);
                }
                else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                    screen.getScreenExit(ScreenExit.UP).setDestination(5, 8, 0);
                }
            }
            else if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                screen.getScreenExit(ScreenExit.DOWN).setDestination(3, 4, 2);
            }
            else if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                screen.getScreenExit(ScreenExit.UP).setDestination(6, 9, 1);
            }
            else if(screen.getZoneIndex() == 5) {
                if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                    screen.getScreenExit(ScreenExit.DOWN).setDestination(0, 8, 1);
                }
                else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                    screen.getScreenExit(ScreenExit.UP).setDestination(0, 8, 0);
                }
            }
        }
    }
}
