package eu.assault2142.hololol.chess.client.networking;

import eu.assault2142.hololol.chess.client.game.ClientGame;
import eu.assault2142.hololol.chess.client.game.Main;
import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.networking.ConnectionThread;
import eu.assault2142.hololol.chess.networking.ServerMessages;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Consumes inputs from the server
 *
 * @author hololol2
 */
public class ServerConnectionThread extends ConnectionThread {

    private final ServerConnection connection;
    private ClientGame game;
    private GameState gamestate;
    private final HashMap<ClientMessages, Consumer<String[]>> consumers;

    /**
     * Create a new ServerConnectionThread
     *
     * @param connection the connection to the scanner
     * @param scanner the scanner for the inputs
     */
    public ServerConnectionThread(ServerConnection connection, Scanner scanner) {
        super(scanner);
        this.connection = connection;
        consumers = new HashMap();
        consumers.put(ClientMessages.AcceptPasswordChange, this::consumeAcceptPasswordChange);
        consumers.put(ClientMessages.AcceptUsernameChange, this::consumeAcceptUsernameChange);
        consumers.put(ClientMessages.Check, this::consumeCheck);
        consumers.put(ClientMessages.Checkmate, this::consumeCheckmate);
        consumers.put(ClientMessages.DeclinePasswordChange, this::consumeDeclinePasswordChange);
        consumers.put(ClientMessages.DeclineUsernameChange, this::consumeDeclineUsernameChange);
        consumers.put(ClientMessages.Draw, this::consumeDraw);
        consumers.put(ClientMessages.DrawOffer, this::consumeDrawOffer);
        consumers.put(ClientMessages.Friends, this::consumeFriends);
        consumers.put(ClientMessages.Gamestart, this::consumeGamestart);
        consumers.put(ClientMessages.Message, this::consumeMessage);
        consumers.put(ClientMessages.Move, this::consumeMove);
        consumers.put(ClientMessages.Name, this::consumeName);
        consumers.put(ClientMessages.Newgame, this::consumeNewGame);
        consumers.put(ClientMessages.Promote, this::consumePromote);
        consumers.put(ClientMessages.Promotion, this::consumePromotion);
        consumers.put(ClientMessages.Request, this::consumeRequest);
        consumers.put(ClientMessages.Resignation, this::consumeResignation);
        consumers.put(ClientMessages.Stalemate, this::consumeStalemate);
        consumers.put(ClientMessages.UsernameWrong, this::consumeNoSuchUsername);
        consumers.put(ClientMessages.Capture, this::consumeCapture);
    }

    @Override
    protected void closeConnection() {
    }

    /**
     * Sets the game
     *
     * @param game the game
     */
    public void setGame(ClientGame game) {
        this.game = game;
    }

    @Override
    protected void consume(String message) {
        Arrays.stream(ClientMessages.values()).forEach((ClientMessages m) -> {
            try {
                String[] parts = parse(message, m.getFormat());
                consumers.getOrDefault(m, this::consumeUnknown).accept(parts);
            } catch (ParseException ex) {
            }
        });
    }

    private void consumeName(String[] parts) {
        connection.setName(parts[0]);
        Main.MENU.setPlayerName(parts[0]);
    }

    private void consumeAcceptUsernameChange(String[] parts) {
        Main.MENU.usernameChanged(parts[0]);
    }

    private void consumeDeclineUsernameChange(String[] parts) {
        Main.MENU.usernameTaken();
    }

    private void consumeAcceptPasswordChange(String[] parts) {
        Main.MENU.passwordChanged();
    }

    private void consumeDeclinePasswordChange(String[] parts) {
        Main.MENU.showErrorMessage("Changing of Password not successfull!", false);
    }

    private void consumeCheck(String[] parts) {
        game.incomingCheck();
    }

    private void consumeCheckmate(String[] parts) {
        game.incomingCheckMate();
    }

    private void consumeStalemate(String[] parts) {
        game.incomingStaleMate();
    }

    private void consumeDraw(String[] parts) {
        game.incomingDraw();
    }

    private void consumeDrawOffer(String[] parts) {
        game.drawOffer();
    }

    private void consumeFriends(String[] parts) {
        String[] friends = parts[0].split(";");
        Main.MENU.updateFriends(friends);
    }

    private void consumeRequest(String[] parts) {
        Main.MENU.friendRequest(parts[0]);
    }

    private void consumeMessage(String[] parts) {
        String[] message = parts[0].split(":");
        if (message[0].equals("Info")) {
            Main.MENU.infoMessage(message[1].replace("_", " "));
        } else {
            Main.MENU.newMessage(message[0], message[1].replace("_", " "));

        }
    }

    private void consumeMove(String[] parts) {
        int number = Integer.parseInt(parts[0]);
        int targetX = Integer.parseInt(parts[1]);
        int targetY = Integer.parseInt(parts[2]);
        game.doMove(number, targetX, targetY);
    }

    private void consumeCapture(String[] parts) {
        int number = Integer.parseInt(parts[0]);
        int targetX = Integer.parseInt(parts[1]);
        int targetY = Integer.parseInt(parts[2]);
        game.doCapture(number, targetX, targetY);
    }

    private void consumePromote(String[] parts) {
        connection.write(ServerMessages.Promotion, new Object[]{game.getGameView().showPromotionChoice(), game.isBlack(), parts[0]});
    }

    private void consumePromotion(String[] parts) {
        int nummerinarray = Integer.parseInt(parts[2]);
        boolean color = parts[1].equals("0");
        game.execPromotion(parts[0], color, nummerinarray);
    }

    private void consumeResignation(String[] parts) {
        game.incomingResignation(parts[0].equals("1"));
    }

    private void consumeGamestart(String[] parts) {
        game = new ClientGame(connection, !parts[0].equals("0"));
    }

    private void consumeNewGame(String[] parts) {
        if (parts[0].equals("enemyoffline")) {
            Main.MENU.enemyOffline();
        } else {
            Main.MENU.gameChallenge(parts[0]);
        }
    }

    private void consumeNoSuchUsername(String[] parts) {
        Main.MENU.unknownUsername();
    }

    private void consumeChallengeDeclined(String[] parts) {
        Main.MENU.challengeDeclined(parts[0]);
    }
}
