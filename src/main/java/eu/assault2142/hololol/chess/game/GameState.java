package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A situation of a chessgame
 *
 * @author hololol2
 */
public class GameState {

    //true if it's the black player's turn
    private boolean blackturn;
    private int capturedBlack = 0;
    private int capturedWhite = 0;

    private Chessman[] chessmenBlack = new Chessman[16];
    private Chessman[] chessmenWhite = new Chessman[16];
    //the game
    private final Game game;

    private Chessman lastmoved;
    //the squares of the game-board
    private final Square[] squares;

    /**
     * Create a copy of the current game-state
     *
     * @param game the game
     */
    public GameState(Game game) {

        squares = new Square[78];
        int o;
        for (int q = 0; q <= 7; q++) {
            for (int y = 0; y <= 7; y++) {
                int l = 0;
                o = 10 * q + y;
                squares[o] = new Square(q, y);
            }
        }
        blackturn = false;
        this.game = game;

        chessmenBlack = buildChessmen(true, squares);
        chessmenWhite = buildChessmen(false, squares);
    }

    /**
     * Create a copy of the current game-state
     *
     * @param gs the game-situation
     */
    public GameState(GameState gs) {
        chessmenBlack = new Chessman[16];
        chessmenWhite = new Chessman[16];
        squares = new Square[78];
        for (int a = 0; a < gs.chessmenBlack.length; a++) {
            chessmenBlack[a] = gs.chessmenBlack[a].clone();
        }
        for (int a = 0; a < gs.chessmenWhite.length; a++) {
            chessmenWhite[a] = gs.chessmenWhite[a].clone();
        }
        for (int a = 0; a < gs.getSquares().length; a++) {
            if (gs.getSquares()[a] != null) {
                squares[a] = gs.getSquares()[a].clone();
            }
        }
        blackturn = gs.blackturn;
        this.game = gs.game;
    }

    @Override
    public GameState clone() {
        return new GameState(this);
    }

    /**
     *
     * @param black true for black player, false for white
     * @return all captures the given player can currently do
     */
    public List<Move> computeAllCaptures(boolean black) {
        LinkedList<Move> ret = new LinkedList();
        IntStream.range(0, 16).mapToObj((int num) -> {
            return getChessmen(black)[num];
        }).filter((Chessman man) -> {
            return !man.isCaptured();
        }).map((Chessman man) -> {
            return man.computeCaptures(false, this);
        }).forEach((List<Move> captures) -> {
            ret.addAll(captures);
        });
        return ret;
    }

    /**
     * Checks if the king is currently in danger
     *
     * @param black the color to check for
     * @param captures all captures currently possible
     * @return true if king is in danger, false otherwise
     */
    public boolean dangerForKing(boolean black, List<Move> captures) {
        List<Move> m = captures;
        boolean b = false;
        for (Move m1 : m) {
            if (m1 != null) {
                Square f = squares[10 * m1.getTargetX() + m1.getTargetY()];
                if (f.occupier != null && f.occupier.getClass() == King.class && f.occupier.isBlack() == black) {
                    b = true;
                }
            }
        }
        return b;
    }

    /**
     * Execute the given move
     *
     * @param chessman the chessman to do the move
     * @param targetx the targetX
     * @param targety the targetY
     * @return a new instance representing the situation after the move
     */
    public GameState emulateMove(Chessman chessman, int targetx, int targety) {
        GameState gsneu = clone();
        boolean b = true;
        Chessman f1;
        if (chessman.isBlack()) {
            f1 = gsneu.chessmenBlack[chessman.getPositionInArray()];
            if (f1.isCaptured()) {
                b = false;

            }
        } else {
            f1 = gsneu.chessmenWhite[chessman.getPositionInArray()];
            if (f1.isCaptured()) {
                b = false;

            }
        }
        if (b && targety <= 7 && targety >= 0 && targetx <= 7 && targetx >= 0) {
            if (gsneu.getSquares()[10 * targetx + targety].occupier != null) {
                Chessman r = gsneu.getSquares()[10 * targetx + targety].occupier;
                if (r.isBlack()) {
                    gsneu.chessmenBlack[r.getPositionInArray()].setCaptured();
                } else {
                    gsneu.chessmenWhite[r.getPositionInArray()].setCaptured();
                }
            }
            gsneu.getSquares()[10 * f1.getX() + f1.getY()].occupier = null;

            f1.doMove(targetx, targety);

            gsneu.getSquares()[10 * targetx + targety].occupier = f1;
            gsneu.blackturn = !gsneu.blackturn;
            return gsneu;

        } else {
            System.out.println("b");
            return this;
        }
    }

    /**
     * Execute the given move
     *
     * @param m the move
     * @return a new instance representing the situation after the move
     */
    public GameState emulateMove(Move m) {
        return GameState.this.emulateMove(m.getChessman(), m.getTargetX(), m.getTargetY());
    }

    /**
     *
     * @param black true for black player, false for white
     * @return all captures the given player can currently do
     */
    public List<Move> getAllCaptures(boolean black) {
        LinkedList<Move> ret = new LinkedList();
        IntStream.range(0, 16).mapToObj((int num) -> {
            return getChessmen(black)[num];
        }).filter((Chessman man) -> {
            return !man.isCaptured();
        }).map((Chessman man) -> {
            return man.getCaptures();
        }).forEach((List<Move> captures) -> {
            ret.addAll(captures);
        });
        return ret;

    }

    /**
     *
     * @param black true for black player, false for white
     * @return all Moves the given player can currently do
     */
    public List<Move> getAllMoves(boolean black) {
        LinkedList<Move> ret = new LinkedList();
        IntStream.range(0, 16).mapToObj((int num) -> {
            return getChessmen(black)[num];
        }).filter((Chessman man) -> {
            return !man.isCaptured();
        }).map((Chessman man) -> {
            return man.getMoves();
        }).forEach((List<Move> captures) -> {
            ret.addAll(captures);
        });
        return ret;
    }

    public int getCaptured(boolean color) {
        if (color) {
            return capturedBlack;
        } else {
            return capturedWhite;
        }
    }

    public Chessman[] getChessmen(boolean color) {
        if (color) {
            return chessmenBlack;
        } else {
            return chessmenWhite;
        }
    }

    public Game getGame() {
        return game;
    }

    public Chessman getLastMoved() {
        return lastmoved;
    }

    public List<Move> getPossibleCaptures(int positioninarray, boolean black) {
        return getChessmen(black)[positioninarray].getCaptures();
    }

    public List<Move> getPossibleMoves(int positioninarray, boolean black) {
        return getChessmen(black)[positioninarray].getMoves();
    }

    public Square getSquare(int targetX, int targetY) {
        if (targetX < 0 || targetY < 0 || targetX > 7 || targetY > 7) {
            return null;
        }
        return squares[10 * targetX + targetY];
    }

    public boolean getTurn() {
        return blackturn;
    }

    public void incCaptured(boolean color) {
        if (color) {
            capturedBlack++;
        } else {
            capturedWhite++;
        }
    }

    public void nextTurn(Chessman moved) {

        resetFields();
        lastmoved = moved;
        blackturn = !blackturn;
        game.updateMovements();
    }

    public void resetFields() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (squares[10 * x + y] != null) {
                    squares[10 * x + y].resetHighlighting();
                }
            }
        }
    }

    protected Chessman[] buildChessmen(boolean black, Square[] squares) {

        Chessman[] figuren = new Chessman[16];
        for (int a = 0; a < 8; a++) {
            figuren[a] = Pawn.createPawn(black, a, this, a);
        }
        figuren[8] = Rook.createTurm(black, 0, this, 8);
        figuren[9] = Rook.createTurm(black, 1, this, 9);
        figuren[10] = Knight.createKnight(black, 0, this, 10);
        figuren[11] = Knight.createKnight(black, 1, this, 11);
        figuren[12] = Bishop.createBishop(black, 0, this, 12);
        figuren[13] = Bishop.createBishop(black, 1, this, 13);
        figuren[14] = Queen.createQueen(black, this, 14);
        figuren[15] = King.createKing(black, this, 15);
        if (black == true) {
            squares[1].occupier = figuren[0];
            squares[11].occupier = figuren[1];
            squares[21].occupier = figuren[2];
            squares[31].occupier = figuren[3];
            squares[41].occupier = figuren[4];
            squares[51].occupier = figuren[5];
            squares[61].occupier = figuren[6];
            squares[71].occupier = figuren[7];
            squares[0].occupier = figuren[8];
            squares[70].occupier = figuren[9];
            squares[10].occupier = figuren[10];
            squares[60].occupier = figuren[11];
            squares[20].occupier = figuren[12];
            squares[50].occupier = figuren[13];
            squares[40].occupier = figuren[15];
            squares[30].occupier = figuren[14];
        }
        if (black == false) {
            squares[6].occupier = figuren[0];
            squares[16].occupier = figuren[1];
            squares[26].occupier = figuren[2];
            squares[36].occupier = figuren[3];
            squares[46].occupier = figuren[4];
            squares[56].occupier = figuren[5];
            squares[66].occupier = figuren[6];
            squares[76].occupier = figuren[7];
            squares[7].occupier = figuren[8];
            squares[77].occupier = figuren[9];
            squares[17].occupier = figuren[10];
            squares[67].occupier = figuren[11];
            squares[27].occupier = figuren[12];
            squares[57].occupier = figuren[13];
            squares[47].occupier = figuren[15];
            squares[37].occupier = figuren[14];
        }
        return figuren;
    }

    /**
     *
     * @return the squares of the game-board
     */
    protected Square[] getSquares() {
        return squares;
    }

}
