package eu.assault2142.hololol.chess.game;

/**
 *
 * @author hololol2
 */
public class ServerMovementUpdater extends MovementUpdater {

    public ServerMovementUpdater(GameState g) {
        super(g);
    }

    @Override
    public void run() {
        updateMovements();
        testCheck();
    }

}
