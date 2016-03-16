/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.networking;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 *
 * @author hololol2
 */
public abstract class ConnectionThread extends Thread {

    protected HashMap<ClientMessages, Consumer<String[]>> consumers;
    protected Scanner scanner;

    public ConnectionThread(Scanner sc) {
        scanner = sc;
        consumers = new HashMap();
    }

    @Override
    public void run() {
        String input;
        while (true) {
            try {
                input = scanner.next();
                consume(input);
            } catch (NoSuchElementException nsee) {
                closeConnection();
                break;
            }
        }
    }

    protected abstract void consume(String message);

    protected abstract void closeConnection();
}
