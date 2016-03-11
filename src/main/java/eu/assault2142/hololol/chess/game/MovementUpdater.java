package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hololol2
 */
public abstract class MovementUpdater extends Thread {

    private final GameState gamestate;
    private boolean schach, schachmatt;

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
        List<Move> schläge = gamestate.getAllCaptures(!gamestate.getTurn());
        //wenn ziel König ist, dann Schach
        for (Move schläge1 : schläge) {
            if (schläge1 != null) {
                Square f = gamestate.getSquare(schläge1.getTargetX(), schläge1.getTargetY());
                if (f.isOccupied() && f.occupier.getClass() == King.class) {
                    schach = true;
                    break;
                }
            }
        }

        List<Move> zü = gamestate.getAllMoves(gamestate.getTurn());
        List<Move> schl = gamestate.getAllCaptures(gamestate.getTurn());
        zü.addAll(schl);
        schachmatt = true;
        //wenn irgendein Zug möglich ist, dann kein schachmatt
        for (Move bewegungen1 : zü) {
            if (bewegungen1 != null) {
                schachmatt = false;
                break;
            }
        }
        if (schachmatt) {
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
        Thread[] t = new Thread[32];
        for (int a = 0; a < 16; a++) {
            if (!gamestate.getChessmen(true)[a].isCaptured()) {
                t[a] = new MovementUpdaterThread(gamestate.getChessmen(true)[a]);
                t[a].start();
            }
            if (!gamestate.getChessmen(false)[a].isCaptured()) {
                t[a + 16] = new MovementUpdaterThread(gamestate.getChessmen(false)[a]);
                t[a + 16].start();
            }
        }
        for (int a = 0; a < 32; a++) {
            try {
                if (t[a] != null) {
                    t[a].join();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MovementUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
