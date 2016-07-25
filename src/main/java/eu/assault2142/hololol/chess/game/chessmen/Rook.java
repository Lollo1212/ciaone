package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.Game;
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
     * @param number the number of the bishop (0 for the left, 1 for the right)
     * @param game the gamestate
     * @param numberinarray the number in the chessmen-array
     * @return the rook
     */
    public static Rook createRook(boolean black, int number, Game game, int numberinarray) {
        if (number < 0 || number > 1) {
            throw new IllegalArgumentException("The given number is incorrect");
        }
        int posY;
        if (black == true) {
            posY = 0;
        } else {
            posY = 7;
        }
        Rook t = new Rook(black, number * 7, posY, game);
        t.positioninarray = numberinarray;
        return t;
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
        if ((pawn.posY == 0 && !pawn.black) || (pawn.posY == 7 && pawn.black)) {
            l = new Rook(pawn.black, pawn.posX, pawn.posY, game);
        } else {
            throw new IllegalArgumentException("The pawn is currently not promotable");
        }
        l.positioninarray = pawn.positioninarray;
        return l;
    }

    /**
     * Creates a new Rook
     *
     * @param black whether this chessman is black or not
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param game the gamestate
     */
    private Rook(boolean black, int posx, int posy, Game game) {
        super(black, posx, posy, game, 5);
        type = NAMES.ROOK;
    }

    @Override
    public Rook clone() {
        Rook t = new Rook(black, posX, posY, game);
        t.captured = captured;
        t.moved = moved;
        t.positioninarray = positioninarray;
        return t;
    }

    @Override
    public List<Move> computeCaptures(boolean considerCheck, GameState gamestate) {
        LinkedList<Move> possibleCaptures = new LinkedList();
        boolean right = true, left = true, up = true, down = true;
        for (int step = 1; step <= 7; step++) {
            if (right && !addIfCapturePossible(possibleCaptures, posX + step, posY, gamestate)) {
                right = false;
            }
            if (left && !addIfCapturePossible(possibleCaptures, posX - step, posY, gamestate)) {
                left = false;
            }
            if (up && !addIfCapturePossible(possibleCaptures, posX, posY - step, gamestate)) {
                up = false;
            }
            if (down && !addIfCapturePossible(possibleCaptures, posX, posY + step, gamestate)) {
                down = false;
            }
        }
        if (considerCheck) {
            possibleCaptures = removeCheckMoves(possibleCaptures, gamestate);
        }
        return possibleCaptures;
    }

    @Override
    public List<Move> computeMoves(boolean considerCheck, GameState gamestate) {
        LinkedList<Move> possibleMoves = new LinkedList();
        boolean right = true, left = true, up = true, down = true;
        for (int step = 1; step <= 7; step++) {
            if (right && !addIfMovePossible(possibleMoves, posX + step, posY, gamestate)) {
                right = false;
            }
            if (left && !addIfMovePossible(possibleMoves, posX - step, posY, gamestate)) {
                left = false;
            }
            if (up && !addIfMovePossible(possibleMoves, posX, posY - step, gamestate)) {
                up = false;
            }
            if (down && !addIfMovePossible(possibleMoves, posX, posY + step, gamestate)) {
                down = false;
            }
        }
        if (considerCheck) {
            possibleMoves = removeCheckMoves(possibleMoves, gamestate);
        }
        return possibleMoves;
    }
}
