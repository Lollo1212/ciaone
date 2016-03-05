package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.GameSituation;
import java.util.LinkedList;

/**
 *
 * @author hololol2
 */
public class Knight extends Chessman {

    /**
     * Creates a new Knight
     *
     * @param black whether this chessman is black or not
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param game the game
     */
    private Knight(boolean black, int posx, int posy, Game game) {
        super(black, posx, posy, game);
        image = game.getImage(NAMES.KNIGHT, black);
        value = 8;
    }

    /**
     * Creates a new Knight
     *
     * @param black whether this chessman is black or not
     * @param number the number of the bishop (0 for the left, 1 for the right)
     * @param game the game
     * @param numberinarray the number in the chessmen-array
     * @return the knight
     */
    public static Knight createKnight(boolean black, int number, Game game, int numberinarray) {
        int a;
        int b;
        if (black == true) {
            a = 0;
        } else {
            a = 7;
        }
        Knight s = null;
        if (number == 0) {
            b = 1;
            s = new Knight(black, b, a, game);
        } else if (number == 1) {
            b = 6;
            s = new Knight(black, b, a, game);
        } else {
            throw new IllegalArgumentException("The given number is incorrect");
        }
        s.positioninarray = numberinarray;
        return s;
    }

    @Override
    public Move[] computeMoves(boolean checkForCheck, GameSituation situation) {
        LinkedList<Move> moves = new LinkedList();
        addIfMovePossible(moves,posx-2,posy+1,situation);
        addIfMovePossible(moves,posx-2,posy-1,situation);
        addIfMovePossible(moves,posx+2,posy+1,situation);
        addIfMovePossible(moves,posx+2,posy-1,situation);
        addIfMovePossible(moves,posx+1,posy+2,situation);
        addIfMovePossible(moves,posx-1,posy+2,situation);
        addIfMovePossible(moves,posx+1,posy-2,situation);
        addIfMovePossible(moves,posx-1,posy-2,situation);
        if (checkForCheck) {
            removeCheckMoves(moves,situation);
        }
        Move[] ret = new Move[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }

    @Override
    public Move[] computeCaptures(boolean checkForChecks,GameSituation situation) {
        LinkedList<Move> captures = new LinkedList();
        addIfCapturePossible(captures,posx-2,posy+1,situation);
        addIfCapturePossible(captures,posx-2,posy-1,situation);
        addIfCapturePossible(captures,posx+2,posy+1,situation);
        addIfCapturePossible(captures,posx+2,posy-1,situation);
        addIfCapturePossible(captures,posx+1,posy+2,situation);
        addIfCapturePossible(captures,posx-1,posy+2,situation);
        addIfCapturePossible(captures,posx+1,posy-2,situation);
        addIfCapturePossible(captures,posx-1,posy-2,situation);
        
        //Überprüfen auf Schach-Position
        if (checkForChecks) {
            removeCheckMoves(captures,situation);
        }
        Move[] ret = new Move[captures.size()];
        ret = captures.toArray(ret);
        return ret;
    }

    /**
     * Create a new Knight by promotion
     *
     * @param pawn the pawn to promote
     * @param game the game
     * @return a new knight
     */
    public static Knight promotion(Pawn pawn, Game game) {
        Knight l = null;
        if ((pawn.posy == 0 && !pawn.black) || (pawn.posy == 7 && pawn.black)) {
            l = new Knight(pawn.black, pawn.posx, pawn.posy, game);
        } else {
            throw new IllegalArgumentException("The pawn is currently not promotable");
        }
        l.positioninarray = pawn.positioninarray;
        return l;
    }

    @Override
    public Knight clone() {
        Knight s = new Knight(black, posx, posy, game);
        s.captured = captured;
        s.moved = moved;
        s.positioninarray = positioninarray;
        return s;
    }
}
