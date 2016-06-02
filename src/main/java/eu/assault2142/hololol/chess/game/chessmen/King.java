package eu.assault2142.hololol.chess.game.chessmen;

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
     * @param game the gamesituation
     * @param numberinarray the number in the chessmen-array
     * @return the king
     */
    public static King createKing(boolean black, GameState game, int numberinarray) {
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
     * @param game the gamesituation
     */
    private King(boolean black, int posx, int posy, GameState game) {
        super(black, posx, posy, game);
        type = NAMES.KING;
        value = 500;
    }

    @Override
    public King clone() {
        King k = new King(black, posx, posy, gamesituation);
        k.captured = captured;
        k.moved = moved;
        k.positioninarray = positioninarray;
        return k;
    }

    @Override
    public List<Movement> computeCaptures(boolean checkForChecks, GameState situation) {
        LinkedList<Movement> captures = new LinkedList();
        for (int k = -1; k <= 1; k++) {
            for (int j = -1; j <= 1; j++) {
                addIfCapturePossible(captures, posx + k, posy + j, situation);
            }
        }
        //Überprüfen auf Schach-Position
        if (checkForChecks) {
            captures = removeCheckMoves(captures, situation);
        }
        return captures;
    }

    /**
     * Compute the castlings this king is allowed to do
     *
     * @param checkForCheck whether to remove castlings which lead to a
     * check-situation
     * @param situation
     * @return an array of possible castlings
     */
    public List<CastlingMove> computeCastlings(boolean checkForCheck, GameState situation) {
        LinkedList<CastlingMove> castlings = new LinkedList();
        Chessman rook1 = situation.getChessman(black, 8);
        Chessman rook2 = situation.getChessman(black, 8);
        if (black == true) {
            if (!moved && !rook1.captured && !rook1.moved && !situation.getSquare(1, 0).isOccupied() && !situation.getSquare(2, 0).isOccupied() && !situation.getSquare(3, 0).isOccupied()) {
                castlings.add(new CastlingMove(2, 0, (Rook) rook1, 3, 0, this));
            }
            if (!moved && !rook2.captured && !rook2.moved && !situation.getSquare(5, 0).isOccupied() && !situation.getSquare(6, 0).isOccupied()) {
                castlings.add(new CastlingMove(6, 0, (Rook) gamesituation.getChessman(true, 9), 5, 0, this));
            }
        } else {
            if (!moved && !rook1.captured && !rook1.moved && !situation.getSquare(1, 7).isOccupied() && !situation.getSquare(2, 7).isOccupied() && !situation.getSquare(3, 7).isOccupied()) {
                castlings.add(new CastlingMove(2, 7, (Rook) gamesituation.getChessman(false, 8), 3, 7, this));
            }
            if (!moved && !rook2.captured && !rook2.moved && !situation.getSquare(5, 7).isOccupied() && !situation.getSquare(6, 7).isOccupied()) {
                castlings.add(new CastlingMove(6, 7, (Rook) gamesituation.getChessman(false, 9), 5, 7, this));
            }
        }
        //Überprüfen ob legal
        if (checkForCheck) {
            GameState gsneu;
            List<Movement> schläge;
            for (int a = 0; a < castlings.size(); a++) {
                CastlingMove move = castlings.get(a);
                int kx = move.targetX;
                if (kx < posx) {
                    for (int b = posx; b >= kx; b--) {
                        gsneu = gamesituation.emulateMove(this, b, posy);
                        schläge = gsneu.computeAllCaptures(!black);
                        if (gsneu.dangerForKing(black)) {
                            castlings.remove(a);
                        }
                    }
                } else {
                    for (int b = posx; b < kx; b++) {
                        gsneu = gamesituation.emulateMove(this, b, posy);
                        schläge = gsneu.computeAllCaptures(!black);
                        if (gsneu.dangerForKing(black)) {
                            castlings.remove(a);
                        }
                    }
                }
            }
        }
        return castlings;
    }

    @Override
    public List<Movement> computeMoves(boolean checkForCheck, GameState situation) {
        LinkedList<Movement> moves = new LinkedList();
        for (int k = -1; k <= 1; k++) {
            for (int j = -1; j <= 1; j++) {
                addIfMovePossible(moves, posx + k, posy + j, situation);
            }
        }
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            moves = removeCheckMoves(moves, situation);

        }
        return moves;
    }

    @Override
    public boolean doCapture(int targetX, int targetY) {
        boolean b;
        b = super.doCapture(targetX, targetY);
        if (b == true) {
            moved = true;
        }
        return b;
    }

    /**
     * Do a castling with this chessman
     *
     * @param move the castling-move to execute
     * @param situation
     * @return true if the capture was successful, false otherwise
     */
    public boolean doCastling(CastlingMove move, GameState situation) {
        if (situation.getTurn() == black) {
            List<CastlingMove> rochaden = computeCastlings(true, situation);
            Optional<CastlingMove> opt = rochaden.stream().filter((CastlingMove castl) -> {
                return castl.equals(move);
            }).findFirst();
            boolean ret = opt.isPresent();
            opt.ifPresent((CastlingMove castl) -> {
                executeCastling(castl, situation);
            });
            return ret;
        }
        return false;
    }

    @Override
    public boolean doMove(int targetX, int targetY) {
        boolean b;
        b = super.doMove(targetX, targetY);
        if (b == true) {
            moved = true;
        }
        return b;
    }

    /**
     * Get all allowed castlings
     *
     * @return a list of the allowed castlings
     */
    public List<CastlingMove> getCastlings() {
        return castlings;
    }

    /**
     * Execute the given castling-move. Does not perform any checks if possible
     *
     * @param move the castling to execute
     * @param situation the current gamestate
     */
    private void executeCastling(CastlingMove move, GameState situation) {
        //könig ziehen
        situation.getSquare(posx, posy).occupier = null;
        situation.getSquare(move.targetX, move.targetY).occupier = this;
        situation.getSquare(move.rook.posx, move.rook.posy).occupier = null;
        situation.getSquare(move.rookX, move.rookY).occupier = move.rook;
        //Turm ziehen
        posx = move.targetX;
        posy = move.targetY;
        move.rook.posx = move.rookX;
        move.rook.posy = move.rookY;
        gamesituation.nextTurn(this);
    }

    @Override
    public void updateMovements() {
        super.updateMovements();
        castlings = computeCastlings(true, gamesituation);
    }

}
