package eu.assault2142.hololol.chess.server.networking;

import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.networking.GameConnectionThread;
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

    private void consumeAcceptRequest(String[] parts) {
        String str = parts[0];
        try {
            Server.SERVER.acceptRequest(connection.getUser().getID(), server.getUser(str).getID());
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    private void consumeDeclineRequest(String[] parts) {
        String str = parts[0];
        try {
            Server.SERVER.declineRequest(connection.getUser().getID(), server.getUser(str).getID());
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    private void consumeLogout(String[] parts) {
        connection.closeConnection();
    }

    private void consumeGame(String[] message) {
        int length = message.length;
        if (message[0].equals("game") && length == 3) {
            String str = message[1];
            if (str.equals("accept")) {
                try {
                    if (server.challengeExists(connection.getUser().getID(), server.getUserID(message[2]))) {
                        server.startGame(connection.getUser(), server.getUser(message[2]));
                    }
                } catch (UnknownUserException ex) {
                    Log.MAINLOG.log(ex.getMessage());
                }
            } else if (str.equals("decline")) {

            }
        }
    }

    private void consumeNewGame(String[] message) {
        int length = message.length;
        if (message[0].equals("newgame") && length >= 2) {
            if (message[1].equals("random")) {

            } else {
                try {
                    if (length == 3 && server.isOnline(server.getUserID(message[2]))) {
                        server.getConnection(server.getUserID(message[2])).write(ClientMessages.Newgame, new Object[]{connection.getUser().getUsername()});
                        server.challenge(connection.getUser().getID(), server.getUserID(message[2]));
                    } else {
                        connection.write(ClientMessages.Newgame, new Object[]{"enemyoffline"});
                    }
                } catch (UnknownUserException ex) {
                    Log.MAINLOG.log(ex.getMessage());
                }
            }
        }
    }

    private void consumeChange(String[] message) {
        int length = message.length;
        if (message[0].equals("change") && length == 3) {
            if (message[1].equals("username")) {
                try {
                    server.setUsername(connection.getUser().getID(), message[2]);
                    connection.write(ClientMessages.AcceptUsernameChange, new Object[]{message[2]});
                } catch (UsernameNotFreeException ex) {
                    connection.write(ClientMessages.DeclineUsernameChange, new Object[]{});
                } catch (UnknownUserException ex) {
                    Log.MAINLOG.log(ex.getMessage());
                }
            }
            if (message[1].equals("password")) {
                try {
                    server.setPassword(connection.getUser().getID(), message[2]);
                    connection.write(ClientMessages.AcceptPasswordChange, new Object[]{});
                } catch (UnknownUserException ex) {
                    Log.MAINLOG.log(ex.getMessage());
                }
            }
        }
    }
}
