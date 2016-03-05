package eu.assault2142.hololol.chess.server.networking;

import eu.assault2142.hololol.chess.game.GameSituation;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import eu.assault2142.hololol.chess.server.exceptions.UnknownUserException;
import eu.assault2142.hololol.chess.server.game.ServerGame;
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
public class ClientConnection implements eu.assault2142.hololol.chess.networking.ClientConnection{

    private Socket socket;
    private Server server;
    private final ConnectionThread reader;
    private Scanner scanner;
    private PrintWriter printwriter;
    private boolean white;
    private User user;
    private ServerGame game;
    private boolean draw;

    public ClientConnection(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        try {
            printwriter = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
        reader = new ConnectionThread(this, server);
    }

    public void write(String str) {
        printwriter.println(str);
    }

    public void startReading() {
        reader.start();
    }

    public void writeMovements() {
        GameSituation gs = game.getGameSituation();
        String str1 = "moves:black:move:";
        String str2 = "moves:white:move:";
        String str3 = "moves:black:capture:";
        String str4 = "move:white:capture:";
        for (int a = 0; a < 16; a++) {
            Move[] m = gs.getAbstractChessmen(true)[a].getMoves();
            for (Move m1 : m) {
                if (m1 != null) {
                    str1 += m1.getTargetX() + "" + m1.getTargetY() + "" + m1.getChessman().getPositionInArray() + ";";
                }
            }
            m = gs.getAbstractChessmen(true)[a].getCaptures();
            for (Move m1 : m) {
                if (m1 != null) {
                    str3 += m1.getTargetX() + "" + m1.getTargetY() + "" + m1.getChessman().getPositionInArray() + ";";
                }
            }
            m = gs.getAbstractChessmen(false)[a].getMoves();
            for (Move m1 : m) {
                if (m1 != null) {
                    str2 += m1.getTargetX() + "" + m1.getTargetY() + "" + m1.getChessman().getPositionInArray() + ";";
                }
            }
            m = gs.getAbstractChessmen(false)[a].getCaptures();
            for (Move m1 : m) {
                if (m1 != null) {
                    str4 += m1.getTargetX() + "" + m1.getTargetY() + "" + m1.getChessman().getPositionInArray() + ";";
                }
            }
        }
        write(str1);
        write(str2);
        write(str3);
        write(str4);
    }

    public Scanner getScanner() {
        return scanner;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void closeConnection() {
        Server.SERVER.logoutUser(user.getID());
        printwriter.close();
        scanner.close();
        try {
            socket.close();
        } catch (IOException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
        Log.MAINLOG.log(user.getUsername() + " logged out");
    }

    public ServerGame getGame() {
        return game;
    }

    public void setGame(ServerGame game) {
        this.game = game;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public boolean isDrawSet() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public void wrongUsername() {
        write("loginerror:username");
    }

    public void loggedin() {
        write("loggedin");
    }

    public void writeFriendList() {
        try {
            write(server.getFriendsAsString(getUser().getID()));
        } catch (UnknownUserException ex) {

        }
    }

    public void writeName() {
        write("name:" + getUser().getUsername());
    }

    public void wrongPassword() {
        write("loginerror:password");
    }

    public void checkmate() {
        write("checkmate");
    }

    public void stalemate() {
        write("stalemate");
    }

    public void check() {
        write("check");
    }

    void hello() {
        write("hello");
    }

    void usernameTaken() {
        write("usernametaken");
    }

    void startGame(int i) {
        write("gamestart:" + i);
    }

    void writeMessage(String msg) {
        write("msg:" + msg);
    }

    void friendRequest(String name) {
        write("request:" + name);
    }

    public void promotion(int i, int positioninarray) {
        write("promotion:" + i + ":" + positioninarray);
    }
}
