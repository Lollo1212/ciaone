package eu.assault2142.hololol.chess.server.util.loginqueue;

import eu.assault2142.hololol.chess.server.user.LoginRequest;

/**
 * A ListElement which stores a pending LoginRequest
 *
 * @author hololol2
 */
public class Node {

    private Node next;
    private final LoginRequest data;

    /**
     * Create a new Node
     *
     * @param data the LoginRequest to store
     */
    public Node(LoginRequest data) {
        this.data = data;
    }

    /**
     * Add a new LoginRequest to the end of the list
     *
     * @param data the LoginRequest
     */
    public void addLast(LoginRequest data) {
        if (next == null) {
            next = new Node(data);
        } else {
            next.addLast(data);
        }
    }

    /**
     * Returns the LoginRequest of this element
     *
     * @return the LoginRequest
     */
    public LoginRequest getData() {
        return data;
    }

    /**
     * Returns the next Node
     *
     * @return the successor in the list
     */
    public Node getNext() {
        return next;
    }
}
