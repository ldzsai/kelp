package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.KelpException;
import com.ldzsai.kelp.Operator;

/**
 * 一元运算符表达式
 * 支持负号(-)、逻辑非(!)、按位取反(~)
 */
public class UnaryOperation extends Expression {

    /**
     * 一元运算符类型
     */
    public enum UnaryOperatorType {
        NEGATION, // 负号 -
        LOGICAL_NOT, // 逻辑非 !
        BITWISE_NOT // 按位取反 ~
    }

    private final String operatorSymbol;
    private final Expression operand;

    public UnaryOperation(String operatorSymbol, Expression operand) {
        this.operatorSymbol = operatorSymbol;
        this.operand = operand;
    }

    @Override
    public Object evaluate(Environment env) throws Exception {
        Object result = operand.evaluate(env);

        if (result == null) {
            throw new KelpException("Cannot apply unary operator to null");
        }

        // 如果操作数是布尔值，转换为数值
        if (result instanceof Boolean) {
            result = ((Boolean) result) ? 1.0 : 0.0;
        }

        if (!(result instanceof Number)) {
            throw new KelpException("Unary operator requires numeric or boolean operand, but got: " +
                    result.getClass().getSimpleName());
        }

        double value = ((Number) result).doubleValue();

        switch (operatorSymbol) {
            case "-":
                return -value;
            case "!":
                return value == 0;
            case "~":
                return ~(int) value;
            default:
                throw new KelpException("Unknown unary operator: " + operatorSymbol);
        }
    }

    public String getOperatorSymbol() {
        return operatorSymbol;
    }

    public Expression getOperand() {
        return operand;
    }
}
