package com.ldzsai.kelp;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ldzsai.kelp.expression.Environment;
import com.ldzsai.kelp.expression.Expression;
import com.ldzsai.kelp.token.Token;

public class ExpressionEngine {
    private Environment env;

    private Lexer lexer;

    private Parser parser;

    private List<Token> tokens;

    // 新增缓存
    private final Map<String, List<Expression>> cache = new ConcurrentHashMap<>();

    // 执行耗时
    private long lastExecutionTime = 0;

    public ExpressionEngine(Environment env) {
        this.env = env;
    }

    /**
     * 执行表达式
     * 
     * @param exp 表达式
     * @return 执行结果
     * @throws Exception 异常
     */
    public Object execute(String exp) throws Exception {
        if (exp == null) {
            throw new KelpException("Expression cannot be null");
        }
        
        // 记录开始时间
        long startTime = System.nanoTime();

        try {
            // 缓存命中判断
            List<Expression> ast = cache.get(exp);
            if (ast == null) {
                tokenizer(exp);
                ast = parser.buildAst();
                // 缓存未命中时解析并存储
                cache.put(exp, ast); 
            }

            StringBuilder result = new StringBuilder();
            for (Expression expression : ast) {
                Object value = expression.evaluate(env);
                if (value != null) {
                    result.append(value);
                }
            }

            // 记录结束时间并计算耗时（单位：毫秒）
            lastExecutionTime = (System.nanoTime() - startTime) / 1_000_000;

            return result.toString();
        } catch (Exception e) {
            // 记录结束时间并计算耗时（单位：毫秒）
            lastExecutionTime = (System.nanoTime() - startTime) / 1_000_000;
            
            if (e instanceof KelpException) {
                throw e;
            }
            throw new KelpException("Error executing expression: " + e.getMessage(), e);
        }
    }

    /**
     * 分词
     * 
     * @param exp 表达式
     */
    private void tokenizer(String exp) throws KelpException {
        try {
            this.lexer = new Lexer(exp);
            this.tokens = lexer.tokenizer();
            this.parser = new Parser(tokens);
        } catch (Exception e) {
            if (e instanceof KelpException) {
                throw e;
            }
            throw new KelpException("Error tokenizing expression: " + e.getMessage(), e);
        }
    }

    /**
     * 获取最近一次执行耗时（毫秒）
     */
    public long getLastExecutionTime() {
        return lastExecutionTime;
    }
    
    /**
     * 获取缓存大小
     * 
     * @return 缓存中表达式的数量
     */
    public int getCacheSize() {
        return cache.size();
    }
    
    /**
     * 清空缓存
     */
    public void clearCache() {
        cache.clear();
    }
}