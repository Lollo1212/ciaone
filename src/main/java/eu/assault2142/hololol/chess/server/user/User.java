package eu.assault2142.hololol.chess.server.user;

import java.util.List;

/**
 * Stores all data for a user-account
 *
 * @author hololol2
 */
public class User {

    private final List<Integer> friends;
    private final int id;
    private final String username;

    /**
     * Create a new User
     *
     * @param name the username
     * @param friends a list of friends
     * @param id an id
     */
    public User(String name, List<Integer> friends, int id) {
        username = name;
        this.friends = friends;
        this.id = id;
    }

    /**
     * Retrieve the friends
     *
     * @return the friends as list
     */
    public List<Integer> getFriendsAsList() {
        return friends;
    }

    /**
     * Retrieve the ID
     *
     * @return the user-ID
     */
    public int getID() {
        return id;
    }

    /**
     * Retrieve the username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
