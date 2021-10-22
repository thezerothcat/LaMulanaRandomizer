package lmr.randomizer.node;

public class CustomTransitionPlacement {
    private String targetTransition;
    private String destinationTransition;
    private boolean pipeTransition;

    public CustomTransitionPlacement(String targetTransition, String destinationTransition) {
        this.targetTransition = targetTransition;
        this.destinationTransition = destinationTransition;
        this.pipeTransition = false;
    }

    public CustomTransitionPlacement(String targetTransition, String destinationTransition, boolean pipeTransition) {
        this.targetTransition = targetTransition;
        this.destinationTransition = destinationTransition;
        this.pipeTransition = pipeTransition;
    }

    public String getTargetTransition() {
        return targetTransition;
    }

    public String getDestinationTransition() {
        return destinationTransition;
    }

    public boolean isPipeTransition() {
        return pipeTransition;
    }
}
