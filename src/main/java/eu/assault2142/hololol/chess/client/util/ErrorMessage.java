package eu.assault2142.hololol.chess.client.util;

import eu.assault2142.hololol.chess.client.menus.MainMenu;
import javax.swing.JOptionPane;

/**
 *
 * @author hololol2
 */
public class ErrorMessage {

    public static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(MainMenu.MAINMENU, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
