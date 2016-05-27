package eu.assault2142.hololol.chess.client.game.ui;

/**
 * Interface for a GameView which displays the Game
 *
 * @author hololol2
 */
public interface IGameView {

    /**
     * Show a Draw-Offer-Message
     */
    public void drawOffer();

    /**
     * Sets whether the movements are currently updating
     *
     * @param updating if true the movements are updating, otherwise not
     */
    public void setMovementsUpdating(boolean updating);

    /**
     * Sets whether to display check-message
     *
     * @param check whether to show check-message
     */
    public void setShowCheck(boolean check);

    /**
     * Close the GameView
     */
    public void hide();

    /**
     * Show a CheckMate-Message
     */
    public void onCheckMate();

    /**
     * Show a Draw-Message
     */
    public void onDraw();

    /**
     * Show a Resignation-Message
     *
     * @param enemy true if the enemy resignated, false otherwise
     */
    public void onResignation(boolean enemy);

    /**
     * Show a StaleMate-Message
     */
    public void onStaleMate();

    /**
     * Show the promotion-choice
     *
     * @return the chessman-type selected (QUEEN,ROOK,KNIGHT,BISHOP)
     */
    public String showPromotionChoice();

}
