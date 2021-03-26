package lmr.randomizer.random;

import lmr.randomizer.Settings;
import lmr.randomizer.update.GameDataTracker;

import java.util.*;

public class NpcRandomizer {
    private Map<String, String> mapOfNpcDoorLocationToContents;

    public NpcRandomizer() {
        mapOfNpcDoorLocationToContents = new HashMap<>(14);
    }

    public void determineNpcLocations(Random random) {
        if (Settings.isFoolsNpc()) {
            List<String> npcDoors = new ArrayList<>();
            npcDoors.add("Sturdy Snake");
            npcDoors.add("Arrogant Metagear");
            List<String> npcs = new ArrayList<>();
            npcs.add("Sturdy Snake");
            npcs.add("Arrogant Metagear");

            String doorLocation = npcDoors.remove(random.nextInt(npcDoors.size()));
            mapOfNpcDoorLocationToContents.put("NPCL: " + doorLocation, "NPC: Yiegah Kungfu");

            mapOfNpcDoorLocationToContents.put("NPCL: Priest Hidlyda", "NPC: Mr. Fishman (Alt)");
            mapOfNpcDoorLocationToContents.put("NPCL: Mr. Fishman (Original)", "NPC: Mr. Fishman (Original)");
            mapOfNpcDoorLocationToContents.put("NPCL: Mr. Fishman (Alt)", "NPC: Mr. Fishman (Original)");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Laydoc", "NPC: Mechanical Efspi");
            mapOfNpcDoorLocationToContents.put("NPCL: Mechanical Efspi", "NPC: Priest Laydoc");

            npcDoors.add("Yiegah Kungfu");

            String npc;
            while(!npcs.isEmpty()) {
                npc = npcs.remove(random.nextInt(npcs.size()));
                doorLocation = npcDoors.remove(random.nextInt(npcDoors.size()));
                mapOfNpcDoorLocationToContents.put("NPCL: " + doorLocation, "NPC: " + npc);
            }
        } else {
            mapOfNpcDoorLocationToContents.put("NPCL: Yiegah Kungfu", "NPC: Yiegah Kungfu");
            mapOfNpcDoorLocationToContents.put("NPCL: Sturdy Snake", "NPC: Sturdy Snake");
            mapOfNpcDoorLocationToContents.put("NPCL: Arrogant Metagear", "NPC: Arrogant Metagear");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Hidlyda", "NPC: Priest Hidlyda");
            mapOfNpcDoorLocationToContents.put("NPCL: Mr. Fishman (Original)", "NPC: Mr. Fishman (Original)");
            mapOfNpcDoorLocationToContents.put("NPCL: Mr. Fishman (Alt)", "NPC: Mr. Fishman (Alt)");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Laydoc", "NPC: Priest Laydoc");
            mapOfNpcDoorLocationToContents.put("NPCL: Mechanical Efspi", "NPC: Mechanical Efspi");
        }
    }

    public String getNpc(String npcLocation) {
        return mapOfNpcDoorLocationToContents.get(npcLocation);
    }

    public void updateNpcs() {
        for (String npcDoorLocation : mapOfNpcDoorLocationToContents.keySet()) {
            GameDataTracker.writeNpcDoor(npcDoorLocation, mapOfNpcDoorLocationToContents.get(npcDoorLocation));
        }
    }
}
