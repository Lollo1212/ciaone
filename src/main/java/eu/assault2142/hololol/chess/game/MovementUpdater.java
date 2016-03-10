/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jojo
 */
public abstract class MovementUpdater extends Thread {//Berechnet nach jedem Zug die Zugmöglichkeiten neu

    private final GameState gamestate;
    private boolean schach, schachmatt;

    public MovementUpdater(GameState g) {
        this.gamestate = g;
    }

    public void testCheck() {
        List<Move> schläge = gamestate.getAllCaptures(!gamestate.getTurn());
        //wenn ziel König ist, dann Schach
        for (Move schläge1 : schläge) {
            if (schläge1 != null) {
                Square f = gamestate.getSquare(schläge1.getTargetX(), schläge1.getTargetY());
                if (f.isOccupied() && f.occupier.getClass() == King.class) {
                    schach = true;
                    break;
                }
            }
        }

        List<Move> zü = gamestate.getAllMoves(gamestate.getTurn());
        List<Move> schl = gamestate.getAllCaptures(gamestate.getTurn());
        zü.addAll(schl);
        schachmatt = true;
        //wenn irgendein Zug möglich ist, dann kein schachmatt
        for (Move bewegungen1 : zü) {
            if (bewegungen1 != null) {
                schachmatt = false;
                break;
            }
        }
        if (schachmatt) {
            if (schach) {
                gamestate.getGame().onCheckMate();
            } else {
                gamestate.getGame().onStaleMate();
            }
        } else if (schach) {
            gamestate.getGame().onCheck();
        }
        /*if (schach && !gamestate.isServer()) {
        ((ClientGame)gamestate).getGameFrame().write("Schach");
        }
        if (schachmatt) {
        //nur wenn schachmatt und schach vorliegt, gewinnt der jeweilige
        if (schach) {
        String gewinner;
        if (gamestate.getTurn()) {
        //gewinner=Start.START.texte[Start.START.einstellungen.sprache][0];
        } else {
        //gewinner=Start.START.texte[Start.START.einstellungen.sprache][1];
        }
        //InfoFrame f=new InfoFrame(gewinner+Start.START.texte[Start.START.einstellungen.sprache][3],200,100,true);
        //f.setVisible(true);
        gamestate.getGameFrame().setVisible(false);
        } else {
        //wenn nur schachmatt vorliegt gibt es Patt
        //InfoFrame f = new InfoFrame("Unentschieden", 200, 100, true);
        //f.setVisible(true);
        gamestate.getGameFrame().setVisible(false);
        }
        }

        System.out.println("Finished CheckMate");
        if (!gamestate.isServer()) {
        System.out.println("Reenabling");
        gamestate.getGameFrame().getGameField().movementsupdating = false;
        }
        if (schach && !gamestate.isServer()) {
        Runnable checkimage = () -> {
        try {
        Thread.sleep(100);
        gamestate.getGameFrame().getGameField().schach = true;
        Thread.sleep(3000);
        gamestate.getGameFrame().getGameField().schach = false;
        } catch (InterruptedException ex) {
        Logger.getLogger(TestCheckMate.class.getName()).log(Level.SEVERE, null, ex);
        }
        };
        new Thread(checkimage).start();
        }
        if (gamestate.isServer()) {//eventuell daten an client schicken
        /*
        gamestate.client1.writeMovements();
        gamestate.client2.writeMovements();
        if (schachmatt) {
        if (schach) {
        gamestate.client1.write("m");
        gamestate.client2.write("m");
        } else {
        gamestate.client1.write("p");
        gamestate.client2.write("p");
        }
        }
        if (schach) {
        gamestate.client1.write("c");
        gamestate.client2.write("c");
        }
         */
        //}
        //if (gamestate.isLocal() && gamestate.bot && gamestate.getTurn()) {
        //Bot.emulateMove();
        //}
        gamestate.getGame().finishedCalcs();
    }

    public void updateMovements() {
        Thread[] t = new Thread[32];
        for (int a = 0; a < 16; a++) {
            if (!gamestate.getChessmen(true)[a].isCaptured()) {
                t[a] = new MovementUpdaterThread(gamestate.getChessmen(true)[a]);
                t[a].start();
            }
            if (!gamestate.getChessmen(false)[a].isCaptured()) {
                t[a + 16] = new MovementUpdaterThread(gamestate.getChessmen(false)[a]);
                t[a + 16].start();
            }
        }
        for (int a = 0; a < 32; a++) {
            try {
                if (t[a] != null) {
                    t[a].join();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MovementUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected GameState getGameSituation() {
        return gamestate;
    }
}
