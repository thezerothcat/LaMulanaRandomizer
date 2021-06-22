package lmr.randomizer;

import java.io.IOException;

public final class HolidaySettings {
    private static HolidaySettings singleton = new HolidaySettings();

    private boolean changed = false;

    private boolean updatedVersion;
    private boolean includeHellTempleNPCs;

    private HolidaySettings() {
        updatedVersion = true;
        includeHellTempleNPCs = true;
    }

    public static boolean isHalloweenMode() {
        return false;
    }

    public static boolean isFools2019Mode() {
        return false;
    }

    public static boolean isFools2020Mode() {
        return false;
    }

    public static boolean isFools2021Mode() {
        return false;
    }

    public static boolean isFools2022Mode() {
        return false;
    }

    public static boolean isHolidayMode() {
        return isFools2019Mode() || isFools2020Mode() || isFools2021Mode() || isFools2022Mode()
                || isHalloweenMode();
    }

    public static boolean isUpdatedVersion() {
        return singleton.updatedVersion;
    }

    public static void setUpdatedVersion(boolean updatedVersion, boolean update) {
        if(update && updatedVersion != singleton.updatedVersion) {
            singleton.changed = true;
        }
        singleton.updatedVersion = updatedVersion;
    }

    public static boolean isIncludeHellTempleNPCs() {
        return singleton.includeHellTempleNPCs;
    }

    public static void setIncludeHellTempleNPCs(boolean includeHellTempleNPCs, boolean update) {
        if(update && includeHellTempleNPCs != singleton.includeHellTempleNPCs) {
            singleton.changed = true;
        }
        singleton.includeHellTempleNPCs = includeHellTempleNPCs;
    }

    public static void saveSettings() {
        if(singleton.changed) {
            try {
                FileUtils.saveHolidaySettings();
            } catch (IOException ex) {
                FileUtils.log("Unable to save settings: " + ex.getMessage());
            }
        }
    }
}
