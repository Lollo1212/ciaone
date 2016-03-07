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
            ((LocalGame) getGame()).getGameFrame().getGameField().movementsupdating = true;
        } else {
            ((ClientGame) getGame()).getGameFrame().getGameField().movementsupdating = true;
        }
    }

    @Override
    protected void onCheck() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onCheckMate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onStaleMate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
