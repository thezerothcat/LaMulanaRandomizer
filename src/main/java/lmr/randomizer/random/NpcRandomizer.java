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
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Giltoriyo", "NPC: Philosopher Giltoriyo");
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Alsedana", "NPC: Philosopher Alsedana");
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Samaranta", "NPC: Philosopher Samaranta");
            mapOfNpcDoorLocationToContents.put("NPCL: Hiner", "NPC: Hiner");
            mapOfNpcDoorLocationToContents.put("NPCL: Moger", "NPC: Moger");
            mapOfNpcDoorLocationToContents.put("NPCL: Sidro", "NPC: Sidro");
            mapOfNpcDoorLocationToContents.put("NPCL: Modro", "NPC: Modro");
            mapOfNpcDoorLocationToContents.put("NPCL: Mud Man Qubert", "NPC: Mud Man Qubert");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Zarnac", "NPC: Priest Zarnac");
            mapOfNpcDoorLocationToContents.put("NPCL: 8bit Elder", "NPC: 8bit Elder");
            mapOfNpcDoorLocationToContents.put("NPCL: duplex", "NPC: duplex");
            mapOfNpcDoorLocationToContents.put("NPCL: Samieru", "NPC: Samieru");
            mapOfNpcDoorLocationToContents.put("NPCL: Naramura", "NPC: Naramura");

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
            npcDoors.add("Philosopher Giltoriyo");
            npcDoors.add("Philosopher Alsedana");
            npcDoors.add("Philosopher Samaranta");
            npcDoors.add("Hiner");
            npcDoors.add("Moger");
            npcDoors.add("Sidro");
            npcDoors.add("Modro");
            npcDoors.add("Mud Man Qubert");
            npcDoors.add("Priest Zarnac");
            npcDoors.add("8bit Elder");
            npcDoors.add("duplex");
            npcDoors.add("Samieru");
            npcDoors.add("Naramura");

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
            npcs.add("Philosopher Giltoriyo");
            npcs.add("Philosopher Alsedana");
            npcs.add("Philosopher Samaranta");
            npcs.add("Hiner");
            npcs.add("Moger");
            npcs.add("Sidro");
            npcs.add("Modro");
            npcs.add("Mud Man Qubert");
            npcs.add("Priest Zarnac");
            npcs.add("8bit Elder");
            npcs.add("duplex");
            npcs.add("Samieru");
            npcs.add("Naramura");

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

            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Giltoriyo", "NPC: Philosopher Giltoriyo");
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Alsedana", "NPC: Philosopher Alsedana");
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Samaranta", "NPC: Philosopher Samaranta");
            mapOfNpcDoorLocationToContents.put("NPCL: Hiner", "NPC: Hiner");
            mapOfNpcDoorLocationToContents.put("NPCL: Moger", "NPC: Moger");
            mapOfNpcDoorLocationToContents.put("NPCL: Sidro", "NPC: Sidro");
            mapOfNpcDoorLocationToContents.put("NPCL: Modro", "NPC: Modro");
            mapOfNpcDoorLocationToContents.put("NPCL: Mud Man Qubert", "NPC: Mud Man Qubert");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Zarnac", "NPC: Priest Zarnac");
            mapOfNpcDoorLocationToContents.put("NPCL: 8bit Elder", "NPC: 8bit Elder");
            mapOfNpcDoorLocationToContents.put("NPCL: duplex", "NPC: duplex");
            mapOfNpcDoorLocationToContents.put("NPCL: Samieru", "NPC: Samieru");
            mapOfNpcDoorLocationToContents.put("NPCL: Naramura", "NPC: Naramura");
        }

//        mapOfNpcDoorLocationToContents.put("NPCL: Priest Triton", "NPC: Priest Triton");
//        mapOfNpcDoorLocationToContents.put("NPCL: 8bit Fairy", "NPC: 8bit Fairy");
//
//        mapOfNpcDoorLocationToContents.put("NPCL: Former Mekuri Master", "NPC: Former Mekuri Master");
//        mapOfNpcDoorLocationToContents.put("NPCL: Priest Alest", "NPC: Priest Alest");
//        mapOfNpcDoorLocationToContents.put("NPCL: Mr. Slushfund", "NPC: Mr. Slushfund");
//
//        mapOfNpcDoorLocationToContents.put("NPCL: Nebur (Original)", "NPC: Nebur (Original)");
//        mapOfNpcDoorLocationToContents.put("NPCL: Nebur (Alt)", "NPC: Nebur (Alt)");
//        mapOfNpcDoorLocationToContents.put("NPCL: Yiear Kungfu", "NPC: Yiear Kungfu");
//        mapOfNpcDoorLocationToContents.put("NPCL: Giant Mopiran", "NPC: Giant Mopiran");
//
//        mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Fobos", "NPC: Philosopher Fobos");
//
//        mapOfNpcDoorLocationToContents.put("NPCL: Xelpud", "NPC: Xelpud");
//        mapOfNpcDoorLocationToContents.put("NPCL: Mulbruk", "NPC: Mulbruk");
//        mapOfNpcDoorLocationToContents.put("NPCL: The Fairy Queen", "NPC: The Fairy Queen");
//        mapOfNpcDoorLocationToContents.put("NPCL: Stray fairy", "NPC: Stray fairy");
    }

    public String getNpc(String npcLocation) {
        return mapOfNpcDoorLocationToContents.get(npcLocation);
    }

    public String getShopNpcLocation(String shopName) {
        if("Shop 1 (Surface)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Modro");
        }
        if("Shop 2 (Surface)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Nebur (Original)");
        }
        if("Shop 2 Alt (Surface)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Nebur (Alt)");
        }
        if("Shop 3 (Surface)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Sidro");
        }
        if("Shop 4 (Guidance)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Penadvent of ghost");
        }
        if("Shop 5 (Illusion)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Mover Athleland");
        }
        if("Shop 6 (Mausoleum)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Greedy Charlie");
        }
        if("Shop 7 (Graveyard)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Giant Mopiran");
        }
        if("Shop 8 (Sun)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Shalom III");
        }
        if("Shop 9 (Sun)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Usas VI");
        }
        if("Shop 10 (Sun)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Kingvalley I");
        }
        if("Shop 11 (Moonlight)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Kingvalley II");
        }
        if("Shop 12 (Spring)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Mr. Fishman (Original)");
        }
        if("Shop 12 Alt (Spring)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Mr. Fishman (Alt)");
        }
        if("Shop 13 (Goddess)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Energetic Belmont");
        }
        if("Shop 14 (Inferno)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Hot-blooded Nemesistwo");
        }
        if("Shop 15 (Ruin)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Mechanical Efspi");
        }
        if("Shop 16 (Extinction)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Operator Combaker");
        }
        if("Shop 17 (Birth)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Mud Man Qubert");
        }
        if("Shop 18 (Lil Bro)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Yiegah Kungfu");
        }
        if("Shop 19 (Big Bro)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Yiear Kungfu");
        }
        if("Shop 20 (Twin Labs)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Arrogant Sturdy Snake");
        }
        if("Shop 21 (Unsolvable)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Arrogant Metagear");
        }
        if("Shop 22 (Endless)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Affected Knimare");
        }
        if("Shop 23 (HT)".equals(shopName)) {
            return findDoorLocationForNpc("NPC: Tailor Dracuet");
        }
        return null;
    }

    private String findDoorLocationForNpc(String npc) {
        for(String npcLocation : mapOfNpcDoorLocationToContents.keySet()) {
            if(mapOfNpcDoorLocationToContents.get(npcLocation).equals(npc)) {
                return npcLocation;
            }
        }
        return npc.replace("NPC:", "NPCL:");
    }

    public void updateNpcs() {
        for (String npcDoorLocation : mapOfNpcDoorLocationToContents.keySet()) {
            GameDataTracker.writeNpcDoor(npcDoorLocation, mapOfNpcDoorLocationToContents.get(npcDoorLocation));
        }
    }
}
