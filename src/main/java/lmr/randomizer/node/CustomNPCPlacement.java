package lmr.randomizer.node;

public class CustomNPCPlacement {
    private String npcLocation; // The NPC who would normally be at this location
    private String npcDoorContents; // The NPC who will be at this location now

    public CustomNPCPlacement(String npcLocation, String npcDoorContents) {
        this.npcLocation = npcLocation;
        this.npcDoorContents = npcDoorContents;
    }

    public String getNpcLocation() {
        return npcLocation;
    }

    public String getNpcDoorContents() {
        return npcDoorContents;
    }
}
