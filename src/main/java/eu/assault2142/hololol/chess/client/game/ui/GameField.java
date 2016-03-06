/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ui;

import eu.assault2142.hololol.chess.client.game.Settings;
import eu.assault2142.hololol.chess.client.translator.Translator;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
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
    
    private ImageIcon ibackschwarz;
    private ImageIcon ibackweiß;
    private ImageIcon itextweiß;
    private ImageIcon itextschwarz;
    private ImageIcon itextschachw;
    private ImageIcon itextschachb;
    private ImageIcon ibackschachw;
    private ImageIcon ibackschachb;
    
    private Graphics graphics;

    public GameField(GameFrame gf) {
        this.gameframe = gf;
        squarelength = gameframe.getFieldLength();
        initTimer();
        loadImages();
    }

    private void initTimer() {
        int delay = 33; //milliseconds
        ActionListener taskPerformer = (ActionEvent evt) -> {
            gameframe.repaint();
        };
        new Timer(delay, taskPerformer).start();
    }
    
    private void loadImages(){
        String sprache = Translator.TRANSLATOR.getLanguage();
        ibackschwarz=new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder+"/bschwarz.gif"));
        ibackweiß=new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder+"/bweiß.gif"));
        itextweiß=new ImageIcon(getClass().getResource("/bilder/tweiß"+sprache+".gif"));
        itextschwarz=new ImageIcon(getClass().getResource("/bilder/tschwarz"+sprache+".gif"));
        itextschachw=new ImageIcon(getClass().getResource("/bilder/tschachw"+sprache+".gif"));
        itextschachb=new ImageIcon(getClass().getResource("/bilder/tschachb"+sprache+".gif"));
        ibackschachb=new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder+"/bschachb.gif"));
        ibackschachw=new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder+"/bschachw.gif"));
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

    public void paintEdgeZone() {
        graphics.setColor(Color.RED);
        graphics.fillRect(8 * squarelength, 0, 2 * squarelength, 8 * squarelength);
        graphics.fillRect(10 * squarelength + 5, 0, 2 * squarelength, 8 * squarelength);

        graphics.setColor(Color.black);
        graphics.fillRect(10 * squarelength, 0, 5, 8 * squarelength);
    }

    public void paintBoard() {//Malt den Schachfeldhintergrund
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                graphics.setColor(gameframe.getGame().getSquare(x, y).currentColor);
                graphics.fillRect(x * squarelength, y * squarelength, squarelength, squarelength);
            }
        }
    }

    public void drawChessman() {//Alle Figuren auf des Feld zeichenen
        for (int i = 0; i <= 15; i++) {
            Chessman f = gameframe.getGame().getFiguren(true)[i];
            drawImage(f.getImage(),f.getX(),f.getY(),1,1);
        }
        for (int i = 0; i <= 15; i++) {
            Chessman f = gameframe.getGame().getFiguren(false)[i];
            drawImage(f.getImage(),f.getX(),f.getY(),1,1);
        }
    }

    public void drawNotification() {
        if (movementsupdating) {
            if (gameframe.getGame().getTurn()) {
                drawImage(ibackschwarz,0,0,8,8);
                drawImage(itextschwarz,0,0,8,8);
            } else {
                drawImage(ibackweiß,0,0,8,8);
                drawImage(itextweiß,0,0,8,8);
            }
        }
        if (check) {
            if (gameframe.getGame().getTurn()) {
                drawImage(ibackschachb,0,0,8,8);
                drawImage(itextschachb,0,0,8,8);
            } else {
                drawImage(ibackschachw,0,0,8,8);
                drawImage(itextschachw,0,0,8,8);
            }
        }
    }
    
    private void drawImage(ImageIcon image,int posx,int posy,int widthx,int widthy){
        graphics.drawImage(image.getImage(), posx*squarelength, posy*squarelength,widthx*squarelength,widthy*squarelength, image.getImageObserver());
    }
}
