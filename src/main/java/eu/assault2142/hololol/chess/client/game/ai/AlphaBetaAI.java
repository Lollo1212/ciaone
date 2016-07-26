/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ai;

import eu.assault2142.hololol.chess.client.util.Pair;
import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import java.util.List;

/**
 *
 * @author hololol2
 */
public class AlphaBetaAI implements IAI {

    private GameState currstate;
    private boolean black = true;

    public AlphaBetaAI(GameState state) {
        this.currstate = state;
    }

    @Override
    public Move bestMove() {
        Pair<Integer, Move> res = alphabeta(currstate, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, true, null);
        System.out.println(res.getLeft());
        return res.getRight();
    }

    public Pair<Integer, Move> alphabeta(GameState before, int depth, int alpha, int beta, boolean maximizingPlayer, Move move) {
        GameState state = move == null ? before : before.emulateMove(move.getChessman(), move.getTargetX(), move.getTargetY());
        if (depth == 0) {
            return new Pair(state.evaluateSituation(black), move);
        }
        if (maximizingPlayer) {
            Pair<Integer, Move> v = new Pair(Integer.MIN_VALUE, null);
            List<Move> moves = state.computeAllMoves(black);
            moves.addAll(state.computeAllCaptures(black));
            for (Move m : moves) {
                int abc = alphabeta(state, depth - 1, alpha, beta, false, m).getLeft();
                v = v.getLeft() >= abc ? v : new Pair(abc, m);
                alpha = Math.max(alpha, v.getLeft());
                if (beta <= alpha) {
                    break;
                }
            }
            return v;
        } else {
            Pair<Integer, Move> v = new Pair(Integer.MAX_VALUE, null);
            List<Move> moves = state.computeAllMoves(!black);
            moves.addAll(state.computeAllCaptures(!black));
            for (Move m : moves) {
                v = new Pair(Math.min(v.getLeft(), alphabeta(state, depth - 1, alpha, beta, true, m).getLeft()), m);
                beta = Math.min(beta, v.getLeft());
                if (beta <= alpha) {
                    break;
                }
            }
            return v;
        }
    }

}
