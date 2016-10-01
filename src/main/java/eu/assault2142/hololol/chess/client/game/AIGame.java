/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.client.game.ai.AlphaBetaAI;
import eu.assault2142.hololol.chess.client.game.ai.IAI;
import eu.assault2142.hololol.chess.game.chessmen.Move;

/**
 *
 * @author hololol2
 */
public class AIGame extends LocalGame {

    private IAI ai;
    private boolean aiblack = true;

    public AIGame() {
        super();
        ai = new AlphaBetaAI(getGameState(), true);
    }

    @Override
    public void finishedCalcs() {
        getGameState().resetHighlightedFields();
        if (getGameState().getTurn() == aiblack) {
            Move move = ai.bestMove();
            move.getChessman().doMove(move.getTargetX(), move.getTargetY());
            move.getChessman().doCapture(move.getTargetX(), move.getTargetY());
        } else {
            gameview.setMovementsUpdating(false);
        }
    }
}
