package lmr.randomizer.randomization;

import lmr.randomizer.randomization.data.ItemPriceCount;

public interface ShopItemPriceCountRandomizer {
    ItemPriceCount getItemPriceCount(String itemName, String location, boolean forceRerollPrices);
}
