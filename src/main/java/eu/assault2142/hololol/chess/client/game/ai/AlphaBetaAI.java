/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ai;

import eu.assault2142.hololol.chess.client.util.Pair;
import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
        List<Move> moves = currstate.computeAllMoves(black);
        moves.addAll(currstate.computeAllCaptures(black));
        int maxrating = Integer.MIN_VALUE;
        List<Move> best = new LinkedList();
        for (Move move : moves) {
            Pair<Integer, Move> res = alphabeta(currstate, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, move);
            if (res.getLeft() > maxrating) {
                maxrating = res.getLeft();
                best.clear();
                best.add(move);
            } else if (res.getLeft() == maxrating) {
                best.add(move);
            }
        }
        Random random = new Random();
        return best.get(random.nextInt(best.size()));
    }

    public Pair<Integer, Move> alphabeta(GameState before, int depth, int alpha, int beta, boolean maximizingPlayer, Move move) {
        GameState state = move == null ? before : before.emulateMove(move.getChessman(), move.getTargetX(), move.getTargetY());
        if (depth == 0) {
            return new Pair(state.evaluateSituation(black), move);
        }
        if (maximizingPlayer) {
            Pair<Integer, Move> v = new Pair(Integer.MIN_VALUE, null);
            List<Move> moves = state.computeAllCaptures(black);
            moves.addAll(state.computeAllMoves(black));
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
                int abc = alphabeta(state, depth - 1, alpha, beta, true, m).getLeft();
                v = v.getLeft() <= abc ? v : new Pair(abc, m);
                beta = Math.min(beta, v.getLeft());
                if (beta <= alpha) {
                    break;
                }
            }
            return v;
        }
    }

}
