package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ui.GameFrame;
import eu.assault2142.hololol.chess.client.networking.ServerConnection;
import eu.assault2142.hololol.chess.client.networking.ServerMessages;
import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
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
            gameframe = new GameFrame(g);
        });
        updateMovements();
    }

    @Override
    public void clickAt(int feldx, int feldy) {
        selected = getGameState().getSquare(feldx, feldy);
        if (picked != null) {
            connection.write(ServerMessages.DoMove, new Object[]{picked.getX() * 10 + picked.getY(), feldx, feldy});
        }
        showPossibleMoves();
    }

    public void doCapture(int a, int x, int y) {
        Chessman f = getGameState().getSquare(10 / a, 10 % a).occupier;
        f.addCapture(new Move(x, y, f));
        f.doCapture(x, y);
    }

    public void doMove(int a, int x, int y) {
        Chessman f = getGameState().getSquare(10 / a, 10 % a).occupier;
        f.addMove(new Move(x, y, f));
        f.doMove(x, y);
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
    public void promotion(Pawn pawn) {
        String promotion = gameframe.showPromotionChoice();
        connection.write(ServerMessages.Promotion, new Object[]{promotion, pawn.isBlack(), pawn.getPositionInArray()});
        updateMovements();
    }

    @Override
    public void updateMovements() {
        getGameView().setMovementsUpdating(true);
        new ClientMovementUpdater(getGameState()).start();
    }

}
