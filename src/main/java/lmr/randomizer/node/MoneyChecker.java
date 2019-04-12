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
public class MoneyChecker {
    private Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject = new HashMap<>();

    private Set<String> accessedNodes = new HashSet<>();
    private Set<String> accessedAreas = new HashSet<>();
    private Set<String> availableShops = new HashSet<>();

    private List<String> queuedUpdates = new ArrayList<>();

    private ItemRandomizer itemRandomizer;
    private ShopRandomizer shopRandomizer;
    private BacksideDoorRandomizer backsideDoorRandomizer;
    private TransitionGateRandomizer transitionGateRandomizer;

    private int numberOfAccessibleSacredOrbs;
    private int accessedMoney;

    public MoneyChecker(ItemRandomizer itemRandomizer, ShopRandomizer shopRandomizer, BacksideDoorRandomizer backsideDoorRandomizer, TransitionGateRandomizer transitionGateRandomizer) {
        accessedMoney = 0;
        mapOfNodeNameToRequirementsObject = copyRequirementsMap(DataFromFile.getMapOfNodeNameToRequirementsObject());
        this.itemRandomizer = itemRandomizer;
        this.shopRandomizer = shopRandomizer;
        this.backsideDoorRandomizer = backsideDoorRandomizer;
        this.transitionGateRandomizer = transitionGateRandomizer;
    }

    private static Map<String, NodeWithRequirements> copyRequirementsMap(Map<String, NodeWithRequirements> mapToCopy) {
        Map<String, NodeWithRequirements> copyMap = new HashMap<>();
        for(Map.Entry<String, NodeWithRequirements> entry : mapToCopy.entrySet()) {
            copyMap.put(entry.getKey(), new NodeWithRequirements(entry.getValue()));
        }
        return copyMap;
    }

    public List<String> getQueuedUpdates() {
        return queuedUpdates;
    }

    public void computeStartingLocationAccess(Integer attemptNumber) {
        String startingLocation = Settings.getStartingLocation();
        String startingExit = startingLocation.replace("Location:", "Exit:");
        computeAccessibleNodes(startingLocation, attemptNumber);
        computeAccessibleNodes(startingExit, attemptNumber);
        queuedUpdates.addAll(transitionGateRandomizer.getTransitionExits(startingExit, attemptNumber));
    }

    public void computeAccessibleNodes(String newState, Integer attemptNumber) {
        String stateToUpdate = newState;
        FileUtils.logDetail("Checking progress for node " + newState, attemptNumber);
        stateToUpdate = checkState(stateToUpdate);
        if(stateToUpdate == null) {
            return;
        }

        accessedNodes.add(newState);
        accessedNodes.add(stateToUpdate);

        NodeWithRequirements node;
        Set<String> nodesToRemove = new HashSet<>();
        for(String nodeName : mapOfNodeNameToRequirementsObject.keySet()) {
            node = mapOfNodeNameToRequirementsObject.get(nodeName);
            if(node.updateRequirements(stateToUpdate)) {
                FileUtils.logDetail("Gained access to node " + nodeName, attemptNumber);
                handleNodeAccess(nodeName, node.getType(), attemptNumber);
                nodesToRemove.add(nodeName);
            }
        }
        for(String nodeToRemove : nodesToRemove) {
            mapOfNodeNameToRequirementsObject.remove(nodeToRemove);
        }
        queuedUpdates.remove(newState);
    }

    private String checkState(String stateToUpdate) {
        if(stateToUpdate.startsWith("Coin:")) {
            accessedMoney += DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(stateToUpdate).getInventoryArg();
            accessedNodes.add(stateToUpdate);
            mapOfNodeNameToRequirementsObject.remove(stateToUpdate);
            queuedUpdates.remove(stateToUpdate);
            return null;
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
        return stateToUpdate;
    }

    public Integer getShopPrice(String itemName, String shopName) {
        if(!accessedNodes.contains("State: Fairy") && !accessedNodes.contains("Location: Dimensional Corridor [Grail]")
                && availableShops.contains(shopName) && accessedAreas.size() <= 3) {
            if("Plane Model".equals(itemName)
                    && accessedAreas.contains("Tower of the Goddess")) {
                return accessedMoney < 50 ? accessedMoney : 50;
            }
            if("Ankh Jewel".equals(itemName) && accessedAreas.contains("Mausoleum of the Giants")) {
                return accessedMoney < 40 ? accessedMoney : 40;
            }
            if("Bronze Mirror".equals(itemName)
                    && (accessedAreas.contains("Gate of Guidance") || accessedAreas.contains("Mausoleum of the Giants"))
                    || accessedAreas.contains("Temple of the Sun")) {
                return accessedMoney < 50 ? accessedMoney : 50;
            }
            if("Helmet".equals(itemName) && accessedAreas.contains("Spring in the Sky")) {
                return accessedMoney < 50 ? accessedMoney : 50;
            }
            if("Feather".equals(itemName)) {
                if(accessedAreas.contains("Graveyard of the Giants")) {
                    return accessedMoney < 50 ? accessedMoney : 50;
                }
                if(accessedAreas.contains("Shrine of the Mother")) {
                    return accessedMoney < 50 ? accessedMoney : 50;
                }
                if(accessedAreas.contains("Tower of the Goddess")) {
                    return accessedMoney < 80 ? accessedMoney : 80;
                }
                if(accessedAreas.contains("Chamber of Birth")) {
                    return accessedMoney < 30 ? accessedMoney : 30;
                }
                if(accessedAreas.contains("Dimensional Corridor")) {
                    return accessedMoney < 80 ? accessedMoney : 80;
                }
                if(accessedAreas.contains("Tower of Ruin")) {
                    return accessedMoney < 80 ? accessedMoney : 80;
                }
            }
            if("Fruit of Eden".equals(itemName) && accessedNodes.contains("Location: Gate of Illusion [Eden]")) {
                return accessedMoney < 50 ? accessedMoney : 50;
            }
            if("Hand Scanner".equals(itemName) && accessedAreas.contains("Gate of Illusion")) {
                return accessedMoney < 50 ? accessedMoney : 50;
            }
            if("Bomb".equals(itemName) && accessedAreas.contains("Graveyard of the Giants")) {
                return accessedMoney < 50 ? accessedMoney : 50;
            }
            if("Bomb".equals(itemName) && accessedAreas.contains("Dimensional Corridor") && Settings.getEnabledDamageBoosts().contains("Item")) {
                return accessedMoney < 20 ? accessedMoney : 20; // Extra cheap because ammo is also needed
            }
            if("Ring".equals(itemName) && accessedAreas.contains("Graveyard of the Giants")) {
                return accessedMoney < 80 ? accessedMoney : 80;
            }
            if("Grapple Claw".equals(itemName)
                    && ((accessedAreas.contains("Graveyard of the Giants") && Settings.getEnabledGlitches().contains("Ice Raindrop"))
                    || (Settings.getEnabledGlitches().contains("Raindrop")))) {
                return accessedMoney < 50 ? accessedMoney : 50;
            }
            if("Hermes' Boots".equals(itemName)
                    && (Settings.getEnabledGlitches().contains("Raindrop") || Settings.getEnabledGlitches().contains("Object Zip"))) {
                return accessedMoney < 80 ? accessedMoney : 80;
            }
            if(accessedAreas.contains("Temple of Moonlight")
                    && ("Shuriken".equals(itemName) || "Rolling Shuriken".equals(itemName)
                    || "Caltrops".equals(itemName) || "Bomb".equals(itemName)
                    || "Chakram".equals(itemName) || "Pistol".equals(itemName))) {
                if(!accessedNodes.contains("Attack: Shuriken") && !accessedNodes.contains("Attack: Rolling Shuriken")
                        && !accessedNodes.contains("Attack: Caltrops") && !accessedNodes.contains("Attack: Bomb")
                        && !accessedNodes.contains("Attack: Chakram") && !accessedNodes.contains("Attack: Pistol")) {
                    return accessedMoney < 50 ? accessedMoney : 50;
                }
            }
            if("Flare Gun".equals(itemName) && accessedAreas.contains("Chamber of Extinction")) {
                return accessedMoney < 50 ? accessedMoney : 50;
            }
            if("Key of Eternity".equals(itemName) && accessedAreas.contains("Endless Corridor")) {
                return accessedMoney < 50 ? accessedMoney : 50;
            }
        }
        return null;
    }

    private void handleNodeAccess(String nodeName, NodeType nodeType, Integer attemptNumber) {
        switch (nodeType) {
            case ITEM_LOCATION:
                if(nodeName.startsWith("Coin:") && !Settings.isRandomizeCoinChests()) {
                    accessedMoney += DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(nodeName).getInventoryArg();
                    accessedNodes.add(nodeName);
                    break;
                }
                if(nodeName.startsWith("Trap:") && !Settings.isRandomizeTrapItems()) {
                    break;
                }
                String item = itemRandomizer.getItem(nodeName);
                if (item == null) {
                    throw new RuntimeException("Unable to find item at " + nodeName + " location of type " + nodeType.toString());
                }
                if (!Settings.getCurrentRemovedItems().contains(item) && !Settings.getRemovedItems().contains(item)) {
                    FileUtils.logDetail("Found item " + item, attemptNumber);
                    queuedUpdates.add(item);
                }
                if("Bronze Mirror".equals(nodeName)) {
                    // Coin pots in Bronze Mirror area
                    accessedMoney += 10; // Probably more than 10, but safer not to overestimate.
                }
                break;
            case MAP_LOCATION:
                accessedMoney += getLocationMoneyValue(nodeName);
                String area = getAreaFromNode(nodeName);
                if(area != null) {
                    accessedAreas.add(area);
                }
                queuedUpdates.add(nodeName);
                queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, attemptNumber));
                break;
            case STATE:
//                if(nodeName.endsWith(" Defeated")) {
//                    accessedMoney += getEnemyMoneyValue(nodeName);
//                }
            case SETTING:
                queuedUpdates.add(nodeName);
                if(nodeName.startsWith("Fairy:")) {
                    queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, attemptNumber));
                }
                break;
            case EXIT:
                queuedUpdates.add(nodeName);
                queuedUpdates.addAll(backsideDoorRandomizer.getAvailableNodes(nodeName, attemptNumber));
                queuedUpdates.addAll(transitionGateRandomizer.getTransitionExits(nodeName, attemptNumber));
                break;
            case SHOP:
                availableShops.add(nodeName);
//                for (String shopItem : shopRandomizer.getShopItems(nodeName)) {
//                    if (shopItem == null) {
//                        throw new RuntimeException("Unable to find item at " + nodeName + " location of type " + nodeType.toString());
//                    }
//                    if (!accessedNodes.contains(shopItem) && !queuedUpdates.contains(shopItem)
//                            && !Settings.getRemovedItems().contains(shopItem)
//                            && !Settings.getCurrentRemovedItems().contains(shopItem)) {
//                        availableShopItems.add(shopItem);
//                    }
//                }
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

    private int getLocationMoneyValue(String locationNodeName) {
        if(locationNodeName.equals("Location: Surface [Main]")) {
            return 40;
        }
        if(locationNodeName.equals("Location: Surface [Ruin Path Lower]")) {
            return 10;
        }
        if(locationNodeName.equals("Location: Gate of Guidance [Main]")) {
            return 60;
        }
        if(locationNodeName.equals("Location: Mausoleum of the Giants")) {
            return 70; // Includes Ghost Lord
        }
        if(locationNodeName.equals("Location: Graveyard of the Giants [West]")) {
            return 30;
        }
        if(locationNodeName.equals("Location: Graveyard of the Giants [Grail]")) {
            return 10;
        }
        if(locationNodeName.equals("Location: Graveyard of the Giants [East]")) {
            return 30;
        }
        if(locationNodeName.equals("Location: Temple of the Sun [Main]")) {
            return 30;
        }
        if(locationNodeName.equals("Location: Temple of the Sun [West]")) {
            return 10;
        }
        if(locationNodeName.equals("Location: Temple of Moonlight [Pyramid]")) {
            return 50;
        }
        if(locationNodeName.equals("Location: Spring in the Sky")) {
            return 30;
        }
        if(locationNodeName.equals("Location: Tower of the Goddess [Lower]")) {
            return 10;
        }
        if(locationNodeName.equals("Location: Inferno Cavern [Main]")) {
            return 10;
        }
        if(locationNodeName.equals("Location: Tower of Ruin [Southeast]")) {
            return 10;
        }
        if(locationNodeName.equals("Location: Tower of Ruin [Southwest]")) {
            return 10;
        }
        if(locationNodeName.equals("Location: Tower of Ruin [Grail]")) {
            return 10;
        }
        if(locationNodeName.equals("Location: Chamber of Extinction [Main]")) {
            return 10;
        }
        if(locationNodeName.equals("Location: Chamber of Extinction [Ankh Lower]")) {
            return 10;
        }
        if(locationNodeName.equals("Location: Twin Labyrinths [Loop]")) {
            return 30;
        }
        if(locationNodeName.equals("Location: Twin Labyrinths [Lower]")) {
            return 20;
        }
        if(locationNodeName.equals("Location: Endless Corridor [1F]")) {
            return 30;
        }
        if(locationNodeName.equals("Location: Endless Corridor [5F]")) {
            return 100;
        }
        if(locationNodeName.equals("Location: Dimensional Corridor [Grail]")) {
            return 20; // Girtablilu
        }
        return 0;
    }

//    // location requirements must be accounted for before using this
//    private int getEnemyMoneyValue(String eventNodeName) {
//        if(eventNodeName.equals("Event: Buer Defeated")) {
//            return 40;
//        }
//        if(eventNodeName.equals("Event: Nuckelavee Defeated")) {
//            return 50;
//        }
//        if(eventNodeName.equals("Event: Pazuzu Defeated")) {
//            return 50;
//        }
//        if(eventNodeName.equals("Event: Argus Defeated")) {
//            return 50;
//        }
//        if(eventNodeName.equals("Event: Chi You Defeated")) {
//            return 40;
//        }
//        if(eventNodeName.equals("Event: Kamaitachi Defeated")) {
//            return 30;
//        }
//        if(eventNodeName.equals("Event: Peryton Defeated")) {
//            return 40;
//        }
//        return 0;
//    }

    private String getAreaFromNode(String nodeName) {
        if(nodeName.contains("Surface")) {
            return "Gate of Guidance";
        }
        if(nodeName.contains("Gate of Guidance")) {
            return "Gate of Guidance";
        }
        if(nodeName.contains("Gate of Illusion")) {
            return "Gate of Guidance";
        }
        if(nodeName.contains("Mausoleum of the Giants")) {
            return "Mausoleum of the Giants";
        }
        if(nodeName.contains("Graveyard of the Giants")) {
            return "Graveyard of the Giants";
        }
        if(nodeName.contains("Temple of the Sun")) {
            return "Temple of the Sun";
        }
        if(nodeName.contains("Temple of Moonlight")) {
            return "Temple of Moonlight";
        }
        if(nodeName.contains("Spring in the Sky")) {
            return "Spring in the Sky";
        }
        if(nodeName.contains("Tower of the Goddess")) {
            return "Tower of the Goddess";
        }
        if(nodeName.contains("Inferno Cavern")) {
            return "Inferno Cavern";
        }
        if(nodeName.contains("Tower of Ruin")) {
            return "Tower of Ruin";
        }
        if(nodeName.contains("Chamber of Extinction")) {
            return "Chamber of Extinction";
        }
        if(nodeName.contains("Chamber of Birth")) {
            return "Chamber of Birth";
        }
        if(nodeName.contains("Twin Labyrinths")) {
            return "Twin Labyrinths";
        }
        if(nodeName.contains("Endless Corridor")) {
            return "Endless Corridor";
        }
        if(nodeName.contains("Dimensional Corridor")) {
            return "Dimensional Corridor";
        }
        if(nodeName.contains("Shrine of the Mother")) {
            return "Shrine of the Mother";
        }
        return null;
    }
}