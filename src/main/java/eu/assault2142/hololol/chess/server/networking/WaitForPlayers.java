package eu.assault2142.hololol.chess.server.networking;

import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.server.exceptions.UsernameNotFreeException;
import eu.assault2142.hololol.chess.server.user.LoginRequest;
import eu.assault2142.hololol.chess.server.util.Log;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author hololol2
 */
public class WaitForPlayers extends Thread {

    private final Server server;
    private Socket socket;

    public WaitForPlayers(Server se) {
        this.server = se;
    }

    @Override
    public void run() {
        while (true) {
            try {
                socket = server.getServerSocket().accept();
                ClientConnection serverclient = new ClientConnection(socket);
                serverclient.write(ClientMessages.Hello, new Object[]{});
                String input = serverclient.getScanner().next();
                String username;
                String pass;
                String[] str = input.split(":");
                if (str.length == 2) {
                    username = str[0];
                    pass = str[1];
                    LoginRequest user = new LoginRequest(username, pass, serverclient);
                    server.getLoginQueue().addLast(user);
                }
                if (str.length == 3 && str[0].equals("r")) {
                    try {
                        server.createNewUser(str[1], str[2]);
                        server.getLoginQueue().addLast(new LoginRequest(str[1], str[2], serverclient));
                    } catch (UsernameNotFreeException ex) {
                        serverclient.write(ClientMessages.UsernameWrong, new Object[]{});
                    }
                }
            } catch (IOException ex) {
                Log.MAINLOG.log(ex.getMessage());
            }
        }
    }
}
