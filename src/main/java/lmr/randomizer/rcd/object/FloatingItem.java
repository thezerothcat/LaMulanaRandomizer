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
        setId((short)0x2f);
        setX(x);
        setY(y);
    }

    public void setInteraction(int interactableBehavior) {
        // 0 = spawn interactable 1 = interactable 30 frames after spawn 2 = spawned from a chest
        getArgs().set(0, (short)interactableBehavior);
    }

    public void setInventoryWord(int inventoryWord) {
        getArgs().set(1, (short)inventoryWord);
    }

    public void setRealItem(boolean realItem) {
        // if >0 add to inv, play sound, otherwise just sets flags
        getArgs().set(2, (short)(realItem ? 1 : 0));
    }
}
