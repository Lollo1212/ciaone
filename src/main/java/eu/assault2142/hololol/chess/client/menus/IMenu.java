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

    /**
     * Show a Connection-Error-Dialog
     */
    public void connectionError();

    public enum LOGINERROR {
        ACCOUNTEXISTS, ACCOUNTNOTEXISTS, WRONGPASSWORD, ALREADYLOGGEDIN, UNKNONWNERROR
    };

    /**
     * Show a Challenge-Declined-Dialog
     *
     * @param username the user which declined the challenge
     */
    public void challengeDeclined(String username);

    /**
     * Show a Enemy-Offline-Dialog
     */
    public void enemyOffline();

    /**
     * Show a Friend-Request-Dialog
     *
     * @param username the user which sent the request
     */
    public void friendRequest(String username);

    /**
     * Show a Game-Challenge-Dialog
     *
     * @param username the user which sent the challenge
     */
    public void gameChallenge(String username);

    /**
     * Update the local Username-Info
     *
     * @param username the (new) username
     */
    public void setPlayerName(String username);

    /**
     * Set the Menu visible
     *
     * @param visible whether it should be visible or not
     */
    public void setVisible(boolean visible);

    /**
     * Show a Info-Message-Dialog
     *
     * @param text the info-message
     */
    public void infoMessage(String text);

    /**
     * Update UI after successfull login
     *
     * @param connection the connection to the server
     */
    public void loggedIn(ServerConnection connection);

    /**
     * Show a Login-Error-Dialog
     *
     * @param error the type of the error
     */
    public void loginError(LOGINERROR error);

    /**
     * Show a new Message from another user
     *
     * @param from the name of the sender
     * @param msg the message
     */
    public void newMessage(String from, String msg);

    /**
     * Show a Password-Changed-Dialog
     */
    public void passwordChanged();

    /**
     * Show a Unknown-Username-Dialog
     */
    public void unknownUsername();

    /**
     * Update the Friends-List
     *
     * @param friends the usernames of the friends
     */
    public void updateFriends(String[] friends);

    /**
     * Show a Username-Changed-Dialog
     *
     * @param newname the new username
     */
    public void usernameChanged(String newname);

    /**
     * Show a Username-Taken-Dialog
     */
    public void usernameTaken();

    /**
     * Show a Error-Dialog
     *
     * @param message the error-message
     * @param exit whether the application should exit after the message is
     * closed
     */
    public void showErrorMessage(String message, boolean exit);
}
