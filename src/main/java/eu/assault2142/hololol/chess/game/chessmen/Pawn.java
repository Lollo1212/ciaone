package eu.assault2142.hololol.chess.game.chessmen;


import eu.assault2142.hololol.chess.client.game.ui.FigurenFrame;
import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.client.game.LocalGame;
import eu.assault2142.hololol.chess.game.GameSituation;
import java.util.LinkedList;

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
     * @param game the game
     */
    private Pawn(boolean black, int posx, int posy, Game game) {
        super(black, posx, posy, game);
        image = game.getImage(NAMES.PAWN, black);
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
                    game.promotion(this);
                    
                }
            } else if (targetY == 0) {
                game.promotion(this);
            }
        }
        return r;
    }

    @Override
    public boolean doCapture(int targetX, int targetY) {
        boolean r = super.doCapture(targetX, targetY);
        //Möglichkeit in andere Figur zu verwandeln, wenn in letzter Reihe
        if (r == true) {
            if (black == true) {
                if (targetY == 7) {
                    //Bauer ist ganz "hinten"
                    game.promotion(this);
                }
            } else if (targetY == 0) {
                game.promotion(this);
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
     * @param game the game
     * @param numberinarray the number in the chessmen-array
     * @return the pawn
     */
    public static Pawn createPawn(boolean black, int number, Game game, int numberinarray) {
        int a;
        if (black == true) {
            a = 1;
        } else {
            a = 6;
        }
        Pawn b;
        if (number == 0) {
            b = new Pawn(black, 0, a, game);
        } else if (number == 1) {
            b = new Pawn(black, 1, a, game);
        } else if (number == 2) {
            b = new Pawn(black, 2, a, game);
        } else if (number == 3) {
            b = new Pawn(black, 3, a, game);
        } else if (number == 4) {
            b = new Pawn(black, 4, a, game);
        } else if (number == 5) {
            b = new Pawn(black, 5, a, game);
        } else if (number == 6) {
            b = new Pawn(black, 6, a, game);
        } else if (number == 7) {
            b = new Pawn(black, 7, a, game);
        } else {
            throw new IllegalArgumentException("The given number is incorrect");
        }
        b.positioninarray = numberinarray;
        return b;
    }

    @Override
    public Move[] computeMoves(boolean checkForCheck, GameSituation situation) {
        LinkedList<Move> moves = new LinkedList();
        Move[] bewegungen = new Move[2];
        int x = 0;
        int j = 0;
        if (black) {
            if(addIfMovePossible(moves,posx,posy+1,situation)&&posy==1){
                addIfMovePossible(moves,posx,posy+2,situation);
            }
        }else{
            if(addIfMovePossible(moves,posx,posy-1,situation)&&posy==6){
                addIfMovePossible(moves,posx,posy-2,situation);
            }
        }
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            removeCheckMoves(moves,situation);
        }
        Move[] ret = new Move[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }

    @Override
    public Move[] computeCaptures(boolean checkForCheck, GameSituation situation) {
        LinkedList<Move> captures = new LinkedList();
        if (black == true) {
            addIfCapturePossible(captures,posx+1,posy+1,situation);
            addIfCapturePossible(captures,posx-1,posy+1,situation);
        } else {
            addIfCapturePossible(captures,posx+1,posy-1,situation);
            addIfCapturePossible(captures,posx-1,posy-1,situation);
        }
        //Überprüfen auf Schach-Position
        if (checkForCheck) {
            removeCheckMoves(captures,situation);
        }
        Move[] ret = new Move[captures.size()];
        ret = captures.toArray(ret);
        return ret;
    }

    @Override
    public Pawn clone() {
        Pawn b = new Pawn(black, posx, posy, game);
        b.captured = captured;
        b.moved = moved;
        b.positioninarray = positioninarray;
        return b;
    }
}
