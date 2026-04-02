package com.ldzsai.kelp.expression;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 变量存储上下文，支持可选的父级作用域链。
 * 通过 synchronized 方法保证并发访问的线程安全性。
 * 支持 null 变量值（区分"显式设置为 null"与"未设置"）。
 */
public class Environment implements Cloneable {
    private static final Object NULL_SENTINEL = new Object();

    private final Map<String, Object> variables;
    private final Environment parent;

    public Environment() {
        this(null);
    }

    public Environment(Environment parent) {
        this.variables = new HashMap<>();
        this.parent = parent;
    }

    public synchronized void setVariable(String name, Object value) {
        variables.put(name, value != null ? value : NULL_SENTINEL);
    }

    /**
     * 根据变量名获取值。未定义的变量和显式设置为 null 的变量均返回 null。
     */
    public synchronized Object getVariable(String name) {
        if (variables.containsKey(name)) {
            Object val = variables.get(name);
            return val == NULL_SENTINEL ? null : val;
        }
        if (parent != null) {
            return parent.getVariable(name);
        }
        return null;
    }

    /**
     * 检查变量是否存在（包括父级作用域）。
     */
    public synchronized boolean hasVariable(String name) {
        return variables.containsKey(name) || (parent != null && parent.hasVariable(name));
    }

    /**
     * 从当前作用域中移除变量。
     */
    public synchronized void removeVariable(String name) {
        variables.remove(name);
    }

    /**
     * 创建继承当前环境的子环境。
     */
    public Environment createChild() {
        return new Environment(this);
    }

    /**
     * 获取父级环境（根环境返回 null）。
     */
    public Environment getParent() {
        return parent;
    }

    /**
     * 获取所有可见变量的不可变快照。
     */
    public synchronized Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        if (parent != null) {
            result.putAll(parent.toMap());
        }
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            result.put(entry.getKey(), entry.getValue() == NULL_SENTINEL ? null : entry.getValue());
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public synchronized Environment clone() {
        Environment clone = new Environment(parent);
        clone.variables.putAll(variables);
        return clone;
    }
}
