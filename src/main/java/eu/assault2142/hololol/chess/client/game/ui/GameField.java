/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ui;

import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.client.game.Settings;
import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author jojo
 */
public class GameField extends JPanel {//eigentliches Spielfeld

    GameFrame jf;
    private Chessman angewählt = null;
    protected int x;
    public boolean movementsupdating = false;
    public boolean schach = false;

    public GameField(GameFrame gf) {
        this.jf = gf;
        x = jf.getFieldLength();
        Runnable repaint = () -> {
            try {
                while(true){
                    gf.repaint();
                    Thread.sleep(10);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(GameField.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        new Thread(repaint).start();
    }

    @Override
    public void paint(final Graphics gr) {
        super.paint(gr);
        //System.out.println("Painting");
        gr.setColor(new Color(255, 0, 0));
        gr.fillRect(8 * x, 0, 2 * x, 8 * x);
        gr.fillRect(10 * x + 5, 0, 2 * x, 8 * x);
        gr.setColor(Color.black);
        gr.fillRect(10 * x, 0, 5, 8 * x);
        zurückSetzen(gr);
        for (int t = 0; t < 8; t++) {
            for (int y = 0; y < 8; y++) {
                Square f = jf.game.getSquare(t,y);
                if (f.posmove == true) {
                    gr.setColor(Color.BLUE);
                    gr.fillRect(x * t, x * y, x, x);
                } else if (f.postarget == true) {
                    gr.setColor(Color.RED);
                    gr.fillRect(x * t, x * y, x, x);
                } else if (f.castling == true) {
                    gr.setColor(Color.MAGENTA);
                    gr.fillRect(x * t, x * y, x, x);
                } else if (f.selected == true) {
                    gr.setColor(Color.CYAN);
                    gr.fillRect(x * t, x * y, x, x);
                }
            }
        }
        drawFiguren(gr);
        if (movementsupdating) {
            if (jf.game.getTurn()) {
                gr.drawImage(jf.ibackschwarz.getImage(), 0, 0, x * 8, x * 8, jf.ibackschwarz.getImageObserver());
                gr.drawImage(jf.itextschwarz.getImage(), 0, 0, x * 8, x * 8, jf.itextschwarz.getImageObserver());
            } else {
                gr.drawImage(jf.ibackweiß.getImage(), 0, 0, x * 8, x * 8, jf.ibackweiß.getImageObserver());
                gr.drawImage(jf.itextweiß.getImage(), 0, 0, x * 8, x * 8, jf.itextweiß.getImageObserver());
            }
        }
        if (schach) {
            if (jf.game.getTurn()) {
                gr.drawImage(jf.ibackschachb.getImage(), 0, 0, x * 8, x * 8, jf.ibackschwarz.getImageObserver());
                gr.drawImage(jf.itextschachb.getImage(), 0, 0, x * 8, x * 8, jf.itextschachb.getImageObserver());
            } else {
                gr.drawImage(jf.ibackschachw.getImage(), 0, 0, x * 8, x * 8, jf.ibackweiß.getImageObserver());
                gr.drawImage(jf.itextschachw.getImage(), 0, 0, x * 8, x * 8, jf.itextschachw.getImageObserver());
            }
        }
    }

    public void zurückSetzen(Graphics g) {//Malt den Schachfeldhintergrund

        g.setColor(Settings.SETTINGS.light);
        g.fillRect(0, 0, x, x);
        g.fillRect(2 * x, 0, x, x);
        g.fillRect(4 * x, 0, x, x);
        g.fillRect(6 * x, 0, x, x);

        g.fillRect(x, x, x, x);
        g.fillRect(3 * x, x, x, x);
        g.fillRect(5 * x, x, x, x);
        g.fillRect(7 * x, x, x, x);

        g.fillRect(0, 2 * x, x, x);
        g.fillRect(2 * x, 2 * x, x, x);
        g.fillRect(4 * x, 2 * x, x, x);
        g.fillRect(6 * x, 2 * x, x, x);

        g.fillRect(x, 3 * x, x, x);
        g.fillRect(3 * x, 3 * x, x, x);
        g.fillRect(5 * x, 3 * x, x, x);
        g.fillRect(7 * x, 3 * x, x, x);

        g.fillRect(0, 4 * x, x, x);
        g.fillRect(2 * x, 4 * x, x, x);
        g.fillRect(4 * x, 4 * x, x, x);
        g.fillRect(6 * x, 4 * x, x, x);

        g.fillRect(x, 5 * x, x, x);
        g.fillRect(3 * x, 5 * x, x, x);
        g.fillRect(5 * x, 5 * x, x, x);
        g.fillRect(7 * x, 5 * x, x, x);

        g.fillRect(0, 6 * x, x, x);
        g.fillRect(2 * x, 6 * x, x, x);
        g.fillRect(4 * x, 6 * x, x, x);
        g.fillRect(6 * x, 6 * x, x, x);

        g.fillRect(x, 7 * x, x, x);
        g.fillRect(3 * x, 7 * x, x, x);
        g.fillRect(5 * x, 7 * x, x, x);
        g.fillRect(7 * x, 7 * x, x, x);

        g.setColor(Settings.SETTINGS.dark);
        g.fillRect(x, 0, x, x);
        g.fillRect(3 * x, 0, x, x);
        g.fillRect(5 * x, 0, x, x);
        g.fillRect(7 * x, 0, x, x);

        g.fillRect(0, x, x, x);
        g.fillRect(2 * x, x, x, x);
        g.fillRect(4 * x, x, x, x);
        g.fillRect(6 * x, x, x, x);

        g.fillRect(x, 2 * x, x, x);
        g.fillRect(3 * x, 2 * x, x, x);
        g.fillRect(5 * x, 2 * x, x, x);
        g.fillRect(7 * x, 2 * x, x, x);

        g.fillRect(0, 3 * x, x, x);
        g.fillRect(2 * x, 3 * x, x, x);
        g.fillRect(4 * x, 3 * x, x, x);
        g.fillRect(6 * x, 3 * x, x, x);

        g.fillRect(x, 4 * x, x, x);
        g.fillRect(3 * x, 4 * x, x, x);
        g.fillRect(5 * x, 4 * x, x, x);
        g.fillRect(7 * x, 4 * x, x, x);

        g.fillRect(0, 5 * x, x, x);
        g.fillRect(2 * x, 5 * x, x, x);
        g.fillRect(4 * x, 5 * x, x, x);
        g.fillRect(6 * x, 5 * x, x, x);

        g.fillRect(x, 6 * x, x, x);
        g.fillRect(3 * x, 6 * x, x, x);
        g.fillRect(5 * x, 6 * x, x, x);
        g.fillRect(7 * x, 6 * x, x, x);

        g.fillRect(0, 7 * x, x, x);
        g.fillRect(2 * x, 7 * x, x, x);
        g.fillRect(4 * x, 7 * x, x, x);
        g.fillRect(6 * x, 7 * x, x, x);
    }

    public void drawFiguren(Graphics g) {//Alle Figuren auf des Feld zeichenen
        for (int i = 0; i <= 15; i++) {
            Chessman f = jf.game.getFiguren(true)[i];

            g.drawImage(f.getImage().getImage(), f.getX() * x, f.getY() * x, x, x, f.getImage().getImageObserver());

        }
        for (int i = 0; i <= 15; i++) {
            Chessman f = jf.game.getFiguren(false)[i];

            g.drawImage(f.getImage().getImage(), f.getX() * x, f.getY() * x, x, x, f.getImage().getImageObserver());

        }
    }

    public void drawImage(ImageIcon i, int x, int y, Graphics g) {//benutzt um eine Figur an bestimmte Stelle zu zeichnen
        int a = 0;
        if (x == 10 || x == 11) {
            a = 5;
        }
        x = jf.getFieldLength() * x;
        y = jf.getFieldLength() * y;
        x += a;
        g.drawImage(i.getImage(), x, y, jf.getFieldLength(), jf.getFieldLength(), i.getImageObserver());
    }

}
