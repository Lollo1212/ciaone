/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.menus;

import eu.assault2142.hololol.chess.client.networking.ServerConnection;

/**
 *
 * @author hololol2
 */
public interface IMenu {

    public void connectionError();

    public enum LOGINERROR {
        ACCOUNTEXISTS, ACCOUNTNOTEXISTS, WRONGPASSWORD, ALREADYLOGGEDIN, UNKNONWNERROR
    };

    public void challengeDeclined(String username);

    public void enemyOffline();

    public void friendRequest(String username);

    public void gameChallenge(String username);

    public void setPlayerName(String part);

    public void setVisible(boolean b);

    public void infoMessage(String text);

    public void loggedIn(ServerConnection c);

    public void loginError(LOGINERROR error);

    public void newMessage(String from, String msg);

    public void passwordChanged();

    public void unknownUsername();

    public void updateFriends(String[] str);

    public void usernameChanged(String newname);

    public void usernameTaken();

    public void showErrorMessage(String message, boolean exit);
}
