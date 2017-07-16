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
        else if(name.contains("Shop")) {
            type = NodeType.SHOP;
        }
        else {
            type = NodeType.ITEM_LOCATION;
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
            requirementSet.remove(newState);
            if(requirementSet.isEmpty()) {
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
        for(List<String> requirementSet : listOfRequirementSets) {
            if(!requirementSet.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public List<List<String>> getAllRequirements() {
        return listOfRequirementSets;
    }
}
