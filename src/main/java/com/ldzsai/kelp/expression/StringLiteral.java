package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.api.ExpressionVisitor;

/**
 * 字符串字面量表达式。
 */
public class StringLiteral extends Expression {
    private final String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitStringLiteral(this);
    }

    @Override
    public String describe() {
        return "'" + value + "'";
    }

    public String getValue() { return value; }
}
