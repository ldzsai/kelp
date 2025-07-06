package com.ldzsai.kelp;

import java.util.function.BiFunction;

public enum Operator {
    ADD("+", (a, b) -> a + b),
    SUBTRACT("-", (a, b) -> a - b),
    MULTIPLY("*", (a, b) -> a * b),
    DIVIDE("/", (a, b) -> a / b);

    private final String symbol;
    private final BiFunction<Double, Double, Double> function;

    Operator(String symbol, BiFunction<Double, Double, Double> function) {
        this.symbol = symbol;
        this.function = function;
    }

    public double apply(double a, double b) {
        return function.apply(a, b);
    }

    public static Operator parse(String symbol) {
        for (Operator op : values()) {
            if (op.symbol.equals(symbol)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown operator: " + symbol);
    }
}
