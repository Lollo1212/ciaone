package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.game.Square;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a chessman in the gamestate
 *
 * @author hololol2
 */
public abstract class Chessman {

    //true if this chessman is black
    boolean black;
    //true if it is captured
    boolean captured;
    List<Movement> captures;
    //the gamestate
    GameState gamestate;
    //false if not moved till now
    boolean moved = false;
    List<Movement> moves;
    //x-coordinate
    int posX;
    //y-coordinate
    int posY;
    //the number in the chessmen-array
    int positioninarray;
    //the class of the chessman
    NAMES type;
    //the value of this chessman
    int value;

    /**
     * Create a new Chessman
     *
     * @param black whether this chessman is black
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param gamestate the gamestate
     */
    protected Chessman(boolean black, int posx, int posy, GameState gamestate) {
        this.gamestate = gamestate;
        if (posx <= 7 && posx >= 0 && posy <= 7 && posy >= 0) {
            this.black = black;
            this.posX = posx;
            this.posY = posy;
        }
    }

    /**
     * Adds the capture to the allowed captures
     *
     * @param move a capture this chessman should be allowed to do
     */
    public void addCapture(Movement move) {
        if (gamestate.getGame().getType() == Game.TYPE.CLIENT) {
            captures.add(move);
        }
    }

    /**
     * Adds the move to the allowed moves
     *
     * @param move a move this chessman should be allowed to do
     */
    public void addMove(Movement move) {
        if (gamestate.getGame().getType() == Game.TYPE.CLIENT) {
            moves.add(move);
        }
    }

    @Override
    public abstract Chessman clone();

    /**
     * Compute the captures this chessman is allowed to do
     *
     * @param checkForCheck whether to remove captures which lead to a
     * check-situation
     * @param gamestate
     * @return an array of possible captures
     */
    public abstract List<Movement> computeCaptures(boolean checkForCheck, GameState gamestate);

    /**
     * Compute the moves this chessman is allowed to do
     *
     * @param checkForCheck whether to remove moves which lead to a
     * check-situation
     * @param gamestate
     * @return an array of possible moves
     */
    public abstract List<Movement> computeMoves(boolean checkForCheck, GameState gamestate);

    /**
     * Do a capture with this chessman
     *
     * @param targetX the x-coordinate of the target-square
     * @param targetY the y-coordinate of the target-square
     * @return true if the capture was successful, false otherwise
     */
    public boolean doCapture(int targetX, int targetY) {
        Movement capture = new Movement(targetX, targetY, this);
        Square square = gamestate.getSquare(targetX, targetY);
        boolean successfull = false;
        if (gamestate.getTurn() == black) {
            List<Movement> bewegungen = getCaptures();
            if (bewegungen != null && square.isOccupiedByColor(!black)) {
                Optional<Movement> findFirst = bewegungen.stream().filter((Movement m) -> {
                    return capture.equals(m);
                }).findFirst();
                successfull = findFirst.isPresent();
                findFirst.ifPresent((Movement m) -> {
                    executeCapture(m);
                });
            }
        }
        return successfull;
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
        Square square = gamestate.getSquare(targetX, targetY);
        boolean successfull = false;
        if (gamestate.getTurn() == black) {
            List<Movement> bewegungen = getMoves();
            if (bewegungen != null && !square.isOccupied()) {
                Optional<Movement> findFirst = bewegungen.stream().filter((Movement m) -> {
                    return move.equals(m);
                }).findFirst();
                successfull = findFirst.isPresent();
                findFirst.ifPresent((Movement m) -> {
                    executeMove(m);
                });
            }
        }
        return successfull;
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

    public NAMES getType() {
        return type;
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
        return posX;
    }

    /**
     *
     * @return the y-coordinate of the chessman
     */
    public int getY() {
        return posY;
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
        posX = 9;
        posY = 9;
    }

    /**
     * Update the allowed moves and captures
     */
    public void updateMovements() {
        moves = computeMoves(true, gamestate);
        captures = computeCaptures(true, gamestate);
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
            if (gamestate.getCaptured(true) > 7) {
                xpos = 9;
                ypos = gamestate.getCaptured(true) - 8;
            } else {
                xpos = 8;
                ypos = gamestate.getCaptured(true);
            }
            gamestate.incCaptured(true);
        } else {
            if (gamestate.getCaptured(false) > 7) {
                xpos = 11;
                ypos = gamestate.getCaptured(false) - 8;
            } else {
                xpos = 10;
                ypos = gamestate.getCaptured(false);
            }
            gamestate.incCaptured(false);
        }
        posX = xpos;
        posY = ypos;
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
     * @param move the capture to execute
     */
    private void executeCapture(Movement move) {
        Chessman chessman = gamestate.getSquare(move.targetX, move.targetY).occupier;
        chessman.captured = true;
        chessman.moveToEdgeZone();
        gamestate.getSquare(posX, posY).occupier = null;
        posX = move.targetX;
        posY = move.targetY;
        gamestate.getSquare(move.targetX, move.targetY).occupier = this;
        gamestate.nextTurn(this);
    }

    /**
     * Execute the given move. Does not perform any checks if possible
     *
     * @param move the move to execute
     */
    private void executeMove(Movement move) {
        gamestate.getSquare(posX, posY).occupier = null;
        posX = move.targetX;
        posY = move.targetY;
        gamestate.getSquare(posX, posY).occupier = this;
        gamestate.nextTurn(this);
    }

    public static enum NAMES {
        BISHOP, KING, KNIGHT, PAWN, QUEEN, ROOK
    }

}
