package eu.assault2142.hololol.chess.client.ki;

import eu.assault2142.hololol.chess.game.chessmen.Move;
import eu.assault2142.hololol.chess.game.GameSituation;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A fast KI-Implementation with Random- and MinMax-KI
 *
 * @author Johannes Wirth
 *
 */
public class KI {

    // the board

    private GameSituation situation;
    // stores the rated moves. filled by the sub-threads
    private RatedMove[] nextmoves;
    // how much sub-threads are still calculating
    private int remaining;
    // to sync the access to nextmoves and remaining
    private final Object LOCK = new Object();
    // to make the mainthread wait for the sub-threads to finish
    private final Object WAIT = new Object();
    // the total number of calculations the KI can do in 3 seconds
    private static final long TOTALCALCS = 18000 * 3000;

    public void init(GameSituation gs) {
        situation = gs;
    }

    public Move nextMove(boolean p1) {
        // Not yet initialized
        if (situation == null) {
            return null;
        }
        RatedMove move = null;
        try {
            move = bestMoveRec(p1, 6);
        } catch (InterruptedException ex) {
            Logger.getLogger(KI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return move.getMovement();
    }

    /**
     * Calculates the best next move recursively
     *
     * @param p1 whether it is player1's turn
     * @param depth the remaining depth of the search-tree
     * @return an object containing the edge and the rating of the best move
     * @throws EdgeNotExistsException
     * @throws EdgeNotFreeException
     * @throws InterruptedException
     */
    private RatedMove bestMoveRec(boolean p1, int depth)
            throws InterruptedException {
        Stack<Move> moves = new Stack();
        int total = 0;
        for(Move m:situation.getAllMoves(p1)){
            moves.push(m);
            total++;
        }
        for(Move m:situation.getAllCaptures(p1)){
            moves.push(m);
            total++;
        }
        remaining = total;
        nextmoves = new RatedMove[remaining];
        // Sets the player, whiches turn it is
        // Iterate over all possible moves
        Move m;
        while (!moves.isEmpty()) {
            m = moves.pop();
            new KIThread(situation, !p1, p1, depth - 1, m).start();
        }
        synchronized (WAIT) {
            while (remaining != 0) {
                WAIT.wait();
            }
        }
        // the rating of the current move
        int ratemove = 0;
        // the best rating seen
        int rating;
        rating = Integer.MIN_VALUE;
        List<Move> best = new LinkedList();
        for (RatedMove rm : nextmoves) {
            ratemove = rm.getRating();

            if (ratemove > rating) {
                rating = ratemove;
                best.clear();
            }
            if (ratemove == rating) {
                best.add(rm.getMovement());
            }
        }
        // return a random rated move with the best rating
        return new RatedMove(situation,rating,getRandomMove(best));
    }

    /**
     * Returns a random move from the list
     *
     * @param moves the list of possible moves
     * @return a random element of the list
     */
    private Move getRandomMove(List<Move> moves) {
        if (moves.isEmpty()) {
            return null;
        }
        Random r = new Random();
        return moves.get(r.nextInt(moves.size()));
    }

    /**
     * Rates the given situation
     *
     * @param field the situation to rate
     * @param p1 whether to rate it for player1
     * @return an int representing the rating (higher is better)
     */
    private int rateSituation(GameSituation gs, boolean p1) {
        return 0;
    }

    /**
     * Calculates the rating of move recursively in a sub-thread
     *
     * @author Johannes Wirth
     *
     */
    private class KIThread extends Thread {
        private GameSituation situation;
        //Stores whether it's player1's turn and if the KI is player1
        private boolean p1, kip1;
        //Stores how far to traverse into the tree
        private int depth;

        /**
         * Creates a new KIThread
         *
         * @param f the current board
         * @param p1 whether it's player1's turn
         * @param kip1 whether player1 is the KI
         * @param depth how far to traverse into the tree
         * @param player the player which does the first move
         * @param edgenum the number of the first move
         */
        private KIThread(GameSituation gs, boolean p1, boolean kip1, int depth, Move m) {
            this.p1 = p1;
            this.kip1 = kip1;
            this.depth = depth;
            //clone the board so changes don't affect he original board
            situation = gs.doMove(m);
            //set priority lower
            setPriority(3);
        }

        @Override
        public void run() {
                RatedMove move;
                move = bestMoveRec(situation,!p1, depth-1);
                //store whether all other sub-threads are finished
                boolean finished;
                //Store the result in the array (has to be synced)
                synchronized (LOCK) {
                    nextmoves[remaining - 1] = move;
                    remaining--;
                    finished = remaining == 0;
                }
                //if all sub-threads have finished notify the main-thread
                synchronized (WAIT) {
                    if (finished) {
                        WAIT.notifyAll();
                    }
                }
        }

        /**
         * Rates the nextMoves recursively
         *
         * @param p1 whether it's player1's turn
         * @param depth the remaining depth
         * @return the best move and its rating
         * @throws EdgeNotExistsException
         * @throws EdgeNotFreeException
         */
        private RatedMove bestMoveRec(GameSituation gs,boolean p1, int depth){
            // Reached maximal depth, calculate current rating
            if (depth == 0) {
                return new RatedMove(gs, rateSituation(gs, kip1),null);
            }

            Stack<Move> moves = new Stack();
        for(Move m:situation.getAllMoves(p1)){
            moves.push(m);
        }
        for(Move m:situation.getAllCaptures(p1)){
            moves.push(m);
        }
            // the rating of the current move
            int ratemove = 0;
            // the best rating seen
            int rating;
            if (kip1 == p1) {
                rating = Integer.MIN_VALUE;
            } else {
                rating = Integer.MAX_VALUE;
            }
            Stack<Move> best = new Stack();
            // Iterate over all possible moves
            Move m;
            while (!moves.isEmpty()) {
                m = moves.pop();
		
                    ratemove = bestMoveRec(gs.doMove(m),p1, depth).getRating();
                // Check whether the KI or the enemy does this move
                if (kip1 == p1) {
					// Get the moves with the best rating, if it is the KI's
                    // turn
                    if (ratemove > rating) {
                        rating = ratemove;
                        best.clear();
                    }
                    if (ratemove == rating) {
                        best.push(m);
                    }
                } else {
                    // Get the moves with the lowest rating if not
                    if (ratemove < rating) {
                        rating = ratemove;
                        best.clear();
                    }
                    if (ratemove == rating) {
                        best.push(m);
                    }
                }
            }
            // return a random rated move with the best rating
            return new RatedMove(gs, rating,best.pop());
        }
    }
}
