package com.ldzsai.kelp.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 基于 java.util.logging 的日志记录器实现。
 */
public class DefaultKelpLogger implements KelpLogger {
    private final Logger logger;

    public DefaultKelpLogger(String name) {
        this.logger = Logger.getLogger(name);
    }

    public DefaultKelpLogger() {
        this("com.ldzsai.kelp");
    }

    @Override
    public void debug(String message, Object... args) {
        logger.log(Level.FINE, format(message, args));
    }

    @Override
    public void info(String message, Object... args) {
        logger.log(Level.INFO, format(message, args));
    }

    @Override
    public void warn(String message, Object... args) {
        logger.log(Level.WARNING, format(message, args));
    }

    @Override
    public void error(String message, Throwable cause, Object... args) {
        logger.log(Level.SEVERE, format(message, args), cause);
    }

    @Override
    public boolean isLevelEnabled(LogLevel level) {
        switch (level) {
            case DEBUG: return logger.isLoggable(Level.FINE);
            case INFO: return logger.isLoggable(Level.INFO);
            case WARN: return logger.isLoggable(Level.WARNING);
            case ERROR: return logger.isLoggable(Level.SEVERE);
            default: return false;
        }
    }

    private String format(String message, Object... args) {
        if (args == null || args.length == 0) return message;
        return String.format(message.replace("{}", "%s"), args);
    }
}
