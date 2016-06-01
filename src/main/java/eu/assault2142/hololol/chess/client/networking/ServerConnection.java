package eu.assault2142.hololol.chess.client.networking;

import eu.assault2142.hololol.chess.client.game.ClientGame;
import eu.assault2142.hololol.chess.client.game.Main;
import eu.assault2142.hololol.chess.client.menu.IMenu;
import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.networking.ServerMessages;
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
import java.text.MessageFormat;
import java.util.Scanner;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * The Connection to a (game-)server
 *
 * @author hololol2
 */
public class ServerConnection {

    /**
     * Connects to the official, central server
     *
     * @param username the username
     * @param password the password
     * @param create wether to create an account
     */
    public static void connect(String username, String password, boolean create) {
        ServerConnection c = null;
        try {
            InetAddress i = InetAddress.getByName("assault2142.eu");
            c = new ServerConnection(i);
        } catch (UnknownHostException ex) {
            Main.MENU.showErrorMessage("Couldn't find Server's IP!", false);
        }
        if (c.scanner != null) {
            c.scanner.next();
            //Der Server ist online
            //Anmeldedaten an Server schicken
            String str = "";
            if (create) {
                str += "r:";
            }
            str += username + ":" + password;
            c.write(str);
            String input = c.scanner.next();
            if (input.equals("loggedin")) {
                //Der Server bestätigt Anmeldung
                c = new ServerConnection(c.socket, c.scanner, c.writer);
                Main.MENU.loggedIn(c);//neues Fenster öffnen
            } else if (create) {
                Main.MENU.loginError(IMenu.LOGINERROR.ACCOUNTEXISTS);
            } else if (input.equals(ClientMessages.PasswordWrong.getValue())) {
                Main.MENU.loginError(IMenu.LOGINERROR.WRONGPASSWORD);
            } else if (input.equals(ClientMessages.UsernameWrong.getValue())) {
                Main.MENU.loginError(IMenu.LOGINERROR.ACCOUNTNOTEXISTS);
            } else if (input.equals(ClientMessages.AlreadyOnline.getValue())) {
                Main.MENU.loginError(IMenu.LOGINERROR.ALREADYLOGGEDIN);
            } else {
                Main.MENU.loginError(IMenu.LOGINERROR.UNKNONWNERROR);
            }
        }
    }

    private ServerConnectionThread connectionThread;

    private ClientGame game;
    private String name;
    private Scanner scanner;
    private Socket socket;
    private PrintWriter writer;

    /**
     * Create a new ServerConnection with the given params
     *
     * @param so the socket of the connection
     * @param sca the scanner
     * @param pwr the writer
     */
    private ServerConnection(Socket so, Scanner sca, PrintWriter pwr) {
        socket = so;
        scanner = sca;
        writer = pwr;
        write("n");
        connectionThread = new ServerConnectionThread(this, sca);
        Thread t = new Thread(connectionThread);
        t.start();
    }

    /**
     * Connect to the official, central server at the given address
     *
     * @param address the address to connect to
     */
    private ServerConnection(InetAddress address) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(getClass().getResourceAsStream("/trustStore.ks"), "assault".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            SSLContext sslcon = SSLContext.getInstance("TLS");

            TrustManager[] trustManagers = tmf.getTrustManagers();
            sslcon.init(null, trustManagers, null);

            SSLSocketFactory ssf = sslcon.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(address, 1024);

            socket.startHandshake();
            this.socket = socket;
            scanner = new Scanner(this.socket.getInputStream());
            writer = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (ConnectException ex) {
            Main.MENU.connectionError();
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | KeyManagementException ex) {
            Main.MENU.showErrorMessage("Unexpected Error while connecting to the Server!", false);
            //System.out.println(Arrays.toString(ex.getStackTrace()));
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Return the nam of the client
     *
     * @return the name of the client
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the client
     *
     * @param string the name of the client
     */
    protected void setName(String string) {
        name = string;
    }

    /**
     * Write a message to the server
     *
     * @param message the type of the message
     * @param replace the things to replace placeholders with
     */
    public void write(ServerMessages message, Object[] replace) {
        write(MessageFormat.format(message.getValue(), replace));
    }

    /**
     * Start a new ClientGame
     *
     * @param color the color you play (0 = white, 1 = black)
     */
    protected void startGame(String color) {
        game = new ClientGame(this, !color.equals("0"));
        connectionThread.setGame(game);
    }

    /**
     * Writes a message to the server
     *
     * @param str the message to send
     */
    private void write(String str) {
        writer.println(str);
    }

}
