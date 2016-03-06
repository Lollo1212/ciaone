/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ui;

import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author jojo
 */
public class GameField extends JPanel {//eigentliches Spielfeld

    GameFrame gameframe;
    protected int squarelength;
    public boolean movementsupdating = false;
    public boolean check = false;

    public GameField(GameFrame gf) {
        this.gameframe = gf;
        squarelength = gameframe.getFieldLength();
        init();
    }

    private void init() {
        int delay = 33; //milliseconds
        ActionListener taskPerformer = (ActionEvent evt) -> {
            gameframe.repaint();
        };
        new Timer(delay, taskPerformer).start();
    }

    @Override
    public void paint(final Graphics gr) {
        super.paint(gr);
        paintEdgeZone(gr);
        paintBoard(gr);
        drawChessman(gr);
        drawNotification(gr);
    }

    public void paintEdgeZone(Graphics gr) {
        gr.setColor(Color.RED);
        gr.fillRect(8 * squarelength, 0, 2 * squarelength, 8 * squarelength);
        gr.fillRect(10 * squarelength + 5, 0, 2 * squarelength, 8 * squarelength);

        gr.setColor(Color.black);
        gr.fillRect(10 * squarelength, 0, 5, 8 * squarelength);
    }

    public void paintBoard(Graphics g) {//Malt den Schachfeldhintergrund
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                g.setColor(gameframe.game.getSquare(x, y).currentColor);
                g.fillRect(x * squarelength, y * squarelength, squarelength, squarelength);
            }
        }
    }

    public void drawChessman(Graphics g) {//Alle Figuren auf des Feld zeichenen
        for (int i = 0; i <= 15; i++) {
            Chessman f = gameframe.game.getFiguren(true)[i];

            g.drawImage(f.getImage().getImage(), f.getX() * squarelength, f.getY() * squarelength, squarelength, squarelength, f.getImage().getImageObserver());

        }
        for (int i = 0; i <= 15; i++) {
            Chessman f = gameframe.game.getFiguren(false)[i];

            g.drawImage(f.getImage().getImage(), f.getX() * squarelength, f.getY() * squarelength, squarelength, squarelength, f.getImage().getImageObserver());

        }
    }

    public void drawNotification(Graphics gr) {
        if (movementsupdating) {
            if (gameframe.game.getTurn()) {
                gr.drawImage(gameframe.ibackschwarz.getImage(), 0, 0, squarelength * 8, squarelength * 8, gameframe.ibackschwarz.getImageObserver());
                gr.drawImage(gameframe.itextschwarz.getImage(), 0, 0, squarelength * 8, squarelength * 8, gameframe.itextschwarz.getImageObserver());
            } else {
                gr.drawImage(gameframe.ibackweiß.getImage(), 0, 0, squarelength * 8, squarelength * 8, gameframe.ibackweiß.getImageObserver());
                gr.drawImage(gameframe.itextweiß.getImage(), 0, 0, squarelength * 8, squarelength * 8, gameframe.itextweiß.getImageObserver());
            }
        }
        if (check) {
            if (gameframe.game.getTurn()) {
                gr.drawImage(gameframe.ibackschachb.getImage(), 0, 0, squarelength * 8, squarelength * 8, gameframe.ibackschwarz.getImageObserver());
                gr.drawImage(gameframe.itextschachb.getImage(), 0, 0, squarelength * 8, squarelength * 8, gameframe.itextschachb.getImageObserver());
            } else {
                gr.drawImage(gameframe.ibackschachw.getImage(), 0, 0, squarelength * 8, squarelength * 8, gameframe.ibackweiß.getImageObserver());
                gr.drawImage(gameframe.itextschachw.getImage(), 0, 0, squarelength * 8, squarelength * 8, gameframe.itextschachw.getImageObserver());
            }
        }
    }
}
