package com.ldzsai.kelp.expression;

import com.ldzsai.kelp.api.ExpressionVisitor;
import com.ldzsai.kelp.exception.KelpException;

/**
 * 所有抽象语法树表达式节点的抽象基类。
 * 支持访问者模式以实现可扩展操作。
 */
public abstract class Expression {
    /**
     * 在指定环境中求值当前表达式。
     *
     * @param env 变量上下文
     * @return 计算结果
     * @throws KelpException 求值失败时抛出异常
     */
    public abstract Object evaluate(Environment env) throws KelpException;

    /**
     * 接受访问者以实现双重分派操作。
     *
     * @param visitor 待接受的访问者
     * @param <T>     访问者返回类型
     * @return 访问者返回的结果
     */
    public abstract <T> T accept(ExpressionVisitor<T> visitor);

    /**
     * 返回此表达式节点的可读描述。
     */
    public abstract String describe();
}
