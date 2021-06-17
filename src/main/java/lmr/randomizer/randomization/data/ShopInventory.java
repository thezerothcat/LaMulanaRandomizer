package lmr.randomizer.randomization.data;

public class ShopInventory {
    private String shopName;
    private String npcName;
    private ShopInventoryData item1;
    private ShopInventoryData item2;
    private ShopInventoryData item3;

    public ShopInventory(String shopName, String npcName) {
        this.shopName = shopName;
        this.npcName = npcName;
    }

    public ShopInventory(ShopInventory shopInventory) {
        this.shopName = shopInventory.getShopName();
        this.npcName = shopInventory.getNpcName();
        this.item1 = new ShopInventoryData(shopInventory.getItem1());
        this.item2 = new ShopInventoryData(shopInventory.getItem2());
        this.item3 = new ShopInventoryData(shopInventory.getItem3());
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getNpcName() {
        return npcName;
    }

    public ShopInventoryData getItem1() {
        return item1;
    }

    public void setItem1(ShopInventoryData item1) {
        this.item1 = item1;
    }

    public ShopInventoryData getItem2() {
        return item2;
    }

    public void setItem2(ShopInventoryData item2) {
        this.item2 = item2;
    }

    public ShopInventoryData getItem3() {
        return item3;
    }

    public void setItem3(ShopInventoryData item3) {
        this.item3 = item3;
    }

    public void setItem(int shopItemNumber, ShopInventoryData shopInventoryData) {
        if(shopItemNumber == 1) {
            item1 = shopInventoryData;
        }
        if(shopItemNumber == 2) {
            item2 = shopInventoryData;
        }
        if(shopItemNumber == 3) {
            item3 = shopInventoryData;
        }
    }

    public ShopInventoryData getShopInventoryData(int shopItemNumber) {
        if(shopItemNumber == 1) {
            return item1;
        }
        if(shopItemNumber == 2) {
            return item2;
        }
        if(shopItemNumber == 3) {
            return item3;
        }
        return null;
    }
}
