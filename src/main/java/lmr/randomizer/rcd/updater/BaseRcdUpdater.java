package lmr.randomizer.rcd.updater;

import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.rcd.RcdFileData;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.AddObject;
import lmr.randomizer.update.FlagTimerUpdates;
import lmr.randomizer.update.GraphicsTextureDrawUpdates;
import lmr.randomizer.update.ScannableUpdates;
import lmr.randomizer.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseRcdUpdater extends RcdUpdater {
    private PotUpdater potUpdater;

    public BaseRcdUpdater(RcdFileData rcdFileData, DatFileData datFileData) {
        super(rcdFileData, datFileData);
        this.potUpdater = new PotUpdater();
    }

    @Override
    boolean updatePot(Pot pot) {
        return potUpdater.updatePot(pot);
    }

    @Override
    boolean updateBat(Bat bat) {
        ObjectContainer objectContainer = bat.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(Settings.isRandomizeTrapItems()) {
            if (screen.getZoneIndex() == 7 && screen.getRoomIndex() == 12 && screen.getScreenIndex() == 0) {
                // Bats for fake Ankh Jewel trap
                return false;
            }
        }
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
    boolean updateSkeleton(Skeleton skeleton) {
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
        ObjectContainer objectContainer = ladder.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(Settings.isRandomizeTransitionGates()) {
            if (screen.getZoneIndex() == 11 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                // Remove the Eden flag, since this door may not lead to Eden.
                Integer flagIndexToRemove = null;
                for(int i = 0; i < ladder.getTestByteOperations().size(); i++) {
                    TestByteOperation testByteOperation = ladder.getTestByteOperations().get(i);
                    if(testByteOperation.getIndex() == FlagConstants.EDEN_UNLOCKED) {
                        flagIndexToRemove = i;
                        break;
                    }
                }
                if(flagIndexToRemove != null) {
                    ladder.getTestByteOperations().remove((int)flagIndexToRemove);
                }
            }
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
        if(Settings.isRandomizeTrapItems()) {
            if (screen.getZoneIndex() == 11 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 3) {
                // Graveyard trap chest dais
                dais.getTestByteOperations().get(0).setIndex(FlagConstants.GRAVEYARD_PUZZLE_TRAP_CHEST);
                dais.getWriteByteOperations().get(0).setIndex(FlagConstants.GRAVEYARD_PUZZLE_TRAP_CHEST);
                dais.getWriteByteOperations().remove(1);
            }
        }

        for (TestByteOperation flagTest : dais.getTestByteOperations()) {
            if(flagTest.getIndex() == FlagConstants.INFERNO_CHAIN_WHIP_CRUSHER_LEFT) {
                // Chain Whip puzzle crusher
                flagTest.setIndex(FlagConstants.SCREEN_FLAG_2E);

                dais.getWriteByteOperations().get(0).setIndex(FlagConstants.SCREEN_FLAG_2E);
                break;
            }
            else if(flagTest.getIndex() == FlagConstants.INFERNO_CHAIN_WHIP_CRUSHER_RIGHT) {
                // Chain Whip puzzle crusher
                flagTest.setIndex(FlagConstants.SCREEN_FLAG_2F);

                dais.getWriteByteOperations().get(0).setIndex(FlagConstants.SCREEN_FLAG_2F);
                break;
            }
        }
        return true;
    }

    @Override
    boolean updateFlagTimer(GameObject flagTimer) {
        return FlagTimerUpdates.updateFlagTimer(flagTimer);
    }

    @Override
    boolean updateRoomSpawner(GameObject roomSpawner) {
        ObjectContainer objectContainer = roomSpawner.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(Settings.isRandomizeTransitionGates() || HolidaySettings.isHalloween2021Mode()) {
            if(screen.getZoneIndex() == 0 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                // Guidance to Surface escape blockage
                for (TestByteOperation flagTest : roomSpawner.getTestByteOperations()) {
                    if(flagTest.getIndex() == FlagConstants.MOTHER_STATE && flagTest.getValue() == 3) {
                        return false;
                    }
                }
            }
            else if(screen.getZoneIndex() == 4 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 1) {
                // Spring to Surface escape blockage
                for (TestByteOperation flagTest : roomSpawner.getTestByteOperations()) {
                    if (flagTest.getIndex() == FlagConstants.MOTHER_STATE && flagTest.getValue() == 3) {
                        return false;
                    }
                }
            }
            else if(screen.getZoneIndex() == 6) {
                // Rubble in place of Spriggan
                if(screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
                    for(TestByteOperation flagTest : roomSpawner.getTestByteOperations()) {
                        if(flagTest.getIndex() == FlagConstants.MOTHER_STATE && flagTest.getValue() == 3) {
                            return false;
                        }
                    }
                }
                if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                    // Rubble blocking Gate of Time door
                    for(TestByteOperation flagTest : roomSpawner.getTestByteOperations()) {
                        if(flagTest.getIndex() == FlagConstants.ESCAPE && flagTest.getValue() == 1) {
                            return false;
                        }
                    }
                }
            }
            else if(screen.getZoneIndex() == 11 && screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                // Rubble blocking...Tower of the Goddess entrance from Graveyard? // todo: Probably meant to be 11 - 2 - 1 (Temple of Moonlight)
                for (TestByteOperation flagTest : roomSpawner.getTestByteOperations()) {
                    if(flagTest.getIndex() == FlagConstants.MOTHER_STATE && flagTest.getValue() == 3) {
                        return false;
                    }
                }
            }
            else if(screen.getZoneIndex() == 12 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                // Rubble blocking Moonlight L1/pipe entrance
                for (TestByteOperation flagTest : roomSpawner.getTestByteOperations()) {
                    if(flagTest.getIndex() == FlagConstants.MOTHER_STATE && flagTest.getValue() == 3) {
                        return false;
                    }
                }
            }
            else if(screen.getZoneIndex() == 13 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                // Rubble in place of Tower of the Goddess backside door
                for (TestByteOperation flagTest : roomSpawner.getTestByteOperations()) {
                    if(flagTest.getIndex() == FlagConstants.MOTHER_STATE && flagTest.getValue() == 3) {
                        return false;
                    }
                }
            }
        }

        if (Settings.isRandomizeNonBossDoors()) {
            if(screen.getZoneIndex() == 6 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                for (TestByteOperation flagTest : roomSpawner.getTestByteOperations()) {
                    if (flagTest.getIndex() == FlagConstants.KEY_FAIRY_DOOR_UNLOCKED && flagTest.getValue() == 1) {
                        return false;
                    }
                }
            }
        }

        if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
            // Temple of the Sun Map chest ladder stuff
            roomSpawner.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_C, ByteOp.FLAG_EQUALS, 0));
        }

        for (TestByteOperation flagTest : roomSpawner.getTestByteOperations()) {
            if(flagTest.getIndex() == FlagConstants.MANTRA_FINAL) {
                if(flagTest.getValue() == 3) {
                    flagTest.setIndex(FlagConstants.MANTRA_LAMULANA);
                    flagTest.setOp(ByteOp.FLAG_LT);
                    flagTest.setValue((byte)1);
                    break;
                }
                else if(flagTest.getValue() == 4) {
                    flagTest.setIndex(FlagConstants.MANTRA_LAMULANA);
                    flagTest.setValue((byte)1);
                    break;
                }
            }
            else if(flagTest.getIndex() == FlagConstants.COG_MUDMEN_STATE) {
                if(flagTest.getValue() == 3) {
                    if(ByteOp.FLAG_EQUALS.equals(flagTest.getOp())) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);
                        roomSpawner.setX(roomSpawner.getX() - 60);
                        break;
                    }
                    if(ByteOp.FLAG_LTEQ.equals(flagTest.getOp())) {
                        flagTest.setValue((byte)4);
                        flagTest.setOp(ByteOp.FLAG_LT);
                        break;
                    }
                }
                else if(flagTest.getValue() != 4) {
                    flagTest.setIndex(FlagConstants.ILLUSION_PUZZLE_COG_CHEST);
                    break;
                }
            }
            else if(flagTest.getIndex() == FlagConstants.SURFACE_UNDERPATH_VISIBLE) {
                roomSpawner.getArgs().set(4, (short)2);
            }
        }
        return true;
    }

    @Override
    boolean updateCrusher(GameObject crusher) {
        for (WriteByteOperation flagUpdate : crusher.getWriteByteOperations()) {
            if(flagUpdate.getIndex() == FlagConstants.INFERNO_CHAIN_WHIP_CRUSHER_LEFT) {
                // Chain Whip puzzle crusher
                flagUpdate.setIndex(FlagConstants.SCREEN_FLAG_2E);
                break;
            }
            else if(flagUpdate.getIndex() == FlagConstants.INFERNO_CHAIN_WHIP_CRUSHER_RIGHT) {
                // Chain Whip puzzle crusher
                flagUpdate.setIndex(FlagConstants.SCREEN_FLAG_2F);
                break;
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
        if(Settings.isRandomizeTransitionGates()) {
            if (screen.getZoneIndex() == 11 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                for(TestByteOperation testByteOperation : hitbox.getTestByteOperations()) {
                    if (testByteOperation.getIndex() == FlagConstants.EDEN_UNLOCKED) {
                        return false;
                    }
                }
            }
        }
        if(Settings.isRandomizeNonBossDoors()) {
            if (screen.getZoneIndex() == 6 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                return false;
            }
        }

        Integer flagIndexToRemove = null;
        TestByteOperation flagTest;
        for (int i = 0; i < hitbox.getTestByteOperations().size(); i++) {
            flagTest = hitbox.getTestByteOperations().get(i);
            if (flagTest.getIndex() == FlagConstants.SUN_WATCHTOWER_LIGHTS) {
                // Turning on the lights in Temple of the Sun; moved in versions after 1.3 and should be restored
                hitbox.setX(400);
                break;
            }
            else if (flagTest.getIndex() == FlagConstants.INFERNO_PUZZLE_FLARE_GUN) {
                // Breakable wall to Flare Gun room; hitbox is inside the wall and makes subweapon breaking a bit awkward
                hitbox.getArgs().set(6, (short)4); // Extend wall hitbox
            }
            else if (flagTest.getIndex() == FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES) {
                // Breakable ceiling to Isis' Pendant room
                flagIndexToRemove = i;
                break;
            }
            else if(flagTest.getIndex() == FlagConstants.GRAVEYARD_WALL_SNAPSHOTS_MURAL) {
                // emusic wall - not requiring precise hitbox
                hitbox.getArgs().set(3, (short)Hitbox.AnyDirection);
            }
            else if (flagTest.getIndex() == FlagConstants.MOONLIGHT_3_WOMEN_FACES) {
                // Moon-Gazing Pit faces - extend hitbox to make things a bit easier
                hitbox.setX(hitbox.getX() - 20);
                hitbox.getArgs().set(6, (short)(hitbox.getArgs().get(6) + 1)); // Extend wall hitbox
            }
            else if(flagTest.getIndex() == FlagConstants.TRUE_SHRINE_TENTACLE) {
                // Main-weapon-only tentacle blocking access to Mother ankh
                hitbox.getArgs().set(4, (short)Hitbox.AnyWeapon);
                break;
            }
            else if(flagTest.getIndex() == FlagConstants.GODDESS_PIPES_SHORTCUT) {
                // Main-weapon-only wall in Tower of the Goddess
                hitbox.getArgs().set(4, (short)Hitbox.AnyWeapon);
                break;
            }
            else if(flagTest.getIndex() == FlagConstants.GRAVEYARD_ILLUSION_LADDER_BLOCKAGE) {
                // Main-weapon-only blockage of the ice block in Graveyard of the Giants, needed to access the up-ladder
                hitbox.getArgs().set(4, (short)Hitbox.AnyWeapon);
                break;
            }
            else if(flagTest.getIndex() == FlagConstants.MOONLIGHT_DEV_ROOM_BREAKABLE_FLOOR) {
                // Moonlight dev room floor
                hitbox.getArgs().set(3, (short)Hitbox.AnyDirection); // Any direction, to be more bomb-friendly.
                break;
            }
            else if(flagTest.getIndex() == FlagConstants.THE_BOSS_STATE) {
                // Trigger for The Boss ankh, coded as a wall that triggers stuff when broken
                hitbox.getArgs().set(4, (short)Hitbox.AnyWeapon);
                break;
            }
            else if(flagTest.getIndex() == FlagConstants.MANTRA_VIY) {
                // Breakable snake statue in Inferno Cavern spike area
                hitbox.getArgs().set(6, (short)2); // Extend wall hitbox
                flagTest.setIndex(FlagConstants.MANTRAS_UNLOCKED);
                flagTest.setValue((byte)1);
            }
        }
        if(flagIndexToRemove != null) {
            hitbox.getTestByteOperations().remove((int)flagIndexToRemove);
        }
        return true;
    }

    @Override
    boolean updateLemezaDetector(GameObject lemezaDetector) {
        ObjectContainer objectContainer = lemezaDetector.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(Settings.isRandomizeTrapItems()) {
            if (screen.getZoneIndex() == 5 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
                // Lemeza Detector part of Inferno Cavern fake Sacred Orb trap
                if(!HolidaySettings.isFools2020Mode()) {
                    return false;
                }
            }
            else if (screen.getZoneIndex() == 7 && screen.getRoomIndex() == 12 && screen.getScreenIndex() == 0) {
                // Lemeza Detector part of Twin Labs fake Ankh Jewel trap
                return false;
            }
        }
        if(Settings.isRandomizeTransitionGates()) {
            if (screen.getZoneIndex() == 14 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                for(WriteByteOperation writeByteOperation : lemezaDetector.getWriteByteOperations()) {
                    if(writeByteOperation.getIndex() == FlagConstants.GODDESS_STATUE_RUIN) {
                        // Lemeza detector for ToG statue
                        return false;
                    }
                }
            }
        }

        if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
            if(lemezaDetector.getTestByteOperations().get(0).getIndex() == FlagConstants.SUN_MAP_CHEST_LADDER_DESPAWNED) {
                lemezaDetector.getWriteByteOperations().remove(0);
            }
            else if(lemezaDetector.getTestByteOperations().get(0).getIndex() == FlagConstants.SUN_MAP_CHEST_LADDER_RESTORED) {
                lemezaDetector.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SUN_MAP_CHEST_LADDER_DESPAWNED, ByteOp.ASSIGN_FLAG, 1));
            }
        }
        return true;
    }

    @Override
    boolean updateChest(GameObject chest) {
        int inventoryArg = chest.getArgs().get(0) - ValueConstants.CHEST_ITEM_OFFSET;
        if(inventoryArg == ItemConstants.COG_OF_THE_SOUL) {
            // Cog of the Soul chest
            TestByteOperation cogChestTest = chest.getTestByteOperations().get(0);
            cogChestTest.setIndex(FlagConstants.ILLUSION_PUZZLE_COG_CHEST);
            WriteByteOperation cogPuzzleFlag = chest.getWriteByteOperations().get(1);
            cogPuzzleFlag.setIndex(FlagConstants.ILLUSION_PUZZLE_COG_CHEST);
        }
        else if(inventoryArg == ItemConstants.DIARY) {
            for(TestByteOperation flagTest : chest.getTestByteOperations()) {
                if(flagTest.getIndex() == FlagConstants.SHRINE_SHAWN) {
                    // Require skull walls removed (normally required for Shawn to appear) instead of directly requiring Shawn's appearance
                    flagTest.setIndex(FlagConstants.SHRINE_DRAGON_BONE);
                    flagTest.setOp(ByteOp.FLAG_EQUALS);
                    flagTest.setValue((byte)1);
                    break;
                }
            }
            // Require showing Talisman to Xelpud (normally required for Shawn to appear)
            chest.getTestByteOperations().add(new TestByteOperation(FlagConstants.XELPUD_CONVERSATION_TALISMAN_FOUND, ByteOp.FLAG_GTEQ, 2));
        }
        else if(inventoryArg == DropType.NOTHING.getValue() - ValueConstants.CHEST_ITEM_OFFSET) {
            if(chest.getObjectContainer() instanceof Screen) {
                Screen screen = (Screen)chest.getObjectContainer();
                if(screen.getZoneIndex() == 18 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
                    // Remove buggy empty chest in transformed Shrine of the Mother
                    return false;
                }
            }
        }

        if(Settings.isRandomizeTrapItems()) {
            WriteByteOperation flagUpdate = chest.getWriteByteOperations().get(0);
            if(flagUpdate.getIndex() == FlagConstants.ROOM_FLAG_3C) {
                chest.getWriteByteOperations().clear();
                chest.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.WF_TRAP_GRAVEYARD, ByteOp.ASSIGN_FLAG, 2));
                chest.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.GRAVEYARD_PUZZLE_TRAP_CHEST, ByteOp.ASSIGN_FLAG, 1));
                chest.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.WF_TRAP_GRAVEYARD, ByteOp.ASSIGN_FLAG, 2));
                chest.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.WF_TRAP_GRAVEYARD, ByteOp.ASSIGN_FLAG, 2));
            }
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

        if(Settings.isBossSpecificAnkhJewels()) {
            int ankhJewelFlag = FlagConstants.getAnkhJewelFlag(screen.getZoneIndex());
            ankh.getTestByteOperations().add(new TestByteOperation(ankhJewelFlag, ByteOp.FLAG_NOT_EQUAL, (byte) 0)); // Don't spawn ankh without jewel collected.
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
        if(floatingItem.getArgs().get(1) == 7) {
            // Remove empowered Key Sword
            return false;
        }
        if(floatingItem.getArgs().get(1) == ItemConstants.KEY_SWORD) {
            // Remove LAMULANA mantra check on the Key Sword floating item
            Integer flagToRemoveIndex = null;
            for (int i = 0; i < floatingItem.getTestByteOperations().size(); i++) {
                if (floatingItem.getTestByteOperations().get(i).getIndex() == FlagConstants.MANTRA_FINAL) {
                    flagToRemoveIndex = i;
                    break;
                }
            }
            floatingItem.getTestByteOperations().remove((int)flagToRemoveIndex);
        }
        return true;
    }

    @Override
    boolean updateSeal(GameObject seal) {
        ObjectContainer objectContainer = seal.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 18 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
            if(Settings.isFeatherlessMode()) {
                if(seal.getY() == 240) {
                    seal.setY(260);
                    if(seal.getX() == 200) {
                        seal.setX(220);
                    }
                    else {
                        seal.setX(380);
                    }
                }
            }
        }
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
    boolean updateSpriggan(GameObject spriggan) {
        if(Settings.isRandomizeTransitionGates() || HolidaySettings.isHalloween2021Mode()) {
            // Spriggan statue still reachable during the escape sequence.
            Integer flagIndexToRemove = null;
            for(int i = 0; i < spriggan.getTestByteOperations().size(); i++) {
                TestByteOperation testByteOperation = spriggan.getTestByteOperations().get(i);
                if(testByteOperation.getIndex() == FlagConstants.MOTHER_STATE) {
                    flagIndexToRemove = i;
                    break;
                }
            }
            if(flagIndexToRemove != null) {
                spriggan.getTestByteOperations().remove((int)flagIndexToRemove);
            }
        }
        return true;
    }

    @Override
    boolean updateBigAnubis(GameObject anubis) {
        // Fighting Anubis shouldn't prevent Mulbruk from giving you Book of the Dead.
        Integer flagUpdateToRemove = null;
        for (int i = 0; i < anubis.getWriteByteOperations().size(); i++) {
            if (anubis.getWriteByteOperations().get(i).getIndex() == FlagConstants.MULBRUK_CONVERSATION_BOOK) {
                flagUpdateToRemove = i;
                break;
            }
        }
        if(flagUpdateToRemove != null) {
            anubis.getWriteByteOperations().remove((int)flagUpdateToRemove);
        }
        return true;
    }

    @Override
    boolean updateFairyPoint(GameObject fairyPoint) {
        if(Settings.isRandomizeNonBossDoors()) {
            Integer testFlagIndex = null;
            for (int i = 0; i < fairyPoint.getTestByteOperations().size(); i++) {
                TestByteOperation flagTest = fairyPoint.getTestByteOperations().get(i);
                if (flagTest.getIndex() == FlagConstants.ESCAPE) {
                    // Un-disable fairy points during the escape
                    testFlagIndex = i;
                }
            }
            if(testFlagIndex != null) {
                fairyPoint.getTestByteOperations().remove((int)testFlagIndex);
            }
        }
        return true;
    }

    @Override
    boolean updateGraphicsTextureDraw(GameObject graphicsTextureDraw) {
        return GraphicsTextureDrawUpdates.updateGraphicsTextureDraw(graphicsTextureDraw);
    }

    @Override
    boolean updateWarpPortal(GameObject warpPortal) {
        if(!warpPortal.getTestByteOperations().isEmpty() && warpPortal.getTestByteOperations().get(0).getIndex() == FlagConstants.ILLUSION_WARP_MAZE_ACTIVE) {
            // Remove broken pot flag check so the warp is just always active.
            warpPortal.getTestByteOperations().remove(0);
        }
        return true;
    }

    @Override
    boolean updateWarpDoor(GameObject warpDoor) {
        if(warpDoor.getArgs().get(0) == 0) {
            if(Settings.isRandomizeBacksideDoors()) {
                Screen screen = (Screen)warpDoor.getObjectContainer();
                int zone = screen.getZoneIndex();
                if(zone == ZoneConstants.EXTINCTION) {
                    if(screen.getRoomIndex() == 7) {
                        if(Settings.isRandomizeTransitionGates()) {
                            Integer flagIndexToRemove = null;
                            for(int i = 0; i < warpDoor.getTestByteOperations().size(); i++) {
                                TestByteOperation testByteOperation = warpDoor.getTestByteOperations().get(i);
                                if(testByteOperation.getIndex() == FlagConstants.ESCAPE) {
                                    flagIndexToRemove = i;
                                    break;
                                }
                            }
                            if(flagIndexToRemove != null) {
                                warpDoor.getTestByteOperations().remove((int)flagIndexToRemove);
                            }
                        }

                        if(Settings.isRandomizeNonBossDoors()) {
//                            doorName = "Door: F9";
                            replaceBacksideDoorFlags(warpDoor, FlagConstants.PALENQUE_STATE, FlagConstants.PALENQUE_GATE_OPEN, false);
                        }
                    }
                    else {
                        // Chamber of Extinction [Magatama Left] => Chamber of Birth [Northeast]
//                        doorName = "Door: F6";
                        replaceBacksideDoorFlags(warpDoor, FlagConstants.PALENQUE_STATE, FlagConstants.PALENQUE_GATE_OPEN, false);
                    }
                }
                else if(zone == ZoneConstants.RUIN) {
                    if(screen.getRoomIndex() != 2) {
                        // Tower of Ruin [Top] => Inferno Cavern [Spikes]
//                        doorName = "Door: B7";
                        replaceBacksideDoorFlags(warpDoor, FlagConstants.BAPHOMET_STATE, FlagConstants.BAPHOMET_GATE_OPEN, false);
                    }
                }
            }
            else if(Settings.isRandomizeTransitionGates()) {
                // Re-enable backside door during the escape
                Integer flagTestToRemove = null;
                for (int i = 0; i < warpDoor.getTestByteOperations().size(); i++) {
                    if (warpDoor.getTestByteOperations().get(i).getIndex() == FlagConstants.MOTHER_STATE) {
                        flagTestToRemove = i;
                        break;
                    }
                }
                if(flagTestToRemove != null) {
                    warpDoor.getTestByteOperations().remove((int)flagTestToRemove);
                }
            }
        }
        return true;
    }

    @Override
    boolean updateSoundEffect(GameObject soundEffect) {
        for (TestByteOperation flagTest : soundEffect.getTestByteOperations()) {
            if(flagTest.getIndex() == FlagConstants.MANTRA_FINAL) {
                // This is based on the LAMULANA mantra being recited, but we have a new flag for that.
                flagTest.setIndex(FlagConstants.MANTRA_LAMULANA);
                flagTest.setValue((byte)1);
                break;
            }
        }

        ObjectContainer objectContainer = soundEffect.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == 1 && screen.getRoomIndex() == 2 && screen.getScreenIndex() == 1) {
            for (TestByteOperation flagTest : soundEffect.getTestByteOperations()) {
                if(flagTest.getIndex() == FlagConstants.SCREEN_FLAG_16) {
                    // Remove Xelpud tent opening sound effect
                    return false;
                }
            }
        }
        else if (screen.getZoneIndex() == 7 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 2) {
            for (WriteByteOperation flagUpdate : soundEffect.getWriteByteOperations()) {
                if(flagUpdate.getIndex() == FlagConstants.BIG_BROTHER_UNLOCKED) {
                    // Remove unneeded sound effect on original Little Brother screen
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    boolean updateUseItemDetector(GameObject useItemDetector) {
        for (TestByteOperation flagTest : useItemDetector.getTestByteOperations()) {
            if (flagTest.getIndex() == FlagConstants.MR_SLUSHFUND_CONVERSATION) {
                // Using Pepper to spawn Treasures chest
                flagTest.setIndex(FlagConstants.WF_TREASURES);
                flagTest.setOp(ByteOp.FLAG_LTEQ);
                flagTest.setValue((byte)1);
                break;
            }
            else if(flagTest.getIndex() == FlagConstants.COG_MUDMEN_STATE) {
                if(flagTest.getValue() == 3) {
                    if(ByteOp.FLAG_EQUALS.equals(flagTest.getOp())) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);
                        useItemDetector.setX(useItemDetector.getX() - 60);
                        break;
                    }
                    if(ByteOp.FLAG_LTEQ.equals(flagTest.getOp())) {
                        flagTest.setValue((byte)4);
                        flagTest.setOp(ByteOp.FLAG_LT);
                        break;
                    }
                }
                else if(flagTest.getValue() != 4) {
                    flagTest.setIndex(FlagConstants.ILLUSION_PUZZLE_COG_CHEST);
                    break;
                }
            }
        }
        return true;
    }

    @Override
    boolean updateScannable(Scannable scannable) {
        return ScannableUpdates.updateScannable(scannable);
    }

    @Override
    boolean updateAutosave(GameObject autosave) {
        ObjectContainer objectContainer = autosave.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(!LocationCoordinateMapper.isSurfaceStart() || Settings.isReducedBossCount()) {
            int zoneIndex = screen.getZoneIndex();
            if(zoneIndex == ZoneConstants.TWIN_FRONT) {
                boolean frontside = autosave.hasUpdate(new WriteByteOperation(FlagConstants.TABLET_GRAIL_TWIN_FRONT, ByteOp.ASSIGN_FLAG, 1));
                autosave.getTestByteOperations().clear();
                autosave.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0));
                autosave.getTestByteOperations().add(new TestByteOperation(LocationCoordinateMapper.getGrailFlag(zoneIndex, frontside), ByteOp.FLAG_EQUALS, 1));
                autosave.getWriteByteOperations().clear();
                autosave.getWriteByteOperations().add(new WriteByteOperation(LocationCoordinateMapper.getGrailFlag(zoneIndex, frontside), ByteOp.ASSIGN_FLAG, 1));
            }
            else {
                autosave.getTestByteOperations().clear();
                autosave.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0));
                if(zoneIndex != ZoneConstants.SURFACE || !LocationCoordinateMapper.isSurfaceStart()) {
                    autosave.getTestByteOperations().add(new TestByteOperation(LocationCoordinateMapper.getGrailFlag(zoneIndex, true), ByteOp.FLAG_EQUALS, 1));
                }

                autosave.getWriteByteOperations().clear();
                autosave.getWriteByteOperations().add(new WriteByteOperation(LocationCoordinateMapper.getGrailFlag(zoneIndex, true), ByteOp.ASSIGN_FLAG, 1));
            }
            if(zoneIndex == ZoneConstants.BIRTH_SKANDA && LocationCoordinateMapper.getStartingZone() == ZoneConstants.BIRTH_SKANDA) {
                // Move Chamber of Birth grail point when starting in that area.
                autosave.setX(200);
                autosave.setY(380);
            }
        }
        return true;
    }

    @Override
    boolean updateConversationDoor(GameObject conversationDoor) {
        int blockNumber = conversationDoor.getArgs().get(4);
        if(conversationDoor.getArgs().get(3) == ConversationDoor.Shop) {
            if(blockNumber == BlockConstants.ShopBlockMrFishmanAlt){
                // Transformed Mr. Fishman shop
                conversationDoor.setX(180);
                conversationDoor.setY(1520);
            }
            else if(blockNumber == BlockConstants.ShopBlockNebur) {
                // To be removed and re-added for NPC shuffling simplicity.
                return false;
            }
            else if(blockNumber == BlockConstants.ShopBlockGiantMopiranAngelShield) {
                // Get rid of alternate Graveyard shop (with the Angel Shield)
                return false;
            }
        }
        else if(blockNumber == BlockConstants.MultiMaster_MulbrukRandomSetA) {
            // To be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == 676) {
            // Giltoriyo, Alsedana, Samaranta, Fobos conversations without Philosopher's Ocarina
            return false;
        }
        else if(blockNumber == 682) {
            // Conversation to inform of unlocking Big Brother's shop, to be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == BlockConstants.Master_FairyQueen_RequestPendant && !HolidaySettings.isHalloween2019Mode()) {
            // First Fairy Queen conversation, completely unneeded for randomizer outside of Halloween.
            return false;
        }
        else if(blockNumber == 685) {
            // To be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == 687) {
            // Fairy Queen conversation, to be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == 688) {
            // Fairy Queen conversation, to be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == BlockConstants.Master_MrSlushfund_WaitingForTreasures) {
            // To be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == BlockConstants.Master_MrSlushfund_Anchor) {
            // To be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == BlockConstants.Master_MrSlushfund_NeverComeBack) {
            // To be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == BlockConstants.Master_StrayFairy) {
            // Stray fairy - Illusion NPC, 10-00-01
            for (TestByteOperation flagTest : conversationDoor.getTestByteOperations()) {
                if (flagTest.getIndex() == FlagConstants.COG_MUDMEN_STATE) {
                    // Swap out the original Cog of the Soul puzzle flag for the custom flag
                    flagTest.setIndex(FlagConstants.ILLUSION_PUZZLE_COG_CHEST);
                }
            }
            for(WriteByteOperation flagUpdate : conversationDoor.getWriteByteOperations()) {
                if (flagUpdate.getIndex() == FlagConstants.COG_MUDMEN_STATE) {
                    // Swap out the original Cog of the Soul puzzle flag for the custom flag
                    flagUpdate.setIndex(FlagConstants.ILLUSION_PUZZLE_COG_CHEST);
                }
            }
        }
        else if(blockNumber == 695) {
            for (TestByteOperation flagTest : conversationDoor.getTestByteOperations()) {
                if (flagTest.getIndex() == FlagConstants.COG_MUDMEN_STATE) {
                    // Swap out the original Cog of the Soul puzzle flag for the custom flag
                    flagTest.setIndex(FlagConstants.ILLUSION_PUZZLE_COG_CHEST);
                }
            }
            for(WriteByteOperation flagUpdate : conversationDoor.getWriteByteOperations()) {
                if (flagUpdate.getIndex() == FlagConstants.COG_MUDMEN_STATE) {
                    // Swap out the original Cog of the Soul puzzle flag for the custom flag
                    flagUpdate.setIndex(FlagConstants.ILLUSION_PUZZLE_COG_CHEST);
                }
            }
        }
        else if(blockNumber == BlockConstants.Master_PhilosopherFobos_Ladder) {
            // Fobos main conversation, to be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == BlockConstants.Removed_MulbrukLowScore) {
            // Low-score version of Mulbruk which could interfere with getting Book of the Dead.
            return false;
        }
        else if(blockNumber == 985) {
            // To be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == 913) {
            // Xelpud conversation after he goes to do the Diary thing.
            return false;
        }
        else if(blockNumber == BlockConstants.Master_PriestAlest_NoItem) {
            // To be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == BlockConstants.Master_MulbrukEscapeRegular
                || blockNumber == BlockConstants.Master_MulbrukEscapeSwimsuit) {
            // To be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == BlockConstants.Master_Mulbruk_ProvocativeBathingSuitReaction) {
            // To be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == BlockConstants.Master_Mulbruk_Empty) {
            return false;
        }
        else if(blockNumber == BlockConstants.MultiMaster_ElderXelpudRandomSetC_Rug
                || blockNumber == BlockConstants.MultiMaster_ElderXelpudRandomSetB
                || blockNumber == BlockConstants.MultiMaster_ElderXelpudRandomSetC_NoRug) {
            // To be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        else if(blockNumber == BlockConstants.MultiMaster_MulbrukRandomSetB
                || blockNumber == BlockConstants.MultiMaster_MulbrukRandomSetC) {
            // To be removed and re-added for NPC shuffling simplicity.
            return false;
        }
        return true;
    }

    @Override
    boolean updateAnimation(GameObject animation) {
        if(Settings.isAlternateMotherAnkh()) {
            for (TestByteOperation testByteOperation : animation.getTestByteOperations()) {
                if (testByteOperation.getIndex() == FlagConstants.MOTHER_STATE) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    boolean updateKeyFairySpot(GameObject keyFairySpot) {
        ObjectContainer objectContainer = keyFairySpot.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == 4 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 3) {
            // Key fairy point for Mr. Fishman shop
            keyFairySpot.setX(180);
            keyFairySpot.setY(1480);
        }
        else if (screen.getZoneIndex() == 6 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
            if(Settings.isRandomizeNonBossDoors()) {
                return false;
            }
        }
        return true;
    }

    @Override
    boolean updateExplosion(GameObject explosion) {
        if(Settings.isRandomizeTrapItems()) {
            if(!explosion.getTestByteOperations().isEmpty() && explosion.getTestByteOperations().get(0).getIndex() == FlagConstants.ILLUSION_PUZZLE_EXPLODING_CHEST) {
                // Exploding chest explosion object.
                return false;
            }
        }
        return true;
    }

    @Override
    boolean updateSavePoint(GameObject savePoint) {
        ObjectContainer objectContainer = savePoint.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(LocationCoordinateMapper.getStartingZone() == ZoneConstants.BIRTH_SKANDA) {
            // Move Chamber of Birth grail point when starting in that area.
            if(screen.getZoneIndex() == ZoneConstants.BIRTH_SKANDA) {
                savePoint.setX(200);
                savePoint.setY(380);
            }
        }
        return true;
    }

    @Override
    boolean updateGrailToggle(GameObject grailToggle) {
        if(grailToggle.getArgs().get(0) == 1) {
            boolean needEscapeTest = true;
            for (int i = 0; i < grailToggle.getTestByteOperations().size(); i++) {
                TestByteOperation flagTest = grailToggle.getTestByteOperations().get(i);
                if (flagTest.getIndex() == FlagConstants.ESCAPE) {
                    needEscapeTest = false;
                    break;
                }
            }
            if(needEscapeTest) {
                grailToggle.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0));
            }
        }

        ObjectContainer objectContainer = grailToggle.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(Settings.isRandomizeNonBossDoors()) {
            if(screen.getZoneIndex() == 8 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    boolean updateMotherAnkh(GameObject motherAnkh) {
        if(Settings.isAlternateMotherAnkh()) {
            motherAnkh.setId((short)0x2e);
            motherAnkh.getArgs().set(0, (short)8);
            motherAnkh.getWriteByteOperations().get(0).setValue((byte) 1);
            motherAnkh.getWriteByteOperations().get(1).setValue((byte) 2);
            motherAnkh.setY(motherAnkh.getY() + 60);
        }
        return true;
    }

    @Override
    boolean updateMantraDetector(GameObject mantraDetector) {
        short mantraNumber = mantraDetector.getArgs().get(0);
        if(mantraNumber > 1) {
            // Don't mess with birth/death
            if(mantraNumber == 2) {
                // Reciting MARDUK won't un-recite SABBAT
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < mantraDetector.getWriteByteOperations().size(); i++) {
                    if (mantraDetector.getWriteByteOperations().get(i).getIndex() == FlagConstants.MANTRA_SABBAT) {
                        flagToRemoveIndex = i;
                        break;
                    }
                }
                if(flagToRemoveIndex != null) {
                    mantraDetector.getWriteByteOperations().remove((int)flagToRemoveIndex);
                }
            }
            else if(mantraNumber == 3) {
                // Reciting SABBAT won't un-recite MU
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < mantraDetector.getWriteByteOperations().size(); i++) {
                    if (mantraDetector.getWriteByteOperations().get(i).getIndex() == FlagConstants.MANTRA_MU) {
                        flagToRemoveIndex = i;
                        break;
                    }
                }
                if(flagToRemoveIndex != null) {
                    mantraDetector.getWriteByteOperations().remove((int)flagToRemoveIndex);
                }
            }
            else if(mantraNumber == 4) {
                // Reciting MU won't un-recite VIY, and statue won't be dependent on it either.
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < mantraDetector.getWriteByteOperations().size(); i++) {
                    if (mantraDetector.getWriteByteOperations().get(i).getIndex() == FlagConstants.MANTRA_VIY) {
                        flagToRemoveIndex = i;
                        break;
                    }
                }
                if(flagToRemoveIndex != null) {
                    mantraDetector.getWriteByteOperations().remove((int)flagToRemoveIndex);
                }
            }
            else if(mantraNumber == 5) {
                // Reciting VIY won't un-recite BAHRUN
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < mantraDetector.getWriteByteOperations().size(); i++) {
                    if (mantraDetector.getWriteByteOperations().get(i).getIndex() == FlagConstants.MANTRA_BAHRUN) {
                        flagToRemoveIndex = i;
                        break;
                    }
                }
                if(flagToRemoveIndex != null) {
                    mantraDetector.getWriteByteOperations().remove((int)flagToRemoveIndex);
                }
            }
            else if(mantraNumber == 6) {
                // Reciting BAHRUN won't un-recite WEDJET
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < mantraDetector.getWriteByteOperations().size(); i++) {
                    if (mantraDetector.getWriteByteOperations().get(i).getIndex() == FlagConstants.MANTRA_WEDJET) {
                        flagToRemoveIndex = i;
                        break;
                    }
                }
                if(flagToRemoveIndex != null) {
                    mantraDetector.getWriteByteOperations().remove((int)flagToRemoveIndex);
                }
            }
            else if(mantraNumber == 7) {
                // Reciting WEDJET won't un-recite ABUTO
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < mantraDetector.getWriteByteOperations().size(); i++) {
                    if (mantraDetector.getWriteByteOperations().get(i).getIndex() == FlagConstants.MANTRA_ABUTO) {
                        flagToRemoveIndex = i;
                        break;
                    }
                }
                if(flagToRemoveIndex != null) {
                    mantraDetector.getWriteByteOperations().remove((int)flagToRemoveIndex);
                }
            }
            else if(mantraNumber == 8) {
                // Reciting ABUTO won't un-recite LAMULANA
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < mantraDetector.getWriteByteOperations().size(); i++) {
                    if (mantraDetector.getWriteByteOperations().get(i).getIndex() == FlagConstants.MANTRA_FINAL) {
                        flagToRemoveIndex = i;
                        break;
                    }
                }
                if(flagToRemoveIndex != null) {
                    mantraDetector.getWriteByteOperations().remove((int)flagToRemoveIndex);
                }
            }
            else if(mantraNumber == 9) {
                // Reciting LAMULANA won't update the flag that upgrades the Key Sword until we're ready.
                for (WriteByteOperation writeByteOperation : mantraDetector.getWriteByteOperations()) {
                    if (writeByteOperation.getIndex() == FlagConstants.MANTRA_FINAL) {
                        writeByteOperation.setIndex(FlagConstants.MANTRA_LAMULANA);
                        writeByteOperation.setValue(1);
                        break;
                    }
                }
            }
            // Keep track of total mantras recited
            mantraDetector.addUpdates(new WriteByteOperation(FlagConstants.MANTRAS_RECITED_COUNT, ByteOp.ADD_FLAG, 1));
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
        if (screen.getZoneIndex() == 6 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
            if(Settings.isRandomizeTransitionGates()) {
                if(transitionGate.getArgs().get(0) == 18) {
                    return false;
                }
                else if(transitionGate.getArgs().get(0) == 9) {
                    TestByteOperation testByteOperation = transitionGate.getTestByteOperations().get(0);
                    testByteOperation.setIndex(FlagConstants.ESCAPE);
                    testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                    testByteOperation.setValue((byte)0);
                }
            }
        }
        else if (screen.getZoneIndex() == 7) {
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
            else if(screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                if(Settings.isRandomizeTransitionGates()) {
                    if(transitionGate.getArgs().get(0) == 18) {
                        return false;
                    }
                    else if(transitionGate.getArgs().get(0) == 9) {
                        TestByteOperation testByteOperation = transitionGate.getTestByteOperations().get(0);
                        testByteOperation.setIndex(FlagConstants.ESCAPE);
                        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                        testByteOperation.setValue((byte)0);
                    }
                }
                else {
                    // Don't change to true shrine until you have Feather, since the old shrine has more requirement options.
                    if (!transitionGate.getTestByteOperations().isEmpty()
                            && transitionGate.getTestByteOperations().get(0).getIndex() == FlagConstants.BOSSES_SHRINE_TRANSFORM) {
                        if(ByteOp.FLAG_EQUALS.equals(transitionGate.getTestByteOperations().get(0).getOp())) {
                            return false;
                        }
                        else {
                            transitionGate.getTestByteOperations().clear();
                        }
                    }
                }
            }
        }
        else if(screen.getZoneIndex() == 8) {
            if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 3) {
                if(Settings.isRandomizeTransitionGates()) {
                    if(transitionGate.getArgs().get(0) == 18) {
                        return false;
                    }
                    else if(transitionGate.getArgs().get(0) == 9) {
                        TestByteOperation testByteOperation = transitionGate.getTestByteOperations().get(0);
                        testByteOperation.setIndex(FlagConstants.ESCAPE);
                        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                        testByteOperation.setValue((byte)0);
                    }
                }
                else {
                    // Don't change to true shrine until you have Feather, since the old shrine has more requirement options.
                    if (ByteOp.FLAG_EQUALS.equals(transitionGate.getTestByteOperations().get(0).getOp())) {
                        TestByteOperation featherCheck = new TestByteOperation();
                        featherCheck.setIndex(FlagConstants.WF_FEATHER);
                        featherCheck.setOp(ByteOp.FLAG_EQUALS);
                        featherCheck.setValue((byte) 2);
                        transitionGate.getTestByteOperations().add(featherCheck);
                    }
                }
            }
            else if(screen.getRoomIndex() == 5 && screen.getScreenIndex() == 3) {
                if(Settings.isRandomizeTransitionGates()) {
                    if(transitionGate.getArgs().get(0) == 18) {
                        return false;
                    }
                    else if(transitionGate.getArgs().get(0) == 9) {
                        TestByteOperation testByteOperation = transitionGate.getTestByteOperations().get(0);
                        testByteOperation.setIndex(FlagConstants.ESCAPE);
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
                for(int i = 0; i < transitionGate.getTestByteOperations().size(); i++) {
                    TestByteOperation testByteOperation = transitionGate.getTestByteOperations().get(i);
                    if(testByteOperation.getIndex() == FlagConstants.EDEN_UNLOCKED) {
                        flagIndexToRemove = i;
                        break;
                    }
                }
                if(flagIndexToRemove != null) {
                    transitionGate.getTestByteOperations().remove((int)flagIndexToRemove);
                }
            }
        }
        return true;
    }

    @Override
    boolean updateEscapeTimer(GameObject escapeTimer) {
        if(Settings.isRandomizeTransitionGates()) {
            escapeTimer.getArgs().set(2, (short)10);
        }
        return true;
    }

    @Override
    boolean updateEscapeScreenShake(GameObject escapeScreenShake) {
        if(Settings.isScreenshakeDisabled()) {
            return false;
        }
        return true;
    }

    private void addEscapeGates() {
        if(Settings.isRandomizeTransitionGates()) {
            for(GameObject transitionGate : rcdFileData.getObjectsById(ObjectIdConstants.TransitionGate)) {
                Screen screen = (Screen)transitionGate.getObjectContainer();
//                FileUtils.logFlush(String.format("Gate on screen [%d, %d, %d] leads to screen [%d, %d, %d] with position (%d, %d)",
//                        screen.getZoneIndex(), screen.getRoomIndex(), screen.getScreenIndex(),
//                        gameObject.getArgs().get(0), gameObject.getArgs().get(1), gameObject.getArgs().get(2),
//                        gameObject.getArgs().get(3), gameObject.getArgs().get(4)));

                if(screen.getZoneIndex() == ZoneConstants.GUIDANCE) {
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        // gateName = "Transition: Guidance L1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                }
                else if(screen.getZoneIndex() == ZoneConstants.SURFACE) {
                    if (screen.getRoomIndex() == 11 && screen.getScreenIndex() == 1) {
                        // gateName = "Transition: Surface R1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                    else if (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 2) {
                        // gateName = "Transition: Surface D1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                    else if (screen.getRoomIndex() == 5 && screen.getScreenIndex() == 2) {
                        // gateName = "Transition: Surface D2";
                        AddObject.addEscapeGate(transitionGate);
                    }
                }
                else if(screen.getZoneIndex() == ZoneConstants.INFERNO) {
                    if (screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                        // gateName = "Transition: Inferno U2";
                        AddObject.addEscapeGate(transitionGate);
                    }
                }
                else if(screen.getZoneIndex() == ZoneConstants.EXTINCTION) {
                    if (screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                        // gateName = "Transition: Extinction U1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                }
                else if(screen.getZoneIndex() == ZoneConstants.TWIN_FRONT) {
                    if (screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                        // gateName = "Transition: Twin U2";
                        GameObject escapeGate = AddObject.addEscapeGate(transitionGate);
                    }
                }
                else if(screen.getZoneIndex() == ZoneConstants.ENDLESS) {
                    if (screen.getRoomIndex() == 2 && screen.getScreenIndex() == 3) {
                        // gateName = "Transition: Endless U1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                    else if (screen.getRoomIndex() == 5 && screen.getScreenIndex() == 3) {
                        // gateName = "Transition: Endless D1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                }
                else if(screen.getZoneIndex() == ZoneConstants.SHRINE_FRONT) {
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        // gateName = "Transition: Shrine U1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                }
                else if(screen.getZoneIndex() == ZoneConstants.RETRO_MAUSOLEUM) {
                    if (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                        // gateName = "Transition: Retromausoleum U1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                    else if (screen.getRoomIndex() == 1 && screen.getScreenIndex() == 2) {
                        // gateName = "Transition: Retromausoleum D1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                }
                else if(screen.getZoneIndex() == ZoneConstants.RETRO_GUIDANCE) {
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                        // gateName = "Transition: Retroguidance D1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                    else if (screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                        // gateName = "Transition: Retroguidance L1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                }
                else if(screen.getZoneIndex() == ZoneConstants.RETRO_SURFACE) {
                    if (screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                        // gateName = "Transition: Retrosurface R1";
                        AddObject.addEscapeGate(transitionGate);
                    }
                }
            }
        }
    }

    private void addGrailTabletLemezaDetectors() {
        if(Settings.isAutomaticGrailPoints()) {
            for(GameObject scannable : rcdFileData.getObjectsById(ObjectIdConstants.Scannable)) {
                ObjectContainer objectContainer = scannable.getObjectContainer();
                if(!(objectContainer instanceof Screen)) {
                    continue;
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
                    AddObject.addGrailDetector(scannable, LocationCoordinateMapper.getGrailFlag(zoneIndex, frontsideGrailScan));
                }
            }
        }
    }

    private void addMissingBacksideDoorObjects() {
        if(Settings.isRandomizeBacksideDoors() && !Settings.isRandomizeNonBossDoors()) {
            for(GameObject warpDoor : rcdFileData.getObjectsById(ObjectIdConstants.WarpDoor)) {
                if(warpDoor.getArgs().get(0) == 0) {
                    Screen screen = (Screen)warpDoor.getObjectContainer();
                    int zone = screen.getZoneIndex();
                    if(zone == ZoneConstants.SURFACE) {
                        // Surface => Tower of the Goddess ("Door: F5")
                        AddObject.addMissingBacksideDoorGate(warpDoor, FlagConstants.VIY_GATE_OPEN);
                    }
                    else if(zone == ZoneConstants.EXTINCTION) {
                        if(screen.getRoomIndex() != 7) {
                            // Chamber of Extinction [Magatama Left] => Chamber of Birth [Northeast] ("Door: F6")
                            AddObject.addMissingBacksideDoorGateTimerAndSound(screen, FlagConstants.PALENQUE_STATE, FlagConstants.PALENQUE_GATE_OPEN);
                            AddObject.addMissingBacksideDoorGate(warpDoor, FlagConstants.PALENQUE_GATE_OPEN);
                            AddObject.addMissingBacksideMirrorTimerAndSound(warpDoor.getObjectContainer(), FlagConstants.PALENQUE_GATE_MIRROR_COVER);
                            AddObject.addMissingBacksideDoorMirrorCoverGraphic(warpDoor, FlagConstants.PALENQUE_GATE_MIRROR_COVER, true);
                        }
                    }
                    else if(zone == ZoneConstants.RUIN) {
                        if(screen.getRoomIndex() != 2) {
                            // Tower of Ruin [Top] => Inferno Cavern [Spikes] ("Door: B7")
                            AddObject.addMissingBacksideDoorGateTimerAndSound(screen, FlagConstants.BAPHOMET_STATE, FlagConstants.BAPHOMET_GATE_OPEN);
                            AddObject.addMissingBacksideDoorGate(warpDoor, FlagConstants.BAPHOMET_GATE_OPEN);
                            AddObject.addMissingBacksideMirrorTimerAndSound(warpDoor.getObjectContainer(), FlagConstants.BAPHOMET_GATE_MIRROR_COVER);
                            AddObject.addMissingBacksideDoorMirrorCoverGraphic(warpDoor, FlagConstants.BAPHOMET_GATE_MIRROR_COVER, false);
                        }
                    }
                }
            }
        }
    }

    private void updateStartingScreen() {
        Screen screen = rcdFileData.getScreen(LocationCoordinateMapper.getStartingZone(),
                LocationCoordinateMapper.getStartingRoom(),
                LocationCoordinateMapper.getStartingScreen());
        int zoneIndex = screen.getZoneIndex();
        if(zoneIndex == ZoneConstants.RETRO_SURFACE) {
            GameObject grailTablet = AddObject.addRetroSurfaceGrailTablet(screen);
            AddObject.addHotspring(grailTablet);
        }
        else if(zoneIndex <= ZoneConstants.SHRINE_BACK
                && zoneIndex != ZoneConstants.SURFACE
                && LocationCoordinateMapper.getStartingZone() == zoneIndex
                && LocationCoordinateMapper.getStartingRoom() == screen.getRoomIndex()
                && LocationCoordinateMapper.getStartingScreen() == screen.getScreenIndex()) {
            List<GameObject> autosaves = screen.getObjectsById(ObjectIdConstants.Autosave);
            if(!autosaves.isEmpty()) {
                AddObject.addHotspring(autosaves.get(0));
            }
        }
        AddObject.addStartingItems(screen);
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
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 300, 880, 41,
                            Arrays.asList(new TestByteOperation(FlagConstants.AMPHISBAENA_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 5),
                                    new TestByteOperation(FlagConstants.AMPHISBAENA_STATE, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
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
                    warpTest.setIndex(FlagConstants.SURFACE_RUINS_FRONT_DOOR_OPEN);
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
            if(roomIndex == 8 && screenIndex == 1) {
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 900, 120, 75,
                            Arrays.asList(new TestByteOperation(FlagConstants.SAKIT_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 1),
                                    new TestByteOperation(FlagConstants.SAKIT_STATE, ByteOp.FLAG_LT, 2),
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
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 400, 320, 104,
                            Arrays.asList(new TestByteOperation(FlagConstants.ELLMAC_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 5),
                                    new TestByteOperation(FlagConstants.ELLMAC_STATE, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
                }
            }
        }
        else if(zoneIndex == 4) {
            if(roomIndex == 3 && screenIndex == 3) {
                AddObject.addTransformedMrFishmanShopDoorGraphic(screen);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 380, 340, 136,
                            Arrays.asList(new TestByteOperation(FlagConstants.BAHAMUT_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 1),
                                    new TestByteOperation(FlagConstants.SPRING_BAHAMUT_ROOM_FLOODED, ByteOp.FLAG_EQUALS, 1),
                                    new TestByteOperation(FlagConstants.BAHAMUT_STATE, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
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
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 460, 560, 149,
                            Arrays.asList(new TestByteOperation(FlagConstants.VIY_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 4),
                                    new TestByteOperation(FlagConstants.VIY_STATE, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
                }
            }
        }
        else if(zoneIndex == 6) {
            if(roomIndex == 2 && screenIndex == 0) {
                if(Settings.isRandomizeTransitionGates()) {
                    AddObject.addExtinctionTorch(screen, 60, 80);
                }
            }
            if(roomIndex == 3 && screenIndex == 1) {
                if(Settings.isRandomizeTransitionGates()) {
                    AddObject.addExtinctionTorch(screen, 680, 240);
                }
            }
            if(Settings.isBossCheckpoints()) {
                if(roomIndex == 9 && screenIndex == 1) {
                    AddObject.addAutosave(screen, 940, 400, 170,
                            Arrays.asList(new TestByteOperation(FlagConstants.EXTINCTION_PALENQUE_SCREEN_MURAL, ByteOp.FLAG_EQUALS, 3),
                                    new TestByteOperation(FlagConstants.PALENQUE_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 3),
                                    new TestByteOperation(FlagConstants.PALENQUE_STATE, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
                }
            }
            if(Settings.isRandomizeBosses()) {
                if(roomIndex == 9 && screenIndex == 1) {
                    AddObject.addTwinLabsDoor(screen);
                }
            }
            if(Settings.isFeatherlessMode()) {
                if(roomIndex == 1 && screenIndex == 1) {
                    // Land on Spriggan without feather
                    AddObject.addPot(screen, 780, 400, PotGraphic.EXTINCTION_POT,
                            DropType.NOTHING, 0, HolidaySettings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0), new ArrayList<>(0));
                }
            }
        }
        else if(zoneIndex == 7) {
            if(roomIndex == 4 && screenIndex == 1) {
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 940, 80, 188,
                            Arrays.asList(new TestByteOperation(FlagConstants.BAPHOMET_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 2),
                                    new TestByteOperation(FlagConstants.BAPHOMET_STATE, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1)); // Text block 206 is backside Twin Labs grail, but they seem to be identical.
                }
            }
            else if(roomIndex == 10 && screenIndex == 1) {
                if(Settings.isFeatherlessMode()) {
                    // Access to Dimensional without Feather
                    AddObject.addPot(screen, 840, 320, PotGraphic.TWIN_LABYRINTHS,
                            DropType.NOTHING, 0, HolidaySettings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0), new ArrayList<>(0));
                }
            }
        }
        else if(zoneIndex == 9) {
            if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addGrailTrueShrineBackupDoor(screen);
                if(Settings.isFeatherlessMode()) {
                    AddObject.addPot(screen, 280, 240, PotGraphic.SHRINE, DropType.NOTHING, 0, new ArrayList<>(0), new ArrayList<>(0));
                }
            }
        }
        else if(zoneIndex == 10) {
            if(Settings.isFeatherlessMode()) {
                if(roomIndex == 1 && screenIndex == 0) {
                    // For not having to damage boost up Gate of Illusion to Cog of the Soul
                    AddObject.addPot(screen, 580, 280, PotGraphic.ILLUSION,
                            DropType.NOTHING, 0, HolidaySettings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0), new ArrayList<>(0));
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
            if(Settings.isFeatherlessMode()) {
                if(roomIndex == 8) {
                    if(screenIndex == 2) {
                        // Nuwa assist
                        AddObject.addPot(screen, 1840, 180, PotGraphic.RUIN,
                                DropType.NOTHING, 0, HolidaySettings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0), new ArrayList<>(0));
                        AddObject.addPot(screen, 1840, 220, PotGraphic.RUIN,
                                DropType.NOTHING, 0, HolidaySettings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0), new ArrayList<>(0));
                    }
                }
            }
        }
        else if(zoneIndex == 15) {
            if(Settings.isFeatherlessMode() && !Settings.getEnabledGlitches().contains("Raindrop")) {
                if(roomIndex == 2 && screenIndex == 1) {
                    // Access to Palenque without Feather
                    AddObject.addPot(screen, 20, 760, PotGraphic.BIRTH,
                            DropType.NOTHING, 0, HolidaySettings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0), new ArrayList<>(0));
                    AddObject.addPot(screen, 20, 800, PotGraphic.BIRTH,
                            DropType.NOTHING, 0, HolidaySettings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0), new ArrayList<>(0));
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
                            Arrays.asList(new TestByteOperation(FlagConstants.TIAMAT_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 1),
                                    new TestByteOperation(FlagConstants.TIAMAT_STATE, ByteOp.FLAG_LT, 2),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
                }
            }
            if(Settings.isFeatherlessMode()) {
                if(roomIndex == 0 && screenIndex == 1) {
                    AddObject.addPot(screen, 500, 680, PotGraphic.DIMENSIONAL,
                            DropType.NOTHING, 0, HolidaySettings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0), new ArrayList<>(0));
                    AddObject.addPot(screen, 500, 720, PotGraphic.DIMENSIONAL,
                            DropType.NOTHING, 0, HolidaySettings.isFools2020Mode() ? Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2)) : new ArrayList<>(0), new ArrayList<>(0));
                }
            }
        }
        else if(zoneIndex == 18) {
            if (roomIndex == 0 && screenIndex == 0) {
                if(HolidaySettings.isFools2022Mode()) {
                    AddObject.addUpperUntrueShrineBackupDoor(screen);
                }
                else {
                    AddObject.addUpperUntrueShrineBackupDoor(screen,
                            new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 9));
                }
            }
            if (roomIndex == 1 && screenIndex == 1) {
                if(Settings.isFeatherlessMode()) {
                    AddObject.addTrueShrineFeatherlessPlatform(screen, 800, 240);
                    AddObject.addTrueShrineFeatherlessPlatform(screen, 1020, 220);
                }
            }
            else if (roomIndex == 3 && screenIndex == 1) {
                // Mother ankh checkpoint
                if(Settings.isBossCheckpoints()) {
                    AddObject.addAutosave(screen, 660, 400, 231,
                            Arrays.asList(new TestByteOperation(FlagConstants.MOTHER_ANKH_PUZZLE, ByteOp.FLAG_GTEQ, 1),
                                    new TestByteOperation(FlagConstants.MOTHER_STATE, ByteOp.FLAG_LTEQ, 2),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
                }
            }
            else if (roomIndex == 7 && screenIndex == 0) {
                AddObject.addGrailUntrueShrineBackupDoor(screen);
            }
            else if (roomIndex == 8 && screenIndex == 1) {
                if(HolidaySettings.isFools2022Mode()) {
                    AddObject.addLowerUntrueShrineBackupDoor(screen);
                }
                else {
                    AddObject.addLowerUntrueShrineBackupDoor(screen,
                            new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 9));
                }
            }
            else if (roomIndex == 9 && screenIndex == 0) {
                if(HolidaySettings.isFools2022Mode()) {
                    AddObject.addSealUntrueShrineBackupDoor(screen);
                }
                else {
                    AddObject.addSealUntrueShrineBackupDoor(screen,
                            new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 9));
                }
            }
        }
    }

    @Override
    void addUntrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == LocationCoordinateMapper.getStartingZone()
                && roomIndex == LocationCoordinateMapper.getStartingRoom()
                && screenIndex == LocationCoordinateMapper.getStartingScreen()) {
            if (Settings.isAutomaticHardmode()) {
                AddObject.addAutomaticHardmodeTimer(screen);
            }
            if (Settings.isAutomaticTranslations()) {
                AddObject.addAutomaticTranslationsTimer(screen);
            }
            AddObject.addGrailToggle(screen, true);
//            AddObject.addEscapeTimer(screen);
        }

        if(zoneIndex == 0) {
            if(roomIndex == 4 && screenIndex == 1) {
                // Ensure you can't lose access to the Guidance elevator. // todo: maybe find a better solution that respects logic
                AddObject.addSecondsTimer(screen, 0,
                        Arrays.asList(new TestByteOperation(FlagConstants.HT_UNLOCK_PROGRESS_EARLY, ByteOp.FLAG_GT, 1), new TestByteOperation(FlagConstants.GUIDANCE_ELEVATOR, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.GUIDANCE_ELEVATOR, ByteOp.ASSIGN_FLAG, 1)));
            }
        }
        else if(zoneIndex == 1) {
            if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addSacredOrbCountTimers(screen);
            }
            else if(roomIndex == 2) {
                if(screenIndex == 0) {
                    if (Settings.isSaveFileNeeded()) {
                        AddObject.addSurfaceKillTimer(screen, false);
                    }
                }
                if(screenIndex == 1) {
                    if (Settings.isSaveFileNeeded()) {
                        AddObject.addSurfaceKillTimer(screen, true);
                    }
                }
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                if (Settings.isSaveFileNeeded()) {
                    AddObject.addSurfaceKillTimer(screen, false);
                }
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                if(Settings.isAlternateMotherAnkh()) {
                    AddObject.addMotherAnkhJewelRecoveryTimer(screen);
                }
            }
        }
        else if(zoneIndex == 3) {
            if(Settings.isRandomizeBosses()) {
                if(roomIndex == 4 && screenIndex == 2) {
                    AddObject.addBossTimer(screen, FlagConstants.ELLMAC_STATE, 0x2d8);
                }
                else if(roomIndex == 8 && screenIndex == 0) {
                    AddObject.addSphinxRemovalTimer(screen);
                }
            }
            if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addWeightDoorTimer(screen, 0x032);
            }
        }
        else if(zoneIndex == 6) {
            if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addFramesTimer(screen, 0,
                        Arrays.asList(new TestByteOperation(FlagConstants.PALENQUE_SMALL_MURAL_ANIMATION_TRIGGERED, ByteOp.FLAG_EQUALS, 1),
                                new TestByteOperation(FlagConstants.PALENQUE_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 1)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.PALENQUE_ANKH_PUZZLE, ByteOp.ASSIGN_FLAG, 2)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addFramesTimer(screen, 0,
                        Arrays.asList(new TestByteOperation(FlagConstants.PALENQUE_SMALL_MURAL_ANIMATION_TRIGGERED, ByteOp.FLAG_EQUALS, 1),
                                new TestByteOperation(FlagConstants.PALENQUE_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 1)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.PALENQUE_ANKH_PUZZLE, ByteOp.ASSIGN_FLAG, 2)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addFramesTimer(screen, 0,
                        Arrays.asList(new TestByteOperation(FlagConstants.PALENQUE_SMALL_MURAL_ANIMATION_TRIGGERED, ByteOp.FLAG_EQUALS, 1),
                                new TestByteOperation(FlagConstants.PALENQUE_ANKH_PUZZLE, ByteOp.FLAG_EQUALS, 1)),
                        Arrays.asList(new WriteByteOperation(FlagConstants.PALENQUE_ANKH_PUZZLE, ByteOp.ASSIGN_FLAG, 2)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addPalenqueMSX2Timer(screen);
                if(Settings.isRandomizeBosses()) {
                    AddObject.addTwinLabsPoisonTimerRemoval(screen, true);
                }
            }
        }
        else if(zoneIndex == 7) {
            if(roomIndex == 15 && screenIndex == 2) {
                AddObject.addAllMantrasRecitedTimer(screen);
            }

            if(Settings.isRandomizeBosses()) {
                if(roomIndex == 0 && screenIndex == 0) {
                    AddObject.addBossTimer(screen, FlagConstants.PALENQUE_STATE, 0x2db);
                }
            }
        }
        else if(zoneIndex == 8) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addAllMantrasRecitedTimer(screen);
            }
        }
        else if(zoneIndex == 9 && roomIndex == 2 && screenIndex == 0) {
            AddObject.addDiaryChestConditionTimer(screen);
        }
        else if(zoneIndex == 10) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addAllMantrasRecitedTimer(screen);
            }
            if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addWeightDoorTimer(screen, FlagConstants.ROOM_FLAG_37);
            }
        }
        else if(zoneIndex == 11) {
            if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addAllMantrasRecitedTimer(screen);
            }
        }
        else if(zoneIndex == 12) {
            if(roomIndex == 10 && screenIndex == 0) {
                AddObject.addAllMantrasRecitedTimer(screen);
            }
            if(roomIndex == 2) {
                if(screenIndex == 0) {
                    AddObject.addMoonlightPassageTimer(screen);
                }
                else if(screenIndex == 1) {
                    AddObject.addWeightDoorTimer(screen, FlagConstants.ROOM_FLAG_45);
                }
            }
            if(roomIndex == 3) {
                if(screenIndex == 0) {
                    AddObject.addWeightDoorTimer(screen, FlagConstants.ROOM_FLAG_37);
                }
            }
        }
        else if(zoneIndex == 13) {
            if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addAllMantrasRecitedTimer(screen);
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
            if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addAllMantrasRecitedTimer(screen);
            }
        }
        else if(zoneIndex == 15) {
            if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addAllMantrasRecitedTimer(screen);
            }
        }
        else if(zoneIndex == 17) {
            if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addAngelShieldPuzzleTimers(screen);
            }
        }
    }

    @Override
    void doUntrackedPostUpdates(){
        potUpdater.addMissingPots();
        addMissingBacksideDoorObjects();
        addGrailTabletLemezaDetectors();
        updateStartingScreen();
    }

    @Override
    void addTrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == 5) {
            if(roomIndex == 1 && screenIndex == 1) {
                if(Settings.isRandomizeTrapItems() && !HolidaySettings.isFools2020Mode()) {
                    AddObject.addFloatingItem(screen, 180, 640, ItemConstants.SACRED_ORB, false,
                            Arrays.asList(new TestByteOperation(FlagConstants.WF_TRAP_INFERNO, ByteOp.FLAG_EQUALS, 0)),
                            Arrays.asList(new WriteByteOperation(FlagConstants.WF_TRAP_INFERNO, ByteOp.ASSIGN_FLAG, 1)));
                }
            }
        }
        else if(zoneIndex == 7) {
            if(roomIndex == 12 && screenIndex == 0) {
                if(Settings.isRandomizeTrapItems()) {
                    AddObject.addFloatingItem(screen, 580, 160, ItemConstants.ANKH_JEWEL, false,
                            Arrays.asList(new TestByteOperation(FlagConstants.WF_TRAP_TWIN, ByteOp.FLAG_EQUALS, 0)),
                            Arrays.asList(new WriteByteOperation(FlagConstants.WF_TRAP_TWIN, ByteOp.ASSIGN_FLAG, 1)));
                }
            }
        }
        if(zoneIndex == 9) {
            if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addUntrueShrineExit(screen, 0);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addUntrueShrineExit(screen, 1);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addUntrueShrineExit(screen, 2);
            }
        }
    }

    @Override
    void addTrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == 1) {
            if(roomIndex == 2) {
                if(screenIndex == 0) {
                    AddObject.addAltSurfaceShopItemTimer(screen);
                }
            }
        }
    }

    @Override
    void doTrackedPostUpdates(){
        addEscapeGates();
        addMissingBacksideDoorObjects();
    }

    @Override
    void updateScreenExits(Screen screen) {
        if(Settings.isRandomizeTransitionGates()
                && (screen.getZoneIndex() == 2 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 2
                || screen.getZoneIndex() == 19 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 1)) {
            // Fix some surface warps that aren't standard -(1, -1, -1)
            screen.getScreenExit(ScreenExit.DOWN).setDestination(-1, -1, -1);
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

        if(!LocationCoordinateMapper.isSurfaceStart() || Settings.getCurrentStartingLocation() == ZoneConstants.NIGHT_SURFACE) {
            for(ScreenExit screenExit : screen.getScreenExits()) {
                if(screenExit.getZoneIndex() == -1 && screenExit.getRoomIndex() == -1 && screenExit.getScreenIndex() == -1) {
                    screenExit.setDestination(LocationCoordinateMapper.getStartingZone(),
                            LocationCoordinateMapper.getStartingRoom(),
                            LocationCoordinateMapper.getStartingScreen());
                }
            }
        }
    }

    private static void replaceBacksideDoorFlags(GameObject gameObject, Integer bossNumber, Integer gateFlag, boolean motherCheck) {
        gameObject.getTestByteOperations().clear();

        Integer bossFlag = FlagConstants.getBossFlag(bossNumber);
        if(bossFlag != null) {
            gameObject.getTestByteOperations().add(new TestByteOperation(bossFlag, ByteOp.FLAG_EQUALS, bossNumber == 9 ? 1 : 3));
        }

        if(gateFlag != null) {
            gameObject.getTestByteOperations().add(new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 1));
        }

        if(motherCheck && !Settings.isRandomizeTransitionGates()) {
            gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.MOTHER_STATE, ByteOp.FLAG_NOT_EQUAL, 3));
        }
    }
}
