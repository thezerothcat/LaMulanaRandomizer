package lmr.randomizer.node;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.randomization.BacksideDoorRandomizer;
import lmr.randomizer.randomization.ItemRandomizer;
import lmr.randomizer.randomization.ShopRandomizer;
import lmr.randomizer.randomization.TransitionGateRandomizer;
import lmr.randomizer.util.LocationCoordinateMapper;

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
    private ItemRandomizer itemRandomizer;
    private ShopRandomizer shopRandomizer;

    private String successNode;

    public EscapeChecker(BacksideDoorRandomizer backsideDoorRandomizer, TransitionGateRandomizer transitionGateRandomizer,
                         ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer,
                         Set<String> accessedNodesFromValidation, String startingLocation, String successNode) {
        this.backsideDoorRandomizer = backsideDoorRandomizer;
        this.transitionGateRandomizer = transitionGateRandomizer;
        this.itemRandomizer = itemRandomizer;
        this.shopRandomizer = shopRandomizer;
        this.successNode = successNode;
        mapOfNodeNameToRequirementsObject = copyRequirementsMap(DataFromFile.getMapOfNodeNameToRequirementsObject());

        queuedUpdates.add(startingLocation);
        queuedUpdates.add(startingLocation.replace("Location:", "Exit:"));
        queuedUpdates.add("State: Escape");
        if(accessedNodesFromValidation.contains("Transition: Moonlight L1")) {
            queuedUpdates.add("State: Phase 1 Moonlight Access");
        }
        if(accessedNodesFromValidation.contains("Transition: Surface R1")) {
            queuedUpdates.add("State: Phase 1 Surface Access");
        }
        if(accessedNodesFromValidation.contains("Location: Tower of the Goddess [Grail]")
                && accessedNodesFromValidation.contains("Plane Model")) {
            queuedUpdates.add("State: Phase 1 Shield Statue Access");
        }
        if(accessedNodesFromValidation.contains("Event: Flooded Temple of the Sun")
                && accessedNodesFromValidation.contains("Seal: O3")
                && accessedNodesFromValidation.contains("Location: Temple of the Sun [East]")) {
            queuedUpdates.add("State: Phase 1 Sun Exits Opened");
        }
        for(String accessedNodeFromValidation : accessedNodesFromValidation) {
            if(!"Holy Grail".equals(accessedNodeFromValidation)
                    && !"State: Pre-Escape".equals(accessedNodeFromValidation)
                    && !accessedNodeFromValidation.endsWith(" Warp")
                    && !accessedNodeFromValidation.startsWith("Location:")
                    && !accessedNodeFromValidation.startsWith("Coin:")
                    && !accessedNodeFromValidation.startsWith("Transition:")
                    && (!accessedNodeFromValidation.startsWith("Exit:") || Settings.isRandomizeNpcs())
                    && !accessedNodeFromValidation.startsWith("NPC:")
                    && !accessedNodeFromValidation.startsWith("NPCL:")
                    && !accessedNodeFromValidation.startsWith("Door:")) {
                queuedUpdates.add(accessedNodeFromValidation);
            }
        }
        for(String queuedUpdateNode : queuedUpdates) {
            mapOfNodeNameToRequirementsObject.remove(queuedUpdateNode);
        }
        if(Settings.getEnabledGlitches().contains("Raindrop")) {
            NodeWithRequirements nodeWithRequirements = mapOfNodeNameToRequirementsObject.get(LocationCoordinateMapper.getStartingLocation());
            if(nodeWithRequirements == null) {
                nodeWithRequirements = new NodeWithRequirements(LocationCoordinateMapper.getStartingLocation());
                mapOfNodeNameToRequirementsObject.put(LocationCoordinateMapper.getStartingLocation(), nodeWithRequirements);
            }
            nodeWithRequirements.addRequirementSet(new ArrayList<>(Arrays.asList("Glitch: Raindrop", LocationCoordinateMapper.getStartingLocation().replace("Location:", "Exit:"))));
        }
        if(!LocationCoordinateMapper.isSurfaceStart() && accessedNodesFromValidation.contains("Location: Surface [Main]")) {
            NodeWithRequirements nodeWithRequirements = mapOfNodeNameToRequirementsObject.get("Location: Surface [Main]");
            if(nodeWithRequirements == null) {
                nodeWithRequirements = new NodeWithRequirements("Location: Surface [Main]");
                mapOfNodeNameToRequirementsObject.put("Location: Surface [Main]", nodeWithRequirements);
            }
            nodeWithRequirements.addRequirementSet(new ArrayList<>(Arrays.asList("Transition: Surface R1")));
        }
        backsideDoorRandomizer.rebuildRequirementsMap();
        FileUtils.log("Nodes accessible at escape start: " + queuedUpdates);
    }

    private static Map<String, NodeWithRequirements> copyRequirementsMap(Map<String, NodeWithRequirements> mapToCopy) {
        Map<String, NodeWithRequirements> copyMap = new HashMap<>();
        for(Map.Entry<String, NodeWithRequirements> entry : mapToCopy.entrySet()) {
            copyMap.put(entry.getKey(), new NodeWithRequirements(entry.getValue()));
        }
        return copyMap;
    }

    public boolean isSuccess() {
        while (!queuedUpdates.isEmpty()) {
            computeAccessibleNodes(queuedUpdates.iterator().next(), null);
            if(accessedNodes.contains(successNode)) {
                return true;
            }
        }
        if(!accessedNodes.contains(successNode)) {
            FileUtils.log("Nodes not accessed: " + mapOfNodeNameToRequirementsObject.keySet());
        }
        return accessedNodes.contains(successNode);
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
                if(!Settings.isRandomizeNpcs()) {
                    if(nodeName.startsWith("Coin:") && !Settings.isRandomizeCoinChests()) {
                        break;
                    }
                    if(nodeName.startsWith("Trap:") && !Settings.isRandomizeTrapItems()) {
                        break;
                    }
                    String item = itemRandomizer.getItem(nodeName);
                    if (item == null) {
                        throw new RuntimeException("Unable to find item at " + nodeName + " location of type " + nodeType.toString());
                    }
                    if (!Settings.getCurrentRemovedItems().contains(item) && !Settings.getRemovedItems().contains(item) && !"Holy Grail".equals(item)) {
                        FileUtils.logDetail("Found item " + item, attemptNumber);
                        queuedUpdates.add(item);
                    }
                }
                break;
            case SHOP:
                if(!Settings.isRandomizeNpcs()) {
                    for(String shopItem : shopRandomizer.getShopItems(nodeName)) {
                        if(shopItem == null) {
                            throw new RuntimeException("Unable to find item at " + nodeName + " location of type " + nodeType.toString());
                        }
                        if(!accessedNodes.contains(shopItem) && !queuedUpdates.contains(shopItem)
                                && !Settings.getRemovedItems().contains(shopItem)
                                && !Settings.getCurrentRemovedItems().contains(shopItem)) {
                            FileUtils.logDetail("Found item " + shopItem, attemptNumber);
                            queuedUpdates.add(shopItem);
                        }
                    }
                }
                break;
            case MAP_LOCATION:
                FileUtils.logDetail("Gained access to node " + nodeName, attemptNumber);
                queuedUpdates.add(nodeName);
                queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, null));
                break;
            case NPC_LOCATION:
                // todo: door contents and move npc randomizer in here
                queuedUpdates.add(nodeName);
                break;
            case NPC:
                if(!Settings.isRandomizeNpcs()) {
                    FileUtils.logDetail("Gained access to node " + nodeName, attemptNumber);
                    queuedUpdates.add(nodeName);
                }
                break;
            case STATE:
            case SETTING:
                queuedUpdates.add(nodeName);
                if(DataFromFile.GUARDIAN_DEFEATED_EVENTS.contains(nodeName) || nodeName.startsWith("Fairy:")) {
                    queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, null));
                }
                if("Event: Skanda Block Removed".equals(nodeName) || "Event: Illusion Unlocked".equals(nodeName)) {
                    if(transitionGateRandomizer.isEndlessL1Open(nodeName)) {
                        queuedUpdates.add("State: Endless L1 Open");
                    }
                }
                break;
            case EXIT:
                if(!Settings.isRandomizeNpcs()) {
                    queuedUpdates.add(nodeName);
                    queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, attemptNumber));
                    queuedUpdates.addAll(transitionGateRandomizer.getTransitionExits(nodeName, attemptNumber));
                }
                break;
            case TRANSITION:
                queuedUpdates.add(nodeName);
                String reverseTransition = transitionGateRandomizer.getTransitionReverse(nodeName);
                if(!accessedNodes.contains(reverseTransition) && !queuedUpdates.contains(reverseTransition)) {
                    FileUtils.logDetail("Gained access to node " + reverseTransition + " through reverse transition " + nodeName, attemptNumber);
                    queuedUpdates.add(reverseTransition);
                    if("Transition: Goddess L2".equals(reverseTransition) ) {
                        queuedUpdates.add("Event: Special Statue Removal");
                    }
                }
                break;
        }
    }
}
