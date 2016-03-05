/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.networking;

import eu.assault2142.hololol.chess.game.chessmen.Move;
import eu.assault2142.hololol.chess.game.GameSituation;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jojo
 */
public class ClientConnection implements eu.assault2142.hololol.chess.networking.ClientConnection{
    Socket so;
    Server s;
    ClientConnectionThread reader;
    Scanner sc;
    PrintWriter pw;
    boolean white;
    boolean ready;
    boolean unentschieden;
    public ClientConnection(Socket so,Server s){
        this.s=s;
        this.so=so;
        try {
            pw=new PrintWriter(so.getOutputStream(),true);
            sc=new Scanner(so.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        reader=new ClientConnectionThread(this);
        reader.start();
    }
    public void write(String str){
        pw.println(str);
    }
    public void writeMovements(){
        GameSituation gs=s.g.getGameSituation();
        String str1="vbm";
        String str2="vwm";
        String str3="vbs";
        String str4="vws";
        for(int a=0;a<16;a++){
            Move[] m=gs.getAbstractChessmen(true)[a].getMoves();
            for(int i=0;i<m.length;i++){
                if(m[i]!=null){
                    str1+=m[i].getTargetX()+""+m[i].getTargetY()+""+m[i].getChessman().getPositionInArray()+";";
                }
            }
            m=gs.getAbstractChessmen(true)[a].getCaptures();
            for(int i=0;i<m.length;i++){
                if(m[i]!=null){
                    str3+=m[i].getTargetX()+""+m[i].getTargetY()+""+m[i].getChessman().getPositionInArray()+";";
                }
            }
            m=gs.getAbstractChessmen(false)[a].getMoves();
            for(int i=0;i<m.length;i++){
                if(m[i]!=null){
                    str2+=m[i].getTargetX()+""+m[i].getTargetY()+""+m[i].getChessman().getPositionInArray()+";";
                }
            }
            m=gs.getAbstractChessmen(false)[a].getCaptures();
            for(int i=0;i<m.length;i++){
                if(m[i]!=null){
                    str4+=m[i].getTargetX()+""+m[i].getTargetY()+""+m[i].getChessman().getPositionInArray()+";";
                }
            }
        }
        write(str1);
        write(str2);
        write(str3);
        write(str4);
    }
}
