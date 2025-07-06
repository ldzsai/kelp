package top.kangert.elp.expression;

import java.util.List;
import java.util.Map;

public class NestedAccess extends Expression {
    private final Expression baseExpression;
    private final Expression nestedExpression;

    public NestedAccess(Expression baseExpression, Expression nestedExpression) {
        this.baseExpression = baseExpression;
        this.nestedExpression = nestedExpression;
    }

    @Override
    public Object evaluate(Environment env) throws Exception {
        Object baseValue = baseExpression.evaluate(env);
        Object keyValue = nestedExpression.evaluate(env);

        if (baseValue instanceof List) {
            List<?> list = (List<?>) baseValue;
            if (keyValue instanceof Integer) {
                int index = (Integer) keyValue;
                if (index >= 0 && index < list.size()) {
                    return list.get(index);
                } else {
                    throw new KelpException("Index out of bounds: " + index);
                }
            } else {
                throw new KelpException("Expected an integer index but got " + keyValue.getClass().getSimpleName());
            }
        } else if (baseValue instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) baseValue;
            if (keyValue instanceof String) {
                String key = (String) keyValue;
                if (map.containsKey(key)) {
                    return map.get(key);
                } else {
                    throw new KelpException("Key not found: " + key);
                }
            } else {
                throw new KelpException("Expected a string key but got " + keyValue.getClass().getSimpleName());
            }
        } else {
            throw new KelpException("Base value is neither a list nor a map: " + baseValue.getClass().getSimpleName());
        }
    }

    @Override
    public String toString() {
        return "(" + baseExpression + "[" + nestedExpression + "])";
    }
}