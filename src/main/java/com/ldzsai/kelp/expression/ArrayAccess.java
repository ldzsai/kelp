package com.ldzsai.kelp.expression;

import java.util.List;

import com.ldzsai.kelp.KelpException;
import com.ldzsai.kelp.api.ExpressionVisitor;

/**
 * 数组/列表索引访问表达式。
 */
public class ArrayAccess extends Expression {
    private final Expression baseExpression;
    private final Expression indexExpression;

    public ArrayAccess(Expression baseExpression, Expression indexExpression) {
        this.baseExpression = baseExpression;
        this.indexExpression = indexExpression;
    }

    @Override
    public Object evaluate(Environment env) throws KelpException {
        Object array = baseExpression.evaluate(env);
        Object indexObj = indexExpression.evaluate(env);

        if (!(indexObj instanceof Number)) {
            throw new KelpException("Array index must be a number, but got: " +
                (indexObj != null ? indexObj.getClass().getSimpleName() : "null"));
        }

        int idx = ((Number) indexObj).intValue();

        if (array instanceof Object[]) {
            Object[] list = (Object[]) array;
            if (idx < 0 || idx >= list.length) {
                throw new KelpException("Array index out of bounds: " + idx +
                    " (array length: " + list.length + ")");
            }
            return list[idx];
        } else if (array instanceof List) {
            List<Object> list = (List<Object>) array;
            if (idx < 0 || idx >= list.size()) {
                throw new KelpException("List index out of bounds: " + idx +
                    " (list size: " + list.size() + ")");
            }
            return list.get(idx);
        } else {
            throw new KelpException("Expected an array or list but got " +
                (array != null ? array.getClass().getSimpleName() : "null"));
        }
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitArrayAccess(this);
    }

    @Override
    public String describe() {
        return baseExpression.describe() + "[" + indexExpression.describe() + "]";
    }

    @Override
    public String toString() {
        return describe();
    }

    public Expression getBaseExpression() { return baseExpression; }
    public Expression getIndexExpression() { return indexExpression; }
}
