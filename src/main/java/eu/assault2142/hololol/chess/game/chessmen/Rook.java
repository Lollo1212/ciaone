package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.GameSituation;
import java.util.LinkedList;

/**
 * Represents a Rook
 *
 * @author hololol2
 */
public class Rook extends Chessman {

    /**
     * Creates a new Rook
     *
     * @param black whether this chessman is black or not
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param game the game
     */
    private Rook(boolean black, int posx, int posy, Game game) {
        super(black, posx, posy, game);
        image = game.getImage(NAMES.ROOK, black);
        value = 5;
    }

    /**
     * Creates a new Rook
     *
     * @param black whether this chessman is black or not
     * @param number the number of the bishop (0 for the left, 1 for the right)
     * @param game the game
     * @param numberinarray the number in the chessmen-array
     * @return the rook
     */
    public static Rook createTurm(boolean black, int number, Game game, int numberinarray) {
        int a;
        int b;
        if (black == true) {
            a = 0;
        } else {
            a = 7;
        }
        if (number == 0) {
            b = 0;
        } else {
            b = 7;
        }
        Rook t = new Rook(black, b, a, game);
        t.positioninarray = numberinarray;
        return t;
    }

    @Override
    public Move[] computeMoves(boolean checkForCheck, GameSituation situation) {
        LinkedList<Move> moves = new LinkedList();
        for (int u = posx - 1; u >= 0; u--) {
            if(!addIfMovePossible(moves,u,posy,situation)) break;
        }
        for (int u = posx + 1; u <= 7; u++) {
            if(!addIfMovePossible(moves,u,posy,situation)) break;
        }
        for (int u = posy - 1; u >= 0; u--) {
            if(!addIfMovePossible(moves,posx,u,situation)) break;
        }
        for (int u = posy + 1; u <= 7; u++) {
            if(!addIfMovePossible(moves,posx,u,situation)) break;
        }
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            moves = removeCheckMoves(moves, situation);
        }
        Move[] ret = new Move[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }

    @Override
    public Move[] computeCaptures(boolean checkForCheck, GameSituation situation) {
        LinkedList<Move> captures = new LinkedList();
        for (int u = posx - 1; u >= 0; u--) {
            if(!addIfCapturePossible(captures,u,posy,situation)) break;
        }
        for (int u = posx + 1; u <= 7; u++) {
            if(!addIfCapturePossible(captures,u,posy,situation)) break;
        }
        for (int u = posy - 1; u >= 0; u--) {
            if(!addIfCapturePossible(captures,posx,u,situation)) break;
        }
        for (int u = posy + 1; u <= 7; u++) {
            if(!addIfCapturePossible(captures,posx,u,situation)) break;
        }
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            captures = removeCheckMoves(captures, situation);
        }
        Move[] ret = new Move[captures.size()];
        ret = captures.toArray(ret);
        return ret;
    }

    /**
     * Create a new Rook by promotion
     *
     * @param pawn the pawn to promote
     * @param game the game
     * @return a new rook
     */
    public static Rook promotion(Pawn pawn, Game game) {
        Rook l = null;
        if ((pawn.posy == 0 && !pawn.black) || (pawn.posy == 7 && pawn.black)) {
            l = new Rook(pawn.black, pawn.posx, pawn.posy, game);
        } else {
            throw new IllegalArgumentException("The pawn is currently not promotable");
        }
        l.positioninarray = pawn.positioninarray;
        return l;
    }

    @Override
    public boolean doMove(int targetX, int targetY) {
        boolean b;
        b = super.doMove(targetX, targetY);
        if (b == true) {
            moved = true;
        }
        return b;
    }

    @Override
    public boolean doCapture(int targetX, int targetY) {
        boolean b;
        b = super.doCapture(targetX, targetY);
        if (b == true) {
            moved = true;
        }
        return b;
    }

    @Override
    public Rook clone() {
        Rook t = new Rook(black, posx, posy, game);
        t.captured = captured;
        t.moved = moved;
        t.positioninarray = positioninarray;
        return t;
    }
}
