package lmr.randomizer.random;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.node.CustomDoorPlacement;
import lmr.randomizer.node.NodeWithRequirements;
import lmr.randomizer.update.GameDataTracker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class BacksideDoorRandomizer {
    private Map<String, String> backsideDoorLocationMap;
    private Map<String, Integer> backsideDoorBossMap;
    private Map<String, String> mapOfDoorToPairDoor;
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
            String frontsideDoor;
            String backsideDoor;
            for(int i = 1; i <= 9; i++) {
                frontsideDoor = "Door: F" + i;
                backsideDoor = "Door: B" + i;
                backsideDoorLocationMap.put(frontsideDoor, getDoorLocation(frontsideDoor));
                backsideDoorLocationMap.put(backsideDoor, getDoorLocation(backsideDoor));
                mapOfDoorToPairDoor.put(frontsideDoor, backsideDoor);
                mapOfDoorToPairDoor.put(backsideDoor, frontsideDoor);
            }
            if(Settings.isRandomize2()) {
                backsideDoorLocationMap.put("Door: B8", getDoorLocation("Door: F5"));
                backsideDoorLocationMap.put("Door: B5", getDoorLocation("Door: F8"));
                mapOfDoorToPairDoor.put("Door: B8", "Door: B5");
                mapOfDoorToPairDoor.put("Door: B5", "Door: B8");

                backsideDoorLocationMap.put("Door: F8", getDoorLocation("Door: B5"));
                backsideDoorLocationMap.put("Door: F5", getDoorLocation("Door: B8"));
                mapOfDoorToPairDoor.put("Door: F8", "Door: F5");
                mapOfDoorToPairDoor.put("Door: F5", "Door: F8");
            }
        }
    }

    public void determineDoorBosses(Random random, Integer attempt) {
        if(Settings.isRandomizeBacksideDoors()) {
            backsideDoorBossMap.clear();
            randomizeBosses(random);
        }
        else {
            String frontsideDoor;
            String backsideDoor;
            for(int i = 1; i <= 7; i++) {
                frontsideDoor = "Door: F" + i;
                backsideDoor = "Door: B" + i;
                backsideDoorBossMap.put(frontsideDoor, i);
                backsideDoorBossMap.put(backsideDoor, i);
            }
            backsideDoorBossMap.put("Door: F9", 9);
            if(Settings.isRandomize2()) {
                backsideDoorBossMap.remove("Door: B5");
            }
        }

        rebuildRequirementsMap();
        logBosses(attempt);
    }

    public void rebuildRequirementsMap() {
        mapOfNodeNameToDoorRequirementsObject.clear();
        for(String door : backsideDoorLocationMap.keySet()) {
            addToMap(door, backsideDoorLocationMap.get(door), backsideDoorBossMap.get(door));
        }
    }

    private void randomizeDoorDestinations(Random random) {
        List<String> riskDoors = new ArrayList<>(4);
        riskDoors.add("Door: F1");
        riskDoors.add("Door: B7");

        List<String> unassignedDoors = new ArrayList<>(14);
        unassignedDoors.add("Door: B2");
        unassignedDoors.add("Door: B3");
        unassignedDoors.add("Door: B4");
        unassignedDoors.add("Door: B5");
        unassignedDoors.add("Door: B6");
        unassignedDoors.add("Door: F2");
        unassignedDoors.add("Door: F3");
        unassignedDoors.add("Door: F5");
        if(Settings.isRandomizeNonBossDoors()) {
            if(Settings.isRandomize2()) {
                backsideDoorLocationMap.put("Door: B8", getDoorLocation("Door: F5"));
                backsideDoorLocationMap.put("Door: B5", getDoorLocation("Door: F8"));
                mapOfDoorToPairDoor.put("Door: B8", "Door: B5");
                mapOfDoorToPairDoor.put("Door: B5", "Door: B8");

                unassignedDoors.remove("Door: F5");

                unassignedDoors.add("Door: B8");
                unassignedDoors.add("Door: B9");
                unassignedDoors.add("Door: F9");
            }
            else {
                riskDoors.add("Door: F8");
                unassignedDoors.add("Door: B8");
                unassignedDoors.add("Door: B9");
                unassignedDoors.add("Door: F9");
            }
        }
        else if(Settings.isRandomize2()) {
            backsideDoorLocationMap.put("Door: F5", getDoorLocation("Door: B8"));
            backsideDoorLocationMap.put("Door: B5", getDoorLocation("Door: F8"));
            backsideDoorLocationMap.put("Door: F8", getDoorLocation("Door: B5"));
            backsideDoorLocationMap.put("Door: B8", getDoorLocation("Door: F5"));
            backsideDoorLocationMap.put("Door: F9", getDoorLocation("Door: F9"));
            backsideDoorLocationMap.put("Door: B9", getDoorLocation("Door: B9"));

            mapOfDoorToPairDoor.put("Door: F5", "Door: F8");
            mapOfDoorToPairDoor.put("Door: B5", "Door: B8");
            mapOfDoorToPairDoor.put("Door: F8", "Door: F5");
            mapOfDoorToPairDoor.put("Door: B8", "Door: B5");
            mapOfDoorToPairDoor.put("Door: F9", "Door: B9");
            mapOfDoorToPairDoor.put("Door: B9", "Door: F9");

            unassignedDoors.remove("Door: F5");
            unassignedDoors.remove("Door: B5");
        }
        else {
            backsideDoorLocationMap.put("Door: F8", getDoorLocation("Door: F8"));
            backsideDoorLocationMap.put("Door: B8", getDoorLocation("Door: B8"));
            backsideDoorLocationMap.put("Door: F9", getDoorLocation("Door: F9"));
            backsideDoorLocationMap.put("Door: B9", getDoorLocation("Door: B9"));

            mapOfDoorToPairDoor.put("Door: F8", "Door: B8");
            mapOfDoorToPairDoor.put("Door: B8", "Door: F8");
            mapOfDoorToPairDoor.put("Door: F9", "Door: B9");
            mapOfDoorToPairDoor.put("Door: B9", "Door: F9");
        }

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
        else if(random.nextBoolean()) {
            riskDoors.add("Door: F6");
            unassignedDoors.add("Door: F7");
        } else {
            unassignedDoors.add("Door: F6");
            riskDoors.add("Door: F7");
        }

        if(Settings.getRemovedItems().contains("Fruit of Eden")) {
            // Only consider manually removed items; random removed items will be rerolled each attempt.
            riskDoors.add("Door: B1");
        }
        else {
            unassignedDoors.add("Door: B1");
        }

        String door1;
        String door2;
        String key1;
        String key2;
        for(CustomDoorPlacement customDoorPlacement : DataFromFile.getCustomPlacementData().getCustomDoorPlacements()) {
            // Plando doors are based on their location; Door: F3=Door: B7 means that Sun door goes to Nuwa door
            door1 = customDoorPlacement.getTargetDoor().replace("Door ", "Door: ");
            door2 = customDoorPlacement.getDestinationDoor().replace("Door ", "Door: ");

            key1 = swapFrontToBack(door2);
            key2 = swapFrontToBack(door1);

            backsideDoorLocationMap.put(key1, getDoorLocation(door1));
            backsideDoorLocationMap.put(key2, getDoorLocation(door2));

            mapOfDoorToPairDoor.put(key1, key2);
            mapOfDoorToPairDoor.put(key2, key1);

            riskDoors.remove(door1);
            riskDoors.remove(door2);
            unassignedDoors.remove(door1);
            unassignedDoors.remove(door2);
        }

        while(!riskDoors.isEmpty()) {
            door1 = riskDoors.get(random.nextInt(riskDoors.size()));
            riskDoors.remove(door1);

            if ("Door: F6".equals(door1) && !Settings.isRandomizeTransitionGates()) {
                // Make sure F6 and F7 don't lead to each other.
                do {
                    door2 = unassignedDoors.get(random.nextInt(unassignedDoors.size()));
                } while ("Door: F7".equals(door2));
            }
            else if ("Door: F7".equals(door1) && !Settings.isRandomizeTransitionGates()) {
                // Make sure F6 and F7 don't lead to each other.
                do {
                    door2 = unassignedDoors.get(random.nextInt(unassignedDoors.size()));
                } while ("Door: F6".equals(door2));
            }
            else {
                door2 = unassignedDoors.get(random.nextInt(unassignedDoors.size()));
            }

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
        if(Settings.isRandomizeNonBossDoors()) {
            bosses.add(9);
        }
        else {
            backsideDoorBossMap.put("Door: F9", 9);
        }

        List<String> doors = new ArrayList<>(16);
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
        if(Settings.isRandomizeNonBossDoors()) {
            doors.add("Door: F8");
            doors.add("Door: B8");
            doors.add("Door: F9");
            doors.add("Door: B9");
        }

        String door;
        String reverseDoor;
        String key1;
        String key2;
        for(CustomDoorPlacement customDoorPlacement : DataFromFile.getCustomPlacementData().getCustomDoorPlacements()) {
            if(customDoorPlacement.getAssignedBoss() != null) {
                door = customDoorPlacement.getTargetDoor().replace("Door ", "Door: ");
                reverseDoor = customDoorPlacement.getDestinationDoor().replace("Door ", "Door: ");

                key1 = swapFrontToBack(reverseDoor);
                key2 = swapFrontToBack(door);

                backsideDoorBossMap.put(key1, customDoorPlacement.getAssignedBoss());
                if(customDoorPlacement.getAssignedBoss() != null && customDoorPlacement.getAssignedBoss() != 9) {
                    backsideDoorBossMap.put(key2, customDoorPlacement.getAssignedBoss());
                }

                bosses.remove(customDoorPlacement.getAssignedBoss());
                doors.remove(key1);
                doors.remove(key2);
            }
        }

        int boss;
        List<String> availableDoors;
        while(!bosses.isEmpty()) {
            boss = bosses.get(random.nextInt(bosses.size()));
            bosses.remove((Integer)boss);

            availableDoors = new ArrayList<>(doors);
            do {
                door = availableDoors.get(random.nextInt(availableDoors.size()));
                reverseDoor = mapOfDoorToPairDoor.get(door);
                availableDoors.remove(door);
                availableDoors.remove(reverseDoor);
            } while(isInvalidAssignment(door, reverseDoor, boss) && !availableDoors.isEmpty());

            doors.remove(door);
            doors.remove(reverseDoor);

            if(boss != 9 && isInvalidAssignment(door, reverseDoor, boss)) {
                continue; // This boss couldn't be assigned to doors and will instead be skipped for now.
            }

            if(!backsideDoorLocationMap.get(door).contains("Dimensional Corridor")) {
                backsideDoorBossMap.put(door, boss);
            }

            if(!backsideDoorLocationMap.get(reverseDoor).contains("Dimensional Corridor")) {
                backsideDoorBossMap.put(reverseDoor, boss);
            }
        }
    }

    private boolean isInvalidAssignment(String door, String reverseDoor, Integer boss) {
        if(boss != 9) {
            return backsideDoorLocationMap.get(door).contains("Dimensional Corridor")
                    || backsideDoorLocationMap.get(reverseDoor).contains("Dimensional Corridor")
                    || backsideDoorLocationMap.get(door).contains("Gate of Time")
                    || backsideDoorLocationMap.get(reverseDoor).contains("Gate of Time");
        }
        return false;
    }

    private void addToMap(String doorName, String doorLocation, Integer bossNumber) {
        NodeWithRequirements node;
        List<String> doorRequirements;

        if(!doorLocation.contains("Endless Corridor [1F]")) {
            node = mapOfNodeNameToDoorRequirementsObject.get(doorName);
            if(node == null) {
                node = new NodeWithRequirements(doorName);
            }

            doorRequirements = new ArrayList<>();
            doorRequirements.add(doorLocation);
            if(bossNumber != null) {
                if(bossNumber == 9) {
                    doorRequirements.add(doorLocation.replace("Location:", "Fairy:"));
                }
                else {
                    doorRequirements.add(getBoss(bossNumber));
                    doorRequirements.add("Bronze Mirror");
                }
            }
            node.addRequirementSet(doorRequirements);
            mapOfNodeNameToDoorRequirementsObject.put(doorName, node);
        }

        String doorExit = doorName.replace("Door: ", "Exit: Door ");
        node = mapOfNodeNameToDoorRequirementsObject.get(doorExit);
        if(node == null) {
            node = new NodeWithRequirements(doorExit);
        }
        doorRequirements = new ArrayList<>();
        doorRequirements.add(doorLocation.replace("Location:", "Exit:"));
        if(bossNumber != null) {
            if(bossNumber != 9) {
                doorRequirements.add(getBoss(bossNumber));
                doorRequirements.add("Bronze Mirror");
            }
        }
        Integer reverseBossNumber = backsideDoorBossMap.get(mapOfDoorToPairDoor.get(doorName));
        if(reverseBossNumber != null && reverseBossNumber == 9) {
            doorRequirements.add(backsideDoorLocationMap.get(mapOfDoorToPairDoor.get(doorName)).replace("Location:", "Fairy:"));
        }
        node.addRequirementSet(doorRequirements);

        doorRequirements = new ArrayList<>();
        doorRequirements.add(doorName);
        node.addRequirementSet(doorRequirements);

        mapOfNodeNameToDoorRequirementsObject.put(doorExit, node);
    }

    public boolean isDoorOneWay(String doorName) {
        String doorLocation = backsideDoorLocationMap.get(doorName);
        return doorLocation.contains("Gate of Guidance") || doorLocation.contains("Tower of Ruin [Top]") || doorLocation.contains("Endless Corridor [1F]")
                || (!Settings.getEnabledGlitches().contains("Lamp Glitch") && doorLocation.contains("Inferno Cavern [Viy]"));
    }

    public List<String> getAvailableNodes(String stateToUpdate, Integer attemptNumber) {
        if(!"Bronze Mirror".equals(stateToUpdate)
                && !stateToUpdate.startsWith("Event:") && !stateToUpdate.startsWith("Location:")
                && !stateToUpdate.startsWith("Exit:") && !stateToUpdate.startsWith("Fairy:")) {
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
        if(!availableNodes.isEmpty()) {
            FileUtils.logDetail("Gained access to nodes " + availableNodes, attemptNumber);
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

            if(Settings.isRandomizeNonBossDoors() || Settings.isRandomize2()) {
                GameDataTracker.writeBacksideDoorV2(doorToReplace, doorKeyAndLocation.getKey(), backsideDoorBossMap.get(doorKeyAndLocation.getKey()));
            }
            else if(!doorToReplace.endsWith("8") && !doorToReplace.endsWith("9")) {
                GameDataTracker.writeBacksideDoor(doorToReplace, doorKeyAndLocation.getKey(), backsideDoorBossMap.get(doorKeyAndLocation.getKey()));
            }
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
        if("Door: F8".equals(door)) {
            return "Location: Endless Corridor [1F]";
        }
        if("Door: F9".equals(door)) {
            return "Location: Chamber of Extinction [Main]";
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
            return "Location: Chamber of Birth [Northeast]";
        }
        if("Door: B7".equals(door)) {
            return "Location: Tower of Ruin [Top]";
        }
        if("Door: B8".equals(door)) {
            return "Location: Dimensional Corridor [Grail]";
        }
        if("Door: B9".equals(door)) {
            return "Location: Gate of Time [Mausoleum Lower]";
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
        if("Location: Endless Corridor [1F]".equals(location)) {
            return "Door: B8";
        }
        if("Location: Chamber of Extinction [Main]".equals(location)) {
            return "Door: B9";
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
        if("Location: Chamber of Birth [Northeast]".equals(location)) {
            return "Door: F6";
        }
        if("Location: Tower of Ruin [Top]".equals(location)) {
            return "Door: F7";
        }
        if("Location: Dimensional Corridor [Grail]".equals(location)) {
            return "Door: F8";
        }
        if("Location: Gate of Time [Mausoleum Lower]".equals(location)) {
            return "Door: F9";
        }
        return null;
    }

    private String getBoss(Integer bossNumber) {
        if(bossNumber == null) {
            return null;
        }
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

    public void logLocations() {
        if(Settings.isRandomizeBacksideDoors() || Settings.isRandomize2()) {
            for (String door : backsideDoorLocationMap.keySet()) {
                FileUtils.log(Translations.getDoorLocation(backsideDoorLocationMap.get(getDoorToLocation(backsideDoorLocationMap.get(door))))
                        + " <==> "
                        + Translations.getDoorLocation(backsideDoorLocationMap.get(door)));
            }
            FileUtils.flush();
        }
    }

    public void logBosses(Integer attemptNumber) {
        if((Settings.isRandomizeBacksideDoors() || Settings.isRandomize2()) && Settings.isDetailedLoggingAttempt(attemptNumber)) {
            String doorLocation;
            for(String door : backsideDoorLocationMap.keySet()) {
                doorLocation = backsideDoorLocationMap.get(door);
                if(!doorLocation.contains("Endless Corridor [1F]")) {
                    FileUtils.log(Translations.getDoorLocation(doorLocation) + ": " + Translations.getText("bosses." + backsideDoorBossMap.get(door)));
                }
            }
        }
    }

    public List<String> getMissingRequirements(String door) {
        NodeWithRequirements nodeWithRequirements = mapOfNodeNameToDoorRequirementsObject.get(door);
        if(nodeWithRequirements == null) {
            return new ArrayList<>(0);
        }
        return nodeWithRequirements.getAllRequirements().get(0);
    }

    public void outputLocations(int attemptNumber) throws IOException {
        BufferedWriter writer = FileUtils.getFileWriter(String.format("%d/doors.txt", Settings.getStartingSeed()));
        if (writer == null) {
            return;
        }

        String doorLocation;
        for(String door : backsideDoorLocationMap.keySet()) {
            doorLocation = backsideDoorLocationMap.get(door);
            if(!doorLocation.contains("Endless Corridor [1F]")) {
                writer.write(Translations.getDoorLocation(doorLocation)
                        + " => " + Translations.getText("bosses." + backsideDoorBossMap.get(door)) + " => "
                        + Translations.getDoorLocation(backsideDoorLocationMap.get(mapOfDoorToPairDoor.get(door))));
                writer.newLine();
            }
        }

        writer.flush();
        writer.close();
    }
}
