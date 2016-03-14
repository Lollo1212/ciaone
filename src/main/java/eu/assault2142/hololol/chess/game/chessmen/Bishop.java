package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.GameState;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Bishop in the chess-game
 *
 * @author hololol2
 */
public class Bishop extends Chessman {

    /**
     * Creates a new Bishop
     *
     * @param black whether this chessman is black or not
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param game the gamesituation
     */
    private Bishop(boolean black, int posx, int posy, GameState game) {
        super(black, posx, posy, game);
        image = game.getGame().getImage(NAMES.BISHOP, black);
        value = 3;
    }

    /**
     * Creates a new Bishop
     *
     * @param black whether this chessman is black or not
     * @param number the number of the bishop (0 for the left, 1 for the right)
     * @param game the gamesituation
     * @param numberinarray the number in the chessmen-array
     * @return the bishop
     */
    public static Bishop createBishop(boolean black, int number, GameState game, int numberinarray) {
        int a;
        int b;
        if (black == true) {
            a = 0;
        } else {
            a = 7;
        }
        Bishop l;
        switch (number) {
            case 0:
                b = 2;
                l = new Bishop(black, b, a, game);
                break;
            case 1:
                b = 5;
                l = new Bishop(black, b, a, game);
                break;
            default:
                throw new IllegalArgumentException("The given number is incorrect");
        }
        l.positioninarray = numberinarray;
        return l;
    }

    @Override
    public List<Movement> computeMoves(boolean checkForCheck, GameState situation) {
        LinkedList<Movement> moves = new LinkedList();
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
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            moves = removeCheckMoves(moves, situation);
        }
        return moves;
    }

    @Override
    public List<Movement> computeCaptures(boolean checkForChecks, GameState situation) {
        LinkedList<Movement> captures = new LinkedList();
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

        //Überprüfen auf Schach-Position
        if (checkForChecks) {
            captures = removeCheckMoves(captures, situation);
        }
        return captures;
    }

    /**
     * Create a new Bishop by promotion
     *
     * @param pawn the pawn to promote
     * @param game the gamesituation
     * @return a new bishop
     */
    public static Bishop promotion(Pawn pawn, GameState game) {
        Bishop l = null;
        if ((pawn.posy == 0 && !pawn.black) || (pawn.posy == 7 && pawn.black)) {
            l = new Bishop(pawn.black, pawn.posx, pawn.posy, game);
        } else {
            throw new IllegalArgumentException("The pawn is currently not promotable");
        }
        l.positioninarray = pawn.positioninarray;
        return l;
    }

    @Override
    public Bishop clone() {
        Bishop l = new Bishop(black, posx, posy, gamesituation);
        l.captured = captured;
        l.moved = moved;
        l.positioninarray = positioninarray;
        return l;
    }
}
