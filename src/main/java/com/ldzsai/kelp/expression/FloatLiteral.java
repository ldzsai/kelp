package com.ldzsai.kelp.expression;

public class FloatLiteral extends Expression {
    private final double value;

    public FloatLiteral(double value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }
}
