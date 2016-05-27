package eu.assault2142.hololol.chess.client.networking;

import eu.assault2142.hololol.chess.client.game.ClientGame;
import eu.assault2142.hololol.chess.client.game.Main;
import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Movement;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.networking.ConnectionThread;
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

    private final ServerConnection client;
    private ClientGame game;
    private GameState gamestate;
    private HashMap<ClientMessages, Consumer<String[]>> consumers;

    /**
     * Create a new ServerConnectionThread
     *
     * @param c the connection to the scanner
     * @param scanner the scanner for the inputs
     */
    public ServerConnectionThread(ServerConnection c, Scanner scanner) {
        super(scanner);
        this.client = c;
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
        consumers.put(ClientMessages.Moves, this::consumeMoves);
        consumers.put(ClientMessages.Name, this::consumeName);
        consumers.put(ClientMessages.Newgame, this::consumeNewGame);
        consumers.put(ClientMessages.Promote, this::consumePromote);
        consumers.put(ClientMessages.Promotion, this::consumePromotion);
        consumers.put(ClientMessages.Request, this::consumeRequest);
        consumers.put(ClientMessages.Resignation, this::consumeResignation);
        consumers.put(ClientMessages.Stalemate, this::consumeStalemate);
        consumers.put(ClientMessages.UsernameWrong, this::consumeNoSuchUsername);
        //gamestate = game.getGameState();
    }

    @Override
    protected void closeConnection() {
    }

    /**
     * Sets the game
     *
     * @param game the game
     */
    void setGame(ClientGame game) {
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
        client.setName(parts[0]);
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

    }

    private void consumeCheck(String[] parts) {
        game.onCheck();
    }

    private void consumeCheckmate(String[] parts) {
        game.onCheckMate();
    }

    private void consumeStalemate(String[] parts) {
        game.onStaleMate();
    }

    private void consumeDraw(String[] parts) {
        game.onDraw();
    }

    private void consumeDrawOffer(String[] parts) {
        game.drawOffer();
    }

    private void consumeFriends(String[] parts) {
        String[] str = parts[0].split(";");
        Main.MENU.updateFriends(str);
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
        int a = Integer.parseInt(parts[0]);
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        game.doMove(a, x, y);
    }

    private void consumeMoves(String[] message) {
        int length = message.length;
        if (message[0].equals("moves") && length == 4) {
            boolean color;
            if (message[1].equals("black")) {
                color = true;
            } else {
                color = false;
            }
            if (message[2].equals("move")) {
                String s = message[3];
                String[] str = s.split(";");
                for (int a = 0; a < str.length; a++) {
                    if (str[a].length() == 3 || str[a].length() == 4) {
                        String x = str[a].substring(0, 1);
                        int posx = Integer.parseInt(x);
                        String y = str[a].substring(1, 2);
                        int posy = Integer.parseInt(y);
                        String fn = str[a].substring(2);
                        int f = Integer.parseInt(fn);

                        game.getGameState().getChessmen(color)[f].addMove(new Movement(posx, posy, gamestate.getChessmen(color)[f]));
                    }
                }
            } else {
                String s = message[3];
                String[] str = s.split(";");
                for (int a = 0; a < str.length; a++) {
                    if (str[a].length() == 3 || str[a].length() == 4) {
                        String x = str[a].substring(0, 1);
                        int posx = Integer.parseInt(x);
                        String y = str[a].substring(1, 2);
                        int posy = Integer.parseInt(y);
                        String fn = str[a].substring(2);
                        int f = Integer.parseInt(fn);
                        game.getGameState().getChessmen(color)[f].addCapture(new Movement(posx, posy, gamestate.getChessmen(color)[f]));
                    }
                }
            }
        }
    }

    private void consumePromote(String[] parts) {
        game.getGameView().showPromotionChoice();
    }

    private void consumePromotion(String[] parts) {
        int nummerinarray = Integer.parseInt(parts[2]);
        String co = parts[1];
        boolean color;
        color = co.equals("0");
        game.promotion((Pawn) gamestate.getChessmen(color)[nummerinarray]);
        switch (parts[0]) {
            case "ROOK":
                gamestate.getChessmen(color)[nummerinarray] = Rook.promotion((Pawn) gamestate.getChessmen(color)[nummerinarray], gamestate);
                break;
            case "KNIGHT":
                gamestate.getChessmen(color)[nummerinarray] = Knight.promotion((Pawn) gamestate.getChessmen(color)[nummerinarray], gamestate);
                break;
            case "BISHOP":
                gamestate.getChessmen(color)[nummerinarray] = Bishop.promotion((Pawn) gamestate.getChessmen(color)[nummerinarray], gamestate);
                break;
            case "QUEEN":
                gamestate.getChessmen(color)[nummerinarray] = Queen.promotion((Pawn) gamestate.getChessmen(color)[nummerinarray], gamestate);
                break;
        }
    }

    private void consumeResignation(String[] parts) {
        game.onResignation(parts[0].equals("1"));
    }

    private void consumeGamestart(String[] parts) {
        client.startGame(parts[0]);
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
