package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ui.GameFrame;
import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.game.chessmen.CastlingMove;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Rook;

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
        gameview = new GameFrame(this);
        updateMovements();
    }

    @Override
    public void clickAt(int squareX, int squareY) {
        selected = getGameState().getSquare(squareX, squareY);
        doMoveIfPossible();
        showPossibleMoves();
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
        if (selected != null) {
            selected.highlight(Square.HIGHLIGHT.SELECTED);
            if (picked != null) {
                picked.doMove(selected.getX(), selected.getY());
                picked.doCapture(selected.getX(), selected.getY());

                if (picked.getClass() == King.class) {
                    ((King) picked).doCastling(getCastlingMove(), getGameState());
                }
            }
            picked = null;
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
            t = (Rook) getGameState().getChessmen(picked.isBlack())[9];
            tx = 5;
        } else {
            t = (Rook) getGameState().getChessmen(picked.isBlack())[8];
            tx = 3;
        }
        ty = picked.isBlack() ? 0 : 7;
        return new CastlingMove(selected.getX(), selected.getY(), t, tx, ty, (King) picked);
    }

}
