package eu.assault2142.hololol.chess.client.networking;


import eu.assault2142.hololol.chess.client.game.LocalGame;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author jojo
 */
public class Server {
    ClientConnection[] client=new ClientConnection[2];
    ServerSocket ss;
    Socket s;
    LocalGame g;
    public Server() throws IOException{
            ss=new ServerSocket(1024);
    }
    public void searchForPlayers(){
        SearchPlayers sp=new SearchPlayers(this);
        Thread spt=new Thread(sp);
        spt.start();
    }
    public void startGame(){
        if(client[1]!=null&&client[0]!=null){
            client[1].write("g1");
            client[1].white=false;
            client[0].write("g0");
            client[0].white=true;
            //g=new LocalGame(client[0],client[1]);
        }else{
        }
    }
}
