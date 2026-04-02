package com.ldzsai.kelp.exception;

/**
 * 词法分析（标记化）过程中抛出的异常。
 */
public class KelpLexException extends KelpException {
    public KelpLexException(String message) {
        super(Stage.LEXING, message, -1, null, null);
    }

    public KelpLexException(String message, int position, String expression) {
        super(Stage.LEXING, message, position, expression, null);
    }

    public KelpLexException(String message, int position, String expression, Throwable cause) {
        super(Stage.LEXING, message, position, expression, cause);
    }
}
