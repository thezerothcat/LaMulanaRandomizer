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
    private Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject = new HashMap<>();

    private Set<String> accessedNodes = new HashSet<>();
    private Set<String> queuedUpdates = new HashSet<>();
    private Set<String> accessibleBossNodes = new HashSet<>();

    private ItemRandomizer itemRandomizer;
    private ShopRandomizer shopRandomizer;

    private int numberOfAccessibleAnkhJewels;
    private int numberOfAccessibleSacredOrbs;

    public AccessChecker() {
        mapOfNodeNameToRequirementsObject = copyRequirementsMap(DataFromFile.getMapOfNodeNameToRequirementsObject());
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
        this.itemRandomizer = accessChecker.itemRandomizer;
        this.shopRandomizer = accessChecker.shopRandomizer;
        this.accessedNodes = new HashSet<>(accessChecker.accessedNodes);
        this.accessibleBossNodes = new HashSet<>(accessChecker.accessibleBossNodes);
        this.numberOfAccessibleAnkhJewels = accessChecker.numberOfAccessibleAnkhJewels;
    }

    public Set<String> getQueuedUpdates() {
        return queuedUpdates;
    }

    public boolean isSuccess() {
        if(Settings.isFullItemAccess()) {
            if(mapOfNodeNameToRequirementsObject.isEmpty()) {
                return true;
            }
            for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
                if(!nodeName.startsWith("Glitch:")) {
                    return false;
                }
            }
            return true;
        }
        if(mapOfNodeNameToRequirementsObject.isEmpty()) {
            return false;
        }
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

        if(stateToUpdate.contains("Ankh Jewel")) {
            stateToUpdate = "Ankh Jewel";
            numberOfAccessibleAnkhJewels += 1;
        }
        if(stateToUpdate.contains("Sacred Orb (")) {
            stateToUpdate = "Sacred Orb";
            numberOfAccessibleSacredOrbs += 1;
            queuedUpdates.add("Sacred Orb: " + numberOfAccessibleSacredOrbs);
        }

        accessedNodes.add(newState);

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
                queuedUpdates.add(item);
                break;
            case MAP_LOCATION:
            case EVENT:
            case GLITCH:
                queuedUpdates.add(nodeName);
                break;
            case SHOP:
                queuedUpdates.addAll(shopRandomizer.getShopItems(nodeName));
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
        }
        else if(item.contains("Sacred Orb")) {
            item = "Sacred Orb";
        }

        return mapOfNodeNameToRequirementsObject.get(location).canContainItem(item);
    }

    public boolean isEnoughAnkhJewelsToDefeatAllAccessibleBosses() {
        return numberOfAccessibleAnkhJewels >= accessibleBossNodes.size();
    }

    public void outputRemaining(long startingSeed, int attemptNumber) throws IOException {
        BufferedWriter writer = FileUtils.getFileWriter(String.format("%s/inaccessible_%s.txt", startingSeed, attemptNumber));
        if (writer == null) {
            return;
        }

        NodeWithRequirements node;
        for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
            node = mapOfNodeNameToRequirementsObject.get(nodeName);
            for(List<String> requirementSet : node.getAllRequirements()) {
                writer.write(nodeName + " => " + requirementSet);
                writer.newLine();
            }
        }
        writer.flush();
        writer.close();

        writer = FileUtils.getFileWriter(String.format("%s/missing_item_%s.txt", startingSeed, attemptNumber));
        if (writer == null) {
            return;
        }

        List<String> inaccessibleNodes = new ArrayList<>(itemRandomizer.getAllItems());
        inaccessibleNodes.removeAll(accessedNodes);
        for(String inaccessibleNode : inaccessibleNodes) {
            writer.write(inaccessibleNode);
            writer.newLine();
        }
        writer.flush();
        writer.close();


        writer = FileUtils.getFileWriter(String.format("%s/accessible_%s.txt", startingSeed, attemptNumber));
        if (writer == null) {
            return;
        }

        for(String item : accessedNodes) {
            writer.write(item);
            writer.newLine();
        }
        writer.flush();
        writer.close();
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
