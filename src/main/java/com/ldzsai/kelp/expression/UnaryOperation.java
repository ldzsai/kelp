package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.KelpException;
import com.ldzsai.kelp.api.ExpressionVisitor;

/**
 * 一元运算表达式。
 * 支持取负（-）、逻辑非（!）和按位取反（~）。
 */
public class UnaryOperation extends Expression {

    public enum UnaryOperatorType {
        NEGATION,
        LOGICAL_NOT,
        BITWISE_NOT
    }

    private final String operatorSymbol;
    private final Expression operand;

    public UnaryOperation(String operatorSymbol, Expression operand) {
        this.operatorSymbol = operatorSymbol;
        this.operand = operand;
    }

    @Override
    public Object evaluate(Environment env) throws KelpException {
        Object result = operand.evaluate(env);

        if (result == null) {
            throw new KelpException("Cannot apply unary operator to null");
        }

        if (result instanceof Boolean) {
            result = ((Boolean) result) ? 1.0 : 0.0;
        }

        if (!(result instanceof Number)) {
            throw new KelpException("Unary operator requires numeric or boolean operand, but got: " +
                    result.getClass().getSimpleName());
        }

        double value = ((Number) result).doubleValue();

        switch (operatorSymbol) {
            case "-": return -value;
            case "!": return value == 0;
            case "~": return (double) (~(int) value);
            default: throw new KelpException("Unknown unary operator: " + operatorSymbol);
        }
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitUnary(this);
    }

    @Override
    public String describe() {
        return operatorSymbol + operand.describe();
    }

    public String getOperatorSymbol() { return operatorSymbol; }
    public Expression getOperand() { return operand; }
}
