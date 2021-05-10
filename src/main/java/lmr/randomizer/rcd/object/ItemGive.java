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
        setId((short)0xb5);
        setX(x);
        setY(y);
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
