package eu.assault2142.hololol.chess.client.game.ui.swing;

import eu.assault2142.hololol.chess.client.game.ClientGame;
import eu.assault2142.hololol.chess.client.game.Game;
import eu.assault2142.hololol.chess.client.game.Main;
import eu.assault2142.hololol.chess.client.util.Translator;
import eu.assault2142.hololol.chess.game.Settings;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.networking.ServerMessages;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * The Frame in which the game is played. Contains the GameBoard and some
 * additional information and buttons
 *
 * @author hololol2
 */
public class GameFrame extends JFrame implements MouseListener, IGameView {

    private final GridBagConstraints constraints;
    private final Game game;
    private GameBoard gamefield;
    private final GridBagLayout layout;
    private int squarelength;

    /**
     * Create a new GameFrame
     *
     * @param game the game to display
     */
    public GameFrame(Game game) {
        super();
        this.game = game;
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        init();
    }

    @Override
    public void drawOffer() {
        JOptionPane.showConfirmDialog(null, Translator.getString("DRAWOFFER_TEXT"), Translator.getString("DRAWOFFER_HEAD"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Returns the GameBoard
     *
     * @return the GameBoard to play on
     */
    public GameBoard getGameBoard() {
        return gamefield;
    }

    @Override
    public void setMovementsUpdating(boolean updating) {
        gamefield.movementsupdating = updating;
    }

    @Override
    public void setShowCheck(boolean show) {
        gamefield.movementsupdating = show;
    }

    /**
     * Returns the length of a square
     *
     * @return the length of one square of the board
     */
    public int getSquareLength() {
        return squarelength;
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
    }

    @Override
    public void mouseExited(MouseEvent evt) {
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        if (evt.getClickCount() == 1 && !gamefield.movementsupdating) {
            game.getGameState().resetFields();
            game.clickAt(evt.getX() / squarelength, evt.getY() / squarelength);
        }
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
    }

    @Override
    public void onCheck() {
        Runnable checkimage = () -> {
            try {
                Thread.sleep(100);
                setShowCheck(true);
                Thread.sleep(2000);
                setShowCheck(false);
            } catch (InterruptedException ex) {
                Main.MENU.showErrorMessage("Unexpected Critical Error!", true);
            }
        };
        new Thread(checkimage).start();
    }

    @Override
    public void onCheckMate() {
        JOptionPane.showMessageDialog(this, "Checkmate!", "Checkmate", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onDraw() {
        JOptionPane.showMessageDialog(null, Translator.getString("DRAW_TEXT"), Translator.getString("DRAW_HEAD"), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onResignation(boolean enemy) {
        if (enemy) {
            JOptionPane.showMessageDialog(null, Translator.getString("RESIGNATION_ENEMY_TEXT"), Translator.getString("RESIGNATION_ENEMY_HEAD"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, Translator.getString("RESIGNATION_SELF_TEXT"), Translator.getString("RESIGNATION_SELF_HEAD"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void onStaleMate() {
        JOptionPane.showMessageDialog(this, "Stalemate!", "Stalemate", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showColor(boolean color) {
        if (color) {
            JOptionPane.showMessageDialog(this, "Color", "Your color is black.", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Color", "Your color is white.", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public String showPromotionChoice() {
        return (String) JOptionPane.showInputDialog(this, Translator.getString("PROMOTION_HEAD"), Translator.getString("PROMOTION_TEXT"), JOptionPane.QUESTION_MESSAGE, null, new String[]{Translator.getString("CHESSMAN_QUEEN"), Translator.getString("CHESSMAN_ROOK"), Translator.getString("CHESSMAN_KNIGHT"), Translator.getString("CHESSMAN_BISHOP")}, Translator.getString("CHESSMAN_QUEEN"));
    }

    /**
     * Get the image for the given chessman
     *
     * @param man the chessman
     * @return the corresponding ImageIcon
     */
    protected ImageIcon getImage(Chessman man) {
        String color = man.isBlack() ? "black" : "white";
        return new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/" + man.getType() + "_" + color + ".gif"));
    }

    /**
     * Adds a component at the specified position
     *
     * @param component the component to add
     * @param gridX the x-position
     * @param gridY the y-positon
     * @param weightX the weight in x-direction
     * @param weightY the weight in y-direction
     * @param gridheight the height in y-direction
     * @param gridwidth the width in x-direction
     */
    private void addComponent(JComponent component, int gridX, int gridY, int weightX, int weightY, int gridheight, int gridwidth) {
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.weightx = weightX;
        constraints.weighty = weightY;
        constraints.gridheight = gridheight;
        constraints.gridwidth = gridwidth;
        layout.setConstraints(component, constraints);
        add(component);
    }

    /**
     * Initializes the GameFrame
     */
    private void init() {
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        double height = screensize.getHeight();
        double width = screensize.getWidth();
        int h = (int) height;
        int w = (int) width;
        squarelength = Math.min(h / 9, w / 12);
        this.setExtendedState(MAXIMIZED_BOTH);
        setUndecorated(true);
        setLayout(layout);
        setVisible(true);
        initComponents();
        addMouseListener(this);
    }

    /**
     * Initializes the components like buttons etc.
     */
    private void initComponents() {
        constraints.fill = GridBagConstraints.BOTH;
        gamefield = new GameBoard(this, game.getGameState());
        addComponent(gamefield, 0, 0, 10, 10, 8, GridBagConstraints.REMAINDER);
        JLabel turn = new JLabel("");
        addComponent(turn, 0, 9, 10, 1, 8, 1);
        if (game.getType() == Game.TYPE.LOCAL) {//Überprüfen auf lokales Spiel
            JButton b = new JButton(Translator.getString("GAME_BUTTON_END"));
            b.addActionListener((ActionEvent e) -> {
                setVisible(false);
                Main.MENU.setVisible(true);
            });
            addComponent(b, 1, 9, 2, 1, 1, 1);
        } else if (game.getType() != Game.TYPE.LOCAL) {
            JButton jb1 = new JButton(Translator.getString("GAME_BUTTON_DRAW"));
            JButton jb2 = new JButton(Translator.getString("GAME_BUTTON_RESIGNATION"));
            jb1.addActionListener((ActionEvent e) -> {
                ((ClientGame) game).getConnection().write(ServerMessages.Draw, null);
            });
            jb2.addActionListener((ActionEvent e) -> {
                ((ClientGame) game).getConnection().write(ServerMessages.Resignation, null);
            });
            addComponent(jb1, 1, 9, 1, 1, 1, 1);
            addComponent(jb2, 2, 9, 1, 1, 1, 1);
        }
    }

}
