package lmr.randomizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by thezerothcat on 10/4/2017.
 */
public final class Translations {
    private static Properties baseTranslations;
    private static Properties allTranslations;

    public static void initTranslations() throws IOException {
        baseTranslations = new Properties();
        loadInternalTranslations(baseTranslations, "lang", "en");
        if("jp".equals(Settings.getLanguage())) {
            Properties jpTranslations = new Properties(baseTranslations);
            loadInternalTranslations(jpTranslations, "lang", "jp");
            loadCustomTranslations(jpTranslations);
        }
        else {
            loadCustomTranslations(baseTranslations);
        }
    }

    public static void reloadTranslations() {
        if("jp".equals(Settings.getLanguage())) {
            Properties jpTranslations = new Properties(baseTranslations);
            try {
                loadInternalTranslations(jpTranslations, "lang", "jp");
                loadCustomTranslations(jpTranslations);
            }
            catch(IOException ex) {
                // Ignored
            }
        }
        else {
            try {
                loadCustomTranslations(baseTranslations);
            }
            catch(IOException ex) {
                // Ignored
            }
        }
    }

    public static String getText(String key) {
        return allTranslations.getProperty(key);
    }

    public static boolean hasKey(String key) {
        return allTranslations.stringPropertyNames().contains(key);
    }

    public static String getLocationAndNpc(String npc) {
        return getLocationAndNpc(npc, npc);
    }

    public static String getLocationAndNpc(String locationKey, String npcKey) {
        if(npcKey == null && locationKey == null) {
            return getText("shops.Shop0Default");
        }
        npcKey = npcKey.replaceAll("[ )('-.]", "");
        locationKey = locationKey.replaceAll("[ )('-.]", "");
        return baseTranslations.getProperty("npcl." + locationKey) + "" + baseTranslations.getProperty("npc." + npcKey);
    }

    protected static void loadInternalTranslations(Properties toLoad, String baseFilename, String langCode) throws IOException {
        try {
            toLoad.load(new InputStreamReader(new FileInputStream(String.format(
                    "src/main/resources/lmr/randomizer/lang/%s_%s.properties", baseFilename, langCode)), "UTF-8"));
        }
        catch (Exception ex) {
            toLoad.load(new InputStreamReader(FileUtils.class.getResourceAsStream(
                    String.format("lang/%s_%s.properties", baseFilename, langCode)), "UTF-8"));
        }
    }

    protected static void loadCustomTranslations(Properties defaultProperties) throws IOException {
        try {
            allTranslations = new Properties(defaultProperties);
            if(HolidaySettings.isFools2022Mode()) {
                loadInternalTranslations(allTranslations, "fools2022", "en");
            }
            else if ((new File("custom-text.properties").exists())) {
                allTranslations.load(new InputStreamReader(new FileInputStream("custom-text.properties"), "UTF-8"));
            }
        }
        catch (Exception ex) {
        }
    }

    public static String getGlitchText(String glitchName) {
        return getText("glitches." + glitchName.replaceAll("[ ']", ""));
    }

    public static String getShopLabel(String npcName, String npcLocation, String npcZone) {
        if(Settings.isRandomizeNpcs()) {
            return String.format(getText("shops.randomNpcLocationFormat"),
                    getText("npc." + npcName.replaceAll("[ )('-.]", "")),
                    getText("npc." + npcLocation.replaceAll("[ )('-.]", "")),
                    getMapLocationText(npcZone));
        }
        return String.format(getText("shops.npcLocationFormat"),
                getText("npc." + npcName.replaceAll("[ )('-.]", "")),
                getMapLocationText(npcZone));
    }

    public static String getItemText(String itemName, boolean removedItem) {
        String translation;
        if(itemName.startsWith("Coin:")) {
            translation = String.format(getText("items.CoinChestFormat"),
                    DataFromFile.getMapOfItemToUsefulIdentifyingRcdData().get(itemName).getInventoryArg());
        }
        else if(itemName.equals("Trap: Exploding")) {
            translation = getText("items.ExplodingChest");
        }
        else if(itemName.startsWith("Trap:")) {
            translation = getText("items.Trap");
        }
        else if(itemName.startsWith("Ankh Jewel (")) {
            translation = String.format(getText("items.AnkhJewelFormat"),
                    getMapLocationText(itemName.substring(itemName.indexOf('(') + 1, itemName.indexOf(')'))));
        }
        else if(itemName.startsWith("Sacred Orb (")) {
            translation = String.format(getText("items.SacredOrbFormat"),
                    getMapLocationText(itemName.substring(itemName.indexOf('(') + 1, itemName.indexOf(')'))));
        }
        else if(itemName.startsWith("Map (")) {
            translation = String.format(getText("items.MapFormat"),
                    getMapLocationText(itemName.substring(itemName.indexOf('(') + 1, itemName.indexOf(')'))));
        }
        else {
            translation = getText("items." + itemName.replaceAll("[ ']", ""));
        }

        if(translation == null) {
            translation = itemName;
        }

        if(removedItem) {
            translation = String.format(getText("items.RemovedFormat"), translation);
        }

        return translation;
    }

    public static String getLocationText(String itemName, boolean cursedLocation) {
        String translation;
        if(itemName.startsWith("Coin:")) {
            translation = String.format(getText("locations.CoinChestFormat"),
                    getText("locations." + itemName.replaceAll("[ :()]", "")));
        }
        else if(itemName.startsWith("Trap:")) {
            translation = String.format(getText("locations.TrapFormat"),
                    getText("locations." + itemName.replaceAll("[ :]", "")));
        }
        else if(itemName.startsWith("Ankh Jewel (")) {
            translation = String.format(getText("items.AnkhJewelFormat"),
                    getMapLocationText(itemName.substring(itemName.indexOf('(') + 1, itemName.indexOf(')'))));
        }
        else if(itemName.startsWith("Sacred Orb (")) {
            translation = String.format(getText("items.SacredOrbFormat"),
                    getMapLocationText(itemName.substring(itemName.indexOf('(') + 1, itemName.indexOf(')'))));
        }
        else if(itemName.startsWith("Map (")) {
            translation = String.format(getText("items.MapFormat"),
                    getMapLocationText(itemName.substring(itemName.indexOf('(') + 1, itemName.indexOf(')'))));
        }
        else {
            translation = getText("items." + itemName.replaceAll("[ ']", ""));
        }

        if(translation == null) {
            translation = itemName;
        }

        return String.format(
                getText(cursedLocation ? "locations.LocationCursedFormat" : "locations.LocationFormat"),
                translation);
    }

    public static String getDoorLocation(String doorName) {
        return getText("doors." + doorName.replace("Location: ", "").replaceAll("[ \\[\\]]", ""));
    }

    public static String getTransitionText(String transitionName) {
        return getText("transitions." + transitionName.replace("Transition: ", "").replaceAll("[: \\[\\]]", ""));
    }

    private static String getMapLocationText(String locationKey) {
        return getText("locations." + locationKey.replaceAll("[ ']", ""));
    }
}
