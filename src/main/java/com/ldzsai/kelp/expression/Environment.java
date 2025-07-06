package com.ldzsai.kelp.expression;

import java.util.HashMap;
import java.util.Map;

public class Environment implements Cloneable {
    private final Map<String, Object> variables = new HashMap<>();

    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    public Object getVariable(String name) {
        return variables.get(name);
    }

    @Override
    public Environment clone() {
        Environment clone = new Environment();
        clone.variables.putAll(variables);
        return clone;
    }
}