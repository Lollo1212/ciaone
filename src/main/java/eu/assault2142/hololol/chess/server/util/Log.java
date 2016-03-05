package eu.assault2142.hololol.chess.server.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Provides basic logging functionality
 * @author hololol2
 */
public class Log extends PrintWriter {

    public static Log MAINLOG;
    public static String PATH = "/root/schach_logs/";

    /**
     * Create a new Log-File
     * @param name the name of the file
     * @throws java.io.IOException if a problem with creating the file occurs
     */
    public Log(String name) throws IOException {
        super(new File(PATH + name + ".log"));
        new File(PATH + name + ".log").createNewFile();
        MAINLOG = this;
    }

    /**
     * Append a string to the end of the log. Adds a current timestamp
     * @param s the string to log
     */
    public void log(String s) {
        super.println(s + "@" + Time.getTime());
        super.flush();
    }
}
