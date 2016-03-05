package eu.assault2142.hololol.chess.client.translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Provides Translation features. Data for Translations is stored in Directory
 * "translations" in the appropriate files.
 *
 * @author hololol2
 *
 */
public class Translator {

    public static Translator TRANSLATOR;
    public static final String EN = "en_us";
    public static final String DE = "de_de";
    private final HashMap<String, String> translations = new HashMap();
    private String lang;

    /**
     * Initializes the Translator and loads the appropriate Translations.
     *
     * @param l The language needed. (for example Translator.EN)
     */
    public Translator(String l) {
        TRANSLATOR = this;
        lang = l;
        loadTranslations();
    }

    /**
     * Returns the translation for the given key.
     *
     * @param key The key to be translated
     * @return The associated translation or an empty String if not found.
     */
    public String getTranslation(String key) {
        String s = translations.getOrDefault(key, "");
        s = s.replace("&oe&", "\u00f6");
        s = s.replace("&ue&", "\u00fc");
        s = s.replace("&ae&", "\u00e4");
        s = s.replace("&ss&", "\u00df");
        return s;
    }

    /**
     * Returns the translation for the given key and the given params.
     *
     * @param key The key to be translated
     * @param params Replaces '&p&' in the translated string with the objects.
     * @return The translated, parameterized String
     */
    public String getTranslation(String key, Object[] params) {
        String s = getTranslation(key);
        for (Object object : params) {
            s = s.replaceFirst("&p&", object.toString());
        }
        return s;
    }

    /**
     * Returns the language of this Translator.
     *
     * @return The language this object translates to.
     */
    public String getLanguage() {
        return lang;
    }

    /**
     * Loads the Translations.
     */
    private void loadTranslations() {
        translations.clear();
        try {
            File keyfile = new File(this.getClass().getResource("/translations/key.lang").toURI());
            File langfile = new File(this.getClass().getResource("/translations/" + lang + ".lang").toURI());
            FileInputStream fis1 = new FileInputStream(keyfile);
            FileInputStream fis2 = new FileInputStream(langfile);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
            Stream<String> k = br1.lines();
            Stream<String> l = br2.lines();
            ArrayList<String> keystrings = new ArrayList();
            ArrayList<String> langstrings = new ArrayList();
            k.forEach(keystrings::add);
            l.forEach(langstrings::add);
            if (keystrings.size() != langstrings.size()) {
                System.err.println("Number of Translations not equal number of Keys! Aborting loading of Translations.");
            } else {
                for (int i = 0; i < keystrings.size(); i++) {
                    translations.put(keystrings.get(i), langstrings.get(i));
                }
            }
            br1.close();
            br2.close();
            fis1.close();
            fis2.close();
        } catch (URISyntaxException | IOException e) {
            System.err.println("Loading of Translations not successfull!");
        }
    }

    /**
     * Sets a new Language and updates the Texts of all registered elements
     *
     * @param lang The new language
     */
    public void setLanguage(String lang) {
        this.lang = lang;
        loadTranslations();
    }

}
