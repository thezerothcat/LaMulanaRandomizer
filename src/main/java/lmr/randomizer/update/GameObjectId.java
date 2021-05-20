package lmr.randomizer.update;

/**
 * Created by thezerothcat on 7/21/2017.
 */
public class GameObjectId {
    private short inventoryArg;
    private int worldFlag; // This will need to get rotated with the chest.

    public GameObjectId(short inventoryArg, int worldFlag) {
        this.inventoryArg = inventoryArg;
        this.worldFlag = worldFlag;
    }

    public GameObjectId(String chestArgString, String worldFlagString) {
        this.inventoryArg = Short.parseShort(chestArgString);
        this.worldFlag = Integer.parseInt(worldFlagString, 16);
    }

    public short getInventoryArg() {
        return inventoryArg;
    }

    public int getWorldFlag() {
        return worldFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameObjectId that = (GameObjectId) o;

        if (inventoryArg != that.inventoryArg) return false;
        return worldFlag == that.worldFlag;
    }

    @Override
    public int hashCode() {
        int result = inventoryArg;
        result = 31 * result + worldFlag;
        return result;
    }

    @Override
    public String toString() {
        return "GameObjectId{" +
                "inventoryArg=" + inventoryArg +
                ", worldFlag=" + worldFlag +
                '}';
    }
}
