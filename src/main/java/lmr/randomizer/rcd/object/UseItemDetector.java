package lmr.randomizer.rcd.object;

/**
 * 0x9c - detection-useitem (Object) (use, not held)
 * 0- (2-32) x-size?
 * 1- (4-24) y-size?
 * 2- (0-16)
 * 3- (0-1)
 *
 * 0x9c detection-UseItem tested; reversed
 *     // Has a six frame delay :/
 *     0 - dX
 *     1 - dY
 *     2 - Item
 *     3 - OnlyWhenGrounded
 */
public class UseItemDetector extends GameObject {
    public UseItemDetector(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 4);
        setId(ObjectIdConstants.UseItemDetector);
        setX(x);
        setY(y);
    }

    public void setWidth(int gTileWidth) {
        getArgs().set(0, (short)gTileWidth);
    }

    public void setHeight(int gTileHeight) {
        getArgs().set(1, (short)gTileHeight);
    }

    public void setItem(int item) {
        getArgs().set(2, (short)item);
    }

    public void setItem(String itemName) {
        if("Hand Scanner".equals(itemName)) {
            setItem(0);
        }
        if("Djed Pillar".equals(itemName)) {
            setItem(1);
        }
        if("Mini Doll".equals(itemName)) {
            setItem(2);
        }
        if("Magatama Jewel".equals(itemName)) {
            setItem(3);
        }
        if("Cog of the Soul".equals(itemName)) {
            setItem(4);
        }
        if("Lamp of Time".equals(itemName)) {
            setItem(5);
        }
        if("Pochette Key".equals(itemName)) {
            setItem(6);
        }
        if("Dragon Bone".equals(itemName)) {
            setItem(7);
        }
        if("Crystal Skull".equals(itemName)) {
            setItem(8);
        }
        if("Vessel".equals(itemName)) {
            setItem(9);
        }
        if("Pepper".equals(itemName)) {
            setItem(10);
        }
        if("Woman Statue".equals(itemName)) {
            setItem(11);
        }
        if("Key of Eternity".equals(itemName)) {
            setItem(12);
        }
        // todo: figure out what this is - Talisman? Diary? Mulana Talisman?
        if("Serpent Staff".equals(itemName)) {
            setItem(14);
        }
        if("Maternity Statue".equals(itemName)) {
            setItem(15);
        }
    }

    public void setGroundOnly(boolean groundOnly) {
        getArgs().set(3, (short)(groundOnly ? 1 : 0));
    }
}
