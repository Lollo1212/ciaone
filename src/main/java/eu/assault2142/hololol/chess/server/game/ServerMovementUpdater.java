/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.server.game;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.MovementUpdater;

/**
 *
 * @author jojo
 */
public class ServerMovementUpdater extends MovementUpdater{

    public ServerMovementUpdater(Game g) {
        super(g);
    }

    @Override
    protected void onStart() {
        
    }

    @Override
    public void testCheck() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onCheck() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onCheckMate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onStaleMate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
