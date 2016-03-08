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
public abstract class MovementUpdater extends Thread{//Berechnet nach jedem Zug die Zugmöglichkeiten neu
    private Game g;
    private boolean schach,schachmatt;
    public MovementUpdater(Game g){
        this.g=g;
    }
    @Override
    public void run(){
        /*Runnable wait = () -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MovementUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        Thread waitthread = new Thread(wait);
        waitthread.start();*/
        updateMovements();
        /*try {
            waitthread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MovementUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        testCheck();
    }
    
    protected abstract void onStart();
    
    protected Game getGame(){
        return g;
    }
    
    public void updateMovements(){
        Thread[] t=new Thread[32];
        onStart();
        for(int a=0;a<16;a++){
            if(!g.getFiguren(true)[a].isCaptured()){
                t[a]=new MovementUpdaterThread(g.getGameSituation().getAbstractChessmen(true)[a]);
                t[a].start();
            }
            if(!g.getFiguren(false)[a].isCaptured()){
                t[a+16]=new MovementUpdaterThread(g.getGameSituation().getAbstractChessmen(false)[a]);
                t[a+16].start();
            }
        }
        for(int a=0;a<32;a++){
            try {
                if(t[a]!=null){
                    t[a].join();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MovementUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void testCheck() {
        Move[] schläge = g.getGameSituation().getAllCaptures(!g.getTurn());
        //wenn ziel König ist, dann Schach
        for (Move schläge1 : schläge) {
            if (schläge1 != null) {
                Square f = g.getSquare(schläge1.getTargetX(),schläge1.getTargetY());
                if (f.occupier.getClass() == King.class) {
                    schach = true;
                    break;
                }
            }
        }

        
        Move[] zü = g.getGameSituation().getAllMoves(g.getTurn());
        Move[] schl = g.getGameSituation().getAllCaptures(g.getTurn());
        Move[] bewegungen = Stream.concat(Arrays.stream(zü), Arrays.stream(schl))
                      .toArray(Move[]::new);
        
        schachmatt = true;
        //wenn irgendein Zug möglich ist, dann kein schachmatt
        for (int a = 0; a < bewegungen.length; a++) {
            if (bewegungen[a] != null) {
                schachmatt = false;
                break;
            }
        }
        if(schachmatt){
            if(schach){
                g.onCheckMate();
            }else{
                g.onStaleMate();
            }
        }else{
            if(schach){
                g.onCheck();
            }
        }
        /*if (schach && !g.isServer()) {
            ((ClientGame)g).getGameFrame().write("Schach");
        }
        if (schachmatt) {
            //nur wenn schachmatt und schach vorliegt, gewinnt der jeweilige
            if (schach) {
                String gewinner;
                if (g.getTurn()) {
                    //gewinner=Start.START.texte[Start.START.einstellungen.sprache][0];
                } else {
                    //gewinner=Start.START.texte[Start.START.einstellungen.sprache][1];
                }
                //InfoFrame f=new InfoFrame(gewinner+Start.START.texte[Start.START.einstellungen.sprache][3],200,100,true);
                //f.setVisible(true);
                g.getGameFrame().setVisible(false);
            } else {
                //wenn nur schachmatt vorliegt gibt es Patt
                //InfoFrame f = new InfoFrame("Unentschieden", 200, 100, true);
                //f.setVisible(true);
                g.getGameFrame().setVisible(false);
            }
        }

        System.out.println("Finished CheckMate");
        if (!g.isServer()) {
            System.out.println("Reenabling");
            g.getGameFrame().getGameField().movementsupdating = false;
        }
        if (schach && !g.isServer()) {
            Runnable checkimage = () -> {
                try {
                    Thread.sleep(100);
                    g.getGameFrame().getGameField().schach = true;
                    Thread.sleep(3000);
                    g.getGameFrame().getGameField().schach = false;
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestCheckMate.class.getName()).log(Level.SEVERE, null, ex);
                }
            };
            new Thread(checkimage).start();
        }
        if (g.isServer()) {//eventuell daten an client schicken
            /*
            g.client1.writeMovements();
            g.client2.writeMovements();
            if (schachmatt) {
                if (schach) {
                    g.client1.write("m");
                    g.client2.write("m");
                } else {
                    g.client1.write("p");
                    g.client2.write("p");
                }
            }
            if (schach) {
                g.client1.write("c");
                g.client2.write("c");
            }
*/
        //}
        //if (g.isLocal() && g.bot && g.getTurn()) {
            //Bot.doMove();
        //}
        g.finishedCalcs();
    }
}
