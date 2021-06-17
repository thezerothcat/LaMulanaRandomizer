package lmr.randomizer.randomization.data;

public class BacksideDoorData {
    private String doorName;
    private String doorDestination;
    private Integer doorBoss;

    public BacksideDoorData(String doorName, String doorDestination, Integer doorBoss) {
        this.doorName = doorName;
        this.doorDestination = doorDestination;
        this.doorBoss = doorBoss;
    }

    public String getDoorName() {
        return doorName;
    }

    public String getDoorDestination() {
        return doorDestination;
    }

    public Integer getDoorBoss() {
        return doorBoss;
    }
}
