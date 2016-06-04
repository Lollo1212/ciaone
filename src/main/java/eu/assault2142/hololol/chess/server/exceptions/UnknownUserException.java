/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.server.exceptions;

/**
 * Signals that the User is unknown. Either it never existed or was deleted
 *
 * @author hololol2
 */
public class UnknownUserException extends Exception {

    public UnknownUserException(String name) {
        super("The User with the Username " + name + " is unknown");
    }

    public UnknownUserException(int id) {
        super("The User with the ID " + id + " is unknown");
    }
}
