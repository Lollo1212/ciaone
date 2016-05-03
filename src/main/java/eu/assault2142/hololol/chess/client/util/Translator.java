package eu.assault2142.hololol.chess.client.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author hololol2
 */
public class Translator {

    private static ResourceBundle bundle;
    private static String langString;

    public static String getLanguageString() {
        return langString;
    }

    public static String getString(String key) {
        return bundle.getString(key);
    }

    public static void setLanguage(Locale locale) {
        bundle = ResourceBundle.getBundle("translations/translations", locale);
        langString = locale.getLanguage();
    }
}
