package lmr.randomizer.randomization;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.node.CustomTransitionPlacement;
import lmr.randomizer.randomization.data.TransitionGateData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class TransitionGateRandomizer {
    private BacksideDoorRandomizer backsideDoorRandomizer;

    private Map<String, String> transitionGateDestinationMap;

    public TransitionGateRandomizer(BacksideDoorRandomizer backsideDoorRandomizer) {
        this.backsideDoorRandomizer = backsideDoorRandomizer;
        transitionGateDestinationMap = new HashMap<>();
    }

    public void determineGateDestinations(Random random) {
        if(Settings.isRandomizeTransitionGates()) {
            transitionGateDestinationMap.clear();
            randomizeHorizontalTransitions(random);
            randomizeVerticalTransitions(random);
            randomizeDuplicateTransition(random);
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
            transitionGateDestinationMap.put("Transition: Inferno W1", "Transition: Extinction U3");

            transitionGateDestinationMap.put("Transition: Extinction L1", "Transition: Sun R1");
            transitionGateDestinationMap.put("Transition: Extinction L2", "Transition: Sun R2");
            transitionGateDestinationMap.put("Transition: Extinction U1", "Transition: Shrine D1");
            transitionGateDestinationMap.put("Transition: Extinction U2", "Transition: Surface D2");
            transitionGateDestinationMap.put("Transition: Extinction U3", "Transition: Inferno W1");

            transitionGateDestinationMap.put("Transition: Twin U1", "Transition: Mausoleum D1");
            transitionGateDestinationMap.put("Transition: Twin U2", "Transition: Shrine D3");
            transitionGateDestinationMap.put("Transition: Twin U3", "Transition: Dimensional D1");
            transitionGateDestinationMap.put("Transition: Twin D1", "Transition: Inferno U1");
            transitionGateDestinationMap.put("Transition: Twin D2", "Transition: Moonlight U2");

            transitionGateDestinationMap.put("Transition: Endless R1", "Transition: Mausoleum L1");
            transitionGateDestinationMap.put("Transition: Endless D1", "Transition: Shrine U1");
            transitionGateDestinationMap.put("Transition: Endless U1", "Transition: Shrine D2");
            transitionGateDestinationMap.put("Transition: Endless L1", "Transition: Endless R1");

            transitionGateDestinationMap.put("Transition: Shrine U1", "Transition: Endless D1");
            transitionGateDestinationMap.put("Transition: Shrine D1", "Transition: Extinction U1");
            transitionGateDestinationMap.put("Transition: Shrine D2", "Transition: Endless U1");
            transitionGateDestinationMap.put("Transition: Shrine D3", "Transition: Twin U2");

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
            transitionGateDestinationMap.put("Transition: Goddess D1", "Transition: Graveyard U2");
            transitionGateDestinationMap.put("Transition: Goddess W1", "Transition: Retromausoleum D1");

            transitionGateDestinationMap.put("Transition: Ruin L1", "Transition: Illusion R2");
            transitionGateDestinationMap.put("Transition: Ruin R1", "Transition: Goddess L2");
            transitionGateDestinationMap.put("Transition: Ruin R2", "Transition: Graveyard L1");

            transitionGateDestinationMap.put("Transition: Birth L1", "Transition: Birth R1");
            transitionGateDestinationMap.put("Transition: Birth R1", "Transition: Birth L1");
            transitionGateDestinationMap.put("Transition: Birth U1", "Transition: Graveyard D1");
            transitionGateDestinationMap.put("Transition: Birth D1", "Transition: Goddess U1");

            transitionGateDestinationMap.put("Transition: Dimensional D1", "Transition: Twin U3");

            transitionGateDestinationMap.put("Transition: Retromausoleum U1", "Transition: Retroguidance D1");
            transitionGateDestinationMap.put("Transition: Retromausoleum D1", "Transition: Goddess W1");

            transitionGateDestinationMap.put("Transition: Retroguidance D1", "Transition: Retromausoleum U1");
            transitionGateDestinationMap.put("Transition: Retroguidance L1", "Transition: Retrosurface R1");

            transitionGateDestinationMap.put("Transition: Retrosurface R1", "Transition: Retroguidance L1");
        }
    }

    private void randomizeHorizontalTransitions(Random random) {
        List<String> leftTransitions = new ArrayList<>();
        List<String> unsafeLeftTransitions = new ArrayList<>();
        List<String> rightTransitions = new ArrayList<>();
        List<String> unsafeRightTransitions = new ArrayList<>();

        leftTransitions.add("Transition: Guidance L1");
        leftTransitions.add("Transition: Mausoleum L1");
        leftTransitions.add("Transition: Graveyard L1");
        leftTransitions.add("Transition: Sun L1");
        leftTransitions.add("Transition: Goddess L2");
        leftTransitions.add("Transition: Extinction L1");
        leftTransitions.add("Transition: Extinction L2");
        leftTransitions.add("Transition: Birth L1");
        leftTransitions.add("Transition: Retroguidance L1");
        unsafeLeftTransitions.add("Transition: Moonlight L1");
        unsafeLeftTransitions.add("Transition: Ruin L1");
        rightTransitions.add("Transition: Surface R1");
        rightTransitions.add("Transition: Illusion R2");
        rightTransitions.add("Transition: Graveyard R1");
        rightTransitions.add("Transition: Sun R1");
        rightTransitions.add("Transition: Sun R2");
        rightTransitions.add("Transition: Inferno R1");
        rightTransitions.add("Transition: Ruin R1");
        rightTransitions.add("Transition: Ruin R2");
        rightTransitions.add("Transition: Birth R1");
        rightTransitions.add("Transition: Endless R1");

        if(Settings.isRandomizeOneWayTransitions()) {
            leftTransitions.add("Transition: Goddess L1");
            if(Settings.getEnabledGlitches().contains("Raindrop")) {
                rightTransitions.add("Transition: Illusion R1");
            }
            else {
                unsafeRightTransitions.add("Transition: Illusion R1");
            }
        }
        else {
            transitionGateDestinationMap.put("Transition: Illusion R1", "Transition: Goddess L1");
            transitionGateDestinationMap.put("Transition: Goddess L1", "Transition: Illusion R1");
        }

        if(Settings.isRequireFullAccess()) {
            unsafeRightTransitions.add("Transition: Retrosurface R1");
        }
        else {
            rightTransitions.add("Transition: Retrosurface R1");
        }

        String chosenTransitionStart;
        String chosenTransitionEnd;

        for(CustomTransitionPlacement customTransitionPlacement : DataFromFile.getCustomPlacementData().getCustomTransitionPlacements()) {
            chosenTransitionStart = customTransitionPlacement.getTargetTransition().replace("Transition ", "Transition: ");
            chosenTransitionEnd = customTransitionPlacement.getDestinationTransition().replace("Transition ", "Transition: ");

            transitionGateDestinationMap.put(chosenTransitionStart, chosenTransitionEnd);
            transitionGateDestinationMap.put(chosenTransitionEnd, chosenTransitionStart);

            leftTransitions.remove(chosenTransitionStart);
            leftTransitions.remove(chosenTransitionEnd);
            unsafeLeftTransitions.remove(chosenTransitionStart);
            unsafeLeftTransitions.remove(chosenTransitionEnd);
            rightTransitions.remove(chosenTransitionStart);
            rightTransitions.remove(chosenTransitionEnd);
            unsafeRightTransitions.remove(chosenTransitionStart);
            unsafeRightTransitions.remove(chosenTransitionEnd);
        }

        while(!unsafeLeftTransitions.isEmpty()) {
            chosenTransitionStart = unsafeLeftTransitions.get(random.nextInt(unsafeLeftTransitions.size()));
            chosenTransitionEnd = rightTransitions.get(random.nextInt(rightTransitions.size()));

            transitionGateDestinationMap.put(chosenTransitionStart, chosenTransitionEnd);
            transitionGateDestinationMap.put(chosenTransitionEnd, chosenTransitionStart);

            unsafeLeftTransitions.remove(chosenTransitionStart);
            rightTransitions.remove(chosenTransitionEnd);
        }
        rightTransitions.addAll(unsafeRightTransitions);

        while(!leftTransitions.isEmpty()) {
            chosenTransitionStart = leftTransitions.get(random.nextInt(leftTransitions.size()));
            chosenTransitionEnd = rightTransitions.get(random.nextInt(rightTransitions.size()));

            transitionGateDestinationMap.put(chosenTransitionStart, chosenTransitionEnd);
            transitionGateDestinationMap.put(chosenTransitionEnd, chosenTransitionStart);

            leftTransitions.remove(chosenTransitionStart);
            rightTransitions.remove(chosenTransitionEnd);
        }
    }

    private void randomizeVerticalTransitions(Random random) {
        List<String> upTransitions = new ArrayList<>();
        List<String> downTransitions = new ArrayList<>();
        List<String> unsafeUpTransitions = new ArrayList<>();
        List<String> unsafeDownTransitions = new ArrayList<>();

        upTransitions.add("Transition: Guidance U1");
        upTransitions.add("Transition: Mausoleum U1");
        upTransitions.add("Transition: Graveyard U1");
        upTransitions.add("Transition: Graveyard U2");
        upTransitions.add("Transition: Sun U1");
        upTransitions.add("Transition: Moonlight U1");
        upTransitions.add("Transition: Moonlight U2");
        upTransitions.add("Transition: Goddess U1");
        upTransitions.add("Transition: Inferno U1");
        upTransitions.add("Transition: Extinction U1");
        upTransitions.add("Transition: Birth U1");
        upTransitions.add("Transition: Twin U1");
        upTransitions.add("Transition: Retromausoleum U1");
        downTransitions.add("Transition: Surface D1");
        downTransitions.add("Transition: Surface D2");
        downTransitions.add("Transition: Guidance D1");
        downTransitions.add("Transition: Guidance D2");
        downTransitions.add("Transition: Illusion D1");
        downTransitions.add("Transition: Mausoleum D1");
        downTransitions.add("Transition: Graveyard D1");
        downTransitions.add("Transition: Goddess D1");
        downTransitions.add("Transition: Twin D1");
        downTransitions.add("Transition: Twin D2");
        downTransitions.add("Transition: Shrine D1");
        downTransitions.add("Transition: Retroguidance D1");
        unsafeDownTransitions.add("Transition: Spring D1");
        unsafeDownTransitions.add("Transition: Birth D1");

        if(Settings.isRandomizeOneWayTransitions()) {
            downTransitions.add("Transition: Endless D1");
            downTransitions.add("Transition: Retromausoleum D1");
            upTransitions.add("Transition: Extinction U3");
            upTransitions.add("Transition: Endless U1");
            upTransitions.add("Transition: Twin U2");
            upTransitions.add("Transition: Twin U3");
            unsafeUpTransitions.add("Transition: Goddess W1");
            unsafeUpTransitions.add("Transition: Shrine U1");
            unsafeDownTransitions.add("Transition: Inferno W1");
            unsafeDownTransitions.add("Transition: Dimensional D1");
            unsafeDownTransitions.add("Transition: Shrine D2");
            unsafeDownTransitions.add("Transition: Shrine D3");
        }
        else {
            transitionGateDestinationMap.put("Transition: Twin U3", "Transition: Dimensional D1");
            transitionGateDestinationMap.put("Transition: Dimensional D1", "Transition: Twin U3");

            transitionGateDestinationMap.put("Transition: Shrine U1", "Transition: Endless D1");
            transitionGateDestinationMap.put("Transition: Endless D1", "Transition: Shrine U1");

            transitionGateDestinationMap.put("Transition: Extinction U3", "Transition: Inferno W1");
            transitionGateDestinationMap.put("Transition: Inferno W1", "Transition: Extinction U3");

            transitionGateDestinationMap.put("Transition: Retromausoleum D1", "Transition: Goddess W1");
            transitionGateDestinationMap.put("Transition: Goddess W1", "Transition: Retromausoleum D1");

            transitionGateDestinationMap.put("Transition: Twin U2", "Transition: Shrine D3");
            transitionGateDestinationMap.put("Transition: Shrine D3", "Transition: Twin U2");

            transitionGateDestinationMap.put("Transition: Endless U1", "Transition: Shrine D2");
            transitionGateDestinationMap.put("Transition: Shrine D2", "Transition: Endless U1");
        }

        // Handle for backside doors in Illusion
        if(backsideDoorRandomizer.isDoorOneWay("Door: F1")) {
            unsafeDownTransitions.add("Transition: Illusion D2");
        }
        else {
            downTransitions.add("Transition: Illusion D2");
        }

        // Handle for backside doors in Extinction (Magatama area)
        if(backsideDoorRandomizer.isDoorOneWay("Door: B6")) {
            unsafeUpTransitions.add("Transition: Extinction U2");
        }
        else {
            upTransitions.add("Transition: Extinction U2");
        }

        // Handle for backside doors in Echidna's Chamber
        if(backsideDoorRandomizer.isDoorOneWay("Door: B7")) {
            unsafeUpTransitions.add("Transition: Inferno U2");
        }
        else {
            upTransitions.add("Transition: Inferno U2");
        }

        String chosenTransitionStart;
        String chosenTransitionEnd;

        for(CustomTransitionPlacement customTransitionPlacement : DataFromFile.getCustomPlacementData().getCustomTransitionPlacements()) {
            chosenTransitionStart = customTransitionPlacement.getTargetTransition().replace("Transition ", "Transition: ");
            chosenTransitionEnd = customTransitionPlacement.getDestinationTransition().replace("Transition ", "Transition: ");

            transitionGateDestinationMap.put(chosenTransitionStart, chosenTransitionEnd);
            transitionGateDestinationMap.put(chosenTransitionEnd, chosenTransitionStart);

            upTransitions.remove(chosenTransitionStart);
            upTransitions.remove(chosenTransitionEnd);
            unsafeUpTransitions.remove(chosenTransitionStart);
            unsafeUpTransitions.remove(chosenTransitionEnd);
            downTransitions.remove(chosenTransitionStart);
            downTransitions.remove(chosenTransitionEnd);
            unsafeDownTransitions.remove(chosenTransitionStart);
            unsafeDownTransitions.remove(chosenTransitionEnd);
        }

        while(!unsafeUpTransitions.isEmpty()) {
            chosenTransitionStart = unsafeUpTransitions.get(random.nextInt(unsafeUpTransitions.size()));
            if("Transition: Shrine U1".equals(chosenTransitionStart)) {
                // Shrine can't lead to itself, but other places can handle it. There should be more than enough down transitions, so no worry about running out of options.
                do {
                    chosenTransitionEnd = downTransitions.get(random.nextInt(downTransitions.size()));
                } while ("Transition: Shrine D1".equals(chosenTransitionEnd));
            }
            else {
                chosenTransitionEnd = downTransitions.get(random.nextInt(downTransitions.size()));
            }

            transitionGateDestinationMap.put(chosenTransitionStart, chosenTransitionEnd);
            transitionGateDestinationMap.put(chosenTransitionEnd, chosenTransitionStart);

            unsafeUpTransitions.remove(chosenTransitionStart);
            downTransitions.remove(chosenTransitionEnd);
        }
        downTransitions.addAll(unsafeDownTransitions);

        while(!upTransitions.isEmpty()) {
            chosenTransitionStart = upTransitions.get(random.nextInt(upTransitions.size()));
            upTransitions.remove(chosenTransitionStart);
            chosenTransitionEnd = downTransitions.get(random.nextInt(downTransitions.size()));
            downTransitions.remove(chosenTransitionEnd);

            transitionGateDestinationMap.put(chosenTransitionStart, chosenTransitionEnd);
            transitionGateDestinationMap.put(chosenTransitionEnd, chosenTransitionStart);
        }
    }

    private void randomizeDuplicateTransition(Random random) {
        if(!Settings.isRandomizeOneWayTransitions()) {
            transitionGateDestinationMap.put("Transition: Endless L1", "Transition: Endless R1");
            return;
        }

        String chosenTransitionStart;
        String chosenTransitionEnd;
        for(CustomTransitionPlacement customTransitionPlacement : DataFromFile.getCustomPlacementData().getCustomTransitionPlacements()) {
            chosenTransitionStart = customTransitionPlacement.getTargetTransition().replace("Transition ", "Transition: ");
            if("Transition: Endless L1".equals(chosenTransitionStart)) {
                chosenTransitionEnd = customTransitionPlacement.getDestinationTransition().replace("Transition ", "Transition: ");
                transitionGateDestinationMap.put(chosenTransitionStart, chosenTransitionEnd);
                return;
            }
        }

        List<String> rightTransitions = new ArrayList<>();
        rightTransitions.add("Transition: Surface R1");
        rightTransitions.add("Transition: Graveyard R1");
        rightTransitions.add("Transition: Inferno R1");
        rightTransitions.add("Transition: Ruin R1");
        rightTransitions.add("Transition: Ruin R2");

        if(!Settings.isFools2021Mode()) {
            rightTransitions.add("Transition: Sun R1");
            rightTransitions.add("Transition: Sun R2");
            rightTransitions.add("Transition: Endless R1");
            if(Settings.isRandomizeOneWayTransitions()) {
                rightTransitions.add("Transition: Illusion R1");
            }
            rightTransitions.add("Transition: Illusion R2");
            rightTransitions.add("Transition: Birth R1");
        }

        String chosenTransition = rightTransitions.get(random.nextInt(rightTransitions.size()));
        transitionGateDestinationMap.put("Transition: Endless L1", chosenTransition);
    }

    public String getTransitionReverse(String transitionReached) {
        return transitionGateDestinationMap.get(transitionReached);
    }

    public boolean isEndlessL1Open(String nodeName) {
        String reverseTransition = transitionGateDestinationMap.get("Transition: Endless L1");
        if(reverseTransition.equals("Transition: Birth R1")) {
            return "Event: Skanda Block Removed".equals(nodeName);
        }
        else if(reverseTransition.equals("Transition: Illusion R1")
                || reverseTransition.equals("Transition: Illusion R2")) {
            return "Event: Illusion Unlocked".equals(nodeName);
        }
        else {
            return true;
        }
    }

    public List<String> getTransitionExits(String nodeName, Integer attemptNumber) {
        String locationName = nodeName.replace("Exit:", "Location:");
        List<String> transitionNames = getGatesFromLocation(locationName);
        if(transitionNames.isEmpty()) {
            return new ArrayList<>(0);
        }
        List<String> transitionExits = new ArrayList<>(transitionNames.size());
        String endlessLeftDoorReverse = getTransitionReverse("Transition: Endless L1");
        for(String transitionName : transitionNames) {
            transitionExits.add(getTransitionReverse(transitionName).replace("Transition:", "Exit:"));
            if(transitionName.equals(endlessLeftDoorReverse)) {
                transitionExits.add("Exit: Endless L1");
            }
        }
        if(!transitionExits.isEmpty()) {
            FileUtils.logDetail("Gained access to nodes " + transitionExits, attemptNumber);
        }
        return transitionExits;
    }

    public List<TransitionGateData> getTransitionGateData() {
        List<TransitionGateData> transitionGateData = new ArrayList<>();
        String endlessL1Reverse = transitionGateDestinationMap.get("Transition: Endless L1");
        for(Map.Entry<String, String> gateStartAndEnd : transitionGateDestinationMap.entrySet()) {
            transitionGateData.add(new TransitionGateData(gateStartAndEnd.getKey(), gateStartAndEnd.getValue(),
                    endlessL1Reverse.equals(gateStartAndEnd.getKey())));
        }
        return transitionGateData;
    }

    /**
     * This doesn't get placed until the end, since it has no effect on logic.
     * @param random for determining where to place the transition
     */
    public void placeTowerOfTheGoddessPassthroughPipe(Random random) {
        List<String> leftTransitions = new ArrayList<>();
        leftTransitions.add("Transition: Guidance L1");
        leftTransitions.add("Transition: Mausoleum L1");
        leftTransitions.add("Transition: Sun L1");
//        leftTransitions.add("Transition: Extinction L1");
//        leftTransitions.add("Transition: Extinction L2");
        leftTransitions.add("Transition: Graveyard L1");
        leftTransitions.add("Transition: Moonlight L1");
        leftTransitions.add("Transition: Goddess L2");
        leftTransitions.add("Transition: Ruin L1");
        leftTransitions.add("Transition: Birth L1");
        leftTransitions.add("Transition: Retroguidance L1");
        String leftTransition = leftTransitions.get(random.nextInt(leftTransitions.size()));
        String rightTransition = transitionGateDestinationMap.get(leftTransition);
        transitionGateDestinationMap.put(leftTransition, "Transition: Pipe R1");
        transitionGateDestinationMap.put("Transition: Pipe R1", leftTransition);
        transitionGateDestinationMap.put(rightTransition, "Transition: Pipe L1");
        transitionGateDestinationMap.put("Transition: Pipe L1", rightTransition);
    }

    private List<String> getGatesFromLocation(String gateName) {
        if("Location: Surface [Main]".equals(gateName)) {
            return Arrays.asList("Transition: Surface R1");
        }
        else if("Location: Surface [Ruin Path Lower]".equals(gateName)) {
            return Arrays.asList("Transition: Surface D1");
        }
        else if("Location: Surface [Ruin Path Upper]".equals(gateName)) {
            return Arrays.asList("Transition: Surface D2");
        }
        else if("Location: Gate of Guidance [Main]".equals(gateName)) {
            return Arrays.asList("Transition: Guidance L1", "Transition: Guidance U1", "Transition: Guidance D1", "Transition: Guidance D2");
        }
        else if("Location: Mausoleum of the Giants".equals(gateName)) {
            return Arrays.asList("Transition: Mausoleum L1", "Transition: Mausoleum U1", "Transition: Mausoleum D1");
        }
        else if("Location: Temple of the Sun [West]".equals(gateName)) {
            return Arrays.asList("Transition: Sun L1");
        }
        else if("Location: Temple of the Sun [Main]".equals(gateName)) {
            return Arrays.asList("Transition: Sun U1");
        }
        else if("Location: Temple of the Sun [East]".equals(gateName)) {
            return Arrays.asList("Transition: Sun R1", "Transition: Sun R2");
        }
        else if("Location: Spring in the Sky [Main]".equals(gateName)) {
            return Arrays.asList("Transition: Spring D1");
        }
        else if("Location: Inferno Cavern [Main]".equals(gateName)) {
            return Arrays.asList("Transition: Inferno R1", "Transition: Inferno U1");
        }
        else if("Location: Inferno Cavern [Lava]".equals(gateName)) {
            return Arrays.asList("Transition: Inferno W1");
        }
        else if("Location: Inferno Cavern [Spikes]".equals(gateName)) {
            return Arrays.asList("Transition: Inferno U2");
        }
        else if("Location: Chamber of Extinction [Map]".equals(gateName)) {
            return Arrays.asList("Transition: Extinction L2");
        }
        else if("Location: Chamber of Extinction [Main]".equals(gateName)) {
            return Arrays.asList("Transition: Extinction U1");
        }
        else if("Location: Chamber of Extinction [Left Main]".equals(gateName)) {
            return Arrays.asList("Transition: Extinction L1");
        }
        else if("Location: Chamber of Extinction [Ankh Upper]".equals(gateName)) {
            return Arrays.asList("Transition: Extinction U3");
        }
        else if("Location: Chamber of Extinction [Magatama Right]".equals(gateName)) {
            return Arrays.asList("Transition: Extinction U2");
        }
        else if("Location: Twin Labyrinths [Loop]".equals(gateName)) {
            return Arrays.asList("Transition: Twin U1");
        }
        else if("Location: Twin Labyrinths [Poison 2]".equals(gateName)) {
            return Arrays.asList("Transition: Twin U2");
        }
        else if("Location: Twin Labyrinths [Poseidon]".equals(gateName)) {
            return Arrays.asList("Transition: Twin U3");
        }
        else if("Location: Twin Labyrinths [Lower]".equals(gateName)) {
            return Arrays.asList("Transition: Twin D1", "Transition: Twin D2");
        }
        else if("Location: Endless Corridor [1F]".equals(gateName)) {
            return Arrays.asList("Transition: Endless R1");
        }
        else if("Location: Endless Corridor [2F]".equals(gateName)) {
            return Arrays.asList("Transition: Endless U1");
        }
        else if("Location: Endless Corridor [5F]".equals(gateName)) {
            return Arrays.asList("Transition: Endless D1");
        }
        else if("Location: Shrine of the Mother [Main]".equals(gateName)) {
            return Arrays.asList("Transition: Shrine U1");
        }
        else if("Location: Shrine of the Mother [Lower]".equals(gateName)) {
            return Arrays.asList("Transition: Shrine D1");
        }
        else if("Location: Shrine of the Mother [Seal]".equals(gateName)) {
            return Arrays.asList("Transition: Shrine D2");
        }
        else if("Location: Shrine of the Mother [Map]".equals(gateName)) {
            return Arrays.asList("Transition: Shrine D3");
        }
        else if("Location: Gate of Illusion [Lower]".equals(gateName)) {
            return Arrays.asList("Transition: Illusion D1");
        }
        else if("Location: Gate of Illusion [Eden]".equals(gateName)) {
            return Arrays.asList("Transition: Illusion D2");
        }
        else if("Location: Gate of Illusion [Pot Room]".equals(gateName)) {
            return Arrays.asList("Transition: Illusion R1");
        }
        else if("Location: Gate of Illusion [Ruin]".equals(gateName)) {
            return Arrays.asList("Transition: Illusion R2");
        }
        else if("Location: Graveyard of the Giants [West]".equals(gateName)) {
            return Arrays.asList("Transition: Graveyard L1", "Transition: Graveyard U1");
        }
        else if("Location: Graveyard of the Giants [Grail]".equals(gateName)) {
            return Arrays.asList("Transition: Graveyard R1");
        }
        else if("Location: Graveyard of the Giants [East]".equals(gateName)) {
            return Arrays.asList("Transition: Graveyard U2", "Transition: Graveyard D1");
        }
        else if("Location: Temple of Moonlight [Lower]".equals(gateName)) {
            return Arrays.asList("Transition: Moonlight L1");
        }
        else if("Location: Temple of Moonlight [Eden]".equals(gateName)) {
            return Arrays.asList("Transition: Moonlight U1");
        }
        else if("Location: Temple of Moonlight [Upper]".equals(gateName)) {
            return Arrays.asList("Transition: Moonlight U2");
        }
        else if("Location: Tower of the Goddess [Spaulder]".equals(gateName)) {
            return Arrays.asList("Transition: Goddess L1");
        }
        else if("Location: Tower of the Goddess [Shield Statue]".equals(gateName)) {
            return Arrays.asList("Transition: Goddess D1");
        }
        else if("Location: Tower of the Goddess [Lower]".equals(gateName)) {
            return Arrays.asList("Transition: Goddess L2");
        }
        else if("Location: Tower of the Goddess [Lamp]".equals(gateName)) {
            return Arrays.asList("Transition: Goddess U1", "Transition: Goddess W1");
        }
        else if("Location: Tower of Ruin [Illusion]".equals(gateName)) {
            return Arrays.asList("Transition: Ruin L1");
        }
        else if("Location: Tower of Ruin [Medicine]".equals(gateName)) {
            return Arrays.asList("Transition: Ruin R1");
        }
        else if("Location: Tower of Ruin [Southeast]".equals(gateName)) {
            return Arrays.asList("Transition: Ruin R2");
        }
        else if("Location: Chamber of Birth [Northeast]".equals(gateName)) {
            return Arrays.asList("Transition: Birth U1");
        }
        else if("Location: Chamber of Birth [Southeast]".equals(gateName)) {
            return Arrays.asList("Transition: Birth L1");
        }
        else if("Location: Chamber of Birth [West Entrance]".equals(gateName)) {
            return Arrays.asList("Transition: Birth D1");
        }
        else if("Location: Chamber of Birth [Skanda]".equals(gateName)) {
            return Arrays.asList("Transition: Birth R1");
        }
        else if("Location: Dimensional Corridor [Lower]".equals(gateName)) {
            return Arrays.asList("Transition: Dimensional D1");
        }
        else if("Location: Gate of Time [Mausoleum Lower]".equals(gateName)) {
            return Arrays.asList("Transition: Retromausoleum D1");
        }
        else if("Location: Gate of Time [Mausoleum Upper]".equals(gateName)) {
            return Arrays.asList("Transition: Retromausoleum U1");
        }
        else if("Location: Gate of Time [Guidance]".equals(gateName)) {
            return Arrays.asList("Transition: Retroguidance L1", "Transition: Retroguidance D1");
        }
        else if("Location: Gate of Time [Surface]".equals(gateName)) {
            return Arrays.asList("Transition: Retrosurface R1");
        }
        return new ArrayList<>(0);
    }

    private String getLocationFromGate(String gateName) {
        if("Transition: Surface R1".equals(gateName)) {
            return "Location: Surface [Main]";
        }
        else if("Transition: Surface D1".equals(gateName)) {
            return "Location: Surface [Ruin Path Lower]";
        }
        else if("Transition: Surface D2".equals(gateName)) {
            return "Location: Surface [Ruin Path Upper]";
        }
        else if("Transition: Guidance L1".equals(gateName)) {
            return "Location: Gate of Guidance [Main]";
        }
        else if("Transition: Guidance U1".equals(gateName)) {
            return "Location: Gate of Guidance [Main]";
        }
        else if("Transition: Guidance D1".equals(gateName)) {
            return "Location: Gate of Guidance [Main]";
        }
        else if("Transition: Guidance D2".equals(gateName)) {
            return "Location: Gate of Guidance [Main]";
        }
        else if("Transition: Mausoleum L1".equals(gateName)) {
            return "Location: Mausoleum of the Giants";
        }
        else if("Transition: Mausoleum U1".equals(gateName)) {
            return "Location: Mausoleum of the Giants";
        }
        else if("Transition: Mausoleum D1".equals(gateName)) {
            return "Location: Mausoleum of the Giants";
        }
        else if("Transition: Sun L1".equals(gateName)) {
            return "Location: Temple of the Sun [West]";
        }
        else if("Transition: Sun U1".equals(gateName)) {
            return "Location: Temple of the Sun [Main]";
        }
        else if("Transition: Sun R1".equals(gateName)) {
            return "Location: Temple of the Sun [East]";
        }
        else if("Transition: Sun R2".equals(gateName)) {
            return "Location: Temple of the Sun [East]";
        }
        else if("Transition: Spring D1".equals(gateName)) {
            return "Location: Spring in the Sky [Main]";
        }
        else if("Transition: Inferno R1".equals(gateName)) {
            return "Location: Inferno Cavern [Main]";
        }
        else if("Transition: Inferno U1".equals(gateName)) {
            return "Location: Inferno Cavern [Main]";
        }
        else if("Transition: Inferno U2".equals(gateName)) {
            return "Location: Inferno Cavern [Spikes]";
        }
//        else if("Transition: Inferno W1".equals(gateName)) {
//            return "Location: Inferno Cavern [Lava]";
//        }
        else if("Transition: Extinction L1".equals(gateName)) {
            return "Location: Chamber of Extinction [Main]";
        }
        else if("Transition: Extinction L2".equals(gateName)) {
            return "Location: Chamber of Extinction [Map]";
        }
        else if("Transition: Extinction U1".equals(gateName)) {
            return "Location: Chamber of Extinction [Main]";
        }
        else if("Transition: Extinction U2".equals(gateName)) {
            return "Location: Chamber of Extinction [Magatama Right]";
        }
//        else if("Transition: Extinction U3".equals(gateName)) {
//            return "Location: Chamber of Extinction [Ankh Upper]";
//        }
        else if("Transition: Twin U1".equals(gateName)) {
            return "Location: Twin Labyrinths [Loop]";
        }
        else if("Transition: Twin U2".equals(gateName)) {
            return "Location: Twin Labyrinths [Poison 2]";
        }
        else if("Transition: Twin U3".equals(gateName)) {
            return "Location: Twin Labyrinths [Poseidon]";
        }
        else if("Transition: Twin D1".equals(gateName)) {
            return "Location: Twin Labyrinths [Lower]";
        }
        else if("Transition: Twin D2".equals(gateName)) {
            return "Location: Twin Labyrinths [Lower]";
        }
        else if("Transition: Endless R1".equals(gateName)) {
            return "Location: Endless Corridor [1F]";
        }
        else if("Transition: Endless U1".equals(gateName)) {
            return "Location: Endless Corridor [2F]";
        }
        else if("Transition: Endless D1".equals(gateName)) {
            return "Location: Endless Corridor [5F]";
        }
        else if("Transition: Shrine U1".equals(gateName)) {
            return "Location: Shrine of the Mother [Main]";
        }
        else if("Transition: Shrine D1".equals(gateName)) {
            return "Location: Shrine of the Mother [Lower]";
        }
        else if("Transition: Shrine D2".equals(gateName)) {
            return "Location: Shrine of the Mother [Seal]";
        }
        else if("Transition: Shrine D3".equals(gateName)) {
            return "Location: Shrine of the Mother [Map]";
        }
        else if("Transition: Illusion D1".equals(gateName)) {
            return "Location: Gate of Illusion [Lower]";
        }
        else if("Transition: Illusion D2".equals(gateName)) {
            return "Location: Gate of Illusion [Eden]";
        }
        else if("Transition: Illusion R1".equals(gateName)) {
            return "Location: Gate of Illusion [Pot Room]";
        }
        else if("Transition: Illusion R2".equals(gateName)) {
            return "Location: Gate of Illusion [Ruin]";
        }
        else if("Transition: Graveyard L1".equals(gateName)) {
            return "Location: Graveyard of the Giants [West]";
        }
        else if("Transition: Graveyard R1".equals(gateName)) {
            return "Location: Graveyard of the Giants [Grail]";
        }
        else if("Transition: Graveyard U1".equals(gateName)) {
            return "Location: Graveyard of the Giants [West]";
        }
        else if("Transition: Graveyard U2".equals(gateName)) {
            return "Location: Graveyard of the Giants [East]";
        }
        else if("Transition: Graveyard D1".equals(gateName)) {
            return "Location: Graveyard of the Giants [East]";
        }
        else if("Transition: Moonlight L1".equals(gateName)) {
            return "Location: Temple of Moonlight [Lower]";
        }
        else if("Transition: Moonlight U1".equals(gateName)) {
            return "Location: Temple of Moonlight [Eden]";
        }
        else if("Transition: Moonlight U2".equals(gateName)) {
            return "Location: Temple of Moonlight [Upper]";
        }
        else if("Transition: Goddess L1".equals(gateName)) {
            return "Location: Tower of the Goddess [Spaulder]";
        }
        else if("Transition: Goddess L2".equals(gateName)) {
            return "Location: Tower of the Goddess [Lower]";
        }
        else if("Transition: Goddess U1".equals(gateName)) {
            return "Location: Tower of the Goddess [Lamp]";
        }
        else if("Transition: Goddess D1".equals(gateName)) {
            return "Location: Tower of the Goddess [Shield Statue]";
        }
        else if("Transition: Ruin L1".equals(gateName)) {
            return "Location: Tower of Ruin [Illusion]";
        }
        else if("Transition: Ruin R1".equals(gateName)) {
            return "Location: Tower of Ruin [Medicine]";
        }
        else if("Transition: Ruin R2".equals(gateName)) {
            return "Location: Tower of Ruin [Southeast]";
        }
        else if("Transition: Birth U1".equals(gateName)) {
            return "Location: Chamber of Birth [Northeast]";
        }
        else if("Transition: Birth L1".equals(gateName)) {
            return "Location: Chamber of Birth [Southeast]";
        }
        else if("Transition: Birth D1".equals(gateName)) {
            return "Location: Chamber of Birth [West Entrance]";
        }
        else if("Transition: Birth R1".equals(gateName)) {
            return "Location: Chamber of Birth [Skanda]";
        }
        else if("Transition: Dimensional D1".equals(gateName)) {
            return "Location: Dimensional Corridor [Lower]";
        }
        return null;
    }

    public void outputLocations(int attemptNumber) throws IOException {
        BufferedWriter writer = FileUtils.getFileWriter(String.format("%d/gates.txt", Settings.getStartingSeed()));
        if (writer == null) {
            return;
        }

        for(String transition : getTransitionList()) {
            String transitionFormat;
            String transitionOut = transitionGateDestinationMap.get(transition);

            if("Transition: Pipe L1".equals(transitionOut)) {
                transitionFormat = "transitions.PipeTransitionFormat";
                transitionOut = transitionGateDestinationMap.get("Transition: Pipe R1");
            }
            else if("Transition: Pipe R1".equals(transitionOut)) {
                transitionFormat = "transitions.PipeTransitionFormat";
                transitionOut = transitionGateDestinationMap.get("Transition: Pipe L1");
            }
            else if(transitionOut.endsWith("W1")) {
                transitionFormat = "transitions.OneWayTransitionFormat";
            }
            else {
                transitionFormat = "transitions.RegularTransitionFormat";
            }
            writer.write(String.format(Translations.getText(transitionFormat),
                    Translations.getTransitionText(transition),
                    Translations.getTransitionText(transitionOut)));
            writer.newLine();
        }

        writer.flush();
        writer.close();
    }

    public static List<String> getTransitionList() {
        return new ArrayList<>(Arrays.asList("Transition: Surface R1", "Transition: Surface D1", "Transition: Surface D2",
                "Transition: Guidance L1", "Transition: Guidance U1", "Transition: Guidance D1", "Transition: Guidance D2",
                "Transition: Mausoleum L1", "Transition: Mausoleum U1", "Transition: Mausoleum D1",
                "Transition: Sun L1", "Transition: Sun R1", "Transition: Sun R2", "Transition: Sun U1",
                "Transition: Spring D1",
                "Transition: Inferno R1", "Transition: Inferno U1", "Transition: Inferno U2",
                "Transition: Extinction L1", "Transition: Extinction L2", "Transition: Extinction U1", "Transition: Extinction U2", "Transition: Extinction U3",
                "Transition: Twin U1", "Transition: Twin U2", "Transition: Twin U3", "Transition: Twin D1", "Transition: Twin D2",
                "Transition: Endless L1", "Transition: Endless R1", "Transition: Endless U1", "Transition: Endless D1",
                "Transition: Shrine U1", "Transition: Shrine D1", "Transition: Shrine D2", "Transition: Shrine D3",
                "Transition: Illusion R1", "Transition: Illusion R2", "Transition: Illusion D1", "Transition: Illusion D2",
                "Transition: Graveyard L1", "Transition: Graveyard R1", "Transition: Graveyard U1", "Transition: Graveyard U2", "Transition: Graveyard D1",
                "Transition: Moonlight L1", "Transition: Moonlight U1", "Transition: Moonlight U2",
                "Transition: Goddess L1", "Transition: Goddess L2", "Transition: Goddess U1", "Transition: Goddess D1",
                "Transition: Ruin L1", "Transition: Ruin R1", "Transition: Ruin R2",
                "Transition: Birth L1", "Transition: Birth R1", "Transition: Birth U1", "Transition: Birth D1",
                "Transition: Dimensional D1",
                "Transition: Retromausoleum U1", "Transition: Retromausoleum D1",
                "Transition: Retroguidance D1",
                "Transition: Retroguidance L1",
                "Transition: Retrosurface R1"));
    }
}
