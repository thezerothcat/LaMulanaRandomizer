package lmr.randomizer.update;

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
                doorObject.getTestByteOperations().add(new TestByteOperation(0xad0, ByteOp.FLAG_EQUALS, 1)); // Flag for having talked to Xelpud to open Surface tents
            }
//            if("NPCL: Priest Zarnac".equals(npcDoorLocation)) {
//                doorObject.getWriteByteOperations().add(new WriteByteOperation(0x017, ByteOp.ASSIGN_FLAG, 1));
//            }
            if("NPCL: Mr. Fishman (Original)".equals(npcDoorLocation)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(0x197, ByteOp.FLAG_GTEQ, 2));
            }
            if("NPCL: Mr. Fishman (Alt)".equals(npcDoorLocation)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(0x197, ByteOp.FLAG_EQUALS, 3));
            }
            if("NPCL: Mud Man Qubert".equals(npcDoorLocation)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(0x23a, ByteOp.FLAG_EQUALS, 4));
            }
            if("NPCL: Naramura".equals(npcDoorLocation) || "NPCL: duplex".equals(npcDoorLocation) || "NPCL: Samieru".equals(npcDoorLocation)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(0x348, ByteOp.FLAG_EQUALS, 1)); // Flag automatically set when dev room software combo is activated.
            }

            // Handle special cases for door contents
            if("NPC: Yiegah Kungfu".equals(npcAssigned)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(0x1f0, ByteOp.FLAG_NOT_EQUAL, 1));
                addLittleBrotherScreenObjects(doorObject);
                AddObject.setLittleBrotherShopScreen(doorObject.getObjectContainer());
            }
            if("NPC: Philosopher Giltoriyo".equals(npcAssigned)) {
                addPhilosopherStoneDoor(doorObject); // Do this before adding tests, so we can carry over any tests based on the location.
                doorObject.getTestByteOperations().add(new TestByteOperation(0x0b5, ByteOp.FLAG_EQUALS, 2));
                doorObject.getWriteByteOperations().add(new WriteByteOperation(0x078, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning Endless Corridor Philosopher ladder
                doorObject.getWriteByteOperations().add(new WriteByteOperation(0xaeb, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning MARDUK mantra, changed in randomizer from 0x12b
            }
            if("NPC: Philosopher Alsedana".equals(npcAssigned)) {
                addPhilosopherStoneDoor(doorObject); // Do this before adding tests, so we can carry over any tests based on the location.
                doorObject.getTestByteOperations().add(new TestByteOperation(0x0b5, ByteOp.FLAG_EQUALS, 2));
                doorObject.getWriteByteOperations().add(new WriteByteOperation(0x07a, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning Nuwa's pyramid Philosopher ladder
            }
            if("NPC: Philosopher Samaranta".equals(npcAssigned)) {
                addPhilosopherStoneDoor(doorObject); // Do this before adding tests, so we can carry over any tests based on the location.
                doorObject.getTestByteOperations().add(new TestByteOperation(0x0b5, ByteOp.FLAG_EQUALS, 2));
                doorObject.getWriteByteOperations().add(new WriteByteOperation(0x08b, ByteOp.ASSIGN_FLAG, 1)); // Trigger for spawning Scales of the Heart puzzle room Philosopher ladder
            }
            if("NPC: Naramura".equals(npcDoorLocation)) {
                doorObject.getWriteByteOperations().add(new WriteByteOperation(0x388, ByteOp.ASSIGN_FLAG, 1)); // Flag indicating Naramura has been spoken to
            }
            if("NPC: duplex".equals(npcDoorLocation)) {
                doorObject.getWriteByteOperations().add(new WriteByteOperation(0x389, ByteOp.ASSIGN_FLAG, 1)); // Flag indicating duplex has been spoken to
            }
            if("NPC: Samieru".equals(npcDoorLocation)) {
                doorObject.getWriteByteOperations().add(new WriteByteOperation(0x38a, ByteOp.ASSIGN_FLAG, 1)); // Flag indicating Samieru has been spoken to
            }

            // Add test to close shop during escape sequence
            doorObject.getTestByteOperations().add(new TestByteOperation(0x0fe, ByteOp.FLAG_NOT_EQUAL, 3));
        }
    }

    private static boolean isShop(String npcAssigned) {
        if("NPC: Nebur (Original)".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Nebur (Alt)".equals(npcAssigned)) {
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
        if("NPC: Nebur (Original)".equals(npcAssigned)) {
            return 34;
        }
        if("NPC: Nebur (Alt)".equals(npcAssigned)) {
            return 490;
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
        bigBrotherProgressTimer.getTestByteOperations().add(new TestByteOperation(0x1ea, ByteOp.FLAG_GTEQ, 3));
        bigBrotherProgressTimer.getTestByteOperations().add(new TestByteOperation(0x1f0, ByteOp.FLAG_EQUALS, 0));
        bigBrotherProgressTimer.getWriteByteOperations().add(new WriteByteOperation(0x1f0, ByteOp.ASSIGN_FLAG, 1));
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
        bigBrotherSoundEffect.getTestByteOperations().add(new TestByteOperation(0x0a7, ByteOp.FLAG_EQUALS, 2));
        bigBrotherSoundEffect.getTestByteOperations().add(new TestByteOperation(0x1f0, ByteOp.FLAG_EQUALS, 2));
        bigBrotherSoundEffect.getTestByteOperations().add(new TestByteOperation(0x00b, ByteOp.FLAG_EQUALS, 1));
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
        bigBrotherNotificationConversation.getTestByteOperations().add(new TestByteOperation(0x1f0, ByteOp.FLAG_EQUALS, 1));
        bigBrotherNotificationConversation.getTestByteOperations().add(new TestByteOperation(0x0fe, ByteOp.FLAG_NOT_EQUAL, 3));
        bigBrotherNotificationConversation.getWriteByteOperations().add(new WriteByteOperation(0x1f0, ByteOp.ASSIGN_FLAG, 2));
        bigBrotherNotificationConversation.getWriteByteOperations().add(new WriteByteOperation(0x00b, ByteOp.ASSIGN_FLAG, 1));
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
        philosopherStoneConversation.getTestByteOperations().add(new TestByteOperation(0x0b5, ByteOp.FLAG_LTEQ, 1)); // Using <= 1 rather than == 0 in case of a chest check setting to 1
        philosopherStoneConversation.getTestByteOperations().add(new TestByteOperation(0x0fe, ByteOp.FLAG_NOT_EQUAL, 3));
        doorObject.getObjectContainer().getObjects().add(philosopherStoneConversation);
    }
}
