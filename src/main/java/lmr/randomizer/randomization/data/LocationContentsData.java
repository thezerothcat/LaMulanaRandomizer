package lmr.randomizer.randomization.data;

public class LocationContentsData {
    private String locationName;
    private String itemName;
    private short locationWorldFlag;
    private short itemInventoryArg;
    private short itemWorldFlag;
    private Short customItemGraphic;

    private Short newWorldFlag;
    private boolean isTrap;
    private boolean isExplodingChest;
    private boolean isCoinChest;
    private boolean isRemovedItem;

    public LocationContentsData(String locationName, String itemName) {
        this.itemName = itemName;
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getItemName() {
        return itemName;
    }

    public short getLocationWorldFlag() {
        return locationWorldFlag;
    }
    public void setLocationWorldFlag(short locationWorldFlag) {
        this.locationWorldFlag = locationWorldFlag;
    }

    public short getItemInventoryArg() {
        return itemInventoryArg;
    }
    public void setItemInventoryArg(short itemInventoryArg) {
        this.itemInventoryArg = itemInventoryArg;
    }

    public short getItemWorldFlag() {
        return itemWorldFlag;
    }
    public void setItemWorldFlag(int itemWorldFlag) {
        this.itemWorldFlag = (short)itemWorldFlag;
    }

    public Short getNewWorldFlag() {
        return newWorldFlag;
    }
    public void setNewWorldFlag(int newWorldFlag) {
        this.newWorldFlag = (short)newWorldFlag;
    }

    public boolean isTrap() {
        return isTrap;
    }
    public void setTrap(boolean trap) {
        isTrap = trap;
    }

    public boolean isExplodingChest() {
        return isExplodingChest;
    }
    public void setExplodingChest(boolean explodingChest) {
        isExplodingChest = explodingChest;
    }

    public boolean isCoinChest() {
        return isCoinChest;
    }
    public void setCoinChest(boolean coinChest) {
        isCoinChest = coinChest;
    }

    public boolean isRemovedItem() {
        return isRemovedItem;
    }
    public void setRemovedItem(boolean removedItem) {
        isRemovedItem = removedItem;
    }

    public Short getCustomItemGraphic() {
        return customItemGraphic;
    }
    public void setCustomItemGraphic(Short customItemGraphic) {
        this.customItemGraphic = customItemGraphic;
    }
}
