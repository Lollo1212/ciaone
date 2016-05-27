/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.menus;

/**
 *
 * @author hololol2
 */
public interface IMenu {

    public static IMenu MENU = null;

    public void challengeDeclined(String username);

    public void enemyOffline();

    public void friendRequest(String username);

    public void gameChallenge(String username);

    public void infoMessage(String text);

    public void newMessage(String from, String msg);

    public void passwordChanged();

    public void unknownUsername();

    public void usernameChanged(String newname);

    public void usernameTaken();
}
