package com.ldzsai.kelp.impl;

import java.util.List;

import com.ldzsai.kelp.api.ExpressionEvaluator;
import com.ldzsai.kelp.exception.KelpEvaluationException;
import com.ldzsai.kelp.expression.Environment;
import com.ldzsai.kelp.expression.Expression;

/**
 * 默认表达式求值器实现。
 * 对每个抽象语法树根节点进行求值，并将结果拼接为字符串。
 */
public class DefaultExpressionEvaluator implements ExpressionEvaluator {

    @Override
    public String evaluate(List<Expression> expressions, Environment env) throws KelpEvaluationException {
        try {
            StringBuilder result = new StringBuilder();
            for (Expression expression : expressions) {
                Object value = expression.evaluate(env);
                if (value != null) {
                    result.append(value);
                }
            }
            return result.toString();
        } catch (KelpEvaluationException e) {
            throw e;
        } catch (Exception e) {
            throw new KelpEvaluationException(
                "Evaluation failed: " + e.getMessage(), e);
        }
    }
}
