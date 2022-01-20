package lmr.randomizer.randomization;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.HolidaySettings;
import lmr.randomizer.node.CustomItemPlacement;
import lmr.randomizer.randomization.data.ItemPriceCount;
import lmr.randomizer.Settings;
import lmr.randomizer.node.MoneyChecker;

import java.util.Random;

public class CategorizedShopItemPriceCountRandomizer implements ShopItemPriceCountRandomizer {
    private boolean subweaponOnly;

    private MoneyChecker moneyChecker;

    private Random random;

    public CategorizedShopItemPriceCountRandomizer(boolean subweaponOnly, MoneyChecker moneyChecker, Random random) {
        this.subweaponOnly = subweaponOnly;
        this.moneyChecker = moneyChecker;
        this.random = random;
    }

    @Override
    public ItemPriceCount getItemPriceCount(String itemName, String location, boolean isRemovedItem) {
        Short price = null;
        Short count = null;
        // Check custom price/count by location
        for(CustomItemPlacement customItemPlacement : DataFromFile.getCustomPlacementData().getCustomItemPlacements()) {
            if(customItemPlacement.getLocation().equals(location) && customItemPlacement.getContents().equals(itemName)
                    && customItemPlacement.getShopPrice() != null) {
                price = customItemPlacement.getShopPrice();
                count = customItemPlacement.getShopCount();
            }
        }

        // Check global custom price
        if(price == null) {
            price = DataFromFile.getCustomPlacementData().getCustomShopPrices().get(itemName);
        }
        // Check global custom count
        if(count == null) {
            count = DataFromFile.getCustomPlacementData().getCustomShopCounts().get(itemName);
        }
        if(count != null && count == -1) {
            count = getRandomCount();
        }

        if(price == null) {
            price = getPrice(itemName, location, isRemovedItem);
        }
        if(count == null) {
            count = getCount(itemName);
        }
        if(price == null && count == null) {
            return null;
        }
        return new ItemPriceCount(price, count);
    }

    private Short getPrice(String itemName, String location, boolean isRemovedItem) {
        String startingWeapon = Settings.getCurrentStartingWeapon();
        if((subweaponOnly && itemName.endsWith(" Ammo")) || itemName.equals(startingWeapon + " Ammo")) {
            return 0;
        }

        Integer shopPrice = moneyChecker == null ? null : moneyChecker.getShopPrice(itemName, location.replaceAll(" Item \\d", ""));
        if(shopPrice != null) {
            Integer defaultShopPrice = null;
            if("Shop 1 (Surface) Item 1".equals(location)) {
                defaultShopPrice = 10;
            }
            else if("Shop 2 (Surface) Item 2".equals(location)) {
                defaultShopPrice = 50;
            }
            else if("Shop 2 (Surface) Item 3".equals(location)) {
                defaultShopPrice = 20;
            }
            else if("Shop 3 (Surface) Item 1".equals(location)) {
                defaultShopPrice = 10;
            }
            else if("Shop 3 (Surface) Item 2".equals(location)) {
                defaultShopPrice = 50;
            }
            else if("Shop 3 (Surface) Item 3".equals(location)) {
                defaultShopPrice = 100;
            }
            else if("Shop 6 (Mausoleum) Item 1".equals(location)) {
                defaultShopPrice = 60;
            }
            else if("Shop 7 (Graveyard) Item 1".equals(location)) {
                defaultShopPrice = 60;
            }
            if(defaultShopPrice != null) {
                return (short)(int)(defaultShopPrice < shopPrice ? defaultShopPrice : shopPrice - 5 * random.nextInt(2));
            }
            return (short)(int)(shopPrice - 5 * random.nextInt(2));
        }

        if ("Weights".equals(itemName)) {
            if(HolidaySettings.isFools2019Mode()) {
                return 1;
            }
            else {
                return isRemovedItem ? (short)10 : null;
            }
        }
        return null;
    }

    private Short getCount(String itemName) {
        String startingWeapon = Settings.getCurrentStartingWeapon();
        if((subweaponOnly && itemName.endsWith(" Ammo")) || itemName.equals(startingWeapon + " Ammo")) {
            if("Shuriken".equals(startingWeapon)) {
                return 150;
            }
            if("Rolling Shuriken".equals(startingWeapon)) {
                return 100;
            }
            if("Earth Spear".equals(startingWeapon)
                    || "Flare Gun".equals(startingWeapon)
                    || "Caltrops".equals(startingWeapon)) {
                return 80;
            }
            if("Bomb".equals(startingWeapon)) {
                return 30;
            }
            if("Chakram".equals(startingWeapon)) {
                return 10;
            }
            if("Pistol".equals(startingWeapon)) {
                return 3;
            }
        }
        if ("Weights".equals(itemName)) {
            if(HolidaySettings.isFools2019Mode()) {
                return getRandomCount();
            }
            else {
                return 5;
            }
        }
        else if (!itemName.endsWith("Ammo")) {
            return 1;
        }
        return null;
    }

    private short getRandomCount() {
        return (short)(random.nextInt(10) + 1);
    }
}
