package lmr.randomizer.update;

import lmr.randomizer.HolidaySettings;
import lmr.randomizer.randomization.data.BacksideDoorData;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.Settings;
import lmr.randomizer.rcd.object.*;

import java.util.List;

public final class BacksideDoorUpdates {
    private BacksideDoorUpdates() { }

    public static void updateBacksideDoorV1(BacksideDoorData backsideDoorData, List<GameObject> objectsToModify) {
        int bossFlag = FlagConstants.getBossFlag(backsideDoorData.getDoorBoss());
        int gateFlag = getGateFlag(backsideDoorData.getDoorBoss());
        int mirrorCoverFlag = getMirrorCoverFlag(backsideDoorData.getDoorBoss());
        for(GameObject gameObject : objectsToModify) {
            if(gameObject.getId() == ObjectIdConstants.WarpDoor) {
                replaceBacksideDoorFlags(gameObject, bossFlag, gateFlag, "Door: B5".equals(backsideDoorData.getDoorName()));
                replaceBacksideDoorArgs(gameObject, backsideDoorData.getDoorDestination());
                AddObject.addBossNumberGraphic(gameObject, backsideDoorData.getDoorBoss(), mirrorCoverFlag);
            }
            else if(gameObject.getId() == ObjectIdConstants.FlagTimer) {
                if(isMirrorFlagObject(gameObject)) {
                    replaceMirrorCoverTimerFlags(gameObject, mirrorCoverFlag);
                }
                else {
                    replaceBacksideDoorTimerFlags(gameObject, bossFlag, gateFlag, "Door: B4".equals(backsideDoorData.getDoorName()));
                }
            }
            else if(gameObject.getId() == ObjectIdConstants.GraphicsTextureDraw) {
                if(isMirrorFlagObject(gameObject)) {
                    replaceMirrorCoverGraphicFlags(gameObject, mirrorCoverFlag);
                }
                else {
                    replaceBacksideDoorCoverFlags(gameObject, gateFlag);
                }
            }
        }
    }

    public static void updateBacksideDoorV2(BacksideDoorData backsideDoorData, List<GameObject> objectsToModify) {
        Integer gateFlag = getGateFlag(backsideDoorData.getDoorBoss());
        for(GameObject gameObject : objectsToModify) {
            replaceBacksideDoorFlags(gameObject, backsideDoorData.getDoorBoss(), gateFlag, isDoorDisabledForEscape(backsideDoorData.getDoorName()));
            replaceBacksideDoorArgs(gameObject, backsideDoorData.getDoorDestination());
            if("Door: F8".equals(backsideDoorData.getDoorDestination())) {
                AddObject.addGrailToggle(gameObject.getObjectContainer(), true);
            }
            AddObject.addNumberlessBacksideDoorGraphic(gameObject);
            if(backsideDoorData.getDoorBoss() != null) {
                if(backsideDoorData.getDoorBoss() == 9) {
                    AddObject.addKeyFairyDoorTimerAndSounds(gameObject.getObjectContainer());
                    AddObject.addBacksideDoorKeyFairyPoint(gameObject);
                    AddObject.addAnimatedDoorCover(gameObject, FlagConstants.KEY_FAIRY_DOOR_UNLOCKED, false);
                    AddObject.addBossNumberGraphicV2(gameObject, backsideDoorData.getDoorBoss(), null);
                }
                else {
                    int mirrorCoverFlag = getMirrorCoverFlag(backsideDoorData.getDoorBoss());
                    AddObject.addAnimatedDoorTimerAndSound(gameObject.getObjectContainer(), FlagConstants.getBossFlag(backsideDoorData.getDoorBoss()), gateFlag);
                    AddObject.addMirrorCoverTimerAndSound(gameObject.getObjectContainer(), mirrorCoverFlag);
                    AddObject.addMirrorCoverGraphic(gameObject, mirrorCoverFlag);
                    AddObject.addAnimatedDoorCover(gameObject, gateFlag, true);
                    AddObject.addBossNumberGraphicV2(gameObject, backsideDoorData.getDoorBoss(), mirrorCoverFlag);
                }
            }
        }
    }

    private static void replaceBacksideDoorFlags(GameObject gameObject, Integer bossNumber, Integer gateFlag, boolean motherCheck) {
        gameObject.getTestByteOperations().clear();

        Integer bossFlag = FlagConstants.getBossFlag(bossNumber);
        if(bossFlag != null) {
            gameObject.getTestByteOperations().add(new TestByteOperation(bossFlag, ByteOp.FLAG_EQUALS, bossNumber == 9 ? 1 : 3));
        }

        if(gateFlag != null) {
            gameObject.getTestByteOperations().add(new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 1));
        }

        if(motherCheck && !Settings.isRandomizeTransitionGates()) {
            gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.MOTHER_STATE, ByteOp.FLAG_NOT_EQUAL, 3));
        }
    }

    private static void replaceBacksideDoorArgs(GameObject gameObject, String doorWithCoordinatesData) {
        gameObject.getArgs().clear();
        gameObject.getArgs().add((short)0);

        if("Door: F1".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)10);
            gameObject.getArgs().add((short)3);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)40);
            gameObject.getArgs().add((short)80);
        }
        else if("Door: F2".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)11);
            gameObject.getArgs().add((short)3);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)80);
            gameObject.getArgs().add((short)160);
        }
        else if("Door: F3".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)12);
            gameObject.getArgs().add((short)4);
            gameObject.getArgs().add((short)2);
            gameObject.getArgs().add((short)200);
            gameObject.getArgs().add((short)240);
        }
        else if("Door: F4".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)14);
            gameObject.getArgs().add((short)2);
            gameObject.getArgs().add((short)1);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)240);
        }
        else if("Door: F5".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)13);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)1);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)400);
        }
        else if("Door: F6".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)15);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)1);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)400);
        }
        else if("Door: F7".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)14);
            gameObject.getArgs().add((short)8);
            gameObject.getArgs().add((short)1);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)120);
        }
        else if("Door: F8".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)17);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)320);
        }
        else if("Door: F9".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)19);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)1);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)320);
        }
        else if("Door: B1".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)5);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)180);
            gameObject.getArgs().add((short)160);
        }
        else if("Door: B2".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)2);
            gameObject.getArgs().add((short)4);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)80);
            gameObject.getArgs().add((short)320);
        }
        else if("Door: B3".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)3);
            gameObject.getArgs().add((short)6);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)460);
            gameObject.getArgs().add((short)160);
        }
        else if("Door: B4".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)5);
            gameObject.getArgs().add((short)8);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)140);
            gameObject.getArgs().add((short)160);
        }
        else if("Door: B5".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)(HolidaySettings.isHalloweenMode() ? 22 : 1));
            gameObject.getArgs().add((short)8);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)200);
            gameObject.getArgs().add((short)320);
        }
        else if("Door: B6".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)6);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)40);
            gameObject.getArgs().add((short)80);
        }
        else if("Door: B7".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)5);
            gameObject.getArgs().add((short)9);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)60);
            gameObject.getArgs().add((short)80);
        }
        else if("Door: B8".equals(doorWithCoordinatesData)) {
            if(HolidaySettings.isFools2021Mode()) {
                gameObject.getArgs().add((short)0);
                gameObject.getArgs().add((short)0);
                gameObject.getArgs().add((short)0);
                gameObject.getArgs().add((short)320);
                gameObject.getArgs().add((short)152);
            }
            else {
                gameObject.getArgs().add((short)8);
                gameObject.getArgs().add((short)0);
                gameObject.getArgs().add((short)1);
                gameObject.getArgs().add((short)400);
                gameObject.getArgs().add((short)180);
            }
        }
        else if("Door: B9".equals(doorWithCoordinatesData)) {
            gameObject.getArgs().add((short)6);
            gameObject.getArgs().add((short)7);
            gameObject.getArgs().add((short)0);
            gameObject.getArgs().add((short)300);
            gameObject.getArgs().add((short)80);
        }
    }

    private static void replaceBacksideDoorTimerFlags(GameObject gameObject, int bossFlag, int gateFlag, boolean useSpecialFlag) {
        gameObject.getTestByteOperations().clear();
        gameObject.getTestByteOperations().add(new TestByteOperation(bossFlag, ByteOp.FLAG_GTEQ, 3));
        gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_BRONZE_MIRROR, ByteOp.FLAG_EQUALS, 2));
        gameObject.getTestByteOperations().add(new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0));

        gameObject.getWriteByteOperations().clear();
        gameObject.getWriteByteOperations().add(new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1));
        gameObject.getWriteByteOperations().add(new WriteByteOperation(useSpecialFlag ? FlagConstants.SCREEN_FLAG_C : FlagConstants.SCREEN_FLAG_29,
                ByteOp.ASSIGN_FLAG, 1));
    }

    private static void replaceMirrorCoverTimerFlags(GameObject gameObject, int mirrorCoverFlag) {
        gameObject.getTestByteOperations().clear();
        gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_BRONZE_MIRROR, ByteOp.FLAG_EQUALS, 2));
        gameObject.getTestByteOperations().add(new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0));

        gameObject.getWriteByteOperations().clear();
        gameObject.getWriteByteOperations().add(new WriteByteOperation(mirrorCoverFlag, ByteOp.ASSIGN_FLAG, 1));
        gameObject.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_28, ByteOp.ASSIGN_FLAG, 1));
    }

    private static void replaceBacksideDoorCoverFlags(GameObject gameObject, int gateFlag) {
        gameObject.getTestByteOperations().clear();
        gameObject.getTestByteOperations().add(new TestByteOperation(gateFlag, ByteOp.FLAG_EQUALS, 0));
        gameObject.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_BRONZE_MIRROR, ByteOp.FLAG_EQUALS, 2));

        gameObject.getWriteByteOperations().clear();
        gameObject.getWriteByteOperations().add(new WriteByteOperation(gateFlag, ByteOp.ASSIGN_FLAG, 1));
    }

    private static void replaceMirrorCoverGraphicFlags(GameObject gameObject, int mirrorCoverFlag) {
        gameObject.getTestByteOperations().clear();
        gameObject.getTestByteOperations().add(new TestByteOperation(mirrorCoverFlag, ByteOp.FLAG_EQUALS, 0));
    }

    private static boolean isMirrorFlagObject(GameObject gameObject) {
        for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
            if(testByteOperation.getIndex() == FlagConstants.AMPHISBAENA_GATE_MIRROR_COVER || testByteOperation.getIndex() == FlagConstants.SAKIT_GATE_MIRROR_COVER
                    || testByteOperation.getIndex() == FlagConstants.ELLMAC_GATE_MIRROR_COVER || testByteOperation.getIndex() == FlagConstants.BAHAMUT_GATE_MIRROR_COVER
                    || testByteOperation.getIndex() == FlagConstants.VIY_GATE_MIRROR_COVER || testByteOperation.getIndex() == FlagConstants.PALENQUE_GATE_MIRROR_COVER
                    || testByteOperation.getIndex() == FlagConstants.BAPHOMET_GATE_MIRROR_COVER) {
                return true;
            }
        }
        return false;
    }

    private static Integer getGateFlag(Integer bossNumber) {
        if(bossNumber == null) {
            return null;
        }
        if(bossNumber == 1) {
            return FlagConstants.AMPHISBAENA_GATE_OPEN;
        }
        if(bossNumber == 2) {
            return FlagConstants.SAKIT_GATE_OPEN;
        }
        if(bossNumber == 3) {
            return FlagConstants.ELLMAC_GATE_OPEN;
        }
        if(bossNumber == 4) {
            return FlagConstants.BAHAMUT_GATE_OPEN;
        }
        if(bossNumber == 5) {
            return FlagConstants.VIY_GATE_OPEN;
        }
        if(bossNumber == 6) {
            return FlagConstants.PALENQUE_GATE_OPEN;
        }
        if(bossNumber == 7) {
            return FlagConstants.BAPHOMET_GATE_OPEN;
        }
        return null;
    }

    private static int getMirrorCoverFlag(Integer bossNumber) {
        if(bossNumber == 1) {
            return FlagConstants.AMPHISBAENA_GATE_MIRROR_COVER;
        }
        if(bossNumber == 2) {
            return FlagConstants.SAKIT_GATE_MIRROR_COVER;
        }
        if(bossNumber == 3) {
            return FlagConstants.ELLMAC_GATE_MIRROR_COVER;
        }
        if(bossNumber == 4) {
            return FlagConstants.BAHAMUT_GATE_MIRROR_COVER;
        }
        if(bossNumber == 5) {
            return FlagConstants.VIY_GATE_MIRROR_COVER;
        }
        if(bossNumber == 6) {
            return FlagConstants.PALENQUE_GATE_MIRROR_COVER;
        }
        if(bossNumber == 7) {
            return FlagConstants.BAPHOMET_GATE_MIRROR_COVER;
        }
        return 0;
    }

    private static boolean isDoorDisabledForEscape(String doorToReplace) {
        return "Door: B5".equals(doorToReplace) || "Door: F9".equals(doorToReplace);
    }
}
