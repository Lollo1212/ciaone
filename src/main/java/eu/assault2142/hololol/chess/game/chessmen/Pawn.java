package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.game.Square;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author hololol2
 */
public class Pawn extends Chessman {

    /**
     * Creates a new Pawn
     *
     * @param black whether this chessman is black or not
     * @param number the number of the bishop (from 0 to 7; 0 for the left, 7
     * for the right)
     * @param game the game
     * @param numberinarray the number in the chessmen-array
     * @return the pawn
     */
    public static Pawn createPawn(boolean black, int number, Game game, int numberinarray) {
        if (number < 0 || number > 7) {
            throw new IllegalArgumentException("The given number is incorrect");
        }
        int posY;
        if (black == true) {
            posY = 1;
        } else {
            posY = 6;
        }
        Pawn pawn = new Pawn(black, number, posY, game);
        pawn.positioninarray = numberinarray;
        return pawn;
    }

    /**
     * Creates a new Pawn
     *
     * @param black whether this chessman is black or not
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param game the game
     */
    private Pawn(boolean black, int posx, int posy, Game game) {
        super(black, posx, posy, game);
        type = NAMES.PAWN;
    }

    @Override
    public Pawn clone() {
        Pawn b = new Pawn(black, posX, posY, game);
        b.captured = captured;
        b.moved = moved;
        b.positioninarray = positioninarray;
        return b;
    }

    @Override
    public List<Move> computeCaptures(boolean considerCheck, GameState situation) {
        LinkedList<Move> possibleCaptures = new LinkedList();
        int targetY = posY + (black ? 1 : -1);
        addIfCapturePossible(possibleCaptures, posX + 1, targetY, situation);
        addIfCapturePossible(possibleCaptures, posX - 1, targetY, situation);

        Chessman lastmoved = gamestate.getLastMoved();
        if (black) {
            if (lastmoved != null && !moved && lastmoved.getClass() == Pawn.class && lastmoved.posY == 4 && (lastmoved.posX == posX + 1 || lastmoved.posX == posX - 1)) {
                possibleCaptures.add(new EnPassantMove(lastmoved.posX, 5, this, (Pawn) lastmoved));
            }
        } else if (lastmoved != null && !moved && lastmoved.getClass() == Pawn.class && lastmoved.posY == 3 && (lastmoved.posX == posX + 1 || lastmoved.posX == posX - 1)) {
            possibleCaptures.add(new EnPassantMove(lastmoved.posX, 2, this, (Pawn) lastmoved));
        }

        if (considerCheck) {
            possibleCaptures = removeCheckMoves(possibleCaptures, situation);
        }
        return possibleCaptures;
    }

    @Override
    public List<Move> computeMoves(boolean checkForCheck, GameState gamestate) {
        LinkedList<Move> possibleCaptures = new LinkedList();

        if (black) {
            if (addIfMovePossible(possibleCaptures, posX, posY + 1, gamestate) && posY == 1) {
                addIfMovePossible(possibleCaptures, posX, posY + 2, gamestate);
            }
        } else if (addIfMovePossible(possibleCaptures, posX, posY - 1, gamestate) && posY == 6) {
            addIfMovePossible(possibleCaptures, posX, posY - 2, gamestate);
        }
        if (checkForCheck) {
            possibleCaptures = removeCheckMoves(possibleCaptures, gamestate);
        }
        return possibleCaptures;
    }

    @Override
    public boolean doCapture(int targetX, int targetY) {
        boolean successfull = super.doCapture(targetX, targetY);
        if (!successfull) {
            Square square = gamestate.getSquare(targetX, targetY + (black ? -1 : +1));
            if (gamestate.getTurn() == black) {
                List<Move> bewegungen = getMoves();
                if (square != null && bewegungen != null && square.isOccupiedByColor(!black)) {
                    Optional<Move> findFirst = bewegungen.stream().filter((Move move) -> {
                        return move.getClass() == EnPassantMove.class && targetX == move.getTargetX();
                    }).findFirst();
                    successfull = findFirst.isPresent();
                    findFirst.ifPresent((Move move) -> {
                        executeEnPassant((EnPassantMove) move);
                    });
                }
            }
        }
        if (successfull) {
            if ((black && targetY == 7) || (!black && targetY == 0)) {
                game.promotion(this);
            }
        }
        return successfull;
    }

    @Override
    public boolean doMove(int targetX, int targetY) {
        boolean successfull = super.doMove(targetX, targetY);
        if (successfull) {
            if ((black && targetY == 7) || (!black && targetY == 0)) {
                game.promotion(this);
            }
        }
        return successfull;
    }

    /**
     * Execute the given EnPassantMove
     *
     * @param move the move to execute
     */
    private void executeEnPassant(EnPassantMove move) {
        Pawn pawn = move.getEnemyPawn();
        pawn.captured = true;
        pawn.moveToEdgeZone();
        gamestate.getSquare(posX, posY).occupier = null;
        posX = move.targetX;
        posY = move.targetY;
        gamestate.getSquare(move.targetX, move.targetY).occupier = this;
        gamestate.nextTurn(this);
    }
}
