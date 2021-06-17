package lmr.randomizer.rcd.object;

/**
 * 0xb5 Instant Item
 *     //performs all updates on collection
 *     0 - Word
 *     1 - Width gtile
 *     2 - Height gtile
 *     3 - SE select
 */
public class ItemGive extends GameObject {
    public ItemGive(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 4);
        setId(ObjectIdConstants.ItemGive);
        setX(x);
        setY(y);
    }

    public ItemGive(GameObject gameObject) {
        super(gameObject.getObjectContainer(), 4);
        setId(ObjectIdConstants.ItemGive);
        setX(gameObject.getX());
        setY(gameObject.getY());
        addTests(gameObject.getTestByteOperations());
        addUpdates(gameObject.getWriteByteOperations());
        setInventoryWord(gameObject.getArgs().get(0));
        setWidth(gameObject.getArgs().get(1));
        setHeight(gameObject.getArgs().get(2));
        setSoundEffect(gameObject.getArgs().get(3));
    }

    public void setInventoryWord(int inventoryWord) {
        getArgs().set(0, (short)inventoryWord);
    }

    public void setWidth(int gTileWidth) {
        getArgs().set(1, (short)gTileWidth);
    }

    public void setHeight(int gTileHeight) {
        getArgs().set(2, (short)gTileHeight);
    }

    public void setSoundEffect(int soundEffect) {
        getArgs().set(3, (short)soundEffect);
    }
}
