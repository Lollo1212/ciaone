package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.Game;
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
     * @param number the number of the bishop (0 for the left, 1 for the right)
     * @param game the game
     * @param numberinarray the number in the chessmen-array
     * @return the knight
     */
    public static Knight createKnight(boolean black, int number, Game game, int numberinarray) {
        int posY;
        if (black == true) {
            posY = 0;
        } else {
            posY = 7;
        }
        Knight knight;
        switch (number) {
            case 0:
                knight = new Knight(black, 1, posY, game);
                break;
            case 1:
                knight = new Knight(black, 6, posY, game);
                break;
            default:
                throw new IllegalArgumentException("The given number is incorrect");
        }
        knight.positioninarray = numberinarray;
        return knight;
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
        if ((pawn.posY == 0 && !pawn.black) || (pawn.posY == 7 && pawn.black)) {
            l = new Knight(pawn.black, pawn.posX, pawn.posY, game);
        } else {
            throw new IllegalArgumentException("The pawn is currently not promotable");
        }
        l.positioninarray = pawn.positioninarray;
        return l;
    }

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
        type = NAMES.KNIGHT;
    }

    @Override
    public Knight clone() {
        Knight s = new Knight(black, posX, posY, game);
        s.captured = captured;
        s.moved = moved;
        s.positioninarray = positioninarray;
        return s;
    }

    @Override
    public List<Move> computeCaptures(boolean checkForChecks, GameState situation) {
        LinkedList<Move> possibleCaptures = new LinkedList();
        addIfCapturePossible(possibleCaptures, posX - 2, posY + 1, situation);
        addIfCapturePossible(possibleCaptures, posX - 2, posY - 1, situation);
        addIfCapturePossible(possibleCaptures, posX + 2, posY + 1, situation);
        addIfCapturePossible(possibleCaptures, posX + 2, posY - 1, situation);
        addIfCapturePossible(possibleCaptures, posX + 1, posY + 2, situation);
        addIfCapturePossible(possibleCaptures, posX - 1, posY + 2, situation);
        addIfCapturePossible(possibleCaptures, posX + 1, posY - 2, situation);
        addIfCapturePossible(possibleCaptures, posX - 1, posY - 2, situation);

        if (checkForChecks) {
            possibleCaptures = removeCheckMoves(possibleCaptures, situation);
        }
        return possibleCaptures;
    }

    @Override
    public List<Move> computeMoves(boolean checkForCheck, GameState situation) {
        LinkedList<Move> possibleMoves = new LinkedList();
        addIfMovePossible(possibleMoves, posX - 2, posY + 1, situation);
        addIfMovePossible(possibleMoves, posX - 2, posY - 1, situation);
        addIfMovePossible(possibleMoves, posX + 2, posY + 1, situation);
        addIfMovePossible(possibleMoves, posX + 2, posY - 1, situation);
        addIfMovePossible(possibleMoves, posX + 1, posY + 2, situation);
        addIfMovePossible(possibleMoves, posX - 1, posY + 2, situation);
        addIfMovePossible(possibleMoves, posX + 1, posY - 2, situation);
        addIfMovePossible(possibleMoves, posX - 1, posY - 2, situation);

        if (checkForCheck) {
            possibleMoves = removeCheckMoves(possibleMoves, situation);
        }
        return possibleMoves;
    }
}
