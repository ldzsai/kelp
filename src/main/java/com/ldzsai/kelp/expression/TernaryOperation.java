package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.KelpException;
import com.ldzsai.kelp.api.ExpressionVisitor;

/**
 * 三元条件表达式。
 * 语法：condition ? trueValue : falseValue
 */
public class TernaryOperation extends Expression {
    private final Expression condition;
    private final Expression trueValue;
    private final Expression falseValue;

    public TernaryOperation(Expression condition, Expression trueValue, Expression falseValue) {
        this.condition = condition;
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }

    @Override
    public Object evaluate(Environment env) throws KelpException {
        Object condResult = condition.evaluate(env);
        boolean conditionValue = toBoolean(condResult);
        return conditionValue ? trueValue.evaluate(env) : falseValue.evaluate(env);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitTernary(this);
    }

    @Override
    public String describe() {
        return "(" + condition.describe() + " ? " + trueValue.describe() + " : " + falseValue.describe() + ")";
    }

    private boolean toBoolean(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof Number) return ((Number) value).doubleValue() != 0;
        if (value instanceof String) return !((String) value).isEmpty();
        return true;
    }

    public Expression getCondition() { return condition; }
    public Expression getTrueValue() { return trueValue; }
    public Expression getFalseValue() { return falseValue; }
}
