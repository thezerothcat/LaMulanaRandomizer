package lmr.randomizer.rcd.object;

/**
 * 0x2f Item-Naked reversed; tested
 *     ptr 0    // spawning chest
 *     0 - 0 = spawn interactable 1 = interactable 30 frames after spawn 2 = spawned from a chest
 *     1 - inventory word
 *     2 - if >0 add to inv, play sound, otherwise just sets flags
 */
public class FloatingItem extends GameObject {
    public FloatingItem(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 3);
        setId(ObjectIdConstants.FloatingItem);
        setX(x);
        setY(y);
    }

    public FloatingItem(GameObject gameObject) {
        super(gameObject.getObjectContainer(), 3);
        setId(ObjectIdConstants.FloatingItem);
        setX(gameObject.getX());
        setY(gameObject.getY());
        addTests(gameObject.getTestByteOperations());
        addUpdates(gameObject.getWriteByteOperations());
        setInteraction(gameObject.getArgs().get(0));
        setInventoryWord(gameObject.getArgs().get(1));
        setRealItem(gameObject.getArgs().get(2) > 0);
    }

    public void setInteraction(int interactableBehavior) {
        // 0 = spawn interactable 1 = interactable 30 frames after spawn 2 = spawned from a chest
        getArgs().set(0, (short)interactableBehavior);
    }

    public short getInventoryWord() {
        return getArgs().get(1);
    }

    public void setInventoryWord(int inventoryWord) {
        getArgs().set(1, (short)inventoryWord);
    }

    public void setRealItem(boolean realItem) {
        // if >0 add to inv, play sound, otherwise just sets flags
        getArgs().set(2, (short)(realItem ? 1 : 0));
    }

    public Short getWorldFlag() {
        return getTestByteOperations().isEmpty() ? null : (short)getTestByteOperations().get(0).getIndex();
    }
}
