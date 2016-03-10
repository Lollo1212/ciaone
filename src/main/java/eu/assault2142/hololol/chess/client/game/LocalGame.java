package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ui.GameFrame;
import eu.assault2142.hololol.chess.client.menus.MainMenu;
import eu.assault2142.hololol.chess.client.translator.Translator;
import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.Settings;
import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.CastlingMove;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * A Game which is completely played locally
 *
 * @author hololol2
 */
public final class LocalGame extends Game {

    private final GameFrame gameframe;
    private Chessman picked;
    private Square selected;

    /**
     * Start a new LocalGame
     */
    public LocalGame() {
        super(TYPE.LOCAL);
        gameframe = new GameFrame(this);
        updateMovements();
    }

    @Override
    public void clickAt(int feldx, int feldy) {
        selected = getGameState().getSquare(feldx, feldy);
        doMoveIfPossible();
        showPossibleMoves();
    }

    @Override
    public void endGame() {
        gameframe.setVisible(false);
        MainMenu.MAINMENU.setVisible(true);
    }

    @Override
    public void finishedCalcs() {
        getGameState().resetFields();
        gameframe.getGameBoard().movementsupdating = false;
    }

    /**
     * Return the frame of the game
     *
     * @return the frame the game is played in
     */
    public GameFrame getGameFrame() {
        return gameframe;
    }

    @Override
    public ImageIcon getImage(Chessman.NAMES name, boolean black) {
        String color = "white";
        if (black) {
            color = "black";
        }
        return new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/" + name + "_" + color + ".gif"));
    }

    @Override
    public void onCheck() {
        Runnable checkimage = () -> {
            try {
                Thread.sleep(100);
                getGameFrame().getGameBoard().check = true;
                Thread.sleep(2000);
                getGameFrame().getGameBoard().check = false;
            } catch (InterruptedException ex) {

            }
        };
        new Thread(checkimage).start();
    }

    @Override
    public void onCheckMate() {
        JOptionPane.showMessageDialog(gameframe, "Checkmate!", "Checkmate", JOptionPane.INFORMATION_MESSAGE);
        this.endGame();
    }

    @Override
    public void onStaleMate() {
        JOptionPane.showMessageDialog(gameframe, "Stalemate!", "Stalemate", JOptionPane.INFORMATION_MESSAGE);
        this.endGame();
    }

    @Override
    public void promotion(Pawn pawn) {
        Chessman man;
        String promotion = (String) JOptionPane.showInputDialog(gameframe, Translator.getBundle().getString("PROMOTION_HEAD"), Translator.getBundle().getString("PROMOTION_TEXT"), JOptionPane.QUESTION_MESSAGE, null, new String[]{Translator.getBundle().getString("CHESSMAN_QUEEN"), Translator.getBundle().getString("CHESSMAN_ROOK"), Translator.getBundle().getString("CHESSMAN_KNIGHT"), Translator.getBundle().getString("CHESSMAN_BISHOP")}, Translator.getBundle().getString("CHESSMAN_QUEEN"));
        switch (promotion) {
            case "ROOK":
                man = Rook.promotion(pawn, getGameState());
                break;
            case "KNIGHT":
                man = Knight.promotion(pawn, getGameState());
                break;
            case "BISHOP":
                man = Bishop.promotion(pawn, getGameState());
                break;
            default:
                man = Queen.promotion(pawn, getGameState());
                break;
        }
        getGameState().getChessmen(pawn.isBlack())[pawn.getPositionInArray()] = man;
        getGameState().getSquare(man.getX(), man.getY()).occupier = man;

        new ClientMovementUpdater(getGameState()).start();
    }

    @Override
    public void updateMovements() {
        getGameFrame().getGameBoard().movementsupdating = true;
        new ClientMovementUpdater(getGameState()).start();
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

    /**
     * Show all possible Moves for the currently selected chessman
     */
    private void showPossibleMoves() {
        if (selected.occupier != null) {
            picked = selected.occupier;
            List<Move> bewegungen = getGameState().getChessmen(picked.isBlack())[picked.getPositionInArray()].getMoves();
            List<Move> schläge = getGameState().getChessmen(picked.isBlack())[picked.getPositionInArray()].getCaptures();
            if (picked.getClass() == King.class) {
                List<CastlingMove> rochaden = ((King) picked).getCastlings(true, getGameState());
                rochaden.stream().forEach((CastlingMove c) -> {
                    getGameState().getSquare(c.getTargetX(), c.getTargetY()).highlight(Square.HIGHLIGHT.CASTLING);
                });

            }
            bewegungen.stream().forEach((Move m) -> {
                getGameState().getSquare(m.getTargetX(), m.getTargetY()).highlight(Square.HIGHLIGHT.MOVETARGET);
            });
            schläge.stream().forEach((Move m) -> {
                getGameState().getSquare(m.getTargetX(), m.getTargetY()).highlight(Square.HIGHLIGHT.CAPTURETARGET);
            });

        }
    }
}
