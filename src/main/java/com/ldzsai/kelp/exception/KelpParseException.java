package com.ldzsai.kelp.exception;

/**
 * 语法分析过程中抛出的异常。
 */
public class KelpParseException extends KelpException {
    public KelpParseException(String message) {
        super(Stage.PARSING, message, -1, null, null);
    }

    public KelpParseException(String message, int position, String expression) {
        super(Stage.PARSING, message, position, expression, null);
    }

    public KelpParseException(String message, int position, String expression, Throwable cause) {
        super(Stage.PARSING, message, position, expression, cause);
    }
}
