package lmr.randomizer.dat;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.Settings;
import lmr.randomizer.node.CustomPlacement;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.GameObjectId;

import java.util.HashSet;
import java.util.Set;

public final class AddObject {
    private AddObject() { }

    private static ObjectContainer xelpudScreen;
    private static ObjectContainer mulbrukScreen;
    private static ObjectContainer littleBrotherShopScreen;

    public static void setXelpudScreen(ObjectContainer xelpudScreen) {
        AddObject.xelpudScreen = xelpudScreen;
    }

    public static void setMulbrukScreen(ObjectContainer mulbrukScreen) {
        AddObject.mulbrukScreen = mulbrukScreen;
    }

    public static void setLittleBrotherShopScreen(ObjectContainer littleBrotherShopScreen) {
        AddObject.littleBrotherShopScreen = littleBrotherShopScreen;
    }

    public static void clearObjects() {
        xelpudScreen = null;
        mulbrukScreen = null;
        littleBrotherShopScreen = null;
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
     * The backup door from Endless Corridor to untransformed Shrine of the Mother allows entry to untrue Shrine
     * during the escape, but it's a time-waster since the gate at the bottom doesn't work during the escape.
     * We'll fix by adding another exit that
     * @param screen the screen to add the object to
     */
    public static void addUntrueShrineExit(Screen screen) {
        GameObject obj = new GameObject(screen);
        obj.setId((short)0xc4);
        obj.getArgs().add((short)6);
        obj.getArgs().add((short)7);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)300);
        obj.getArgs().add((short)20);
        obj.getArgs().add((short)1);
        obj.getArgs().add((short)1);
        obj.setX(300);
        obj.setY(940);

        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(898);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte)1);
        obj.getTestByteOperations().add(testByteOperation);

        screen.getObjects().add(obj);
    }

    /**
     * Add kill timers for randomized main weapons.
     * @param screen the screen to add the objects to
     * @param isXelpudScreen true if this is Xelpud's screen, where the kill timer should not activate until after you talk to him
     */
    public static void addRandomWeaponKillTimer(Screen screen, boolean isXelpudScreen) {
        GameObject randomWeaponKillTimer = new GameObject(screen);
        randomWeaponKillTimer.setId((short) 0x0b);
        randomWeaponKillTimer.getArgs().add((short) 0);
        randomWeaponKillTimer.getArgs().add((short) 0);
        randomWeaponKillTimer.setX(-1);
        randomWeaponKillTimer.setY(-1);

        TestByteOperation flagTest = new TestByteOperation();
        GameObjectId randomWeaponInfo = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(Settings.getCurrentStartingWeapon());
        flagTest.setIndex(randomWeaponInfo.getWorldFlag());
        flagTest.setValue((byte) 2);
        flagTest.setOp(ByteOp.FLAG_NOT_EQUAL);
        randomWeaponKillTimer.getTestByteOperations().add(flagTest);

        if(isXelpudScreen) {
            flagTest = new TestByteOperation();
            flagTest.setIndex(0x07c);
            flagTest.setValue((byte) 1);
            flagTest.setOp(ByteOp.FLAG_EQUALS);
            randomWeaponKillTimer.getTestByteOperations().add(flagTest);
        }

        WriteByteOperation flagUpdate = new WriteByteOperation();
        flagUpdate.setIndex(0x3e9);
        flagUpdate.setValue((byte) 1);
        flagUpdate.setOp(ByteOp.ASSIGN_FLAG);
        randomWeaponKillTimer.getWriteByteOperations().add(flagUpdate);

        screen.getObjects().add(0, randomWeaponKillTimer);
    }

    /**
     * Add an alternative gate from Endless Corridor to untransformed Shrine of the Mother room containing the Death Seal chest.
     * @param screen the screen to add the objects to
     */
    public static void addEndlessCorridorNoFeatherUntrueShrineGate(Screen screen) {
        GameObject backupShrineDoor = new GameObject(screen);
        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoor.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(182);
        testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
        testByteOperation.setValue((byte) 2);
        backupShrineDoor.getTestByteOperations().add(testByteOperation);

        backupShrineDoor.setId((short) 0xc4);
        backupShrineDoor.setX(2220);
        backupShrineDoor.setY(0);

        backupShrineDoor.getArgs().add((short)9);
        backupShrineDoor.getArgs().add((short)9);
        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)300);
        backupShrineDoor.getArgs().add((short)392);
        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)0);

        screen.getObjects().add(backupShrineDoor);
    }

    /**
     * Add a backup door from Chamber of Extinction to untransformed Shrine of the Mother.
     * @param screen the screen to add the objects to
     */
    public static void addExtinctionUntrueShrineBackupDoor(Screen screen) {
        GameObject backupShrineDoor = new GameObject(screen);
        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoor.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(500);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte) 1);
        backupShrineDoor.getTestByteOperations().add(testByteOperation);

        backupShrineDoor.setId((short) 0x98);
        backupShrineDoor.setX(1040);
        backupShrineDoor.setY(80);

        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)9);
        backupShrineDoor.getArgs().add((short)8);
        backupShrineDoor.getArgs().add((short)1);
        backupShrineDoor.getArgs().add((short)300);
        backupShrineDoor.getArgs().add((short)320);

        screen.getObjects().add(backupShrineDoor);

        GameObject backupShrineDoorGraphic = new GameObject(screen);
        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoorGraphic.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(500);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte) 1);
        backupShrineDoorGraphic.getTestByteOperations().add(testByteOperation);

        backupShrineDoorGraphic.getArgs().add((short)-1);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)920);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)1);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)255);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);


        backupShrineDoorGraphic.setId((short) 0x93);
        backupShrineDoorGraphic.setX(1020);
        backupShrineDoorGraphic.setY(40);

        screen.getObjects().add(backupShrineDoorGraphic);
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
        for(CustomPlacement customPlacement : DataFromFile.getCustomItemPlacements()) {
            if(customPlacement.isStartingItem()) {
                startingItems.add(customPlacement.getContents());
            }
        }

        for(String itemName : startingItems) {
            GameObjectId gameObjectId = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(itemName);

            GameObject itemGive = new GameObject(screen);
            itemGive.setId((short) 0xb5);
            itemGive.getArgs().add(gameObjectId.getInventoryArg());
            itemGive.getArgs().add((short)2);
            itemGive.getArgs().add((short)3);
            itemGive.getArgs().add((short)39);
            itemGive.setX(940);
            itemGive.setY(160);

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

//    /**
//     * Unused alternate version relying on graphics file modification.
//     * @param backsideDoor the object we're decorating with graphics
//     * @param bossNumber 1=Amphisbaena, 7=Baphomet, etc.
//     */
//    public static void addBossGraphic(GameObject backsideDoor, int bossNumber) {
//        GameObject doorNumberGraphic = new GameObject(backsideDoor.getObjectContainer());
//        doorNumberGraphic.setId((short) 0x93);
//        doorNumberGraphic.setX(backsideDoor.getX());
//        doorNumberGraphic.setY(backsideDoor.getY() - 40);
//        doorNumberGraphic.getArgs().add((short)-1); // Layer
//        doorNumberGraphic.getArgs().add((short)-1); // 01.effect.png for anything not 0-6?
//        doorNumberGraphic.getArgs().add((short)(30 * (bossNumber - 1))); // Imagex
//        doorNumberGraphic.getArgs().add((short)(478)); // Imagey
//        doorNumberGraphic.getArgs().add((short)30); // dx
//        doorNumberGraphic.getArgs().add((short)34); // dy
//        doorNumberGraphic.getArgs().add((short)0); // 0: act as if animation already played; 1: allow animation; 2: ..?
//        doorNumberGraphic.getArgs().add((short)0); // Animation frames
//        doorNumberGraphic.getArgs().add((short)1); // Pause frames
//        doorNumberGraphic.getArgs().add((short)0); // Repeat count (<1 is forever)
//        doorNumberGraphic.getArgs().add((short)0); // Hittile to fill with
//        doorNumberGraphic.getArgs().add((short)0); // Entry effect (0=static, 1=fade, 2=animate; show LAST frame)
//        doorNumberGraphic.getArgs().add((short)0); // Exit effect (0=disallow animation, 1=fade, 2=default, 3=large break on completion/failure, 4=default, 5=animate on failure/frame 1 on success, 6=break glass on completion/failure, default=disappear instantly)
//        doorNumberGraphic.getArgs().add((short)0); // Cycle colors t/f
//        doorNumberGraphic.getArgs().add((short)0); // Alpha/frame
//        doorNumberGraphic.getArgs().add((short)255); // Max alpha
//        doorNumberGraphic.getArgs().add((short)0); // R/frame
//        doorNumberGraphic.getArgs().add((short)0); // Max R
//        doorNumberGraphic.getArgs().add((short)0); // G/frame
//        doorNumberGraphic.getArgs().add((short)0); // Max G
//        doorNumberGraphic.getArgs().add((short)0); // B/frame
//        doorNumberGraphic.getArgs().add((short)0); // Max B
//        doorNumberGraphic.getArgs().add((short)0); // blend (0=normal, 1= add, 2=...14=)
//        doorNumberGraphic.getArgs().add((short)1); // not0?
//
//        for (TestByteOperation testByteOperation : backsideDoor.getTestByteOperations()) {
//            doorNumberGraphic.getTestByteOperations().add(new TestByteOperation(testByteOperation));
//        }
//        backsideDoor.getObjectContainer().getObjects().add(doorNumberGraphic);
//    }

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

    public static void addBackupShrineDoor(ObjectContainer objectContainer) {
        // Add actual door to old Shrine of the Mother
        GameObject backupShrineDoor = new GameObject(objectContainer);
        TestByteOperation testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoor.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(500);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte) 1);
        backupShrineDoor.getTestByteOperations().add(testByteOperation);

        backupShrineDoor.setId((short) 0x98);
        backupShrineDoor.setX(2430);
        backupShrineDoor.setY(320);

        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)9);
        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)420);
        backupShrineDoor.getArgs().add((short)152);

        objectContainer.getObjects().add(backupShrineDoor);

        // Add graphics for door to old Shrine of the Mother
        GameObject backupShrineDoorGraphic = new GameObject(objectContainer);
        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(258);
        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
        testByteOperation.setValue((byte) 9);
        backupShrineDoorGraphic.getTestByteOperations().add(testByteOperation);

        testByteOperation = new TestByteOperation();
        testByteOperation.setIndex(500);
        testByteOperation.setOp(ByteOp.FLAG_GTEQ);
        testByteOperation.setValue((byte) 1);
        backupShrineDoorGraphic.getTestByteOperations().add(testByteOperation);

        backupShrineDoorGraphic.getArgs().add((short)-1);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)540);
        backupShrineDoorGraphic.getArgs().add((short)40);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)80);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)1);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)255);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);
        backupShrineDoorGraphic.getArgs().add((short)0);

        backupShrineDoorGraphic.setId((short) 0x93);
        backupShrineDoorGraphic.setX(2410);
        backupShrineDoorGraphic.setY(280);

        objectContainer.getObjects().add(backupShrineDoorGraphic);
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
}
