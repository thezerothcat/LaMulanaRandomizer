package lmr.randomizer;

import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;

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

        if(removedItem) {
            translation = String.format(getText("items.RemovedFormat"), translation);
        }
//        return translation == null ? itemName : translation;
        return translation;
    }

    public static String getLocationText(String itemName, boolean cursedLocation) {
        String translation;
        if(itemName.startsWith("Coin:")) {
            translation = String.format(getText("locations.CoinChestFormat"), getCoinChestLocation(itemName));
        }
        else if(itemName.equals("Trap: Exploding")) {
            translation = getText("items.ExplodingChest");
        }
        else if(itemName.equals("Trap: Graveyard")) {
            translation = String.format(getText("locations.TrapFormat"),
                    getMapLocationText("Graveyard"));
        }
        else if(itemName.equals("Trap: Inferno Orb")) {
            translation = String.format(getText("locations.TrapFormat"),
                    getMapLocationText("Inferno"));
        }
        else if(itemName.equals("Trap: Twin Ankh")) {
            translation = String.format(getText("locations.TrapFormat"),
                    getMapLocationText("Twin"));
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

//        return translation == null ? itemName : translation;

        translation = String.format(
                getText(cursedLocation ? "locations.LocationCursedFormat" : "locations.LocationFormat"),
                translation);
        return translation;
    }

    private static String getMapLocationText(String locationKey) {
        return getText("locations." + locationKey.replaceAll("[ ']", ""));
    }

    private static String getCoinChestLocation(String itemName) {
        String locationKey = itemName.contains("(")
                ? itemName.substring(0, itemName.indexOf('('))
                : itemName;
        locationKey = locationKey.replace("Coin: ", "");
        String locationName = getMapLocationText(locationKey);
        if(itemName.contains("(")) {
            locationName += " " + itemName.substring(itemName.indexOf('('), itemName.indexOf(')') + 1);
        }
        return locationName;
    }
}
