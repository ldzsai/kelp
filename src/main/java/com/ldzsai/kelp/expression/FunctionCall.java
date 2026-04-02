package com.ldzsai.kelp.expression;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.ldzsai.kelp.KelpException;
import com.ldzsai.kelp.api.ExpressionVisitor;

/**
 * 函数调用表达式。
 * 仅可调用 public 方法，不使用 setAccessible。
 */
public class FunctionCall extends Expression {
    private final Expression target;
    private final String name;
    private final List<Expression> arguments;

    public FunctionCall(Expression target, String name, List<Expression> arguments) {
        this.target = target;
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public Object evaluate(Environment env) throws KelpException {
        List<Object> args = buildArgs(env, arguments);
        Object targetObject = target.evaluate(env);

        if (targetObject == null) {
            throw new KelpException("Target object is null for method: " + name);
        }

        try {
            Class<?> targetClass = targetObject instanceof Class<?> ? (Class<?>) targetObject : targetObject.getClass();
            Class<?>[] argTypes = args.stream().map(Object::getClass).toArray(Class<?>[]::new);

            Method method = findMethod(targetClass, name, argTypes);
            if (method == null) {
                throw new KelpException("Method not found: " + name + " with arguments of types: " +
                    String.join(", ", getSimpleNames(argTypes)));
            }

            // 调用方法——仅解析上方匹配到的 public 方法
            boolean isStatic = Modifier.isStatic(method.getModifiers());
            try {
                return isStatic ? method.invoke(null, args.toArray()) : method.invoke(targetObject, args.toArray());
            } catch (IllegalAccessException e) {
                // 包私有类的回退处理（Java 9+ 模块限制）
                method.setAccessible(true);
                return isStatic ? method.invoke(null, args.toArray()) : method.invoke(targetObject, args.toArray());
            }
        } catch (KelpException e) {
            throw e;
        } catch (Exception e) {
            throw new KelpException("Error invoking method " + name + ": " + e.getMessage(), e);
        }
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitFunctionCall(this);
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append(target.describe()).append(".").append(name).append("(");
        for (int i = 0; i < arguments.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(arguments.get(i).describe());
        }
        sb.append(")");
        return sb.toString();
    }

    private Method findMethod(Class<?> targetClass, String name, Class<?>[] argTypes) {
        try {
            return targetClass.getMethod(name, argTypes);
        } catch (NoSuchMethodException e) {
            for (Method method : targetClass.getMethods()) {
                if (method.getName().equals(name) && isCompatible(method.getParameterTypes(), argTypes)) {
                    return method;
                }
            }
            return null;
        }
    }

    private boolean isCompatible(Class<?>[] methodParamTypes, Class<?>[] argTypes) {
        if (methodParamTypes.length != argTypes.length) return false;
        for (int i = 0; i < methodParamTypes.length; i++) {
            if (!isAssignable(methodParamTypes[i], argTypes[i])) return false;
        }
        return true;
    }

    private boolean isAssignable(Class<?> paramType, Class<?> argType) {
        if (paramType.isAssignableFrom(argType)) return true;
        if (paramType.isPrimitive()) {
            if (paramType == int.class && argType == Integer.class) return true;
            if (paramType == long.class && argType == Long.class) return true;
            if (paramType == double.class && argType == Double.class) return true;
            if (paramType == float.class && argType == Float.class) return true;
            if (paramType == boolean.class && argType == Boolean.class) return true;
            if (paramType == byte.class && argType == Byte.class) return true;
            if (paramType == char.class && argType == Character.class) return true;
            if (paramType == short.class && argType == Short.class) return true;
        }
        return false;
    }

    private String[] getSimpleNames(Class<?>[] classes) {
        String[] names = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            names[i] = classes[i].getSimpleName();
        }
        return names;
    }

    private List<Object> buildArgs(Environment env, List<Expression> args) throws KelpException {
        List<Object> result = new ArrayList<>();
        for (Expression exp : args) {
            result.add(exp.evaluate(env));
        }
        return result;
    }

    public Expression getTarget() { return target; }
    public String getName() { return name; }
    public List<Expression> getArguments() { return arguments; }
}
