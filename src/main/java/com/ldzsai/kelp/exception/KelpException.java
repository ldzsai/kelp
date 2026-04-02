package com.ldzsai.kelp.exception;

import com.ldzsai.kelp.api.PipelineContext;

/**
 * 所有 Kelp 异常的基类。
 * 携带阶段、位置和表达式上下文以实现精确的错误汇报。
 */
public class KelpException extends RuntimeException {
    private final Stage stage;
    private final int position;
    private final String expression;

    public enum Stage { LEXING, PARSING, EVALUATING, FUNCTION_CALL, TYPE_COERCION, PIPELINE }

    public KelpException(String message) {
        this(Stage.PIPELINE, message, -1, null, null);
    }

    public KelpException(String message, Throwable cause) {
        this(Stage.PIPELINE, message, -1, null, cause);
    }

    public KelpException(Stage stage, String message, int position, String expression, Throwable cause) {
        super(formatMessage(stage, message, position, expression), cause);
        this.stage = stage;
        this.position = position;
        this.expression = expression;
    }

    private static String formatMessage(Stage stage, String message, int position, String expression) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(stage).append("] ");
        sb.append(message);
        if (position >= 0) {
            sb.append(" at position ").append(position);
        }
        if (expression != null && !expression.isEmpty()) {
            String truncated = expression.length() > 100
                ? expression.substring(0, 100) + "..."
                : expression;
            sb.append(" in expression: ").append(truncated);
        }
        return sb.toString();
    }

    public Stage getStage() {
        return stage;
    }

    public int getPosition() {
        return position;
    }

    public String getExpression() {
        return expression;
    }
}
