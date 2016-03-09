package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.*;
import javax.swing.ImageIcon;

/**
 * Represents a chess game
 *
 * @author hololol2
 */
public abstract class Game {

    public final TYPE type;
    private final GameState gamestate;

    protected Game(TYPE t) {
        type = t;
        gamestate = new GameState(this);
    }

    public abstract void clickAt(int feldx, int feldy);

    public abstract void endGame();

    public abstract void finishedCalcs();

    public GameState getGameState() {
        return gamestate;
    }

    public abstract ImageIcon getImage(Chessman.NAMES name, boolean black);

    public TYPE getType() {
        return type;
    }

    public abstract void onCheck();

    public abstract void onCheckMate();

    public abstract void onStaleMate();

    public abstract void promotion(Pawn pawn);

    protected abstract void updateMovements();

    public static enum TYPE {
        LOCAL, SERVER, CLIENT
    }
}
