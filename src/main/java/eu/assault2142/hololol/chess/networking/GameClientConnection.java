package eu.assault2142.hololol.chess.networking;

import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.game.ServerGame;
import eu.assault2142.hololol.chess.game.chessmen.Movement;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author hololol2
 */
public class GameClientConnection {

    private Socket socket;
    protected ConnectionThread reader;
    protected Scanner scanner;
    protected PrintWriter printwriter;
    private boolean white;
    protected ServerGame game;
    private boolean draw;

    public GameClientConnection(Socket socket) {
        this.socket = socket;
        try {
            printwriter = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
        }
        reader = new GameConnectionThread(this);
    }

    private void write(String str) {
        printwriter.println(str);
    }

    public void startReading() {
        reader.start();
    }

    public void write(ClientMessages message, Object[] replace) {
        write(MessageFormat.format(message.getValue(), replace));
    }

    public void writeMovements() {
        GameState gs = game.getGameState();
        String str1 = "moves:black:move:";
        String str2 = "moves:white:move:";
        String str3 = "moves:black:capture:";
        String str4 = "move:white:capture:";
        for (int a = 0; a < 16; a++) {
            List<Movement> m = gs.getChessmen(true)[a].getMoves();
            for (Movement m1 : m) {
                if (m1 != null) {
                    str1 += m1.getTargetX() + "" + m1.getTargetY() + "" + m1.getChessman().getPositionInArray() + ";";
                }
            }
            m = gs.getChessmen(true)[a].getCaptures();
            for (Movement m1 : m) {
                if (m1 != null) {
                    str3 += m1.getTargetX() + "" + m1.getTargetY() + "" + m1.getChessman().getPositionInArray() + ";";
                }
            }
            m = gs.getChessmen(false)[a].getMoves();
            for (Movement m1 : m) {
                if (m1 != null) {
                    str2 += m1.getTargetX() + "" + m1.getTargetY() + "" + m1.getChessman().getPositionInArray() + ";";
                }
            }
            m = gs.getChessmen(false)[a].getCaptures();
            for (Movement m1 : m) {
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

    public void closeConnection() {
        printwriter.close();
        scanner.close();
        try {
            socket.close();
        } catch (IOException ex) {
        }
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

    void startGame(int i) {
        write("gamestart:" + i);
    }

    public void promotion(int i, int positioninarray) {
        write("promotion:" + i + ":" + positioninarray);
    }

    ServerGame getGame() {
        return game;
    }
}
