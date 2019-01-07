package lmr.randomizer.node;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.random.BacksideDoorRandomizer;
import lmr.randomizer.random.TransitionGateRandomizer;

import java.util.*;

/**
 * Created by thezerothcat on 7/11/2017.
 */
public class EscapeChecker {
    private Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject = new HashMap<>();

    private Set<String> accessedNodes = new HashSet<>();

    private List<String> queuedUpdates = new ArrayList<>();

    private BacksideDoorRandomizer backsideDoorRandomizer;
    private TransitionGateRandomizer transitionGateRandomizer;

    public EscapeChecker(BacksideDoorRandomizer backsideDoorRandomizer, TransitionGateRandomizer transitionGateRandomizer,
                         Set<String> accessedNodesFromValidation) {
        this.backsideDoorRandomizer = backsideDoorRandomizer;
        this.transitionGateRandomizer = transitionGateRandomizer;
        buildFilteredRequirementsMap();

        queuedUpdates.add("Location: True Shrine of the Mother");
        queuedUpdates.add("State: Escape");
        if(accessedNodesFromValidation.contains("Transition: Moonlight L1")) {
            queuedUpdates.add("State: Phase 1 Moonlight Access");
        }
        for(String accessedNodeFromValidation : accessedNodesFromValidation) {
            if(!"Holy Grail".equals(accessedNodeFromValidation)
                    && !accessedNodeFromValidation.startsWith("Location:")
                    && !accessedNodeFromValidation.startsWith("Coin:")
                    && !accessedNodeFromValidation.startsWith("Transition:")
                    && !accessedNodeFromValidation.startsWith("Exit:")) {
                queuedUpdates.add(accessedNodeFromValidation);
            }
        }
        for(String queuedUpdateNode : queuedUpdates) {
            mapOfNodeNameToRequirementsObject.remove(queuedUpdateNode);
        }
        FileUtils.log("Nodes accessible at escape start: " + queuedUpdates);
    }

    private static Map<String, NodeWithRequirements> copyRequirementsMap(Map<String, NodeWithRequirements> mapToCopy) {
        Map<String, NodeWithRequirements> copyMap = new HashMap<>();
        for(Map.Entry<String, NodeWithRequirements> entry : mapToCopy.entrySet()) {
            copyMap.put(entry.getKey(), new NodeWithRequirements(entry.getValue()));
        }
        return copyMap;
    }

    private void buildFilteredRequirementsMap() {
        mapOfNodeNameToRequirementsObject = copyRequirementsMap(DataFromFile.getMapOfNodeNameToRequirementsObject());
        Set<String> nodeNames = new HashSet<>(mapOfNodeNameToRequirementsObject.keySet());
        NodeType nodeType;
        for(String nodeName : nodeNames) {
            nodeType = mapOfNodeNameToRequirementsObject.get(nodeName).getType();
            if(NodeType.ITEM_LOCATION.equals(nodeType) || NodeType.SHOP.equals(nodeType)) {
                mapOfNodeNameToRequirementsObject.remove(nodeName);
            }
        }
    }

    public boolean isSuccess() {
        while (!queuedUpdates.isEmpty()) {
            computeAccessibleNodes(queuedUpdates.iterator().next(), null);
            if(accessedNodes.contains("Event: Escape")) {
                return true;
            }
        }
        if(!accessedNodes.contains("Event: Escape")) {
            FileUtils.log("Nodes not accessed: " + mapOfNodeNameToRequirementsObject.keySet());
        }
        return accessedNodes.contains("Event: Escape");
    }

    public void computeAccessibleNodes(String newState, Integer attemptNumber) {
        accessedNodes.add(newState);

        NodeWithRequirements node;
        Set<String> nodesToRemove = new HashSet<>();
        for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
            node = mapOfNodeNameToRequirementsObject.get(nodeName);
            if(node.updateRequirements(newState)) {
                handleNodeAccess(nodeName, node.getType(), attemptNumber);
                nodesToRemove.add(nodeName);
            }
        }
        for(String nodeToRemove : nodesToRemove) {
            mapOfNodeNameToRequirementsObject.remove(nodeToRemove);
        }
        queuedUpdates.remove(newState);
    }

    private void handleNodeAccess(String nodeName, NodeType nodeType, Integer attemptNumber) {
        switch (nodeType) {
            case ITEM_LOCATION:
            case SHOP:
                break;
            case MAP_LOCATION:
                FileUtils.logDetail("Gained access to node " + nodeName, attemptNumber);
                queuedUpdates.add(nodeName);
                queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, null));
                break;
            case STATE:
            case SETTING:
                queuedUpdates.add(nodeName);
                if(DataFromFile.GUARDIAN_DEFEATED_EVENTS.contains(nodeName)) {
                    queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, null));
                }
                break;
            case EXIT:
                queuedUpdates.add(nodeName);
                queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, attemptNumber));
                queuedUpdates.addAll(transitionGateRandomizer.getTransitionExits(nodeName, attemptNumber));
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
}
