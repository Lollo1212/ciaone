/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.networking;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author hololol2
 */
public abstract class ConnectionThread extends Thread {

    protected Scanner scanner;

    public ConnectionThread(Scanner sc) {
        scanner = sc;
    }

    @Override
    public void run() {
        String input;
        while (true) {
            try {

                input = scanner.next();
                consume(input);
            } catch (NoSuchElementException | IllegalStateException nsee) {
                closeConnection();
                break;
            }
        }
    }

    protected String[] parse(String message, MessageFormat format) throws ParseException {
        return Arrays.stream(format.parse(message)).toArray(String[]::new);
    }

    protected void consumeUnknown(String[] parts) {
    }

    protected abstract void consume(String message);

    protected abstract void closeConnection();
}
