package com.ldzsai.kelp.expression;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 函数调用表达式
 */
public class FunctionCall extends Expression {
    // 函数调用表达式的目标
    private final Expression target;

    // 函数名称
    private final String name;

    // 函数参数
    private final List<Expression> arguments;

    public FunctionCall(Expression target, String name, List<Expression> arguments) {
        this.target = target;
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public Object evaluate(Environment env) throws Exception {
        // 解析参数
        List<Object> args = buildArgs(env, arguments);

        // 获取目标对象或类
        Object targetObject = target.evaluate(env);

        try {
            // 获取目标类
            Class<?> targetClass = targetObject instanceof Class<?> ? (Class<?>) targetObject : targetObject.getClass();

            // 获取参数类型
            Class<?>[] argTypes = args.stream().map(Object::getClass).toArray(Class<?>[]::new);

            // 查找方法
            Method method = targetClass.getMethod(name, argTypes);

            // 设置方法可访问
            method.setAccessible(true);

            // 判断是否为静态方法
            boolean isStatic = Modifier.isStatic(method.getModifiers());

            // 调用方法
            return isStatic ? method.invoke(null, args.toArray()) : method.invoke(targetObject, args.toArray());
        } catch (Exception e) {
            throw new RuntimeException("Error invoking method " + name, e);
        }
    }

    /**
     * 解析参数列表
     * 
     * @param env  上下文
     * @param args 参数表达式列表（参数支持exp、常量、字符串等）
     * @return 参数列表
     * @throws Exception 错误
     */
    private List<Object> buildArgs(Environment env, List<Expression> args) throws Exception {
        List<Object> result = new ArrayList<Object>();
        for (Expression exp : args) {
            result.add(exp.evaluate(env));
        }
        return result;
    }
}