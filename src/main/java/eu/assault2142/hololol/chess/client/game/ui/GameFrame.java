package eu.assault2142.hololol.chess.client.game.ui;

import eu.assault2142.hololol.chess.client.game.ClientGame;
import eu.assault2142.hololol.chess.client.menus.MainMenu;
import eu.assault2142.hololol.chess.networking.ServerMessages;
import eu.assault2142.hololol.chess.client.util.Translator;
import eu.assault2142.hololol.chess.game.Game;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
     * @param g the game to display
     */
    public GameFrame(Game g) {
        super();
        game = g;
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        init();
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
    public void setMovementsUpdating(boolean b) {
        gamefield.movementsupdating = b;
    }

    @Override
    public void setShowCheck(boolean b) {
        gamefield.movementsupdating = b;
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
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getClickCount() == 1 && !gamefield.movementsupdating) {//Abfangen von Mehrfachklicks
            game.getGameState().resetFields();
            game.clickAt(e.getX() / squarelength, e.getY() / squarelength);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void onCheckMate() {
        JOptionPane.showMessageDialog(this, "Checkmate!", "Checkmate", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onStaleMate() {

        JOptionPane.showMessageDialog(this, "Stalemate!", "Stalemate", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public String showPromotionChoice() {
        return (String) JOptionPane.showInputDialog(this, Translator.getString("PROMOTION_HEAD"), Translator.getString("PROMOTION_TEXT"), JOptionPane.QUESTION_MESSAGE, null, new String[]{Translator.getString("CHESSMAN_QUEEN"), Translator.getString("CHESSMAN_ROOK"), Translator.getString("CHESSMAN_KNIGHT"), Translator.getString("CHESSMAN_BISHOP")}, Translator.getString("CHESSMAN_QUEEN"));
    }

    /**
     * Adds a component at the specified position
     *
     * @param component the component to add
     * @param gridx the x-position
     * @param gridy the y-positon
     * @param weightx the weight in x-direction
     * @param weighty the weight in y-direction
     * @param gridheight the height in y-direction
     * @param gridwidth the width in x-direction
     */
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
                MainMenu.MAINMENU.setVisible(true);
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
