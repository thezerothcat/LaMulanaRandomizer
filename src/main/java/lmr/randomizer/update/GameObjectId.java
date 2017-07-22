package lmr.randomizer.update;

/**
 * Created by thezerothcat on 7/21/2017.
 */
public class GameObjectId {
    private short chestArg; // If the item gets placed in a chest, the chest will need to use this as its arg 0.
    private int worldFlag; // This will need to get rotated with the chest.

    public GameObjectId(short chestArg, int worldFlag) {
        this.chestArg = chestArg;
        this.worldFlag = worldFlag;
    }

    public GameObjectId(String chestArgString, String worldFlagString) {
        this.chestArg = Short.parseShort(chestArgString);
        this.worldFlag = Integer.parseInt(worldFlagString, 16);
    }

    public short getChestArg() {
        return chestArg;
    }

    public int getWorldFlag() {
        return worldFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameObjectId that = (GameObjectId) o;

        if (chestArg != that.chestArg) return false;
        return worldFlag == that.worldFlag;
    }

    @Override
    public int hashCode() {
        int result = chestArg;
        result = 31 * result + worldFlag;
        return result;
    }
}
