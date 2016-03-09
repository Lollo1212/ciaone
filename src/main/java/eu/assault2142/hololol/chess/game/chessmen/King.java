package eu.assault2142.hololol.chess.game.chessmen;

import eu.assault2142.hololol.chess.game.GameState;
import java.util.LinkedList;

/**
 *
 * @author hololol2
 */
public class King extends Chessman {

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
        image = game.getGame().getImage(NAMES.KING, black);
        value = 500;
    }

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

    @Override
    public Move[] computeMoves(boolean checkForCheck, GameState situation) {
        LinkedList<Move> moves = new LinkedList();
        for (int k = -1; k <= 1; k++) {
            for (int j = -1; j <= 1; j++) {
                addIfMovePossible(moves, posx + k, posy + j, situation);
            }
        }
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            moves = removeCheckMoves(moves, situation);

        }
        Move[] ret = new Move[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }

    @Override
    public Move[] computeCaptures(boolean checkForChecks, GameState situation) {
        LinkedList<Move> captures = new LinkedList();
        for (int k = -1; k <= 1; k++) {
            for (int j = -1; j <= 1; j++) {
                addIfCapturePossible(captures, posx + k, posy + j, situation);
            }
        }
        //Überprüfen auf Schach-Position
        if (checkForChecks) {
            captures = removeCheckMoves(captures, situation);
        }
        Move[] ret = new Move[captures.size()];
        ret = captures.toArray(ret);
        return ret;
    }

    /**
     * Return the castlings this king is allowed to do
     *
     * @param checkForCheck whether to remove castlings which lead to a
     * check-situation
     * @return an array of possible castlings
     */
    public CastlingMove[] computeCastlings(boolean checkForCheck, GameState situation) {
        LinkedList<CastlingMove> castlings = new LinkedList();
        if (black == true) {
            if (!moved && !gamesituation.getChessmen(true)[8].captured && !gamesituation.getChessmen(true)[8].moved && !situation.getSquare(1, 0).isOccupied() && !situation.getSquare(2, 0).isOccupied() && !situation.getSquare(3, 0).isOccupied()) {
                castlings.add(new CastlingMove(2, 0, (Rook) gamesituation.getChessmen(true)[8], 3, 0, this));
            }
            if (!moved && !gamesituation.getChessmen(true)[9].captured && !gamesituation.getChessmen(true)[9].moved && !situation.getSquare(5, 0).isOccupied() && !situation.getSquare(6, 0).isOccupied() && !situation.getSquare(7, 0).isOccupied()) {
                castlings.add(new CastlingMove(6, 0, (Rook) gamesituation.getChessmen(true)[9], 5, 0, this));
            }
        } else {
            if (!moved && !gamesituation.getChessmen(false)[8].captured && !gamesituation.getChessmen(false)[8].moved && !situation.getSquare(1, 7).isOccupied() && !situation.getSquare(2, 7).isOccupied() && !situation.getSquare(3, 7).isOccupied()) {
                castlings.add(new CastlingMove(2, 7, (Rook) gamesituation.getChessmen(false)[8], 3, 7, this));
            }
            if (!moved && !gamesituation.getChessmen(false)[9].captured && !gamesituation.getChessmen(false)[9].moved && !situation.getSquare(5, 7).isOccupied() && !situation.getSquare(6, 7).isOccupied() && !situation.getSquare(7, 7).isOccupied()) {
                castlings.add(new CastlingMove(6, 7, (Rook) gamesituation.getChessmen(false)[9], 5, 7, this));
            }
        }
        //Überprüfen ob legal
        if (checkForCheck) {
            GameState gsneu;
            Move schläge[];
            for (int a = 0; a < castlings.size(); a++) {
                CastlingMove move = castlings.get(a);
                int kx = move.targetX;
                int ky = move.targetY;
                if (kx < posx) {
                    for (int b = posx; b > kx; b--) {
                        gsneu = gamesituation.doMove(this, b, posy);
                        schläge = gsneu.computeAllCaptures(!black);
                        for (Move schläge1 : schläge) {
                            if (schläge1 != null && schläge1.targetX != b && schläge1.targetY != posy) {
                                castlings.remove(a);
                                break;
                            }
                        }
                    }
                } else {
                    for (int b = posx; b < kx; b++) {
                        gsneu = gamesituation.doMove(this, b, posy);
                        schläge = gsneu.computeAllCaptures(!black);
                        for (Move schläge1 : schläge) {
                            if (schläge1 != null && schläge1.targetX != b && schläge1.targetY != posy) {
                                castlings.remove(a);
                                break;
                            }
                        }
                    }
                }
            }
        }
        CastlingMove[] ret = new CastlingMove[castlings.size()];
        ret = castlings.toArray(ret);
        return ret;
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
     * Execute a castling with this chessman
     *
     * @param move the castling-move to execute
     * @param situation
     * @return true if the capture was successful, false otherwise
     */
    public boolean doCastling(CastlingMove move, GameState situation) {
        CastlingMove[] rochaden = computeCastlings(true, situation);
        if (rochaden.length == 0) {
            return false;
        }
        if (gamesituation.getTurn() == black) {
            if (rochaden[0] != null && rochaden[0].equals(move)) {
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
                return true;
            } else if (rochaden[1] != null && rochaden[1].equals(move)) {
                situation.getSquare(posx, posy).occupier = null;
                situation.getSquare(move.targetX, move.targetY).occupier = this;
                situation.getSquare(move.rook.posx, move.rook.posy).occupier = null;
                situation.getSquare(move.rookX, move.rookY).occupier = move.rook;
                posx = move.targetX;
                posy = move.targetY;
                move.rook.posx = move.rookX;
                move.rook.posy = move.rookY;
                gamesituation.nextTurn(this);
                return true;
            }
        }
        return false;
    }

    @Override
    public King clone() {
        King k = new King(black, posx, posy, gamesituation);
        k.captured = captured;
        k.moved = moved;
        k.positioninarray = positioninarray;
        return k;
    }
}
