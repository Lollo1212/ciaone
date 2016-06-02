package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Movement;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A situation in a chess-game
 *
 * @author hololol2
 */
public class GameState {

    private boolean blackturn;
    private int capturedBlack = 0;
    private int capturedWhite = 0;

    private Chessman[] chessmenBlack = new Chessman[16];
    private Chessman[] chessmenWhite = new Chessman[16];

    private final Game game;

    private Chessman lastmoved;

    private final Square[] squares;

    /**
     * Create a a new (initial) game-state for the given game
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
     * @param gamestate the game-situation
     */
    public GameState(GameState gamestate) {
        chessmenBlack = new Chessman[16];
        chessmenWhite = new Chessman[16];
        squares = new Square[78];
        for (int a = 0; a < gamestate.chessmenBlack.length; a++) {
            chessmenBlack[a] = gamestate.chessmenBlack[a].clone();
        }
        for (int a = 0; a < gamestate.chessmenWhite.length; a++) {
            chessmenWhite[a] = gamestate.chessmenWhite[a].clone();
        }
        for (int a = 0; a < gamestate.getSquares().length; a++) {
            if (gamestate.getSquares()[a] != null) {
                squares[a] = gamestate.getSquares()[a].clone();
            }
        }
        Arrays.stream(chessmenBlack).forEach((Chessman man) -> {
            getSquare(man.getX(), man.getY()).occupier = man;
        });
        Arrays.stream(chessmenWhite).forEach((Chessman man) -> {
            getSquare(man.getX(), man.getY()).occupier = man;
        });
        blackturn = gamestate.blackturn;
        this.game = gamestate.game;
    }

    /**
     *
     * @param black true for black player, false for white
     * @return all captures the given player can currently do
     */
    public List<Movement> computeAllCaptures(boolean black) {
        LinkedList<Movement> captures = new LinkedList();
        IntStream.range(0, 16).mapToObj((int num) -> {
            return getChessman(black, num);
        }).filter((Chessman man) -> {
            return !man.isCaptured();
        }).map((Chessman man) -> {
            return man.computeCaptures(false, this);
        }).forEach((List<Movement> cap) -> {
            captures.addAll(cap);
        });
        return captures;
    }

    /**
     * Checks if the king is currently in danger
     *
     * @param black the color to check for
     * @return true if king is in danger, false otherwise
     */
    public boolean dangerForKing(boolean black) {
        List<Movement> moves = computeAllCaptures(!black);
        boolean kingInDanger = false;
        for (Movement move : moves) {
            if (move != null) {
                Square square = squares[10 * move.getTargetX() + move.getTargetY()];
                if (square.occupier != null && square.occupier.getClass() == King.class && square.occupier.isBlack() == black) {
                    kingInDanger = true;
                }
            }
        }
        return kingInDanger;
    }

    /**
     * Emulate the given move
     *
     * @param chessman the chessman to do the move
     * @param targetX the targetX
     * @param targetY the targetY
     * @return a new instance representing the situation after the move
     */
    public GameState emulateMove(Chessman chessman, int targetX, int targetY) {
        GameState newState = new GameState(this);
        boolean notCaptured = true;
        Chessman man;
        if (chessman.isBlack()) {
            man = newState.chessmenBlack[chessman.getPositionInArray()];
            if (man.isCaptured()) {
                notCaptured = false;

            }
        } else {
            man = newState.chessmenWhite[chessman.getPositionInArray()];
            if (man.isCaptured()) {
                notCaptured = false;

            }
        }
        Square square = getSquare(targetX, targetY);
        if (notCaptured && square != null) {
            if (square.isOccupied()) {
                Chessman r = square.occupier;
                if (man.isBlack() != r.isBlack()) {
                    newState.getChessman(r.isBlack(), r.getPositionInArray()).setCaptured();
                }
            }
            newState.getSquare(man.getX(), man.getY()).occupier = null;

            man.doMove(targetX, targetY);

            newState.getSquares()[10 * targetX + targetY].occupier = man;
            newState.blackturn = !newState.blackturn;
            return newState;

        } else {
            System.out.println("Emulating Move not successfull");
            return this;
        }
    }

    /**
     * Emulate the given move
     *
     * @param move the move
     * @return a new instance representing the situation after the move
     */
    public GameState emulateMove(Movement move) {
        return GameState.this.emulateMove(move.getChessman(), move.getTargetX(), move.getTargetY());
    }

    /**
     * Get all captures for the given player
     *
     * @param black true for black player, false for white
     * @return all captures the given player can currently do
     */
    public List<Movement> getAllCaptures(boolean black) {
        LinkedList<Movement> captures = new LinkedList();
        IntStream.range(0, 16).mapToObj((int num) -> {
            return getChessman(black, num);
        }).filter((Chessman man) -> {
            return !man.isCaptured();
        }).map((Chessman man) -> {
            return man.getCaptures();
        }).forEach((List<Movement> cap) -> {
            captures.addAll(cap);
        });
        return captures;

    }

    /**
     * Get all Moves for the given player
     *
     * @param black true for black player, false for white
     * @return all Moves the given player can currently do
     */
    public List<Movement> getAllMoves(boolean black) {
        LinkedList<Movement> moves = new LinkedList();
        IntStream.range(0, 16).mapToObj((int num) -> {
            return getChessman(black, num);
        }).filter((Chessman man) -> {
            return !man.isCaptured();
        }).map((Chessman man) -> {
            return man.getMoves();
        }).forEach((List<Movement> mov) -> {
            moves.addAll(mov);
        });

        return moves;
    }

    /**
     * Get the number of captured chessman of the given color
     *
     * @param black whether to select the number of captured black chessman
     * @return the number of captured chessman of the color
     */
    public int getCaptured(boolean black) {
        if (black) {
            return capturedBlack;
        } else {
            return capturedWhite;
        }
    }

    /**
     * Get all chessmen of the given color
     *
     * @param black the color of the chessman
     * @param number the number of the chessman
     * @return the chessman with the given number and color
     */
    public Chessman getChessman(boolean black, int number) {
        if (black) {
            return chessmenBlack[number];
        } else {
            return chessmenWhite[number];
        }
    }

    /**
     * Get the Game
     *
     * @return the Game this state belongs to
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get the Chessman which was moved recently
     *
     * @return the last recently moved chessman
     */
    public Chessman getLastMoved() {
        return lastmoved;
    }

    /**
     * Get all possible Captures for the given chessman
     *
     * @param positioninarray the number of the chessman
     * @param black the color of the chessman
     * @return a list of all captures
     */
    public List<Movement> getPossibleCaptures(int positioninarray, boolean black) {
        return getChessman(black, positioninarray).getCaptures();
    }

    /**
     * Get all possible Moves for the given chessman
     *
     * @param positioninarray the number of the chessman
     * @param black the color of the chessman
     * @return a list of all moves
     */
    public List<Movement> getPossibleMoves(int positioninarray, boolean black) {
        return getChessman(black, positioninarray).getMoves();
    }

    /**
     * Returns the square at the given position
     *
     * @param targetX the x-coordinate
     * @param targetY the y-coordinate
     * @return the square
     */
    public Square getSquare(int targetX, int targetY) {
        if (targetX < 0 || targetY < 0 || targetX > 7 || targetY > 7) {
            return null;
        }
        return squares[10 * targetX + targetY];
    }

    /**
     * Check which player's turn it is
     *
     * @return true if it's black's turn, false otherwise
     */
    public boolean getTurn() {
        return blackturn;
    }

    /**
     * Increase the number of captured chessmen
     *
     * @param color the color of the captured chessman
     */
    public void incCaptured(boolean color) {
        if (color) {
            capturedBlack++;
        } else {
            capturedWhite++;
        }
    }

    /**
     * Prepare game for next move
     *
     * @param moved the chessman which was moved
     */
    public void nextTurn(Chessman moved) {

        resetFields();
        lastmoved = moved;
        blackturn = !blackturn;
        game.updateMovements();
    }

    /**
     * Reset markings on all fields
     */
    public void resetFields() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (squares[10 * x + y] != null) {
                    squares[10 * x + y].resetHighlighting();
                }
            }
        }
    }

    /**
     * Initialize the chessman for a player
     *
     * @param black the color of the player
     * @param squares the squares of the game
     * @return an array of the chessmen
     */
    protected final Chessman[] buildChessmen(boolean black, Square[] squares) {

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
     * Return the squares
     *
     * @return the squares of the game-board
     */
    private Square[] getSquares() {
        return squares;
    }

    void setChessman(boolean black, int number, Chessman man) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
