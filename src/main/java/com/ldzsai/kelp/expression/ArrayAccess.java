package com.ldzsai.kelp.expression;

import java.util.List;

import com.ldzsai.kelp.KelpException;

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
        Object indexObj = indexExpression.evaluate(env);

        // 检查索引是否为数字类型
        if (!(indexObj instanceof Number)) {
            throw new KelpException("Array index must be a number, but got: " + 
                (indexObj != null ? indexObj.getClass().getSimpleName() : "null"));
        }

        int idx = ((Number) indexObj).intValue();

        // 处理数组类型
        if (array instanceof Object[]) {
            Object[] list = (Object[]) array;
            if (idx < 0 || idx >= list.length) {
                throw new KelpException("Array index out of bounds: " + idx + 
                    " (array length: " + list.length + ")");
            }
            return list[idx];
        } 
        // 处理List类型
        else if (array instanceof List) {
            List<Object> list = (List<Object>) array;
            if (idx < 0 || idx >= list.size()) {
                throw new KelpException("List index out of bounds: " + idx + 
                    " (list size: " + list.size() + ")");
            }
            return list.get(idx);
        } 
        // 不支持的类型
        else {
            throw new KelpException("Expected an array or list but got " + 
                (array != null ? array.getClass().getSimpleName() : "null"));
        }
    }

    @Override
    public String toString() {
        return baseExpression.getClass().getSimpleName() + "[" + indexExpression + "]";
    }
}