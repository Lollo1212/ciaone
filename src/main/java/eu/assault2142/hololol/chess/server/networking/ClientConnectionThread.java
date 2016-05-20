package eu.assault2142.hololol.chess.server.networking;

import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.networking.GameConnectionThread;
import eu.assault2142.hololol.chess.networking.ServerMessages;
import eu.assault2142.hololol.chess.server.exceptions.UnknownUserException;
import eu.assault2142.hololol.chess.server.exceptions.UsernameNotFreeException;
import eu.assault2142.hololol.chess.server.util.Log;

/**
 *
 * @author hololol2
 */
public class ClientConnectionThread extends GameConnectionThread {

    private final ClientConnection connection;
    private final Server server;

    public ClientConnectionThread(ClientConnection serverclient, Server server) {
        super(serverclient);
        this.connection = serverclient;
        this.server = server;
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

    private void consumeMessage(String[] parts) {
        try {
            server.sendMessage(server.getUserID(parts[0]), connection.getUser().getUsername() + ":" + parts[1]);
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    private void consumeAddFriend(String[] parts) {
        String str = parts[0];
        try {
            Server.SERVER.addFriendRequest(connection.getUser().getID(), server.getUser(str).getID());
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    private void consumeRemoveFriend(String[] parts) {
        String str = parts[0];
        try {
            Server.SERVER.removeFriend(connection.getUser().getID(), server.getUser(str).getID());
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    private void consumeAcceptFriend(String[] parts) {
        String str = parts[0];
        try {
            Server.SERVER.acceptRequest(connection.getUser().getID(), server.getUser(str).getID());
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    private void consumeDeclineFriend(String[] parts) {
        String str = parts[0];
        try {
            Server.SERVER.declineRequest(connection.getUser().getID(), server.getUser(str).getID());
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    private void consumeLogout(String[] parts) {
        System.out.println("consLogout");
        connection.closeConnection();
    }

    private void consumeAcceptGame(String[] parts) {
        try {
            if (server.challengeExists(connection.getUser().getID(), server.getUserID(parts[0]))) {
                server.startGame(connection.getUser(), server.getUser(parts[0]));
            }
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    private void consumeDeclineGame(String[] parts) {

    }

    private void consumeRandomGame(String[] parts) {

    }

    private void consumeFriendGame(String[] parts) {
        try {
            if (server.isOnline(server.getUserID(parts[0]))) {
                server.getConnection(server.getUserID(parts[0])).write(ClientMessages.Newgame, new Object[]{connection.getUser().getUsername()});
                server.challenge(connection.getUser().getID(), server.getUserID(parts[0]));
            } else {
                connection.write(ClientMessages.Newgame, new Object[]{"enemyoffline"});
            }
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    private void consumeChangeUsername(String[] parts) {
        try {
            server.setUsername(connection.getUser().getID(), parts[0]);
            connection.write(ClientMessages.AcceptUsernameChange, new Object[]{parts[0]});
        } catch (UsernameNotFreeException ex) {
            connection.write(ClientMessages.DeclineUsernameChange, new Object[]{});
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    private void consumeChangePassword(String[] parts) {
        try {
            server.setPassword(connection.getUser().getID(), parts[0]);
            connection.write(ClientMessages.AcceptPasswordChange, new Object[]{});
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }
}
