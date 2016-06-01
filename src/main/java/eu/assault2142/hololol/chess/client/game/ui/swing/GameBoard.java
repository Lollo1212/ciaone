package eu.assault2142.hololol.chess.client.game.ui.swing;

import eu.assault2142.hololol.chess.client.util.Translator;
import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.game.Settings;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Displays the gamestate-board and updates it
 *
 * @author hololol2
 */
public class GameBoard extends JPanel {

    protected boolean check = false;
    protected boolean movementsupdating = false;

    private ImageIcon backgroundCheckBlack;
    private ImageIcon backgroundCheckWhite;
    private ImageIcon backgroundTurnBlack;
    private ImageIcon backgroundTurnWhite;
    private ImageIcon fontCheckBlack;
    private ImageIcon fontCheckWhite;
    private ImageIcon fontTurnBlack;
    private ImageIcon fontTurnWhite;
    private final GameFrame gameframe;
    private final GameState gamestate;
    private Graphics graphics;

    private final int squarelength;

    /**
     * Create a new GameField
     *
     * @param gameframe the frame this GameField belongs to
     * @param state the gamestate to display
     */
    public GameBoard(GameFrame gameframe, GameState state) {
        gamestate = state;
        this.gameframe = gameframe;
        squarelength = gameframe.getSquareLength();
        loadImages();
        initTimer();
    }

    @Override
    public void paint(Graphics gr) {
        super.paint(gr);
        graphics = gr;
        paintEdgeZone();
        paintBoard();
        drawChessman();
        drawNotification();
    }

    /**
     * Paint the chessmen to their positions
     */
    private void drawChessman() {
        for (int i = 0; i <= 15; i++) {
            Chessman f = gamestate.getChessmen(true)[i];
            drawImage(gameframe.getImage(f), f.getX(), f.getY(), 1, 1);
        }
        for (int i = 0; i <= 15; i++) {
            Chessman f = gamestate.getChessmen(false)[i];
            drawImage(gameframe.getImage(f), f.getX(), f.getY(), 1, 1);
        }
    }

    /**
     * Draws an image to the gamestate-board
     *
     * @param image the image to paint
     * @param squareX the x-pos of the image (in gamestate-fields 0-7)
     * @param squareY the y-pos of the image (in gamestate-fields 0-7)
     * @param width the x-width of the image (in gamestate-fields 0-7)
     * @param height the y-width of the image (in gamestate-fields 0-7)
     */
    private void drawImage(ImageIcon image, int squareX, int squareY, int width, int height) {
        graphics.drawImage(image.getImage(), squareX * squarelength, squareY * squarelength, width * squarelength, height * squarelength, image.getImageObserver());
    }

    /**
     * Draw notifications like check and movements-updating
     */
    private void drawNotification() {
        if (movementsupdating) {
            if (gamestate.getTurn()) {
                drawImage(backgroundTurnBlack, 0, 0, 8, 8);
                drawImage(fontTurnBlack, 0, 0, 8, 8);
            } else {
                drawImage(backgroundTurnWhite, 0, 0, 8, 8);
                drawImage(fontTurnWhite, 0, 0, 8, 8);
            }
        }
        if (check) {
            if (gamestate.getTurn()) {
                drawImage(backgroundCheckBlack, 0, 0, 8, 8);
                drawImage(fontCheckBlack, 0, 0, 8, 8);
            } else {
                drawImage(backgroundCheckWhite, 0, 0, 8, 8);
                drawImage(fontCheckWhite, 0, 0, 8, 8);
            }
        }
    }

    /**
     * Initialize the Timer which repaints the board
     */
    private void initTimer() {
        int delay = 33;
        ActionListener taskPerformer = (ActionEvent evt) -> {
            gameframe.repaint();
        };
        new Timer(delay, taskPerformer).start();
    }

    /**
     * Load the images
     */
    private void loadImages() {
        String sprache = Translator.getLanguageString();
        backgroundTurnBlack = new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/bschwarz.gif"));
        backgroundTurnWhite = new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/bweiß.gif"));
        fontTurnWhite = new ImageIcon(getClass().getResource("/fonts/tweiß" + sprache + ".gif"));
        fontTurnBlack = new ImageIcon(getClass().getResource("/fonts/tschwarz" + sprache + ".gif"));
        fontCheckWhite = new ImageIcon(getClass().getResource("/fonts/tschachw" + sprache + ".gif"));
        fontCheckBlack = new ImageIcon(getClass().getResource("/fonts/tschachb" + sprache + ".gif"));
        backgroundCheckBlack = new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/bschachb.gif"));
        backgroundCheckWhite = new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/bschachw.gif"));
    }

    /**
     * Paint the board-background
     */
    private void paintBoard() {
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                graphics.setColor(gamestate.getSquare(x, y).currentColor);
                graphics.fillRect(x * squarelength, y * squarelength, squarelength, squarelength);
            }
        }
    }

    /**
     * Paint the edge-zone where captured chessmen are displayed
     */
    private void paintEdgeZone() {
        graphics.setColor(Color.RED);
        graphics.fillRect(8 * squarelength, 0, 2 * squarelength, 8 * squarelength);
        graphics.fillRect(10 * squarelength + 5, 0, 2 * squarelength, 8 * squarelength);

        graphics.setColor(Color.black);
        graphics.fillRect(10 * squarelength, 0, 5, 8 * squarelength);
    }
}
