package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.GameSituation;

/**
 * Represents a chessman that is used to precompute moves
 *
 * @author hololol2
 */
public class AbstractChessman extends Chessman {

    //the chessman represented by this instance
    private Chessman chessman;
    //the possible moves
    private Move[] moves;
    //the possible captures
    private Move[] captures;
    //a clone of the chessman to do moves
    private Chessman clone;

    /**
     * Creates a new AbstractChessman
     *
     * @param man the chessman to represent
     * @param game the game
     */
    public AbstractChessman(Chessman man, Game game) {
        super(man.black, man.posx, man.posy, game);
        this.chessman = man;
        clone = man.clone();
        moves = new Move[0];
        captures = new Move[0];
    }

    /**
     * Compute next moves and captures
     */
    public void updateMovements() {
        moves = chessman.computeMoves(true,game.getGameSituation());
        captures = chessman.computeCaptures(true,game.getGameSituation());
    }

    @Override
    public AbstractChessman clone() {
        AbstractChessman af = new AbstractChessman(chessman, game);
        return af;
    }

    /**
     * Returns the moves
     *
     * @return the possible moves of the chessman
     */
    public Move[] getMoves() {
        return moves;
    }

    /**
     * Returns the captures
     *
     * @return the possible captures of the chessman
     */
    public Move[] getCaptures() {
        return captures;
    }

    /**
     *
     * @return the chessman represented by this object
     */
    public Chessman getChessman() {
        return chessman;
    }

    /**
     *
     * @return the clone this object works with
     */
    public Chessman getClone() {
        return clone;
    }

    /**
     * Set this object as captured
     */
    public void setCaptured() {
        clone.captured = true;
        clone.posx = 9;
        clone.posy = 9;
    }

    @Override
    public boolean doMove(int x, int y) {
        posx = x;
        posy = y;
        clone.posx = x;
        clone.posy = y;
        return true;
    }

    /**
     * Adds a move to the allowed moves
     *
     * @param m the move
     */
    public void addMove(Move m) {
        Move[] mov = new Move[moves.length + 1];
        System.arraycopy(moves, 0, mov, 0, moves.length);
        mov[mov.length - 1] = m;
        moves = mov;
    }

    /**
     * Adds a capture to the allowed captures
     *
     * @param m the capture
     */
    public void addCapture(Move m) {
        Move[] mov = new Move[captures.length + 1];
        System.arraycopy(captures, 0, mov, 0, captures.length);
        mov[mov.length - 1] = m;
        captures = mov;
    }

    @Override
    public boolean isCaptured() {
        return clone.isCaptured();
    }

    @Override
    public int getValue() {
        return chessman.getValue();
    }

    @Override
    public Move[] computeMoves(boolean checkForCheck, GameSituation situation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Move[] computeCaptures(boolean checkForCheck, GameSituation situation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
