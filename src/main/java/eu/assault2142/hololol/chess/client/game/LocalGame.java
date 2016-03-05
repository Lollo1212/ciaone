/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.client.game.ui.GameFrame;
import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import javax.swing.ImageIcon;

/**
 *
 * @author jojo
 */
public class LocalGame extends Game {

    private GameFrame p;
    public boolean bot;

    public LocalGame() {//Lokales Spiel
        super(TYPE.LOCAL);
        p = new GameFrame(this);
        new ClientMovementUpdater(this).start();
    }

    public GameFrame getGameFrame() {
        return p;
    }

    

    @Override
    protected Chessman[] buildChessmen(boolean black,Square[] squares) {
            Settings.SETTINGS.dark = Settings.SETTINGS.darkColors[Settings.SETTINGS.skin];
            Settings.SETTINGS.light = Settings.SETTINGS.lightColors[Settings.SETTINGS.skin];
            Settings.SETTINGS.chessmenFolder = Settings.SETTINGS.chessmenFolders[Settings.SETTINGS.skin];
        return super.buildChessmen(black,squares);
    }

    @Override
    public ImageIcon getImage(Chessman.NAMES name, boolean black) {
        String color = "white";
        if(black) color = "black";
        return new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/"+name+"_"+color+".gif"));
    }

    @Override
    public void updateMovements() {
        new ClientMovementUpdater(this).start();
    }

    @Override
    public void promotion(Pawn pawn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
