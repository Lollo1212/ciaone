/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.server.exceptions;

/**
 * Signals that the given Username is already taken
 *
 * @author hololol2
 */
public class UsernameNotFreeException extends Exception {

    public UsernameNotFreeException(String name) {
        super("The Username " + name + " is already taken");
    }
}
