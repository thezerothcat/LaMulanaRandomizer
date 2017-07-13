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

    public boolean canAccessNode(List<String> accessibleItems) {
        if(listOfRequirementSets.isEmpty()) {
            return true;
        }

        boolean requirementsMet;
        for(List<String> requirementSet : listOfRequirementSets) {
            requirementsMet = true;
            for(String singleRequirement : requirementSet) {
                if(!accessibleItems.contains(singleRequirement)) {
                    // todo: add logic for events/locations
                    requirementsMet = false;
                    break; // Go to outer loop; this requirement set isn't met.
                }
            }
            if(requirementsMet) {
                return true;
            }
        }
        return false;
    }

    public int updateRequirements(String newState) {
        int minRequirement = Integer.MAX_VALUE;
        List<String> requirementSet;
        for(int i = 0; i < listOfRequirementSets.size(); i++) {
            requirementSet = listOfRequirementSets.get(i);
            requirementSet.remove(newState);
            if(requirementSet.isEmpty()) {
                return 0;
            }
            if(requirementSet.size() < minRequirement) {
                minRequirement = requirementSet.size();
            }
        }
        return minRequirement;
    }

    public List<List<String>> getAllRequirements() {
        return listOfRequirementSets;
    }
}
