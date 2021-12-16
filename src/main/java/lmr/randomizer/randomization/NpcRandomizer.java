package lmr.randomizer.randomization;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.node.CustomNPCPlacement;
import lmr.randomizer.rcd.object.ConversationDoor;
import lmr.randomizer.util.LocationCoordinateMapper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class NpcRandomizer {
    // Ignores Former Mekuri Master which isn't always accessible.
    private static final List<String> SURFACE_INITIAL_DOOR_LOCATIONS =
            Arrays.asList("NPCL: Elder Xelpud", "NPCL: Nebur", "NPCL: Sidro", "NPCL: Modro", "NPCL: Hiner", "NPCL: Moger");

    private Map<String, String> mapOfNpcDoorLocationToContents;

    public NpcRandomizer() {
        mapOfNpcDoorLocationToContents = new HashMap<>(14);
    }

    public void determineNpcLocations(Random random) {
        if (HolidaySettings.isFools2021Mode()) {
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
            mapOfNpcDoorLocationToContents.put("NPCL: Yiear Kungfu", "NPC: Yiear Kungfu");

            mapOfNpcDoorLocationToContents.put("NPCL: Priest Xanado", "NPC: Priest Xanado");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Romancis", "NPC: Priest Romancis");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Aramo", "NPC: Priest Aramo");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Triton", "NPC: Priest Triton");
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
            mapOfNpcDoorLocationToContents.put("NPCL: Giant Mopiran", "NPC: Giant Mopiran");
            mapOfNpcDoorLocationToContents.put("NPCL: Kingvalley II", "NPC: Kingvalley II");
            mapOfNpcDoorLocationToContents.put("NPCL: Energetic Belmont", "NPC: Energetic Belmont");
            mapOfNpcDoorLocationToContents.put("NPCL: Tailor Dracuet", "NPC: Tailor Dracuet");
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Giltoriyo", "NPC: Philosopher Giltoriyo");
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Alsedana", "NPC: Philosopher Alsedana");
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Samaranta", "NPC: Philosopher Samaranta");
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Fobos", "NPC: Philosopher Fobos");
            mapOfNpcDoorLocationToContents.put("NPCL: The Fairy Queen", "NPC: The Fairy Queen");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Alest", "NPC: Priest Alest");
            mapOfNpcDoorLocationToContents.put("NPCL: Mr. Slushfund", "NPC: Mr. Slushfund");
            mapOfNpcDoorLocationToContents.put("NPCL: Elder Xelpud", "NPC: Elder Xelpud");
            mapOfNpcDoorLocationToContents.put("NPCL: Hiner", "NPC: Hiner");
            mapOfNpcDoorLocationToContents.put("NPCL: Moger", "NPC: Moger");
            mapOfNpcDoorLocationToContents.put("NPCL: Former Mekuri Master", "NPC: Former Mekuri Master");
            mapOfNpcDoorLocationToContents.put("NPCL: Nebur", "NPC: Nebur");
            mapOfNpcDoorLocationToContents.put("NPCL: Sidro", "NPC: Sidro");
            mapOfNpcDoorLocationToContents.put("NPCL: Modro", "NPC: Modro");
            mapOfNpcDoorLocationToContents.put("NPCL: Mulbruk", "NPC: Mulbruk");
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
            npcDoors.add("Yiear Kungfu");
            npcDoors.add("Priest Xanado");
            npcDoors.add("Priest Romancis");
            npcDoors.add("Priest Aramo");
            npcDoors.add("Priest Triton");
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
            npcDoors.add("Giant Mopiran");
            npcDoors.add("Kingvalley II");
            npcDoors.add("Energetic Belmont");
            npcDoors.add("Arrogant Metagear");
            npcDoors.add("Arrogant Sturdy Snake");
            npcDoors.add("Philosopher Giltoriyo");
            npcDoors.add("Philosopher Alsedana");
            npcDoors.add("Philosopher Samaranta");
            npcDoors.add("Philosopher Fobos");
            npcDoors.add("The Fairy Queen");
            npcDoors.add("Priest Alest");
            npcDoors.add("Mr. Slushfund");
            npcDoors.add("Elder Xelpud");
            npcDoors.add("Hiner");
            npcDoors.add("Moger");
            npcDoors.add("Former Mekuri Master");
            npcDoors.add("Nebur");
            npcDoors.add("Sidro");
            npcDoors.add("Modro");
            npcDoors.add("Mulbruk");
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
            npcs.add("Yiear Kungfu");
            npcs.add("Priest Xanado");
            npcs.add("Priest Romancis");
            npcs.add("Priest Aramo");
            npcs.add("Priest Triton");
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
            npcs.add("Giant Mopiran");
            npcs.add("Kingvalley II");
            npcs.add("Energetic Belmont");
            npcs.add("Arrogant Metagear");
            npcs.add("Arrogant Sturdy Snake");
            npcs.add("Philosopher Giltoriyo");
            npcs.add("Philosopher Alsedana");
            npcs.add("Philosopher Samaranta");
            npcs.add("Philosopher Fobos");
            npcs.add("The Fairy Queen");
            npcs.add("Priest Alest");
            npcs.add("Mr. Slushfund");
            npcs.add("Elder Xelpud");
            npcs.add("Hiner");
            npcs.add("Moger");
            npcs.add("Former Mekuri Master");
            npcs.add("Nebur");
            npcs.add("Sidro");
            npcs.add("Modro");
            npcs.add("Mulbruk");
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

            for(CustomNPCPlacement customNPCPlacement : DataFromFile.getCustomPlacementData().getCustomNPCPlacements()) {
                mapOfNpcDoorLocationToContents.put("NPCL: " + customNPCPlacement.getNpcLocation(), "NPC: " + customNPCPlacement.getNpcDoorContents());
                npcDoors.remove(customNPCPlacement.getNpcLocation());
                npcs.remove(customNPCPlacement.getNpcDoorContents());
            }

            String doorLocation;
            String npc;
            while(!npcs.isEmpty()) {
                npc = npcs.remove(random.nextInt(npcs.size()));
                doorLocation = npcDoors.remove(random.nextInt(npcDoors.size()));
                mapOfNpcDoorLocationToContents.put("NPCL: " + doorLocation, "NPC: " + npc);
            }

            if(LocationCoordinateMapper.isSurfaceStart()) {
                String npcPreviouslyInXelpudTent = mapOfNpcDoorLocationToContents.get("NPCL: Elder Xelpud");
                if(npcPreviouslyInXelpudTent.equals("NPC: Elder Xelpud")) {
                    List<String> surfaceShops = getSurfaceShops();
                    if(surfaceShops.isEmpty()) {
                        List<String> possibleDoors = new ArrayList<>(SURFACE_INITIAL_DOOR_LOCATIONS);
                        possibleDoors.remove("NPCL: Elder Xelpud");
                        replaceDoorContentsWithShop(possibleDoors.get(random.nextInt(possibleDoors.size())), random);
                    }
                }
                if(!npcPreviouslyInXelpudTent.equals("NPC: Elder Xelpud") && !ConversationDoor.isShop(npcPreviouslyInXelpudTent)) {
                    replaceDoorContentsWithShop("NPCL: Elder Xelpud", random);
                }
            }
        }
        else {
            mapOfNpcDoorLocationToContents.put("NPCL: Yiegah Kungfu", "NPC: Yiegah Kungfu");
            mapOfNpcDoorLocationToContents.put("NPCL: Yiear Kungfu", "NPC: Yiear Kungfu");
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
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Triton", "NPC: Priest Triton");
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
            mapOfNpcDoorLocationToContents.put("NPCL: Giant Mopiran", "NPC: Giant Mopiran");
            mapOfNpcDoorLocationToContents.put("NPCL: Kingvalley II", "NPC: Kingvalley II");
            mapOfNpcDoorLocationToContents.put("NPCL: Energetic Belmont", "NPC: Energetic Belmont");
            mapOfNpcDoorLocationToContents.put("NPCL: Tailor Dracuet", "NPC: Tailor Dracuet");

            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Giltoriyo", "NPC: Philosopher Giltoriyo");
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Alsedana", "NPC: Philosopher Alsedana");
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Samaranta", "NPC: Philosopher Samaranta");
            mapOfNpcDoorLocationToContents.put("NPCL: Philosopher Fobos", "NPC: Philosopher Fobos");
            mapOfNpcDoorLocationToContents.put("NPCL: The Fairy Queen", "NPC: The Fairy Queen");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Alest", "NPC: Priest Alest");
            mapOfNpcDoorLocationToContents.put("NPCL: Mr. Slushfund", "NPC: Mr. Slushfund");
            mapOfNpcDoorLocationToContents.put("NPCL: Elder Xelpud", "NPC: Elder Xelpud");
            mapOfNpcDoorLocationToContents.put("NPCL: Hiner", "NPC: Hiner");
            mapOfNpcDoorLocationToContents.put("NPCL: Moger", "NPC: Moger");
            mapOfNpcDoorLocationToContents.put("NPCL: Former Mekuri Master", "NPC: Former Mekuri Master");
            mapOfNpcDoorLocationToContents.put("NPCL: Nebur", "NPC: Nebur");
            mapOfNpcDoorLocationToContents.put("NPCL: Sidro", "NPC: Sidro");
            mapOfNpcDoorLocationToContents.put("NPCL: Modro", "NPC: Modro");
            mapOfNpcDoorLocationToContents.put("NPCL: Mulbruk", "NPC: Mulbruk");
            mapOfNpcDoorLocationToContents.put("NPCL: Mud Man Qubert", "NPC: Mud Man Qubert");
            mapOfNpcDoorLocationToContents.put("NPCL: Priest Zarnac", "NPC: Priest Zarnac");
            mapOfNpcDoorLocationToContents.put("NPCL: 8bit Elder", "NPC: 8bit Elder");
            mapOfNpcDoorLocationToContents.put("NPCL: duplex", "NPC: duplex");
            mapOfNpcDoorLocationToContents.put("NPCL: Samieru", "NPC: Samieru");
            mapOfNpcDoorLocationToContents.put("NPCL: Naramura", "NPC: Naramura");
        }

//        mapOfNpcDoorLocationToContents.put("NPCL: 8bit Fairy", "NPC: 8bit Fairy");
//
//        mapOfNpcDoorLocationToContents.put("NPCL: Mulbruk", "NPC: Mulbruk");
//        mapOfNpcDoorLocationToContents.put("NPCL: Stray fairy", "NPC: Stray fairy");
//        mapOfNpcDoorLocationToContents.put("NPCL: Fairy", "NPC: Fairy");
    }

    public List<String> getSurfaceShops() {
        List<String> surfaceShops = new ArrayList<>();
        String xelpudDoorNpc = mapOfNpcDoorLocationToContents.get("NPCL: Elder Xelpud");
        if(xelpudDoorNpc.equals("NPC: Elder Xelpud")) {
            for(String surfaceDoor : SURFACE_INITIAL_DOOR_LOCATIONS) {
                String surfaceNpc = mapOfNpcDoorLocationToContents.get(surfaceDoor);
                if(ConversationDoor.isShop(surfaceNpc)) {
                    surfaceShops.add(getShopName(surfaceNpc.replaceAll("NPC: ", "")));
                }
            }
        }
        else if(ConversationDoor.isShop(xelpudDoorNpc)) {
            surfaceShops.add(getShopName(xelpudDoorNpc.replaceAll("NPC: ", "")));
        }
        return surfaceShops;
    }

    private void replaceDoorContentsWithShop(String npcLocation, Random random) {
        String doorPreviousNpc = mapOfNpcDoorLocationToContents.get(npcLocation);
        List<String> shopNpcs = new ArrayList<>();

        for(String placedNpc : mapOfNpcDoorLocationToContents.values()) {
            if(ConversationDoor.isShop(placedNpc)) {
                shopNpcs.add(placedNpc);
            }
        }
        String moveToDoorNpc = shopNpcs.get(random.nextInt(shopNpcs.size()));
        String moveToDoorNpcOldLocation = getNpcLocation(moveToDoorNpc.replaceAll("NPC: ", ""));

        mapOfNpcDoorLocationToContents.put(npcLocation, moveToDoorNpc);
        mapOfNpcDoorLocationToContents.put(moveToDoorNpcOldLocation, doorPreviousNpc);
    }

    public String getNpc(String npcLocation) {
        return mapOfNpcDoorLocationToContents.get(npcLocation);
    }

    public boolean isDimensionalCorridor(String itemLocation) {
        if(itemLocation.startsWith("Shop")) {
            return getNpc("NPCL: Philosopher Fobos").replaceAll("NPC: ", "")
                    .equals(getShopNpcName(itemLocation));
        }
        return false;
    }

    public String getNpcLocation(String npc) {
        return npc == null ? null : findDoorLocationForNpc("NPC: " + npc);
    }

    public String getShopNpcName(String shopName) {
        if("Shop 1 (Surface)".equals(shopName)) {
            return "Modro";
        }
        if("Shop 2 (Surface)".equals(shopName)) {
            return "Nebur";
        }
        if("Shop 2 Alt (Surface)".equals(shopName)) {
            return "Nebur";
        }
        if("Shop 3 (Surface)".equals(shopName)) {
            return "Sidro";
        }
        if("Shop 4 (Guidance)".equals(shopName)) {
            return "Penadvent of ghost";
        }
        if("Shop 5 (Illusion)".equals(shopName)) {
            return "Mover Athleland";
        }
        if("Shop 6 (Mausoleum)".equals(shopName)) {
            return "Greedy Charlie";
        }
        if("Shop 7 (Graveyard)".equals(shopName)) {
            return "Giant Mopiran";
        }
        if("Shop 8 (Sun)".equals(shopName)) {
            return "Shalom III";
        }
        if("Shop 9 (Sun)".equals(shopName)) {
            return "Usas VI";
        }
        if("Shop 10 (Sun)".equals(shopName)) {
            return "Kingvalley I";
        }
        if("Shop 11 (Moonlight)".equals(shopName)) {
            return "Kingvalley II";
        }
        if("Shop 12 (Spring)".equals(shopName)) {
            return "Mr. Fishman (Original)";
        }
        if("Shop 12 Alt (Spring)".equals(shopName)) {
            return "Mr. Fishman (Alt)";
        }
        if("Shop 13 (Goddess)".equals(shopName)) {
            return "Energetic Belmont";
        }
        if("Shop 14 (Inferno)".equals(shopName)) {
            return "Hot-blooded Nemesistwo";
        }
        if("Shop 15 (Ruin)".equals(shopName)) {
            return "Mechanical Efspi";
        }
        if("Shop 16 (Extinction)".equals(shopName)) {
            return "Operator Combaker";
        }
        if("Shop 17 (Birth)".equals(shopName)) {
            return "Mud Man Qubert";
        }
        if("Shop 18 (Lil Bro)".equals(shopName)) {
            return "Yiegah Kungfu";
        }
        if("Shop 19 (Big Bro)".equals(shopName)) {
            return "Yiear Kungfu";
        }
        if("Shop 20 (Twin Labs)".equals(shopName)) {
            return "Arrogant Sturdy Snake";
        }
        if("Shop 21 (Unsolvable)".equals(shopName)) {
            return "Arrogant Metagear";
        }
        if("Shop 22 (Endless)".equals(shopName)) {
            return "Affected Knimare";
        }
        if("Shop 23 (HT)".equals(shopName)) {
            return "Tailor Dracuet";
        }
        return null;
    }

    public String getShopName(String npcName) {
        npcName = npcName.replaceAll("NPC: ", "");
        if("Modro".equals(npcName)) {
            return "Shop 1 (Surface)";
        }
        if("Nebur".equals(npcName)) {
            return "Shop 2 (Surface)";
//            return "Shop 2 Alt (Surface)";
        }
        if("Sidro".equals(npcName)) {
            return "Shop 3 (Surface)";
        }
        if("Penadvent of ghost".equals(npcName)) {
            return "Shop 4 (Guidance)";
        }
        if("Mover Athleland".equals(npcName)) {
            return "Shop 5 (Illusion)";
        }
        if("Greedy Charlie".equals(npcName)) {
            return "Shop 6 (Mausoleum)";
        }
        if("Giant Mopiran".equals(npcName)) {
            return "Shop 7 (Graveyard)";
        }
        if("Shalom III".equals(npcName)) {
            return "Shop 8 (Sun)";
        }
        if("Usas VI".equals(npcName)) {
            return "Shop 9 (Sun)";
        }
        if("Kingvalley I".equals(npcName)) {
            return "Shop 10 (Sun)";
        }
        if("Kingvalley II".equals(npcName)) {
            return "Shop 11 (Moonlight)";
        }
        if("Mr. Fishman (Original)".equals(npcName)) {
            return "Shop 12 (Spring)";
        }
        if("Mr. Fishman (Alt)".equals(npcName)) {
            return "Shop 12 Alt (Spring)";
        }
        if("Energetic Belmont".equals(npcName)) {
            return "Shop 13 (Goddess)";
        }
        if("Hot-blooded Nemesistwo".equals(npcName)) {
            return "Shop 14 (Inferno)";
        }
        if("Mechanical Efspi".equals(npcName)) {
            return "Shop 15 (Ruin)";
        }
        if("Operator Combaker".equals(npcName)) {
            return "Shop 16 (Extinction)";
        }
        if("Mud Man Qubert".equals(npcName)) {
            return "Shop 17 (Birth)";
        }
        if("Yiegah Kungfu".equals(npcName)) {
            return "Shop 18 (Lil Bro)";
        }
        if("Yiear Kungfu".equals(npcName)) {
            return "Shop 19 (Big Bro)";
        }
        if("Arrogant Sturdy Snake".equals(npcName)) {
            return "Shop 20 (Twin Labs)";
        }
        if("Arrogant Metagear".equals(npcName)) {
            return "Shop 21 (Unsolvable)";
        }
        if("Affected Knimare".equals(npcName)) {
            return "Shop 22 (Endless)";
        }
        if("Tailor Dracuet".equals(npcName)) {
            return "Shop 23 (HT)";
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

    public String getNpcZone(String npcName) {
        if("Elder Xelpud".equals(npcName) || "Former Mekuri Master".equals(npcName)
                ||  "Hiner".equals(npcName) || "Moger".equals(npcName)
                || "Nebur".equals(npcName) || "Sidro".equals(npcName) || "Modro".equals(npcName)) {
            return "Surface";
        }
        if("Penadvent of ghost".equals(npcName) || "Priest Zarnac".equals(npcName)) {
            return "Gate of Guidance";
        }
        if("Priest Xanado".equals(npcName) || "Greedy Charlie".equals(npcName)) {
            return "Mausoleum of the Giants";
        }
        if("Mulbruk".equals(npcName) || "Priest Madomono".equals(npcName)
                || "Shalom III".equals(npcName) || "Usas VI".equals(npcName) || "Kingvalley I".equals(npcName)) {
            return "Temple of the Sun";
        }
        if("Philosopher Giltoriyo".equals(npcName) || "Priest Hidlyda".equals(npcName)
                || "Mr. Fishman (Original)".equals(npcName) || "Mr. Fishman (Alt)".equals(npcName)) {
            return "Spring in the Sky";
        }
        if("Priest Romancis".equals(npcName) || "Priest Gailious".equals(npcName)
                || "Hot-blooded Nemesistwo".equals(npcName)) {
            return "Inferno Cavern";
        }
        if("Priest Aramo".equals(npcName) || "Priest Triton".equals(npcName)
                || "Operator Combaker".equals(npcName)) {
            return "Chamber of Extinction";
        }
        if("Priest Jaguarfiv".equals(npcName)
                || "Yiegah Kungfu".equals(npcName) || "Yiear Kungfu".equals(npcName)
                || "Arrogant Sturdy Snake".equals(npcName) || "Arrogant Metagear".equals(npcName)) {
            return "Twin Labyrinths";
        }
        if("The Fairy Queen".equals(npcName) || "Affected Knimare".equals(npcName)) {
            return "Endless Corridor";
        }
        if("Mr. Slushfund".equals(npcName) || "Priest Alest".equals(npcName)
                || "duplex".equals(npcName) || "Stray fairy".equals(npcName)
                || "Mover Athleland".equals(npcName)) {
            return "Gate of Illusion";
        }
        if("Giant Thexde".equals(npcName) || "Giant Mopiran".equals(npcName)) {
            return "Graveyard of the Giants";
        }
        if("Philosopher Alsedana".equals(npcName) || "Samieru".equals(npcName)
                || "Kingvalley II".equals(npcName)) {
            return "Temple of Moonlight";
        }
        if("Philosopher Samaranta".equals(npcName) || "Naramura".equals(npcName)
                || "Energetic Belmont".equals(npcName)) {
            return "Tower of the Goddess";
        }
        if("Priest Laydoc".equals(npcName) || "Mechanical Efspi".equals(npcName)) {
            return "Tower of Ruin";
        }
        if("Priest Ashgine".equals(npcName) || "Mud Man Qubert".equals(npcName)) {
            return "Chamber of Birth";
        }
        if("Philosopher Fobos".equals(npcName)) {
            return "Dimensional Corridor";
        }
        if("8bit Elder".equals(npcName) || "8bit Fairy".equals(npcName)) {
            return "Gate of Time";
        }
        if("Tailor Dracuet".equals(npcName)) {
            return "Hell Temple";
        }
        return null;
    }

    public Set<String> getNpcDoors() {
        Set<String> npcDoors = new HashSet<>(mapOfNpcDoorLocationToContents.keySet());
        if(!Settings.isRandomizeDracuetShop()) {
            npcDoors.remove("NPCL: Tailor Dracuet");
        }
        return npcDoors;
    }

    public void outputLocations(BufferedWriter writer) throws IOException {
        writer.newLine();
        writer.write(Translations.getText("section.npcs"));
        writer.newLine();

        Map<String, String> translatedNpcNameToTranslatedNpcLocation = new HashMap<>();
        for(Map.Entry<String, String> npcDoorToNpc : mapOfNpcDoorLocationToContents.entrySet()) {
            String npcLocationKey = npcDoorToNpc.getKey().replaceAll("NPCL: ", "").replaceAll("[ )('-.]", "");
            String npcKey = npcDoorToNpc.getValue().replaceAll("NPC: ", "").replaceAll("[ )('-.]", "");
            String translatedNpcName = Translations.getText("npc." + npcKey);
            String translatedNpcLocation =  Translations.getText("npc." + npcLocationKey).replaceAll("：", "");
            translatedNpcNameToTranslatedNpcLocation.put(translatedNpcName, translatedNpcLocation);
        }
        List<String> translatedNpcNames = new ArrayList<>(translatedNpcNameToTranslatedNpcLocation.keySet());
        Collections.sort(translatedNpcNames, String.CASE_INSENSITIVE_ORDER);
        for(String translatedNpcName : translatedNpcNames) {
            String translatedNpcLocation = translatedNpcNameToTranslatedNpcLocation.get(translatedNpcName);
                writer.write(translatedNpcName + ": "
                        + String.format(Translations.getText("locations.LocationFormat"), translatedNpcLocation));
            writer.newLine();
        }
    }
}
