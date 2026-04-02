package com.ldzsai.kelp.exception;

/**
 * 表达式求值过程中抛出的异常。
 */
public class KelpEvaluationException extends KelpException {
    public KelpEvaluationException(String message) {
        super(Stage.EVALUATING, message, -1, null, null);
    }

    public KelpEvaluationException(String message, Throwable cause) {
        super(Stage.EVALUATING, message, -1, null, cause);
    }

    public KelpEvaluationException(Stage stage, String message, int position, String expression, Throwable cause) {
        super(stage, message, position, expression, cause);
    }
}
