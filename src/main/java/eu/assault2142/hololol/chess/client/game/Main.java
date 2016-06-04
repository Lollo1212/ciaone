package eu.assault2142.hololol.chess.client.game;

import com.alee.laf.WebLookAndFeel;
import eu.assault2142.hololol.chess.client.menu.IMenu;
import eu.assault2142.hololol.chess.client.menu.swing.SwingMenu;
import eu.assault2142.hololol.chess.client.util.Settings;
import eu.assault2142.hololol.chess.client.util.Translator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 *
 * @author hololol2
 */
public class Main {

    public static IMenu MENU;

    public static void main(String[] Args) throws URISyntaxException, FileNotFoundException, IOException, ClassNotFoundException {
        Settings.init("");
        Locale locale = Locale.ENGLISH;
        Locale.setDefault(locale);
        Translator.setLanguage(locale);
        JOptionPane.setDefaultLocale(locale);
        WebLookAndFeel.install(true);

        MENU = new SwingMenu();
    }
}
