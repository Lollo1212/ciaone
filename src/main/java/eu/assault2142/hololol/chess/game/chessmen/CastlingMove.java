package eu.assault2142.hololol.chess.game.chessmen;

/**
 * Represents a castling-move
 *
 * @author hololol2
 */
public class CastlingMove extends Move {

    //The rook involved
    private Rook rook;
    //the targetX of the rook
    private int rookX;
    //the targetY of the rook
    private int rookY;

    /**
     * Create a new Castling-Move
     *
     * @param kingx the targetX of the king
     * @param kingY the targetY of the king
     * @param rook the rook
     * @param rookX the targetX of the rook
     * @param rookY the targetY of the rook
     * @param king the king
     */
    public CastlingMove(int kingx, int kingY, Rook rook, int rookX, int rookY, King king) {
        super(kingx, kingY, king);
        this.rook = rook;
        this.rookX = rookX;
        this.rookY = rookY;
    }

    /**
     * Check if 2 castling-moves are equivalent
     *
     * @param rm the move to compare to
     * @return true if equal, false otherwise
     */
    public boolean equals(CastlingMove rm) {
        return rm.rook == rook && rm.rookX == rookX && rm.rookY == rookY && super.equals(rm);
    }

    public Rook getRook() {
        return rook;
    }

    public int getRookX() {
        return rookX;
    }

    public int getRookY() {
        return rookY;
    }
}
