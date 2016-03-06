/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.networking;

import eu.assault2142.hololol.chess.client.menus.MainMenu;
import eu.assault2142.hololol.chess.client.translator.Translator;
import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import eu.assault2142.hololol.chess.networking.ConnectionThread;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author jojo
 */
public class ServerConnectionThread extends ConnectionThread {

    private final ServerConnection client;

    public ServerConnectionThread(ServerConnection c) {
        super(c.getScanner());
        this.client = c;
        consumers.add(this::consumeAccount);
        consumers.add(this::consumeCheckStaleMate);
        consumers.add(this::consumeDraw);
        consumers.add(this::consumeFriends);
        consumers.add(this::consumeMessage);
        consumers.add(this::consumeMoveCapture);
        consumers.add(this::consumeMoves);
        consumers.add(this::consumePromote);
        consumers.add(this::consumePromotion);
        consumers.add(this::consumeResignation);
        consumers.add(this::consumeStartGame);
    }

    @Override
    protected void closeConnection() {
    }

    private void consumeMessage(String[] message) {
        int length = message.length;
        if (message[0].equals("msg") && length == 3) {
            if (message[1].equals("Info")) {
                JOptionPane.showMessageDialog(MainMenu.MAINMENU, message[2].replace("_", " "), Translator.getBundle().getString("DIALOG_INFO_HEAD"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                MainMenu.MAINMENU.newMessage(message[1], message[2].replace("_", " "));
            }
        }
    }

    private void consumeAccount(String[] message) {
        int length = message.length;
        if (message[0].equals("name") && length == 2) {
            client.setName(message[1]);
            MainMenu.MAINMENU.setPlayerName(message[1]);
        } else if (message[0].equals("change") && length >= 3) {
            if (message[1].equals("username")) {
                if (message[2].equals("accept") && length == 4) {
                    JOptionPane.showMessageDialog(MainMenu.MAINMENU, java.text.MessageFormat.format(Translator.getBundle().getString("NAMECHANGED_TEXT"), new Object[]{message[3]}), Translator.getBundle().getString("NAMECHANGED_HEAD"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getBundle().getString("NAMECHANGE_TAKEN_TEXT"), Translator.getBundle().getString("NAMECHANGE_TAKEN_HEAD"), JOptionPane.WARNING_MESSAGE);
                }
            }
            if (message[1].equals("password") && message[2].equals("accept")) {
                JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getBundle().getString("PASSCHANGED_TEXT"), Translator.getBundle().getString("PASSCHANGED_HEAD"), JOptionPane.INFORMATION_MESSAGE);
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
            int addfriend = JOptionPane.showConfirmDialog(null, message[1] + Translator.getBundle().getString("FRIENDREQ_ADD_TEXT"), Translator.getBundle().getString("FRIENDREQ_ADD_HEAD"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (addfriend == JOptionPane.NO_OPTION) {
                client.declineFriendRequest(message[1]);
            } else {
                client.acceptFriendRequest(message[1]);
            }
        }
    }

    private void consumeStartGame(String[] message) {
        int length = message.length;
        if (message[0].equals("newgame") && length == 2) {
            if (message[1].equals("enemyoffline")) {
                JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getBundle().getString("GAME_ENEMYOFF_TEXT"), Translator.getBundle().getString("GAME_ENEMYOFF_HEAD"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                String str = message[1];
                int selected = JOptionPane.showConfirmDialog(MainMenu.MAINMENU, java.text.MessageFormat.format(Translator.getBundle().getString("GAME_START?_TEXT"), new Object[]{str}), Translator.getBundle().getString("GAME_START?_HEAD"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (selected == JOptionPane.YES_OPTION) {
                    client.acceptChallenge(str);
                } else {
                    client.declineChallenge(str);
                }
            }
        } else if (message[0].equals("gamestart") && length == 2) {
            //client.g = new LocalGame(client, Integer.parseInt(message[1]));
        }
    }

    private void consumeMoveCapture(String[] message) {
        int length = message.length;
        if (message[0].equals("move") && length == 4) {
            int a = Integer.parseInt(message[1]);
            int x = Integer.parseInt(message[2]);
            int y = Integer.parseInt(message[3]);
            //Chessman f = client.g.getSquares()[a].occupier;
            //client.g.getGameSituation().getAbstractChessmen(f.isBlack())[f.getPositionInArray()].addMove(new Move(x, y, f));
            //f.doMove(x, y);
            client.g.getGameFrame().getGameField().movementsupdating = true;
            //client.g.updateGameSituation();
        } else if (message[0].equals("capture") && length == 4) {
            int a = Integer.parseInt(message[1]);
            int x = Integer.parseInt(message[2]);
            int y = Integer.parseInt(message[3]);
            //Chessman f = client.g.getSquares()[a].occupier;
            //client.g.getGameSituation().getAbstractChessmen(f.isBlack())[f.getPositionInArray()].addCapture(new Move(x, y, f));
            //f.doCapture(x, y);
            client.g.getGameFrame().getGameField().movementsupdating = true;
            //client.g.updateGameSituation();
        }
    }

    private void consumeCheckStaleMate(String[] message) {
        if (message[0].equals("check")) {
            Runnable checkimage = () -> {
                try {
                    Thread.sleep(100);
                    client.g.getGameFrame().getGameField().check = true;
                    Thread.sleep(3000);
                    client.g.getGameFrame().getGameField().check = false;
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            };
            new Thread(checkimage).start();
        } else if (message[0].equals("checkmate")) {
            String gewinner = message[1];
            //Checkmate
            //InfoFrame f=new InfoFrame(gewinner+Start.START.texte[Start.START.einstellungen.sprache][3],200,100,true);
            //f.setVisible(true);
            client.g.getGameFrame().setVisible(false);
        } else if (message[0].equals("stalemate")) {
            JOptionPane.showMessageDialog(null, Translator.getBundle().getString("DRAW_TEXT"), Translator.getBundle().getString("DRAW_HEAD"), JOptionPane.INFORMATION_MESSAGE);

            client.g.getGameFrame().setVisible(false);
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

                        client.g.getGameSituation().getAbstractChessmen(color)[f].addMove(new Move(posx, posy, client.g.getFiguren(color)[f]));
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
                        client.g.getGameSituation().getAbstractChessmen(color)[f].addCapture(new Move(posx, posy, client.g.getFiguren(color)[f]));
                    }
                }
            }
            client.g.getGameFrame().getGameField().movementsupdating = false;
            client.g.resetFields();
        }
    }

    private void consumeResignation(String[] message) {
        int length = message.length;
        if (message[0].equals("resignation") && length == 2) {
            if (message[1].equals("1")) {
                JOptionPane.showMessageDialog(null, Translator.getBundle().getString("RESIGNATION_ENEMY_TEXT"), Translator.getBundle().getString("RESIGNATION_ENEMY_HEAD"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, Translator.getBundle().getString("RESIGNATION_SELF_TEXT"), Translator.getBundle().getString("RESIGNATION_SELF_HEAD"), JOptionPane.INFORMATION_MESSAGE);
            }
            client.g.getGameFrame().setVisible(false);
        }
    }

    private void consumeDraw(String[] message) {
        int length = message.length;
        if (message[0].equals("draw") && length == 2) {
            if (message[1].equals("0")) {
                JOptionPane.showConfirmDialog(MainMenu.MAINMENU, Translator.getBundle().getString("DRAWOFFER_TEXT"), Translator.getBundle().getString("DRAWOFFER_HEAD"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getBundle().getString("DRAW_TEXT"), Translator.getBundle().getString("DRAW_HEAD"), JOptionPane.INFORMATION_MESSAGE);
                client.g.getGameFrame().setVisible(false);
            }
        }
    }

    private void consumePromotion(String[] message) {
        int length = message.length;
        if (message[0].equals("promotion") && length == 3) {

            int nummerinarray = Integer.parseInt(message[2]);
            String co = message[1];
            boolean color;
            if (client.equals("0")) {
                color = true;
            } else {
                color = false;
            }
            switch (Integer.parseInt(message[2])) {
                case 0:
                    client.g.getFiguren(color)[nummerinarray] = Rook.promotion((Pawn) client.g.getFiguren(color)[nummerinarray], client.g);
                    break;
                case 1:
                    client.g.getFiguren(color)[nummerinarray] = Knight.promotion((Pawn) client.g.getFiguren(color)[nummerinarray], client.g);
                    break;
                case 2:
                    client.g.getFiguren(color)[nummerinarray] = Bishop.promotion((Pawn) client.g.getFiguren(color)[nummerinarray], client.g);
                    break;
                case 3:
                    client.g.getFiguren(color)[nummerinarray] = Queen.promotion((Pawn) client.g.getFiguren(color)[nummerinarray], client.g);
                    break;
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
            JOptionPane.showInputDialog(client.getGame().getGameFrame(), Translator.getBundle().getString("PROMOTION_TEXT"), Translator.getBundle().getString("PROMOTION_HEAD"), JOptionPane.QUESTION_MESSAGE, null, new String[]{Translator.getBundle().getString("CHESSMAN_QUEEN"), Translator.getBundle().getString("CHESSMAN_ROOK"), Translator.getBundle().getString("CHESSMAN_KNIGHT"), Translator.getBundle().getString("CHESSMAN_BISHOP")}, Translator.getBundle().getString("CHESSMAN_QUEEN"));
        }
    }
}
