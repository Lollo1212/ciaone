package eu.assault2142.hololol.chess.networking;

import eu.assault2142.hololol.chess.game.GameState;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 *
 * @author hololol2
 */
public class GameConnectionThread extends ConnectionThread {

    private final GameClientConnection connection;
    private GameState gamestate;
    protected HashMap<ServerMessages, Consumer<String[]>> consumers;

    public GameConnectionThread(GameClientConnection serverclient) {
        super(serverclient.getScanner());
        this.connection = serverclient;
        consumers = new HashMap();
        consumers.put(ServerMessages.Click, this::consumeClick);
        consumers.put(ServerMessages.Draw, this::consumeDraw);
        consumers.put(ServerMessages.Promotion, this::consumePromotion);
        consumers.put(ServerMessages.Resignation, this::consumeResignation);
        //gamestate = serverclient.getGame().getGameState();
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
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        connection.getGame().incomingClick(x, y, connection);
    }

    private void consumePromotion(String[] parts) {
        String target = parts[0];
        String c = parts[1];
        boolean color;
        color = c.equals("true");
        String nia = parts[2];
        int nummerinarray = Integer.parseInt(nia);
        connection.game.execPromotion(target, color, nummerinarray);
        connection.getGame().getClient1().write(ClientMessages.Promotion, new Object[]{target, c, nia});
        connection.getGame().getClient2().write(ClientMessages.Promotion, new Object[]{target, c, nia});
    }

    private void consumeResignation(String[] parts) {
        if (connection == connection.getGame().getClient1()) {
            connection.getGame().getClient1().write(ClientMessages.Resignation, new Object[]{0});
            connection.getGame().getClient2().write(ClientMessages.Resignation, new Object[]{1});
        } else {
            connection.getGame().getClient1().write(ClientMessages.Resignation, new Object[]{1});
            connection.getGame().getClient2().write(ClientMessages.Resignation, new Object[]{0});
        }
    }

    private void consumeDraw(String[] parts) {
        connection.setDraw(true);
        if (connection.getGame().getClient1().isDrawSet() && connection.getGame().getClient2().isDrawSet()) {
            connection.getGame().getClient1().write(ClientMessages.Draw, new Object[]{1});
            connection.getGame().getClient2().write(ClientMessages.Resignation, new Object[]{1});
        }

        if (connection == connection.getGame().getClient1()) {
            connection.getGame().getClient2().write(ClientMessages.DrawOffer, new Object[]{0});
        } else {
            connection.getGame().getClient1().write(ClientMessages.DrawOffer, new Object[]{0});
        }
    }

    @Override
    protected void closeConnection() {
        connection.closeConnection();
    }
}
