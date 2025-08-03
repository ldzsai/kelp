package com.ldzsai.kelp.expression;

import java.util.Map;

import com.ldzsai.kelp.KelpException;

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

        // 处理字符串直接返回的情况
        if (base instanceof String) {
            return base;
        }

        // 检查基础对象是否为Map类型
        if (!(base instanceof Map)) {
            throw new KelpException("Expected an object (Map) but got " + 
                (base != null ? base.getClass().getSimpleName() : "null"));
        }

        // 检查键是否为字符串类型
        if (!(key instanceof String)) {
            throw new KelpException("Expected a string key but got " + 
                (key != null ? key.getClass().getSimpleName() : "null"));
        }

        Map<String, ?> map = (Map<String, ?>) base;
        String keyStr = (String) key;

        // 检查键是否存在
        if (!map.containsKey(keyStr)) {
            throw new KelpException("Cannot find the key '" + keyStr + "' in the object");
        }

        return map.get(keyStr);
    }

    @Override
    public String toString() {
        return baseExpression.getClass().getSimpleName() + "[" + keyExpression + "]";
    }
}