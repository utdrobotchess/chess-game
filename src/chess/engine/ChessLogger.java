/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

import java.io.*;

public class ChessLogger {
    public static final Logger logger = Logger.getLogger(ChessLogger.class.getName());
    private static ChessLogger instance = null;

    private static final int FILE_SIZE_LIMIT = 1024 * 1024;
    private static final int FILE_COUNT = 10;
    private static final boolean APPEND = false;

    /* Enforces singleton property of the logger */
    public static ChessLogger getInstance() {
        if (instance == null) {
            prepareLogger();
            instance = new ChessLogger();
        }

        return instance;
    }

    private static void prepareLogger() {
        FileHandler handler;

        try {
            handler = new FileHandler("log/chessgame_log.%u.%g.txt", FILE_SIZE_LIMIT,
                                      FILE_COUNT, APPEND);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.FINEST);
        } catch (IOException ex) {
            System.err.println("Error in creating log file");
        }
    }
}
