package eu.assault2142.hololol.chess.game.chessmen;

/**
 * Represents an EnPassant-move
 *
 * @author hololol2
 */
public class EnPassantMove extends Move {

    private final Pawn pawn;

    /**
     * Create a new EnPassantMove
     *
     * @param targetX the target x-coordinate
     * @param targetY the target y-coordinate
     * @param chessman the chessman which does the move
     * @param pawn the enemy pawn which will be captured
     */
    public EnPassantMove(int targetX, int targetY, Chessman chessman, Pawn pawn) {
        super(targetX, targetY, chessman);
        this.pawn = pawn;
    }

    /**
     * Get the enemy pawn
     *
     * @return the pawn which will be captured
     */
    public Pawn getEnemyPawn() {
        return pawn;
    }

}
