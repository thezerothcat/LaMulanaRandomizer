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
        if (Settings.isFools2021Mode()) {
            List<String> npcDoors = new ArrayList<>();
            npcDoors.add("Arrogant Sturdy Snake");
            npcDoors.add("Arrogant Metagear");
            List<String> npcs = new ArrayList<>();
            npcs.add("Arrogant Metagear");

            String doorLocation = npcDoors.remove(random.nextInt(npcDoors.size()));
            mapOfNpcDoorLocationToContents.put("NPCL: " + doorLocation, "NPC: Yiegah Kungfu");

            mapOfNpcDoorLocationToContents.put("NPCL: Priest Hidlyda", "NPC: Mr. Fishman (Original)");
            mapOfNpcDoorLocationToContents.put("NPCL: Mr. Fishman (Original)", "NPC: Mr. Fishman (Alt)");
            mapOfNpcDoorLocationToContents.put("NPCL: Mr. Fishman (Alt)", "NPC: Mr. Fishman (Alt)");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Laydoc", "NPC: Mechanical Efspi");
            mapOfNpcDoorLocationToContents.put("NPCL: Mechanical Efspi", "NPC: Priest Laydoc");
            mapOfNpcDoorLocationToContents.put("NPCL: Yiegah Kungfu", "NPC: Arrogant Sturdy Snake");

            mapOfNpcDoorLocationToContents.put("NPCL: Priest Xanado", "NPC: Priest Xanado");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Romancis", "NPC: Priest Romancis");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Aramo", "NPC: Priest Aramo");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Jaguarfiv", "NPC: Priest Jaguarfiv");
            mapOfNpcDoorLocationToContents.put("NPCL: Giant Thexde", "NPC: Giant Thexde");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Ashgine", "NPC: Priest Ashgine");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Madomono", "NPC: Priest Madomono");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Gailious", "NPC: Priest Gailious");

            mapOfNpcDoorLocationToContents.put("NPCL: Penadvent of ghost", "NPC: Penadvent of ghost");
            mapOfNpcDoorLocationToContents.put("NPCL: Greedy Charlie", "NPC: Greedy Charlie");
            mapOfNpcDoorLocationToContents.put("NPCL: Shalom III", "NPC: Shalom III");
            mapOfNpcDoorLocationToContents.put("NPCL: Usas VI", "NPC: Usas VI");
            mapOfNpcDoorLocationToContents.put("NPCL: Kingvalley I", "NPC: Kingvalley I");
            mapOfNpcDoorLocationToContents.put("NPCL: Hot-blooded Nemesistwo", "NPC: Hot-blooded Nemesistwo");
            mapOfNpcDoorLocationToContents.put("NPCL: Operator Combaker", "NPC: Operator Combaker");
            mapOfNpcDoorLocationToContents.put("NPCL: Affected Knimare", "NPC: Affected Knimare");
            mapOfNpcDoorLocationToContents.put("NPCL: Mover Athleland", "NPC: Mover Athleland");
            mapOfNpcDoorLocationToContents.put("NPCL: Kingvalley II", "NPC: Kingvalley II");
            mapOfNpcDoorLocationToContents.put("NPCL: Energetic Belmont", "NPC: Energetic Belmont");
            mapOfNpcDoorLocationToContents.put("NPCL: Tailor Dracuet", "NPC: Tailor Dracuet");

            String npc;
            while(!npcs.isEmpty()) {
                npc = npcs.remove(random.nextInt(npcs.size()));
                doorLocation = npcDoors.remove(random.nextInt(npcDoors.size()));
                mapOfNpcDoorLocationToContents.put("NPCL: " + doorLocation, "NPC: " + npc);
            }
        }
        else if(Settings.isRandomizeNpcs()) {
            List<String> npcDoors = new ArrayList<>();
            npcDoors.add("Priest Hidlyda");
            npcDoors.add("Mr. Fishman (Original)");
            npcDoors.add("Mr. Fishman (Alt)");
            npcDoors.add("Priest Laydoc");
            npcDoors.add("Mechanical Efspi");
            npcDoors.add("Yiegah Kungfu");
            npcDoors.add("Priest Xanado");
            npcDoors.add("Priest Romancis");
            npcDoors.add("Priest Aramo");
            npcDoors.add("Priest Jaguarfiv");
            npcDoors.add("Giant Thexde");
            npcDoors.add("Priest Ashgine");
            npcDoors.add("Priest Madomono");
            npcDoors.add("Priest Gailious");
            npcDoors.add("Penadvent of ghost");
            npcDoors.add("Greedy Charlie");
            npcDoors.add("Shalom III");
            npcDoors.add("Usas VI");
            npcDoors.add("Kingvalley I");
            npcDoors.add("Hot-blooded Nemesistwo");
            npcDoors.add("Operator Combaker");
            npcDoors.add("Affected Knimare");
            npcDoors.add("Mover Athleland");
            npcDoors.add("Kingvalley II");
            npcDoors.add("Energetic Belmont");
            npcDoors.add("Arrogant Metagear");
            npcDoors.add("Arrogant Sturdy Snake");

            List<String> npcs = new ArrayList<>();
            npcs.add("Priest Hidlyda");
            npcs.add("Mr. Fishman (Original)");
            npcs.add("Mr. Fishman (Alt)");
            npcs.add("Priest Laydoc");
            npcs.add("Mechanical Efspi");
            npcs.add("Yiegah Kungfu");
            npcs.add("Priest Xanado");
            npcs.add("Priest Romancis");
            npcs.add("Priest Aramo");
            npcs.add("Priest Jaguarfiv");
            npcs.add("Giant Thexde");
            npcs.add("Priest Ashgine");
            npcs.add("Priest Madomono");
            npcs.add("Priest Gailious");
            npcs.add("Penadvent of ghost");
            npcs.add("Greedy Charlie");
            npcs.add("Shalom III");
            npcs.add("Usas VI");
            npcs.add("Kingvalley I");
            npcs.add("Hot-blooded Nemesistwo");
            npcs.add("Operator Combaker");
            npcs.add("Affected Knimare");
            npcs.add("Mover Athleland");
            npcs.add("Kingvalley II");
            npcs.add("Energetic Belmont");
            npcs.add("Arrogant Metagear");
            npcs.add("Arrogant Sturdy Snake");

            if(Settings.isRandomizeDracuetShop()) {
                npcDoors.add("Tailor Dracuet");
                npcs.add("Tailor Dracuet");
            }
            else {
                mapOfNpcDoorLocationToContents.put("NPCL: Tailor Dracuet", "NPC: Tailor Dracuet");
            }

            String doorLocation;
            String npc;
            while(!npcs.isEmpty()) {
                npc = npcs.remove(random.nextInt(npcs.size()));
                doorLocation = npcDoors.remove(random.nextInt(npcDoors.size()));
                mapOfNpcDoorLocationToContents.put("NPCL: " + doorLocation, "NPC: " + npc);
            }
        }
        else {
            mapOfNpcDoorLocationToContents.put("NPCL: Yiegah Kungfu", "NPC: Yiegah Kungfu");
            mapOfNpcDoorLocationToContents.put("NPCL: Arrogant Sturdy Snake", "NPC: Arrogant Sturdy Snake");
            mapOfNpcDoorLocationToContents.put("NPCL: Arrogant Metagear", "NPC: Arrogant Metagear");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Hidlyda", "NPC: Priest Hidlyda");
            mapOfNpcDoorLocationToContents.put("NPCL: Mr. Fishman (Original)", "NPC: Mr. Fishman (Original)");
            mapOfNpcDoorLocationToContents.put("NPCL: Mr. Fishman (Alt)", "NPC: Mr. Fishman (Alt)");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Laydoc", "NPC: Priest Laydoc");
            mapOfNpcDoorLocationToContents.put("NPCL: Mechanical Efspi", "NPC: Mechanical Efspi");

            mapOfNpcDoorLocationToContents.put("NPCL: Priest Xanado", "NPC: Priest Xanado");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Romancis", "NPC: Priest Romancis");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Aramo", "NPC: Priest Aramo");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Jaguarfiv", "NPC: Priest Jaguarfiv");
            mapOfNpcDoorLocationToContents.put("NPCL: Giant Thexde", "NPC: Giant Thexde");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Ashgine", "NPC: Priest Ashgine");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Madomono", "NPC: Priest Madomono");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Gailious", "NPC: Priest Gailious");

            mapOfNpcDoorLocationToContents.put("NPCL: Penadvent of ghost", "NPC: Penadvent of ghost");
            mapOfNpcDoorLocationToContents.put("NPCL: Greedy Charlie", "NPC: Greedy Charlie");
            mapOfNpcDoorLocationToContents.put("NPCL: Shalom III", "NPC: Shalom III");
            mapOfNpcDoorLocationToContents.put("NPCL: Usas VI", "NPC: Usas VI");
            mapOfNpcDoorLocationToContents.put("NPCL: Kingvalley I", "NPC: Kingvalley I");
            mapOfNpcDoorLocationToContents.put("NPCL: Hot-blooded Nemesistwo", "NPC: Hot-blooded Nemesistwo");
            mapOfNpcDoorLocationToContents.put("NPCL: Operator Combaker", "NPC: Operator Combaker");
            mapOfNpcDoorLocationToContents.put("NPCL: Affected Knimare", "NPC: Affected Knimare");
            mapOfNpcDoorLocationToContents.put("NPCL: Mover Athleland", "NPC: Mover Athleland");
            mapOfNpcDoorLocationToContents.put("NPCL: Kingvalley II", "NPC: Kingvalley II");
            mapOfNpcDoorLocationToContents.put("NPCL: Energetic Belmont", "NPC: Energetic Belmont");
            mapOfNpcDoorLocationToContents.put("NPCL: Tailor Dracuet", "NPC: Tailor Dracuet");
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
