package eu.assault2142.hololol.chess.server.util.loginqueue;

import eu.assault2142.hololol.chess.server.user.User;

/**
 *
 * @author hololol2
 */
public class DataElement {

    private final User user;

    public DataElement(User u) {
        user = u;
    }
    
    public User getUser(){
        return user;
    }
}
