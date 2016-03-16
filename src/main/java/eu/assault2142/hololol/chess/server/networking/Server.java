package eu.assault2142.hololol.chess.server.networking;

import eu.assault2142.hololol.chess.game.ServerGame;
import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.server.exceptions.UnknownUserException;
import eu.assault2142.hololol.chess.server.exceptions.UsernameNotFreeException;
import eu.assault2142.hololol.chess.server.user.User;
import eu.assault2142.hololol.chess.server.util.Log;
import eu.assault2142.hololol.chess.server.util.Store;
import eu.assault2142.hololol.chess.server.util.loginqueue.LoginQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.util.Pair;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * The central Server who manages all online clients, games and
 * database-connections
 *
 * @author hololol2
 */
public class Server {

    public static Server SERVER;
    private ServerSocket serverSocket;
    private final HashMap<Integer, ClientConnection> loggedinaccounts = new HashMap();
    private LoginQueue loginqueue;
    private Store store;
    private final HashMap<Integer, Integer> gamechallenges = new HashMap();
    private final ArrayList<Pair<Integer, Integer>> challenges = new ArrayList();

    public boolean checkPassword(int id, String password) throws UnknownUserException {
        return store.checkPassword(id, password);
    }

    /**
     * Initializes the Server
     */
    public void init() {
        try {
            store = new Store();
            Server.SERVER = this;
            File file = new File("/root/schach_logs/data.tmp");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
            }
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream("keyStore.pkcs12"), "assault".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, "assault".toCharArray());

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), null, null);

            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(1024);
            serverSocket = s;
            Log.MAINLOG.log("Starting server");
            loginqueue = new LoginQueue();
            Log.MAINLOG.log("LoginQueue initialized");
            new WaitForPlayers(this).start();
            Log.MAINLOG.log("Ready");
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException | CertificateException | IOException ex) {
            Log.MAINLOG.log(ex.getMessage());
        }
    }

    /**
     * Start a new game with the given clients
     *
     * @param u1 the first player
     * @param u2 the second player
     */
    public void startGame(User u1, User u2) {
        ClientConnection conn1 = getConnection(u1.getID());
        ClientConnection conn2 = getConnection(u2.getID());
        Log.MAINLOG.log("Game started");
        conn1.write(ClientMessages.Gamestart, new Object[]{1});
        conn2.write(ClientMessages.Gamestart, new Object[]{0});
        conn1.setWhite(false);
        conn2.setWhite(true);
        ServerGame g = new ServerGame(conn1, conn2);
        conn1.setGame(g);
        conn2.setGame(g);

    }

    /**
     * Login the user and send all pending messages and friend-requests
     *
     * @param uid the user's id
     * @param connection the connection to the client
     * @throws
     * eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void loginUser(int uid, ClientConnection connection) throws UnknownUserException {
        loggedinaccounts.put(uid, connection);
        List<String> msgs = store.getMessages(uid);
        msgs.stream().forEach((msg) -> {
            connection.write(ClientMessages.Message, new Object[]{msg});
            try {
                store.deleteMessage(uid, msg);
            } catch (UnknownUserException ex) {

            }
        });
        List<Integer> requests = store.getRequests(uid);
        requests.stream().forEach((requid) -> {
            try {
                connection.write(ClientMessages.Request, new Object[]{store.getName(requid)});
            } catch (UnknownUserException ex) {

            }
        });

    }

    /**
     * Logout the user
     *
     * @param uid the user's id
     */
    public void logoutUser(int uid) {
        loggedinaccounts.remove(uid);
    }

    /**
     * Checks if the user is online
     *
     * @param uid the user's id
     * @return true if the user is online, false otherwise
     */
    public boolean isOnline(int uid) {
        return loggedinaccounts.containsKey(uid);
    }

    /**
     *
     * @return the server-socket of this instance
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     *
     * @return the login-queue of this instance
     */
    public LoginQueue getLoginQueue() {
        return loginqueue;
    }

    /**
     * Create a new User
     *
     * @param name the username
     * @param pass the password
     * @throws
     * eu.assault2142.hololol.chess.server.exceptions.UsernameNotFreeException
     */
    public void createNewUser(String name, String pass) throws UsernameNotFreeException {
        int id = store.createNewUser(name, pass);
    }

    /**
     * Retrieve the given user
     *
     * @param name the user's name
     * @return the user
     * @throws
     * eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public User getUser(String name) throws UnknownUserException {
        return store.getUser(store.getID(name));
    }

    /**
     * Get the connection of a user
     *
     * @param uid the user's id
     * @return the connection or null if he is offline
     */
    public ClientConnection getConnection(int uid) {
        return loggedinaccounts.get(uid);
    }

    /**
     * Retrieve the user's friends
     *
     * @param uid the user's id
     * @return a list of his friends' ids
     * @throws
     * eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public String[] getFriends(int uid) throws UnknownUserException {
        List<Integer> friends = store.getFriends(uid);
        String[] names = new String[friends.size()];
        for (int i = 0; i < friends.size(); i++) {
            names[i] = store.getName(friends.get(i));
        }
        return names;
    }

    /**
     * Retrieve the user-id
     *
     * @param name the user's name
     * @return the id of the user
     * @throws
     * eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public int getUserID(String name) throws UnknownUserException {
        return store.getID(name);
    }

    /**
     * Cancel the friendship
     *
     * @param remover the id of the remover
     * @param removed the id of the removed
     * @throws
     * eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void removeFriend(int remover, int removed) throws UnknownUserException {
        store.removeFriend(remover, removed);
        if (isOnline(remover)) {
            loggedinaccounts.get(remover).writeFriendList();
        }
        if (isOnline(removed)) {
            loggedinaccounts.get(removed).writeFriendList();
        }
    }

    /**
     * Create a new friend-request and notify the target
     *
     * @param uid the id of the requester
     * @param targetid the id of the target
     * @throws
     * eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void addFriendRequest(int uid, int targetid) throws UnknownUserException {
        store.addFriendRequest(uid, targetid);
        if (isOnline(targetid)) {
            loggedinaccounts.get(targetid).write(ClientMessages.Request, new Object[]{store.getName(uid)});
        }
    }

    /**
     * Decline a friend-request
     *
     * @param uid the id of the decline
     * @param declinedid the id of the original requester
     * @throws
     * eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void declineRequest(int uid, int declinedid) throws UnknownUserException {
        store.removeRequest(declinedid, uid);
        sendMessage(declinedid, "Info:" + store.getName(uid) + "_declined_your_Friendrequest");
    }

    /**
     * Accept a friend-request
     *
     * @param uid the id of the acceptor
     * @param acceptedid the id of the original requester
     * @throws
     * eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void acceptRequest(int uid, int acceptedid) throws UnknownUserException {
        if (store.requestExists(acceptedid, uid)) {
            store.removeRequest(acceptedid, uid);
            store.addFriend(uid, acceptedid);
            if (isOnline(uid)) {
                loggedinaccounts.get(uid).writeFriendList();
            }
            if (isOnline(acceptedid)) {
                loggedinaccounts.get(acceptedid).writeFriendList();
            }
        }
    }

    /**
     * Send a message to the given user
     *
     * @param recipient the recipient's id
     * @param msg the message
     * @throws
     * eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void sendMessage(int recipient, String msg) throws UnknownUserException {
        store.addMessage(recipient, msg);
        if (isOnline(recipient)) {
            loggedinaccounts.get(recipient).write(ClientMessages.Message, new Object[]{msg});
            store.deleteMessage(recipient, msg);
        }
    }

    /**
     * Retrieve the friends of the user
     *
     * @param uid the user's id
     * @return the friends as a string separated with semicolons
     * @throws
     * eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public String getFriendsAsString(int uid) throws UnknownUserException {
        String[] string = getFriends(uid);
        String s = "";
        for (String string1 : string) {
            s += string1 + ";";
        }
        return s;
    }

    public void challenge(int challenger, int challenged) {
        gamechallenges.put(challenger, challenged);
    }

    /**
     * Check if a challenge for the given user exists
     *
     * @param uid the ID of the challenged user
     * @param challengerid the ID of the challenging user
     * @return true if the user was challenged
     */
    public boolean challengeExists(int uid, int challengerid) {
        return gamechallenges.get(challengerid) == uid;
    }

    /**
     * Retrieve the username for the given user-ID
     *
     * @param uid the User-ID
     * @return the name of the user
     */
    private String getName(int uid) throws UnknownUserException {
        return store.getName(uid);
    }

    public void setPassword(int uid, String password) throws UnknownUserException {
        store.setPassword(uid, password);
    }

    public void setUsername(int uid, String username) throws UnknownUserException, UsernameNotFreeException {
        store.setUsername(uid, username);
    }
}
