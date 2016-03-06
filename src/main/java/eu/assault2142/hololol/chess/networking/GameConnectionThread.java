package eu.assault2142.hololol.chess.networking;

import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 *
 * @author hololol2
 */
public class GameConnectionThread extends Thread {

    private final GameClientConnection connection;
    protected final LinkedList<Consumer<String[]>> consumers;

    public GameConnectionThread(GameClientConnection serverclient) {
        this.connection = serverclient;
        consumers = new LinkedList();
        consumers.add(this::consumeMove);
        consumers.add(this::consumeDraw);
        consumers.add(this::consumePromotion);
        consumers.add(this::consumeResignation);
    }

    @Override
    public void run() {
        String input;
        while (true) {
            try {
                input = connection.getScanner().next();
                String[] message = input.split(":");
                consumers.forEach((Consumer<String[]> consumer) -> {
                    consumer.accept(message);
                });
            }
            catch (NoSuchElementException nsee) {
                connection.closeConnection();
                break;
            }
        }
    }

    private void consumeMove(String[] message) {
        int length = message.length;
        if (message[0].equals("move") && length == 4) {
            int a = Integer.parseInt(message[1]);
            int x = Integer.parseInt(message[2]);
            int y = Integer.parseInt(message[3]);
            if (a < 78) {
                Chessman f = connection.getGame().getSquare(a / 10, a % 10).occupier;
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
    }

    private void consumePromotion(String[] message) {
        String input = message[0] + ":" + message[1] + ":" + message[2] + ":" + message[3];
        int length = message.length;
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
    }

    private void consumeResignation(String[] message) {
        if (message[0].equals("resignation")) {
            if (connection == connection.getGame().getClient1()) {
                connection.getGame().getClient1().write("resignation:0");
                connection.getGame().getClient2().write("resignation:1");
            } else {
                connection.getGame().getClient1().write("resignation:1");
                connection.getGame().getClient2().write("resignation:0");
            }
        }
    }

    private void consumeDraw(String[] message) {
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
    }
}
