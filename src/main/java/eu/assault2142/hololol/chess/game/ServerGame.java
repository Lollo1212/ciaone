package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.CastlingMove;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.server.networking.ClientConnection;
import javax.swing.ImageIcon;

/**
 *
 * @author hololol2
 */
public class ServerGame extends Game {

    public ClientConnection client1;
    public ClientConnection client2;
    private Square selected;
    private Chessman picked;

    public ServerGame(ClientConnection a, ClientConnection b) {
        super(TYPE.SERVER);
        client1 = a;
        client2 = b;
    }

    @Override
    public void clickAt(int feldx, int feldy) {
        selected = getGameState().getSquare(feldx, feldy);
        doMoveIfPossible();
    }

    /**
     * Execute the selected Move or Capture if it is possible
     */
    private void doMoveIfPossible() {
        if (selected != null) {
            selected.highlight(Square.HIGHLIGHT.SELECTED);
            if (picked != null) {
                if (picked.doMove(selected.getX(), selected.getY())
                        || picked.doCapture(selected.getX(), selected.getY())) {
                    client1.write(ClientMessages.Move, new Object[]{picked.getPositionInArray(), selected.getX(), selected.getY()});
                }

                if (picked.getClass() == King.class) {
                    ((King) picked).doCastling(getCastlingMove(), getGameState());
                }
                client1.write(ClientMessages.Move, new Object[]{picked.getPositionInArray(), selected.getX(), selected.getY()});
            }

            picked = null;
        }
    }

    /**
     * Assemble the CastlingMove which is currently selected
     *
     * @return the assembled CastlingMove
     */
    private CastlingMove getCastlingMove() {
        Rook t;
        int tx;
        int ty;
        if (picked.getX() < selected.getX()) {
            t = (Rook) getGameState().getChessmen(picked.isBlack())[9];
            tx = 5;
        } else {
            t = (Rook) getGameState().getChessmen(picked.isBlack())[8];
            tx = 3;
        }
        ty = picked.isBlack() ? 0 : 7;
        return new CastlingMove(selected.getX(), selected.getY(), t, tx, ty, (King) picked);
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
