package lmr.randomizer.randomization.data;

public class TransitionGateData {
    private String gateName;
    private String gateDestination;
    private boolean endlessL1Reverse;

    public TransitionGateData(String gateName, String gateDestination, boolean endlessL1Reverse) {
        this.gateName = gateName;
        this.gateDestination = gateDestination;
        this.endlessL1Reverse = endlessL1Reverse;
    }

    public String getGateName() {
        return gateName;
    }

    public String getGateDestination() {
        return gateDestination;
    }

    public boolean isEndlessL1Reverse() {
        return endlessL1Reverse;
    }
}
