package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.GameState;
import java.util.LinkedList;
import java.util.List;

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
     * @param game the gamestate
     */
    private Knight(boolean black, int posx, int posy, GameState game) {
        super(black, posx, posy, game);
        type = NAMES.KNIGHT;
        value = 8;
    }

    /**
     * Creates a new Knight
     *
     * @param black whether this chessman is black or not
     * @param number the number of the bishop (0 for the left, 1 for the right)
     * @param game the gamestate
     * @param numberinarray the number in the chessmen-array
     * @return the knight
     */
    public static Knight createKnight(boolean black, int number, GameState game, int numberinarray) {
        int a;
        int b;
        if (black == true) {
            a = 0;
        } else {
            a = 7;
        }
        Knight s = null;
        switch (number) {
            case 0:
                b = 1;
                s = new Knight(black, b, a, game);
                break;
            case 1:
                b = 6;
                s = new Knight(black, b, a, game);
                break;
            default:
                throw new IllegalArgumentException("The given number is incorrect");
        }
        s.positioninarray = numberinarray;
        return s;
    }

    @Override
    public List<Move> computeMoves(boolean checkForCheck, GameState situation) {
        LinkedList<Move> moves = new LinkedList();
        addIfMovePossible(moves, posX - 2, posY + 1, situation);
        addIfMovePossible(moves, posX - 2, posY - 1, situation);
        addIfMovePossible(moves, posX + 2, posY + 1, situation);
        addIfMovePossible(moves, posX + 2, posY - 1, situation);
        addIfMovePossible(moves, posX + 1, posY + 2, situation);
        addIfMovePossible(moves, posX - 1, posY + 2, situation);
        addIfMovePossible(moves, posX + 1, posY - 2, situation);
        addIfMovePossible(moves, posX - 1, posY - 2, situation);
        if (checkForCheck) {
            moves = removeCheckMoves(moves, situation);
        }
        return moves;
    }

    @Override
    public List<Move> computeCaptures(boolean checkForChecks, GameState situation) {
        LinkedList<Move> captures = new LinkedList();
        addIfCapturePossible(captures, posX - 2, posY + 1, situation);
        addIfCapturePossible(captures, posX - 2, posY - 1, situation);
        addIfCapturePossible(captures, posX + 2, posY + 1, situation);
        addIfCapturePossible(captures, posX + 2, posY - 1, situation);
        addIfCapturePossible(captures, posX + 1, posY + 2, situation);
        addIfCapturePossible(captures, posX - 1, posY + 2, situation);
        addIfCapturePossible(captures, posX + 1, posY - 2, situation);
        addIfCapturePossible(captures, posX - 1, posY - 2, situation);

        //Überprüfen auf Schach-Position
        if (checkForChecks) {
            captures = removeCheckMoves(captures, situation);
        }
        return captures;
    }

    /**
     * Create a new Knight by promotion
     *
     * @param pawn the pawn to promote
     * @param game the gamestate
     * @return a new knight
     */
    public static Knight promotion(Pawn pawn, GameState game) {
        Knight l = null;
        if ((pawn.posY == 0 && !pawn.black) || (pawn.posY == 7 && pawn.black)) {
            l = new Knight(pawn.black, pawn.posX, pawn.posY, game);
        } else {
            throw new IllegalArgumentException("The pawn is currently not promotable");
        }
        l.positioninarray = pawn.positioninarray;
        return l;
    }

    @Override
    public Knight clone() {
        Knight s = new Knight(black, posX, posY, gamestate);
        s.captured = captured;
        s.moved = moved;
        s.positioninarray = positioninarray;
        return s;
    }
}
