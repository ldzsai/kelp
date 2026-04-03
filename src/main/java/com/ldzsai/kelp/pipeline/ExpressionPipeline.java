package com.ldzsai.kelp.pipeline;

import java.util.Collections;
import java.util.List;

import com.ldzsai.kelp.api.Cache;
import com.ldzsai.kelp.api.ExpressionEvaluator;
import com.ldzsai.kelp.api.ExpressionParser;
import com.ldzsai.kelp.api.PipelineContext;
import com.ldzsai.kelp.api.PipelineMiddleware;
import com.ldzsai.kelp.api.Tokenizer;
import com.ldzsai.kelp.exception.KelpException;
import com.ldzsai.kelp.expression.Environment;
import com.ldzsai.kelp.expression.Expression;
import com.ldzsai.kelp.token.Token;

/**
 * 支持中间件的核心处理流水线。
 * 协调 词法分析 → 语法分析 → 求值 的流程，并支持前置/后置中间件链。
 */
public class ExpressionPipeline {
    private final Tokenizer tokenizer;
    private final ExpressionParser parser;
    private final ExpressionEvaluator evaluator;
    private final Cache<String, List<Expression>> astCache;
    private final List<PipelineMiddleware> middlewares;

    public ExpressionPipeline(
            Tokenizer tokenizer,
            ExpressionParser parser,
            ExpressionEvaluator evaluator,
            Cache<String, List<Expression>> astCache,
            List<PipelineMiddleware> middlewares) {
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.evaluator = evaluator;
        this.astCache = astCache;
        this.middlewares = middlewares != null ? middlewares : Collections.emptyList();
    }

    /**
     * 通过流水线执行表达式。
     *
     * @param input 表达式字符串
     * @param env   变量环境
     * @return 结果字符串
     * @throws KelpException 处理过程中发生任何错误时抛出
     */
    public String execute(String input, Environment env) {
        PipelineContext ctx = new PipelineContext(input, env);
        long startTime = System.nanoTime();

        try {
            // 前置处理中间件链
            for (PipelineMiddleware mw : middlewares) {
                if (!mw.beforeProcess(ctx)) {
                    // 运行后置处理以进行错误汇报
                    for (int i = middlewares.size() - 1; i >= 0; i--) {
                        middlewares.get(i).afterProcess(ctx);
                    }
                    // 如果存在错误，抛出第一个
                    if (ctx.hasErrors()) {
                        throw ctx.getErrors().get(0);
                    }
                    return ctx.getResult() != null ? ctx.getResult() : "";
                }
            }

            // 核心流水线
            List<Expression> ast = getOrParse(input);
            String result = evaluator.evaluate(ast, env);
            ctx.setResult(result);
            ctx.setExecutionTimeNanos(System.nanoTime() - startTime);

            // 后置处理中间件链
            for (int i = middlewares.size() - 1; i >= 0; i--) {
                middlewares.get(i).afterProcess(ctx);
            }

            return ctx.getResult() != null ? ctx.getResult() : "";

        } catch (KelpException e) {
            ctx.addError(e);
            ctx.setExecutionTimeNanos(System.nanoTime() - startTime);
            // 即使发生错误也运行后置处理
            for (int i = middlewares.size() - 1; i >= 0; i--) {
                try {
                    middlewares.get(i).afterProcess(ctx);
                } catch (KelpException ignored) {
                    // ErrorWrappingMiddleware 抛出的异常属于预期行为
                }
            }
            throw e;
        }
    }

    private List<Expression> getOrParse(String input) {
        List<Expression> cached = astCache.get(input);
        if (cached != null) return cached;

        List<Token> tokens = tokenizer.tokenize(input);
        List<Expression> ast = parser.parse(tokens);
        astCache.put(input, Collections.unmodifiableList(ast));
        return ast;
    }

    /**
     * 获取抽象语法树缓存以供外部检查。
     */
    public Cache<String, List<Expression>> getCache() {
        return astCache;
    }
}
