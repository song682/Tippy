package decok.dfcdvadstf.tips.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * - What is it?
 * - A logger that MONITOR our tips going on.
 */


public class TippyLogger {
    public static final Logger LOGGER = LogManager.getLogger("Tippy");

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warn(String message) {
        LOGGER.warn(message);
    }

    public static void error(String message) {
        LOGGER.error(message);
    }
}