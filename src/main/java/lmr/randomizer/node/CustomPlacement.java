package lmr.randomizer.node;

public class CustomPlacement {
    private String location; // if null, contents should be removed; name can be either item or shop inventory
    private String contents; // what to put in the location

    public CustomPlacement(String location, String contents) {
        this.location = location;
        this.contents = contents;
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
}
