/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ui.GameFrame;
import eu.assault2142.hololol.chess.client.networking.ServerConnection;
import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import java.awt.EventQueue;
import javax.swing.ImageIcon;

/**
 *
 * @author jojo
 */
public class ClientGame extends Game {

    private GameFrame p;
    private ServerConnection client = null;

    public ClientGame(ServerConnection c, final int farbe) {//Spiel eines Clienten
        super(TYPE.CLIENT);
        client = c;
        final ClientGame g = this;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                p = new GameFrame(g);
                if (farbe == 0) {
                    //p.write(Start.START.texte[Start.START.einstellungen.sprache][37]+Start.START.texte[Start.START.einstellungen.sprache][0]);
                } else {
                    //p.write(Start.START.texte[Start.START.einstellungen.sprache][37]+Start.START.texte[Start.START.einstellungen.sprache][1]);
                }
            }
        });
    }

    @Override
    public ImageIcon getImage(Chessman.NAMES name, boolean black) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateMovements() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ServerConnection getClient() {
        return client;
    }

    @Override
    public void promotion(Pawn pawn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ServerConnection getConnection(){
        return client;
    }

}
