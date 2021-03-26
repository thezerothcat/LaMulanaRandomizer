package lmr.randomizer.dat;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.shop.BlockCmdSingle;
import lmr.randomizer.dat.shop.BlockStringData;
import lmr.randomizer.dat.shop.MasterNpcBlock;
import lmr.randomizer.dat.shop.ShopBlock;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.GameObjectId;
import lmr.randomizer.update.LocationCoordinateMapper;

import java.util.ArrayList;
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
    public static void addSecondsTimer(ObjectContainer objectContainer, int delaySeconds, List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        GameObject obj = new GameObject(objectContainer);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)delaySeconds);
        obj.getArgs().add((short)0);
        obj.setX(-1);
        obj.setY(-1);

        obj.getTestByteOperations().addAll(tests);
        obj.getWriteByteOperations().addAll(updates);
        objectContainer.getObjects().add(0, obj);
    }

    /**
     * Convenience for adding a timer object to any screen.
     * @param objectContainer to add the timer object to
     * @param delayFrames seconds to wait before the timer runs its updates
     * @param tests tests to put on the timer object
     * @param updates updates the timer object should make when all of its tests pass
     */
    public static GameObject addFramesTimer(ObjectContainer objectContainer, int delayFrames, List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        GameObject obj = new GameObject(objectContainer);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)delayFrames);
        obj.setX(-1);
        obj.setY(-1);

        obj.getTestByteOperations().addAll(tests);
        obj.getWriteByteOperations().addAll(updates);
        objectContainer.getObjects().add(0, obj);
        return obj;
    }

    public static void addFloatingItem(Screen screen, int x, int y, int itemArg, boolean realItem, List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x2f);
        obj.setX(x);
        obj.setY(y);

        obj.getArgs().clear();
        obj.getArgs().add((short)0); // Interactable any time?
        obj.getArgs().add((short)itemArg); // Item arg
        obj.getArgs().add((short)(realItem ? 1 : 0)); // 0 = fake item, 1 = real item
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
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x9f);
        obj.getArgs().add((short)textBlock);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)506);
        obj.getArgs().add((short)280);
        obj.setX(x);
        obj.setY(y);

        obj.getTestByteOperations().addAll(tests);
        obj.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0));
        obj.getWriteByteOperations().add(update);
        screen.getObjects().add(obj);
        return obj;
    }

    /**
     * Add a timer to set the flag for solving the Diary chest puzzle if the appropriate conditions are met.
     * @param screen the screen to add the timers to
     */
    public static void addDiaryChestConditionTimer(Screen screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.setX(-1);
        obj.setY(-1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(2796);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte)3);
        obj.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(536);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte)1);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(537);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)2);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, obj);
    }

    /**
     * Add a timer to trigger
     * @param screen the screen to add the timers to
     */
    public static void addPalenqueMSX2Timer(Screen screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.setX(-1);
        obj.setY(-1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x102);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)4);
        obj.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x21d);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x382);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x21d);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x317);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x328);
        writeByteOperation.setOp(ByteOp.ADD_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, obj);
    }

    /**
     * Add a timer to open weight doors during the escape that don't open naturally.
     * @param screen the screen to add the timers to
     */
    public static void addWeightDoorTimer(Screen screen, int weightFlag) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)0);
        obj.setX(-1);
        obj.setY(-1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x382);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        obj.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(weightFlag);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(weightFlag);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, obj);
    }

    /**
     * Add a timer to set the flag that resets the Twin Labyrinths poison timer
     * @param screen the screen to add the timers to
     */
    public static void addTwinLabsPoisonTimerRemoval(ObjectContainer screen, boolean resetPuzzle) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.setX(-1);
        obj.setY(-1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x1dc);
        testByteOperation.setOp(ByteOp.FLAG_LTEQ);
        testByteOperation.setValue((byte)1);
        obj.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x1d7);
        testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x1d7);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)0);
        obj.getWriteByteOperations().add(writeByteOperation);

        if(resetPuzzle) {
            writeByteOperation = new WriteByteOperation();
            writeByteOperation.setIndex(0x1dc);
            writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
            writeByteOperation.setValue((byte)0);
            obj.getWriteByteOperations().add(writeByteOperation);
        }

        screen.getObjects().add(0, obj);
    }

    /**
     * Add a timer to make Isis' Pendant room ceiling breakable
     * @param screen the screen to add the timers to
     */
    public static void addIsisRoomCeilingTimer(ObjectContainer screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.setX(-1);
        obj.setY(-1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x17a);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x17a);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)2);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, obj);
    }

    /**
     * Add a timer to make Goddess statue shield disappear after leaving the room
     * @param screen the screen to add the timers to
     */
    public static void addGoddessShieldTimer(ObjectContainer screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.setX(-1);
        obj.setY(-1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x284);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte)2);
        obj.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x34e);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x34e);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)12);
        obj.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x284);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)3);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, obj);
    }

    /**
     * Add a timer to set the flag for solving the Diary chest puzzle if the appropriate conditions are met.
     * @param transitionGate the gate to put the detector with
     */
    public static void addGoddessStatueLemezaDetector(GameObject transitionGate) {
        GameObject obj = new GameObject(transitionGate.getObjectContainer());
        obj.setId((short)0x14);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)2);
        obj.getArgs().add((short)3);
        obj.setX(transitionGate.getX() - 40);
        obj.setY(transitionGate.getY() - 20);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x278);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x278);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        transitionGate.getObjectContainer().getObjects().add(obj);
    }

    /**
     * Add timer to fix the puzzle for passage between Temple of Moonlight and Twin Labyrinths
     * @param screen the screen to add the timers to
     */
    public static void addMoonlightPassageTimer(Screen screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.setX(-1);
        obj.setY(-1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(606);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(606);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)0);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, obj);
    }

    /**
     * Add timer for failed Flail Whip puzzle.
     * @param screen the screen to add the timers to
     */
    public static void addFlailWhipPuzzleTimer(Screen screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.setX(-1);
        obj.setY(-1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(635);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(635);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)0);
        obj.getWriteByteOperations().add(writeByteOperation);

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
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)30);
        obj.setX(-1);
        obj.setY(-1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(706);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        obj.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(722);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(748);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte)11);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, obj);

        // Timer to spawn dais for odd number of children
        obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)30);
        obj.setX(-1);
        obj.setY(-1);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(706);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(723);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(748);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte)11);
        obj.getTestByteOperations().add(testByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, obj);
    }

    /**
     * Add timer for failed Flail Whip puzzle.
     * @param screen the screen to add the timers to
     */
    public static void addSacredOrbCountTimers(Screen screen) {
        for(int i = 0; i < 10; i++) {
            GameObject obj = new GameObject(screen);
            obj.setId((short)0x0b);
            obj.getArgs().add((short)0);
            obj.getArgs().add((short)0);
            obj.setX(-1);
            obj.setY(-1);

            TestByteOperation testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(0x355 + i);
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            testByteOperation.setValue((byte)0);
            obj.getTestByteOperations().add(testByteOperation);

            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(0x0c7 + i);
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            testByteOperation.setValue((byte)2);
            obj.getTestByteOperations().add(testByteOperation);

            WriteByteOperation writeByteOperation = new WriteByteOperation();
            writeByteOperation.setIndex(0x355 + i);
            writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
            writeByteOperation.setValue((byte)1);
            obj.getWriteByteOperations().add(writeByteOperation);

            writeByteOperation = new WriteByteOperation();
            writeByteOperation.setIndex(0x354);
            writeByteOperation.setOp(ByteOp.ADD_FLAG);
            writeByteOperation.setValue((byte)1);
            obj.getWriteByteOperations().add(writeByteOperation);

            screen.getObjects().add(0, obj);
        }
    }

    /**
     * Add kill timers for randomized main weapons.
     * @param screen the screen to add the objects to
     * @param isXelpudScreen true if this is Xelpud's screen, where the kill timer should not activate until after you talk to him
     */
    public static void addSurfaceKillTimer(Screen screen, boolean isXelpudScreen) {
        GameObject killTimer = new GameObject(screen);
        killTimer.setId((short) 0x0b);
        killTimer.getArgs().add((short) 0);
        killTimer.getArgs().add((short) 0);
        killTimer.setX(-1);
        killTimer.setY(-1);

        TestByteOperation flagTest = new TestByteOperation();
        flagTest.setIndex(0xad1);
        flagTest.setValue((byte) 1);
        flagTest.setOp(ByteOp.FLAG_NOT_EQUAL);
        killTimer.getTestByteOperations().add(flagTest);

        if(isXelpudScreen) {
            flagTest = new TestByteOperation();
            flagTest.setIndex(0xad0);
            flagTest.setValue((byte) 1);
            flagTest.setOp(ByteOp.FLAG_EQUALS);
            killTimer.getTestByteOperations().add(flagTest);
        }

        WriteByteOperation flagUpdate = new WriteByteOperation();
        flagUpdate.setIndex(0x3e9);
        flagUpdate.setValue((byte) 1);
        flagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        killTimer.getWriteByteOperations().add(flagUpdate);

        screen.getObjects().add(0, killTimer);
    }

    /**
     * Add replacement timer for Xelpud dialogue state to compensate for removed 0x07c flag in intro conversation
     * @param screen the screen to add the objects to
     */
    public static void addXelpudIntroTimer(Screen screen) {
        GameObject timer = new GameObject(screen);
        timer.setId((short) 0x0b);
        timer.getArgs().add((short) 0);
        timer.getArgs().add((short) 0);
        timer.setX(-1);
        timer.setY(-1);

        TestByteOperation flagTest = new TestByteOperation();
        flagTest.setIndex(0xad0);
        flagTest.setValue((byte) 1);
        flagTest.setOp(ByteOp.FLAG_EQUALS);
        timer.getTestByteOperations().add(flagTest);

        flagTest = new TestByteOperation();
        flagTest.setIndex(0x07c);
        flagTest.setValue((byte) 0);
        flagTest.setOp(ByteOp.FLAG_EQUALS);
        timer.getTestByteOperations().add(flagTest);

        WriteByteOperation flagUpdate = new WriteByteOperation();
        flagUpdate.setIndex(0x07c);
        flagUpdate.setValue((byte) 1);
        flagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        timer.getWriteByteOperations().add(flagUpdate);

        screen.getObjects().add(0, timer);
    }

    /**
     * Add a backup door from Chamber of Extinction to untransformed Shrine of the Mother.
     * @param screen the screen to add the objects to
     */
    public static void addLowerUntrueShrineBackupDoor(Screen screen) {
        GameObject backupShrineDoor = new GameObject(screen);

        backupShrineDoor.setId((short) 0x98);
        backupShrineDoor.setX(260);
        backupShrineDoor.setY(800);

        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)9);
        backupShrineDoor.getArgs().add((short)8);
        backupShrineDoor.getArgs().add((short)1);
        backupShrineDoor.getArgs().add((short)300);
        backupShrineDoor.getArgs().add((short)320);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoor.getTestByteOperations().add(testByteOperation);

        screen.getObjects().add(backupShrineDoor);

        GameObject backupShrineDoorGraphic = new GameObject(screen);
        backupShrineDoorGraphic.setId((short) 0x93);
        backupShrineDoorGraphic.setX(240);
        backupShrineDoorGraphic.setY(760);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoorGraphic.getTestByteOperations().add(testByteOperation);

        backupShrineDoorGraphic.getArgs().add((short)-1);
        backupShrineDoorGraphic.getArgs().add((short)-1);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)512);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        backupShrineDoorGraphic.getArgs().add((short)0); // Animation frames
        backupShrineDoorGraphic.getArgs().add((short)1); // Pause frames
        backupShrineDoorGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        backupShrineDoorGraphic.getArgs().add((short)0); // Hittile to fill with
        backupShrineDoorGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        backupShrineDoorGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        backupShrineDoorGraphic.getArgs().add((short)0); // Cycle colors t/f
        backupShrineDoorGraphic.getArgs().add((short)0); // Alpha/frame
        backupShrineDoorGraphic.getArgs().add((short)255); // Max alpha
        backupShrineDoorGraphic.getArgs().add((short)0); // R/frame
        backupShrineDoorGraphic.getArgs().add((short)0); // Max R
        backupShrineDoorGraphic.getArgs().add((short)0); // G/frame
        backupShrineDoorGraphic.getArgs().add((short)0); // Max G
        backupShrineDoorGraphic.getArgs().add((short)0); // B/frame
        backupShrineDoorGraphic.getArgs().add((short)0); // Max B
        backupShrineDoorGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        backupShrineDoorGraphic.getArgs().add((short)1); // not0?
        screen.getObjects().add(backupShrineDoorGraphic);
    }

    /**
     * Add a backup door from Chamber of Extinction to untransformed Shrine of the Mother.
     * @param screen the screen to add the objects to
     */
    public static void addUpperUntrueShrineBackupDoor(Screen screen) {
        GameObject backupShrineDoor = new GameObject(screen);

        backupShrineDoor.setId((short) 0x98);
        backupShrineDoor.setX(340);
        backupShrineDoor.setY(80);

        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)9);
        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)340);
        backupShrineDoor.getArgs().add((short)92);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoor.getTestByteOperations().add(testByteOperation);

        screen.getObjects().add(backupShrineDoor);

        GameObject backupShrineDoorGraphic = new GameObject(screen);
        backupShrineDoorGraphic.setId((short) 0x93);
        backupShrineDoorGraphic.setX(320);
        backupShrineDoorGraphic.setY(40);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoorGraphic.getTestByteOperations().add(testByteOperation);

        backupShrineDoorGraphic.getArgs().add((short)-1);
        backupShrineDoorGraphic.getArgs().add((short)-1);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)512);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        backupShrineDoorGraphic.getArgs().add((short)0); // Animation frames
        backupShrineDoorGraphic.getArgs().add((short)1); // Pause frames
        backupShrineDoorGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        backupShrineDoorGraphic.getArgs().add((short)0); // Hittile to fill with
        backupShrineDoorGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        backupShrineDoorGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        backupShrineDoorGraphic.getArgs().add((short)0); // Cycle colors t/f
        backupShrineDoorGraphic.getArgs().add((short)0); // Alpha/frame
        backupShrineDoorGraphic.getArgs().add((short)255); // Max alpha
        backupShrineDoorGraphic.getArgs().add((short)0); // R/frame
        backupShrineDoorGraphic.getArgs().add((short)0); // Max R
        backupShrineDoorGraphic.getArgs().add((short)0); // G/frame
        backupShrineDoorGraphic.getArgs().add((short)0); // Max G
        backupShrineDoorGraphic.getArgs().add((short)0); // B/frame
        backupShrineDoorGraphic.getArgs().add((short)0); // Max B
        backupShrineDoorGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        backupShrineDoorGraphic.getArgs().add((short)1); // not0?
        screen.getObjects().add(backupShrineDoorGraphic);
    }

    /**
     * Add a backup door from Endless Corridor to untransformed Shrine of the Mother.
     * @param screen the screen to add the objects to
     */
    public static void addSealUntrueShrineBackupDoor(Screen screen) {
        GameObject backupShrineDoor = new GameObject(screen);

        backupShrineDoor.setId((short) 0x98);
        backupShrineDoor.setX(500);
        backupShrineDoor.setY(400);

        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)9);
        backupShrineDoor.getArgs().add((short)9);
        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)300);
        backupShrineDoor.getArgs().add((short)332);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoor.getTestByteOperations().add(testByteOperation);

        screen.getObjects().add(backupShrineDoor);

        GameObject backupShrineDoorGraphic = new GameObject(screen);
        backupShrineDoorGraphic.setId((short) 0x93);
        backupShrineDoorGraphic.setX(480);
        backupShrineDoorGraphic.setY(360);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoorGraphic.getTestByteOperations().add(testByteOperation);

        backupShrineDoorGraphic.getArgs().add((short)-1);
        backupShrineDoorGraphic.getArgs().add((short)-1);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)512);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        backupShrineDoorGraphic.getArgs().add((short)0); // Animation frames
        backupShrineDoorGraphic.getArgs().add((short)1); // Pause frames
        backupShrineDoorGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        backupShrineDoorGraphic.getArgs().add((short)0); // Hittile to fill with
        backupShrineDoorGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        backupShrineDoorGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        backupShrineDoorGraphic.getArgs().add((short)0); // Cycle colors t/f
        backupShrineDoorGraphic.getArgs().add((short)0); // Alpha/frame
        backupShrineDoorGraphic.getArgs().add((short)255); // Max alpha
        backupShrineDoorGraphic.getArgs().add((short)0); // R/frame
        backupShrineDoorGraphic.getArgs().add((short)0); // Max R
        backupShrineDoorGraphic.getArgs().add((short)0); // G/frame
        backupShrineDoorGraphic.getArgs().add((short)0); // Max G
        backupShrineDoorGraphic.getArgs().add((short)0); // B/frame
        backupShrineDoorGraphic.getArgs().add((short)0); // Max B
        backupShrineDoorGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        backupShrineDoorGraphic.getArgs().add((short)1); // not0?
        screen.getObjects().add(backupShrineDoorGraphic);
    }

    /**
     * Add Skanda door cover to the screen that left-transitions into west Chamber of Birth
     * @param transitionGate the gate to cover with the graphic
     */
    public static void addSkandaBlock(GameObject transitionGate) {
        GameObject skandaBlockGraphic = new GameObject(transitionGate.getObjectContainer());

        skandaBlockGraphic.setId((short) 0x93);
        skandaBlockGraphic.setX(transitionGate.getX());
        skandaBlockGraphic.setY(transitionGate.getY() - 20);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x2a6);
        testByteOperation.setOp(ByteOp.FLAG_LTEQ);
        testByteOperation.setValue((byte) 1);
        skandaBlockGraphic.getTestByteOperations().add(testByteOperation);

        skandaBlockGraphic.getArgs().add((short)19);
        skandaBlockGraphic.getArgs().add((short)-1);
        skandaBlockGraphic.getArgs().add((short)640);
        skandaBlockGraphic.getArgs().add((short)512);
        skandaBlockGraphic.getArgs().add((short)40);
        skandaBlockGraphic.getArgs().add((short)60);
        skandaBlockGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        skandaBlockGraphic.getArgs().add((short)1); // Animation frames
        skandaBlockGraphic.getArgs().add((short)4); // Pause frames
        skandaBlockGraphic.getArgs().add((short)1); // Repeat count (<1 is forever)
        skandaBlockGraphic.getArgs().add((short)128); // Hittile to fill with
        skandaBlockGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        skandaBlockGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        skandaBlockGraphic.getArgs().add((short)0); // Cycle colors t/f
        skandaBlockGraphic.getArgs().add((short)0); // Alpha/frame
        skandaBlockGraphic.getArgs().add((short)255); // Max alpha
        skandaBlockGraphic.getArgs().add((short)0); // R/frame
        skandaBlockGraphic.getArgs().add((short)0); // Max R
        skandaBlockGraphic.getArgs().add((short)0); // G/frame
        skandaBlockGraphic.getArgs().add((short)0); // Max G
        skandaBlockGraphic.getArgs().add((short)0); // B/frame
        skandaBlockGraphic.getArgs().add((short)0); // Max B
        skandaBlockGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        skandaBlockGraphic.getArgs().add((short)1); // not0?
        transitionGate.getObjectContainer().getObjects().add(skandaBlockGraphic);
    }

    /**
     * Add Illusion door cover to the screen that left-transitions into upper Gate of Illusion
     * @param transitionGate the gate to cover with the graphic
     */
    public static void addIllusionFruitBlockHorizontal(GameObject transitionGate, boolean useRuinGraphic) {
        GameObject fruitBlockGraphic = new GameObject(transitionGate.getObjectContainer());

        fruitBlockGraphic.setId((short) 0x93);
        fruitBlockGraphic.setX(transitionGate.getX());
        fruitBlockGraphic.setY(transitionGate.getY() - 40);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x226);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 0);
        fruitBlockGraphic.getTestByteOperations().add(testByteOperation);

        fruitBlockGraphic.getArgs().add((short)19);
        fruitBlockGraphic.getArgs().add((short)-1);
        fruitBlockGraphic.getArgs().add((short)(useRuinGraphic ? 620 : 540));
        fruitBlockGraphic.getArgs().add((short)512);
        fruitBlockGraphic.getArgs().add((short)20);
        fruitBlockGraphic.getArgs().add((short)80);
        fruitBlockGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        fruitBlockGraphic.getArgs().add((short)1); // Animation frames
        fruitBlockGraphic.getArgs().add((short)4); // Pause frames
        fruitBlockGraphic.getArgs().add((short)1); // Repeat count (<1 is forever)
        fruitBlockGraphic.getArgs().add((short)128); // Hittile to fill with
        fruitBlockGraphic.getArgs().add((short)1); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        fruitBlockGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        fruitBlockGraphic.getArgs().add((short)0); // Cycle colors t/f
        fruitBlockGraphic.getArgs().add((short)0); // Alpha/frame
        fruitBlockGraphic.getArgs().add((short)255); // Max alpha
        fruitBlockGraphic.getArgs().add((short)0); // R/frame
        fruitBlockGraphic.getArgs().add((short)0); // Max R
        fruitBlockGraphic.getArgs().add((short)0); // G/frame
        fruitBlockGraphic.getArgs().add((short)0); // Max G
        fruitBlockGraphic.getArgs().add((short)0); // B/frame
        fruitBlockGraphic.getArgs().add((short)0); // Max B
        fruitBlockGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        fruitBlockGraphic.getArgs().add((short)1); // not0?
        transitionGate.getObjectContainer().getObjects().add(fruitBlockGraphic);
    }

    /**
     * Add Illusion door cover to the screen that up-transitions into lower Gate of Illusion
     * @param transitionGate the gate to cover with the graphic
     */
    public static void addIllusionFruitBlockVertical(GameObject transitionGate) {
        GameObject fruitBlockGraphic = new GameObject(transitionGate.getObjectContainer());

        fruitBlockGraphic.setId((short) 0x93);
        fruitBlockGraphic.setX(transitionGate.getX() - 20);
        fruitBlockGraphic.setY(transitionGate.getY());

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x226);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 0);
        fruitBlockGraphic.getTestByteOperations().add(testByteOperation);

        fruitBlockGraphic.getArgs().add((short)19);
        fruitBlockGraphic.getArgs().add((short)-1);
        fruitBlockGraphic.getArgs().add((short)400);
        fruitBlockGraphic.getArgs().add((short)512);
        fruitBlockGraphic.getArgs().add((short)80);
        fruitBlockGraphic.getArgs().add((short)80);
        fruitBlockGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        fruitBlockGraphic.getArgs().add((short)1); // Animation frames
        fruitBlockGraphic.getArgs().add((short)4); // Pause frames
        fruitBlockGraphic.getArgs().add((short)1); // Repeat count (<1 is forever)
        fruitBlockGraphic.getArgs().add((short)128); // Hittile to fill with
        fruitBlockGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        fruitBlockGraphic.getArgs().add((short)1); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        fruitBlockGraphic.getArgs().add((short)0); // Cycle colors t/f
        fruitBlockGraphic.getArgs().add((short)0); // Alpha/frame
        fruitBlockGraphic.getArgs().add((short)255); // Max alpha
        fruitBlockGraphic.getArgs().add((short)0); // R/frame
        fruitBlockGraphic.getArgs().add((short)0); // Max R
        fruitBlockGraphic.getArgs().add((short)0); // G/frame
        fruitBlockGraphic.getArgs().add((short)0); // Max G
        fruitBlockGraphic.getArgs().add((short)0); // B/frame
        fruitBlockGraphic.getArgs().add((short)0); // Max B
        fruitBlockGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        fruitBlockGraphic.getArgs().add((short)1); // not0?
        transitionGate.getObjectContainer().getObjects().add(fruitBlockGraphic);
    }

    /**
     * Add ankh jewel cover when boss ankh jewel not yet collected
     * @param ankh the ankh to cover with the graphic
     */
    public static void addBossSpecificAnkhCover(GameObject ankh, int ankhFlag) {
        GameObject ankhCover = new GameObject(ankh.getObjectContainer());

        ankhCover.setId((short) 0x93);
        ankhCover.setX(ankh.getX() - 20);
        ankhCover.setY(ankh.getY() - 20);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(ankhFlag);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 0);
        ankhCover.getTestByteOperations().add(testByteOperation);

        ankhCover.getArgs().add((short)1);
        ankhCover.getArgs().add((short)-1);
        ankhCover.getArgs().add((short)840);
        ankhCover.getArgs().add((short)512);
        ankhCover.getArgs().add((short)60);
        ankhCover.getArgs().add((short)60);
        ankhCover.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        ankhCover.getArgs().add((short)1); // Animation frames
        ankhCover.getArgs().add((short)4); // Pause frames
        ankhCover.getArgs().add((short)1); // Repeat count (<1 is forever)
        ankhCover.getArgs().add((short)128); // Hittile to fill with
        ankhCover.getArgs().add((short)1); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        ankhCover.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        ankhCover.getArgs().add((short)0); // Cycle colors t/f
        ankhCover.getArgs().add((short)0); // Alpha/frame
        ankhCover.getArgs().add((short)255); // Max alpha
        ankhCover.getArgs().add((short)0); // R/frame
        ankhCover.getArgs().add((short)0); // Max R
        ankhCover.getArgs().add((short)0); // G/frame
        ankhCover.getArgs().add((short)0); // Max G
        ankhCover.getArgs().add((short)0); // B/frame
        ankhCover.getArgs().add((short)0); // Max B
        ankhCover.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        ankhCover.getArgs().add((short)1); // not0?
        ankh.getObjectContainer().getObjects().add(ankhCover);
    }

    /**
     * Add pushable block blocker
     * @param pushableBlock the pushable block to cover with graphic
     */
    public static void addPushableBlockBlockage(GameObject pushableBlock) {
        GameObject blockage = new GameObject(pushableBlock.getObjectContainer());

        blockage.setId((short) 0x93);
        blockage.setX(pushableBlock.getX());
        blockage.setY(pushableBlock.getY());

        for(TestByteOperation testByteOperation : pushableBlock.getTestByteOperations()) {
            blockage.getTestByteOperations().add(new TestByteOperation(testByteOperation));
        }

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x0a8);
        testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
        testByteOperation.setValue((byte) 2);
        blockage.getTestByteOperations().add(testByteOperation);

        blockage.getArgs().add((short)1);
        if(pushableBlock.getId() == 0xa9) {
            blockage.getArgs().add((short)1); // eveg
            blockage.getArgs().add((short)900);
            blockage.getArgs().add((short)60);
            blockage.getArgs().add((short)40);
            blockage.getArgs().add((short)40);
        }
        else {
            blockage.getArgs().add(pushableBlock.getArgs().get(0));
            blockage.getArgs().add(pushableBlock.getArgs().get(1));
            blockage.getArgs().add(pushableBlock.getArgs().get(2));
            blockage.getArgs().add(pushableBlock.getArgs().get(3));
            blockage.getArgs().add(pushableBlock.getArgs().get(4));
        }
        blockage.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        blockage.getArgs().add((short)1); // Animation frames
        blockage.getArgs().add((short)4); // Pause frames
        blockage.getArgs().add((short)1); // Repeat count (<1 is forever)
        blockage.getArgs().add((short)128); // Hittile to fill with
        blockage.getArgs().add((short)1); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        blockage.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        blockage.getArgs().add((short)0); // Cycle colors t/f
        blockage.getArgs().add((short)0); // Alpha/frame
        blockage.getArgs().add((short)255); // Max alpha
        blockage.getArgs().add((short)0); // R/frame
        blockage.getArgs().add((short)0); // Max R
        blockage.getArgs().add((short)0); // G/frame
        blockage.getArgs().add((short)0); // Max G
        blockage.getArgs().add((short)0); // B/frame
        blockage.getArgs().add((short)0); // Max B
        blockage.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        blockage.getArgs().add((short)1); // not0?
        pushableBlock.getObjectContainer().getObjects().add(blockage);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x0a8);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 2);
        pushableBlock.getTestByteOperations().add(testByteOperation);
    }

    /**
     * Add pushable block blocker
     * @param pushableBlock the pushable block to cover with graphic
     */
    public static void addInfernoPushableBlockReplacements(GameObject pushableBlock) {
        GameObject fallingBlock = new GameObject(pushableBlock.getObjectContainer());

        fallingBlock.setId((short) 0xb9);
        fallingBlock.setX(pushableBlock.getX());
        fallingBlock.setY(pushableBlock.getY());
        fallingBlock.getArgs().clear();
        fallingBlock.getArgs().add((short)1);
        fallingBlock.getArgs().add((short)900);
        fallingBlock.getArgs().add((short)60);
        fallingBlock.getArgs().add((short)40);
        fallingBlock.getArgs().add((short)40);
        fallingBlock.getArgs().add((short)0); // nothing
        fallingBlock.getArgs().add((short)1); // damage
        fallingBlock.getArgs().add((short)1); // landing behavior

        for(TestByteOperation test : pushableBlock.getTestByteOperations()) {
            fallingBlock.getTestByteOperations().add(new TestByteOperation(test));
        }

        // Falling block only happens like this if you don't have Glove
        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x0a8);
        testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
        testByteOperation.setValue((byte) 2);
        fallingBlock.getTestByteOperations().add(testByteOperation);

        // Falling block only falls once.
        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0xacb);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 0);
        fallingBlock.getTestByteOperations().add(testByteOperation);

        // Falling block updates flag that will trigger the non-pushable block to spawn.
        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0xacb);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte) 1);
        fallingBlock.getWriteByteOperations().add(writeByteOperation);

        pushableBlock.getObjectContainer().getObjects().add(fallingBlock);

        // Create non-pushable block
        GameObject blockage = new GameObject(pushableBlock.getObjectContainer());

        blockage.setId((short) 0x93);
        blockage.setX(pushableBlock.getX());
        blockage.setY(pushableBlock.getY() + 60);

        // Use the same tests as the real block, so that this won't appear until after you've used the dais.
        for(TestByteOperation test : pushableBlock.getTestByteOperations()) {
            blockage.getTestByteOperations().add(new TestByteOperation(test));
        }

        // Only do this if you don't have Glove.
        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x0a8);
        testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
        testByteOperation.setValue((byte) 2);
        blockage.getTestByteOperations().add(testByteOperation);

        // Only do this if you've watched the block fall animation.
        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0xacb);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 1);
        blockage.getTestByteOperations().add(testByteOperation);

        blockage.getArgs().add((short)1);
        blockage.getArgs().add((short)1); // eveg
        blockage.getArgs().add((short)900);
        blockage.getArgs().add((short)60);
        blockage.getArgs().add((short)40);
        blockage.getArgs().add((short)40);
        blockage.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        blockage.getArgs().add((short)1); // Animation frames
        blockage.getArgs().add((short)4); // Pause frames
        blockage.getArgs().add((short)1); // Repeat count (<1 is forever)
        blockage.getArgs().add((short)128); // Hittile to fill with
        blockage.getArgs().add((short)1); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        blockage.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        blockage.getArgs().add((short)0); // Cycle colors t/f
        blockage.getArgs().add((short)0); // Alpha/frame
        blockage.getArgs().add((short)255); // Max alpha
        blockage.getArgs().add((short)0); // R/frame
        blockage.getArgs().add((short)0); // Max R
        blockage.getArgs().add((short)0); // G/frame
        blockage.getArgs().add((short)0); // Max G
        blockage.getArgs().add((short)0); // B/frame
        blockage.getArgs().add((short)0); // Max B
        blockage.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        blockage.getArgs().add((short)1); // not0?
        pushableBlock.getObjectContainer().getObjects().add(blockage);

        // Update existing pushable block to require Glove
        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x0a8);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 2);
        pushableBlock.getTestByteOperations().add(testByteOperation);
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
     * Add a timer.
     * @param objectContainer the screen to add the objects to
     */
    public static void addRuinGloveTimer(ObjectContainer objectContainer) {
        GameObject ruinGloveTimer = new GameObject(objectContainer);
        ruinGloveTimer.setId((short) 0x0b);
        ruinGloveTimer.getArgs().add((short) 0);
        ruinGloveTimer.getArgs().add((short) 0);
        ruinGloveTimer.setX(-1);
        ruinGloveTimer.setY(-1);

        TestByteOperation timerFlagTest = new TestByteOperation();
        timerFlagTest.setIndex(0x28d);
        timerFlagTest.setValue((byte) 1);
        timerFlagTest.setOp(ByteOp.FLAG_EQUALS);
        ruinGloveTimer.getTestByteOperations().add(timerFlagTest);

        timerFlagTest = new TestByteOperation();
        timerFlagTest.setIndex(0x0a8);
        timerFlagTest.setOp(ByteOp.FLAG_EQUALS);
        timerFlagTest.setValue((byte) 2);
        ruinGloveTimer.getTestByteOperations().add(timerFlagTest);

        timerFlagTest = new TestByteOperation();
        timerFlagTest.setIndex(0xacc);
        timerFlagTest.setOp(ByteOp.FLAG_EQUALS);
        timerFlagTest.setValue((byte) 0);
        ruinGloveTimer.getTestByteOperations().add(timerFlagTest);

        WriteByteOperation ruinGloveTimerUpdate = new WriteByteOperation();
        ruinGloveTimerUpdate.setIndex(0xacc);
        ruinGloveTimerUpdate.setValue((byte) 1);
        ruinGloveTimerUpdate.setOp(ByteOp.ASSIGN_FLAG);
        ruinGloveTimer.getWriteByteOperations().add(ruinGloveTimerUpdate);

        objectContainer.getObjects().add(0, ruinGloveTimer);
    }

    /**
     * Add a timer to automatically start hard mode.
     * @param screen the screen to add the objects to
     */
    public static void addAutomaticHardmodeTimer(Screen screen) {
        GameObject automaticHardmodeTimer = new GameObject(screen);
        automaticHardmodeTimer.setId((short) 0x0b);
        automaticHardmodeTimer.getArgs().add((short) 0);
        automaticHardmodeTimer.getArgs().add((short) 0);
        automaticHardmodeTimer.setX(-1);
        automaticHardmodeTimer.setY(-1);

        TestByteOperation automaticHardModeTimerFlagTest = new TestByteOperation();
        automaticHardModeTimerFlagTest.setIndex(362);
        automaticHardModeTimerFlagTest.setValue((byte) 2);
        automaticHardModeTimerFlagTest.setOp(ByteOp.FLAG_NOT_EQUAL);
        automaticHardmodeTimer.getTestByteOperations().add(automaticHardModeTimerFlagTest);

        WriteByteOperation automaticHardModeTimerFlagUpdate = new WriteByteOperation();
        automaticHardModeTimerFlagUpdate.setIndex(362);
        automaticHardModeTimerFlagUpdate.setValue((byte) 2);
        automaticHardModeTimerFlagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        automaticHardmodeTimer.getWriteByteOperations().add(automaticHardModeTimerFlagUpdate);

        screen.getObjects().add(0, automaticHardmodeTimer);
    }

    /**
     * Add a timer to automatically learn ancient La-Mulanese.
     * @param screen the screen to add the objects to
     */
    public static void addAutomaticTranslationsTimer(Screen screen) {
        GameObject automaticTranslationTimer = new GameObject(screen);
        automaticTranslationTimer.setId((short) 0x0b);
        automaticTranslationTimer.getArgs().add((short) 0);
        automaticTranslationTimer.getArgs().add((short) 0);
        automaticTranslationTimer.setX(-1);
        automaticTranslationTimer.setY(-1);

        TestByteOperation automaticTranslationTimerFlagTest = new TestByteOperation();
        automaticTranslationTimerFlagTest.setIndex(746);
        automaticTranslationTimerFlagTest.setValue((byte) 0);
        automaticTranslationTimerFlagTest.setOp(ByteOp.FLAG_EQUALS);
        automaticTranslationTimer.getTestByteOperations().add(automaticTranslationTimerFlagTest);

        WriteByteOperation automaticTranslationTimerFlagUpdate = new WriteByteOperation();
        automaticTranslationTimerFlagUpdate.setIndex(741);
        automaticTranslationTimerFlagUpdate.setValue((byte) 3);
        automaticTranslationTimerFlagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        automaticTranslationTimer.getWriteByteOperations().add(automaticTranslationTimerFlagUpdate);

        automaticTranslationTimerFlagUpdate = new WriteByteOperation();
        automaticTranslationTimerFlagUpdate.setIndex(746);
        automaticTranslationTimerFlagUpdate.setValue((byte) 1);
        automaticTranslationTimerFlagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        automaticTranslationTimer.getWriteByteOperations().add(automaticTranslationTimerFlagUpdate);

        screen.getObjects().add(0, automaticTranslationTimer);
    }

    public static void addAutomaticMantrasTimer(ObjectContainer screen) {
        GameObject mantraTimer = new GameObject(screen);
        mantraTimer.setId((short)0x0b);
        mantraTimer.getArgs().add((short) 0);
        mantraTimer.getArgs().add((short) 0);
        mantraTimer.setX(-1);
        mantraTimer.setY(-1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(292);
        testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
        testByteOperation.setValue((byte)4);
        mantraTimer.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(292);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(4);
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
    }

    public static void addTransformedMrFishmanShopDoorGraphic(GameObject mrFishmanShopDoor) {
        GameObject backupFishNewDoorGraphic = new GameObject(mrFishmanShopDoor.getObjectContainer());
        backupFishNewDoorGraphic.getTestByteOperations().add(new TestByteOperation(0x197, ByteOp.FLAG_EQUALS, 3));
        backupFishNewDoorGraphic.getTestByteOperations().add(new TestByteOperation(0x0fe, ByteOp.FLAG_NOT_EQUAL, 3));

        backupFishNewDoorGraphic.getArgs().add((short)-1);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)260);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)40);
        backupFishNewDoorGraphic.getArgs().add((short)40);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)1);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)255);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);
        backupFishNewDoorGraphic.getArgs().add((short)0);

        backupFishNewDoorGraphic.setId((short) 0x93);
        backupFishNewDoorGraphic.setX(180);
        backupFishNewDoorGraphic.setY(1520);

        mrFishmanShopDoor.getObjectContainer().getObjects().add(backupFishNewDoorGraphic);
    }

    /**
     * Actually-used modification for backside door numbers.
     * @param backsideDoor the object we're decorating with graphics
     * @param bossNumber 1=Amphisbaena, 7=Baphomet, etc.
     */
    public static void addBossNumberGraphic(GameObject backsideDoor, int bossNumber, int mirrorCoverFlag) {
        GameObject doorNumberGraphic = new GameObject(backsideDoor.getObjectContainer());
        doorNumberGraphic.setId((short) 0x93);
        doorNumberGraphic.setX(backsideDoor.getX());
        doorNumberGraphic.setY(backsideDoor.getY() - 40);
        doorNumberGraphic.getArgs().add((short)-1); // Layer
        doorNumberGraphic.getArgs().add((short)7); // 01.effect.png for anything not 0-6?
        doorNumberGraphic.getArgs().add((short)(53 +  16 * (bossNumber - 1))); // Imagex
        doorNumberGraphic.getArgs().add((short)(209)); // Imagey
        doorNumberGraphic.getArgs().add((short)29); // dx
        doorNumberGraphic.getArgs().add((short)31); // dy
        doorNumberGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        doorNumberGraphic.getArgs().add((short)0); // Animation frames
        doorNumberGraphic.getArgs().add((short)1); // Pause frames
        doorNumberGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        doorNumberGraphic.getArgs().add((short)0); // Hittile to fill with
        doorNumberGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        doorNumberGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        doorNumberGraphic.getArgs().add((short)0); // Cycle colors t/f
        doorNumberGraphic.getArgs().add((short)0); // Alpha/frame
        doorNumberGraphic.getArgs().add((short)255); // Max alpha
        doorNumberGraphic.getArgs().add((short)0); // R/frame
        doorNumberGraphic.getArgs().add((short)0); // Max R
        doorNumberGraphic.getArgs().add((short)0); // G/frame
        doorNumberGraphic.getArgs().add((short)0); // Max G
        doorNumberGraphic.getArgs().add((short)0); // B/frame
        doorNumberGraphic.getArgs().add((short)0); // Max B
        doorNumberGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        doorNumberGraphic.getArgs().add((short)0); // not0?

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(mirrorCoverFlag);
        testByteOperation.setOp(ByteOp.FLAG_GT);
        testByteOperation.setValue((byte)0);
        doorNumberGraphic.getTestByteOperations().add(testByteOperation);

        backsideDoor.getObjectContainer().getObjects().add(doorNumberGraphic);

        GameObject doorCoverup = new GameObject(backsideDoor.getObjectContainer());
        doorCoverup.setId((short) 0x93);
        doorCoverup.setX(backsideDoor.getX());
        doorCoverup.setY(backsideDoor.getY() - 40);
        doorCoverup.getArgs().add((short)0); // Layer
        doorCoverup.getArgs().add((short)0); // 01.effect.png for anything not 0-6?
        doorCoverup.getArgs().add((short)(940)); // Imagex
//        doorNumberGraphic.getArgs().add((short)(478)); // Imagey
        doorCoverup.getArgs().add((short)(0)); // Imagey
        doorCoverup.getArgs().add((short)11); // dx
//        doorCoverup.getArgs().add((short)32); // dy
        doorCoverup.getArgs().add((short)33); // dy
        doorCoverup.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        doorCoverup.getArgs().add((short)0); // Animation frames
        doorCoverup.getArgs().add((short)1); // Pause frames
        doorCoverup.getArgs().add((short)0); // Repeat count (<1 is forever)
        doorCoverup.getArgs().add((short)0); // Hittile to fill with
        doorCoverup.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        doorCoverup.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        doorCoverup.getArgs().add((short)0); // Cycle colors t/f
        doorCoverup.getArgs().add((short)0); // Alpha/frame
        doorCoverup.getArgs().add((short)255); // Max alpha
        doorCoverup.getArgs().add((short)0); // R/frame
        doorCoverup.getArgs().add((short)0); // Max R
        doorCoverup.getArgs().add((short)0); // G/frame
        doorCoverup.getArgs().add((short)0); // Max G
        doorCoverup.getArgs().add((short)0); // B/frame
        doorCoverup.getArgs().add((short)0); // Max B
        doorCoverup.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        doorCoverup.getArgs().add((short)0); // not0?

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(mirrorCoverFlag);
        testByteOperation.setOp(ByteOp.FLAG_GT);
        testByteOperation.setValue((byte)0);
        doorCoverup.getTestByteOperations().add(testByteOperation);

        backsideDoor.getObjectContainer().getObjects().add(doorCoverup);

        doorCoverup = new GameObject(backsideDoor.getObjectContainer());
        doorCoverup.setId((short) 0x93);
        doorCoverup.setX(backsideDoor.getX());
        doorCoverup.setY(backsideDoor.getY() - 40);
        doorCoverup.getArgs().add((short)0); // Layer
        doorCoverup.getArgs().add((short)0); // 01.effect.png for anything not 0-6?
        doorCoverup.getArgs().add((short)(940)); // Imagex
//        doorNumberGraphic.getArgs().add((short)(478)); // Imagey
        doorCoverup.getArgs().add((short)(0)); // Imagey
        doorCoverup.getArgs().add((short)29); // dx
        doorCoverup.getArgs().add((short)11); // dy
        doorCoverup.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        doorCoverup.getArgs().add((short)0); // Animation frames
        doorCoverup.getArgs().add((short)1); // Pause frames
        doorCoverup.getArgs().add((short)0); // Repeat count (<1 is forever)
        doorCoverup.getArgs().add((short)0); // Hittile to fill with
        doorCoverup.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        doorCoverup.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        doorCoverup.getArgs().add((short)0); // Cycle colors t/f
        doorCoverup.getArgs().add((short)0); // Alpha/frame
        doorCoverup.getArgs().add((short)255); // Max alpha
        doorCoverup.getArgs().add((short)0); // R/frame
        doorCoverup.getArgs().add((short)0); // Max R
        doorCoverup.getArgs().add((short)0); // G/frame
        doorCoverup.getArgs().add((short)0); // Max G
        doorCoverup.getArgs().add((short)0); // B/frame
        doorCoverup.getArgs().add((short)0); // Max B
        doorCoverup.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        doorCoverup.getArgs().add((short)0); // not0?

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(mirrorCoverFlag);
        testByteOperation.setOp(ByteOp.FLAG_GT);
        testByteOperation.setValue((byte)0);
        doorCoverup.getTestByteOperations().add(testByteOperation);

        backsideDoor.getObjectContainer().getObjects().add(doorCoverup);
    }

    /**
     * Actually-used modification for backside door numbers.
     * @param backsideDoor the object we're decorating with graphics
     * @param bossNumber 1=Amphisbaena, 7=Baphomet, etc.
     */
    public static void addBossNumberGraphicV2(GameObject backsideDoor, int bossNumber, Integer mirrorCoverFlag) {
        GameObject doorNumberGraphic = new GameObject(backsideDoor.getObjectContainer());
        doorNumberGraphic.setId((short) 0x93);
        doorNumberGraphic.setX(backsideDoor.getX() - 20);
        doorNumberGraphic.setY(backsideDoor.getY() - 40);
        doorNumberGraphic.getArgs().add((short)0); // Layer
        doorNumberGraphic.getArgs().add((short)7); // 01.effect.png for anything not 0-6?
        if(bossNumber == 9) {
            doorNumberGraphic.getArgs().add((short)(50 * 9)); // Imagex
        }
        else {
            doorNumberGraphic.getArgs().add((short)(50 * (bossNumber - 1))); // Imagex
        }
        doorNumberGraphic.getArgs().add((short)592); // Imagey
        doorNumberGraphic.getArgs().add((short)50); // dx
        doorNumberGraphic.getArgs().add((short)36); // dy
        doorNumberGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        doorNumberGraphic.getArgs().add((short)0); // Animation frames
        doorNumberGraphic.getArgs().add((short)1); // Pause frames
        doorNumberGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        doorNumberGraphic.getArgs().add((short)0); // Hittile to fill with
        doorNumberGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        doorNumberGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        doorNumberGraphic.getArgs().add((short)0); // Cycle colors t/f
        doorNumberGraphic.getArgs().add((short)0); // Alpha/frame
        doorNumberGraphic.getArgs().add((short)255); // Max alpha
        doorNumberGraphic.getArgs().add((short)0); // R/frame
        doorNumberGraphic.getArgs().add((short)0); // Max R
        doorNumberGraphic.getArgs().add((short)0); // G/frame
        doorNumberGraphic.getArgs().add((short)0); // Max G
        doorNumberGraphic.getArgs().add((short)0); // B/frame
        doorNumberGraphic.getArgs().add((short)0); // Max B
        doorNumberGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        doorNumberGraphic.getArgs().add((short)0); // not0?

        if(mirrorCoverFlag != null) {
            TestByteOperation testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(mirrorCoverFlag);
            testByteOperation.setOp(ByteOp.FLAG_GT);
            testByteOperation.setValue((byte)0);
            doorNumberGraphic.getTestByteOperations().add(testByteOperation);
        }

        backsideDoor.getObjectContainer().getObjects().add(doorNumberGraphic);
    }

    public static void addNumberlessBacksideDoorGraphic(GameObject backsideDoor) {
        int zoneIndex = ((Screen)backsideDoor.getObjectContainer()).getZoneIndex();
        if(zoneIndex == 19) {
            return;
//            doorCoverGraphic.getArgs().add((short)320); // Imagex
        }

        GameObject doorCoverGraphic = new GameObject(backsideDoor.getObjectContainer());
        doorCoverGraphic.setId((short)0x93);
        doorCoverGraphic.setX(backsideDoor.getX() - 20);
        doorCoverGraphic.setY(backsideDoor.getY() - 40);
        doorCoverGraphic.getArgs().add((short)-1); // Layer
        doorCoverGraphic.getArgs().add((short)7); // 01.effect.png for anything not 0-6?

        if(zoneIndex == 6) {
            doorCoverGraphic.getArgs().add((short)80); // Imagex
        }
        else if(zoneIndex == 10) {
            doorCoverGraphic.getArgs().add((short)160); // Imagex
        }
        else if(zoneIndex == 13) {
            doorCoverGraphic.getArgs().add((short)240); // Imagex
        }
        else {
            doorCoverGraphic.getArgs().add((short)0); // Imagex
        }

        doorCoverGraphic.getArgs().add((short)512); // Imagey

        doorCoverGraphic.getArgs().add((short)80); // dx
        doorCoverGraphic.getArgs().add((short)80); // dy
        doorCoverGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        doorCoverGraphic.getArgs().add((short)0); // Animation frames
        doorCoverGraphic.getArgs().add((short)1); // Pause frames
        doorCoverGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        doorCoverGraphic.getArgs().add((short)0); // Hittile to fill with
        doorCoverGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        doorCoverGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        doorCoverGraphic.getArgs().add((short)0); // Cycle colors t/f
        doorCoverGraphic.getArgs().add((short)0); // Alpha/frame
        doorCoverGraphic.getArgs().add((short)255); // Max alpha
        doorCoverGraphic.getArgs().add((short)0); // R/frame
        doorCoverGraphic.getArgs().add((short)0); // Max R
        doorCoverGraphic.getArgs().add((short)0); // G/frame
        doorCoverGraphic.getArgs().add((short)0); // Max G
        doorCoverGraphic.getArgs().add((short)0); // B/frame
        doorCoverGraphic.getArgs().add((short)0); // Max B
        doorCoverGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        doorCoverGraphic.getArgs().add((short)1); // not0?

        backsideDoor.getObjectContainer().getObjects().add(doorCoverGraphic);
    }

    public static GameObject addAnimatedDoorCover(GameObject backsideDoor, int gateFlag, boolean checkBronzeMirror) {
        GameObject doorCoverGraphic = new GameObject(backsideDoor.getObjectContainer());
        doorCoverGraphic.setId((short)0x93);
        doorCoverGraphic.setX(backsideDoor.getX());
        doorCoverGraphic.setY(backsideDoor.getY());
        doorCoverGraphic.getArgs().add((short)0); // Layer
        doorCoverGraphic.getArgs().add((short)7); // 01.effect.png for anything not 0-6?
        doorCoverGraphic.getArgs().add((short)0); // Imagex
        doorCoverGraphic.getArgs().add((short)360); // Imagey
        doorCoverGraphic.getArgs().add((short)40); // dx
        doorCoverGraphic.getArgs().add((short)40); // dy
        doorCoverGraphic.getArgs().add((short)1); // 0: act as if animation already played; 1: allow animation; 2: ..?
        doorCoverGraphic.getArgs().add((short)7); // Animation frames
        doorCoverGraphic.getArgs().add((short)6); // Pause frames
        doorCoverGraphic.getArgs().add((short)1); // Repeat count (<1 is forever)
        doorCoverGraphic.getArgs().add((short)0); // Hittile to fill with
        doorCoverGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        doorCoverGraphic.getArgs().add((short)5); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        doorCoverGraphic.getArgs().add((short)0); // Cycle colors t/f
        doorCoverGraphic.getArgs().add((short)0); // Alpha/frame
        doorCoverGraphic.getArgs().add((short)255); // Max alpha
        doorCoverGraphic.getArgs().add((short)0); // R/frame
        doorCoverGraphic.getArgs().add((short)0); // Max R
        doorCoverGraphic.getArgs().add((short)0); // G/frame
        doorCoverGraphic.getArgs().add((short)0); // Max G
        doorCoverGraphic.getArgs().add((short)0); // B/frame
        doorCoverGraphic.getArgs().add((short)0); // Max B
        doorCoverGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        doorCoverGraphic.getArgs().add((short)1); // not0?

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(gateFlag);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)0);
        doorCoverGraphic.getTestByteOperations().add(testFlag);

        if(checkBronzeMirror) {
            testFlag = new TestByteOperation();
            testFlag.setIndex(0x0ae);
            testFlag.setOp(ByteOp.FLAG_EQUALS);
            testFlag.setValue((byte)2);
            doorCoverGraphic.getTestByteOperations().add(testFlag);
        }

        WriteByteOperation updateFlag = new WriteByteOperation();
        updateFlag.setIndex(gateFlag);
        updateFlag.setValue((byte) 1);
        updateFlag.setOp(ByteOp.ASSIGN_FLAG);
        doorCoverGraphic.getWriteByteOperations().add(updateFlag);

        backsideDoor.getObjectContainer().getObjects().add(doorCoverGraphic);
        return doorCoverGraphic;
    }

    /**
     * Add fairy point to reveal backside door
     * @param backsideDoor to add key fairy point for
     */
    public static void addBacksideDoorKeyFairyPoint(GameObject backsideDoor) {
        GameObject obj = new GameObject(backsideDoor.getObjectContainer());
        obj.setId((short)0xa7);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)40);
        obj.getArgs().add((short)40);
        obj.setX(backsideDoor.getX());
        obj.setY(backsideDoor.getY() - 40);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x1c9);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x1c9);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x029);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x38c);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        backsideDoor.getObjectContainer().getObjects().add(obj);
    }

    public static void addMirrorCoverTimerAndSound(ObjectContainer objectContainer, int mirrorCoverFlag) {
        GameObject mirrorTimer = new GameObject(objectContainer);
        mirrorTimer.setId((short) 0x0b);
        mirrorTimer.getArgs().add((short) 0);
        mirrorTimer.getArgs().add((short) 30);
        mirrorTimer.setX(-1);
        mirrorTimer.setY(-1);

        TestByteOperation mirrorTimerTest = new TestByteOperation();
        mirrorTimerTest.setIndex(0x0ae);
        mirrorTimerTest.setValue((byte) 2);
        mirrorTimerTest.setOp(ByteOp.FLAG_EQUALS);
        mirrorTimer.getTestByteOperations().add(mirrorTimerTest);

        mirrorTimerTest = new TestByteOperation();
        mirrorTimerTest.setIndex(mirrorCoverFlag);
        mirrorTimerTest.setValue((byte) 0);
        mirrorTimerTest.setOp(ByteOp.FLAG_EQUALS);
        mirrorTimer.getTestByteOperations().add(mirrorTimerTest);

        WriteByteOperation mirrorTimerUpdate = new WriteByteOperation();
        mirrorTimerUpdate.setIndex(mirrorCoverFlag);
        mirrorTimerUpdate.setValue((byte) 1);
        mirrorTimerUpdate.setOp(ByteOp.ASSIGN_FLAG);
        mirrorTimer.getWriteByteOperations().add(mirrorTimerUpdate);

        mirrorTimerUpdate = new WriteByteOperation();
        mirrorTimerUpdate.setIndex(0x028);
        mirrorTimerUpdate.setValue((byte) 1);
        mirrorTimerUpdate.setOp(ByteOp.ASSIGN_FLAG);
        mirrorTimer.getWriteByteOperations().add(mirrorTimerUpdate);

        int zoneIndex = ((Screen)objectContainer).getZoneIndex();
        int roomIndex = ((Screen)objectContainer).getRoomIndex();

        GameObject mirrorSoundEffect = new GameObject(objectContainer);
        mirrorSoundEffect.setId((short)0x9b);
        mirrorSoundEffect.getArgs().add((short)48); // sound effect
        mirrorSoundEffect.getArgs().add((short)127); // volume initial
        mirrorSoundEffect.getArgs().add(getMirrorCoverSoundBalance(zoneIndex, roomIndex)); // balance initial
        mirrorSoundEffect.getArgs().add((short)-700); // pitch initial
        mirrorSoundEffect.getArgs().add((short)127); // volume final
        mirrorSoundEffect.getArgs().add(getMirrorCoverSoundBalance(zoneIndex, roomIndex)); // balance final
        mirrorSoundEffect.getArgs().add((short)-700); // pitch final
        mirrorSoundEffect.getArgs().add((short)20); // voice priority
        mirrorSoundEffect.getArgs().add((short)0); // UNUSED????
        mirrorSoundEffect.getArgs().add((short)0); // frames to wait before playing
        mirrorSoundEffect.getArgs().add((short)0); // vibrate (bool)
        mirrorSoundEffect.getArgs().add((short)10); // ??? vibration strength
        mirrorSoundEffect.getArgs().add((short)10); // volume slide frames
        mirrorSoundEffect.getArgs().add((short)0); // balance slide frames
        mirrorSoundEffect.getArgs().add((short)0); // pitch slide frames
        mirrorSoundEffect.setX(-1);
        mirrorSoundEffect.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(0x028);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)1);
        mirrorSoundEffect.getTestByteOperations().add(testFlag);

        objectContainer.getObjects().add(0, mirrorSoundEffect);
        objectContainer.getObjects().add(0, mirrorTimer);
    }

    public static GameObject addAnimatedDoorTimerAndSound(ObjectContainer objectContainer, int bossFlag, int gateFlag) {
        GameObject doorTimer = new GameObject(objectContainer);
        doorTimer.setId((short) 0x0b);
        doorTimer.getArgs().add((short) 1);
        doorTimer.getArgs().add((short) 0);
        doorTimer.setX(-1);
        doorTimer.setY(-1);

        TestByteOperation doorTimerTest = new TestByteOperation();
        doorTimerTest.setIndex(bossFlag);
        doorTimerTest.setValue((byte) 3);
        doorTimerTest.setOp(ByteOp.FLAG_GTEQ);
        doorTimer.getTestByteOperations().add(doorTimerTest);

        doorTimerTest = new TestByteOperation();
        doorTimerTest.setIndex(0x0ae);
        doorTimerTest.setValue((byte) 2);
        doorTimerTest.setOp(ByteOp.FLAG_EQUALS);
        doorTimer.getTestByteOperations().add(doorTimerTest);

        doorTimerTest = new TestByteOperation();
        doorTimerTest.setIndex(gateFlag);
        doorTimerTest.setValue((byte) 0);
        doorTimerTest.setOp(ByteOp.FLAG_EQUALS);
        doorTimer.getTestByteOperations().add(doorTimerTest);

        WriteByteOperation doorTimerUpdate = new WriteByteOperation();
        doorTimerUpdate.setIndex(gateFlag);
        doorTimerUpdate.setValue((byte) 1);
        doorTimerUpdate.setOp(ByteOp.ASSIGN_FLAG);
        doorTimer.getWriteByteOperations().add(doorTimerUpdate);

        doorTimerUpdate = new WriteByteOperation();
        doorTimerUpdate.setIndex(0x029);
        doorTimerUpdate.setValue((byte) 1);
        doorTimerUpdate.setOp(ByteOp.ASSIGN_FLAG);
        doorTimer.getWriteByteOperations().add(doorTimerUpdate);

        int zoneIndex = ((Screen)objectContainer).getZoneIndex();
        int roomIndex = ((Screen)objectContainer).getRoomIndex();

        GameObject doorSoundEffect = new GameObject(objectContainer);
        doorSoundEffect.setId((short)0x9b);
        doorSoundEffect.getArgs().add((short)95);
        doorSoundEffect.getArgs().add((short)127);
        doorSoundEffect.getArgs().add(getAnimatedDoorSoundBalance(zoneIndex, roomIndex)); // balance initial
        doorSoundEffect.getArgs().add((short)-400);
        doorSoundEffect.getArgs().add((short)127);
        doorSoundEffect.getArgs().add(getAnimatedDoorSoundBalance(zoneIndex, roomIndex)); // balance final
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)15);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)10);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.setX(-1);
        doorSoundEffect.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(0x029);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)1);
        doorSoundEffect.getTestByteOperations().add(testFlag);

        objectContainer.getObjects().add(0, doorSoundEffect);
        objectContainer.getObjects().add(0, doorTimer);
        return doorTimer;
    }

    public static GameObject addKeyFairyDoorTimerAndSounds(ObjectContainer objectContainer) {
        GameObject keyFairyTimer = new GameObject(objectContainer);
        keyFairyTimer.setX(-1);
        keyFairyTimer.setY(-1);
        keyFairyTimer.getArgs().add((short)0);
        keyFairyTimer.getArgs().add((short)0);

        TestByteOperation keyFairyTimerTest = new TestByteOperation();
        keyFairyTimerTest.setIndex(0x38c);
        keyFairyTimerTest.setValue((byte)1);
        keyFairyTimerTest.setOp(ByteOp.FLAG_EQUALS);
        keyFairyTimer.getTestByteOperations().add(keyFairyTimerTest);

        WriteByteOperation keyFairyTimerUpdate = new WriteByteOperation();
        keyFairyTimerUpdate.setIndex(0x38c);
        keyFairyTimerUpdate.setValue((byte)2);
        keyFairyTimerUpdate.setOp(ByteOp.ASSIGN_FLAG);
        keyFairyTimer.getWriteByteOperations().add(keyFairyTimerUpdate);

        keyFairyTimerUpdate = new WriteByteOperation();
        keyFairyTimerUpdate.setIndex(0x386);
        keyFairyTimerUpdate.setValue((byte)1);
        keyFairyTimerUpdate.setOp(ByteOp.ADD_FLAG);
        keyFairyTimer.getWriteByteOperations().add(keyFairyTimerUpdate);

        GameObject shellHornSound = new GameObject(objectContainer);
        shellHornSound.setId((short) 0x9b);
        shellHornSound.setX(-1);
        shellHornSound.setY(-1);

        shellHornSound.getArgs().add((short)30);
        shellHornSound.getArgs().add((short)120);
        shellHornSound.getArgs().add((short)64);
        shellHornSound.getArgs().add((short)0);
        shellHornSound.getArgs().add((short)120);
        shellHornSound.getArgs().add((short)64);
        shellHornSound.getArgs().add((short)0);
        shellHornSound.getArgs().add((short)25);
        shellHornSound.getArgs().add((short)1);
        shellHornSound.getArgs().add((short)5);
        shellHornSound.getArgs().add((short)0);
        shellHornSound.getArgs().add((short)10);
        shellHornSound.getArgs().add((short)0);
        shellHornSound.getArgs().add((short)0);
        shellHornSound.getArgs().add((short)0);

        TestByteOperation shellHornSoundTest = new TestByteOperation();
        shellHornSoundTest.setIndex(0x0a7);
        shellHornSoundTest.setValue((byte)2);
        shellHornSoundTest.setOp(ByteOp.FLAG_EQUALS);
        shellHornSound.getTestByteOperations().add(shellHornSoundTest);

        shellHornSoundTest = new TestByteOperation();
        shellHornSoundTest.setIndex(0x1c9);
        shellHornSoundTest.setValue((byte)1);
        shellHornSoundTest.setOp(ByteOp.FLAG_EQUALS);
        shellHornSound.getTestByteOperations().add(shellHornSoundTest);

        shellHornSoundTest = new TestByteOperation();
        shellHornSoundTest.setIndex(0x029);
        shellHornSoundTest.setValue((byte)1);
        shellHornSoundTest.setOp(ByteOp.FLAG_EQUALS);
        shellHornSound.getTestByteOperations().add(shellHornSoundTest);

        int zoneIndex = ((Screen)objectContainer).getZoneIndex();
        int roomIndex = ((Screen)objectContainer).getRoomIndex();

        GameObject animatedDoorSound = new GameObject(objectContainer);
        animatedDoorSound.setId((short) 0x9b);
        animatedDoorSound.setX(-1);
        animatedDoorSound.setY(-1);

        animatedDoorSound.getArgs().add((short)95);
        animatedDoorSound.getArgs().add((short)127);
        animatedDoorSound.getArgs().add(getAnimatedDoorSoundBalance(zoneIndex, roomIndex)); // balance initial
        animatedDoorSound.getArgs().add((short)-400);
        animatedDoorSound.getArgs().add((short)127);
        animatedDoorSound.getArgs().add(getAnimatedDoorSoundBalance(zoneIndex, roomIndex)); // balance final
        animatedDoorSound.getArgs().add((short)0);
        animatedDoorSound.getArgs().add((short)15);
        animatedDoorSound.getArgs().add((short)0);
        animatedDoorSound.getArgs().add((short)0);
        animatedDoorSound.getArgs().add((short)0);
        animatedDoorSound.getArgs().add((short)10);
        animatedDoorSound.getArgs().add((short)0);
        animatedDoorSound.getArgs().add((short)0);
        animatedDoorSound.getArgs().add((short)0);

        TestByteOperation animatedDoorSoundTest = new TestByteOperation();
        animatedDoorSoundTest.setIndex(0x029);
        animatedDoorSoundTest.setValue((byte)1);
        animatedDoorSoundTest.setOp(ByteOp.FLAG_EQUALS);
        animatedDoorSound.getTestByteOperations().add(animatedDoorSoundTest);

        objectContainer.getObjects().add(0, animatedDoorSound);
        objectContainer.getObjects().add(0, shellHornSound);
        return shellHornSound;
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
        GameObject mirrorCoverGraphic = new GameObject(backsideDoor.getObjectContainer());
        mirrorCoverGraphic.setId((short) 0x93);
        mirrorCoverGraphic.setX(xPos);
        mirrorCoverGraphic.setY(yPos);
        mirrorCoverGraphic.getArgs().add(layer); // Layer
        mirrorCoverGraphic.getArgs().add(fileEnum); // 01.effect.png for anything not 0-6?
        mirrorCoverGraphic.getArgs().add(imageX); // Imagex
        mirrorCoverGraphic.getArgs().add(imageY); // Imagey
        mirrorCoverGraphic.getArgs().add(width); // dx
        mirrorCoverGraphic.getArgs().add(height); // dy
        mirrorCoverGraphic.getArgs().add((short)1); // 0: act as if animation already played; 1: allow animation; 2: ..?
        mirrorCoverGraphic.getArgs().add((short)1); // Animation frames
        mirrorCoverGraphic.getArgs().add((short)4); // Pause frames
        mirrorCoverGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        mirrorCoverGraphic.getArgs().add((short)0); // Hittile to fill with
        mirrorCoverGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        mirrorCoverGraphic.getArgs().add((short)1); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        mirrorCoverGraphic.getArgs().add((short)0); // Cycle colors t/f
        mirrorCoverGraphic.getArgs().add((short)0); // Alpha/frame
        mirrorCoverGraphic.getArgs().add((short)255); // Max alpha
        mirrorCoverGraphic.getArgs().add((short)0); // R/frame
        mirrorCoverGraphic.getArgs().add((short)0); // Max R
        mirrorCoverGraphic.getArgs().add((short)0); // G/frame
        mirrorCoverGraphic.getArgs().add((short)0); // Max G
        mirrorCoverGraphic.getArgs().add((short)0); // B/frame
        mirrorCoverGraphic.getArgs().add((short)0); // Max B
        mirrorCoverGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        mirrorCoverGraphic.getArgs().add((short)1); // not0?

        TestByteOperation mirrorCoverTest = new TestByteOperation();
        mirrorCoverTest.setIndex(mirrorCoverFlag);
        mirrorCoverTest.setValue((byte) 0);
        mirrorCoverTest.setOp(ByteOp.FLAG_EQUALS);
        mirrorCoverGraphic.getTestByteOperations().add(mirrorCoverTest);

        backsideDoor.getObjectContainer().getObjects().add(mirrorCoverGraphic);
    }

    public static void addGrailToggle(ObjectContainer objectContainer, boolean enableGrail) {
        if(objectContainer == null) {
            objectContainer = dimensionalExitScreen;
        }
        GameObject grailToggle = new GameObject(objectContainer);
        grailToggle.setId((short)0xb7);
        grailToggle.setX(-1);
        grailToggle.setY(-1);
        grailToggle.getArgs().add((short)(enableGrail ? 1 : 0));

        // Disable during escape
        grailToggle.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0));

        objectContainer.getObjects().add(0, grailToggle);
    }

    public static GameObject addAltSurfaceShopItemTimer(ObjectContainer objectContainer) {
        // Handles the case where the shop item can be obtained somewhere else and you already have it.
        // Without this timer, the shop could potentially be unable to transform back to its original state.
        GameObject altSurfaceShopTimer = new GameObject(objectContainer);
        altSurfaceShopTimer.setId((short) 0x0b);
        altSurfaceShopTimer.getArgs().add((short) 0);
        altSurfaceShopTimer.getArgs().add((short) 0);
        altSurfaceShopTimer.setX(-1);
        altSurfaceShopTimer.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(742);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)1);
        altSurfaceShopTimer.getTestByteOperations().add(testFlag);

        WriteByteOperation updateFlag = new WriteByteOperation();
        updateFlag.setIndex(742);
        updateFlag.setValue((byte) 2);
        updateFlag.setOp(ByteOp.ASSIGN_FLAG);
        altSurfaceShopTimer.getWriteByteOperations().add(updateFlag);

        objectContainer.getObjects().add(0, altSurfaceShopTimer);
        return altSurfaceShopTimer;
    }

    public static void addLittleBrotherShopTimer(short shopItemFlag) {
        // Sets item world flags from 1 to 2, since one of the shop flags is taken up by checking the Big Brother shop trigger.
        GameObject littleBrotherShopItemTimer = new GameObject(littleBrotherShopScreen);
        littleBrotherShopItemTimer.setId((short) 0x0b);
        littleBrotherShopItemTimer.getArgs().add((short) 0);
        littleBrotherShopItemTimer.getArgs().add((short) 0);
        littleBrotherShopItemTimer.setX(-1);
        littleBrotherShopItemTimer.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(shopItemFlag);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)1);
        littleBrotherShopItemTimer.getTestByteOperations().add(testFlag);

        WriteByteOperation updateFlag = new WriteByteOperation();
        updateFlag.setIndex(shopItemFlag);
        updateFlag.setValue((byte) 2);
        updateFlag.setOp(ByteOp.ASSIGN_FLAG);
        littleBrotherShopItemTimer.getWriteByteOperations().add(updateFlag);

        littleBrotherShopScreen.getObjects().add(0, littleBrotherShopItemTimer);
    }

    /**
     * Detect Lemeza entering the lower surface area and get rid of the cover so you can see.
     * @param screen
     */
    public static void addSurfaceCoverDetector(ObjectContainer screen) {
        GameObject surfaceCoverDetector = new GameObject(screen);
        surfaceCoverDetector.setId((short)0x14);
        surfaceCoverDetector.setX(420);
        surfaceCoverDetector.setY(1340);

        surfaceCoverDetector.getArgs().add((short)0); // seconds wait
        surfaceCoverDetector.getArgs().add((short)0); // frames wait
        surfaceCoverDetector.getArgs().add((short)0); // continuous/total
        surfaceCoverDetector.getArgs().add((short)0); // interaction type 0 = any time except paused 1 = 2 = 3 = 4 = just be on the ground, ok. default: sleep

        surfaceCoverDetector.getArgs().add((short)3); // graphical tile width
        surfaceCoverDetector.getArgs().add((short)5); // graphical tile height

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x14c);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        surfaceCoverDetector.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x14c);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(1);
        surfaceCoverDetector.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(surfaceCoverDetector);
    }

    /**
     * Add Diary updated timer to Xelpud's screen.
     * @param objectContainer the screen to add the objects to
     */
    public static void addDiaryTalismanConversationTimers(ObjectContainer objectContainer) {
        // Timer to trigger Xelpud Diary conversation (gives Mulana Talisman) if you enter his screen with the Diary.
        GameObject diaryFlagTimer = new GameObject(objectContainer);
        diaryFlagTimer.setId((short) 0x0b);
        diaryFlagTimer.getArgs().add((short) 0);
        diaryFlagTimer.getArgs().add((short) 0);
        diaryFlagTimer.setX(-1);
        diaryFlagTimer.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(260);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)2);
        diaryFlagTimer.getTestByteOperations().add(testFlag);

        testFlag = new TestByteOperation();
        testFlag.setIndex(2797);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)0);
        diaryFlagTimer.getTestByteOperations().add(testFlag);

        WriteByteOperation updateFlag = new WriteByteOperation();
        updateFlag.setIndex(2797);
        updateFlag.setValue((byte) 1);
        updateFlag.setOp(ByteOp.ASSIGN_FLAG);
        diaryFlagTimer.getWriteByteOperations().add(updateFlag);

        objectContainer.getObjects().add(0, diaryFlagTimer);

        // Timer to trigger Xelpud Talisman conversation (allows Diary chest appearance) if you enter his screen with the Talisman.
        GameObject xelpudTalismanConversationTimer = new GameObject(objectContainer);
        xelpudTalismanConversationTimer.setId((short) 0x0b);
        xelpudTalismanConversationTimer.getArgs().add((short) 0);
        xelpudTalismanConversationTimer.getArgs().add((short) 0);
        xelpudTalismanConversationTimer.setX(-1);
        xelpudTalismanConversationTimer.setY(-1);

        testFlag = new TestByteOperation();
        testFlag.setIndex(164);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)2);
        xelpudTalismanConversationTimer.getTestByteOperations().add(testFlag);

        testFlag = new TestByteOperation();
        testFlag.setIndex(2796);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)0);
        xelpudTalismanConversationTimer.getTestByteOperations().add(testFlag);

        testFlag = new TestByteOperation();
        testFlag.setIndex(0x07c);
        testFlag.setValue((byte) 1);
        testFlag.setOp(ByteOp.FLAG_GTEQ);
        xelpudTalismanConversationTimer.getTestByteOperations().add(testFlag);

        updateFlag = new WriteByteOperation();
        updateFlag.setIndex(2796);
        updateFlag.setValue((byte) 1);
        updateFlag.setOp(ByteOp.ASSIGN_FLAG);
        xelpudTalismanConversationTimer.getWriteByteOperations().add(updateFlag);

        objectContainer.getObjects().add(0, xelpudTalismanConversationTimer);
    }

    /**
     * Cheats for easy testing. Gives Shrine of the Mother frontside grail via a timer on the starting screen.
     * Other warps could be added for convenience as needed (0x064 = guidance, through 0x075 = backside shrine)
     * @param objectContainer screen to add the timers to
     */
    public static void addGrailWarpTimers(ObjectContainer objectContainer) {
        GameObject warpTimer = new GameObject(objectContainer);
        warpTimer.setId((short) 0x0b);
        warpTimer.getArgs().add((short) 0);
        warpTimer.getArgs().add((short) 0);
        warpTimer.setX(-1);
        warpTimer.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(0x06c);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)0);
        warpTimer.getTestByteOperations().add(testFlag);

        WriteByteOperation updateFlag = new WriteByteOperation();
        updateFlag.setIndex(0x06c);
        updateFlag.setValue((byte) 1);
        updateFlag.setOp(ByteOp.ASSIGN_FLAG);
        warpTimer.getWriteByteOperations().add(updateFlag);

        objectContainer.getObjects().add(0, warpTimer);
    }

    /**
     * Adds sound effect for removed/trap items
     * @param objectContainer to add the sound effect to
     * @param newWorldFlag flag to update once the sound has been played, so it will only play once
     * @param screenFlag screen flag (non-permanent) to indicate that the sound should be played
     */
    public static void addNoItemSoundEffect(ObjectContainer objectContainer, Integer newWorldFlag, Integer screenFlag) {
        GameObject noItemSoundEffect = new GameObject(objectContainer);
        noItemSoundEffect.setId((short)0x9b);
        noItemSoundEffect.getArgs().add((short)80);
        noItemSoundEffect.getArgs().add((short)120);
        noItemSoundEffect.getArgs().add((short)64);
        noItemSoundEffect.getArgs().add((short)0);
        noItemSoundEffect.getArgs().add((short)120);
        noItemSoundEffect.getArgs().add((short)64);
        noItemSoundEffect.getArgs().add((short)0);
        noItemSoundEffect.getArgs().add((short)25);
        noItemSoundEffect.getArgs().add((short)1);
        noItemSoundEffect.getArgs().add((short)5);
        noItemSoundEffect.getArgs().add((short)0);
        noItemSoundEffect.getArgs().add((short)10);
        noItemSoundEffect.getArgs().add((short)0);
        noItemSoundEffect.getArgs().add((short)0);
        noItemSoundEffect.getArgs().add((short)0);
        noItemSoundEffect.setX(-1);
        noItemSoundEffect.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(newWorldFlag);
        testFlag.setOp(ByteOp.FLAG_GT);
        testFlag.setValue((byte)0);
        noItemSoundEffect.getTestByteOperations().add(testFlag);

        testFlag = new TestByteOperation();
        testFlag.setIndex(screenFlag);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)1);
        noItemSoundEffect.getTestByteOperations().add(testFlag);

        objectContainer.getObjects().add(0, noItemSoundEffect);
    }

    public static void addBat(ObjectContainer objectContainer, int xPos, int yPos, int screenFlag) {
        GameObject bat = new GameObject(objectContainer);
        bat.setId((short)0x02);
        bat.setX(xPos);
        bat.setY(yPos);
        bat.getArgs().add((short)1);
        bat.getArgs().add((short)1);
        bat.getArgs().add((short)2);
        bat.getArgs().add((short)0);
        bat.getArgs().add((short)3);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(screenFlag);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        bat.getTestByteOperations().add(testByteOperation);

        objectContainer.getObjects().add(bat);
    }

    public static void addExplosion(ObjectContainer objectContainer, int xPos, int yPos, int explosionTriggerFlag, int damage, boolean percentDamage) {
        GameObject explosion = new GameObject(objectContainer);
        explosion.setId((short)0xb4);
        explosion.setX(xPos - 80);
        explosion.setY(yPos - 80);
        explosion.getArgs().add((short)200); // Width & Height
        explosion.getArgs().add((short)1); // Undocumented
        explosion.getArgs().add((short)6); // Frames No Animation
        explosion.getArgs().add((short)6); // Undocumented
        explosion.getArgs().add((short)(percentDamage ? 1 : 0)); // hp or %
        explosion.getArgs().add((short)damage); // Damage
        explosion.getArgs().add((short)85); // sound effect select

        explosion.getTestByteOperations().add(new TestByteOperation(explosionTriggerFlag, ByteOp.FLAG_EQUALS, 1));
        explosion.getWriteByteOperations().add(new WriteByteOperation(explosionTriggerFlag, ByteOp.ASSIGN_FLAG, 2));

        objectContainer.getObjects().add(explosion);
    }

    public static void addFoolsExplosion(ObjectContainer objectContainer, int xPos, int yPos, int newWorldFlag) {
        GameObject explosion = new GameObject(objectContainer);
        explosion.setId((short)0xb4);
        explosion.setX(640 * (xPos / 640));
        explosion.setY(480 * (yPos / 480));
        explosion.getArgs().add((short)640); // Width & Height
        explosion.getArgs().add((short)1); // Undocumented
        explosion.getArgs().add((short)6); // Frames No Animation
        explosion.getArgs().add((short)6); // Undocumented
        explosion.getArgs().add((short)1); // hp or %
        explosion.getArgs().add((short)60); // Damage
        explosion.getArgs().add((short)85); // sound effect select

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(newWorldFlag);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        explosion.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(newWorldFlag);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(2);
        explosion.getWriteByteOperations().add(writeByteOperation);

        objectContainer.getObjects().add(explosion);
    }

    /**
     * Add a pot to a screen
     * @param screen the screen to add the objects to
     * @param graphic
     */
    public static void addPot(ObjectContainer screen, int x, int y, int graphic, List<TestByteOperation> tests) {
        GameObject addedPot = new GameObject(screen);
        addedPot.setId((short) 0x0);
        addedPot.setX(x);
        addedPot.setY(y);

        addedPot.getArgs().add((short)0);
        addedPot.getArgs().add((short)0);
        addedPot.getArgs().add((short)-1);
        addedPot.getArgs().add((short)1);
        addedPot.getArgs().add((short)graphic);
        addedPot.getArgs().add((short)105);
        addedPot.getArgs().add((short)35);
        addedPot.getArgs().add((short)17);
        addedPot.getArgs().add((short)0);

        addedPot.getTestByteOperations().addAll(tests);

        screen.getObjects().add(addedPot);
    }

    public static void addMoonlightFeatherlessPlatform(Screen screen) {
        GameObject platform = new GameObject(screen);
        platform.setId((short)0x93);
        platform.setX(580);
        platform.setY(200);

        platform.getArgs().add((short)0); // Layer
        platform.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        platform.getArgs().add((short)0); // Imagex
        platform.getArgs().add((short)80); // Imagey
        platform.getArgs().add((short)40); // dx
        platform.getArgs().add((short)20); // dy
        platform.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        platform.getArgs().add((short)1); // Animation frames
        platform.getArgs().add((short)0); // Pause frames
        platform.getArgs().add((short)0); // Repeat count (<1 is forever)
        platform.getArgs().add((short)128); // Hittile to fill with
        platform.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        platform.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        platform.getArgs().add((short)0); // Cycle colors t/f
        platform.getArgs().add((short)0); // Alpha/frame
        platform.getArgs().add((short)255); // Max alpha
        platform.getArgs().add((short)0); // R/frame
        platform.getArgs().add((short)0); // Max R
        platform.getArgs().add((short)0); // G/frame
        platform.getArgs().add((short)0); // Max G
        platform.getArgs().add((short)0); // B/frame
        platform.getArgs().add((short)0); // Max B
        platform.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        platform.getArgs().add((short)1); // not0?

        if (Settings.isFools2020Mode()) {
            platform.getTestByteOperations().add(new TestByteOperation(0xacf, ByteOp.FLAG_EQUALS, 2));
        }

        screen.getObjects().add(platform);
    }

    public static void addTwinPuzzleFeatherlessPlatform(Screen screen) {
        GameObject platform = new GameObject(screen);
        platform.setId((short)0x93);
        platform.setX(900);
        platform.setY(280);

        platform.getArgs().add((short)0); // Layer
        platform.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        platform.getArgs().add((short)280); // Imagex
        platform.getArgs().add((short)80); // Imagey
        platform.getArgs().add((short)40); // dx
        platform.getArgs().add((short)20); // dy
        platform.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        platform.getArgs().add((short)1); // Animation frames
        platform.getArgs().add((short)0); // Pause frames
        platform.getArgs().add((short)0); // Repeat count (<1 is forever)
        platform.getArgs().add((short)128); // Hittile to fill with
        platform.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        platform.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        platform.getArgs().add((short)0); // Cycle colors t/f
        platform.getArgs().add((short)0); // Alpha/frame
        platform.getArgs().add((short)255); // Max alpha
        platform.getArgs().add((short)0); // R/frame
        platform.getArgs().add((short)0); // Max R
        platform.getArgs().add((short)0); // G/frame
        platform.getArgs().add((short)0); // Max G
        platform.getArgs().add((short)0); // B/frame
        platform.getArgs().add((short)0); // Max B
        platform.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        platform.getArgs().add((short)1); // not0?

        screen.getObjects().add(platform);
    }

    public static void addTwinPuzzleBlockFix(Screen screen) {
        GameObject platform = new GameObject(screen);
        platform.setId((short)0x93);
        platform.setX(1060);
        platform.setY(440);

        platform.getArgs().add((short)0); // Layer
        platform.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        platform.getArgs().add((short)280); // Imagex
        platform.getArgs().add((short)80); // Imagey
        platform.getArgs().add((short)40); // dx
        platform.getArgs().add((short)20); // dy
        platform.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        platform.getArgs().add((short)1); // Animation frames
        platform.getArgs().add((short)0); // Pause frames
        platform.getArgs().add((short)0); // Repeat count (<1 is forever)
        platform.getArgs().add((short)128); // Hittile to fill with
        platform.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        platform.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        platform.getArgs().add((short)0); // Cycle colors t/f
        platform.getArgs().add((short)0); // Alpha/frame
        platform.getArgs().add((short)255); // Max alpha
        platform.getArgs().add((short)0); // R/frame
        platform.getArgs().add((short)0); // Max R
        platform.getArgs().add((short)0); // G/frame
        platform.getArgs().add((short)0); // Max G
        platform.getArgs().add((short)0); // B/frame
        platform.getArgs().add((short)0); // Max B
        platform.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        platform.getArgs().add((short)1); // not0?

        screen.getObjects().add(platform);
    }

    public static void addTrueShrineFeatherlessPlatform(Screen screen, int x, int y) {
        GameObject platform = new GameObject(screen);
        platform.setId((short)0x93);
        platform.setX(x);
        platform.setY(y);

        platform.getArgs().add((short)0); // Layer
        platform.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        platform.getArgs().add((short)220); // Imagex
        platform.getArgs().add((short)40); // Imagey
        platform.getArgs().add((short)40); // dx
        platform.getArgs().add((short)20); // dy
        platform.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        platform.getArgs().add((short)1); // Animation frames
        platform.getArgs().add((short)0); // Pause frames
        platform.getArgs().add((short)0); // Repeat count (<1 is forever)
        platform.getArgs().add((short)128); // Hittile to fill with
        platform.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        platform.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        platform.getArgs().add((short)0); // Cycle colors t/f
        platform.getArgs().add((short)0); // Alpha/frame
        platform.getArgs().add((short)255); // Max alpha
        platform.getArgs().add((short)0); // R/frame
        platform.getArgs().add((short)0); // Max R
        platform.getArgs().add((short)0); // G/frame
        platform.getArgs().add((short)0); // Max G
        platform.getArgs().add((short)0); // B/frame
        platform.getArgs().add((short)0); // Max B
        platform.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        platform.getArgs().add((short)1); // not0?

        screen.getObjects().add(platform);
    }

    public static void addInfernoFakeWeaponCover(Screen screen, List<TestByteOperation> tests) {
        GameObject weaponCover = new GameObject(screen);
        weaponCover.setId((short)0x93);
        weaponCover.setX(20);
        weaponCover.setY(300);

        weaponCover.getArgs().add((short)0); // Layer
        weaponCover.getArgs().add((short)1); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        weaponCover.getArgs().add((short)420); // Imagex
        weaponCover.getArgs().add((short)0); // Imagey
        weaponCover.getArgs().add((short)120); // dx
        weaponCover.getArgs().add((short)60); // dy
        weaponCover.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        weaponCover.getArgs().add((short)1); // Animation frames
        weaponCover.getArgs().add((short)0); // Pause frames
        weaponCover.getArgs().add((short)0); // Repeat count (<1 is forever)
        weaponCover.getArgs().add((short)128); // Hittile to fill with
        weaponCover.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        weaponCover.getArgs().add((short)1); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        weaponCover.getArgs().add((short)0); // Cycle colors t/f
        weaponCover.getArgs().add((short)0); // Alpha/frame
        weaponCover.getArgs().add((short)255); // Max alpha
        weaponCover.getArgs().add((short)0); // R/frame
        weaponCover.getArgs().add((short)0); // Max R
        weaponCover.getArgs().add((short)0); // G/frame
        weaponCover.getArgs().add((short)0); // Max G
        weaponCover.getArgs().add((short)0); // B/frame
        weaponCover.getArgs().add((short)0); // Max B
        weaponCover.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        weaponCover.getArgs().add((short)1); // not0?

        weaponCover.getTestByteOperations().addAll(tests);

        screen.getObjects().add(weaponCover);
    }

    public static void addLittleBrotherWeightWaster(Screen screen) {
        GameObject weightWaster = new GameObject(screen);
        weightWaster.setId((short)0x08);
        weightWaster.setX(560);
        weightWaster.setY(1140);

        weightWaster.getArgs().add((short)0); // (0-1) Light red dust or pink dust
        weightWaster.getArgs().add((short)60); // (1-270) Falling time (in frames?)
        weightWaster.getArgs().add((short)0); // (-1-50) RiseFlag -1 Never Rise. 0 Always Rise
        weightWaster.getArgs().add((short)2); // (0-2) Image
        weightWaster.getArgs().add((short)0); // (0) (unused?)
        weightWaster.getArgs().add((short)860); // (180-860) ImageX
        weightWaster.getArgs().add((short)60); // (0-100) ImageY
        weightWaster.getArgs().add((short)1); // (0-1) Width 0 = Half-width, 1 = Full-width
        weightWaster.getArgs().add((short)10); // (0-10) (probably unused height)
        weightWaster.getArgs().add((short)60); // (0-60) RiseSpeed

        weightWaster.getTestByteOperations().add(new TestByteOperation(0x1f0, ByteOp.FLAG_LT, 2));
        weightWaster.getTestByteOperations().add(new TestByteOperation(0x1ea, ByteOp.FLAG_GT, 0));

        screen.getObjects().add(weightWaster);
    }

    /**
     * Add timer to auto-open the shortcut to lower Tower of the Goddess after flooding,
     * to be used only when starting with a subweapon (in case no main weapon is available) to prevent soflocks.
     * @param screen the screen to add the timers to
     */
    public static void addFloodedTowerShortcutTimer(ObjectContainer screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.setX(-1);
        obj.setY(-1);

        // Check that the tower has been flooded.
        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(404);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        obj.getTestByteOperations().add(testByteOperation);

        // Check that feather has been found, otherwise it's not possible to get down before flooding the tower anyway.
        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(182);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 2);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(877);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, obj);
    }

    /**
     * Add timer to set wrong color medicine statue to match whatever's in the Vessel.
     * @param screen the screen to add the timers to
     */
    public static void addMedicineStatueTimer(ObjectContainer screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.setX(-1);
        obj.setY(-1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(2772);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x34f);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x34f);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(2772);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, obj);
    }

    /**
     * Add weights to activate/deactivate hardmode without literacy.
     * @param screen the screen to add the timers to
     */
    public static void addHardmodeToggleWeights(ObjectContainer screen) {
        // Toggle off
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x08);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)60);
        obj.getArgs().add((short)-1);
        obj.getArgs().add((short)2);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)860);
        obj.getArgs().add((short)60);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)10);
        obj.getArgs().add((short)60);
        obj.setX(560);
        obj.setY(100);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(362);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)2);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(362);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)0);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(obj);

        // Toggle on
        obj = new GameObject(screen);
        obj.setId((short)0x08);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)60);
        obj.getArgs().add((short)-1);
        obj.getArgs().add((short)2);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)860);
        obj.getArgs().add((short)60);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)10);
        obj.getArgs().add((short)60);
        obj.setX(560);
        obj.setY(100);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(362);
        testByteOperation.setOp(ByteOp.FLAG_LT);
        testByteOperation.setValue((byte)2);
        obj.getTestByteOperations().add(testByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(362);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)2);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(obj);
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
        GameObject hotspring = new GameObject(reference.getObjectContainer());
        hotspring.setId((short)0xad);
        hotspring.setX(reference.getX() - 20);
        hotspring.setY(reference.getY() + 20);

        hotspring.getArgs().add((short)4);
        hotspring.getArgs().add((short)2);
        hotspring.getArgs().add((short)8);
        hotspring.getArgs().add((short)2);

        reference.getObjectContainer().getObjects().add(hotspring);
    }

    public static void addSurfaceGrailTablet(Screen screen) {
        GameObject grailTablet = new GameObject(screen);
        grailTablet.setId((short)0x9e);
        grailTablet.setX(1120);
        grailTablet.setY(80);

        grailTablet.getArgs().add((short)38);
        grailTablet.getArgs().add((short)0);
        grailTablet.getArgs().add((short)0);
        grailTablet.getArgs().add((short)1);
        grailTablet.getArgs().add((short)1);
        grailTablet.getArgs().add((short)1);
        grailTablet.getArgs().add((short)1);

        grailTablet.getArgs().add((short)0);
        grailTablet.getArgs().add((short)40);
        grailTablet.getArgs().add((short)40);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(LocationCoordinateMapper.getGrailFlag(1, true));
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        grailTablet.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(LocationCoordinateMapper.getGrailFlag(1, true));
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(1);
        grailTablet.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(grailTablet);

        if(Settings.isAutomaticGrailPoints()) {
            addGrailDetector(grailTablet, LocationCoordinateMapper.getGrailFlag(1, true));
        }
    }

    public static GameObject addSpecialGrailTablet(Screen screen) {
        GameObject grailTablet = new GameObject(screen);
        grailTablet.setId((short)0x9f);
        grailTablet.setX(400);
        grailTablet.setY(160);

        grailTablet.getArgs().add((short)38);
        grailTablet.getArgs().add((short)0);
        grailTablet.getArgs().add((short)0);
        grailTablet.getArgs().add((short)1);
        grailTablet.getArgs().add((short)1);
        grailTablet.getArgs().add((short)1);
        grailTablet.getArgs().add((short)1);

        grailTablet.getArgs().add((short)0);
        grailTablet.getArgs().add((short)40);
        grailTablet.getArgs().add((short)40);

        screen.getObjects().add(grailTablet);

        GameObject grailGraphic = new GameObject(screen);
        grailGraphic.setId((short)0x93);
        grailGraphic.setX(grailTablet.getX());
        grailGraphic.setY(grailTablet.getY());

        grailGraphic.getArgs().add((short)0); // Layer
        grailGraphic.getArgs().add((short)0); // 01.effect.png for anything not 0-6?
        grailGraphic.getArgs().add((short)40); // Imagex
        grailGraphic.getArgs().add((short)0); // Imagey
        grailGraphic.getArgs().add((short)40); // dx
        grailGraphic.getArgs().add((short)40); // dy
        grailGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        grailGraphic.getArgs().add((short)1); // Animation frames
        grailGraphic.getArgs().add((short)0); // Pause frames
        grailGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        grailGraphic.getArgs().add((short)0); // Hittile to fill with
        grailGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        grailGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        grailGraphic.getArgs().add((short)0); // Cycle colors t/f
        grailGraphic.getArgs().add((short)0); // Alpha/frame
        grailGraphic.getArgs().add((short)255); // Max alpha
        grailGraphic.getArgs().add((short)0); // R/frame
        grailGraphic.getArgs().add((short)0); // Max R
        grailGraphic.getArgs().add((short)0); // G/frame
        grailGraphic.getArgs().add((short)0); // Max G
        grailGraphic.getArgs().add((short)0); // B/frame
        grailGraphic.getArgs().add((short)0); // Max B
        grailGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        grailGraphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(grailGraphic);

        GameObject grailSave = new GameObject(screen);
        grailSave.setId((short)0xb6);
        grailSave.setX(grailTablet.getX());
        grailSave.setY(grailTablet.getY());

        grailSave.getArgs().add((short)33);

        screen.getObjects().add(grailSave);

        return grailTablet;
    }

    public static GameObject addWarp(Screen screen, int warpX, int warpY, int width, int height, int destZone, int destRoom, int destScreen, int destX, int destY) {
        GameObject warp = new GameObject(screen);
        warp.setId((short) 0x97);
        warp.setX(warpX);
        warp.setY(warpY);

        warp.getArgs().add((short)destZone);
        warp.getArgs().add((short)destRoom);
        warp.getArgs().add((short)destScreen);
        warp.getArgs().add((short)destX);
        warp.getArgs().add((short)destY);
        warp.getArgs().add((short)width);
        warp.getArgs().add((short)height);
        warp.getArgs().add((short)4);
        warp.getArgs().add((short)4);

        screen.getObjects().add(warp);

        return warp;
    }

    public static GameObject addLemezaDetector(Screen screen, int detectorX, int detectorY, int width, int height, List<TestByteOperation> tests, List<WriteByteOperation> updates) {
        GameObject detector = new GameObject(screen);
        detector.setId((short) 0x14);
        detector.setX(detectorX);
        detector.setY(detectorY);

        detector.getArgs().add((short)0);
        detector.getArgs().add((short)0);
        detector.getArgs().add((short)0);
        detector.getArgs().add((short)0);
        detector.getArgs().add((short)width);
        detector.getArgs().add((short)height);

        detector.getTestByteOperations().addAll(tests);
        detector.getWriteByteOperations().addAll(updates);

        screen.getObjects().add(detector);

        return detector;
    }

    public static GameObject addPunchyFist(Screen screen, int x, int y, List<WriteByteOperation> updates) {
        GameObject punchyFist = new GameObject(screen);
        punchyFist.setId((short) 0xa3);
        punchyFist.setX(x);
        punchyFist.setY(y);

        punchyFist.getArgs().add((short)0);
        punchyFist.getArgs().add((short)0);
        punchyFist.getArgs().add((short)0);
        punchyFist.getArgs().add((short)63);
        punchyFist.getArgs().add((short)0);
        punchyFist.getArgs().add((short)0);
        punchyFist.getArgs().add((short)0);
        punchyFist.getArgs().add((short)32);
        punchyFist.getArgs().add((short)32);
        punchyFist.getArgs().add((short)-1);
        punchyFist.getArgs().add((short)-1);
        punchyFist.getArgs().add((short)-1);
        punchyFist.getArgs().add((short)-1);
        punchyFist.getArgs().add((short)-1);
        punchyFist.getArgs().add((short)-1);
        punchyFist.getArgs().add((short)-1);
        punchyFist.getArgs().add((short)-1);
        punchyFist.getArgs().add((short)-1);
        punchyFist.getArgs().add((short)0);
        punchyFist.getArgs().add((short)-160);
        punchyFist.getArgs().add((short)0);
        punchyFist.getArgs().add((short)0);
        punchyFist.getArgs().add((short)1000);

        punchyFist.getWriteByteOperations().addAll(updates);

        screen.getObjects().add(punchyFist);

        return punchyFist;
    }

    public static void addEscapeTimer(Screen screen, int beginConditionFlag, int beginConditionValue) {
        // The escape timer itself
        GameObject escapeTimer = new GameObject(screen);
        escapeTimer.setId((short) 0xc5);
        escapeTimer.setX(-1);
        escapeTimer.setY(-1);

        int timerMinutes;
        int timerSeconds;
        if(Settings.isHalloweenMode() && Settings.isIncludeHellTempleNPCs()) {
            timerMinutes = 10;
            timerSeconds = 31;
        }
        else {
            timerMinutes = Settings.isRandomizeTransitionGates() ? 10 : 5;
            timerSeconds = 0;
        }

        escapeTimer.getArgs().add((short)264);
        escapeTimer.getArgs().add((short)20);
        escapeTimer.getArgs().add((short)timerMinutes);
        escapeTimer.getArgs().add((short)timerSeconds);
        escapeTimer.getArgs().add((short)0);
        escapeTimer.getArgs().add((short)10);
        escapeTimer.getArgs().add((short)-1);
        escapeTimer.getArgs().add((short)-1);
        escapeTimer.getArgs().add((short)-1);
        escapeTimer.getArgs().add((short)1000);
        escapeTimer.getArgs().add((short)1001);
        escapeTimer.getArgs().add((short)1002);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x382);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        escapeTimer.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x403);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        escapeTimer.getTestByteOperations().add(testByteOperation);

        screen.getObjects().add(0, escapeTimer);

        if(!Settings.isScreenshakeDisabled()) {
            // Escape screen shake
            GameObject escapeScreenShake = new GameObject(screen);
            escapeScreenShake.setId((short) 0xc7);
            escapeScreenShake.setX(-1);
            escapeScreenShake.setY(-1);

            escapeScreenShake.getArgs().add((short)-1);
            escapeScreenShake.getArgs().add((short)0);

            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(0x382);
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            testByteOperation.setValue((byte)1);
            escapeScreenShake.getTestByteOperations().add(testByteOperation);

            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(0x403);
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            testByteOperation.setValue((byte)0);
            escapeScreenShake.getTestByteOperations().add(testByteOperation);

            screen.getObjects().add(0, escapeScreenShake);
        }

        // Disable grail for escape
        GameObject grailToggle = new GameObject(screen);
        grailToggle.setId((short)0xb7);
        grailToggle.setX(-1);
        grailToggle.setY(-1);
        grailToggle.getArgs().add((short)0);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x382);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        grailToggle.getTestByteOperations().add(testByteOperation);

        screen.getObjects().add(0, grailToggle);

        GameObject beginEscapeTestTimer = new GameObject(screen);
        beginEscapeTestTimer.setId((short)0x0b);
        beginEscapeTestTimer.setX(-1);
        beginEscapeTestTimer.setY(-1);
        beginEscapeTestTimer.getArgs().add((short)0);
        beginEscapeTestTimer.getArgs().add((short)0);

        // Count number of NPCs visited
        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(beginConditionFlag);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte)beginConditionValue);
        beginEscapeTestTimer.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x382);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        beginEscapeTestTimer.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x382);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        beginEscapeTestTimer.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, beginEscapeTestTimer);

        GameObject testTimer = new GameObject(screen);
        testTimer.setId((short)0x0b);
        testTimer.setX(-1);
        testTimer.setY(-1);
        testTimer.getArgs().add((short)0);
        testTimer.getArgs().add((short)2);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x382);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        testTimer.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x403);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        testTimer.getTestByteOperations().add(testByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x403);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)1);
        testTimer.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, testTimer);
    }

    public static void addNpcConversationTimer(Screen screen, int flag) {
        GameObject conversationTimer = new GameObject(screen);
        conversationTimer.setId((short)0x0b);
        conversationTimer.setX(-1);
        conversationTimer.setY(-1);
        conversationTimer.getArgs().add((short)0);
        conversationTimer.getArgs().add((short)0);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(flag);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        conversationTimer.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(flag);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue((byte)2);
        conversationTimer.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0xaca);
        writeByteOperation.setOp(ByteOp.ADD_FLAG);
        writeByteOperation.setValue((byte)1);
        conversationTimer.getWriteByteOperations().add(writeByteOperation);

        if(flag == 0xabe) {
            // Mr. Slushfund
            writeByteOperation = new WriteByteOperation();
            writeByteOperation.setIndex(46);
            writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
            writeByteOperation.setValue((byte)1);
            conversationTimer.getWriteByteOperations().add(writeByteOperation);
        }

        screen.getObjects().add(0, conversationTimer);
    }

    public static GameObject addGhostSpawner(Screen screen, int spawnRate) {
        GameObject ghostSpawner = new GameObject(screen);
        ghostSpawner.setId((short)0x1f);
        ghostSpawner.setX((short)-1);
        ghostSpawner.setY((short)-1);
        ghostSpawner.getArgs().add((short)spawnRate); // Spawning period
        ghostSpawner.getArgs().add((short)3); // Maximum Ghosts
        ghostSpawner.getArgs().add((short)0); // UNKNOWN - bugged?
        if(screen.getZoneIndex() == 23) {
            ghostSpawner.getArgs().add((short)0); // Speed AND Drop-type
        }
        else {
            ghostSpawner.getArgs().add((short)1); // Speed AND Drop-type
        }
        ghostSpawner.getArgs().add((short)1); // Health
        ghostSpawner.getArgs().add((short)2); // Damage AND Soul
        ghostSpawner.getArgs().add((short)3); // UNKNOWN - bugged?
        screen.getObjects().add(ghostSpawner);
        return ghostSpawner;
    }

    public static GameObject addGhostLord(Screen screen, int x, int y, int speed, int health, int damage, int soul) {
        GameObject ghostLord = new GameObject(screen);
        ghostLord.setId((short)0x20);
        ghostLord.setX((short)x);
        ghostLord.setY((short)y);

        ghostLord.getArgs().add((short)1); // Drop type - coins
        ghostLord.getArgs().add((short)30); // Amount
        ghostLord.getArgs().add((short)speed); // Speed (up to 4 is allowed for this mode)
        ghostLord.getArgs().add((short)health); // Health
        ghostLord.getArgs().add((short)damage); // Damage
        ghostLord.getArgs().add((short)soul); // Soul
        screen.getObjects().add(ghostLord);
        return ghostLord;
    }

    public static void addHTExitDoor(Screen screen) {
        GameObject htExitDoor = new GameObject(screen);

        htExitDoor.setId((short) 0x98);
        htExitDoor.setX(220);
        htExitDoor.setY(80);

        htExitDoor.getArgs().add((short)0);
        htExitDoor.getArgs().add((short)23);
        htExitDoor.getArgs().add((short)0);
        htExitDoor.getArgs().add((short)0);
        htExitDoor.getArgs().add((short)180);
        htExitDoor.getArgs().add((short)332);

        htExitDoor.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1));

        screen.getObjects().add(htExitDoor);

        GameObject htExitDoorGraphic = new GameObject(screen);
        htExitDoorGraphic.setId((short) 0x93);
        htExitDoorGraphic.setX(200);
        htExitDoorGraphic.setY(40);

        htExitDoorGraphic.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1));

        htExitDoorGraphic.getArgs().add((short)-1);
        htExitDoorGraphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        htExitDoorGraphic.getArgs().add((short)560);
        htExitDoorGraphic.getArgs().add((short)40);
        htExitDoorGraphic.getArgs().add((short)80);
        htExitDoorGraphic.getArgs().add((short)80);
        htExitDoorGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        htExitDoorGraphic.getArgs().add((short)0); // Animation frames
        htExitDoorGraphic.getArgs().add((short)1); // Pause frames
        htExitDoorGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        htExitDoorGraphic.getArgs().add((short)0); // Hittile to fill with
        htExitDoorGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        htExitDoorGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        htExitDoorGraphic.getArgs().add((short)0); // Cycle colors t/f
        htExitDoorGraphic.getArgs().add((short)0); // Alpha/frame
        htExitDoorGraphic.getArgs().add((short)255); // Max alpha
        htExitDoorGraphic.getArgs().add((short)0); // R/frame
        htExitDoorGraphic.getArgs().add((short)0); // Max R
        htExitDoorGraphic.getArgs().add((short)0); // G/frame
        htExitDoorGraphic.getArgs().add((short)0); // Max G
        htExitDoorGraphic.getArgs().add((short)0); // B/frame
        htExitDoorGraphic.getArgs().add((short)0); // Max B
        htExitDoorGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        htExitDoorGraphic.getArgs().add((short)1); // not0?
        screen.getObjects().add(htExitDoorGraphic);
    }

    public static GameObject addLaserWall(Screen screen, int x, int y, boolean flatDamage, int damage) {
        GameObject laserWall = new GameObject(screen);
        laserWall.setId((short) 0xab);
        laserWall.setX(x);
        laserWall.setY(y);

        laserWall.getArgs().add((short)(flatDamage ? 1 : 0)); // % or hp
        laserWall.getArgs().add((short)damage); // damage

        screen.getObjects().add(laserWall);
        return laserWall;
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

        TestByteOperation testByteOperation = new TestByteOperation(0x7f1, ByteOp.FLAG_EQUALS, 2);
        platform1.getTestByteOperations().add(testByteOperation);
//        platform2.getTestByteOperations().add(testByteOperation);
        platform3.getTestByteOperations().add(testByteOperation);
    }

//    public static void addHadoukenTurtle(Screen screen, int x, int y) {
//        GameObject enemy = new GameObject(screen);
//        enemy.setId((short)0x62);
//        enemy.setX(x);
//        enemy.setY(y);
//
//        enemy.getArgs().add((short)0); // Facing
//        enemy.getArgs().add((short)11); // Drop type
//        enemy.getArgs().add((short)3); // Speed
//        enemy.getArgs().add((short)24); // Health
//        enemy.getArgs().add((short)16); // Contact damage
//        enemy.getArgs().add((short)11); // Soul
//        enemy.getArgs().add((short)3); // Proj Speed
//        enemy.getArgs().add((short)5); // Projectiles per volley
//        enemy.getArgs().add((short)10); // Delay between shots
//        enemy.getArgs().add((short)16); // Projectile damage
//
//        TestByteOperation enemyTest = new TestByteOperation();
//        enemyTest.setIndex(0x1cf);
//        enemyTest.setValue((byte) 2);
//        enemyTest.setOp(ByteOp.FLAG_EQUALS);
//        enemy.getTestByteOperations().add(enemyTest);
//
//        screen.getObjects().add(enemy);
//    }

    public static GameObject addGuidanceShop(Screen screen) {
        GameObject graphic = new GameObject(screen);
        graphic.setId((short) 0x93);
        graphic.setX(240);
        graphic.setY(380);

        graphic.getArgs().add((short)0); // Layer
        graphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        graphic.getArgs().add((short)200); // Imagex
        graphic.getArgs().add((short)100); // Imagey
        graphic.getArgs().add((short)40); // dx
        graphic.getArgs().add((short)60); // dy
        graphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        graphic.getArgs().add((short)0); // Animation frames
        graphic.getArgs().add((short)1); // Pause frames
        graphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        graphic.getArgs().add((short)0); // Hittile to fill with
        graphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        graphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        graphic.getArgs().add((short)0); // Cycle colors t/f
        graphic.getArgs().add((short)0); // Alpha/frame
        graphic.getArgs().add((short)255); // Max alpha
        graphic.getArgs().add((short)0); // R/frame
        graphic.getArgs().add((short)0); // Max R
        graphic.getArgs().add((short)0); // G/frame
        graphic.getArgs().add((short)0); // Max G
        graphic.getArgs().add((short)0); // B/frame
        graphic.getArgs().add((short)0); // Max B
        graphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        graphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(graphic);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(240);
        shop.setY(400);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addMausoleumShop(Screen screen) {
        GameObject graphic = new GameObject(screen);
        graphic.setId((short) 0x93);
        graphic.setX(300);
        graphic.setY(240);

        graphic.getArgs().add((short)0); // Layer
        graphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        graphic.getArgs().add((short)320); // Imagex
        graphic.getArgs().add((short)212); // Imagey
        graphic.getArgs().add((short)40); // dx
        graphic.getArgs().add((short)60); // dy
        graphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        graphic.getArgs().add((short)0); // Animation frames
        graphic.getArgs().add((short)1); // Pause frames
        graphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        graphic.getArgs().add((short)0); // Hittile to fill with
        graphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        graphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        graphic.getArgs().add((short)0); // Cycle colors t/f
        graphic.getArgs().add((short)0); // Alpha/frame
        graphic.getArgs().add((short)255); // Max alpha
        graphic.getArgs().add((short)0); // R/frame
        graphic.getArgs().add((short)0); // Max R
        graphic.getArgs().add((short)0); // G/frame
        graphic.getArgs().add((short)0); // Max G
        graphic.getArgs().add((short)0); // B/frame
        graphic.getArgs().add((short)0); // Max B
        graphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        graphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(graphic);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(300);
        shop.setY(240);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addInfernoShop(Screen screen) {
        GameObject graphic = new GameObject(screen);
        graphic.setId((short) 0x93);
        graphic.setX(220);
        graphic.setY(60);

        graphic.getArgs().add((short)0); // Layer
        graphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        graphic.getArgs().add((short)525); // Imagex
        graphic.getArgs().add((short)40); // Imagey
        graphic.getArgs().add((short)70); // dx
        graphic.getArgs().add((short)60); // dy
        graphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        graphic.getArgs().add((short)0); // Animation frames
        graphic.getArgs().add((short)1); // Pause frames
        graphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        graphic.getArgs().add((short)0); // Hittile to fill with
        graphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        graphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        graphic.getArgs().add((short)0); // Cycle colors t/f
        graphic.getArgs().add((short)0); // Alpha/frame
        graphic.getArgs().add((short)255); // Max alpha
        graphic.getArgs().add((short)0); // R/frame
        graphic.getArgs().add((short)0); // Max R
        graphic.getArgs().add((short)0); // G/frame
        graphic.getArgs().add((short)0); // Max G
        graphic.getArgs().add((short)0); // B/frame
        graphic.getArgs().add((short)0); // Max B
        graphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        graphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(graphic);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(240);
        shop.setY(80);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addIllusionShop(Screen screen) {
        GameObject graphic = new GameObject(screen);
        graphic.setId((short) 0x93);
        graphic.setX(220);
        graphic.setY(80);

        graphic.getArgs().add((short)0); // Layer
        graphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        graphic.getArgs().add((short)940); // Imagex
        graphic.getArgs().add((short)172); // Imagey
        graphic.getArgs().add((short)40); // dx
        graphic.getArgs().add((short)50); // dy
        graphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        graphic.getArgs().add((short)0); // Animation frames
        graphic.getArgs().add((short)1); // Pause frames
        graphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        graphic.getArgs().add((short)0); // Hittile to fill with
        graphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        graphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        graphic.getArgs().add((short)0); // Cycle colors t/f
        graphic.getArgs().add((short)0); // Alpha/frame
        graphic.getArgs().add((short)255); // Max alpha
        graphic.getArgs().add((short)0); // R/frame
        graphic.getArgs().add((short)0); // Max R
        graphic.getArgs().add((short)0); // G/frame
        graphic.getArgs().add((short)0); // Max G
        graphic.getArgs().add((short)0); // B/frame
        graphic.getArgs().add((short)0); // Max B
        graphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        graphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(graphic);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(220);
        shop.setY(80);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addTwinLabsFrontShop(Screen screen) {
        GameObject graphic = new GameObject(screen);
        graphic.setId((short) 0x93);
        graphic.setX(880);
        graphic.setY(380);

        graphic.getArgs().add((short)0); // Layer
        graphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        graphic.getArgs().add((short)500); // Imagex
        graphic.getArgs().add((short)200); // Imagey
        graphic.getArgs().add((short)80); // dx
        graphic.getArgs().add((short)60); // dy
        graphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        graphic.getArgs().add((short)0); // Animation frames
        graphic.getArgs().add((short)1); // Pause frames
        graphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        graphic.getArgs().add((short)0); // Hittile to fill with
        graphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        graphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        graphic.getArgs().add((short)0); // Cycle colors t/f
        graphic.getArgs().add((short)0); // Alpha/frame
        graphic.getArgs().add((short)255); // Max alpha
        graphic.getArgs().add((short)0); // R/frame
        graphic.getArgs().add((short)0); // Max R
        graphic.getArgs().add((short)0); // G/frame
        graphic.getArgs().add((short)0); // Max G
        graphic.getArgs().add((short)0); // B/frame
        graphic.getArgs().add((short)0); // Max B
        graphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        graphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(graphic);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(900);
        shop.setY(400);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addTwinLabsBackShop(Screen screen) {
        GameObject graphic = new GameObject(screen);
        graphic.setId((short) 0x93);
//        graphic.setX(800);
//        graphic.setY(380);
        graphic.setX(920);
        graphic.setY(220);

        graphic.getArgs().add((short)0); // Layer
        graphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        graphic.getArgs().add((short)500); // Imagex
        graphic.getArgs().add((short)200); // Imagey
        graphic.getArgs().add((short)80); // dx
        graphic.getArgs().add((short)60); // dy
        graphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        graphic.getArgs().add((short)0); // Animation frames
        graphic.getArgs().add((short)1); // Pause frames
        graphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        graphic.getArgs().add((short)0); // Hittile to fill with
        graphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        graphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        graphic.getArgs().add((short)0); // Cycle colors t/f
        graphic.getArgs().add((short)0); // Alpha/frame
        graphic.getArgs().add((short)255); // Max alpha
        graphic.getArgs().add((short)0); // R/frame
        graphic.getArgs().add((short)0); // Max R
        graphic.getArgs().add((short)0); // G/frame
        graphic.getArgs().add((short)0); // Max G
        graphic.getArgs().add((short)0); // B/frame
        graphic.getArgs().add((short)0); // Max B
        graphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        graphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(graphic);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
//        shop.setX(820);
//        shop.setY(400);
        shop.setX(940);
        shop.setY(240);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addEndlessShop(Screen screen) {
        GameObject graphic = new GameObject(screen);
        graphic.setId((short) 0x93);
        graphic.setX(500);
        graphic.setY(40);

        graphic.getArgs().add((short)0); // Layer
        graphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        graphic.getArgs().add((short)540); // Imagex
        graphic.getArgs().add((short)40); // Imagey
        graphic.getArgs().add((short)80); // dx
        graphic.getArgs().add((short)80); // dy
        graphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        graphic.getArgs().add((short)0); // Animation frames
        graphic.getArgs().add((short)1); // Pause frames
        graphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        graphic.getArgs().add((short)0); // Hittile to fill with
        graphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        graphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        graphic.getArgs().add((short)0); // Cycle colors t/f
        graphic.getArgs().add((short)0); // Alpha/frame
        graphic.getArgs().add((short)255); // Max alpha
        graphic.getArgs().add((short)0); // R/frame
        graphic.getArgs().add((short)0); // Max R
        graphic.getArgs().add((short)0); // G/frame
        graphic.getArgs().add((short)0); // Max G
        graphic.getArgs().add((short)0); // B/frame
        graphic.getArgs().add((short)0); // Max B
        graphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        graphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(graphic);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(520);
        shop.setY(80);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addGraveyardShop(Screen screen) {
        GameObject graphic = new GameObject(screen);
        graphic.setId((short) 0x93);
        graphic.setX(880);
        graphic.setY(140);

        graphic.getArgs().add((short)0); // Layer
        graphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        graphic.getArgs().add((short)560); // Imagex
        graphic.getArgs().add((short)280); // Imagey
        graphic.getArgs().add((short)50); // dx
        graphic.getArgs().add((short)60); // dy
        graphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        graphic.getArgs().add((short)0); // Animation frames
        graphic.getArgs().add((short)1); // Pause frames
        graphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        graphic.getArgs().add((short)0); // Hittile to fill with
        graphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        graphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        graphic.getArgs().add((short)0); // Cycle colors t/f
        graphic.getArgs().add((short)0); // Alpha/frame
        graphic.getArgs().add((short)255); // Max alpha
        graphic.getArgs().add((short)0); // R/frame
        graphic.getArgs().add((short)0); // Max R
        graphic.getArgs().add((short)0); // G/frame
        graphic.getArgs().add((short)0); // Max G
        graphic.getArgs().add((short)0); // B/frame
        graphic.getArgs().add((short)0); // Max B
        graphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        graphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(graphic);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(880);
        shop.setY(160);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addGoddessShop(Screen screen) {
        GameObject graphic = new GameObject(screen);
        graphic.setId((short) 0x93);
        graphic.setX(940);
        graphic.setY(300);

        graphic.getArgs().add((short)0); // Layer
        graphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        graphic.getArgs().add((short)660); // Imagex
        graphic.getArgs().add((short)520); // Imagey
        graphic.getArgs().add((short)40); // dx
        graphic.getArgs().add((short)60); // dy
        graphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        graphic.getArgs().add((short)0); // Animation frames
        graphic.getArgs().add((short)1); // Pause frames
        graphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        graphic.getArgs().add((short)0); // Hittile to fill with
        graphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        graphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        graphic.getArgs().add((short)0); // Cycle colors t/f
        graphic.getArgs().add((short)0); // Alpha/frame
        graphic.getArgs().add((short)255); // Max alpha
        graphic.getArgs().add((short)0); // R/frame
        graphic.getArgs().add((short)0); // Max R
        graphic.getArgs().add((short)0); // G/frame
        graphic.getArgs().add((short)0); // Max G
        graphic.getArgs().add((short)0); // B/frame
        graphic.getArgs().add((short)0); // Max B
        graphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        graphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(graphic);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(940);
        shop.setY(320);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addRuinShop(Screen screen) {
        GameObject graphic = new GameObject(screen);
        graphic.setId((short) 0x93);
        graphic.setX(200);
        graphic.setY(380);

        graphic.getArgs().add((short)0); // Layer
        graphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        graphic.getArgs().add((short)280); // Imagex
        graphic.getArgs().add((short)180); // Imagey
        graphic.getArgs().add((short)40); // dx
        graphic.getArgs().add((short)60); // dy
        graphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        graphic.getArgs().add((short)0); // Animation frames
        graphic.getArgs().add((short)1); // Pause frames
        graphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        graphic.getArgs().add((short)0); // Hittile to fill with
        graphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        graphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        graphic.getArgs().add((short)0); // Cycle colors t/f
        graphic.getArgs().add((short)0); // Alpha/frame
        graphic.getArgs().add((short)255); // Max alpha
        graphic.getArgs().add((short)0); // R/frame
        graphic.getArgs().add((short)0); // Max R
        graphic.getArgs().add((short)0); // G/frame
        graphic.getArgs().add((short)0); // Max G
        graphic.getArgs().add((short)0); // B/frame
        graphic.getArgs().add((short)0); // Max B
        graphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        graphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(graphic);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(200);
        shop.setY(400);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addBirthStartStuff(Screen screen) {
        GameObject shopGraphic = new GameObject(screen);
        shopGraphic.setId((short) 0x93);
        shopGraphic.setX(140);
        shopGraphic.setY(300);

        shopGraphic.getArgs().add((short)0); // Layer
        shopGraphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        shopGraphic.getArgs().add((short)40); // Imagex
        shopGraphic.getArgs().add((short)100); // Imagey
        shopGraphic.getArgs().add((short)40); // dx
        shopGraphic.getArgs().add((short)60); // dy
        shopGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        shopGraphic.getArgs().add((short)0); // Animation frames
        shopGraphic.getArgs().add((short)1); // Pause frames
        shopGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        shopGraphic.getArgs().add((short)0); // Hittile to fill with
        shopGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        shopGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        shopGraphic.getArgs().add((short)0); // Cycle colors t/f
        shopGraphic.getArgs().add((short)0); // Alpha/frame
        shopGraphic.getArgs().add((short)255); // Max alpha
        shopGraphic.getArgs().add((short)0); // R/frame
        shopGraphic.getArgs().add((short)0); // Max R
        shopGraphic.getArgs().add((short)0); // G/frame
        shopGraphic.getArgs().add((short)0); // Max G
        shopGraphic.getArgs().add((short)0); // B/frame
        shopGraphic.getArgs().add((short)0); // Max B
        shopGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        shopGraphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(shopGraphic);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(140);
        shop.setY(320);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);

        GameObject coverGraphic = new GameObject(screen);
        coverGraphic.setId((short) 0x93);
        coverGraphic.setX(320);
        coverGraphic.setY(300);

        coverGraphic.getArgs().add((short)0); // Layer
        coverGraphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        coverGraphic.getArgs().add((short)600); // Imagex
        coverGraphic.getArgs().add((short)140); // Imagey
        coverGraphic.getArgs().add((short)60); // dx
        coverGraphic.getArgs().add((short)60); // dy
        coverGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        coverGraphic.getArgs().add((short)0); // Animation frames
        coverGraphic.getArgs().add((short)1); // Pause frames
        coverGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        coverGraphic.getArgs().add((short)0); // Hittile to fill with
        coverGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        coverGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        coverGraphic.getArgs().add((short)0); // Cycle colors t/f
        coverGraphic.getArgs().add((short)0); // Alpha/frame
        coverGraphic.getArgs().add((short)255); // Max alpha
        coverGraphic.getArgs().add((short)0); // R/frame
        coverGraphic.getArgs().add((short)0); // Max R
        coverGraphic.getArgs().add((short)0); // G/frame
        coverGraphic.getArgs().add((short)0); // Max G
        coverGraphic.getArgs().add((short)0); // B/frame
        coverGraphic.getArgs().add((short)0); // Max B
        coverGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        coverGraphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(coverGraphic);

        GameObject tabletGraphic1 = new GameObject(screen);
        tabletGraphic1.setId((short) 0x93);
        tabletGraphic1.setX(200);
        tabletGraphic1.setY(380);

        tabletGraphic1.getArgs().add((short)0); // Layer
        tabletGraphic1.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        tabletGraphic1.getArgs().add((short)320); // Imagex
        tabletGraphic1.getArgs().add((short)0); // Imagey
        tabletGraphic1.getArgs().add((short)40); // dx
        tabletGraphic1.getArgs().add((short)40); // dy
        tabletGraphic1.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        tabletGraphic1.getArgs().add((short)0); // Animation frames
        tabletGraphic1.getArgs().add((short)1); // Pause frames
        tabletGraphic1.getArgs().add((short)0); // Repeat count (<1 is forever)
        tabletGraphic1.getArgs().add((short)0); // Hittile to fill with
        tabletGraphic1.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        tabletGraphic1.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        tabletGraphic1.getArgs().add((short)0); // Cycle colors t/f
        tabletGraphic1.getArgs().add((short)0); // Alpha/frame
        tabletGraphic1.getArgs().add((short)255); // Max alpha
        tabletGraphic1.getArgs().add((short)0); // R/frame
        tabletGraphic1.getArgs().add((short)0); // Max R
        tabletGraphic1.getArgs().add((short)0); // G/frame
        tabletGraphic1.getArgs().add((short)0); // Max G
        tabletGraphic1.getArgs().add((short)0); // B/frame
        tabletGraphic1.getArgs().add((short)0); // Max B
        tabletGraphic1.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        tabletGraphic1.getArgs().add((short)1); // not0?

        screen.getObjects().add(tabletGraphic1);

        GameObject tabletGraphic2 = new GameObject(screen);
        tabletGraphic2.setId((short) 0x93);
        tabletGraphic2.setX(200);
        tabletGraphic2.setY(420);

        tabletGraphic2.getArgs().add((short)0); // Layer
        tabletGraphic2.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        tabletGraphic2.getArgs().add((short)360); // Imagex
        tabletGraphic2.getArgs().add((short)0); // Imagey
        tabletGraphic2.getArgs().add((short)40); // dx
        tabletGraphic2.getArgs().add((short)20); // dy
        tabletGraphic2.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        tabletGraphic2.getArgs().add((short)0); // Animation frames
        tabletGraphic2.getArgs().add((short)1); // Pause frames
        tabletGraphic2.getArgs().add((short)0); // Repeat count (<1 is forever)
        tabletGraphic2.getArgs().add((short)0); // Hittile to fill with
        tabletGraphic2.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        tabletGraphic2.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        tabletGraphic2.getArgs().add((short)0); // Cycle colors t/f
        tabletGraphic2.getArgs().add((short)0); // Alpha/frame
        tabletGraphic2.getArgs().add((short)255); // Max alpha
        tabletGraphic2.getArgs().add((short)0); // R/frame
        tabletGraphic2.getArgs().add((short)0); // Max R
        tabletGraphic2.getArgs().add((short)0); // G/frame
        tabletGraphic2.getArgs().add((short)0); // Max G
        tabletGraphic2.getArgs().add((short)0); // B/frame
        tabletGraphic2.getArgs().add((short)0); // Max B
        tabletGraphic2.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        tabletGraphic2.getArgs().add((short)1); // not0?

        screen.getObjects().add(tabletGraphic2);


        return shop;
    }

    public static GameObject addRetroSurfaceShop(Screen screen) {
        GameObject tent = new GameObject(screen);
        tent.setId((short) 0x93);
        tent.setX(480);
        tent.setY(200);

        tent.getArgs().add((short)0); // Layer
        tent.getArgs().add((short)0); // 01.effect.png for anything not 0-6?
        tent.getArgs().add((short)0); // Imagex
        tent.getArgs().add((short)120); // Imagey
        tent.getArgs().add((short)80); // dx
        tent.getArgs().add((short)40); // dy
        tent.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        tent.getArgs().add((short)0); // Animation frames
        tent.getArgs().add((short)1); // Pause frames
        tent.getArgs().add((short)0); // Repeat count (<1 is forever)
        tent.getArgs().add((short)0); // Hittile to fill with
        tent.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        tent.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        tent.getArgs().add((short)0); // Cycle colors t/f
        tent.getArgs().add((short)0); // Alpha/frame
        tent.getArgs().add((short)255); // Max alpha
        tent.getArgs().add((short)0); // R/frame
        tent.getArgs().add((short)0); // Max R
        tent.getArgs().add((short)0); // G/frame
        tent.getArgs().add((short)0); // Max G
        tent.getArgs().add((short)0); // B/frame
        tent.getArgs().add((short)0); // Max B
        tent.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        tent.getArgs().add((short)1); // not0?

        screen.getObjects().add(tent);

        GameObject tent2 = new GameObject(screen);
        tent2.setId((short) 0x93);
        tent2.setX(480);
        tent2.setY(240);

        tent2.getArgs().add((short)0); // Layer
        tent2.getArgs().add((short)0); // 01.effect.png for anything not 0-6?
        tent2.getArgs().add((short)80); // Imagex
        tent2.getArgs().add((short)120); // Imagey
        tent2.getArgs().add((short)80); // dx
        tent2.getArgs().add((short)40); // dy
        tent2.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        tent2.getArgs().add((short)0); // Animation frames
        tent2.getArgs().add((short)1); // Pause frames
        tent2.getArgs().add((short)0); // Repeat count (<1 is forever)
        tent2.getArgs().add((short)0); // Hittile to fill with
        tent2.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        tent2.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        tent2.getArgs().add((short)0); // Cycle colors t/f
        tent2.getArgs().add((short)0); // Alpha/frame
        tent2.getArgs().add((short)255); // Max alpha
        tent2.getArgs().add((short)0); // R/frame
        tent2.getArgs().add((short)0); // Max R
        tent2.getArgs().add((short)0); // G/frame
        tent2.getArgs().add((short)0); // Max G
        tent2.getArgs().add((short)0); // B/frame
        tent2.getArgs().add((short)0); // Max B
        tent2.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        tent2.getArgs().add((short)1); // not0?

        screen.getObjects().add(tent2);

        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(500);
        shop.setY(240);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)36);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        screen.getObjects().add(shop);
        return shop;
    }

    public static GameObject addSecretShop(Screen screen, int secretShopBlock) {
        GameObject shop = new GameObject(screen);
        shop.setId((short) 0xa0);
        shop.setX(0);
        shop.setY(240);

        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)1);
        shop.getArgs().add((short)secretShopBlock);
        shop.getArgs().add((short)0);
        shop.getArgs().add((short)0);

        shop.getTestByteOperations().add(new TestByteOperation(0xacd, ByteOp.FLAG_GTEQ, 1));

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

        dance.getTestByteOperations().add(new TestByteOperation(0xacd, ByteOp.FLAG_EQUALS, 0));
        dance.getWriteByteOperations().add(new WriteByteOperation(0xacd, ByteOp.ASSIGN_FLAG, 1));

        screen.getObjects().add(dance);
        return dance;
    }

    public static void addItemGive(GameObject referenceObj, int inventoryArg, int randomizeGraphicsFlag, int worldFlag) {
        GameObject itemGive = new GameObject(referenceObj.getObjectContainer());
        itemGive.setId((short) 0xb5);
        int x = referenceObj.getX();
        int y = referenceObj.getY();
        itemGive.setX((x / 640) * 640);
        itemGive.setY((y / 480) * 480);

        itemGive.getArgs().add((short)inventoryArg);
        itemGive.getArgs().add((short)32);
        itemGive.getArgs().add((short)24);
        itemGive.getArgs().add((short)39);

        TestByteOperation itemGiveTest = new TestByteOperation();
        itemGiveTest.setIndex(randomizeGraphicsFlag);
        itemGiveTest.setValue((byte) 2);
        itemGiveTest.setOp(ByteOp.FLAG_EQUALS);
        itemGive.getTestByteOperations().add(itemGiveTest);

        itemGiveTest = new TestByteOperation();
        itemGiveTest.setIndex(worldFlag);
        itemGiveTest.setValue((byte) 2);
        itemGiveTest.setOp(ByteOp.FLAG_NOT_EQUAL);
        itemGive.getTestByteOperations().add(itemGiveTest);

        WriteByteOperation itemGiveUpdate = new WriteByteOperation();
        itemGiveUpdate.setIndex(worldFlag);
        itemGiveUpdate.setValue((byte) 2);
        itemGiveUpdate.setOp(ByteOp.ASSIGN_FLAG);
        itemGive.getWriteByteOperations().add(itemGiveUpdate);

        referenceObj.getObjectContainer().getObjects().add(itemGive);
    }

    public static void addGrailDetector(GameObject gameObject, int grailFlag) {
        GameObject grailDetector = new GameObject(gameObject.getObjectContainer());
        grailDetector.setId((short)0x14);
        grailDetector.setX(gameObject.getX());
        grailDetector.setY(gameObject.getY() - 20);

        grailDetector.getArgs().add((short)0); // seconds wait
        grailDetector.getArgs().add((short)0); // frames wait
        grailDetector.getArgs().add((short)0); // continuous/total
        grailDetector.getArgs().add((short)0); // interaction type 0 = any time except paused 1 = 2 = 3 = 4 = just be on the ground, ok. default: sleep
        grailDetector.getArgs().add((short)2); // graphical tile width
        grailDetector.getArgs().add((short)3); // graphical tile height

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(grailFlag);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        grailDetector.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(grailFlag);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(1);
        grailDetector.getWriteByteOperations().add(writeByteOperation);

        gameObject.getObjectContainer().getObjects().add(grailDetector);
    }

    /**
     * Add exit to untrue shrine so it doesn't break during the escape
     * @param screen the screen to add the object to
     */
    public static GameObject addUntrueShrineExit(Screen screen, int whichShrineExit) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0xc4);
        if(whichShrineExit == 0) {
            obj.setX(300);
            obj.setY(940);
            obj.getArgs().add((short) 6);
            obj.getArgs().add((short) 7);
            obj.getArgs().add((short) 1);
            obj.getArgs().add((short) 300);
            obj.getArgs().add((short) 20);
        }
        else if(whichShrineExit == 1) {
            obj.setX(300);
            obj.setY(460);
            obj.getArgs().add((short) 8);
            obj.getArgs().add((short) 2);
            obj.getArgs().add((short) 3);
            obj.getArgs().add((short) 300);
            obj.getArgs().add((short) 20);
        }
        else {
            obj.setX(940);
            obj.setY(460);
            obj.getArgs().add((short) 7);
            obj.getArgs().add((short) 9);
            obj.getArgs().add((short) 1);
            obj.getArgs().add((short) 300);
            obj.getArgs().add((short) 20);
        }
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x382);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        obj.getTestByteOperations().add(testByteOperation);

        screen.getObjects().add(obj);
        return obj;
    }

    /**
     * Add exit to untrue shrine so it doesn't break during the escape
     * @param screen the screen to add the object to
     */
    public static GameObject addSpecialTransitionWarp(Screen screen, int zoneIndex) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x97);

        obj.getArgs().add((short) zoneIndex);
        obj.getArgs().add((short) 8);
        obj.getArgs().add((short) 1);

        if(zoneIndex == 0) {
            obj.setX(140);
            obj.setY(440);

            obj.getArgs().add((short)300);
            obj.getArgs().add((short)20);
        }
        else if(zoneIndex == 5){
            obj.setX(300);
            obj.setY(440);

            obj.getArgs().add((short)140);
            obj.getArgs().add((short)20);
        }
        obj.getArgs().add((short)4);
        obj.getArgs().add((short)4);
        obj.getArgs().add((short)4);
        obj.getArgs().add((short)4);

        screen.getObjects().add(obj);
        return obj;
    }

    /**
     * Add exit to untrue shrine so it doesn't break during the escape
     * @param screen the screen to add the object to
     */
    public static GameObject addSpecialTransitionGate(Screen screen, int zoneIndex) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0xc4);

        obj.getArgs().add((short) zoneIndex);
        obj.getArgs().add((short) 8);
        obj.getArgs().add((short) 0);

        if(zoneIndex == 0) {
            obj.setX(140);
            obj.setY(460);

            obj.getArgs().add((short)300);
            obj.getArgs().add((short)392);
            obj.getArgs().add((short)0);
        }
        else if(zoneIndex == 5){
            obj.setX(300);
            obj.setY(460);

            obj.getArgs().add((short)140);
            obj.getArgs().add((short)392);
            obj.getArgs().add((short)0);
        }
        obj.getArgs().add((short)0);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x382);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        screen.getObjects().add(obj);
        addEscapeGate(obj);
        return obj;
    }

    public static GameObject addEscapeGate(GameObject nonEscapeGate) {
        GameObject escapeGate = new GameObject(nonEscapeGate);
        for(TestByteOperation testByteOperation : escapeGate.getTestByteOperations()) {
            if(testByteOperation.getIndex() == 0x382 && testByteOperation.getValue() == 0) {
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
        testByteOperation.setIndex(0x102);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)9);

        // Add extra check for not during escape, since escape door is different.
        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x382);
        testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
        testByteOperation.setValue((byte)1);
        trueShrineGate.getTestByteOperations().add(testByteOperation);

        trueShrineGate.getArgs().set(0, (short)18);
        trueShrineGate.getArgs().set(6, (short)0);
        basicGate.getObjectContainer().getObjects().add(trueShrineGate);
    }

    public static void addShrineMapSoundEffect(ObjectContainer objectContainer) {
        GameObject shrineMapSoundEffect = new GameObject(objectContainer);
        shrineMapSoundEffect.setId((short)0x9b);
        shrineMapSoundEffect.getArgs().add((short)30);
        shrineMapSoundEffect.getArgs().add((short)120);
        shrineMapSoundEffect.getArgs().add((short)64);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.getArgs().add((short)120);
        shrineMapSoundEffect.getArgs().add((short)64);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.getArgs().add((short)25);
        shrineMapSoundEffect.getArgs().add((short)1);
        shrineMapSoundEffect.getArgs().add((short)5);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.getArgs().add((short)10);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.getArgs().add((short)0);
        shrineMapSoundEffect.setX(-1);
        shrineMapSoundEffect.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(218);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)2);
        shrineMapSoundEffect.getTestByteOperations().add(testFlag);

        testFlag = new TestByteOperation();
        testFlag.setIndex(42);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)1);
        shrineMapSoundEffect.getTestByteOperations().add(testFlag);

        GameObject shrineMapSoundEffectRemovalTimer = new GameObject(objectContainer);
        shrineMapSoundEffectRemovalTimer.setId((short) 0x0b);
        shrineMapSoundEffectRemovalTimer.getArgs().add((short) 0);
        shrineMapSoundEffectRemovalTimer.getArgs().add((short) 0);
        shrineMapSoundEffectRemovalTimer.setX(-1);
        shrineMapSoundEffectRemovalTimer.setY(-1);

        TestByteOperation shrineMapSoundEffectRemovalTimerFlagTest = new TestByteOperation();
        shrineMapSoundEffectRemovalTimerFlagTest.setIndex(2798);
        shrineMapSoundEffectRemovalTimerFlagTest.setValue((byte) 0);
        shrineMapSoundEffectRemovalTimerFlagTest.setOp(ByteOp.FLAG_EQUALS);
        shrineMapSoundEffectRemovalTimer.getTestByteOperations().add(shrineMapSoundEffectRemovalTimerFlagTest);

        shrineMapSoundEffectRemovalTimerFlagTest = new TestByteOperation();
        shrineMapSoundEffectRemovalTimerFlagTest.setIndex(218);
        shrineMapSoundEffectRemovalTimerFlagTest.setValue((byte) 2);
        shrineMapSoundEffectRemovalTimerFlagTest.setOp(ByteOp.FLAG_EQUALS);
        shrineMapSoundEffectRemovalTimer.getTestByteOperations().add(shrineMapSoundEffectRemovalTimerFlagTest);

        WriteByteOperation shrineMapSoundEffectRemovalTimerFlagUpdate = new WriteByteOperation();
        shrineMapSoundEffectRemovalTimerFlagUpdate.setIndex(2798);
        shrineMapSoundEffectRemovalTimerFlagUpdate.setValue((byte) 1);
        shrineMapSoundEffectRemovalTimerFlagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        shrineMapSoundEffectRemovalTimer.getWriteByteOperations().add(shrineMapSoundEffectRemovalTimerFlagUpdate);

        shrineMapSoundEffectRemovalTimerFlagUpdate = new WriteByteOperation();
        shrineMapSoundEffectRemovalTimerFlagUpdate.setIndex(42);
        shrineMapSoundEffectRemovalTimerFlagUpdate.setValue((byte) 1);
        shrineMapSoundEffectRemovalTimerFlagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        shrineMapSoundEffectRemovalTimer.getWriteByteOperations().add(shrineMapSoundEffectRemovalTimerFlagUpdate);

        objectContainer.getObjects().add(0, shrineMapSoundEffect);
        objectContainer.getObjects().add(0, shrineMapSoundEffectRemovalTimer);
    }

    public static void addMotherAnkhJewelRecoveryTimer(Screen screen) {
        GameObject jewelRecoveryTimer = new GameObject(screen);
        jewelRecoveryTimer.setId((short) 0x0b);
        jewelRecoveryTimer.getArgs().add((short) 0);
        jewelRecoveryTimer.getArgs().add((short) 0);
        jewelRecoveryTimer.setX(-1);
        jewelRecoveryTimer.setY(-1);

        TestByteOperation flagTest = new TestByteOperation();
        flagTest.setIndex(0x0fe);
        flagTest.setOp(ByteOp.FLAG_EQUALS);
        flagTest.setValue((byte) 2);
        jewelRecoveryTimer.getTestByteOperations().add(flagTest);

        flagTest = new TestByteOperation();
        flagTest.setIndex(0x382);
        flagTest.setOp(ByteOp.FLAG_EQUALS);
        flagTest.setValue((byte) 0);
        jewelRecoveryTimer.getTestByteOperations().add(flagTest);

        flagTest = new TestByteOperation();
        flagTest.setIndex(0xad2);
        flagTest.setOp(ByteOp.FLAG_EQUALS);
        flagTest.setValue((byte) 0);
        jewelRecoveryTimer.getTestByteOperations().add(flagTest);

        WriteByteOperation flagUpdate = new WriteByteOperation();
        flagUpdate.setIndex(0xad2);
        flagUpdate.setValue((byte) 1);
        flagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        jewelRecoveryTimer.getWriteByteOperations().add(flagUpdate);

        screen.getObjects().add(0, jewelRecoveryTimer);
    }

    public static void addMotherAnkhJewelItemGive(Screen screen) {
        GameObject itemGive = new GameObject(screen);
        itemGive.setId((short) 0xb5);
        itemGive.setX(100);
        itemGive.setY(60);

        itemGive.getArgs().add((short)19);
        itemGive.getArgs().add((short)12);
        itemGive.getArgs().add((short)16);
        itemGive.getArgs().add((short)39);

        TestByteOperation itemGiveTest = new TestByteOperation();
        itemGiveTest.setIndex(0xad2);
        itemGiveTest.setValue((byte) 1);
        itemGiveTest.setOp(ByteOp.FLAG_EQUALS);
        itemGive.getTestByteOperations().add(itemGiveTest);

        WriteByteOperation itemGiveUpdate = new WriteByteOperation();
        itemGiveUpdate.setIndex(0xad2);
        itemGiveUpdate.setValue((byte) 0);
        itemGiveUpdate.setOp(ByteOp.ASSIGN_FLAG);
        itemGive.getWriteByteOperations().add(itemGiveUpdate);

        itemGiveUpdate = new WriteByteOperation();
        itemGiveUpdate.setIndex(0x0fe);
        itemGiveUpdate.setValue((byte) 1);
        itemGiveUpdate.setOp(ByteOp.ASSIGN_FLAG);
        itemGive.getWriteByteOperations().add(itemGiveUpdate);

        screen.getObjects().add(itemGive);
    }

    public static GameObject addMissingBacksideDoorCover(GameObject backsideDoor, int gateFlag) {
        GameObject doorCoverGraphic = new GameObject(backsideDoor.getObjectContainer());
        doorCoverGraphic.setId((short)0x93);
        doorCoverGraphic.setX(backsideDoor.getX());
        doorCoverGraphic.setY(backsideDoor.getY());
        doorCoverGraphic.getArgs().add((short)-1); // Layer
        doorCoverGraphic.getArgs().add((short)7); // 01.effect.png for anything not 0-6?
        doorCoverGraphic.getArgs().add((short)0); // Imagex
        doorCoverGraphic.getArgs().add((short)360); // Imagey
        doorCoverGraphic.getArgs().add((short)40); // dx
        doorCoverGraphic.getArgs().add((short)40); // dy
        doorCoverGraphic.getArgs().add((short)1); // 0: act as if animation already played; 1: allow animation; 2: ..?
        doorCoverGraphic.getArgs().add((short)7); // Animation frames
        doorCoverGraphic.getArgs().add((short)6); // Pause frames
        doorCoverGraphic.getArgs().add((short)1); // Repeat count (<1 is forever)
        doorCoverGraphic.getArgs().add((short)0); // Hittile to fill with
        doorCoverGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        doorCoverGraphic.getArgs().add((short)5); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        doorCoverGraphic.getArgs().add((short)0); // Cycle colors t/f
        doorCoverGraphic.getArgs().add((short)0); // Alpha/frame
        doorCoverGraphic.getArgs().add((short)255); // Max alpha
        doorCoverGraphic.getArgs().add((short)0); // R/frame
        doorCoverGraphic.getArgs().add((short)0); // Max R
        doorCoverGraphic.getArgs().add((short)0); // G/frame
        doorCoverGraphic.getArgs().add((short)0); // Max G
        doorCoverGraphic.getArgs().add((short)0); // B/frame
        doorCoverGraphic.getArgs().add((short)0); // Max B
        doorCoverGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        doorCoverGraphic.getArgs().add((short)1); // not0?

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(gateFlag);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)0);
        doorCoverGraphic.getTestByteOperations().add(testFlag);

        testFlag = new TestByteOperation();
        testFlag.setIndex(0x0ae);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)2);
        doorCoverGraphic.getTestByteOperations().add(testFlag);

        WriteByteOperation updateFlag = new WriteByteOperation();
        updateFlag.setIndex(gateFlag);
        updateFlag.setValue((byte) 1);
        updateFlag.setOp(ByteOp.ASSIGN_FLAG);
        doorCoverGraphic.getWriteByteOperations().add(updateFlag);

        backsideDoor.getObjectContainer().getObjects().add(doorCoverGraphic);
        return doorCoverGraphic;
    }

    public static GameObject addMissingBacksideDoorTimerAndSound(ObjectContainer objectContainer, int bossFlag, int gateFlag) {
        GameObject doorSoundEffect = new GameObject(objectContainer);
        doorSoundEffect.setId((short)0x9b);
        doorSoundEffect.getArgs().add((short)95);
        doorSoundEffect.getArgs().add((short)127);
        doorSoundEffect.getArgs().add((short)64); // balance initial
        doorSoundEffect.getArgs().add((short)-400);
        doorSoundEffect.getArgs().add((short)127);
        doorSoundEffect.getArgs().add((short)64); // balance final
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)15);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)10);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.getArgs().add((short)0);
        doorSoundEffect.setX(-1);
        doorSoundEffect.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(0x029);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)1);
        doorSoundEffect.getTestByteOperations().add(testFlag);

        GameObject doorTimer = new GameObject(objectContainer);
        doorTimer.setId((short) 0x0b);
        doorTimer.getArgs().add((short) 1);
        doorTimer.getArgs().add((short) 0);
        doorTimer.setX(-1);
        doorTimer.setY(-1);

        TestByteOperation doorTimerTest = new TestByteOperation();
        doorTimerTest.setIndex(bossFlag);
        doorTimerTest.setValue((byte) 3);
        doorTimerTest.setOp(ByteOp.FLAG_GTEQ);
        doorTimer.getTestByteOperations().add(doorTimerTest);

        doorTimerTest = new TestByteOperation();
        doorTimerTest.setIndex(0x0ae);
        doorTimerTest.setValue((byte) 2);
        doorTimerTest.setOp(ByteOp.FLAG_EQUALS);
        doorTimer.getTestByteOperations().add(doorTimerTest);

        doorTimerTest = new TestByteOperation();
        doorTimerTest.setIndex(gateFlag);
        doorTimerTest.setValue((byte) 0);
        doorTimerTest.setOp(ByteOp.FLAG_EQUALS);
        doorTimer.getTestByteOperations().add(doorTimerTest);

        WriteByteOperation doorTimerUpdate = new WriteByteOperation();
        doorTimerUpdate.setIndex(gateFlag);
        doorTimerUpdate.setValue((byte) 1);
        doorTimerUpdate.setOp(ByteOp.ASSIGN_FLAG);
        doorTimer.getWriteByteOperations().add(doorTimerUpdate);

        doorTimerUpdate = new WriteByteOperation();
        doorTimerUpdate.setIndex(0x029);
        doorTimerUpdate.setValue((byte) 1);
        doorTimerUpdate.setOp(ByteOp.ASSIGN_FLAG);
        doorTimer.getWriteByteOperations().add(doorTimerUpdate);

        objectContainer.getObjects().add(0, doorSoundEffect);
        objectContainer.getObjects().add(0, doorTimer);
        return doorTimer;
    }

    public static GameObject addMissingBacksideMirrorTimerAndSound(ObjectContainer objectContainer, int mirrorCoverFlag) {
        GameObject mirrorSoundEffect = new GameObject(objectContainer);
        mirrorSoundEffect.setId((short)0x9b);
        mirrorSoundEffect.getArgs().add((short)48); // sound effect
        mirrorSoundEffect.getArgs().add((short)127); // volume initial
        mirrorSoundEffect.getArgs().add((short)64); // balance initial
        mirrorSoundEffect.getArgs().add((short)-700); // pitch initial
        mirrorSoundEffect.getArgs().add((short)127); // volume final
        mirrorSoundEffect.getArgs().add((short)64); // balance final
        mirrorSoundEffect.getArgs().add((short)-700); // pitch final
        mirrorSoundEffect.getArgs().add((short)20); // voice priority
        mirrorSoundEffect.getArgs().add((short)0); // UNUSED????
        mirrorSoundEffect.getArgs().add((short)0); // frames to wait before playing
        mirrorSoundEffect.getArgs().add((short)0); // vibrate (bool)
        mirrorSoundEffect.getArgs().add((short)10); // ??? vibration strength
        mirrorSoundEffect.getArgs().add((short)10); // volume slide frames
        mirrorSoundEffect.getArgs().add((short)0); // balance slide frames
        mirrorSoundEffect.getArgs().add((short)0); // pitch slide frames
        mirrorSoundEffect.setX(-1);
        mirrorSoundEffect.setY(-1);

        TestByteOperation testFlag = new TestByteOperation();
        testFlag.setIndex(0x028);
        testFlag.setOp(ByteOp.FLAG_EQUALS);
        testFlag.setValue((byte)1);
        mirrorSoundEffect.getTestByteOperations().add(testFlag);

        GameObject mirrorTimer = new GameObject(objectContainer);
        mirrorTimer.setId((short) 0x0b);
        mirrorTimer.getArgs().add((short) 0);
        mirrorTimer.getArgs().add((short) 30);
        mirrorTimer.setX(-1);
        mirrorTimer.setY(-1);

        TestByteOperation mirrorTimerTest = new TestByteOperation();
        mirrorTimerTest.setIndex(0x0ae);
        mirrorTimerTest.setValue((byte) 2);
        mirrorTimerTest.setOp(ByteOp.FLAG_EQUALS);
        mirrorTimer.getTestByteOperations().add(mirrorTimerTest);

        mirrorTimerTest = new TestByteOperation();
        mirrorTimerTest.setIndex(mirrorCoverFlag);
        mirrorTimerTest.setValue((byte) 0);
        mirrorTimerTest.setOp(ByteOp.FLAG_EQUALS);
        mirrorTimer.getTestByteOperations().add(mirrorTimerTest);

        WriteByteOperation mirrorTimerUpdate = new WriteByteOperation();
        mirrorTimerUpdate.setIndex(mirrorCoverFlag);
        mirrorTimerUpdate.setValue((byte) 1);
        mirrorTimerUpdate.setOp(ByteOp.ASSIGN_FLAG);
        mirrorTimer.getWriteByteOperations().add(mirrorTimerUpdate);

        mirrorTimerUpdate = new WriteByteOperation();
        mirrorTimerUpdate.setIndex(0x028);
        mirrorTimerUpdate.setValue((byte) 1);
        mirrorTimerUpdate.setOp(ByteOp.ASSIGN_FLAG);
        mirrorTimer.getWriteByteOperations().add(mirrorTimerUpdate);

        objectContainer.getObjects().add(0, mirrorSoundEffect);
        objectContainer.getObjects().add(0, mirrorTimer);
        return mirrorTimer;
    }

    public static GameObject addMissingBacksideDoorMirrorCoverGraphic(GameObject backsideDoor, int mirrorCoverFlag, boolean extinctionDoor) {
        GameObject mirrorCoverGraphic = new GameObject(backsideDoor.getObjectContainer());
        mirrorCoverGraphic.setId((short) 0x93);
        mirrorCoverGraphic.setX(backsideDoor.getX() - 20);
        mirrorCoverGraphic.setY(backsideDoor.getY() - 40);
        mirrorCoverGraphic.getArgs().add((short)-1); // Layer
        mirrorCoverGraphic.getArgs().add((short)1); // 01.effect.png for anything not 0-6?
        if(extinctionDoor) {
            mirrorCoverGraphic.getArgs().add((short)940); // Imagex
            mirrorCoverGraphic.getArgs().add((short)120); // Imagey
        }
        else {
            mirrorCoverGraphic.getArgs().add((short)540); // Imagex
            mirrorCoverGraphic.getArgs().add((short)0); // Imagey
        }
        mirrorCoverGraphic.getArgs().add((short)80); // dx
        mirrorCoverGraphic.getArgs().add((short)80); // dy
        mirrorCoverGraphic.getArgs().add((short)1); // 0: act as if animation already played; 1: allow animation; 2: ..?
        mirrorCoverGraphic.getArgs().add((short)1); // Animation frames
        mirrorCoverGraphic.getArgs().add((short)4); // Pause frames
        mirrorCoverGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        mirrorCoverGraphic.getArgs().add((short)0); // Hittile to fill with
        mirrorCoverGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        mirrorCoverGraphic.getArgs().add((short)1); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        mirrorCoverGraphic.getArgs().add((short)0); // Cycle colors t/f
        mirrorCoverGraphic.getArgs().add((short)0); // Alpha/frame
        mirrorCoverGraphic.getArgs().add((short)255); // Max alpha
        mirrorCoverGraphic.getArgs().add((short)0); // R/frame
        mirrorCoverGraphic.getArgs().add((short)0); // Max R
        mirrorCoverGraphic.getArgs().add((short)0); // G/frame
        mirrorCoverGraphic.getArgs().add((short)0); // Max G
        mirrorCoverGraphic.getArgs().add((short)0); // B/frame
        mirrorCoverGraphic.getArgs().add((short)0); // Max B
        mirrorCoverGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        mirrorCoverGraphic.getArgs().add((short)1); // not0?

        TestByteOperation mirrorCoverTest = new TestByteOperation();
        mirrorCoverTest.setIndex(mirrorCoverFlag);
        mirrorCoverTest.setValue((byte) 0);
        mirrorCoverTest.setOp(ByteOp.FLAG_EQUALS);
        mirrorCoverGraphic.getTestByteOperations().add(mirrorCoverTest);

        backsideDoor.getObjectContainer().getObjects().add(mirrorCoverGraphic);
        return mirrorCoverGraphic;
    }

    public static void addExtinctionTorch(Screen screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x12);
        obj.setX(60);
        obj.setY(80);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)4);
        obj.getArgs().add((short)11);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)2);
        obj.getArgs().add((short)2);
        obj.getArgs().add((short)-1);
        obj.getArgs().add((short)-1);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x1c2);
        testByteOperation.setOp(ByteOp.FLAG_LTEQ);
        testByteOperation.setValue((byte)2);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x1cd);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(1);
        obj.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(obj);

        obj = new GameObject(screen);
        obj.setId((short)0x93);
        obj.setX(60);
        obj.setY(80);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)80);
        obj.getArgs().add((short)400);
        obj.getArgs().add((short)40);
        obj.getArgs().add((short)40);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)4);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)5);
        obj.getArgs().add((short)160);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)1);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x1cd);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        obj.getTestByteOperations().add(testByteOperation);

        screen.getObjects().add(obj);

        obj = new GameObject(screen);
        obj.setId((short)0x93);
        obj.setX(60);
        obj.setY(80);
        obj.getArgs().add((short)-1);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)40);
        obj.getArgs().add((short)400);
        obj.getArgs().add((short)40);
        obj.getArgs().add((short)40);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)255);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)1);

        screen.getObjects().add(obj);
    }

    /**
     * Add ladder to help with Dimensional Sacred Orb chest after Ushumgallu's death.
     * @param screen the screen to add the timers to
     */
    public static void addDimensionalOrbLadder(ObjectContainer screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0x07);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)8);
        obj.getArgs().add((short)2);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)660);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)1);
        obj.setX(560);
        obj.setY(620);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x2cc);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)2);
        obj.getTestByteOperations().add(testByteOperation);

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
            halloweenBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 1));
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

            halloweenBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 0)); // Can-exit flag

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
            npcCountBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)0xace, (short)hintNumber));
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
        foolsOptionBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 1)); // Can-exit flag
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
        foolsOptionBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 0)); // Can-exit flag
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
        bookOfTheDeadConversation.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_NOT_EQUAL, 1));
        bookOfTheDeadConversation.getTestByteOperations().add(new TestByteOperation(0x32a, ByteOp.FLAG_EQUALS, 1));
        bookOfTheDeadConversation.getTestByteOperations().add(new TestByteOperation(0x391, ByteOp.FLAG_GTEQ, 1));

        GameObject optionConversation = new GameObject(escapeConversationNormal);
        optionConversation.getArgs().set(4, (short)optionMasterNpcBlock.getBlockNumber());
        optionConversation.getTestByteOperations().clear();
        optionConversation.getTestByteOperations().add(new TestByteOperation(0x382, ByteOp.FLAG_NOT_EQUAL, 1)); // Option to quit
        optionConversation.getTestByteOperations().add(new TestByteOperation(0x32a, ByteOp.FLAG_NOT_EQUAL, 1));
        optionConversation.getTestByteOperations().add(new TestByteOperation(0x391, ByteOp.FLAG_GTEQ, 1));

        mulbrukScreen.getObjects().clear();
        mulbrukScreen.getObjects().addAll(keptObjects);
        mulbrukScreen.getObjects().add(bookOfTheDeadConversation);
        mulbrukScreen.getObjects().add(optionConversation);
    }

    public static int addDevRoomHintBlock(List<Block> blocks, boolean updateConversationFlag) {
        Block devHintBlock = new Block(blocks.size());
        if(updateConversationFlag) {
            devHintBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)0xace, (short)0));
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
        mulbrukHTBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 1));

        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.htMulbruk1"));
        for (Short shortCharacter : stringCharacters) {
            mulbrukHTBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        mulbrukHTBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)0x3bb, (short)1)); // Unlock HT
        mulbrukHTBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)0xace, (short)totalHints)); // Set 0xace (Mulbruk hints cycle flag) to +1
        mulbrukHTBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

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
        mulbrukHTBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 0));

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

        blocks.add(htExplanation);

        GameObject tabletReadable = new GameObject(screen);
        tabletReadable.setId((short)0x9e);
        tabletReadable.setX(60);
        tabletReadable.setY(400);

        tabletReadable.getArgs().add((short)htExplanation.getBlockNumber());
        tabletReadable.getArgs().add((short)0);
        tabletReadable.getArgs().add((short)0);
        tabletReadable.getArgs().add((short)1);
        tabletReadable.getArgs().add((short)1);
        tabletReadable.getArgs().add((short)1);
        tabletReadable.getArgs().add((short)1);

        tabletReadable.getArgs().add((short)1);
        tabletReadable.getArgs().add((short)1);
        tabletReadable.getArgs().add((short)0);

        tabletReadable.getArgs().add((short)40);
        tabletReadable.getArgs().add((short)40);

        screen.getObjects().add(tabletReadable);

        GameObject tabletGraphic = new GameObject(screen);
        tabletGraphic.setId((short)0x93);
        tabletGraphic.setX(60);
        tabletGraphic.setY(400);

        tabletGraphic.getArgs().add((short)-1);
        tabletGraphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        tabletGraphic.getArgs().add((short)0);
        tabletGraphic.getArgs().add((short)0);
        tabletGraphic.getArgs().add((short)40); // dx
        tabletGraphic.getArgs().add((short)40); // dy
        tabletGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        tabletGraphic.getArgs().add((short)0); // Animation frames
        tabletGraphic.getArgs().add((short)1); // Pause frames
        tabletGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        tabletGraphic.getArgs().add((short)0); // Hittile to fill with
        tabletGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        tabletGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        tabletGraphic.getArgs().add((short)0); // Cycle colors t/f
        tabletGraphic.getArgs().add((short)0); // Alpha/frame
        tabletGraphic.getArgs().add((short)255); // Max alpha
        tabletGraphic.getArgs().add((short)0); // R/frame
        tabletGraphic.getArgs().add((short)0); // Max R
        tabletGraphic.getArgs().add((short)0); // G/frame
        tabletGraphic.getArgs().add((short)0); // Max G
        tabletGraphic.getArgs().add((short)0); // B/frame
        tabletGraphic.getArgs().add((short)0); // Max B
        tabletGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        tabletGraphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(tabletGraphic);

        GameObject htSkipDais = new GameObject(screen);
        htSkipDais.setId((short)0x08);
        htSkipDais.setX(60);
        htSkipDais.setY(420);

        htSkipDais.getArgs().add((short)0); // (0-1) Light red dust or pink dust
        htSkipDais.getArgs().add((short)60); // (1-270) Falling time (in frames?)
        htSkipDais.getArgs().add((short)-1); // (-1-50) RiseFlag -1 Never Rise. 0 Always Rise
        htSkipDais.getArgs().add((short)2); // (0-2) Image
        htSkipDais.getArgs().add((short)0); // (0) (unused?)
        htSkipDais.getArgs().add((short)860); // (180-860) ImageX
        htSkipDais.getArgs().add((short)60); // (0-100) ImageY
        htSkipDais.getArgs().add((short)1); // (0-1) Width 0 = Half-width, 1 = Full-width
        htSkipDais.getArgs().add((short)10); // (0-10) (probably unused height)
        htSkipDais.getArgs().add((short)60); // (0-60) RiseSpeed

        htSkipDais.getTestByteOperations().add(new TestByteOperation(0x70d, ByteOp.FLAG_EQUALS, 0));
        htSkipDais.getWriteByteOperations().add(new WriteByteOperation(0x70d, ByteOp.ASSIGN_FLAG, 1));

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

        blocks.add(htExplanation);

        GameObject tabletReadable = new GameObject(screen);
        tabletReadable.setId((short)0x9e);
        tabletReadable.setX(120);
        tabletReadable.setY(400);

        tabletReadable.getArgs().add((short)htExplanation.getBlockNumber());
        tabletReadable.getArgs().add((short)0);
        tabletReadable.getArgs().add((short)0);
        tabletReadable.getArgs().add((short)1);
        tabletReadable.getArgs().add((short)1);
        tabletReadable.getArgs().add((short)1);
        tabletReadable.getArgs().add((short)1);

        tabletReadable.getArgs().add((short)1);
        tabletReadable.getArgs().add((short)1);
        tabletReadable.getArgs().add((short)0);

        tabletReadable.getArgs().add((short)40);
        tabletReadable.getArgs().add((short)40);

        screen.getObjects().add(tabletReadable);

        GameObject tabletGraphic = new GameObject(screen);
        tabletGraphic.setId((short)0x93);
        tabletGraphic.setX(120);
        tabletGraphic.setY(400);

        tabletGraphic.getArgs().add((short)-1);
        tabletGraphic.getArgs().add((short)0); // 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        tabletGraphic.getArgs().add((short)0);
        tabletGraphic.getArgs().add((short)0);
        tabletGraphic.getArgs().add((short)40); // dx
        tabletGraphic.getArgs().add((short)40); // dy
        tabletGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        tabletGraphic.getArgs().add((short)0); // Animation frames
        tabletGraphic.getArgs().add((short)1); // Pause frames
        tabletGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        tabletGraphic.getArgs().add((short)0); // Hittile to fill with
        tabletGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        tabletGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        tabletGraphic.getArgs().add((short)0); // Cycle colors t/f
        tabletGraphic.getArgs().add((short)0); // Alpha/frame
        tabletGraphic.getArgs().add((short)255); // Max alpha
        tabletGraphic.getArgs().add((short)0); // R/frame
        tabletGraphic.getArgs().add((short)0); // Max R
        tabletGraphic.getArgs().add((short)0); // G/frame
        tabletGraphic.getArgs().add((short)0); // Max G
        tabletGraphic.getArgs().add((short)0); // B/frame
        tabletGraphic.getArgs().add((short)0); // Max B
        tabletGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        tabletGraphic.getArgs().add((short)1); // not0?

        screen.getObjects().add(tabletGraphic);
    }

    /**
     * Add a door to twin labs (replacing Ellmac fall), using boss ankh as a reference object)
     * @param reference boss ankh to use for determining coordinates/screen for adding the door
     */
    public static void addTwinLabsDoor(GameObject reference) {
        GameObject doorGraphic = new GameObject(reference.getObjectContainer());
        doorGraphic.setId((short)0x93);
        doorGraphic.setX(reference.getX() - 20);
        doorGraphic.setY(reference.getY() - 40);
        doorGraphic.getArgs().add((short)0); // Layer
        doorGraphic.getArgs().add((short)7); // 01.effect.png for anything not 0-6?
        doorGraphic.getArgs().add((short)80); // Imagex
        doorGraphic.getArgs().add((short)512); // Imagey
        doorGraphic.getArgs().add((short)80); // dx
        doorGraphic.getArgs().add((short)80); // dy
        doorGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
        doorGraphic.getArgs().add((short)0); // Animation frames
        doorGraphic.getArgs().add((short)1); // Pause frames
        doorGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
        doorGraphic.getArgs().add((short)0); // Hittile to fill with
        doorGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
        doorGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
        doorGraphic.getArgs().add((short)0); // Cycle colors t/f
        doorGraphic.getArgs().add((short)0); // Alpha/frame
        doorGraphic.getArgs().add((short)255); // Max alpha
        doorGraphic.getArgs().add((short)0); // R/frame
        doorGraphic.getArgs().add((short)0); // Max R
        doorGraphic.getArgs().add((short)0); // G/frame
        doorGraphic.getArgs().add((short)0); // Max G
        doorGraphic.getArgs().add((short)0); // B/frame
        doorGraphic.getArgs().add((short)0); // Max B
        doorGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
        doorGraphic.getArgs().add((short)1); // not0?

        reference.getObjectContainer().getObjects().add(doorGraphic);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x0fb);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte)3);
        doorGraphic.getTestByteOperations().add(testByteOperation);

        GameObject door = new GameObject(reference.getObjectContainer());
        door.setId((short)0x98);
        door.setX(reference.getX());
        door.setY(reference.getY());

        door.getArgs().add((short)0);
        door.getArgs().add((short)7);
        door.getArgs().add((short)0);
        door.getArgs().add((short)0);
        door.getArgs().add((short)300);
        door.getArgs().add((short)0);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x0fb);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte)3);
        door.getTestByteOperations().add(testByteOperation);

        reference.getObjectContainer().getObjects().add(doorGraphic);
        reference.getObjectContainer().getObjects().add(door);
    }

    /**
     * Timers important for marking a boss defeated. Relevant for random boss exits since
     * some bosses have these timers on a different screen.
     * @param screen
     * @param bossFlag
     * @param otherFlag
     */
    public static void addBossTimer(Screen screen, int bossFlag, int otherFlag) {
        GameObject bossTimer1 = new GameObject(screen);
        bossTimer1.setId((short)0x0b);
        bossTimer1.setX(-1);
        bossTimer1.setY(-1);

        bossTimer1.getArgs().add((short)0);
        bossTimer1.getArgs().add((short)0);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(bossFlag);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)3);
        bossTimer1.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(otherFlag);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        bossTimer1.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x102);
        writeByteOperation.setOp(ByteOp.ADD_FLAG);
        writeByteOperation.setValue(1);
        bossTimer1.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(otherFlag);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(1);
        bossTimer1.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x07b);
        writeByteOperation.setOp(ByteOp.ADD_FLAG);
        writeByteOperation.setValue(8);
        bossTimer1.getWriteByteOperations().add(writeByteOperation);

        if(bossFlag == 0x0fb) {
            writeByteOperation = new WriteByteOperation();
            writeByteOperation.setIndex(0x3b8);
            writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
            writeByteOperation.setValue(3);
            bossTimer1.getWriteByteOperations().add(writeByteOperation);
        }

        screen.getObjects().add(0, bossTimer1);

        GameObject bossTimer2 = new GameObject(screen);
        bossTimer2.setId((short)0x0b);
        bossTimer2.setX(-1);
        bossTimer2.setY(-1);

        bossTimer2.getArgs().add((short)0);
        bossTimer2.getArgs().add((short)0);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x102);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)8);
        bossTimer2.getTestByteOperations().add(testByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x102);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(9);
        bossTimer2.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x07b);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(200);
        bossTimer2.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x06c);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(0);
        bossTimer2.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x2e1);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(1);
        bossTimer2.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(0, bossTimer2);
    }

    /**
     * For marking sphinx defeated when coming from an unusual path (random transitions).
     * @param screen
     */
    public static void addSphinxRemovalTimer(Screen screen) {
        GameObject sphinxRemovalTimer = new GameObject(screen);
        sphinxRemovalTimer.setId((short)0x0b);
        sphinxRemovalTimer.setX(-1);
        sphinxRemovalTimer.setY(-1);

        sphinxRemovalTimer.getArgs().add((short)4);
        sphinxRemovalTimer.getArgs().add((short)0);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x173);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte)1);
        sphinxRemovalTimer.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x173);
        testByteOperation.setOp(ByteOp.FLAG_LT);
        testByteOperation.setValue((byte)5);
        sphinxRemovalTimer.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x173);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(5);
        sphinxRemovalTimer.getWriteByteOperations().add(writeByteOperation);

        writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(0x17d);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(1);
        sphinxRemovalTimer.getWriteByteOperations().add(writeByteOperation);
        screen.getObjects().add(0, sphinxRemovalTimer);
    }

    /**
     * Add 0x95 object
     * @param screen to add to
     * @param x position
     * @param y position
     */
    public static void addEyeOfDivineRetribution(Screen screen, int x, int y) {
        GameObject eyeOfDivineRetribution = new GameObject(screen);
        eyeOfDivineRetribution.setId((short)0x95);
        eyeOfDivineRetribution.setX(x);
        eyeOfDivineRetribution.setY(y);

        eyeOfDivineRetribution.getArgs().add((short)10); // Flag 0x000a
        eyeOfDivineRetribution.getArgs().add((short)0); // 0 = percent hp; 1 = flat damage
        eyeOfDivineRetribution.getArgs().add((short)100); // Damage is percent or flat depending on previous arg.
        screen.getObjects().add(eyeOfDivineRetribution);
    }

    /**
     * Add 0xa9 object
     * @param screen to add to
     * @param x position
     * @param y position
     */
    public static void addPushableBlock(Screen screen, int x, int y, List<TestByteOperation> tests) {
        GameObject pushableBlock = new GameObject(screen);
        pushableBlock.setId((short)0xa9);
        pushableBlock.setX(x);
        pushableBlock.setY(y);

        pushableBlock.getArgs().add((short)1); // Push damage
        pushableBlock.getArgs().add((short)1); // Fall damage

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
        GameObject hitbox = new GameObject(screen);
        hitbox.setId((short)0x12);
        hitbox.setX(x);
        hitbox.setY(y);

        hitbox.getArgs().add((short)0); // visual 1:dust >1: star
        hitbox.getArgs().add((short)1); // 0:hp 1:hits
        hitbox.getArgs().add((short)1); // health
        hitbox.getArgs().add((short)4); // direction: 0 - up 1 - right 2 - down 3 - left 4 - any
        hitbox.getArgs().add((short)18); // weapon type: 0-15 same as word, 16 all main 17 all sub 18 all 19 none
        hitbox.getArgs().add((short)0); // Update Type - 0= break: update all 4 / wrongwep: update none; 1= break: update 0,2 / wrongwep: update 1,3
        hitbox.getArgs().add((short)width); // hitbox sizex
        hitbox.getArgs().add((short)height); // hitbox sizey
        hitbox.getArgs().add((short)105); // Hit Success Sound Effect (-1 for silent)
        hitbox.getArgs().add((short)104); // Hit Fail Sound Effect (-1 for silent)
        hitbox.getArgs().add((short)1); // dust1 density 1
        hitbox.getArgs().add((short)2); // dust2 density 2

        hitbox.getTestByteOperations().addAll(tests);
        hitbox.getWriteByteOperations().addAll(updates);

        screen.getObjects().add(hitbox);
    }

    /**
     * Add 0x96 object
     */
    public static void addExtendingSpikes(GameObject referenceObj, int flagIndex) {
        GameObject extendingSpikes = new GameObject(referenceObj.getObjectContainer());

        extendingSpikes.setId((short) 0x96);
        extendingSpikes.setX(referenceObj.getX() - 20);
        extendingSpikes.setY(referenceObj.getY() + 20);

        extendingSpikes.getArgs().add((short)0);
        extendingSpikes.getArgs().add((short)0);
        extendingSpikes.getArgs().add((short)4);
        extendingSpikes.getArgs().add((short)3);
        extendingSpikes.getArgs().add((short)0); // Activation delay
        extendingSpikes.getArgs().add((short)100);
        extendingSpikes.getArgs().add((short)100);
        extendingSpikes.getArgs().add((short)120);
        extendingSpikes.getArgs().add((short)120);
        extendingSpikes.getArgs().add((short)0);
        extendingSpikes.getArgs().add((short)200);
        extendingSpikes.getArgs().add((short)100);
        extendingSpikes.getArgs().add((short)500);
        extendingSpikes.getArgs().add((short)0);
        extendingSpikes.getArgs().add((short)1);
        extendingSpikes.getArgs().add((short)0);
        extendingSpikes.getArgs().add((short)320);
        extendingSpikes.getArgs().add((short)40);
        extendingSpikes.getArgs().add((short)80);
        extendingSpikes.getArgs().add((short)60);
        extendingSpikes.getArgs().add((short)21);
        extendingSpikes.getArgs().add((short)41);
        extendingSpikes.getArgs().add((short)0);
        extendingSpikes.getArgs().add((short)20);
        extendingSpikes.getArgs().add((short)1);
        extendingSpikes.getArgs().add((short)20);

        extendingSpikes.getWriteByteOperations().add(new WriteByteOperation(flagIndex, ByteOp.ASSIGN_FLAG, 1));
        extendingSpikes.getWriteByteOperations().add(new WriteByteOperation(flagIndex, ByteOp.ASSIGN_FLAG, 0));

        referenceObj.getObjectContainer().getObjects().add(extendingSpikes);

        GameObject failPuzzleSound = new GameObject(referenceObj.getObjectContainer());

        failPuzzleSound.setId((short) 0x9b);
        failPuzzleSound.setX(-1);
        failPuzzleSound.setY(-1);

        failPuzzleSound.getArgs().add((short)80);
        failPuzzleSound.getArgs().add((short)120);
        failPuzzleSound.getArgs().add((short)64);
        failPuzzleSound.getArgs().add((short)0);
        failPuzzleSound.getArgs().add((short)120);
        failPuzzleSound.getArgs().add((short)64);
        failPuzzleSound.getArgs().add((short)0);
        failPuzzleSound.getArgs().add((short)25);
        failPuzzleSound.getArgs().add((short)1);
        failPuzzleSound.getArgs().add((short)5);
        failPuzzleSound.getArgs().add((short)0);
        failPuzzleSound.getArgs().add((short)10);
        failPuzzleSound.getArgs().add((short)0);
        failPuzzleSound.getArgs().add((short)0);
        failPuzzleSound.getArgs().add((short)0);

        failPuzzleSound.getTestByteOperations().add(new TestByteOperation(0xa7, ByteOp.FLAG_EQUALS, 2));
        failPuzzleSound.getTestByteOperations().add(new TestByteOperation(flagIndex, ByteOp.FLAG_EQUALS, 1));

        referenceObj.getObjectContainer().getObjects().add(0, failPuzzleSound);
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
            obj.getTestByteOperations().add(new TestByteOperation(0x165, ByteOp.FLAG_EQUALS, 0));
            obj.getWriteByteOperations().add(new WriteByteOperation(0x165, ByteOp.ASSIGN_FLAG, 1));
            obj.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));

            addSuccessSound(screen);
        }
        else {
            obj.getTestByteOperations().add(new TestByteOperation(0x009, ByteOp.FLAG_EQUALS, 0));
            obj.getWriteByteOperations().add(new WriteByteOperation(0x009, ByteOp.ASSIGN_FLAG, 1));

            addExtendingSpikes(obj, 0x009);
        }
        screen.getObjects().add(obj);
    }

    public static void addSuccessSound(ObjectContainer objectContainer) {
        GameObject successSound = new GameObject(objectContainer);
        successSound.setId((short)0x9b);
        successSound.setX(-1);
        successSound.setY(-1);
        successSound.getArgs().add((short)30);
        successSound.getArgs().add((short)120);
        successSound.getArgs().add((short)64);
        successSound.getArgs().add((short)0);
        successSound.getArgs().add((short)120);
        successSound.getArgs().add((short)64);
        successSound.getArgs().add((short)0);
        successSound.getArgs().add((short)25);
        successSound.getArgs().add((short)1);
        successSound.getArgs().add((short)5);
        successSound.getArgs().add((short)0);
        successSound.getArgs().add((short)10);
        successSound.getArgs().add((short)0);
        successSound.getArgs().add((short)0);
        successSound.getArgs().add((short)0);

        successSound.getTestByteOperations().add(new TestByteOperation(0x0a7, ByteOp.FLAG_EQUALS, 2));
        successSound.getTestByteOperations().add(new TestByteOperation(0x165, ByteOp.ASSIGN_FLAG, 1));
        successSound.getTestByteOperations().add(new TestByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));

        objectContainer.getObjects().add(0, successSound);
    }

    /**
     * Add 0x98 object
     * @param screen to add to
     * @param x position
     * @param y position
     */
    public static void addWarpDoor(Screen screen, int x, int y, int destZone, int destRoom, int destScreen, int destX, int destY, List<TestByteOperation> tests) {
        GameObject warpDoor = new GameObject(screen);

        warpDoor.setId((short) 0x98);
        warpDoor.setX(x);
        warpDoor.setY(y);

        warpDoor.getArgs().add((short)0); // Interaction type: 0 = press up. 1 = press down.
        warpDoor.getArgs().add((short)destZone); // Destination field
        warpDoor.getArgs().add((short)destRoom); // Destination room
        warpDoor.getArgs().add((short)destScreen); // Destination screen
        warpDoor.getArgs().add((short)destX); // Destination screen X
        warpDoor.getArgs().add((short)destY); // Destination screen Y

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

        amphisbaenaAnkh.getWriteByteOperations().add(new WriteByteOperation(0x0f6, ByteOp.ASSIGN_FLAG, (byte)1));
        amphisbaenaAnkh.getWriteByteOperations().add(new WriteByteOperation(0x0f6, ByteOp.ASSIGN_FLAG, (byte)2));
        amphisbaenaAnkh.getWriteByteOperations().add(new WriteByteOperation(0x0f6, ByteOp.ASSIGN_FLAG, (byte)3));
        amphisbaenaAnkh.getWriteByteOperations().add(new WriteByteOperation(0x133, ByteOp.ASSIGN_FLAG, (byte)6));

        screen.getObjects().add(amphisbaenaAnkh);
    }
}
