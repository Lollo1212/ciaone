package eu.assault2142.hololol.chess.server.util.loginqueue;

import eu.assault2142.hololol.chess.networking.ClientMessages;
import eu.assault2142.hololol.chess.server.exceptions.UnknownUserException;
import eu.assault2142.hololol.chess.server.networking.Server;
import eu.assault2142.hololol.chess.server.user.LoginRequest;
import eu.assault2142.hololol.chess.server.user.User;
import eu.assault2142.hololol.chess.server.util.Log;

/**
 * Processes pending LoginRequests
 *
 * @author hololol2
 */
public class LoginThread extends Thread {

    private final LoginQueue loginqueue;

    /**
     * Create a new LoginThread
     *
     * @param queue the LoginQueue to process
     */
    public LoginThread(LoginQueue queue) {
        loginqueue = queue;
    }

    @Override
    public void run() {
        while (true) {

            if (!loginqueue.isEmpty()) {
                LoginRequest request = loginqueue.pop();
                try {
                    User u2 = Server.SERVER.getUser(request.getUsername());
                    if (u2 == null) {
                        Log.MAINLOG.log(request.getUsername() + " couldn't log in.");
                        request.getConnection().write(ClientMessages.UsernameWrong, new Object[]{});
                        continue;
                    }
                    if (u2.getPassword().equals(request.getPassword())) {
                        //Passwords match
                        Log.MAINLOG.log(request.getUsername() + " logged in successful");
                        request.getConnection().write(ClientMessages.LoggedIn, new Object[]{});
                        Server.SERVER.loginUser(u2.getID(), request.getConnection());
                        request.getConnection().startReading();
                        request.getConnection().setUser(u2);
                        String[] str = Server.SERVER.getFriends(u2.getID());
                        request.getConnection().writeFriendList();
                        request.getConnection().write(ClientMessages.Name, new Object[]{request.getConnection().getUser().getUsername()});
                    } else {
                        //Passwords don't match
                        Log.MAINLOG.log(u2.getUsername() + " couldn't log in.");
                        request.getConnection().write(ClientMessages.PasswordWrong, new Object[]{});
                    }
                } catch (UnknownUserException ex) {
                    request.getConnection().write(ClientMessages.UsernameWrong, new Object[]{});
                    Log.MAINLOG.log(ex.getMessage());
                }

            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Log.MAINLOG.log(ex.getMessage());
                }
            }
        }
    }
}
