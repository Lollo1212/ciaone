/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.menu.swing;

import eu.assault2142.hololol.chess.client.menu.IMenu;
import eu.assault2142.hololol.chess.client.networking.ServerConnection;
import eu.assault2142.hololol.chess.client.util.Translator;
import eu.assault2142.hololol.chess.game.Settings;
import eu.assault2142.hololol.chess.networking.ServerMessages;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * A Swing-Implementation of IMenu
 *
 * @author hololol2
 */
public class SwingMenu implements IMenu {

    private ServerConnection connection;
    private final MainMenu menu;

    /**
     * Create the Menu
     */
    public SwingMenu() {
        menu = new MainMenu(this);
        menu.setVisible(true);
    }

    /**
     * Connect to the (Main)Server
     *
     * @param username the loginname
     * @param password the password
     * @param create whether to create a account or login to an existing one
     */
    protected void connectToServer(String username, String password, boolean create) {
        Settings.SETTINGS.username = username;
        if (username.length() < 4) {
            JOptionPane.showMessageDialog(menu, Translator.getString("NAMETOOSHORT_TEXT"), Translator.getString("NAMETOOSHORT_HEAD"), JOptionPane.WARNING_MESSAGE);
        } else if (password.length() < 4) {
            JOptionPane.showMessageDialog(menu, Translator.getString("PASSTOOSHORT_TEXT"), Translator.getString("PASSTOOSHORT_HEAD"), JOptionPane.WARNING_MESSAGE);
        } else {
            ServerConnection.connect(username, password, create);
        }
    }

    @Override
    public void challengeDeclined(String username) {
        JOptionPane.showMessageDialog(menu, username + " declined your Challenge!", "Challenge Declined", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void connectionError() {
        JOptionPane.showMessageDialog(menu, Translator.getString("COULDN'T CONNECT TO SERVER"), Translator.getString("CONNECTION ERROR"), JOptionPane.WARNING_MESSAGE);
        menu.enableLoginButton();
    }

    @Override
    public void enemyOffline() {
        JOptionPane.showMessageDialog(menu, Translator.getString("GAME_ENEMYOFF_TEXT"), Translator.getString("GAME_ENEMYOFF_HEAD"), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void friendRequest(String username) {
        int addfriend = JOptionPane.showConfirmDialog(menu, username + Translator.getString("FRIENDREQ_ADD_TEXT"), Translator.getString("FRIENDREQ_ADD_HEAD"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (addfriend == JOptionPane.NO_OPTION) {
            connection.write(ServerMessages.DeclineFriend, new Object[]{username});
        } else if (addfriend == JOptionPane.YES_OPTION) {
            connection.write(ServerMessages.AcceptFriend, new Object[]{username});
        }
    }

    @Override
    public void gameChallenge(String username) {
        int selected = JOptionPane.showConfirmDialog(menu, java.text.MessageFormat.format(Translator.getString("GAME_START?_TEXT"), new Object[]{username}), Translator.getString("GAME_START?_HEAD"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (selected == JOptionPane.YES_OPTION) {
            connection.write(ServerMessages.AcceptGame, new Object[]{username});
        } else {
            connection.write(ServerMessages.DeclineGame, new Object[]{username});
        }
    }

    @Override
    public void setPlayerName(String username) {
        menu.setPlayerName(username);
    }

    @Override
    public void setVisible(boolean visible) {
        menu.setVisible(visible);
    }

    @Override
    public void infoMessage(String text) {
        JOptionPane.showMessageDialog(menu, text, Translator.getString("DIALOG_INFO_HEAD"), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void loggedIn(ServerConnection connection) {
        this.connection = connection;
        menu.loggedIn(connection);
    }

    @Override
    public void loginError(LOGINERROR error) {
        switch (error) {
            case ACCOUNTEXISTS:
                JOptionPane.showMessageDialog(menu, Translator.getString("ACCOUNT EXISTIERT BEREITS"), Translator.getString("LOGIN ERROR"), JOptionPane.ERROR_MESSAGE);
                menu.enableLoginButton();
                break;
            case WRONGPASSWORD:
                JOptionPane.showMessageDialog(menu, Translator.getString("PASSWORT FALSCH"), Translator.getString("LOGIN ERROR"), JOptionPane.ERROR_MESSAGE);
                menu.enableLoginButton();
                break;
            case ACCOUNTNOTEXISTS:
                JOptionPane.showMessageDialog(menu, Translator.getString("ACCOUNT EXISTIERT NICHT"), Translator.getString("LOGIN ERROR"), JOptionPane.ERROR_MESSAGE);
                menu.enableLoginButton();
                break;
            case ALREADYLOGGEDIN:
                JOptionPane.showMessageDialog(menu, "Already Online", Translator.getString("LOGIN ERROR"), JOptionPane.ERROR_MESSAGE);
                menu.enableLoginButton();
                break;
            default:
                JOptionPane.showMessageDialog(menu, "Unknown Login Error", Translator.getString("LOGIN ERROR"), JOptionPane.ERROR_MESSAGE);
                menu.enableLoginButton();
        }
    }

    @Override
    public void newMessage(String from, String msg) {
        menu.newMessage(from, msg);
    }

    @Override
    public void passwordChanged() {
        JOptionPane.showMessageDialog(menu, Translator.getString("PASSCHANGED_TEXT"), Translator.getString("PASSCHANGED_HEAD"), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void unknownUsername() {
        JOptionPane.showMessageDialog(menu, "There is no User with this name!", "Unknown Username", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void updateFriends(String[] friends) {
        menu.updateFriends(friends);
    }

    @Override
    public void usernameChanged(String newname) {
        JOptionPane.showMessageDialog(menu, java.text.MessageFormat.format(Translator.getString("NAMECHANGED_TEXT"), new Object[]{newname}), Translator.getString("NAMECHANGED_HEAD"), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void usernameTaken() {
        JOptionPane.showMessageDialog(menu, Translator.getString("NAMECHANGE_TAKEN_TEXT"), Translator.getString("NAMECHANGE_TAKEN_HEAD"), JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void showErrorMessage(String message, boolean exit) {
        JOptionPane.showMessageDialog(menu, message, "Error", JOptionPane.ERROR_MESSAGE);
        if (exit) {
            System.exit(1);
        }
        menu.enableLoginButton();
    }

    /**
     * Send a Change-Password-Request to the SErver
     */
    protected void changePassword() {
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(
                menu,
                passwordField,
                Translator.getString("CHANGEPASS_HEAD"),
                JOptionPane.OK_CANCEL_OPTION);
        String input = new String(passwordField.getPassword());
        if (option == JOptionPane.OK_OPTION) {
            if (input.length() > 3) {
                connection.write(ServerMessages.ChangePassword, new Object[]{input});
            } else {
                JOptionPane.showMessageDialog(menu, Translator.getString("PASSTOOSHORT_TEXT"), Translator.getString("PASSTOOSHORT_HEAD"), JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
