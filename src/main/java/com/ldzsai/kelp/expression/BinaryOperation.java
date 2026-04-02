package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.KelpException;
import com.ldzsai.kelp.Operator;
import com.ldzsai.kelp.api.ExpressionVisitor;

/**
 * 二元运算表达式。
 * 支持算术、比较、逻辑和位运算符。
 * 实现了 && 和 || 的短路求值逻辑。
 * 支持正确的 null 值比较语义。
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
    public Object evaluate(Environment env) throws KelpException {
        // 逻辑运算符的短路求值处理
        if (operator.isLogical()) {
            return evaluateLogicalOperation(env);
        }

        Object leftResult = left.evaluate(env);
        Object rightResult = right.evaluate(env);

        if (operator.isArithmetic() || operator.isBitwise()) {
            return evaluateNumericOperation(leftResult, rightResult);
        }

        if (operator.isComparison()) {
            return evaluateComparison(leftResult, rightResult);
        }

        throw new KelpException("Unknown operator type: " + operator);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitBinary(this);
    }

    @Override
    public String describe() {
        return "(" + left.describe() + " " + operator.getSymbol() + " " + right.describe() + ")";
    }

    /**
     * 逻辑运算符的短路求值。
     */
    private Object evaluateLogicalOperation(Environment env) {
        Object leftResult = left.evaluate(env);
        boolean leftBool = toBoolean(leftResult);

        switch (operator) {
            case LOGICAL_AND:
                if (!leftBool) return false;
                return toBoolean(right.evaluate(env));
            case LOGICAL_OR:
                if (leftBool) return true;
                return toBoolean(right.evaluate(env));
            default:
                throw new KelpException("Unknown logical operator: " + operator);
        }
    }

    private Object evaluateNumericOperation(Object leftResult, Object rightResult) {
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

        if (operator == Operator.DIVIDE && rightValue == 0) {
            throw new KelpException("Division by zero");
        }
        if (operator == Operator.INTEGER_DIVIDE && rightValue == 0) {
            throw new KelpException("Integer division by zero");
        }
        if (operator == Operator.MODULO && rightValue == 0) {
            throw new KelpException("Modulo by zero");
        }

        // 位运算使用整数截断
        if (operator.isBitwise()) {
            return evaluateBitwiseOperation((int) leftValue, (int) rightValue);
        }

        return operator.apply(leftValue, rightValue);
    }

    private Object evaluateBitwiseOperation(int leftValue, int rightValue) {
        switch (operator) {
            case BIT_AND: return (double) (leftValue & rightValue);
            case BIT_OR: return (double) (leftValue | rightValue);
            case BIT_XOR: return (double) (leftValue ^ rightValue);
            case LEFT_SHIFT: return (double) (leftValue << rightValue);
            case RIGHT_SHIFT: return (double) (leftValue >> rightValue);
            case UNSIGNED_RIGHT_SHIFT: return (double) (leftValue >>> rightValue);
            default: throw new KelpException("Unknown bitwise operator: " + operator);
        }
    }

    private Object evaluateComparison(Object leftResult, Object rightResult) {
        if (leftResult instanceof Number && rightResult instanceof Number) {
            double lv = ((Number) leftResult).doubleValue();
            double rv = ((Number) rightResult).doubleValue();
            switch (operator) {
                case EQUALS: return lv == rv;
                case NOT_EQUALS: return lv != rv;
                case GREATER_THAN: return lv > rv;
                case LESS_THAN: return lv < rv;
                case GREATER_OR_EQUAL: return lv >= rv;
                case LESS_OR_EQUAL: return lv <= rv;
            }
        }

        if (leftResult instanceof String && rightResult instanceof String) {
            String ls = (String) leftResult;
            String rs = (String) rightResult;
            switch (operator) {
                case EQUALS: return ls.equals(rs);
                case NOT_EQUALS: return !ls.equals(rs);
                default: throw new KelpException("String comparison only supports == and != operators");
            }
        }

        if (leftResult instanceof Boolean && rightResult instanceof Boolean) {
            boolean lb = (Boolean) leftResult;
            boolean rb = (Boolean) rightResult;
            switch (operator) {
                case EQUALS: return lb == rb;
                case NOT_EQUALS: return lb != rb;
                default: throw new KelpException("Boolean comparison only supports == and != operators");
            }
        }

        // null 值比较语义：null == null 为 true，null != null 为 false
        switch (operator) {
            case EQUALS:
                if (leftResult == null && rightResult == null) return true;
                if (leftResult == null || rightResult == null) return false;
                return leftResult.equals(rightResult);
            case NOT_EQUALS:
                if (leftResult == null && rightResult == null) return false;
                if (leftResult == null || rightResult == null) return true;
                return !leftResult.equals(rightResult);
            default:
                throw new KelpException("Comparison not supported for types: " +
                        (leftResult != null ? leftResult.getClass().getSimpleName() : "null") + " and " +
                        (rightResult != null ? rightResult.getClass().getSimpleName() : "null"));
        }
    }

    private boolean toBoolean(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof Number) return ((Number) value).doubleValue() != 0;
        if (value instanceof String) return !((String) value).isEmpty();
        return true;
    }

    public Expression getLeft() { return left; }
    public Operator getOperator() { return operator; }
    public Expression getRight() { return right; }
}
