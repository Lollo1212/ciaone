/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.networking;

import eu.assault2142.hololol.chess.client.game.LocalGame;
import eu.assault2142.hololol.chess.client.game.ui.GameFrame;
import eu.assault2142.hololol.chess.client.menus.MainMenu;
import eu.assault2142.hololol.chess.client.translator.Translator;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author jojo
 */
public class ServerConnection {

    LocalGame g;
    GameFrame gframe;
    boolean gamestarted;
    Scanner sc;
    PrintWriter pw;
    Socket s;
    ServerConnectionThread ct;
    public Thread t;
    private String name;

    public ServerConnection(InetAddress address) {
        try {
            KeyStore ks = KeyStore.getInstance(Translator.getBundle().getString("JKS"));
            ks.load(new FileInputStream(Translator.getBundle().getString("TRUSTSTORE")), Translator.getBundle().getString("ASSAULT").toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            SSLContext sslcon = SSLContext.getInstance(Translator.getBundle().getString("TLS"));
            TrustManager[] trustManagers = tmf.getTrustManagers();
            sslcon.init(null, trustManagers, null);

            SSLSocketFactory ssf = sslcon.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(address, 1024);
            socket.startHandshake();
            s = socket;
            sc = new Scanner(s.getInputStream());
            pw = new PrintWriter(s.getOutputStream(), true);
        } catch (ConnectException ex) {
            JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getBundle().getString("COULDN'T CONNECT TO SERVER"), Translator.getBundle().getString("CONNECTION ERROR"), JOptionPane.WARNING_MESSAGE);
            MainMenu.MAINMENU.enableLoginButton();
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ServerConnection(Socket so, Scanner sca, PrintWriter pwr) {
        s = so;
        sc = sca;
        pw = pwr;
        write(Translator.getBundle().getString("N"));
        ct = new ServerConnectionThread(this);
        t = new Thread(ct);
        t.start();
    }

    private void write(String str) {
        pw.println(str);
    }

    public Scanner getScanner() {
        return sc;
    }

    public Socket getSocket() {
        return s;
    }

    public PrintWriter getPrintWriter() {
        return pw;
    }

    public LocalGame getGame() {
        return g;
    }

    public static void connect(String username, String password, boolean create) {
        ServerConnection c = null;
        try {
            InetAddress i = InetAddress.getByName(Translator.getBundle().getString("OWNCLOUD.ASSAULT2142.EU"));
            c = new ServerConnection(i);
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
        }
        if (c.getScanner() != null) {
            c.getScanner().next();
            //Der Server ist online
            //Anmeldedaten an Server schicken
            String str = "";
            if (create) {
                str += Translator.getBundle().getString("R:");
            }
            str += username + java.text.MessageFormat.format(Translator.getBundle().getString(":{0}"), new Object[]{password});
            c.write(str);
            String input = c.getScanner().next();
            System.err.println(input);
            if (input.equals(Translator.getBundle().getString("LOGGEDIN"))) {
                //Der Server bestätigt Anmeldung

                c = new ServerConnection(c.getSocket(), c.getScanner(), c.getPrintWriter());
                MainMenu.MAINMENU.loggedIn(c);//neues Fenster öffnen
            } else//Server verweigert Anmeldung
            {
                if (create) {
                    JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getBundle().getString("ACCOUNT EXISTIERT BEREITS"), Translator.getBundle().getString("LOGIN ERROR"), JOptionPane.ERROR_MESSAGE);
                    MainMenu.MAINMENU.enableLoginButton();
                    //InfoFrame f=new InfoFrame("Account existiert bereits",300,100,true);
                } else if (input.equals(Translator.getBundle().getString("LOGINERROR:PASSWORD"))) {
                    JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getBundle().getString("PASSWORT FALSCH"), Translator.getBundle().getString("LOGIN ERROR"), JOptionPane.ERROR_MESSAGE);
                    MainMenu.MAINMENU.enableLoginButton();
                } else {
                    JOptionPane.showMessageDialog(MainMenu.MAINMENU, Translator.getBundle().getString("ACCOUNT EXISTIERT NICHT"), Translator.getBundle().getString("LOGIN ERROR"), JOptionPane.ERROR_MESSAGE);
                    MainMenu.MAINMENU.enableLoginButton();
                }//InfoFrame f=new InfoFrame("Benutzername oder Passwort falsch",300,100,true);
            }
        }
    }

    public void setName(String string) {
        name = string;
    }

    public String getName() {
        return name;
    }

    public void writeMessage(String to, String msg) {
        write("msg:" + to + ":" + msg.replace(" ", "_"));
    }

    public void addFriend(String name) {
        write("friends:add:" + name);
    }

    public void removeFriend(String name) {
        write("friends:remove:" + name);
    }

    public void acceptFriendRequest(String name) {
        write("friends:accept:" + name);
    }

    public void declineFriendRequest(String name) {
        write("friends:decline:" + name);
    }

    public void logout() {
        write("logout");
    }

    public void changeUsername(String newname) {
        write("change:username:" + newname);
    }

    public void changePassword(String newpass) {
        write("change:password:" + newpass);
    }

    public void acceptChallenge(String challenger) {
        write("game:accept:" + challenger);
    }

    public void declineChallenge(String challenger) {
        write("game:decline:" + challenger);
    }

    public void challengeFriend(String name) {
        write("newgame:friend:" + name);
    }

    public void playRandom() {
        write("newgame:random");
    }

    public void doMove(int chessman, int x, int y) {
        write("move:" + chessman + ":" + x + ":" + y);
    }

    public void resignate() {
        write("resignation");
    }

    public void offerDraw() {
        write("draw");
    }

    public void promotion(String chessman, int color, int posinarray) {
        write("promotion:" + chessman + ":" + color + ":" + posinarray);
    }

}
