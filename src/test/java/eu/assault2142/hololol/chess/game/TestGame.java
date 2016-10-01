/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.client.game.ClientMovementUpdater;
import eu.assault2142.hololol.chess.client.game.ai.AlphaBetaAI;
import eu.assault2142.hololol.chess.client.game.ui.GameViewFactory;
import eu.assault2142.hololol.chess.client.util.Settings;
import eu.assault2142.hololol.chess.client.util.Translator;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hololol2
 */
public class TestGame extends eu.assault2142.hololol.chess.client.game.Game {

    public TestGame(String pieces, boolean color, String castling, String enpassant) {
        super(TYPE.LOCAL);

        EventQueue.invokeLater(new Thread() {
            @Override
            public void run() {
                gameview = GameViewFactory.startGameView(TestGame.this, GameViewFactory.GameViews.SWING);
            }
        });
        try {
            gamestate = new TestGameState(this, pieces, color, castling, enpassant);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(TestGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(TestGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TestGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TestGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TestGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(TestGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void clickAt(int squareX, int squareY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void endGame() {
        System.exit(0);
    }

    @Override
    public void finishedCalcs() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCheck() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCheckMate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onStaleMate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void promotion(Pawn pawn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public final void updateMovements() {
        new ClientMovementUpdater(this).start();
    }

    public static void main(String[] args) {
        Settings.init("");
        Locale locale = Locale.ENGLISH;
        Locale.setDefault(locale);
        Translator.setLanguage(locale);
        Game game = new TestGame("3r1k2/4npp1/1ppr3p/p6P/P2PPPP1/1NR5/5K2/2R5", true, "-", "-");
        AlphaBetaAI ai = new AlphaBetaAI(game.getGameState(), true);
        Move move = ai.bestMove();
        System.out.println(move.getChessman().getType() + " " + move.getTargetX() + " " + move.getTargetY());
    }

}
