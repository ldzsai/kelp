package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.KelpException;
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
        Object leftResult = left.evaluate(env);
        Object rightResult = right.evaluate(env);

        // 类型检查和转换
        if (!(leftResult instanceof Number)) {
            throw new KelpException("Left operand must be a number, but got: " + leftResult.getClass().getSimpleName());
        }
        
        if (!(rightResult instanceof Number)) {
            throw new KelpException("Right operand must be a number, but got: " + rightResult.getClass().getSimpleName());
        }

        double leftValue = ((Number) leftResult).doubleValue();
        double rightValue = ((Number) rightResult).doubleValue();
        
        // 检查除零错误
        if (operator == Operator.DIVIDE && rightValue == 0) {
            throw new KelpException("Division by zero");
        }
        
        return operator.apply(leftValue, rightValue);
    }
}