package com.ldzsai.kelp.api;

import java.util.List;

import com.ldzsai.kelp.exception.KelpLexException;
import com.ldzsai.kelp.token.Token;

/**
 * 词法分析契约接口。
 * 无状态——相同输入始终产生相同输出。
 */
public interface Tokenizer {
    /**
     * 将输入表达式字符串分解为标记列表。
     *
     * @param input 原始表达式字符串（例如 "Hello ${name + 1}"）
     * @return 不可变的标记列表
     * @throws KelpLexException 词法错误时抛出
     */
    List<Token> tokenize(String input) throws KelpLexException;
}
