package com.ldzsai.kelp.expression;

import java.util.Map;

import com.ldzsai.kelp.KelpException;
import com.ldzsai.kelp.api.ExpressionVisitor;

/**
 * 对象/映射键访问表达式。
 */
public class ObjectKeyAccess extends Expression {
    private final Expression baseExpression;
    private final Expression keyExpression;

    public ObjectKeyAccess(Expression baseExpression, Expression keyExpression) {
        this.baseExpression = baseExpression;
        this.keyExpression = keyExpression;
    }

    @Override
    public Object evaluate(Environment env) throws KelpException {
        Object base = baseExpression.evaluate(env);
        Object key = keyExpression.evaluate(env);

        // 字符串直接透传（用于字符串属性访问模式）
        if (base instanceof String) {
            return base;
        }

        if (!(base instanceof Map)) {
            throw new KelpException("Expected an object (Map) but got " +
                (base != null ? base.getClass().getSimpleName() : "null"));
        }

        if (!(key instanceof String)) {
            throw new KelpException("Expected a string key but got " +
                (key != null ? key.getClass().getSimpleName() : "null"));
        }

        Map<String, ?> map = (Map<String, ?>) base;
        String keyStr = (String) key;

        if (!map.containsKey(keyStr)) {
            throw new KelpException("Cannot find the key '" + keyStr + "' in the object");
        }

        return map.get(keyStr);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitObjectKeyAccess(this);
    }

    @Override
    public String describe() {
        return baseExpression.describe() + "[" + keyExpression.describe() + "]";
    }

    @Override
    public String toString() {
        return describe();
    }

    public Expression getBaseExpression() { return baseExpression; }
    public Expression getKeyExpression() { return keyExpression; }
}
