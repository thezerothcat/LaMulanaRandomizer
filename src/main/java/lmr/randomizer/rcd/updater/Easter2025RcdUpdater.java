package lmr.randomizer.rcd.updater;

import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.rcd.RcdFileData;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.AddObject;
import lmr.randomizer.util.*;

import java.util.Arrays;
import java.util.Random;

public class Easter2025RcdUpdater extends RcdUpdater {
    public Easter2025RcdUpdater(RcdFileData rcdFileData, DatFileData datFileData) {
        super(rcdFileData, datFileData);
    }

    @Override
    boolean updateWarpPortal(GameObject warpPortal) {
        ObjectContainer objectContainer = warpPortal.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == ZoneConstants.EXTINCTION && screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
            warpPortal.getTestByteOperations().clear();
            warpPortal.addTests(new TestByteOperation(FlagConstants.HT_UNLOCK_CHAIN_PRIMARY, ByteOp.FLAG_GTEQ, 8),
                    new TestByteOperation(FlagConstants.MULBRUK_CONVERSATION_HT, ByteOp.FLAG_NOT_EQUAL, 0),
                    new TestByteOperation(FlagConstants.PALENQUE_STATE, ByteOp.FLAG_NOT_EQUAL, 2));
        }
        return true;
    }

    @Override
    boolean updateScannable(Scannable scannable) {
        ObjectContainer objectContainer = scannable.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(scannable.getTextBlock() == BlockConstants.Skeleton_RedOneTooStrong) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(33), ByteOp.FLAG_GTEQ, 1));
        }
        if(scannable.getTextBlock() == BlockConstants.Scannable_HotSprings) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(2), ByteOp.FLAG_GTEQ, 1));
        }
        if(scannable.getTextBlock() == BlockConstants.ResearchMode_Volcano) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(4), ByteOp.FLAG_GTEQ, 1));
        }
        if(scannable.getTextBlock() == BlockConstants.Tablet_Moonlight_AtopTheNavelOfNeptune) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(9), ByteOp.FLAG_GTEQ, 1));
        }
        if(scannable.getTextBlock() == BlockConstants.Tablet_Mausoleum_Migela) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(28), ByteOp.FLAG_GTEQ, 1));
        }
        if(scannable.getTextBlock() == BlockConstants.ResearchMode_Endless_Keyhole) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(20), ByteOp.FLAG_GTEQ, 1));
        }
        if(scannable.getTextBlock() == BlockConstants.ArchaeologyDictionary_MayanAirship) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(32), ByteOp.FLAG_GTEQ, 1));
        }
        if(scannable.getTextBlock() == BlockConstants.Tablet_Mausoleum_Hardmode_Activation) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(34), ByteOp.FLAG_GTEQ, 1));
        }
        if(scannable.getTextBlock() == BlockConstants.ResearchMode_Waterweed) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(36), ByteOp.FLAG_GTEQ, 1));
        }
        if(scannable.getTextBlock() == BlockConstants.ArchaeologyDictionary_AjantaCaves) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(38), ByteOp.FLAG_GTEQ, 1));
        }
        if(scannable.getTextBlock() == BlockConstants.Scannable_Laptop) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(93), ByteOp.FLAG_GTEQ, 1));
        }
        if(scannable.getTextBlock() == BlockConstants.RuinsDictionary_EyeofRetribution) {
            if(screen.getZoneIndex() == ZoneConstants.SUN && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 5) {
                scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(31), ByteOp.FLAG_GTEQ, 1));
            }
        }
        if(scannable.getTextBlock() == BlockConstants.ArchaeologyDictionary_MuralDepictingAFace) {
            if(screen.getZoneIndex() == ZoneConstants.GUIDANCE && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 1) {
                scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(89), ByteOp.FLAG_GTEQ, 1));
            }
        }
        if(scannable.getTextBlock() == BlockConstants.Tablet_Retromausoleum_BeholdGateOfTime) {
            scannable.addTests(new TestByteOperation(EggConstants.getEggFlag(58), ByteOp.FLAG_GTEQ, 1));
        }

        return true;
    }

    @Override
    boolean updateLemezaDetector(GameObject lemezaDetector) {
        ObjectContainer objectContainer = lemezaDetector.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == ZoneConstants.HT_1 && screen.getRoomIndex() == 9 && screen.getScreenIndex() == 0) {
            if(lemezaDetector.hasTest(new TestByteOperation(FlagConstants.SCREEN_FLAG_E, ByteOp.FLAG_EQUALS, 0))) {
                lemezaDetector.getArgs().set(1, (short)5); // Add a few frames of delay before the animation is activated
            }
        }
        return true;
    }

    @Override
    void addUntrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == ZoneConstants.SURFACE || zoneIndex == ZoneConstants.NIGHT_SURFACE) {
            if(roomIndex == 1 && screenIndex == 1) {
                addHiddenEgg(screen, 40, 840, 2);
            }
            if(roomIndex == 2 && screenIndex == 0) {
                addHiddenEgg(screen, 300, 160, 1);
            }
//            if(roomIndex == 2 && screenIndex == 1) {
//                addHiddenEgg(screen, 940, 160, 1);
//            }
            if(roomIndex == 5 && screenIndex == 2) {
                addHiddenEgg(screen, 440, 1040, 94);
            }
            if(roomIndex == 7 && screenIndex == 1) {
                addHiddenEgg(screen, 840, 240, 71);

                GraphicsTextureDraw nest = new GraphicsTextureDraw(screen, 840, 240);
                nest.setLayer(1);
                nest.setImageFile(GraphicsTextureDraw.ImageFile_01effect);
                nest.setImageX(840);
                nest.setImageY(512);
                nest.setImageWidth(40);
                nest.setImageHeight(40);
                nest.setAnimation(0, 1, 0, 0);
                nest.setCollision(HitTile.Air);
                nest.setRGBAMax(0, 0, 0, 255);
                nest.setArg23(1);
                screen.getObjects().add(nest);
            }
            if(roomIndex == 10 && screenIndex == 3) {
                addHiddenEgg(screen, 1120, 800, 67);
            }
        }
        if(zoneIndex == ZoneConstants.GUIDANCE) {
            if(roomIndex == 0 && screenIndex == 0) {
                addHiddenEgg(screen, 200, 320, 12);
            }
            if(roomIndex == 3 && screenIndex == 1) {
                addHiddenEgg(screen, 980, 180, 89,
                        new TestByteOperation(FlagConstants.GUIDANCE_PUZZLE_TRAP_FACE, ByteOp.FLAG_EQUALS, 2));
            }
            if(roomIndex == 4 && screenIndex == 1) {
                addHiddenEgg(screen, 380, 520, 82);
            }
            if(roomIndex == 6 && screenIndex == 1) {
                addHiddenEgg(screen, 1020, 160, 32);
            }
            if(roomIndex == 7 && screenIndex == 0) {
                addHiddenEgg(screen, 0, 80, 33);
            }
        }
        if(zoneIndex == ZoneConstants.MAUSOLEUM) {
            if(roomIndex == 1 && screenIndex == 1) {
                addHiddenEgg(screen, 780, 400, 28);
            }
            if(roomIndex == 2 && screenIndex == 0) {
                addHiddenEgg(screen, 560, 60, 34);
            }
            if(roomIndex == 4 && screenIndex == 0) {
                addHiddenEgg(screen, 240, 140, 13);
            }
            if(roomIndex == 8 && screenIndex == 2) {
                addHiddenEgg(screen, 1780, 20, 76);
            }
        }
        if(zoneIndex == ZoneConstants.SUN) {
            if(roomIndex == 0 && screenIndex == 1) {
                addHiddenEgg(screen, 1140, 400, 6);
            }
            if(roomIndex == 1 && screenIndex == 0) {
                addHiddenEgg(screen, 320, 340, 73);
            }
            if(roomIndex == 3 && screenIndex == 0) {
                addHiddenEgg(screen,  100, 80, 74);
            }
            if(roomIndex == 4 && screenIndex == 5) {
                addHiddenEgg(screen, 1280, 520, 31);
            }
            if(roomIndex == 7 && screenIndex == 1) {
                addHiddenEgg(screen, 900, 400, -1, 35);
            }
        }
        if(zoneIndex == ZoneConstants.SPRING) {
            if(roomIndex == 2 && screenIndex == 0) {
                addHiddenEgg(screen, 320, 400, 14);
            }
            if(roomIndex == 2 && screenIndex == 1) {
                addHiddenEgg(screen, 0, 560, 96);
            }
            if(roomIndex == 3 && screenIndex == 0) {
                addHiddenEgg(screen, 560, 120, 37);
            }
            if(roomIndex == 7 && screenIndex == 0) {
                addHiddenEgg(screen, 580, 320, 36);
            }
        }
        if(zoneIndex == ZoneConstants.INFERNO) {
            if(roomIndex == 3 && screenIndex == 0) {
                addHiddenEgg(screen, 40, 160, 27);
            }
            if(roomIndex == 3 && screenIndex == 2) {
                addHiddenEgg(screen, 1580, 160, 79);
            }
            if(roomIndex == 4 && screenIndex == 0) {
                addHiddenEgg(screen, 360, 80, 39);
            }
            if(roomIndex == 6 && screenIndex == 0) {
                addHiddenEgg(screen, 300, 300, 38);
            }
            if(roomIndex == 9 && screenIndex == 0) {
                addHiddenEgg(screen, 20, 400, 8);
            }
        }
        if(zoneIndex == ZoneConstants.EXTINCTION) {
            if(roomIndex == 0 && screenIndex == 0) {
                addHiddenEgg(screen, 0, 400, 100);
            }
            if(roomIndex == 3 && screenIndex == 0) {
                addHiddenEgg(screen, 300, 80, 70);
            }
            if(roomIndex == 7 && screenIndex == 1) {
                addHiddenEgg(screen, 900, 240, 40);
            }
            if(roomIndex == 9 && screenIndex == 0) {
                addHiddenEgg(screen, 320, 60, 5);
            }
        }
        if(zoneIndex == ZoneConstants.TWIN_FRONT) {
            if(roomIndex == 1 && screenIndex == 0) {
                addHiddenEgg(screen, 300, 260, 4);
            }
            if(roomIndex == 2 && screenIndex == 0) {
                addHiddenEgg(screen, 480, 320, 88);
            }
            if(roomIndex == 8 && screenIndex == 1) {
                addHiddenEgg(screen, 1220, 400, 42);
            }
            if(roomIndex == 9 && screenIndex == 0) {
                addHiddenEgg(screen, 240, 400, 41);
            }
            if(roomIndex == 12 && screenIndex == 2) {
                addHiddenEgg(screen, 60, 1100, 99);
            }
            if(roomIndex == 15 && screenIndex == 1) {
                addHiddenEgg(screen, 1140, 280, 84);
            }
        }
        if(zoneIndex == ZoneConstants.ENDLESS) {
            if(roomIndex == 1 && screenIndex == 1) {
                addHiddenEgg(screen, 900, 320, 20);
            }
            if(roomIndex == 3 && screenIndex == 2) {
                addHiddenEgg(screen, 1500, 400, 90);
            }
            if(roomIndex == 4 && screenIndex == 3) {
                addHiddenEgg(screen, 2400, 120, 19);
            }
            if(roomIndex == 5 && screenIndex == 2) {
                addHiddenEgg(screen, 1580, 240, 30);
            }
        }
        if(zoneIndex == ZoneConstants.SHRINE_FRONT) {
            if(roomIndex == 0 && screenIndex == 0) {
                addHiddenEgg(screen, 20, 240, 21);
            }
            if(roomIndex == 2 && screenIndex == 1) {
                addHiddenEgg(screen, 1040, 40, 43);
            }
            if(roomIndex == 3 && screenIndex == 0) {
                addHiddenEgg(screen, 300, 400, 93);
            }
            if(roomIndex == 5 && screenIndex == 0) {
                addHiddenEgg(screen, 180, 240, 23);
            }
            if(roomIndex == 9 && screenIndex == 1) {
                addHiddenEgg(screen, 940, 80, 86);
            }
        }
        if(zoneIndex == ZoneConstants.ILLUSION) {
            if(roomIndex == 2 && screenIndex == 0) {
                addHiddenEgg(screen, 580, 220, 45);
            }
            if(roomIndex == 5 && screenIndex == 1) {
                addHiddenEgg(screen, 1000, 60, 15);
            }
            if(roomIndex == 6 && screenIndex == 0) {
                addHiddenEgg(screen, 20, 220, 75);
            }
            if(roomIndex == 9 && screenIndex == 0) {
                addHiddenEgg(screen, 280, 320, 44);
            }
            if(roomIndex == 9 && screenIndex == 1) {
                addHiddenEgg(screen, 1160, 200, 87);
            }
        }
        if(zoneIndex == ZoneConstants.GRAVEYARD) {
            if(roomIndex == 1 && screenIndex == 0) {
                addHiddenEgg(screen, 20, 420, 3);
            }
            if(roomIndex == 3 && screenIndex == 0) {
                addHiddenEgg(screen, 480, 400, 11);
            }
            if(roomIndex == 4 && screenIndex == 2) {
                addHiddenEgg(screen, 20, 1360, 95);
            }
            if(roomIndex == 7 && screenIndex == 1) {
                addHiddenEgg(screen, 520, 900, 68);
            }
        }
        if(zoneIndex == ZoneConstants.MOONLIGHT) {
            if(roomIndex == 0 && screenIndex == 1) {
                addHiddenEgg(screen, 540, 640, 9)
                        .addUpdates(new WriteByteOperation(FlagConstants.SCREEN_FLAG_C, ByteOp.ASSIGN_FLAG, 1));
                addHiddenEgg(screen, 40, 760, 69)
                        .addUpdates(new WriteByteOperation(FlagConstants.SCREEN_FLAG_C, ByteOp.ASSIGN_FLAG, 2));
            }
            if(roomIndex == 3 && screenIndex == 0) {
                addHiddenEgg(screen, 0, 80, 46);
            }
            if(roomIndex == 4 && screenIndex == 3) {
                addHiddenEgg(screen, 400, 1660, 1, 29);
            }
            if(roomIndex == 6 && screenIndex == 0) {
                addHiddenEgg(screen, 340, 160, 50);
            }
            if(roomIndex == 9 && screenIndex == 1) {
                addHiddenEgg(screen, 660, 240, 49);
            }
        }
        if(zoneIndex == ZoneConstants.GODDESS) {
            if(roomIndex == 0 && screenIndex == 1) {
                addHiddenEgg(screen, 100, 720, 1, 81);
            }
            if(roomIndex == 1 && screenIndex == 1) {
                addHiddenEgg(screen, 560, 760, 92);
            }
            if(roomIndex == 2 && screenIndex == 1) {
                int eggFlag = EggConstants.getEggFlag(48);
                GraphicsTextureDraw eggGraphic = new GraphicsTextureDraw(screen, 300, 700);
                eggGraphic.setLayer(0);
                eggGraphic.setImageFile(GraphicsTextureDraw.ImageFile_01effect);
                eggGraphic.setImageX(EggConstants.getEggImageX(48));
                eggGraphic.setImageY(EggConstants.getEggImageY(48));
                eggGraphic.setImageWidth(40);
                eggGraphic.setImageHeight(40);
                eggGraphic.setAnimation(0, 1, 0, 0);
                eggGraphic.setCollision(HitTile.Air);
                eggGraphic.setRGBAMax(0, 0, 0, 255);
                eggGraphic.setArg23(1);
                eggGraphic.addTests(new TestByteOperation(eggFlag, ByteOp.FLAG_LT, 1));
                screen.getObjects().add(eggGraphic);
            }
            if(roomIndex == 3 && screenIndex == 1) {
                addHiddenEgg(screen, 120, 700, 7);
            }
            if(roomIndex == 4 && screenIndex == 0) {
                addHiddenEgg(screen, 580, 80, 47);
            }
            if(roomIndex == 5 && screenIndex == 0) {
                addHiddenEgg(screen, 180, 220, 80);
            }
            if(roomIndex == 8 && screenIndex == 0) {
                addHiddenEgg(screen, 300, 220, 48);
            }
        }
        if(zoneIndex == ZoneConstants.RUIN) {
            if(roomIndex == 1 && screenIndex == 1) {
                addHiddenEgg(screen, 820, 60, 51);
            }
            if(roomIndex == 2 && screenIndex == 1) {
                addHiddenEgg(screen, 300, 880, 83);
            }
            if(roomIndex == 5 && screenIndex == 0) {
                addHiddenEgg(screen, 140, 320, 16);
            }
            if(roomIndex == 8 && screenIndex == 2) {
                addHiddenEgg(screen, 1380, 260, 52);
            }
        }
        if(zoneIndex == ZoneConstants.BIRTH_SWORDS) {
            if(roomIndex == 1 && screenIndex == 1) {
                addHiddenEgg(screen, 720, 320, 17);
            }
            if(roomIndex == 4 && screenIndex == 0) {
                addHiddenEgg(screen, 20, 80, 54);
            }
        }
        if(zoneIndex == ZoneConstants.BIRTH_SKANDA) {
            if(roomIndex == 0 && screenIndex == 0) {
                addHiddenEgg(screen, 380, 400, 24);
            }
            if(roomIndex == 3 && screenIndex == 1) {
                addHiddenEgg(screen, 640, 160, 77);
            }
        }
        if(zoneIndex == ZoneConstants.DIMENSIONAL) {
            if(roomIndex == 1 && screenIndex == 0) {
                addHiddenEgg(screen, 560, 100, 18);
            }
            if(roomIndex == 4 && screenIndex == 0) {
                addHiddenEgg(screen, 540, 400, 98);
            }
            if(roomIndex == 8 && screenIndex == 0) {
                addHiddenEgg(screen, 300, 20, 53);
            }
            if(roomIndex == 10 && screenIndex == 1) {
                addHiddenEgg(screen, 40, 560, 55);
            }
        }
        if(zoneIndex == ZoneConstants.SHRINE_BACK) {
            if(roomIndex == 0 && screenIndex == 0) {
                addHiddenEgg(screen, 20, 240, 22);
            }
            if(roomIndex == 1 && screenIndex == 1) {
                addHiddenEgg(screen, 1120, 400, 56);
            }
            if(roomIndex == 3 && screenIndex == 1) {
                addHiddenEgg(screen, 980, 320, 57);
            }
            if(roomIndex == 5 && screenIndex == 1) {
                addHiddenEgg(screen, 1220, 100, 91);
            }
            if(roomIndex == 8 && screenIndex == 1) {
                addHiddenEgg(screen, 20, 700, 85);
            }
        }
        if(zoneIndex == ZoneConstants.RETRO_MAUSOLEUM) {
            if(roomIndex == 2 && screenIndex == 1) {
                addHiddenEgg(screen, 800, 300, 58);
            }
            if(roomIndex == 3 && screenIndex == 1) {
                addHiddenEgg(screen, 40, 560, 97);
            }
        }
        if(zoneIndex == ZoneConstants.RETRO_GUIDANCE) {
            if(roomIndex == 1 && screenIndex == 0) {
                addHiddenEgg(screen, 420, 240, 59);
            }
            if(roomIndex == 4 && screenIndex == 0) {
                addHiddenEgg(screen, 0, 180, 60);
            }
        }
        if(zoneIndex == ZoneConstants.RETRO_SURFACE) {
            if(roomIndex == 0 && screenIndex == 0) {
                addHiddenEgg(screen, 240, 320, 25)
                        .addUpdates(new WriteByteOperation(FlagConstants.SCREEN_FLAG_C, ByteOp.ASSIGN_FLAG, 1));
                addHiddenEgg(screen, 340, 140, 1, 26)
                        .addUpdates(new WriteByteOperation(FlagConstants.SCREEN_FLAG_C, ByteOp.ASSIGN_FLAG, 2));
            }
        }
        if(zoneIndex == ZoneConstants.HT_1) {
            if(roomIndex == 6 && screenIndex == 1) {
                addHiddenEgg(screen, 680, 400, 63);
            }
            if(roomIndex == 8 && screenIndex == 0) {
                addHiddenEgg(screen, 300, 160, 66);
            }
            if(roomIndex == 9 && screenIndex == 0) {
                addHiddenEgg(screen, 560, 240, 10);
            }
            if(roomIndex == 14 && screenIndex == 1) {
                addHiddenEgg(screen, 1120, 260, 72);
            }
            if(roomIndex == 22 && screenIndex == 0) {
                addHiddenEgg(screen, 40, 240, 65);
            }
        }
    }

    @Override
    public void doPostShuffleUpdates() {
        Random variableEggRandom = new Random();

        // Add Burning Cavern RNG eggs.
        int roomIndex = variableEggRandom.nextInt(4);
        int screenIndex = roomIndex == 0
                ? (variableEggRandom.nextInt(4) + 1)
                : variableEggRandom.nextInt(5);
        Screen burningCavernEggScreen = rcdFileData.getScreen(ZoneConstants.BURNING_CAVERN, roomIndex, screenIndex);

        int x = 80;
        int y = 20 * variableEggRandom.nextInt(22) + 480 * screenIndex;
        addHiddenEgg(burningCavernEggScreen, x, y, 61);

        roomIndex = variableEggRandom.nextInt(4);
        screenIndex = roomIndex == 0
                ? (variableEggRandom.nextInt(4) + 1)
                : variableEggRandom.nextInt(5);
        burningCavernEggScreen = rcdFileData.getScreen(ZoneConstants.BURNING_CAVERN, roomIndex, screenIndex);
        x = 520;
        y = 20 * variableEggRandom.nextInt(20) + 480 * screenIndex;
        addHiddenEgg(burningCavernEggScreen, x, y, 62);

        // Add HT random egg
        Screen pillarScreen = rcdFileData.getScreen(ZoneConstants.HT_1, 6 , 0);
        x = 140 + 80 * variableEggRandom.nextInt(5);
        y = 400;
        addHiddenEgg(pillarScreen, x, y, 64);

        // Add Inferno random egg
        Screen infernoEntranceScreen = rcdFileData.getScreen(ZoneConstants.INFERNO, 0 , 1);
        x = 800 + 120 * variableEggRandom.nextInt(3);
        y = 180;
        addHiddenEgg(infernoEntranceScreen, x, y, 78);
    }

    private UseItemDetector addHiddenEgg(Screen screen, int x, int y, int eggNumber, TestByteOperation... tests) {
        return addHiddenEgg(screen, x, y, 0, eggNumber, tests);
    }

    private UseItemDetector addHiddenEgg(Screen screen, int x, int y, int layer, int eggNumber, TestByteOperation... tests) {
        int eggFlag = EggConstants.getEggFlag(eggNumber);

        GraphicsTextureDraw eggGraphic = new GraphicsTextureDraw(screen, x, y);
        eggGraphic.setLayer(layer);
        eggGraphic.setImageFile(GraphicsTextureDraw.ImageFile_01effect);
        eggGraphic.setImageX(EggConstants.getEggImageX(eggNumber));
        eggGraphic.setImageY(EggConstants.getEggImageY(eggNumber));
        eggGraphic.setImageWidth(eggNumber == EggConstants.TOTAL_HIDDEN_EGGS ? 60 : 40);
        eggGraphic.setImageHeight(40);
        eggGraphic.setAnimation(0, 1, 0, 0);
        eggGraphic.setCollision(HitTile.Air);
        eggGraphic.setRGBAMax(0, 0, 0, 255);
        eggGraphic.setArg23(1);
        eggGraphic.addTests(new TestByteOperation(eggFlag, ByteOp.FLAG_LT, 1));
        for(TestByteOperation test : tests) {
            eggGraphic.addTests(test);
        }
        screen.getObjects().add(eggGraphic);

        return addEggDetection(screen, x, y, eggNumber, tests);
    }

    private UseItemDetector addEggDetection(Screen screen, int x, int y, int eggNumber, TestByteOperation... tests) {
        int offsetX = EggConstants.getEggInnerXOffset(eggNumber);
        int detectionX = x;
        int xWidth = 4;
        if (offsetX < 10 && x % 640 != 0) {
            detectionX -= 20;
        }
        else if (offsetX >= 30) {
            detectionX += 20;
            if (x % 640 == 600) {
                xWidth = 2;
            }
        }
        int offsetY = EggConstants.getEggInnerYOffset(eggNumber);
        int detectionY = y;
        int yHeight = 6;
        if (offsetY < 10) {
            if (y % 480 != 0) {
                detectionY -= 40;
            } else {
                yHeight = 4;
            }
        }
        else if (offsetY < 30) {
            if (y % 480 != 0) {
                detectionY -= 20;
            } else {
                yHeight = 4;
            }
        }

        if (eggNumber == 10) {
            yHeight = 2;
        }
        else if (eggNumber == 64 || eggNumber == 100) {
            xWidth = 2;
        }

        int eggFlag = EggConstants.getEggFlag(eggNumber);
        UseItemDetector useItemDetector;
        if(eggNumber == 34) {
            useItemDetector = (UseItemDetector)AddObject.addUseItemDetector(screen, detectionX, detectionY, xWidth, yHeight, "Hand Scanner")
                    .addTests(new TestByteOperation(eggFlag, ByteOp.FLAG_EQUALS, 0),
                    new TestByteOperation(FlagConstants.HARDMODE, ByteOp.FLAG_NOT_EQUAL, 0))
                    .addUpdates(new WriteByteOperation(eggFlag, ByteOp.ASSIGN_FLAG, 1));
        }
        else {
            useItemDetector = (UseItemDetector)AddObject.addUseItemDetector(screen, detectionX, detectionY, xWidth, yHeight, "Hand Scanner")
                    .addTests(new TestByteOperation(eggFlag, ByteOp.FLAG_EQUALS, 0))
                    .addUpdates(new WriteByteOperation(eggFlag, ByteOp.ASSIGN_FLAG, 1));
        }
        for(TestByteOperation test : tests) {
            useItemDetector.addTests(test);
        }

        AddObject.addItemGive(screen, x / 640 * 640, y / 480 * 480, ItemConstants.WATERPROOF_CASE,
                Arrays.asList(
                        new TestByteOperation(eggFlag, ByteOp.FLAG_EQUALS, 1)),
                Arrays.asList(
                        new WriteByteOperation(eggFlag, ByteOp.ASSIGN_FLAG, 2),
                        new WriteByteOperation(FlagConstants.CUSTOM_EASTER2025_TOTAL_EGGS, ByteOp.ADD_FLAG, 1)))
                .setSoundEffect(SoundEffect.ItemCollected);
        return useItemDetector;
    }
}
