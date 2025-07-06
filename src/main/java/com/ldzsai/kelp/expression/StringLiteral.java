package com.ldzsai.kelp.expression;

public class StringLiteral extends Expression {

    private String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Object evaluate(Environment env) throws Exception {
        return value;
    }

}
