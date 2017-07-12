package lmr.randomizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/11/2017.
 */
public class NodeWithRequirements {
    private String name;
    private List<List<String>> listOfRequirementSets = new ArrayList<>();

    public NodeWithRequirements(String name) {
        this.name = name;
    }

    public void addRequirementSet(List<String> requirementSet) {
        this.listOfRequirementSets.add(requirementSet);
    }

    public boolean canAccessNode(List<String> accessibleItems) {
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

    public List<List<String>> getAllRequirements() {
        return listOfRequirementSets;
    }
}
