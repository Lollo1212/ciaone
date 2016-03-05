/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.networking;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author jojo
 */
public class SearchPlayers implements Runnable{
    Server s;
    public SearchPlayers(Server s){
        this.s=s;
    }
    @Override
    public void run() {
            Socket so1=null;
            try {
                so1 = s.ss.accept();
                s.client[0]=new ClientConnection(so1,s);
            } catch (IOException ex) {
            }
            Socket so2=null;
            try {
                so2 = s.ss.accept();
                s.client[1]=new ClientConnection(so2,s);
            } catch (IOException ex) {
            }
    }
}
