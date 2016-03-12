package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.server.networking.ClientConnection;
import javax.swing.ImageIcon;

/**
 *
 * @author hololol2
 */
public class ServerGame extends Game {

    public ClientConnection client1;
    public ClientConnection client2;

    public ServerGame(ClientConnection a, ClientConnection b) {
        super(TYPE.SERVER);
        client1 = a;
        client2 = b;
    }

    @Override
    public void clickAt(int feldx, int feldy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void endGame() {
        //send end game
    }

    @Override
    public void finishedCalcs() {
        //send moves
    }

    /**
     * Get the first player
     *
     * @return the connection to the player
     */
    public ClientConnection getClient1() {
        return client1;
    }

    /**
     * Get the second player
     *
     * @return the connection to the player
     */
    public ClientConnection getClient2() {
        return client2;
    }

    @Override
    public ImageIcon getImage(Chessman.NAMES name, boolean black) {
        return null;
    }

    @Override
    public void onCheck() {
        //send check
    }

    @Override
    public void onCheckMate() {
        //send checkmate
    }

    @Override
    public void onStaleMate() {
        //send stalemate
    }

    @Override
    public void promotion(Pawn pawn) {
        //send promotion
    }

    @Override
    public void updateMovements() {
        new ServerMovementUpdater(getGameState()).start();
    }
}
