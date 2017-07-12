package lmr.randomizer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thezerothcat on 7/11/2017.
 */
public class Requirements {
    private Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject = new HashMap<>();

    public Requirements() {

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

        writer.flush();
        writer.close();
    }
}
