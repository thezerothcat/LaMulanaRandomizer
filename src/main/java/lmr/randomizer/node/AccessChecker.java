package lmr.randomizer.node;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.random.ItemRandomizer;
import lmr.randomizer.random.ShopRandomizer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 7/11/2017.
 */
public class AccessChecker {
    private static final List<String> NODES_TO_DELAY = Arrays.asList("Anchor");

    private Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject = new HashMap<>();
    private static Map<String, NodeWithRequirements> mapOfNodeNameToExitRequirementsObject;

    private Set<String> accessedNodes = new HashSet<>();

    private List<String> queuedUpdates = new ArrayList<>();
    private Set<String> accessibleBossNodes = new HashSet<>();

    private ItemRandomizer itemRandomizer;
    private ShopRandomizer shopRandomizer;

    private int numberOfAccessibleAnkhJewels;
    private int numberOfAccessibleSacredOrbs;
    private int bossesDefeated;

    public AccessChecker() {
        mapOfNodeNameToRequirementsObject = copyRequirementsMap(DataFromFile.getMapOfNodeNameToRequirementsObject());
//        mapOfNodeNameToExitRequirementsObject = copyRequirementsMap(DataFromFile.getMapOfNodeNameToExitRequirementsObject());
    }

    private static Map<String, NodeWithRequirements> copyRequirementsMap(Map<String, NodeWithRequirements> mapToCopy) {
        Map<String, NodeWithRequirements> copyMap = new HashMap<>();
        for(Map.Entry<String, NodeWithRequirements> entry : mapToCopy.entrySet()) {
            copyMap.put(entry.getKey(), new NodeWithRequirements(entry.getValue()));
        }
        return copyMap;
    }

    public AccessChecker(AccessChecker accessChecker) {
        this.mapOfNodeNameToRequirementsObject = copyRequirementsMap(accessChecker.mapOfNodeNameToRequirementsObject);
//        this.mapOfNodeNameToExitRequirementsObject = copyRequirementsMap(accessChecker.mapOfNodeNameToExitRequirementsObject);
        this.itemRandomizer = accessChecker.itemRandomizer;
        this.shopRandomizer = accessChecker.shopRandomizer;
        this.accessedNodes = new HashSet<>(accessChecker.accessedNodes);
        this.accessibleBossNodes = new HashSet<>(accessChecker.accessibleBossNodes);
        this.bossesDefeated = accessChecker.bossesDefeated;
        this.numberOfAccessibleAnkhJewels = accessChecker.numberOfAccessibleAnkhJewels;
    }

    public List<String> getQueuedUpdates() {
        return queuedUpdates;
    }

    public boolean isSuccess() {
        for(String requiredItem : DataFromFile.getWinRequirements()) {
            if(!accessedNodes.contains(requiredItem)) {
                return false;
            }
        }
        return true;
    }

    public void computeAccessibleNodes(String newState) {
        String stateToUpdate = newState;
        if(stateToUpdate.contains("Amphisbaena Accessible") || stateToUpdate.contains("Sakit Accessible")
                || stateToUpdate.contains("Ellmac Accessible") || stateToUpdate.contains("Bahamut Accessible")
                || stateToUpdate.contains("Viy Accessible") || stateToUpdate.contains("Baphomet Accessible")
                || stateToUpdate.contains("Palenque Accessible") || stateToUpdate.contains("Tiamat Accessible")) {
            accessibleBossNodes.add(stateToUpdate);
            mapOfNodeNameToRequirementsObject.remove(stateToUpdate);
            queuedUpdates.remove(stateToUpdate);
            return;
        }
        if(stateToUpdate.startsWith("Coin:")) {
            accessedNodes.add(stateToUpdate);
            mapOfNodeNameToRequirementsObject.remove(stateToUpdate);
            queuedUpdates.remove(stateToUpdate);
            return;
        }

        if(NODES_TO_DELAY.contains(newState) && queuedUpdates.size() > 1) {
            // Re-add this update to the end of the queue. // todo: might want a separate queue for these things
            queuedUpdates.remove(newState);
            queuedUpdates.add(newState);
            return;
        }
        if(stateToUpdate.contains("Ankh Jewel")) {
            stateToUpdate = "Ankh Jewel";
            numberOfAccessibleAnkhJewels += 1;
        }
        if(stateToUpdate.contains("Amphisbaena Defeated") || stateToUpdate.contains("Sakit Defeated")
                || stateToUpdate.contains("Ellmac Defeated") || stateToUpdate.contains("Bahamut Defeated")
                || stateToUpdate.contains("Viy Defeated") || stateToUpdate.contains("Baphomet Defeated")
                || stateToUpdate.contains("Palenque Defeated") || stateToUpdate.contains("Tiamat Defeated")) {
            bossesDefeated += 1;
            if(bossesDefeated == 8 && !accessedNodes.contains("Event: All Bosses Defeated")) {
                computeAccessibleNodes("Event: All Bosses Defeated");
            }
        }
        if(stateToUpdate.contains("Sacred Orb (")) {
            stateToUpdate = "Sacred Orb";
            numberOfAccessibleSacredOrbs += 1;
            queuedUpdates.add("Sacred Orb: " + numberOfAccessibleSacredOrbs);
        }
        if(Settings.isRandomizeMainWeapon()
                && !"Whip".equals(Settings.getCurrentStartingWeapon()) && "Whip".equals(stateToUpdate)) {
            stateToUpdate = "Chain Whip";
        }

        accessedNodes.add(newState);
        accessedNodes.add(stateToUpdate);

        NodeWithRequirements node;
        Set<String> nodesToRemove = new HashSet<>();
        for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
            node = mapOfNodeNameToRequirementsObject.get(nodeName);
            if(node.updateRequirements(stateToUpdate)) {
                handleNodeAccess(nodeName, node.getType());
                nodesToRemove.add(nodeName);
            }
        }
        for(String nodeToRemove : nodesToRemove) {
            mapOfNodeNameToRequirementsObject.remove(nodeToRemove);
        }
        queuedUpdates.remove(newState);
    }

    public void markBossAccessed(String bossEventNodeName) {
        accessedNodes.add(bossEventNodeName);
        accessibleBossNodes.remove(bossEventNodeName);
        numberOfAccessibleAnkhJewels -= 1;
        NodeWithRequirements node;
        Set<String> nodesToRemove = new HashSet<>();
        for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
            node = mapOfNodeNameToRequirementsObject.get(nodeName);
            if(node.updateRequirements(bossEventNodeName)) {
                handleNodeAccess(nodeName, node.getType());
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
                    handleNodeAccess(nodeName, node.getType());
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
        for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
            node = mapOfNodeNameToRequirementsObject.get(nodeName);
            if(node.updateRequirements(bossDefeatedNodeName)) {
                handleNodeAccess(nodeName, node.getType());
                nodesToRemove.add(nodeName);
            }
        }
        for(String nodeToRemove : nodesToRemove) {
            mapOfNodeNameToRequirementsObject.remove(nodeToRemove);
        }
    }

    private void handleNodeAccess(String nodeName, NodeType nodeType) {
        switch (nodeType) {
            case ITEM_LOCATION:
                String item = itemRandomizer.getItem(nodeName);
                if(!Settings.getCurrentRemovedItems().contains(item)) {
                    queuedUpdates.add(item);
                }
                break;
            case MAP_LOCATION:
//                if("Location: Temple of the Sun".equals(nodeName)) {
//                    if(!accessedNodes.contains("Flare Gun Ammo") && !queuedUpdates.contains("Flare Gun Ammo")) {
//                        queuedUpdates.add("Flare Gun Ammo");
//                    }
//                }
//                else if("Location: Gate of Guidance".equals(nodeName)) {
//                    if(!accessedNodes.contains("Shuriken Ammo") && !queuedUpdates.contains("Shuriken Ammo")) {
//                        queuedUpdates.add("Shuriken Ammo");
//                    }
//                }
//                else if("Location: Temple of the Sun".equals(nodeName)) {
//                    if(!accessedNodes.contains("Rolling Shuriken Ammo") && !queuedUpdates.contains("Rolling Shuriken Ammo")) {
//                        queuedUpdates.add("Rolling Shuriken Ammo");
//                    }
//                }
//                else if("Location: Chamber of Extinction [Main]".equals(nodeName)) {
//                    if(!accessedNodes.contains("Flare Gun Ammo") && !queuedUpdates.contains("Flare Gun Ammo")) {
//                        queuedUpdates.add("Flare Gun Ammo");
//                    }
//                    if(!accessedNodes.contains("Earth Spear Ammo") && !queuedUpdates.contains("Earth Spear Ammo")) {
//                        queuedUpdates.add("Earth Spear Ammo");
//                    }
//                }
//                else if("Location: Graveyard of the Giants [West]".equals(nodeName)
//                        || "Location: Graveyard of the Giants [East]".equals(nodeName)) {
//                    if(!accessedNodes.contains("Earth Spear Ammo") && !queuedUpdates.contains("Earth Spear Ammo")) {
//                        queuedUpdates.add("Earth Spear Ammo");
//                    }
//                }
//                else if("Location: Spring in the Sky".equals(nodeName)) {
//                    if(!accessedNodes.contains("Shuriken Ammo") && !queuedUpdates.contains("Shuriken Ammo")) {
//                        queuedUpdates.add("Shuriken Ammo");
//                    }
//                    if(!accessedNodes.contains("Caltrops Ammo") && !queuedUpdates.contains("Caltrops Ammo")) {
//                        queuedUpdates.add("Caltrops Ammo");
//                    }
//                }
//                else if("Location: Tower of the Goddess [Lower]".equals(nodeName)) {
//                    if(!accessedNodes.contains("Chakram Ammo") && !queuedUpdates.contains("Chakram Ammo")) {
//                        queuedUpdates.add("Chakram Ammo");
//                    }
//                }
            case ATTACK:
            case EVENT:
            case EXIT:
            case GLITCH:
                queuedUpdates.add(nodeName);
                break;
            case SHOP:
                for(String shopItem : shopRandomizer.getShopItems(nodeName)) {
                    if(!accessedNodes.contains(shopItem) && !queuedUpdates.contains(shopItem)
                            && !Settings.getCurrentRemovedItems().contains(shopItem)) {
                        queuedUpdates.add(shopItem);
                    }
                }
                break;
        }
    }

    public boolean validRequirements(String item, String location) {
        if(location.contains("Shop")) {
            location = location.substring(0, location.indexOf(")") + 1);
        }

        NodeWithRequirements node = mapOfNodeNameToRequirementsObject.get(location);
        if(node == null) {
            FileUtils.log("No requirements for item " + item);
        }

        if(item.contains("Ankh Jewel")) {
            item = "Ankh Jewel";
            if("emusic.exe".equals(location) || "beolamu.exe".equals(location) || "mantra.exe".equals(location)) {
                return false;
            }
        }
        else if(item.contains("Sacred Orb")) {
            item = "Sacred Orb";
            if(location.contains("Shop") && shopRandomizer.shopContainsSacredOrb(location)) {
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
        else if(item.equals("Chain Whip") || item.equals("Buckler") || item.contains("Silver Shield")) {
            if("emusic.exe".equals(location) || "beolamu.exe".equals(location) || "mantra.exe".equals(location)) {
                return false;
            }
        }

        if(Settings.getCurrentRemovedItems().contains(item)
                || Settings.getRemovedItems().contains(item)
                || Settings.getStartingItems().contains(item)) {
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

        return mapOfNodeNameToRequirementsObject.get(location).canContainItem(item);
    }

    public boolean isEnoughAnkhJewelsToDefeatAllAccessibleBosses() {
        return numberOfAccessibleAnkhJewels >= accessibleBossNodes.size();
    }

    public void initExitRequirements() {
//        for(String exitNodeName : mapOfNodeNameToExitRequirementsObject.keySet()) {
//            NodeWithRequirements exitNode = new NodeWithRequirements(exitNodeName);
//            for(List<String> requirementSet : mapOfNodeNameToExitRequirementsObject.get(exitNodeName).getAllRequirements()) {
//                List<String> exitNodeRequirements = new ArrayList<>();
//                List<String> nodesToExpand = new ArrayList<>();
//                for(String requirement : requirementSet) {
//                    if(requirement.equals(exitNodeName)) {
//                        continue;
//                    }
//                    if(DataFromFile.getAllItems().contains(requirement)) {
//                        String itemLocation = itemRandomizer.findNameOfNodeContainingItem(requirement);
//                        if(DataFromFile.getMapOfExitRequirementNodeToAccessibleNodes().get(exitNodeName).contains(itemLocation)) {
//                            nodesToExpand.add(itemLocation);
//                        }
//                    } else {
//                        exitNodeRequirements.add(requirement);
//                    }
//                }
//                List<List<String>> newExitRequirementSets = new ArrayList<>();
//                for(String nodeName : nodesToExpand) {
//                    if(newExitRequirementSets.isEmpty()) {
//                        NodeWithRequirements node = mapOfNodeNameToRequirementsObject.get(nodeName);
//                        for (List<String> nodeRequirementSet : node.getAllRequirements()) {
//                            List<String> newList = new ArrayList<>(exitNodeRequirements);
//                            for(String nodeReq : nodeRequirementSet) {
//                                if(!nodeReq.equals(exitNodeName.replace("Exit:", "Location:"))) {
//                                    newList.add(nodeReq);
//                                }
//                            }
//                            newExitRequirementSets.add(newList);
//                        }
//                    }
////                    else {
////                        NodeWithRequirements node = mapOfNodeNameToRequirementsObject.get(nodeName);
////                        for (List<String> nodeRequirementSet : node.getAllRequirements()) {
////                            List<String> newList = new ArrayList<>(nodeRequirementSet);
////                            newList.addAll(exitNodeRequirements);
////                            for(List<String> existingRequirementSet : )
////                            newExitRequirementSets.add(newList);
////                        }
////                    }
//                }
//
//                for(List<String> newExitRequirementSet : newExitRequirementSets) {
//                    exitNode.addRequirementSet(newExitRequirementSet);
//                }
//            }
//            mapOfNodeNameToRequirementsObject.put(exitNodeName, exitNode);
//        }
    }

    public void setItemRandomizer(ItemRandomizer itemRandomizer) {
        this.itemRandomizer = itemRandomizer;
    }

    public void setShopRandomizer(ShopRandomizer shopRandomizer) {
        this.shopRandomizer = shopRandomizer;
    }

    public boolean updateForBosses(int attempt) {
        List<Thread> threads = new ArrayList<>(accessibleBossNodes.size());
        List<AnkhJewelLockChecker> ankhJewelLockCheckers = new ArrayList<>();
        for(String bossNode : accessibleBossNodes) {
            AnkhJewelLockChecker ankhJewelLockChecker = new AnkhJewelLockChecker(new AccessChecker(this), bossNode);
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
