package lmr.randomizer.update;

import lmr.randomizer.FileUtils;
import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Settings;
import lmr.randomizer.util.*;

import java.util.Arrays;

public final class SaveFileUpdates {
    private SaveFileUpdates() { }

    public static byte[] getSaveFileBytes() {
        byte[] saveData = buildNewSave();
        setStartingWeapon(saveData);
        setFlagValue(saveData, FlagConstants.RANDOMIZER_SAVE_LOADED, 1);
        if(!LocationCoordinateMapper.isSurfaceStart()) {
            short grailFlag = LocationCoordinateMapper.getGrailFlag(LocationCoordinateMapper.getStartingZone(), LocationCoordinateMapper.isFrontsideStart());
            if(FlagConstants.TABLET_GRAIL_GUIDANCE <= grailFlag && FlagConstants.TABLET_GRAIL_SHRINE_BACK >= grailFlag) {
                setFlagValue(saveData, grailFlag, 1);// Set starting grail flag if this is a traditional starting location, so your grail will be empowered.
            }
            setInventoryCount(saveData, ItemConstants.WEIGHT, 10); // Start with 10 weights

            if(Settings.getCurrentStartingLocation() == ZoneConstants.EXTINCTION) {
                setFlagValue(saveData, FlagConstants.EXTINCTION_TEMP_LIGHT, 1); // A bit of light to start things off
            }
            if(Settings.getCurrentStartingLocation() == ZoneConstants.TWIN_FRONT) {
                setFlagValue(saveData, FlagConstants.TWINS_FRONT_GRAIL_ELEVATOR, 2); // Auto-solve elevator puzzle
            }
//            if(Settings.getCurrentStartingLocation() == ZoneConstants.GODDESS) {
//                setFlagValue(saveData, FlagConstants.GODDESS_LIGHTS_ON, 3);
//            }
        }
        if(Settings.isReducedBossCount()) {
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_GUIDANCE, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_MAUSOLEUM, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_SUN, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_SPRING, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_INFERNO, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_EXTINCTION, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_TWIN_FRONT, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_ENDLESS, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_ILLUSION, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_GRAVEYARD, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_MOONLIGHT, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_GODDESS, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_RUIN, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_BIRTH, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_TWIN_BACK, 1);
            setFlagValue(saveData, FlagConstants.TABLET_GRAIL_DIMENSIONAL, 1);
        }

        if(HolidaySettings.isHalloween2019Mode()) {
            // Unlock Mulbruk so you can get Halloween hints.
            setFlagValue(saveData, FlagConstants.MULBRUK_CONVERSATIONS_EARLY, 1);
            setFlagValue(saveData, FlagConstants.MULBRUK_DOOR_UNSEALED, 2);
            setFlagValue(saveData, FlagConstants.MULBRUK_CONVERSATION_AWAKE, 1);

//            saveData[0x11 + 0x70e] = (byte)1; // room 20 floor
//            saveData[0x11 + 0x7d1] = (byte)1; // room 2
//            saveData[0x11 + 0x7d4] = (byte)1; // room 5
//            saveData[0x11 + 0x7d5] = (byte)1; // room 6
//            saveData[0x11 + 0x7d6] = (byte)1; // room 7
//            saveData[0x11 + 0x7d7] = (byte)2; // room 8
//            saveData[0x11 + 0x7d8] = (byte)1; // room 9
//            saveData[0x11 + 0x7d9] = (byte)1; // room 10
//            saveData[0x11 + 0x7da] = (byte)1; // room 11
//            saveData[0x11 + 0x7db] = (byte)1; // room 12
//            saveData[0x11 + 0x7dc] = (byte)1; // room 13
//            saveData[0x11 + 0x7de] = (byte)1; // room 15
//            saveData[0x11 + 0x7e0] = (byte)1; // room 17
//            saveData[0x11 + 0x7e1] = (byte)1; // room 18
//            saveData[0x11 + FlagConstants.HT_SOLVED_ROOM19] = (byte)1; // room 19
//            saveData[0x11 + 0x7e5] = (byte)1; // room 22
//            saveData[0x11 + 0x7e6] = (byte)1; // room 20
//            saveData[0x11 + 0x7e7] = (byte)2; // room 24
//            saveData[0x11 + 0x7f7] = (byte)2; // Key fairy room solved
////            saveData[0x11 + 0x7e9] = (byte)1; // room 25
////            saveData[0x11 + 0x7ef] = (byte)1; // room 32
////            saveData[0x11 + 0x7f0] = (byte)1; // room 33
//            saveData[0x11 + 0x70c] = (byte)1; // room 34
        }
        if(HolidaySettings.isHalloween2021Mode()) {
            setInventoryCount(saveData, ItemConstants.MAP, 1);
        }
        if(Settings.isFeatherlessMode()) {
            // Ice block puzzle forced.
            saveData[0x11 + 0x243] = 3;
            saveData[0x11 + 0x244] = 3;
        }
        if(HolidaySettings.isFools2020Mode()) {
            // Unlock Mulbruk so you can have conversations about quitting the game
            setFlagValue(saveData, FlagConstants.MULBRUK_CONVERSATIONS_EARLY, 1);
            setFlagValue(saveData, FlagConstants.MULBRUK_DOOR_UNSEALED, 2);
            setFlagValue(saveData, FlagConstants.MULBRUK_CONVERSATION_AWAKE, 1);

            // Default Extinction lighting
            setFlagValue(saveData, FlagConstants.EXTINCTION_TEMP_LIGHT, 1);
        }
        if(HolidaySettings.isFools2021Mode()) {
            setFlagValue(saveData, FlagConstants.ELLMAC_ANKH_PUZZLE, 5); // Ellmac ankh puzzle solved
            setFlagValue(saveData, FlagConstants.EXTINCTION_TEMP_LIGHT, 1); // Default Extinction lighting
        }

        return saveData;
    }

    private static byte[] buildNewSave() {
        byte[] saveData = new byte[16384];
        Arrays.fill(saveData, (byte)0);
        saveData[0] = (byte)1;
        saveData[3] = (byte)0;
        saveData[4] = (byte)0;
        saveData[5] = LocationCoordinateMapper.getStartingZone();
        saveData[6] = LocationCoordinateMapper.getStartingRoom();
        saveData[7] = LocationCoordinateMapper.getStartingScreen();
        saveData[8] = (byte)(((LocationCoordinateMapper.getStartingX() % 640) >> 8) & 0xff);
        saveData[9] = (byte)((LocationCoordinateMapper.getStartingX() % 640) & 0xff);
        saveData[10] = (byte)(((LocationCoordinateMapper.getStartingY() % 480) >> 8) & 0xff);
        saveData[11] = (byte)((LocationCoordinateMapper.getStartingY() % 480) & 0xff);
        saveData[12] = (byte)1;
        saveData[13] = (byte)0;
        saveData[14] = (byte)32;
        saveData[841] = (byte)1;
        saveData[859] = (byte)1;
        saveData[4624] = (byte)-1;
        saveData[4625] = (byte)-1;
        saveData[4630] = (byte)46;

        int index = 4633;
        for(int i = 0; i < 46; i++) {
            for(int j = 0; j < 6; j++) {
                saveData[index++] = (byte) 0;
            }
            saveData[index++] = (byte) -1;
            saveData[index++] = (byte) -1;
        }
        index += 13;
        for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 13; j++) {
                saveData[index++] = (byte) 0;
            }
            saveData[index++] = (byte) -1;
        }
//        try {
//            saveData = FileUtils.getBytes(String.format("%s/lm_00.sav", Settings.getLaMulanaSaveDir()));
//        }
//        catch (Exception ex) {
//            return saveData;
//        }
        return saveData;
    }

    private static void setStartingWeapon(byte[] saveData) {
        String startingWeapon = Settings.getCurrentStartingWeapon();
        if(!"Whip".equals(startingWeapon)) {
            FileUtils.logFlush("Updating weapon data");
            saveData[4113] = (byte)-1; // word + 0x1011; remove whip
            saveData[4114] = (byte)-1; // word + 0x1011; remove whip

            if("Chain Whip".equals(startingWeapon)) {
                saveData[142] = (byte)2; // byte + 0x11; add chain whip flag
                saveData[4115] = (byte)1; // word + 0x1011; add chain whip
                saveData[4623] = (byte)1; // Held main weapon item number
                saveData[4626] = (byte)0; // Held main weapon slot number
            }
            else if("Flail Whip".equals(startingWeapon)) {
                saveData[143] = (byte)2; // byte + 0x11; add flail whip flag
                saveData[4117] = (byte)1; // word + 0x1011; add flail whip
                saveData[4623] = (byte)2; // Held main weapon item number
                saveData[4626] = (byte)0; // Held main weapon slot number
            }
            else if("Knife".equals(startingWeapon)) {
                saveData[144] = (byte)2; // byte + 0x11; add knife flag
                saveData[4119] = (byte)1; // word + 0x1011; add knife
                saveData[4623] = (byte)3; // Held main weapon item number
                saveData[4626] = (byte)1; // Held main weapon slot number
            }
            else if("Key Sword".equals(startingWeapon)) {
                saveData[145] = (byte)2; // byte + 0x11; add key sword flag
                saveData[4121] = (byte)1; // word + 0x1011; add key sword
                saveData[4623] = (byte)4; // Held main weapon item number
                saveData[4626] = (byte)2; // Held main weapon slot number

                if(Settings.isAutomaticMantras()) {
                    saveData[0x11 + FlagConstants.MANTRA_FINAL] = (byte)4;
                }
            }
            else if("Axe".equals(startingWeapon)) {
                saveData[146] = (byte)2; // byte + 0x11; add axe flag
                saveData[4123] = (byte)1; // word + 0x1011; add axe
                saveData[4623] = (byte)5; // Held main weapon item number
                saveData[4626] = (byte)3; // Held main weapon slot number
            }
            else if("Katana".equals(startingWeapon)) {
                saveData[147] = (byte)2; // byte + 0x11; add katana flag
                saveData[4125] = (byte)1; // word + 0x1011; add katana
                saveData[4623] = (byte)6; // Held main weapon item number
                saveData[4626] = (byte)4; // Held main weapon slot number
            }
            else {
                saveData[4623] = (byte)-1; // Held main weapon item number
                saveData[4626] = (byte)-1; // Held main weapon slot number

                if("Shuriken".equals(startingWeapon)) {
                    saveData[148] = (byte)2;
                    saveData[4129] = (byte)1;
                    saveData[4624] = (byte)8; // Held subweapon item number
                    saveData[4627] = (byte)0; // Held subweapon slot number
                    saveData[0x06b * 2 + 0x1011 + 1] = (byte)150; // Ammo
                }
                else if("Rolling Shuriken".equals(startingWeapon)) {
                    saveData[149] = (byte)2;
                    saveData[4131] = (byte)1;
                    saveData[4624] = (byte)9; // Held subweapon item number
                    saveData[4627] = (byte)1; // Held subweapon slot number
                    saveData[0x06c * 2 + 0x1011 + 1] = (byte)100; // Ammo
                }
                else if("Earth Spear".equals(startingWeapon)) {
                    saveData[150] = (byte)2;
                    saveData[4133] = (byte)1;
                    saveData[4624] = (byte)10; // Held subweapon item number
                    saveData[4627] = (byte)2; // Held subweapon slot number
                    saveData[0x06d * 2 + 0x1011 + 1] = (byte)80; // Ammo
                }
                else if("Flare Gun".equals(startingWeapon)) {
                    saveData[151] = (byte)2;
                    saveData[4135] = (byte)1;
                    saveData[4624] = (byte)11; // Held subweapon item number
                    saveData[4627] = (byte)3; // Held subweapon slot number
                    saveData[0x06e * 2 + 0x1011 + 1] = (byte)80; // Ammo
                }
                else if("Bomb".equals(startingWeapon)) {
                    saveData[152] = (byte)2;
                    saveData[4137] = (byte)1;
                    saveData[4624] = (byte)12; // Held subweapon item number
                    saveData[4627] = (byte)4; // Held subweapon slot number
                    saveData[0x06f * 2 + 0x1011 + 1] = (byte)30; // Ammo
                }
                else if("Chakram".equals(startingWeapon)) {
                    saveData[153] = (byte)2;
                    saveData[4139] = (byte)1;
                    saveData[4624] = (byte)13; // Held subweapon item number
                    saveData[4627] = (byte)5; // Held subweapon slot number
                    saveData[0x070 * 2 + 0x1011 + 1] = (byte)10; // Ammo
                }
                else if("Caltrops".equals(startingWeapon)) {
                    saveData[154] = (byte)2;
                    saveData[4141] = (byte)1;
                    saveData[4624] = (byte)14; // Held subweapon item number
                    saveData[4627] = (byte)6; // Held subweapon slot number
                    saveData[0x071 * 2 + 0x1011 + 1] = (byte)80; // Ammo
                }
                else if("Pistol".equals(startingWeapon)) {
                    saveData[155] = (byte)2;
                    saveData[4143] = (byte)1;
                    saveData[4624] = (byte)15; // Held subweapon item number
                    saveData[4627] = (byte)7; // Held subweapon slot number
                    saveData[0x072 * 2 + 0x1011 + 1] = (byte)3; // Ammo
                    saveData[0x074 * 2 + 0x1011 + 1] = (byte)6; // Ammo
                }
                else {
                    saveData[4129] = (byte)1;
                    saveData[4131] = (byte)1;
                    saveData[4133] = (byte)1;
                    saveData[4135] = (byte)1;
                    saveData[4137] = (byte)1;
                    saveData[4139] = (byte)1;
                    saveData[4141] = (byte)1;
                    saveData[4143] = (byte)1;

                    saveData[4624] = (byte)19; // Held subweapon item number
                    saveData[4627] = (byte)9; // Held subweapon slot number
                    saveData[ItemConstants.ANKH_JEWEL * 2 + ValueConstants.SAVE_FILE_ITEM_COUNT_OFFSET + 1] = (byte)1; // Ammo
                }
            }
        }
    }

    private static void setFlagValue(byte[] saveData, int flagIndex, int flagValue) {
        saveData[flagIndex + ValueConstants.SAVE_FILE_FLAG_VALUE_OFFSET] = (byte)flagValue;
    }

    private static void setInventoryCount(byte[] saveData, int itemIndex, int quantity) {
        saveData[itemIndex * 2 + ValueConstants.SAVE_FILE_ITEM_COUNT_OFFSET + 1] = (byte)quantity;
    }
}
