package top.kangert.elp.expression;

import java.util.List;

public class ArrayAccess extends Expression {
    private final Expression baseExpression;
    private final Expression indexExpression;

    public ArrayAccess(Expression baseExpression, Expression indexExpression) {
        this.baseExpression = baseExpression;
        this.indexExpression = indexExpression;
    }

    @Override
    public Object evaluate(Environment env) throws Exception {
        Object array = baseExpression.evaluate(env);
        int idx = ((Number) indexExpression.evaluate(env)).intValue();

        if (array instanceof Object[]) {
            Object[] list = (Object[]) array;
            if (idx < 0 || idx >= list.length) {
                throw new KelpException("Index out of bounds: " + idx);
            }
            return list[idx];
        } else if (array instanceof List) {
            List<Object> list = (List<Object>) array;
            if (idx < 0 || idx >= list.size()) {
                throw new KelpException("Index out of bounds: " + idx);
            }
            return list.get(idx);
        } else {
            throw new KelpException("Expected an array but got " + array.getClass().getSimpleName());
        }
    }

    @Override
    public String toString() {
        return baseExpression.getClass().getSimpleName() + "[" + indexExpression + "]";
    }
}
