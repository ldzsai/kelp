package com.ldzsai.kelp.impl;

import java.util.List;

import com.ldzsai.kelp.Parser;
import com.ldzsai.kelp.api.ExpressionParser;
import com.ldzsai.kelp.exception.KelpParseException;
import com.ldzsai.kelp.expression.Expression;
import com.ldzsai.kelp.token.Token;

/**
 * 默认语法分析器实现，封装现有的递归下降解析器。
 */
public class DefaultExpressionParser implements ExpressionParser {

    @Override
    public List<Expression> parse(List<Token> tokens) throws KelpParseException {
        try {
            Parser parser = new Parser(tokens);
            return parser.buildAst();
        } catch (Exception e) {
            throw new KelpParseException(
                "Parsing failed: " + e.getMessage(), -1, null, e);
        }
    }
}
