package com.ldzsai.kelp.expression;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.ldzsai.kelp.KelpException;

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
        
        if (targetObject == null) {
            throw new KelpException("Target object is null for method: " + name);
        }

        try {
            // 获取目标类
            Class<?> targetClass = targetObject instanceof Class<?> ? (Class<?>) targetObject : targetObject.getClass();

            // 获取参数类型
            Class<?>[] argTypes = args.stream().map(Object::getClass).toArray(Class<?>[]::new);

            // 查找方法
            Method method = findMethod(targetClass, name, argTypes);
            
            if (method == null) {
                throw new KelpException("Method not found: " + name + " with arguments of types: " + 
                    String.join(", ", getSimpleNames(argTypes)));
            }

            // 设置方法可访问
            method.setAccessible(true);

            // 判断是否为静态方法
            boolean isStatic = Modifier.isStatic(method.getModifiers());

            // 调用方法
            return isStatic ? method.invoke(null, args.toArray()) : method.invoke(targetObject, args.toArray());
        } catch (Exception e) {
            if (e instanceof KelpException) {
                throw e;
            }
            throw new KelpException("Error invoking method " + name + ": " + e.getMessage());
        }
    }

    /**
     * 查找匹配的方法（包括参数类型转换）
     */
    private Method findMethod(Class<?> targetClass, String name, Class<?>[] argTypes) {
        try {
            // 首先尝试精确匹配
            return targetClass.getMethod(name, argTypes);
        } catch (NoSuchMethodException e) {
            // 如果精确匹配失败，尝试查找兼容的方法
            for (Method method : targetClass.getMethods()) {
                if (method.getName().equals(name) && isCompatible(method.getParameterTypes(), argTypes)) {
                    return method;
                }
            }
            return null;
        }
    }

    /**
     * 检查参数类型是否兼容
     */
    private boolean isCompatible(Class<?>[] methodParamTypes, Class<?>[] argTypes) {
        if (methodParamTypes.length != argTypes.length) {
            return false;
        }
        
        for (int i = 0; i < methodParamTypes.length; i++) {
            if (!isAssignable(methodParamTypes[i], argTypes[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查类型是否可以赋值
     */
    private boolean isAssignable(Class<?> paramType, Class<?> argType) {
        if (paramType.isAssignableFrom(argType)) {
            return true;
        }
        
        // 处理基本类型和包装类型
        if (paramType.isPrimitive()) {
            if (paramType == int.class && argType == Integer.class) return true;
            if (paramType == long.class && argType == Long.class) return true;
            if (paramType == double.class && argType == Double.class) return true;
            if (paramType == float.class && argType == Float.class) return true;
            if (paramType == boolean.class && argType == Boolean.class) return true;
            if (paramType == byte.class && argType == Byte.class) return true;
            if (paramType == char.class && argType == Character.class) return true;
            if (paramType == short.class && argType == Short.class) return true;
        } else if (argType.isPrimitive()) {
            if (paramType == Integer.class && argType == int.class) return true;
            if (paramType == Long.class && argType == long.class) return true;
            if (paramType == Double.class && argType == double.class) return true;
            if (paramType == Float.class && argType == float.class) return true;
            if (paramType == Boolean.class && argType == boolean.class) return true;
            if (paramType == Byte.class && argType == byte.class) return true;
            if (paramType == Character.class && argType == char.class) return true;
            if (paramType == Short.class && argType == short.class) return true;
        }
        
        return false;
    }

    /**
     * 获取类名列表
     */
    private String[] getSimpleNames(Class<?>[] classes) {
        String[] names = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            names[i] = classes[i].getSimpleName();
        }
        return names;
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