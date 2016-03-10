package eu.assault2142.hololol.chess.server.networking;

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
        consumers.add(this::consumeChange);
        consumers.add(this::consumeFriend);
        consumers.add(this::consumeGame);
        consumers.add(this::consumeLogout);
        consumers.add(this::consumeMessage);
        consumers.add(this::consumeNewGame);
    }

    private void consumeMessage(String[] message) {
        int length = message.length;
        if (message[0].equals("msg") && length == 3) {
            try {
                server.sendMessage(server.getUserID(message[1]), connection.getUser().getUsername() + ":" + message[2]);
            } catch (UnknownUserException ex) {
                Log.MAINLOG.log(ex.getMessage());
            }
        }
    }

    private void consumeFriend(String[] message) {
        int length = message.length;
        try {
            if (message[0].equals("friends") && length == 3) {
                if (length >= 2 && message[1].equals("add")) {
                    String str = message[2];
                    Server.SERVER.addFriendRequest(connection.getUser().getID(), server.getUser(str).getID());

                } else if (length == 3 && message[1].equals("remove")) {

                    String str = message[2];
                    Server.SERVER.removeFriend(connection.getUser().getID(), server.getUser(str).getID());

                } else if (length >= 2 && message[1].equals("accept")) {

                    String str = message[2];
                    Server.SERVER.acceptRequest(connection.getUser().getID(), server.getUser(str).getID());

                } else if (length >= 2 && message[1].equals("decline")) {

                    String str = message[2];
                    Server.SERVER.declineRequest(connection.getUser().getID(), server.getUser(str).getID());

                }
            }
        } catch (UnknownUserException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    private void consumeLogout(String[] message) {
        if (message[0].equals("logout")) {
            connection.closeConnection();
        }
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
                        server.getConnection(server.getUserID(message[2])).write("newgame:" + connection.getUser().getUsername());
                        server.challenge(connection.getUser().getID(), server.getUserID(message[2]));
                    } else {
                        connection.write("newgame:enemyoffline");
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
                    connection.write("change:username:accept:" + message[2]);
                } catch (UsernameNotFreeException ex) {
                    connection.write("change:username:decline");
                } catch (UnknownUserException ex) {
                    Log.MAINLOG.log(ex.getMessage());
                }
            }
            if (message[1].equals("password")) {
                try {
                    server.setPassword(connection.getUser().getID(), message[2]);
                    connection.write("change:password:accept");
                } catch (UnknownUserException ex) {
                    Log.MAINLOG.log(ex.getMessage());
                }
            }
        }
    }
}
