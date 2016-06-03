package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.GameState;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author hololol2
 */
public class King extends Chessman {

    private List<CastlingMove> castlings;

    /**
     * Creates a new King
     *
     * @param black whether this chessman is black or not
     * @param game the game
     * @param numberinarray the number in the chessmen-array
     * @return the king
     */
    public static King createKing(boolean black, Game game, int numberinarray) {
        int a;
        int b;
        if (black == true) {
            a = 4;
            b = 0;
        } else {
            a = 4;
            b = 7;
        }
        King k = new King(black, a, b, game);
        k.positioninarray = numberinarray;
        return k;
    }

    /**
     * Creates a new King
     *
     * @param black whether this chessman is black or not
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param game the game
     */
    private King(boolean black, int posx, int posy, Game game) {
        super(black, posx, posy, game);
        type = NAMES.KING;
    }

    @Override
    public King clone() {
        King k = new King(black, posX, posY, game);
        k.captured = captured;
        k.moved = moved;
        k.positioninarray = positioninarray;
        return k;
    }

    @Override
    public List<Move> computeCaptures(boolean checkForChecks, GameState situation) {
        LinkedList<Move> possibleCaptures = new LinkedList();
        for (int k = -1; k <= 1; k++) {
            for (int j = -1; j <= 1; j++) {
                addIfCapturePossible(possibleCaptures, posX + k, posY + j, situation);
            }
        }
        if (checkForChecks) {
            possibleCaptures = removeCheckMoves(possibleCaptures, situation);
        }
        return possibleCaptures;
    }

    /**
     * Compute the castlings this king is allowed to do
     *
     * @param checkForCheck whether to remove castlings which lead to a
     * check-situation
     * @param gamestate the game-state
     * @return an array of possible castlings
     */
    public List<CastlingMove> computeCastlings(boolean checkForCheck, GameState gamestate) {
        LinkedList<CastlingMove> possibleCastlings = new LinkedList();
        Chessman rook1 = gamestate.getChessman(black, 8);
        Chessman rook2 = gamestate.getChessman(black, 8);
        if (black == true) {
            if (!moved && !rook1.captured && !rook1.moved && !gamestate.getSquare(1, 0).isOccupied() && !gamestate.getSquare(2, 0).isOccupied() && !gamestate.getSquare(3, 0).isOccupied()) {
                possibleCastlings.add(new CastlingMove(2, 0, (Rook) rook1, 3, 0, this));
            }
            if (!moved && !rook2.captured && !rook2.moved && !gamestate.getSquare(5, 0).isOccupied() && !gamestate.getSquare(6, 0).isOccupied()) {
                possibleCastlings.add(new CastlingMove(6, 0, (Rook) gamestate.getChessman(true, 9), 5, 0, this));
            }
        } else {
            if (!moved && !rook1.captured && !rook1.moved && !gamestate.getSquare(1, 7).isOccupied() && !gamestate.getSquare(2, 7).isOccupied() && !gamestate.getSquare(3, 7).isOccupied()) {
                possibleCastlings.add(new CastlingMove(2, 7, (Rook) gamestate.getChessman(false, 8), 3, 7, this));
            }
            if (!moved && !rook2.captured && !rook2.moved && !gamestate.getSquare(5, 7).isOccupied() && !gamestate.getSquare(6, 7).isOccupied()) {
                possibleCastlings.add(new CastlingMove(6, 7, (Rook) gamestate.getChessman(false, 9), 5, 7, this));
            }
        }
        if (checkForCheck) {
            GameState gsneu;
            List<Move> enemyCaptures;
            for (int a = 0; a < possibleCastlings.size(); a++) {
                CastlingMove move = possibleCastlings.get(a);
                int kx = move.targetX;
                if (kx < posX) {
                    for (int b = posX; b >= kx; b--) {
                        gsneu = gamestate.emulateMove(this, b, posY);
                        enemyCaptures = gsneu.computeAllCaptures(!black);
                        if (gsneu.dangerForKing(black)) {
                            possibleCastlings.remove(a);
                        }
                    }
                } else {
                    for (int b = posX; b < kx; b++) {
                        gsneu = gamestate.emulateMove(this, b, posY);
                        enemyCaptures = gsneu.computeAllCaptures(!black);
                        if (gsneu.dangerForKing(black)) {
                            possibleCastlings.remove(a);
                        }
                    }
                }
            }
        }
        return possibleCastlings;
    }

    @Override
    public List<Move> computeMoves(boolean checkForCheck, GameState gamestate) {
        LinkedList<Move> possibleMoves = new LinkedList();
        for (int k = -1; k <= 1; k++) {
            for (int j = -1; j <= 1; j++) {
                addIfMovePossible(possibleMoves, posX + k, posY + j, gamestate);
            }
        }
        if (checkForCheck) {
            possibleMoves = removeCheckMoves(possibleMoves, gamestate);

        }
        return possibleMoves;
    }

    /**
     * Do a castling with this chessman
     *
     * @param move the castling-move to execute
     * @param gamestate the game-state
     * @return true if the capture was successful, false otherwise
     */
    public boolean doCastling(CastlingMove move, GameState gamestate) {
        if (gamestate.getTurn() == black) {
            List<CastlingMove> rochaden = computeCastlings(true, gamestate);
            Optional<CastlingMove> opt = rochaden.stream().filter((CastlingMove castl) -> {
                return castl.equals(move);
            }).findFirst();
            boolean ret = opt.isPresent();
            opt.ifPresent((CastlingMove castl) -> {
                executeCastling(castl, gamestate);
            });
            return ret;
        }
        return false;
    }

    /**
     * Get all allowed castlings
     *
     * @return a list of the allowed castlings
     */
    public List<CastlingMove> getCastlings() {
        return castlings;
    }

    @Override
    public void updateMovements() {
        super.updateMovements();
        castlings = computeCastlings(true, gamestate);
    }

    /**
     * Execute the given castling-move. Does not perform any checks if possible
     *
     * @param move the castling to execute
     * @param gamestate the current gamestate
     */
    private void executeCastling(CastlingMove move, GameState gamestate) {

        gamestate.getSquare(posX, posY).occupier = null;
        gamestate.getSquare(move.targetX, move.targetY).occupier = this;
        gamestate.getSquare(move.getRook().posX, move.getRook().posY).occupier = null;
        gamestate.getSquare(move.getRookX(), move.getRookY()).occupier = move.getRook();

        posX = move.targetX;
        posY = move.targetY;
        move.getRook().posX = move.getRookX();
        move.getRook().posY = move.getRookY();

        gamestate.nextTurn(this);
    }

}
