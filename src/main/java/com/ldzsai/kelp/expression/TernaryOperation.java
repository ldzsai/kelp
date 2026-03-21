package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.KelpException;

/**
 * 三元条件运算符表达式
 * 语法: condition ? trueValue : falseValue
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
    public Object evaluate(Environment env) throws Exception {
        Object condResult = condition.evaluate(env);

        // 将条件转换为布尔值
        boolean conditionValue = toBoolean(condResult);

        return conditionValue ? trueValue.evaluate(env) : falseValue.evaluate(env);
    }

    /**
     * 将对象转换为布尔值
     */
    private boolean toBoolean(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue() != 0;
        }
        if (value instanceof String) {
            String str = (String) value;
            // 空字符串为false，非空为true
            return !str.isEmpty();
        }
        // 其他对象默认为true
        return true;
    }

    public Expression getCondition() {
        return condition;
    }

    public Expression getTrueValue() {
        return trueValue;
    }

    public Expression getFalseValue() {
        return falseValue;
    }
}
