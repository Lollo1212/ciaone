package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ui.GameFrame;
import eu.assault2142.hololol.chess.client.networking.ServerConnection;
import eu.assault2142.hololol.chess.client.translator.Translator;
import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
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
        Chessman man;
        String promotion = (String) JOptionPane.showInputDialog(gameframe, Translator.getBundle().getString("PROMOTION_HEAD"), Translator.getBundle().getString("PROMOTION_TEXT"), JOptionPane.QUESTION_MESSAGE, null, new String[]{Translator.getBundle().getString("CHESSMAN_QUEEN"), Translator.getBundle().getString("CHESSMAN_ROOK"), Translator.getBundle().getString("CHESSMAN_KNIGHT"), Translator.getBundle().getString("CHESSMAN_BISHOP")}, Translator.getBundle().getString("CHESSMAN_QUEEN"));
        switch (promotion) {
            //bei lokalem Spiel wird der Bauer direkt gesetzt,
            //bei Serverbasiertem senden der Daten an den Server
            case "ROOK":
                man = Rook.promotion(pawn, this);
                break;
            case "KNIGHT":
                man = Knight.promotion(pawn, this);
                break;
            case "BISHOP":
                man = Bishop.promotion(pawn, this);
                break;
            default:
                man = Queen.promotion(pawn, this);
                break;
        }
        getFiguren(pawn.isBlack())[pawn.getPositionInArray()] = man;
        getSquare(man.getX(), man.getY()).occupier = man;

        new ClientMovementUpdater(this).start();
    }

    @Override
    public void finishedCalcs() {
        gameframe.getGameBoard().movementsupdating = false;
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
