package lmr.randomizer.randomization;

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
    public ItemPriceCount getItemPriceCount(String shopItem, String shopItemLocation, boolean isRemovedItem) {
        String startingWeapon = Settings.getCurrentStartingWeapon();
        if((subweaponOnly && shopItem.endsWith(" Ammo")) || shopItem.equals(startingWeapon + " Ammo")) {
            if("Shuriken".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)150);
            }
            if("Rolling Shuriken".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)100);
            }
            if("Earth Spear".equals(startingWeapon)
                    || "Flare Gun".equals(startingWeapon)
                    || "Caltrops".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)80);
            }
            if("Bomb".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)30);
            }
            if("Chakram".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)10);
            }
            if("Pistol".equals(startingWeapon)) {
                return new ItemPriceCount((short)0, (short)3);
            }
        }

        Integer shopPrice = moneyChecker == null ? null : moneyChecker.getShopPrice(shopItem, shopItemLocation.replaceAll(" Item \\d", ""));
        if(shopPrice != null) {
            Integer defaultShopPrice = null;
            if("Shop 1 (Surface) Item 1".equals(shopItemLocation)) {
                defaultShopPrice = 10;
            }
            else if("Shop 2 (Surface) Item 2".equals(shopItemLocation)) {
                defaultShopPrice = 50;
            }
            else if("Shop 2 (Surface) Item 3".equals(shopItemLocation)) {
                defaultShopPrice = 20;
            }
            else if("Shop 3 (Surface) Item 1".equals(shopItemLocation)) {
                defaultShopPrice = 10;
            }
            else if("Shop 3 (Surface) Item 2".equals(shopItemLocation)) {
                defaultShopPrice = 50;
            }
            else if("Shop 3 (Surface) Item 3".equals(shopItemLocation)) {
                defaultShopPrice = 100;
            }
            else if("Shop 6 (Mausoleum) Item 1".equals(shopItemLocation)) {
                defaultShopPrice = 60;
            }
            else if("Shop 7 (Graveyard) Item 1".equals(shopItemLocation)) {
                defaultShopPrice = 60;
            }
            if(defaultShopPrice != null) {
                return new ItemPriceCount((short)(int)(defaultShopPrice < shopPrice ? defaultShopPrice : shopPrice - 5 * random.nextInt(2)), null);
            }
            return new ItemPriceCount((short)(int)(shopPrice - 5 * random.nextInt(2)), null);
        }

        if ("Weights".equals(shopItem)) {
            if(Settings.isFools2019Mode()) {
                return new ItemPriceCount(1, random.nextInt(10) + 1);
            }
            else {
                return new ItemPriceCount(isRemovedItem ? 10 : null, 5);
            }
        }
        else if (!shopItem.endsWith("Ammo")) {
            return new ItemPriceCount(null, 1);
        }

        return null;
    }
}
