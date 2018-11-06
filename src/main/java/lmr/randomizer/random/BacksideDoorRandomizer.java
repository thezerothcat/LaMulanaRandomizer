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
    private Map<String, NodeWithRequirements> mapOfNodeNameToDoorRequirementsObject;

    public BacksideDoorRandomizer() {
        backsideDoorLocationMap = new HashMap<>(14);
        backsideDoorBossMap = new HashMap<>(14);
        mapOfNodeNameToDoorRequirementsObject = new HashMap<>(14);
    }

    public BacksideDoorRandomizer(BacksideDoorRandomizer backsideDoorRandomizer) {
        backsideDoorLocationMap = new HashMap<>(backsideDoorRandomizer.backsideDoorLocationMap);
        backsideDoorBossMap = new HashMap<>(backsideDoorRandomizer.backsideDoorBossMap);
        mapOfNodeNameToDoorRequirementsObject = copyRequirementsMap(backsideDoorRandomizer.mapOfNodeNameToDoorRequirementsObject);
    }

    private static Map<String, NodeWithRequirements> copyRequirementsMap(Map<String, NodeWithRequirements> mapToCopy) {
        Map<String, NodeWithRequirements> copyMap = new HashMap<>();
        for(Map.Entry<String, NodeWithRequirements> entry : mapToCopy.entrySet()) {
            copyMap.put(entry.getKey(), new NodeWithRequirements(entry.getValue()));
        }
        return copyMap;
    }

    public void determineBacksideDoors(Random random) {
        if(Settings.isRandomizeBacksideDoors()) {
            randomizeBacksideDoors(random);
        }
        else {
            String fronstideDoor;
            String backsideDoor;
            for(int i = 1; i <= 7; i++) {
                fronstideDoor = "Door: F" + i;
                backsideDoor = "Door: B" + i;
                backsideDoorLocationMap.put(fronstideDoor, getDoorLocation(fronstideDoor));
                backsideDoorLocationMap.put(backsideDoor, getDoorLocation(backsideDoor));
                backsideDoorBossMap.put(fronstideDoor, i);
                backsideDoorBossMap.put(backsideDoor, i);
            }
        }

        for(String door : backsideDoorLocationMap.keySet()) {
            addToMap(door, backsideDoorLocationMap.get(door), getBoss(backsideDoorBossMap.get(door)));
        }
    }

    private void randomizeBacksideDoors(Random random) {
        List<String> frontsideDoors = new ArrayList<>(6);
        frontsideDoors.add("Door: F1");
        frontsideDoors.add("Door: F2");
        frontsideDoors.add("Door: F3");
        frontsideDoors.add("Door: F4");
        frontsideDoors.add("Door: F5");
        frontsideDoors.add("Door: F6");
        frontsideDoors.add("Door: F7");

        List<String> backsideDoors = new ArrayList<>(6);
        backsideDoors.add("Door: B1");
        backsideDoors.add("Door: B2");
        backsideDoors.add("Door: B3");
        backsideDoors.add("Door: B4");
        backsideDoors.add("Door: B5");
        backsideDoors.add("Door: B6");

        List<String> unplacedDoors = new ArrayList<>(7);
        unplacedDoors.add("Door: B2");
        unplacedDoors.add("Door: B3");
        unplacedDoors.add("Door: B4");
        unplacedDoors.add("Door: B5");
        unplacedDoors.add("Door: B6");
        unplacedDoors.add("Door: B7");

        List<Integer> bosses = new ArrayList<>(7);
        bosses.add(1);
        bosses.add(2);
        bosses.add(3);
        bosses.add(4);
        bosses.add(5);
        bosses.add(6);
        bosses.add(7);

        String doorToPlace = "Door: B1";
        String backsideDoor = backsideDoors.get(random.nextInt(backsideDoors.size()));
        backsideDoors.remove(backsideDoor);
        String location = getDoorLocation(backsideDoor);
        String reverseLocation = getDoorLocation(doorToPlace.replace("Door: B", "Door: F"));
        String frontsideDoor = getDoorToLocation(location);
        frontsideDoors.remove(frontsideDoor);
        int bossNumber = bosses.get(random.nextInt(bosses.size()));
        bosses.remove((Integer)bossNumber);
        backsideDoorLocationMap.put(doorToPlace, location);
        backsideDoorLocationMap.put(frontsideDoor, reverseLocation);
        backsideDoorBossMap.put(doorToPlace, bossNumber);
        backsideDoorBossMap.put(frontsideDoor, bossNumber);

        if(!Settings.getEnabledGlitches().contains("Lamp Glitch")) {
            doorToPlace = "Door: B4";
            backsideDoor = backsideDoors.get(random.nextInt(backsideDoors.size()));
            backsideDoors.remove(backsideDoor);
            location = getDoorLocation(backsideDoor);
            reverseLocation = getDoorLocation(doorToPlace.replace("Door: B", "Door: F"));
            frontsideDoor = getDoorToLocation(location);
            frontsideDoors.remove(frontsideDoor);
            bossNumber = bosses.get(random.nextInt(bosses.size()));
            bosses.remove((Integer)bossNumber);
            backsideDoorLocationMap.put(doorToPlace, location);
            backsideDoorLocationMap.put(frontsideDoor, reverseLocation);
            backsideDoorBossMap.put(doorToPlace, bossNumber);
            backsideDoorBossMap.put(frontsideDoor, bossNumber);
            unplacedDoors.remove(doorToPlace);
        }

        if(!Settings.getEnabledGlitches().contains("Raindrop")) {
            if(random.nextBoolean()) {
                doorToPlace = "Door: B6";
            }
            else {
                doorToPlace = "Door: B7";
            }
            backsideDoor = backsideDoors.get(random.nextInt(backsideDoors.size()));
            backsideDoors.remove(backsideDoor);
            location = getDoorLocation(backsideDoor);
            reverseLocation = getDoorLocation(doorToPlace.replace("Door: B", "Door: F"));
            frontsideDoor = getDoorToLocation(location);
            frontsideDoors.remove(frontsideDoor);
            bossNumber = bosses.get(random.nextInt(bosses.size()));
            bosses.remove((Integer)bossNumber);
            backsideDoorLocationMap.put(doorToPlace, location);
            backsideDoorLocationMap.put(frontsideDoor, reverseLocation);
            backsideDoorBossMap.put(doorToPlace, bossNumber);
            backsideDoorBossMap.put(frontsideDoor, bossNumber);
            unplacedDoors.remove(doorToPlace);
        }

        backsideDoors.add("Door: B7"); // This one is dangerous, so we'll add it only now that we've placed the dangerous frontside doors.

        while(!unplacedDoors.isEmpty()) {
            doorToPlace = unplacedDoors.get(random.nextInt(unplacedDoors.size()));
            backsideDoor = backsideDoors.get(random.nextInt(backsideDoors.size()));
            backsideDoors.remove(backsideDoor);
            location = getDoorLocation(backsideDoor);
            reverseLocation = getDoorLocation(doorToPlace.replace("Door: B", "Door: F"));
            frontsideDoor = getDoorToLocation(location);
            frontsideDoors.remove(frontsideDoor);
            bossNumber = bosses.get(random.nextInt(bosses.size()));
            bosses.remove((Integer)bossNumber);
            backsideDoorLocationMap.put(doorToPlace, location);
            backsideDoorLocationMap.put(frontsideDoor, reverseLocation);
            backsideDoorBossMap.put(doorToPlace, bossNumber);
            backsideDoorBossMap.put(frontsideDoor, bossNumber);
            unplacedDoors.remove(doorToPlace);
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

    public List<String> getAvailableNodes(String stateToUpdate) {
        if(!stateToUpdate.startsWith("Event:") && !stateToUpdate.startsWith("Location:")) {
            return new ArrayList<>(0);
        }

        NodeWithRequirements node;
        List<String> availableNodes = new ArrayList<>(1);
        for(String nodeName : mapOfNodeNameToDoorRequirementsObject.keySet()) {
            node = mapOfNodeNameToDoorRequirementsObject.get(nodeName);
            if(node.updateRequirements(stateToUpdate)) {
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
