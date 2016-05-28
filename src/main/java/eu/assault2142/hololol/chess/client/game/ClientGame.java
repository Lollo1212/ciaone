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

    /**
     * Create a new ClientGame
     *
     * @param connection the connection to the server
     * @param color the color you play (true -> black)
     */
    public ClientGame(ServerConnection connection, boolean color) {
        super(TYPE.CLIENT);
        this.connection = connection;
        final ClientGame g = this;
        EventQueue.invokeLater(() -> {
            gameview = new GameFrame(g);
            updateMovements();
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

    public void onDraw() {
        gameview.onDraw();
        endGame();
    }

    public void onResignation(boolean enemy) {
        gameview.onResignation(enemy);
        endGame();
    }

    @Override
    public void promotion(Pawn pawn) {
        String promotion = gameview.showPromotionChoice();
        connection.write(ServerMessages.Promotion, new Object[]{promotion, pawn.isBlack(), pawn.getPositionInArray()});
        updateMovements();
    }

}
