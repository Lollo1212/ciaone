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
    
    //the number of the skin used
    public int skin;
    
    @Deprecated
    public int sprache;
    
    //the username used to play online
    public String username;
    
    
    //the current chessmen-folder
    public String chessmenFolder="/schach_figuren_medium";
    //possible chessmen-folders
    public final String[] chessmenFolders={"/schach_figuren_high","/schach_figuren_medium","/schach_figuren_low","/figuren_starcraft","/figuren_stronghold"};
    
    
    //Color of dark squares
    public Color dark=new Color(209,139,71);
    //Color of light squares
    public Color light=new Color(255,206,158);
    //possible light colors
    public final Color[] lightColors = {new Color(255,206,158),new Color(255,206,158),new Color(255,206,158),Color.white,new Color(6,124,0)};
    //possible dark colors
    public final Color[] darkColors = {new Color(209,139,71),new Color(209,139,71),new Color(209,139,71),Color.black,new Color(3,63,0)};
    public Settings(int sk,int sp,String user){
        SETTINGS = this;
        skin=sk;
        sprache=sp;
        username=user;
    }
    
    public void updateSkinSettings(int skin){
        dark=darkColors[skin];
        light=lightColors[skin];
        chessmenFolder=chessmenFolders[skin];
        this.skin=skin;
    }
}
