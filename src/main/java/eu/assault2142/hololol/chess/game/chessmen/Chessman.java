package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.game.Square;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.swing.ImageIcon;

/**
 * Represents a chessman in the gamesituation
 *
 * @author hololol2
 */
public abstract class Chessman {

    public void setCaptured() {
        captured = true;
        posx = 9;
        posy = 9;
    }

    public void addCapture(Move move) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addMove(Move move) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateMovements() {
        moves = computeMoves(true, gamesituation);
        captures = computeCaptures(true, gamesituation);
    }

    public static enum NAMES {
        BISHOP, KING, KNIGHT, PAWN, QUEEN, ROOK
    };

    //Image of the chessman
    ImageIcon image;
    //the gamesituation
    GameState gamesituation;
    //true if this chessman is black
    boolean black;
    //true if it is captured
    boolean captured;
    //x-coordinate
    int posx;
    //y-coordinate
    int posy;
    //false if not moved till now
    boolean moved = false;
    //the number in the chessmen-array
    int positioninarray;
    //the value of this chessman
    int value;
    Move[] moves;
    Move[] captures;

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
     * Execute a move with this chessman
     *
     * @param targetX the x-coordinate of the target-square
     * @param targetY the y-coordinate of the target-square
     * @return true if the move was successful, false otherwise
     */
    public boolean doMove(int targetX, int targetY) {
        Move move = new Move(targetX, targetY, this);
        Square square = gamesituation.getSquare(targetX, targetY);
        boolean r = false;
        if (gamesituation.getTurn() == black) {
            Move[] bewegungen = gamesituation.getPossibleMoves(positioninarray, black);
            if (bewegungen != null && !square.isOccupied()) {
                Optional<Move> findFirst = Arrays.stream(bewegungen).filter((Move m) -> {
                    return move.equals(m);
                }).findFirst();
                r = findFirst.isPresent();
                findFirst.ifPresent((Move m) -> {
                    executeMove(m);
                });
            }
        }
        return r;
    }

    /**
     * Execute a capture with this chessman
     *
     * @param targetX the x-coordinate of the target-square
     * @param targetY the y-coordinate of the target-square
     * @return true if the capture was successful, false otherwise
     */
    public boolean doCapture(int targetX, int targetY) {
        Move capture = new Move(targetX, targetY, this);
        Square square = gamesituation.getSquare(targetX, targetY);
        boolean r = false;
        if (gamesituation.getTurn() == black) {
            Move[] bewegungen = gamesituation.getPossibleCaptures(positioninarray, black);
            if (bewegungen != null && square.isOccupiedByColor(!black)) {
                Optional<Move> findFirst = Arrays.stream(bewegungen).filter((Move m) -> {
                    return capture.equals(m);
                }).findFirst();
                r = findFirst.isPresent();
                findFirst.ifPresent((Move m) -> {
                    executeCapture(m);
                });
            }
        }
        return r;
    }

    /**
     * Return the moves this chessman is allowed to do
     *
     * @param checkForCheck whether to remove moves which lead to a
     * check-situation
     * @param situation
     * @return an array of possible moves
     */
    public abstract Move[] computeMoves(boolean checkForCheck, GameState situation);

    /**
     * Return the captures this chessman is allowed to do
     *
     * @param checkForCheck whether to remove captures which lead to a
     * check-situation
     * @param situation
     * @return an array of possible captures
     */
    public abstract Move[] computeCaptures(boolean checkForCheck, GameState situation);

    @Override
    public abstract Chessman clone();

    /**
     *
     * @return the position this chessman has in the array
     */
    public int getPositionInArray() {
        return positioninarray;
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
     * @return true if the chessman is captured, false otherwise
     */
    public boolean isCaptured() {
        return captured;
    }

    /**
     *
     * @return the value of the chessman
     */
    public int getValue() {
        return value;
    }

    /**
     * Print the Info to the commandline after a move
     */
    public void printState() {
        /*
        String n;
        if (!gamesituation.isServer()) {
            if (black == true) {
                n = Translator.TRANSLATOR.getTranslation("game_color_black");
            } else {
                n = Translator.TRANSLATOR.getTranslation("game_color_white");
            }
            ((ClientGame)gamesituation).getGameFrame().write(Translator.TRANSLATOR.getTranslation("game_player_turn", new Object[]{n}));
        }*/
    }

    /**
     *
     * @return the image of the chessman
     */
    public ImageIcon getImage() {
        return image;
    }

    private void executeMove(Move move) {
        gamesituation.getSquare(posx, posy).occupier = null;
        posx = move.targetX;
        posy = move.targetY;
        gamesituation.getSquare(posx, posy).occupier = this;
        gamesituation.nextTurn(this);
    }

    private void executeCapture(Move m) {
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

    protected LinkedList<Move> removeCheckMoves(List<Move> moves, GameState situation) {
        GameState gsneu;
        LinkedList<Move> ret = new LinkedList();
        for (int c = 0; c < moves.size(); c++) {
            gsneu = situation.emulateMove(this, moves.get(c).targetX, moves.get(c).targetY);
            if (gsneu.dangerForKing(black, gsneu.computeAllCaptures(!black))) {
                //moves.remove(c);
            } else {
                ret.add(moves.get(c));
            }
        }
        return ret;
    }

    protected boolean addIfMovePossible(List<Move> moves, int posx, int posy, GameState situation) {
        Square square = situation.getSquare(posx, posy);
        if (square != null && !square.isOccupied()) {
            moves.add(new Move(posx, posy, this));
            return true;
        }
        return false;
    }

    protected boolean addIfCapturePossible(List<Move> captures, int posx, int posy, GameState situation) {
        Square square = situation.getSquare(posx, posy);
        if (square != null) {
            if (square.isOccupiedByColor(!black)) {
                captures.add(new Move(posx, posy, this));
                return false;
            }
            return !square.isOccupied();
        }
        return false;
    }

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

    public Move[] getMoves() {
        return moves;
    }

    public Move[] getCaptures() {
        return captures;
    }
}
