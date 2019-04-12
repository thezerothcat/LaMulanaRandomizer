package lmr.randomizer.update;

import lmr.randomizer.Settings;

public final class LocationCoordinateMapper {
    private LocationCoordinateMapper() {
    }

    public static byte getStartingZone() {
        if(Settings.isRandomizeStartingLocation()) {
            return 21;
        }
        return 1;
    }

    public static byte getStartingRoom() {
        if(Settings.isRandomizeStartingLocation()) {
            return 0;
        }
        return 2;
    }

    public static byte getStartingScreen() {
        if(Settings.isRandomizeStartingLocation()) {
            return 0;
        }
        return 1;
    }

    public static short getStartingX() {
        if(Settings.isRandomizeStartingLocation()) {
            return 400;
        }
        return 940;
    }

    public static short getStartingY() {
        if(Settings.isRandomizeStartingLocation()) {
            return 160;
        }
        return 160;
    }

    public static String getStartingScreenName() {
        if(Settings.isRandomizeStartingLocation()) {
            return "Gate of Time (Surface)";
        }
        return "Village of Departure";
    }
}
