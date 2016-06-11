package eu.assault2142.hololol.chess.networking;

import eu.assault2142.hololol.chess.game.ServerGame;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hololol2
 */
public class GameClientConnection {

    protected PrintWriter printwriter;
    protected ConnectionThread reader;
    protected Scanner scanner;
    private boolean draw;
    private ServerGame game;
    private Socket socket;

    public GameClientConnection(Socket socket) {
        draw = false;
        try {
            this.socket = socket;
            printwriter = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(socket.getInputStream());
            reader = new GameConnectionThread(this);
        } catch (IOException ex) {
            Logger.getLogger(GameClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeConnection() {
        try {
            printwriter.close();
            scanner.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(GameClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void endGame() {
        game = null;
        draw = false;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public ServerGame getGame() {
        return game;
    }

    public void setGame(ServerGame game) {
        this.game = game;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public boolean isDrawSet() {
        return draw;
    }

    public void startReading() {
        reader.start();
    }

    public void write(ClientMessages message, Object... replace) {
        printwriter.println(MessageFormat.format(message.getValue(), replace));
    }

}
