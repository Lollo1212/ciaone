package eu.assault2142.hololol.chess.server.networking;

import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import eu.assault2142.hololol.chess.server.exceptions.UnknownUserException;
import eu.assault2142.hololol.chess.server.exceptions.UsernameNotFreeException;
import eu.assault2142.hololol.chess.server.util.Log;
import java.util.NoSuchElementException;

/**
 *
 * @author hololol2
 */
public class ConnectionThread extends Thread {

    private final ClientConnection connection;
    private final Server server;

    public ConnectionThread(ClientConnection serverclient, Server server) {
        this.connection = serverclient;
        this.server = server;
    }

    @Override
    public void run() {
        String input;
        while (true) {
            try {
                input = connection.getScanner().next();
                String[] message = input.split(":");
                int length = message.length;
                if (message[0].equals("msg") && length == 3) {
                    server.sendMessage(server.getUserID(message[1]), connection.getUser().getUsername() + ":" + message[2]);
                }
                if (message[0].equals("friends") && length == 3) {
                    if (length >= 2 && message[1].equals("add")) {
                        String str = message[2];
                        Server.SERVER.addFriendRequest(connection.getUser().getID(), server.getUser(str).getID());
                    } else if (length >= 2 && message[1].equals("remove")) {
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
                if (message[0].equals("logout")) {
                    connection.closeConnection();
                    break;
                }
                if (message[0].equals("game") && length == 3) {
                    String str = message[1];
                    if (str.equals("accept")) {
                        if (server.challengeExists(connection.getUser().getID(), server.getUserID(message[2]))) {
                            server.startGame(connection.getUser(), server.getUser(message[2]));
                        }
                    }else if(str.equals("decline")){
                        
                    }
                }
                if(message[0].equals("newgame") && length >= 2){
                    if(message[1].equals("random")){
                        
                    }else if (length == 3 && server.isOnline(server.getUserID(message[2]))) {
                        server.getConnection(server.getUserID(message[2])).write("newgame:" + connection.getUser().getUsername());
                        server.challenge(connection.getUser().getID(), server.getUserID(message[2]));
                    } else {
                        connection.write("newgame:enemyoffline");
                    }
                }
                if (message[0].equals("move") && length == 4) {
                    int a = Integer.parseInt(message[1]);
                    int x = Integer.parseInt(message[2]);
                    int y = Integer.parseInt(message[3]);
                    if (a < 78) {
                        Chessman f = connection.getGame().getSquare(a/10,a%10).occupier;
                        if (f != null && !f.isBlack() == connection.isWhite() && f.isBlack() == connection.getGame().getTurn() && (f.doMove(x, y))) {
                            connection.getGame().getClient1().write("move:" + a + ":" + x + ":" + y);
                            connection.getGame().getClient2().write("move:" + a + ":" + x + ":" + y);
                        }
                        if (f != null && !f.isBlack() == connection.isWhite() && f.isBlack() == connection.getGame().getTurn() && (f.doCapture(x, y))) {
                            connection.getGame().getClient1().write("capture:" + a + ":" + x + ":" + y);
                            connection.getGame().getClient2().write("capture:" + a + ":" + x + ":" + y);
                        }
                    }
                }
                if (message[0].equals("resignation")) {
                    if (connection == connection.getGame().getClient1()) {
                        connection.getGame().getClient1().write("resignation:0");
                        connection.getGame().getClient2().write("resignation:1");
                    } else {
                        connection.getGame().getClient1().write("resignation:1");
                        connection.getGame().getClient2().write("resignation:0");
                    }
                }
                if (message[0].equals("draw")) {
                    connection.setDraw(true);
                    if (connection.getGame().getClient1().isDrawSet() && connection.getGame().getClient2().isDrawSet()) {
                        connection.getGame().getClient1().write("draw:1");
                        connection.getGame().getClient2().write("draw:1");
                    }

                    if (connection == connection.getGame().getClient1()) {
                        connection.getGame().getClient2().write("draw:0");
                    } else {
                        connection.getGame().getClient1().write("draw:0");
                    }

                }
                if (message[0].equals("promotion") && length == 4) {
                    String n = message[1];
                    String c = message[2];
                    boolean color;
                    color = c.equals("0");
                    String nia = message[3];
                    int nummerinarray = Integer.parseInt(nia);
                    switch (n) {
                        case "rook":
                            Chessman f = Rook.promotion((Pawn) connection.getGame().getFiguren(color)[nummerinarray], connection.getGame());
                            if (f != null) {
                                connection.getGame().getFiguren(color)[nummerinarray] = f;
                                connection.getGame().getClient1().write(input);
                                connection.getGame().getClient2().write(input);
                            }
                            break;
                        case "knight":
                            f = Knight.promotion((Pawn) connection.getGame().getFiguren(color)[nummerinarray], connection.getGame());
                            if (f != null) {
                                connection.getGame().getFiguren(color)[nummerinarray] = f;
                                connection.getGame().getClient1().write(input);
                                connection.getGame().getClient2().write(input);
                            }
                            break;
                        case "bishop":
                            f = Bishop.promotion((Pawn) connection.getGame().getFiguren(color)[nummerinarray], connection.getGame());
                            if (f != null) {
                                connection.getGame().getFiguren(color)[nummerinarray] = f;
                                connection.getGame().getClient1().write(input);
                                connection.getGame().getClient2().write(input);
                            }
                            break;
                        case "queen":
                            f = Queen.promotion((Pawn) connection.getGame().getFiguren(color)[nummerinarray], connection.getGame());
                            if (f != null) {
                                connection.getGame().getFiguren(color)[nummerinarray] = f;
                                connection.getGame().getClient1().write(input);
                                connection.getGame().getClient2().write(input);
                            }
                            break;
                    }

                }
                if (message[0].equals("change") && length == 3) {
                    if (message[1].equals("username")) {
                        try {
                            server.setUsername(connection.getUser().getID(), message[2]);
                            connection.write("change:username:accept:" + message[2]);
                        } catch (UsernameNotFreeException ex) {
                            connection.write("change:username:decline");
                        }
                    }
                    if (message[1].equals("password")) {
                        server.setPassword(connection.getUser().getID(), message[2]);
                        connection.write("change:password:accept");
                    }
                }
            } catch (NoSuchElementException nsee) {
                connection.closeConnection();
                break;
            } catch (UnknownUserException ex) {
                Log.MAINLOG.log(ex.getMessage());
            }
        }
    }
}
