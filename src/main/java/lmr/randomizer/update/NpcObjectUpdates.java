package lmr.randomizer.update;

import lmr.randomizer.dat.AddObject;
import lmr.randomizer.rcd.object.*;

public final class NpcObjectUpdates {
    private NpcObjectUpdates() { }

    public static void updateDoor(GameObject doorObject, String npcAssigned) {
        // Args 0-2 and 5-6 seem to just be 0 for the conversations referenced so far
        if(doorObject != null) {
            doorObject.getArgs().set(3, (short)(isShop(npcAssigned) ? 1 : 0));
            doorObject.getArgs().set(4, getConversationArg(npcAssigned));

            // Handle special cases
            if("NPC: Yiegah Kungfu".equals(npcAssigned)) {
                doorObject.getTestByteOperations().add(new TestByteOperation(0x1f0, ByteOp.FLAG_NOT_EQUAL, 1));
                NpcObjectUpdates.addLittleBrotherScreenObjects(doorObject);
                AddObject.setLittleBrotherShopScreen(doorObject.getObjectContainer());
            }
        }
    }

    private static boolean isShop(String npcAssigned) {
        if("NPC: Yiegah Kungfu".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Sturdy Snake".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Arrogant Metagear".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mr. Fishman (Original)".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mr. Fishman (Alt)".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mechanical Efspi".equals(npcAssigned)) {
            return true;
        }
        return false;
    }

    private static short getConversationArg(String npcAssigned) {
        if("NPC: Yiegah Kungfu".equals(npcAssigned)) {
            return 185;
        }
        if("NPC: Arrogant Metagear".equals(npcAssigned)) {
            return 187;
        }
        if("NPC: Sturdy Snake".equals(npcAssigned)) {
            return 204;
        }
        if("NPC: Mr. Fishman (Original)".equals(npcAssigned)) {
            return 132;
        }
        if("NPC: Mr. Fishman (Alt)".equals(npcAssigned)) {
            return 133;
        }
        if("NPC: Mechanical Efspi".equals(npcAssigned)) {
            return 321;
        }
        if("NPC: Priest Laydoc".equals(npcAssigned)) {
            return 701;
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
}
