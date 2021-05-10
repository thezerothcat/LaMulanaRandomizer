package lmr.randomizer;

import lmr.randomizer.dat.*;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.LocationCoordinateMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddObjectTest {
    @Test
    public void testAddSecondsTimer() {
        Screen screen = new Screen();

        int testSeconds = 2;
        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);
        WriteByteOperation writeFlag1 = new WriteByteOperation(234, ByteOp.ASSIGN_FLAG, 3);
        WriteByteOperation writeFlag2 = new WriteByteOperation(432, ByteOp.ADD_FLAG, 2);

        AddObject.addSecondsTimer(screen, testSeconds, Arrays.asList(testFlag1, testFlag2), Arrays.asList(writeFlag1, writeFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertTrue(containsUpdate(gameObject, writeFlag1));
        Assert.assertTrue(containsUpdate(gameObject, writeFlag2));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddFramesTimer() {
        Screen screen = new Screen();

        int testFrames = 2;
        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);
        WriteByteOperation writeFlag1 = new WriteByteOperation(234, ByteOp.ASSIGN_FLAG, 3);
        WriteByteOperation writeFlag2 = new WriteByteOperation(432, ByteOp.ADD_FLAG, 2);

        AddObject.addFramesTimer(screen, testFrames, Arrays.asList(testFlag1, testFlag2), Arrays.asList(writeFlag1, writeFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 2);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertTrue(containsUpdate(gameObject, writeFlag1));
        Assert.assertTrue(containsUpdate(gameObject, writeFlag2));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddFloatingItem() {
        Screen screen = new Screen();

        int x = 540;
        int y = 300;
        int itemArg = 86;
        boolean realItem = true;
        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);
        WriteByteOperation writeFlag1 = new WriteByteOperation(234, ByteOp.ASSIGN_FLAG, 3);
        WriteByteOperation writeFlag2 = new WriteByteOperation(432, ByteOp.ADD_FLAG, 2);

        AddObject.addFloatingItem(screen, x, y, itemArg, realItem, Arrays.asList(testFlag1, testFlag2),
                Arrays.asList(writeFlag1, writeFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x2f);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), itemArg);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 3);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertTrue(containsUpdate(gameObject, writeFlag1));
        Assert.assertTrue(containsUpdate(gameObject, writeFlag2));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddAutosave() {
        Screen screen = new Screen();

        int x = 540;
        int y = 300;
        int textBlock = 281;
        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);
        WriteByteOperation writeFlag1 = new WriteByteOperation(234, ByteOp.ASSIGN_FLAG, 3);

        AddObject.addAutosave(screen, x, y, textBlock, Arrays.asList(testFlag1, testFlag2), writeFlag1);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x9f);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), textBlock);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 506);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 280);
        Assert.assertEquals(gameObject.getArgs().size(), 9);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0)),
                "Test for not-escaping not found");
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 3);
        Assert.assertTrue(containsUpdate(gameObject, writeFlag1));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }


    @Test
    public void testAddDiaryChestConditionTimer() {
        Screen screen = new Screen();

        AddObject.addDiaryChestConditionTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(FlagConstants.XELPUD_TALISMAN_CONVOS, ByteOp.FLAG_GTEQ, 3)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(536, ByteOp.FLAG_GTEQ, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(537, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddPalenqueMSX2Timer() {
        Screen screen = new Screen();

        AddObject.addPalenqueMSX2Timer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(FlagConstants.BOSSES_SHRINE_TRANSFORM, ByteOp.FLAG_EQUALS, 4)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x21d, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x21d, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x317, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x328, ByteOp.ADD_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 3);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 3);
    }

    @Test
    public void testAddWeightDoorTimer() {
        Screen screen = new Screen();

        int weightFlag = 222;
        AddObject.addWeightDoorTimer(screen, weightFlag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(weightFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(weightFlag, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddTwinLabsPoisonTimerRemoval() {
        Screen screen = new Screen();

        AddObject.addTwinLabsPoisonTimerRemoval(screen, true);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x1dc, ByteOp.FLAG_LTEQ, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x1d7, ByteOp.FLAG_NOT_EQUAL, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x1d7, ByteOp.ASSIGN_FLAG, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x1dc, ByteOp.ASSIGN_FLAG, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);

        AddObject.addTwinLabsPoisonTimerRemoval(screen, false);
        gameObject = screen.getObjects().get(0); // Timers should be added to front of objects list
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x1dc, ByteOp.FLAG_LTEQ, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x1d7, ByteOp.FLAG_NOT_EQUAL, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x1d7, ByteOp.ASSIGN_FLAG, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddIsisRoomCeilingTimer() {
        Screen screen = new Screen();

        AddObject.addIsisRoomCeilingTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x17a, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x17a, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddGoddessShieldTimer() {
        Screen screen = new Screen();

        AddObject.addGoddessShieldTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x284, ByteOp.FLAG_GTEQ, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x34e, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x34e, ByteOp.ASSIGN_FLAG, 12)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x284, ByteOp.ASSIGN_FLAG, 3)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddGoddessStatueLemezaDetector() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();
        GameObject transitionGate = new GameObject(screen);
        transitionGate.setX(x);
        transitionGate.setY(y);
        screen.getObjects().add(transitionGate);

        AddObject.addGoddessStatueLemezaDetector(transitionGate);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == transitionGate, "Positioned object should be added to end of objects list");
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x14);
        Assert.assertEquals(gameObject.getX(), x - 40);
        Assert.assertEquals(gameObject.getY(), y - 20);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 3);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x278, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x278, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddEndlessCorridorLeftExitLemezaDetector() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();
        GameObject transitionGate = new GameObject(screen);
        transitionGate.setX(x);
        transitionGate.setY(y);
        screen.getObjects().add(transitionGate);

        AddObject.addEndlessCorridorLeftExitLemezaDetector(transitionGate);

        // Added success sound, make sure it's the right object type, but don't test the whole thing here
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x9b);

        // The original object
        gameObject = screen.getObjects().get(1);
        Assert.assertTrue(gameObject == transitionGate, "Lemeza detector should be added to end of objects list");

        // The added Lemeza detector
        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x14);
        Assert.assertEquals(gameObject.getX(), x - 40);
        Assert.assertEquals(gameObject.getY(), y - 20);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 3);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x1f6, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x1f6, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddMoonlightPassageTimer() {
        Screen screen = new Screen();

        AddObject.addMoonlightPassageTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(606, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(606, ByteOp.ASSIGN_FLAG, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddFlailWhipPuzzleTimer() {
        Screen screen = new Screen();

        AddObject.addFlailWhipPuzzleTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(635, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(635, ByteOp.ASSIGN_FLAG, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddAngelShieldPuzzleTimers() {
        Screen screen = new Screen();

        AddObject.addAngelShieldPuzzleTimers(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 30, "expected 30 frame delay");
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(706, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(723, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(748, ByteOp.FLAG_GTEQ, 11)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 3);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 30, "expected 30 frame delay");
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(706, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(722, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(748, ByteOp.FLAG_GTEQ, 11)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 3);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddSacredOrbCountTimers() {
        Screen screen = new Screen();

        AddObject.addSacredOrbCountTimers(screen);

        GameObject gameObject;

        for (int i = 0; i < 10; i++) {
            gameObject = screen.getObjects().get(9 - i);
            Assert.assertEquals(gameObject.getId(), 0x0b);
            Assert.assertEquals(gameObject.getX(), -1);
            Assert.assertEquals(gameObject.getY(), -1);
            Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
            Assert.assertEquals(gameObject.getArgs().size(), 2);
            Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x355 + i, ByteOp.FLAG_EQUALS, 0)));
            Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x0c7 + i, ByteOp.FLAG_EQUALS, 2)));
            Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x355 + i, ByteOp.ASSIGN_FLAG, 1)));
            Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x354, ByteOp.ADD_FLAG, 1)));
            Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
            Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
        }
    }

    @Test
    public void testAddSurfaceKillTimer() {
        Screen screen = new Screen();

        AddObject.addSurfaceKillTimer(screen, true);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0xad1, ByteOp.FLAG_NOT_EQUAL, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0xad0, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x3e9, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        AddObject.addSurfaceKillTimer(screen, false);
        gameObject = screen.getObjects().get(0); // Timers should be added to front of objects list
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0xad1, ByteOp.FLAG_NOT_EQUAL, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x3e9, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddXelpudIntroTimer() {
        Screen screen = new Screen();

        AddObject.addXelpudIntroTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0xad0, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x07c, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x07c, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddLowerUntrueShrineBackupDoor() {
        Screen screen = new Screen();

        AddObject.addLowerUntrueShrineBackupDoor(screen);

        // Functional door object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x98);
        Assert.assertEquals(gameObject.getX(), 260);
        Assert.assertEquals(gameObject.getY(), 800);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 9);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 320);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(258, ByteOp.FLAG_EQUALS, 9)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Door graphics
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 240);
        Assert.assertEquals(gameObject.getY(), 760);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(258, ByteOp.FLAG_EQUALS, 9)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddUpperUntrueShrineBackupDoor() {
        Screen screen = new Screen();

        AddObject.addUpperUntrueShrineBackupDoor(screen);

        // Functional door object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x98);
        Assert.assertEquals(gameObject.getX(), 340);
        Assert.assertEquals(gameObject.getY(), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 9);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 340);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 92);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(258, ByteOp.FLAG_EQUALS, 9)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Door graphics
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 320);
        Assert.assertEquals(gameObject.getY(), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(258, ByteOp.FLAG_EQUALS, 9)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddSealUntrueShrineBackupDoor() {
        Screen screen = new Screen();

        AddObject.addSealUntrueShrineBackupDoor(screen);

        // Functional door object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x98);
        Assert.assertEquals(gameObject.getX(), 500);
        Assert.assertEquals(gameObject.getY(), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 9);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 9);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 332);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(258, ByteOp.FLAG_EQUALS, 9)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Door graphics
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 480);
        Assert.assertEquals(gameObject.getY(), 360);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(258, ByteOp.FLAG_EQUALS, 9)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddSkandaBlock() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();
        GameObject transitionGate = new GameObject(screen);
        transitionGate.setX(x);
        transitionGate.setY(y);
        screen.getObjects().add(transitionGate);

        AddObject.addSkandaBlock(transitionGate);

        // The original object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == transitionGate, "Positioned object should be added to end of objects list");

        // The added graphics object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y - 20);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 19);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 640);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 128);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x2a6, ByteOp.FLAG_LTEQ, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddIllusionFruitBlockHorizontal() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();
        GameObject transitionGate = new GameObject(screen);
        transitionGate.setX(x);
        transitionGate.setY(y);
        screen.getObjects().add(transitionGate);

        AddObject.addIllusionFruitBlockHorizontal(transitionGate, true);
        AddObject.addIllusionFruitBlockHorizontal(transitionGate, false);

        // The original object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == transitionGate, "Positioned object should be added to end of objects list");

        // The added graphics object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 19);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 620);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 128);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x226, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 19);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 540);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 128);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x226, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddIllusionFruitBlockVertical() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();
        GameObject transitionGate = new GameObject(screen);
        transitionGate.setX(x);
        transitionGate.setY(y);
        screen.getObjects().add(transitionGate);

        AddObject.addIllusionFruitBlockVertical(transitionGate);

        // The original object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == transitionGate, "Positioned object should be added to end of objects list");

        // The added graphics object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 19);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 128);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x226, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddBossSpecificAnkhCover() {
        int x = 540;
        int y = 300;
        int ankhFlag = 123;

        Screen screen = new Screen();
        GameObject ankh = new GameObject(screen);
        ankh.setX(x);
        ankh.setY(y);
        screen.getObjects().add(ankh);

        AddObject.addBossSpecificAnkhCover(ankh, ankhFlag);

        // The original object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == ankh, "Positioned object should be added to end of objects list");

        // The added graphics object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y - 20);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 840);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 128);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(ankhFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddWallCopy() {
        int x = 940;
        int y = 40;
        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);
        WriteByteOperation writeFlag1 = new WriteByteOperation(234, ByteOp.ASSIGN_FLAG, 3);

        Screen screen = new Screen();
        GameObject wall = new GameObject(screen);
        wall.setId((short)0x12);
        wall.setX(x);
        wall.setY(y);
        wall.getArgs().add((short)0);
        wall.getArgs().add((short)1);
        wall.getArgs().add((short)1);
        wall.getArgs().add((short)4);
        wall.getArgs().add((short)11);
        wall.getArgs().add((short)0);
        wall.getArgs().add((short)2);
        wall.getArgs().add((short)2);
        wall.getArgs().add((short)-1);
        wall.getArgs().add((short)-1);
        wall.getArgs().add((short)1);
        wall.getArgs().add((short)2);
        wall.addTests(testFlag1, testFlag2);
        wall.addUpdates(writeFlag1);
        screen.getObjects().add(wall);

        AddObject.addWallCopy(wall, 0);
        AddObject.addWallCopy(wall, 1);
        AddObject.addWallCopy(wall, 2);
        AddObject.addWallCopy(wall, 3);
        AddObject.addWallCopy(wall, 4);

        // The original object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == wall, "Positioned object should be added to end of objects list");

        // The added wall object
        for(int i = 0; i <= 4; i++) {
            gameObject = screen.getObjects().get(i + 1);
            Assert.assertEquals(gameObject.getId(), 0x12);
            Assert.assertEquals(gameObject.getX(), x);
            Assert.assertEquals(gameObject.getY(), y);
            Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
            Assert.assertEquals((int)gameObject.getArgs().get(2), 1);
            Assert.assertEquals((int)gameObject.getArgs().get(3), i);
            Assert.assertEquals((int)gameObject.getArgs().get(4), 11);
            Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(6), 2);
            Assert.assertEquals((int)gameObject.getArgs().get(7), 2);
            Assert.assertEquals((int)gameObject.getArgs().get(8), -1);
            Assert.assertEquals((int)gameObject.getArgs().get(9), -1);
            Assert.assertEquals((int)gameObject.getArgs().get(10), 1);
            Assert.assertEquals((int)gameObject.getArgs().get(11), 2);
            Assert.assertEquals(gameObject.getArgs().size(), 12);
            Assert.assertTrue(containsTest(gameObject, testFlag1));
            Assert.assertTrue(containsTest(gameObject, testFlag2));
            Assert.assertTrue(containsUpdate(gameObject, writeFlag1));
            Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
            Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
        }
    }

    @Test
    public void testAddAutomaticHardmodeTimer() {
        Screen screen = new Screen();

        AddObject.addAutomaticHardmodeTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(362, ByteOp.FLAG_NOT_EQUAL, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(362, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddAutomaticTranslationsTimer() {
        Screen screen = new Screen();

        AddObject.addAutomaticTranslationsTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(746, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(741, ByteOp.ASSIGN_FLAG, 3)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(746, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddAutomaticMantrasTimer() {
        Screen screen = new Screen();

        AddObject.addAutomaticMantrasTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(292, ByteOp.FLAG_NOT_EQUAL, 4)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(292, ByteOp.ASSIGN_FLAG, 4)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddStartingItems() {
        // todo: write tests
        AddObject.addStartingItems(new Screen());
    }

    @Test
    public void testAddTransformedMrFishmanShopDoorGraphic() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();
        GameObject originalFishmanShop = new GameObject(screen);
        originalFishmanShop.setX(x);
        originalFishmanShop.setY(y);
        screen.getObjects().add(originalFishmanShop);

        AddObject.addTransformedMrFishmanShopDoorGraphic(originalFishmanShop);

        // The original object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == originalFishmanShop, "Positioned object should be added to end of objects list");

        // The added graphics object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 180);
        Assert.assertEquals(gameObject.getY(), 1520);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 260);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x197, ByteOp.FLAG_EQUALS, 3)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x0fe, ByteOp.FLAG_NOT_EQUAL, 3)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddBossNumberGraphic() {
        int x = 540;
        int y = 300;
        int mirrorCoverFlag = 123;

        Screen screen = new Screen();
        GameObject backsideDoor = new GameObject(screen);
        backsideDoor.setX(x);
        backsideDoor.setY(y);
        screen.getObjects().add(backsideDoor);

        for(int i = 1; i <= 10; i++) {
            AddObject.addBossNumberGraphic(backsideDoor, i, mirrorCoverFlag);
        }

        // The original object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == backsideDoor, "Positioned object should be added to end of objects list");

        // The added objects
        for(int i = 0; i < 10; i++) {
            gameObject = screen.getObjects().get((i * 3) + 1);
            Assert.assertEquals(gameObject.getId(), 0x93);
            Assert.assertEquals(gameObject.getX(), x);
            Assert.assertEquals(gameObject.getY(), y - 40);
            Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
            Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
            Assert.assertEquals((int)gameObject.getArgs().get(2), (53 +  16 * i));
            Assert.assertEquals((int)gameObject.getArgs().get(3), 209);
            Assert.assertEquals((int)gameObject.getArgs().get(4), 29);
            Assert.assertEquals((int)gameObject.getArgs().get(5), 31);
            Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
            Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
            Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(23), 0);
            Assert.assertEquals(gameObject.getArgs().size(), 24);
            Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_GT, 0)));
            Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
            Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

            gameObject = screen.getObjects().get((i * 3) + 2);
            Assert.assertEquals(gameObject.getId(), 0x93);
            Assert.assertEquals(gameObject.getX(), x);
            Assert.assertEquals(gameObject.getY(), y - 40);
            Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(2), 940);
            Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(4), 11);
            Assert.assertEquals((int)gameObject.getArgs().get(5), 33);
            Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
            Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
            Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(23), 0);
            Assert.assertEquals(gameObject.getArgs().size(), 24);
            Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_GT, 0)));
            Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
            Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

            gameObject = screen.getObjects().get((i * 3) + 3);
            Assert.assertEquals(gameObject.getId(), 0x93);
            Assert.assertEquals(gameObject.getX(), x);
            Assert.assertEquals(gameObject.getY(), y - 40);
            Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(2), 940);
            Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(4), 29);
            Assert.assertEquals((int)gameObject.getArgs().get(5), 11);
            Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
            Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
            Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(23), 0);
            Assert.assertEquals(gameObject.getArgs().size(), 24);
            Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_GT, 0)));
            Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
            Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
        }
    }

    @Test
    public void testAddBossNumberGraphicV2() {
        int x = 540;
        int y = 300;
        int mirrorCoverFlag = 123;

        Screen screen = new Screen();
        GameObject backsideDoor = new GameObject(screen);
        backsideDoor.setX(x);
        backsideDoor.setY(y);
        screen.getObjects().add(backsideDoor);

        AddObject.addBossNumberGraphicV2(backsideDoor, 1, null);
        for(int i = 2; i <= 10; i++) {
            AddObject.addBossNumberGraphicV2(backsideDoor, i, mirrorCoverFlag);
        }

        // The original object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == backsideDoor, "Positioned object should be added to end of objects list");

        // The added objects
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 592);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 50);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        for(int i = 1; i < 8; i++) {
            gameObject = screen.getObjects().get(i + 1);
            Assert.assertEquals(gameObject.getId(), 0x93);
            Assert.assertEquals(gameObject.getX(), x - 20);
            Assert.assertEquals(gameObject.getY(), y - 40);
            Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
            Assert.assertEquals((int)gameObject.getArgs().get(2), 50 * i);
            Assert.assertEquals((int)gameObject.getArgs().get(3), 592);
            Assert.assertEquals((int)gameObject.getArgs().get(4), 50);
            Assert.assertEquals((int)gameObject.getArgs().get(5), 36);
            Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
            Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
            Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
            Assert.assertEquals((int)gameObject.getArgs().get(23), 0);
            Assert.assertEquals(gameObject.getArgs().size(), 24);
            Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_GT, 0)));
            Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
            Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
        }

        gameObject = screen.getObjects().get(10);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 50 * 9);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 592);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 50);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_GT, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddNumberlessBacksideDoorGraphic() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();
        GameObject backsideDoor = new GameObject(screen);
        backsideDoor.setX(x);
        backsideDoor.setY(y);
        screen.getObjects().add(backsideDoor);

        screen.setZoneIndex(0);
        AddObject.addNumberlessBacksideDoorGraphic(backsideDoor);
        screen.setZoneIndex(6);
        AddObject.addNumberlessBacksideDoorGraphic(backsideDoor);
        screen.setZoneIndex(10);
        AddObject.addNumberlessBacksideDoorGraphic(backsideDoor);
        screen.setZoneIndex(13);
        AddObject.addNumberlessBacksideDoorGraphic(backsideDoor);
        screen.setZoneIndex(19);
        AddObject.addNumberlessBacksideDoorGraphic(backsideDoor);

        // The original object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == backsideDoor, "Positioned object should be added to end of objects list");

        // The added graphics object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(3);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 160);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(4);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 240);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        Assert.assertEquals(screen.getObjects().size(), 5);
    }

    @Test
    public void testAddAnimatedDoorCover() {
        int x = 540;
        int y = 300;
        int gateFlag = 123;

        Screen screen = new Screen();
        GameObject backsideDoor = new GameObject(screen);
        backsideDoor.setX(x);
        backsideDoor.setY(y);
        screen.getObjects().add(backsideDoor);

        AddObject.addAnimatedDoorCover(backsideDoor, gateFlag, true);
        AddObject.addAnimatedDoorCover(backsideDoor, gateFlag, false);

        // The original object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == backsideDoor, "Positioned object should be added to end of objects list");

        // The added objects
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 360);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 7);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x0ae, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 360);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 7);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddBacksideDoorKeyFairyPoint() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();
        GameObject backsideDoor = new GameObject(screen);
        backsideDoor.setX(x);
        backsideDoor.setY(y);
        screen.getObjects().add(backsideDoor);

        AddObject.addBacksideDoorKeyFairyPoint(backsideDoor);

        // The original object
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == backsideDoor, "Positioned object should be added to end of objects list");

        // The added objects
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa7);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 40);
        Assert.assertEquals(gameObject.getArgs().size(), 3);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x1c9, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x1c9, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x029, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x38c, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 3);
    }

    @Test
    public void testAddAnimatedDoorTimerAndSound() {
        int bossFlag = 123;
        int gateFlag = 234;

        Screen screen = new Screen();

        screen.setZoneIndex(0);
        screen.setRoomIndex(0);
        AddObject.addAnimatedDoorTimerAndSound(screen, bossFlag, gateFlag);

        // Timer
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(bossFlag, ByteOp.FLAG_GTEQ, 3)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x0ae, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 3);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x029, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);

        // Sound effect
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 95);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 127);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(3), -400);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 127);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 15);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 15);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x029, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(1);
        screen.setRoomIndex(0);
        bossFlag = 432;
        gateFlag = 333;
        AddObject.addAnimatedDoorTimerAndSound(screen, bossFlag, gateFlag);

        // Timer
        gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(bossFlag, ByteOp.FLAG_GTEQ, 3)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x0ae, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 3);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x029, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);

        // Sound effect
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 95);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 127);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(3), -400);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 127);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 15);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 15);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x029, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(12);
        screen.setRoomIndex(0);
        AddObject.addAnimatedDoorTimerAndSound(screen, bossFlag, gateFlag);

        // Sound effect
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);

        screen.setZoneIndex(2);
        screen.setRoomIndex(0);
        AddObject.addAnimatedDoorTimerAndSound(screen, bossFlag, gateFlag);

        // Sound effect
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 20);

        screen.setZoneIndex(3);
        screen.setRoomIndex(0);
        AddObject.addAnimatedDoorTimerAndSound(screen, bossFlag, gateFlag);

        // Sound effect
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 100);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 100);

        screen.setZoneIndex(5);
        screen.setRoomIndex(0);
        AddObject.addAnimatedDoorTimerAndSound(screen, bossFlag, gateFlag);

        // Sound effect
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 10);

        screen.setZoneIndex(5);
        screen.setRoomIndex(8);
        AddObject.addAnimatedDoorTimerAndSound(screen, bossFlag, gateFlag);

        // Sound effect
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 30);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 30);

        screen.setZoneIndex(10);
        screen.setRoomIndex(8);
        AddObject.addAnimatedDoorTimerAndSound(screen, bossFlag, gateFlag);

        // Sound effect
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 10);

        screen.setZoneIndex(11);
        screen.setRoomIndex(8);
        AddObject.addAnimatedDoorTimerAndSound(screen, bossFlag, gateFlag);

        // Sound effect
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 20);

        screen.setZoneIndex(13);
        screen.setRoomIndex(8);
        AddObject.addAnimatedDoorTimerAndSound(screen, bossFlag, gateFlag);

        // Sound effect
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 64);
    }

    @Test
    public void testAddMirrorCoverGraphic() {
        int x = 540;
        int y = 300;
        int mirrorCoverFlag = 123;

        Screen screen = new Screen();
        GameObject backsideDoor = new GameObject(screen);
        backsideDoor.setX(x);
        backsideDoor.setY(y);
        screen.getObjects().add(backsideDoor);

        screen.setZoneIndex(0);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        GameObject gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 600);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 160);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY());
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 680);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 160);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(1);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(3);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 900);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(2);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(4);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 280);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 580);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(5);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY());
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 360);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 580);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(3);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(6);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 700);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        Assert.assertEquals(screen.getObjects().size(), 7);
        screen.setZoneIndex(4);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);
        Assert.assertEquals(screen.getObjects().size(), 7);

        screen.setZoneIndex(5);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(7);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 940);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(6);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(8);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 940);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(10);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(9);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 620);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(11);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(10);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 940);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(12);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(11);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 540);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(13);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(12);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 940);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 220);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(14);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(13);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 540);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(15);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(14);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 340);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(15);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY());
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 460);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        Assert.assertEquals(screen.getObjects().size(), 16);
        screen.setZoneIndex(16);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);
        Assert.assertEquals(screen.getObjects().size(), 16);
        screen.setZoneIndex(17);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);
        Assert.assertEquals(screen.getObjects().size(), 16);
        screen.setZoneIndex(18);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);
        Assert.assertEquals(screen.getObjects().size(), 16);

        screen.setZoneIndex(19);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);

        gameObject = screen.getObjects().get(16);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), backsideDoor.getX() - 20);
        Assert.assertEquals(gameObject.getY(), backsideDoor.getY() - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 760);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        Assert.assertEquals(screen.getObjects().size(), 17);
        screen.setZoneIndex(20);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);
        Assert.assertEquals(screen.getObjects().size(), 17);
        screen.setZoneIndex(21);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);
        Assert.assertEquals(screen.getObjects().size(), 17);
        screen.setZoneIndex(22);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);
        Assert.assertEquals(screen.getObjects().size(), 17);
        screen.setZoneIndex(23);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);
        Assert.assertEquals(screen.getObjects().size(), 17);
        screen.setZoneIndex(24);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);
        Assert.assertEquals(screen.getObjects().size(), 17);
        screen.setZoneIndex(25);
        AddObject.addMirrorCoverGraphic(backsideDoor, mirrorCoverFlag);
        Assert.assertEquals(screen.getObjects().size(), 17);
    }

    @Test
    public void testAddGrailToggle() {
        Screen screen = new Screen();
        Screen dimensionalExitScreen = new Screen();

        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);

        AddObject.setDimensionalExitScreen(dimensionalExitScreen);
        AddObject.addGrailToggle(screen, true, testFlag1, testFlag2);
        AddObject.addGrailToggle(screen, false, testFlag1);
        AddObject.addGrailToggle(null, false, testFlag1);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xb7);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 1);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
        Assert.assertTrue(gameObject.getObjectContainer() == screen);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xb7);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 1);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 3);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
        Assert.assertTrue(gameObject.getObjectContainer() == screen);

        gameObject = dimensionalExitScreen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xb7);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 1);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
        Assert.assertTrue(gameObject.getObjectContainer() == dimensionalExitScreen);

        Assert.assertEquals(screen.getObjects().size(), 2);
        Assert.assertEquals(dimensionalExitScreen.getObjects().size(), 1);
    }

    @Test
    public void testAddAltSurfaceShopItemTimer() {
        Screen screen = new Screen();

        AddObject.addAltSurfaceShopItemTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(742, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(742, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddLittleBrotherShopTimer() {
        int shopItemFlag = 123;

        Screen screen = new Screen();

        AddObject.setLittleBrotherShopScreen(screen);
        AddObject.addLittleBrotherShopTimer((short)shopItemFlag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(shopItemFlag, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(shopItemFlag, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddSurfaceCoverDetector() {
        Screen screen = new Screen();

        AddObject.addSurfaceCoverDetector(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x14);
        Assert.assertEquals(gameObject.getX(), 420);
        Assert.assertEquals(gameObject.getY(), 1340);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 5);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x14c, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x14c, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddDiaryTalismanConversationTimers() {
        Screen screen = new Screen();

        AddObject.addDiaryTalismanConversationTimers(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(164, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(2796, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x07c, ByteOp.FLAG_GTEQ, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(2796, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 3);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(260, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(2797, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(2797, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddGrailWarpTimers() {
        Screen screen = new Screen();

        AddObject.addGrailWarpTimers(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x06c, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x06c, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddNoItemSoundEffect() {
        int newWorldFlag = 123;
        int screenFlag = 9;

        Screen screen = new Screen();

        AddObject.addNoItemSoundEffect(screen, newWorldFlag, screenFlag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 25);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 15);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(newWorldFlag, ByteOp.FLAG_GT, 0)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(screenFlag, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddBat() {
        int x = 540;
        int y = 300;
        int screenFlag = 9;

        Screen screen = new Screen();

        AddObject.addBat(screen, x, y, screenFlag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x02);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 3);
        Assert.assertEquals(gameObject.getArgs().size(), 5);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(screenFlag, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddExplosion() {
        int x1 = 540;
        int y1 = 300;
        int x2 = 540;
        int y2 = 300;
        int explosionTriggerFlag1 = 123;
        int explosionTriggerFlag2 = 321;
        int damage1 = 32;
        int damage2 = 50;

        Screen screen = new Screen();

        AddObject.addExplosion(screen, x1, y1, explosionTriggerFlag1, damage1, false);
        AddObject.addExplosion(screen, x2, y2, explosionTriggerFlag2, damage2, true);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xb4);
        Assert.assertEquals(gameObject.getX(), x1 - 80);
        Assert.assertEquals(gameObject.getY(), y1 - 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 200);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(5), damage1);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 85);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(explosionTriggerFlag1, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(explosionTriggerFlag1, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xb4);
        Assert.assertEquals(gameObject.getX(), x2 - 80);
        Assert.assertEquals(gameObject.getY(), y2 - 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 200);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(5), damage2);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 85);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(explosionTriggerFlag2, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(explosionTriggerFlag2, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddFoolsExplosion() {
        int x1 = 100;
        int y1 = 300;
        int x2 = 680;
        int y2 = 500;
        int x3 = 1500;
        int y3 = 1000;
        int x4 = 1280;
        int y4 = 960;
        int explosionTriggerFlag = 123;

        Screen screen = new Screen();

        AddObject.addFoolsExplosion(screen, x1, y1, explosionTriggerFlag);
        AddObject.addFoolsExplosion(screen, x2, y2, explosionTriggerFlag);
        AddObject.addFoolsExplosion(screen, x3, y3, explosionTriggerFlag);
        AddObject.addFoolsExplosion(screen, x4, y4, explosionTriggerFlag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xb4);
        Assert.assertEquals(gameObject.getX(), 0);
        Assert.assertEquals(gameObject.getY(), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 640);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 85);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(explosionTriggerFlag, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(explosionTriggerFlag, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getX(), 640);
        Assert.assertEquals(gameObject.getY(), 480);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getX(), 1280);
        Assert.assertEquals(gameObject.getY(), 960);

        gameObject = screen.getObjects().get(3);
        Assert.assertEquals(gameObject.getX(), 1280);
        Assert.assertEquals(gameObject.getY(), 960);
    }

    @Test
    public void testAddPot() {
        int x = 540;
        int y = 300;
        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);
        WriteByteOperation writeFlag1 = new WriteByteOperation(234, ByteOp.ASSIGN_FLAG, 3);
        WriteByteOperation writeFlag2 = new WriteByteOperation(432, ByteOp.ADD_FLAG, 2);

        Screen screen = new Screen();

        AddObject.addPot(screen, x, y, PotGraphic.BIRTH, DropType.COINS, 10, Arrays.asList(testFlag1, testFlag2), Arrays.asList(writeFlag1, writeFlag2));
        AddObject.addPot(screen, x + 20, y - 40, PotGraphic.ENDLESS, DropType.CALTROPS_AMMO, 3, new ArrayList<>(), new ArrayList<>());

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x00);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), DropType.COINS.getValue());
        Assert.assertEquals((int)gameObject.getArgs().get(1), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(2), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), PotGraphic.BIRTH.getGraphic());
        Assert.assertEquals((int)gameObject.getArgs().get(5), 105);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 35);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 17);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 9);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertTrue(containsUpdate(gameObject, writeFlag1));
        Assert.assertTrue(containsUpdate(gameObject, writeFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x00);
        Assert.assertEquals(gameObject.getX(), x + 20);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), DropType.CALTROPS_AMMO.getValue());
        Assert.assertEquals((int)gameObject.getArgs().get(1), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(2), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), PotGraphic.ENDLESS.getGraphic());
        Assert.assertEquals((int)gameObject.getArgs().get(5), 105);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 35);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 17);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 9);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddMoonlightFeatherlessPlatform() {
        Screen screen = new Screen();

        AddObject.addMoonlightFeatherlessPlatform(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 580);
        Assert.assertEquals(gameObject.getY(), 200);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 128);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0); // todo: should probably arrange to check for the fools mode test being added
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddTwinPuzzleFeatherlessPlatform() {
        Screen screen = new Screen();

        AddObject.addTwinPuzzleFeatherlessPlatform(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 900);
        Assert.assertEquals(gameObject.getY(), 280);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 280);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 128);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddTwinPuzzleBlockFix() {
        Screen screen = new Screen();

        AddObject.addTwinPuzzleBlockFix(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 1060);
        Assert.assertEquals(gameObject.getY(), 440);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 280);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 128);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddTrueShrineFeatherlessPlatform() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();

        AddObject.addTrueShrineFeatherlessPlatform(screen, x, y);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 220);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 128);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddInfernoFakeWeaponCover() {
        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);

        Screen screen = new Screen();

        AddObject.addInfernoFakeWeaponCover(screen, Arrays.asList(testFlag1, testFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 20);
        Assert.assertEquals(gameObject.getY(), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 420);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 128);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }


    @Test
    public void testAddLittleBrotherWeightWaster() {
        Screen screen = new Screen();

        AddObject.addLittleBrotherWeightWaster(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x08);
        Assert.assertEquals(gameObject.getX(), 560);
        Assert.assertEquals(gameObject.getY(), 1140);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 860);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 60);
        Assert.assertEquals(gameObject.getArgs().size(), 10);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x1f0, ByteOp.FLAG_LT, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x1ea, ByteOp.FLAG_GT, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddFloodedTowerShortcutTimer() {
        Screen screen = new Screen();

        AddObject.addFloodedTowerShortcutTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(404, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(182, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(877, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddMedicineStatueTimer() {
        Screen screen = new Screen();

        AddObject.addMedicineStatueTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(2772, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x34f, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x34f, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(2772, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddHardmodeToggleWeights() {
        Screen screen = new Screen();

        AddObject.addHardmodeToggleWeights(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x08);
        Assert.assertEquals(gameObject.getX(), 560);
        Assert.assertEquals(gameObject.getY(), 100);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(2), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 860);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 60);
        Assert.assertEquals(gameObject.getArgs().size(), 10);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(362, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(362, ByteOp.ASSIGN_FLAG, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x08);
        Assert.assertEquals(gameObject.getX(), 560);
        Assert.assertEquals(gameObject.getY(), 100);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(2), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 860);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 60);
        Assert.assertEquals(gameObject.getArgs().size(), 10);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(362, ByteOp.FLAG_LT, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(362, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddSpecialItemObjects_1() {
        // todo: later
    }

    @Test
    public void testAddSpecialItemObjects_2() {
        // todo: later
    }

    @Test
    public void testAddHotspring() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();
        GameObject hotspringReference = new GameObject(screen);
        hotspringReference.setX(x);
        hotspringReference.setY(y);
        screen.getObjects().add(hotspringReference);

        AddObject.addHotspring(hotspringReference);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == hotspringReference, "Positioned object should be added to end of objects list");

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xad);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y + 20);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 2);
        Assert.assertEquals(gameObject.getArgs().size(), 4);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddSurfaceGrailTablet() {
        Screen screen = new Screen();

        AddObject.addSurfaceGrailTablet(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x9e);
        Assert.assertEquals(gameObject.getX(), 1120);
        Assert.assertEquals(gameObject.getY(), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 38);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 40);
        Assert.assertEquals(gameObject.getArgs().size(), 12);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(LocationCoordinateMapper.getGrailFlag(1, true), ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(LocationCoordinateMapper.getGrailFlag(1, true), ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
        Assert.assertEquals(screen.getObjects().size(), 1);

        Settings.setAutomaticGrailPoints(true, false);
        AddObject.addSurfaceGrailTablet(screen);

        // Make sure a grail detector got added
        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x14);

        Assert.assertEquals(screen.getObjects().size(), 3);
    }

    @Test
    public void testAddSpecialGrailTablet() {
        Screen screen = new Screen();

        AddObject.addRetroSurfaceGrailTablet(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x9f);
        Assert.assertEquals(gameObject.getX(), 400);
        Assert.assertEquals(gameObject.getY(), 160);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 38);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 506);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 280);
        Assert.assertEquals(gameObject.getArgs().size(), 9);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 400);
        Assert.assertEquals(gameObject.getY(), 160);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0xb6);
        Assert.assertEquals(gameObject.getX(), 400);
        Assert.assertEquals(gameObject.getY(), 160);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 33);
        Assert.assertEquals(gameObject.getArgs().size(), 1);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        Assert.assertEquals(screen.getObjects().size(), 3);
    }

    @Test
    public void testAddWarp() {
        int x = 540;
        int y = 300;
        int width = 4;
        int height = 5;
        int destZone = 1;
        int destRoom = 2;
        int destScreen = 3;
        int destX = 300;
        int destY = 192;

        Screen screen = new Screen();

        AddObject.addWarp(screen, x, y, width, height, destZone, destRoom, destScreen, destX, destY);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x97);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), destZone);
        Assert.assertEquals((int)gameObject.getArgs().get(1), destRoom);
        Assert.assertEquals((int)gameObject.getArgs().get(2), destScreen);
        Assert.assertEquals((int)gameObject.getArgs().get(3), destX);
        Assert.assertEquals((int)gameObject.getArgs().get(4), destY);
        Assert.assertEquals((int)gameObject.getArgs().get(5), width);
        Assert.assertEquals((int)gameObject.getArgs().get(6), height);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals(gameObject.getArgs().size(), 9);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddLemezaDetector() {
        int x = 540;
        int y = 300;
        int width = 4;
        int height = 5;
        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);
        WriteByteOperation writeFlag1 = new WriteByteOperation(234, ByteOp.ASSIGN_FLAG, 3);
        WriteByteOperation writeFlag2 = new WriteByteOperation(432, ByteOp.ADD_FLAG, 2);

        Screen screen = new Screen();

        AddObject.addLemezaDetector(screen, x, y, width, height, Arrays.asList(testFlag1, testFlag2), Arrays.asList(writeFlag1, writeFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x14);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), width);
        Assert.assertEquals((int)gameObject.getArgs().get(5), height);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertTrue(containsUpdate(gameObject, writeFlag1));
        Assert.assertTrue(containsUpdate(gameObject, writeFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddPunchyFist() {
        int x = 540;
        int y = 300;
        WriteByteOperation writeFlag1 = new WriteByteOperation(234, ByteOp.ASSIGN_FLAG, 3);
        WriteByteOperation writeFlag2 = new WriteByteOperation(432, ByteOp.ADD_FLAG, 2);

        Screen screen = new Screen();

        AddObject.addPunchyFist(screen, x, y, Arrays.asList(writeFlag1, writeFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xa3);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 63);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 32);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 32);
        Assert.assertEquals((int)gameObject.getArgs().get(9), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(10), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(11), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(12), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(14), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(15), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(16), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(17), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), -160);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 1000);
        Assert.assertEquals(gameObject.getArgs().size(), 23);
        Assert.assertTrue(containsUpdate(gameObject, writeFlag1));
        Assert.assertTrue(containsUpdate(gameObject, writeFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddEscapeTimer() {
        Screen screen = new Screen();

        Settings.setRandomizeTransitionGates(false, false);
        Settings.setScreenshakeDisabled(false, false);
        AddObject.addEscapeTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xc7);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x403, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 2);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x403, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x403, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0xc5);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 264);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(6), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1000);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 2746);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 2747);
        Assert.assertEquals(gameObject.getArgs().size(), 12);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x403, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
        Assert.assertEquals(screen.getObjects().size(), 3);

        screen.getObjects().clear();
        Settings.setRandomizeTransitionGates(true, false);
        Settings.setScreenshakeDisabled(true, false);
        AddObject.addEscapeTimer(screen);

        gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 2);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x403, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x403, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xc5);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 264);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(6), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1000);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 2746);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 2747);
        Assert.assertEquals(gameObject.getArgs().size(), 12);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x403, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddEscapeTimer_2() {
        int beginConditionFlag = 123;
        int beginConditionValue = 3;

        Screen screen = new Screen();

        Settings.setRandomizeTransitionGates(false, false);
        Settings.setScreenshakeDisabled(false, false);
        AddObject.addEscapeTimer(screen, beginConditionFlag, beginConditionValue);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 2);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x403, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x403, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(beginConditionFlag, ByteOp.FLAG_GTEQ, beginConditionValue)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x382, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0xb7);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 1);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(3);
        Assert.assertEquals(gameObject.getId(), 0xc7);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x403, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(4);
        Assert.assertEquals(gameObject.getId(), 0xc5);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 264);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(6), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1000);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 1001);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 1002);
        Assert.assertEquals(gameObject.getArgs().size(), 12);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x403, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
        Assert.assertEquals(screen.getObjects().size(), 5);

        screen.getObjects().clear();
        Settings.setRandomizeTransitionGates(true, false);
        Settings.setScreenshakeDisabled(true, false);
        AddObject.addEscapeTimer(screen, beginConditionFlag, beginConditionValue);

        gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 2);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x403, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x403, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(beginConditionFlag, ByteOp.FLAG_GTEQ, beginConditionValue)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x382, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0xb7);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 1);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(3);
        Assert.assertEquals(gameObject.getId(), 0xc5);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 264);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(6), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1000);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 1001);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 1002);
        Assert.assertEquals(gameObject.getArgs().size(), 12);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x403, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
        Assert.assertEquals(screen.getObjects().size(), 4);
    }

    @Test
    public void testAddNpcConversationTimer() {
        int flag = 123;

        Screen screen = new Screen();

        AddObject.addNpcConversationTimer(screen, flag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(flag, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(flag, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0xaca, ByteOp.ADD_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);

        AddObject.addNpcConversationTimer(screen, 0xabe);

        gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0xabe, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0xabe, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0xaca, ByteOp.ADD_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(46, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 3);
    }

    @Test
    public void testAddHalloweenGhosts() {
        // todo: delete or implement test
    }

    @Test
    public void testAddGhostSpawner() {
        int spawnRate = 360;

        Screen screen = new Screen();
        screen.setZoneIndex(1);

        AddObject.addGhostSpawner(screen, spawnRate);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x1f);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), spawnRate);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 3);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        screen.setZoneIndex(23);
        AddObject.addGhostSpawner(screen, spawnRate);

        gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x1f);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), spawnRate);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 3);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddGhostLord() {
        int x = 540;
        int y = 300;
        int speed = 1;
        int health = 300;
        int damage = 10;
        int soul = 20;

        Screen screen = new Screen();

        AddObject.addGhostLord(screen, x, y, speed, health, damage, soul);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x20);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 30);
        Assert.assertEquals((int)gameObject.getArgs().get(2), speed);
        Assert.assertEquals((int)gameObject.getArgs().get(3), health);
        Assert.assertEquals((int)gameObject.getArgs().get(4), damage);
        Assert.assertEquals((int)gameObject.getArgs().get(5), soul);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        x = 100;
        y = 0;
        speed = 0;
        health = 52;
        damage = 128;
        soul = 352;
        AddObject.addGhostLord(screen, x, y, speed, health, damage, soul);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x20);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 30);
        Assert.assertEquals((int)gameObject.getArgs().get(2), speed);
        Assert.assertEquals((int)gameObject.getArgs().get(3), health);
        Assert.assertEquals((int)gameObject.getArgs().get(4), damage);
        Assert.assertEquals((int)gameObject.getArgs().get(5), soul);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddHTExitDoor() {
        Screen screen = new Screen();

        AddObject.addHTExitDoor(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x98);
        Assert.assertEquals(gameObject.getX(), 220);
        Assert.assertEquals(gameObject.getY(), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 23);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 180);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 332);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 200);
        Assert.assertEquals(gameObject.getY(), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 560);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddLaserWall() {
        int x = 540;
        int y = 300;
        int damage = 10;

        Screen screen = new Screen();

        AddObject.addLaserWall(screen, x, y, true, damage);
        AddObject.addLaserWall(screen, x, y, false, damage);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xab);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 10);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xab);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 10);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddStunWitch() {
        int x1 = 540;
        int y1 = 300;
        int x2 = 0;
        int y2 = 500;

        Screen screen = new Screen();

        AddObject.addStunWitch(screen, x1, y1, true);
        AddObject.addStunWitch(screen, x2, y2, false);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x55);
        Assert.assertEquals(gameObject.getX(), x1);
        Assert.assertEquals(gameObject.getY(), y1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 150);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 20);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x55);
        Assert.assertEquals(gameObject.getX(), x2);
        Assert.assertEquals(gameObject.getY(), y2);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 150);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 20);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddMovingPlatforms() {
        Screen screen = new Screen();

        AddObject.addMovingPlatforms(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0c);
        Assert.assertEquals(gameObject.getX(), 800);
        Assert.assertEquals(gameObject.getY(), 160);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 380);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 790);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 150);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 220);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 270);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 150);
        Assert.assertEquals(gameObject.getArgs().size(), 18);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x7f1, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x0c);
        Assert.assertEquals(gameObject.getX(), 1100);
        Assert.assertEquals(gameObject.getY(), 360);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 380);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1090);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 150);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 220);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 90);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 150);
        Assert.assertEquals(gameObject.getArgs().size(), 18);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x7f1, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddHadoukenTurtle() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();

        AddObject.addHadoukenTurtle(screen, x, y);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x62);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 11);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 24);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 16);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 11);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 16);
        Assert.assertEquals(gameObject.getArgs().size(), 10);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x1cf, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddGuidanceShop() {
        Screen screen = new Screen();

        AddObject.addGuidanceShop(screen);

        // Door graphics
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 240);
        Assert.assertEquals(gameObject.getY(), 380);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 200);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 100);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 240);
        Assert.assertEquals(gameObject.getY(), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddMausoleumShop() {
        Screen screen = new Screen();

        AddObject.addMausoleumShop(screen);

        // Door graphics
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 300);
        Assert.assertEquals(gameObject.getY(), 240);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 320);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 212);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 300);
        Assert.assertEquals(gameObject.getY(), 240);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddInfernoShop() {
        Screen screen = new Screen();

        AddObject.addInfernoShop(screen);

        // Door graphics
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 220);
        Assert.assertEquals(gameObject.getY(), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 525);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 70);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 240);
        Assert.assertEquals(gameObject.getY(), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddIllusionShop() {
        Screen screen = new Screen();

        AddObject.addIllusionShop(screen);

        // Door graphics
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 220);
        Assert.assertEquals(gameObject.getY(), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 940);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 172);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 50);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 220);
        Assert.assertEquals(gameObject.getY(), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddTwinLabsFrontShop() {
        Screen screen = new Screen();

        AddObject.addTwinLabsFrontShop(screen);

        // Door graphics
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 880);
        Assert.assertEquals(gameObject.getY(), 380);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 500);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 200);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 900);
        Assert.assertEquals(gameObject.getY(), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddTwinLabsBackShop() {
        Screen screen = new Screen();

        AddObject.addTwinLabsBackShop(screen);

        // Door graphics
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 920);
        Assert.assertEquals(gameObject.getY(), 220);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 500);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 200);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 940);
        Assert.assertEquals(gameObject.getY(), 240);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddEndlessShop() {
        Screen screen = new Screen();

        AddObject.addEndlessShop(screen);

        // Door graphics
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 500);
        Assert.assertEquals(gameObject.getY(), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 540);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 520);
        Assert.assertEquals(gameObject.getY(), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddGraveyardShop() {
        Screen screen = new Screen();

        AddObject.addGraveyardShop(screen);

        // Door graphics
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 880);
        Assert.assertEquals(gameObject.getY(), 140);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 560);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 280);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 50);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 880);
        Assert.assertEquals(gameObject.getY(), 160);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddGoddessShop() {
        Screen screen = new Screen();

        AddObject.addGoddessShop(screen);

        // Door graphics
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 940);
        Assert.assertEquals(gameObject.getY(), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 660);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 520);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 940);
        Assert.assertEquals(gameObject.getY(), 320);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddRuinShop() {
        Screen screen = new Screen();

        AddObject.addRuinShop(screen);

        // Door graphics
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 200);
        Assert.assertEquals(gameObject.getY(), 380);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 280);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 180);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 200);
        Assert.assertEquals(gameObject.getY(), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddBirthStartStuff() {
        Screen screen = new Screen();

        AddObject.addBirthStartStuff(screen);

        // Door graphics
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 140);
        Assert.assertEquals(gameObject.getY(), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 100);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 140);
        Assert.assertEquals(gameObject.getY(), 320);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Tablet cover graphic
        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 320);
        Assert.assertEquals(gameObject.getY(), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 600);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 140);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Tablet graphic 1
        gameObject = screen.getObjects().get(3);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 200);
        Assert.assertEquals(gameObject.getY(), 380);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 320);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Tablet graphic 2
        gameObject = screen.getObjects().get(4);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 200);
        Assert.assertEquals(gameObject.getY(), 420);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 360);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddRetroSurfaceShop() {
        Screen screen = new Screen();

        AddObject.addRetroSurfaceShop(screen);

        // Door graphics, part 1
        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 480);
        Assert.assertEquals(gameObject.getY(), 200);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Door graphics, part 2
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 480);
        Assert.assertEquals(gameObject.getY(), 240);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 500);
        Assert.assertEquals(gameObject.getY(), 240);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 36);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddSecretShop() {
        int shopBlock = 1234;

        Screen screen = new Screen();

        AddObject.addSecretShop(screen, shopBlock);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xa0);
        Assert.assertEquals(gameObject.getX(), 0);
        Assert.assertEquals(gameObject.getY(), 240);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), shopBlock);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0xacd, ByteOp.FLAG_GTEQ, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddDanceDetector() {
        int danceBlock = 1234;

        Screen screen = new Screen();

        AddObject.addDanceDetector(screen, danceBlock);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xb8);
        Assert.assertEquals(gameObject.getX(), 0);
        Assert.assertEquals(gameObject.getY(), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(0), danceBlock);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 32);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 24);
        Assert.assertEquals(gameObject.getArgs().size(), 3);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0xacd, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0xacd, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddItemGive() {
        int x1 = 100;
        int y1 = 300;
        int x2 = 680;
        int y2 = 500;
        int x3 = 1500;
        int y3 = 1000;
        int x4 = 1280;
        int y4 = 960;

        int inventoryArg = 84;
        int randomizeGraphicsFlag = 123;
        int worldFlag = 234;

        Screen screen = new Screen();
        GameObject objectOnScreen = new GameObject(screen);
        screen.getObjects().add(objectOnScreen);

        objectOnScreen.setX(x1);
        objectOnScreen.setY(y1);
        AddObject.addItemGive(objectOnScreen, inventoryArg, randomizeGraphicsFlag, worldFlag);
        objectOnScreen.setX(x2);
        objectOnScreen.setY(y2);
        AddObject.addItemGive(objectOnScreen, inventoryArg, randomizeGraphicsFlag, worldFlag);
        objectOnScreen.setX(x3);
        objectOnScreen.setY(y3);
        AddObject.addItemGive(objectOnScreen, inventoryArg, randomizeGraphicsFlag, worldFlag);
        objectOnScreen.setX(x4);
        objectOnScreen.setY(y4);
        AddObject.addItemGive(objectOnScreen, inventoryArg, randomizeGraphicsFlag, worldFlag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == objectOnScreen, "Positioned objects should be added to end of objects list");

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xb5);
        Assert.assertEquals(gameObject.getX(), 0);
        Assert.assertEquals(gameObject.getY(), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(0), inventoryArg);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 32);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 24);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 39);
        Assert.assertEquals(gameObject.getArgs().size(), 4);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(randomizeGraphicsFlag, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(worldFlag, ByteOp.FLAG_NOT_EQUAL, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(worldFlag, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0xb5);
        Assert.assertEquals(gameObject.getX(), 640);
        Assert.assertEquals(gameObject.getY(), 480);
        Assert.assertEquals((int)gameObject.getArgs().get(0), inventoryArg);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 32);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 24);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 39);
        Assert.assertEquals(gameObject.getArgs().size(), 4);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(randomizeGraphicsFlag, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(worldFlag, ByteOp.FLAG_NOT_EQUAL, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(worldFlag, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(3);
        Assert.assertEquals(gameObject.getId(), 0xb5);
        Assert.assertEquals(gameObject.getX(), 1280);
        Assert.assertEquals(gameObject.getY(), 960);
        Assert.assertEquals((int)gameObject.getArgs().get(0), inventoryArg);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 32);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 24);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 39);
        Assert.assertEquals(gameObject.getArgs().size(), 4);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(randomizeGraphicsFlag, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(worldFlag, ByteOp.FLAG_NOT_EQUAL, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(worldFlag, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(4);
        Assert.assertEquals(gameObject.getId(), 0xb5);
        Assert.assertEquals(gameObject.getX(), 1280);
        Assert.assertEquals(gameObject.getY(), 960);
        Assert.assertEquals((int)gameObject.getArgs().get(0), inventoryArg);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 32);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 24);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 39);
        Assert.assertEquals(gameObject.getArgs().size(), 4);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(randomizeGraphicsFlag, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(worldFlag, ByteOp.FLAG_NOT_EQUAL, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(worldFlag, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddItemGive_2() {
        int x = 540;
        int y = 300;
        int inventoryArg = 84;
        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);
        WriteByteOperation writeFlag1 = new WriteByteOperation(234, ByteOp.ASSIGN_FLAG, 3);
        WriteByteOperation writeFlag2 = new WriteByteOperation(432, ByteOp.ADD_FLAG, 2);

        Screen screen = new Screen();

        AddObject.addItemGive(screen, x, y, inventoryArg, Arrays.asList(testFlag1, testFlag2), Arrays.asList(writeFlag1, writeFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xb5);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), inventoryArg);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 32);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 24);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 39);
        Assert.assertEquals(gameObject.getArgs().size(), 4);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertTrue(containsUpdate(gameObject, writeFlag1));
        Assert.assertTrue(containsUpdate(gameObject, writeFlag2));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddGrailDetector() {
        int x = 540;
        int y = 300;
        int grailFlag = 123;

        Screen screen = new Screen();
        GameObject grailTablet = new GameObject(screen);
        grailTablet.setX(x);
        grailTablet.setY(y);
        screen.getObjects().add(grailTablet);

        AddObject.addGrailDetector(grailTablet, grailFlag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == grailTablet, "Positioned object should be added to end of objects list");

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x14);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y - 20);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 3);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(grailFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(grailFlag, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddUntrueShrineExit() {
        Screen screen = new Screen();

        AddObject.addUntrueShrineExit(screen, 0);
        AddObject.addUntrueShrineExit(screen, 1);
        AddObject.addUntrueShrineExit(screen, 2);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xc4);
        Assert.assertEquals(gameObject.getX(), 300);
        Assert.assertEquals(gameObject.getY(), 940);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 7);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xc4);
        Assert.assertEquals(gameObject.getX(), 300);
        Assert.assertEquals(gameObject.getY(), 460);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0xc4);
        Assert.assertEquals(gameObject.getX(), 940);
        Assert.assertEquals(gameObject.getY(), 460);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 7);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 9);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddSpecialTransitionWarp() {
        Screen screen = new Screen();

        AddObject.addSpecialTransitionWarp(screen, 0);
        AddObject.addSpecialTransitionWarp(screen, 5);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x97);
        Assert.assertEquals(gameObject.getX(), 140);
        Assert.assertEquals(gameObject.getY(), 440);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals(gameObject.getArgs().size(), 9);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x97);
        Assert.assertEquals(gameObject.getX(), 300);
        Assert.assertEquals(gameObject.getY(), 440);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 140);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals(gameObject.getArgs().size(), 9);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddSpecialTransitionGate() {
        Screen screen = new Screen();

        AddObject.addSpecialTransitionGate(screen, 0);
        AddObject.addSpecialTransitionGate(screen, 5);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xc4);
        Assert.assertEquals(gameObject.getX(), 140);
        Assert.assertEquals(gameObject.getY(), 460);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 392);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xc4); // This is added by addEscapeGate, doesn't need further testing

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0xc4);
        Assert.assertEquals(gameObject.getX(), 300);
        Assert.assertEquals(gameObject.getY(), 460);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 140);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 392);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(3);
        Assert.assertEquals(gameObject.getId(), 0xc4); // This is added by addEscapeGate, doesn't need further testing
    }

    @Test
    public void testAddEscapeGate() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();

        GameObject nonEscapeGate = new GameObject(screen);
        nonEscapeGate.setId((short)0xc4);
        nonEscapeGate.setX(x);
        nonEscapeGate.setY(y);
        nonEscapeGate.getArgs().add((short)1);
        nonEscapeGate.getArgs().add((short)2);
        nonEscapeGate.getArgs().add((short)3);
        nonEscapeGate.getArgs().add((short)300);
        nonEscapeGate.getArgs().add((short)392);
        nonEscapeGate.getArgs().add((short)0);
        nonEscapeGate.getArgs().add((short)0);
        nonEscapeGate.addTests(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0));
        screen.getObjects().add(nonEscapeGate);

        AddObject.addEscapeGate(nonEscapeGate);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == nonEscapeGate, "Positioned object should be added to end of objects list");

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xc4);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 392);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertFalse(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddTrueShrineGate() {
        int x = 540;
        int y = 300;
        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);

        Screen screen = new Screen();

        GameObject transitionGate = new GameObject(screen);
        transitionGate.setId((short)0xc4);
        transitionGate.setX(x);
        transitionGate.setY(y);
        transitionGate.getArgs().add((short)9);
        transitionGate.getArgs().add((short)2);
        transitionGate.getArgs().add((short)3);
        transitionGate.getArgs().add((short)300);
        transitionGate.getArgs().add((short)392);
        transitionGate.getArgs().add((short)0);
        transitionGate.getArgs().add((short)1);
        transitionGate.addTests(new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0));
        screen.getObjects().add(transitionGate);

        AddObject.addTrueShrineGate(transitionGate);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == transitionGate, "Positioned object should be added to end of objects list");

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0xc4);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 18);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 392);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 7);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x102, ByteOp.FLAG_EQUALS, 9)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_NOT_EQUAL, 1)));
        Assert.assertFalse(containsTest(gameObject, testFlag1));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddShrineMapSoundEffect() {
        Screen screen = new Screen();

        AddObject.addShrineMapSoundEffect(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(2798, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(218, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(2798, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(42, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 30);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 25);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 15);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(218, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(42, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddMotherAnkhJewelRecoveryTimer() {
        Screen screen = new Screen();

        AddObject.addMotherAnkhJewelRecoveryTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x0fe, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x382, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0xad2, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0xad2, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 3);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddMotherAnkhJewelItemGive() {
        Screen screen = new Screen();

        AddObject.addMotherAnkhJewelItemGive(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xb5);
        Assert.assertEquals(gameObject.getX(), 100);
        Assert.assertEquals(gameObject.getY(), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 19);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 12);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 16);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 39);
        Assert.assertEquals(gameObject.getArgs().size(), 4);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0xad2, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0xad2, ByteOp.ASSIGN_FLAG, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x0fe, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddMissingBacksideDoorCover() {
        int x = 540;
        int y = 300;
        int gateFlag = 123;

        Screen screen = new Screen();
        GameObject backsideDoor = new GameObject(screen);
        backsideDoor.setX(x);
        backsideDoor.setY(y);
        screen.getObjects().add(backsideDoor);

        AddObject.addMissingBacksideDoorCover(backsideDoor, gateFlag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == backsideDoor, "Positioned object should be added to end of objects list");

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 360);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 7);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 6);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x0ae, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddMissingBacksideDoorTimerAndSound() {
        int bossFlag = 123;
        int gateFlag = 321;
        Screen screen = new Screen();

        AddObject.addMissingBacksideDoorTimerAndSound(screen, bossFlag, gateFlag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(bossFlag, ByteOp.FLAG_GTEQ, 3)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x0ae, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x029, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 3);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 95);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 127);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(3), -400);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 127);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 15);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 15);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x029, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddMissingBacksideMirrorTimerAndSound() {
        int mirrorCoverFlag = 123;
        Screen screen = new Screen();

        AddObject.addMissingBacksideMirrorTimerAndSound(screen, mirrorCoverFlag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 30);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x0ae, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(mirrorCoverFlag, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x028, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 48);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 127);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(3), -700);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 127);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(6), -700);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 15);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x028, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddMissingBacksideDoorMirrorCoverGraphic() {
        int x = 540;
        int y = 300;
        int mirrorCoverFlag = 123;

        Screen screen = new Screen();
        GameObject backsideDoor = new GameObject(screen);
        backsideDoor.setX(x);
        backsideDoor.setY(y);
        screen.getObjects().add(backsideDoor);

        AddObject.addMissingBacksideDoorMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, true);
        AddObject.addMissingBacksideDoorMirrorCoverGraphic(backsideDoor, mirrorCoverFlag, false);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == backsideDoor, "Positioned object should be added to end of objects list");

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 940);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 540);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddExtinctionTorch() {
        Screen screen = new Screen();

        AddObject.addExtinctionTorch(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x12);
        Assert.assertEquals(gameObject.getX(), 60);
        Assert.assertEquals(gameObject.getY(), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 11);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(8), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 12);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x1c2, ByteOp.FLAG_LTEQ, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x1cd, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 60);
        Assert.assertEquals(gameObject.getY(), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 160);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x1cd, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 60);
        Assert.assertEquals(gameObject.getY(), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddDimensionalOrbLadder() {
        Screen screen = new Screen();

        AddObject.addDimensionalOrbLadder(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x07);
        Assert.assertEquals(gameObject.getX(), 560);
        Assert.assertEquals(gameObject.getY(), 620);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 8);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 660);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 8);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x2cc, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddShopBlock() {
        // todo: delete or implement test
    }

    @Test
    public void testAddSecretShopBlock() {
        // todo: delete or implement test
    }

    @Test
    public void testAddDanceBlock() {
        // todo: delete or implement test
    }

    @Test
    public void testAddNpcBlock() {
        // todo: delete or implement test
    }

    @Test
    public void testAddNoCandyMasterBlock() {
        // todo: delete or implement test
    }

    @Test
    public void testAddNpcCountBlock() {
        // todo: delete or implement test
    }

    @Test
    public void testAddNpcHintBlock() {
        // todo: delete or implement test
    }

    @Test
    public void testAddFoolsMulbrukBlocks() {
        // todo: delete or implement test
    }

    @Test
    public void testAddDevRoomHintBlock() {
        // todo: delete or implement test
    }

    @Test
    public void testAddMulbrukHTBlock() {
        // todo: delete or implement test
    }

    @Test
    public void testAddAllNpcsBlock() {
        // todo: delete or implement test
    }

    @Test
    public void testAddNoCandyBlock() {
        // todo: delete or implement test
    }

    @Test
    public void testAddHTSkip() {
        Screen screen = new Screen();
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block(0));
        blocks.add(new Block(1));
        blocks.add(new Block(2));
        blocks.add(new Block(3));

        try {
            Translations.initTranslations();
        }
        catch(Exception ex) {
            Assert.fail("Unable to initialize translations");
        }
        AddObject.addHTSkip(screen, blocks);

        Assert.assertEquals(blocks.size(), 5);
        Assert.assertEquals(blocks.get(blocks.size() - 1).getBlockNumber(), 4);

        Assert.assertEquals(screen.getObjects().size(), 3);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x9e);
        Assert.assertEquals(gameObject.getX(), 60);
        Assert.assertEquals(gameObject.getY(), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 40);
        Assert.assertEquals(gameObject.getArgs().size(), 12);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 60);
        Assert.assertEquals(gameObject.getY(), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x08);
        Assert.assertEquals(gameObject.getX(), 60);
        Assert.assertEquals(gameObject.getY(), 420);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(2), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 860);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 60);
        Assert.assertEquals(gameObject.getArgs().size(), 10);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x70d, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x70d, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 1);
    }

    @Test
    public void testAddHTWarning() {
        Screen screen = new Screen();
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block(0));
        blocks.add(new Block(1));
        blocks.add(new Block(2));

        try {
            Translations.initTranslations();
        }
        catch(Exception ex) {
            Assert.fail("Unable to initialize translations");
        }
        AddObject.addHTWarning(screen, blocks);

        Assert.assertEquals(blocks.size(), 4);
        Assert.assertEquals(blocks.get(blocks.size() - 1).getBlockNumber(), 3);
        Block tabletBlock = blocks.get(blocks.size() - 1);
        BlockContents tabletData = tabletBlock.getBlockContents().get(tabletBlock.getBlockContents().size() - 1);
        Assert.assertTrue(tabletData instanceof BlockListData, "Expected tablet data list at end of block contents");
        List<Short> tabletDataList = ((BlockListData)tabletData).getData();
        Assert.assertEquals((int)tabletDataList.get(0), 0, "Expected tablet language of English");
        Assert.assertEquals((int)tabletDataList.get(1), 0, "Expected tablet image of none");

        Assert.assertEquals(screen.getObjects().size(), 2);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x9e);
        Assert.assertEquals(gameObject.getX(), 120);
        Assert.assertEquals(gameObject.getY(), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 40);
        Assert.assertEquals(gameObject.getArgs().size(), 12);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), 120);
        Assert.assertEquals(gameObject.getY(), 400);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddTwinLabsDoor() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();
        GameObject ankh = new GameObject(screen);
        ankh.setX(x);
        ankh.setY(y);
        screen.getObjects().add(ankh);

        AddObject.addTwinLabsDoor(ankh);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertTrue(gameObject == ankh, "Positioned object should be added to end of objects list");

        // Door graphics
        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y - 40);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 512);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x0fb, ByteOp.FLAG_GTEQ, 3)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        // Functional door object
        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x98);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 7);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 300);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x0fb, ByteOp.FLAG_GTEQ, 3)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddBossTimer() {
        int bossFlag = 123;
        int otherFlag = 321;

        Screen screen = new Screen();

        AddObject.addBossTimer(screen, bossFlag, otherFlag);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x102, ByteOp.FLAG_EQUALS, 8)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x102, ByteOp.ASSIGN_FLAG, 9)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x07b, ByteOp.ASSIGN_FLAG, 200)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x06c, ByteOp.ASSIGN_FLAG, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x2e1, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 4);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(bossFlag, ByteOp.FLAG_EQUALS, 3)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(otherFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x102, ByteOp.ADD_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(otherFlag, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x07b, ByteOp.ADD_FLAG, 8)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 3);

        screen.getObjects().clear();
        bossFlag = 0x0fb;
        otherFlag = 432;

        AddObject.addBossTimer(screen, bossFlag, otherFlag);

        gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x102, ByteOp.FLAG_EQUALS, 8)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x102, ByteOp.ASSIGN_FLAG, 9)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x07b, ByteOp.ASSIGN_FLAG, 200)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x06c, ByteOp.ASSIGN_FLAG, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x2e1, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 4);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(bossFlag, ByteOp.FLAG_EQUALS, 3)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(otherFlag, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x102, ByteOp.ADD_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(otherFlag, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x07b, ByteOp.ADD_FLAG, 8)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x3b8, ByteOp.ASSIGN_FLAG, 3)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 4);
    }

    @Test
    public void testAddSphinxRemovalTimer() {
        Screen screen = new Screen();

        AddObject.addSphinxRemovalTimer(screen);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x0b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x173, ByteOp.FLAG_GTEQ, 1)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x173, ByteOp.FLAG_LT, 5)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x173, ByteOp.ASSIGN_FLAG, 5)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x17d, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddEyeOfDivineRetribution() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();

        AddObject.addEyeOfDivineRetribution(screen, x, y);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x95);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 100);
        Assert.assertEquals(gameObject.getArgs().size(), 3);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddPushableBlock() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();

        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);

        AddObject.addPushableBlock(screen, x, y, Arrays.asList(testFlag1, testFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0xa9);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 2);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddHitbox() {
        int x = 540;
        int y = 300;
        int width = 32;
        int height = 24;

        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);
        WriteByteOperation writeFlag1 = new WriteByteOperation(234, ByteOp.ASSIGN_FLAG, 3);
        WriteByteOperation writeFlag2 = new WriteByteOperation(432, ByteOp.ADD_FLAG, 2);

        Screen screen = new Screen();

        AddObject.addHitbox(screen, x, y, width, height, Arrays.asList(testFlag1, testFlag2), Arrays.asList(writeFlag1, writeFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x12);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 18);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(6), width);
        Assert.assertEquals((int)gameObject.getArgs().get(7), height);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 105);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 104);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 2);
        Assert.assertEquals(gameObject.getArgs().size(), 12);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertTrue(containsUpdate(gameObject, writeFlag1));
        Assert.assertTrue(containsUpdate(gameObject, writeFlag2));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);
    }

    @Test
    public void testAddExtendingSpikes() {
        int x = 540;
        int y = 300;
        int flagIndex = 123;

        Screen screen = new Screen();
        GameObject dais = new GameObject(screen);
        dais.setX(x);
        dais.setY(y);
        screen.getObjects().add(dais);

        AddObject.addExtendingSpikes(dais, flagIndex);

        GameObject gameObject = screen.getObjects().get(1);
        Assert.assertTrue(gameObject == dais, "Positioned object should be added to end of objects list, non-positioned object to front");

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x96);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y + 20);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 100);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 100);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 200);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 100);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 500);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 320);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 40);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 60);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 21);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 41);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(24), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(25), 20);
        Assert.assertEquals(gameObject.getArgs().size(), 26);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(flagIndex, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(flagIndex, ByteOp.ASSIGN_FLAG, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);

        // Sound effect
        gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 25);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 15);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0xa7, ByteOp.FLAG_EQUALS, 2)));
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(flagIndex, ByteOp.FLAG_EQUALS, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddZebuDais() {
        // todo: write tests
//        AddObject.addZebuDais(new Screen());
    }

    @Test
    public void testAddSuccessSound() {
        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);

        Screen screen = new Screen();

        AddObject.addSuccessSound(screen, Arrays.asList(testFlag1, testFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x9b);
        Assert.assertEquals(gameObject.getX(), -1);
        Assert.assertEquals(gameObject.getY(), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 30);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 120);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 64);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 25);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 5);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 10);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 15);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddWarpDoor() {
        int x = 540;
        int y = 300;
        int destZone = 1;
        int destRoom = 2;
        int destScreen = 3;
        int destX = 300;
        int destY = 192;

        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);

        Screen screen = new Screen();

        AddObject.addWarpDoor(screen, x, y, destZone, destRoom, destScreen, destX, destY, Arrays.asList(testFlag1, testFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x98);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), destZone);
        Assert.assertEquals((int)gameObject.getArgs().get(2), destRoom);
        Assert.assertEquals((int)gameObject.getArgs().get(3), destScreen);
        Assert.assertEquals((int)gameObject.getArgs().get(4), destX);
        Assert.assertEquals((int)gameObject.getArgs().get(5), destY);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddAmphisbaenaAnkh() {
        int x = 540;
        int y = 300;
        int damage = 128;

        TestByteOperation testFlag1 = new TestByteOperation(123, ByteOp.FLAG_NOT_EQUAL, 1);
        TestByteOperation testFlag2 = new TestByteOperation(321, ByteOp.FLAG_GT, 4);

        Screen screen = new Screen();

        AddObject.addAmphisbaenaAnkh(screen, x, y, damage, Arrays.asList(testFlag1, testFlag2));

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x2e);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(3), damage);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(5), damage);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(24), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(25), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(26), 1519);
        Assert.assertEquals((int)gameObject.getArgs().get(27), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(28), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(29), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(30), 1519);
        Assert.assertEquals((int)gameObject.getArgs().get(31), 0);
        Assert.assertEquals(gameObject.getArgs().size(), 32);
        Assert.assertTrue(containsTest(gameObject, testFlag1));
        Assert.assertTrue(containsTest(gameObject, testFlag2));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 2);
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x0f6, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x0f6, ByteOp.ASSIGN_FLAG, 2)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x0f6, ByteOp.ASSIGN_FLAG, 3)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x133, ByteOp.ASSIGN_FLAG, 6)));
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 4);
    }

    @Test
    public void testAddLampStation() {
        int x = 540;
        int y = 300;

        Screen screen = new Screen();

        AddObject.addLampStation(screen, x, y);

        GameObject gameObject = screen.getObjects().get(0);
        Assert.assertEquals(gameObject.getId(), 0x14);
        Assert.assertEquals(gameObject.getX(), x);
        Assert.assertEquals(gameObject.getY(), y);
        Assert.assertEquals((int)gameObject.getArgs().get(0), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 2);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 3);
        Assert.assertEquals(gameObject.getArgs().size(), 6);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x34d, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x34d, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertTrue(containsUpdate(gameObject, new WriteByteOperation(0x3ed, ByteOp.ASSIGN_FLAG, 1)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 2);

        gameObject = screen.getObjects().get(1);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x - 20);
        Assert.assertEquals(gameObject.getY(), y - 20);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 940);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 140);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 80);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 0);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);

        gameObject = screen.getObjects().get(2);
        Assert.assertEquals(gameObject.getId(), 0x93);
        Assert.assertEquals(gameObject.getX(), x + 20);
        Assert.assertEquals(gameObject.getY(), y + 20);
        Assert.assertEquals((int)gameObject.getArgs().get(0), -1);
        Assert.assertEquals((int)gameObject.getArgs().get(1), 3);
        Assert.assertEquals((int)gameObject.getArgs().get(2), 280);
        Assert.assertEquals((int)gameObject.getArgs().get(3), 112);
        Assert.assertEquals((int)gameObject.getArgs().get(4), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(5), 20);
        Assert.assertEquals((int)gameObject.getArgs().get(6), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(7), 1);
        Assert.assertEquals((int)gameObject.getArgs().get(8), 4);
        Assert.assertEquals((int)gameObject.getArgs().get(9), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(10), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(11), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(12), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(13), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(14), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(15), 255);
        Assert.assertEquals((int)gameObject.getArgs().get(16), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(17), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(18), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(19), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(20), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(21), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(22), 0);
        Assert.assertEquals((int)gameObject.getArgs().get(23), 1);
        Assert.assertEquals(gameObject.getArgs().size(), 24);
        Assert.assertTrue(containsTest(gameObject, new TestByteOperation(0x34d, ByteOp.FLAG_EQUALS, 0)));
        Assert.assertEquals(gameObject.getTestByteOperations().size(), 1);
        Assert.assertEquals(gameObject.getWriteByteOperations().size(), 0);
    }

    @Test
    public void testAddCustomItemGives() {
        // todo: this is going to be huge
    }

    private boolean containsTest(GameObject gameObject, TestByteOperation match) {
        for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
            if(testByteOperation.getIndex() == match.getIndex()
                    && testByteOperation.getOp().equals(match.getOp())
                    && testByteOperation.getValue() == match.getValue()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsUpdate(GameObject gameObject, WriteByteOperation match) {
        for(WriteByteOperation writeByteOperation : gameObject.getWriteByteOperations()) {
            if(writeByteOperation.getIndex() == match.getIndex()
                    && writeByteOperation.getOp().equals(match.getOp())
                    && writeByteOperation.getValue() == match.getValue()) {
                return true;
            }
        }
        return false;
    }
}
