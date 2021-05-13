package lmr.randomizer.update;

import lmr.randomizer.FlagConstants;
import lmr.randomizer.dat.AddObject;
import lmr.randomizer.rcd.object.*;

public final class NpcObjectUpdates {
    private NpcObjectUpdates() { }

    public static void updateDoor(GameObject doorObject, String npcDoorLocation, String npcAssigned) {
        // Args 0-2 and 5-6 seem to just be 0 for the conversations referenced so far
        if(doorObject != null) {
            doorObject.getArgs().set(3, (short)(isShop(npcAssigned) ? 1 : 0));
            doorObject.getArgs().set(4, getConversationArg(npcAssigned));

            doorObject.getTestByteOperations().clear();
            doorObject.getWriteByteOperations().clear();

            // Handle special cases for location of door
            if("NPCL: Sidro".equals(npcDoorLocation) || "NPCL: Modro".equals(npcDoorLocation) || "NPCL: Hiner".equals(npcDoorLocation) || "NPCL: Moger".equals(npcDoorLocation)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.XELPUD_CONVERSATION_INTRO, ByteOp.FLAG_EQUALS, 1)); // Flag for having talked to Xelpud to open Surface tents
            }
//            if("NPCL: Priest Zarnac".equals(npcDoorLocation)) {
//                doorObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_17, ByteOp.ASSIGN_FLAG, 1));
//            }
            if("NPCL: Mr. Fishman (Original)".equals(npcDoorLocation)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.FISH_SHOP_UNLOCKS, ByteOp.FLAG_GTEQ, 2));
            }
            if("NPCL: Mr. Fishman (Alt)".equals(npcDoorLocation)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.FISH_SHOP_UNLOCKS, ByteOp.FLAG_EQUALS, 3));
            }
            if("NPCL: Mud Man Qubert".equals(npcDoorLocation)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.COG_MUDMEN_STATE, ByteOp.FLAG_EQUALS, 4));
            }
            if("NPCL: Naramura".equals(npcDoorLocation) || "NPCL: duplex".equals(npcDoorLocation) || "NPCL: Samieru".equals(npcDoorLocation)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.DEV_ROOM_COMBO, ByteOp.FLAG_EQUALS, 1)); // Flag automatically set when dev room software combo is activated.
            }

            // Handle special cases for door contents
            if("NPC: Yiegah Kungfu".equals(npcAssigned)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.BIG_BROTHER_UNLOCKED, ByteOp.FLAG_NOT_EQUAL, 1));
                addLittleBrotherScreenObjects(doorObject);
                AddObject.setLittleBrotherShopScreen(doorObject.getObjectContainer());
            }
            if("NPC: Philosopher Giltoriyo".equals(npcAssigned)) {
                addPhilosopherStoneDoor(doorObject); // Do this before adding tests, so we can carry over any tests based on the location.
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_PHILOSOPHERS_OCARINA, ByteOp.FLAG_EQUALS, 2));
                doorObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.GILTORIYO_LADDER, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning Endless Corridor Philosopher ladder
                doorObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.MANTRAS_UNLOCKED, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning MARDUK mantra, changed in randomizer from 0x12b
            }
            if("NPC: Philosopher Alsedana".equals(npcAssigned)) {
                addPhilosopherStoneDoor(doorObject); // Do this before adding tests, so we can carry over any tests based on the location.
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_PHILOSOPHERS_OCARINA, ByteOp.FLAG_EQUALS, 2));
                doorObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.ALSEDANA_LADDER, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning Nuwa's pyramid Philosopher ladder
            }
            if("NPC: Philosopher Samaranta".equals(npcAssigned)) {
                addPhilosopherStoneDoor(doorObject); // Do this before adding tests, so we can carry over any tests based on the location.
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_PHILOSOPHERS_OCARINA, ByteOp.FLAG_EQUALS, 2));
                doorObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SAMARANTA_LADDER, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning Scales of the Heart puzzle room Philosopher ladder
            }
            if("NPC: Philosopher Fobos".equals(npcAssigned)) {
                addFobosDoors(doorObject); // Do this before adding tests, so we can carry over any tests based on the location.
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_PHILOSOPHERS_OCARINA, ByteOp.FLAG_EQUALS, 2));
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.MEDICINE_SOLVED, ByteOp.FLAG_EQUALS, 1)); // Medicine puzzle solved
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.FOBOS_LADDER, ByteOp.FLAG_NOT_EQUAL, 0)); // Fobos spoken to
            }
            if("NPC: The Fairy Queen".equals(npcAssigned)) {
                addFairyQueenDoors(doorObject); // Do this before adding tests, so we can carry over any tests based on the location.
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.FLAG_LTEQ, 1)); // Fairy Queen conversation progress
                doorObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_ISIS_PENDANT, ByteOp.FLAG_EQUALS, 2)); // Isis' Pendant collected
                doorObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.ASSIGN_FLAG, 2)); // Fairy Queen conversation progress
                doorObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.FAIRY_POINTS_ACTIVE, ByteOp.ASSIGN_FLAG, 1)); // Fairy points active
            }
            if("NPC: Naramura".equals(npcDoorLocation)) {
                doorObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.NARAMURA_SPOKEN, ByteOp.ASSIGN_FLAG, 1)); // Flag indicating Naramura has been spoken to
            }
            if("NPC: duplex".equals(npcDoorLocation)) {
                doorObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.DUPLEX_SPOKEN, ByteOp.ASSIGN_FLAG, 1)); // Flag indicating duplex has been spoken to
            }
            if("NPC: Samieru".equals(npcDoorLocation)) {
                doorObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SAMIERU_SPOKEN, ByteOp.ASSIGN_FLAG, 1)); // Flag indicating Samieru has been spoken to
            }
        }
    }

    private static boolean isShop(String npcAssigned) {
        if("NPC: Nebur".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Sidro".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Modro".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Penadvent of ghost".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Greedy Charlie".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Shalom III".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Usas VI".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Kingvalley I".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mr. Fishman (Original)".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mr. Fishman (Alt)".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Operator Combaker".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Yiegah Kungfu".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Arrogant Metagear".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Arrogant Sturdy Snake".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Yiear Kungfu".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Affected Knimare".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mover Athleland".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Giant Mopiran".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Kingvalley II".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Energetic Belmont".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mechanical Efspi".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mud Man Qubert".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Hot-blooded Nemesistwo".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Tailor Dracuet".equals(npcAssigned)) {
            return true;
        }
        return false;
    }

    private static short getConversationArg(String npcAssigned) {
        if("NPC: Nebur".equals(npcAssigned)) {
            return 34; // 490 for Alt
        }
        if("NPC: Sidro".equals(npcAssigned)) {
            return 35;
        }
        if("NPC: Modro".equals(npcAssigned)) {
            return 36;
        }
        if("NPC: Penadvent of ghost".equals(npcAssigned)) {
            return 39;
        }
        if("NPC: Greedy Charlie".equals(npcAssigned)) {
            return 74;
        }
        if("NPC: Shalom III".equals(npcAssigned)) {
            return 100;
        }
        if("NPC: Usas VI".equals(npcAssigned)) {
            return 102;
        }
        if("NPC: Kingvalley I".equals(npcAssigned)) {
            return 103;
        }
        if("NPC: Mr. Fishman (Original)".equals(npcAssigned)) {
            return 132;
        }
        if("NPC: Mr. Fishman (Alt)".equals(npcAssigned)) {
            return 133;
        }
        if("NPC: Operator Combaker".equals(npcAssigned)) {
            return 167;
        }
        if("NPC: Yiegah Kungfu".equals(npcAssigned)) {
            return 185;
        }
        if("NPC: Arrogant Metagear".equals(npcAssigned)) {
            return 187;
        }
        if("NPC: Arrogant Sturdy Snake".equals(npcAssigned)) {
            return 204;
        }
        if("NPC: Yiear Kungfu".equals(npcAssigned)) {
            return 205;
        }
        if("NPC: Affected Knimare".equals(npcAssigned)) {
            return 220;
        }
        if("NPC: Mover Athleland".equals(npcAssigned)) {
            return 244;
        }
        if("NPC: Giant Mopiran".equals(npcAssigned)) {
            return 272;
        }
        if("NPC: Kingvalley II".equals(npcAssigned)) {
            return 290;
        }
        if("NPC: Energetic Belmont".equals(npcAssigned)) {
            return 303;
        }
        if("NPC: Mechanical Efspi".equals(npcAssigned)) {
            return 321;
        }
        if("NPC: Mud Man Qubert".equals(npcAssigned)) {
            return 337;
        }
        if("NPC: Hot-blooded Nemesistwo".equals(npcAssigned)) {
            return 470;
        }
        if("NPC: Hiner".equals(npcAssigned)) {
            return 671;
        }
        if("NPC: Moger".equals(npcAssigned)) {
            return 672;
        }
        if("NPC: Former Mekuri Master".equals(npcAssigned)) {
            return 673;
        }
        if("NPC: Priest Zarnac".equals(npcAssigned)) {
            return 674;
        }
        if("NPC: Priest Xanado".equals(npcAssigned)) {
            return 675;
        }
        if("NPC: Philosopher Giltoriyo".equals(npcAssigned)) {
            return 677;
        }
        if("NPC: Priest Hidlyda".equals(npcAssigned)) {
            return 678;
        }
        if("NPC: Priest Romancis".equals(npcAssigned)) {
            return 679;
        }
        if("NPC: Priest Aramo".equals(npcAssigned)) {
            return 680;
        }
        if("NPC: Priest Triton".equals(npcAssigned)) {
            return 681;
        }
        if("NPC: Priest Jaguarfiv".equals(npcAssigned)) {
            return 683;
        }
        if("NPC: The Fairy Queen".equals(npcAssigned)) {
            return 686;
        }
        if("NPC: Mr. Slushfund".equals(npcAssigned)) {
            return 689;
        }
        if("NPC: Priest Alest".equals(npcAssigned)) {
            return 693;
        }
        if("NPC: Stray fairy".equals(npcAssigned)) {
            return 694;
        }
        if("NPC: Giant Thexde".equals(npcAssigned)) {
            return 696;
        }
        if("NPC: Philosopher Alsedana".equals(npcAssigned)) {
            return 698;
        }
        if("NPC: Philosopher Samaranta".equals(npcAssigned)) {
            return 700;
        }
        if("NPC: Priest Laydoc".equals(npcAssigned)) {
            return 701;
        }
        if("NPC: Priest Ashgine".equals(npcAssigned)) {
            return 702;
        }
        if("NPC: Philosopher Fobos".equals(npcAssigned)) {
            return 705;
        }
        if("NPC: 8bit Elder".equals(npcAssigned)) {
            return 706;
        }
        if("NPC: duplex".equals(npcAssigned)) {
            return 707;
        }
        if("NPC: Samieru".equals(npcAssigned)) {
            return 708;
        }
        if("NPC: Naramura".equals(npcAssigned)) {
            return 709;
        }
        if("NPC: 8bit Fairy".equals(npcAssigned)) {
            return 710;
        }
        if("NPC: Priest Madomono".equals(npcAssigned)) {
            return 718;
        }
        if("NPC: Priest Gailious".equals(npcAssigned)) {
            return 723;
        }
        if("NPC: Tailor Dracuet".equals(npcAssigned)) {
            return 1008;
        }
        return 0;
    }

    /**
     * @param doorObject the base npc door object, to use as a positional reference
     */
    public static void addLittleBrotherScreenObjects(GameObject doorObject) {
        Screen screen = (Screen)doorObject.getObjectContainer();
        GameObject bigBrotherProgressTimer = new GameObject(screen);
        bigBrotherProgressTimer.setId((short)0x0b);
        bigBrotherProgressTimer.getArgs().add((short)0);
        bigBrotherProgressTimer.getArgs().add((short)0);
        bigBrotherProgressTimer.setX(-1);
        bigBrotherProgressTimer.setY(-1);
        bigBrotherProgressTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.LITTLE_BROTHER_PURCHASES, ByteOp.FLAG_GTEQ, 3));
        bigBrotherProgressTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.BIG_BROTHER_UNLOCKED, ByteOp.FLAG_EQUALS, 0));
        bigBrotherProgressTimer.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.BIG_BROTHER_UNLOCKED, ByteOp.ASSIGN_FLAG, 1));
        screen.getObjects().add(0, bigBrotherProgressTimer);

        GameObject bigBrotherSoundEffect = new GameObject(screen);
        bigBrotherSoundEffect.setId((short)0x9b);
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
        bigBrotherNotificationConversation.setId((short)0xa0);
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
    }

    /**
     * @param doorObject the base npc door object, to use as a positional reference
     */
    public static void addPhilosopherStoneDoor(GameObject doorObject) {
        GameObject philosopherStoneConversation = new GameObject(doorObject.getObjectContainer());
        philosopherStoneConversation.setId((short)0xa0);
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
        fobosConversation.setId((short)0xa0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)704);
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
        fobosConversation.setId((short)0xa0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)0);
        fobosConversation.getArgs().add((short)704);
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
        GameObject fairyQueenConveration = new GameObject(doorObject.getObjectContainer());
        fairyQueenConveration.setId((short)0xa0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)685);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.setX(doorObject.getX());
        fairyQueenConveration.setY(doorObject.getY());
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            fairyQueenConveration.getTestByteOperations().add(testByteOperation);
        }
        fairyQueenConveration.getTestByteOperations().add(new TestByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.FLAG_LTEQ, 1)); // Fairy Queen conversation progress
        fairyQueenConveration.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_ISIS_PENDANT, ByteOp.FLAG_LTEQ, 1)); // Isis' Pendant not found
        doorObject.getObjectContainer().getObjects().add(fairyQueenConveration);

        fairyQueenConveration = new GameObject(doorObject.getObjectContainer());
        fairyQueenConveration.setId((short)0xa0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)687);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.setX(doorObject.getX());
        fairyQueenConveration.setY(doorObject.getY());
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            fairyQueenConveration.getTestByteOperations().add(testByteOperation);
        }
        fairyQueenConveration.getTestByteOperations().add(new TestByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.FLAG_EQUALS, 2)); // Fairy Queen conversation progress; fairy points unlocked
        fairyQueenConveration.getTestByteOperations().add(new TestByteOperation(FlagConstants.SHRINE_FAIRY_BLOCK, ByteOp.FLAG_EQUALS, 0)); // Fairy block in True Shrine hasn't spawned yet
        doorObject.getObjectContainer().getObjects().add(fairyQueenConveration);

        fairyQueenConveration = new GameObject(doorObject.getObjectContainer());
        fairyQueenConveration.setId((short)0xa0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)688);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.setX(doorObject.getX());
        fairyQueenConveration.setY(doorObject.getY());
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            fairyQueenConveration.getTestByteOperations().add(testByteOperation);
        }
        fairyQueenConveration.getTestByteOperations().add(new TestByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.FLAG_EQUALS, 2)); // Fairy Queen conversation progress; fairy points unlocked
        fairyQueenConveration.getTestByteOperations().add(new TestByteOperation(FlagConstants.SHRINE_FAIRY_BLOCK, ByteOp.FLAG_EQUALS, 1)); // Fairy block in True Shrine has spawned
        fairyQueenConveration.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SHRINE_FAIRY_BLOCK, ByteOp.ASSIGN_FLAG, 2)); // Fairy block in True Shrine will be removed when you go there
        doorObject.getObjectContainer().getObjects().add(fairyQueenConveration);

        fairyQueenConveration = new GameObject(doorObject.getObjectContainer());
        fairyQueenConveration.setId((short)0xa0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)985);
        fairyQueenConveration.getArgs().add((short)0);
        fairyQueenConveration.getArgs().add((short)1);
        fairyQueenConveration.setX(doorObject.getX());
        fairyQueenConveration.setY(doorObject.getY());
        for(TestByteOperation testByteOperation : doorObject.getTestByteOperations()) {
            fairyQueenConveration.getTestByteOperations().add(testByteOperation);
        }
        fairyQueenConveration.getTestByteOperations().add(new TestByteOperation(FlagConstants.SHRINE_FAIRY_BLOCK, ByteOp.FLAG_EQUALS, 2)); // Fairy block in True Shrine will be removed when you go there (have had the previous conversation)
        doorObject.getObjectContainer().getObjects().add(fairyQueenConveration);
    }
}
