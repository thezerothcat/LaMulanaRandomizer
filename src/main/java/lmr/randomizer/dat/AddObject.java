package lmr.randomizer.dat;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.shop.BlockCmdSingle;
import lmr.randomizer.dat.shop.BlockStringData;
import lmr.randomizer.dat.shop.ShopBlock;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.GameObjectId;
import lmr.randomizer.update.LocationCoordinateMapper;

import java.util.*;

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
    public static void addTwinLabsPoisonTimerRemoval(ObjectContainer screen) {
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
            testByteOperation.setIndex(853 + i);
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            testByteOperation.setValue((byte)0);
            obj.getTestByteOperations().add(testByteOperation);

            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(199 + i);
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            testByteOperation.setValue((byte)2);
            obj.getTestByteOperations().add(testByteOperation);

            WriteByteOperation writeByteOperation = new WriteByteOperation();
            writeByteOperation.setIndex(853 + i);
            writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
            writeByteOperation.setValue((byte)1);
            obj.getWriteByteOperations().add(writeByteOperation);

            writeByteOperation = new WriteByteOperation();
            writeByteOperation.setIndex(852);
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
        flagTest.setIndex(0xaa4);
        flagTest.setValue((byte) 1);
        flagTest.setOp(ByteOp.FLAG_NOT_EQUAL);
        killTimer.getTestByteOperations().add(flagTest);

        if(isXelpudScreen) {
            flagTest = new TestByteOperation();
            flagTest.setIndex(0x07c);
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
     * Add a timer to automatically start hard mode.
     * @param screen the screen to add the objects to
     */
    public static void addAutomaticHardmode(Screen screen) {
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
    public static void addAutomaticTranslations(Screen screen) {
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

    public static void addAutomaticMantras(ObjectContainer screen) {
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
        Set<String> startingItems = new HashSet<>(Settings.getStartingItems());
        startingItems.addAll(DataFromFile.getCustomPlacementData().getStartingItems());

        for(String itemName : startingItems) {
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

    public static GameObject addBackupGyoninFishShop(GameObject untransformedGyoninFishShop) {
        ObjectContainer objectContainer = untransformedGyoninFishShop.getObjectContainer();
        if(objectContainer instanceof Screen) {
            GameObject backupFishShop = new GameObject(untransformedGyoninFishShop.getObjectContainer());
            for (int i = 0; i < untransformedGyoninFishShop.getArgs().size(); i++) {
                backupFishShop.getArgs().add(untransformedGyoninFishShop.getArgs().get(i));
            }
            TestByteOperation testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(407);
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            testByteOperation.setValue((byte) 3);
            backupFishShop.getTestByteOperations().add(testByteOperation);

            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(254);
            testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
            testByteOperation.setValue((byte) 3);
            backupFishShop.getTestByteOperations().add(testByteOperation);

            backupFishShop.setId((short) 0xa0);
            backupFishShop.setX(180);
            backupFishShop.setY(1520);

            untransformedGyoninFishShop.getObjectContainer().getObjects().add(backupFishShop);

            GameObject backupFishNewDoorGraphic = new GameObject(untransformedGyoninFishShop.getObjectContainer());
            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(407);
            testByteOperation.setOp(ByteOp.FLAG_EQUALS);
            testByteOperation.setValue((byte) 3);
            backupFishNewDoorGraphic.getTestByteOperations().add(testByteOperation);

            testByteOperation = new TestByteOperation();
            testByteOperation.setIndex(254);
            testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
            testByteOperation.setValue((byte) 3);
            backupFishNewDoorGraphic.getTestByteOperations().add(testByteOperation);

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

            untransformedGyoninFishShop.getObjectContainer().getObjects().add(backupFishNewDoorGraphic);
            return backupFishShop;
        }
        return null;
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

    public static void addGrailToggle(ObjectContainer objectContainer) {
        if(objectContainer == null) {
            objectContainer = dimensionalExitScreen;
        }
        GameObject grailToggle = new GameObject(objectContainer);
        grailToggle.setId((short)0xb7);
        grailToggle.setX(-1);
        grailToggle.setY(-1);
        grailToggle.getArgs().add((short)1);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(0x382);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        grailToggle.getTestByteOperations().add(testByteOperation);

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

    public static void addExplosion(ObjectContainer objectContainer, int xPos, int yPos, int newWorldFlag) {
        GameObject explosion = new GameObject(objectContainer);
        explosion.setId((short)0xb4);
        explosion.setX(xPos - 80);
        explosion.setY(yPos - 80);
        explosion.getArgs().add((short)200);
        explosion.getArgs().add((short)1);
        explosion.getArgs().add((short)6);
        explosion.getArgs().add((short)6);
        explosion.getArgs().add((short)1);
        explosion.getArgs().add((short)60);
        explosion.getArgs().add((short)85);

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
     * For not having to damage boost up Gate of Illusion to Cog of the Soul
     * @param screen the screen to add the objects to
     */
    public static void addFeatherlessCogAccessPot(ObjectContainer screen) {
        GameObject featherlessCogAccessPot = new GameObject(screen);
        featherlessCogAccessPot.setId((short) 0x0);
        featherlessCogAccessPot.setX(580);
        featherlessCogAccessPot.setY(260);

        featherlessCogAccessPot.getArgs().add((short)0);
        featherlessCogAccessPot.getArgs().add((short)0);
        featherlessCogAccessPot.getArgs().add((short)-1);
        featherlessCogAccessPot.getArgs().add((short)1);
        featherlessCogAccessPot.getArgs().add((short)10);
        featherlessCogAccessPot.getArgs().add((short)105);
        featherlessCogAccessPot.getArgs().add((short)35);
        featherlessCogAccessPot.getArgs().add((short)17);
        featherlessCogAccessPot.getArgs().add((short)0);

        screen.getObjects().add(featherlessCogAccessPot);
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
        testByteOperation.setIndex(847);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        obj.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(847);
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
            AddObject.addAutomaticMantras(objectContainer);
        }
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
        testByteOperation.setIndex(Settings.isRandomize2() ? 0x075 : 0x0ad3);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)0);
        grailTablet.getTestByteOperations().add(testByteOperation);

        WriteByteOperation writeByteOperation = new WriteByteOperation();
        writeByteOperation.setIndex(Settings.isRandomize2() ? 0x075 : 0x0ad3);
        writeByteOperation.setOp(ByteOp.ASSIGN_FLAG);
        writeByteOperation.setValue(1);
        grailTablet.getWriteByteOperations().add(writeByteOperation);

        screen.getObjects().add(grailTablet);

        if(Settings.isAutomaticGrailPoints()) {
            addGrailDetector(grailTablet, Settings.isRandomize2() ? 0x075 : 0x0ad3);
        }
    }

    public static void addSpecialGrailTablet(Screen screen) {
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
    }

    public static void addSnouter(Screen screen, int x, int y, boolean faceRight) {
        GameObject snouter = new GameObject(screen);
        snouter.setId((short) 0x05);
        snouter.setX(x);
        snouter.setY(y);

        snouter.getArgs().add((short)(faceRight ? 0 : 1));
        snouter.getArgs().add((short)1); // drop type
        snouter.getArgs().add((short)18); // bounce rate
        snouter.getArgs().add((short)200); // hp
        snouter.getArgs().add((short)1); // damage
        snouter.getArgs().add((short)3); // soul

        screen.getObjects().add(snouter);
    }

    public static void addWarp(Screen screen, int warpX, int warpY, int destZone, int destRoom, int destScreen, int destX, int destY) {
        GameObject warp = new GameObject(screen);
        warp.setId((short) 0x97);
        warp.setX(warpX);
        warp.setY(warpY);

        warp.getArgs().add((short)destZone);
        warp.getArgs().add((short)destRoom);
        warp.getArgs().add((short)destScreen);
        warp.getArgs().add((short)destX);
        warp.getArgs().add((short)destY);
        warp.getArgs().add((short)destY);
        warp.getArgs().add((short)4);
        warp.getArgs().add((short)4);

        screen.getObjects().add(warp);
    }

    public static void addSpaulderGive(Screen screen, int x, int y) {
        GameObject itemGive = new GameObject(screen);
        itemGive.setId((short) 0xb5);
        itemGive.setX((x / 640) * 640);
        itemGive.setY((y / 480) * 480);

        itemGive.getArgs().add((short)62);
        itemGive.getArgs().add((short)32);
        itemGive.getArgs().add((short)24);
        itemGive.getArgs().add((short)39);

        TestByteOperation itemGiveTest = new TestByteOperation();
        itemGiveTest.setIndex(0xad0);
        itemGiveTest.setValue((byte) 0);
        itemGiveTest.setOp(ByteOp.FLAG_EQUALS);
        itemGive.getTestByteOperations().add(itemGiveTest);

        itemGiveTest = new TestByteOperation();
        itemGiveTest.setIndex(0xad1);
        itemGiveTest.setValue((byte) 0);
        itemGiveTest.setOp(ByteOp.FLAG_GT);
        itemGive.getTestByteOperations().add(itemGiveTest);

        WriteByteOperation itemGiveUpdate = new WriteByteOperation();
        itemGiveUpdate.setIndex(0xad0);
        itemGiveUpdate.setValue((byte) 2);
        itemGiveUpdate.setOp(ByteOp.ASSIGN_FLAG);
        itemGive.getWriteByteOperations().add(itemGiveUpdate);

        screen.getObjects().add(itemGive);
    }

    public static void addSpaulderGive2(Screen screen, int x, int y, int flag) {
        GameObject itemGive = new GameObject(screen);
        itemGive.setId((short) 0xb5);
        itemGive.setX((x / 640) * 640);
        itemGive.setY((y / 480) * 480);

        itemGive.getArgs().add((short)62);
        itemGive.getArgs().add((short)32);
        itemGive.getArgs().add((short)24);
        itemGive.getArgs().add((short)39);

        TestByteOperation itemGiveTest = new TestByteOperation();
        itemGiveTest.setIndex(flag);
        itemGiveTest.setValue((byte) 1);
        itemGiveTest.setOp(ByteOp.FLAG_EQUALS);
        itemGive.getTestByteOperations().add(itemGiveTest);

        WriteByteOperation itemGiveUpdate = new WriteByteOperation();
        itemGiveUpdate.setIndex(flag);
        itemGiveUpdate.setValue((byte) 2);
        itemGiveUpdate.setOp(ByteOp.ASSIGN_FLAG);
        itemGive.getWriteByteOperations().add(itemGiveUpdate);

        screen.getObjects().add(itemGive);
    }

    public static void addSpaulderGive3(Screen screen, int x, int y, int flag) {
        GameObject itemGive = new GameObject(screen);
        itemGive.setId((short) 0xb5);
        itemGive.setX(x);
        itemGive.setY(y);

        itemGive.getArgs().add((short)62);
        itemGive.getArgs().add((short)2);
        itemGive.getArgs().add((short)3);
        itemGive.getArgs().add((short)39);

        TestByteOperation itemGiveTest = new TestByteOperation();
        itemGiveTest.setIndex(flag);
        itemGiveTest.setValue((byte) 0);
        itemGiveTest.setOp(ByteOp.FLAG_EQUALS);
        itemGive.getTestByteOperations().add(itemGiveTest);

        WriteByteOperation itemGiveUpdate = new WriteByteOperation();
        itemGiveUpdate.setIndex(flag);
        itemGiveUpdate.setValue((byte) 2);
        itemGiveUpdate.setOp(ByteOp.ASSIGN_FLAG);
        itemGive.getWriteByteOperations().add(itemGiveUpdate);

        screen.getObjects().add(itemGive);
    }

    public static void addDisguisedSpaulder(Screen screen, int x, int y, int inventoryArg, int fakeFlag) {
        GameObject fakeItem = new GameObject(screen);
        fakeItem.setId((short) 0x2f);
        fakeItem.setX(x);
        fakeItem.setY(y);

        fakeItem.getArgs().add((short)0);
        fakeItem.getArgs().add((short)inventoryArg);
        fakeItem.getArgs().add((short)0);

        TestByteOperation fakeItemTest = new TestByteOperation();
        fakeItemTest.setIndex(fakeFlag);
        fakeItemTest.setValue((byte) 0);
        fakeItemTest.setOp(ByteOp.FLAG_EQUALS);
        fakeItem.getTestByteOperations().add(fakeItemTest);

        WriteByteOperation fakeItemUpdate = new WriteByteOperation();
        fakeItemUpdate.setIndex(fakeFlag);
        fakeItemUpdate.setValue((byte) 1);
        fakeItemUpdate.setOp(ByteOp.ASSIGN_FLAG);
        fakeItem.getWriteByteOperations().add(fakeItemUpdate);

        screen.getObjects().add(fakeItem);

        GameObject itemGive = new GameObject(screen);
        itemGive.setId((short) 0xb5);
        itemGive.setX(0);
        itemGive.setY(0);

        itemGive.getArgs().add((short)62);
        itemGive.getArgs().add((short)32);
        itemGive.getArgs().add((short)24);
        itemGive.getArgs().add((short)39);

        TestByteOperation itemGiveTest = new TestByteOperation();
        itemGiveTest.setIndex(fakeFlag);
        itemGiveTest.setValue((byte) 1);
        itemGiveTest.setOp(ByteOp.FLAG_EQUALS);
        itemGive.getTestByteOperations().add(itemGiveTest);

        WriteByteOperation itemGiveUpdate = new WriteByteOperation();
        itemGiveUpdate.setIndex(fakeFlag);
        itemGiveUpdate.setValue((byte) 2);
        itemGiveUpdate.setOp(ByteOp.ASSIGN_FLAG);
        itemGive.getWriteByteOperations().add(itemGiveUpdate);

        screen.getObjects().add(itemGive);
    }

    public static void addLaserWall(Screen screen, int x, int y, int flag) {
        GameObject laserWall = new GameObject(screen);
        laserWall.setId((short) 0xab);
        laserWall.setX(x);
        laserWall.setY(y);

        laserWall.getArgs().add((short)1);
        laserWall.getArgs().add((short)1); // drop type

        TestByteOperation laserWallTest = new TestByteOperation();
        laserWallTest.setIndex(flag);
        laserWallTest.setValue((byte) 0);
        laserWallTest.setOp(ByteOp.FLAG_GT);
        laserWall.getTestByteOperations().add(laserWallTest);

        screen.getObjects().add(laserWall);
    }

    public static void addObviousSpaulder(Screen screen, int x, int y, int fakeFlag) {
        GameObject fakeItem = new GameObject(screen);
        fakeItem.setId((short) 0x2f);
        fakeItem.setX(x);
        fakeItem.setY(y);

        fakeItem.getArgs().add((short)0);
        fakeItem.getArgs().add((short)62);
        fakeItem.getArgs().add((short)1);

        TestByteOperation fakeItemTest = new TestByteOperation();
        fakeItemTest.setIndex(fakeFlag);
        fakeItemTest.setValue((byte) 0);
        fakeItemTest.setOp(ByteOp.FLAG_EQUALS);
        fakeItem.getTestByteOperations().add(fakeItemTest);

        WriteByteOperation fakeItemUpdate = new WriteByteOperation();
        fakeItemUpdate.setIndex(fakeFlag);
        fakeItemUpdate.setValue((byte) 2);
        fakeItemUpdate.setOp(ByteOp.ASSIGN_FLAG);
        fakeItem.getWriteByteOperations().add(fakeItemUpdate);

        screen.getObjects().add(fakeItem);
    }

    public static void addItemGive(GameObject referenceObj, int inventoryArg, int randomize5Flag, int worldFlag) {
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
        itemGiveTest.setIndex(randomize5Flag);
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
    public static GameObject addSpecialTransitionGate(Screen screen, int zoneIndex, int screenIndex) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0xc4);

        obj.getArgs().add((short) zoneIndex);
        obj.getArgs().add((short) 8);
        obj.getArgs().add((short) screenIndex);

        if(zoneIndex == 0) {
            if(screenIndex == 0) {
                obj.setX(140);
                obj.setY(460);

                obj.getArgs().add((short)300);
                obj.getArgs().add((short)392);
                obj.getArgs().add((short)0);
            }
            else if(screenIndex == 1) {
                obj.setX(140);
                obj.setY(460);

                obj.getArgs().add((short)300);
                obj.getArgs().add((short)20);
                obj.getArgs().add((short)1);

                GameObject floor = new GameObject(screen);
                floor.setId((short)0x93);
                floor.setX(obj.getX());
                floor.setY(obj.getY());
                floor.getArgs().add((short)-1); // Layer
                floor.getArgs().add((short)7); // 01.effect.png for anything not 0-6?
                floor.getArgs().add((short)0); // Imagex
                floor.getArgs().add((short)700); // Imagey
                floor.getArgs().add((short)40); // dx
                floor.getArgs().add((short)40); // dy
                floor.getArgs().add((short)1); // 0: act as if animation already played; 1: allow animation; 2: ..?
                floor.getArgs().add((short)1); // Animation frames
                floor.getArgs().add((short)1); // Pause frames
                floor.getArgs().add((short)1); // Repeat count (<1 is forever)
                floor.getArgs().add((short)128); // Hittile to fill with
                floor.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
                floor.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
                floor.getArgs().add((short)0); // Cycle colors t/f
                floor.getArgs().add((short)0); // Alpha/frame
                floor.getArgs().add((short)255); // Max alpha
                floor.getArgs().add((short)0); // R/frame
                floor.getArgs().add((short)0); // Max R
                floor.getArgs().add((short)0); // G/frame
                floor.getArgs().add((short)0); // Max G
                floor.getArgs().add((short)0); // B/frame
                floor.getArgs().add((short)0); // Max B
                floor.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
                floor.getArgs().add((short)1); // not0?

                screen.getObjects().add(floor);
            }
        }
        else if(zoneIndex == 5){
            if(screenIndex == 0) {
                obj.setX(300);
                obj.setY(460);

                obj.getArgs().add((short)140);
                obj.getArgs().add((short)392);
                obj.getArgs().add((short)0);
            }
            else if(screenIndex == 1) {
                obj.setX(300);
                obj.setY(460);

                obj.getArgs().add((short)140);
                obj.getArgs().add((short)20);
                obj.getArgs().add((short)1);

                GameObject floor = new GameObject(screen);
                floor.setId((short)0x93);
                floor.setX(obj.getX());
                floor.setY(obj.getY());
                floor.getArgs().add((short)-1); // Layer
                floor.getArgs().add((short)7); // 01.effect.png for anything not 0-6?
                floor.getArgs().add((short)0); // Imagex
                floor.getArgs().add((short)700); // Imagey
                floor.getArgs().add((short)40); // dx
                floor.getArgs().add((short)40); // dy
                floor.getArgs().add((short)1); // 0: act as if animation already played; 1: allow animation; 2: ..?
                floor.getArgs().add((short)1); // Animation frames
                floor.getArgs().add((short)1); // Pause frames
                floor.getArgs().add((short)1); // Repeat count (<1 is forever)
                floor.getArgs().add((short)128); // Hittile to fill with
                floor.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
                floor.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
                floor.getArgs().add((short)0); // Cycle colors t/f
                floor.getArgs().add((short)0); // Alpha/frame
                floor.getArgs().add((short)255); // Max alpha
                floor.getArgs().add((short)0); // R/frame
                floor.getArgs().add((short)0); // Max R
                floor.getArgs().add((short)0); // G/frame
                floor.getArgs().add((short)0); // Max G
                floor.getArgs().add((short)0); // B/frame
                floor.getArgs().add((short)0); // Max B
                floor.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
                floor.getArgs().add((short)1); // not0?

                screen.getObjects().add(floor);
            }
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

    public static ShopBlock addShopBlock(List<Block> blocks, Random random) {
        ShopBlock shopBlock = new ShopBlock(blocks.size());
        List<Integer> shopBlocks = new ArrayList<>(DataFromFile.getMapOfShopNameToShopBlock().values());

        BlockListData shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)105); // Weights
        shopBlockData.getData().add((short)105);
        shopBlockData.getData().add((short)105);
        shopBlockData.getData().add((short)0x000a);
        shopBlock.setInventoryItemArgsList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)10); // Weights
        shopBlockData.getData().add((short)10);
        shopBlockData.getData().add((short)10);
        shopBlockData.getData().add((short)0x000a);
        shopBlock.setInventoryPriceList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)5); // Weights
        shopBlockData.getData().add((short)5);
        shopBlockData.getData().add((short)5);
        shopBlockData.getData().add((short)0x000a);
        shopBlock.setInventoryCountList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)0); // Weights
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0x000a);
        shopBlock.setFlagList(shopBlockData);

        shopBlockData = new BlockListData((short)0x004e, (short)3);
        shopBlockData.getData().add((short)0); // Weights
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0);
        shopBlockData.getData().add((short)0x000a);
        shopBlock.setExitFlagList(shopBlockData);

        shopBlock.setBackground(new BlockCmdSingle(((ShopBlock)blocks.get(shopBlocks.get(random.nextInt(shopBlocks.size())))).getBackground()));
        shopBlock.setSprite(new BlockCmdSingle(((ShopBlock)blocks.get(shopBlocks.get(random.nextInt(shopBlocks.size())))).getSprite()));
        shopBlock.setMusic(new BlockCmdSingle(((ShopBlock)blocks.get(shopBlocks.get(random.nextInt(shopBlocks.size())))).getMusic()));

        BlockStringData existingBlockData = ((ShopBlock)blocks.get(shopBlocks.get(random.nextInt(shopBlocks.size())))).getBunemonText();
        BlockStringData blockStringData = new BlockStringData(existingBlockData);
        shopBlock.setBunemonText(blockStringData);

        existingBlockData = ((ShopBlock)blocks.get(shopBlocks.get(random.nextInt(shopBlocks.size())))).getBunemonLocation();
        blockStringData = new BlockStringData(existingBlockData);
        shopBlock.setBunemonLocation(blockStringData);

        ShopBlock foolShop = (ShopBlock)blocks.get(DataFromFile.getMapOfShopNameToShopBlock().get("Shop 2 (Surface)"));
        for(int i = 0; i < 18; i++) {
            if(i == 15 || i == 16 || i == 17) {
                existingBlockData = foolShop.getString(i);
                shopBlock.setString(new BlockStringData(existingBlockData), i);
            }
            else {
                existingBlockData = ((ShopBlock)blocks.get(shopBlocks.get(random.nextInt(shopBlocks.size())))).getString(i);
                shopBlock.setString(new BlockStringData(existingBlockData), i);
            }
            shopBlock.getString(i).setItemNameStartIndex(null);
            shopBlock.getString(i).setItemNameEndIndex(null);

            // Remove poses so the game won't crash.
            List<Short> cleanData = new ArrayList<>();
            boolean useNextData = true;
            blockStringData = shopBlock.getString(i);
            for(int j = 0; j < blockStringData.getData().size(); j++) {
                if(useNextData) {
                    if(blockStringData.getData().get(j) == 0x0046) {
                        useNextData = false;
                    }
                    else {
                        cleanData.add(blockStringData.getData().get(j));
                    }
                }
                else {
                    useNextData = true;
                }
            }
            shopBlock.getString(i).getData().clear();
            shopBlock.getString(i).getData().addAll(cleanData);
        }
        blocks.add(shopBlock);
        return shopBlock;
    }
}
