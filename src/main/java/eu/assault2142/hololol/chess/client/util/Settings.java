/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.util;

/**
 * Stores Settings of the Application
 *
 * @author hololol2
 */
public class Settings {

    public static Settings SETTINGS;
    //the username used to play online
    public String username;
    //the current chessmen-folder
    public String chessmenFolder = "/chessmen_high";

    private Settings(String user) {
        username = user;
    }

    public static void init(String username) {
        SETTINGS = new Settings(username);
    }
}
