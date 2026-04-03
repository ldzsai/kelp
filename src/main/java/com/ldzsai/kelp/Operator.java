package com.ldzsai.kelp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * 运算符枚举，包含位运算的正确实现。
 * 每个运算符携带其符号、类型和求值 lambda。
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
    EQUALS("==", OperatorType.COMPARISON, (a, b) -> a.doubleValue() == b.doubleValue()),
    NOT_EQUALS("!=", OperatorType.COMPARISON, (a, b) -> a.doubleValue() != b.doubleValue()),
    GREATER_THAN(">", OperatorType.COMPARISON, (a, b) -> a > b),
    LESS_THAN("<", OperatorType.COMPARISON, (a, b) -> a < b),
    GREATER_OR_EQUAL(">=", OperatorType.COMPARISON, (a, b) -> a >= b),
    LESS_OR_EQUAL("<=", OperatorType.COMPARISON, (a, b) -> a <= b),

    // 逻辑运算符
    LOGICAL_AND("&&", OperatorType.LOGICAL, (a, b) -> (a != 0) && (b != 0)),
    LOGICAL_OR("||", OperatorType.LOGICAL, (a, b) -> (a != 0) || (b != 0)),

    // 位运算符——正确的实现
    BIT_AND("&", OperatorType.BITWISE, (a, b) -> (double) (a.intValue() & b.intValue())),
    BIT_OR("|", OperatorType.BITWISE, (a, b) -> (double) (a.intValue() | b.intValue())),
    BIT_XOR("^", OperatorType.BITWISE, (a, b) -> (double) (a.intValue() ^ b.intValue())),
    LEFT_SHIFT("<<", OperatorType.BITWISE, (a, b) -> (double) (a.intValue() << b.intValue())),
    RIGHT_SHIFT(">>", OperatorType.BITWISE, (a, b) -> (double) (a.intValue() >> b.intValue())),
    UNSIGNED_RIGHT_SHIFT(">>>", OperatorType.BITWISE, (a, b) -> (double) (a.intValue() >>> b.intValue()));

    public enum OperatorType {
        ARITHMETIC,
        COMPARISON,
        LOGICAL,
        BITWISE
    }

    private static final Map<String, Operator> SYMBOL_MAP = new ConcurrentHashMap<>();

    static {
        for (Operator op : values()) {
            SYMBOL_MAP.put(op.symbol, op);
        }
    }

    private final String symbol;
    private final OperatorType type;
    private final BiFunction<Double, Double, Object> function;

    Operator(String symbol, OperatorType type, BiFunction<Double, Double, Object> function) {
        this.symbol = symbol;
        this.type = type;
        this.function = function;
    }

    public Object apply(double a, double b) {
        return function.apply(a, b);
    }

    public static Object applyNegation(double a) {
        return -a;
    }

    public static Object applyLogicalNot(double a) {
        return a == 0;
    }

    public static Object applyBitwiseNot(double a) {
        return (double) (~(int) a);
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
     * 将符号字符串解析为运算符。使用 O(1) 映射查找。
     *
     * @param symbol 运算符符号
     * @return 对应的运算符
     * @throws IllegalArgumentException 未知运算符时抛出
     */
    public static Operator parse(String symbol) {
        Operator op = SYMBOL_MAP.get(symbol);
        if (op == null) {
            throw new IllegalArgumentException("Unknown operator: " + symbol);
        }
        return op;
    }

    /**
     * 检查符号是否为已识别的运算符。
     */
    public static boolean isValidOperator(String symbol) {
        return SYMBOL_MAP.containsKey(symbol);
    }

    public int getPrecedence() {
        switch (this) {
            case POWER: return 1;
            case MULTIPLY: case DIVIDE: case MODULO: case INTEGER_DIVIDE: return 2;
            case ADD: case SUBTRACT: return 3;
            case LEFT_SHIFT: case RIGHT_SHIFT: case UNSIGNED_RIGHT_SHIFT: return 4;
            case GREATER_THAN: case LESS_THAN: case GREATER_OR_EQUAL: case LESS_OR_EQUAL: return 5;
            case EQUALS: case NOT_EQUALS: return 6;
            case BIT_AND: return 7;
            case BIT_XOR: return 8;
            case BIT_OR: return 9;
            case LOGICAL_AND: return 10;
            case LOGICAL_OR: return 11;
            default: return 99;
        }
    }

    public boolean isLeftAssociative() {
        return this != POWER;
    }
}
