package lmr.randomizer.node;

public class CustomTransitionPlacement {
    private String targetTransition;
    private String destinationTransition;

    public CustomTransitionPlacement(String targetTransition, String destinationTransition) {
        this.targetTransition = targetTransition;
        this.destinationTransition = destinationTransition;;
    }

    public String getTargetTransition() {
        return targetTransition;
    }

    public String getDestinationTransition() {
        return destinationTransition;
    }
}
