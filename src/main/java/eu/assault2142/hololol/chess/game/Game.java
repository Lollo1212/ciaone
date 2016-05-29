package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.*;

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

    public void execPromotion(String target, boolean color, int number) {
        Pawn pawn = (Pawn) getGameState().getChessmen(color)[number];
        Chessman man;
        switch (target) {
            case "ROOK":
                man = Rook.promotion(pawn, getGameState());
                break;
            case "KNIGHT":
                man = Knight.promotion(pawn, getGameState());
                break;
            case "BISHOP":
                man = Bishop.promotion(pawn, getGameState());
                break;
            default:
                man = Queen.promotion(pawn, getGameState());
                break;
        }
        getGameState().getChessmen(color)[number] = man;
        getGameState().getSquare(man.getX(), man.getY()).occupier = man;

        updateMovements();
    }
}
