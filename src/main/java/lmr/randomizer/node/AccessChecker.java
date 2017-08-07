package lmr.randomizer.node;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
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

    private List<String> accessedNodes = new ArrayList<>();
    private Set<String> queuedUpdates = new HashSet<>();

    private ItemRandomizer itemRandomizer;
    private ShopRandomizer shopRandomizer;

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

    public Set<String> getQueuedUpdates() {
        return queuedUpdates;
    }

    public boolean isSuccess() {
        return mapOfNodeNameToRequirementsObject.isEmpty();
    }

    public void computeAccessibleNodes(String newState) {
        String stateToUpdate = newState;
        if(stateToUpdate.contains("Ankh Jewel")) {
            stateToUpdate = "Ankh Jewel";
        }
        if(stateToUpdate.contains("Sacred Orb")) {
            stateToUpdate = "Sacred Orb";
        }

        accessedNodes.add(newState);

        NodeWithRequirements node;
        List<String> nodesToRemove = new ArrayList<>();
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

    private void handleNodeAccess(String nodeName, NodeType nodeType) {
        switch (nodeType) {
            case ITEM_LOCATION:
                String item = itemRandomizer.getItem(nodeName);
                queuedUpdates.add(item);
                break;
            case MAP_LOCATION:
                queuedUpdates.add(nodeName);
                break;
            case EVENT:
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

        writer = FileUtils.getFileWriter(String.format("target/missing_item%s_%s.txt", startingSeed, attemptNumber));
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


        writer = FileUtils.getFileWriter(String.format("target/accessible%s_%s.txt", startingSeed, attemptNumber));
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
}
