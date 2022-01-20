package lmr.randomizer.node;

public class CustomItemPlacement {
    private String location; // name can be either item or shop inventory
    private String contents; // what to put in the location
    private String itemGraphic; // ability to customize graphic for traps

    private Short shopPrice;
    private Short shopCount;

    public CustomItemPlacement(String location, String contents, Short shopPrice, Short shopCount) {
        this.location = location;
        this.contents = contents;
        this.shopPrice = shopPrice;
        this.shopCount = shopCount;
    }

    public CustomItemPlacement(String location, String contents, String itemGraphic, Short shopPrice, Short shopCount) {
        this.location = location;
        this.contents = contents;
        this.itemGraphic = itemGraphic;
        this.shopPrice = shopPrice;
        this.shopCount = shopCount;
    }

    public CustomItemPlacement(String location, String contents, String itemGraphic) {
        this.location = location;
        this.contents = contents;
        this.itemGraphic = itemGraphic;
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

    public String getItemGraphic() {
        return itemGraphic;
    }

    public Short getShopPrice() {
        return shopPrice;
    }

    public Short getShopCount() {
        return shopCount;
    }
}
