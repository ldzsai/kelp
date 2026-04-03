package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.api.ExpressionVisitor;

/**
 * 浮点数（双精度）字面量表达式。
 */
public class FloatLiteral extends Expression {
    private final double value;

    public FloatLiteral(double value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitFloatLiteral(this);
    }

    @Override
    public String describe() {
        return String.valueOf(value);
    }

    public double getValue() { return value; }
}
