package com.ldzsai.kelp.api;

import java.util.List;

import com.ldzsai.kelp.exception.KelpParseException;
import com.ldzsai.kelp.expression.Expression;
import com.ldzsai.kelp.token.Token;

/**
 * 语法分析契约接口。
 * 将标记流转换为抽象语法树。
 */
public interface ExpressionParser {
    /**
     * 将标记列表解析为抽象语法树（根表达式节点列表）。
     *
     * @param tokens 来自词法分析器的标记列表
     * @return 抽象语法树根表达式节点列表
     * @throws KelpParseException 语法错误时抛出
     */
    List<Expression> parse(List<Token> tokens) throws KelpParseException;
}
