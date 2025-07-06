package top.kangert.elp.expression;

import java.util.Map;

public class ObjectKeyAccess extends Expression {
    private final Expression baseExpression;
    private final Expression keyExpression;

    public ObjectKeyAccess(Expression baseExpression, Expression keyExpression) {
        this.baseExpression = baseExpression;
        this.keyExpression = keyExpression;
    }

    @Override
    public Object evaluate(Environment env) throws Exception {
        Object base = baseExpression.evaluate(env);
        Object key = keyExpression.evaluate(env);

        if (base instanceof String) {
            return (String) base;
        }

        if (!(base instanceof Map)) {
            throw new KelpException("Expected an object but got " + base.getClass().getSimpleName());
        }

        if (!(key instanceof String)) {
            throw new KelpException("Expected a string key but got " + key.getClass().getSimpleName());
        }

        Map<String, ?> map = (Map<String, ?>) base;
        String keyStr = (String) key;

        if (!map.containsKey(keyStr)) {
            throw new KelpException("Cannot find the key for '" + keyStr + "'");
        }

        return map.get(keyStr);
    }

    @Override
    public String toString() {
        return baseExpression.getClass().getSimpleName() + "[" + keyExpression + "]";
    }
}