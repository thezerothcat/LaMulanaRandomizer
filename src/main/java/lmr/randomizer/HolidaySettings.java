package lmr.randomizer;

public final class HolidaySettings {
    private static HolidaySettings singleton = new HolidaySettings();

    private boolean changed = false;

    private boolean updatedVersion;
    private boolean includeHellTempleNPCs;
    private boolean includeOptionalContent;

    private HolidaySettings() {
        updatedVersion = true;
        includeHellTempleNPCs = true;
        includeOptionalContent = true;
    }

    public static boolean isChanged() {
        return singleton.changed;
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

    public static boolean isHalloween2019Mode() {
        return false;
    }

    public static boolean isHalloween2021Mode() {
        return false;
    }

    public static boolean isHolidayMode() {
        return isFools2019Mode() || isFools2020Mode() || isFools2021Mode() || isFools2022Mode()
                || isHalloween2019Mode() || isHalloween2021Mode();
    }

    public static boolean isHalloweenMode() {
        return isHalloween2019Mode() || isHalloween2021Mode();
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

    public static boolean isIncludeOptionalContent() {
        return singleton.includeOptionalContent;
    }

    public static void setIncludeOptionalContent(boolean includeOptionalContent, boolean update) {
        if(update && includeOptionalContent != singleton.includeOptionalContent) {
            singleton.changed = true;
        }
        singleton.includeOptionalContent = includeOptionalContent;
    }

    public static boolean isSaveFileNeeded() {
        return isHalloweenMode() || isFools2020Mode() ||isFools2021Mode() || isFools2022Mode();
    }
}
