package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import java.awt.Color;

/**
 * Represents a square on the chessboard
 *
 * @author hololol2
 */
public class Square {

    public static enum HIGHLIGHT {
        MOVETARGET, CAPTURETARGET, CASTLING, SELECTED
    };

    public Color baseColor;
    public Color currentColor;
    //the x-coordinate
    private final int x;
    //the y-coordinate
    private final int y;
    //the chessman on the field
    public Chessman occupier;

    /**
     * Create a new Square
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Square(int x, int y) {
        this.x = x;
        this.y = y;
        setColor();
    }

    @Override
    public Square clone() {
        Square f = new Square(x, y);
        f.occupier = occupier;
        return f;
    }

    /**
     * Get the x-coordinate
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y-coordinate
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Set the Color to light or dark depending on the coordinates
     */
    private void setColor() {
        if ((x + y) % 2 == 0) {
            baseColor = Settings.SETTINGS.light;
        } else {
            baseColor = Settings.SETTINGS.dark;
        }
        currentColor = baseColor;
    }

    /**
     * Check whether the square is occupied
     *
     * @return true if the square is occupied, false otherwise
     */
    public boolean isOccupied() {
        return occupier != null;
    }

    /**
     * Check whether the square is occupied by a given player
     *
     * @param black the color of the player
     * @return true if the square is occupied by the player, false otherwise
     */
    public boolean isOccupiedByColor(boolean black) {
        return isOccupied() && occupier.isBlack() == black;
    }

    /**
     * Highlights the square with the given color
     *
     * @param color the color to highlight with
     */
    public void highlight(HIGHLIGHT color) {
        switch (color) {
            case MOVETARGET:
                currentColor = Color.BLUE;
                break;
            case CAPTURETARGET:
                currentColor = Color.RED;
                break;
            case CASTLING:
                currentColor = Color.MAGENTA;
                break;
            default:
                currentColor = Color.CYAN;
                break;
        }
    }

    /**
     * Resets the hightlighting
     */
    public void resetHighlighting() {
        currentColor = baseColor;
    }
}
