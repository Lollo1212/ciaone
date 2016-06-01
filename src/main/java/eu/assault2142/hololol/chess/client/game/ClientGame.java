package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ui.GameFrame;
import eu.assault2142.hololol.chess.client.networking.ServerConnection;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Movement;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.networking.ServerMessages;
import java.awt.EventQueue;

/**
 * A connection-game for a game played on a server
 *
 * @author hololol2
 */
public final class ClientGame extends Game {

    // Whether this client's color is black
    private final boolean black;
    // The connection to the server
    private final ServerConnection connection;

    /**
     * Create a new ClientGame
     *
     * @param connection the connection to the server
     * @param black whether this client's color is black
     */
    public ClientGame(ServerConnection connection, boolean black) {
        super(TYPE.CLIENT);
        this.black = black;
        this.connection = connection;
        final ClientGame g = this;
        EventQueue.invokeLater(() -> {
            gameview = new GameFrame(g);
            updateMovements();
            gameview.showColor(black);
        });
    }

    @Override
    public void clickAt(int squareX, int squareY) {
        selected = getGameState().getSquare(squareX, squareY);
        showPossibleMoves();
        connection.write(ServerMessages.Click, new Object[]{squareX, squareY});
    }

    /**
     * Executes a capture send from the server
     *
     * @param number the number of the chessman
     * @param targetX the target x-coordinate
     * @param targetY the target y-coordinate
     */
    public void doCapture(int number, int targetX, int targetY) {
        Chessman man = getGameState().getChessmen(getGameState().getTurn())[number];
        man.addCapture(new Movement(targetX, targetY, man));
        man.doCapture(targetX, targetY);
        updateMovements();
    }

    /**
     * Executes a move send from the server
     *
     * @param number the number of the chessman
     * @param targetX the target x-coordinate
     * @param targetY the target y-coordinate
     */
    public void doMove(int number, int targetX, int targetY) {
        Chessman man = getGameState().getChessmen(getGameState().getTurn())[number];
        man.addMove(new Movement(targetX, targetY, man));
        man.doMove(targetX, targetY);
        updateMovements();
    }

    /**
     * Offer the Player a Draw-Offer from the enemy
     */
    public void drawOffer() {
        gameview.drawOffer();
    }

    /**
     * Returns the connection to the server
     *
     * @return the connection to the server
     */
    public ServerConnection getConnection() {
        return connection;
    }

    /**
     * Inform about Check
     */
    public void incomingCheck() {
        gameview.onCheck();
    }

    /**
     * Inform about CheckMate
     */
    public void incomingCheckMate() {
        gameview.onCheckMate();
        this.endGame();
    }

    /**
     * Inform about successfull Draw
     */
    public void incomingDraw() {
        gameview.onDraw();
        endGame();
    }

    /**
     * Inform about Resignation of a player
     *
     * @param enemy true if the enemy resignated, false if you
     */
    public void incomingResignation(boolean enemy) {
        gameview.onResignation(enemy);
        endGame();
    }

    /**
     * Inform about Stalemate
     */
    public void incomingStaleMate() {
        gameview.onStaleMate();
        this.endGame();
    }

    /**
     * Return the color of the local player
     *
     * @return true if the player's color is black, false otherwise
     */
    public boolean isBlack() {
        return black;
    }

    @Override
    public void onCheck() {
    }

    @Override
    public void onCheckMate() {
    }

    @Override
    public void onStaleMate() {
    }

    @Override
    public void promotion(Pawn pawn) {
    }

}
