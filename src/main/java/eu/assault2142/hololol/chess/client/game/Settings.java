/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game;

import java.awt.Color;

/**
 * Stores Settings of the Application
 * 
 * @author hololol2
 */
public class Settings{
    public static Settings SETTINGS;
    //the username used to play online
    public String username;
    //the current chessmen-folder
    public String chessmenFolder="/schach_figuren_high";
    //Color of dark squares
    public Color dark=new Color(209,139,71);
    //Color of light squares
    public Color light=new Color(255,206,158);
    public Settings(int sp,String user){
        SETTINGS = this;
        username=user;
    }
}
