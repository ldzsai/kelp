package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.api.ExpressionVisitor;

/**
 * 整数字面量表达式。
 */
public class IntegerLiteral extends Expression {
    private final int value;

    public IntegerLiteral(int value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitIntegerLiteral(this);
    }

    @Override
    public String describe() {
        return String.valueOf(value);
    }

    public int getValue() { return value; }
}
