package eu.assault2142.hololol.chess.server.util.loginqueue;

import eu.assault2142.hololol.chess.server.user.LoginRequest;

/**
 * Stores pending LoginRequests
 *
 * @author hololol2
 */
public class LoginQueue {

    private Node first;
    private final LoginThread loginthread;

    /**
     * Create a new LoginQueue and the corresponding LoginThread
     */
    public LoginQueue() {
        loginthread = new LoginThread(this);
        loginthread.start();
    }

    /**
     * Adds a new LoginRequest to the back
     *
     * @param data the LoginRequest
     */
    public void addLast(LoginRequest data) {
        if (first == null) {
            first = new Node(data);
        } else {
            first.addLast(data);
        }
    }

    /**
     * Check whether the list is empty
     *
     * @return true if there is no pending request, false otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Remove and retrieve the first LoginRequest
     *
     * @return the next LoginRequest
     */
    public LoginRequest pop() {
        if (first == null) {
            return null;
        }
        LoginRequest request = first.getData();
        first = first.getNext();
        return request;
    }
}
