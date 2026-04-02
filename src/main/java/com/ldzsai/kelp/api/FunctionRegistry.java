package com.ldzsai.kelp.api;

import java.util.function.Function;

/**
 * 自定义函数注册与查找。
 * 将函数调用与 Java 反射机制解耦。
 */
public interface FunctionRegistry {
    /**
     * 注册一个由 lambda 实现的命名函数。
     *
     * @param name     表达式中使用的函数名称
     * @param function 函数实现
     */
    void register(String name, Function<Object[], Object> function);

    /**
     * 根据名称查找函数。
     *
     * @param name 函数名称
     * @return 对应的函数，未注册时返回 null
     */
    Function<Object[], Object> resolve(String name);

    /**
     * 检查函数是否已注册。
     */
    boolean hasFunction(String name);
}
