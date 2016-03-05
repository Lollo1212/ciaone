package eu.assault2142.hololol.chess.client.ki;

import eu.assault2142.hololol.chess.game.chessmen.Move;
import eu.assault2142.hololol.chess.game.GameSituation;


/**
 * Stores the number of a move and its rating
 * @author Johannes Wirth
 *
 */
@Deprecated
public class RatedMove{
	private GameSituation situation;
	private int rating;
        private Move m;
	/**
	 * Creates a new RatedMove
	 * @param gs the current situation
	 * @param rat the rating of the move
	 */
	public RatedMove(GameSituation gs, int rat,Move m){
            situation = gs;
            rating = rat;
	}
	/**
	 * Get the edge number
	 * @return the edge number of the move
	 */
	public Move getMovement(){
            return m;
	}
	/**
	 * Get the rating
	 * @return the rating of the move
	 */
	public int getRating(){
		return rating;
	}
}
