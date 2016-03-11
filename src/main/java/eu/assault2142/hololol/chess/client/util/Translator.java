package eu.assault2142.hololol.chess.client.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author hololol2
 */
public class Translator {

    private static ResourceBundle bundle;

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static void setLanguage(Locale locale) {
        bundle = ResourceBundle.getBundle("translations/translations", locale);
    }
}
