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
    //the posX-coordinate
    private final int posX;
    //the posY-coordinate
    private final int posY;
    //the chessman on the field
    public Chessman occupier;

    /**
     * Create a new Square
     *
     * @param x the posX-coordinate
     * @param y the posY-coordinate
     */
    public Square(int x, int y) {
        this.posX = x;
        this.posY = y;
        setColor();
    }

    @Override
    public Square clone() {
        Square f = new Square(posX, posY);
        f.occupier = occupier;
        return f;
    }

    /**
     * Get the posX-coordinate
     *
     * @return the posX-coordinate
     */
    public int getX() {
        return posX;
    }

    /**
     * Get the posY-coordinate
     *
     * @return the posY-coordinate
     */
    public int getY() {
        return posY;
    }

    /**
     * Set the Color to light or dark depending on the coordinates
     */
    private void setColor() {
        if ((posX + posY) % 2 == 0) {
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
