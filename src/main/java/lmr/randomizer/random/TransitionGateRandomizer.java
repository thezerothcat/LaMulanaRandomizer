package lmr.randomizer.random;

import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.update.GameDataTracker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class TransitionGateRandomizer {
    Map<String, String> transitionGateDestinationMap;

    public TransitionGateRandomizer() {
        transitionGateDestinationMap = new HashMap<>();
    }

    public void determineGateDestinations(Random random) {
        if(Settings.isRandomizeTransitionGates()) {
            transitionGateDestinationMap.clear();
            randomizeGateDestinations(random);
        }
        else if(transitionGateDestinationMap.isEmpty()) {
            transitionGateDestinationMap.put("Transition: Surface R1", "Transition: Guidance L1");
            transitionGateDestinationMap.put("Transition: Surface D1", "Transition: Inferno U2");
            transitionGateDestinationMap.put("Transition: Surface D2", "Transition: Extinction U2");

            transitionGateDestinationMap.put("Transition: Guidance L1", "Transition: Surface R1");
            transitionGateDestinationMap.put("Transition: Guidance U1", "Transition: Spring D1");
            transitionGateDestinationMap.put("Transition: Guidance D1", "Transition: Mausoleum U1");
            transitionGateDestinationMap.put("Transition: Guidance D2", "Transition: Sun U1");

            transitionGateDestinationMap.put("Transition: Mausoleum L1", "Transition: Endless R1");
            transitionGateDestinationMap.put("Transition: Mausoleum U1", "Transition: Guidance D1");
            transitionGateDestinationMap.put("Transition: Mausoleum D1", "Transition: Twin U1");

            transitionGateDestinationMap.put("Transition: Sun L1", "Transition: Inferno R1");
            transitionGateDestinationMap.put("Transition: Sun R1", "Transition: Extinction L1");
            transitionGateDestinationMap.put("Transition: Sun R2", "Transition: Extinction L2");
            transitionGateDestinationMap.put("Transition: Sun U1", "Transition: Guidance D2");

            transitionGateDestinationMap.put("Transition: Spring D1", "Transition: Guidance U1");

            transitionGateDestinationMap.put("Transition: Inferno R1", "Transition: Sun L1");
            transitionGateDestinationMap.put("Transition: Inferno U1", "Transition: Twin D1");
            transitionGateDestinationMap.put("Transition: Inferno U2", "Transition: Surface D1");

            transitionGateDestinationMap.put("Transition: Extinction L1", "Transition: Sun R1"); // todo: ignore?
            transitionGateDestinationMap.put("Transition: Extinction L2", "Transition: Sun R2"); // todo: ignore?
            transitionGateDestinationMap.put("Transition: Extinction U1", "Transition: Shrine D1"); // todo: ignore?
            transitionGateDestinationMap.put("Transition: Extinction U2", "Transition: Surface D2");

            transitionGateDestinationMap.put("Transition: Twin U1", "Transition: Mausoleum D1");
            transitionGateDestinationMap.put("Transition: Twin U2", "Transition: Shrine D3");
            transitionGateDestinationMap.put("Transition: Twin U3", "Transition: Dimensional D1");
            transitionGateDestinationMap.put("Transition: Twin D1", "Transition: Inferno U1");
            transitionGateDestinationMap.put("Transition: Twin D2", "Transition: Moonlight U2");

            transitionGateDestinationMap.put("Transition: Endless R1", "Transition: Mausoleum L1");
            transitionGateDestinationMap.put("Transition: Endless D1", "Transition: Shrine U1");
            transitionGateDestinationMap.put("Transition: Endless U1", "Transition: Shrine D2");

            transitionGateDestinationMap.put("Transition: Shrine U1", "Transition: Endless D1");
            transitionGateDestinationMap.put("Transition: Shrine D1", "Transition: Extinction U1");
            transitionGateDestinationMap.put("Transition: Shrine D2", "Transition: Endless U1");
            transitionGateDestinationMap.put("Transition: Shrine D3", "Transition: Twin U1");

            transitionGateDestinationMap.put("Transition: Illusion D1", "Transition: Graveyard U1");
            transitionGateDestinationMap.put("Transition: Illusion D2", "Transition: Moonlight U1");
            transitionGateDestinationMap.put("Transition: Illusion R1", "Transition: Goddess L1");
            transitionGateDestinationMap.put("Transition: Illusion R2", "Transition: Ruin L1");

            transitionGateDestinationMap.put("Transition: Graveyard L1", "Transition: Ruin R2");
            transitionGateDestinationMap.put("Transition: Graveyard R1", "Transition: Moonlight L1");
            transitionGateDestinationMap.put("Transition: Graveyard U1", "Transition: Illusion D1");
            transitionGateDestinationMap.put("Transition: Graveyard U2", "Transition: Goddess D1");
            transitionGateDestinationMap.put("Transition: Graveyard D1", "Transition: Birth U1");

            transitionGateDestinationMap.put("Transition: Moonlight L1", "Transition: Graveyard R1");
            transitionGateDestinationMap.put("Transition: Moonlight U1", "Transition: Illusion D2");
            transitionGateDestinationMap.put("Transition: Moonlight U2", "Transition: Twin D2");

            transitionGateDestinationMap.put("Transition: Goddess L1", "Transition: Illusion R1");
            transitionGateDestinationMap.put("Transition: Goddess L2", "Transition: Ruin R1");
            transitionGateDestinationMap.put("Transition: Goddess U1", "Transition: Birth D1");
            transitionGateDestinationMap.put("Transition: Goddess D1", "Transition: Graveyard U1");

            transitionGateDestinationMap.put("Transition: Ruin L1", "Transition: Illusion R2");
            transitionGateDestinationMap.put("Transition: Ruin R1", "Transition: Goddess L2");
            transitionGateDestinationMap.put("Transition: Ruin R2", "Transition: Graveyard L1");

            transitionGateDestinationMap.put("Transition: Birth L1", "Transition: Birth R1");
            transitionGateDestinationMap.put("Transition: Birth R1", "Transition: Birth L1");
            transitionGateDestinationMap.put("Transition: Birth U1", "Transition: Graveyard D1");
            transitionGateDestinationMap.put("Transition: Birth D1", "Transition: Goddess U1");

            transitionGateDestinationMap.put("Transition: Dimensional D1", "Transition: Twin U3");
        }
    }

    private void randomizeGateDestinations(Random random) {
        transitionGateDestinationMap.put("Transition: Sun R1", "Transition: Extinction L1");
        transitionGateDestinationMap.put("Transition: Sun R2", "Transition: Extinction L2");

        transitionGateDestinationMap.put("Transition: Extinction L1", "Transition: Sun R1"); // todo: ignore?
        transitionGateDestinationMap.put("Transition: Extinction L2", "Transition: Sun R2"); // todo: ignore?
        transitionGateDestinationMap.put("Transition: Extinction U1", "Transition: Shrine D1"); // todo: ignore?

        transitionGateDestinationMap.put("Transition: Twin U3", "Transition: Dimensional D1");

        transitionGateDestinationMap.put("Transition: Endless D1", "Transition: Shrine U1");
        transitionGateDestinationMap.put("Transition: Endless U1", "Transition: Shrine D2");

        transitionGateDestinationMap.put("Transition: Shrine U1", "Transition: Endless D1");
        transitionGateDestinationMap.put("Transition: Shrine D1", "Transition: Extinction U1");
        transitionGateDestinationMap.put("Transition: Shrine D2", "Transition: Endless U1");

        transitionGateDestinationMap.put("Transition: Illusion D1", "Transition: Graveyard U1");
        transitionGateDestinationMap.put("Transition: Illusion R1", "Transition: Goddess L1");
        transitionGateDestinationMap.put("Transition: Illusion R2", "Transition: Ruin L1");

        transitionGateDestinationMap.put("Transition: Graveyard U1", "Transition: Illusion D1");
        transitionGateDestinationMap.put("Transition: Graveyard U2", "Transition: Goddess D1");

        transitionGateDestinationMap.put("Transition: Goddess L1", "Transition: Illusion R1");
        transitionGateDestinationMap.put("Transition: Goddess L2", "Transition: Ruin R1");
        transitionGateDestinationMap.put("Transition: Goddess U1", "Transition: Birth D1");
        transitionGateDestinationMap.put("Transition: Goddess D1", "Transition: Graveyard U1");

        transitionGateDestinationMap.put("Transition: Ruin L1", "Transition: Illusion R2");
        transitionGateDestinationMap.put("Transition: Ruin R1", "Transition: Goddess L2");

        transitionGateDestinationMap.put("Transition: Birth L1", "Transition: Birth R1");
        transitionGateDestinationMap.put("Transition: Birth R1", "Transition: Birth L1");

        transitionGateDestinationMap.put("Transition: Dimensional D1", "Transition: Twin U3");

        List<String> leftTransitions = new ArrayList<>();
        List<String> rightTransitions = new ArrayList<>();
        List<String> upTransitions = new ArrayList<>();
        List<String> downTransitions = new ArrayList<>();

        leftTransitions.add("Transition: Guidance L1");
        leftTransitions.add("Transition: Mausoleum L1");
        leftTransitions.add("Transition: Graveyard L1");
        leftTransitions.add("Transition: Sun L1");
        leftTransitions.add("Transition: Moonlight L1");
        rightTransitions.add("Transition: Surface R1");
        rightTransitions.add("Transition: Inferno R1");
        rightTransitions.add("Transition: Graveyard R1");
        rightTransitions.add("Transition: Ruin R2");
        rightTransitions.add("Transition: Endless R1");
        upTransitions.add("Transition: Guidance U1");
        upTransitions.add("Transition: Mausoleum U1");
        upTransitions.add("Transition: Sun U1");
        upTransitions.add("Transition: Moonlight U1");
        upTransitions.add("Transition: Moonlight U2");
        upTransitions.add("Transition: Goddess U1");
        upTransitions.add("Transition: Inferno U1");
        upTransitions.add("Transition: Inferno U2");
        upTransitions.add("Transition: Extinction U2");
        upTransitions.add("Transition: Birth U1");
        upTransitions.add("Transition: Twin U1");
        upTransitions.add("Transition: Twin U2");
        downTransitions.add("Transition: Surface D1");
        downTransitions.add("Transition: Surface D2");
        downTransitions.add("Transition: Guidance D1");
        downTransitions.add("Transition: Guidance D2");
        downTransitions.add("Transition: Illusion D2");
        downTransitions.add("Transition: Mausoleum D1");
        downTransitions.add("Transition: Graveyard D1");
        downTransitions.add("Transition: Spring D1");
        downTransitions.add("Transition: Birth D1");
        downTransitions.add("Transition: Twin D1");
        downTransitions.add("Transition: Twin D2");
        downTransitions.add("Transition: Shrine D3");

        String chosenTransitionStart;
        String chosenTransitionEnd;
        while(!leftTransitions.isEmpty()) {
            chosenTransitionStart = leftTransitions.get(random.nextInt(leftTransitions.size()));
            leftTransitions.remove(chosenTransitionStart);
            chosenTransitionEnd = rightTransitions.get(random.nextInt(rightTransitions.size()));
            rightTransitions.remove(chosenTransitionEnd);

            transitionGateDestinationMap.put(chosenTransitionStart, chosenTransitionEnd);
            transitionGateDestinationMap.put(chosenTransitionEnd, chosenTransitionStart);
        }

        while(!upTransitions.isEmpty()) {
            chosenTransitionStart = upTransitions.get(random.nextInt(upTransitions.size()));
            upTransitions.remove(chosenTransitionStart);
            chosenTransitionEnd = downTransitions.get(random.nextInt(downTransitions.size()));
            downTransitions.remove(chosenTransitionEnd);

            transitionGateDestinationMap.put(chosenTransitionStart, chosenTransitionEnd);
            transitionGateDestinationMap.put(chosenTransitionEnd, chosenTransitionStart);
        }
    }

    public String getTransitionReverse(String transitionReached) {
        return transitionGateDestinationMap.get(transitionReached);
    }

    public void updateTransitions() {
        for(Map.Entry<String, String> gateStartAndEnd : transitionGateDestinationMap.entrySet()) {
            GameDataTracker.writeTransitionGate(gateStartAndEnd.getKey(), gateStartAndEnd.getValue());
        }
    }

    public void outputLocations(int attemptNumber) throws IOException {
        BufferedWriter writer = FileUtils.getFileWriter(String.format("%d/gates.txt", Settings.getStartingSeed()));
        if (writer == null) {
            return;
        }

        // todo: log

        writer.flush();
        writer.close();
    }
}
