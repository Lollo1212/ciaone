package eu.assault2142.hololol.chess.game.chessmen;

/**
 * Represents a EnPassant-move
 *
 * @author hololol2
 */
public class EnPassantMove extends Movement {

    private final Pawn enemy;

    /**
     * Create a new EnPassantMove
     *
     * @param targetX the target x-coordinate
     * @param targetY the target y-coordinate
     * @param chessman the chessman which does the move
     * @param e the enemy pawn which will be captured
     */
    public EnPassantMove(int targetX, int targetY, Chessman chessman, Pawn e) {
        super(targetX, targetY, chessman);
        enemy = e;
    }

    /**
     * Get the enemy pawn
     *
     * @return the pawn which will be captured
     */
    public Pawn getEnemyPawn() {
        return enemy;
    }

}
