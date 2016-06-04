package eu.assault2142.hololol.chess.server.networking;

import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.networking.GameClientConnection;
import eu.assault2142.hololol.chess.server.exceptions.UnknownUserException;
import eu.assault2142.hololol.chess.server.user.User;
import eu.assault2142.hololol.chess.server.util.Log;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hololol2
 */
public class ClientConnection extends GameClientConnection {

    private User user;

    public ClientConnection(Socket socket) {
        super(socket);
        reader = new ClientConnectionThread(this);
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

    public void writeFriendList() {
        try {
            write(ClientMessages.Friends, new Object[]{Server.SERVER.getFriendsAsString(getUser().getID())});
        } catch (UnknownUserException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
