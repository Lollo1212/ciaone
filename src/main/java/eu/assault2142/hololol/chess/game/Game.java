package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.*;
import javax.swing.ImageIcon;

/**
 * Represents a chess game
 *
 * @author hololol2
 */
public abstract class Game {

    private final TYPE type;
    private final GameState gamestate;

    protected Game(TYPE t) {
        type = t;
        gamestate = new GameState(this);
    }

    /**
     * Consume a click at the given position
     *
     * @param feldx the x-coordinate (in fields)
     * @param feldy the y-coordinate (in fields)
     */
    public abstract void clickAt(int feldx, int feldy);

    /**
     * End the game
     */
    public abstract void endGame();

    /**
     * The next-movements-calculations have finished
     */
    public abstract void finishedCalcs();

    /**
     * Get the GameState
     *
     * @return the GameState
     */
    public GameState getGameState() {
        return gamestate;
    }

    /**
     * Get the image for the given chessman
     *
     * @param name the "class" of the chessman
     * @param black the color of the chessman
     * @return the corresponding ImageIcon
     */
    public abstract ImageIcon getImage(Chessman.NAMES name, boolean black);

    /**
     * Get the type of the game
     *
     * @return the type (Server,Local,Client)
     */
    public TYPE getType() {
        return type;
    }

    /**
     * Do Check-Action
     */
    public abstract void onCheck();

    /**
     * Do CheckMate-Action
     */
    public abstract void onCheckMate();

    /**
     * Do StaleMate-Action
     */
    public abstract void onStaleMate();

    /**
     * Do Promote-Action
     *
     * @param pawn the Pawn to promote
     */
    public abstract void promotion(Pawn pawn);

    /**
     * Start movment-calulactions
     */
    protected abstract void updateMovements();

    public static enum TYPE {
        LOCAL, SERVER, CLIENT
    }
}
