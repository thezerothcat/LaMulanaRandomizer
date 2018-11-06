package lmr.randomizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by thezerothcat on 10/4/2017.
 */
public final class Translations {
    private static Properties enTranslations;
    private static Properties jpTranslations;

    public static void initTranslations() throws IOException {
        enTranslations = new Properties();
        jpTranslations = null;

        loadTranslations(enTranslations, "en");
    }

    public static String getText(String key) {
        if("jp".equals(Settings.getLanguage())) {
            return getJapaneseTranslation(key);
        }
        return enTranslations.getProperty(key);
    }

    protected static String getJapaneseTranslation(String key) {
        if(jpTranslations == null) {
            jpTranslations = new Properties(enTranslations);
            try {
                loadTranslations(jpTranslations, "jp");
            }
            catch (Exception ex) {
                FileUtils.log("Unable to load translations");
                enTranslations.getProperty(key);
            }
        }
        return jpTranslations.getProperty(key);
    }

    protected static void loadTranslations(Properties toLoad, String langCode) throws IOException {
        try {
            toLoad.load(new InputStreamReader(new FileInputStream(String.format(
                    "src/main/resources/lmr/randomizer/lang/lang_%s.properties", langCode)), "UTF-8"));
        }
        catch (Exception ex) {
            toLoad.load(new InputStreamReader(FileUtils.class.getResourceAsStream(
                    String.format("lang/lang_%s.properties", langCode)), "UTF-8"));
        }
    }

    public static String getGlitchText(String glitchName) {
        return getText("glitches." + glitchName.replaceAll("[ ']", ""));
    }

    public static String getShopItemText(String shopName, int itemNumber) {
        return String.format(getText("shops.ItemFormat"),
                getText("shops." + shopName.replaceAll("[ )(]", "")),
                itemNumber);
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

    private static String getMapLocationText(String locationKey) {
        return getText("locations." + locationKey.replaceAll("[ ']", ""));
    }
}
