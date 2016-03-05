/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.server.game;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.server.networking.ClientConnection;
import javax.swing.ImageIcon;

/**
 *
 * @author jojo
 */
public class ServerGame extends Game{
    public ClientConnection client1 = null;
    public ClientConnection client2 = null;
    public ServerGame(ClientConnection a,ClientConnection b){//Spiel eines Lan-Servers
        super(TYPE.SERVER);
        client1=a;
        client2=b;
        new ServerMovementUpdater(this).start();
    }

    @Override
    public ImageIcon getImage(Chessman.NAMES name, boolean black) {
        return null;
    }

    @Override
    public void updateMovements() {
        new ServerMovementUpdater(this).start();
    }

    @Override
    public void promotion(Pawn pawn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ClientConnection getClient1() {
        return client1;
    }

    public ClientConnection getClient2() {
        return client2;
    }

    @Override
    public void finishedCalcs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
