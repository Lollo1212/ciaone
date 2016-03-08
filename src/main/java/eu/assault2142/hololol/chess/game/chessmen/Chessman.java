package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.GameSituation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.swing.ImageIcon;

/**
 * Represents a chessman in the game
 *
 * @author hololol2
 */
public abstract class Chessman {

    public static enum NAMES {
        BISHOP, KING, KNIGHT, PAWN, QUEEN, ROOK
    };

    //Image of the chessman
    ImageIcon image;
    //the game
    Game game;
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

    /**
     *
     * @param black whether this chessman is black
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param game the game
     */
    protected Chessman(boolean black, int posx, int posy, Game game) {
        this.game = game;
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
        Square square = game.getSquare(targetX, targetY);
        boolean r = false;
        if (game.getTurn() == black) {
            Move[] bewegungen = game.getPossibleMoves(positioninarray, black);
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
        Square square = game.getSquare(targetX, targetY);
        boolean r = false;
        if (game.getTurn() == black) {
            Move[] bewegungen = game.getPossibleCaptures(positioninarray, black);
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
    public abstract Move[] computeMoves(boolean checkForCheck, GameSituation situation);

    /**
     * Return the captures this chessman is allowed to do
     *
     * @param checkForCheck whether to remove captures which lead to a
     * check-situation
     * @param situation
     * @return an array of possible captures
     */
    public abstract Move[] computeCaptures(boolean checkForCheck, GameSituation situation);

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
        if (!game.isServer()) {
            if (black == true) {
                n = Translator.TRANSLATOR.getTranslation("game_color_black");
            } else {
                n = Translator.TRANSLATOR.getTranslation("game_color_white");
            }
            ((ClientGame)game).getGameFrame().write(Translator.TRANSLATOR.getTranslation("game_player_turn", new Object[]{n}));
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
        game.getSquare(posx, posy).occupier = null;
        posx = move.targetX;
        posy = move.targetY;
        game.getSquare(posx, posy).occupier = this;
        game.nextTurn();
    }

    private void executeCapture(Move m) {
        //Figur schlagen
        Chessman f = game.getSquare(m.targetX, m.targetY).occupier;
        f.captured = true;
        f.moveToEdgeZone();
        game.getSquare(posx, posy).occupier = null;
        posx = m.targetX;
        posy = m.targetY;
        game.getSquare(m.targetX, m.targetY).occupier = this;
        game.nextTurn();
    }

    protected LinkedList<Move> removeCheckMoves(List<Move> moves, GameSituation situation) {
        GameSituation gsneu;
        LinkedList<Move> ret = new LinkedList();
        for (int c = 0; c < moves.size(); c++) {
            gsneu = situation.doMove(this, moves.get(c).targetX, moves.get(c).targetY);
            if (gsneu.dangerForKing(black, gsneu.getAllCaptures(!black))) {
                //moves.remove(c);
            } else {
                ret.add(moves.get(c));
            }
        }
        return ret;
    }

    protected boolean addIfMovePossible(List<Move> moves, int posx, int posy,GameSituation situation) {
        Square square = situation.getSquare(posx , posy );
        if (square != null && !square.isOccupied()) {
            moves.add(new Move(posx , posy , this));
            return true;
        }
        return false;
    }
    
    protected boolean addIfCapturePossible(List<Move> captures, int posx, int posy,GameSituation situation) {
        Square square = situation.getSquare(posx , posy );
        if (square != null) {
            if(square.isOccupiedByColor(!black)){
                captures.add(new Move(posx , posy , this));
                return false;
            }
            return !square.isOccupied();
        }
        return false;
    }
    
    private void moveToEdgeZone(){
        int xpos;
        int ypos;
        if (black) {
            if (game.getCaptured(true) > 7) {
                xpos = 9;
                ypos = game.getCaptured(true) - 8;
            } else {
                xpos = 8;
                ypos = game.getCaptured(true);
            }
            game.incCaptured(true);
        } else {
            if (game.getCaptured(false) > 7) {
                xpos = 11;
                ypos = game.getCaptured(false) - 8;
            } else {
                xpos = 10;
                ypos = game.getCaptured(false);
            }
            game.incCaptured(false);
        }
        posx = xpos;
        posy = ypos;
    }
}
