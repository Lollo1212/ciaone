/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author jojo
 */
public abstract class MovementUpdater extends Thread {//Berechnet nach jedem Zug die Zugmöglichkeiten neu

    private final GameState gamesituation;
    private boolean schach, schachmatt;

    public MovementUpdater(GameState g) {
        this.gamesituation = g;
    }

    public void testCheck() {
        Move[] schläge = gamesituation.getAllCaptures(!gamesituation.getTurn());
        //wenn ziel König ist, dann Schach
        for (Move schläge1 : schläge) {
            if (schläge1 != null) {
                Square f = gamesituation.getSquare(schläge1.getTargetX(), schläge1.getTargetY());
                if (f.isOccupied() && f.occupier.getClass() == King.class) {
                    schach = true;
                    break;
                }
            }
        }

        Move[] zü = gamesituation.getAllMoves(gamesituation.getTurn());
        Move[] schl = gamesituation.getAllCaptures(gamesituation.getTurn());
        Move[] bewegungen = Stream.concat(Arrays.stream(zü), Arrays.stream(schl))
                .toArray(Move[]::new);
        schachmatt = true;
        //wenn irgendein Zug möglich ist, dann kein schachmatt
        for (Move bewegungen1 : bewegungen) {
            if (bewegungen1 != null) {
                schachmatt = false;
                break;
            }
        }
        if (schachmatt) {
            if (schach) {
                gamesituation.getGame().onCheckMate();
            } else {
                gamesituation.getGame().onStaleMate();
            }
        } else if (schach) {
            gamesituation.getGame().onCheck();
        }
        /*if (schach && !gamesituation.isServer()) {
        ((ClientGame)gamesituation).getGameFrame().write("Schach");
        }
        if (schachmatt) {
        //nur wenn schachmatt und schach vorliegt, gewinnt der jeweilige
        if (schach) {
        String gewinner;
        if (gamesituation.getTurn()) {
        //gewinner=Start.START.texte[Start.START.einstellungen.sprache][0];
        } else {
        //gewinner=Start.START.texte[Start.START.einstellungen.sprache][1];
        }
        //InfoFrame f=new InfoFrame(gewinner+Start.START.texte[Start.START.einstellungen.sprache][3],200,100,true);
        //f.setVisible(true);
        gamesituation.getGameFrame().setVisible(false);
        } else {
        //wenn nur schachmatt vorliegt gibt es Patt
        //InfoFrame f = new InfoFrame("Unentschieden", 200, 100, true);
        //f.setVisible(true);
        gamesituation.getGameFrame().setVisible(false);
        }
        }

        System.out.println("Finished CheckMate");
        if (!gamesituation.isServer()) {
        System.out.println("Reenabling");
        gamesituation.getGameFrame().getGameField().movementsupdating = false;
        }
        if (schach && !gamesituation.isServer()) {
        Runnable checkimage = () -> {
        try {
        Thread.sleep(100);
        gamesituation.getGameFrame().getGameField().schach = true;
        Thread.sleep(3000);
        gamesituation.getGameFrame().getGameField().schach = false;
        } catch (InterruptedException ex) {
        Logger.getLogger(TestCheckMate.class.getName()).log(Level.SEVERE, null, ex);
        }
        };
        new Thread(checkimage).start();
        }
        if (gamesituation.isServer()) {//eventuell daten an client schicken
        /*
        gamesituation.client1.writeMovements();
        gamesituation.client2.writeMovements();
        if (schachmatt) {
        if (schach) {
        gamesituation.client1.write("m");
        gamesituation.client2.write("m");
        } else {
        gamesituation.client1.write("p");
        gamesituation.client2.write("p");
        }
        }
        if (schach) {
        gamesituation.client1.write("c");
        gamesituation.client2.write("c");
        }
         */
        //}
        //if (gamesituation.isLocal() && gamesituation.bot && gamesituation.getTurn()) {
        //Bot.emulateMove();
        //}
        gamesituation.getGame().finishedCalcs();
    }

    public void updateMovements() {
        Thread[] t = new Thread[32];
        for (int a = 0; a < 16; a++) {
            if (!gamesituation.getChessmen(true)[a].isCaptured()) {
                t[a] = new MovementUpdaterThread(gamesituation.getChessmen(true)[a]);
                t[a].start();
            }
            if (!gamesituation.getChessmen(false)[a].isCaptured()) {
                t[a + 16] = new MovementUpdaterThread(gamesituation.getChessmen(false)[a]);
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
        return gamesituation;
    }
}
