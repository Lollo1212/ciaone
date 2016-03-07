package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ui.GameFrame;
import eu.assault2142.hololol.chess.client.networking.ServerConnection;
import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * A client-game for a game played on a server
 *
 * @author hololol2
 */
public class ClientGame extends Game {

    private GameFrame gameframe;
    private ServerConnection client = null;

    /**
     * Create a new ClientGame
     *
     * @param c the connection to the server
     * @param color the color you play (true -> black)
     */
    public ClientGame(ServerConnection c, boolean color) {//Spiel eines Clienten
        super(TYPE.CLIENT);
        client = c;
        final ClientGame g = this;
        EventQueue.invokeLater(() -> {
            gameframe = new GameFrame(g);
            JOptionPane.showConfirmDialog(gameframe, "Your color is ", "Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * Returns the connection to the server
     *
     * @return the connection to the server
     */
    public ServerConnection getConnection() {
        return client;
    }

    /**
     * Return the frame of the game
     *
     * @return the frame the game is played in
     */
    public GameFrame getGameFrame() {
        return gameframe;
    }

    @Override
    public ImageIcon getImage(Chessman.NAMES name, boolean black) {
        String color = "white";
        if (black) {
            color = "black";
        }
        return new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/" + name + "_" + color + ".gif"));
    }

    @Override
    public void updateMovements() {
    }

    @Override
    public void promotion(Pawn pawn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void finishedCalcs() {
        gameframe.getGameField().movementsupdating = false;
    }

    @Override
    public void endGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clickAt(int feldx, int feldy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
