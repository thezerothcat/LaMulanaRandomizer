package lmr.randomizer.node;

import lmr.randomizer.FileUtils;
import lmr.randomizer.ItemRandomizer;
import lmr.randomizer.ShopRandomizer;
import lmr.randomizer.node.NodeWithRequirements;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 7/11/2017.
 */
public class AccessChecker {
    private Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject = new HashMap<>();
    private List<String> accessibleMapLocations = new ArrayList<>();
    private List<String> accessibleEvents = new ArrayList<>();

    private Set<String> queuedUpdates = new HashSet<>();

    private ItemRandomizer itemRandomizer;
    private ShopRandomizer shopRandomizer;

    private int minRequirementsToNextItemLocation = 1; // todo: hardcoding first pass might come back to bite me later

    public AccessChecker() {
    }

    public int getMinRequirementsToNextItemLocation() {
        return minRequirementsToNextItemLocation;
    }

    public Set<String> getQueuedUpdates() {
        return queuedUpdates;
    }

    public void addNode(String name, String requirementSet) {
        NodeWithRequirements node = mapOfNodeNameToRequirementsObject.get(name);
        if(node == null) {
            node = new NodeWithRequirements(name);
            mapOfNodeNameToRequirementsObject.put(name, node);
        }
        node.addRequirementSet(buildRequirementSet(requirementSet));
    }

    private List<String> buildRequirementSet(String requirementSet) {
        if(requirementSet == null || requirementSet.isEmpty() || "None".equalsIgnoreCase(requirementSet)) {
            return new ArrayList<>();
        }
        if(!requirementSet.contains(",")) {
            return Arrays.asList(requirementSet);
        }
        List<String> requirements = new ArrayList<>();
        for(String requirement : requirementSet.split(", ?")) {
            requirements.add(requirement);
        }
        return requirements;
    }

    public void computeAccessibleNodes(List<String> currentState) {
        NodeWithRequirements node;
        List<String> nodesToRemove = new ArrayList<>();
        for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
            node = mapOfNodeNameToRequirementsObject.get(nodeName);
            if(node.canAccessNode(currentState)) {
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
                itemRandomizer.addLocation(nodeName);
                break;
            case MAP_LOCATION:
                // wait for the current loop to finish, then call update requirements again with this location
                queuedUpdates.add(nodeName);
                break;
            case EVENT:
                // wait for the current loop to finish, then call update requirements again with this event
                queuedUpdates.add(nodeName);
                break;
            case SHOP:
                shopRandomizer.addShopAccess(nodeName);
                break;
        }
    }

    /**
     * Traverses the map of nodes, removes requirements that are met by the provided argument, and returns the minimum
     * @param newState item/event/location/shop/whatever that was just placed or made accessible
     */
    public void updateRequirements(String newState) {
        if(newState == null) {
            newState = queuedUpdates.iterator().next();
        }

        minRequirementsToNextItemLocation = Integer.MAX_VALUE;
        int currentNodeMinRequirements;
        NodeWithRequirements node;
        Set<String> nodesToRemove = new HashSet<>(); // Track the nodes that should be removed from the map, so we don't have to keep checking requirements.
        for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
            node = mapOfNodeNameToRequirementsObject.get(nodeName);
            currentNodeMinRequirements = node.updateRequirements(newState);
            if(currentNodeMinRequirements == 0) {
                // Node is now accessible
                handleNodeAccess(nodeName, node.getType());
//                    if(!NodeType.BOSS.equals(node.getType())) {
                nodesToRemove.add(nodeName);
//                    }
            }
            else if(currentNodeMinRequirements < minRequirementsToNextItemLocation) {
                minRequirementsToNextItemLocation = currentNodeMinRequirements;
            }
        }

        for(String nodeName : nodesToRemove) {
            mapOfNodeNameToRequirementsObject.remove(nodeName);
        }
        queuedUpdates.remove(newState);
    }

    public void setItemRandomizer(ItemRandomizer itemRandomizer) {
        this.itemRandomizer = itemRandomizer;
    }

    public void setShopRandomizer(ShopRandomizer shopRandomizer) {
        this.shopRandomizer = shopRandomizer;
    }

    public void outputRequirements() throws IOException {
        BufferedWriter writer = FileUtils.getFileWriter("requirements_test.txt");
        if (writer == null) {
            return;
        }

        for(String node : mapOfNodeNameToRequirementsObject.keySet()) {
            writer.write(String.format("Requirements for %s:", node));
            writer.newLine();
            for(List<String> requirementSet : mapOfNodeNameToRequirementsObject.get(node).getAllRequirements()) {
                writer.write(requirementSet.toString());
                writer.newLine();
            }
        }

        writer.newLine();
//        writer.write("Initially accessible nodes: " + accessibleNodes.toString());
//
        writer.flush();
        writer.close();
    }
}
