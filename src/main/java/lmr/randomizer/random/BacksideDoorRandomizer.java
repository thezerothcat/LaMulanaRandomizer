package lmr.randomizer.random;

import javafx.util.Pair;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.node.NodeWithRequirements;
import lmr.randomizer.update.GameDataTracker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class BacksideDoorRandomizer {
    Map<String, String> backsideDoorLocationMap;
    Map<String, Integer> backsideDoorBossMap;
    Map<String, String> mapOfDoorToPairDoor;
    private Map<String, NodeWithRequirements> mapOfNodeNameToDoorRequirementsObject;

    public BacksideDoorRandomizer() {
        backsideDoorLocationMap = new HashMap<>(14);
        backsideDoorBossMap = new HashMap<>(14);
        mapOfDoorToPairDoor = new HashMap<>(14);
        mapOfNodeNameToDoorRequirementsObject = new HashMap<>(14);
    }

    public BacksideDoorRandomizer(BacksideDoorRandomizer backsideDoorRandomizer) {
        backsideDoorLocationMap = new HashMap<>(backsideDoorRandomizer.backsideDoorLocationMap);
        backsideDoorBossMap = new HashMap<>(backsideDoorRandomizer.backsideDoorBossMap);
        mapOfDoorToPairDoor = new HashMap<>(backsideDoorRandomizer.mapOfDoorToPairDoor);
        mapOfNodeNameToDoorRequirementsObject = copyRequirementsMap(backsideDoorRandomizer.mapOfNodeNameToDoorRequirementsObject);
    }

    private static Map<String, NodeWithRequirements> copyRequirementsMap(Map<String, NodeWithRequirements> mapToCopy) {
        Map<String, NodeWithRequirements> copyMap = new HashMap<>();
        for(Map.Entry<String, NodeWithRequirements> entry : mapToCopy.entrySet()) {
            copyMap.put(entry.getKey(), new NodeWithRequirements(entry.getValue()));
        }
        return copyMap;
    }

    public void determineDoorDestinations(Random random) {
        if(Settings.isRandomizeBacksideDoors()) {
            randomizeDoorDestinations(random);
        }
        else {
            String fronstideDoor;
            String backsideDoor;
            for(int i = 1; i <= 7; i++) {
                fronstideDoor = "Door: F" + i;
                backsideDoor = "Door: B" + i;
                backsideDoorLocationMap.put(fronstideDoor, getDoorLocation(fronstideDoor));
                backsideDoorLocationMap.put(backsideDoor, getDoorLocation(backsideDoor));
            }
        }
    }

    public void determineDoorBosses(Random random) {
        if(Settings.isRandomizeBacksideDoors()) {
            backsideDoorBossMap.clear();
            randomizeBosses(random);
        }
        else {
            String fronstideDoor;
            String backsideDoor;
            for(int i = 1; i <= 7; i++) {
                fronstideDoor = "Door: F" + i;
                backsideDoor = "Door: B" + i;
                backsideDoorBossMap.put(fronstideDoor, i);
                backsideDoorBossMap.put(backsideDoor, i);
            }
        }

        mapOfNodeNameToDoorRequirementsObject.clear();
        for(String door : backsideDoorLocationMap.keySet()) {
            addToMap(door, backsideDoorLocationMap.get(door), getBoss(backsideDoorBossMap.get(door)));
        }
    }

    private void randomizeDoorDestinations(Random random) {
        List<String> riskDoors = new ArrayList<>(4);
        riskDoors.add("Door: F1");
        riskDoors.add("Door: B7");

        List<String> unassignedDoors = new ArrayList<>(14);
        unassignedDoors.add("Door: B1");
        unassignedDoors.add("Door: B2");
        unassignedDoors.add("Door: B3");
        unassignedDoors.add("Door: B4");
        unassignedDoors.add("Door: B5");
        unassignedDoors.add("Door: B6");
        unassignedDoors.add("Door: F2");
        unassignedDoors.add("Door: F3");
        unassignedDoors.add("Door: F5");

        if(Settings.getEnabledGlitches().contains("Lamp Glitch")) {
            unassignedDoors.add("Door: F4");
        }
        else {
            riskDoors.add("Door: F4");
        }

        if(Settings.getEnabledGlitches().contains("Raindrop")) {
            unassignedDoors.add("Door: F6");
            unassignedDoors.add("Door: F7");
        }
        else if (random.nextBoolean()) {
            riskDoors.add("Door: F6");
            unassignedDoors.add("Door: F7");
        } else {
            unassignedDoors.add("Door: F6");
            riskDoors.add("Door: F7");
        }

        String door1;
        String door2;
        String key1;
        String key2;
        while(!riskDoors.isEmpty()) {
            door1 = riskDoors.get(random.nextInt(riskDoors.size()));
            riskDoors.remove(door1);

            door2 = unassignedDoors.get(random.nextInt(unassignedDoors.size()));
            unassignedDoors.remove(door2);

            key1 = swapFrontToBack(door2);
            key2 = swapFrontToBack(door1);

            backsideDoorLocationMap.put(key1, getDoorLocation(door1));
            backsideDoorLocationMap.put(key2, getDoorLocation(door2));

            mapOfDoorToPairDoor.put(key1, key2);
            mapOfDoorToPairDoor.put(key2, key1);
        }

        while(!unassignedDoors.isEmpty()) {
            door1 = unassignedDoors.get(random.nextInt(unassignedDoors.size()));
            unassignedDoors.remove(door1);

            door2 = unassignedDoors.get(random.nextInt(unassignedDoors.size()));
            unassignedDoors.remove(door2);

            key1 = swapFrontToBack(door2);
            key2 = swapFrontToBack(door1);

            backsideDoorLocationMap.put(key1, getDoorLocation(door1));
            backsideDoorLocationMap.put(key2, getDoorLocation(door2));

            mapOfDoorToPairDoor.put(key1, key2);
            mapOfDoorToPairDoor.put(key2, key1);
        }
    }

    private void randomizeBosses(Random random) {
        List<Integer> bosses = new ArrayList<>(7);
        bosses.add(1);
        bosses.add(2);
        bosses.add(3);
        bosses.add(4);
        bosses.add(5);
        bosses.add(6);
        bosses.add(7);

        List<String> doors = new ArrayList<>(7);
        doors.add("Door: F1");
        doors.add("Door: F2");
        doors.add("Door: F3");
        doors.add("Door: F4");
        doors.add("Door: F5");
        doors.add("Door: F6");
        doors.add("Door: F7");
        doors.add("Door: B1");
        doors.add("Door: B2");
        doors.add("Door: B3");
        doors.add("Door: B4");
        doors.add("Door: B5");
        doors.add("Door: B6");
        doors.add("Door: B7");

        int boss;
        String door;
        String reverseDoor;
        while(!bosses.isEmpty()) {
            boss = bosses.get(random.nextInt(bosses.size()));
            bosses.remove((Integer)boss);
            door = doors.get(random.nextInt(doors.size()));
            reverseDoor = mapOfDoorToPairDoor.get(door);
            doors.remove(door);
            doors.remove(reverseDoor);
            backsideDoorBossMap.put(door, boss);
            backsideDoorBossMap.put(reverseDoor, boss);
        }
    }

    private void addToMap(String doorName, String doorLocation, String requiredBoss) {
        NodeWithRequirements node = mapOfNodeNameToDoorRequirementsObject.get(doorName);
        if(node == null) {
            node = new NodeWithRequirements(doorName);
            mapOfNodeNameToDoorRequirementsObject.put(doorName, node);
        }
        List<String> doorRequirements = new ArrayList<>(2);
        doorRequirements.add(doorLocation);
        doorRequirements.add(requiredBoss);
        node.addRequirementSet(doorRequirements);

        node = mapOfNodeNameToDoorRequirementsObject.get(doorName);
        if(node == null) {
            node = new NodeWithRequirements(doorName);
            mapOfNodeNameToDoorRequirementsObject.put(doorName, node);
        }
        doorRequirements = new ArrayList<>(1);
        doorRequirements.add(doorLocation);
        doorRequirements.add(requiredBoss);
        node.addRequirementSet(doorRequirements);
    }

    public List<String> getSettingNodes() {
        List<String> settingNodes = new ArrayList<>();
        for(Map.Entry<String, String> doorKeyAndLocation : backsideDoorLocationMap.entrySet()) {
            if(doorKeyAndLocation.getValue().contains("Gate of Guidance")) {
                settingNodes.add("Setting: " + doorKeyAndLocation.getKey().replace("Door:", "Guidance"));
            }
            else if(doorKeyAndLocation.getValue().contains("Inferno Cavern [Viy]")) {
                settingNodes.add("Setting: " + doorKeyAndLocation.getKey().replace("Door:", "Viy"));
            }
        }
        return settingNodes;
    }

    public List<String> getAvailableNodes(String stateToUpdate, Integer attemptNumber) {
        if(!stateToUpdate.startsWith("Event:") && !stateToUpdate.startsWith("Location:")) {
            return new ArrayList<>(0);
        }

        NodeWithRequirements node;
        List<String> availableNodes = new ArrayList<>(1);
        for(String nodeName : mapOfNodeNameToDoorRequirementsObject.keySet()) {
            node = mapOfNodeNameToDoorRequirementsObject.get(nodeName);
            if(node.updateRequirements(stateToUpdate)) {
                FileUtils.logDetail("Gained access to node " + nodeName, attemptNumber);
                availableNodes.add(nodeName);
            }
        }
        for(String door : availableNodes) {
            mapOfNodeNameToDoorRequirementsObject.remove(door);
        }
        return availableNodes;
    }

    public void updateBacksideDoors() {
        String doorToReplace;
        for(Map.Entry<String, String> doorKeyAndLocation : backsideDoorLocationMap.entrySet()) {
            doorToReplace = getDoorToLocation(doorKeyAndLocation.getValue());
            if(doorToReplace.startsWith("Door: B")) {
                doorToReplace = doorToReplace.replace("Door: B", "Door: F");
            }
            else {
                doorToReplace = doorToReplace.replace("Door: F", "Door: B");
            }
            int bossNumber = backsideDoorBossMap.get(doorKeyAndLocation.getKey());
            GameDataTracker.writeBacksideDoor(doorToReplace, doorKeyAndLocation.getKey(), bossNumber);
        }
    }

    private String getDoorLocation(String door) {
        if("Door: F1".equals(door)) {
            return "Location: Gate of Guidance [Door]";
        }
        if("Door: F2".equals(door)) {
            return "Location: Mausoleum of the Giants";
        }
        if("Door: F3".equals(door)) {
            return "Location: Temple of the Sun [Main]";
        }
        if("Door: F4".equals(door)) {
            return "Location: Inferno Cavern [Viy]";
        }
        if("Door: F5".equals(door)) {
            return "Location: Surface [Main]";
        }
        if("Door: F6".equals(door)) {
            return "Location: Chamber of Extinction [Magatama Left]";
        }
        if("Door: F7".equals(door)) {
            return "Location: Inferno Cavern [Spikes]";
        }

        if("Door: B1".equals(door)) {
            return "Location: Gate of Illusion [Grail]";
        }
        if("Door: B2".equals(door)) {
            return "Location: Graveyard of the Giants [West]";
        }
        if("Door: B3".equals(door)) {
            return "Location: Temple of Moonlight [Lower]";
        }
        if("Door: B4".equals(door)) {
            return "Location: Tower of Ruin [Southwest Door]";
        }
        if("Door: B5".equals(door)) {
            return "Location: Tower of the Goddess [Lower]";
        }
        if("Door: B6".equals(door)) {
            return "Location: Chamber of Birth [East]";
        }
        if("Door: B7".equals(door)) {
            return "Location: Tower of Ruin [Top]";
        }
        return null;
    }

    private String getDoorToLocation(String location) {
        if("Location: Gate of Guidance [Door]".equals(location)) {
            return "Door: B1";
        }
        if("Location: Mausoleum of the Giants".equals(location)) {
            return "Door: B2";
        }
        if("Location: Temple of the Sun [Main]".equals(location)) {
            return "Door: B3";
        }
        if("Location: Inferno Cavern [Viy]".equals(location)) {
            return "Door: B4";
        }
        if("Location: Surface [Main]".equals(location)) {
            return "Door: B5";
        }
        if("Location: Chamber of Extinction [Magatama Left]".equals(location)) {
            return "Door: B6";
        }
        if("Location: Inferno Cavern [Spikes]".equals(location)) {
            return "Door: B7";
        }

        if("Location: Gate of Illusion [Grail]".equals(location)) {
            return "Door: F1";
        }
        if("Location: Graveyard of the Giants [West]".equals(location)) {
            return "Door: F2";
        }
        if("Location: Temple of Moonlight [Lower]".equals(location)) {
            return "Door: F3";
        }
        if("Location: Tower of Ruin [Southwest Door]".equals(location)) {
            return "Door: F4";
        }
        if("Location: Tower of the Goddess [Lower]".equals(location)) {
            return "Door: F5";
        }
        if("Location: Chamber of Birth [East]".equals(location)) {
            return "Door: F6";
        }
        if("Location: Tower of Ruin [Top]".equals(location)) {
            return "Door: F7";
        }
        return null;
    }

    private String getBoss(int bossNumber) {
        if(bossNumber == 1) {
            return "Event: Amphisbaena Defeated";
        }
        if(bossNumber == 2) {
            return "Event: Sakit Defeated";
        }
        if(bossNumber == 3) {
            return "Event: Ellmac Defeated";
        }
        if(bossNumber == 4) {
            return "Event: Bahamut Defeated";
        }
        if(bossNumber == 5) {
            return "Event: Viy Defeated";
        }
        if(bossNumber == 6) {
            return "Event: Palenque Defeated";
        }
        if(bossNumber == 7) {
            return "Event: Baphomet Defeated";
        }
        return null;
    }

    private String swapFrontToBack(String door) {
        return door.startsWith("Door: B")
                ? door.replace("Door: B", "Door: F")
                : door.replace("Door: F", "Door: B");
    }

    public void logLocations() throws IOException {
        for(String door : backsideDoorLocationMap.keySet()) {
            FileUtils.log(door + ": " + backsideDoorLocationMap.get(door));
        }
        FileUtils.flush();
    }

    public void outputLocations(int attemptNumber) throws IOException {
        BufferedWriter writer = FileUtils.getFileWriter(String.format("%d/doors.txt", Settings.getStartingSeed()));
        if (writer == null) {
            return;
        }

        Map<Integer, List<String>> mapOfBossToBacksideDoors = new HashMap<>(14);
        List<String> backsideDoorsForBoss;
        Pair<String, String> door1AndDoor2;
        for(Map.Entry<String, Integer> doorNameWithBossNumber : backsideDoorBossMap.entrySet()) {
            backsideDoorsForBoss = mapOfBossToBacksideDoors.get(doorNameWithBossNumber.getValue());
            if(backsideDoorsForBoss == null) {
                backsideDoorsForBoss = new ArrayList<>(2);
                mapOfBossToBacksideDoors.put(doorNameWithBossNumber.getValue(), backsideDoorsForBoss);
            }
            backsideDoorsForBoss.add(doorNameWithBossNumber.getKey());
        }

        for(int i = 1; i <= 7; i++) {
            door1AndDoor2 = sortDoors(mapOfBossToBacksideDoors.get(i));
            writer.write(Translations.getDoorLocation(backsideDoorLocationMap.get(door1AndDoor2.getKey()))
                    + " <= " + Translations.getText("bosses." + i) + " => "
                    + Translations.getDoorLocation(backsideDoorLocationMap.get(door1AndDoor2.getValue())));
            writer.newLine();
        }

        writer.flush();
        writer.close();
    }

    private Pair<String, String> sortDoors(List<String> doors) {
        String door1 = doors.get(0);
        String door2 = doors.get(1);
        if(door1.startsWith("Door: F")) {
            if(door2.startsWith("Door: B") || door1.compareTo(door2) < 1) {
                return new Pair<>(door1, door2);
            }
            return new Pair<>(door2, door1);
        }
        if(door2.startsWith("Door: F") || door2.compareTo(door1) < 1) {
            return new Pair<>(door2, door1);
        }
        return new Pair<>(door1, door2);
    }
}
