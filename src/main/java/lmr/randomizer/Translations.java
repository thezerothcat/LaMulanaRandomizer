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

    public static String getItemText(String itemName) {
        return getText("items." + itemName.replaceAll("[ ']", ""));
    }

    public static String getGlitchText(String glitchName) {
        return getText("glitches." + glitchName.replaceAll("[ ']", ""));
    }
}
