/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ui;

import eu.assault2142.hololol.chess.client.game.Game;
import eu.assault2142.hololol.chess.client.game.ui.monkey.MonkeyGameView;
import eu.assault2142.hololol.chess.client.game.ui.swing.GameFrame;

/**
 *
 * @author jojow
 */
public class GameViewFactory {
    
    public enum GameViews { SWING, JMONKEY };
    
    public static IGameView startGameView(Game game, GameViews type){
        switch(type){
            case JMONKEY:
                MonkeyGameView view = new MonkeyGameView(game);
                view.start();
                return view;
            default:
                return new GameFrame(game);
        }
    }
}
