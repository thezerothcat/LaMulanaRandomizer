package lmr.randomizer.update;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.Settings;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.util.BlockConstants;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.util.LocationCoordinateMapper;
import lmr.randomizer.util.ZoneConstants;

public class ScannableUpdates {
    private ScannableUpdates() { }

    public static boolean updateScannable(Scannable scannable) {
        ObjectContainer objectContainer = scannable.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        updateBlankTabletFlag(scannable);
        Screen screen = (Screen)objectContainer;
        updateScannableReadFlag(scannable);
        updateGrailFlag(scannable, screen.getZoneIndex());
        if(Settings.isRandomizeNonBossDoors()) {
            if (screen.getZoneIndex() == 1 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 0) {
                for(TestByteOperation testByteOperation : scannable.getTestByteOperations()) {
                    if (testByteOperation.getIndex() == FlagConstants.VIY_GATE_MIRROR_COVER) {
                        return false;
                    }
                }
            }
            if (screen.getZoneIndex() == 6 && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                for(TestByteOperation testByteOperation : scannable.getTestByteOperations()) {
                    if (testByteOperation.getIndex() == FlagConstants.KEY_FAIRY_DOOR_UNLOCKED) {
                        return false;
                    }
                }
            }

            for(TestByteOperation testByteOperation : scannable.getTestByteOperations()) {
                if (testByteOperation.getIndex() == FlagConstants.WF_BRONZE_MIRROR
                        && ByteOp.FLAG_EQUALS.equals(testByteOperation.getOp())
                        && testByteOperation.getValue() == 0) {
                    return false;
                }
            }
        }

        if(LocationCoordinateMapper.getStartingZone() == ZoneConstants.BIRTH_SKANDA) {
            if(screen.getZoneIndex() == ZoneConstants.BIRTH_SKANDA
                    && screen.getRoomIndex() == LocationCoordinateMapper.getStartingRoom()
                    && screen.getScreenIndex() == LocationCoordinateMapper.getStartingScreen()
                    && scannable.getX() == 340 && scannable.getY() == 320) {
                // Move Chamber of Birth grail point when starting in that area.
                scannable.setX(200);
                scannable.setY(380);
            }
        }

        int languageBlock = scannable.getArgs().get(0);

        if(languageBlock == 648) {
            for (TestByteOperation flagTest : scannable.getTestByteOperations()) {
                if (flagTest.getIndex() == FlagConstants.MANTRA_FINAL && flagTest.getValue() == 4) {
                    flagTest.setIndex(FlagConstants.MANTRA_LAMULANA);
                    flagTest.setValue((byte)1);
                    break;
                }
            }
        }

        for (TestByteOperation flagTest : scannable.getTestByteOperations()) {
            if(flagTest.getIndex() == FlagConstants.COG_MUDMEN_STATE) {
                if(flagTest.getValue() == 3) {
                    if(ByteOp.FLAG_EQUALS.equals(flagTest.getOp())) {
                        flagTest.setOp(ByteOp.FLAG_LTEQ);
                        scannable.setX(scannable.getX() - 60);
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
        }
        return true;
    }

    private static void updateBlankTabletFlag(Scannable scannable) {
        if(scannable.getTextBlock() == BlockConstants.BrokenTablet_NoText) {
            if(!scannable.getWriteByteOperations().isEmpty()) {
                scannable.getWriteByteOperations().get(0).setIndex(FlagConstants.TABLET_GLOW_GUIDANCE_ENTRANCE_BROKEN);
            }
        }
    }

    private static void updateGrailFlag(Scannable scannable, int zoneIndex) {
        if(scannable.getTextBlock() == BlockConstants.GrailTablet_Surface
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Guidance
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Mausoleum
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Sun
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Spring
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Inferno
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Extinction
                || scannable.getTextBlock() == BlockConstants.GrailTablet_TwinFront
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Endless
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Shrine
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Illusion
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Graveyard
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Moonlight
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Goddess
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Ruin
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Birth
                || scannable.getTextBlock() == BlockConstants.GrailTablet_TwinBack
                || scannable.getTextBlock() == BlockConstants.GrailTablet_Dimensional) {
            if(zoneIndex == ZoneConstants.TWIN_FRONT) {
                boolean frontside = scannable.hasUpdate(new WriteByteOperation(FlagConstants.TABLET_GRAIL_TWIN_FRONT, ByteOp.ASSIGN_FLAG, 1));
                scannable.getTestByteOperations().clear();
                scannable.getTestByteOperations().add(new TestByteOperation(LocationCoordinateMapper.getGrailFlag(zoneIndex, frontside), ByteOp.FLAG_EQUALS, 0));
                scannable.getWriteByteOperations().clear();
                scannable.getWriteByteOperations().add(new WriteByteOperation(LocationCoordinateMapper.getGrailFlag(zoneIndex, frontside), ByteOp.ASSIGN_FLAG, 1));
            }
            else {
                scannable.getTestByteOperations().clear();
                scannable.getTestByteOperations().add(new TestByteOperation(LocationCoordinateMapper.getGrailFlag(zoneIndex, true), ByteOp.FLAG_EQUALS, 0));

                scannable.getWriteByteOperations().clear();
                scannable.getWriteByteOperations().add(new WriteByteOperation(LocationCoordinateMapper.getGrailFlag(zoneIndex, true), ByteOp.ASSIGN_FLAG, 1));
            }
        }
    }

    private static void updateScannableReadFlag(GameObject scannable) {
        if(scannable.getArgs().get(0) == BlockConstants.Tablet_Retromausoleum_An8BitWorld) {
            scannable.getWriteByteOperations().clear();
            return;
        }
        if(scannable.getWriteByteOperations().size() == 1) {
            WriteByteOperation writeByteOperation = scannable.getWriteByteOperations().get(0);
            if(writeByteOperation.getValue() == 1
                    && ByteOp.ASSIGN_FLAG.equals(writeByteOperation.getOp())
                    && DataFromFile.getRemovedTabletGlowFlags().contains(writeByteOperation.getIndex())) {
                scannable.removeUpdate(new WriteByteOperation(writeByteOperation.getIndex(), ByteOp.ASSIGN_FLAG, 1));
            }
        }
    }
}
