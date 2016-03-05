/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.AbstractChessman;

/**
 *
 * @author Jojo
 */
public class MovementUpdaterThread extends Thread{
    AbstractChessman f;
    public MovementUpdaterThread(AbstractChessman f){
        this.f=f;
    }
    @Override
    public void run(){
        f.updateMovements();
    }
}
