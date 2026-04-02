package com.ldzsai.kelp.logging;

/**
 * 最小化日志抽象接口——零外部依赖。
 */
public interface KelpLogger {
    void debug(String message, Object... args);
    void info(String message, Object... args);
    void warn(String message, Object... args);
    void error(String message, Throwable cause, Object... args);
    boolean isLevelEnabled(LogLevel level);

    enum LogLevel { DEBUG, INFO, WARN, ERROR, OFF }
}
