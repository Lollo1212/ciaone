package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.MovementUpdater;

/**
 * Updates the possible moves and captures after each turn. Also checks for
 * check/checkmate/stalemate-situations.
 *
 * @author hololol2
 */
public class ClientMovementUpdater extends MovementUpdater {

    /**
     * Create a new Instance
     *
     * @param g the game to update
     */
    public ClientMovementUpdater(Game g) {
        super(g);
    }

    @Override
    public void onStart() {
        if (getGame().getClass() == LocalGame.class) {
            ((LocalGame) getGame()).getGameFrame().getGameBoard().movementsupdating = true;
        } else {
            ((ClientGame) getGame()).getGameFrame().getGameBoard().movementsupdating = true;
        }
    }

    @Override
    public void run() {
        Runnable wait = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        };
        Thread waitthread = new Thread(wait);
        waitthread.start();
        updateMovements();
        try {
            waitthread.join();
        } catch (InterruptedException ex) {
        }
        testCheck();
    }

}
