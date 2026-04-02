package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.api.ExpressionVisitor;

/**
 * 变量引用表达式。
 */
public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(Environment env) {
        return env.getVariable(name);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitVariable(this);
    }

    @Override
    public String describe() {
        return name;
    }

    public String getName() { return name; }
}
