/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.game;

/**
 *
 * @author jojo
 */
public class ServerMovementUpdater extends MovementUpdater {

    public ServerMovementUpdater(GameState g) {
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
    public void run() {
        updateMovements();
        testCheck();
    }

}
