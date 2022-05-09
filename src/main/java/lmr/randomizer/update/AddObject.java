package lmr.randomizer.update;

import lmr.randomizer.*;
import lmr.randomizer.dat.blocks.Block;
import lmr.randomizer.dat.blocks.contents.*;
import lmr.randomizer.dat.blocks.MasterNpcBlock;
import lmr.randomizer.dat.blocks.ShopBlock;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.randomization.data.GameObjectId;
import lmr.randomizer.randomization.data.ShopInventory;
import lmr.randomizer.randomization.data.ShopInventoryData;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AddObject {
    private AddObject() { }

    private static ObjectContainer mulbrukScreen;

    public static void setMulbrukScreen(ObjectContainer mulbrukScreen) {
        AddObject.mulbrukScreen = mulbrukScreen;
    }

    public static void clearObjects() {
        mulbrukScreen = null;
    }

    /**
     * Convenience for adding a timer object to any screen.
     * @param objectContainer to add the timer object to
     * @param delaySeconds seconds to wait before the timer runs its updates
     * @param tests tests to put on the timer object
     * @param updates updates the timer object should make when all of its tests pass
     */
    public static GameObject addSecondsTimer(ObjectContainer objectContainer, int delaySeconds, List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        FlagTimer obj = new FlagTimer(objectContainer);
        obj.setDelaySeconds(delaySeconds);

        obj.getTestByteOperations().addAll(tests);
        obj.getWriteByteOperations().addAll(updates);
        objectContainer.getObjects().add(0, obj);
        return obj;
    }

    /**
     * Convenience for adding a timer object to any screen.
     * @param objectContainer to add the timer object to
     * @param delayFrames seconds to wait before the timer runs its updates
     * @param tests tests to put on the timer object
     * @param updates updates the timer object should make when all of its tests pass
     */
    public static GameObject addFramesTimer(ObjectContainer objectContainer, int delayFrames,
                                            List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        FlagTimer obj = new FlagTimer(objectContainer);
        obj.setDelayFrames(delayFrames);

        obj.getTestByteOperations().addAll(tests);
        obj.getWriteByteOperations().addAll(updates);
        objectContainer.getObjects().add(0, obj);
        return obj;
    }

    public static void addFloatingItem(Screen screen, int x, int y, int itemArg, boolean realItem, List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        FloatingItem obj = addFloatingItem(screen, x, y, itemArg, realItem);
        obj.getTestByteOperations().addAll(tests);
        obj.getWriteByteOperations().addAll(updates);
    }

    public static FloatingItem addFloatingItem(Screen screen, int x, int y, int itemArg, boolean realItem) {
        FloatingItem obj = new FloatingItem(screen, x, y);
        obj.setInventoryWord(itemArg);
        obj.setRealItem(realItem);
        screen.getObjects().add(obj);
        return obj;
    }

    /**
     * Add a quicksave object (not active during the escape, and not if you don't have grail)
     * @param screen to add the object to
     * @param x position of the quicksave object
     * @param y position of the quicksave object
     * @param tests existence tests for the quicksave
     * @param update update to make when the quicksave happens
     */
    public static GameObject addAutosave(Screen screen, int x, int y, int textBlock, List<TestByteOperation> tests, WriteByteOperation update) {
        Autosave obj = new Autosave(screen, x, y);
        obj.setTextBlock(textBlock);

        obj.getTestByteOperations().addAll(tests);
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0));
        obj.getWriteByteOperations().add(update);
        screen.getObjects().add(obj);
        return obj;
    }

    /**
     * Add a timer to set the flag for solving the Diary chest puzzle if the appropriate conditions are met.
     * @param screen the screen to add the timers to
     */
    public static void addDiaryChestConditionTimer(Screen screen) {
        FlagTimer obj = new FlagTimer(screen);
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.XELPUD_CONVERSATION_TALISMAN_FOUND, ByteOp.FLAG_GTEQ, 3));
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.SHRINE_DRAGON_BONE, ByteOp.FLAG_GTEQ, 1));
        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SHRINE_DIARY_CHEST, ByteOp.ASSIGN_FLAG, 2));
        screen.getObjects().add(0, obj);
    }

    /**
     * Add a timer to trigger
     * @param screen the screen to add the timers to
     */
    public static void addPalenqueMSX2Timer(Screen screen) {
        FlagTimer obj = new FlagTimer(screen);
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 4));
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.XELPUD_CONVERSATION_MSX2, ByteOp.FLAG_EQUALS, 0));
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0));

        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.XELPUD_CONVERSATION_MSX2, ByteOp.ASSIGN_FLAG, 1));
        if(!HolidaySettings.isHalloween2021Mode()) {
            obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAIL_43, ByteOp.ASSIGN_FLAG, 1));
            obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAIL_COUNT, ByteOp.ADD_FLAG, 1));
        }

        screen.getObjects().add(0, obj);
    }

    /**
     * Add a timer to open weight doors during the escape that don't open naturally.
     * @param screen the screen to add the timers to
     */
    public static void addWeightDoorTimer(Screen screen, int weightFlag) {
        FlagTimer obj = new FlagTimer(screen);
        obj.setDelaySeconds(1);

        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1));
        obj.getTestByteOperations().add(new TestByteOperation(weightFlag, ByteOp.FLAG_EQUALS, 0));

        obj.getWriteByteOperations().add(new WriteByteOperation(weightFlag, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(0, obj);
    }

    /**
     * Add a timer to set the flag that resets the Twin Labyrinths poison timer
     * @param screen the screen to add the timers to
     */
    public static void addTwinLabsPoisonTimerRemoval(ObjectContainer screen, boolean resetPuzzle) {
        FlagTimer obj = new FlagTimer(screen);

        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.TWINS_RELEASED, ByteOp.FLAG_LTEQ, 1));
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.TWINS_POISON_TIMER, ByteOp.FLAG_NOT_EQUAL, 0));

        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.TWINS_POISON_TIMER, ByteOp.ASSIGN_FLAG, 0));

        if(resetPuzzle) {
            obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.TWINS_RELEASED, ByteOp.ASSIGN_FLAG, 0));
        }

        screen.getObjects().add(0, obj);
    }

    /**
     * Add a timer to make Isis' Pendant room ceiling breakable
     * @param screen the screen to add the timers to
     */
    public static void addIsisRoomCeilingTimer(ObjectContainer screen) {
        FlagTimer obj = new FlagTimer(screen);

        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.BUER_STATE, ByteOp.FLAG_EQUALS, 1));

        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.BUER_STATE, ByteOp.ASSIGN_FLAG, 2));

        screen.getObjects().add(0, obj);
    }

    /**
     * Add a timer to make Goddess statue shield disappear after leaving the room
     * @param screen the screen to add the timers to
     */
    public static void addGoddessShieldTimer(ObjectContainer screen) {
        FlagTimer obj = new FlagTimer(screen);

        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.GODDESS_STATUE_SHIELD_ANIMATION, ByteOp.FLAG_GTEQ, 2));
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.GODDESS_STATUE_SHIELD_EXISTS, ByteOp.FLAG_EQUALS, 0));

        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.GODDESS_STATUE_SHIELD_EXISTS, ByteOp.ASSIGN_FLAG, 1));
        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.GODDESS_STATUE_SHIELD_ANIMATION, ByteOp.ASSIGN_FLAG, 3));

        screen.getObjects().add(0, obj);
    }

    /**
     * Add a lemeza detector to set the flag for making the goddess statue disappear.
     * @param transitionGate the gate to put the detector with
     */
    public static void addGoddessStatueLemezaDetector(GameObject transitionGate) {
        addLemezaDetector(transitionGate.getObjectContainer(), transitionGate.getX() - 40, transitionGate.getY() - 20, 2, 3,
                Arrays.asList(new TestByteOperation(FlagConstants.GODDESS_STATUE_RUIN, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(new WriteByteOperation(FlagConstants.GODDESS_STATUE_RUIN, ByteOp.ASSIGN_FLAG, 1)));
    }

    /**
     * Add a lemeza detector to set the flag for solving the Endless Corridor Map chest puzzle after leaving through the left exit of Endless Corridor
     * @param transitionGate the gate to put the detector with
     */
    public static void addEndlessCorridorLeftExitLemezaDetector(GameObject transitionGate) {
        addLemezaDetector(transitionGate.getObjectContainer(), transitionGate.getX() - 40, transitionGate.getY() - 20, 2, 3,
                Arrays.asList(new TestByteOperation(FlagConstants.ENDLESS_PUZZLE_MAP_CHEST, ByteOp.FLAG_EQUALS, 1)),
                Arrays.asList(new WriteByteOperation(FlagConstants.ENDLESS_PUZZLE_MAP_CHEST, ByteOp.ASSIGN_FLAG, 2),
                        new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1)));

        addSuccessSound(transitionGate.getObjectContainer(), Arrays.asList(
                new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2),
                new TestByteOperation(FlagConstants.ENDLESS_PUZZLE_MAP_CHEST, ByteOp.FLAG_EQUALS, 2),
                new TestByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.FLAG_EQUALS, 1)));
    }

    /**
     * Add timer to fix the puzzle for passage between Temple of Moonlight and Twin Labyrinths
     * @param screen the screen to add the timers to
     */
    public static void addMoonlightPassageTimer(Screen screen) {
        FlagTimer obj = new FlagTimer(screen);

        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.MOONLIGHT_TO_TWIN_BREAKABLE_FLOOR, ByteOp.FLAG_EQUALS, 1));

        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MOONLIGHT_TO_TWIN_BREAKABLE_FLOOR, ByteOp.ASSIGN_FLAG, 0));

        screen.getObjects().add(0, obj);
    }

    /**
     * Add timer for failed Flail Whip puzzle.
     * @param screen the screen to add the timers to
     */
    public static void addFlailWhipPuzzleTimer(Screen screen) {
        FlagTimer obj = new FlagTimer(screen);

        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.GODDESS_PUZZLE_FLAIL_WHIP, ByteOp.FLAG_EQUALS, 1));

        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.GODDESS_PUZZLE_FLAIL_WHIP, ByteOp.ASSIGN_FLAG, 0));

        screen.getObjects().add(0, obj);
    }

    /**
     * Add timer for failed Angel Shield puzzle. Note that we're making a backup timer for both daises, even though
     * logically the number of children killed should always be odd when all 11 are dead. Having a safety net for
     * both of them should be harmless, and who knows what sort of odd game state weirdness can happen?
     * @param screen the screen to add the timers to
     */
    public static void addAngelShieldPuzzleTimers(Screen screen) {
        // Timer to spawn dais for even number of children
        FlagTimer obj = new FlagTimer(screen);
        obj.setDelayFrames(30);

        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.DIMENSIONAL_CHILDREN_PARITY, ByteOp.FLAG_EQUALS, 1));
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.DIMENSIONAL_ANGEL_SHIELD_DAIS_LEFT, ByteOp.FLAG_EQUALS, 0));
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.DIMENSIONAL_CHILDREN_DEAD, ByteOp.FLAG_GTEQ, 11));

        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_0, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(0, obj);

        // Timer to spawn dais for odd number of children
        obj = new FlagTimer(screen);
        obj.setDelayFrames(30);

        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.DIMENSIONAL_CHILDREN_PARITY, ByteOp.FLAG_EQUALS, 0));
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.DIMENSIONAL_ANGEL_SHIELD_DAIS_RIGHT, ByteOp.FLAG_EQUALS, 0));
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.DIMENSIONAL_CHILDREN_DEAD, ByteOp.FLAG_GTEQ, 11));

        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_0, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(0, obj);
    }

    /**
     * Add timer to track sacred orb count incremented
     * @param screen the screen to add the timers to
     */
    public static void addSacredOrbCountTimers(Screen screen) {
        for(int i = 0; i < 10; i++) {
            FlagTimer obj = new FlagTimer(screen);

            obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.ORB_COUNT_INCREMENTED_GUIDANCE + i, ByteOp.FLAG_EQUALS, 0));
            obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_SACRED_ORB_GUIDANCE + i, ByteOp.FLAG_EQUALS, 2));

            obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.ORB_COUNT_INCREMENTED_GUIDANCE + i, ByteOp.ASSIGN_FLAG, 1));
            obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SACRED_ORB_COUNT, ByteOp.ADD_FLAG, 1));

            screen.getObjects().add(0, obj);
        }
    }

    /**
     * Add kill timers for randomized main weapons.
     * @param screen the screen to add the objects to
     * @param isXelpudScreen true if this is Xelpud's screen, where the kill timer should not activate until after you talk to him
     */
    public static void addSurfaceKillTimer(Screen screen, boolean isXelpudScreen) {
        FlagTimer killTimer = new FlagTimer(screen);

        killTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.RANDOMIZER_SAVE_LOADED, ByteOp.FLAG_NOT_EQUAL, 1));

        if(isXelpudScreen) {
            killTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.XELPUD_CONVERSATION_INTRO, ByteOp.FLAG_EQUALS, 1));
        }

        killTimer.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.KILL_FLAG, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(0, killTimer);
    }

    /**
     * Add a backup door from Chamber of Extinction to untransformed Shrine of the Mother.
     * @param screen the screen to add the objects to
     */
    public static void addLowerUntrueShrineBackupDoor(Screen screen, TestByteOperation... additionalTests) {
        WarpDoor warpDoor = addWarpDoor(screen, 260, 800,9, 8, 1, 300, 320, new ArrayList<>(0));
        warpDoor.addTests(additionalTests);

        GraphicsTextureDraw backupShrineDoorGraphic = new GraphicsTextureDraw(screen, 240, 760);
        backupShrineDoorGraphic.addTests(additionalTests);

        backupShrineDoorGraphic.setLayer(-1);
        backupShrineDoorGraphic.setImageFile("01effect.png");
        backupShrineDoorGraphic.setImageX(0);
        backupShrineDoorGraphic.setImageY(512);
        backupShrineDoorGraphic.setImageWidth(80);
        backupShrineDoorGraphic.setImageHeight(80);
        backupShrineDoorGraphic.setAnimation(0, 0, 1, 0);
        backupShrineDoorGraphic.setCollision(HitTile.Air);
        backupShrineDoorGraphic.setRGBAMax(0, 0, 0, 255);
        backupShrineDoorGraphic.setArg23(1);

        screen.getObjects().add(backupShrineDoorGraphic);
    }

    /**
     * Add a backup door from transformed Shrine of the Mother grail to untransformed Shrine of the Mother grail.
     * @param screen the screen to add the objects to
     */
    public static void addGrailUntrueShrineBackupDoor(Screen screen) {
        addWarpDoor(screen, 500, 80, 9, 7, 0, 500, 80,
                Arrays.asList(new TestByteOperation(LocationCoordinateMapper.getGrailFlag(ZoneConstants.SHRINE_FRONT, true), ByteOp.FLAG_EQUALS, 1)));

        GraphicsTextureDraw backupShrineDoorGraphic = new GraphicsTextureDraw(screen, 480, 40);

        backupShrineDoorGraphic.getTestByteOperations().add(new TestByteOperation(LocationCoordinateMapper.getGrailFlag(ZoneConstants.SHRINE_FRONT, true), ByteOp.FLAG_EQUALS, 1));

        backupShrineDoorGraphic.setLayer(-1);
        backupShrineDoorGraphic.setImageFile("01effect.png");
        backupShrineDoorGraphic.setImageX(0);
        backupShrineDoorGraphic.setImageY(512);
        backupShrineDoorGraphic.setImageWidth(80);
        backupShrineDoorGraphic.setImageHeight(80);
        backupShrineDoorGraphic.setAnimation(0, 0, 1, 0);
        backupShrineDoorGraphic.setCollision(HitTile.Air);
        backupShrineDoorGraphic.setRGBAMax(0, 0, 0, 255);
        backupShrineDoorGraphic.setArg23(1);

        screen.getObjects().add(backupShrineDoorGraphic);
    }

    /**
     * Add a backup door from untransformed Shrine of the Mother grail to transformed Shrine of the Mother grail.
     * @param screen the screen to add the objects to
     */
    public static void addGrailTrueShrineBackupDoor(Screen screen) {
        addWarpDoor(screen, 500, 80,18, 7, 0, 500, 80,
                Arrays.asList(new TestByteOperation(LocationCoordinateMapper.getGrailFlag(ZoneConstants.SHRINE_BACK, false), ByteOp.FLAG_EQUALS, 1)));

        GraphicsTextureDraw backupShrineDoorGraphic = new GraphicsTextureDraw(screen, 480, 40);

        backupShrineDoorGraphic.getTestByteOperations().add(new TestByteOperation(LocationCoordinateMapper.getGrailFlag(ZoneConstants.SHRINE_BACK, false), ByteOp.FLAG_EQUALS, 1));

        backupShrineDoorGraphic.setLayer(-1);
        backupShrineDoorGraphic.setImageFile("01effect.png");
        backupShrineDoorGraphic.setImageX(0);
        backupShrineDoorGraphic.setImageY(512);
        backupShrineDoorGraphic.setImageWidth(80);
        backupShrineDoorGraphic.setImageHeight(80);
        backupShrineDoorGraphic.setAnimation(0, 0, 1, 0);
        backupShrineDoorGraphic.setCollision(HitTile.Air);
        backupShrineDoorGraphic.setRGBAMax(0, 0, 0, 255);
        backupShrineDoorGraphic.setArg23(1);

        screen.getObjects().add(backupShrineDoorGraphic);
    }

    /**
     * Add a backup door from Chamber of Extinction to untransformed Shrine of the Mother.
     * @param screen the screen to add the objects to
     */
    public static void addUpperUntrueShrineBackupDoor(Screen screen, TestByteOperation... additionalTests) {
        WarpDoor warpDoor = addWarpDoor(screen, 340, 80,9, 0, 0, 340, 92, new ArrayList<>(0));
        warpDoor.addTests(additionalTests);

        GraphicsTextureDraw backupShrineDoorGraphic = new GraphicsTextureDraw(screen, 320, 40);
        backupShrineDoorGraphic.addTests(additionalTests);

        backupShrineDoorGraphic.setLayer(-1);
        backupShrineDoorGraphic.setImageFile("01effect.png");
        backupShrineDoorGraphic.setImageX(0);
        backupShrineDoorGraphic.setImageY(512);
        backupShrineDoorGraphic.setImageWidth(80);
        backupShrineDoorGraphic.setImageHeight(80);
        backupShrineDoorGraphic.setAnimation(0, 0, 1, 0);
        backupShrineDoorGraphic.setCollision(HitTile.Air);
        backupShrineDoorGraphic.setRGBAMax(0, 0, 0, 255);
        backupShrineDoorGraphic.setArg23(1);

        screen.getObjects().add(backupShrineDoorGraphic);
    }

    /**
     * Add a backup door from Endless Corridor to untransformed Shrine of the Mother.
     * @param screen the screen to add the objects to
     */
    public static void addSealUntrueShrineBackupDoor(Screen screen, TestByteOperation... additionalTests) {
        WarpDoor warpDoor = addWarpDoor(screen, 500, 400,9, 9, 0, 300, 332, new ArrayList<>(0));
        warpDoor.addTests(additionalTests);

        GraphicsTextureDraw backupShrineDoorGraphic = new GraphicsTextureDraw(screen, 480, 360);
        backupShrineDoorGraphic.addTests(additionalTests);

        backupShrineDoorGraphic.setLayer(-1);
        backupShrineDoorGraphic.setImageFile("01effect.png");
        backupShrineDoorGraphic.setImageX(0);
        backupShrineDoorGraphic.setImageY(512);
        backupShrineDoorGraphic.setImageWidth(80);
        backupShrineDoorGraphic.setImageHeight(80);
        backupShrineDoorGraphic.setAnimation(0, 0, 1, 0);
        backupShrineDoorGraphic.setCollision(HitTile.Air);
        backupShrineDoorGraphic.setRGBAMax(0, 0, 0, 255);
        backupShrineDoorGraphic.setArg23(1);

        screen.getObjects().add(backupShrineDoorGraphic);
    }

    /**
     * Add Skanda door cover to the screen that left-transitions into west Chamber of Birth
     * @param transitionGate the gate to cover with the graphic
     */
    public static void addSkandaBlock(GameObject transitionGate) {
        GraphicsTextureDraw skandaBlockGraphic = new GraphicsTextureDraw(transitionGate.getObjectContainer(), transitionGate.getX(), transitionGate.getY() - 20);

        skandaBlockGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.SKANDA_STATE, ByteOp.FLAG_LTEQ, 1));

        skandaBlockGraphic.setLayer(19);
        skandaBlockGraphic.setImageFile("01effect.png");
        skandaBlockGraphic.setImageX(640);
        skandaBlockGraphic.setImageY(512);
        skandaBlockGraphic.setImageWidth(40);
        skandaBlockGraphic.setImageHeight(60);
        skandaBlockGraphic.setAnimation(0, 1, 4, 1);
        skandaBlockGraphic.setCollision(HitTile.Solid);
        skandaBlockGraphic.setRGBAMax(0, 0, 0, 255);
        skandaBlockGraphic.setArg23(1);

        transitionGate.getObjectContainer().getObjects().add(skandaBlockGraphic);
    }

    /**
     * Add Illusion door cover to the screen that left-transitions into upper Gate of Illusion
     * @param transitionGate the gate to cover with the graphic
     */
    public static void addIllusionFruitBlockHorizontal(GameObject transitionGate, boolean useRuinGraphic) {
        GraphicsTextureDraw fruitBlockGraphic = new GraphicsTextureDraw(transitionGate.getObjectContainer(), transitionGate.getX(), transitionGate.getY() - 40);

        fruitBlockGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.EDEN_UNLOCKED, ByteOp.FLAG_EQUALS, 0));

        fruitBlockGraphic.setLayer(19);
        fruitBlockGraphic.setImageFile("01effect.png");
        fruitBlockGraphic.setImageX(useRuinGraphic ? 620 : 540);
        fruitBlockGraphic.setImageY(512);
        fruitBlockGraphic.setImageWidth(20);
        fruitBlockGraphic.setImageHeight(80);
        fruitBlockGraphic.setAnimation(0, 1, 4, 1);
        fruitBlockGraphic.setCollision(HitTile.Solid);
        fruitBlockGraphic.setEntryEffect(GraphicsTextureDraw.EntryEffect_FadeIn);
        fruitBlockGraphic.setRGBAMax(0, 0, 0, 255);
        fruitBlockGraphic.setArg23(1);

        transitionGate.getObjectContainer().getObjects().add(fruitBlockGraphic);
    }

    /**
     * Add Illusion door cover to the screen that up-transitions into lower Gate of Illusion
     * @param transitionGate the gate to cover with the graphic
     */
    public static void addIllusionFruitBlockVertical(GameObject transitionGate) {
        GraphicsTextureDraw fruitBlockGraphic = new GraphicsTextureDraw(transitionGate.getObjectContainer(),
                transitionGate.getX() - 20, transitionGate.getY());

        fruitBlockGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.EDEN_UNLOCKED, ByteOp.FLAG_EQUALS, 0));

        fruitBlockGraphic.setLayer(19);
        fruitBlockGraphic.setImageFile("01effect.png");
        fruitBlockGraphic.setImageX(400);
        fruitBlockGraphic.setImageY(512);
        fruitBlockGraphic.setImageWidth(80);
        fruitBlockGraphic.setImageHeight(80);
        fruitBlockGraphic.setAnimation(0, 1, 4, 1);
        fruitBlockGraphic.setCollision(HitTile.Solid); // Hittile to fill with
        fruitBlockGraphic.setExitEffect(GraphicsTextureDraw.ExitEffect_FadeOut);
        fruitBlockGraphic.setRGBAMax(0, 0, 0, 255);
        fruitBlockGraphic.setArg23(1);

        transitionGate.getObjectContainer().getObjects().add(fruitBlockGraphic);
    }

    /**
     * Add a copy of a directional wall hitbox that's hittable from a different direction.
     * @param originalWall the wall to make a copy of
     */
    public static void addWallCopy(GameObject originalWall, int wallDirection) {
        GameObject wallCopy = new GameObject(originalWall);
        wallCopy.getArgs().set(3, (short)wallDirection);
        originalWall.getObjectContainer().getObjects().add(wallCopy);
    }

    /**
     * Add a timer to automatically start hard mode.
     * @param screen the screen to add the objects to
     */
    public static void addAutomaticHardmodeTimer(Screen screen) {
        FlagTimer automaticHardmodeTimer = new FlagTimer(screen);

        automaticHardmodeTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.HARDMODE, ByteOp.FLAG_NOT_EQUAL, 2));

        automaticHardmodeTimer.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.HARDMODE, ByteOp.ASSIGN_FLAG, 2));

        screen.getObjects().add(0, automaticHardmodeTimer);
    }

    /**
     * Add a timer to automatically learn ancient La-Mulanese.
     * @param screen the screen to add the objects to
     */
    public static void addAutomaticTranslationsTimer(Screen screen) {
        FlagTimer automaticTranslationTimer = new FlagTimer(screen);

        automaticTranslationTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.ANCIENT_LAMULANESE_LEARNED, ByteOp.FLAG_EQUALS, 0));

        automaticTranslationTimer.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.TRANSLATION_TABLETS_READ, ByteOp.ASSIGN_FLAG, 3));
        automaticTranslationTimer.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.ANCIENT_LAMULANESE_LEARNED, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(0, automaticTranslationTimer);
    }

    public static void addAutomaticMantrasTimer(ObjectContainer screen) {
        FlagTimer mantraTimer = new FlagTimer(screen);

        mantraTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.MANTRA_FINAL, ByteOp.FLAG_NOT_EQUAL, 4));
        mantraTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_KEY_SWORD, ByteOp.FLAG_GTEQ, 2));

        WriteByteOperation writeByteOperation = new WriteByteOperation(FlagConstants.MANTRA_FINAL, ByteOp.ASSIGN_FLAG, 4);
        mantraTimer.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, mantraTimer);
    }

    /**
     * Add instant item give to the starting location so items will be given at the start of the game.
     * @param screen the screen to add the objects to
     */
    public static void addStartingItems(ObjectContainer screen) {
        for(String itemName : Settings.getStartingItemsIncludingCustom()) {
            GameObjectId gameObjectId = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(itemName);

            GameObject itemGive = new GameObject(screen);
            itemGive.setId((short) 0xb5);
            itemGive.getArgs().add(gameObjectId.getInventoryArg());
            itemGive.getArgs().add((short)2);
            itemGive.getArgs().add((short)3);
            itemGive.getArgs().add((short)39);
            itemGive.setX(LocationCoordinateMapper.getStartingX());
            itemGive.setY(LocationCoordinateMapper.getStartingY());

            itemGive.getTestByteOperations().add(new TestByteOperation(gameObjectId.getWorldFlag(), ByteOp.FLAG_EQUALS, 0));
            itemGive.getWriteByteOperations().add(new WriteByteOperation(gameObjectId.getWorldFlag(), ByteOp.ASSIGN_FLAG, 2));
            screen.getObjects().add(itemGive);
        }
        // todo: write tests and then switch from the above code to the commented-out code below
//        for(String itemName : Settings.getStartingItemsIncludingCustom()) {
//            GameObjectId gameObjectId = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(itemName);
//
//            ItemGive itemGive = new ItemGive(screen, LocationCoordinateMapper.getStartingX(), LocationCoordinateMapper.getStartingY());
//            itemGive.setInventoryWord(gameObjectId.getInventoryArg());
//            itemGive.setWidth(2);
//            itemGive.setHeight(3);
//            itemGive.setSoundEffect(39);
//
//            itemGive.getTestByteOperations().add(new TestByteOperation(gameObjectId.getWorldFlag(), ByteOp.FLAG_EQUALS, 0));
//
//            itemGive.getWriteByteOperations().add(new WriteByteOperation(gameObjectId.getWorldFlag(), ByteOp.ASSIGN_FLAG, 2));
//            screen.getObjects().add(itemGive);
//        }
    }

    public static void addTransformedMrFishmanShopDoorGraphic(ObjectContainer objectContainer) {
        GraphicsTextureDraw transformedMrFishmanShopDoorGraphic = new GraphicsTextureDraw(objectContainer, 180, 1520);
        transformedMrFishmanShopDoorGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.FISH_SHOP_UNLOCKS, ByteOp.FLAG_EQUALS, 3));

        transformedMrFishmanShopDoorGraphic.setLayer(-1);
        transformedMrFishmanShopDoorGraphic.setImageFile("map*_1.png");
        transformedMrFishmanShopDoorGraphic.setImageX(260);
        transformedMrFishmanShopDoorGraphic.setImageY(0);
        transformedMrFishmanShopDoorGraphic.setImageWidth(40);
        transformedMrFishmanShopDoorGraphic.setImageHeight(40);
        transformedMrFishmanShopDoorGraphic.setAnimation(0, 1, 0, 0);
        transformedMrFishmanShopDoorGraphic.setCollision(HitTile.Air);
        transformedMrFishmanShopDoorGraphic.setRGBAMax(0, 0, 0, 255);
//        transformedMrFishmanShopDoorGraphic.setArg23(1); // todo: for some reason this was 0?

        objectContainer.getObjects().add(transformedMrFishmanShopDoorGraphic);
    }

    /**
     * Actually-used modification for backside door numbers.
     * @param backsideDoor the object we're decorating with graphics
     * @param bossNumber 1=Amphisbaena, 7=Baphomet, etc.
     */
    public static void addBossNumberGraphic(GameObject backsideDoor, int bossNumber, int mirrorCoverFlag) {
        GraphicsTextureDraw doorNumberGraphic = new GraphicsTextureDraw(backsideDoor.getObjectContainer(), backsideDoor.getX(), backsideDoor.getY() - 40);
        doorNumberGraphic.setLayer(-1);
        doorNumberGraphic.setImageFile("01effect.png");
        doorNumberGraphic.setImageX(53 +  16 * (bossNumber - 1));
        doorNumberGraphic.setImageY(209);
        doorNumberGraphic.setImageWidth(29);
        doorNumberGraphic.setImageHeight(31);
        doorNumberGraphic.setAnimation(0, 0, 1, 0);
        doorNumberGraphic.setCollision(HitTile.Air);
        doorNumberGraphic.setRGBAMax(0, 0, 0, 255);
//        doorNumberGraphic.setArg23(1); // todo: was 0

        doorNumberGraphic.getTestByteOperations().add(new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_GT, 0));

        backsideDoor.getObjectContainer().getObjects().add(doorNumberGraphic);

        GraphicsTextureDraw doorCoverup = new GraphicsTextureDraw(backsideDoor.getObjectContainer(), backsideDoor.getX(), backsideDoor.getY() - 40);
        doorCoverup.setLayer(0);
        doorCoverup.setImageFile("map*_1.png");
        doorCoverup.setImageX(940);
        doorCoverup.setImageY(0);
        doorCoverup.setImageWidth(11);
        doorCoverup.setImageHeight(33);
        doorCoverup.setAnimation(0, 0, 1, 0);
        doorCoverup.setCollision(HitTile.Air);
        doorCoverup.setRGBAMax(0, 0, 0, 255);
//        doorCoverup.setArg23(1); // todo: was 0

        doorCoverup.getTestByteOperations().add(new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_GT, 0));

        backsideDoor.getObjectContainer().getObjects().add(doorCoverup);

        doorCoverup = new GraphicsTextureDraw(backsideDoor.getObjectContainer(), backsideDoor.getX(), backsideDoor.getY() - 40);
        doorCoverup.setLayer(0);
        doorCoverup.setImageFile("map*_1.png");
        doorCoverup.setImageX(940);
        doorCoverup.setImageY(0);
        doorCoverup.setImageWidth(29);
        doorCoverup.setImageHeight(11);
        doorCoverup.setAnimation(0, 0, 1, 0);
        doorCoverup.setCollision(HitTile.Air);
        doorCoverup.setRGBAMax(0, 0, 0, 255);
//        doorCoverup.setArg23(1); // todo: was 0

        doorCoverup.getTestByteOperations().add(new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_GT, 0));

        backsideDoor.getObjectContainer().getObjects().add(doorCoverup);
    }

    /**
     * Actually-used modification for backside door numbers.
     * @param backsideDoor the object we're decorating with graphics
     * @param bossNumber 1=Amphisbaena, 7=Baphomet, etc.
     */
    public static void addBossNumberGraphicV2(GameObject backsideDoor, int bossNumber, Integer mirrorCoverFlag) {
        GraphicsTextureDraw doorNumberGraphic = new GraphicsTextureDraw(backsideDoor.getObjectContainer(), backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        doorNumberGraphic.setLayer(0);
        doorNumberGraphic.setImageFile("01effect.png");
        if(bossNumber == 9) {
            doorNumberGraphic.setImageX(50 * 9);
        }
        else {
            doorNumberGraphic.setImageX(50 * (bossNumber - 1));
        }
        doorNumberGraphic.setImageY(592);
        doorNumberGraphic.setImageWidth(50);
        doorNumberGraphic.setImageHeight(36);
        doorNumberGraphic.setAnimation(0, 0, 1, 0);
        doorNumberGraphic.setCollision(HitTile.Air);
        doorNumberGraphic.setRGBAMax(0, 0, 0, 255);
//        doorNumberGraphic.setArg23(1); // todo: was 0

        if(mirrorCoverFlag != null) {
            doorNumberGraphic.getTestByteOperations().add(new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_GT, 0));
        }

        backsideDoor.getObjectContainer().getObjects().add(doorNumberGraphic);
    }

    public static void addNumberlessBacksideDoorGraphic(GameObject backsideDoor) {
        int zoneIndex = ((Screen)backsideDoor.getObjectContainer()).getZoneIndex();
        if(zoneIndex == 19) {
            return;
        }

        GraphicsTextureDraw doorCoverGraphic = new GraphicsTextureDraw(backsideDoor.getObjectContainer(), backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        doorCoverGraphic.setLayer(-1);
        doorCoverGraphic.setImageFile("01effect.png");
        if(zoneIndex == 6) {
            doorCoverGraphic.setImageX(80);
        }
        else if(zoneIndex == 10) {
            doorCoverGraphic.setImageX(160);
        }
        else if(zoneIndex == 13) {
            doorCoverGraphic.setImageX(240);
        }
        else {
            doorCoverGraphic.setImageX(0);
        }

        doorCoverGraphic.setImageY(512);
        doorCoverGraphic.setImageWidth(80);
        doorCoverGraphic.setImageHeight(80);
        doorCoverGraphic.setAnimation(0, 0, 1, 0);
        doorCoverGraphic.setCollision(HitTile.Air);
        doorCoverGraphic.setRGBAMax(0, 0, 0, 255);
        doorCoverGraphic.setArg23(1);

        backsideDoor.getObjectContainer().getObjects().add(doorCoverGraphic);
    }

    public static GameObject addAnimatedDoorCover(GameObject backsideDoor, int gateFlag, boolean checkBronzeMirror) {
        GraphicsTextureDraw doorCoverGraphic = new GraphicsTextureDraw(backsideDoor.getObjectContainer(), backsideDoor.getX(), backsideDoor.getY());

        doorCoverGraphic.setLayer(0);
        doorCoverGraphic.setImageFile("01effect.png");
        doorCoverGraphic.setImageX(0);
        doorCoverGraphic.setImageY(360);
        doorCoverGraphic.setImageWidth(40);
        doorCoverGraphic.setImageHeight(40);
        doorCoverGraphic.setAnimation(1, 7, 6, 1);
        doorCoverGraphic.setCollision(HitTile.Air);
        doorCoverGraphic.setExitEffect(GraphicsTextureDraw.ExitEffect_Animate);
        doorCoverGraphic.setRGBAMax(0, 0, 0, 255);
        doorCoverGraphic.setArg23(1);

        doorCoverGraphic.getTestByteOperations().add(new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0));

        if(checkBronzeMirror) {
            doorCoverGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_BRONZE_MIRROR, ByteOp.FLAG_EQUALS, 2));
        }

        doorCoverGraphic.getWriteByteOperations().add(new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1));

        backsideDoor.getObjectContainer().getObjects().add(doorCoverGraphic);
        return doorCoverGraphic;
    }

    /**
     * Add fairy point to reveal backside door
     * @param backsideDoor to add key fairy point for
     */
    public static void addBacksideDoorKeyFairyPoint(GameObject backsideDoor) {
        KeyFairySpot obj = new KeyFairySpot(backsideDoor.getObjectContainer(), backsideDoor.getX(), backsideDoor.getY() - 40);
        obj.setWidth(40);
        obj.setHeight(40);

        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.KEY_FAIRY_DOOR_UNLOCKED, ByteOp.FLAG_EQUALS, 0));

        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.KEY_FAIRY_DOOR_UNLOCKED, ByteOp.ASSIGN_FLAG, 1));
        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_29, ByteOp.ASSIGN_FLAG, 1));
        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.KEY_FAIRY_DOOR_UNLOCKED_V2, ByteOp.ASSIGN_FLAG, 1));

        backsideDoor.getObjectContainer().getObjects().add(obj);
    }

    public static void addMirrorCoverTimerAndSound(ObjectContainer objectContainer, int mirrorCoverFlag) {
        FlagTimer mirrorTimer = new FlagTimer(objectContainer);
        mirrorTimer.setDelayFrames(30);

        mirrorTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_BRONZE_MIRROR, ByteOp.FLAG_EQUALS, 2));
        mirrorTimer.getTestByteOperations().add(new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0));

        mirrorTimer.getWriteByteOperations().add(new WriteByteOperation(mirrorCoverFlag, ByteOp.ASSIGN_FLAG, 1));
        mirrorTimer.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_28, ByteOp.ASSIGN_FLAG, 1));

        int zoneIndex = ((Screen)objectContainer).getZoneIndex();
        int roomIndex = ((Screen)objectContainer).getRoomIndex();
        int soundBalance = getMirrorCoverSoundBalance(zoneIndex, roomIndex);

        SoundEffect mirrorSoundEffect = new SoundEffect(objectContainer);
        mirrorSoundEffect.setSoundEffect(48);
        mirrorSoundEffect.setVolume(127, 127, 10);
        mirrorSoundEffect.setBalance(soundBalance, soundBalance, 0);
        mirrorSoundEffect.setPitch(-700, -700, 0);
        mirrorSoundEffect.setPriority(20);
//        mirrorSoundEffect.setArg8(0);
//        mirrorSoundEffect.setFramesDelay(0);
//        mirrorSoundEffect.setControllerRumble(false);
        mirrorSoundEffect.setRumbleStrength(10);

        mirrorSoundEffect.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_28, ByteOp.FLAG_EQUALS, 1));

        objectContainer.getObjects().add(0, mirrorSoundEffect);
        objectContainer.getObjects().add(0, mirrorTimer);
    }

    public static GameObject addAnimatedDoorTimerAndSound(ObjectContainer objectContainer, Integer bossFlag, int gateFlag) {
        return addAnimatedDoorTimerAndSound(objectContainer, new TestByteOperation(bossFlag, ByteOp.FLAG_GTEQ, 3), gateFlag, true);
    }

    public static GameObject addAnimatedDoorTimerAndSound(ObjectContainer objectContainer, TestByteOperation gateCondition, int gateFlag, boolean bronzeMirrorRequired) {
        FlagTimer doorTimer = new FlagTimer(objectContainer);
        doorTimer.setDelaySeconds(1);

        doorTimer.getTestByteOperations().add(gateCondition);
        if(bronzeMirrorRequired) {
            doorTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_BRONZE_MIRROR, ByteOp.FLAG_EQUALS, 2));
        }
        doorTimer.getTestByteOperations().add(new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0));

        doorTimer.getWriteByteOperations().add(new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1));
        doorTimer.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_29, ByteOp.ASSIGN_FLAG, 1));

        int zoneIndex = ((Screen)objectContainer).getZoneIndex();
        int roomIndex = ((Screen)objectContainer).getRoomIndex();
        int soundBalance = getAnimatedDoorSoundBalance(zoneIndex, roomIndex);

        SoundEffect doorSoundEffect = new SoundEffect(objectContainer);
        doorSoundEffect.setSoundEffect(95);
        doorSoundEffect.setVolume(127, 127, 0);
        doorSoundEffect.setBalance(soundBalance, soundBalance, 0);
        doorSoundEffect.setPitch(-400, 0, 0);
        doorSoundEffect.setPriority(15);
//        doorSoundEffect.setArg8(0);
//        doorSoundEffect.setFramesDelay(0);
//        doorSoundEffect.setControllerRumble(false);
        doorSoundEffect.setRumbleStrength(10);

        doorSoundEffect.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_29, ByteOp.FLAG_EQUALS, 1));

        objectContainer.getObjects().add(0, doorSoundEffect);
        objectContainer.getObjects().add(0, doorTimer);
        return doorTimer;
    }

    public static GameObject addAnimatedDoorSound(ObjectContainer objectContainer) {
        int zoneIndex = ((Screen)objectContainer).getZoneIndex();
        int roomIndex = ((Screen)objectContainer).getRoomIndex();
        int soundBalance = getAnimatedDoorSoundBalance(zoneIndex, roomIndex);

        SoundEffect doorSoundEffect = new SoundEffect(objectContainer);
        doorSoundEffect.setSoundEffect(95);
        doorSoundEffect.setVolume(127, 127, 0);
        doorSoundEffect.setBalance(soundBalance, soundBalance, 0);
        doorSoundEffect.setPitch(-400, 0, 0);
        doorSoundEffect.setPriority(15);
//        doorSoundEffect.setArg8(0);
//        doorSoundEffect.setFramesDelay(0);
//        doorSoundEffect.setControllerRumble(false);
        doorSoundEffect.setRumbleStrength(10);

        doorSoundEffect.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_29, ByteOp.FLAG_EQUALS, 1));

        objectContainer.getObjects().add(0, doorSoundEffect);
        return doorSoundEffect;
    }

    public static void addKeyFairyDoorTimerAndSounds(ObjectContainer objectContainer) {
        // todo: fix and test this?
        GameObject keyFairyTimer = new GameObject(objectContainer);
        keyFairyTimer.setX(-1);
        keyFairyTimer.setY(-1);
        keyFairyTimer.getArgs().add((short)0);
        keyFairyTimer.getArgs().add((short)0);

        TestByteOperation keyFairyTimerTest = new TestByteOperation();
        keyFairyTimerTest.setIndex(FlagConstants.KEY_FAIRY_DOOR_UNLOCKED_V2);
        keyFairyTimerTest.setValue((byte)1);
        keyFairyTimerTest.setOp(ByteOp.FLAG_EQUALS);
        keyFairyTimer.getTestByteOperations().add(keyFairyTimerTest);

        WriteByteOperation keyFairyTimerUpdate = new WriteByteOperation();
        keyFairyTimerUpdate.setIndex(FlagConstants.KEY_FAIRY_DOOR_UNLOCKED_V2);
        keyFairyTimerUpdate.setValue((byte)2);
        keyFairyTimerUpdate.setOp(ByteOp.ASSIGN_FLAG);
        keyFairyTimer.getWriteByteOperations().add(keyFairyTimerUpdate);

        keyFairyTimerUpdate = new WriteByteOperation();
        keyFairyTimerUpdate.setIndex(FlagConstants.KEY_FAIRY_POINTS);
        keyFairyTimerUpdate.setValue((byte)1);
        keyFairyTimerUpdate.setOp(ByteOp.ADD_FLAG);
        keyFairyTimer.getWriteByteOperations().add(keyFairyTimerUpdate);
        // todo: fix and test this?

        int zoneIndex = ((Screen)objectContainer).getZoneIndex();
        int roomIndex = ((Screen)objectContainer).getRoomIndex();
        int soundBalance = getAnimatedDoorSoundBalance(zoneIndex, roomIndex);

        SoundEffect animatedDoorSound = new SoundEffect(objectContainer);
        animatedDoorSound.setSoundEffect(95);
        animatedDoorSound.setVolume(127, 127, 0);
        animatedDoorSound.setBalance(soundBalance, soundBalance, 0);
        animatedDoorSound.setPitch(400, 0, 0);
        animatedDoorSound.setPriority(15);
//        animatedDoorSound.setArg8(0);
//        animatedDoorSound.setFramesDelay(0);
//        animatedDoorSound.setControllerRumble(false);
        animatedDoorSound.setRumbleStrength(10);

        animatedDoorSound.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_29, ByteOp.FLAG_EQUALS, 1));

        objectContainer.getObjects().add(0, animatedDoorSound);

        addSuccessSound(objectContainer, Arrays.asList(
                new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2),
                new TestByteOperation(FlagConstants.KEY_FAIRY_DOOR_UNLOCKED, ByteOp.FLAG_EQUALS, 1),
                new TestByteOperation(FlagConstants.SCREEN_FLAG_29, ByteOp.FLAG_EQUALS, 1)));
    }

    private static short getMirrorCoverSoundBalance(int zoneIndex, int roomIndex) {
        if(zoneIndex == 0 || zoneIndex == 1) {
            return (short)40;
        }
        else if(zoneIndex == 5 && roomIndex == 8) {
            return (short)30;
        }
        return (short)64; // zoneIndex == 2, zoneIndex == 3, zoneIndex == 10, zoneIndex == 11, zoneIndex == 12, zoneIndex == 13, zoneIndex == 15, and all doors that don't have it?
    }

    private static short getAnimatedDoorSoundBalance(int zoneIndex, int roomIndex) {
        if(zoneIndex == 0 || zoneIndex == 1 || zoneIndex == 12) {
            return (short)40;
        }
        else if(zoneIndex == 2) {
            return (short)20;
        }
        else if(zoneIndex == 3) {
            return (short)100;
        }
        else if(zoneIndex == 5) {
            if(roomIndex == 8) {
                return (short)30;
            }
            else {
                return (short)10;
            }
        }
        else if(zoneIndex == 10) {
            return (short)10;
        }
        else if(zoneIndex == 11) {
            return (short)20;
        }
        return (short)64; // zoneIndex == 13, zoneIndex == 15, and all doors that don't have it?
    }

    public static void addMirrorCoverGraphic(GameObject backsideDoor, int mirrorCoverFlag) {
        int zoneIndex = ((Screen)backsideDoor.getObjectContainer()).getZoneIndex();

        if(zoneIndex == 0) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)600, (short)160, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)680, (short)160, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY());
        }
        else if(zoneIndex == 1) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)900, (short)60, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 2) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)0, (short)280, (short)580, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)0, (short)360, (short)580, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY());
        }
        else if(zoneIndex == 3) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)700, (short)80, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 5) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)940, (short)0, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 6) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)940, (short)120, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 10) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)620, (short)0, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 11) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)940, (short)120, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 12) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)540, (short)80, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 13) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)940, (short)220, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 14) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)540, (short)0, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 15) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)340, (short)0, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)1, (short)460, (short)60, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY());
        }
        else if(zoneIndex == 19) {
            addMirrorCoverGraphic(backsideDoor.getObjectContainer(), mirrorCoverFlag, (short)0, (short)-1, (short)760, (short)512, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
    }

    public static void addMirrorCoverGraphic(ObjectContainer objectContainer, Integer mirrorCoverFlag, short layer, short fileEnum, short imageX, short imageY, short width, short height, int xPos, int yPos) {
        GraphicsTextureDraw mirrorCoverGraphic = new GraphicsTextureDraw(objectContainer, xPos, yPos);
        mirrorCoverGraphic.setLayer(layer);
        mirrorCoverGraphic.setImageFile(fileEnum);
        mirrorCoverGraphic.setImageX(imageX);
        mirrorCoverGraphic.setImageY(imageY);
        mirrorCoverGraphic.setImageWidth(width);
        mirrorCoverGraphic.setImageHeight(height);
        mirrorCoverGraphic.setAnimation(1, 1, 4, 0);
        mirrorCoverGraphic.setCollision(HitTile.Air);
        mirrorCoverGraphic.setExitEffect(GraphicsTextureDraw.ExitEffect_FadeOut);
        mirrorCoverGraphic.setRGBAMax(0, 0, 0, 255);
        mirrorCoverGraphic.setArg23(1);

        if(mirrorCoverFlag != null) {
            mirrorCoverGraphic.addTests(new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0));
        }

        objectContainer.getObjects().add(mirrorCoverGraphic);
    }

    public static void addGrailToggle(ObjectContainer objectContainer, boolean enableGrail, TestByteOperation... tests) {
        GrailToggle grailToggle = new GrailToggle(objectContainer, enableGrail);

        // Disable during escape
        grailToggle.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0));
        for(TestByteOperation test : tests) {
            grailToggle.getTestByteOperations().add(test);
        }

        objectContainer.getObjects().add(0, grailToggle);
    }

    public static GameObject addAltSurfaceShopItemTimer(ObjectContainer objectContainer) {
        // Handles the case where the shop item can be obtained somewhere else and you already have it.
        // Without this timer, the shop could potentially be unable to transform back to its original state.
        return addFramesTimer(objectContainer, 0,
                Arrays.asList(new TestByteOperation(FlagConstants.WF_MSX2, ByteOp.FLAG_EQUALS, 1)),
                Arrays.asList(new WriteByteOperation(FlagConstants.WF_MSX2, ByteOp.ASSIGN_FLAG, 2)));
    }

    public static void addLittleBrotherShopTimer(ObjectContainer objectContainer, short shopItemFlag) {
        // Sets item world flags from 1 to 2, since one of the shop flags is taken up by checking the Big Brother shop trigger.
        addFramesTimer(objectContainer, 0,
                Arrays.asList(new TestByteOperation(shopItemFlag, ByteOp.FLAG_EQUALS, 1)),
                Arrays.asList(new WriteByteOperation(shopItemFlag, ByteOp.ASSIGN_FLAG, 2)));
    }

    /**
     * Detect Lemeza entering the lower surface area and get rid of the cover so you can see.
     * @param screen
     */
    public static void addSurfaceCoverDetector(ObjectContainer screen) {
        addLemezaDetector(screen, 420, 1340, 3, 5,
                Arrays.asList(new TestByteOperation(FlagConstants.SURFACE_UNDERPATH_VISIBLE, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(new WriteByteOperation(FlagConstants.SURFACE_UNDERPATH_VISIBLE, ByteOp.ASSIGN_FLAG, (byte)1)));
    }

    /**
     * Cheats for easy testing. Gives Shrine of the Mother frontside grail via a timer on the starting screen.
     * Other warps could be added for convenience as needed (0x064 = guidance, through 0x075 = backside shrine)
     * @param objectContainer screen to add the timers to
     */
    public static void addGrailWarpTimers(ObjectContainer objectContainer) {
        addFramesTimer(objectContainer, 0,
                Arrays.asList(new TestByteOperation(FlagConstants.TABLET_GRAIL_SHRINE_FRONT, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(new WriteByteOperation(FlagConstants.TABLET_GRAIL_SHRINE_FRONT, ByteOp.ASSIGN_FLAG, 1)));
    }

    /**
     * Adds sound effect for removed/trap items
     * @param objectContainer to add the sound effect to
     * @param newWorldFlag flag to update once the sound has been played, so it will only play once
     * @param screenFlag screen flag (non-permanent) to indicate that the sound should be played
     */
    public static void addNoItemSoundEffect(ObjectContainer objectContainer, Short newWorldFlag, Integer screenFlag) {
        SoundEffect noItemSoundEffect = new SoundEffect(objectContainer);
        noItemSoundEffect.setSoundEffect(SoundEffect.ShellHornFailure);
        noItemSoundEffect.setVolumeBalancePitch(120, 64, 0);
        noItemSoundEffect.setPriority(25);
        noItemSoundEffect.setArg8(1);
        noItemSoundEffect.setFramesDelay(5);
//        noItemSoundEffect.setControllerRumble(false);
        noItemSoundEffect.setRumbleStrength(10);

        noItemSoundEffect.addTests(
                new TestByteOperation(newWorldFlag, ByteOp.FLAG_GT, 0),
                new TestByteOperation(screenFlag, ByteOp.FLAG_EQUALS, 1));

        objectContainer.getObjects().add(0, noItemSoundEffect);
    }

    /**
     * Adds sound effect for curse
     * @param chest reference for effect
     * @param newWorldFlag flag to update once the sound has been played, so it will only play once
     * @param screenFlag screen flag (non-permanent) to indicate that the sound should be played
     */
    public static void addCurseEffect(Chest chest, Short newWorldFlag, Integer screenFlag) {
        SoundEffect curseSoundEffect = new SoundEffect(chest.getObjectContainer());
        curseSoundEffect.setSoundEffect(SoundEffect.HardmodeActivated);
        curseSoundEffect.setVolumeBalancePitch(127, 64, 0);
        curseSoundEffect.setPriority(15);
        curseSoundEffect.setArg8(0);
        curseSoundEffect.setFramesDelay(5); // 5 / 35 / 65
//        curseSoundEffect.setControllerRumble(false);
        curseSoundEffect.setRumbleStrength(10);

        curseSoundEffect.addTests(
                new TestByteOperation(newWorldFlag, ByteOp.FLAG_GT, 0),
                new TestByteOperation(screenFlag, ByteOp.FLAG_EQUALS, 1));

        chest.getObjectContainer().getObjects().add(0, curseSoundEffect);

        GraphicsTextureDraw curseGraphic = new GraphicsTextureDraw(chest.getObjectContainer(), 640 * (chest.getX() / 640) + 260, 480 * (chest.getY() / 480) + 80);

        curseGraphic.setLayer(10); // todo: fix
        curseGraphic.setImageFile("01effect.png");
        curseGraphic.setImageX(0);
        curseGraphic.setImageY(712);
        curseGraphic.setImageWidth(120);
        curseGraphic.setImageHeight(180);
        curseGraphic.setAnimation(0, 1, 0, 0);
        curseGraphic.setEntryEffect(GraphicsTextureDraw.EntryEffect_FadeIn);
        curseGraphic.setExitEffect(GraphicsTextureDraw.ExitEffect_BreakGlass);
        curseGraphic.setCollision(HitTile.Air);
        curseGraphic.setRGBAMax(0, 0, 0, 255);
        curseGraphic.setBlendMode(GraphicsTextureDraw.BlendMode_Add);
        curseGraphic.setArg23(1);
        curseGraphic.addTests(
                new TestByteOperation(newWorldFlag, ByteOp.FLAG_GT, 0),
                new TestByteOperation(screenFlag, ByteOp.FLAG_EQUALS, 1));
        chest.getObjectContainer().getObjects().add(curseGraphic);
    }

    /**
     * Add graphics for the given Spikes object
     * @param spikes object to add graphics for
     * @param spikeDirection whether the spikes should face up, down, left or right - value from Spikes constant
     * @return GraphicsTextureDraw for spikes
     */
    public static GraphicsTextureDraw addSpikesGraphic(Spikes spikes, int spikeDirection) {
        GraphicsTextureDraw spikesGraphic = new GraphicsTextureDraw(spikes.getObjectContainer(), spikes.getX(), spikes.getY());
        spikesGraphic.addTests(spikes.getTestByteOperations());

        spikesGraphic.setLayer(2);
        spikesGraphic.setImageFile("01effect.png");

        if(spikeDirection == Spikes.FACE_DOWN) {
            spikesGraphic.setImageX(160);
            spikesGraphic.setImageY(712);
        }
        else if(spikeDirection == Spikes.FACE_UP) {
            spikesGraphic.setImageX(160);
            spikesGraphic.setImageY(752);
        }
        else if(spikeDirection == Spikes.FACE_LEFT) {
            spikesGraphic.setImageX(360);
            spikesGraphic.setImageY(712);
        }
        else if(spikeDirection == Spikes.FACE_RIGHT) {
            spikesGraphic.setImageX(120);
            spikesGraphic.setImageY(712);
        }

        spikesGraphic.setImageWidth(spikes.getWidth() * 20);
        spikesGraphic.setImageHeight(spikes.getHeight() * 20);
        spikesGraphic.setAnimation(0, 1, 0, 0);
        spikesGraphic.setEntryEffect(GraphicsTextureDraw.EntryEffect_FadeIn);
        spikesGraphic.setCollision(HitTile.Air);
        spikesGraphic.setRGBAMax(0, 0, 0, 255);
        spikesGraphic.setArg23(1);
        spikes.getObjectContainer().getObjects().add(spikesGraphic);
        return spikesGraphic;
    }

    public static Bat addBat(ObjectContainer objectContainer, int xPos, int yPos, int screenFlag) {
        Bat bat = addBat(objectContainer, xPos, yPos);
        bat.addTests(new TestByteOperation(screenFlag, ByteOp.FLAG_GTEQ, 1));
        return bat;
    }

    public static Bat addBat(ObjectContainer objectContainer, int xPos, int yPos) {
        Bat bat = new Bat(objectContainer, xPos, yPos);
        bat.setFrontsideBat(true);
        bat.setInitiallyFlying(true);
        bat.setDropType(1);
        bat.setArg2(2);
        bat.setDamage(3);
        objectContainer.getObjects().add(bat);
        return bat;
    }

    public static void addTrapItemBats(GameObject gameObject, short flagIndex) {
        int xPos = gameObject.getX();
        int yPos = gameObject.getY();
        addBat(gameObject.getObjectContainer(), xPos - 20, yPos, flagIndex);
        addBat(gameObject.getObjectContainer(), xPos + 20, yPos, flagIndex);
        addBat(gameObject.getObjectContainer(), xPos, yPos - 20, flagIndex);
        addBat(gameObject.getObjectContainer(), xPos, yPos + 20, flagIndex);
    }

    public static void addExplosion(ObjectContainer objectContainer, int xPos, int yPos, int explosionTriggerFlag, int damage, boolean percentDamage) {
        Explosion explosion = new Explosion(objectContainer, xPos - 80, yPos - 80);
        explosion.setSize(200);
        if (percentDamage) {
            explosion.setPercentDamage(damage);
        }
        else {
            explosion.setFlatDamage(damage);
        }
        explosion.addTests(new TestByteOperation(explosionTriggerFlag, ByteOp.FLAG_EQUALS, 1));
        explosion.addUpdates(new WriteByteOperation(explosionTriggerFlag, ByteOp.ASSIGN_FLAG, 2));
        objectContainer.getObjects().add(explosion);
    }

    public static void addFoolsExplosion(ObjectContainer objectContainer, int xPos, int yPos, int newWorldFlag) {
        addFullscreenExplosion(objectContainer, xPos, yPos, 60, newWorldFlag);
    }

    public static void addFullscreenExplosion(ObjectContainer objectContainer, int xPos, int yPos, int percentDamage, int newWorldFlag) {
        Explosion explosion = new Explosion(objectContainer, 640 * (xPos / 640), 480 * (yPos / 480));
        explosion.setSize(640);
        explosion.setPercentDamage(percentDamage);
        explosion.addTests(new TestByteOperation(newWorldFlag, ByteOp.FLAG_EQUALS, 1));
        explosion.addUpdates(new WriteByteOperation(newWorldFlag, ByteOp.ASSIGN_FLAG, 2));
        objectContainer.getObjects().add(explosion);
    }

    /**
     * Add a pot to a screen
     * @param screen the screen to add the objects to
     * @param graphic
     * @param dropType
     * @param dropQuantity
     * @param updates
     */
    public static void addPot(ObjectContainer screen, int x, int y, PotGraphic graphic, DropType dropType, int dropQuantity, List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        Pot addedPot = addPot(screen, x, y, graphic, dropType, dropQuantity);
        addedPot.getTestByteOperations().addAll(tests);
        addedPot.getWriteByteOperations().addAll(updates);
    }

    public static Pot addPot(ObjectContainer screen, int x, int y, PotGraphic graphic, DropType dropType, int dropQuantity) {
        Pot addedPot = new Pot(screen, x, y);
        addedPot.setDrops(dropType, dropQuantity);
        addedPot.setFlag(-1, 1);
        addedPot.setPotGraphic(graphic);
        addedPot.setSoundEffects(105, 35, 17);
        addedPot.setPitchShift(0);

        screen.getObjects().add(addedPot);
        return addedPot;
    }

    public static void addMoonlightFeatherlessPlatform(Screen screen) {
        GraphicsTextureDraw platform = new GraphicsTextureDraw(screen, 580, 200);

        platform.setLayer(0);
        platform.setImageFile("map*_1.png");
        platform.setImageX(0);
        platform.setImageY(80);
        platform.setImageWidth(40);
        platform.setImageHeight(20);
        platform.setAnimation(0, 1, 0, 0);
        platform.setCollision(HitTile.Solid);
        platform.setRGBAMax(0, 0, 0, 255);
        platform.setArg23(1);

        if (HolidaySettings.isFools2020Mode()) {
            platform.addTests(new TestByteOperation(FlagConstants.CUSTOM_WF_FAKE_FEATHER, ByteOp.FLAG_EQUALS, 2));
        }

        screen.getObjects().add(platform);
    }

    public static void addTwinPuzzleFeatherlessPlatform(Screen screen) {
        GraphicsTextureDraw platform = new GraphicsTextureDraw(screen, 900, 280);

        platform.setLayer(0);
        platform.setImageFile("map*_1.png");
        platform.setImageX(280);
        platform.setImageY(80);
        platform.setImageWidth(40);
        platform.setImageHeight(20);
        platform.setAnimation(0, 1, 0, 0);
        platform.setCollision(HitTile.Solid);
        platform.setRGBAMax(0, 0, 0, 255);
        platform.setArg23(1);

        screen.getObjects().add(platform);
    }

    public static void addTwinPuzzleBlockFix(Screen screen) {
        GraphicsTextureDraw platform = new GraphicsTextureDraw(screen, 1060, 440);

        platform.setLayer(0);
        platform.setImageFile("map*_1.png");
        platform.setImageX(280);
        platform.setImageY(80);
        platform.setImageWidth(40);
        platform.setImageHeight(20);
        platform.setAnimation(0, 1, 0, 0);
        platform.setCollision(HitTile.Solid);
        platform.setRGBAMax(0, 0, 0, 255);
        platform.setArg23(1);

        screen.getObjects().add(platform);
    }

    public static void addTrueShrineFeatherlessPlatform(Screen screen, int x, int y) {
        GraphicsTextureDraw platform = new GraphicsTextureDraw(screen, x, y);

        platform.setLayer(0);
        platform.setImageFile("map*_1.png");
        platform.setImageX(220);
        platform.setImageY(40);
        platform.setImageWidth(40);
        platform.setImageHeight(20);
        platform.setAnimation(0, 1, 0, 0);
        platform.setCollision(HitTile.Solid);
        platform.setRGBAMax(0, 0, 0, 255);
        platform.setArg23(1);

        screen.getObjects().add(platform);
    }

    public static void addInfernoFakeWeaponCover(Screen screen, List<TestByteOperation> tests) {
        GraphicsTextureDraw weaponCover = new GraphicsTextureDraw(screen, 20, 300);

        weaponCover.setLayer(0);
        weaponCover.setImageFile("eveg.png");
        weaponCover.setImageX(420);
        weaponCover.setImageY(0);
        weaponCover.setImageWidth(120);
        weaponCover.setImageHeight(60);
        weaponCover.setAnimation(0, 1, 0, 0);
        weaponCover.setCollision(HitTile.Solid);
        weaponCover.setExitEffect(GraphicsTextureDraw.ExitEffect_FadeOut);
        weaponCover.setRGBAMax(0, 0, 0, 255);
        weaponCover.setArg23(1);

        weaponCover.getTestByteOperations().addAll(tests);

        screen.getObjects().add(weaponCover);
    }

    public static void addLittleBrotherWeightWaster(Screen screen) {
        Dais weightWaster = new Dais(screen, 560, 1140);
//        weightWaster.setDustAppearance(0);
        weightWaster.setFallingTime(60);
        weightWaster.setRise(0);
        weightWaster.setImage(2);
//        weightWaster.setArg4(0);
        weightWaster.setImageX(860);
        weightWaster.setImageY(60);
        weightWaster.setFullWidth();
        weightWaster.setArg8(10);
        weightWaster.setRiseSpeed(60);

        weightWaster.addTests(
                new TestByteOperation(FlagConstants.BIG_BROTHER_UNLOCKED, ByteOp.FLAG_LT, 2),
                new TestByteOperation(FlagConstants.LITTLE_BROTHER_PURCHASES, ByteOp.FLAG_GT, 0));

        screen.getObjects().add(weightWaster);
    }

    /**
     * Add timer to auto-open the shortcut to lower Tower of the Goddess after flooding,
     * to be used only when starting with a subweapon (in case no main weapon is available) to prevent soflocks.
     * @param screen the screen to add the timers to
     */
    public static void addFloodedTowerShortcutTimer(ObjectContainer screen) {
        addFramesTimer(screen, 0,
                Arrays.asList(
                        new TestByteOperation(FlagConstants.SPRING_LEFT_HATCH, ByteOp.FLAG_EQUALS, 1), // Check that the tower has been flooded.
                        new TestByteOperation(FlagConstants.WF_FEATHER, ByteOp.FLAG_EQUALS, 2)), // Check that feather has been found, otherwise it's not possible to get down before flooding the tower anyway.
                Arrays.asList(new WriteByteOperation(FlagConstants.GODDESS_PIPES_SHORTCUT, ByteOp.ASSIGN_FLAG, 1)));
    }

    /**
     * Add timer to set wrong color medicine statue to match whatever's in the Vessel.
     * @param screen the screen to add the timers to
     */
    public static void addMedicineStatueTimer(ObjectContainer screen, int medicineWorldFlag) {
        addFramesTimer(screen, 0,
                Arrays.asList(
                        new TestByteOperation(medicineWorldFlag, ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(FlagConstants.MEDICINE_SOLVED, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(
                        new WriteByteOperation(FlagConstants.MEDICINE_SOLVED, ByteOp.ASSIGN_FLAG, 1)));
    }

    /**
     * Add weights to activate/deactivate hardmode without literacy.
     * @param screen the screen to add the timers to
     */
    public static void addHardmodeToggleWeights(ObjectContainer screen) {
        // Toggle off
        Dais dais = new Dais(screen, 560, 100);
//        dais.setDustAppearance(0);
        dais.setFallingTime(60);
        dais.setRise(-1);
        dais.setImage(2);
//        dais.setArg4(0);
        dais.setImageX(860);
        dais.setImageY(60);
        dais.setFullWidth();
        dais.setArg8(10);
        dais.setRiseSpeed(60);

        dais.addTests(new TestByteOperation(FlagConstants.HARDMODE, ByteOp.FLAG_EQUALS, 2));
        dais.addUpdates(new WriteByteOperation(FlagConstants.HARDMODE, ByteOp.ASSIGN_FLAG, 0));

        screen.getObjects().add(dais);

        // Toggle on
        dais = new Dais(screen, 560, 100);
//        dais.setDustAppearance(0);
        dais.setFallingTime(60);
        dais.setRise(-1);
        dais.setImage(2);
//        dais.setArg4(0);
        dais.setImageX(860);
        dais.setImageY(60);
        dais.setFullWidth();
        dais.setArg8(10);
        dais.setRiseSpeed(60);

        dais.addTests(new TestByteOperation(FlagConstants.HARDMODE, ByteOp.FLAG_LT, 2));
        dais.addUpdates(new WriteByteOperation(FlagConstants.HARDMODE, ByteOp.ASSIGN_FLAG, 2));

        screen.getObjects().add(dais);
    }

    public static boolean addSpecialItemObjects(String chestLocation, String chestContents) {
        if("Book of the Dead".equals(chestLocation)) {
            // Mulbruk location, but no object with flags to update.
            addSpecialItemObjects(mulbrukScreen, chestContents);
            return true;
        }
        return false;
    }

    public static void addSpecialItemObjects(ObjectContainer objectContainer, String newContents) {
        if ("Map (Shrine of the Mother)".equals(newContents)) {
            AddObject.addShrineMapSoundEffect(objectContainer);
        }
        if (newContents.startsWith("Medicine of the Mind") && !newContents.contains("Yellow")) {
            int medicineWorldFlag;
            if(newContents.contains("Green")) {
                medicineWorldFlag = FlagConstants.WF_MEDICINE_GREEN;
            }
            else if(newContents.contains("Red")) {
                medicineWorldFlag = FlagConstants.WF_MEDICINE_RED;
            }
            else {
                medicineWorldFlag = FlagConstants.WF_MEDICINE_YELLOW;
            }
            AddObject.addMedicineStatueTimer(objectContainer, medicineWorldFlag);
        }
        if(Settings.isAutomaticMantras() && "Key Sword".equals(newContents)) {
            AddObject.addAutomaticMantrasTimer(objectContainer);
        }
    }

    public static void addHotspring(GameObject reference) {
        HotSpring hotspring = new HotSpring(reference.getObjectContainer(), reference.getX() - 20, reference.getY() + 20);

        hotspring.setWidth(4);
        hotspring.setHeight(2);
        hotspring.setFramesOfNoHeal(8);
        hotspring.setHealAmount(2);

        reference.getObjectContainer().getObjects().add(hotspring);
    }

    public static void addSurfaceGrailTablet(Screen screen) {
        Scannable grailTablet = new Scannable(screen, 1120, 80);

        grailTablet.setTextBlock(38);
        grailTablet.setArg1(0);
        grailTablet.setArg2(0);
        grailTablet.setArg3(1);
        grailTablet.setArg4(1);
        grailTablet.setArg5(1);
        grailTablet.setArg6(1);

        grailTablet.setArg7(1);
        grailTablet.setArg8(1);
        grailTablet.setArg9(0);

        grailTablet.setDimensions(40, 40);

        int grailFlag = LocationCoordinateMapper.getGrailFlag(1, true);
        grailTablet.addTests(new TestByteOperation(grailFlag, ByteOp.FLAG_EQUALS, 0));
        grailTablet.addUpdates(new WriteByteOperation(grailFlag, ByteOp.ASSIGN_FLAG, 1) );

        screen.getObjects().add(grailTablet);

        if(Settings.isAutomaticGrailPoints()) {
            addGrailDetector(grailTablet, grailFlag);
        }
    }

    public static GameObject addRetroSurfaceGrailTablet(Screen screen) {
        Autosave grailTablet = new Autosave(screen, 400, 160); // todo: this looks pretty broken, needs testing. retro surface start?? no tests/writes either
        grailTablet.setTextBlock(38);

        screen.getObjects().add(grailTablet);

        GraphicsTextureDraw grailGraphic = new GraphicsTextureDraw(screen, grailTablet.getX(), grailTablet.getY());

        grailGraphic.setLayer(0);
        grailGraphic.setImageFile("map*_1.png");
        grailGraphic.setImageX(40);
        grailGraphic.setImageY(0);
        grailGraphic.setImageWidth(40);
        grailGraphic.setImageHeight(40);
        grailGraphic.setAnimation(0, 1, 0, 0);
        grailGraphic.setCollision(HitTile.Air);
        grailGraphic.setRGBAMax(0, 0, 0, 255);
        grailGraphic.setArg23(1);

        screen.getObjects().add(grailGraphic);

        SavePoint grailSave = new SavePoint(screen, grailTablet.getX(), grailTablet.getY());
        // todo: should add a test for not escaping

        screen.getObjects().add(grailSave);

        return grailTablet;
    }

    public static WarpPortal addWarp(Screen screen, int warpX, int warpY, int width, int height, int destZone, int destRoom, int destScreen, int destX, int destY) {
        WarpPortal warp = new WarpPortal(screen, warpX, warpY);
        warp.setDestination(destZone, destRoom, destScreen, destX, destY);
        warp.setDimensions(width, height);

        screen.getObjects().add(warp);

        return warp;
    }

    public static LemezaDetector addLemezaDetector(ObjectContainer objectContainer, int detectorX, int detectorY, int width, int height, List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        LemezaDetector detector = new LemezaDetector(objectContainer, detectorX, detectorY);

//        detector.setSecondsWait(0);
//        detector.setFramesWait(0);
//        detector.setCumulative(false);
//        detector.setInteractionType(0);
        detector.setGraphicalTileWidth(width);
        detector.setGraphicalTileHeight(height);

        detector.getTestByteOperations().addAll(tests);
        detector.getWriteByteOperations().addAll(updates);

        objectContainer.getObjects().add(detector);

        return detector;
    }

    public static void addPunchyFist(Screen screen, int x, int y, List<WriteByteOperation> updates) {
        Animation punchyFist = new Animation(screen, x, y);

        punchyFist.setLayer(0);
        punchyFist.setArg1(0);
        punchyFist.setGraphic(0);
        punchyFist.setMdd(63);
        punchyFist.setEntryEffect(0);
        punchyFist.setExitEffect(0);
        punchyFist.setUseDustTrail(0);
        punchyFist.setMotionDamage(32);
        punchyFist.setFinishedDamage(32);
        punchyFist.setArg9(-1);
        punchyFist.setArg10(-1);
        punchyFist.setArg11(-1);
        punchyFist.setArg12(-1);
        punchyFist.setArg13(-1);
        punchyFist.setArg14(-1);
        punchyFist.setArg15(-1);
        punchyFist.setArg16(-1);
        punchyFist.setArg17(-1);
        punchyFist.setArg18(0);
        punchyFist.setAnimationXShift(-160);
        punchyFist.setAnimationYShift(0);
        punchyFist.setArg21(0);
        punchyFist.setArg22(1000);

        punchyFist.getWriteByteOperations().addAll(updates);

        screen.getObjects().add(punchyFist);
    }

    public static void addEscapeTimer(Screen screen) {
        // The escape timer itself
        EscapeTimer escapeTimer = new EscapeTimer(screen);
        escapeTimer.setXPosition(264);
        escapeTimer.setYPosition(20);

        if(HolidaySettings.isHalloween2019Mode() && HolidaySettings.isIncludeHellTempleNPCs()) {
            escapeTimer.setMinutes(10);
            escapeTimer.setSeconds(31);
        }
        else {
            escapeTimer.setMinutes(Settings.isRandomizeTransitionGates() ? 10 : 5);
            escapeTimer.setSeconds(0);
        }

        escapeTimer.setTimerMode(EscapeTimer.CountDown);
        escapeTimer.setRedSeconds(10);
        escapeTimer.setSecondSound(-1);
        escapeTimer.setRedSecondSound(-1);
        escapeTimer.setArg8(-1);
        escapeTimer.setPauseTimerFlag(1000);
        escapeTimer.setTimerRunOutFlag(2746);
        escapeTimer.setStopTimerFlag(2747);

        escapeTimer.addTests(
                new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1),
                new TestByteOperation(FlagConstants.ESCAPE_TRIGGERED, ByteOp.FLAG_EQUALS, 0));

        screen.getObjects().add(0, escapeTimer);

        addFramesTimer(screen, 2,
                Arrays.asList(
                        new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1),
                        new TestByteOperation(FlagConstants.ESCAPE_TRIGGERED, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(new WriteByteOperation(FlagConstants.ESCAPE_TRIGGERED, ByteOp.ASSIGN_FLAG, 1)));

        if(!Settings.isScreenshakeDisabled()) {
            // Escape screen shake
            EscapeScreenShake escapeScreenShake = new EscapeScreenShake(screen);
            escapeScreenShake.setStopFlag(-1);
            escapeScreenShake.setStopValue(0);

            escapeScreenShake.addTests(
                    new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1),
                    new TestByteOperation(FlagConstants.ESCAPE_TRIGGERED, ByteOp.FLAG_EQUALS, 0));

            screen.getObjects().add(0, escapeScreenShake);
        }
    }

    public static void addEscapeTimer(Screen screen, int beginConditionFlag, int beginConditionValue) {
        // The escape timer itself
        EscapeTimer escapeTimer = new EscapeTimer(screen);
        escapeTimer.setXPosition(264);
        escapeTimer.setYPosition(20);

        if(HolidaySettings.isHalloween2021Mode()
                || (HolidaySettings.isHalloween2019Mode() && HolidaySettings.isIncludeHellTempleNPCs())) {
            escapeTimer.setMinutes(10);
            escapeTimer.setSeconds(31);
        }
        else {
            escapeTimer.setMinutes(Settings.isRandomizeTransitionGates() ? 10 : 5);
            escapeTimer.setSeconds(0);
        }

        escapeTimer.setTimerMode(EscapeTimer.CountDown);
        escapeTimer.setRedSeconds(10);
        escapeTimer.setSecondSound(-1);
        escapeTimer.setRedSecondSound(-1);
        escapeTimer.setArg8(-1);
        escapeTimer.setPauseTimerFlag(1000);
        escapeTimer.setTimerRunOutFlag(1001);
        escapeTimer.setStopTimerFlag(1002);

        escapeTimer.addTests(
                new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1),
                new TestByteOperation(FlagConstants.ESCAPE_TRIGGERED, ByteOp.FLAG_EQUALS, 0));

        screen.getObjects().add(0, escapeTimer);

        if(!Settings.isScreenshakeDisabled()) {
            // Escape screen shake
            EscapeScreenShake escapeScreenShake = new EscapeScreenShake(screen);
            escapeScreenShake.setStopFlag(-1);
            escapeScreenShake.setStopValue(0);

            escapeScreenShake.addTests(
                    new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1),
                    new TestByteOperation(FlagConstants.ESCAPE_TRIGGERED, ByteOp.FLAG_EQUALS, 0));

            screen.getObjects().add(0, escapeScreenShake);
        }

        // Disable grail for escape
        GrailToggle grailToggle = new GrailToggle(screen, false);

        grailToggle.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1));

        screen.getObjects().add(0, grailToggle);

        addFramesTimer(screen, 0,
                Arrays.asList(
                        new TestByteOperation(beginConditionFlag, ByteOp.FLAG_GTEQ, beginConditionValue), // Count number of NPCs visited
                        new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(new WriteByteOperation(FlagConstants.ESCAPE, ByteOp.ASSIGN_FLAG, 1)));

        addFramesTimer(screen, 2,
                Arrays.asList(
                        new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1),
                        new TestByteOperation(FlagConstants.ESCAPE_TRIGGERED, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(new WriteByteOperation(FlagConstants.ESCAPE_TRIGGERED, ByteOp.ASSIGN_FLAG, 1)));
    }

    public static void addNpcConversationTimer(Screen screen, int flag) {
        List<WriteByteOperation> updates = new ArrayList<>();
        updates.add(new WriteByteOperation(flag, ByteOp.ASSIGN_FLAG, 2));
        updates.add(new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, ByteOp.ADD_FLAG, 1));
        if(flag == 0xabe) {
            // Mr. Slushfund
            updates.add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2E, ByteOp.ASSIGN_FLAG, 1));
        }
        addFramesTimer(screen, 0, Arrays.asList(new TestByteOperation(flag, ByteOp.FLAG_EQUALS, 1)), updates);
    }

    public static void addChonchonSpawner(Screen screen,
                                          int maxChonchons, int damage, int spawnRate, int speed, int amplitude,
                                          DropType dropType, TestByteOperation... tests) {
        ChonchonSpawner chonchonSpawner = new ChonchonSpawner(screen);
        chonchonSpawner.setSpawnRate(spawnRate);
        chonchonSpawner.setMaxChonchons(maxChonchons);
        chonchonSpawner.setOscillationAmplitude(amplitude);
        chonchonSpawner.setHealth(1);
        chonchonSpawner.setDamage(damage);
        chonchonSpawner.setDropType(dropType.getValue());
        chonchonSpawner.setSpeed(speed);
        chonchonSpawner.addTests(tests);
        screen.getObjects().add(0, chonchonSpawner);
    }

    public static void addGhostSpawner(Screen screen, int maxGhosts, int damageAndSoul, int spawnRate, int speedAndDropType, TestByteOperation... tests) {
        GhostSpawner ghostSpawner = new GhostSpawner(screen);
        ghostSpawner.setSpawnRate(spawnRate);
        ghostSpawner.setMaxGhosts(maxGhosts);
        ghostSpawner.setArg2(0);
        ghostSpawner.setGhostSpeedAndDropType(speedAndDropType);
        ghostSpawner.setGhostHealth(1);
        ghostSpawner.setGhostDamageAndSoul(damageAndSoul);
        ghostSpawner.setArg6(3);

        ghostSpawner.addTests(tests);

        screen.getObjects().add(0, ghostSpawner);
    }

    public static Skeleton addSkeleton(Screen screen, int x, int y, TestByteOperation... tests) {
        Skeleton skeleton = new Skeleton(screen, x, y);
        skeleton.setFacing(0);
        skeleton.setDropType(DropType.RANDOM_COINS_WEIGHTS_SOUL);
        skeleton.setSpeed(1);
        skeleton.setInitiallyWalking(true);
        skeleton.setSkeletonType(Skeleton.TYPE_SKULLETON);
        skeleton.setHealth(4);
        skeleton.setContactDamage(4);
        skeleton.setProjectileDamage(2);
        skeleton.setSoul(3);
        skeleton.setProjectileSpeed(2);

        screen.getObjects().add(skeleton);
        return skeleton;
    }

    public static GameObject addGhostLord(Screen screen, int x, int y, int speed, int health, int damage, int soul) {
        GhostLord ghostLord = new GhostLord(screen, x, y);
        ghostLord.setDrops(DropType.COINS, 30);
        ghostLord.setSpeed(speed);
        ghostLord.setHealth(health);
        ghostLord.setDamage(damage);
        ghostLord.setSoul(soul);
        screen.getObjects().add(ghostLord);
        return ghostLord;
    }

    public static void addHTExitDoor(Screen screen) {
        WarpDoor htExitDoor = new WarpDoor(screen, 220, 80);
        htExitDoor.setDestination(23, 0, 0, 180, 332);

        htExitDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1));

        screen.getObjects().add(htExitDoor);

        GraphicsTextureDraw htExitDoorGraphic = new GraphicsTextureDraw(screen, 200, 40);

        htExitDoorGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1));

        htExitDoorGraphic.setLayer(-1);
        htExitDoorGraphic.setImageFile("map*_1.png");
        htExitDoorGraphic.setImageX(560);
        htExitDoorGraphic.setImageY(40);
        htExitDoorGraphic.setImageWidth(80);
        htExitDoorGraphic.setImageHeight(80);
        htExitDoorGraphic.setAnimation(0, 0, 1, 0);
        htExitDoorGraphic.setCollision(HitTile.Air);
        htExitDoorGraphic.setRGBAMax(0, 0, 0, 255);
        htExitDoorGraphic.setArg23(1);

        screen.getObjects().add(htExitDoorGraphic);
    }

    public static void addCreditsDoor(ObjectContainer screen, int x, int y) {
        ConversationDoor escapeDoor = new ConversationDoor(screen, x, y);
        escapeDoor.setArg0(0);
        escapeDoor.setArg1(0);
        escapeDoor.setArg2(0);
        escapeDoor.setDoorType(ConversationDoor.SingleConversation);
        escapeDoor.setBlockNumber(926);
        escapeDoor.setArg5(0);
        escapeDoor.setDisallowMusicChange(true);
        escapeDoor.addTests(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1));
        screen.getObjects().add(escapeDoor);
    }

    public static GameObject addLaserWall(Screen screen, int x, int y, boolean flatDamage, int damage) {
        LaserWall laserWall = new LaserWall(screen, x, y);

        if(flatDamage) {
            laserWall.setFlatDamage(damage);
        }
        else {
            laserWall.setPercentDamage(damage);
        }

        screen.getObjects().add(laserWall);
        return laserWall;
    }

    public static void addAllMantrasRecitedTimer(ObjectContainer objectContainer) {
        FlagTimer mantraTimer = new FlagTimer(objectContainer);
        int totalMantras = 8;
        if(Settings.getEnabledGlitches().contains("Lamp Glitch")) {
            totalMantras = 5;
        }
        mantraTimer.addTests(new TestByteOperation(FlagConstants.MANTRAS_RECITED_COUNT, ByteOp.FLAG_GTEQ, totalMantras),
                new TestByteOperation(FlagConstants.MANTRA_FINAL, ByteOp.FLAG_NOT_EQUAL, 4));
        mantraTimer.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MANTRA_FINAL, ByteOp.ASSIGN_FLAG, 4));

        objectContainer.getObjects().add(0, mantraTimer);
    }

    public static GameObject addStunWitch(Screen screen, int x, int y, boolean faceRight) {
        GameObject stunWitch = new GameObject(screen);
        stunWitch.setId((short) 0x55);
        stunWitch.setX(x);
        stunWitch.setY(y);

        stunWitch.getArgs().add((short)(faceRight ? 0 : 1));
        stunWitch.getArgs().add((short)2);
        stunWitch.getArgs().add((short)0); // Drop type
        stunWitch.getArgs().add((short)2); // Speed
        stunWitch.getArgs().add((short)400); // Health
        stunWitch.getArgs().add((short)5); // Contact damage
        stunWitch.getArgs().add((short)0); // Soul
        stunWitch.getArgs().add((short)120); // Time between volleys attacks
        stunWitch.getArgs().add((short)1); // Projectiles per volley
        stunWitch.getArgs().add((short)20); // Delay after shot
        stunWitch.getArgs().add((short)6); // Projectile speed
        stunWitch.getArgs().add((short)2); // Secondary projectile speed (first split for witches)
        stunWitch.getArgs().add((short)2); // Tertiary projectile speed (first split for witches)
        stunWitch.getArgs().add((short)8); // Initial projectile damage
        stunWitch.getArgs().add((short)8); // Secondary projectile damage (lingering flame, first split)
        stunWitch.getArgs().add((short)8); // Tertiary projectile damage (second split)
        stunWitch.getArgs().add((short)2); // Initial projectile duration (time to first split)
        stunWitch.getArgs().add((short)150); // Secondary projectile duration (flame duration, time to second split)
        stunWitch.getArgs().add((short)2); // Tertiary projectile duration (flame duration, time to second split)
        stunWitch.getArgs().add((short)0); // Crashes the game when changed

        screen.getObjects().add(stunWitch);
        return stunWitch;
    }

    public static void addMovingPlatforms(Screen screen) {
        GameObject platform1 = new GameObject(screen);
        platform1.setId((short) 0xc);
        platform1.setX(800);
        platform1.setY(160);

        platform1.getArgs().add((short)0); // 0 - Tile Sheet map=0,1 eveg=2
        platform1.getArgs().add((short)380); // 1 - (0-980) TileX
        platform1.getArgs().add((short)120); // 2 - (0-562) TileY
        platform1.getArgs().add((short)40); // 3 - (20-200) dX
        platform1.getArgs().add((short)20); // 4 - (20-120) dY
        platform1.getArgs().add((short)0); // 5 - (0-6) Displaces the platform sprite number of pixels left. Setting too high can break vertical platforms
        platform1.getArgs().add((short)0); // 6 - (0-20) Displaces the platform sprite num pixels up. Interferes with vert. screen transitioning standing on platform. Setting too high can break horiz platforms
        platform1.getArgs().add((short)40); // 7 - (20-200) hitbox width
        platform1.getArgs().add((short)20); // 8 - (20-120) hitbox height
        platform1.getArgs().add((short)790); // 9 - (-1-1980) Platform left bound in Tile-Block (-1 causes horizontal platforms to wrap around the screen, like the one in hell temple)
        platform1.getArgs().add((short)150); // 10- (80-1320) Platform upper bound in Tile-Block (-1 causes vertical platforms to wrap)
        platform1.getArgs().add((short)40); // 11- (0-520) How far the platform moves right. Setting too low can break vertical platforms
        platform1.getArgs().add((short)220); // 12- (40-1120) How far the platform moves down. Setting too low can break horizontal platforms
        platform1.getArgs().add((short)270); // 13- (0-270) Platform Direction (angle CW from x direction)
        platform1.getArgs().add((short)1); // 14- (0-1) 0 = Stops when it reaches the edge on the side, 1 = moves back and forth
        platform1.getArgs().add((short)1); // 15- (0-1) ??
        platform1.getArgs().add((short)0); // 16- (0-1) ??
        platform1.getArgs().add((short)150); // 17- (100-240) Platform Speed
        screen.getObjects().add(platform1);

//        GameObject platform2 = new GameObject(screen);
//        platform2.setId((short) 0xc);
//        platform2.setX(920);
//        platform2.setY(240);
//
//        platform2.getArgs().add((short)0); // 0 - Tile Sheet map=0,1 eveg=2
//        platform2.getArgs().add((short)380); // 1 - (0-980) TileX
//        platform2.getArgs().add((short)120); // 2 - (0-562) TileY
//        platform2.getArgs().add((short)40); // 3 - (20-200) dX
//        platform2.getArgs().add((short)20); // 4 - (20-120) dY
//        platform2.getArgs().add((short)0); // 5 - (0-6) Displaces the platform sprite number of pixels left. Setting too high can break vertical platforms
//        platform2.getArgs().add((short)0); // 6 - (0-20) Displaces the platform sprite num pixels up. Interferes with vert. screen transitioning standing on platform. Setting too high can break horiz platforms
//        platform2.getArgs().add((short)40); // 7 - (20-200) hitbox width
//        platform2.getArgs().add((short)20); // 8 - (20-120) hitbox height
//        platform2.getArgs().add((short)860); // 9 - (-1-1980) Platform left bound in Tile-Block (-1 causes horizontal platforms to wrap around the screen, like the one in hell temple)
//        platform2.getArgs().add((short)200); // 10- (80-1320) Platform upper bound in Tile-Block (-1 causes vertical platforms to wrap)
//        platform2.getArgs().add((short)180); // 11- (0-520) How far the platform moves right. Setting too low can break vertical platforms
//        platform2.getArgs().add((short)180); // 12- (40-1120) How far the platform moves down. Setting too low can break horizontal platforms
//        platform2.getArgs().add((short)45); // 13- (0-270) Platform Direction (angle CW from x direction)
//        platform2.getArgs().add((short)1); // 14- (0-1) 0 = Stops when it reaches the edge on the side, 1 = moves back and forth
//        platform2.getArgs().add((short)1); // 15- (0-1) ??
//        platform2.getArgs().add((short)0); // 16- (0-1) ??
//        platform2.getArgs().add((short)150); // 17- (100-240) Platform Speed
//        screen.getObjects().add(platform2);

        GameObject platform3 = new GameObject(screen);
        platform3.setId((short) 0xc);
        platform3.setX(1100);
        platform3.setY(360);

        platform3.getArgs().add((short)0); // 0 - Tile Sheet map=0,1 eveg=2
        platform3.getArgs().add((short)380); // 1 - (0-980) TileX
        platform3.getArgs().add((short)120); // 2 - (0-562) TileY
        platform3.getArgs().add((short)40); // 3 - (20-200) dX
        platform3.getArgs().add((short)20); // 4 - (20-120) dY
        platform3.getArgs().add((short)0); // 5 - (0-6) Displaces the platform sprite number of pixels left. Setting too high can break vertical platforms
        platform3.getArgs().add((short)0); // 6 - (0-20) Displaces the platform sprite num pixels up. Interferes with vert. screen transitioning standing on platform. Setting too high can break horiz platforms
        platform3.getArgs().add((short)40); // 7 - (20-200) hitbox width
        platform3.getArgs().add((short)20); // 8 - (20-120) hitbox height
        platform3.getArgs().add((short)1090); // 9 - (-1-1980) Platform left bound in Tile-Block (-1 causes horizontal platforms to wrap around the screen, like the one in hell temple)
        platform3.getArgs().add((short)150); // 10- (80-1320) Platform upper bound in Tile-Block (-1 causes vertical platforms to wrap)
        platform3.getArgs().add((short)40); // 11- (0-520) How far the platform moves right. Setting too low can break vertical platforms
        platform3.getArgs().add((short)220); // 12- (40-1120) How far the platform moves down. Setting too low can break horizontal platforms
        platform3.getArgs().add((short)90); // 13- (0-270) Platform Direction (angle CW from x direction)
        platform3.getArgs().add((short)1); // 14- (0-1) 0 = Stops when it reaches the edge on the side, 1 = moves back and forth
        platform3.getArgs().add((short)1); // 15- (0-1) ??
        platform3.getArgs().add((short)0); // 16- (0-1) ??
        platform3.getArgs().add((short)150); // 17- (100-240) Platform Speed
        screen.getObjects().add(platform3);

        TestByteOperation testByteOperation = new TestByteOperation(FlagConstants.THE_BOSS_STATE, ByteOp.FLAG_EQUALS, 2);
        platform1.getTestByteOperations().add(testByteOperation);
//        platform2.getTestByteOperations().add(testByteOperation);
        platform3.getTestByteOperations().add(testByteOperation);
    }

    public static void addHadoukenTurtle(Screen screen, int x, int y) {
        GameObject enemy = new GameObject(screen);
        enemy.setId((short)0x62);
        enemy.setX(x);
        enemy.setY(y);

        enemy.getArgs().add((short)0); // Facing
        enemy.getArgs().add((short)11); // Drop type
        enemy.getArgs().add((short)3); // Speed
        enemy.getArgs().add((short)24); // Health
        enemy.getArgs().add((short)16); // Contact damage
        enemy.getArgs().add((short)11); // Soul
        enemy.getArgs().add((short)3); // Proj Speed
        enemy.getArgs().add((short)5); // Projectiles per volley
        enemy.getArgs().add((short)10); // Delay between shots
        enemy.getArgs().add((short)16); // Projectile damage

        TestByteOperation enemyTest = new TestByteOperation();
        enemyTest.setIndex(FlagConstants.EXTINCTION_TRAP_FAKE_ANKH);
        enemyTest.setValue((byte) 2);
        enemyTest.setOp(ByteOp.FLAG_EQUALS);
        enemy.getTestByteOperations().add(enemyTest);

        screen.getObjects().add(enemy);
    }

    public static GameObject addSecretShop(Screen screen, int secretShopBlock) {
        ConversationDoor shop = new ConversationDoor(screen, 0, 240);
        shop.setShopDefaults();
        shop.setBlockNumber(secretShopBlock);

        shop.addTests(new TestByteOperation(FlagConstants.CUSTOM_SECRET_SHOP, ByteOp.FLAG_GTEQ, 1));

        screen.getObjects().add(shop);
        return shop;
    }

    public static DanceDetector addDanceDetector(Screen screen, int x, int y, int danceBlock) {
        DanceDetector danceDetector = new DanceDetector(screen, x, y);

        danceDetector.setBlockNumber(danceBlock);
        danceDetector.setWidth(32);
        danceDetector.setHeight(24);

        danceDetector.getTestByteOperations().add(new TestByteOperation(FlagConstants.CUSTOM_SECRET_SHOP, ByteOp.FLAG_EQUALS, 0));
        danceDetector.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.CUSTOM_SECRET_SHOP, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(danceDetector);
        return danceDetector;
    }

    public static DanceDetector addDanceDetector(Screen screen, int x, int y, int danceBlock, List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        DanceDetector danceDetector = new DanceDetector(screen, x, y);

        danceDetector.setBlockNumber(danceBlock);
        danceDetector.setWidth(32);
        danceDetector.setHeight(24);

        danceDetector.addTests(tests);
        danceDetector.addUpdates(updates);

        screen.getObjects().add(danceDetector);
        return danceDetector;
    }

    public static ItemGive addItemGive(GameObject referenceObj, int inventoryArg, int randomizeGraphicsFlag, int worldFlag) {
        int x = (referenceObj.getX() / 640) * 640;
        int y = (referenceObj.getY() / 480) * 480;
        ItemGive itemGive = new ItemGive(referenceObj.getObjectContainer(), x, y);
        itemGive.setInventoryWord(inventoryArg);
        itemGive.setWidth(32);
        itemGive.setHeight(24);
        itemGive.setSoundEffect(39);

        itemGive.getTestByteOperations().add(new TestByteOperation(randomizeGraphicsFlag, ByteOp.FLAG_EQUALS, 2));
        itemGive.getTestByteOperations().add(new TestByteOperation(worldFlag, ByteOp.FLAG_NOT_EQUAL, 2));

        itemGive.getWriteByteOperations().add(new WriteByteOperation(worldFlag, ByteOp.ASSIGN_FLAG, 2));

        referenceObj.getObjectContainer().getObjects().add(itemGive);
        return itemGive;
    }

    public static ItemGive addItemGive(GameObject referenceObj, int inventoryArg) {
        int x = (referenceObj.getX() / 640) * 640;
        int y = (referenceObj.getY() / 480) * 480;
        ItemGive itemGive = new ItemGive(referenceObj.getObjectContainer(), x, y);
        itemGive.setInventoryWord(inventoryArg);
        itemGive.setWidth(32);
        itemGive.setHeight(24);
        itemGive.setSoundEffect(39);

        referenceObj.getObjectContainer().getObjects().add(itemGive);
        return itemGive;
    }

    public static void addItemGive(Screen screen, int startingX, int startingY, int inventoryArg,
                                   List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        ItemGive itemGive = new ItemGive(screen, startingX, startingY);

        itemGive.setInventoryWord(inventoryArg);
        itemGive.setWidth(32);
        itemGive.setHeight(24);
        itemGive.setSoundEffect(39);

        itemGive.getTestByteOperations().addAll(tests);
        itemGive.getWriteByteOperations().addAll(updates);

        screen.getObjects().add(itemGive);
    }

    public static void addGrailDetector(GameObject gameObject, int grailFlag) {
        addLemezaDetector(gameObject.getObjectContainer(), gameObject.getX(), gameObject.getY() - 20, 2, 3,
                Arrays.asList(new TestByteOperation(grailFlag, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(new WriteByteOperation(grailFlag, ByteOp.ASSIGN_FLAG, (byte)1)));
    }

    /**
     * Add exit to untrue shrine so it doesn't break during the escape
     * @param screen the screen to add the object to
     */
    public static GameObject addUntrueShrineExit(Screen screen, int whichShrineExit) {
        TransitionGate obj;
        if(whichShrineExit == 0) {
            obj = new TransitionGate(screen, 300, 940);
            obj.setDestination(6, 7, 1, 300, 20);
        }
        else if(whichShrineExit == 1) {
            obj = new TransitionGate(screen, 300, 460);
            obj.setDestination(8, 2, 3, 300, 20);
        }
        else {
            obj = new TransitionGate(screen, 940, 460);
            obj.setDestination(7, 9, 1, 300, 20);
        }
        obj.setTransitionDirection(TransitionGate.Down);
        obj.setArg6(1);

        obj.addTests(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1));

        screen.getObjects().add(obj);
        return obj;
    }

    /**
     * Add exit to untrue shrine so it doesn't break during the escape
     * @param screen the screen to add the object to
     */
    public static void addSpecialTransitionWarp(Screen screen, int zoneIndex) {
        WarpPortal warp;
        if(zoneIndex == 0) {
            warp = new WarpPortal(screen, 140, 440);
            warp.setDestination(zoneIndex, 8, 1, 300, 20);
        }
        else { //if(zoneIndex == 5){
            warp = new WarpPortal(screen, 300, 440);
            warp.setDestination(zoneIndex, 8, 1, 140, 20);
        }
        warp.setDimensions(4, 4);

        screen.getObjects().add(warp);
    }

    /**
     * Add exit to untrue shrine so it doesn't break during the escape
     * @param screen the screen to add the object to
     */
    public static GameObject addSpecialTransitionGate(Screen screen, int zoneIndex) {
        TransitionGate obj;

        if(zoneIndex == 0) {
            obj = new TransitionGate(screen, 140, 460);
            obj.setDestination(zoneIndex, 8, 0, 300, 392);
            obj.setTransitionDirection(TransitionGate.Up);
        }
        else { // if(zoneIndex == 5){
            obj = new TransitionGate(screen, 300, 460);
            obj.setDestination(zoneIndex, 8, 0, 140, 392);
            obj.setTransitionDirection(TransitionGate.Up);
        }
        obj.setArg6(0);

        obj.addTests(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0));

        screen.getObjects().add(obj);
        addEscapeGate(obj);
        return obj;
    }

    public static GameObject addEscapeGate(GameObject nonEscapeGate) {
        GameObject escapeGate = new GameObject(nonEscapeGate);
        for(TestByteOperation testByteOperation : escapeGate.getTestByteOperations()) {
            if(testByteOperation.getIndex() == FlagConstants.ESCAPE && testByteOperation.getValue() == 0) {
                testByteOperation.setValue((byte)1);
                break;
            }
        }
        escapeGate.getArgs().set(6, (short)1);
        nonEscapeGate.getObjectContainer().getObjects().add(escapeGate);
        return escapeGate;
    }

    public static void addTrueShrineGate(GameObject basicGate) {
        GameObject trueShrineGate = new GameObject(basicGate);

        TestByteOperation testByteOperation = trueShrineGate.getTestByteOperations().get(0);
        testByteOperation.setIndex(FlagConstants.BOSSES_SHRINE_TRANSFORM);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)9);

        // Add extra check for not during escape, since escape door is different.
        trueShrineGate.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_NOT_EQUAL, 1));

        trueShrineGate.getArgs().set(0, (short)18);
        trueShrineGate.getArgs().set(6, (short)0);
        basicGate.getObjectContainer().getObjects().add(trueShrineGate);
    }

    public static void addShrineMapSoundEffect(ObjectContainer objectContainer) {
        addSuccessSound(objectContainer, Arrays.asList(
                new TestByteOperation(FlagConstants.WF_MAP_SHRINE, ByteOp.FLAG_EQUALS, 2),
                new TestByteOperation(FlagConstants.SCREEN_FLAG_2A, ByteOp.FLAG_EQUALS, 1)));

        addFramesTimer(objectContainer, 0,
                Arrays.asList(
                        new TestByteOperation(FlagConstants.SOUND_EFFECT_PLAYED_SHRINE_MAP, ByteOp.FLAG_EQUALS, 0),
                        new TestByteOperation(FlagConstants.WF_MAP_SHRINE, ByteOp.FLAG_EQUALS, 2)),
                Arrays.asList(
                        new WriteByteOperation(FlagConstants.SOUND_EFFECT_PLAYED_SHRINE_MAP, ByteOp.ASSIGN_FLAG, 1),
                        new WriteByteOperation(FlagConstants.SCREEN_FLAG_2A, ByteOp.ASSIGN_FLAG, 1)));

    }

    public static void addMotherAnkhJewelRecoveryTimer(Screen screen) {
        addFramesTimer(screen, 0,
                Arrays.asList(
                        new TestByteOperation(FlagConstants.MOTHER_STATE, ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0),
                        new TestByteOperation(FlagConstants.MOTHER_ANKH_JEWEL_RECOVERY, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(new WriteByteOperation(FlagConstants.MOTHER_ANKH_JEWEL_RECOVERY, ByteOp.ASSIGN_FLAG, 1)));
    }

    public static void addMotherAnkhJewelItemGive(Screen screen) {
        ItemGive itemGive = new ItemGive(screen, 100, 60);

        itemGive.setInventoryWord(19);
        itemGive.setWidth(12);
        itemGive.setHeight(16);
        itemGive.setSoundEffect(39);

        itemGive.addTests(new TestByteOperation(FlagConstants.MOTHER_ANKH_JEWEL_RECOVERY, ByteOp.FLAG_EQUALS, 1));

        itemGive.addUpdates(
                new WriteByteOperation(FlagConstants.MOTHER_ANKH_JEWEL_RECOVERY, ByteOp.ASSIGN_FLAG, 0),
                new WriteByteOperation(FlagConstants.MOTHER_STATE, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(itemGive);
    }

    public static GameObject addMissingBacksideDoorGate(GameObject backsideDoor, int gateFlag) {
        GraphicsTextureDraw doorCoverGraphic = new GraphicsTextureDraw(backsideDoor.getObjectContainer(), backsideDoor.getX(), backsideDoor.getY());

        doorCoverGraphic.setLayer(-1);
        doorCoverGraphic.setImageFile("01effect.png");
        doorCoverGraphic.setImageX(0);
        doorCoverGraphic.setImageY(360);
        doorCoverGraphic.setImageWidth(40);
        doorCoverGraphic.setImageHeight(40);
        doorCoverGraphic.setAnimation(1, 7, 6, 1);
        doorCoverGraphic.setCollision(HitTile.Air);
        doorCoverGraphic.setExitEffect(GraphicsTextureDraw.ExitEffect_Animate);
        doorCoverGraphic.setRGBAMax(0, 0, 0, 255);
        doorCoverGraphic.setArg23(1);

        doorCoverGraphic.addTests(
                new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0),
                new TestByteOperation(FlagConstants.WF_BRONZE_MIRROR, ByteOp.FLAG_EQUALS, 2));

        doorCoverGraphic.addUpdates(new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1));

        backsideDoor.getObjectContainer().getObjects().add(doorCoverGraphic);
        return doorCoverGraphic;
    }

    public static GameObject addMissingBacksideDoorGateTimerAndSound(ObjectContainer objectContainer, int bossFlag, int gateFlag) {
        SoundEffect doorSoundEffect = new SoundEffect(objectContainer);
        doorSoundEffect.setSoundEffect(95);
        doorSoundEffect.setVolume(127, 127, 0);
        doorSoundEffect.setBalance(64, 64, 0);
        doorSoundEffect.setPitch(-400, 0, 0);
        doorSoundEffect.setPriority(15);
//        doorSoundEffect.setArg8(0);
//        doorSoundEffect.setFramesDelay(0);
//        doorSoundEffect.setControllerRumble(false);
        doorSoundEffect.setRumbleStrength(10);

        doorSoundEffect.addTests(new TestByteOperation(FlagConstants.SCREEN_FLAG_29, ByteOp.FLAG_EQUALS, 1));

        objectContainer.getObjects().add(0, doorSoundEffect);

        return addSecondsTimer(objectContainer, 1,
            Arrays.asList(
                    new TestByteOperation(bossFlag, ByteOp.FLAG_GTEQ, 3),
                    new TestByteOperation(FlagConstants.WF_BRONZE_MIRROR, ByteOp.FLAG_EQUALS, 2),
                    new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0)),
            Arrays.asList(
                    new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_29, ByteOp.ASSIGN_FLAG, 1)));
    }

    public static GameObject addMissingBacksideMirrorTimerAndSound(ObjectContainer objectContainer, int mirrorCoverFlag) {
        SoundEffect mirrorSoundEffect = new SoundEffect(objectContainer);
        mirrorSoundEffect.setSoundEffect(48);
        mirrorSoundEffect.setVolume(127, 127, 10);
        mirrorSoundEffect.setBalance(64, 64, 0);
        mirrorSoundEffect.setPitch(-700, -700, 0);
        mirrorSoundEffect.setPriority(20);
//        mirrorSoundEffect.setArg8(0);
//        mirrorSoundEffect.setFramesDelay(0);
//        mirrorSoundEffect.setControllerRumble(false);
        mirrorSoundEffect.setRumbleStrength(10);

        mirrorSoundEffect.addTests(new TestByteOperation(FlagConstants.SCREEN_FLAG_28, ByteOp.FLAG_EQUALS, 1));

        objectContainer.getObjects().add(0, mirrorSoundEffect);

        return addFramesTimer(objectContainer, 30,
                Arrays.asList(
                        new TestByteOperation(FlagConstants.WF_BRONZE_MIRROR, ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(
                        new WriteByteOperation(mirrorCoverFlag, ByteOp.ASSIGN_FLAG, 1),
                        new WriteByteOperation(FlagConstants.SCREEN_FLAG_28, ByteOp.ASSIGN_FLAG, 1)));
    }

    public static GameObject addMissingBacksideDoorMirrorCoverGraphic(GameObject backsideDoor, int mirrorCoverFlag, boolean extinctionDoor) {
        GraphicsTextureDraw mirrorCoverGraphic = new GraphicsTextureDraw(backsideDoor.getObjectContainer(), backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        mirrorCoverGraphic.setLayer(-1);
        mirrorCoverGraphic.setImageFile("eveg*.png");
        if(extinctionDoor) {
            mirrorCoverGraphic.setImageX(940);
            mirrorCoverGraphic.setImageY(120);
        }
        else {
            mirrorCoverGraphic.setImageX(540);
            mirrorCoverGraphic.setImageY(0);
        }
        mirrorCoverGraphic.setImageWidth(80);
        mirrorCoverGraphic.setImageHeight(80);
        mirrorCoverGraphic.setAnimation(1, 1, 4, 0);
        mirrorCoverGraphic.setCollision(HitTile.Air);
        mirrorCoverGraphic.setExitEffect(GraphicsTextureDraw.ExitEffect_FadeOut);
        mirrorCoverGraphic.setRGBAMax(0, 0, 0, 255);
        mirrorCoverGraphic.setArg23(1);

        mirrorCoverGraphic.addTests(new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0));

        backsideDoor.getObjectContainer().getObjects().add(mirrorCoverGraphic);
        return mirrorCoverGraphic;
    }

    public static void addExtinctionTorch(Screen screen, int x, int y) {
        Hitbox extinctionTorchHitbox = new Hitbox(screen, x, y);
//        extinctionTorchHitbox.setVisual(0);
        extinctionTorchHitbox.setHealth(1);
        extinctionTorchHitbox.setBreakable(Hitbox.AnyDirection, Hitbox.FlareGun);
//        extinctionTorchHitbox.setUpdateOnlyForCorrectWeapon();
        extinctionTorchHitbox.setDimensions(2, 2);
        extinctionTorchHitbox.setSoundEffects(-1, -1);
//        extinctionTorchHitbox.setDustDensity1(0);
//        extinctionTorchHitbox.setDustDensity2(0);

        extinctionTorchHitbox.addTests(new TestByteOperation(FlagConstants.EXTINCTION_PERMA_LIGHT, ByteOp.FLAG_LTEQ, 2));
        extinctionTorchHitbox.addUpdates(new WriteByteOperation(FlagConstants.EXTINCTION_TEMP_LIGHT, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(extinctionTorchHitbox);

        GraphicsTextureDraw extinctionTorchLight = new GraphicsTextureDraw(screen, x, y);
        extinctionTorchLight.setLayer(0);
        extinctionTorchLight.setImageFile("map*_1.png");
        extinctionTorchLight.setImageX(80);
        extinctionTorchLight.setImageY(400);
        extinctionTorchLight.setImageWidth(40);
        extinctionTorchLight.setImageHeight(40);
        extinctionTorchLight.setAnimation(0, 1, 4, 0);
        extinctionTorchLight.setCollision(HitTile.Air);
        extinctionTorchLight.setExitEffect(GraphicsTextureDraw.ExitEffect_FadeOut);
        extinctionTorchLight.setCycleColors(true);
        extinctionTorchLight.setRGBAPerFrame(0, 0, 0, 5);
        extinctionTorchLight.setRGBAMax(0, 0, 0, 160);
        extinctionTorchLight.setArg23(1);

        extinctionTorchLight.addTests(new TestByteOperation(FlagConstants.EXTINCTION_TEMP_LIGHT, ByteOp.FLAG_EQUALS, 1));

        screen.getObjects().add(extinctionTorchLight);

        GraphicsTextureDraw extinctionTorch = new GraphicsTextureDraw(screen, x, y);
        extinctionTorch.setLayer(-1);
        extinctionTorch.setImageFile("map*_1.png");
        extinctionTorch.setImageX(40);
        extinctionTorch.setImageY(400);
        extinctionTorch.setImageWidth(40);
        extinctionTorch.setImageHeight(40);
        extinctionTorch.setAnimation(0, 0, 1, 0);
        extinctionTorch.setCollision(HitTile.Air);
        extinctionTorch.setRGBAMax(0, 0, 0, 255);
        extinctionTorch.setArg23(1);

        screen.getObjects().add(extinctionTorch);
    }

    /**
     * Add ladder to help with Dimensional Sacred Orb chest after Ushumgallu's death.
     * @param screen the screen to add the timers to
     */
    public static void addDimensionalOrbLadder(ObjectContainer screen) {
        Ladder obj = new Ladder(screen, 560, 620);
        obj.setExtendDirection(Ladder.FromTop);
        obj.setHeight(8);
        obj.setGraphicsFromEvegPng();
        obj.setArg3(0);
        obj.setImageX(660);
        obj.setImageY(0);
        obj.setStandardLadder();
        obj.setArg7(1);
        obj.addTests(new TestByteOperation(FlagConstants.USHUMGALLU_STATE, ByteOp.FLAG_EQUALS, 2));
        screen.getObjects().add(obj);
    }

    public static Block buildBonusCandyBlock(int bonusCount) {
        Block bonusCandyBlock = new Block();
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween2021.bonus" + bonusCount));
        for(Short shortCharacter : stringCharacters) {
            bonusCandyBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        bonusCandyBlock.getBlockContents().add(new BlockFlagData(FlagConstants.CUSTOM_HALLOWEEN2021_XELPUD_BONUS_CANDY_CONVERSATIONS, 2 * bonusCount));
        return bonusCandyBlock;
    }

    public static ShopBlock buildSecretShopBlock() {
        ShopBlock shopBlock = new ShopBlock();

        BlockListData shopBlockData = new BlockListData((short)3);
        shopBlockData.getData().add(DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Scriptures").getInventoryArg());
        shopBlockData.getData().add(DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Perfume").getInventoryArg());
        shopBlockData.getData().add(ItemConstants.COIN);
        shopBlock.setInventoryItemArgsList(shopBlockData);

        shopBlockData = new BlockListData((short)3);
        shopBlockData.getData().add((short)1);
        shopBlockData.getData().add((short)1);
        shopBlockData.getData().add((short)0);
        shopBlock.setInventoryPriceList(shopBlockData);

        shopBlockData = new BlockListData((short)3);
        shopBlockData.getData().add((short)1);
        shopBlockData.getData().add((short)1);
        shopBlockData.getData().add((short)100);
        shopBlock.setInventoryCountList(shopBlockData);

        shopBlockData = new BlockListData((short)3);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlock.setFlagList(shopBlockData);

        shopBlockData = new BlockListData((short)3);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlock.setExitFlagList(shopBlockData);

        shopBlock.setBackground(new BlockCmdSingle((short)4));
        shopBlock.setSprite(new BlockCmdSingle((short)0x2dc));
        shopBlock.setMusic(new BlockCmdSingle((short)4));

        BlockStringData blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(String.format("%s , %s , %s",
                Translations.getText("items.Scriptures"), Translations.getText("items.Perfume"), Translations.getText("items.Coin"))));
        shopBlock.setBunemonText(blockStringData);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText(
                "shop0.screenName.zone" + LocationCoordinateMapper.getStartingZone() + (LocationCoordinateMapper.isFrontsideStart() ? ".front" : ".back"))));
        shopBlock.setBunemonLocation(blockStringData);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.yesPurchaseString")));
        shopBlock.setString(blockStringData, 0);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.noPurchaseString")));
        shopBlock.setString(blockStringData, 1);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("text.secretShop")));
        shopBlock.setString(blockStringData, 2);

        blockStringData = new BlockStringData();
        List<Short> data = FileUtils.stringToData(Translations.getText("shop0.askItem1String.1"));
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add(BlockDataConstants.ItemName);
        data.add((short)105);
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0);
        data.add((short)0);
        data.add((short)0);
        blockStringData.getData().addAll(data);
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.askItem1String.2")));
        shopBlock.setString(blockStringData, 3);

        blockStringData = new BlockStringData();
        data = FileUtils.stringToData(Translations.getText("shop0.askItem2String.1"));
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add(BlockDataConstants.ItemName);
        data.add((short)105);
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0);
        data.add((short)0);
        data.add((short)0);
        blockStringData.getData().addAll(data);
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.askItem2String.2")));
        shopBlock.setString(blockStringData, 4);

        blockStringData = new BlockStringData();
        data = FileUtils.stringToData(Translations.getText("shop0.askItem3String.1"));
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add(BlockDataConstants.ItemName);
        data.add((short)105);
        data.add(BlockDataConstants.ColorChange);
        data.add((short)0);
        data.add((short)0);
        data.add((short)0);
        blockStringData.getData().addAll(data);
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.askItem3String.2")));
        shopBlock.setString(blockStringData, 5);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.boughtItem1String")));
        shopBlock.setString(blockStringData, 6);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.boughtItem2String")));
        shopBlock.setString(blockStringData, 7);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.boughtItem3String")));
        shopBlock.setString(blockStringData, 8);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.soldOutItem1String")));
        shopBlock.setString(blockStringData, 9);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.soldOutItem2String")));
        shopBlock.setString(blockStringData, 10);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.soldOutItem3String")));
        shopBlock.setString(blockStringData, 11);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.cancelItem1String")));
        shopBlock.setString(blockStringData, 12);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.cancelItem2String")));
        shopBlock.setString(blockStringData, 13);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.cancelItem3String")));
        shopBlock.setString(blockStringData, 14);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.noMoneyItem1String")));
        shopBlock.setString(blockStringData, 15);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.noMoneyItem2String")));
        shopBlock.setString(blockStringData, 16);

        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.noMoneyItem3String")));
        shopBlock.setString(blockStringData, 17);
        return shopBlock;
    }

    public static Block buildDanceBlock() {
        Block danceBlock = new Block();
        BlockListData danceMove = new BlockListData((short)1);
        danceMove.getData().add((short)1); // Jump
        danceBlock.getBlockContents().add(danceMove);
        danceBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        danceMove = new BlockListData((short)1);
        danceMove.getData().add((short)3); // Swing right
        danceBlock.getBlockContents().add(danceMove);
        danceBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        danceMove = new BlockListData((short)1);
        danceMove.getData().add((short)2); // Swing left
        danceBlock.getBlockContents().add(danceMove);
        danceBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        danceMove = new BlockListData((short)1);
        danceMove.getData().add((short)1); // Jump
        danceBlock.getBlockContents().add(danceMove);
        danceBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        danceMove = new BlockListData((short)1);
        danceMove.getData().add((short)2); // Swing left
        danceBlock.getBlockContents().add(danceMove);
        danceBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        danceMove = new BlockListData((short)1);
        danceMove.getData().add((short)3); // Swing right
        danceBlock.getBlockContents().add(danceMove);

        return danceBlock;
    }

    public static Block buildHalloweenCandyBlock(CustomBlockEnum customBlockEnum) {
        int npcConversationFlag = 0;
        String textPart2Key = "event.halloween.text2";
        if(CustomBlockEnum.HalloweenCandyConversationBlock_Hiner.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_Hiner);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_Moger.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_Moger);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_FormerMekuriMaster.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_FormerMekuriMaster_Mekuri);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestZarnac.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestZarnac);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestXanado.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestXanado);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherGiltoriyo.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PhilosopherGiltoriyo);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestHidlyda.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestHidlyda);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestRomancis.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestRomancis);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestAramo.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestAramo);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestTriton.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestTriton);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestJaguarfiv.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestJaguarfiv);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_MrSlushfund.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_MrSlushfund_Pepper);
            textPart2Key = "event.halloween.fraud";
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestAlest.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestAlest);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_StrayFairy.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_StrayFairy);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_GiantThexde.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_GiantThexde);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherAlsedana.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PhilosopherAlsedana);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherSamaranta.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PhilosopherSamaranta);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestLaydoc.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestLaydoc);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestAshgine.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestAshgine);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PhilosopherFobos.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PhilosopherFobos_Ladder);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_8BitElder.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_8BitElder);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_duplex.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_duplex);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_Samieru.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_Samieru);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_Naramura.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_Naramura);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_8bitFairy.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_8BitFairy);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestMadomono.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestMadomono);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_PriestGailious.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_PriestGailious);
        }
        else if(CustomBlockEnum.HalloweenCandyConversationBlock_NightSurfaceFairy.equals(customBlockEnum)) {
            npcConversationFlag = FlagConstants.getNpcConversationFlag(BlockConstants.Master_Fairy_NightSurface);
        }

        if(npcConversationFlag > 0) {
            Block candyBlock = new Block();
            List<BlockContents> candyBlockContents = candyBlock.getBlockContents();
            candyBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, 1));
            List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.text1"));
            for (Short shortCharacter : stringCharacters) {
                candyBlockContents.add(new BlockSingleData(shortCharacter));
            }
            candyBlockContents.add(new BlockItemData(ItemConstants.SECRET_TREASURE_OF_LIFE));
            candyBlockContents.add(new BlockFlagData(npcConversationFlag, 1));
            candyBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

            stringCharacters = FileUtils.stringToData(Translations.getText(textPart2Key));
            for (Short shortCharacter : stringCharacters) {
                candyBlockContents.add(new BlockSingleData(shortCharacter));
            }

            candyBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, 0));
            return candyBlock;
        }
        return null;
    }

    public static Block buildMasterBlock(CustomBlockEnum customBlockEnum, int textBlockIndex) { // List<Block> blocks, int templateBlockIndex
        if (CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetWaitForNightfall.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("TailorDracuetFaces", "TailorDracuet")));
            return new MasterNpcBlock(textBlockIndex, 0x021, 0x2f0, 0x00e, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetBackInTime.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("TailorDracuetConfusion", "TailorDracuet")));
            return new MasterNpcBlock(textBlockIndex, 0x023, 0x2f0, 0x00e, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetHugeCasket.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("TailorDracuetGuidance", "TailorDracuet")));
            return new MasterNpcBlock(textBlockIndex, 0x021, 0x2f0, 0x00e, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenNoCandyReferenceBlock_DracuetHTUnlocked.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("TailorDracuetAnterior", "TailorDracuet")));
            return new MasterNpcBlock(textBlockIndex, 0x021, 0x2f0, 0x00e, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_Hiner.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("Hiner")));
            return new MasterNpcBlock(textBlockIndex, 0x000, 0x2d9, 0x004, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_Moger.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("Moger")));
            return new MasterNpcBlock(textBlockIndex, 0x000, 0x2da, 0x004, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_FormerMekuriMaster.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("FormerMekuriMaster")));
            return new MasterNpcBlock(textBlockIndex, 0x000, 0x2dd, 0x004, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestZarnac.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestZarnac")));
            return new MasterNpcBlock(textBlockIndex, 0x003, 0x2dd, 0x00e, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestXanado.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestXanado")));
            return new MasterNpcBlock(textBlockIndex, 0x003, 0x2dd, 0x00e, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherGiltoriyo.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PhilosopherGiltoriyo")));
            return new MasterNpcBlock(textBlockIndex, 0x020, 0x2e6, 0x023, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestHidlyda.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestHidlyda")));
            return new MasterNpcBlock(textBlockIndex, 0x007, 0x2dd, 0x00e, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestRomancis.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestRomancis")));
            return new MasterNpcBlock(textBlockIndex, 0x009, 0x2dd, 0x00e, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestAramo.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestAramo")));
            return new MasterNpcBlock(textBlockIndex, 0x00b, 0x2dd, 0x00e, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestTriton.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestTriton")));
            return new MasterNpcBlock(textBlockIndex, 0x00b, 0x2dd, 0x00e, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestJaguarfiv.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestJaguarfiv")));
            return new MasterNpcBlock(textBlockIndex, 0x00d, 0x2dd, 0x00e, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_MrSlushfund.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("MrSlushfund")));
            return new MasterNpcBlock(textBlockIndex, 0x00f, 0x2dd, 0x00e, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_PriestAlest.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestAlest")));
            return new MasterNpcBlock(textBlockIndex, 0x00f, 0x2dd, 0x00e, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_StrayFairy.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("Strayfairy")));
            return new MasterNpcBlock(textBlockIndex, 0x00f, 0x2e3, 0x00e, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_GiantThexde.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("GiantThexde")));
            return new MasterNpcBlock(textBlockIndex, 0x011, 0x2f0, 0x00e, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherAlsedana.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PhilosopherAlsedana")));
            return new MasterNpcBlock(textBlockIndex, 0x01c, 0x2e7, 0x023, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherSamaranta.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PhilosopherSamaranta")));
            return new MasterNpcBlock(textBlockIndex, 0x01d, 0x2e8, 0x023, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestLaydoc.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestLaydoc")));
            return new MasterNpcBlock(textBlockIndex, 0x015, 0x2dd, 0x00e, blockStringData);
        }
        else if(CustomBlockEnum.HalloweenCandyReferenceBlock_PriestAshgine.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestAshgine")));
            return new MasterNpcBlock(textBlockIndex, 0x017, 0x2e5, 0x00e, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_PhilosopherFobos.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PhilosopherFobos")));
            return new MasterNpcBlock(textBlockIndex, 0x01e, 0x2e9, 0x023, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_8BitElder.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("8bitElder")));
            return new MasterNpcBlock(textBlockIndex, 0x01b, 0x2ed, 0x003, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_duplex.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("duplex")));
            return new MasterNpcBlock(textBlockIndex, 0x00f, 0x2eb, 0x01c, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_Samieru.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("Samieru")));
            return new MasterNpcBlock(textBlockIndex, 0x013, 0x2ec, 0x021, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_Naramura.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("Naramura")));
            return new MasterNpcBlock(textBlockIndex, 0x007, 0x2ea, 0x017, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_8bitFairy.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("8bitFairy")));
            return new MasterNpcBlock(textBlockIndex, 0x01b, 0x2ee, 0x00e, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_PriestMadomono.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestMadomono")));
            return new MasterNpcBlock(textBlockIndex, 0x005, 0x2dd, 0x00e, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_PriestGailious.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("PriestGailious")));
            return new MasterNpcBlock(textBlockIndex, 0x009, 0x2dd, 0x00e, blockStringData);
        }
        else if (CustomBlockEnum.HalloweenCandyReferenceBlock_NightSurfaceFairy.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("Fairy")));
            return new MasterNpcBlock(textBlockIndex, 0x022, 0x2f0, 0x003, blockStringData);
        }
        else if (CustomBlockEnum.Fools2020ReferenceBlock_MulbrukEarlyExitPrompt.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("Mulbruk")));
            return new MasterNpcBlock(textBlockIndex, 0x019, 0x2f1, 0x2710, blockStringData);
        }
        else if (CustomBlockEnum.Fools2020ReferenceBlock_MulbrukBookOfTheDead.equals(customBlockEnum)) {
            BlockStringData blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getLocationAndNpc("Mulbruk")));
            return new MasterNpcBlock(textBlockIndex, 0x019, 0x2e0, 0x00f, blockStringData);
        }

        return null;
    }

    public static Block buildNpcCountBlock(int currentNpcs, int maxNpcs) {
        Block npcCountBlock = new Block();
        List<Short> stringCharacters = FileUtils.stringToData(String.format(Translations.getText("event.halloween.npcCount"), currentNpcs, maxNpcs));
        for (Short shortCharacter : stringCharacters) {
            npcCountBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        return npcCountBlock;
    }

    public static Block buildNpcHintBlock(int hintNumber, boolean updateConversationFlag) {
        Block npcCountBlock = new Block();
        List<BlockContents> npcCountBlockContents = npcCountBlock.getBlockContents();
        if(updateConversationFlag) {
            npcCountBlockContents.add(new BlockFlagData((short)FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_HINT, (short)hintNumber));
        }

        String hintText = getHintText(hintNumber);
        String[] hintTexts = hintText.split("%s");
        List<Short> stringCharacters;
        if(hintTexts.length > 0) {
            stringCharacters = FileUtils.stringToData(hintTexts[0]);
            for (Short shortCharacter : stringCharacters) {
                npcCountBlockContents.add(new BlockSingleData(shortCharacter));
            }
        }
        npcCountBlockContents.add(new BlockColorsData((short)0x96, (short)0x32, (short)0));
        stringCharacters = FileUtils.stringToData(getLocationText(hintNumber));
        for (Short shortCharacter : stringCharacters) {
            npcCountBlockContents.add(new BlockSingleData(shortCharacter));
        }
        npcCountBlockContents.add(new BlockColorsData((short)0, (short)0, (short)0));

        stringCharacters = FileUtils.stringToData(hintTexts[hintTexts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            npcCountBlockContents.add(new BlockSingleData(shortCharacter));
        }

        return npcCountBlock;
    }

    public static Block buildFoolsEarlyExitBlock() {
        // Conversation to go to credits early.
        Block foolsEarlyExitBlock = new Block();
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit1"));
        for(Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockItemData((short)ItemConstants.SPAULDER));
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit2"));
        for(Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit3"));
        for(Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit4"));
        for(Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit5"));
        for(Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit6"));
        for(Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit7"));
        for(Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit8"));
        for(Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }

        foolsEarlyExitBlock.getBlockContents().add(new BlockPoseData(8));
        foolsEarlyExitBlock.getBlockContents().add(new BlockPoseData(9));
        foolsEarlyExitBlock.getBlockContents().add(new BlockSceneData(0)); // Scene 0 (credits)
        return foolsEarlyExitBlock;
    }

    public static Block buildFoolsExitPromptBlock(short foolsEarlyExitBlockNumber) {
        // Conversation offering to quit
        Block foolsOptionBlock = new Block();
        foolsOptionBlock.getBlockContents().add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)1)); // Can-exit flag
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exitPrompt"));
        for (Short shortCharacter : stringCharacters) {
            foolsOptionBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsOptionBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        BlockListData repeatCmd = new BlockListData((short)1);
        repeatCmd.getData().add(foolsEarlyExitBlockNumber);
        foolsOptionBlock.getBlockContents().add(repeatCmd);
        foolsOptionBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        stringCharacters = FileUtils.stringToData(Translations.getText("prompt.yes"));
        for (Short shortCharacter : stringCharacters) {
            foolsOptionBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsOptionBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        stringCharacters = FileUtils.stringToData(Translations.getText("prompt.no"));
        for (Short shortCharacter : stringCharacters) {
            foolsOptionBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsOptionBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.EndOfEntry));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.noQuit"));
        for (Short shortCharacter : stringCharacters) {
            foolsOptionBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsOptionBlock.getBlockContents().add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)0));
        foolsOptionBlock.getBlockContents().add(new BlockSingleData(BlockDataConstants.EndOfEntry));
        return foolsOptionBlock;
    }

    public static Block buildDevRoomHintBlock(boolean updateConversationFlag) {
        Block devHintBlock = new Block();
        List<BlockContents> devHintBlockContents = devHintBlock.getBlockContents();
        if(updateConversationFlag) {
            devHintBlockContents.add(new BlockFlagData((short)FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_HINT, (short)0));
        }

        String hintText = Translations.getText("event.halloween.hintDevs");
        List<Short> stringCharacters = FileUtils.stringToData(hintText);
        for (Short shortCharacter : stringCharacters) {
            devHintBlockContents.add(new BlockSingleData(shortCharacter));
        }
        return devHintBlock;
    }

    public static Block buildMulbrukHTBlock(int totalHints) {
        Block mulbrukHTBlock = new Block();
        List<BlockContents> mulbrukHTBlockContents = mulbrukHTBlock.getBlockContents();
        mulbrukHTBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));

        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.htMulbruk1"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlockContents.add(new BlockFlagData((short)FlagConstants.HT_UNLOCKED, (short)1)); // Unlock HT
        mulbrukHTBlockContents.add(new BlockFlagData((short)FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_HINT, (short)totalHints)); // Set 0xace (Mulbruk hints cycle flag) to +1
        mulbrukHTBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        String textLine = Translations.getText("event.halloween.htMulbruk2");
        String[] textParts = textLine.split("%s");
        if(textParts.length > 0) {
            stringCharacters = FileUtils.stringToData(textParts[0]);
            for (Short shortCharacter : stringCharacters) {
                mulbrukHTBlockContents.add(new BlockSingleData(shortCharacter));
            }
        }
        mulbrukHTBlockContents.add(new BlockColorsData((short)0x96, (short)0, (short)0x64));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.halloweenCostume"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlockContents.add(new BlockColorsData((short)0, (short)0, (short)0));
        stringCharacters = FileUtils.stringToData(textParts[textParts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        textLine = Translations.getText("event.halloween.htMulbruk3");
        textParts = textLine.split("%s");
        if(textParts.length > 0) {
            stringCharacters = FileUtils.stringToData(textParts[0]);
            for (Short shortCharacter : stringCharacters) {
                mulbrukHTBlockContents.add(new BlockSingleData(shortCharacter));
            }
        }
        mulbrukHTBlockContents.add(new BlockColorsData((short)0x96, (short)0x32, (short)0));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.helloweenTemple"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlockContents.add(new BlockColorsData((short)0, (short)0, (short)0));
        stringCharacters = FileUtils.stringToData(textParts[textParts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlockContents.add(new BlockSingleData(BlockDataConstants.Cls));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.htMulbruk4"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlockContents.add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlockContents.add(new BlockFlagData(FlagConstants.CONVERSATION_CANT_LEAVE, (short)0));

        return mulbrukHTBlock;
    }

    public static Block buildAllNpcsBlock() {
        Block allNpcsBlock = new Block();
        List<Short> stringCharacters;
        String textLine = String.format(Translations.getText("event.halloween.xelpudAll"), 29, 29, "%s");
        String[] textParts = textLine.split("%s");
        if(textParts.length > 0) {
            stringCharacters = FileUtils.stringToData(textParts[0]);
            for (Short shortCharacter : stringCharacters) {
                allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
            }
        }
        allNpcsBlock.getBlockContents().add(new BlockColorsData((short)0x96, (short)0x32, (short)0));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.mulbruk"));
        for (Short shortCharacter : stringCharacters) {
            allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        allNpcsBlock.getBlockContents().add(new BlockColorsData((short)0, (short)0, (short)0));
        stringCharacters = FileUtils.stringToData(textParts[textParts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        return allNpcsBlock;
    }

    public static Block buildNoCandyConversationBlock() {
        Block allNpcsBlock = new Block();
        List<Short> stringCharacters;
        String textLine = Translations.getText("event.halloween.noDracuet");
        String[] textParts = textLine.split("%s");
        if(textParts.length > 0) {
            stringCharacters = FileUtils.stringToData(textParts[0]);
            for (Short shortCharacter : stringCharacters) {
                allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
            }
        }
        allNpcsBlock.getBlockContents().add(new BlockColorsData(0x96, 0, 0x64));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.halloweenCandy"));
        for (Short shortCharacter : stringCharacters) {
            allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        allNpcsBlock.getBlockContents().add(new BlockColorsData(0, 0, 0));
        stringCharacters = FileUtils.stringToData(textParts[textParts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        return allNpcsBlock;
    }

    private static String getHintText(int hintNumber) {
        String peopleText = "";
        if(hintNumber == 1) {
            peopleText = String.format(Translations.getText("event.halloween.hintPlural"), HolidaySettings.isIncludeHellTempleNPCs() ? 4 : 3);
        }
        else if(hintNumber == 2 || hintNumber == 3 || hintNumber == 4
                || hintNumber == 8 || hintNumber == 9 || hintNumber == 11
                || hintNumber == 14 || hintNumber == 15 || hintNumber == 16) {
            peopleText = Translations.getText("event.halloween.hintSingular");
        }
        else if(hintNumber == 5 || hintNumber == 6 || hintNumber == 7
                || hintNumber == 12 || hintNumber == 13 || hintNumber == 17) {
            peopleText = String.format(Translations.getText("event.halloween.hintPlural"), 2);
        }
        else if(hintNumber == 10) {
            peopleText = String.format(Translations.getText("event.halloween.hintPlural"), 4);
        }

        return String.format(Translations.getText("event.halloween.hintTemplateMain"), "%s", peopleText);
    }

    private static String getLocationText(int hintNumber) {
        if(hintNumber == 1) {
            return Translations.getText("locations.Surface");
        }
        else if(hintNumber == 2) {
            return Translations.getText("locations.GateofGuidance");
        }
        else if(hintNumber == 3) {
            return Translations.getText("locations.MausoleumoftheGiants");
        }
        else if(hintNumber == 4) {
            return Translations.getText("locations.TempleoftheSun");
        }
        else if(hintNumber == 5) {
            return Translations.getText("locations.SpringintheSky");
        }
        else if(hintNumber == 6) {
            return Translations.getText("locations.InfernoCavern");
        }
        else if(hintNumber == 7) {
            return Translations.getText("locations.ChamberofExtinction");
        }
        else if(hintNumber == 8) {
            return Translations.getText("locations.TwinLabyrinths");
        }
        else if(hintNumber == 9) {
            return Translations.getText("locations.EndlessCorridor");
        }
        else if(hintNumber == 10) {
            return Translations.getText("locations.GateofIllusion");
        }
        else if(hintNumber == 11) {
            return Translations.getText("locations.GraveyardoftheGiants");
        }
        else if(hintNumber == 12) {
            return Translations.getText("locations.TempleofMoonlight");
        }
        else if(hintNumber == 13) {
            return Translations.getText("locations.ToweroftheGoddess");
        }
        else if(hintNumber == 14) {
            return Translations.getText("locations.TowerofRuin");
        }
        else if(hintNumber == 15) {
            return Translations.getText("locations.ChamberofBirth");
        }
        else if(hintNumber == 16) {
            return Translations.getText("locations.DimensionalCorridor");
        }
        else if(hintNumber == 17) {
            return Translations.getText("locations.GateofTime");
        }
        return "";
    }

    public static Block buildHTSkipBlock() {
        Block htExplanation = new Block();
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.htSkip"));
        for (Short shortCharacter : stringCharacters) {
            htExplanation.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        htExplanation.getBlockContents().add(new BlockSingleData((short)0x000a)); // End record
        BlockListData tabletData = new BlockListData((short)2);
        tabletData.getData().add((short)0); // Language: 0 = English; 1 = La-Mulanese; 2 = Ancient La-Mulanese; 3 = Rosetta Stone
        tabletData.getData().add((short)0); // Slate: 0 = No image; 1 = use slate00.png; 1 = use slate01.png
        htExplanation.getBlockContents().add(tabletData);

        return htExplanation;
    }

    public static void addHTSkipTablet(Screen screen, short tabletBlock) {
        Scannable tabletReadable = new Scannable(screen, 60, 400);

        tabletReadable.setTextBlock(tabletBlock);
        tabletReadable.setArg1(0);
        tabletReadable.setArg2(0);
        tabletReadable.setArg3(1);
        tabletReadable.setArg4(1);
        tabletReadable.setArg5(1);
        tabletReadable.setArg6(1);

        tabletReadable.setArg7(1);
        tabletReadable.setArg8(1);
        tabletReadable.setArg9(0);

        tabletReadable.setDimensions(40, 40);

        screen.getObjects().add(tabletReadable);

        GraphicsTextureDraw tabletGraphic = new GraphicsTextureDraw(screen, 60, 400);

        tabletGraphic.setLayer(-1);
        tabletGraphic.setImageFile("map*_1.png");
        tabletGraphic.setImageX(0);
        tabletGraphic.setImageY(0);
        tabletGraphic.setImageWidth(40);
        tabletGraphic.setImageHeight(40);
        tabletGraphic.setAnimation(0, 0, 1, 0);
        tabletGraphic.setCollision(HitTile.Air);
        tabletGraphic.setRGBAMax(0, 0, 0, 255);
        tabletGraphic.setArg23(1);

        screen.getObjects().add(tabletGraphic);

        Dais htSkipDais = new Dais(screen, 60, 420);

//        htSkipDais.setDustAppearance(0);
        htSkipDais.setFallingTime(60);
        htSkipDais.setRise(-1);
        htSkipDais.setImage(2);
//        htSkipDais.setArg4(0);
        htSkipDais.setImageX(860);
        htSkipDais.setImageY(60);
        htSkipDais.setFullWidth();
        htSkipDais.setArg8(10);
        htSkipDais.setRiseSpeed(60);

        htSkipDais.getTestByteOperations().add(new TestByteOperation(FlagConstants.HT_SOLVED_ROOM33_PILLARS, ByteOp.FLAG_EQUALS, 0));
        htSkipDais.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.HT_SOLVED_ROOM33_PILLARS, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(htSkipDais);
    }

    public static void addHTGrailWarningTablet(Screen screen, short tabletBlock) {
        Scannable tabletReadable = new Scannable(screen, 120, 400);

        tabletReadable.setTextBlock(tabletBlock);
        tabletReadable.setArg1(0);
        tabletReadable.setArg2(0);
        tabletReadable.setArg3(1);
        tabletReadable.setArg4(1);
        tabletReadable.setArg5(1);
        tabletReadable.setArg6(1);

        tabletReadable.setArg7(1);
        tabletReadable.setArg8(1);
        tabletReadable.setArg9(0);

        tabletReadable.setDimensions(40, 40);

        screen.getObjects().add(tabletReadable);

        GraphicsTextureDraw tabletGraphic = new GraphicsTextureDraw(screen, 120, 400);

        tabletGraphic.setLayer(-1);
        tabletGraphic.setImageFile("map*_1.png");
        tabletGraphic.setImageX(0);
        tabletGraphic.setImageY(0);
        tabletGraphic.setImageWidth(40);
        tabletGraphic.setImageHeight(40);
        tabletGraphic.setAnimation(0, 0, 1, 0);
        tabletGraphic.setCollision(HitTile.Air);
        tabletGraphic.setRGBAMax(0, 0, 0, 255);
        tabletGraphic.setArg23(1);

        screen.getObjects().add(tabletGraphic);
    }

    public static Block buildHTGrailWarningBlock() {
        Block htExplanation = new Block();

        List<Short> stringCharacters;
        String textLine = Translations.getText("event.halloween.htLogic");
        String[] textParts = textLine.split("%s");
        if(textParts.length > 0) {
            stringCharacters = FileUtils.stringToData(textParts[0]);
            for (Short shortCharacter : stringCharacters) {
                htExplanation.getBlockContents().add(new BlockSingleData(shortCharacter));
            }
        }
        htExplanation.getBlockContents().add(new BlockColorsData((short)0x96, (short)0, (short)0x64));
        stringCharacters = FileUtils.stringToData(Translations.getText("items.HolyGrail"));
        for (Short shortCharacter : stringCharacters) {
            htExplanation.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        htExplanation.getBlockContents().add(new BlockColorsData((short)0, (short)0, (short)0));
        stringCharacters = FileUtils.stringToData(textParts[textParts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            htExplanation.getBlockContents().add(new BlockSingleData(shortCharacter));
        }

        htExplanation.getBlockContents().add(new BlockSingleData(BlockDataConstants.EndOfEntry));
        BlockListData tabletData = new BlockListData((short)2);
        tabletData.getData().add((short)0); // Language: 0 = English; 1 = La-Mulanese; 2 = Ancient La-Mulanese; 3 = Rosetta Stone
        tabletData.getData().add((short)0); // Slate: 0 = No image; 1 = use slate00.png; 1 = use slate01.png
        htExplanation.getBlockContents().add(tabletData);
        return htExplanation;
    }

    /**
     * Add a door to twin labs (replacing Ellmac fall) where the boss ankh would be
     * @param objectContainer to add to
     */
    public static void addTwinLabsDoor(ObjectContainer objectContainer) {
        GraphicsTextureDraw doorGraphic = new GraphicsTextureDraw(objectContainer, 920, 360);

        doorGraphic.setLayer(0);
        doorGraphic.setImageFile("01effect.png");
        doorGraphic.setImageX(80);
        doorGraphic.setImageY(512);
        doorGraphic.setImageWidth(80);
        doorGraphic.setImageHeight(80);
        doorGraphic.setAnimation(0, 0, 1, 0);
        doorGraphic.setCollision(HitTile.Air);
        doorGraphic.setRGBAMax(0, 0, 0, 255);
        doorGraphic.setArg23(1);

        doorGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.PALENQUE_STATE, ByteOp.FLAG_GTEQ, 3));

        WarpDoor door = new WarpDoor(objectContainer, 940, 400);
        door.setDestination(7, 0, 0, 300, 0);

        door.getTestByteOperations().add(new TestByteOperation(FlagConstants.PALENQUE_STATE, ByteOp.FLAG_GTEQ, 3));

        objectContainer.getObjects().add(doorGraphic);
        objectContainer.getObjects().add(door);
    }

    /**
     * Timers important for marking a boss defeated. Relevant for random boss exits since
     * some bosses have these timers on a different screen.
     * @param screen
     * @param bossFlag
     * @param otherFlag
     */
    public static void addBossTimer(Screen screen, int bossFlag, int otherFlag) {
        GameObject bossTimer1 = addFramesTimer(screen, 0,
                Arrays.asList(
                        new TestByteOperation(bossFlag, ByteOp.FLAG_EQUALS, 3),
                        new TestByteOperation(otherFlag, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(
                        new WriteByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.ADD_FLAG, 1),
                        new WriteByteOperation(otherFlag, ByteOp.ASSIGN_FLAG, 1),
                        new WriteByteOperation(FlagConstants.SCORE, ByteOp.ADD_FLAG, 8)));

        if(bossFlag == FlagConstants.PALENQUE_STATE) {
            bossTimer1.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.EXTINCTION_BACKUP_JEWEL, ByteOp.ASSIGN_FLAG, 3));
        }

        // todo: this one might need to check bosses defeated count; fortunately it's only used for an old holiday rando
        addFramesTimer(screen, 0,
                Arrays.asList(new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 8)),
                Arrays.asList(
                        new WriteByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.ASSIGN_FLAG, 9),
                        new WriteByteOperation(FlagConstants.SCORE, ByteOp.ASSIGN_FLAG, 200),
                        new WriteByteOperation(FlagConstants.SURFACE_TRANSFORM_WIND_HOWLING, ByteOp.ASSIGN_FLAG, 1)));
    }

    /**
     * For marking sphinx defeated when coming from an unusual path (random transitions).
     * @param screen
     */
    public static void addSphinxRemovalTimer(Screen screen) {
        // todo: is this really meant to be seconds rather than frames?
        addSecondsTimer(screen, 4,
                Arrays.asList(
                        new TestByteOperation(FlagConstants.SPHINX_DESTROYED, ByteOp.FLAG_GTEQ, 1),
                        new TestByteOperation(FlagConstants.SPHINX_DESTROYED, ByteOp.FLAG_LT, 5)),
                Arrays.asList(
                        new WriteByteOperation(FlagConstants.SPHINX_DESTROYED, ByteOp.ASSIGN_FLAG, 5),
                        new WriteByteOperation(FlagConstants.SPHINX_DESTROYED_V2, ByteOp.ASSIGN_FLAG, 1)));
    }

    /**
     * Add 0x95 object
     * @param screen to add to
     * @param x position
     * @param y position
     */
    public static EyeOfRetribution addEyeOfDivineRetribution(Screen screen, int x, int y) {
        EyeOfRetribution eyeOfDivineRetribution = new EyeOfRetribution(screen, x, y);
        screen.getObjects().add(eyeOfDivineRetribution);
        return eyeOfDivineRetribution;
    }

    /**
     * Add 0xa9 object
     * @param screen to add to
     * @param x position
     * @param y position
     */
    public static void addPushableBlock(Screen screen, int x, int y, List<TestByteOperation> tests) {
        PushableBlock pushableBlock = new PushableBlock(screen, x, y);
        pushableBlock.setPushDamage(1);
        pushableBlock.setFallDamage(1);

        pushableBlock.getTestByteOperations().addAll(tests);

        screen.getObjects().add(pushableBlock);
    }

    /**
     * Add 0x12 object
     * @param screen to add to
     * @param x position
     * @param y position
     */
    public static void addHitbox(Screen screen, int x, int y, int width, int height, List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        Hitbox hitbox = new Hitbox(screen, x, y);
//        hitbox.setVisual(0);
        hitbox.setHitCount(1);
        hitbox.setBreakable(Hitbox.AnyDirection, Hitbox.AnyWeapon);
//        hitbox.setUpdateOnlyForCorrectWeapon();
        hitbox.setDimensions(width, height);
        hitbox.setSoundEffects(105, 104);
        hitbox.setDustDensity1(1);
        hitbox.setDustDensity2(2);

        hitbox.getTestByteOperations().addAll(tests);
        hitbox.getWriteByteOperations().addAll(updates);

        screen.getObjects().add(hitbox);
    }

    /**
     * Add 0x96 object
     */
    public static ExtendableSpikes addExtendingSpikes(ObjectContainer objectContainer, int x, int y, int flagIndex) {
        ExtendableSpikes extendingSpikes = new ExtendableSpikes(objectContainer, x, y);

        extendingSpikes.setLayer(0);
        extendingSpikes.setDirection(ExtendableSpikes.Up);
        extendingSpikes.setExtendedWidth(4);
        extendingSpikes.setExtendedLength(3);
        extendingSpikes.setActivationDelay(0);
        extendingSpikes.setArg5(100);
        extendingSpikes.setArg6(100);
        extendingSpikes.setArg7(120);
        extendingSpikes.setUpdate1Delay(120);
        extendingSpikes.setRetractDelay(0);
        extendingSpikes.setArg10(200);
        extendingSpikes.setArg11(100);
        extendingSpikes.setArg12(500);
        extendingSpikes.setUpdate2Delay(0);
        extendingSpikes.setImageFile("eveg*.png");
        extendingSpikes.setRoomNumber(0);
        extendingSpikes.setImageX(320);
        extendingSpikes.setImageY(40);
        extendingSpikes.setDx(80);
        extendingSpikes.setDy(60);
        extendingSpikes.setExtendSound(21);
        extendingSpikes.setRetractSound(41);
        extendingSpikes.setPercentDamage(20);
        extendingSpikes.setSideDamage(1);
        extendingSpikes.setArg25(20);

        extendingSpikes.addUpdates(
                new WriteByteOperation(flagIndex, ByteOp.ASSIGN_FLAG, 1),
                new WriteByteOperation(flagIndex, ByteOp.ASSIGN_FLAG, 0));

        objectContainer.getObjects().add(extendingSpikes);

        SoundEffect failPuzzleSound = new SoundEffect(objectContainer);
        failPuzzleSound.setSoundEffect(SoundEffect.ShellHornFailure);
        failPuzzleSound.setVolumeBalancePitch(120, 64, 0);
        failPuzzleSound.setPriority(25);
        failPuzzleSound.setArg8(1);
        failPuzzleSound.setFramesDelay(5);
//        failPuzzleSound.setControllerRumble(false);
        failPuzzleSound.setRumbleStrength(10);

        failPuzzleSound.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2));
        failPuzzleSound.getTestByteOperations().add(new TestByteOperation(flagIndex, ByteOp.FLAG_EQUALS, 1));

        objectContainer.getObjects().add(0, failPuzzleSound);

        return extendingSpikes;
    }

    public static void addZebuDais(ObjectContainer screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x08);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)10); // Falling speed
        obj.getArgs().add((short)-1);
        obj.getArgs().add((short)2);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)660);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)10);
        obj.getArgs().add((short)60);
        obj.setX(360);
        obj.setY(420);

        if("Zebu".equals(Settings.getCurrentGiant())) {
            obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 0));
            obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.ASSIGN_FLAG, 1));
            obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));

            addSuccessSound(screen, Arrays.asList(
                    new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2),
                    new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 1),
                    new TestByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.FLAG_EQUALS, 1)));
        }
        else {
            obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.FLAG_EQUALS, 0));
            obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1));

            addExtendingSpikes(obj.getObjectContainer(), obj.getX() - 20, obj.getY() + 20, FlagConstants.SCREEN_FLAG_9);
        }
        screen.getObjects().add(obj);
        // todo: write tests and then switch from the above code to the commented-out code below
//        Dais obj = new Dais(screen, 360, 420);
////        obj.setDustAppearance(0);
//        obj.setFallingTime(10);
//        obj.setRise(-1);
//        obj.setImage(2);
////        obj.setArg4(0);
//        obj.setImageX(660);
//        obj.setImageY(0);
//        obj.setFullWidth();
//        obj.setArg8(10);
//        obj.setRiseSpeed(60);
//
//        if("Zebu".equals(Settings.getCurrentGiant())) {
//            obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 0));
//            obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.ASSIGN_FLAG, 1));
//            obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));
//
//            addSuccessSound(screen, Arrays.asList(
//                    new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2),
//                    new TestByteOperation(FlagConstants.MAUSOLEUM_PUZZLE_ORB_CHEST, ByteOp.FLAG_EQUALS, 1),
//                    new TestByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.FLAG_EQUALS, 1)));
//        }
//        else {
//            obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.FLAG_EQUALS, 0));
//            obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1));
//
//            addExtendingSpikes(obj, FlagConstants.SCREEN_FLAG_9);
//        }
//        screen.getObjects().add(obj);
    }

    public static void addSuccessSound(ObjectContainer objectContainer, List<TestByteOperation> tests) {
        SoundEffect successSound = new SoundEffect(objectContainer);
        successSound.setSoundEffect(SoundEffect.ShellHornSuccess);
        successSound.setVolumeBalancePitch(120, 64, 0);
        successSound.setPriority(25);
        successSound.setArg8(1);
        successSound.setFramesDelay(5);
//        successSound.setControllerRumble(false);
        successSound.setRumbleStrength(10);

        successSound.getTestByteOperations().addAll(tests);

        objectContainer.getObjects().add(0, successSound);
    }

    public static void addMantraRecitedSound(ObjectContainer objectContainer, List<TestByteOperation> tests) {
        SoundEffect mantraRecitedSound = new SoundEffect(objectContainer);
        mantraRecitedSound.setSoundEffect(SoundEffect.MantraRecited);
        mantraRecitedSound.setVolumeBalancePitch(127, 64, 1000);
        mantraRecitedSound.setPriority(20);
        mantraRecitedSound.setArg8(0);
        mantraRecitedSound.setFramesDelay(0);
        mantraRecitedSound.setControllerRumble(true);
        mantraRecitedSound.setRumbleStrength(4);

        mantraRecitedSound.getTestByteOperations().addAll(tests);

        objectContainer.getObjects().add(0, mantraRecitedSound);
    }

    public static SoundEffect addShellHornFailureSound(ObjectContainer objectContainer, boolean requireShellHorn) {
        SoundEffect failPuzzleSound = new SoundEffect(objectContainer);
        failPuzzleSound.setSoundEffect(SoundEffect.ShellHornFailure);
        failPuzzleSound.setVolumeBalancePitch(120, 64, 0);
        failPuzzleSound.setPriority(25);
        failPuzzleSound.setArg8(1);
        failPuzzleSound.setFramesDelay(5);
//        failPuzzleSound.setControllerRumble(false);
        failPuzzleSound.setRumbleStrength(10);

        if(requireShellHorn) {
            failPuzzleSound.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2));
        }

        objectContainer.getObjects().add(0, failPuzzleSound);
        return failPuzzleSound;
    }

    /**
     * Add 0x98 object
     * @param screen to add to
     * @param x position
     * @param y position
     */
    public static WarpDoor addWarpDoor(Screen screen, int x, int y, int destZone, int destRoom, int destScreen, int destX, int destY, List<TestByteOperation> tests) {
        WarpDoor warpDoor = new WarpDoor(screen, x, y);
        warpDoor.setDestination(destZone, destRoom, destScreen, destX, destY);
        warpDoor.getTestByteOperations().addAll(tests);
        screen.getObjects().add(warpDoor);
        return warpDoor;
    }

    /**
     * Add MantraDetector
     * @param screen to add to
     * @param mantraNumber which mantra to recite
     */
    public static MantraDetector addMantraDetector(Screen screen, int mantraNumber) {
        MantraDetector mantraDetector = new MantraDetector(screen);
        mantraDetector.setMantraNumber(mantraNumber);
        screen.getObjects().add(0, mantraDetector);
        return mantraDetector;
    }

    /**
     * Add UseItemDetector
     * @param screen to add to
     * @param itemName to detect usage of
     */
    public static UseItemDetector addUseItemDetector(Screen screen, int x, int y, int width, int height, String itemName) {
        UseItemDetector useItemDetector = new UseItemDetector(screen, x, y);
        useItemDetector.setItem(itemName);
        useItemDetector.setWidth(width);
        useItemDetector.setHeight(height);
        screen.getObjects().add(useItemDetector);
        return useItemDetector;
    }

    /**
     * Add SnapshotsScan
     * @param screen to add to
     * @param textBlock
     * @param itemArg
     * @param highlightX
     * @param highlightY
     */
    public static SnapshotsScan addSnapshotsScan(Screen screen, int textBlock, int itemArg, int highlightX, int highlightY) {
        SnapshotsScan snapshotsScan = new SnapshotsScan(screen);
        snapshotsScan.setTextCard(textBlock);
        snapshotsScan.setInventoryWord(itemArg);
        snapshotsScan.setLocation(highlightX, highlightY);
        screen.getObjects().add(0, snapshotsScan);
        return snapshotsScan;
    }

    /**
     * Add OneWayDoor
     * @param screen to add to
     * @param x
     * @param y
     * @param direction
     */
    public static OneWayDoor addOneWayDoor(Screen screen, int x, int y, int direction) {
        OneWayDoor oneWayDoor = new OneWayDoor(screen, x, y);
        oneWayDoor.setDirection(direction);
        screen.getObjects().add(oneWayDoor);
        return oneWayDoor;
    }

    /**
     * Add DynamicWall
     * @param screen to add to
     * @param x
     * @param y
     * @param height
     */
    public static DynamicWall addDynamicWall(Screen screen, int x, int y, int height) {
        DynamicWall dynamicWall = new DynamicWall(screen, x, y);
        dynamicWall.setHeight(height);
        dynamicWall.setGraphicsFromZone(screen.getZoneIndex());
        screen.getObjects().add(dynamicWall);
        return dynamicWall;
    }

    /**
     * Add 0x2e object
     * @param screen to add to
     * @param x position
     * @param y position
     */
    public static GameObject addAmphisbaenaAnkh(Screen screen, int x, int y, int damage, List<TestByteOperation> tests) {
        GameObject amphisbaenaAnkh = new GameObject(screen);

        amphisbaenaAnkh.setId((short) 0x2e);
        amphisbaenaAnkh.setX(x);
        amphisbaenaAnkh.setY(y);

        amphisbaenaAnkh.getArgs().add((short)0); // Boss type (0=amphisbaena)
        amphisbaenaAnkh.getArgs().add((short)0); // Speed
        amphisbaenaAnkh.getArgs().add((short)4); // Health
        amphisbaenaAnkh.getArgs().add((short)damage); // Contact Damage
        amphisbaenaAnkh.getArgs().add((short)1); // Flame Speed
        amphisbaenaAnkh.getArgs().add((short)damage); // Flame Damage
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)-1);
        amphisbaenaAnkh.getArgs().add((short)-1);
        amphisbaenaAnkh.getArgs().add((short)1519);
        amphisbaenaAnkh.getArgs().add((short)0);
        amphisbaenaAnkh.getArgs().add((short)-1);
        amphisbaenaAnkh.getArgs().add((short)-1);
        amphisbaenaAnkh.getArgs().add((short)1519);
        amphisbaenaAnkh.getArgs().add((short)0);

        amphisbaenaAnkh.getTestByteOperations().addAll(tests);

        amphisbaenaAnkh.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.AMPHISBAENA_STATE, ByteOp.ASSIGN_FLAG, (byte)1));
        amphisbaenaAnkh.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.AMPHISBAENA_STATE, ByteOp.ASSIGN_FLAG, (byte)2));
        amphisbaenaAnkh.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.AMPHISBAENA_STATE, ByteOp.ASSIGN_FLAG, (byte)3));
        amphisbaenaAnkh.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.AMPHISBAENA_ANKH_PUZZLE, ByteOp.ASSIGN_FLAG, (byte)6));

        screen.getObjects().add(amphisbaenaAnkh);
        return amphisbaenaAnkh;
    }

    /**
     * Add discount lamp of time recharge station
     * @param screen to add to
     * @param x position
     * @param y position
     */
    public static void addLampStation(Screen screen, int x, int y) {
        LemezaDetector lampDetector = addLemezaDetector(screen, x, y, 2, 3,
                Arrays.asList(new TestByteOperation(FlagConstants.LAMP_OF_TIME_STATE, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(new WriteByteOperation(FlagConstants.LAMP_OF_TIME_STATE, ByteOp.ASSIGN_FLAG, (byte)1),
                        new WriteByteOperation(FlagConstants.LAMP_STATION_UNKNOWN, ByteOp.ASSIGN_FLAG, (byte)1)));
        lampDetector.setSecondsWait(1);

        GraphicsTextureDraw lampGraphic = new GraphicsTextureDraw(screen, x - 20, y - 20);
        lampGraphic.setLayer(-1);
        lampGraphic.setImageFile("02comenemy.png");
        lampGraphic.setImageX(940);
        lampGraphic.setImageY(140);
        lampGraphic.setImageWidth(80);
        lampGraphic.setImageHeight(80);
        lampGraphic.setAnimation(1, 1, 4, 0);
        lampGraphic.setCollision(HitTile.Air);
        lampGraphic.setRGBAMax(0, 0, 0, 255);
        lampGraphic.setArg23(1);

        screen.getObjects().add(lampGraphic);

        GraphicsTextureDraw lampFlame = new GraphicsTextureDraw(screen, x + 20, y + 20);
        lampFlame.setLayer(-1);
        lampFlame.setImageFile("02comenemy.png"); // Uses the mirror ghost graphic
        lampFlame.setImageX(280);
        lampFlame.setImageY(112);
        lampFlame.setImageWidth(20);
        lampFlame.setImageHeight(20);
        lampFlame.setAnimation(1, 1, 4, 0);
        lampFlame.setCollision(HitTile.Air);
        lampFlame.setRGBAMax(0, 0, 0, 255);
        lampFlame.setArg23(1);

        lampFlame.getTestByteOperations().add(new TestByteOperation(FlagConstants.LAMP_OF_TIME_STATE, ByteOp.FLAG_EQUALS, 0));
        screen.getObjects().add(lampFlame);
    }

    public static void addShopObjects(ConversationDoor conversationDoor, ShopInventory shopInventory, Short customBlockIndex) {
        short blockNumber = conversationDoor.getBlockNumber();
        if(blockNumber == BlockConstants.ShopBlockYiegahKungfu) {
            ConversationDoorUpdates.addLittleBrotherScreenObjects(conversationDoor, shopInventory);
        }

        addObjectsForShopItem(conversationDoor, shopInventory.getItem1(), customBlockIndex);
        addObjectsForShopItem(conversationDoor, shopInventory.getItem2(), customBlockIndex);
        addObjectsForShopItem(conversationDoor, shopInventory.getItem3(), customBlockIndex);
    }

    private static void addObjectsForShopItem(ConversationDoor conversationDoor, ShopInventoryData shopInventoryData, Short customBlockIndex) {
        if(shopInventoryData.getInventoryArg() == ItemConstants.SACRED_ORB && customBlockIndex != null) {
            addTransformedShop(conversationDoor, customBlockIndex, shopInventoryData.getWorldFlag());

        }
        if(Settings.isAutomaticMantras() && shopInventoryData.getInventoryArg() == ItemConstants.KEY_SWORD) {
            AddObject.addAutomaticMantrasTimer(conversationDoor.getObjectContainer());
        }
        if(shopInventoryData.getInventoryArg() == ItemConstants.MEDICINE_OF_THE_MIND_YELLOW
                || shopInventoryData.getInventoryArg() == ItemConstants.MEDICINE_OF_THE_MIND_GREEN
                || shopInventoryData.getInventoryArg() == ItemConstants.MEDICINE_OF_THE_MIND_RED) {
            AddObject.addMedicineStatueTimer(conversationDoor.getObjectContainer(), shopInventoryData.getWorldFlag());
        }
        if(shopInventoryData.getWorldFlag() == FlagConstants.WF_MAP_SHRINE) {
            AddObject.addShrineMapSoundEffect(conversationDoor.getObjectContainer());
        }
    }

    private static void addTransformedShop(ConversationDoor conversationDoor, Short customBlockIndex, Short transformShopFlag) {
        ConversationDoor newConversationDoor = new ConversationDoor(conversationDoor);
        newConversationDoor.setBlockNumber(customBlockIndex);
        newConversationDoor.addTests(new TestByteOperation(transformShopFlag, ByteOp.FLAG_EQUALS, 2));
        conversationDoor.getObjectContainer().getObjects().add(newConversationDoor);

        conversationDoor.addTests(new TestByteOperation(transformShopFlag, ByteOp.FLAG_LT, 2));
    }

    public static Trapdoor addTrapdoor(Screen screen, int x, int y) {
        Trapdoor trapdoor = new Trapdoor(screen, x, y);
        trapdoor.setOpenFlagIndex(0);
        trapdoor.setFrameDelayOrFlagValue(1);
        trapdoor.setFramesOpenAfterAnimation(60);
        trapdoor.setImageCoordinates(screen.getZoneIndex());
        screen.getObjects().add(trapdoor);

        return trapdoor;
    }

    public static Spikes addSpikes(Screen screen, int x, int y, int width, int height, int spikeDirection, TestByteOperation... tests) {
        Spikes spikes = new Spikes(screen, x, y);
        spikes.setFacing(spikeDirection);
        spikes.setWidth(width);
        spikes.setHeight(height);
        spikes.setDamage(false, 15);
        spikes.addTests(tests);
        screen.getObjects().add(spikes);

        addSpikesGraphic(spikes, spikeDirection);

        return spikes;
    }

    public static PressurePlate addPressurePlate(Screen screen, int x, int y, int detectionType) {
        PressurePlate pressurePlate = new PressurePlate(screen, x, y);
        pressurePlate.setGraphicsFromZone(screen.getZoneIndex());
        pressurePlate.setDetectionType(detectionType);
        screen.getObjects().add(pressurePlate);

        return pressurePlate;
    }

    public static PressurePlate addPressurePlate(Screen screen, int x, int y) {
        PressurePlate pressurePlate = new PressurePlate(screen, x, y);
        pressurePlate.setGraphicsFromZone(screen.getZoneIndex());
        pressurePlate.setDetectionType(PressurePlate.Detection_Lemeza);
        screen.getObjects().add(pressurePlate);

        return pressurePlate;
    }

    public static void addHalloweenEmailTimers(ObjectContainer objectContainer, int maxCurseLevel) {
        addHalloweenEmailTimer(objectContainer, ValueConstants.ENEMY_THRESHOLD1, FlagConstants.MAIL_01).addUpdates(
                new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_BAT_LEVEL, ByteOp.ASSIGN_FLAG, 1),
                new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_SKELETON_LEVEL, ByteOp.ASSIGN_FLAG, 1)
        );

        addHalloweenEmailTimer(objectContainer, ValueConstants.GHOST_WITCH_THRESHOLD1, FlagConstants.MAIL_02);

        addHalloweenEmailTimer(objectContainer, ValueConstants.TRAPDOOR_THRESHOLD, FlagConstants.MAIL_03);

        addHalloweenEmailTimer(objectContainer, ValueConstants.TIME_THRESHOLD, FlagConstants.MAIL_04);

        addHalloweenEmailTimer(objectContainer, ValueConstants.SPIKES_THRESHOLD, FlagConstants.MAIL_05);

        addHalloweenEmailTimer(objectContainer, ValueConstants.BAT_RETRIBUTION_THRESHOLD, FlagConstants.MAIL_06).addUpdates(
                new WriteByteOperation(FlagConstants.GUIDANCE_BATS_KILLED_COUNT, ByteOp.ASSIGN_FLAG, 0),
                new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_BAT_RETRIBUTION, ByteOp.ASSIGN_FLAG, 1));

        addHalloweenEmailTimer(objectContainer, ValueConstants.POVERTY_CHEST_THRESHOLD, FlagConstants.MAIL_07);

        addHalloweenEmailTimer(objectContainer, ValueConstants.STRONGER_ENEMIES_THRESHOLD, FlagConstants.MAIL_08).addUpdates(
                new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_BAT_LEVEL, ByteOp.ASSIGN_FLAG, 2),
                new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_SKELETON_LEVEL, ByteOp.ASSIGN_FLAG, 2));

        addHalloweenEmailTimer(objectContainer, ValueConstants.NON_WALKABLE_SPIKE_THRESHOLD, FlagConstants.MAIL_09);
        addHalloweenEmailTimer(objectContainer, ValueConstants.GHOST_THRESHOLD3, FlagConstants.MAIL_10);
        addHalloweenEmailTimer(objectContainer, maxCurseLevel, FlagConstants.MAIL_11);
    }

    private static GameObject addHalloweenEmailTimer(ObjectContainer objectContainer, int curseThreshold, int mailFlagIndex) {
        return addFramesTimer(objectContainer, 3,
                Arrays.asList(
                        new TestByteOperation(mailFlagIndex, ByteOp.FLAG_EQUALS, 0),
                        new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_EQUALS, curseThreshold)),
                Arrays.asList(
                        new WriteByteOperation(mailFlagIndex, ByteOp.ASSIGN_FLAG, 1),
                        new WriteByteOperation(FlagConstants.MAIL_COUNT, ByteOp.ADD_FLAG, 1)));
    }

    public static void addMapCountTimer(ObjectContainer objectContainer, int mapWorldFlag) {
        int mapCountFlag = FlagConstants.getMapCountUpdatedFlag(mapWorldFlag);
        addFramesTimer(objectContainer, 0, Arrays.asList(
                new TestByteOperation(mapCountFlag, ByteOp.FLAG_EQUALS, 0),
                new TestByteOperation(mapWorldFlag, ByteOp.FLAG_EQUALS, 2)),
                Arrays.asList(
                        new WriteByteOperation(mapCountFlag, ByteOp.ASSIGN_FLAG, 1),
                        new WriteByteOperation(FlagConstants.ACHIEVEMENT_MAP_COUNT, ByteOp.ADD_FLAG, 1)));
    }

    /**
     * Add an item give to every screen in the game, with the appropriate coordinates.
     * @param screen to add to
     * @param inventoryArg item to give
     * @param itemGiveTriggerFlag flag to test for determining when to give the item
     * @param itemGiveTriggerFlagValue value which triggers the item to be given
     * @param itemGiveStateFlag flag to update after receiving the item
     * @param itemGiveStateFlagValue value to set the flag to
     */
    public static void addCustomItemGives(Screen screen, int inventoryArg,
                                          int itemGiveTriggerFlag, int itemGiveTriggerFlagValue,
                                          int itemGiveStateFlag, int itemGiveStateFlagValue) {
        addToAllScreens(screen, new AddToAllScreens() {
            @Override
            public void addToScreen(Screen screen, int leftmostX, int topmostY) {
                AddObject.addItemGive(screen, leftmostX, topmostY, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        });
    }

    /**
     * Add an item give to every screen in the game, with the appropriate coordinates.
     * @param screen to add to
     * @param addToAllScreens receives screen reference and coordinates and adds the needed object(s)
     */
    public static void addToAllScreens(Screen screen, AddToAllScreens addToAllScreens) {
        int zoneIndex = screen.getZoneIndex();
        int roomIndex = screen.getRoomIndex();
        int screenIndex = screen.getScreenIndex();
        if(zoneIndex == 0) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 1) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 0 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 5 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 10 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 10 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 10 && screenIndex == 3) {
                addToAllScreens.addToScreen(screen, 1920, 0);
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 11 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 2) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 5 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 7 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
        }
        else if(zoneIndex == 3) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 4 && screenIndex == 3) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 4 && screenIndex == 4) {
                addToAllScreens.addToScreen(screen, 640, 480);
            }
            else if(roomIndex == 4 && screenIndex == 5) {
                addToAllScreens.addToScreen(screen, 1280, 480);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
        }
        else if(zoneIndex == 4) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 0 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 3 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 3 && screenIndex == 3) {
                addToAllScreens.addToScreen(screen, 0, 1440);
            }
            else if(roomIndex == 3 && screenIndex == 4) {
                addToAllScreens.addToScreen(screen, 0, 1920);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 12 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 13 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 17 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 24 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 35 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
        }
        else if (zoneIndex == 5) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 2 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 6) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 7) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 3 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 10 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 11 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 11 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 12 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 12 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 12 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 13 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 13 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 14 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 14 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 15 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 15 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 15 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 16 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 16 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 16 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
        }
        else if(zoneIndex == 8) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 2 && screenIndex == 3) {
                addToAllScreens.addToScreen(screen, 1920, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 3 && screenIndex == 3) {
                addToAllScreens.addToScreen(screen, 1920, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 4 && screenIndex == 3) {
                addToAllScreens.addToScreen(screen, 1920, 0);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 5 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 5 && screenIndex == 3) {
                addToAllScreens.addToScreen(screen, 1920, 0);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 12 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
        }
        else if(zoneIndex == 9) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 10) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 2 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 7 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
        }
        else if(zoneIndex == 11) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 0 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 4 && screenIndex == 3) {
                addToAllScreens.addToScreen(screen, 0, 1440);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 12) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 480);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 4 && screenIndex == 3) {
                addToAllScreens.addToScreen(screen, 0, 1440);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
        }
        else if(zoneIndex == 13) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 2 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 6 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 6 && screenIndex == 3) {
                addToAllScreens.addToScreen(screen, 0, 1440);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 7 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 14 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 15 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 16 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 26 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
        }
        else if(zoneIndex == 14) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 0 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
        }
        else if(zoneIndex == 15) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
        }
        else if(zoneIndex == 16) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
        }
        else if(zoneIndex == 17) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 10 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
        }
        else if(zoneIndex == 18) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 19) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 20) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 1 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 21) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 22) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 0 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 1280, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 5 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 960);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 10 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 10 && screenIndex == 2) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 10 && screenIndex == 3) {
                addToAllScreens.addToScreen(screen, 640, 480);
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 11 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 23) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 10 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 12 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 12 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 13 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 14 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 14 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 15 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 15 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 16 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 17 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 18 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 18 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
            else if(roomIndex == 19 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 20 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 21 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 22 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 22 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 640, 0);
            }
        }
        else if(zoneIndex == 24) {
            if(roomIndex == 0 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                addToAllScreens.addToScreen(screen, 0, 0);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                addToAllScreens.addToScreen(screen, 0, 480);
            }
        }
        else if(zoneIndex == 25) {
            addToAllScreens.addToScreen(screen, 0, 480 * screenIndex);
        }
    }
}
