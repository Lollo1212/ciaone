package eu.assault2142.hololol.chess.server.networking;

import eu.assault2142.hololol.chess.game.ServerGame;
import eu.assault2142.hololol.chess.networking.GameClientConnection;
import eu.assault2142.hololol.chess.server.user.User;
import eu.assault2142.hololol.chess.server.util.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author hololol2
 */
public class ClientConnection extends GameClientConnection {

    private Socket socket;
    private Server server;
    private Scanner scanner;
    private PrintWriter printwriter;
    private User user;
    private ServerGame game;

    public ClientConnection(Socket socket, Server server) {
        super(socket);
        this.server = server;
        try {
            printwriter = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
        reader = new ClientConnectionThread(this, server);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void closeConnection() {
        Server.SERVER.logoutUser(user.getID());
        printwriter.close();
        scanner.close();
        Log.MAINLOG.log(user.getUsername() + " logged out");
    }

    public ServerGame getGame() {
        return game;
    }

    public void setGame(ServerGame game) {
        this.game = game;
    }

    public void writeFriendList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
