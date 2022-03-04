package lmr.randomizer.rcd.updater;

import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.ItemConstants;
import lmr.randomizer.Settings;
import lmr.randomizer.util.ZoneConstants;
import lmr.randomizer.update.AddObject;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.rcd.RcdFileData;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.util.LocationCoordinateMapper;

import java.util.ArrayList;
import java.util.Arrays;

public class Fools2021RcdUpdater extends RcdUpdater {
    public Fools2021RcdUpdater(RcdFileData rcdFileData, DatFileData datFileData) {
        super(rcdFileData, datFileData);
    }

    @Override
    boolean updateBat(Bat bat) {
        ObjectContainer objectContainer = bat.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        // Move bats at Surface Sacred Orb
        if(screen.getZoneIndex() == 1 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
            bat.setY(bat.getY() - 120);
        }
        return true;
    }

    @Override
    boolean updateDais(GameObject dais) {
        ObjectContainer objectContainer = dais.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        updateDaisForGiantsPuzzle(dais, screen);

        if (screen.getZoneIndex() == 10 && screen.getRoomIndex() == 9 && screen.getScreenIndex() == 0) {
            // Adjust dais for Illusion skeleton
            if(dais.getY() == 340) {
                dais.getTestByteOperations().clear();
                dais.getWriteByteOperations().clear();

                if(dais.getX() == 280) {
                    dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_0, ByteOp.FLAG_EQUALS, 0));
                    dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.ILLUSION_PROGRESS_SKELETON_DAIS_TO_ELEVATOR, ByteOp.FLAG_EQUALS, 0));
                    dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_0, ByteOp.ASSIGN_FLAG, 1));
                    dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.ILLUSION_PROGRESS_SKELETON_DAIS_TO_ELEVATOR, ByteOp.ASSIGN_FLAG, 1));
                }
                else {
                    dais.getArgs().set(2, (short)0);
                }
            }
        }
        return true;
    }

    private void updateDaisForGiantsPuzzle(GameObject dais, Screen screen) {
        if(screen.getZoneIndex() == 2) {
            // Adjust dais for "foot of Futo" puzzle. Zebu handled separately.
            if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                if(dais.getX() == 140) {
                    if("Migela".equals(Settings.getCurrentGiant())) {
                        dais.getTestByteOperations().clear();
                        dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 0));

                        dais.getWriteByteOperations().clear();
                        dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.ASSIGN_FLAG, 1));
                        dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));
                    }
                    else {
                        dais.getArgs().set(1, (short)10); // Falling speed
                    }
                }
                else {
                    // Futo
                    dais.getArgs().set(1, (short)10); // Falling speed

                    dais.getTestByteOperations().clear();
                    dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_8, ByteOp.FLAG_EQUALS, 0));

                    dais.getWriteByteOperations().clear();
                    dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_8, ByteOp.ASSIGN_FLAG, 1));
                }
            }
            else if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                if("Bado".equals(Settings.getCurrentGiant())) {
                    dais.getTestByteOperations().clear();
                    dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 0));

                    dais.getWriteByteOperations().clear();
                    dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.ASSIGN_FLAG, 1));
                    dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));
                }
                else {
                    dais.getArgs().set(1, (short)10); // Falling speed
                }
            }
            else if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 2) {
                if(dais.getY() == 340) {
                    // Make sure not to get the Sakit ankh unlock dais
                    if("Ledo".equals(Settings.getCurrentGiant())) {
                        dais.getTestByteOperations().clear();
                        dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 0));

                        dais.getWriteByteOperations().clear();
                        dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.ASSIGN_FLAG, 1));
                        dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));
                    }
                    else {
                        dais.getArgs().set(1, (short)10); // Falling speed
                    }
                }
            }
            else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                if("Abuto".equals(Settings.getCurrentGiant())) {
                    dais.getTestByteOperations().clear();
                    dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 0));

                    dais.getWriteByteOperations().clear();
                    dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.ASSIGN_FLAG, 1));
                    dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));
                }
                else {
                    dais.getArgs().set(1, (short)10); // Falling speed
                }
            }
            else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                if(dais.getX() == 760) {
                    if("Sakit".equals(Settings.getCurrentGiant())) {
                        dais.getTestByteOperations().clear();
                        dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 0));
                        dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.SAKIT_ANKH_PUZZLE, ByteOp.FLAG_NOT_EQUAL, 1));

                        dais.getWriteByteOperations().clear();
                        dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.ASSIGN_FLAG, 1));
                        dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));
                    }
                    else {
                        dais.getArgs().set(1, (short)10); // Falling speed
                    }
                }
                else { //if(obj.getX() == 1140) {
                    if("Ji".equals(Settings.getCurrentGiant())) {
                        dais.getTestByteOperations().clear();
                        dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 0));
                        dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.SAKIT_ANKH_PUZZLE, ByteOp.FLAG_NOT_EQUAL, 1));

                        dais.getWriteByteOperations().clear();
                        dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.ASSIGN_FLAG, 1));
                        dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));
                    }
                    else {
                        dais.getArgs().set(1, (short)10); // Falling speed
                    }
                }
            }
            else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 2) {
                if("Ribu".equals(Settings.getCurrentGiant())) {
                    dais.getTestByteOperations().clear();
                    dais.getTestByteOperations().add(new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 0));

                    dais.getWriteByteOperations().clear();
                    dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.ASSIGN_FLAG, 1));
                    dais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));
                }
                else {
                    dais.getArgs().set(1, (short)10); // Falling speed
                }
            }
        }
    }

    @Override
    boolean updateFlagTimer(GameObject flagTimer) {
        if (flagTimer.getObjectContainer() instanceof Zone) {
            boolean found = false;
            for(WriteByteOperation flagUpdate : flagTimer.getWriteByteOperations()) {
                if(flagUpdate.getIndex() == FlagConstants.EXTINCTION_TEMP_LIGHT) {
                    found = true;
                    break;
                }
            }
            if(found) {
                flagTimer.getArgs().set(0, (short)5); // 5 seconds
            }
        }

        if(Settings.isReducedBossCount() && Settings.getCurrentBossCount() != 8) {
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
        return true;
    }

    @Override
    boolean updateRoomSpawner(GameObject roomSpawner) {
        ObjectContainer objectContainer = roomSpawner.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 1 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
            // Surface Sacred Orb/bat trap coverings
            if(roomSpawner.getWriteByteOperations().isEmpty()) {
                roomSpawner.getTestByteOperations().clear();
                roomSpawner.getTestByteOperations().add(new TestByteOperation(FlagConstants.SURFACE_WATERFALL_WALL_BATS, ByteOp.FLAG_EQUALS, 0));
                roomSpawner.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SURFACE_WATERFALL_WALL_BATS, ByteOp.ASSIGN_FLAG, 2));
            }
            else {
                roomSpawner.getTestByteOperations().clear();
                roomSpawner.getTestByteOperations().add(new TestByteOperation(FlagConstants.SURFACE_PUZZLE_WATERFALL_WALL_SACRED_ORB, ByteOp.FLAG_EQUALS, 0));
                roomSpawner.getWriteByteOperations().clear();
            }
        }
        else if(screen.getZoneIndex() == 11 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
            // Graveyard hot spring
            for (TestByteOperation flagTest : roomSpawner.getTestByteOperations()) {
                if(flagTest.getIndex() == FlagConstants.GRAVEYARD_HOT_SPRING) {
                    if(flagTest.getValue() == 1) {
                        return false;
                    }
                    else {
                        flagTest.setValue((byte)1);
                    }
                }
            }
        }
        return true;
    }

    @Override
    boolean updateHitbox(Hitbox hitbox) {
        ObjectContainer objectContainer = hitbox.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == 12) {
            if (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0
                    || screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0
                    || screen.getRoomIndex() == 5 && screen.getScreenIndex() == 1
                    || screen.getRoomIndex() == 6 && screen.getScreenIndex() == 0) {
                for (WriteByteOperation writeByteOperation : hitbox.getWriteByteOperations()) {
                    if (writeByteOperation.getIndex() == FlagConstants.SCREEN_FLAG_A) {
                        // Retribution trigger wall in Moonlight pyramid
                        return false;
                    }
                }
            }
        }
        else if(screen.getZoneIndex() == 1 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
            // Surface walls
            for (TestByteOperation testByteOperation: hitbox.getTestByteOperations()) {
                if (testByteOperation.getIndex() == FlagConstants.SURFACE_PUZZLE_WATERFALL_WALL_SACRED_ORB) {
                    testByteOperation.setIndex(FlagConstants.SURFACE_WATERFALL_WALL_BATS);
                }
                else if (testByteOperation.getIndex() == FlagConstants.SURFACE_WATERFALL_WALL_BATS) {
                    testByteOperation.setIndex(FlagConstants.SURFACE_PUZZLE_WATERFALL_WALL_SACRED_ORB);
                }
                else if (testByteOperation.getIndex() == FlagConstants.SCREEN_FLAG_B) {
                    testByteOperation.setIndex(FlagConstants.SCREEN_FLAG_D);
                }
                else if (testByteOperation.getIndex() == FlagConstants.SCREEN_FLAG_D) {
                    testByteOperation.setIndex(FlagConstants.SCREEN_FLAG_B);
                }
            }
            for (WriteByteOperation writeByteOperation : hitbox.getWriteByteOperations()) {
                if (writeByteOperation.getIndex() == FlagConstants.SURFACE_PUZZLE_WATERFALL_WALL_SACRED_ORB) {
                    writeByteOperation.setIndex(FlagConstants.SURFACE_WATERFALL_WALL_BATS);
                }
                else if (writeByteOperation.getIndex() == FlagConstants.SURFACE_WATERFALL_WALL_BATS) {
                    writeByteOperation.setIndex(FlagConstants.SURFACE_PUZZLE_WATERFALL_WALL_SACRED_ORB);
                }
                else if (writeByteOperation.getIndex() == FlagConstants.SCREEN_FLAG_B) {
                    writeByteOperation.setIndex(FlagConstants.SCREEN_FLAG_D);
                }
                else if (writeByteOperation.getIndex() == FlagConstants.SCREEN_FLAG_D) {
                    writeByteOperation.setIndex(FlagConstants.SCREEN_FLAG_B);
                }
            }
        }
        return true;
    }

    @Override
    boolean updateSteam(GameObject steam) {
        ObjectContainer objectContainer = steam.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 11 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
            for(TestByteOperation testByteOperation : steam.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.GRAVEYARD_HOT_SPRING) {
                    testByteOperation.setValue((byte)(testByteOperation.getValue() == 1 ? 0 : 1));
                }
            }
        }
        return true;
    }

    @Override
    boolean updateChest(GameObject chest) {
        ObjectContainer objectContainer = chest.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == 0 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
            return false; // Remove Treasures chest from Guidance.
        }
        else if (screen.getZoneIndex() == 1 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
            // Move Sacred Orb chest on Surface.
            chest.setY(chest.getY() + 120);
        }
        else if (screen.getZoneIndex() == 5 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
            // Swap flags for Ice Cape chest in Inferno Cavern.
            for(WriteByteOperation writeByteOperation : chest.getWriteByteOperations()) {
                if(writeByteOperation.getIndex() == FlagConstants.INFERNO_PUZZLE_ICE_CAPE_CHEST) {
                    writeByteOperation.setIndex(FlagConstants.INFERNO_PUZZLE_FLARE_GUN);
                }
            }
        }
        else if (screen.getZoneIndex() == 12 && screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
            return false; // Remove Philosopher's Ocarina chest from Moonlight.
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
    boolean updateAnkh(GameObject ankh) {
        ObjectContainer objectContainer = ankh.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        // Changes for Amphisbaena ankh
        if (screen.getZoneIndex() == 0 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
            for(TestByteOperation testByteOperation : ankh.getTestByteOperations()) {
                if (testByteOperation.getIndex() == FlagConstants.HARDMODE && ByteOp.FLAG_EQUALS.equals(testByteOperation.getOp()) && testByteOperation.getValue() == 2) {
                    // Delete the hardmode variant of Amphisbaena ankh
                    return false;
                }
            }
            ankh.getTestByteOperations().add(new TestByteOperation(FlagConstants.SACRED_ORB_COUNT, ByteOp.FLAG_EQUALS, 0));

            // Reduce health
            ankh.getArgs().set(2, (short)4);
            // Buff damage
            ankh.getArgs().set(3, (short)32);
            ankh.getArgs().set(5, (short)32);
        }
        return true;
    }

    @Override
    boolean updateFloatingItem(GameObject floatingItem) {
        if(floatingItem.getArgs().get(1) == 7) {
            // Remove empowered Key Sword
            return false;
        }

        ObjectContainer objectContainer = floatingItem.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == 12 && screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
            // Philosopher's Ocarina
            convertToChest(floatingItem);
        }
        return true;
    }

    @Override
    boolean updateUmuDabrutu(GameObject umuDabrutu) {
        umuDabrutu.getArgs().set(3, (short)30); // HP
        return true;
    }

    @Override
    boolean updateUrmahlullu(GameObject urmahlullu) {
        urmahlullu.getArgs().set(3, (short)30); // HP
        return true;
    }

    @Override
    boolean updateMushnahhu(GameObject mushnahhu) {
        mushnahhu.getArgs().set(3, (short)1); // HP per worm
//        mushnahhu.getArgs().set(4, (short)1); // Contact damage for worms
        return true;
    }

    @Override
    boolean updateEyeOfRetribution(GameObject eyeOfRetribution) {
        ObjectContainer objectContainer = eyeOfRetribution.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == 12) {
            if(screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                return false;
            }
            else if(screen.getRoomIndex() == 4 && screen.getScreenIndex() == 1) {
                return false;
            }
            else if(screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
                return false;
            }
            else if(screen.getRoomIndex() == 5 && screen.getScreenIndex() == 1) {
                return false;
            }
            else if(screen.getRoomIndex() == 6 && screen.getScreenIndex() == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    boolean updateExtendableSpikes(GameObject extendableSpikes) {
        ObjectContainer objectContainer = extendableSpikes.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        return updateExtendableSpikesForGiantsPuzzle(extendableSpikes, screen);
    }

    private boolean updateExtendableSpikesForGiantsPuzzle(GameObject extendableSpikes, Screen screen) {
        // Adjust spikes for "foot of Futo" puzzle, if using a different giant. Zebu and Futo handled separately.
        if(screen.getZoneIndex() == 2) {
            if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                if("Migela".equals(Settings.getCurrentGiant())) {
                    return false;
                }
                else {
                    extendableSpikes.getArgs().set(4, (short)0); // Activation delay
                }
            }
            else if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                if("Bado".equals(Settings.getCurrentGiant())) {
                    return false;
                }
                else {
                    extendableSpikes.getArgs().set(4, (short)0); // Activation delay
                }
            }
            else if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 2) {
                if("Ledo".equals(Settings.getCurrentGiant())) {
                    return false;
                }
                else {
                    extendableSpikes.getArgs().set(4, (short)0); // Activation delay
                }
            }
            else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                if("Abuto".equals(Settings.getCurrentGiant())) {
                    return false;
                }
                else {
                    extendableSpikes.getArgs().set(4, (short)0); // Activation delay
                }
            }
            else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
                if(extendableSpikes.getX() == 740) {
                    if("Sakit".equals(Settings.getCurrentGiant())) {
                        return false;
                    }
                    else {
                        extendableSpikes.getArgs().set(4, (short)0); // Activation delay
                    }
                }
                else { //if(obj.getX() == 1120) {
                    if("Ji".equals(Settings.getCurrentGiant())) {
                        return false;
                    }
                    else {
                        extendableSpikes.getArgs().set(4, (short)0); // Activation delay
                    }
                }
            }
            else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 2) {
                if("Ribu".equals(Settings.getCurrentGiant())) {
                    return false;
                }
                else {
                    extendableSpikes.getArgs().set(4, (short)0); // Activation delay
                }
            }
        }
        return true;
    }

    @Override
    boolean updateWarpPortal(GameObject warpPortal) {
        ObjectContainer objectContainer = warpPortal.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 1) {
            // Loop Sun warp
            warpPortal.getArgs().set(0, (short)3);
            warpPortal.getArgs().set(1, (short)6);
            warpPortal.getArgs().set(2, (short)0);
            warpPortal.getArgs().set(3, (short)460);
            warpPortal.getArgs().set(4, (short)152);
        }
        if(screen.getZoneIndex() == 16 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 2) {
            // Birth to Moonlight
            warpPortal.getArgs().set(0, (short)12);
            warpPortal.getArgs().set(1, (short)5);
            warpPortal.getArgs().set(2, (short)1);
            warpPortal.getArgs().set(3, (short)300);
            warpPortal.getArgs().set(4, (short)312);
            warpPortal.getTestByteOperations().add(new TestByteOperation(FlagConstants.BIRTH_GANESHA_SCANNED, ByteOp.FLAG_GT, 0));
        }
        return true;
    }

    @Override
    boolean updateFallingRoom(GameObject crusher) {
        ObjectContainer objectContainer = crusher.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        // Remove Treasures chest falling animation from Guidance.
        if (screen.getZoneIndex() == 0 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
            return false;
        }
        return true;
    }

    @Override
    boolean updateScannable(Scannable scannable) {
        ObjectContainer objectContainer = scannable.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;

        int languageBlock = scannable.getArgs().get(0);
        int zoneIndex = screen.getZoneIndex();
        boolean frontsideGrailScan = (languageBlock == 41 || languageBlock == 75 || languageBlock == 104 || languageBlock == 136
                || languageBlock == 149 || languageBlock == 170 || languageBlock == 188 || languageBlock == 221)
                || (languageBlock == 231 && zoneIndex == 9);
        boolean backsideGrailScan = languageBlock == 250 || languageBlock == 275 || languageBlock == 291 || languageBlock == 305
                || languageBlock == 323 || languageBlock == 339 || languageBlock == 206 || languageBlock == 358
                || (languageBlock == 231 && zoneIndex != 9);

        if(frontsideGrailScan || backsideGrailScan) {
            // Swap out original for custom flags
            for (TestByteOperation testByteOperation : scannable.getTestByteOperations()) {
                if (testByteOperation.getIndex() == LocationCoordinateMapper.getOriginalGrailFlag(zoneIndex, frontsideGrailScan)) {
                    testByteOperation.setIndex(LocationCoordinateMapper.getGrailFlag(zoneIndex, frontsideGrailScan));
                }
            }
            for (WriteByteOperation writeByteOperation : scannable.getWriteByteOperations()) {
                if (writeByteOperation.getIndex() == LocationCoordinateMapper.getOriginalGrailFlag(zoneIndex, frontsideGrailScan)) {
                    writeByteOperation.setIndex(LocationCoordinateMapper.getGrailFlag(zoneIndex, frontsideGrailScan));
                }
            }
        }
        return true;
    }

    @Override
    boolean updateAutosave(GameObject autosave) {
        int languageBlock = autosave.getArgs().get(0);
        int zoneIndex = ((Screen)autosave.getObjectContainer()).getZoneIndex();
        boolean front = (languageBlock == 41 || languageBlock == 75 || languageBlock == 104 || languageBlock == 136
                || languageBlock == 149 || languageBlock == 170 || languageBlock == 188 || languageBlock == 221)
                || (languageBlock == 231 && zoneIndex == 9);
        // Swap out original for custom flags
        for (TestByteOperation testByteOperation : autosave.getTestByteOperations()) {
            if (testByteOperation.getIndex() == LocationCoordinateMapper.getOriginalGrailFlag(zoneIndex, front)) {
                testByteOperation.setIndex(LocationCoordinateMapper.getGrailFlag(zoneIndex, front));
            }
        }
        for (WriteByteOperation writeByteOperation : autosave.getWriteByteOperations()) {
            if (writeByteOperation.getIndex() == LocationCoordinateMapper.getOriginalGrailFlag(zoneIndex, front)) {
                writeByteOperation.setIndex(LocationCoordinateMapper.getGrailFlag(zoneIndex, front));
            }
        }
        return true;
    }

    @Override
    boolean updateHotSpring(GameObject hotSpring) {
        ObjectContainer objectContainer = hotSpring.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 11 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
            for(TestByteOperation testByteOperation : hotSpring.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.GRAVEYARD_HOT_SPRING) {
                    testByteOperation.setValue((byte)(testByteOperation.getValue() == 1 ? 0 : 1));
                }
            }
        }
        return true;
    }

    @Override
    boolean updateSnapshotsScan(GameObject snapshotsScan) {
        if(snapshotsScan.getArgs().get(3) == ItemConstants.SOFTWARE_MANTRA) {
            snapshotsScan.getTestByteOperations().add(new TestByteOperation(FlagConstants.EXTINCTION_PERMA_LIGHT, ByteOp.FLAG_EQUALS, 3));
        }
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
                // Loop the Twin Labyrinths ladder that normally goes to Ellmac's room
                transitionGate.getArgs().set(0, (short)7);
                transitionGate.getArgs().set(1, (short)0);
                transitionGate.getArgs().set(2, (short)0);
                transitionGate.getArgs().set(3, (short)480);
                transitionGate.getArgs().set(4, (short)392);
            }
        }
        return true;
    }

    @Override
    boolean updateEscapeTimer(GameObject escapeTimer) {
        // Change flags to not kill the player.
        escapeTimer.getArgs().set(10, (short)2746);
        escapeTimer.getArgs().set(11, (short)2747);
        return true;
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

    @Override
    void addUntrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        AddObject.addCustomItemGives(screen, ItemConstants.SOFTWARE_XMAILER,
                FlagConstants.CUSTOM_FOOLS2021_ESCAPE_TIMER_STATE, 0, FlagConstants.CUSTOM_FOOLS2021_XMAILER_RECEIVED, 1);

        if(zoneIndex == 0) {
            if(roomIndex == 0 && screenIndex == 0) {
                // Treasures room
                AddObject.addWarpDoor(screen, 320, 160, 3, 6, 0, 460, 152,
                        Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_GTEQ, 1)));
                AddObject.addPushableBlock(screen, 320, 180, Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 1)));
                AddObject.addPushableBlock(screen, 320, 180, Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 2)));
                AddObject.addPushableBlock(screen, 320, 180, Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 3)));
                AddObject.addPushableBlock(screen, 320, 180, Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 4)));

                // After the falling block sequence, the blocks can be replaced by stationary ones.
                AddObject.addPushableBlock(screen, 320, 200, Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 5), new TestByteOperation(FlagConstants.SCREEN_FLAG_D, ByteOp.FLAG_EQUALS, 0)));
                AddObject.addPushableBlock(screen, 320, 240, Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 5), new TestByteOperation(FlagConstants.SCREEN_FLAG_D, ByteOp.FLAG_EQUALS, 0)));
                AddObject.addPushableBlock(screen, 320, 280, Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 5), new TestByteOperation(FlagConstants.SCREEN_FLAG_D, ByteOp.FLAG_EQUALS, 0)));
                AddObject.addPushableBlock(screen, 320, 320, Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 5), new TestByteOperation(FlagConstants.SCREEN_FLAG_D, ByteOp.FLAG_EQUALS, 0)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                // Amphisbaena boss room
                AddObject.addEyeOfDivineRetribution(screen, 300, 500);
                AddObject.addHitbox(screen, 0, 480, 32, 24, new ArrayList<>(0), Arrays.asList(new WriteByteOperation(FlagConstants.SCREEN_FLAG_A, ByteOp.ADD_FLAG, 1)));

                for (int i = 1; i <= 10; i++) {
                    AddObject.addAmphisbaenaAnkh(screen, 300, 880, 32 * (i + 1),
                            Arrays.asList(new TestByteOperation(FlagConstants.AMPHISBAENA_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 5),
                                    new TestByteOperation(FlagConstants.SACRED_ORB_COUNT, ByteOp.FLAG_EQUALS, i)));
                }
            }
        }
        else if(zoneIndex == 2) {
            if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addZebuDais(screen);
            }
            if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addExtendingSpikes(screen, 320, 360, FlagConstants.SCREEN_FLAG_8);
            }
        }
        else if(zoneIndex == 3) {
            if(roomIndex == 0 && screenIndex == 0) {
                // Sun room with ladder for up transition
                AddObject.addWarp(screen, 0, 80, 32, 20, 9, 9, 0, 300, 392)
                        .getTestByteOperations().add(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, (byte)0));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                // Sun grail screen
                AddObject.addWarp(screen, 640, 80, 32, 20, 9, 9, 0, 300, 392)
                        .getTestByteOperations().add(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, (byte)0));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                // Room right of Buer
                AddObject.addWarp(screen, 0, 0, 32, 24, 9, 9, 0, 300, 392)
                        .getTestByteOperations().add(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, (byte)0));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                // Room below grail / above Mulbruk
                AddObject.addWarp(screen, 640, 0, 32, 24, 9, 9, 0, 300, 392)
                        .getTestByteOperations().add(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, (byte)0));
            }
            else if(roomIndex == 4 && screenIndex == 3) {
                // Sun room with Sacred Orb chest
                AddObject.addPot(screen, 40, 480, PotGraphic.SUN, DropType.NOTHING, 0, new ArrayList<>(0), new ArrayList<>(0));
                AddObject.addPot(screen, 40, 640, PotGraphic.SUN, DropType.NOTHING, 0, new ArrayList<>(0), new ArrayList<>(0));
                AddObject.addPot(screen, 40, 800, PotGraphic.SUN, DropType.NOTHING, 0, new ArrayList<>(0), new ArrayList<>(0));

                AddObject.addLemezaDetector(screen, 0, 600, 6, 4,
                        Arrays.asList(new TestByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1)));
                AddObject.addPot(screen, 40, 480, PotGraphic.SUN, DropType.NOTHING, 0, Arrays.asList(new TestByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.FLAG_EQUALS, 1)), new ArrayList<>(0));
            }
            else if(roomIndex == 4 && screenIndex == 5) {
                // Sun room with Bronze Mirror chest
                AddObject.addWarp(screen, 1280, 480, 32, 24, 12, 8, 0, 20, 232)
                        .getTestByteOperations().add(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, (byte)0));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                // Sun room with Bronze Mirror chest
                AddObject.addWarp(screen, 0, 0, 28, 24, 12, 8, 0, 20, 232)
                        .getTestByteOperations().add(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, (byte)0));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                // Backside door room
                AddObject.addLampStation(screen, 260, 60);
            }
        }
        else if(zoneIndex == 4) {
            if (roomIndex == 8 && screenIndex == 1) {
                // Transition to Ellmac
                AddObject.addWarp(screen, 0, 440 + 480, 32, 4, 3, 8, 0, 360, 40)
                        .getTestByteOperations().add(new TestByteOperation(FlagConstants.ELLMAC_STATE, ByteOp.FLAG_NOT_EQUAL, (byte) 2));
            }
        }
        else if(zoneIndex == 5) {
            if(roomIndex == 4) {
                if(screenIndex == 0) {
                    AddObject.addInfernoFakeWeaponCover(screen,
                            Arrays.asList(new TestByteOperation(FlagConstants.INFERNO_PUZZLE_ICE_CAPE_CHEST, ByteOp.FLAG_LT, 2)));
                }
            }
        }
        else if(zoneIndex == 7) {
            if(roomIndex == 3 && screenIndex == 2) {
                AddObject.addLittleBrotherWeightWaster(screen);
            }
        }
        else if(zoneIndex == 8) {
            if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addFloatingItem(screen, 860, 400, ItemConstants.SPAULDER, true,
                        Arrays.asList(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_GT, 0),
                                new TestByteOperation(FlagConstants.CUSTOM_FOOLS2021_ENDLESS_5F_SPAULDER, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.CUSTOM_FOOLS2021_ENDLESS_5F_SPAULDER, ByteOp.ASSIGN_FLAG, 1)));
            }
        }
        else if(zoneIndex == 9) {
            if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addFloatingItem(screen, 940, 240, ItemConstants.SPAULDER, true,
                        Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 0),
                                new TestByteOperation(FlagConstants.CUSTOM_FOOLS2021_TREASURY_SPAULDER, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.CUSTOM_FOOLS2021_TREASURY_SPAULDER, ByteOp.ASSIGN_FLAG, 1)));
            }
        }
        else if(zoneIndex == 17) {
            if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addExplosion(screen, 700, 320, FlagConstants.SCREEN_FLAG_8, 50, true);
                AddObject.addExplosion(screen, 1200, 400, FlagConstants.SCREEN_FLAG_9, 50, true);
            }
        }
    }

    @Override
    void addUntrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == LocationCoordinateMapper.getStartingZone()
                && roomIndex == LocationCoordinateMapper.getStartingRoom()
                && screenIndex == LocationCoordinateMapper.getStartingScreen()) {
            AddObject.addEscapeTimer(screen);
        }

        if(zoneIndex == 0) {
            if(roomIndex == 0 && screenIndex == 0) {
                // Treasures room
                AddObject.addFramesTimer(screen, 24,
                        Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 1)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.ASSIGN_FLAG, 2)));
                AddObject.addFramesTimer(screen, 20,
                        Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 2)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.ASSIGN_FLAG, 3)));
                AddObject.addFramesTimer(screen, 12,
                        Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 3)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.ASSIGN_FLAG, 4)));
                AddObject.addFramesTimer(screen, 6,
                        Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_EQUALS, 4)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.ASSIGN_FLAG, 5)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                // Treasures room - ensure the process of falling blocks triggered by Pepper is reset if unfinished.
                AddObject.addFramesTimer(screen, 0,
                        Arrays.asList(new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_GT, 0),
                                new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.FLAG_LT, 5)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.GUIDANCE_PUZZLE_TREASURES_CHEST, ByteOp.ASSIGN_FLAG, 1)));
            }

            if(roomIndex == 8 && screenIndex == 0) {
                // Make sure Amphisbaena is appropriately buffed.
                AddObject.addSacredOrbCountTimers(screen);
            }
        }
        else if(zoneIndex == 2) {
            addSuccessSoundsForGiantsPuzzle(screen, roomIndex, screenIndex);
        }
    }

    private void addSuccessSoundsForGiantsPuzzle(Screen screen, int roomIndex, int screenIndex) {
        // Add success sound to any screen except Futo/Migela (who already have one) or Zebu (who is addressed separately)
        if(roomIndex == 7 && screenIndex == 1) {
            if("Bado".equals(Settings.getCurrentGiant())) {
                AddObject.addSuccessSound(screen, Arrays.asList(
                        new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 1),
                        new TestByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.FLAG_EQUALS, 1)));
            }
        }
        else if(roomIndex == 7 && screenIndex == 2) {
            if("Ledo".equals(Settings.getCurrentGiant())) {
                AddObject.addSuccessSound(screen, Arrays.asList(
                        new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 1),
                        new TestByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.FLAG_EQUALS, 1)));
            }
        }
        else if(roomIndex == 8 && screenIndex == 0) {
            if("Abuto".equals(Settings.getCurrentGiant())) {
                AddObject.addSuccessSound(screen, Arrays.asList(
                        new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 1),
                        new TestByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.FLAG_EQUALS, 1)));
            }
        }
        else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
            if("Sakit".equals(Settings.getCurrentGiant()) || "Ji".equals(Settings.getCurrentGiant())) {
                AddObject.addSuccessSound(screen, Arrays.asList(
                        new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 1),
                        new TestByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.FLAG_EQUALS, 1)));
            }
        }
        else if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 2) {
            if("Ribu".equals(Settings.getCurrentGiant())) {
                AddObject.addSuccessSound(screen, Arrays.asList(
                        new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 1),
                        new TestByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.FLAG_EQUALS, 1)));
            }
        }
    }

    @Override
    void addUntrackedCustomNoPositionZoneObjects(Zone zone) {
        if(zone.getZoneIndex() == 6) {
            // Add reverse lighting timer
            AddObject.addSecondsTimer(zone, 5,
                    Arrays.asList(new TestByteOperation(FlagConstants.EXTINCTION_PERMA_LIGHT, ByteOp.FLAG_NOT_EQUAL, 3),
                            new TestByteOperation(FlagConstants.EXTINCTION_TEMP_LIGHT, ByteOp.FLAG_EQUALS, 0)),
                    Arrays.asList(new WriteByteOperation(FlagConstants.EXTINCTION_TEMP_LIGHT, ByteOp.ASSIGN_FLAG, 1)));
        }
    }

    @Override
    public void doPostShuffleUpdates() {
        updateFlareGunItem();
        updateSunItems();
    }

    private void updateFlareGunItem() {
        Zone infernoCavern = rcdFileData.getZone(ZoneConstants.INFERNO);
        Room flarePuzzleRoom = infernoCavern.getRoom(4);
        Screen flarePuzzleScreen = flarePuzzleRoom.getScreen(0);

        for(GameObject gameObject : flarePuzzleScreen.getObjectsById(ObjectIdConstants.FloatingItem)) {
            gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.INFERNO_PUZZLE_ICE_CAPE_CHEST, ByteOp.FLAG_GTEQ, 2));
        }
    }

    private void updateSunItems() {
        Zone sun = rcdFileData.getZone(ZoneConstants.SUN);
        for(GameObject gameObject : sun.getObjectsById(ObjectIdConstants.Chest)) {
            Chest chest = (Chest)gameObject;
            if(chest.getUnlockedCheck().getIndex() == FlagConstants.SUN_CHEST_MAP) {
                chest.setDrops(ItemConstants.SPAULDER, 1);
                chest.setEmptyCheck(new WriteByteOperation(0xac2, ByteOp.ASSIGN_FLAG, 2));
                chest.setUpdateWhenOpened(new WriteByteOperation(0xac2, ByteOp.ASSIGN_FLAG, 1));
                chest.setUpdateWhenCollected(new WriteByteOperation(0xac2, ByteOp.ASSIGN_FLAG, 2));
            }
            else if(chest.getUnlockedCheck().getIndex() == FlagConstants.SUN_CHEST_ISIS_PENDANT) {
                chest.setDrops(ItemConstants.SPAULDER, 1);
                chest.setEmptyCheck(new WriteByteOperation(0xac0, ByteOp.ASSIGN_FLAG, 2));
                chest.setUpdateWhenOpened(new WriteByteOperation(0xac0, ByteOp.ASSIGN_FLAG, 1));
                chest.setUpdateWhenCollected(new WriteByteOperation(0xac0, ByteOp.ASSIGN_FLAG, 2));
            }
            else if(chest.getUnlockedCheck().getIndex() == FlagConstants.SUN_CHEST_BRONZE_MIRROR) {
                chest.setDrops(ItemConstants.SPAULDER, 1);
                chest.setEmptyCheck(new WriteByteOperation(0xabf, ByteOp.ASSIGN_FLAG, 2));
                chest.setUpdateWhenOpened(new WriteByteOperation(0xabf, ByteOp.ASSIGN_FLAG, 1));
                chest.setUpdateWhenCollected(new WriteByteOperation(0xabf, ByteOp.ASSIGN_FLAG, 2));
            }
            else if(chest.getUnlockedCheck().getIndex() == FlagConstants.SUN_CHEST_COIN) {
                chest.setDrops(ItemConstants.SPAULDER, 1);
                chest.setEmptyCheck(new WriteByteOperation(0xac5, ByteOp.ASSIGN_FLAG, 2));
                chest.setUpdateWhenOpened(new WriteByteOperation(0xac5, ByteOp.ASSIGN_FLAG, 1));
                chest.setUpdateWhenCollected(new WriteByteOperation(0xac5, ByteOp.ASSIGN_FLAG, 2));
            }
            else if(chest.getUnlockedCheck().getIndex() == FlagConstants.SUN_CHEST_SACRED_ORB) {
                chest.setDrops(ItemConstants.SPAULDER, 1);
                chest.setEmptyCheck(new WriteByteOperation(0xac3, ByteOp.ASSIGN_FLAG, 2));
                chest.setUpdateWhenOpened(new WriteByteOperation(0xac3, ByteOp.ASSIGN_FLAG, 1));
                chest.setUpdateWhenCollected(new WriteByteOperation(0xac3, ByteOp.ASSIGN_FLAG, 2));
            }
            else if(chest.getUnlockedCheck().getIndex() == FlagConstants.SUN_CHEST_ANKH_JEWEL) {
                chest.setDrops(ItemConstants.SPAULDER, 1);
                chest.setEmptyCheck(new WriteByteOperation(0xabe, ByteOp.ASSIGN_FLAG, 2));
                chest.setUpdateWhenOpened(new WriteByteOperation(0xabe, ByteOp.ASSIGN_FLAG, 1));
                chest.setUpdateWhenCollected(new WriteByteOperation(0xabe, ByteOp.ASSIGN_FLAG, 2));
            }
        }
        for(GameObject gameObject : sun.getObjectsById(ObjectIdConstants.FloatingItem)) {
            // Can't use puzzle flags, so check for flags of the items placed in this location via custom placements. // todo: might be safer to do this by screen in case of removed map
            FloatingItem floatingItem = (FloatingItem)gameObject;
            if(floatingItem.getWorldFlag() == FlagConstants.WF_MAP_GRAVEYARD) {
                floatingItem.setInventoryWord(ItemConstants.SPAULDER);
                floatingItem.setRealItem(true);
                for(TestByteOperation testByteOperation : floatingItem.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == FlagConstants.WF_MAP_GRAVEYARD) {
                        testByteOperation.setIndex(0xac1);
                    }
                }
                for(WriteByteOperation writeByteOperation : floatingItem.getWriteByteOperations()) {
                    if(writeByteOperation.getIndex() == FlagConstants.WF_MAP_GRAVEYARD) {
                        writeByteOperation.setIndex(0xac1);
                    }
                }
            }
            else if(floatingItem.getWorldFlag() == FlagConstants.WF_MAP_DIMENSIONAL) {
                // Look for the map placed in this location via custom placements
                floatingItem.setInventoryWord(ItemConstants.SPAULDER);
                floatingItem.setRealItem(true);
                for(TestByteOperation testByteOperation : floatingItem.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == FlagConstants.WF_MAP_DIMENSIONAL) {
                        testByteOperation.setIndex(0xac4);
                    }
                }
                for(WriteByteOperation writeByteOperation : floatingItem.getWriteByteOperations()) {
                    if(writeByteOperation.getIndex() == FlagConstants.WF_MAP_DIMENSIONAL) {
                        writeByteOperation.setIndex(0xac4);
                    }
                }
            }
        }

        Zone shrineFront = rcdFileData.getZone(ZoneConstants.SHRINE_FRONT);
        for(GameObject gameObject : shrineFront.getObjectsById(ObjectIdConstants.FloatingItem)) {
            Chest chest = (Chest)gameObject;
            if(chest.getUnlockedCheck().getIndex() == FlagConstants.SHRINE_CHEST_MAP) {
                chest.setDrops(ItemConstants.SPAULDER, 1);
                chest.setEmptyCheck(new WriteByteOperation(0xac6, ByteOp.ASSIGN_FLAG, 2));
                chest.setUpdateWhenOpened(new WriteByteOperation(0xac6, ByteOp.ASSIGN_FLAG, 1));
                chest.setUpdateWhenCollected(new WriteByteOperation(0xac6, ByteOp.ASSIGN_FLAG, 2));
            }
        }
        Zone shrineBack = rcdFileData.getZone(ZoneConstants.SHRINE_BACK);
        for(GameObject gameObject : shrineBack.getObjectsById(ObjectIdConstants.FloatingItem)) {
            Chest chest = (Chest)gameObject;
            if(chest.getUnlockedCheck().getIndex() == FlagConstants.SHRINE_CHEST_MAP) {
                chest.setDrops(ItemConstants.SPAULDER, 1);
                chest.setEmptyCheck(new WriteByteOperation(0xac6, ByteOp.ASSIGN_FLAG, 2));
                chest.setUpdateWhenOpened(new WriteByteOperation(0xac6, ByteOp.ASSIGN_FLAG, 1));
                chest.setUpdateWhenCollected(new WriteByteOperation(0xac6, ByteOp.ASSIGN_FLAG, 2));
            }
        }
    }

    @Override
    void updateScreenExits(Screen screen) {
        if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 0) {
            // Prevent falling into Ellmac's pit
            screen.getScreenExit(ScreenExit.DOWN).setDestination(3, 6, 0);
        }
        if(screen.getZoneIndex() == 4 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
            // Spring in the Sky drain to Ellmac
            screen.getScreenExit(ScreenExit.DOWN).setDestination(3, 8, 0);
        }
        if(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
            screen.getScreenExit(ScreenExit.UP).setDestination(7, 0, 0);
        }
    }
}
