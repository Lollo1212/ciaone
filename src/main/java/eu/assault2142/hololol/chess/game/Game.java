package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.*;

/**
 * Represents a chess game
 *
 * @author hololol2
 */
public abstract class Game {

    protected Chessman picked;
    protected Square selected;
    private final GameState gamestate;
    private final TYPE type;

    /**
     * Create a new Game
     *
     * @param type the type of the game
     */
    protected Game(TYPE type) {
        this.type = type;
        gamestate = new GameState(this);
        gamestate.init();
    }

    /**
     * Consume a click at the given position
     *
     * @param squareX the x-coordinate (in squares)
     * @param squareY the y-coordinate (in squares)
     */
    public abstract void clickAt(int squareX, int squareY);

    /**
     * End the game
     */
    public abstract void endGame();

    /**
     * Execute a Promotion-Command
     *
     * @param target the target-type of the promotion
     * @param black whether the chessman is black
     * @param number the number of the chessman
     */
    public void execPromotion(String target, boolean black, int number) {
        Pawn pawn = (Pawn) getGameState().getChessman(black, number);
        Chessman man;
        switch (target) {
            case "ROOK":
                man = Rook.promotion(pawn, this);
                break;
            case "KNIGHT":
                man = Knight.promotion(pawn, this);
                break;
            case "BISHOP":
                man = Bishop.promotion(pawn, this);
                break;
            default:
                man = Queen.promotion(pawn, this);
                break;
        }
        getGameState().setChessman(black, number, man);
        getGameState().getSquare(man.getXPosition(), man.getYPosition()).occupier = man;

        updateMovements();
    }

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

}
