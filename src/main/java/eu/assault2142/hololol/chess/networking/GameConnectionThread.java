package eu.assault2142.hololol.chess.networking;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 *
 * @author hololol2
 */
public class GameConnectionThread extends ConnectionThread {

    protected HashMap<ServerMessages, Consumer<String[]>> consumers;
    private final GameClientConnection connection;

    public GameConnectionThread(GameClientConnection serverclient) {
        super(serverclient.getScanner());
        this.connection = serverclient;
        consumers = new HashMap();
        consumers.put(ServerMessages.Click, this::consumeClick);
        consumers.put(ServerMessages.Draw, this::consumeDraw);
        consumers.put(ServerMessages.Promotion, this::consumePromotion);
        consumers.put(ServerMessages.Resignation, this::consumeResignation);
    }

    @Override
    protected void closeConnection() {
        connection.closeConnection();
    }

    @Override
    protected void consume(String message) {
        Arrays.stream(ServerMessages.values()).forEach((ServerMessages m) -> {
            try {
                String[] parts = parse(message, m.getFormat());
                consumers.getOrDefault(m, this::consumeUnknown).accept(parts);
            } catch (ParseException ex) {
            }
        });
    }

    private void consumeClick(String[] parts) {
        int squareX = Integer.parseInt(parts[0]);
        int squareY = Integer.parseInt(parts[1]);
        connection.getGame().incomingClick(squareX, squareY, connection);
    }

    private void consumeDraw(String[] parts) {
        connection.setDraw(true);
        if (connection.getGame().getClient1().isDrawSet() && connection.getGame().getClient2().isDrawSet()) {
            connection.getGame().getClient1().write(ClientMessages.Draw);
            connection.getGame().getClient2().write(ClientMessages.Draw);
            connection.getGame().getClient1().setDraw(false);
            connection.getGame().getClient2().setDraw(false);
            connection.getGame().endGame();
        } else if (connection == connection.getGame().getClient1()) {
            connection.getGame().getClient2().write(ClientMessages.DrawOffer);
        } else {
            connection.getGame().getClient1().write(ClientMessages.DrawOffer);
        }
    }

    private void consumePromotion(String[] parts) {
        String target = parts[0];
        String c = parts[1];
        boolean color = c.equals("true");
        String nia = parts[2];
        int number = Integer.parseInt(nia);
        connection.getGame().execPromotion(target, color, number);
        connection.getGame().getClient1().write(ClientMessages.Promotion, target, c, nia);
        connection.getGame().getClient2().write(ClientMessages.Promotion, target, c, nia);
    }

    private void consumeResignation(String[] parts) {
        if (connection.equals(connection.getGame().getClient1())) {
            connection.getGame().getClient1().write(ClientMessages.Resignation, 0);
            connection.getGame().getClient2().write(ClientMessages.Resignation, 1);
        } else {
            connection.getGame().getClient1().write(ClientMessages.Resignation, 1);
            connection.getGame().getClient2().write(ClientMessages.Resignation, 0);
        }
        connection.getGame().endGame();
    }

}
