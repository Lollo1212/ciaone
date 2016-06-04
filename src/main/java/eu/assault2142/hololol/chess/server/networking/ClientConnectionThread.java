package eu.assault2142.hololol.chess.server.networking;

import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.networking.GameConnectionThread;
import eu.assault2142.hololol.chess.networking.ServerMessages;
import eu.assault2142.hololol.chess.server.exceptions.UnknownUserException;
import eu.assault2142.hololol.chess.server.exceptions.UsernameNotFreeException;
import eu.assault2142.hololol.chess.server.util.Log;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hololol2
 */
public class ClientConnectionThread extends GameConnectionThread {

    protected final ClientConnection connection;

    public ClientConnectionThread(ClientConnection serverclient) {
        super(serverclient);
        this.connection = serverclient;
        consumers.put(ServerMessages.AcceptGame, this::consumeAcceptGame);
        consumers.put(ServerMessages.AcceptFriend, this::consumeAcceptFriend);
        consumers.put(ServerMessages.AddFriend, this::consumeAddFriend);
        consumers.put(ServerMessages.ChangeUsername, this::consumeChangeUsername);
        consumers.put(ServerMessages.ChangePassword, this::consumeChangePassword);
        consumers.put(ServerMessages.DeclineGame, this::consumeDeclineGame);
        consumers.put(ServerMessages.DeclineFriend, this::consumeDeclineFriend);
        consumers.put(ServerMessages.FriendGame, this::consumeFriendGame);
        consumers.put(ServerMessages.Logout, this::consumeLogout);
        consumers.put(ServerMessages.Message, this::consumeMessage);
        consumers.put(ServerMessages.RandomGame, this::consumeRandomGame);
        consumers.put(ServerMessages.RemoveFriend, this::consumeRemoveFriend);
    }

    protected void consumeAcceptFriend(String[] parts) {
        String str = parts[0];
        try {
            Server.SERVER.acceptRequest(connection.getUser().getID(), Server.SERVER.getUser(str).getID());
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    protected void consumeAcceptGame(String[] parts) {
        try {
            if (Server.SERVER.challengeExists(connection.getUser().getID(), Server.SERVER.getUserID(parts[0]))) {
                Server.SERVER.startGame(connection.getUser(), Server.SERVER.getUser(parts[0]));
            }
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    protected void consumeAddFriend(String[] parts) {
        String str = parts[0];
        try {
            Server.SERVER.addFriendRequest(connection.getUser().getID(), Server.SERVER.getUser(str).getID());
        } catch (UnknownUserException ex) {
            connection.write(ClientMessages.UsernameWrong, 0);
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    protected void consumeChangePassword(String[] parts) {
        try {
            Server.SERVER.setPassword(connection.getUser().getID(), parts[0]);
            connection.write(ClientMessages.AcceptPasswordChange);
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    protected void consumeChangeUsername(String[] parts) {
        try {
            Server.SERVER.setUsername(connection.getUser().getID(), parts[0]);
            connection.write(ClientMessages.AcceptUsernameChange, parts[0]);
        } catch (UsernameNotFreeException ex) {
            connection.write(ClientMessages.DeclineUsernameChange);
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    protected void consumeDeclineFriend(String[] parts) {
        String str = parts[0];
        try {
            Server.SERVER.declineRequest(connection.getUser().getID(), Server.SERVER.getUser(str).getID());
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    protected void consumeDeclineGame(String[] parts) {
        try {
            Server.SERVER.declineChallenge(connection.getUser().getID(), Server.SERVER.getUserID(parts[0]));
        } catch (UnknownUserException ex) {
            Logger.getLogger(ClientConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void consumeFriendGame(String[] parts) {
        try {
            if (Server.SERVER.isOnline(Server.SERVER.getUserID(parts[0]))) {
                Server.SERVER.getConnection(Server.SERVER.getUserID(parts[0])).write(ClientMessages.Newgame, connection.getUser().getUsername());
                Server.SERVER.challenge(connection.getUser().getID(), Server.SERVER.getUserID(parts[0]));
            } else {
                connection.write(ClientMessages.Newgame, "enemyoffline");
            }
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    protected void consumeLogout(String[] parts) {
        connection.closeConnection();
    }

    protected void consumeMessage(String[] parts) {
        try {
            Server.SERVER.sendMessage(Server.SERVER.getUserID(parts[0]), connection.getUser().getUsername() + ":" + parts[1]);
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    protected void consumeRandomGame(String[] parts) {

    }

    protected void consumeRemoveFriend(String[] parts) {
        String str = parts[0];
        try {
            Server.SERVER.removeFriend(connection.getUser().getID(), Server.SERVER.getUser(str).getID());
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }
}
