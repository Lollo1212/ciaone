package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ui.GameViewFactory;
import eu.assault2142.hololol.chess.game.chessmen.CastlingMove;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import java.awt.EventQueue;

/**
 * A Game which is completely played locally
 *
 * @author hololol2
 */
public final class LocalGame extends Game {

    /**
     * Start a new LocalGame
     */
    public LocalGame() {
        super(TYPE.LOCAL);

        EventQueue.invokeLater(new Thread() {
            @Override
            public void run() {
                gameview = GameViewFactory.startGameView(LocalGame.this, GameViewFactory.GameViews.SWING);
                updateMovements();
            }
        });
    }

    @Override
    public void clickAt(int squareX, int squareY) {
        selected = getGameState().getSquare(squareX, squareY);
        doMoveIfPossible();
        showPossibleMoves();
    }

    @Override
    public void finishedCalcs() {
        getGameState().resetHighlightedFields();
        gameview.setMovementsUpdating(false);
    }

    @Override
    public void onCheck() {
        gameview.onCheck();
    }

    @Override
    public void onCheckMate() {
        gameview.onCheckMate();
        this.endGame();
    }

    @Override
    public void onStaleMate() {
        gameview.onStaleMate();
        this.endGame();
    }

    @Override
    public void promotion(Pawn pawn) {
        String target = gameview.showPromotionChoice();
        execPromotion(target, pawn.isBlack(), pawn.getPositionInArray());
    }

    /**
     * Execute the selected Move or Capture if it is possible
     */
    private void doMoveIfPossible() {
        if (selected != null && picked != null) {
            picked.doMove(selected.getX(), selected.getY());
            picked.doCapture(selected.getX(), selected.getY());

            if (picked.getClass() == King.class) {
                ((King) picked).doCastling(getCastlingMove());
            }
        }
        picked = null;
    }

    /**
     * Assemble the CastlingMove which is currently selected
     *
     * @return the assembled CastlingMove
     */
    private CastlingMove getCastlingMove() {
        Rook rook;
        int rookX;
        int rookY;
        if (picked.getXPosition() < selected.getX()) {
            rook = (Rook) getGameState().getChessman(picked.isBlack(), 9);
            rookX = 5;
        } else {
            rook = (Rook) getGameState().getChessman(picked.isBlack(), 8);
            rookX = 3;
        }
        rookY = picked.isBlack() ? 0 : 7;
        return new CastlingMove(selected.getX(), selected.getY(), rook, rookX, rookY, (King) picked);
    }

}
