/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.networking;

import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import java.util.NoSuchElementException;

/**
 *
 * @author jojo
 */
public class ClientConnectionThread extends Thread{
    ClientConnection sc;
    public ClientConnectionThread(ClientConnection s){
        sc=s;
    }
    @Override
    public void run(){
            read();
    }
    public void read(){
        String input;
        while(true){
            try{
                input=sc.sc.next();
                sc.unentschieden=false;
                if(input.substring(0,1).equals("b")){
                    input=input.substring(1);
                    int a=Integer.parseInt(input.substring(0,2));
                    int x=Integer.parseInt(input.substring(2,3));
                    int y=Integer.parseInt(input.substring(3,4));
                    if(a<78){
                        Chessman f=sc.s.g.getSquare(a/10,a%10).occupier;
                        if(f!=null&&!f.isBlack()==sc.white&&f.isBlack()==sc.s.g.getTurn()&&(f.doMove(x,y))){
                            sc.s.client[0].write("b"+input);
                            sc.s.client[1].write("b"+input);
                        }
                        if(f!=null&&!f.isBlack()==sc.white&&f.isBlack()==sc.s.g.getTurn()&&(f.doCapture(x,y))){
                            sc.s.client[0].write("s"+input);
                            sc.s.client[1].write("s"+input);
                        }
                    }
                }
                if(input.substring(0,1).equals("n")){
                    input=input.replace("_", " ");
                }
                if(input.substring(0,1).equals("a")){
                    if(sc==sc.s.client[0]){
                        sc.s.client[0].write("a0");
                        sc.s.client[1].write("a1");
                    }else{
                        sc.s.client[0].write("a1");
                        sc.s.client[1].write("a0");
                    }
                }
                if(input.substring(0,1).equals("u")){
                    if(input.equals("u")){
                        sc.unentschieden=true;
                        if(sc.s.client[0].unentschieden&&sc.s.client[1].unentschieden){
                            sc.s.client[0].write("u1");
                            sc.s.client[1].write("u1");
                        }else{
                            if(sc==sc.s.client[0]){
                                sc.s.client[1].write("u0");
                            }else{
                                sc.s.client[0].write("u0");
                            }
                        }
                    }else{
                        if(input.substring(1,2).equals("n")){
                            sc.s.client[0].unentschieden=false;
                            sc.s.client[1].unentschieden=false;
                        }
                    }
                }
                if(input.substring(0,1).equals("f")){
                    String n=input.substring(1,2);
                    String c=input.substring(2,3);
                    boolean color;
                    if(c.equals("0")){
                        color=true;
                    }else{
                        color=false;
                    }
                    String nia=input.substring(3);
                    int nummer=Integer.parseInt(n);
                    int nummerinarray=Integer.parseInt(nia);
                    switch(nummer){
                        case 0:
                            Chessman f=Rook.promotion((Pawn)sc.s.g.getFiguren(color)[nummerinarray],sc.s.g);
                            if(f!=null){
                                sc.s.g.getFiguren(color)[nummerinarray]=f;
                                sc.s.client[0].write(input);
                                sc.s.client[1].write(input);
                            }
                            break;
                        case 1:
                            f=Knight.promotion((Pawn)sc.s.g.getFiguren(color)[nummerinarray],sc.s.g);
                            if(f!=null){
                                sc.s.g.getFiguren(color)[nummerinarray]=f;
                                sc.s.client[0].write(input);
                                sc.s.client[1].write(input);
                            }
                            break;
                        case 2:
                            f=Bishop.promotion((Pawn)sc.s.g.getFiguren(color)[nummerinarray],sc.s.g);
                            if(f!=null){
                                sc.s.g.getFiguren(color)[nummerinarray]=f;
                                sc.s.client[0].write(input);
                                sc.s.client[1].write(input);
                            }
                            break;
                        case 3:
                            f=Queen.promotion((Pawn)sc.s.g.getFiguren(color)[nummerinarray],sc.s.g);
                            if(f!=null){
                                sc.s.g.getFiguren(color)[nummerinarray]=f;
                                sc.s.client[0].write(input);
                                sc.s.client[1].write(input);
                            }
                            break;
                    }
                }
            } catch(NoSuchElementException nsee) {
                break;
            }
        } 
    }  
}
