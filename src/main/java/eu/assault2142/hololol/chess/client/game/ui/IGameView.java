/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ui;

/**
 *
 * @author hololol2
 */
public interface IGameView {

    public void setMovementsUpdating(boolean b);

    public void setShowCheck(boolean b);

    public void hide();

    public void onCheckMate();

    public void onStaleMate();

    public String showPromotionChoice();

}
