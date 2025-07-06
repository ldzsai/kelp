package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.Operator;

public class BinaryOperation extends Expression {
    private final Expression left;
    private final Operator operator;
    private final Expression right;

    public BinaryOperation(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object evaluate(Environment env) throws Exception {
        double leftValue = ((Number) left.evaluate(env)).doubleValue();
        double rightValue = ((Number) right.evaluate(env)).doubleValue();
        return operator.apply(leftValue, rightValue);
    }
}
