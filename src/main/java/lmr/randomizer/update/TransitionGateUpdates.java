package lmr.randomizer.update;

import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.Settings;
import lmr.randomizer.rcd.object.*;

public final class TransitionGateUpdates {
    private TransitionGateUpdates() { }

    public static void replaceTransitionGateArgs(GameObject gameObject, String gateDestination) {
        if("Transition: Surface R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)(Settings.isHalloweenMode() ? 22 : 1));
            gameObject.getArgs().set(1, (short)11);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Surface D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)(Settings.isHalloweenMode() ? 22 : 1));
            gameObject.getArgs().set(1, (short)4);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)440);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Surface D2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)(Settings.isHalloweenMode() ? 22 : 1));
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)440);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Guidance L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)0);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Guidance U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)0);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Guidance D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)0);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Guidance D2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)0);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Mausoleum L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)2);
            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Mausoleum U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)2);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Mausoleum D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)2);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)480);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Sun L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)3);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Sun R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)3);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)72);
        }
        else if("Transition: Sun R2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)3);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)232);
        }
        else if("Transition: Sun U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)3);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Spring D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)4);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Inferno R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)5);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Inferno U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)5);
            gameObject.getArgs().set(1, (short)1);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Inferno U2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)5);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)440);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Inferno W1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)5);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)40);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Extinction L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)6);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)72);
        }
        else if("Transition: Extinction L2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)6);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)232);
        }
        else if("Transition: Extinction U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)6);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Extinction U2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)6);
            gameObject.getArgs().set(1, (short)1);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)440);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Extinction U3".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)6);
            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)40);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Twin U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)7);
            gameObject.getArgs().set(1, (short)3);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)480);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Twin U2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)7);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Twin U3".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)7);
            gameObject.getArgs().set(1, (short)10);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)120);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Twin D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)7);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Twin D2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)7);
            gameObject.getArgs().set(1, (short)16);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Endless R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)8);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Endless U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)8);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)3);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Endless D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)8);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)3);
            gameObject.getArgs().set(3, (short)420);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Shrine U1".equals(gateDestination)) {
            if(gameObject.getTestByteOperations().get(0).getValue() == 1) {
                // Escape door
                gameObject.getArgs().set(0, (short)18);
            }
            else {
                // Non-escape door
                gameObject.getArgs().set(0, (short)9);
            }

            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)420);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Shrine D1".equals(gateDestination)) {
            if(gameObject.getTestByteOperations().get(0).getValue() == 1) {
                // Escape door
                gameObject.getArgs().set(0, (short)18);
            }
            else {
                // Non-escape door
                gameObject.getArgs().set(0, (short)9);
            }

            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Shrine D2".equals(gateDestination)) {
            if(gameObject.getTestByteOperations().get(0).getValue() == 1) {
                // Escape door
                gameObject.getArgs().set(0, (short)18);
            }
            else {
                // Non-escape door
                gameObject.getArgs().set(0, (short)9);
            }

            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Shrine D3".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)9);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)300);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Illusion R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)10);
            gameObject.getArgs().set(1, (short)1);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Illusion R2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)10);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Illusion D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)10);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Illusion D2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)10);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)120);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Graveyard L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)11);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Graveyard R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)11);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)232);
        }
        else if("Transition: Graveyard U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)11);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Graveyard U2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)11);
            gameObject.getArgs().set(1, (short)9);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Graveyard D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)11);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)500);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Moonlight L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)12);
            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)232);
        }
        else if("Transition: Moonlight U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)12);
            gameObject.getArgs().set(1, (short)1);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)120);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Moonlight U2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)12);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Goddess L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Goddess L2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)2);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Goddess U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)4);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)80);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Goddess D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)540);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Goddess W1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)4);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)500);
            gameObject.getArgs().set(4, (short)40);
        }
        else if("Transition: Ruin L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)14);
            gameObject.getArgs().set(1, (short)5);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Ruin R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)14);
            gameObject.getArgs().set(1, (short)7);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Ruin R2".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)14);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)152);
        }
        else if("Transition: Birth L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)15);
            gameObject.getArgs().set(1, (short)3);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Birth R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)16);
            gameObject.getArgs().set(1, (short)3);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Birth U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)15);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)500);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Birth D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)16);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)80);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Dimensional D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)17);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)120);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Retromausoleum U1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)19);
            gameObject.getArgs().set(1, (short)4);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)20);
        }
        else if("Transition: Retromausoleum D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)19);
            gameObject.getArgs().set(1, (short)1);
            gameObject.getArgs().set(2, (short)2);
            gameObject.getArgs().set(3, (short)480);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Retroguidance L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)20);
            gameObject.getArgs().set(1, (short)4);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Retroguidance D1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)20);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)60);
            gameObject.getArgs().set(4, (short)392);
        }
        else if("Transition: Retrosurface R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)21);
            gameObject.getArgs().set(1, (short)0);
            gameObject.getArgs().set(2, (short)1);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)312);
        }
        else if("Transition: Pipe L1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)20);
            gameObject.getArgs().set(4, (short)232);
        }
        else if("Transition: Pipe R1".equals(gateDestination)) {
            gameObject.getArgs().set(0, (short)13);
            gameObject.getArgs().set(1, (short)8);
            gameObject.getArgs().set(2, (short)0);
            gameObject.getArgs().set(3, (short)580);
            gameObject.getArgs().set(4, (short)232);
        }
    }

    public static void updateScreenTransition(GameObject transitionGate, String gateDestination) {
        int screenExitIndex = 0;
        if(gateDestination.equals("Transition: Goddess W1")) {
            screenExitIndex = 2;
        }
        else if(gateDestination.equals("Transition: Inferno W1")) {
            screenExitIndex = 0;
        }
        else {
            char transitionDirection = gateDestination.charAt(gateDestination.length() - 2);
            if (transitionDirection == 'U') {
                screenExitIndex = 2;
            } else if (transitionDirection == 'R') {
                screenExitIndex = 3;
            } else if (transitionDirection == 'D') {
                screenExitIndex = 0;
            } else if (transitionDirection == 'L') {
                screenExitIndex = 1;
            }
        }

        ScreenExit screenExit = new ScreenExit();
        byte zoneIndex = transitionGate.getArgs().get(0).byteValue();
        if(zoneIndex == 18) {
            screenExit.setZoneIndex((byte)9);
        }
        else if(Settings.isHalloweenMode() && zoneIndex == 1) {
            // Night Surface, not normal Surface. Not necessary if the gate is already up to date, but checking for safety anyway.
            screenExit.setZoneIndex((byte)22);
        }
        else {
            screenExit.setZoneIndex(zoneIndex);
        }
        screenExit.setRoomIndex(transitionGate.getArgs().get(1).byteValue());
        screenExit.setScreenIndex(transitionGate.getArgs().get(2).byteValue());
        ((Screen)transitionGate.getObjectContainer()).getScreenExits().set(screenExitIndex, screenExit);
    }

    public static void replaceTransitionGateFlags(GameObject gameObject, String gateToUpdate, String gateDestination) {
        if(gateToUpdate.equals("Transition: Illusion R2")) {
            // Add extra check for Fruit of Eden placed.
            gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.EDEN_UNLOCKED, ByteOp.FLAG_NOT_EQUAL, 0));
        }

        if(gateDestination.startsWith("Transition: Shrine") && !"Transition: Shrine D3".equals(gateDestination)) {
            TestByteOperation testByteOperation = gameObject.getTestByteOperations().get(0);
            if(testByteOperation.getValue() != 1) {
                // Non-escape door
                testByteOperation.setIndex(FlagConstants.BOSSES_SHRINE_TRANSFORM);
                testByteOperation.setOp(ByteOp.FLAG_NOT_EQUAL);
                testByteOperation.setValue((byte)9);

                // Add extra check for not during escape, since escape door is different.
                gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_NOT_EQUAL, 1));
            }
        }
        else if(gateDestination.equals("Transition: Illusion R1")
                || gateDestination.equals("Transition: Illusion R2")) {
            // Add extra check for Fruit of Eden placed.
            gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.EDEN_UNLOCKED, ByteOp.FLAG_NOT_EQUAL, 0));
        }
    }

    public static void updateScreenTransition(Screen screen, String gateDestination) {
        int screenExitIndex = 0;
        if(gateDestination.equals("Transition: Goddess W1")) {
            screenExitIndex = 2;
        }
        else if(gateDestination.equals("Transition: Inferno W1")) {
            screenExitIndex = 0;
        }
        else {
            char transitionDirection = gateDestination.charAt(gateDestination.length() - 2);
            if (transitionDirection == 'U') {
                screenExitIndex = 2;
            } else if (transitionDirection == 'R') {
                screenExitIndex = 3;
            } else if (transitionDirection == 'D') {
                screenExitIndex = 0;
            } else if (transitionDirection == 'L') {
                screenExitIndex = 1;
            }
        }

        screen.getScreenExits().set(screenExitIndex, getScreenExit(gateDestination));
    }

    private static ScreenExit getScreenExit(String gateDestination) {
        ScreenExit screenExit = new ScreenExit();
        if("Transition: Surface R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)(Settings.isHalloweenMode() ? 22 : 1));
            screenExit.setRoomIndex((byte)11);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Surface D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)(Settings.isHalloweenMode() ? 22 : 1));
            screenExit.setRoomIndex((byte)4);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Surface D2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)(Settings.isHalloweenMode() ? 22 : 1));
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Guidance L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)0);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Guidance U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)0);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Guidance D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)0);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Guidance D2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)0);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Mausoleum L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)2);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Mausoleum U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)2);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Mausoleum D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)2);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Sun L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)3);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Sun R1".equals(gateDestination) || "Transition: Sun R2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)3);
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Sun U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)3);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Spring D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)4);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Inferno R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)5);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Inferno U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)5);
            screenExit.setRoomIndex((byte)1);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Inferno U2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)5);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Inferno W1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)5);
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Extinction L1".equals(gateDestination) || "Transition: Extinction L2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)6);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Extinction U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)6);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Extinction U2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)6);
            screenExit.setRoomIndex((byte)1);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Extinction U3".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)6);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Twin U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)7);
            screenExit.setRoomIndex((byte)3);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Twin U2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)7);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Twin U3".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)7);
            screenExit.setRoomIndex((byte)10);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Twin D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)7);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Twin D2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)7);
            screenExit.setRoomIndex((byte)16);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Endless R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)8);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Endless U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)8);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)3);
        }
        else if("Transition: Endless D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)8);
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)3);
        }
        else if("Transition: Shrine U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)9);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Shrine D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)9);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Shrine D2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)9);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Shrine D3".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)9);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Illusion R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)10);
            screenExit.setRoomIndex((byte)1);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Illusion R2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)10);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Illusion D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)10);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Illusion D2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)10);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Graveyard L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)11);
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Graveyard R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)11);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Graveyard U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)11);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Graveyard U2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)11);
            screenExit.setRoomIndex((byte)9);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Graveyard D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)11);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Moonlight L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)12);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Moonlight U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)12);
            screenExit.setRoomIndex((byte)1);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Moonlight U2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)12);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Goddess L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Goddess L2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)2);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Goddess U1".equals(gateDestination) || "Transition: Goddess W1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)4);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Goddess D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Ruin L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)14);
            screenExit.setRoomIndex((byte)5);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Ruin R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)14);
            screenExit.setRoomIndex((byte)7);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Ruin R2".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)14);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Birth L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)15);
            screenExit.setRoomIndex((byte)3);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Birth R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)16);
            screenExit.setRoomIndex((byte)3);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Birth U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)15);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Birth D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)16);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Dimensional D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)17);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Retromausoleum U1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)19);
            screenExit.setRoomIndex((byte)4);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Retromausoleum D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)19);
            screenExit.setRoomIndex((byte)1);
            screenExit.setScreenIndex((byte)2);
        }
        else if("Transition: Retroguidance L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)20);
            screenExit.setRoomIndex((byte)4);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Retroguidance D1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)20);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Retrosurface R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)21);
            screenExit.setRoomIndex((byte)0);
            screenExit.setScreenIndex((byte)1);
        }
        else if("Transition: Pipe L1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)0);
        }
        else if("Transition: Pipe R1".equals(gateDestination)) {
            screenExit.setZoneIndex((byte)13);
            screenExit.setRoomIndex((byte)8);
            screenExit.setScreenIndex((byte)0);
        }
        return screenExit;
    }
}
