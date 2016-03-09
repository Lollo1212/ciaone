/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.Chessman;

/**
 *
 * @author Jojo
 */
public class MovementUpdaterThread extends Thread {

    Chessman f;

    public MovementUpdaterThread(Chessman f) {
        this.f = f;
    }

    @Override
    public void run() {
        f.updateMovements();
    }
}
