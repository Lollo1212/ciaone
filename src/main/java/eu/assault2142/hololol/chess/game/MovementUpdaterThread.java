package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.Chessman;

/**
 *
 * @author hololol2
 */
public class MovementUpdaterThread extends Thread {

    private final Chessman chessman;

    /**
     * Create a new MovementUpdaterThread
     *
     * @param man the chessman to update movements
     */
    public MovementUpdaterThread(Chessman man) {
        this.chessman = man;
    }

    @Override
    public void run() {
        chessman.updateMovements();
    }
}
