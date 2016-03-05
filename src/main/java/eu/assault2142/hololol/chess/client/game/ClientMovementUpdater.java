/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.MovementUpdater;

/**
 *
 * @author jojo
 */
public class ClientMovementUpdater extends MovementUpdater{
    
    public ClientMovementUpdater(Game g) {
        super(g);
    }
    
    @Override
    public void onStart(){
        ((LocalGame)getGame()).getGameFrame().getGameField().movementsupdating=true;
    }

    @Override
    public void testCheck() {
        super.testCheck();
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
