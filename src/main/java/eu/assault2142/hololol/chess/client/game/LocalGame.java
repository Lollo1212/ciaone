/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game;

import eu.assault2142.hololol.chess.game.Game;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.client.game.ui.GameFrame;
import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author jojo
 */
public class LocalGame extends Game {

    private GameFrame p;
    public boolean bot;

    public LocalGame() {//Lokales Spiel
        super(TYPE.LOCAL);
        p = new GameFrame(this);
        new ClientMovementUpdater(this).start();
    }

    public GameFrame getGameFrame() {
        return p;
    }

    @Override
    public ImageIcon getImage(Chessman.NAMES name, boolean black) {
        String color = "white";
        if (black) {
            color = "black";
        }
        return new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder + "/" + name + "_" + color + ".gif"));
    }

    @Override
    public void updateMovements() {
        new ClientMovementUpdater(this).start();
    }

    @Override
    public void promotion(Pawn pawn) {
        Chessman man;
        String selected = (String) JOptionPane.showInputDialog(p, "Promotion", "Promotion", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Queen", "Rook", "Knight", "Bishop"}, "Queen");
        switch (selected) {
            //bei lokalem Spiel wird der Bauer direkt gesetzt,
            //bei Serverbasiertem senden der Daten an den Server
            case "Rook":
                    man = Rook.promotion(pawn,this);
                break;
            case "Knight":
                man = Knight.promotion(pawn,this);
                break;
            case "Bishop":
                man = Bishop.promotion(pawn,this);
                break;
            default:
                man = Queen.promotion(pawn,this);
                break;
        }
        getFiguren(pawn.isBlack())[pawn.getPositionInArray()] = man;
        getSquare(man.getX(), man.getY()).occupier = man;
        

        new ClientMovementUpdater(this).start();
    }

    @Override
    public void finishedCalcs() {

        p.getGameField().movementsupdating = false;
    }

}
