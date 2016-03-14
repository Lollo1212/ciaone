package eu.assault2142.hololol.chess.game.chessmen;

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
     * @param posx the x-coordinate
     * @param posy the y-coordinate
     * @param game the gamesituation
     */
    private Pawn(boolean black, int posx, int posy, GameState game) {
        super(black, posx, posy, game);
        image = game.getGame().getImage(NAMES.PAWN, black);
        value = 1;
    }

    @Override
    public boolean doMove(int targetX, int targetY) {
        boolean r = super.doMove(targetX, targetY);
        //Möglichkeit in andere Figur zu verwandeln, wenn in letzter Reihe
        if (r == true) {
            if (black == true) {
                if (targetY == 7) {
                    //Bauer ist ganz "hinten"
                    gamesituation.getGame().promotion(this);

                }
            } else if (targetY == 0) {
                gamesituation.getGame().promotion(this);
            }
        }
        return r;
    }

    @Override
    public boolean doCapture(int targetX, int targetY) {
        boolean r = super.doCapture(targetX, targetY);
        if (!r) {
            Square square = gamesituation.getSquare(targetX, targetY + (black ? -1 : +1));
            if (gamesituation.getTurn() == black) {
                List<Movement> bewegungen = gamesituation.getPossibleCaptures(positioninarray, black);
                if (square != null && bewegungen != null && square.isOccupiedByColor(!black)) {
                    Optional<Movement> findFirst = bewegungen.stream().filter((Movement m) -> {
                        return m.getClass() == EnPassantMove.class && targetX == m.getTargetX();
                    }).findFirst();
                    r = findFirst.isPresent();
                    findFirst.ifPresent((Movement m) -> {
                        executeEnPassant((EnPassantMove) m);
                    });
                }
            }
        }
        //Möglichkeit in andere Figur zu verwandeln, wenn in letzter Reihe
        if (r == true) {
            if (black == true) {
                if (targetY == 7) {
                    //Bauer ist ganz "hinten"
                    gamesituation.getGame().promotion(this);
                }
            } else if (targetY == 0) {
                gamesituation.getGame().promotion(this);
            }
        }
        return r;
    }

    /**
     * Creates a new Pawn
     *
     * @param black whether this chessman is black or not
     * @param number the number of the bishop (from 0 to 7; 0 for the left, 7
     * for the right)
     * @param game the gamesituation
     * @param numberinarray the number in the chessmen-array
     * @return the pawn
     */
    public static Pawn createPawn(boolean black, int number, GameState game, int numberinarray) {
        int a;
        if (black == true) {
            a = 1;
        } else {
            a = 6;
        }
        Pawn b;
        switch (number) {
            case 0:
                b = new Pawn(black, 0, a, game);
                break;
            case 1:
                b = new Pawn(black, 1, a, game);
                break;
            case 2:
                b = new Pawn(black, 2, a, game);
                break;
            case 3:
                b = new Pawn(black, 3, a, game);
                break;
            case 4:
                b = new Pawn(black, 4, a, game);
                break;
            case 5:
                b = new Pawn(black, 5, a, game);
                break;
            case 6:
                b = new Pawn(black, 6, a, game);
                break;
            case 7:
                b = new Pawn(black, 7, a, game);
                break;
            default:
                throw new IllegalArgumentException("The given number is incorrect");
        }
        b.positioninarray = numberinarray;
        return b;
    }

    @Override
    public List<Movement> computeMoves(boolean checkForCheck, GameState situation) {
        LinkedList<Movement> moves = new LinkedList();
        if (black) {
            if (addIfMovePossible(moves, posx, posy + 1, situation) && posy == 1) {
                addIfMovePossible(moves, posx, posy + 2, situation);
            }
        } else if (addIfMovePossible(moves, posx, posy - 1, situation) && posy == 6) {
            addIfMovePossible(moves, posx, posy - 2, situation);
        }
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            moves = removeCheckMoves(moves, situation);
        }
        return moves;
    }

    @Override
    public List<Movement> computeCaptures(boolean checkForCheck, GameState situation) {
        LinkedList<Movement> captures = new LinkedList();
        if (black == true) {
            addIfCapturePossible(captures, posx + 1, posy + 1, situation);
            addIfCapturePossible(captures, posx - 1, posy + 1, situation);
            Chessman lastmoved = gamesituation.getLastMoved();
            if (posy == 4 && lastmoved.getClass() == Pawn.class && lastmoved.posy == 4 && (lastmoved.posx == posx + 1 || lastmoved.posx == posx - 1)) {
                captures.add(new EnPassantMove(lastmoved.posx, 5, this, (Pawn) lastmoved));
            }
        } else {
            addIfCapturePossible(captures, posx + 1, posy - 1, situation);
            addIfCapturePossible(captures, posx - 1, posy - 1, situation);
            Chessman lastmoved = gamesituation.getLastMoved();
            if (posy == 3 && lastmoved.getClass() == Pawn.class && lastmoved.posy == 3 && (lastmoved.posx == posx + 1 || lastmoved.posx == posx - 1)) {
                captures.add(new EnPassantMove(lastmoved.posx, 2, this, (Pawn) lastmoved));
            }
        }
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            captures = removeCheckMoves(captures, situation);
        }
        return captures;
    }

    @Override
    public Pawn clone() {
        Pawn b = new Pawn(black, posx, posy, gamesituation);
        b.captured = captured;
        b.moved = moved;
        b.positioninarray = positioninarray;
        return b;
    }

    /**
     * Execute the given EnPassantMove
     *
     * @param m the move to execute
     */
    private void executeEnPassant(EnPassantMove m) {
        //Figur schlagen
        Pawn p = m.getEnemyPawn();
        p.captured = true;
        p.moveToEdgeZone();
        gamesituation.getSquare(posx, posy).occupier = null;
        posx = m.targetX;
        posy = m.targetY;
        gamesituation.getSquare(m.targetX, m.targetY).occupier = this;
        gamesituation.nextTurn(this);
    }
}
