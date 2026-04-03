package com.ldzsai.kelp.impl;

import java.util.List;

import com.ldzsai.kelp.Lexer;
import com.ldzsai.kelp.api.Tokenizer;
import com.ldzsai.kelp.exception.KelpLexException;
import com.ldzsai.kelp.token.Token;

/**
 * 默认标记器实现，封装现有的词法分析器。
 */
public class DefaultTokenizer implements Tokenizer {

    @Override
    public List<Token> tokenize(String input) throws KelpLexException {
        try {
            Lexer lexer = new Lexer(input);
            return lexer.tokenize();
        } catch (Exception e) {
            throw new KelpLexException(
                "Tokenization failed: " + e.getMessage(), -1, input, e);
        }
    }
}
