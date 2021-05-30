package lmr.randomizer.rcd.object;

/**
 * 0xa0 args: Partial Reversed; Tested
 *     0 -
 *     1 -
 *     2 -
 *     3 - conv-type
 *     4 - block
 *     5 -
 *     6 - Disallow Music Change
 */
public class ConversationDoor extends GameObject {
    public static int SingleConversation = 0;
    public static int Shop = 1;
    public static int ConversationTree = 2;

    public ConversationDoor(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 7);
        setId(ObjectIdConstants.ConversationDoor);
        setX(x);
        setY(y);
    }

    public void setShopDefaults() {
        setArg0(0);
        setArg1(0);
        setArg2(0);
        setDoorType(Shop);
        setBlockNumber(36);
        setArg5(0);
        setDisallowMusicChange(false);
    }

    public void setArg0(int arg0) {
        getArgs().set(0, (short)arg0);
    }

    public void setArg1(int arg1) {
        getArgs().set(1, (short)arg1);
    }

    public void setArg2(int arg2) {
        getArgs().set(2, (short)arg2);
    }

    public void setDoorType(int doorType) {
        getArgs().set(3, (short)doorType);
    }

    public void setBlockNumber(int blockNumber) {
        getArgs().set(4, (short)blockNumber);
    }

    public void setArg5(int arg5) {
        getArgs().set(5, (short)arg5);
    }

    public void setDisallowMusicChange(boolean disallowMusicChange) {
        getArgs().set(6, (short)(disallowMusicChange ? 1 : 0));
    }
}
