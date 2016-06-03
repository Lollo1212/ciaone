package eu.assault2142.hololol.chess.game.chessmen;

/**
 * Represents a Move or Capture
 *
 * @author hololol2
 */
public class Move {

    //the x-coordinate of the target-square
    int targetX;
    //the y-coordinate of the target-square
    int targetY;
    //the chessman which does the move
    Chessman chessman;

    /**
     *
     * @param targetX the target x-coordinate
     * @param targetY the target y-coordinate
     * @param chessman the chessman
     */
    public Move(int targetX, int targetY, Chessman chessman) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.chessman = chessman;
    }

    /**
     *
     * @return the target x-coordinate
     */
    public int getTargetX() {
        return targetX;
    }

    /**
     *
     * @return the target y-coordinate
     */
    public int getTargetY() {
        return targetY;
    }

    /**
     *
     * @return the chessman
     */
    public Chessman getChessman() {
        return chessman;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other.getClass() == Move.class) {
            Move move = (Move) other;
            return move.targetX == targetX && move.targetY == targetY && move.chessman.equals(chessman);
        } else {
            return super.equals(other);
        }
    }
}
