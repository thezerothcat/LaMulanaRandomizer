package lmr.randomizer.node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/11/2017.
 */
public class NodeWithRequirements {
    private List<List<String>> listOfRequirementSets = new ArrayList<>();
    private NodeType type;

    public NodeWithRequirements(String name) {
        if(name.startsWith("Event:")) {
            type = NodeType.EVENT;
        }
        else if(name.startsWith("Location:")) {
            type = NodeType.MAP_LOCATION;
        }
        else if(name.startsWith("Glitch:")) {
            type = NodeType.GLITCH;
        }
        else if(name.startsWith("Exit:")) {
            type = NodeType.EXIT;
        }
        else if(name.contains("Shop")) {
            type = NodeType.SHOP;
        }
        else if(name.contains("Attack:")) {
            type = NodeType.ATTACK;
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
