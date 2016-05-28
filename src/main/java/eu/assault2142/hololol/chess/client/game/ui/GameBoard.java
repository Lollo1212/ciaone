package eu.assault2142.hololol.chess.client.game.ui;

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
    private final GameState gamestate;
    private final GameFrame gameframe;
    private Graphics graphics;
    private ImageIcon ibackschachb;
    private ImageIcon ibackschachw;

    private ImageIcon ibackschwarz;
    private ImageIcon ibackweiß;
    private ImageIcon itextschachb;
    private ImageIcon itextschachw;
    private ImageIcon itextschwarz;
    private ImageIcon itextweiß;
    private final int squarelength;

    /**
     * Create a new GameField
     *
     * @param gf the frame this GameField belongs to
     * @param g the gamestate to display
     */
    public GameBoard(GameFrame gf, GameState g) {
        gamestate = g;
        this.gameframe = gf;
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
     * @param posx the x-pos of the image (in gamestate-fields 0-7)
     * @param posy the y-pos of the image (in gamestate-fields 0-7)
     * @param widthx the x-width of the image (in gamestate-fields 0-7)
     * @param widthy the y-width of the image (in gamestate-fields 0-7)
     */
    private void drawImage(ImageIcon image, int posx, int posy, int widthx, int widthy) {
        graphics.drawImage(image.getImage(), posx * squarelength, posy * squarelength, widthx * squarelength, widthy * squarelength, image.getImageObserver());
    }

    /**
     * Draw notifications like check and movements-updating
     */
    private void drawNotification() {
        if (movementsupdating) {
            if (gamestate.getTurn()) {
                drawImage(ibackschwarz, 0, 0, 8, 8);
                drawImage(itextschwarz, 0, 0, 8, 8);
            } else {
                drawImage(ibackweiß, 0, 0, 8, 8);
                drawImage(itextweiß, 0, 0, 8, 8);
            }
        }
        if (check) {
            if (gamestate.getTurn()) {
                drawImage(ibackschachb, 0, 0, 8, 8);
                drawImage(itextschachb, 0, 0, 8, 8);
            } else {
                drawImage(ibackschachw, 0, 0, 8, 8);
                drawImage(itextschachw, 0, 0, 8, 8);
            }
        }
    }

    /**
     * Initialize the Timer which repaints the board
     */
    private void initTimer() {
        int delay = 33; //milliseconds
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
        ibackschwarz = new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/bschwarz.gif"));
        ibackweiß = new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/bweiß.gif"));
        itextweiß = new ImageIcon(getClass().getResource("/bilder/tweiß" + sprache + ".gif"));
        itextschwarz = new ImageIcon(getClass().getResource("/bilder/tschwarz" + sprache + ".gif"));
        itextschachw = new ImageIcon(getClass().getResource("/bilder/tschachw" + sprache + ".gif"));
        itextschachb = new ImageIcon(getClass().getResource("/bilder/tschachb" + sprache + ".gif"));
        ibackschachb = new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/bschachb.gif"));
        ibackschachw = new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/bschachw.gif"));
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
