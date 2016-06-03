package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.game.Square;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a chessman in the gamesituation
 *
 * @author hololol2
 */
public abstract class Chessman {

    //true if this chessman is black
    boolean black;
    //true if it is captured
    boolean captured;
    List<Movement> captures;
    //the gamesituation
    GameState gamesituation;
    //false if not moved till now
    boolean moved = false;
    List<Movement> moves;
    //the number in the chessmen-array
    int positioninarray;
    //x-coordinate
    int posx;
    //y-coordinate
    int posy;
    //the value of this chessman
    int value;
    //the class of the chessman
    NAMES type;

    /**
     *
     * @param black whether this chessman is black
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param gamesituation the gamesituation
     */
    protected Chessman(boolean black, int posx, int posy, GameState gamesituation) {
        this.gamesituation = gamesituation;
        if (posx <= 7 && posx >= 0 && posy <= 7 && posy >= 0) {
            this.black = black;
            this.posx = posx;
            this.posy = posy;
        }
    }

    /**
     * Adds the capture to the allowed captures
     *
     * @param move a capture this chessman should be allowed to do
     */
    public void addCapture(Movement move) {
        captures.add(move);
    }

    /**
     * Adds the move to the allowed moves
     *
     * @param move a move this chessman should be allowed to do
     */
    public void addMove(Movement move) {
        moves.add(move);
    }

    @Override
    public abstract Chessman clone();

    /**
     * Compute the captures this chessman is allowed to do
     *
     * @param checkForCheck whether to remove captures which lead to a
     * check-situation
     * @param situation
     * @return an array of possible captures
     */
    public abstract List<Movement> computeCaptures(boolean checkForCheck, GameState situation);

    /**
     * Compute the moves this chessman is allowed to do
     *
     * @param checkForCheck whether to remove moves which lead to a
     * check-situation
     * @param situation
     * @return an array of possible moves
     */
    public abstract List<Movement> computeMoves(boolean checkForCheck, GameState situation);

    /**
     * Do a capture with this chessman
     *
     * @param targetX the x-coordinate of the target-square
     * @param targetY the y-coordinate of the target-square
     * @return true if the capture was successful, false otherwise
     */
    public boolean doCapture(int targetX, int targetY) {
        Movement capture = new Movement(targetX, targetY, this);
        Square square = gamesituation.getSquare(targetX, targetY);
        boolean r = false;
        if (gamesituation.getTurn() == black) {
            List<Movement> bewegungen = getCaptures();
            if (bewegungen != null && square.isOccupiedByColor(!black)) {
                Optional<Movement> findFirst = bewegungen.stream().filter((Movement m) -> {
                    return capture.equals(m);
                }).findFirst();
                r = findFirst.isPresent();
                findFirst.ifPresent((Movement m) -> {
                    executeCapture(m);
                });
            }
        }
        return r;
    }

    /**
     * Do a move with this chessman
     *
     * @param targetX the x-coordinate of the target-square
     * @param targetY the y-coordinate of the target-square
     * @return true if the move was successful, false otherwise
     */
    public boolean doMove(int targetX, int targetY) {
        Movement move = new Movement(targetX, targetY, this);
        Square square = gamesituation.getSquare(targetX, targetY);
        boolean r = false;
        if (gamesituation.getTurn() == black) {
            List<Movement> bewegungen = getMoves();
            if (bewegungen != null && !square.isOccupied()) {
                Optional<Movement> findFirst = bewegungen.stream().filter((Movement m) -> {
                    return move.equals(m);
                }).findFirst();
                r = findFirst.isPresent();
                findFirst.ifPresent((Movement m) -> {
                    executeMove(m);
                });
            }
        }
        return r;
    }

    /**
     * Get the possible captures
     *
     * @return the captures this chessman is allowed to do
     */
    public List<Movement> getCaptures() {
        return captures;
    }

    /**
     * Get the possible moves
     *
     * @return the moves this chessman is allowed to do
     */
    public List<Movement> getMoves() {
        return moves;
    }

    /**
     *
     * @return the position this chessman has in the array
     */
    public int getPositionInArray() {
        return positioninarray;
    }

    /**
     *
     * @return the value of the chessman
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @return the x-coordinate of the chessman
     */
    public int getX() {
        return posx;
    }

    /**
     *
     * @return the y-coordinate of the chessman
     */
    public int getY() {
        return posy;
    }

    /**
     *
     * @return true if this chessman is black, false otherwise
     */
    public boolean isBlack() {
        return black;
    }

    /**
     *
     * @return true if the chessman is captured, false otherwise
     */
    public boolean isCaptured() {
        return captured;
    }

    /**
     * Set this chessman as captured and move it to the edge
     */
    public void setCaptured() {
        captured = true;
        posx = 9;
        posy = 9;
    }

    /**
     * Update the allowed moves and captures
     */
    public void updateMovements() {
        moves = computeMoves(true, gamesituation);
        captures = computeCaptures(true, gamesituation);
    }

    /**
     * Add the given capture to the list if the capture is possible (target
     * occupied by enemy chessman)
     *
     * @param captures the list of captures
     * @param posx the targetX
     * @param posy the targetY
     * @param situation the current gamestate
     * @return true if the target is not occupied, false otherwise
     */
    protected boolean addIfCapturePossible(List<Movement> captures, int posx, int posy, GameState situation) {
        Square square = situation.getSquare(posx, posy);
        if (square != null) {
            if (square.isOccupiedByColor(!black)) {
                captures.add(new Movement(posx, posy, this));
                return false;
            }
            return !square.isOccupied();
        }
        return false;
    }

    /**
     * Add the given move to the list if the move is possible (target not
     * occupied by any chessman)
     *
     * @param moves the list of moves
     * @param posx the targetX
     * @param posy the targetY
     * @param situation the current gamestate
     * @return true if the target is not occupied, false otherwise
     */
    protected boolean addIfMovePossible(List<Movement> moves, int posx, int posy, GameState situation) {
        Square square = situation.getSquare(posx, posy);
        if (square != null && !square.isOccupied()) {
            moves.add(new Movement(posx, posy, this));
            return true;
        }
        return false;
    }

    /**
     * Moves the pawn to the correct position in the edge-zone
     */
    protected void moveToEdgeZone() {
        int xpos;
        int ypos;
        if (black) {
            if (gamesituation.getCaptured(true) > 7) {
                xpos = 9;
                ypos = gamesituation.getCaptured(true) - 8;
            } else {
                xpos = 8;
                ypos = gamesituation.getCaptured(true);
            }
            gamesituation.incCaptured(true);
        } else {
            if (gamesituation.getCaptured(false) > 7) {
                xpos = 11;
                ypos = gamesituation.getCaptured(false) - 8;
            } else {
                xpos = 10;
                ypos = gamesituation.getCaptured(false);
            }
            gamesituation.incCaptured(false);
        }
        posx = xpos;
        posy = ypos;
    }

    /**
     * Remove all moves which would result in a check-position against this
     * player
     *
     * @param moves the list of moves
     * @param situation the gamestate
     * @return the list without the check-moves
     */
    protected LinkedList<Movement> removeCheckMoves(List<Movement> moves, GameState situation) {
        LinkedList<Movement> ret = new LinkedList();
        moves.stream().filter((Movement move) -> {
            GameState gsnew = situation.emulateMove(this, move.targetX, move.targetY);
            return !gsnew.dangerForKing(black);
        }).forEach(ret::add);
        return ret;
    }

    /**
     * Execute the capture. Does not perform any checks if possible
     *
     * @param m the capture to execute
     */
    private void executeCapture(Movement m) {
        //Figur schlagen
        Chessman f = gamesituation.getSquare(m.targetX, m.targetY).occupier;
        f.captured = true;
        f.moveToEdgeZone();
        gamesituation.getSquare(posx, posy).occupier = null;
        posx = m.targetX;
        posy = m.targetY;
        gamesituation.getSquare(m.targetX, m.targetY).occupier = this;
        gamesituation.nextTurn(this);
    }

    /**
     * Execute the given move. Does not perform any checks if possible
     *
     * @param move the move to execute
     */
    private void executeMove(Movement move) {
        gamesituation.getSquare(posx, posy).occupier = null;
        posx = move.targetX;
        posy = move.targetY;
        gamesituation.getSquare(posx, posy).occupier = this;
        gamesituation.nextTurn(this);
    }

    public static enum NAMES {
        BISHOP, KING, KNIGHT, PAWN, QUEEN, ROOK
    }

    public NAMES getType() {
        return type;
    }
}
