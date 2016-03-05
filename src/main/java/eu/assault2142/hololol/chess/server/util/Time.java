package eu.assault2142.hololol.chess.server.util;

import java.util.GregorianCalendar;

/**
 * Provides simple Timestamp-Formatting
 *
 * @author hololol2
 */
public class Time {

    /**
     * Get a formated timestamp
     *
     * @return the current time as YYYY-MM-DD_HH:MM:SS
     */
    public static String getTime() {
        GregorianCalendar gc = new GregorianCalendar();
        int m = gc.get(GregorianCalendar.MONTH);
        m++;
        return gc.get(GregorianCalendar.YEAR) + "-" + m + "-" + gc.get(GregorianCalendar.DAY_OF_MONTH) + "_" + gc.get(GregorianCalendar.HOUR_OF_DAY) + ":" + gc.get(GregorianCalendar.MINUTE) + ":" + gc.get(GregorianCalendar.SECOND);
    }
}
