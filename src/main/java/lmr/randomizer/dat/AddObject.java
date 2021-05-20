package lmr.randomizer.dat;

import lmr.randomizer.*;
import lmr.randomizer.dat.shop.BlockCmdSingle;
import lmr.randomizer.dat.shop.BlockStringData;
import lmr.randomizer.dat.shop.MasterNpcBlock;
import lmr.randomizer.dat.shop.ShopBlock;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.GameObjectId;
import lmr.randomizer.update.LocationCoordinateMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AddObject {
    private AddObject() { }

    private static ObjectContainer xelpudScreen;
    private static ObjectContainer mulbrukScreen;
    private static ObjectContainer littleBrotherShopScreen;
    private static ObjectContainer dimensionalExitScreen;

    public static void setXelpudScreen(ObjectContainer xelpudScreen) {
        AddObject.xelpudScreen = xelpudScreen;
    }

    public static void setMulbrukScreen(ObjectContainer mulbrukScreen) {
        AddObject.mulbrukScreen = mulbrukScreen;
    }

    public static void setLittleBrotherShopScreen(ObjectContainer littleBrotherShopScreen) {
        AddObject.littleBrotherShopScreen = littleBrotherShopScreen;
    }

    public static void setDimensionalExitScreen(ObjectContainer dimensionalExitScreen) {
        AddObject.dimensionalExitScreen = dimensionalExitScreen;
    }

    public static void clearObjects() {
        xelpudScreen = null;
        mulbrukScreen = null;
        littleBrotherShopScreen = null;
        dimensionalExitScreen = null;
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
        FloatingItem obj = new FloatingItem(screen, x, y);
        obj.setInventoryWord(itemArg);
        obj.setRealItem(realItem);
        obj.getTestByteOperations().addAll(tests);
        obj.getWriteByteOperations().addAll(updates);
        screen.getObjects().add(obj);
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
        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAIL_43, ByteOp.ASSIGN_FLAG, 1));
        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MAIL_COUNT, ByteOp.ADD_FLAG, 1));

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
        obj.getTestByteOperations().add(new TestByteOperation(FlagConstants.TWINS_POISON, ByteOp.FLAG_NOT_EQUAL, 0));

        obj.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.TWINS_POISON, ByteOp.ASSIGN_FLAG, 0));

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
     * Add timer for failed Flail Whip puzzle.
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
     * Add replacement timer for Xelpud dialogue state to compensate for removed 0x07c flag in intro conversation
     * @param screen the screen to add the objects to
     */
    public static void addXelpudIntroTimer(Screen screen) {
        FlagTimer timer = new FlagTimer(screen);

        timer.getTestByteOperations().add(new TestByteOperation(FlagConstants.XELPUD_CONVERSATION_INTRO, ByteOp.FLAG_EQUALS, 1));
        timer.getTestByteOperations().add(new TestByteOperation(FlagConstants.XELPUD_CONVERSATION_GENERAL, ByteOp.FLAG_EQUALS, 0));

        timer.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.XELPUD_CONVERSATION_GENERAL, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(0, timer);
    }

    /**
     * Add a backup door from Chamber of Extinction to untransformed Shrine of the Mother.
     * @param screen the screen to add the objects to
     */
    public static void addLowerUntrueShrineBackupDoor(Screen screen) {
        addWarpDoor(screen, 260, 800,9, 8, 1, 300, 320,
                Arrays.asList(new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 9)));

        GraphicsTextureDraw backupShrineDoorGraphic = new GraphicsTextureDraw(screen, 240, 760);

        backupShrineDoorGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 9));

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
    public static void addUpperUntrueShrineBackupDoor(Screen screen) {
        addWarpDoor(screen, 340, 80,9, 0, 0, 340, 92,
                Arrays.asList(new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 9)));

        GraphicsTextureDraw backupShrineDoorGraphic = new GraphicsTextureDraw(screen, 320, 40);

        backupShrineDoorGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 9));

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
    public static void addSealUntrueShrineBackupDoor(Screen screen) {
        addWarpDoor(screen, 500, 400,9, 9, 0, 300, 332,
                Arrays.asList(new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 9)));

        GraphicsTextureDraw backupShrineDoorGraphic = new GraphicsTextureDraw(screen, 480, 360);

        backupShrineDoorGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 9));

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

            TestByteOperation itemGiveTest = new TestByteOperation();
            itemGiveTest.setIndex(gameObjectId.getWorldFlag());
            itemGiveTest.setValue((byte) 0);
            itemGiveTest.setOp(ByteOp.FLAG_EQUALS);
            itemGive.getTestByteOperations().add(itemGiveTest);

            WriteByteOperation itemGiveUpdate = new WriteByteOperation();
            itemGiveUpdate.setIndex(gameObjectId.getWorldFlag());
            itemGiveUpdate.setValue((byte) 2);
            itemGiveUpdate.setOp(ByteOp.ASSIGN_FLAG);
            itemGive.getWriteByteOperations().add(itemGiveUpdate);
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
        GraphicsTextureDraw backupFishNewDoorGraphic = new GraphicsTextureDraw(objectContainer, 180, 1520);
        backupFishNewDoorGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.FISH_SHOP_UNLOCKS, ByteOp.FLAG_EQUALS, 3));
        backupFishNewDoorGraphic.getTestByteOperations().add(new TestByteOperation(FlagConstants.MOTHER_STATE, ByteOp.FLAG_NOT_EQUAL, 3));

        backupFishNewDoorGraphic.setLayer(-1);
        backupFishNewDoorGraphic.setImageFile("map*_1.png");
        backupFishNewDoorGraphic.setImageX(260);
        backupFishNewDoorGraphic.setImageY(0);
        backupFishNewDoorGraphic.setImageWidth(40);
        backupFishNewDoorGraphic.setImageHeight(40);
        backupFishNewDoorGraphic.setAnimation(0, 1, 0, 0);
        backupFishNewDoorGraphic.setCollision(HitTile.Air);
        backupFishNewDoorGraphic.setRGBAMax(0, 0, 0, 255);
//        backupFishNewDoorGraphic.setArg23(1); // todo: for some reason this was 0?

        objectContainer.getObjects().add(backupFishNewDoorGraphic);
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

    public static GameObject addAnimatedDoorTimerAndSound(ObjectContainer objectContainer, int bossFlag, int gateFlag) {
        FlagTimer doorTimer = new FlagTimer(objectContainer);
        doorTimer.setDelaySeconds(1);

        doorTimer.getTestByteOperations().add(new TestByteOperation(bossFlag, ByteOp.FLAG_GTEQ, 3));
        doorTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_BRONZE_MIRROR, ByteOp.FLAG_EQUALS, 2));
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
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)600, (short)160, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)680, (short)160, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY());
        }
        else if(zoneIndex == 1) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)900, (short)60, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 2) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)0, (short)280, (short)580, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)0, (short)360, (short)580, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY());
        }
        else if(zoneIndex == 3) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)700, (short)80, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 5) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)940, (short)0, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 6) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)940, (short)120, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 10) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)620, (short)0, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 11) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)940, (short)120, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 12) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)540, (short)80, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 13) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)940, (short)220, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 14) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)540, (short)0, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
        else if(zoneIndex == 15) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)340, (short)0, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)1, (short)460, (short)60, (short)80, (short)40, backsideDoor.getX() - 20, backsideDoor.getY());
        }
        else if(zoneIndex == 19) {
            addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, (short)0, (short)-1, (short)760, (short)512, (short)80, (short)80, backsideDoor.getX() - 20, backsideDoor.getY() - 40);
        }
    }

    private static void addMirrorCoverGraphic(GameObject backsideDoor, int mirrorCoverFlag, short layer, short fileEnum, short imageX, short imageY, short width, short height, int xPos, int yPos) {
        GraphicsTextureDraw mirrorCoverGraphic = new GraphicsTextureDraw(backsideDoor.getObjectContainer(), xPos, yPos);
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

        mirrorCoverGraphic.addTests(new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0));

        backsideDoor.getObjectContainer().getObjects().add(mirrorCoverGraphic);
    }

    public static void addGrailToggle(ObjectContainer objectContainer, boolean enableGrail, TestByteOperation... tests) {
        if(objectContainer == null) {
            objectContainer = dimensionalExitScreen;
        }

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

    public static void addLittleBrotherShopTimer(short shopItemFlag) {
        // Sets item world flags from 1 to 2, since one of the shop flags is taken up by checking the Big Brother shop trigger.
        addFramesTimer(littleBrotherShopScreen, 0,
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
     * Add Diary updated timer to Xelpud's screen.
     * @param objectContainer the screen to add the objects to
     */
    public static void addDiaryTalismanConversationTimers(ObjectContainer objectContainer) {
        // Timer to trigger Xelpud Diary conversation (gives Mulana Talisman) if you enter his screen with the Diary.
        addFramesTimer(objectContainer, 0,
                Arrays.asList(
                        new TestByteOperation(FlagConstants.WF_DIARY, ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(FlagConstants.XELPUD_CONVERSATION_DIARY_FOUND, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(new WriteByteOperation(FlagConstants.XELPUD_CONVERSATION_DIARY_FOUND, ByteOp.ASSIGN_FLAG, 1)));

        // Timer to trigger Xelpud Talisman conversation (allows Diary chest appearance) if you enter his screen with the Talisman.
        addFramesTimer(objectContainer, 0,
                Arrays.asList(
                        new TestByteOperation(FlagConstants.WF_TALISMAN, ByteOp.FLAG_EQUALS, 2),
                        new TestByteOperation(FlagConstants.XELPUD_CONVERSATION_TALISMAN_FOUND, ByteOp.FLAG_EQUALS, 0),
                        new TestByteOperation(FlagConstants.XELPUD_CONVERSATION_GENERAL, ByteOp.FLAG_GTEQ, 1)),
                Arrays.asList(new WriteByteOperation(FlagConstants.XELPUD_CONVERSATION_TALISMAN_FOUND, ByteOp.ASSIGN_FLAG, 1)));
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
    public static void addNoItemSoundEffect(ObjectContainer objectContainer, Integer newWorldFlag, Integer screenFlag) {
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

    public static void addBat(ObjectContainer objectContainer, int xPos, int yPos, int screenFlag) {
        Bat bat = new Bat(objectContainer, xPos, yPos);
        bat.setFrontsideBat(true);
        bat.setInitiallyFlying(true);
        bat.setDropType(1);
        bat.setArg2(2);
        bat.setDamage(3);

        bat.addTests(new TestByteOperation(screenFlag, ByteOp.FLAG_EQUALS, 1));

        objectContainer.getObjects().add(bat);
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
        Explosion explosion = new Explosion(objectContainer, 640 * (xPos / 640), 480 * (yPos / 480));
        explosion.setSize(640);
        explosion.setPercentDamage(60);
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
        Pot addedPot = new Pot(screen, x, y);
        addedPot.setDrops(dropType, dropQuantity);
        addedPot.setFlag(-1, 1);
        addedPot.setPotGraphic(graphic);
        addedPot.setSoundEffects(105, 35, 17);
        addedPot.setPitchShift(0);

        addedPot.getTestByteOperations().addAll(tests);
        addedPot.getWriteByteOperations().addAll(updates);

        screen.getObjects().add(addedPot);
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

        if (Settings.isFools2020Mode()) {
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
    public static void addMedicineStatueTimer(ObjectContainer screen) {
        addFramesTimer(screen, 0,
                Arrays.asList(
                        new TestByteOperation(FlagConstants.WRONG_COLOR_MEDICINE, ByteOp.FLAG_EQUALS, 0),
                        new TestByteOperation(FlagConstants.MEDICINE_SOLVED, ByteOp.FLAG_EQUALS, 0)),
                Arrays.asList(
                        new WriteByteOperation(FlagConstants.MEDICINE_SOLVED, ByteOp.ASSIGN_FLAG, 1),
                        new WriteByteOperation(FlagConstants.WRONG_COLOR_MEDICINE, ByteOp.ASSIGN_FLAG, 1)));
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
        if("xmailer.exe".equals(chestLocation) || "Mulana Talisman".equals(chestLocation)) {
            // Xelpud location, but no object with flags to update.
            addSpecialItemObjects(xelpudScreen, chestContents);
            return true;
        }
        else if("Book of the Dead".equals(chestLocation)) {
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
            AddObject.addMedicineStatueTimer(objectContainer);
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

    public static GameObject addWarp(Screen screen, int warpX, int warpY, int width, int height, int destZone, int destRoom, int destScreen, int destX, int destY) {
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

        if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
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

        if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
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

    public static void addHalloweenGhosts(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex != 0 && zoneIndex != 7) {
            // Guidance can't have ghosts because of red skeletons.
            // Twin labs can't have ghosts because of witches.
//            if(zoneIndex == 3 && roomIndex == 8 && screenIndex == 0) {
//                // Ellmac
//                AddObject.addGhostSpawner(screen).getTestByteOperations().add(new TestByteOperation(FlagConstants.BAHAMUT_STATE, ByteOp.FLAG_EQUALS, 0));
//                AddObject.addGhostSpawner(screen).getTestByteOperations().add(new TestByteOperation(FlagConstants.BAHAMUT_STATE, ByteOp.FLAG_GT, 2));
//                AddObject.addGhostSpawner(screen).getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_1, ByteOp.FLAG_EQUALS, 1));
//                AddObject.addTimer(screen, 10,
//                        Arrays.asList(new TestByteOperation(FlagConstants.BAHAMUT_STATE, ByteOp.FLAG_EQUALS, 2)),
//                        Arrays.asList(new WriteByteOperation(FlagConstants.SCREEN_FLAG_1, ByteOp.ASSIGN_FLAG, 1)));
//            }
//            if(zoneIndex == 4 && roomIndex == 4 && screenIndex == 0) {
//                // Bahamut
//                AddObject.addGhostSpawner(screen).getTestByteOperations().add(new TestByteOperation(FlagConstants.BAHAMUT_STATE, ByteOp.FLAG_EQUALS, 0));
//                AddObject.addGhostSpawner(screen).getTestByteOperations().add(new TestByteOperation(FlagConstants.BAHAMUT_STATE, ByteOp.FLAG_GT, 2));
//                AddObject.addGhostSpawner(screen).getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_1, ByteOp.FLAG_EQUALS, 1));
//                AddObject.addTimer(screen, 10,
//                        Arrays.asList(new TestByteOperation(FlagConstants.BAHAMUT_STATE, ByteOp.FLAG_EQUALS, 2)),
//                        Arrays.asList(new WriteByteOperation(FlagConstants.SCREEN_FLAG_1, ByteOp.ASSIGN_FLAG, 1)));
//            }
//            else {
            if(zoneIndex == 2) {
//                if(roomIndex == 3 && screenIndex != 0) {
                AddObject.addGhostSpawner(screen, 120);
//                }
//                else if(roomIndex == 5 && screenIndex != 1) {
//                    AddObject.addGhostSpawner(screen, 120);
//                }
//                else if(roomIndex == 9 && screenIndex != 0) {
//                    AddObject.addGhostSpawner(screen, 120);
//                }
//                else if(roomIndex != 7 && roomIndex != 8) {
//                    AddObject.addGhostSpawner(screen, 120);
//                }
            }
            else if(zoneIndex == 19) {
                if(roomIndex == 0 && screenIndex != 0) {
                    AddObject.addGhostSpawner(screen, 120);
                }
                else if(roomIndex != 1) {
                    AddObject.addGhostSpawner(screen, 120);
                }
            }
            else if(zoneIndex == 23) {
                if(roomIndex != 22 || screenIndex != 1) {
                    // No ghosts in The Boss's room.
                    AddObject.addGhostSpawner(screen, 240);
                }
            }
            else if(zoneIndex == 24) {
                AddObject.addGhostSpawner(screen, 240);
            }
            else {
                AddObject.addGhostSpawner(screen, 120);
            }
        }
    }

    public static void addGhostSpawner(Screen screen, int spawnRate) {
        GhostSpawner ghostSpawner = new GhostSpawner(screen);
        ghostSpawner.setSpawnRate(spawnRate);
        ghostSpawner.setMaxGhosts(3);
        ghostSpawner.setArg2(0);

        if(screen.getZoneIndex() == 23) {
            ghostSpawner.setGhostSpeedAndDropType(0);
        }
        else {
            ghostSpawner.setGhostSpeedAndDropType(1);
        }
        ghostSpawner.setGhostHealth(1);
        ghostSpawner.setGhostDamageAndSoul(2);
        ghostSpawner.setArg6(3);

        screen.getObjects().add(0, ghostSpawner);
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

    public static GameObject addGuidanceShop(Screen screen) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 240, 380);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(200);
        graphic.setImageY(100);
        graphic.setImageWidth(40);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 240, 400);
        shop.setShopDefaults();

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addMausoleumShop(Screen screen) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 300, 240);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(320);
        graphic.setImageY(212);
        graphic.setImageWidth(40);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 300, 240);
        shop.setShopDefaults();

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addInfernoShop(Screen screen) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 220, 60);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(525);
        graphic.setImageY(40);
        graphic.setImageWidth(70);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 240, 80);
        shop.setShopDefaults();

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addIllusionShop(Screen screen) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 220, 80);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(940);
        graphic.setImageY(172);
        graphic.setImageWidth(40);
        graphic.setImageHeight(50);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 220, 80);
        shop.setShopDefaults();

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addTwinLabsFrontShop(Screen screen) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 880, 380);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(500);
        graphic.setImageY(200);
        graphic.setImageWidth(80);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 900, 400);
        shop.setShopDefaults();

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addTwinLabsBackShop(Screen screen) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 920, 220);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(500);
        graphic.setImageY(200);
        graphic.setImageWidth(80);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 940, 240);
        shop.setShopDefaults();

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addEndlessShop(Screen screen) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 500, 40);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(540);
        graphic.setImageY(40);
        graphic.setImageWidth(80);
        graphic.setImageHeight(80);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 520, 80);
        shop.setShopDefaults();

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addGraveyardShop(Screen screen) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 880, 140);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(560);
        graphic.setImageY(280);
        graphic.setImageWidth(50);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 880, 160);
        shop.setShopDefaults();

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addGoddessShop(Screen screen) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 940, 300);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(660);
        graphic.setImageY(520);
        graphic.setImageWidth(40);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 940, 320);
        shop.setShopDefaults();

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addRuinShop(Screen screen) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 200, 380);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(280);
        graphic.setImageY(180);
        graphic.setImageWidth(40);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 200, 400);
        shop.setShopDefaults();

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addBirthStartStuff(Screen screen) {
        GraphicsTextureDraw shopGraphic = new GraphicsTextureDraw(screen, 140, 300);

        shopGraphic.setLayer(0);
        shopGraphic.setImageFile("map*_1.png");
        shopGraphic.setImageX(40);
        shopGraphic.setImageY(100);
        shopGraphic.setImageWidth(40);
        shopGraphic.setImageHeight(60);
        shopGraphic.setAnimation(0, 0, 1, 0);
        shopGraphic.setCollision(HitTile.Air);
        shopGraphic.setRGBAMax(0, 0, 0, 255);
        shopGraphic.setArg23(1);

        screen.getObjects().add(shopGraphic);

        ConversationDoor shop = new ConversationDoor(screen, 140, 320);
        shop.setShopDefaults();

        screen.getObjects().add(shop);

        GraphicsTextureDraw coverGraphic = new GraphicsTextureDraw(screen, 320, 300);

        coverGraphic.setLayer(0);
        coverGraphic.setImageFile("map*_1.png");
        coverGraphic.setImageX(600);
        coverGraphic.setImageY(140);
        coverGraphic.setImageWidth(60);
        coverGraphic.setImageHeight(60);
        coverGraphic.setAnimation(0, 0, 1, 0);
        coverGraphic.setCollision(HitTile.Air);
        coverGraphic.setRGBAMax(0, 0, 0, 255);
        coverGraphic.setArg23(1);

        screen.getObjects().add(coverGraphic);

        GraphicsTextureDraw tabletGraphic1 = new GraphicsTextureDraw(screen, 200, 380);

        tabletGraphic1.setLayer(0);
        tabletGraphic1.setImageFile("map*_1.png");
        tabletGraphic1.setImageX(320);
        tabletGraphic1.setImageY(0);
        tabletGraphic1.setImageWidth(40);
        tabletGraphic1.setImageHeight(40);
        tabletGraphic1.setAnimation(0, 0, 1, 0);
        tabletGraphic1.setCollision(HitTile.Air);
        tabletGraphic1.setRGBAMax(0, 0, 0, 255);
        tabletGraphic1.setArg23(1);

        screen.getObjects().add(tabletGraphic1);

        GraphicsTextureDraw tabletGraphic2 = new GraphicsTextureDraw(screen, 200, 420);

        tabletGraphic2.setLayer(0);
        tabletGraphic2.setImageFile("map*_1.png");
        tabletGraphic2.setImageX(360);
        tabletGraphic2.setImageY(0);
        tabletGraphic2.setImageWidth(40);
        tabletGraphic2.setImageHeight(20);
        tabletGraphic2.setAnimation(0, 0, 1, 0);
        tabletGraphic2.setCollision(HitTile.Air);
        tabletGraphic2.setRGBAMax(0, 0, 0, 255);
        tabletGraphic2.setArg23(1);

        screen.getObjects().add(tabletGraphic2);

        return shop;
    }

    public static GameObject addRetroSurfaceShop(Screen screen) {
        GraphicsTextureDraw tent = new GraphicsTextureDraw(screen, 480,200);

        tent.setLayer(0);
        tent.setImageFile("map*_1.png");
        tent.setImageX(0);
        tent.setImageY(120);
        tent.setImageWidth(80);
        tent.setImageHeight(40);
        tent.setAnimation(0, 0, 1, 0);
        tent.setCollision(HitTile.Air);
        tent.setRGBAMax(0, 0, 0, 255);
        tent.setArg23(1);

        screen.getObjects().add(tent);

        GraphicsTextureDraw tent2 = new GraphicsTextureDraw(screen, 480, 240);

        tent2.setLayer(0);
        tent2.setImageFile("map*_1.png");
        tent2.setImageX(80);
        tent2.setImageY(120);
        tent2.setImageWidth(80);
        tent2.setImageHeight(40);
        tent2.setAnimation(0, 0, 1, 0);
        tent2.setCollision(HitTile.Air);
        tent2.setRGBAMax(0, 0, 0, 255);
        tent2.setArg23(1);

        screen.getObjects().add(tent2);

        ConversationDoor shop = new ConversationDoor(screen, 500, 240);
        shop.setShopDefaults();

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addSecretShop(Screen screen, int secretShopBlock) {
        ConversationDoor shop = new ConversationDoor(screen, 0, 240);
        shop.setShopDefaults();
        shop.setBlockNumber(secretShopBlock);

        shop.addTests(new TestByteOperation(FlagConstants.CUSTOM_SECRET_SHOP, ByteOp.FLAG_GTEQ, 1));

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addDanceDetector(Screen screen, int danceBlock) {
        GameObject dance = new GameObject(screen);
        dance.setId((short) 0xb8);
        dance.setX(0);
        dance.setY(0);

        dance.getArgs().add((short)danceBlock);
        dance.getArgs().add((short)32);
        dance.getArgs().add((short)24);

        dance.getTestByteOperations().add(new TestByteOperation(FlagConstants.CUSTOM_SECRET_SHOP, ByteOp.FLAG_EQUALS, 0));
        dance.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.CUSTOM_SECRET_SHOP, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(dance);
        return dance;
    }

    public static void addItemGive(GameObject referenceObj, int inventoryArg, int randomizeGraphicsFlag, int worldFlag) {
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

    public static GameObject addMissingBacksideDoorCover(GameObject backsideDoor, int gateFlag) {
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

    public static GameObject addMissingBacksideDoorTimerAndSound(ObjectContainer objectContainer, int bossFlag, int gateFlag) {
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

    public static void addExtinctionTorch(Screen screen) {
        Hitbox extinctionTorchHitbox = new Hitbox(screen, 60, 80);
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

        GraphicsTextureDraw extinctionTorchLight = new GraphicsTextureDraw(screen, 60, 80);
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

        GraphicsTextureDraw extinctionTorch = new GraphicsTextureDraw(screen, 60, 80);
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

    public static ShopBlock addShopBlock(List<Block> blocks) {
        ShopBlock shopBlock = new ShopBlock(blocks.size());

        BlockListData shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)105);
        shopBlockData.getData().add((short)105);
        shopBlockData.getData().add((short)105);
        shopBlockData.getData().add((short)0x000a);
        shopBlock.setInventoryItemArgsList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)10);
        shopBlockData.getData().add((short)10);
        shopBlockData.getData().add((short)10);
        shopBlockData.getData().add((short)0x000a);
        shopBlock.setInventoryPriceList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)5);
        shopBlockData.getData().add((short)5);
        shopBlockData.getData().add((short)5);
        shopBlockData.getData().add((short)0x000a);
        shopBlock.setInventoryCountList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0x000a);
        shopBlock.setFlagList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0x000a);
        shopBlock.setExitFlagList(shopBlockData);

        shopBlock.setBackground(new BlockCmdSingle((short)4));
        shopBlock.setSprite(new BlockCmdSingle((short)0x2dc));
        shopBlock.setMusic(new BlockCmdSingle((short)4));

        BlockStringData blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData("Weight , Weight , Weight"));
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
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("text.intro")));
        shopBlock.setString(blockStringData, 2);

        blockStringData = new BlockStringData();
        List<Short> data = FileUtils.stringToData(Translations.getText("shop0.askItem1String.1"));
        data.add((short)0x004a);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add((short)77);
        data.add((short)105);
        data.add((short)0x004a);
        data.add((short)0);
        data.add((short)0);
        data.add((short)0);
        blockStringData.getData().addAll(data);
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.askItem1String.2")));
        shopBlock.setString(blockStringData, 3);

        blockStringData = new BlockStringData();
        data = FileUtils.stringToData(Translations.getText("shop0.askItem2String.1"));
        data.add((short)0x004a);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add((short)77);
        data.add((short)105);
        data.add((short)0x004a);
        data.add((short)0);
        data.add((short)0);
        data.add((short)0);
        blockStringData.getData().addAll(data);
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.askItem2String.2")));
        shopBlock.setString(blockStringData, 4);

        blockStringData = new BlockStringData();
        data = FileUtils.stringToData(Translations.getText("shop0.askItem3String.1"));
        data.add((short)0x004a);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add((short)77);
        data.add((short)105);
        data.add((short)0x004a);
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
        blocks.add(shopBlock);
        return shopBlock;
    }

    public static ShopBlock addSecretShopBlock(List<Block> blocks) {
        ShopBlock shopBlock = new ShopBlock(blocks.size());

        BlockListData shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add(DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Scriptures").getInventoryArg());
        shopBlockData.getData().add(DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get("Perfume").getInventoryArg());
        shopBlockData.getData().add((short)0x06a);
        shopBlock.setInventoryItemArgsList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)1);
        shopBlockData.getData().add((short)1);
        shopBlockData.getData().add((short)0);
        shopBlock.setInventoryPriceList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)1);
        shopBlockData.getData().add((short)1);
        shopBlockData.getData().add((short)100);
        shopBlock.setInventoryCountList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlock.setFlagList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
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
        data.add((short)0x004a);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add((short)77);
        data.add((short)105);
        data.add((short)0x004a);
        data.add((short)0);
        data.add((short)0);
        data.add((short)0);
        blockStringData.getData().addAll(data);
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.askItem1String.2")));
        shopBlock.setString(blockStringData, 3);

        blockStringData = new BlockStringData();
        data = FileUtils.stringToData(Translations.getText("shop0.askItem2String.1"));
        data.add((short)0x004a);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add((short)77);
        data.add((short)105);
        data.add((short)0x004a);
        data.add((short)0);
        data.add((short)0);
        data.add((short)0);
        blockStringData.getData().addAll(data);
        blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("shop0.askItem2String.2")));
        shopBlock.setString(blockStringData, 4);

        blockStringData = new BlockStringData();
        data = FileUtils.stringToData(Translations.getText("shop0.askItem3String.1"));
        data.add((short)0x004a);
        data.add((short)0x96);
        data.add((short)0);
        data.add((short)0x64);
        blockStringData.setItemNameStartIndex(data.size());
        blockStringData.setItemNameEndIndex(blockStringData.getItemNameStartIndex() + 2);
        data.add((short)77);
        data.add((short)105);
        data.add((short)0x004a);
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
        blocks.add(shopBlock);
        return shopBlock;
    }

    public static Block addDanceBlock(List<Block> blocks) {
        Block danceBlock = new Block(blocks.size());
        BlockListData danceMove = new BlockListData((short)0x004e, (short)1);
        danceMove.getData().add((short)1); // Jump
        danceBlock.getBlockContents().add(danceMove);
        danceBlock.getBlockContents().add(new BlockSingleData((short)0x000a));

        danceMove = new BlockListData((short)0x004e, (short)1);
        danceMove.getData().add((short)3); // Swing right
        danceBlock.getBlockContents().add(danceMove);
        danceBlock.getBlockContents().add(new BlockSingleData((short)0x000a));

        danceMove = new BlockListData((short)0x004e, (short)1);
        danceMove.getData().add((short)1); // Jump
        danceBlock.getBlockContents().add(danceMove);
        danceBlock.getBlockContents().add(new BlockSingleData((short)0x000a));

        danceMove = new BlockListData((short)0x004e, (short)1);
        danceMove.getData().add((short)2); // Swing left
        danceBlock.getBlockContents().add(danceMove);

        blocks.add(danceBlock);
        return danceBlock;
    }

    public static int addNpcBlock(List<Block> blocks, int templateBlockIndex, int conversationFlagIndex) {
        Block templateBlock = blocks.get(templateBlockIndex);
        if(templateBlock instanceof MasterNpcBlock) {
            Block halloweenBlock = new Block(blocks.size());
            halloweenBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));
            List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.text1"));
            for (Short shortCharacter : stringCharacters) {
                halloweenBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
            }
            halloweenBlock.getBlockContents().add(new BlockItemData((short)0x0042, (short)84)); // Secret Treasure of Life
            halloweenBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)conversationFlagIndex, (short)1));
            halloweenBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

            stringCharacters = FileUtils.stringToData(Translations.getText(templateBlockIndex == 689 ? "event.halloween.fraud" : "event.halloween.text2"));
            for (Short shortCharacter : stringCharacters) {
                halloweenBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
            }

            halloweenBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)FlagConstants.CONVERSATION_CANT_LEAVE, (short)0)); // Can-exit flag

            blocks.add(halloweenBlock);

            // Build master block referencing this
            MasterNpcBlock masterNpcBlock = new MasterNpcBlock((MasterNpcBlock)templateBlock, blocks.size());
            masterNpcBlock.setTextCard(new BlockCmdSingle((short)halloweenBlock.getBlockNumber()));
            blocks.add(masterNpcBlock);
            return masterNpcBlock.getBlockNumber();
        }
        return 0;
    }

    public static int addNoCandyMasterBlock(List<Block> blocks, int templateBlockIndex, int noCandyTextBlockIndex) {
        Block templateBlock = blocks.get(templateBlockIndex);
        if(templateBlock instanceof MasterNpcBlock) {
            MasterNpcBlock masterNpcBlock = new MasterNpcBlock((MasterNpcBlock)templateBlock, blocks.size());
            masterNpcBlock.setTextCard(new BlockCmdSingle((short)noCandyTextBlockIndex));
            blocks.add(masterNpcBlock);
            return masterNpcBlock.getBlockNumber();
        }
        return 0;
    }

    public static int addNpcCountBlock(List<Block> blocks, int currentNpcs, int maxNpcs) {
        Block npcCountBlock = new Block(blocks.size());
        List<Short> stringCharacters = FileUtils.stringToData(String.format(Translations.getText("event.halloween.npcCount"), currentNpcs, maxNpcs));
        for (Short shortCharacter : stringCharacters) {
            npcCountBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        blocks.add(npcCountBlock);
        return npcCountBlock.getBlockNumber();
    }

    public static int addNpcHintBlock(List<Block> blocks, int hintNumber, boolean updateConversationFlag) {
        Block npcCountBlock = new Block(blocks.size());
        if(updateConversationFlag) {
            npcCountBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_HINT, (short)hintNumber));
        }

        String hintText = getHintText(hintNumber);
        String[] hintTexts = hintText.split("%s");
        List<Short> stringCharacters;
        if(hintTexts.length > 0) {
            stringCharacters = FileUtils.stringToData(hintTexts[0]);
            for (Short shortCharacter : stringCharacters) {
                npcCountBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
            }
        }
        npcCountBlock.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0x96, (short)0x32, (short)0));
        stringCharacters = FileUtils.stringToData(getLocationText(hintNumber));
        for (Short shortCharacter : stringCharacters) {
            npcCountBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        npcCountBlock.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0, (short)0, (short)0));

        stringCharacters = FileUtils.stringToData(hintTexts[hintTexts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            npcCountBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }

        blocks.add(npcCountBlock);
        return npcCountBlock.getBlockNumber();
    }

    public static void addFoolsMulbrukBlocks(Screen mulbrukScreen, List<Block> datInfo) {
        // Conversation to go to credits early.
        Block foolsEarlyExitBlock = new Block(datInfo.size());
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit1"));
        for (Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockItemData((short)0x0042, (short)62)); // Spaulder
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit2"));
        for (Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit3"));
        for (Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit4"));
        for (Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit5"));
        for (Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit6"));
        for (Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit7"));
        for (Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exit8"));
        for (Short shortCharacter : stringCharacters) {
            foolsEarlyExitBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }

        foolsEarlyExitBlock.getBlockContents().add(new BlockPoseData((short)0x0046, (short)8)); // Pose 8
        foolsEarlyExitBlock.getBlockContents().add(new BlockPoseData((short)0x0046, (short)9)); // Pose 9
        foolsEarlyExitBlock.getBlockContents().add(new BlockSceneData((short)0x004f, (short)0)); // Scene 0 (credits)
        datInfo.add(foolsEarlyExitBlock);

        // Conversation offering to quit
        Block foolsOptionBlock = new Block(datInfo.size());
        foolsOptionBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)FlagConstants.CONVERSATION_CANT_LEAVE, (short)1)); // Can-exit flag
        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.exitPrompt"));
        for (Short shortCharacter : stringCharacters) {
            foolsOptionBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsOptionBlock.getBlockContents().add(new BlockSingleData((short)0x000a));

        BlockListData repeatCmd = new BlockListData((short)0x004e, (short)1);
        repeatCmd.getData().add((short)foolsEarlyExitBlock.getBlockNumber());
        foolsOptionBlock.getBlockContents().add(repeatCmd);
        foolsOptionBlock.getBlockContents().add(new BlockSingleData((short)0x000a));

        stringCharacters = FileUtils.stringToData(Translations.getText("prompt.yes"));
        for (Short shortCharacter : stringCharacters) {
            foolsOptionBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsOptionBlock.getBlockContents().add(new BlockSingleData((short)0x000a));

        stringCharacters = FileUtils.stringToData(Translations.getText("prompt.no"));
        for (Short shortCharacter : stringCharacters) {
            foolsOptionBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsOptionBlock.getBlockContents().add(new BlockSingleData((short)0x000a));

        stringCharacters = FileUtils.stringToData(Translations.getText("event.fools2020.noQuit"));
        for (Short shortCharacter : stringCharacters) {
            foolsOptionBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        foolsOptionBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)FlagConstants.CONVERSATION_CANT_LEAVE, (short)0)); // Can-exit flag
        foolsOptionBlock.getBlockContents().add(new BlockSingleData((short)0x000a));
        datInfo.add(foolsOptionBlock);

        // Master block - Some eggs
        MasterNpcBlock optionMasterNpcBlock = new MasterNpcBlock((MasterNpcBlock)datInfo.get(0x39c), datInfo.size());
        optionMasterNpcBlock.setTextCard(new BlockCmdSingle((short)foolsOptionBlock.getBlockNumber()));
        datInfo.add(optionMasterNpcBlock);

        // Master block - Book of the Dead
        MasterNpcBlock bookMasterNpcBlock = new MasterNpcBlock(datInfo.size());
        bookMasterNpcBlock.setTextCard(new BlockCmdSingle((short)397));
        bookMasterNpcBlock.setBackground(new BlockCmdSingle((short)0x019));
        bookMasterNpcBlock.setNpcCard(new BlockCmdSingle((short)0x2e0)); // 2f0?
        bookMasterNpcBlock.setMusic(new BlockCmdSingle((short)0x00f));
        bookMasterNpcBlock.setNpcName(((MasterNpcBlock)datInfo.get(0x39c)).getNpcName());
        datInfo.add(bookMasterNpcBlock);

        // Find existing objects
        GameObject escapeConversationNormal = null;
        List<GameObject> keptObjects = new ArrayList<>();
        for(GameObject gameObject : mulbrukScreen.getObjects()) {
            if(gameObject.getId() == 0xa0) {
                if(gameObject.getArgs().get(4) == 924) {
                    escapeConversationNormal = gameObject;
                    keptObjects.add(gameObject);
                }
                else if(gameObject.getArgs().get(4) == 926) {
                    keptObjects.add(gameObject);
                }
            }
            else {
                keptObjects.add(gameObject);
            }
        }

        GameObject bookOfTheDeadConversation = new GameObject(escapeConversationNormal);
        bookOfTheDeadConversation.getArgs().set(4, (short)bookMasterNpcBlock.getBlockNumber());
        bookOfTheDeadConversation.getTestByteOperations().clear();
        bookOfTheDeadConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_NOT_EQUAL, 1));
        bookOfTheDeadConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.MULBRUK_CONVERSATION_BOOK, ByteOp.FLAG_EQUALS, 1));
        bookOfTheDeadConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.MULBRUK_CONVERSATION_AWAKE, ByteOp.FLAG_GTEQ, 1));

        GameObject optionConversation = new GameObject(escapeConversationNormal);
        optionConversation.getArgs().set(4, (short)optionMasterNpcBlock.getBlockNumber());
        optionConversation.getTestByteOperations().clear();
        optionConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_NOT_EQUAL, 1)); // Option to quit
        optionConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.MULBRUK_CONVERSATION_BOOK, ByteOp.FLAG_NOT_EQUAL, 1));
        optionConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.MULBRUK_CONVERSATION_AWAKE, ByteOp.FLAG_GTEQ, 1));

        mulbrukScreen.getObjects().clear();
        mulbrukScreen.getObjects().addAll(keptObjects);
        mulbrukScreen.getObjects().add(bookOfTheDeadConversation);
        mulbrukScreen.getObjects().add(optionConversation);
    }

    public static int addDevRoomHintBlock(List<Block> blocks, boolean updateConversationFlag) {
        Block devHintBlock = new Block(blocks.size());
        if(updateConversationFlag) {
            devHintBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_HINT, (short)0));
        }

        String hintText = Translations.getText("event.halloween.hintDevs");
        List<Short> stringCharacters = FileUtils.stringToData(hintText);
        for (Short shortCharacter : stringCharacters) {
            devHintBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        blocks.add(devHintBlock);
        return devHintBlock.getBlockNumber();
    }

    public static int addMulbrukHTBlock(List<Block> blocks, int totalHints) {
        Block mulbrukHTBlock = new Block(blocks.size());
        mulbrukHTBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)FlagConstants.CONVERSATION_CANT_LEAVE, (short)1));

        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.htMulbruk1"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)FlagConstants.HT_UNLOCKED, (short)1)); // Unlock HT
        mulbrukHTBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_HINT, (short)totalHints)); // Set 0xace (Mulbruk hints cycle flag) to +1
        mulbrukHTBlock.getBlockContents().add(new BlockSingleData((short)0x0044)); // {CLS}

        String textLine = Translations.getText("event.halloween.htMulbruk2");
        String[] textParts = textLine.split("%s");
        if(textParts.length > 0) {
            stringCharacters = FileUtils.stringToData(textParts[0]);
            for (Short shortCharacter : stringCharacters) {
                mulbrukHTBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
            }
        }
        mulbrukHTBlock.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0x96, (short)0, (short)0x64));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.halloweenCostume"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlock.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0, (short)0, (short)0));
        stringCharacters = FileUtils.stringToData(textParts[textParts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

        textLine = Translations.getText("event.halloween.htMulbruk3");
        textParts = textLine.split("%s");
        if(textParts.length > 0) {
            stringCharacters = FileUtils.stringToData(textParts[0]);
            for (Short shortCharacter : stringCharacters) {
                mulbrukHTBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
            }
        }
        mulbrukHTBlock.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0x96, (short)0x32, (short)0));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.helloweenTemple"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlock.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0, (short)0, (short)0));
        stringCharacters = FileUtils.stringToData(textParts[textParts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.htMulbruk4"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)FlagConstants.CONVERSATION_CANT_LEAVE, (short)0));

        blocks.add(mulbrukHTBlock);
        return mulbrukHTBlock.getBlockNumber();
    }

    public static int addAllNpcsBlock(List<Block> blocks) {
        Block allNpcsBlock = new Block(blocks.size());
        List<Short> stringCharacters;
        String textLine = String.format(Translations.getText("event.halloween.xelpudAll"), 29, 29, "%s");
        String[] textParts = textLine.split("%s");
        if(textParts.length > 0) {
            stringCharacters = FileUtils.stringToData(textParts[0]);
            for (Short shortCharacter : stringCharacters) {
                allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
            }
        }
        allNpcsBlock.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0x96, (short)0x32, (short)0));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.mulbruk"));
        for (Short shortCharacter : stringCharacters) {
            allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        allNpcsBlock.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0, (short)0, (short)0));
        stringCharacters = FileUtils.stringToData(textParts[textParts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        blocks.add(allNpcsBlock);
        return allNpcsBlock.getBlockNumber();
    }

    public static int addNoCandyBlock(List<Block> blocks) {
        Block allNpcsBlock = new Block(blocks.size());
        List<Short> stringCharacters;
        String textLine = Translations.getText("event.halloween.noDracuet");
        String[] textParts = textLine.split("%s");
        if(textParts.length > 0) {
            stringCharacters = FileUtils.stringToData(textParts[0]);
            for (Short shortCharacter : stringCharacters) {
                allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
            }
        }
        allNpcsBlock.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0x96, (short)0, (short)0x64));
        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.halloweenCandy"));
        for (Short shortCharacter : stringCharacters) {
            allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        allNpcsBlock.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0, (short)0, (short)0));
        stringCharacters = FileUtils.stringToData(textParts[textParts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            allNpcsBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        blocks.add(allNpcsBlock);
        return allNpcsBlock.getBlockNumber();
    }

    private static String getHintText(int hintNumber) {
        String peopleText = "";
        if(hintNumber == 1) {
            peopleText = String.format(Translations.getText("event.halloween.hintPlural"), Settings.isIncludeHellTempleNPCs() ? 4 : 3);
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

    public static void addHTSkip(Screen screen, List<Block> blocks) {
        Block htExplanation = new Block(blocks.size());
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.htSkip"));
        for (Short shortCharacter : stringCharacters) {
            htExplanation.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        htExplanation.getBlockContents().add(new BlockSingleData((short)0x000a)); // End record
        BlockListData tabletData = new BlockListData((short)0x004e, (short)2);
        tabletData.getData().add((short)0); // Language: 0 = English; 1 = La-Mulanese; 2 = Ancient La-Mulanese; 3 = Rosetta Stone
        tabletData.getData().add((short)0); // Slate: 0 = No image; 1 = use slate00.png; 1 = use slate01.png
        htExplanation.getBlockContents().add(tabletData);

        blocks.add(htExplanation);

        Scannable tabletReadable = new Scannable(screen, 60, 400);

        tabletReadable.setTextBlock((short)htExplanation.getBlockNumber());
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

    public static void addHTWarning(Screen screen, List<Block> blocks) {
        Block htExplanation = new Block(blocks.size());

        List<Short> stringCharacters;
        String textLine = Translations.getText("event.halloween.htLogic");
        String[] textParts = textLine.split("%s");
        if(textParts.length > 0) {
            stringCharacters = FileUtils.stringToData(textParts[0]);
            for (Short shortCharacter : stringCharacters) {
                htExplanation.getBlockContents().add(new BlockSingleData(shortCharacter));
            }
        }
        htExplanation.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0x96, (short)0, (short)0x64));
        stringCharacters = FileUtils.stringToData(Translations.getText("items.HolyGrail"));
        for (Short shortCharacter : stringCharacters) {
            htExplanation.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        htExplanation.getBlockContents().add(new BlockColorsData((short)0x004a, (short)0, (short)0, (short)0));
        stringCharacters = FileUtils.stringToData(textParts[textParts.length > 0 ? 1 : 0]);
        for (Short shortCharacter : stringCharacters) {
            htExplanation.getBlockContents().add(new BlockSingleData(shortCharacter));
        }

        htExplanation.getBlockContents().add(new BlockSingleData((short)0x000a)); // End record
        BlockListData tabletData = new BlockListData((short)0x004e, (short)2);
        tabletData.getData().add((short)0); // Language: 0 = English; 1 = La-Mulanese; 2 = Ancient La-Mulanese; 3 = Rosetta Stone
        tabletData.getData().add((short)0); // Slate: 0 = No image; 1 = use slate00.png; 1 = use slate01.png
        htExplanation.getBlockContents().add(tabletData);

        blocks.add(htExplanation);

        Scannable tabletReadable = new Scannable(screen, 120, 400);

        tabletReadable.setTextBlock((short)htExplanation.getBlockNumber());
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
                        new WriteByteOperation(FlagConstants.TABLET_GRAIL_SHRINE_FRONT, ByteOp.ASSIGN_FLAG, 0),
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
    public static void addEyeOfDivineRetribution(Screen screen, int x, int y) {
        EyeOfRetribution eyeOfDivineRetribution = new EyeOfRetribution(screen, x, y);
        eyeOfDivineRetribution.setPercentDamage(100);
        screen.getObjects().add(eyeOfDivineRetribution);
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
    public static void addExtendingSpikes(ObjectContainer objectContainer, int x, int y, int flagIndex) {
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

    /**
     * Add 0x98 object
     * @param screen to add to
     * @param x position
     * @param y position
     */
    public static void addWarpDoor(Screen screen, int x, int y, int destZone, int destRoom, int destScreen, int destX, int destY, List<TestByteOperation> tests) {
        WarpDoor warpDoor = new WarpDoor(screen, x, y);
        warpDoor.setDestination(destZone, destRoom, destScreen, destX, destY);
        warpDoor.getTestByteOperations().addAll(tests);
        screen.getObjects().add(warpDoor);
    }

    /**
     * Add 0x2e object
     * @param screen to add to
     * @param x position
     * @param y position
     */
    public static void addAmphisbaenaAnkh(Screen screen, int x, int y, int damage, List<TestByteOperation> tests) {
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
        int zoneIndex = screen.getZoneIndex();
        int roomIndex = screen.getRoomIndex();
        int screenIndex = screen.getScreenIndex();
        if(zoneIndex == 0) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 1) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 3) {
                AddObject.addItemGive(screen, 1920, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 11 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 2) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 3) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 3) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 4) {
                AddObject.addItemGive(screen, 640, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 5) {
                AddObject.addItemGive(screen, 1280, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 4) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 3) {
                AddObject.addItemGive(screen, 0, 1440, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 4) {
                AddObject.addItemGive(screen, 0, 1920, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 12 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 13 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 17 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 24 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 35 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if (zoneIndex == 5) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 6) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 7) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 11 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 11 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 12 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 12 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 12 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 13 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 13 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 14 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 14 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 15 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 15 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 16 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 16 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 16 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 8) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 3) {
                AddObject.addItemGive(screen, 1920, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 3) {
                AddObject.addItemGive(screen, 1920, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 3) {
                AddObject.addItemGive(screen, 1920, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 3) {
                AddObject.addItemGive(screen, 1920, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 12 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 9) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 10) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 11) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 3) {
                AddObject.addItemGive(screen, 0, 1440, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 12) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 3) {
                AddObject.addItemGive(screen, 0, 1440, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 13) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 3) {
                AddObject.addItemGive(screen, 0, 1440, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 14 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 15 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 16 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 26 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 14) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 15) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 16) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 17) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 18) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 19) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 20) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 21) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 22) {
            if(roomIndex == 0 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 0 && screenIndex == 2) {
                AddObject.addItemGive(screen, 1280, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 1 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 5 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 960, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 7 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 1) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 2) {
                AddObject.addItemGive(screen, 0, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 10 && screenIndex == 3) {
                AddObject.addItemGive(screen, 640, 480, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 11 && screenIndex == 0) {
                AddObject.addItemGive(screen, 0, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
            else if(roomIndex == 11 && screenIndex == 1) {
                AddObject.addItemGive(screen, 640, 0, inventoryArg,
                        Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                        Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
            }
        }
        else if(zoneIndex == 25) {
            AddObject.addItemGive(screen, 0, 480 * screenIndex, 86,
                    Arrays.asList(new TestByteOperation(itemGiveTriggerFlag, ByteOp.FLAG_GT, itemGiveTriggerFlagValue), new TestByteOperation(itemGiveStateFlag, ByteOp.FLAG_EQUALS, 0)),
                    Arrays.asList(new WriteByteOperation(itemGiveStateFlag, ByteOp.ASSIGN_FLAG, itemGiveStateFlagValue)));
        }
    }
}
