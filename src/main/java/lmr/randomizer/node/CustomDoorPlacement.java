package lmr.randomizer.node;

public class CustomDoorPlacement {
    private String targetDoor; // The door being assigned a destination.
    private String destinationDoor; // The door Lemeza arrives at after passing through the target door.

    private Integer assignedBoss;

    public CustomDoorPlacement(String targetDoor, String destinationDoor, String assignedBoss) {
        this.targetDoor = targetDoor;
        this.destinationDoor = destinationDoor;
        if(assignedBoss != null) {
            this.assignedBoss = getBossNumber(assignedBoss);
        }
    }

    public String getTargetDoor() {
        return targetDoor;
    }

    public String getDestinationDoor() {
        return destinationDoor;
    }

    public Integer getAssignedBoss() {
        return assignedBoss;
    }

    private static Integer getBossNumber(String bossText) {
        if(bossText.length() == 1) {
            try {
                return Integer.parseInt(bossText);
            }
            catch (NumberFormatException ex) {
                return null;
            }
        }
        if("None".equals(bossText)) {
            return 0;
        }
        if("Amphisbaena".equals(bossText)) {
            return 1;
        }
        if("Sakit".equals(bossText)) {
            return 2;
        }
        if("Ellmac".equals(bossText)) {
            return 3;
        }
        if("Bahamut".equals(bossText)) {
            return 4;
        }
        if("Viy".equals(bossText)) {
            return 5;
        }
        if("Palenque".equals(bossText)) {
            return 6;
        }
        if("Baphomet".equals(bossText)) {
            return 7;
        }
        if("Tiamat".equals(bossText)) {
            return 8;
        }
        if("Key Fairy".equals(bossText)) {
            return 9;
        }
        return null;
    }
}
