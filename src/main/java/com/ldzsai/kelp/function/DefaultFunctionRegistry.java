package com.ldzsai.kelp.function;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.ldzsai.kelp.api.FunctionRegistry;

/**
 * 基于 ConcurrentHashMap 的线程安全函数注册表。
 */
public class DefaultFunctionRegistry implements FunctionRegistry {
    private final ConcurrentHashMap<String, Function<Object[], Object>> functions = new ConcurrentHashMap<>();

    @Override
    public void register(String name, Function<Object[], Object> function) {
        if (name == null || function == null) {
            throw new IllegalArgumentException("Name and function must not be null");
        }
        functions.put(name, function);
    }

    @Override
    public Function<Object[], Object> resolve(String name) {
        return functions.get(name);
    }

    @Override
    public boolean hasFunction(String name) {
        return functions.containsKey(name);
    }
}
