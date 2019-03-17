package lmr.randomizer.update;

import lmr.randomizer.Settings;

public final class LocationCoordinateMapper {
    private LocationCoordinateMapper() {
    }

    public static byte getStartingZone() {
        if(Settings.isRandomize1()) {
            return 21;
        }
        return 1;
    }

    public static byte getStartingRoom() {
        if(Settings.isRandomize1()) {
            return 0;
        }
        return 2;
    }

    public static byte getStartingScreen() {
        if(Settings.isRandomize1()) {
            return 0;
        }
        return 1;
    }

    public static short getStartingX() {
        if(Settings.isRandomize1()) {
            return 400;
        }
        return 940;
    }

    public static short getStartingY() {
        if(Settings.isRandomize1()) {
            return 160;
        }
        return 160;
    }
}
