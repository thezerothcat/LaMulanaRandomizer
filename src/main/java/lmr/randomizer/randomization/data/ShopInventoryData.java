package lmr.randomizer.randomization.data;

import lmr.randomizer.DataFromFile;

public class ShopInventoryData {
    private short inventoryArg;
    private short worldFlag;
    private ItemPriceCount itemPriceCount;
    private Integer customTextNumber;

    public ShopInventoryData() {
    }

    public ShopInventoryData(ShopInventoryData shopInventoryData) {
        worldFlag = shopInventoryData.getWorldFlag();
        inventoryArg = shopInventoryData.getInventoryArg();
        itemPriceCount = new ItemPriceCount(shopInventoryData.getItemPriceCount());
        customTextNumber = shopInventoryData.getCustomTextNumber();
    }

    public ShopInventoryData(String itemName, ItemPriceCount itemPriceCount) {
        this.inventoryArg = getInventoryArg(itemName);
        this.worldFlag = getWorldFlag(itemName);
        this.itemPriceCount = itemPriceCount;
    }

    public short getInventoryArg() {
        return inventoryArg;
    }

    public void setInventoryArg(short inventoryArg) {
        this.inventoryArg = inventoryArg;
    }

    public short getWorldFlag() {
        return worldFlag;
    }

    public void setWorldFlag(short worldFlag) {
        this.worldFlag = worldFlag;
    }

    public ItemPriceCount getItemPriceCount() {
        return itemPriceCount;
    }

    public void setItemPriceCount(ItemPriceCount itemPriceCount) {
        this.itemPriceCount = itemPriceCount;
    }

    public Integer getCustomTextNumber() {
        return customTextNumber;
    }

    public void setCustomTextNumber(int customTextNumber) {
        this.customTextNumber = customTextNumber;
    }

    public static short getInventoryArg(String itemName) {
        if("Weights".equals(itemName)) {
            return 105;
        }
        if("Coin".equals(itemName)) {
            return 106;
        }
        if("Shuriken Ammo".equals(itemName)) {
            return 107;
        }
        if("Rolling Shuriken Ammo".equals(itemName)) {
            return 108;
        }
        if("Earth Spear Ammo".equals(itemName)) {
            return 109;
        }
        if("Flare Gun Ammo".equals(itemName)) {
            return 110;
        }
        if("Bomb Ammo".equals(itemName)) {
            return 111;
        }
        if("Chakram Ammo".equals(itemName)) {
            return 112;
        }
        if("Caltrops Ammo".equals(itemName)) {
            return 113;
        }
        if("Pistol Ammo".equals(itemName)) {
            return 114;
        }
        return DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(itemName).getInventoryArg();
    }

    public static short getWorldFlag(String itemName) {
        if("Weights".equals(itemName)) {
            return 0;
        }
        if(itemName.endsWith("Ammo")) {
            return 0;
        }
        if("Coin".equals(itemName)) {
            return 0;
        }
        return (short)DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(itemName).getWorldFlag();
    }
}
