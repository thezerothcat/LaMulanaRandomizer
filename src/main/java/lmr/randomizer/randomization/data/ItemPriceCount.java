package lmr.randomizer.randomization.data;

public class ItemPriceCount {
    private Short price;
    private Short count;

    public ItemPriceCount(Short price, Short count) {
        this.price = price;
        this.count = count;
    }

    public ItemPriceCount(Integer price, Integer count) {
        this.price = price == null ? null : price.shortValue();
        this.count = count == null ? null : count.shortValue();
    }

    public ItemPriceCount(ItemPriceCount itemPriceCount) {
        if(itemPriceCount != null) {
            this.price = itemPriceCount.getPrice();
            this.count = itemPriceCount.getCount();
        }
    }

    public Short getPrice() {
        return price;
    }

    public Short getCount() {
        return count;
    }
}
