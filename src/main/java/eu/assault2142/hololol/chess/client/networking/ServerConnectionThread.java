package eu.assault2142.hololol.chess.client.networking;

import eu.assault2142.hololol.chess.client.game.ClientGame;
import eu.assault2142.hololol.chess.client.menus.MainMenu;
import eu.assault2142.hololol.chess.client.util.Translator;
import eu.assault2142.hololol.chess.game.GameState;
import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Movement;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.networking.ConnectionThread;
import eu.assault2142.hololol.chess.networking.ServerMessages;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import javax.swing.JOptionPane;

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
        MainMenu.MAINMENU.setPlayerName(parts[0]);
    }

    private void consumeAcceptUsernameChange(String[] parts) {
        JOptionPane.showMessageDialog(MainMenu.MAINMENU, java.text.MessageFormat.format(Translator.getString("NAMECHANGED_TEXT"), new Object[]{parts[0]}), Translator.getString("NAMECHANGED_HEAD"), JOptionPane.INFORMATION_MESSAGE);
    }

    private void consumeDeclineUsernameChange(String[] parts) {
        JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getString("NAMECHANGE_TAKEN_TEXT"), Translator.getString("NAMECHANGE_TAKEN_HEAD"), JOptionPane.WARNING_MESSAGE);
    }

    private void consumeAcceptPasswordChange(String[] parts) {
        JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getString("PASSCHANGED_TEXT"), Translator.getString("PASSCHANGED_HEAD"), JOptionPane.INFORMATION_MESSAGE);
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
        JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getString("DRAW_TEXT"), Translator.getString("DRAW_HEAD"), JOptionPane.INFORMATION_MESSAGE);
        game.endGame();
    }

    private void consumeDrawOffer(String[] parts) {
        JOptionPane.showConfirmDialog(MainMenu.MAINMENU, Translator.getString("DRAWOFFER_TEXT"), Translator.getString("DRAWOFFER_HEAD"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    private void consumeFriends(String[] parts) {
        String[] str = parts[0].split(";");
        MainMenu.MAINMENU.updateFriends(str);
    }

    private void consumeRequest(String[] parts) {
        int addfriend = JOptionPane.showConfirmDialog(MainMenu.MAINMENU, parts[0] + Translator.getString("FRIENDREQ_ADD_TEXT"), Translator.getString("FRIENDREQ_ADD_HEAD"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (addfriend == JOptionPane.NO_OPTION) {
            client.write(ServerMessages.DeclineFriend, new Object[]{parts[0]});
        } else if (addfriend == JOptionPane.YES_OPTION) {
            client.write(ServerMessages.AcceptFriend, new Object[]{parts[0]});
        }
    }

    private void consumeMessage(String[] parts) {
        String[] message = parts[0].split(":");
        if (message[0].equals("Info")) {
            JOptionPane.showMessageDialog(MainMenu.MAINMENU, message[1].replace("_", " "), Translator.getString("DIALOG_INFO_HEAD"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            MainMenu.MAINMENU.newMessage(message[0], message[1].replace("_", " "));

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
        if (parts[0].equals("1")) {
            JOptionPane.showMessageDialog(null, Translator.getString("RESIGNATION_ENEMY_TEXT"), Translator.getString("RESIGNATION_ENEMY_HEAD"), JOptionPane.INFORMATION_MESSAGE);
            game.endGame();
        } else {
            JOptionPane.showMessageDialog(null, Translator.getString("RESIGNATION_SELF_TEXT"), Translator.getString("RESIGNATION_SELF_HEAD"), JOptionPane.INFORMATION_MESSAGE);
            game.endGame();
        }
    }

    private void consumeGamestart(String[] parts) {
        client.startGame(parts[0]);
    }

    private void consumeNewGame(String[] parts) {
        if (parts[0].equals("enemyoffline")) {
            JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getString("GAME_ENEMYOFF_TEXT"), Translator.getString("GAME_ENEMYOFF_HEAD"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            String str = parts[0];
            int selected = JOptionPane.showConfirmDialog(MainMenu.MAINMENU, java.text.MessageFormat.format(Translator.getString("GAME_START?_TEXT"), new Object[]{str}), Translator.getString("GAME_START?_HEAD"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (selected == JOptionPane.YES_OPTION) {
                client.write(ServerMessages.AcceptGame, new Object[]{str});
            } else {
                client.write(ServerMessages.DeclineGame, new Object[]{str});
            }
        }
    }

    private void consumeNoSuchUsername(String[] parts) {
        JOptionPane.showMessageDialog(MainMenu.MAINMENU, "There is no User with this name!", "Unknown Username", JOptionPane.INFORMATION_MESSAGE);
    }

    private void consumeChallengeDeclined(String[] parts) {
        JOptionPane.showMessageDialog(MainMenu.MAINMENU, parts[0] + " declined your Challenge!", "Challenge Declined", JOptionPane.INFORMATION_MESSAGE);
    }
}
