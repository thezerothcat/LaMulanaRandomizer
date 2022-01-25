package lmr.randomizer.util;

import lmr.randomizer.Settings;

public final class LocationCoordinateMapper {
    private LocationCoordinateMapper() {
    }

    public static byte getStartingZone() {
        int zone = Settings.getCurrentStartingLocation();
        if(zone >= 0 && zone <= 14) {
            return (byte)zone;
        }
        if(zone == -7) {
            return 7;
        }
        if(zone == 16) {
            return 16;
        }
        if(zone == 21) {
            return 21;
        }
        if(zone == 22) {
            return 22;
        }
        if(zone == 23) {
            return 23;
        }
        if(zone == 24) {
            return 24;
        }
        return 1;
    }

    public static byte getStartingRoom() {
        return getStartingRoom(LocationCoordinateMapper.getStartingZone(), isFrontsideStart());
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
            return front ? (byte)0 : (byte)15;
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
        if(zone == 22) {
            return getStartingRoom(1, front);
        }
        if(zone == 23) {
//            return 18;
//            return 22;
//            return 14;
            return 0; // Entrance door
        }
        if(zone == 24) {
            return 2;
//            return 1;
        }
        return 2;
    }

    public static byte getStartingScreen() {
        return getStartingScreen(LocationCoordinateMapper.getStartingZone());
    }

    public static byte getStartingScreen(int zone) {
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
        if(zone == 22) {
            return getStartingScreen(1);
        }
        if(zone == 23) {
            return 0;
        }
        if(zone == 24) {
            return 0;
        }
        return 1;
    }

    public static short getStartingX() {
        return getStartingX(LocationCoordinateMapper.getStartingZone(), isFrontsideStart());
    }

    public static short getStartingX(int zone, boolean front) {
        if(zone == 0) {
            return 140;
        }
        if(zone == 1) {
            if(Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart() || Settings.isRandomizeStartingLocation()) {
                return 1120;
            }
            return 940;
        }
        if(zone == 2) {
            return 160;
        }
        if(zone == 3) {
            return 1200;
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
            return front ? (short)740 : 820;
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
            return 800;
        }
        if(zone == 12) {
            return 220;
        }
        if(zone == 13) {
            return 940;
        }
        if(zone == 14) {
            return 140;
        }
        if(zone == 15 || zone == 16) {
            if(LocationCoordinateMapper.getStartingZone() == 16) {
                return 200;
            }
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
        if(zone == 22) {
            return 1120;
        }
        if(zone == 23) {
            return 300;
        }
        if(zone == 24) {
            return 300;
        }
        return 940;
    }

    public static short getStartingY() {
        return getStartingY(LocationCoordinateMapper.getStartingZone(), isFrontsideStart());
    }

    public static short getStartingY(int zone, boolean front) {
        if(zone == 0) {
            return 392;
        }
        if(zone == 1) {
            if(Settings.isAllowMainWeaponStart() || Settings.isAllowSubweaponStart() || Settings.isRandomizeStartingLocation()) {
                return 72;
            }
            return 160;
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
            return 632;
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
            return 552;
        }
        if(zone == 13) {
            return 232;
        }
        if(zone == 14) {
            return 392;
        }
        if(zone == 15 || zone == 16) {
            if(LocationCoordinateMapper.getStartingZone() == 16) {
                return 392;
            }
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
        if(zone == 22) {
            return 72;
        }
        if(zone == 23) {
            return 72;
        }
        if(zone == 24) {
            return 72;
        }
        return 160;
    }

    public static short getGrailFlag(int zone, boolean front) {
        if(Settings.isReducedBossCount()) {
            if(zone == 0) {
                return FlagConstants.TABLET_GLOW_GUIDANCE_BACKSIDE_DOOR_BROKEN;
            }
            if(zone == 1) {
                return FlagConstants.TABLET_GRAIL_SURFACE;
            }
            if(zone == 2) {
                return FlagConstants.TABLET_GLOW_GUIDANCE_SHOP_BROKEN;
            }
            if(zone == 3) {
                return FlagConstants.TABLET_GLOW_MAUSOLEUM_TOP_BROKEN;
            }
            if(zone == 4) {
                return FlagConstants.TABLET_GLOW_MAUSOLEUM_TRAPDOOR_BROKEN;
            }
            if(zone == 5) {
                return FlagConstants.TABLET_GLOW_MAUSOLEUM_ELEVATOR_SHAFT_BROKEN;
            }
            if(zone == 6) {
                return FlagConstants.TABLET_GLOW_SUN_ABOVE_MULBRUK_BROKEN;
            }
            if(zone == 7) {
                return front ? (short)FlagConstants.TABLET_GLOW_SUN_MULBRUK_SCREEN_BROKEN : FlagConstants.TABLET_GLOW_SPRING_SACRED_LAKE_BROKEN;
            }
            if(zone == 8) {
                return FlagConstants.TABLET_GLOW_SPRING_MAP_SCREEN_BROKEN;
            }
            if(zone == 9) {
                return FlagConstants.TABLET_GRAIL_SHRINE_FRONT;
            }
            if(zone == 10) {
                return FlagConstants.TABLET_GLOW_EXTINCTION_SMALL_MURAL_BROKEN;
            }
            if(zone == 11) {
                return FlagConstants.TABLET_GLOW_TWIN_UPPER_LEFT_BROKEN;
            }
            if(zone == 12) {
                return FlagConstants.TABLET_GLOW_TWIN_MAP_SCREEN_BROKEN;
            }
            if(zone == 13) {
                return FlagConstants.TABLET_GLOW_TWIN_YIEGAH_SCREEN_BROKEN;
            }
            if(zone == 14) {
                return FlagConstants.TABLET_GLOW_TWIN_BELOW_ZU_BROKEN;
            }
            if(zone == 15 || zone == 16) {
                return FlagConstants.TABLET_GLOW_TWIN_IDIGNA_BROKEN;
            }
            if(zone == 17) {
                return FlagConstants.TABLET_GLOW_TWIN_POISON_2_BROKEN;
            }
        }
        return getOriginalGrailFlag(zone, front);
    }

    public static short getGrailFlagByOriginalFlag(int flag) {
        if(flag == FlagConstants.TABLET_GRAIL_SURFACE) {
            return getGrailFlag(ZoneConstants.SURFACE, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_GUIDANCE) {
            return getGrailFlag(ZoneConstants.GUIDANCE, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_MAUSOLEUM) {
            return getGrailFlag(ZoneConstants.MAUSOLEUM, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_SUN) {
            return getGrailFlag(ZoneConstants.SUN, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_SPRING) {
            return getGrailFlag(ZoneConstants.SPRING, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_INFERNO) {
            return getGrailFlag(ZoneConstants.INFERNO, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_EXTINCTION) {
            return getGrailFlag(ZoneConstants.EXTINCTION, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_TWIN_FRONT) {
            return getGrailFlag(ZoneConstants.TWIN_FRONT, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_TWIN_BACK) {
            return getGrailFlag(ZoneConstants.TWIN_FRONT, false);
        }
        if(flag == FlagConstants.TABLET_GRAIL_ENDLESS) {
            return getGrailFlag(ZoneConstants.ENDLESS, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_SHRINE_FRONT) {
            return getGrailFlag(ZoneConstants.SHRINE_FRONT, LocationCoordinateMapper.isFrontsideStart());
        }
        if(flag == FlagConstants.TABLET_GRAIL_SHRINE_BACK) {
            return getGrailFlag(ZoneConstants.SHRINE_BACK, !LocationCoordinateMapper.isFrontsideStart());
        }
        if(flag == FlagConstants.TABLET_GRAIL_ILLUSION) {
            return getGrailFlag(ZoneConstants.ILLUSION, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_GRAVEYARD) {
            return getGrailFlag(ZoneConstants.GRAVEYARD, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_MOONLIGHT) {
            return getGrailFlag(ZoneConstants.MOONLIGHT, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_GODDESS) {
            return getGrailFlag(ZoneConstants.GODDESS, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_RUIN) {
            return getGrailFlag(ZoneConstants.RUIN, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_BIRTH) {
            return getGrailFlag(ZoneConstants.BIRTH_SKANDA, true);
        }
        if(flag == FlagConstants.TABLET_GRAIL_DIMENSIONAL) {
            return getGrailFlag(ZoneConstants.DIMENSIONAL, true);
        }
        return getGrailFlag(ZoneConstants.GUIDANCE, true); // Default
    }

    public static short getOriginalGrailFlag(int zone, boolean front) {
        if(zone == 0) {
            return FlagConstants.TABLET_GRAIL_GUIDANCE;
        }
        if(zone == 1) {
            return FlagConstants.TABLET_GRAIL_SURFACE; // Technically not used for the Surface grail, but it did apply for tablet glow.
        }
        if(zone == 2) {
            return FlagConstants.TABLET_GRAIL_MAUSOLEUM;
        }
        if(zone == 3) {
            return FlagConstants.TABLET_GRAIL_SUN;
        }
        if(zone == 4) {
            return FlagConstants.TABLET_GRAIL_SPRING;
        }
        if(zone == 5) {
            return FlagConstants.TABLET_GRAIL_INFERNO;
        }
        if(zone == 6) {
            return FlagConstants.TABLET_GRAIL_EXTINCTION;
        }
        if(zone == 7) {
            return front ? (short)FlagConstants.TABLET_GRAIL_TWIN_FRONT : FlagConstants.TABLET_GRAIL_TWIN_BACK;
        }
        if(zone == 8) {
            return FlagConstants.TABLET_GRAIL_ENDLESS;
        }
        if(zone == 9) {
            return FlagConstants.TABLET_GRAIL_SHRINE_FRONT;
        }
        if(zone == 10) {
            return FlagConstants.TABLET_GRAIL_ILLUSION;
        }
        if(zone == 11) {
            return FlagConstants.TABLET_GRAIL_GRAVEYARD;
        }
        if(zone == 12) {
            return FlagConstants.TABLET_GRAIL_MOONLIGHT;
        }
        if(zone == 13) {
            return FlagConstants.TABLET_GRAIL_GODDESS;
        }
        if(zone == 14) {
            return FlagConstants.TABLET_GRAIL_RUIN;
        }
        if(zone == 15 || zone == 16) {
            return FlagConstants.TABLET_GRAIL_BIRTH;
        }
        if(zone == 17) {
            return FlagConstants.TABLET_GRAIL_DIMENSIONAL;
        }
        if(zone == 18) {
            return FlagConstants.TABLET_GRAIL_SHRINE_BACK;
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
        return Settings.getCurrentStartingLocation() == 1 || Settings.getCurrentStartingLocation() == 22;
    }

    public static boolean isFrontsideStart() {
        int zone = Settings.getCurrentStartingLocation();
        if(zone >= 0 && zone <= 9) {
            return true;
        }
        if(zone == 21) {
            return true;
        }
        if(zone == 22) {
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

    public static String getStartingZoneName(int zone) {
        if(zone == 0) {
            return "Gate of Guidance";
        }
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
        if(zone == -7) {
            return "Twin Labyrinths (Back)";
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
        if(zone == 22) {
            return getStartingZoneName(1);
        }
        return "Unknown";
    }

    public static Integer getStartingZoneFromName(String name) {
        if("Gate of Guidance".equals(name) || "Guidance".equals(name)) {
            return 0;
        }
        if("Surface".equals(name)) {
            return 1;
        }
        if("Mausoleum of the Giants".equals(name) || "Mausoleum".equals(name)) {
            return 2;
        }
        if("Temple of the Sun".equals(name) || "Sun".equals(name)) {
            return 3;
        }
        if("Spring in the Sky".equals(name) || "Spring".equals(name)) {
            return 4;
        }
        if("Inferno Cavern".equals(name) || "Inferno".equals(name)) {
            return 5;
        }
        if("Chamber of Extinction".equals(name) || "Extinction".equals(name)) {
            return 6;
        }
        if(name.contains("Twin ")) {
            if(name.toLowerCase().contains("back")) {
                return -7;
            }
            return 7;
        }
        if("Twin Labyrinths (Front)".equals(name) || "Twin Labyrinths Front".equals(name) || "Twin Labs".equals(name)) {
            return 7;
        }
        if("Endless Corridor".equals(name) || "Endless".equals(name)) {
            return 8;
        }
        if("Shrine of the Mother".equals(name) || "Shrine".equals(name)) {
            return 9;
        }
        if("Gate of Illusion".equals(name) || "Illusion".equals(name)) {
            return 10;
        }
        if("Graveyard of the Giants".equals(name) || "Graveyard".equals(name)) {
            return 11;
        }
        if("Temple of Moonlight".equals(name) || "Moonlight".equals(name)) {
            return 12;
        }
        if("Tower of the Goddess".equals(name) || "Goddess".equals(name)) {
            return 13;
        }
        if("Tower of Ruin".equals(name) || "Ruin".equals(name)) {
            return 14;
        }
        if("Chamber of Birth".equals(name) || "Birth".equals(name)) {
            return 16;
        }
        if("Dimensional Corridor".equals(name) || "Dimensional".equals(name)) {
            return 17;
        }
        if("Gate of Time (Surface)".equals(name) || "Gate of Time Surface".equals(name) || "Surface of Time".equals(name) || "Retro Surface".equals(name) || "Retrosurface".equals(name)) {
            return 21;
        }
        if(name.contains("Night") && name.contains("Surface")) {
            return 22;
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
        if(zone == 22) {
            return getImageIndex(1, front);
        }
        return 1;
    }

    public static String getStartingLocation() {
        int zone = Settings.getCurrentStartingLocation();
        if(zone == 0) {
            return "Location: Gate of Guidance [Main]";
        }
        if(zone == 1) {
            return "Location: Surface [Main]";
        }
        if(zone == 2) {
            return "Location: Mausoleum of the Giants";
        }
        if(zone == 3) {
            return "Location: Temple of the Sun [Main]";
        }
        if(zone == 4) {
            return "Location: Spring in the Sky [Main]";
        }
        if(zone == 5) {
            return "Location: Inferno Cavern [Main]";
        }
        if(zone == 6) {
            return "Location: Chamber of Extinction [Main]";
        }
        if(zone == 7) {
            return "Location: Twin Labyrinths [Poison 1]";
        }
        if(zone == -7) {
            return "Location: Twin Labyrinths [Lower]";
        }
        if(zone == 8) {
            return "Location: Endless Corridor [1F]";
        }
        if(zone == 10) {
            return "Location: Gate of Illusion [Grail]";
        }
        if(zone == 11) {
            return "Location: Graveyard of the Giants [Grail]";
        }
        if(zone == 12) {
            return "Location: Temple of Moonlight [Grail]";
        }
        if(zone == 13) {
            return "Location: Tower of the Goddess [Grail]";
        }
        if(zone == 14) {
            return "Location: Tower of Ruin [Grail]";
        }
        if(zone == 16) {
            return "Location: Chamber of Birth [West Entrance]";
        }
        if(zone == 21) {
            return "Location: Gate of Time [Surface]";
        }
        if(zone == 22) {
            return "Location: Surface [Main]";
        }
        return "Location: Surface [Main]";
    }
}
