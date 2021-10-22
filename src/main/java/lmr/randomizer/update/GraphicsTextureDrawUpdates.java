package lmr.randomizer.update;

import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Settings;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.ZoneConstants;

public class GraphicsTextureDrawUpdates {
    public static boolean updateGraphicsTextureDraw(GameObject graphicsTextureDraw) {
        ObjectContainer objectContainer = graphicsTextureDraw.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        int zoneIndex = screen.getZoneIndex();
        int roomIndex = screen.getRoomIndex();
        int screenIndex = screen.getScreenIndex();
        if(zoneIndex == ZoneConstants.SURFACE) {
            if(roomIndex == 2 && screenIndex == 1) {
                if(isXelpudTentClosedGraphic(graphicsTextureDraw)) {
                    return false;
                }
            }
            updateSurfaceTentClosedGraphics(graphicsTextureDraw, roomIndex, screenIndex);
        }
        if(isNpcDoorCover(graphicsTextureDraw)) {
            return false;
        }

        if(Settings.isRandomizeTrapItems()) {
            if(isUnwantedFakeItemGraphic(graphicsTextureDraw, zoneIndex, roomIndex, screenIndex)) {
                return false;
            }
        }

        if(Settings.isRandomizeTransitionGates()) {
            if(isFruitBlockGraphic(graphicsTextureDraw, zoneIndex, roomIndex, screenIndex)) {
                return false;
            }
            else if(isSkandaBlockGraphic(graphicsTextureDraw, zoneIndex, roomIndex, screenIndex)) {
                return false;
            }
        }

        if(Settings.isRandomizeNonBossDoors()) {
            if(isExtinctionKeyFairyDoorCover(graphicsTextureDraw, zoneIndex, roomIndex, screenIndex)) {
                for (TestByteOperation flagTest : graphicsTextureDraw.getTestByteOperations()) {
                    if(flagTest.getIndex() == FlagConstants.KEY_FAIRY_DOOR_UNLOCKED && flagTest.getValue() == 0) {
                        return false;
                    }
                }
            }
            else if(isBossDoorMirrorCoverOrGate(graphicsTextureDraw)) {
                return false;
            }
        }
        updateTests(graphicsTextureDraw, zoneIndex);
        return true;
    }

    private static boolean isXelpudTentClosedGraphic(GameObject graphicsTextureDraw) {
        // Remove graphic for closed Xelpud tent; rando will have it start open instead.
        for(TestByteOperation testByteOperation : graphicsTextureDraw.getTestByteOperations()) {
            if(testByteOperation.getIndex() == FlagConstants.XELPUD_TENT_OPEN) {
                return true;
            }
        }
        return false;
    }

    private static boolean isUnwantedFakeItemGraphic(GameObject graphicsTextureDraw, int zoneIndex, int roomIndex, int screenIndex) {
        if (zoneIndex == ZoneConstants.INFERNO && roomIndex == 1 && screenIndex == 1) {
            if(!HolidaySettings.isFools2020Mode()) {
                for(TestByteOperation testByteOperation : graphicsTextureDraw.getTestByteOperations()) {
                    if(testByteOperation.getIndex() == FlagConstants.INFERNO_FAKE_ORB_CRUSHER) {
                        // Graphical part of Inferno Cavern fake Sacred Orb trap
                        return true;
                    }
                }
            }
        }
        else if(zoneIndex == ZoneConstants.TWIN_FRONT && roomIndex == 12 && screenIndex == 0) {
            if(!graphicsTextureDraw.getTestByteOperations().isEmpty() && graphicsTextureDraw.getTestByteOperations().get(0).getIndex() == FlagConstants.ROOM_FLAG_35) {
                // Graphical part of Twin Labs fake Ankh Jewel trap
                return true;
            }
        }
        return false;
    }

    private static boolean isFruitBlockGraphic(GameObject graphicsTextureDraw, int zoneIndex, int roomIndex, int screenIndex) {
        if (zoneIndex == ZoneConstants.GRAVEYARD && roomIndex == 0 && screenIndex == 1) {
            for(TestByteOperation testByteOperation : graphicsTextureDraw.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.EDEN_UNLOCKED) {
                    return true;
                }
            }
        }
        else if (zoneIndex == ZoneConstants.GODDESS && roomIndex == 7 && screenIndex == 0) {
            for(TestByteOperation testByteOperation : graphicsTextureDraw.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.EDEN_UNLOCKED) {
                    return true;
                }
            }
        }
        else if (zoneIndex == ZoneConstants.RUIN && roomIndex == 5 && screenIndex == 0) {
            for(TestByteOperation testByteOperation : graphicsTextureDraw.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.EDEN_UNLOCKED) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isSkandaBlockGraphic(GameObject graphicsTextureDraw, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == ZoneConstants.BIRTH_SWORDS && roomIndex == 3 && screenIndex == 1) {
            for(TestByteOperation testByteOperation : graphicsTextureDraw.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.SKANDA_STATE) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isExtinctionKeyFairyDoorCover(GameObject graphicsTextureDraw, int zoneIndex, int roomIndex, int screenIndex) {
        if (zoneIndex == ZoneConstants.EXTINCTION && roomIndex == 7 && screenIndex == 0) {
            for (TestByteOperation flagTest : graphicsTextureDraw.getTestByteOperations()) {
                if(flagTest.getIndex() == FlagConstants.KEY_FAIRY_DOOR_UNLOCKED && flagTest.getValue() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isBossDoorMirrorCoverOrGate(GameObject graphicsTextureDraw) {
        for (TestByteOperation flagTest : graphicsTextureDraw.getTestByteOperations()) {
            if(flagTest.getIndex() == FlagConstants.AMPHISBAENA_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.AMPHISBAENA_GATE_OPEN
                    || flagTest.getIndex() == FlagConstants.SAKIT_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.SAKIT_GATE_OPEN
                    || flagTest.getIndex() == FlagConstants.ELLMAC_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.ELLMAC_GATE_OPEN
                    || flagTest.getIndex() == FlagConstants.BAHAMUT_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.BAHAMUT_GATE_OPEN
                    || flagTest.getIndex() == FlagConstants.VIY_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.VIY_GATE_OPEN
                    || flagTest.getIndex() == FlagConstants.PALENQUE_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.PALENQUE_GATE_OPEN
                    || flagTest.getIndex() == FlagConstants.BAPHOMET_GATE_MIRROR_COVER || flagTest.getIndex() == FlagConstants.BAPHOMET_GATE_OPEN) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNpcDoorCover(GameObject graphicsTextureDraw) {
        for (TestByteOperation flagTest : graphicsTextureDraw.getTestByteOperations()) {
            if(flagTest.getIndex() == FlagConstants.RETRO_XELPUD_DOOR_COVER
                    || flagTest.getIndex() == FlagConstants.WF_SOFTWARE_MEKURI
                    || (flagTest.getIndex() == FlagConstants.MOTHER_STATE && ByteOp.FLAG_EQUALS.equals(flagTest.getOp()) && flagTest.getValue() == 3)) {
                return true;
            }
        }
        return false;
    }

    private static void updateSurfaceTentClosedGraphics(GameObject graphicsTextureDraw, int roomIndex, int screenIndex) {
        if(roomIndex == 0 && screenIndex == 2
                || roomIndex == 2 && screenIndex == 0
                || roomIndex == 2 && screenIndex == 1
                || roomIndex == 10 && screenIndex == 3) {
            // Graphics for closed surface tents before talking to Xelpud
            for(int i = 0; i < graphicsTextureDraw.getTestByteOperations().size(); i++) {
                TestByteOperation flagTest = graphicsTextureDraw.getTestByteOperations().get(i);
                if(flagTest.getIndex() == FlagConstants.SURFACE_RUINS_OPENED) {
                    // Swap Xelpud first-conversation flag with custom
                    flagTest.setIndex(FlagConstants.XELPUD_CONVERSATION_INTRO);
                }
            }
        }
        if(roomIndex == 0 && screenIndex == 2) {
            // Hiner door has wrong layer
            graphicsTextureDraw.getArgs().set(0, (short)0);
        }
    }

    private static void updateTests(GameObject graphicsTextureDraw, int zoneIndex) {
        Integer removeFlagIndex = null;
        for (int flagIndex = 0; flagIndex < graphicsTextureDraw.getTestByteOperations().size(); flagIndex++) {
            TestByteOperation flagTest = graphicsTextureDraw.getTestByteOperations().get(flagIndex);
            if(flagTest.getIndex() == FlagConstants.MANTRA_FINAL) {
                if(flagTest.getValue() == 4) {
                    flagTest.setIndex(FlagConstants.MANTRA_LAMULANA);
                    flagTest.setValue((byte)1);
                }
            }
            else if(flagTest.getIndex() == FlagConstants.COG_MUDMEN_STATE) {
                if(flagTest.getValue() == 3) {
                    if(ByteOp.FLAG_EQUALS.equals(flagTest.getOp())) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);
                        graphicsTextureDraw.setX(graphicsTextureDraw.getX() - 60);
                        break;
                    }
                    if(ByteOp.FLAG_LTEQ.equals(flagTest.getOp())) {
                        flagTest.setValue((byte)4);
                        flagTest.setOp(ByteOp.FLAG_LT);
                        break;
                    }
                }
                else if(flagTest.getValue() != 4) {
                    flagTest.setIndex(FlagConstants.ILLUSION_PUZZLE_COG_CHEST);
                    break;
                }
            }
            else if(flagTest.getIndex() == FlagConstants.SURFACE_RUINS_OPENED) {
                if(zoneIndex != ZoneConstants.SURFACE && zoneIndex != ZoneConstants.NIGHT_SURFACE) {
                    // Some graphics objects wrongly test for having talked to Xelpud on the Surface, notably warp portal graphics.
                    if(ByteOp.FLAG_NOT_EQUAL.equals(flagTest.getOp()) && flagTest.getValue() == 0) {
                        removeFlagIndex = flagIndex;
                    }
                }
            }
        }
        if(removeFlagIndex != null) {
            graphicsTextureDraw.getTestByteOperations().remove((int)removeFlagIndex);
        }
    }


    /**
     * Add door cover graphic to display after talking to Former Mekuri Master to indicate the door can't be re-entered.
     * Note that there is no test for whether the NPC's door is present/visible; the assumption is made that the only
     * way to get into a state where itemFlagMekuri is >= 2 is to have talked to Former Mekuri Master, which requires
     * the door to have been usable in the first place.
     * @param conversationDoor the door to use as a reference for adding the cover graphic
     * @param itemFlagMekuri the flag to test for whether or not the door cover should be visible
     */
    public static void addNpcDoorCover(ConversationDoor conversationDoor, int itemFlagMekuri) {
        Screen screen = (Screen)conversationDoor.getObjectContainer();
        if(screen.getZoneIndex() == ZoneConstants.GUIDANCE) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY());
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(480, 160);
            graphicsTextureDraw.setImageSize(40, 40);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.SURFACE) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            if(screen.getRoomIndex() == 0 && screen.getScreenIndex() == 2) {
                graphicsTextureDraw.setLayer(-7);
            }
            else if(screen.getRoomIndex() == 2 && screen.getScreenIndex() == 0) {
                graphicsTextureDraw.setLayer(0);
            }
            else if(screen.getRoomIndex() == 2 && screen.getScreenIndex() == 1) {
                graphicsTextureDraw.setLayer(0);
            }
            else { // if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                graphicsTextureDraw.setLayer(0);
            }
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(800, 0);
            graphicsTextureDraw.setImageSize(40, 60);
            graphicsTextureDraw.setAnimation(0, 1, 4, 1);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.MAUSOLEUM) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY());
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(680, 80);
            graphicsTextureDraw.setImageSize(40, 40);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.SUN) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_map); // Most shops in Sun wrongly pull from eveg; only Shalom III has the correct door cover in vanilla
            graphicsTextureDraw.setImagePosition(720, 760);
            graphicsTextureDraw.setImageSize(40, 60);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.SPRING) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY());
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(360, 80);
            graphicsTextureDraw.setImageSize(40, 40);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.INFERNO) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(660, 0);
            graphicsTextureDraw.setImageSize(40, 60);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.EXTINCTION) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(620, 210);
            graphicsTextureDraw.setImageSize(40, 60);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.TWIN_FRONT) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(660, 0);
            graphicsTextureDraw.setImageSize(40, 60);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.ENDLESS) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(380, 100);
            graphicsTextureDraw.setImageSize(40, 60);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.ILLUSION) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY());
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(700, 100);
            graphicsTextureDraw.setImageSize(40, 40);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.GRAVEYARD) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(700, 0);
            graphicsTextureDraw.setImageSize(40, 60);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.MOONLIGHT) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            if(screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
                // Samieru's door
                graphicsTextureDraw.setImagePosition(760, 0);
            }
            else if(screen.getRoomIndex() == 6 && screen.getScreenIndex() == 0) {
                // Alsedana's door
                graphicsTextureDraw.setImagePosition(800, 0);
            }
            else { //if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                // Kingvalley II's door
                graphicsTextureDraw.setImagePosition(840, 0);
            }
            graphicsTextureDraw.setImageSize(40, 40);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.GODDESS) {
            if(!(screen.getRoomIndex() == 6 && screen.getScreenIndex() == 3)) { // Exclude Naramura's door
                GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY());
                graphicsTextureDraw.setLayer(-1);
                graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
                graphicsTextureDraw.setImagePosition(480, 368);
                graphicsTextureDraw.setImageSize(40, 40);
                graphicsTextureDraw.setAnimation(1, 1, 4, 0);
                graphicsTextureDraw.setCollision(HitTile.Air);
                graphicsTextureDraw.setEntryEffect(0);
                graphicsTextureDraw.setExitEffect(0);
                graphicsTextureDraw.setCycleColors(false);
                graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
                graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
                graphicsTextureDraw.setBlendMode(0);
                graphicsTextureDraw.setArg23(1);
                graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
                conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
            }
        }
        else if(screen.getZoneIndex() == ZoneConstants.RUIN) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(840, 0);
            graphicsTextureDraw.setImageSize(40, 60);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.BIRTH_SKANDA) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            if(screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
                // Priest Ashgine's door
                graphicsTextureDraw.setImagePosition(940, 60);
            }
            else { //if(screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
                // Mud Man Qubert's door
                graphicsTextureDraw.setImagePosition(660, 0);
            }
            graphicsTextureDraw.setImageSize(40, 60);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.DIMENSIONAL) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            graphicsTextureDraw.setLayer(-1);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            graphicsTextureDraw.setImagePosition(700, 0);
            graphicsTextureDraw.setImageSize(40, 60);
            graphicsTextureDraw.setAnimation(1, 1, 4, 0);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setRGBAMax(0, 0, 0, 255);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
        else if(screen.getZoneIndex() == ZoneConstants.RETRO_SURFACE) {
            GraphicsTextureDraw graphicsTextureDraw = new GraphicsTextureDraw(screen, conversationDoor.getX(), conversationDoor.getY() - 20);
            graphicsTextureDraw.setLayer(0);
            graphicsTextureDraw.setImageFile(GraphicsTextureDraw.ImageFile_map);
            graphicsTextureDraw.setImagePosition(480, 40);
            graphicsTextureDraw.setImageSize(40, 60);
            graphicsTextureDraw.setAnimation(0, 1, 4, 1);
            graphicsTextureDraw.setCollision(HitTile.Air);
            graphicsTextureDraw.setEntryEffect(0);
            graphicsTextureDraw.setExitEffect(0);
            graphicsTextureDraw.setCycleColors(false);
            graphicsTextureDraw.setRGBAPerFrame(0, 0, 0, 0);
            graphicsTextureDraw.setBlendMode(0);
            graphicsTextureDraw.setArg23(1);
            graphicsTextureDraw.addTests(new TestByteOperation(itemFlagMekuri, ByteOp.FLAG_GTEQ, 2));
            conversationDoor.getObjectContainer().getObjects().add(graphicsTextureDraw);
        }
    }
}
