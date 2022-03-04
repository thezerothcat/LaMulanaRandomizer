package lmr.randomizer.rcd.object;

/**
 * 0x2c chest Reversed
 *     0 - item type, 1-10 are drop types, 11+ are inventory word - 11, 0 is empty
 *     1 - if drop then quantity
 *           // if item then item arg 2 (whether it's a real item)
 *     2 - if 0 then brown else blue
 *     3 - if >0, cursed
 *     4 - if 0 then flat curse damage else percentage
 *     5 - curse damage
 *     update 0    // if world[idx] == val then set chest to empty (creation and every frame)
 *                 // op not used
 *     update 1    // if world[idx] == val then set chest to ajar (creation and every frame)
 *                 // op not used
 *     update 2    // performed when chest is opened
 *     update 3    // if chest has item then performed on item pickup
 *                 // if chest has drop then performed on drop
 *                 // if chest is empty then performed when chest is opened
 */
public class Chest extends GameObject {
    public static final short COIN_CHEST = 0;
    public static final short ITEM_CHEST = 1;

    public Chest(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 6);
        setId(ObjectIdConstants.Chest);
        setX(x);
        setY(y);
    }

    public Chest(GameObject gameObject) {
        super(gameObject);
    }

    public void setDrops(int dropType, int quantity) {
        setDropType(dropType);
        setDropQuantity(quantity);
    }

    public int getDropType() {
        return getArgs().get(0);
    }

    public void setDropType(int dropType) {
        getArgs().set(0, (short)dropType);
    }

    public int getDropQuantity() {
        return getArgs().get(1);
    }

    public void setDropQuantity(int quantity) {
        getArgs().set(1, (short)quantity);
    }

    public void setChestGraphic(int chestGraphic) {
        getArgs().set(2, (short)chestGraphic);
    }

    public boolean isCursed() {
        return getArgs().get(3) > 0;
    }

    public void setCursed(boolean cursed) {
        getArgs().set(3, (short)(cursed ? 1 : 0));
    }

    public void setFlatCurseDamage(int damage) {
        setPercentDamage(false);
        setCurseDamage(damage);
    }

    public void setPercentCurseDamage(int damage) {
        setPercentDamage(true);
        setCurseDamage(damage);
    }

    public void setPercentDamage(boolean percentDamage) {
        getArgs().set(4, (short)(percentDamage ? 1 : 0));
    }

    public void setCurseDamage(int damage) {
        getArgs().set(5, (short)damage);
    }

    public void setEmptyCheck(WriteByteOperation writeByteOperation) {
        // update 0    // if world[idx] == val then set chest to empty (creation and every frame)
                        // op not used
        getWriteByteOperations().set(0, writeByteOperation);
    }

    /**
     * @return WriteByteOperation for the chest's puzzle flag
     */
    public WriteByteOperation getUnlockedCheck() {
        // update 1    // if world[idx] == val then set chest to ajar (creation and every frame)
        return getWriteByteOperations().get(1);
    }
    public void setUnlockedCheck(WriteByteOperation writeByteOperation) {
        // update 1    // if world[idx] == val then set chest to ajar (creation and every frame)
        getWriteByteOperations().set(1, writeByteOperation);
    }
    public void setUpdateWhenOpened(WriteByteOperation writeByteOperation) {
        // update 2    // performed when chest is opened
        getWriteByteOperations().set(2, writeByteOperation);
    }

    public void setUpdateWhenCollected(WriteByteOperation writeByteOperation) {
         // update 3    // if chest has item then performed on item pickup
                        // if chest has drop then performed on drop
                        // if chest is empty then performed when chest is opened
        getWriteByteOperations().set(3, writeByteOperation);
    }
}
