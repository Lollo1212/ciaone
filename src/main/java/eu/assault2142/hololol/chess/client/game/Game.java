/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ui.GameFrame;
import eu.assault2142.hololol.chess.client.menus.MainMenu;
import eu.assault2142.hololol.chess.game.Settings;
import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.game.chessmen.CastlingMove;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author hololol2
 */
public abstract class Game extends eu.assault2142.hololol.chess.game.Game {

    protected GameFrame gameframe;
    protected Chessman picked;
    protected Square selected;

    public Game(TYPE type) {
        super(type);
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

    /**
     * Show all possible Moves for the currently selected chessman
     */
    protected void showPossibleMoves() {
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
