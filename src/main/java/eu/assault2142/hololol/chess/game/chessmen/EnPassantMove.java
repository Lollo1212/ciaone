/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.game.chessmen;

/**
 *
 * @author hololol2
 */
public class EnPassantMove extends Move {

    private Pawn enemy;

    public EnPassantMove(int targetX, int targetY, Chessman chessman, Pawn e) {
        super(targetX, targetY, chessman);
        enemy = e;
    }

    public Pawn getEnemyPawn() {
        return enemy;
    }

}
