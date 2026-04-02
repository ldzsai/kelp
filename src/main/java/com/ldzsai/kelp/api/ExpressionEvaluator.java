package com.ldzsai.kelp.api;

import java.util.List;

import com.ldzsai.kelp.exception.KelpEvaluationException;
import com.ldzsai.kelp.expression.Environment;
import com.ldzsai.kelp.expression.Expression;

/**
 * 求值契约接口。
 * 对抽象语法树节点在指定环境中进行求值。
 */
public interface ExpressionEvaluator {
    /**
     * 对一组抽象语法树根节点在指定环境中进行求值。
     *
     * @param expressions 抽象语法树根节点列表
     * @param env         变量环境
     * @return 拼接后的字符串结果
     * @throws KelpEvaluationException 求值错误时抛出
     */
    String evaluate(List<Expression> expressions, Environment env) throws KelpEvaluationException;
}
