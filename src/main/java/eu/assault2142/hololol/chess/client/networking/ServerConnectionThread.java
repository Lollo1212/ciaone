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
import java.text.MessageFormat;
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
    private HashMap<ClientMessages, Consumer<String[]>> consumer;

    /**
     * Create a new ServerConnectionThread
     *
     * @param c the connection to the scanner
     * @param scanner the scanner for the inputs
     */
    public ServerConnectionThread(ServerConnection c, Scanner scanner) {
        super(scanner);
        this.client = c;
        consumer.put(ClientMessages.Name, this::consumeName);
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

    private void consume(String message) {
        Arrays.stream(ClientMessages.values()).forEach((ClientMessages m) -> {
            try {
                String[] parts = parse(message, m.getFormat());
                consumer.getOrDefault(m, this::consumeUnknown).accept(parts);
            } catch (ParseException ex) {
            }
        });
    }

    private String[] parse(String message, MessageFormat format) throws ParseException {
        return Arrays.stream(format.parse(message)).toArray(String[]::new);
    }

    private void consumeUnknown(String[] parts) {

    }

    private void consumeName(String[] parts) {
        client.setName(parts[0].toString());
        MainMenu.MAINMENU.setPlayerName(parts[0].toString());
    }

    private void consumeAccount(String message) {
        if (message[0].equals("change") && length >= 3) {
            if (message[1].equals("username")) {
                if (message[2].equals("accept") && length == 4) {
                    JOptionPane.showMessageDialog(MainMenu.MAINMENU, java.text.MessageFormat.format(Translator.getString("NAMECHANGED_TEXT"), new Object[]{message[3]}), Translator.getString("NAMECHANGED_HEAD"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getString("NAMECHANGE_TAKEN_TEXT"), Translator.getString("NAMECHANGE_TAKEN_HEAD"), JOptionPane.WARNING_MESSAGE);
                }
            }
            if (message[1].equals("password") && message[2].equals("accept")) {
                JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getString("PASSCHANGED_TEXT"), Translator.getString("PASSCHANGED_HEAD"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void consumeCheckStaleMate(String[] message) {
        if (message[0].equals("check")) {
            game.onCheck();
        } else if (message[0].equals("checkmate")) {
            game.onCheckMate();
        } else if (message[0].equals("stalemate")) {
            game.onStaleMate();
        }
    }

    private void consumeDraw(String[] message) {
        int length = message.length;
        if (message[0].equals("draw")) {
            if (length == 2 && message[1].equals("offer")) {
                JOptionPane.showConfirmDialog(MainMenu.MAINMENU, Translator.getString("DRAWOFFER_TEXT"), Translator.getString("DRAWOFFER_HEAD"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getString("DRAW_TEXT"), Translator.getString("DRAW_HEAD"), JOptionPane.INFORMATION_MESSAGE);
                game.endGame();
            }
        }
    }

    private void consumeFriends(String[] message) {
        int length = message.length;
        if (message[0].equals("friends")) {
            if (length == 2) {
                String[] str = message[1].split(";");
                MainMenu.MAINMENU.updateFriends(str);
            } else {
                MainMenu.MAINMENU.updateFriends(new String[0]);
            }
        } else if (message[0].equals("request") && length == 2) {
            int addfriend = JOptionPane.showConfirmDialog(null, message[1] + Translator.getString("FRIENDREQ_ADD_TEXT"), Translator.getString("FRIENDREQ_ADD_HEAD"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (addfriend == JOptionPane.NO_OPTION) {
                client.write(ServerMessages.DeclineFriend, new Object[]{message[1]});
            } else {
                client.write(ServerMessages.AcceptFriend, new Object[]{message[1]});
            }
        }
    }

    private void consumeMessage(String[] message) {
        int length = message.length;
        if (message[0].equals("msg") && length == 3) {
            if (message[1].equals("Info")) {
                JOptionPane.showMessageDialog(MainMenu.MAINMENU, message[2].replace("_", " "), Translator.getString("DIALOG_INFO_HEAD"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                MainMenu.MAINMENU.newMessage(message[1], message[2].replace("_", " "));
            }
        }
    }

    private void consumeMoveCapture(String[] message) {
        int length = message.length;
        if (message[0].equals("move") && length == 4) {
            int a = Integer.parseInt(message[1]);
            int x = Integer.parseInt(message[2]);
            int y = Integer.parseInt(message[3]);
            game.doMove(a, x, y);
        } else if (message[0].equals("capture") && length == 4) {
            int a = Integer.parseInt(message[1]);
            int x = Integer.parseInt(message[2]);
            int y = Integer.parseInt(message[3]);
            game.doCapture(a, x, y);
        }
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

    private void consumePromote(String[] message) {
        int length = message.length;
        if (message[0].equals("promote") && length == 3) {
            boolean color;
            if (message[1].equals("0")) {
                color = true;
            } else {
                color = false;
            }
            game.getGameView().showPromotionChoice();
        }
    }

    private void consumePromotion(String[] message) {
        int length = message.length;
        if (message[0].equals("promotion") && length == 3) {

            int nummerinarray = Integer.parseInt(message[2]);
            String co = message[1];
            boolean color;
            color = co.equals("0");
            game.promotion((Pawn) gamestate.getChessmen(color)[nummerinarray]);
            switch (Integer.parseInt(message[2])) {
                case 0:
                    gamestate.getChessmen(color)[nummerinarray] = Rook.promotion((Pawn) gamestate.getChessmen(color)[nummerinarray], gamestate);
                    break;
                case 1:
                    gamestate.getChessmen(color)[nummerinarray] = Knight.promotion((Pawn) gamestate.getChessmen(color)[nummerinarray], gamestate);
                    break;
                case 2:
                    gamestate.getChessmen(color)[nummerinarray] = Bishop.promotion((Pawn) gamestate.getChessmen(color)[nummerinarray], gamestate);
                    break;
                case 3:
                    gamestate.getChessmen(color)[nummerinarray] = Queen.promotion((Pawn) gamestate.getChessmen(color)[nummerinarray], gamestate);
                    break;
            }
        }
    }

    private void consumeResignation(String[] message) {
        int length = message.length;
        if (message[0].equals("resignation") && length == 2) {
            if (message[1].equals("1")) {
                JOptionPane.showMessageDialog(null, Translator.getString("RESIGNATION_ENEMY_TEXT"), Translator.getString("RESIGNATION_ENEMY_HEAD"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, Translator.getString("RESIGNATION_SELF_TEXT"), Translator.getString("RESIGNATION_SELF_HEAD"), JOptionPane.INFORMATION_MESSAGE);
            }
            game.getGameView().hide();
        }
    }

    private void consumeStartGame(String[] message) {
        int length = message.length;
        if (message[0].equals("newgame") && length == 2) {
            if (message[1].equals("enemyoffline")) {
                JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getString("GAME_ENEMYOFF_TEXT"), Translator.getString("GAME_ENEMYOFF_HEAD"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                String str = message[1];
                int selected = JOptionPane.showConfirmDialog(MainMenu.MAINMENU, java.text.MessageFormat.format(Translator.getString("GAME_START?_TEXT"), new Object[]{str}), Translator.getString("GAME_START?_HEAD"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (selected == JOptionPane.YES_OPTION) {
                    client.write(ServerMessages.AcceptGame, new Object[]{str});
                } else {
                    client.write(ServerMessages.DeclineGame, new Object[]{str});
                }
            }
        } else if (message[0].equals("gamestart") && length == 2) {
            client.startGame(message[1]);
        }
    }
}
