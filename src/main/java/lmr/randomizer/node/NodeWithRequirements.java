package lmr.randomizer.node;

import lmr.randomizer.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/11/2017.
 */
public class NodeWithRequirements {
    private List<List<String>> listOfRequirementSets = new ArrayList<>();
    private NodeType type;

    public NodeWithRequirements(String name) {
        if(name.startsWith("Location:")) {
            type = NodeType.MAP_LOCATION;
        }
        else if(name.startsWith("Exit:")) {
            type = NodeType.EXIT;
        }
        else if(name.startsWith("Setting:") || name.startsWith("Glitch:")) {
            type = NodeType.SETTING;
        }
        else if(name.startsWith("Event:") || name.startsWith("State:") || name.startsWith("Fairy:") || name.startsWith("Attack:") || name.startsWith("Combo:")) {
            type = NodeType.STATE;
        }
        else if(name.startsWith("Transition:")) {
            type = NodeType.TRANSITION;
        }
        else if(name.startsWith("NPC:")) {
            type = NodeType.NPC;
        }
        else if(name.startsWith("Egg:")) {
            type = NodeType.EASTER_EGG;
        }
        else if(name.startsWith("Shop")) {
            type = NodeType.SHOP;
        }
        else {
            type = NodeType.ITEM_LOCATION;
        }
    }

    public NodeWithRequirements(NodeWithRequirements nodeWithRequirements) {
        this.type = nodeWithRequirements.type;
        for(List<String> requirementSet : nodeWithRequirements.getAllRequirements()) {
            this.listOfRequirementSets.add(new ArrayList<>(requirementSet));
        }
    }

    public NodeType getType() {
        return type;
    }

    public void addRequirementSet(List<String> requirementSet) {
        this.listOfRequirementSets.add(new ArrayList<>(requirementSet));
    }

    /**
     * This is intended to help avoid placing items somewhere they can't be reached.
     */
    public void expandRequirements() {
        for(List<String> requirementSet : listOfRequirementSets) {
            if(requirementSet.contains("Attack: Flare Gun")) {
                requirementSet.add("Flare Gun");
            }
            if(requirementSet.contains("Attack: Bomb")) {
                requirementSet.add("Bomb");
            }
            if(requirementSet.contains("State: Literacy")) {
                requirementSet.remove("State: Literacy");
                requirementSet.add("Hand Scanner");
                requirementSet.add("reader.exe");
            }
            if(requirementSet.contains("State: Extinction Light") && Settings.isRequireFlaresForExtinction()) {
                requirementSet.add("Flare Gun");
            }
            if(requirementSet.contains("State: Key Fairy Access")) {
                requirementSet.add("Isis' Pendant");
            }
            if(requirementSet.contains("State: Lamp")) {
                requirementSet.add("Lamp of Time");
            }
            if(requirementSet.contains("Event: Illusion Unlocked")) {
                requirementSet.add("Fruit of Eden");
            }
            if(requirementSet.contains("Event: Mulbruk Awakened")) {
                requirementSet.add("Origin Seal");
            }
            if(requirementSet.contains("Event: Mudmen Awakened")) {
                requirementSet.add("Cog of the Soul");
                requirementSet.add("Feather");
            }
            if(requirementSet.contains("Event: Flooded Spring in the Sky")) {
                requirementSet.add("Helmet");
                requirementSet.add("Origin Seal");
            }
            if(requirementSet.contains("Event: Remove Shrine Skulls")) {
                requirementSet.add("Dragon Bone");
                requirementSet.add("yagomap.exe");
                requirementSet.add("yagostr.exe");
                requirementSet.add("Map (Shrine of the Mother)");
            }
        }
    }

    /**
     * @param newState the new item/location/event/shop that just became accessible
     * @return true if this node is now accessible
     */
    public boolean updateRequirements(String newState) {
        List<String> requirementSet;
        for(int i = 0; i < listOfRequirementSets.size(); i++) {
            requirementSet = listOfRequirementSets.get(i);
            if(requirementSet.contains("!" + newState)) {
                requirementSet.add("INACCESSIBLE");
                return false;
            }
            requirementSet.remove(newState);
            if(requirementSet.isEmpty()) {
                return true;
            }
            boolean onlyNotRequirements = true;
            for(String requirement : requirementSet) {
                if(!requirement.startsWith("!")) {
                    onlyNotRequirements = false;
                    break;
                }
            }
            if(onlyNotRequirements) {
                return true;
            }
        }
        return false;
    }

    /**
     * // todo: currently this only checks direct requirements; should it check indirect ones also?
     * @param item an item we're considering putting in this node
     * @return true if the node can contain the item
     */
    public boolean canContainItem(String item) {
        boolean badRequirementSet;
        for(List<String> requirementSet : listOfRequirementSets) {
            if(requirementSet.contains(item)) {
                continue; // This requirement set doesn't work; try another one.
            }
            if(item.equals("Ankh Jewel")) {
                badRequirementSet = false;
                for(String requirement : requirementSet) {
                    if(requirement.contains("Amphisbaena Defeated") || requirement.contains("Sakit Defeated")
                            || requirement.contains("Ellmac Defeated") || requirement.contains("Bahamut Defeated")
                            || requirement.contains("Viy Defeated") || requirement.contains("Baphomet Defeated")
                            || requirement.contains("Palenque Defeated") || requirement.contains("Tiamat Defeated")) {
                        badRequirementSet = true;
                        break;
                    }
                }
                if(badRequirementSet) {
                    continue; // Minimize Ankh Jewel lock.
                }
            }
            return true;
        }
        return false;
    }

    public List<List<String>> getAllRequirements() {
        return listOfRequirementSets;
    }
}
