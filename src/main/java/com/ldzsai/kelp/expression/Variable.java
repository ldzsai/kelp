package com.ldzsai.kelp.expression;

public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(Environment env) {
        return env.getVariable(name);
    }
}
