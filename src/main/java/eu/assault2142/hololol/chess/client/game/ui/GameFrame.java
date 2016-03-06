/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ui;

import eu.assault2142.hololol.chess.client.game.ClientGame;
import eu.assault2142.hololol.chess.client.menus.MainMenu;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.Game;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author jojo
 */
public class GameFrame extends JFrame implements MouseListener {//Frame, in dem das Schachfeld eingebettet ist

    private final GridBagLayout layout;
    private final GridBagConstraints constraints;
    private final Game game;
    private static Chessman picked = null;//beim letzten Klick angecklickte Figur
    private int squarelength;//Länge eines Kästchen auf dem Schachfeld
    private GameField gamefield;
    public boolean focus = true;

    public GameFrame(Game gr) {
        super();
        game = gr;
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        init();
    }

    private void init() {
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();//Bildschirmgröße
        double height = screensize.getHeight();
        double width = screensize.getWidth();
        int h = (int) height;
        int w = (int) width;
        squarelength = Math.min(h / 9, w / 12);//Berechenen einer Kästchenlänge
        this.setExtendedState(MAXIMIZED_BOTH);//Maximieren
        setUndecorated(true);//"Rand" entfernen
        setLayout(layout);
        setVisible(true);
    }

    public void write(String text) {
    }

    public GameField getGameField() {
        return gamefield;
    }

    public int getFieldLength() {
        return squarelength;
    }

    public Game getGame() {
        return game;
    }

    private void addComponent(JComponent component, int gridx, int gridy, int weightx, int weighty, int gridheight, int gridwidth) {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.gridheight = gridheight;
        constraints.gridwidth = gridwidth;
        layout.setConstraints(component, constraints);
        add(component);
    }

    private void initComponents() {
        constraints.fill = GridBagConstraints.BOTH;
        gamefield = new GameField(this);
        addComponent(gamefield, 0, 0, 10, 10, 8, GridBagConstraints.REMAINDER);
        JLabel turn = new JLabel("");
        addComponent(turn, 0, 9, 10, 1, 8, 1);
        if (game.getType() == Game.TYPE.LOCAL) {//Überprüfen auf lokales Spiel
            JButton b = new JButton(java.util.ResourceBundle.getBundle("translations/translations").getString("GAME_BUTTON_END"));
            b.addActionListener((ActionEvent e) -> {
                setVisible(false);
                MainMenu.MAINMENU.setVisible(true);
            });
            addComponent(b, 1, 9, 2, 1, 1, 1);
        } else if (game.getType() != Game.TYPE.LOCAL) {
            JButton jb1 = new JButton(java.util.ResourceBundle.getBundle("translations/translations").getString("GAME_BUTTON_DRAW"));
            JButton jb2 = new JButton(java.util.ResourceBundle.getBundle("translations/translations").getString("GAME_BUTTON_RESIGNATION"));
            jb1.addActionListener((ActionEvent e) -> {
                ((ClientGame) game).getConnection().offerDraw();
            });
            jb2.addActionListener((ActionEvent e) -> {
                ((ClientGame) game).getConnection().resignate();
            });
            addComponent(jb1, 1, 9, 1, 1, 1, 1);
            addComponent(jb2, 2, 9, 1, 1, 1, 1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getClickCount() == 1 && !gamefield.movementsupdating && focus) {//Abfangen von Mehrfachklicks

            game.resetFields();

            game.clickAt(e.getX() / squarelength, e.getY() / squarelength);

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
