package lmr.randomizer.node;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.random.BacksideDoorRandomizer;
import lmr.randomizer.random.ItemRandomizer;
import lmr.randomizer.random.ShopRandomizer;
import lmr.randomizer.random.TransitionGateRandomizer;

import java.util.*;

/**
 * Created by thezerothcat on 7/11/2017.
 */
public class AccessChecker {
    private static final List<String> NODES_TO_DELAY = Arrays.asList("Anchor");

    private Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject = new HashMap<>();
    private Map<String, Set<String>> mapOfRequirementsToNodeNameObject = new HashMap<>();

    private Set<String> accessedNodes = new HashSet<>();

    private List<String> queuedUpdates = new ArrayList<>();
    private Set<String> accessibleBossNodes = new HashSet<>();

    private ItemRandomizer itemRandomizer;
    private ShopRandomizer shopRandomizer;
    private BacksideDoorRandomizer backsideDoorRandomizer;
    private TransitionGateRandomizer transitionGateRandomizer;

    private int numberOfAccessibleAnkhJewels;
    private int numberOfCollectedAnkhJewels;
    private int numberOfAccessibleSacredOrbs;
    private int bossesDefeated;

    public AccessChecker() {
        mapOfNodeNameToRequirementsObject = copyRequirementsMap(DataFromFile.getMapOfNodeNameToRequirementsObject());
        mapOfRequirementsToNodeNameObject = copyNodeNameMap(DataFromFile.getMapOfRequirementsToNodeNameObject());
    }

    public AccessChecker(AccessChecker accessChecker, boolean copyAll) {
        this.mapOfNodeNameToRequirementsObject = copyRequirementsMap(accessChecker.mapOfNodeNameToRequirementsObject);
        this.mapOfRequirementsToNodeNameObject = copyNodeNameMap(accessChecker.mapOfRequirementsToNodeNameObject);
        this.itemRandomizer = copyAll ? new ItemRandomizer(accessChecker.itemRandomizer) : accessChecker.itemRandomizer;
        this.shopRandomizer = copyAll ? accessChecker.shopRandomizer.copy() : accessChecker.shopRandomizer;
        this.backsideDoorRandomizer = new BacksideDoorRandomizer(accessChecker.backsideDoorRandomizer);
        this.transitionGateRandomizer = accessChecker.transitionGateRandomizer; // Might need to copy at some point, but currently this only keeps a map/doesn't track state.
        this.accessedNodes = new HashSet<>(accessChecker.accessedNodes);
        this.accessibleBossNodes = new HashSet<>(accessChecker.accessibleBossNodes);
        this.bossesDefeated = accessChecker.bossesDefeated;
        this.numberOfAccessibleAnkhJewels = accessChecker.numberOfAccessibleAnkhJewels;
        this.numberOfCollectedAnkhJewels = accessChecker.numberOfCollectedAnkhJewels;
    }

    public void determineCursedChests(Random random) {
        List<String> cursedChests = new ArrayList<>(DataFromFile.getCustomPlacementData().getCursedChests());
        if(cursedChests.isEmpty()) {
            if(Settings.isRandomizeCursedChests()) {
                List<String> possibleChests = new ArrayList<>(DataFromFile.getChestOnlyLocations());
                if (Settings.isRandomizeCoinChests()) {
                    possibleChests.addAll(DataFromFile.getAllCoinChests());
                }
                if (Settings.isRandomizeEscapeChest()) {
                    possibleChests.add(DataFromFile.ESCAPE_CHEST_NAME);
                }
                possibleChests.removeAll(DataFromFile.getNonRandomizedItems());
                String cursedChest;
                for (int i = 0; i < 4; i++) {
                    cursedChest = possibleChests.get(random.nextInt(possibleChests.size()));
                    cursedChests.add(cursedChest);
                    possibleChests.remove(cursedChest);
                }
            }
        }
        if(!cursedChests.isEmpty()) {
            Settings.setCurrentCursedChests(cursedChests);
        }
        for(String chestLocation : Settings.getCurrentCursedChests()) {
            NodeWithRequirements chest = mapOfNodeNameToRequirementsObject.get(chestLocation);
            Set nodeSet = mapOfRequirementsToNodeNameObject.get("Mulana Talisman");
            if(nodeSet == null)
                nodeSet = new HashSet<String>();
            nodeSet.add(chestLocation);
            mapOfRequirementsToNodeNameObject.put("Mulana Talisman", nodeSet);
            for(List<String> requirementSet : chest.getAllRequirements()) {
                requirementSet.add("Mulana Talisman");
            }
        }
    }

    private static Map<String, NodeWithRequirements> copyRequirementsMap(Map<String, NodeWithRequirements> mapToCopy) {
        Map<String, NodeWithRequirements> copyMap = new HashMap<>();
        for(Map.Entry<String, NodeWithRequirements> entry : mapToCopy.entrySet()) {
            copyMap.put(entry.getKey(), new NodeWithRequirements(entry.getValue()));
        }
        return copyMap;
    }

    private static Map<String, Set<String>> copyNodeNameMap(Map<String, Set<String>> mapToCopy) {
        Map<String, Set<String>> copyMap = new HashMap<>();
        for(Map.Entry<String, Set<String>> entry : mapToCopy.entrySet()) {
            copyMap.put(entry.getKey(), new HashSet(entry.getValue()));
        }
        return copyMap;
    }

    public List<String> getQueuedUpdates() {
        return queuedUpdates;
    }

    public boolean isSuccess(Integer attemptNumber) {
        if(isRequireFullAccess()) {
            if(mapOfNodeNameToRequirementsObject.isEmpty()) {
                return true;
            }

            for(String requiredItem : DataFromFile.getWinRequirements()) {
                if(!accessedNodes.contains(requiredItem)) {
                    FileUtils.log("Win requirement not accessible: " + requiredItem + ", accessed nodes = " + accessedNodes.size());
                    if(accessedNodes.size() > 500 || Settings.isDetailedLoggingAttempt(attemptNumber)) {
                        List<String> logged = new ArrayList<>();
                        if (requiredItem.startsWith("Event:") || requiredItem.startsWith("Location:")) {
                            logAccess(requiredItem, logged);
                        }
                    }
                    return false;
                }
            }

            NodeType nodeType;
            for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
                nodeType = mapOfNodeNameToRequirementsObject.get(nodeName).getType();
                if(NodeType.STATE.equals(nodeType) || NodeType.MAP_LOCATION.equals(nodeType)
                        || NodeType.EXIT.equals(nodeType)|| NodeType.SETTING.equals(nodeType) || NodeType.TRANSITION.equals(nodeType) ) {
                    continue;
                }
                else if(NodeType.ITEM_LOCATION.equals(nodeType)) {
                    if(DataFromFile.ESCAPE_CHEST_NAME.equals(nodeName)) {
                        continue;
                    }
                    String item = itemRandomizer.getItem(nodeName);
                    if(item.startsWith("Coin:") || item.startsWith("Trap:") || Settings.getStartingItemsIncludingCustom().contains(item)) {
                        continue;
                    }
                }
                FileUtils.log("Inaccessible node detected: " + nodeName + " containing " + itemRandomizer.getItem(nodeName) + ", accessed nodes = " + accessedNodes.size());
                if(accessedNodes.size() > 500 || Settings.isDetailedLoggingAttempt(attemptNumber)) {
                    List<String> logged = new ArrayList<>();
                    logAccess(nodeName, logged);
                }
                return false;
            }
            if(isEscapeSuccess()) {
                FileUtils.log("Successful resolution found with accessed nodes = " + accessedNodes.size());
                return true;
            }
            else {
                return false;
            }
        }
        for(String requiredItem : DataFromFile.getWinRequirements()) {
            if(!accessedNodes.contains(requiredItem)) {
                FileUtils.log("Win requirement not accessible: " + requiredItem + ", accessed nodes = " + accessedNodes.size());
                if(accessedNodes.size() > 500 || Settings.isDetailedLoggingAttempt(attemptNumber)) {
                    List<String> logged = new ArrayList<>();
                    if (requiredItem.startsWith("Event:") || requiredItem.startsWith("Location:")) {
                        logAccess(requiredItem, logged);
                    }
                }
                return false;
            }
        }
        if(isEscapeSuccess()) {
            FileUtils.log("Successful resolution found with accessed nodes = " + accessedNodes.size());
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isEscapeSuccess() {
        return !Settings.isRandomizeTransitionGates()
                || new EscapeChecker(backsideDoorRandomizer, transitionGateRandomizer, itemRandomizer, shopRandomizer, accessedNodes).isSuccess();
    }

    private void logAccess(String requiredNode, List<String> loggedRequirements) {
        if(loggedRequirements.contains(requiredNode)) {
            return;
        }

        NodeWithRequirements remainingRequirements = mapOfNodeNameToRequirementsObject.get(requiredNode);
        if(remainingRequirements == null) {
            return;
        }

        loggedRequirements.add(requiredNode);
        for (List<String> requirementSet : remainingRequirements.getAllRequirements()) {
            FileUtils.log("Missing requirements for " + requiredNode + " from set: " + requirementSet);
            for (String requirement : requirementSet) {
                if(requirement.startsWith("Transition:")) {
                    FileUtils.log("Missing requirements for " + requirement + ": " + transitionGateRandomizer.getTransitionReverse(requirement));
                    logAccess(transitionGateRandomizer.getTransitionReverse(requirement), loggedRequirements);
                }

                if(!requirement.contains(": ")) {
                    continue;
                }
                remainingRequirements = mapOfNodeNameToRequirementsObject.get(requirement);
                if(remainingRequirements != null) {
                    for (List<String> requirementsForRequirement : remainingRequirements.getAllRequirements()) {
                        FileUtils.log("Missing requirements for " + requirement + ": " + requirementsForRequirement);
                        for (String requirementInner : requirementsForRequirement) {
                            if (requirementInner.startsWith("Door:")) {
                                List<String> missingRequirements = backsideDoorRandomizer.getMissingRequirements(requirementInner);
                                for(String doorRequirement : missingRequirements) {
                                    logAccess(doorRequirement, loggedRequirements);
                                }
                            }
                            else if (requirementInner.startsWith("Transition:")) {
                                FileUtils.log("Unable to access " + transitionGateRandomizer.getTransitionReverse(requirementInner));
                                logAccess(transitionGateRandomizer.getTransitionReverse(requirementInner), loggedRequirements);
                            }
                            else if (requirementInner.startsWith("Event:") || requirementInner.startsWith("State:") || requirementInner.startsWith("Location:")) {
                                logAccess(requirementInner, loggedRequirements);
                            }
                        }
                    }
                }
            }
        }

    }

    private boolean isRequireFullAccess() {
        if(!Settings.isRequireFullAccess()) {
            return false;
        }
        if(Settings.getCurrentRemovedItems().size() == 1 && "Whip".equals(Settings.getCurrentRemovedItems().iterator().next())) {
            return true;
        }
        return Settings.getRemovedItems().isEmpty() && Settings.getCurrentRemovedItems().isEmpty();
    }

    public void computeStartingLocationAccess(boolean fullValidation, Integer attemptNumber) {
        String startingLocation = Settings.getStartingLocation();
        String startingExit = startingLocation.replace("Location:", "Exit:");
        computeAccessibleNodes(startingLocation, fullValidation, attemptNumber);
        computeAccessibleNodes(startingExit, fullValidation, attemptNumber);
        queuedUpdates.addAll(transitionGateRandomizer.getTransitionExits(startingExit, attemptNumber));
        if(fullValidation) {
            queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(startingLocation, attemptNumber));
            queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(startingExit, attemptNumber));
        }
    }

    public void computeAccessibleNodes(String newState, Integer attemptNumber) {
        computeAccessibleNodes(newState, true, attemptNumber);
    }

    public void computeAccessibleNodes(String newState, boolean fullValidation, Integer attemptNumber) {
        String stateToUpdate = newState;
        if(fullValidation) {
            FileUtils.logDetail("Checking progress for node " + newState, attemptNumber);
            stateToUpdate = checkState(stateToUpdate);
            if(stateToUpdate == null) {
                return;
            }
        }
        accessedNodes.add(newState);
        accessedNodes.add(stateToUpdate);

        NodeWithRequirements node;
        Set<String> nodesToRemove = new HashSet<>();

        // If nothing requires this state, don't bother checking for newly opened nodes since there will be none.
        // Only use this shortcut during full validation, or you lose some initial nodes which cause different output to previous rando version.
        if (fullValidation) {
            if(mapOfRequirementsToNodeNameObject.containsKey(stateToUpdate)) {
                for(String nodeName : mapOfRequirementsToNodeNameObject.get(stateToUpdate)) {
                    node = mapOfNodeNameToRequirementsObject.get(nodeName);
                    if(node != null && node.updateRequirements(stateToUpdate)) {
                        FileUtils.logDetail("Gained access to node " + nodeName, attemptNumber);
                        handleNodeAccess(nodeName, node.getType(), fullValidation, attemptNumber);
                        nodesToRemove.add(nodeName);
                    }
                }
            }
        }
        else { // When not doing full validation, just use old version of this check.  It's slower but this doesn't happen many times per loop so not a big deal
            for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
                node = mapOfNodeNameToRequirementsObject.get(nodeName);
                if(node.updateRequirements(stateToUpdate)) {
                    FileUtils.logDetail("Gained access to node " + nodeName, attemptNumber);
                    handleNodeAccess(nodeName, node.getType(), fullValidation, attemptNumber);
                    nodesToRemove.add(nodeName);
                }
            }
        }

        for(String nodeToRemove : nodesToRemove) {
            mapOfNodeNameToRequirementsObject.remove(nodeToRemove);
        }

        queuedUpdates.remove(newState);
    }

    private String checkState(String stateToUpdate) {
        if(stateToUpdate.contains("Amphisbaena Accessible") || stateToUpdate.contains("Sakit Accessible")
                || stateToUpdate.contains("Ellmac Accessible") || stateToUpdate.contains("Bahamut Accessible")
                || stateToUpdate.contains("Viy Accessible") || stateToUpdate.contains("Baphomet Accessible")
                || stateToUpdate.contains("Palenque Accessible") || stateToUpdate.contains("Tiamat Accessible")) {
            accessibleBossNodes.add(stateToUpdate);
            mapOfNodeNameToRequirementsObject.remove(stateToUpdate);
            queuedUpdates.remove(stateToUpdate);
            return null;
        }
        if(stateToUpdate.startsWith("Coin:")) {
            accessedNodes.add(stateToUpdate);
            mapOfNodeNameToRequirementsObject.remove(stateToUpdate);
            queuedUpdates.remove(stateToUpdate);
            return null;
        }
        if(NODES_TO_DELAY.contains(stateToUpdate) && queuedUpdates.size() > 1) {
            // Re-add this update to the end of the queue. // todo: might want a separate queue for these things
            queuedUpdates.remove(stateToUpdate);
            queuedUpdates.add(stateToUpdate);
            return null;
        }
        if(stateToUpdate.contains("Ankh Jewel") && !stateToUpdate.equals("Ankh Jewel: 9")) {
            numberOfAccessibleAnkhJewels += 1;
            numberOfCollectedAnkhJewels += 1;
            if(numberOfCollectedAnkhJewels == 9) {
                // Alternate Mother Ankh
                queuedUpdates.add("Ankh Jewel: 9");
            }
            return "Ankh Jewel";
        }
        if(stateToUpdate.contains("Sacred Orb (")) {
            numberOfAccessibleSacredOrbs += 1;
            queuedUpdates.add("Sacred Orb: " + numberOfAccessibleSacredOrbs);
            return "Sacred Orb";
        }
        if(!"Whip".equals(Settings.getCurrentStartingWeapon()) && "Whip".equals(stateToUpdate)) {
            return null; // Whip is a removed item.
        }
        if(stateToUpdate.equals("Vessel")) {
            if(Settings.getMedicineColor() != null) {
                return String.format("Medicine of the Mind (%s)", Settings.getMedicineColor());
            }
        }
        if(stateToUpdate.contains("Amphisbaena Defeated") || stateToUpdate.contains("Sakit Defeated")
                || stateToUpdate.contains("Ellmac Defeated") || stateToUpdate.contains("Bahamut Defeated")
                || stateToUpdate.contains("Viy Defeated") || stateToUpdate.contains("Baphomet Defeated")
                || stateToUpdate.contains("Palenque Defeated") || stateToUpdate.contains("Tiamat Defeated")) {
            bossesDefeated += 1;
            if(bossesDefeated == 8 && !accessedNodes.contains("Event: All Bosses Defeated")) {
                computeAccessibleNodes("Event: All Bosses Defeated", null);
            }
        }
        return stateToUpdate;
    }

    public void markBossAccessed(String bossEventNodeName) {
        accessedNodes.add(bossEventNodeName);
        accessibleBossNodes.remove(bossEventNodeName);
        numberOfAccessibleAnkhJewels -= 1;
        NodeWithRequirements node;
        Set<String> nodesToRemove = new HashSet<>();
        for(String nodeName : mapOfRequirementsToNodeNameObject.get(bossEventNodeName)) {
            node = mapOfNodeNameToRequirementsObject.get(nodeName);
            if(node != null && node.updateRequirements(bossEventNodeName)) {
                handleNodeAccess(nodeName, node.getType(), true, null);
                nodesToRemove.add(nodeName);
            }
        }
        for(String nodeToRemove : nodesToRemove) {
            mapOfNodeNameToRequirementsObject.remove(nodeToRemove);
        }
    }

    public void markBossDefeated(String bossEventNodeName) {
        if(!accessibleBossNodes.contains(bossEventNodeName)) {
            return;
        }
        markBossAccessed(bossEventNodeName);

        bossesDefeated += 1;
        if(bossesDefeated == 8) {
            // Handle special event for all bosses defeated. This must happen before we mark the final boss as defeated
            // to ensure that items in Shrine of the Mother are recognized as no longer available.
            mapOfNodeNameToRequirementsObject.remove("Event: All Bosses Defeated");

            accessedNodes.add("Event: All Bosses Defeated");
            NodeWithRequirements node;
            Set<String> nodesToRemove = new HashSet<>();
            for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
                node = mapOfNodeNameToRequirementsObject.get(nodeName);
                if(node.updateRequirements("Event: All Bosses Defeated")) {
                    handleNodeAccess(nodeName, node.getType(), true, null);
                    nodesToRemove.add(nodeName);
                }
            }
            for(String nodeToRemove : nodesToRemove) {
                mapOfNodeNameToRequirementsObject.remove(nodeToRemove);
            }
        }

        String bossDefeatedNodeName = bossEventNodeName.replace("Accessible", "Defeated");
        mapOfNodeNameToRequirementsObject.remove(bossDefeatedNodeName);

        accessedNodes.add(bossDefeatedNodeName);
        NodeWithRequirements node;
        Set<String> nodesToRemove = new HashSet<>();
        for(String nodeName : mapOfRequirementsToNodeNameObject.get(bossDefeatedNodeName)) {
            node = mapOfNodeNameToRequirementsObject.get(nodeName);
            if(node != null && node.updateRequirements(bossDefeatedNodeName)) {
                handleNodeAccess(nodeName, node.getType(), true, null);
                nodesToRemove.add(nodeName);
            }
        }
        for(String nodeToRemove : nodesToRemove) {
            mapOfNodeNameToRequirementsObject.remove(nodeToRemove);
        }
    }

    private void handleNodeAccess(String nodeName, NodeType nodeType, boolean fullValidation, Integer attemptNumber) {
        switch (nodeType) {
            case ITEM_LOCATION:
                if(nodeName.startsWith("Coin:") && !Settings.isRandomizeCoinChests()) {
                    break;
                }
                if(nodeName.startsWith("Trap:") && !Settings.isRandomizeTrapItems()) {
                    break;
                }
                if(fullValidation) {
                    String item = itemRandomizer.getItem(nodeName);
                    if (item == null) {
                        throw new RuntimeException("Unable to find item at " + nodeName + " location of type " + nodeType.toString());
                    }
                    if (!Settings.getCurrentRemovedItems().contains(item) && !Settings.getRemovedItems().contains(item)) {
                        FileUtils.logDetail("Found item " + item, attemptNumber);
                        queuedUpdates.add(item);
                    }
                }
                else {
                    DataFromFile.getInitialNonShopItemLocations().add(nodeName);
                }
                break;
            case MAP_LOCATION:
                if(fullValidation) {
                    queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, attemptNumber));
                }
            case STATE:
            case SETTING:
                if(fullValidation) {
                    queuedUpdates.add(nodeName);
                    if(DataFromFile.GUARDIAN_DEFEATED_EVENTS.contains(nodeName)) {
                        queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, attemptNumber));
                    }
                }
                else if(!DataFromFile.GUARDIAN_DEFEATED_EVENTS.contains(nodeName)) {
                    queuedUpdates.add(nodeName);
                }
                break;
            case EXIT:
                queuedUpdates.add(nodeName);
                if(fullValidation) {
                    queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, attemptNumber));
                }
                queuedUpdates.addAll(transitionGateRandomizer.getTransitionExits(nodeName, attemptNumber));
                break;
            case SHOP:
                if(fullValidation) {
                    for (String shopItem : shopRandomizer.getShopItems(nodeName)) {
                        if (shopItem == null) {
                            throw new RuntimeException("Unable to find item at " + nodeName + " location of type " + nodeType.toString());
                        }
                        if (!accessedNodes.contains(shopItem) && !queuedUpdates.contains(shopItem)
                                && !Settings.getRemovedItems().contains(shopItem)
                                && !Settings.getCurrentRemovedItems().contains(shopItem)) {
                            FileUtils.logDetail("Found item " + shopItem, attemptNumber);
                            queuedUpdates.add(shopItem);
                        }
                    }
                }
                else {
                    DataFromFile.getInitialShops().add(nodeName);
                }
                break;
            case TRANSITION:
                queuedUpdates.add(nodeName);
                String reverseTransition = transitionGateRandomizer.getTransitionReverse(nodeName);
                if(!accessedNodes.contains(reverseTransition) && !queuedUpdates.contains(reverseTransition)) {
                    FileUtils.logDetail("Gained access to node " + reverseTransition, attemptNumber);
                    queuedUpdates.add(reverseTransition);
                    if("Transition: Goddess L2".equals(reverseTransition) ) {
                        queuedUpdates.add("Event: Special Statue Removal");
                    }
                }
                break;
        }
    }

    public boolean validRequirements(String item, String location) {
        if(DataFromFile.ESCAPE_CHEST_NAME.equals(location)) {
            // Must be something not strictly required for the seed.
            // This doesn't prevent some conditionally required items from getting placed here, but it should mostly be optional stuff.
            return DataFromFile.getRandomRemovableItems().contains(item);
        }
        if(location.contains("Shop")) {
            location = location.substring(0, location.indexOf(")") + 1);
        }

        NodeWithRequirements node = mapOfNodeNameToRequirementsObject.get(location);
        if(node == null) {
            FileUtils.log("No requirements for item " + item);
        }

        if(item.equals("Dimensional Key")) {
            if("Angel Shield".equals(location) || "beolamu.exe".equals(location) || "Sacred Orb (Dimensional Corridor)".equals(location)
                    || "Ankh Jewel (Dimensional Corridor".equals(location) || "Magatama Jewel".equals(location)
                    || "Map (Dimensional Corridor)".equals(location) || "Coin: Dimensional".equals(location)) {
                return false;
            }
        }
        else if(item.contains("Ankh Jewel")) {
            item = "Ankh Jewel";
            if("emusic.exe".equals(location) || "beolamu.exe".equals(location) || "mantra.exe".equals(location)) {
                return false;
            }
        }
        else if(item.contains("Sacred Orb")) {
            item = "Sacred Orb";
            if(location.contains("Shop") && shopRandomizer.shopHasTransformation(location)) {
                return false;
            }
            if("emusic.exe".equals(location) || "beolamu.exe".equals(location) || "mantra.exe".equals(location)) {
                return false;
            }
        }
        else if(item.startsWith("Map")) {
            // Don't put maps in conversations or torude scans, because the item-give dialog won't behave normally.
            if(DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(location) && !"Map (Surface)".equals(location)) {
                return false;
            }
            if("emusic.exe".equals(location) || "beolamu.exe".equals(location) || "mantra.exe".equals(location)) {
                return false;
            }
            if(Settings.isReplaceMapsWithWeights()
                    && !"Map (Shrine of the Mother)".equals(item) && "Shop 2 Alt (Surface)".equals(location)) {
                // Don't put removed map in transforming Surface shop.
                return false;
            }
        }
        else if(item.startsWith("Trap:")) {
            // Shop, NPC, torude scan can't give traps.
            if(location.contains("Shop")) {
                return false;
            }
            if(DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(location)) {
                return false;
            }
            if("emusic.exe".equals(location) || "beolamu.exe".equals(location) || "mantra.exe".equals(location)) {
                return false;
            }
            if(DataFromFile.getBannedTrapLocations().contains(location)) {
                // Don't put a trap here, because we don't have handling for separate screen flags, and it'll
                // look bad if opening one chest procs both traps.
                return false;
            }
        }
        else if(item.equals("Whip") || item.equals("Chain Whip") || item.equals("Buckler") || item.contains("Silver Shield")) {
            if("emusic.exe".equals(location) || "beolamu.exe".equals(location) || "mantra.exe".equals(location)) {
                return false;
            }
            if("Shop 2 Alt (Surface)".equals(location)) {
                // Don't put low/mid tier shield/whip in the shop; you can't buy it to transform the shop if you have the higher tier version.
                return false;
            }
        }

        if(Settings.getCurrentRemovedItems().contains(item)
                || Settings.getRemovedItems().contains(item)
                || Settings.getStartingItemsIncludingCustom().contains(item)) {
            if("Shop 2 Alt (Surface)".equals(location)) {
                // Don't put removed item in transforming Surface shop.
                return false;
            }
            else if(DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(location) ) {
                // Don't put removed item in conversations or instant-item locations, for now.
                return false;
            }
            else if("emusic.exe".equals(location) || "beolamu.exe".equals(location) || "mantra.exe".equals(location)) {
                // Don't put removed item in torude scan spots, for now.
                return false;
            }
        }

        if(Settings.isRandomizeCursedChests() && Settings.getMaxRandomRemovedItems() == 0
                && "Mulana Talisman".equals(item) && Settings.getCurrentCursedChests().contains(location)) {
            return false;
        }

        return mapOfNodeNameToRequirementsObject.get(location).canContainItem(item);
    }

    public boolean isEnoughAnkhJewelsToDefeatAllAccessibleBosses() {
        return numberOfAccessibleAnkhJewels >= accessibleBossNodes.size();
    }

    public void setItemRandomizer(ItemRandomizer itemRandomizer) {
        this.itemRandomizer = itemRandomizer;
    }

    public void setShopRandomizer(ShopRandomizer shopRandomizer) {
        this.shopRandomizer = shopRandomizer;
    }

    public void setBacksideDoorRandomizer(BacksideDoorRandomizer backsideDoorRandomizer) {
        this.backsideDoorRandomizer = backsideDoorRandomizer;
    }

    public void setTransitionGateRandomizer(TransitionGateRandomizer transitionGateRandomizer) {
        this.transitionGateRandomizer = transitionGateRandomizer;
    }

    public boolean updateForBosses() {
        List<Thread> threads = new ArrayList<>(accessibleBossNodes.size());
        List<AnkhJewelLockChecker> ankhJewelLockCheckers = new ArrayList<>();
        for(String bossNode : accessibleBossNodes) {
            AnkhJewelLockChecker ankhJewelLockChecker = new AnkhJewelLockChecker(new AccessChecker(this, false), bossNode);
            ankhJewelLockCheckers.add(ankhJewelLockChecker);
            Thread thread = new Thread(ankhJewelLockChecker);
            threads.add(thread);
            thread.start();
        }

        try {
            for(Thread thread : threads) {
                thread.join();
            }
        }
        catch (InterruptedException ex) {
            FileUtils.log("Error: interrupted thread while checking for ankh jewel locks");
        }

        for(AnkhJewelLockChecker ankhJewelLockChecker : ankhJewelLockCheckers) {
            if(!ankhJewelLockChecker.isEnoughAnkhJewelsToDefeatAllAccessibleBosses()) {
                return false;
            }
        }
        NodeWithRequirements bossNode;
        List<String> copyAccessibleBossNodes = new ArrayList<>(accessibleBossNodes);
        for(String accessibleBoss : copyAccessibleBossNodes) {
            bossNode = mapOfNodeNameToRequirementsObject.get(accessibleBoss.replace("Accessible", "Defeated"));
            if(bossNode != null && !accessedNodes.contains(accessibleBoss)) {
                markBossAccessed(accessibleBoss);
            }
        }
        return true;
    }
}
