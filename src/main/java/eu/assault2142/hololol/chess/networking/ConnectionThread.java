/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.networking;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 *
 * @author hololol2
 */
public abstract class ConnectionThread extends Thread {

    protected final LinkedList<Consumer<String>> consumers;
    protected Scanner scanner;

    public ConnectionThread(Scanner sc) {
        scanner = sc;
        consumers = new LinkedList();
    }

    @Override
    public void run() {
        String input;
        while (true) {
            try {
                input = scanner.next();
                String text = input;
                consumers.forEach((Consumer<String> consumer) -> {
                    consumer.accept(text);
                });
            } catch (NoSuchElementException nsee) {
                closeConnection();
                break;
            }
        }
    }

    protected abstract void closeConnection();
}
