package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ui.IGameView;
import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.game.chessmen.CastlingMove;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Movement;
import java.util.List;

/**
 * A Game which is played on the Client. Either as LocalGame or as a ClientGame
 * to a ServerGame
 *
 * @author hololol2
 */
public abstract class Game extends eu.assault2142.hololol.chess.game.Game {

    protected IGameView gameview;
    protected Chessman picked;
    protected Square selected;

    /**
     * Create a new Game
     *
     * @param type the type of the game
     */
    public Game(TYPE type) {
        super(type);
    }

    @Override
    public void endGame() {
        gameview.hide();
        Main.MENU.setVisible(true);
    }

    @Override
    public void finishedCalcs() {
        getGameState().resetFields();
        gameview.setMovementsUpdating(false);
    }

    /**
     * Return the GameView
     *
     * @return the view the game is played in
     */
    public IGameView getGameView() {
        return gameview;
    }

    @Override
    public void onCheck() {
        Runnable checkimage = () -> {
            try {
                Thread.sleep(100);
                getGameView().setShowCheck(true);
                Thread.sleep(2000);
                getGameView().setShowCheck(false);
            } catch (InterruptedException ex) {
                Main.MENU.showErrorMessage("Unexpected Critical Error!", true);
            }
        };
        new Thread(checkimage).start();
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

    /**
     * Show all possible Moves for the currently selected chessman
     */
    protected void showPossibleMoves() {
        if (selected.occupier != null) {
            picked = selected.occupier;
            List<Movement> bewegungen = getGameState().getChessmen(picked.isBlack())[picked.getPositionInArray()].getMoves();
            List<Movement> schläge = getGameState().getChessmen(picked.isBlack())[picked.getPositionInArray()].getCaptures();
            if (picked.getClass() == King.class) {
                List<CastlingMove> rochaden = ((King) picked).getCastlings();
                rochaden.stream().forEach((CastlingMove c) -> {
                    getGameState().getSquare(c.getTargetX(), c.getTargetY()).highlight(Square.HIGHLIGHT.CASTLING);
                });

            }
            bewegungen.stream().forEach((Movement m) -> {
                getGameState().getSquare(m.getTargetX(), m.getTargetY()).highlight(Square.HIGHLIGHT.MOVETARGET);
            });
            schläge.stream().forEach((Movement m) -> {
                getGameState().getSquare(m.getTargetX(), m.getTargetY()).highlight(Square.HIGHLIGHT.CAPTURETARGET);
            });

        }
    }
}
