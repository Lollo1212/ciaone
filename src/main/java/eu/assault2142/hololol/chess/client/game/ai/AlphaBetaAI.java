/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ai;

import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import java.util.List;

/**
 *
 * @author hololol2
 */
public class AlphaBetaAI implements IAI {

    private GameState state;
    private boolean black = true;

    @Override
    public Move bestMove() {
        alphabeta(state, 10, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return null;
    }

    public int alphabeta(GameState state, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0) {
            return state.evaluateSituation(black);
        }
        if (maximizingPlayer) {
            int v = Integer.MIN_VALUE;
            List<Move> moves = state.getAllMoves(black);
            for (Move m : moves) {
                v = Math.max(v, alphabeta(state.emulateMove(m.getChessman(), m.getTargetX(), m.getTargetY()), depth - 1, alpha, beta, false));
                alpha = Math.max(alpha, v);
                if (beta <= alpha) {
                    break;
                }
            }
            return v;
        } else {
            int v = Integer.MAX_VALUE;
            List<Move> moves = state.getAllMoves(!black);
            for (Move m : moves) {
                v = Math.min(v, alphabeta(state.emulateMove(m.getChessman(), m.getTargetX(), m.getTargetY()), depth - 1, alpha, beta, true));
                beta = Math.min(beta, v);
                if (beta <= alpha) {
                    break;
                }
            }
            return v;
        }
    }

}
