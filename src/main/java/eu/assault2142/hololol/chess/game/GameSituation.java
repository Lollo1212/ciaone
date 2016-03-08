package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.AbstractChessman;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A situation of a chessgame
 *
 * @author hololol2
 */
public class GameSituation {

    //abstractchessmen of the black player
    private final AbstractChessman[] chessmenBlackAbstract;
    //abstractchessmen of the white player
    private final AbstractChessman[] chessmenWhiteAbstract;
    //the squares of the game-board
    private final Square[] squares;
    //true if it's the black player's turn
    private boolean blackturn;
    //the game
    private final Game game;
    private int capturedBlack = 0;
    private int capturedWhite = 0;

    private Chessman[] chessmenBlack = new Chessman[16];
    private Chessman[] chessmenWhite = new Chessman[16];

    /**
     * Create a copy of the current game-state
     *
     * @param game the game
     */
    public GameSituation(Game game) {

        squares = new Square[78];
        int o;
        for (int q = 0; q <= 7; q++) {
            for (int y = 0; y <= 7; y++) {
                int l = 0;
                o = 10 * q + y;
                squares[o] = new Square(q, y);
            }
        }
        chessmenBlackAbstract = new AbstractChessman[16];
        chessmenWhiteAbstract = new AbstractChessman[16];
        blackturn = false;
        this.game = game;

        chessmenBlack = game.buildChessmen(true, squares);
        chessmenWhite = game.buildChessmen(false, squares);
        for (int a = 0; a < chessmenBlack.length; a++) {
            chessmenBlackAbstract[a] = new AbstractChessman(chessmenBlack[a], game);
        }
        for (int a = 0; a < chessmenWhite.length; a++) {
            chessmenWhiteAbstract[a] = new AbstractChessman(chessmenWhite[a], game);
        }
    }

    /**
     * Create a copy of the current game-state
     *
     * @param gs the game-situation
     */
    public GameSituation(GameSituation gs) {
        chessmenBlackAbstract = new AbstractChessman[16];
        chessmenWhiteAbstract = new AbstractChessman[16];
        squares = new Square[78];
        for (int a = 0; a < gs.chessmenBlackAbstract.length; a++) {
            chessmenBlackAbstract[a] = gs.chessmenBlackAbstract[a].clone();
        }
        for (int a = 0; a < gs.chessmenWhiteAbstract.length; a++) {
            chessmenWhiteAbstract[a] = gs.chessmenWhiteAbstract[a].clone();
        }
        for (int a = 0; a < gs.getSquares().length; a++) {
            if (gs.getSquares()[a] != null) {
                squares[a] = gs.getSquares()[a].clone();
            }
        }
        blackturn = gs.blackturn;
        this.game = gs.game;
    }

    /**
     *
     * @param black true for black player, false for white
     * @return all Moves the given player can currently do
     */
    public Move[] getAllMoves(boolean black) {
        ArrayList<Move> m = new ArrayList();
        if (black == true) {
            for (int a = 0; a < 16; a++) {
                if (!chessmenBlackAbstract[a].isCaptured()) {
                    Move[] z = chessmenBlackAbstract[a].getMoves();
                    m.addAll(Arrays.asList(z));
                    if (a == 15) {
                        Move[] x = ((King) chessmenBlackAbstract[a].getClone()).computeCastlings(false, this);
                        m.addAll(Arrays.asList(x));
                    }
                }
            }
        } else {
            for (int a = 0; a < 16; a++) {
                if (!chessmenBlackAbstract[a].isCaptured()) {
                    Move[] z = chessmenWhiteAbstract[a].getMoves();
                    m.addAll(Arrays.asList(z));
                    if (a == 15) {
                        Move[] x = ((King) chessmenWhiteAbstract[a].getClone()).computeCastlings(false, this);
                        m.addAll(Arrays.asList(x));
                    }
                }
            }
        }
        Move[] mo = new Move[m.size()];
        for (int x = 0; x < mo.length; x++) {
            mo[x] = (Move) m.get(x);
        }
        return mo;
    }

    /**
     *
     * @param black true for black player, false for white
     * @return all captures the given player can currently do
     */
    public Move[] getAllCaptures(boolean black) {
        ArrayList<Move> m = new ArrayList();
        if (black) {
            for (int a = 0; a < 16; a++) {
                if (!chessmenBlackAbstract[a].isCaptured()) {
                    Move[] z = chessmenBlackAbstract[a].getCaptures();
                    m.addAll(Arrays.asList(z));
                }
            }
        } else {
            for (int a = 0; a < 16; a++) {
                if (!chessmenWhiteAbstract[a].isCaptured()) {
                    Move[] z = chessmenWhiteAbstract[a].getCaptures();
                    m.addAll(Arrays.asList(z));
                }
            }
        }
        ArrayList nichts = new ArrayList();
        nichts.add(null);
        m.removeAll(nichts);
        Move[] mo = new Move[m.size()];
        for (int x = 0; x < m.size(); x++) {
            mo[x] = m.get(x);
            //System.out.println(mo[x].f.getClass()+""+mo[x].x+""+mo[x].y+mo[x].f.schwarz);
        }
        return mo;
    }

    /**
     *
     * @param black true for black player, false for white
     * @return all captures the given player can currently do
     */
    public Move[] computeAllCaptures(boolean black) {
        ArrayList<Move> m = new ArrayList();
        if (black) {
            for (int a = 0; a < 16; a++) {
                if (!chessmenBlackAbstract[a].isCaptured()) {
                    Move[] z = chessmenBlackAbstract[a].getClone().computeCaptures(false, this);
                    m.addAll(Arrays.asList(z));
                }
            }
        } else {
            for (int a = 0; a < 16; a++) {
                if (!chessmenWhiteAbstract[a].isCaptured()) {
                    Move[] z = chessmenWhiteAbstract[a].getClone().computeCaptures(false, this);
                    m.addAll(Arrays.asList(z));
                }
            }
        }
        ArrayList nichts = new ArrayList();
        nichts.add(null);
        m.removeAll(nichts);
        Move[] mo = new Move[m.size()];
        for (int x = 0; x < m.size(); x++) {
            mo[x] = m.get(x);
            //System.out.println(mo[x].f.getClass()+""+mo[x].x+""+mo[x].y+mo[x].f.schwarz);
        }
        return mo;
    }

    /**
     * Checks if the king is currently in danger
     *
     * @param black the color to check for
     * @param captures all captures currently possible
     * @return true if king is in danger, false otherwise
     */
    public boolean dangerForKing(boolean black, Move[] captures) {
        Move[] m = captures;
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
    public GameSituation doMove(Chessman chessman, int targetx, int targety) {
        GameSituation gsneu = clone();
        boolean b = true;
        AbstractChessman f1;
        if (chessman.isBlack()) {
            f1 = gsneu.chessmenBlackAbstract[chessman.getPositionInArray()];
            if (f1.isCaptured()) {
                b = false;

            }
        } else {
            f1 = gsneu.chessmenWhiteAbstract[chessman.getPositionInArray()];
            if (f1.isCaptured()) {
                b = false;

            }
        }
        if (b && targety <= 7 && targety >= 0 && targetx <= 7 && targetx >= 0) {
            if (gsneu.getSquares()[10 * targetx + targety].occupier != null) {
                Chessman r = gsneu.getSquares()[10 * targetx + targety].occupier;
                if (r.isBlack()) {
                    gsneu.chessmenBlackAbstract[r.getPositionInArray()].setCaptured();
                } else {
                    gsneu.chessmenWhiteAbstract[r.getPositionInArray()].setCaptured();
                }
            }
            gsneu.getSquares()[10 * f1.getClone().getX() + f1.getClone().getY()].occupier = null;

            f1.doMove(targetx, targety);

            gsneu.getSquares()[10 * targetx + targety].occupier = f1.getClone();
            gsneu.blackturn = !gsneu.blackturn;
            return gsneu;

        } else {
            System.out.println("b");
            return this;
        }
    }

    @Override
    public GameSituation clone() {
        return new GameSituation(this);
    }

    /**
     *
     * @return the squares of the game-board
     */
    protected Square[] getSquares() {
        return squares;
    }

    /**
     *
     * @param color the color of the chessmen
     * @return an array with all chessmen of the given color
     */
    public AbstractChessman[] getAbstractChessmen(boolean color) {
        if (color) {
            return chessmenBlackAbstract;
        } else {
            return chessmenWhiteAbstract;
        }
    }

    public Chessman[] getChessmen(boolean color) {
        if (color) {
            return chessmenBlack;
        } else {
            return chessmenWhite;
        }
    }

    /**
     * Execute the given move
     *
     * @param m the move
     * @return a new instance representing the situation after the move
     */
    public GameSituation doMove(Move m) {
        return doMove(m.getChessman(), m.getTargetX(), m.getTargetY());
    }

    public Square getSquare(int targetX, int targetY) {
        if (targetX < 0 || targetY < 0 || targetX > 7 || targetY > 7) {
            return null;
        }
        return squares[10 * targetX + targetY];
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

    public boolean getTurn() {
        return blackturn;
    }

    protected void nextTurn() {
        blackturn = !blackturn;
    }

    public int getCaptured(boolean color) {
        if (color) {
            return capturedBlack;
        } else {
            return capturedWhite;
        }
    }

    public void incCaptured(boolean color) {
        if (color) {
            capturedBlack++;
        } else {
            capturedWhite++;
        }
    }
}
