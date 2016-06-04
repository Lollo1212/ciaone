package eu.assault2142.hololol.chess.server.networking;

import eu.assault2142.hololol.chess.game.Settings;
import eu.assault2142.hololol.chess.server.util.Log;
import eu.assault2142.hololol.chess.server.util.Time;
import java.io.IOException;

/**
 *
 * @author hololol2
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Log log = new Log("main" + Time.getTime());
        Settings.init("default");
        new Server().init();
    }
}
