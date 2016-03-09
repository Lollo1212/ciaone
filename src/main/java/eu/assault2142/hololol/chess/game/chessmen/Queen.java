package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.GameState;
import java.util.LinkedList;

/**
 *
 * @author hololol2
 */
public class Queen extends Chessman {

    /**
     * Creates a new Queen
     *
     * @param black whether this chessman is black or not
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param game the gamesituation
     */
    private Queen(boolean black, int posx, int posy, GameState game) {
        super(black, posx, posy, game);
        image = game.getGame().getImage(NAMES.QUEEN, black);
        value = 10;
    }

    /**
     * Creates a new Queen
     *
     * @param black whether this chessman is black or not
     * @param game the gamesituation
     * @param numberinarray the number in the chessmen-array
     * @return the queen
     */
    public static Queen createQueen(boolean black, GameState game, int numberinarray) {
        int a;
        int b;
        if (black == true) {
            a = 3;
            b = 0;
        } else {
            a = 3;
            b = 7;
        }
        Queen d = new Queen(black, a, b, game);
        d.positioninarray = numberinarray;
        return d;
    }

    /**
     * Create a new Queen by promotion
     *
     * @param pawn the pawn to promote
     * @param game the gamesituation
     * @return a new queen
     */
    public static Queen promotion(Pawn pawn, GameState game) {
        Queen l = null;
        if ((pawn.posy == 0 && !pawn.black) || (pawn.posy == 7 && pawn.black)) {
            l = new Queen(pawn.black, pawn.posx, pawn.posy, game);
        } else {
            throw new IllegalArgumentException("The pawn is currently not promotable");
        }
        l.positioninarray = pawn.positioninarray;
        return l;
    }

    @Override
    public Move[] computeMoves(boolean checkForCheck, GameState situation) {
        LinkedList<Move> moves = new LinkedList();
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(moves, posx + c, posy + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(moves, posx + c, posy - c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(moves, posx - c, posy + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(moves, posx - c, posy - c, situation)) {
                break;
            }
        }
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
        Move[] ret = new Move[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }

    @Override
    public Move[] computeCaptures(boolean checkForCheck, GameState situation) {
        LinkedList<Move> captures = new LinkedList();
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(captures, posx + c, posy + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(captures, posx + c, posy - c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(captures, posx - c, posy + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(captures, posx - c, posy - c, situation)) {
                break;
            }
        }
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
        Move[] ret = new Move[captures.size()];
        ret = captures.toArray(ret);
        return ret;
    }

    @Override
    public Queen clone() {
        Queen d = new Queen(black, posx, posy, gamesituation);
        d.captured = captured;
        d.moved = moved;
        d.positioninarray = positioninarray;
        return d;
    }
}
