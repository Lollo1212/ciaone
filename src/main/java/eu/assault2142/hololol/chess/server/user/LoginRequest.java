/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.server.user;

import eu.assault2142.hololol.chess.server.networking.ClientConnection;

/**
 * Stores a login-request from a client
 *
 * @author hololol2
 */
public class LoginRequest {

    private final String username;
    private final String password;
    private final ClientConnection connection;

    /**
     * Create a new login-request
     *
     * @param name the username
     * @param pass the password
     * @param conn the client
     */
    public LoginRequest(String name, String pass, ClientConnection conn) {
        username = name;
        password = pass;
        connection = conn;
    }

    /**
     * Retrieve the Username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieve the Connection
     *
     * @return the connection to the client
     */
    public ClientConnection getConnection() {
        return connection;
    }

    /**
     * Retrieve the password
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}
