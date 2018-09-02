package lmr.randomizer.node;

public class CustomPlacement {
    private String location; // if null, contents should be removed; name can be either item or shop inventory
    private String contents; // what to put in the location
    private String itemGraphic; // ability to customize graphic for traps

    private Short shopPrice;
    private Short shopCount;

    private boolean curseChest;
    private boolean removeItem;

    public CustomPlacement(String location, String contents, Short shopPrice, Short shopCount) {
        this.location = location;
        this.contents = contents;
        this.shopPrice = shopPrice;
        this.shopCount = shopCount;
    }

    public CustomPlacement(String location, String contents, String itemGraphic, boolean removeItem, boolean curseChest) {
        this.location = location;
        this.contents = contents;
        this.itemGraphic = itemGraphic;
        this.removeItem = removeItem;
        this.curseChest = curseChest;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getItemGraphic() {
        return itemGraphic;
    }

    public void setItemGraphic(String itemGraphic) {
        this.itemGraphic = itemGraphic;
    }

    public boolean isCurseChest() {
        return curseChest;
    }

    public void setCurseChest(boolean curseChest) {
        this.curseChest = curseChest;
    }

    public boolean isRemoveItem() {
        return removeItem;
    }

    public void setRemoveItem(boolean removeItem) {
        this.removeItem = removeItem;
    }

    public Short getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(Short shopPrice) {
        this.shopPrice = shopPrice;
    }

    public Short getShopCount() {
        return shopCount;
    }

    public void setShopCount(Short shopCount) {
        this.shopCount = shopCount;
    }
}
