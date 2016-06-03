package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Movement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hololol2
 */
public abstract class MovementUpdater extends Thread {

    private final GameState gamestate;
    private boolean schach, nomovepossible;

    /**
     * Create a new MovementUpdate
     *
     * @param g the gamestate to update
     */
    public MovementUpdater(GameState g) {
        this.gamestate = g;
    }

    /**
     * Check for check, checkmate and stalemate
     */
    protected void testCheck() {
        List<Movement> captures = gamestate.getAllCaptures(!gamestate.getTurn());
        for (Movement capture : captures) {
            if (capture != null) {
                Square square = gamestate.getSquare(capture.getTargetX(), capture.getTargetY());
                if (square.isOccupied() && square.occupier.getClass() == King.class) {
                    schach = true;
                    break;
                }
            }
        }

        List<Movement> moves = gamestate.getAllMoves(gamestate.getTurn());
        captures = gamestate.getAllCaptures(gamestate.getTurn());
        moves.addAll(captures);
        nomovepossible = true;
        for (Movement move : moves) {
            if (move != null) {
                nomovepossible = false;
                break;
            }
        }
        if (nomovepossible) {
            if (schach) {
                gamestate.getGame().onCheckMate();
            } else {
                gamestate.getGame().onStaleMate();
            }
        } else if (schach) {
            gamestate.getGame().onCheck();
        }
        gamestate.getGame().finishedCalcs();
    }

    /**
     * Update the movements
     */
    protected void updateMovements() {
        Thread[] threads = new Thread[32];
        for (int chessmanNumber = 0; chessmanNumber < 16; chessmanNumber++) {
            if (!gamestate.getChessman(true, chessmanNumber).isCaptured()) {
                threads[chessmanNumber] = new MovementUpdaterThread(gamestate.getChessman(true, chessmanNumber));
                threads[chessmanNumber].start();
            }
            if (!gamestate.getChessman(false, chessmanNumber).isCaptured()) {
                threads[chessmanNumber + 16] = new MovementUpdaterThread(gamestate.getChessman(false, chessmanNumber));
                threads[chessmanNumber + 16].start();
            }
        }
        for (Thread thread : threads) {
            try {
                if (thread != null) {
                    thread.join();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MovementUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
