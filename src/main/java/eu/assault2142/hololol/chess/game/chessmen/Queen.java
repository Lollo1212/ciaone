package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.GameState;
import java.util.LinkedList;
import java.util.List;

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
     * @param game the game
     */
    private Queen(boolean black, int posx, int posy, Game game) {
        super(black, posx, posy, game);
        type = NAMES.QUEEN;
    }

    /**
     * Creates a new Queen
     *
     * @param black whether this chessman is black or not
     * @param game the game
     * @param numberinarray the number in the chessmen-array
     * @return the queen
     */
    public static Queen createQueen(boolean black, Game game, int numberinarray) {
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
     * @param game the game
     * @return a new queen
     */
    public static Queen promotion(Pawn pawn, Game game) {
        Queen l = null;
        if ((pawn.posY == 0 && !pawn.black) || (pawn.posY == 7 && pawn.black)) {
            l = new Queen(pawn.black, pawn.posX, pawn.posY, game);
        } else {
            throw new IllegalArgumentException("The pawn is currently not promotable");
        }
        l.positioninarray = pawn.positioninarray;
        return l;
    }

    @Override
    public List<Move> computeMoves(boolean checkForCheck, GameState situation) {
        LinkedList<Move> moves = new LinkedList();
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(moves, posX + c, posY + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(moves, posX + c, posY - c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(moves, posX - c, posY + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfMovePossible(moves, posX - c, posY - c, situation)) {
                break;
            }
        }
        for (int u = posX - 1; u >= 0; u--) {
            if (!addIfMovePossible(moves, u, posY, situation)) {
                break;
            }
        }
        for (int u = posX + 1; u <= 7; u++) {
            if (!addIfMovePossible(moves, u, posY, situation)) {
                break;
            }
        }
        for (int u = posY - 1; u >= 0; u--) {
            if (!addIfMovePossible(moves, posX, u, situation)) {
                break;
            }
        }
        for (int u = posY + 1; u <= 7; u++) {
            if (!addIfMovePossible(moves, posX, u, situation)) {
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
    public List<Move> computeCaptures(boolean checkForCheck, GameState situation) {
        LinkedList<Move> captures = new LinkedList();
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(captures, posX + c, posY + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(captures, posX + c, posY - c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(captures, posX - c, posY + c, situation)) {
                break;
            }
        }
        for (int c = 1; c <= 6; c++) {
            if (!addIfCapturePossible(captures, posX - c, posY - c, situation)) {
                break;
            }
        }
        for (int u = posX - 1; u >= 0; u--) {
            if (!addIfCapturePossible(captures, u, posY, situation)) {
                break;
            }
        }
        for (int u = posX + 1; u <= 7; u++) {
            if (!addIfCapturePossible(captures, u, posY, situation)) {
                break;
            }
        }
        for (int u = posY - 1; u >= 0; u--) {
            if (!addIfCapturePossible(captures, posX, u, situation)) {
                break;
            }
        }
        for (int u = posY + 1; u <= 7; u++) {
            if (!addIfCapturePossible(captures, posX, u, situation)) {
                break;
            }
        }
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            captures = removeCheckMoves(captures, situation);
        }
        return captures;
    }

    @Override
    public Queen clone() {
        Queen d = new Queen(black, posX, posY, game);
        d.captured = captured;
        d.moved = moved;
        d.positioninarray = positioninarray;
        return d;
    }
}
