package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ui.IGameView;
import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.game.chessmen.CastlingMove;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import java.util.List;

/**
 * A Game which is played on the Client. Either as LocalGame or as a ClientGame
 * to a ServerGame
 *
 * @author hololol2
 */
public abstract class Game extends eu.assault2142.hololol.chess.game.Game {

    protected IGameView gameview;

    /**
     * Create a new Game
     *
     * @param type the type of the game
     */
    public Game(TYPE type) {
        super(type);
    }

    public void acceptDraw() {

    }

    @Override
    public void endGame() {
        gameview.hide();
        Main.MENU.setVisible(true);
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
    public final void updateMovements() {
        getGameView().setMovementsUpdating(true);
        new ClientMovementUpdater(this).start();
    }

    /**
     * Show all possible Moves for the currently selected chessman
     */
    protected void showPossibleMoves() {
        if (selected != null) {
            selected.highlight(Square.HIGHLIGHT.SELECTED);
            if (selected.occupier != null) {
                picked = selected.occupier;
                List<Move> moves = getGameState().getChessman(picked.isBlack(), picked.getPositionInArray()).getMoves();
                List<Move> captures = getGameState().getChessman(picked.isBlack(), picked.getPositionInArray()).getCaptures();

                moves.stream().forEach((Move move) -> {
                    getGameState().getSquare(move.getTargetX(), move.getTargetY()).highlight(Square.HIGHLIGHT.MOVETARGET);
                });

                captures.stream().forEach((Move move) -> {
                    getGameState().getSquare(move.getTargetX(), move.getTargetY()).highlight(Square.HIGHLIGHT.CAPTURETARGET);
                });

                if (picked.getClass() == King.class) {
                    List<CastlingMove> castlings = ((King) picked).getCastlings();
                    castlings.stream().forEach((CastlingMove castling) -> {
                        getGameState().getSquare(castling.getTargetX(), castling.getTargetY()).highlight(Square.HIGHLIGHT.CASTLING);
                    });

                }
            }
        }
    }
}
