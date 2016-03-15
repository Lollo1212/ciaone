/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author hololol2
 */
public class Test {

    public static void main(String[] args) {
        String password = "test";
        String candidate = "test";
// gensalt's log_rounds parameter determines the complexity
// the work factor is 2**log_rounds, and the default is 10
        long time = System.currentTimeMillis();
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        time = System.currentTimeMillis() - time;
        System.out.println(time);
// Check that an unencrypted password matches one that has
// previously been hashed
        if (BCrypt.checkpw(candidate, hashed)) {
            System.out.println("It matches");
        } else {
            System.out.println("It does not match");
        }
    }
}
