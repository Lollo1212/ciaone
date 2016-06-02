package eu.assault2142.hololol.chess.client.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provides Translation support
 *
 * @author hololol2
 */
public class Translator {

    private static ResourceBundle bundle;
    private static String languageCode;

    /**
     * Get the current Language
     *
     * @return get the code of the current language
     */
    public static String getLanguageCode() {
        return languageCode;
    }

    /**
     * Get a string in the current language
     *
     * @param key the key of the string
     * @return the corresponding string
     */
    public static String getString(String key) {
        return bundle.getString(key);
    }

    /**
     * Change the language
     *
     * @param locale the new language
     */
    public static void setLanguage(Locale locale) {
        bundle = ResourceBundle.getBundle("translations/translations", locale);
        languageCode = locale.getLanguage();
    }
}
