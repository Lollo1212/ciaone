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

    private final ServerConnection connection;
    private final boolean color;

    /**
     * Create a new ClientGame
     *
     * @param connection the connection to the server
     * @param color the color you play (true -> black)
     */
    public ClientGame(ServerConnection connection, boolean color) {
        super(TYPE.CLIENT);
        this.color = color;
        this.connection = connection;
        final ClientGame g = this;
        EventQueue.invokeLater(() -> {
            gameview = new GameFrame(g);
            updateMovements();
            gameview.showColor(color);
        });
    }

    @Override
    public void clickAt(int feldx, int feldy) {
        selected = getGameState().getSquare(feldx, feldy);
        showPossibleMoves();
        connection.write(ServerMessages.Click, new Object[]{feldx, feldy});
    }

    /**
     * Executes a capture send from the server
     *
     * @param a the coordinates of the chessman
     * @param x the target x-coordinate
     * @param y the target y-coordinate
     */
    public void doCapture(int a, int x, int y) {
        Chessman f = getGameState().getChessmen(getGameState().getTurn())[a];
        f.addCapture(new Movement(x, y, f));
        f.doCapture(x, y);
        updateMovements();
    }

    /**
     * Executes a move send from the server
     *
     * @param a the coordinates of the chessman
     * @param x the target x-coordinate
     * @param y the target y-coordinate
     */
    public void doMove(int a, int x, int y) {
        Chessman f = getGameState().getChessmen(getGameState().getTurn())[a];
        f.addMove(new Movement(x, y, f));
        f.doMove(x, y);
        updateMovements();
    }

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

    @Override
    public void onCheckMate() {
    }

    public void onDraw() {
        gameview.onDraw();
        endGame();
    }

    public void onResignation(boolean enemy) {
        gameview.onResignation(enemy);
        endGame();
    }

    @Override
    public void onStaleMate() {
    }

    @Override
    public void promotion(Pawn pawn) {
    }

    public boolean isBlack() {
        return color;
    }

    public void incomingCheckMate() {
        gameview.onCheckMate();
        this.endGame();
    }

    public void incomingStaleMate() {
        gameview.onStaleMate();
        this.endGame();
    }

    public void incomingPromotion(String target, boolean color, int number) {
        execPromotion(target, color, number);
    }

    public void incomingCheck() {
        gameview.onCheck();
    }

    @Override
    public void onCheck() {

    }
}
