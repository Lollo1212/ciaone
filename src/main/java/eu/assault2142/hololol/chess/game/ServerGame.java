package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.CastlingMove;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.networking.GameClientConnection;
import eu.assault2142.hololol.chess.server.networking.ClientConnection;

/**
 *
 * @author hololol2
 */
public class ServerGame extends Game {

    public GameClientConnection client1;
    public GameClientConnection client2;

    public ServerGame(ClientConnection a, ClientConnection b) {
        super(TYPE.SERVER);
        client1 = a;
        client2 = b;
        updateMovements();
    }

    public void incomingClick(int posx, int posy, GameClientConnection conn) {
        if (conn.equals(client1) == getGameState().getTurn()) {
            clickAt(posx, posy);
        }
    }

    @Override
    public void clickAt(int feldx, int feldy) {
        selected = getGameState().getSquare(feldx, feldy);
        doMoveIfPossible();
        picked = selected.occupier;
    }

    /**
     * Execute the selected Move or Capture if it is possible
     */
    private void doMoveIfPossible() {
        if (selected != null) {
            if (picked != null) {
                if (picked.doMove(selected.getX(), selected.getY())) {
                    client1.write(ClientMessages.Move, new Object[]{picked.getPositionInArray(), selected.getX(), selected.getY()});
                    client2.write(ClientMessages.Move, new Object[]{picked.getPositionInArray(), selected.getX(), selected.getY()});
                }
                if (picked.doCapture(selected.getX(), selected.getY())) {
                    client1.write(ClientMessages.Capture, new Object[]{picked.getPositionInArray(), selected.getX(), selected.getY()});
                    client2.write(ClientMessages.Capture, new Object[]{picked.getPositionInArray(), selected.getX(), selected.getY()});
                }

                if (picked.getClass() == King.class) {
                    //((King) picked).doCastling(getCastlingMove(), getGameState());
                }
            }
        }
    }

    /**
     * Assemble the CastlingMove which is currently selected
     *
     * @return the assembled CastlingMove
     */
    private CastlingMove getCastlingMove() {
        Rook t;
        int tx;
        int ty;
        if (picked.getX() < selected.getX()) {
            t = (Rook) getGameState().getChessman(picked.isBlack(), 9);
            tx = 5;
        } else {
            t = (Rook) getGameState().getChessman(picked.isBlack(), 8);
            tx = 3;
        }
        ty = picked.isBlack() ? 0 : 7;
        return new CastlingMove(selected.getX(), selected.getY(), t, tx, ty, (King) picked);
    }

    @Override
    public void endGame() {
    }

    @Override
    public void finishedCalcs() {
        //send moves
    }

    /**
     * Get the first player
     *
     * @return the connection to the player
     */
    public GameClientConnection getClient1() {
        return client1;
    }

    /**
     * Get the second player
     *
     * @return the connection to the player
     */
    public GameClientConnection getClient2() {
        return client2;
    }

    @Override
    public void onCheck() {
        client1.write(ClientMessages.Check, null);
        client2.write(ClientMessages.Check, null);
    }

    @Override
    public void onCheckMate() {
        client1.write(ClientMessages.Checkmate, null);
        client2.write(ClientMessages.Checkmate, null);
    }

    @Override
    public void onStaleMate() {
        client1.write(ClientMessages.Stalemate, null);
        client2.write(ClientMessages.Stalemate, null);
    }

    @Override
    public void promotion(Pawn pawn) {
        if (pawn.isBlack()) {
            client1.write(ClientMessages.Promote, new Object[]{pawn.getPositionInArray()});
        } else {
            client2.write(ClientMessages.Promote, new Object[]{pawn.getPositionInArray()});
        }
    }

    @Override
    public void updateMovements() {
        new ServerMovementUpdater(getGameState()).start();
    }
}
