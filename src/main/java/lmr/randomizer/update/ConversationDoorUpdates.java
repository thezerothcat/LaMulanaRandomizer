package lmr.randomizer.update;

import lmr.randomizer.randomization.data.ShopInventory;
import lmr.randomizer.util.BlockConstants;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.rcd.object.*;

public final class ConversationDoorUpdates {
    private ConversationDoorUpdates() { }

    /**
     * @param doorObject the base npc door object, to use as a positional reference
     */
    public static void addLittleBrotherScreenObjects(GameObject doorObject, ShopInventory shopInventory) {
        Screen screen = (Screen)doorObject.getObjectContainer();
        GameObject bigBrotherProgressTimer = new GameObject(screen);
        bigBrotherProgressTimer.setId(ObjectIdConstants.FlagTimer);
        bigBrotherProgressTimer.getArgs().add((short)0);
        bigBrotherProgressTimer.getArgs().add((short)0);
        bigBrotherProgressTimer.setX(-1);
        bigBrotherProgressTimer.setY(-1);
        bigBrotherProgressTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.LITTLE_BROTHER_PURCHASES, ByteOp.FLAG_GTEQ, 3));
        bigBrotherProgressTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.BIG_BROTHER_UNLOCKED, ByteOp.FLAG_EQUALS, 0));
        bigBrotherProgressTimer.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.BIG_BROTHER_UNLOCKED, ByteOp.ASSIGN_FLAG, 1));
        screen.getObjects().add(0, bigBrotherProgressTimer);

        GameObject bigBrotherSoundEffect = new GameObject(screen);
        bigBrotherSoundEffect.setId(ObjectIdConstants.SoundEffect);
        bigBrotherSoundEffect.getArgs().add((short)30);
        bigBrotherSoundEffect.getArgs().add((short)120);
        bigBrotherSoundEffect.getArgs().add((short)64);
        bigBrotherSoundEffect.getArgs().add((short)0);
        bigBrotherSoundEffect.getArgs().add((short)120);
        bigBrotherSoundEffect.getArgs().add((short)64);
        bigBrotherSoundEffect.getArgs().add((short)0);
        bigBrotherSoundEffect.getArgs().add((short)25);
        bigBrotherSoundEffect.getArgs().add((short)1);
        bigBrotherSoundEffect.getArgs().add((short)5);
        bigBrotherSoundEffect.getArgs().add((short)0);
        bigBrotherSoundEffect.getArgs().add((short)10);
        bigBrotherSoundEffect.getArgs().add((short)0);
        bigBrotherSoundEffect.getArgs().add((short)0);
        bigBrotherSoundEffect.getArgs().add((short)0);
        bigBrotherSoundEffect.setX(-1);
        bigBrotherSoundEffect.setY(-1);
        bigBrotherSoundEffect.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_SHELL_HORN, ByteOp.FLAG_EQUALS, 2));
        bigBrotherSoundEffect.getTestByteOperations().add(new TestByteOperation(FlagConstants.BIG_BROTHER_UNLOCKED, ByteOp.FLAG_EQUALS, 2));
        bigBrotherSoundEffect.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.FLAG_EQUALS, 1));
        screen.getObjects().add(0, bigBrotherSoundEffect);

        GameObject bigBrotherNotificationConversation = new GameObject(screen);
        bigBrotherNotificationConversation.setId(ObjectIdConstants.ConversationDoor);
        bigBrotherNotificationConversation.getArgs().add((short)0);
        bigBrotherNotificationConversation.getArgs().add((short)0);
        bigBrotherNotificationConversation.getArgs().add((short)0);
        bigBrotherNotificationConversation.getArgs().add((short)0);
        bigBrotherNotificationConversation.getArgs().add((short)682);
        bigBrotherNotificationConversation.getArgs().add((short)0);
        bigBrotherNotificationConversation.getArgs().add((short)0);
        bigBrotherNotificationConversation.setX(doorObject.getX());
        bigBrotherNotificationConversation.setY(doorObject.getY());
        bigBrotherNotificationConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.BIG_BROTHER_UNLOCKED, ByteOp.FLAG_EQUALS, 1));
        bigBrotherNotificationConversation.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.BIG_BROTHER_UNLOCKED, ByteOp.ASSIGN_FLAG, 2));
        bigBrotherNotificationConversation.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));
        screen.getObjects().add(bigBrotherNotificationConversation);

        // Little Brother's shop
        Short worldFlag = shopInventory.getItem1().getWorldFlag();
        if(worldFlag != 0) {
            AddObject.addLittleBrotherShopTimer(screen, worldFlag);
        }
        worldFlag = shopInventory.getItem2().getWorldFlag();
        if(worldFlag != 0) {
            AddObject.addLittleBrotherShopTimer(screen, worldFlag);
        }
        worldFlag = shopInventory.getItem3().getWorldFlag();
        if(worldFlag != 0) {
            AddObject.addLittleBrotherShopTimer(screen, worldFlag);
        }
    }

    /**
     * @param doorObject the base npc door object, to use as a positional reference
     */
    public static void addPhilosopherStoneDoor(GameObject doorObject) {
        GameObject philosopherStoneConversation = new GameObject(doorObject.getObjectContainer());
        philosopherStoneConversation.setId(ObjectIdConstants.ConversationDoor);
        philosopherStoneConversation.getArgs().add((short)0);
        philosopherStoneConversation.getArgs().add((short)0);
        philosopherStoneConversation.getArgs().add((short)0);
        philosopherStoneConversation.getArgs().add((short)0);
        philosopherStoneConversation.getArgs().add((short)676);
        philosopherStoneConversation.getArgs().add((short)0);
        philosopherStoneConversation.getArgs().add((short)0);
        philosopherStoneConversation.setX(doorObject.getX());
        philosopherStoneConversation.setY(doorObject.getY());
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            philosopherStoneConversation.getTestByteOperations().add(testByteOperation);
        }
        philosopherStoneConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_PHILOSOPHERS_OCARINA, ByteOp.FLAG_LTEQ, 1)); // Using <= 1 rather than == 0 in case of a chest check setting to 1
        doorObject.getObjectContainer().getObjects().add(philosopherStoneConversation);
    }

    /**
     * @param doorObject the base npc door object, to use as a positional reference
     */
    public static void addFobosDoors(GameObject doorObject) {
        addPhilosopherStoneDoor(doorObject);
        GameObject fobosConversation = new GameObject(doorObject.getObjectContainer());
        fobosConversation.setId(ObjectIdConstants.ConversationDoor);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)BlockConstants.Master_PhilosopherFobos_Ladder);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.setX(doorObject.getX());
        fobosConversation.setY(doorObject.getY());
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            fobosConversation.getTestByteOperations().add(testByteOperation);
        }
        fobosConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_PHILOSOPHERS_OCARINA, ByteOp.FLAG_EQUALS, 2)); // Philosopher's Ocarina collected
        fobosConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.FOBOS_LADDER, ByteOp.FLAG_EQUALS, 0)); // Fobos not yet spoken to
        fobosConversation.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.FOBOS_LADDER, ByteOp.ASSIGN_FLAG, 1)); // Trigger for Shrine of the Mother ladder
        doorObject.getObjectContainer().getObjects().add(fobosConversation);

        fobosConversation = new GameObject(doorObject.getObjectContainer());
        fobosConversation.setId(ObjectIdConstants.ConversationDoor);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)BlockConstants.Master_PhilosopherFobos_Ladder);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.setX(doorObject.getX());
        fobosConversation.setY(doorObject.getY());
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            fobosConversation.getTestByteOperations().add(testByteOperation);
        }
        fobosConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_PHILOSOPHERS_OCARINA, ByteOp.FLAG_EQUALS, 2)); // Philosopher's Ocarina collected
        fobosConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.MEDICINE_SOLVED, ByteOp.FLAG_EQUALS, 0)); // Medicine puzzle not solved
        fobosConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.FOBOS_LADDER, ByteOp.FLAG_NOT_EQUAL, 0)); // Fobos spoken to
        doorObject.getObjectContainer().getObjects().add(fobosConversation);
    }

    /**
     * @param doorObject the base npc door object, to use as a positional reference
     */
    public static void addFairyQueenDoors(GameObject doorObject) {
        GameObject fairyQueenConversation = new GameObject(doorObject.getObjectContainer());
        fairyQueenConversation.setId(ObjectIdConstants.ConversationDoor);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)685);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.setX(doorObject.getX());
        fairyQueenConversation.setY(doorObject.getY());
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            fairyQueenConversation.getTestByteOperations().add(testByteOperation);
        }
        fairyQueenConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.FLAG_LTEQ, 1)); // Fairy Queen conversation progress
        fairyQueenConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_ISIS_PENDANT, ByteOp.FLAG_LTEQ, 1)); // Isis' Pendant not found
        doorObject.getObjectContainer().getObjects().add(fairyQueenConversation);

        fairyQueenConversation = new GameObject(doorObject.getObjectContainer());
        fairyQueenConversation.setId(ObjectIdConstants.ConversationDoor);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)687);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.setX(doorObject.getX());
        fairyQueenConversation.setY(doorObject.getY());
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            fairyQueenConversation.getTestByteOperations().add(testByteOperation);
        }
        fairyQueenConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.FLAG_EQUALS, 2)); // Fairy Queen conversation progress; fairy points unlocked
        fairyQueenConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.SHRINE_FAIRY_BLOCK, ByteOp.FLAG_EQUALS, 0)); // Fairy block in True Shrine hasn't spawned yet
        doorObject.getObjectContainer().getObjects().add(fairyQueenConversation);

        fairyQueenConversation = new GameObject(doorObject.getObjectContainer());
        fairyQueenConversation.setId(ObjectIdConstants.ConversationDoor);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)688);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.setX(doorObject.getX());
        fairyQueenConversation.setY(doorObject.getY());
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            fairyQueenConversation.getTestByteOperations().add(testByteOperation);
        }
        fairyQueenConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.FLAG_EQUALS, 2)); // Fairy Queen conversation progress; fairy points unlocked
        fairyQueenConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.SHRINE_FAIRY_BLOCK, ByteOp.FLAG_EQUALS, 1)); // Fairy block in True Shrine has spawned
        fairyQueenConversation.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SHRINE_FAIRY_BLOCK, ByteOp.ASSIGN_FLAG, 2)); // Fairy block in True Shrine will be removed when you go there
        doorObject.getObjectContainer().getObjects().add(fairyQueenConversation);

        fairyQueenConversation = new GameObject(doorObject.getObjectContainer());
        fairyQueenConversation.setId(ObjectIdConstants.ConversationDoor);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)985);
        fairyQueenConversation.getArgs().add((short)0);
        fairyQueenConversation.getArgs().add((short)1);
        fairyQueenConversation.setX(doorObject.getX());
        fairyQueenConversation.setY(doorObject.getY());
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            fairyQueenConversation.getTestByteOperations().add(testByteOperation);
        }
        fairyQueenConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.SHRINE_FAIRY_BLOCK, ByteOp.FLAG_EQUALS, 2)); // Fairy block in True Shrine will be removed when you go there (have had the previous conversation)
        doorObject.getObjectContainer().getObjects().add(fairyQueenConversation);
    }

    /**
     * @param doorObject the base npc door object, to use as a positional reference
     */
    public static void addPriestAlestDoor(ConversationDoor doorObject, int itemFlag) {
        ConversationDoor priestAlestConversation = new ConversationDoor(doorObject.getObjectContainer(), doorObject.getX(), doorObject.getY());
        priestAlestConversation.setDoorType(ConversationDoor.SingleConversation);
        priestAlestConversation.setBlockNumber(BlockConstants.Master_PriestAlest_NoItem);
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            priestAlestConversation.getTestByteOperations().add(testByteOperation);
        }
        priestAlestConversation.getTestByteOperations().add(new TestByteOperation(itemFlag, ByteOp.FLAG_GTEQ, 2)); // Mini Doll conversation has already happened
        doorObject.getObjectContainer().getObjects().add(priestAlestConversation);
    }

    /**
     * @param doorObject the base npc door object, to use as a positional reference
     */
    public static void addMrSlushfundDoors(ConversationDoor doorObject, int itemFlagAnchor) {
        ConversationDoor mrSlushfundConversation = new ConversationDoor(doorObject.getObjectContainer(), doorObject.getX(), doorObject.getY());
        mrSlushfundConversation.setDoorType(ConversationDoor.SingleConversation);
        mrSlushfundConversation.setBlockNumber(BlockConstants.Master_MrSlushfund_WaitingForTreasures);
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            mrSlushfundConversation.getTestByteOperations().add(testByteOperation);
        }
        mrSlushfundConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.MR_SLUSHFUND_CONVERSATION_PEPPER, ByteOp.FLAG_GT, 0)); // Pepper conversation has already happened
        mrSlushfundConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_TREASURES, ByteOp.FLAG_LTEQ, 1)); // Treasures not collected
        doorObject.getObjectContainer().getObjects().add(mrSlushfundConversation);

        mrSlushfundConversation = new ConversationDoor(doorObject.getObjectContainer(), doorObject.getX(), doorObject.getY());
        mrSlushfundConversation.setDoorType(ConversationDoor.SingleConversation);
        mrSlushfundConversation.setBlockNumber(BlockConstants.Master_MrSlushfund_Anchor);
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            mrSlushfundConversation.getTestByteOperations().add(testByteOperation);
        }
        mrSlushfundConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_TREASURES, ByteOp.FLAG_EQUALS, 2)); // Treasures collected
        mrSlushfundConversation.getTestByteOperations().add(new TestByteOperation(itemFlagAnchor, ByteOp.FLAG_LT, 2)); // Treasures item not collected
        doorObject.getObjectContainer().getObjects().add(mrSlushfundConversation);

        mrSlushfundConversation = new ConversationDoor(doorObject.getObjectContainer(), doorObject.getX(), doorObject.getY());
        mrSlushfundConversation.setDoorType(ConversationDoor.SingleConversation);
        mrSlushfundConversation.setBlockNumber(BlockConstants.Master_MrSlushfund_NeverComeBack);
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            mrSlushfundConversation.getTestByteOperations().add(testByteOperation);
        }
        mrSlushfundConversation.getTestByteOperations().add(new TestByteOperation(itemFlagAnchor, ByteOp.FLAG_GTEQ, 2)); // Treasures item collected
        mrSlushfundConversation.getTestByteOperations().add(new TestByteOperation(FlagConstants.MR_SLUSHFUND_CONVERSATION_PEPPER, ByteOp.FLAG_GT, 0)); // Pepper conversation has already happened
        doorObject.getObjectContainer().getObjects().add(mrSlushfundConversation);
    }
}
