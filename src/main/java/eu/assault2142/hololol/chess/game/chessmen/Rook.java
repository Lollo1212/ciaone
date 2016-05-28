package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.GameState;
import java.util.LinkedList;
import java.util.List;

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
     * @param game the gamesituation
     */
    private Rook(boolean black, int posx, int posy, GameState game) {
        super(black, posx, posy, game);
        type = NAMES.ROOK;
        value = 5;
    }

    /**
     * Creates a new Rook
     *
     * @param black whether this chessman is black or not
     * @param number the number of the bishop (0 for the left, 1 for the right)
     * @param game the gamesituation
     * @param numberinarray the number in the chessmen-array
     * @return the rook
     */
    public static Rook createTurm(boolean black, int number, GameState game, int numberinarray) {
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
    public List<Movement> computeMoves(boolean checkForCheck, GameState situation) {
        LinkedList<Movement> moves = new LinkedList();
        for (int u = posx - 1; u >= 0; u--) {
            if (!addIfMovePossible(moves, u, posy, situation)) {
                break;
            }
        }
        for (int u = posx + 1; u <= 7; u++) {
            if (!addIfMovePossible(moves, u, posy, situation)) {
                break;
            }
        }
        for (int u = posy - 1; u >= 0; u--) {
            if (!addIfMovePossible(moves, posx, u, situation)) {
                break;
            }
        }
        for (int u = posy + 1; u <= 7; u++) {
            if (!addIfMovePossible(moves, posx, u, situation)) {
                break;
            }
        }
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            moves = removeCheckMoves(moves, situation);
        }
        return moves;
    }

    @Override
    public List<Movement> computeCaptures(boolean checkForCheck, GameState situation) {
        LinkedList<Movement> captures = new LinkedList();
        for (int u = posx - 1; u >= 0; u--) {
            if (!addIfCapturePossible(captures, u, posy, situation)) {
                break;
            }
        }
        for (int u = posx + 1; u <= 7; u++) {
            if (!addIfCapturePossible(captures, u, posy, situation)) {
                break;
            }
        }
        for (int u = posy - 1; u >= 0; u--) {
            if (!addIfCapturePossible(captures, posx, u, situation)) {
                break;
            }
        }
        for (int u = posy + 1; u <= 7; u++) {
            if (!addIfCapturePossible(captures, posx, u, situation)) {
                break;
            }
        }
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            captures = removeCheckMoves(captures, situation);
        }
        return captures;
    }

    /**
     * Create a new Rook by promotion
     *
     * @param pawn the pawn to promote
     * @param game the gamesituation
     * @return a new rook
     */
    public static Rook promotion(Pawn pawn, GameState game) {
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
        Rook t = new Rook(black, posx, posy, gamesituation);
        t.captured = captured;
        t.moved = moved;
        t.positioninarray = positioninarray;
        return t;
    }
}
