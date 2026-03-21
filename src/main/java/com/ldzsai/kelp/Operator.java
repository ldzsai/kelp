package com.ldzsai.kelp;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 运算符枚举
 * 支持算术运算符、比较运算符、逻辑运算符和位运算符
 */
public enum Operator {
    // 算术运算符
    ADD("+", OperatorType.ARITHMETIC, (a, b) -> a + b),
    SUBTRACT("-", OperatorType.ARITHMETIC, (a, b) -> a - b),
    MULTIPLY("*", OperatorType.ARITHMETIC, (a, b) -> a * b),
    DIVIDE("/", OperatorType.ARITHMETIC, (a, b) -> a / b),
    MODULO("%", OperatorType.ARITHMETIC, (a, b) -> a % b),
    POWER("**", OperatorType.ARITHMETIC, (a, b) -> Math.pow(a, b)),
    INTEGER_DIVIDE("//", OperatorType.ARITHMETIC, (a, b) -> Double.valueOf((int) (a / b))),

    // 比较运算符
    EQUALS("==", OperatorType.COMPARISON, (a, b) -> a.equals(b)),
    NOT_EQUALS("!=", OperatorType.COMPARISON, (a, b) -> !a.equals(b)),
    GREATER_THAN(">", OperatorType.COMPARISON, (a, b) -> a > b),
    LESS_THAN("<", OperatorType.COMPARISON, (a, b) -> a < b),
    GREATER_OR_EQUAL(">=", OperatorType.COMPARISON, (a, b) -> a >= b),
    LESS_OR_EQUAL("<=", OperatorType.COMPARISON, (a, b) -> a <= b),

    // 逻辑运算符
    LOGICAL_AND("&&", OperatorType.LOGICAL, (a, b) -> (a != 0) && (b != 0)),
    LOGICAL_OR("||", OperatorType.LOGICAL, (a, b) -> (a != 0) || (b != 0)),

    // 位运算符
    BIT_AND("&", OperatorType.BITWISE, (a, b) -> a),
    BIT_OR("|", OperatorType.BITWISE, (a, b) -> a),
    BIT_XOR("^", OperatorType.BITWISE, (a, b) -> a),
    LEFT_SHIFT("<<", OperatorType.BITWISE, (a, b) -> a),
    RIGHT_SHIFT(">>", OperatorType.BITWISE, (a, b) -> a),
    UNSIGNED_RIGHT_SHIFT(">>>", OperatorType.BITWISE, (a, b) -> a);

    // 运算符类型
    public enum OperatorType {
        ARITHMETIC, // 算术运算符
        COMPARISON, // 比较运算符
        LOGICAL, // 逻辑运算符
        BITWISE // 位运算符
    }

    private final String symbol;
    private final OperatorType type;
    private final BiFunction<Double, Double, Object> function;

    Operator(String symbol, OperatorType type, BiFunction<Double, Double, Object> function) {
        this.symbol = symbol;
        this.type = type;
        this.function = function;
    }

    /**
     * 应用运算符
     * 
     * @param a 左边操作数
     * @param b 右边操作数
     * @return 运算结果
     */
    public Object apply(double a, double b) {
        return function.apply(a, b);
    }

    /**
     * 应用一元负运算符
     * 
     * @param a 操作数
     * @return 负值
     */
    public Object applyNegation(double a) {
        return -a;
    }

    /**
     * 应用一元逻辑非运算符
     * 
     * @param a 操作数
     * @return 逻辑非结果
     */
    public Object applyLogicalNot(double a) {
        return a == 0;
    }

    /**
     * 应用按位取反运算符
     * 
     * @param a 操作数
     * @return 按位取反结果
     */
    public Object applyBitwiseNot(double a) {
        return ~(int) a;
    }

    public String getSymbol() {
        return symbol;
    }

    public OperatorType getType() {
        return type;
    }

    public boolean isArithmetic() {
        return type == OperatorType.ARITHMETIC;
    }

    public boolean isComparison() {
        return type == OperatorType.COMPARISON;
    }

    public boolean isLogical() {
        return type == OperatorType.LOGICAL;
    }

    public boolean isBitwise() {
        return type == OperatorType.BITWISE;
    }

    /**
     * 根据符号解析运算符
     * 
     * @param symbol 运算符符号
     * @return 运算符
     */
    public static Operator parse(String symbol) {
        for (Operator op : values()) {
            if (op.symbol.equals(symbol)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown operator: " + symbol);
    }

    /**
     * 检查是否为有效运算符符号
     * 
     * @param symbol 符号
     * @return 是否有效
     */
    public static boolean isValidOperator(String symbol) {
        for (Operator op : values()) {
            if (op.symbol.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取运算符优先级（数值越小优先级越高）
     * 
     * @return 优先级
     */
    public int getPrecedence() {
        switch (this) {
            case POWER:
                return 1;
            case MULTIPLY:
            case DIVIDE:
            case MODULO:
            case INTEGER_DIVIDE:
                return 2;
            case ADD:
            case SUBTRACT:
                return 3;
            case LEFT_SHIFT:
            case RIGHT_SHIFT:
            case UNSIGNED_RIGHT_SHIFT:
                return 4;
            case GREATER_THAN:
            case LESS_THAN:
            case GREATER_OR_EQUAL:
            case LESS_OR_EQUAL:
                return 5;
            case EQUALS:
            case NOT_EQUALS:
                return 6;
            case BIT_AND:
                return 7;
            case BIT_XOR:
                return 8;
            case BIT_OR:
                return 9;
            case LOGICAL_AND:
                return 10;
            case LOGICAL_OR:
                return 11;
            default:
                return 99;
        }
    }

    /**
     * 检查是否左结合
     * 
     * @return 是否左结合
     */
    public boolean isLeftAssociative() {
        return this != POWER;
    }
}
