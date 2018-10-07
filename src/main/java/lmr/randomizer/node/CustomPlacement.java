package lmr.randomizer.node;

public class CustomPlacement {
    private String location; // if null, contents should be removed; name can be either item or shop inventory
    private String contents; // what to put in the location
    private String itemGraphic; // ability to customize graphic for traps

    private Short shopPrice;
    private Short shopCount;

    private boolean curseChest;
    private boolean removeItem;
    private boolean removeLogic;
    private boolean startingWeapon;
    private boolean startingItem;

    public CustomPlacement(String nodeName) {
        this.location = nodeName;
        this.removeLogic = true;
    }

    public CustomPlacement(String location, String contents, Short shopPrice, Short shopCount) {
        this.location = location;
        this.contents = contents;
        this.shopPrice = shopPrice;
        this.shopCount = shopCount;
    }

    public CustomPlacement(String location, String contents, String itemGraphic, boolean curseChest) {
        this.location = location;
        this.contents = contents;
        this.itemGraphic = itemGraphic;
        this.curseChest = curseChest;
    }

    public CustomPlacement(String contents, boolean removeItem, boolean startingWeaponTrueStartingItemFalse) {
        this.contents = contents;
        if(removeItem) {
            this.removeItem = true;
        }
        else if(startingWeaponTrueStartingItemFalse) {
            this.startingWeapon = true;
        }
        else {
            this.startingItem = true;
        }
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

    public boolean isRemoveLogic() {
        return removeLogic;
    }

    public void setRemoveLogic(boolean removeLogic) {
        this.removeLogic = removeLogic;
    }

    public boolean isStartingWeapon() {
        return startingWeapon;
    }

    public void setStartingWeapon(boolean startingWeapon) {
        this.startingWeapon = startingWeapon;
    }

    public boolean isStartingItem() {
        return startingItem;
    }

    public void setStartingItem(boolean startingItem) {
        this.startingItem = startingItem;
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
