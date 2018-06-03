package lmr.randomizer.dat;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.Settings;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.GameDataTracker;
import lmr.randomizer.update.GameObjectId;

public final class AddObject {
    private AddObject() { }

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

        screen.getObjects().add(obj);
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

        screen.getObjects().add(obj);
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

        screen.getObjects().add(obj);

        // Timer to spawn dais for odd number of children
        obj = new GameObject(screen);
        obj.setId((short)0x0b);
        obj.getArgs().add((short)0);
        obj.getArgs().add((short)30);

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

            screen.getObjects().add(obj);
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

    /**
     * Add instant item give to the starting location so items will be given at the start of the game.
     * @param screen the screen to add the objects to
     */
    public static void addStartingItems(Screen screen) {
        for(String itemName : Settings.getStartingItems()) {
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

    /**
     * For not having to damage boost up Gate of Illusion to Cog of the Soul
     * @param screen the screen to add the objects to
     */
    public static void addFeatherlessCogAccessPot(Screen screen) {
        GameObject backupShrineDoor = new GameObject(screen);
        backupShrineDoor.setId((short) 0x0);
        backupShrineDoor.setX(580);
        backupShrineDoor.setY(260);

        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)0);
        backupShrineDoor.getArgs().add((short)-1);
        backupShrineDoor.getArgs().add((short)1);
        backupShrineDoor.getArgs().add((short)10);
        backupShrineDoor.getArgs().add((short)105);
        backupShrineDoor.getArgs().add((short)35);
        backupShrineDoor.getArgs().add((short)17);
        backupShrineDoor.getArgs().add((short)0);

        screen.getObjects().add(backupShrineDoor);
    }

}
