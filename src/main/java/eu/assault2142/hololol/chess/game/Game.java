package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.*;
import javax.swing.ImageIcon;

/**
 * Represents a chess game
 *
 * @author hololol2
 */
public abstract class Game {

    public static enum TYPE {
        LOCAL, SERVER, CLIENT
    };

    public final TYPE type;
    private GameState gamesituation;

    protected Game(TYPE t) {
        type = t;
        gamesituation = new GameState(this);
    }

    public GameState getGameSituation() {
        return gamesituation;
    }

    private void nextTurn(Chessman moved) {

        //Bildschirmausgabe "... ist dran"
        gamesituation.nextTurn(moved);

        updateMovements();
    }

    public TYPE getType() {
        return type;
    }

    public abstract ImageIcon getImage(Chessman.NAMES name, boolean black);

    protected abstract void updateMovements();

    public abstract void promotion(Pawn pawn);

    public abstract void finishedCalcs();

    public abstract void endGame();

    public abstract void clickAt(int feldx, int feldy);

    public abstract void onCheckMate();

    public abstract void onStaleMate();

    public abstract void onCheck();
}
