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

    private final Game game;
    private boolean schach, schachmatt;

    public MovementUpdater(Game g) {
        this.game = g;
    }

    protected abstract void onStart();

    protected Game getGame() {
        return game;
    }

    public void updateMovements() {
        Thread[] t = new Thread[32];
        onStart();
        for (int a = 0; a < 16; a++) {
            if (!game.getFiguren(true)[a].isCaptured()) {
                t[a] = new MovementUpdaterThread(game.getGameSituation().getAbstractChessmen(true)[a]);
                t[a].start();
            }
            if (!game.getFiguren(false)[a].isCaptured()) {
                t[a + 16] = new MovementUpdaterThread(game.getGameSituation().getAbstractChessmen(false)[a]);
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

    public void testCheck() {
        Move[] schläge = game.getGameSituation().getAllCaptures(!game.getTurn());
        //wenn ziel König ist, dann Schach
        for (Move schläge1 : schläge) {
            if (schläge1 != null) {
                Square f = game.getSquare(schläge1.getTargetX(), schläge1.getTargetY());
                if (f.occupier.getClass() == King.class) {
                    schach = true;
                    break;
                }
            }
        }

        Move[] zü = game.getGameSituation().getAllMoves(game.getTurn());
        Move[] schl = game.getGameSituation().getAllCaptures(game.getTurn());
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
                game.onCheckMate();
            } else {
                game.onStaleMate();
            }
        } else if (schach) {
            game.onCheck();
        }
        /*if (schach && !game.isServer()) {
            ((ClientGame)game).getGameFrame().write("Schach");
        }
        if (schachmatt) {
            //nur wenn schachmatt und schach vorliegt, gewinnt der jeweilige
            if (schach) {
                String gewinner;
                if (game.getTurn()) {
                    //gewinner=Start.START.texte[Start.START.einstellungen.sprache][0];
                } else {
                    //gewinner=Start.START.texte[Start.START.einstellungen.sprache][1];
                }
                //InfoFrame f=new InfoFrame(gewinner+Start.START.texte[Start.START.einstellungen.sprache][3],200,100,true);
                //f.setVisible(true);
                game.getGameFrame().setVisible(false);
            } else {
                //wenn nur schachmatt vorliegt gibt es Patt
                //InfoFrame f = new InfoFrame("Unentschieden", 200, 100, true);
                //f.setVisible(true);
                game.getGameFrame().setVisible(false);
            }
        }

        System.out.println("Finished CheckMate");
        if (!game.isServer()) {
            System.out.println("Reenabling");
            game.getGameFrame().getGameField().movementsupdating = false;
        }
        if (schach && !game.isServer()) {
            Runnable checkimage = () -> {
                try {
                    Thread.sleep(100);
                    game.getGameFrame().getGameField().schach = true;
                    Thread.sleep(3000);
                    game.getGameFrame().getGameField().schach = false;
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestCheckMate.class.getName()).log(Level.SEVERE, null, ex);
                }
            };
            new Thread(checkimage).start();
        }
        if (game.isServer()) {//eventuell daten an client schicken
            /*
            game.client1.writeMovements();
            game.client2.writeMovements();
            if (schachmatt) {
                if (schach) {
                    game.client1.write("m");
                    game.client2.write("m");
                } else {
                    game.client1.write("p");
                    game.client2.write("p");
                }
            }
            if (schach) {
                game.client1.write("c");
                game.client2.write("c");
            }
         */
        //}
        //if (game.isLocal() && game.bot && game.getTurn()) {
        //Bot.doMove();
        //}
        game.finishedCalcs();
    }
}
