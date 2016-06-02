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
     * @param create whether to create an account
     */
    public static void connect(String username, String password, boolean create) {
        ServerConnection connection;
        try {
            InetAddress address = InetAddress.getByName("assault2142.eu");
            connection = new ServerConnection(address);
        } catch (UnknownHostException ex) {
            Main.MENU.showErrorMessage("Couldn't find Server's IP!", false);
            return;
        }
        if (connection.scanner != null) {
            connection.scanner.next();
            String loginData = "";
            if (create) {
                loginData += "r:";
            }
            loginData += username + ":" + password;
            connection.write(loginData);
            String response = connection.scanner.next();
            if (response.equals("loggedin")) {
                connection = new ServerConnection(connection.socket, connection.scanner, connection.writer);
                Main.MENU.loggedIn(connection);
            } else if (create) {
                Main.MENU.loginError(IMenu.LOGINERROR.ACCOUNTEXISTS);
            } else if (response.equals(ClientMessages.PasswordWrong.getValue())) {
                Main.MENU.loginError(IMenu.LOGINERROR.WRONGPASSWORD);
            } else if (response.equals(ClientMessages.UsernameWrong.getValue())) {
                Main.MENU.loginError(IMenu.LOGINERROR.ACCOUNTNOTEXISTS);
            } else if (response.equals(ClientMessages.AlreadyOnline.getValue())) {
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
     * @param socket the socket of the connection
     * @param scanner the scanner
     * @param writer the writer
     */
    private ServerConnection(Socket socket, Scanner scanner, PrintWriter writer) {
        this.socket = socket;
        this.scanner = scanner;
        this.writer = writer;
        write("n");
        connectionThread = new ServerConnectionThread(this, scanner);
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
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(getClass().getResourceAsStream("/trustStore.ks"), "assault".toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            context.init(null, trustManagers, null);

            SSLSocketFactory socketFactory = context.getSocketFactory();
            SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(address, 1024);

            sslSocket.startHandshake();
            socket = sslSocket;
            scanner = new Scanner(this.socket.getInputStream());
            writer = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (ConnectException ex) {
            Main.MENU.connectionError();
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | KeyManagementException ex) {
            Main.MENU.showErrorMessage("Unexpected Error while connecting to the Server!", false);
        }
    }

    /**
     * Return the name of the client
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
     * @param replace the things to replace the placeholder with
     */
    public void write(ServerMessages message, Object[] replace) {
        write(MessageFormat.format(message.getValue(), replace));
    }

    /**
     * Writes a message to the server
     *
     * @param str the message to send
     */
    private void write(String str) {
        writer.println(str);
    }

    /**
     * Set the game
     *
     * @param game the current game
     */
    public void setGame(ClientGame game) {
        this.game = game;
    }
}
