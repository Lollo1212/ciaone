package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.util.Translator;
import eu.assault2142.hololol.chess.game.MovementUpdater;

/**
 * Updates the possible moves and captures after each turn. Also checks for
 * check/checkmate/stalemate-situations. Used in games with a UI
 *
 * @author hololol2
 */
public class ClientMovementUpdater extends MovementUpdater {

    /**
     * Create a new Instance
     *
     * @param game the game
     */
    public ClientMovementUpdater(Game game) {
        super(game);
    }

    @Override
    public void run() {

        Runnable wait = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Main.MENU.showErrorMessage(Translator.getString("CRITICAL_ERROR"), true);
            }
        };
        Thread waitthread = new Thread(wait);
        waitthread.start();
        updateMovements();
        try {
            waitthread.join();
        } catch (InterruptedException ex) {
            Main.MENU.showErrorMessage(Translator.getString("CRITICAL_ERROR"), true);
        }
        testCheck();
    }

}
