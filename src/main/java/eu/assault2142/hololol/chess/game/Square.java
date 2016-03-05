package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.Chessman;

/**
 * Represents a square on the chessboard
 *
 * @author hololol2
 */
public class Square {

    //whether the square is a castling target
    public boolean castling = false;
    //whether this square is a move target
    public boolean posmove = false;
    //whether this square is a capture target
    public boolean postarget = false;
    //whether the square is selected
    public boolean selected = false;
    //whether this is a light field
    public boolean light;
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
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
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
        this.light = false;
        if (x == 0 || x == 2 || x == 4 || x == 6) {
            if (y == 0 || y == 2 || y == 4 || y == 6) {
                this.light = true;
            }
        }
        if (x == 1 || x == 3 || x == 5 || x == 7) {
            if (y == 1 || y == 3 || y == 5 || y == 7) {
                this.light = true;
            }
        }
    }
    
    public boolean isOccupied(){
        return occupier!=null;
    }
    public boolean isOccupiedByColor(boolean black){
        return isOccupied()&&occupier.isBlack()==black;
    }
}
