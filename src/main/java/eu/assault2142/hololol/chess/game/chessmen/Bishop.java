package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.Game;
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
     * @param game the game
     * @param numberinarray the number in the chessmen-array
     * @return the bishop
     */
    public static Bishop createBishop(boolean black, int number, Game game, int numberinarray) {
        if (number < 0 || number > 1) {
            throw new IllegalArgumentException("The given number is incorrect");
        }
        int posY;
        if (black) {
            posY = 0;
        } else {
            posY = 7;
        }
        Bishop bishop = new Bishop(black, 2 + number * 3, posY, game);
        bishop.positioninarray = numberinarray;
        return bishop;
    }

    /**
     * Create a new Bishop by promotion
     *
     * @param pawn the pawn to promote
     * @param game the game
     * @return a new bishop
     */
    public static Bishop promotion(Pawn pawn, Game game) {
        Bishop bishop;
        if ((pawn.posY == 0 && !pawn.black) || (pawn.posY == 7 && pawn.black)) {
            bishop = new Bishop(pawn.black, pawn.posX, pawn.posY, game);
        } else {
            throw new IllegalArgumentException("The pawn is currently not promotable");
        }
        bishop.positioninarray = pawn.positioninarray;
        return bishop;
    }

    /**
     * Creates a new Bishop
     *
     * @param black whether this chessman is black or not
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param game the game
     */
    private Bishop(boolean black, int posx, int posy, Game game) {
        super(black, posx, posy, game, 3);
        type = NAMES.BISHOP;
    }

    @Override
    public Bishop clone() {
        Bishop l = new Bishop(black, posX, posY, game);
        l.captured = captured;
        l.moved = moved;
        l.positioninarray = positioninarray;
        return l;
    }

    @Override
    public List<Move> computeCaptures(boolean considerCheck, GameState gamestate) {
        LinkedList<Move> possibleCaptures = new LinkedList();
        boolean upright = true, downright = true, downleft = true, upleft = true;
        for (int step = 1; step <= 6; step++) {
            if (downright && !addIfCapturePossible(possibleCaptures, posX + step, posY + step, gamestate)) {
                downright = false;
            }
            if (upright && !addIfCapturePossible(possibleCaptures, posX + step, posY - step, gamestate)) {
                upright = false;
            }
            if (downleft && !addIfCapturePossible(possibleCaptures, posX - step, posY + step, gamestate)) {
                downleft = false;
            }
            if (upleft && !addIfCapturePossible(possibleCaptures, posX - step, posY - step, gamestate)) {
                upleft = false;
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
        boolean upright = true, downright = true, downleft = true, upleft = true;
        for (int step = 1; step <= 6; step++) {
            if (downright && !addIfMovePossible(possibleMoves, posX + step, posY + step, gamestate)) {
                downright = false;
            }
            if (upright && !addIfMovePossible(possibleMoves, posX + step, posY - step, gamestate)) {
                upright = false;
            }
            if (downleft && !addIfMovePossible(possibleMoves, posX - step, posY + step, gamestate)) {
                downleft = false;
            }
            if (upleft && !addIfMovePossible(possibleMoves, posX - step, posY - step, gamestate)) {
                upleft = false;
            }
        }
        if (considerCheck) {
            possibleMoves = removeCheckMoves(possibleMoves, gamestate);
        }
        return possibleMoves;
    }
}
