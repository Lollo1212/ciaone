package eu.assault2142.hololol.chess.client.game;

import com.alee.laf.WebLookAndFeel;
import eu.assault2142.hololol.chess.client.menus.MainMenu;
import eu.assault2142.hololol.chess.client.translator.Translator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.prefs.Preferences;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hololol2
 */
public class Main {

    public static void main(String[] Args) throws URISyntaxException, FileNotFoundException, IOException, ClassNotFoundException {
        Settings e;
        //Daten aus der Registry auslesen
        Preferences pref = Preferences.userRoot().node("jojoschach");
        int a = Integer.parseInt(pref.get("skin", "0"));
        int b = Integer.parseInt(pref.get("sprache", "0"));
        String c = pref.get("username", "");
        e = new Settings(b, c);
        //Öffnen des Hauptmenüs
        Translator.setLanguage(Locale.getDefault());
        WebLookAndFeel.install();
        new MainMenu().setVisible(true);
    }
}
