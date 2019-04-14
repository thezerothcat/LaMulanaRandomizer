package lmr.randomizer.update;

import lmr.randomizer.Settings;

public final class LocationCoordinateMapper {
    private LocationCoordinateMapper() {
    }

    public static byte getStartingZone() {
        int zone = Settings.getCurrentStartingLocation();
        if(zone == 1) {
            return 1;
        }
        if(zone == 4) {
            return 4;
        }
        if(zone == 5) {
            return 5;
        }
        if(zone == 10) {
            return 10;
        }
        if(zone == 21) {
            return 21;
        }
        return 1;
    }

    public static byte getStartingRoom() {
        return getStartingRoom(Settings.getCurrentStartingLocation(), isFrontsideStart());
    }

    public static byte getStartingRoom(int zone, boolean front) {
        if(zone == 0) {
            return 1;
        }
        if(zone == 1) {
            return 2;
        }
        if(zone == 2) {
            return 5;
        }
        if(zone == 3) {
            return 0;
        }
        if(zone == 4) {
            return 6;
        }
        if(zone == 5) {
            return 6;
        }
        if(zone == 6) {
            return 6;
        }
        if(zone == 7) {
            return front ? (byte)0 : 15;
        }
        if(zone == 8) {
            return 1;
        }
        if(zone == 9) {
            return 7;
        }
        if(zone == 10) {
            return 4;
        }
        if(zone == 11) {
            return 2;
        }
        if(zone == 12) {
            return 1;
        }
        if(zone == 13) {
            return 7;
        }
        if(zone == 14) {
            return 6;
        }
        if(zone == 15 || zone == 16) {
            return 0;
        }
        if(zone == 17) {
            return 0;
        }
        if(zone == 18) {
            return 7;
        }
        if(zone == 21) {
            return 0;
        }
        return 2;
    }

    public static byte getStartingScreen() {
        return getStartingScreen(Settings.getCurrentStartingLocation(), isFrontsideStart());
    }

    public static byte getStartingScreen(int zone, boolean front) {
        if(zone == 0) {
            return 0;
        }
        if(zone == 1) {
            return 1;
        }
        if(zone == 2) {
            return 0;
        }
        if(zone == 3) {
            return 1;
        }
        if(zone == 4) {
            return 0;
        }
        if(zone == 5) {
            return 0;
        }
        if(zone == 6) {
            return 1;
        }
        if(zone == 7) {
            return 1;
        }
        if(zone == 8) {
            return 0;
        }
        if(zone == 9) {
            return 0;
        }
        if(zone == 10) {
            return 0;
        }
        if(zone == 11) {
            return 1;
        }
        if(zone == 12) {
            return 1;
        }
        if(zone == 13) {
            return 1;
        }
        if(zone == 14) {
            return 0;
        }
        if(zone == 15 || zone == 16) {
            return 0;
        }
        if(zone == 17) {
            return 0;
        }
        if(zone == 18) {
            return 0;
        }
        if(zone == 21) {
            return 0;
        }
        return 1;
    }

    public static short getStartingX() {
        return getStartingX(Settings.getCurrentStartingLocation(), isFrontsideStart());
    }

    public static short getStartingX(int zone, boolean front) {
        if(zone == 0) {
            return 140;
        }
        if(zone == 1) {
            return 1120;
        }
        if(zone == 2) {
            return 160;
        }
        if(zone == 3) {
            return 560;
        }
        if(zone == 4) {
            return 120;
        }
        if(zone == 5) {
            return 300;
        }
        if(zone == 6) {
            return 40;
        }
        if(zone == 7) {
            return front ? (short)100 : 180;
        }
        if(zone == 8) {
            return 440;
        }
        if(zone == 9) {
            return 420;
        }
        if(zone == 10) {
            return 300;
        }
        if(zone == 11) {
            return 160;
        }
        if(zone == 12) {
            return 220;
        }
        if(zone == 13) {
            return 300;
        }
        if(zone == 14) {
            return 140;
        }
        if(zone == 15 || zone == 16) {
            return 340;
        }
        if(zone == 17) {
            return 120;
        }
        if(zone == 18) {
            return 420;
        }
        if(zone == 21) {
            return 400;
        }
        return 940;
    }

    public static short getStartingY() {
        return getStartingY(Settings.getCurrentStartingLocation(), isFrontsideStart());
    }

    public static short getStartingY(int zone, boolean front) {
        if(zone == 0) {
            return 392;
        }
        if(zone == 1) {
            return 72;
        }
        if(zone == 2) {
            return 312;
        }
        if(zone == 3) {
            return 152;
        }
        if(zone == 4) {
            return 152;
        }
        if(zone == 5) {
            return 72;
        }
        if(zone == 6) {
            return 152;
        }
        if(zone == 7) {
            return front ? (short)392 : 312;
        }
        if(zone == 8) {
            return 72;
        }
        if(zone == 9) {
            return 72;
        }
        if(zone == 10) {
            return 72;
        }
        if(zone == 11) {
            return 152;
        }
        if(zone == 12) {
            return 72;
        }
        if(zone == 13) {
            return 232;
        }
        if(zone == 14) {
            return 392;
        }
        if(zone == 15 || zone == 16) {
            return 312;
        }
        if(zone == 17) {
            return 72;
        }
        if(zone == 18) {
            return 72;
        }
        if(zone == 21) {
            return 160;
        }
        return 160;
    }

    public static short getGrailFlag(int zone, boolean front) {
        if(zone == 0) {
            return 0x064;
        }
        if(zone == 1) {
            return 0xad3;
        }
        if(zone == 2) {
            return 0x065;
        }
        if(zone == 3) {
            return 0x066;
        }
        if(zone == 4) {
            return 0x067;
        }
        if(zone == 5) {
            return 0x068;
        }
        if(zone == 6) {
            return 0x069;
        }
        if(zone == 7) {
            return front ? (short)0x06a : 0x073;
        }
        if(zone == 8) {
            return 0x06b;
        }
        if(zone == 9) {
            return 0x06c;
        }
        if(zone == 10) {
            return 0x06d;
        }
        if(zone == 11) {
            return 0x06e;
        }
        if(zone == 12) {
            return 0x06f;
        }
        if(zone == 13) {
            return 0x070;
        }
        if(zone == 14) {
            return 0x071;
        }
        if(zone == 15 || zone == 16) {
            return 0x072;
        }
        if(zone == 17) {
            return 0x074;
        }
        if(zone == 18) {
            return 0x075;
        }
//        if(zone == 19) {
//            return 0xacf; // Retro mausoleum
//        }
//        if(zone == 20) {
//            return 0xace; // Retro guidance
//        }
//        if(zone == 21) {
//            return 0xacd; // Retro surface
//        }
        return 0xacf;
    }

    public static boolean isSurfaceStart() {
        return Settings.getCurrentStartingLocation() == 1;
    }

    public static boolean isFrontsideStart() {
        int zone = Settings.getCurrentStartingLocation();
        if(zone >= 0 && zone <= 6) {
            return true;
        }
        if(zone == 8 || zone == 9) {
            return true;
        }
        if(zone == 21) {
            return true;
        }
        return false;
    }

    public static short getNextWarpZone(int currentWarpZone, boolean front) {
        if(currentWarpZone == 21) {
            return 1;
        }
        if(currentWarpZone == 1) {
            return 0;
        }
        if(currentWarpZone == 0) {
            return 2;
        }
        if(currentWarpZone == 2) {
            return 3;
        }
        if(currentWarpZone == 3) {
            return 4;
        }
        if(currentWarpZone == 4) {
            return 5;
        }
        if(currentWarpZone == 5) {
            return 6;
        }
        if(currentWarpZone == 6) {
            return 7;
        }
        if(currentWarpZone == 7) {
            return front ? (short)8 : 17;
        }
        if(currentWarpZone == 8) {
            return 9;
        }
        if(currentWarpZone == 9) {
            return 1;
        }
        if(currentWarpZone == 10) {
            return 11;
        }
        if(currentWarpZone == 11) {
            return 12;
        }
        if(currentWarpZone == 12) {
            return 13;
        }
        if(currentWarpZone == 13) {
            return 14;
        }
        if(currentWarpZone == 14) {
            return 16;
        }
        if(currentWarpZone == 15) {
            return 7;
        }
        if(currentWarpZone == 16) {
            return 7;
        }
        if(currentWarpZone == 17) {
            return 18;
        }
        if(currentWarpZone == 18) {
            return 10;
        }
        if(front) {
            // Default
            return 1;
        }
        else {
            // Default
            return 10;
        }
    }

    public static String getStartingScreenName() {
        int zone = Settings.getCurrentStartingLocation();
        if(zone == 1) {
            return "Village of Departure";
        }
        if(zone == 5) {
            return "Hall of Worship";
        }
        if(zone == 10) {
            return "Chapel";
        }
        if(zone == 21) {
            return "Gate of Time (Surface)";
        }
        return "Village of Departure";
    }

    public static String getStartingZoneName(int zone) {
        if(zone == 1) {
            return "Surface";
        }
        if(zone == 2) {
            return "Mausoleum of the Giants";
        }
        if(zone == 3) {
            return "Temple of the Sun";
        }
        if(zone == 4) {
            return "Spring in the Sky";
        }
        if(zone == 5) {
            return "Inferno Cavern";
        }
        if(zone == 6) {
            return "Chamber of Extinction";
        }
        if(zone == 7) {
            return "Twin Labyrinths (Front)";
        }
        if(zone == 8) {
            return "Endless Corridor";
        }
        if(zone == 9) {
            return "Shrine of the Mother";
        }
        if(zone == 10) {
            return "Gate of Illusion";
        }
        if(zone == 11) {
            return "Graveyard of the Giants";
        }
        if(zone == 12) {
            return "Temple of Moonlight";
        }
        if(zone == 13) {
            return "Tower of the Goddess";
        }
        if(zone == 14) {
            return "Tower of Ruin";
        }
        if(zone == 15 || zone == 16) {
            return "Chamber of Birth";
        }
        if(zone == 17) {
            return "Dimensional Corridor";
        }
        if(zone == 21) {
            return "Gate of Time (Surface)";
        }
        return "Unknown";
    }

    public static Integer getStartingZoneFromName(String name) {
        if("Surface".equals(name)) {
            return 1;
        }
        if("Inferno Cavern".equals(name) || "Inferno".equals(name)) {
            return 5;
        }
        if("Gate of Illusion".equals(name) || "Illusion".equals(name)) {
            return 10;
        }
        if("Gate of Time (Surface)".equals(name) || "Gate of Time Surface".equals(name) || "Surface of Time".equals(name) || "Retro Surface".equals(name) || "Retrosurface".equals(name)) {
            return 21;
        }
        return null;
    }

    public static short getImageIndex(int zone, boolean front) {
        if(zone == 0) {
            return 0;
        }
        if(zone == 2) {
            return 2;
        }
        if(zone == 3) {
            return 3;
        }
        if(zone == 4) {
            return 4;
        }
        if(zone == 5) {
            return 5;
        }
        if(zone == 6) {
            return 6;
        }
        if(zone == 7) {
            return front ? (short)7 : 16;
        }
        if(zone == 8) {
            return 8;
        }
        if(zone == 9) {
            return 9;
        }
        if(zone == 10) {
            return 10;
        }
        if(zone == 11) {
            return 11;
        }
        if(zone == 12) {
            return 12;
        }
        if(zone == 13) {
            return 13;
        }
        if(zone == 14) {
            return 14;
        }
        if(zone == 15 || zone == 16) {
            return 15;
        }
        if(zone == 17) {
            return 17;
        }
        if(zone == 18) {
            return 18;
        }
        if(zone == 19) {
            return 19;
        }
        if(zone == 20) {
            return 19;
        }
        if(zone == 21) {
            return 19;
        }
        return 1;
    }

    public static String getStartingLocation() {
        int zone = getStartingZone();
        if(zone == 1) {
            return "Location: Surface [Main]";
        }
        if(zone == 4) {
            return "Location: Spring in the Sky";
        }
        if(zone == 5) {
            return "Location: Inferno Cavern [Main]";
        }
        if(zone == 10) {
            return "Location: Gate of Illusion [Grail]";
        }
        if(zone == 21) {
            return "Location: Gate of Time [Surface]";
        }
        return "Location: Surface [Main]";
    }
}
