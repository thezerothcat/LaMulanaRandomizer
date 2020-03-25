package lmr.randomizer;

public class ItemPriceCount {
    private Short price;
    private Short count;

    public ItemPriceCount(Short price, Short count) {
        this.price = price;
        this.count = count;
    }

    public Short getPrice() {
        return price;
    }

    public Short getCount() {
        return count;
    }
}
