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
     * @param number the number of the bishop (0 for the left, 1 for the right)
     * @param game the gamestate
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

    /**
     * Create a new Bishop by promotion
     *
     * @param pawn the pawn to promote
     * @param game the gamestate
     * @return a new bishop
     */
    public static Bishop promotion(Pawn pawn, GameState game) {
        Bishop l = null;
        if ((pawn.posY == 0 && !pawn.black) || (pawn.posY == 7 && pawn.black)) {
            l = new Bishop(pawn.black, pawn.posX, pawn.posY, game);
        } else {
            throw new IllegalArgumentException("The pawn is currently not promotable");
        }
        l.positioninarray = pawn.positioninarray;
        return l;
    }

    /**
     * Creates a new Bishop
     *
     * @param black whether this chessman is black or not
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param game the gamestate
     */
    private Bishop(boolean black, int posx, int posy, GameState game) {
        super(black, posx, posy, game);
        type = NAMES.BISHOP;
        value = 3;
    }

    @Override
    public Bishop clone() {
        Bishop l = new Bishop(black, posX, posY, gamestate);
        l.captured = captured;
        l.moved = moved;
        l.positioninarray = positioninarray;
        return l;
    }

    @Override
    public List<Move> computeCaptures(boolean checkForChecks, GameState situation) {
        LinkedList<Move> possibelCaptures = new LinkedList();
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(possibelCaptures, posX + c, posY + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(possibelCaptures, posX + c, posY - c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(possibelCaptures, posX - c, posY + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(possibelCaptures, posX - c, posY - c, situation)) {
                break;
            }
        }
        if (checkForChecks) {
            possibelCaptures = removeCheckMoves(possibelCaptures, situation);
        }
        return possibelCaptures;
    }

    @Override
    public List<Move> computeMoves(boolean checkForCheck, GameState situation) {
        LinkedList<Move> possibleMoves = new LinkedList();
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(possibleMoves, posX + c, posY + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(possibleMoves, posX + c, posY - c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(possibleMoves, posX - c, posY + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(possibleMoves, posX - c, posY - c, situation)) {
                break;
            }
        }
        if (checkForCheck) {
            possibleMoves = removeCheckMoves(possibleMoves, situation);
        }
        return possibleMoves;
    }
}
