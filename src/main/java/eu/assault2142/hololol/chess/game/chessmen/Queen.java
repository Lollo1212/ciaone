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
        int posY;
        if (black) {
            posY = 0;
        } else {
            posY = 7;
        }
        Queen d = new Queen(black, 3, posY, game);
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
    public Queen clone() {
        Queen d = new Queen(black, posX, posY, game);
        d.captured = captured;
        d.moved = moved;
        d.positioninarray = positioninarray;
        return d;
    }
}
