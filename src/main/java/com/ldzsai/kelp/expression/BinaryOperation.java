package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.KelpException;
import com.ldzsai.kelp.Operator;

/**
 * 二元运算表达式
 * 支持算术运算符、比较运算符、逻辑运算符和位运算符
 */
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

        // 算术和位运算符需要数值类型
        if (operator.isArithmetic() || operator.isBitwise()) {
            return evaluateNumericOperation(leftResult, rightResult);
        }

        // 比较运算符
        if (operator.isComparison()) {
            return evaluateComparison(leftResult, rightResult);
        }

        // 逻辑运算符
        if (operator.isLogical()) {
            return evaluateLogicalOperation(leftResult, rightResult);
        }

        throw new KelpException("Unknown operator type: " + operator);
    }

    /**
     * 评估数值运算（算术和位运算）
     */
    private Object evaluateNumericOperation(Object leftResult, Object rightResult) {
        // 类型检查和转换
        if (!(leftResult instanceof Number)) {
            throw new KelpException("Left operand must be a number, but got: " +
                    (leftResult != null ? leftResult.getClass().getSimpleName() : "null"));
        }

        if (!(rightResult instanceof Number)) {
            throw new KelpException("Right operand must be a number, but got: " +
                    (rightResult != null ? rightResult.getClass().getSimpleName() : "null"));
        }

        double leftValue = ((Number) leftResult).doubleValue();
        double rightValue = ((Number) rightResult).doubleValue();

        // 检查除零错误
        if (operator == Operator.DIVIDE && rightValue == 0) {
            throw new KelpException("Division by zero");
        }

        // 整除需要检查除零
        if (operator == Operator.INTEGER_DIVIDE && rightValue == 0) {
            throw new KelpException("Integer division by zero");
        }

        // 模运算需要检查除零
        if (operator == Operator.MODULO && rightValue == 0) {
            throw new KelpException("Modulo by zero");
        }

        // 位运算单独处理
        if (operator.isBitwise()) {
            return evaluateBitwiseOperation((int) leftValue, (int) rightValue);
        }

        return operator.apply(leftValue, rightValue);
    }

    /**
     * 评估位运算
     */
    private Object evaluateBitwiseOperation(int leftValue, int rightValue) {
        switch (operator) {
            case BIT_AND:
                return (double) (leftValue & rightValue);
            case BIT_OR:
                return (double) (leftValue | rightValue);
            case BIT_XOR:
                return (double) (leftValue ^ rightValue);
            case LEFT_SHIFT:
                return (double) (leftValue << rightValue);
            case RIGHT_SHIFT:
                return (double) (leftValue >> rightValue);
            case UNSIGNED_RIGHT_SHIFT:
                return (double) (leftValue >>> rightValue);
            default:
                throw new KelpException("Unknown bitwise operator: " + operator);
        }
    }

    /**
     * 评估比较运算
     */
    private Object evaluateComparison(Object leftResult, Object rightResult) {
        // 如果是数值类型，进行数值比较
        if (leftResult instanceof Number && rightResult instanceof Number) {
            double leftValue = ((Number) leftResult).doubleValue();
            double rightValue = ((Number) rightResult).doubleValue();

            switch (operator) {
                case EQUALS:
                    return leftValue == rightValue;
                case NOT_EQUALS:
                    return leftValue != rightValue;
                case GREATER_THAN:
                    return leftValue > rightValue;
                case LESS_THAN:
                    return leftValue < rightValue;
                case GREATER_OR_EQUAL:
                    return leftValue >= rightValue;
                case LESS_OR_EQUAL:
                    return leftValue <= rightValue;
            }
        }

        // 字符串比较
        if (leftResult instanceof String && rightResult instanceof String) {
            String leftStr = (String) leftResult;
            String rightStr = (String) rightResult;

            switch (operator) {
                case EQUALS:
                    return leftStr.equals(rightStr);
                case NOT_EQUALS:
                    return !leftStr.equals(rightStr);
                default:
                    throw new KelpException("String comparison only supports == and != operators");
            }
        }

        // 布尔值比较
        if (leftResult instanceof Boolean && rightResult instanceof Boolean) {
            boolean leftBool = (Boolean) leftResult;
            boolean rightBool = (Boolean) rightResult;

            switch (operator) {
                case EQUALS:
                    return leftBool == rightBool;
                case NOT_EQUALS:
                    return leftBool != rightBool;
                default:
                    throw new KelpException("Boolean comparison only supports == and != operators");
            }
        }

        // 对象比较
        switch (operator) {
            case EQUALS:
                return leftResult != null && leftResult.equals(rightResult);
            case NOT_EQUALS:
                return leftResult == null || !leftResult.equals(rightResult);
            default:
                throw new KelpException("Comparison not supported for types: " +
                        (leftResult != null ? leftResult.getClass().getSimpleName() : "null") + " and " +
                        (rightResult != null ? rightResult.getClass().getSimpleName() : "null"));
        }
    }

    /**
     * 评估逻辑运算
     */
    private Object evaluateLogicalOperation(Object leftResult, Object rightResult) {
        // 将操作数转换为布尔值
        boolean leftBool = toBoolean(leftResult);
        boolean rightBool = toBoolean(rightResult);

        switch (operator) {
            case LOGICAL_AND:
                return leftBool && rightBool;
            case LOGICAL_OR:
                return leftBool || rightBool;
            default:
                throw new KelpException("Unknown logical operator: " + operator);
        }
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
            return !((String) value).isEmpty();
        }
        return true;
    }

    public Expression getLeft() {
        return left;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }
}
