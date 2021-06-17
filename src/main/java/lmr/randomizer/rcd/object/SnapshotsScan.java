package lmr.randomizer.rcd.object;

/**
 * 0xc3 Snapshots Detection
 *     0 - LocationX
 *     1 - LocationY
 *     2 - TextCard
 *     3 - ItemWord
 */
public class SnapshotsScan extends GameObject {

    public SnapshotsScan(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 4);
        setId(ObjectIdConstants.SnapshotsScan);
        setX(-1);
        setY(-1);
    }

    public SnapshotsScan(GameObject gameObject) {
        super(gameObject.getObjectContainer(), 4);
        setId(ObjectIdConstants.SnapshotsScan);
        setX(gameObject.getX());
        setY(gameObject.getY());
        addTests(gameObject.getTestByteOperations());
        addUpdates(gameObject.getWriteByteOperations());
        setLocation(gameObject.getArgs().get(0), gameObject.getArgs().get(1));
        setTextCard(gameObject.getArgs().get(2));
        setInventoryWord(gameObject.getArgs().get(3));
    }

    public void setLocation(int x, int y) {
        setLocationX(x);
        setLocationY(y);
    }

    public void setLocationX(int x) {
        getArgs().set(0, (short)x);
    }

    public void setLocationY(int y) {
        getArgs().set(1, (short)y);
    }

    public void setTextCard(int textCard) {
        getArgs().set(2, (short)textCard);
    }

    public short getInventoryWord() {
        return getArgs().get(3);
    }
    public void setInventoryWord(int inventoryWord) {
        getArgs().set(3, (short)inventoryWord);
    }
}
