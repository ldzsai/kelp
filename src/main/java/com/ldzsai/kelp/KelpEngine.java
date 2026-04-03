package com.ldzsai.kelp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ldzsai.kelp.api.Cache;
import com.ldzsai.kelp.api.ExpressionEvaluator;
import com.ldzsai.kelp.api.ExpressionParser;
import com.ldzsai.kelp.api.FunctionRegistry;
import com.ldzsai.kelp.api.PipelineMiddleware;
import com.ldzsai.kelp.api.Tokenizer;
import com.ldzsai.kelp.cache.LRUCache;
import com.ldzsai.kelp.config.EngineConfiguration;
import com.ldzsai.kelp.exception.KelpException;
import com.ldzsai.kelp.expression.Environment;
import com.ldzsai.kelp.expression.Expression;
import com.ldzsai.kelp.function.DefaultFunctionRegistry;
import com.ldzsai.kelp.impl.DefaultExpressionEvaluator;
import com.ldzsai.kelp.impl.DefaultExpressionParser;
import com.ldzsai.kelp.impl.DefaultTokenizer;
import com.ldzsai.kelp.logging.DefaultKelpLogger;
import com.ldzsai.kelp.logging.KelpLogger;
import com.ldzsai.kelp.metrics.MetricsCollector;
import com.ldzsai.kelp.pipeline.ExpressionPipeline;
import com.ldzsai.kelp.pipeline.middleware.ErrorWrappingMiddleware;
import com.ldzsai.kelp.pipeline.middleware.InputValidationMiddleware;
import com.ldzsai.kelp.pipeline.middleware.LoggingMiddleware;
import com.ldzsai.kelp.pipeline.middleware.MetricsMiddleware;

/**
 * 现代表达式引擎门面类。
 * 通过构建器模式构造后保证线程安全。
 * 采用支持中间件的流水线架构。
 *
 * <pre>
 * KelpEngine engine = KelpEngine.builder()
 *     .maxCacheSize(500)
 *     .enableMetrics(true)
 *     .build();
 *
 * Environment env = new Environment();
 * env.setVariable("name", "world");
 * String result = engine.execute("Hello ${name}", env);
 * </pre>
 */
public class KelpEngine {
    private final EngineConfiguration config;
    private final Tokenizer tokenizer;
    private final ExpressionParser parser;
    private final ExpressionEvaluator evaluator;
    private final Cache<String, List<Expression>> astCache;
    private final FunctionRegistry functionRegistry;
    private final KelpLogger logger;
    private final MetricsCollector metrics;
    private final ExpressionPipeline pipeline;

    KelpEngine(
            EngineConfiguration config,
            Tokenizer tokenizer,
            ExpressionParser parser,
            ExpressionEvaluator evaluator,
            Cache<String, List<Expression>> astCache,
            FunctionRegistry functionRegistry,
            KelpLogger logger,
            MetricsCollector metrics,
            List<PipelineMiddleware> middlewares) {
        this.config = config;
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.evaluator = evaluator;
        this.astCache = astCache;
        this.functionRegistry = functionRegistry;
        this.logger = logger;
        this.metrics = metrics;
        this.pipeline = new ExpressionPipeline(tokenizer, parser, evaluator, astCache, middlewares);
    }

    /**
     * 使用指定环境执行表达式字符串。
     *
     * @param expression 表达式字符串
     * @param env        变量环境
     * @return 以字符串形式返回的结果
     * @throws KelpException 发生任何错误时抛出
     */
    public String execute(String expression, Environment env) throws KelpException {
        return pipeline.execute(expression, env);
    }

    /**
     * 获取引擎配置。
     */
    public EngineConfiguration getConfig() {
        return config;
    }

    /**
     * 获取函数注册表以注册自定义函数。
     */
    public FunctionRegistry getFunctionRegistry() {
        return functionRegistry;
    }

    /**
     * 获取抽象语法树缓存以供检查。
     */
    public Cache<String, List<Expression>> getCache() {
        return astCache;
    }

    /**
     * 获取指标收集器（仅在指标功能启用时可用）。
     */
    public MetricsCollector getMetrics() {
        return metrics;
    }

    /**
     * 获取日志记录器。
     */
    public KelpLogger getLogger() {
        return logger;
    }

    /**
     * 获取底层流水线以供高级用途使用。
     */
    public ExpressionPipeline getPipeline() {
        return pipeline;
    }

    /**
     * 创建带有默认配置的新引擎构建器。
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 创建带有默认设置的新引擎（便捷工厂方法）。
     */
    public static KelpEngine create() {
        return new Builder().build();
    }

    /**
     * 用于构造自定义配置 KelpEngine 实例的构建器。
     */
    public static class Builder {
        private EngineConfiguration config = EngineConfiguration.defaults();
        private Tokenizer tokenizer;
        private ExpressionParser parser;
        private ExpressionEvaluator evaluator;
        private Cache<String, List<Expression>> astCache;
        private FunctionRegistry functionRegistry;
        private KelpLogger logger;
        private MetricsCollector metrics;
        private final List<PipelineMiddleware> middlewares = new ArrayList<>();
        private boolean useDefaultMiddlewares = true;

        // 配置快捷方法
        public Builder config(EngineConfiguration config) { this.config = config; return this; }
        public Builder maxExpressionLength(int len) { this.config = EngineConfiguration.builder().maxExpressionLength(len).maxCacheSize(config.getMaxCacheSize()).enableLogging(config.isEnableLogging()).enableMetrics(config.isEnableMetrics()).strictNullAccess(config.isStrictNullAccess()).maxRecursionDepth(config.getMaxRecursionDepth()).build(); return this; }
        public Builder maxCacheSize(int size) { this.config = EngineConfiguration.builder().maxExpressionLength(config.getMaxExpressionLength()).maxCacheSize(size).enableLogging(config.isEnableLogging()).enableMetrics(config.isEnableMetrics()).strictNullAccess(config.isStrictNullAccess()).maxRecursionDepth(config.getMaxRecursionDepth()).build(); return this; }
        public Builder enableLogging(boolean enable) { this.config = EngineConfiguration.builder().maxExpressionLength(config.getMaxExpressionLength()).maxCacheSize(config.getMaxCacheSize()).enableLogging(enable).enableMetrics(config.isEnableMetrics()).strictNullAccess(config.isStrictNullAccess()).maxRecursionDepth(config.getMaxRecursionDepth()).build(); return this; }
        public Builder enableMetrics(boolean enable) { this.config = EngineConfiguration.builder().maxExpressionLength(config.getMaxExpressionLength()).maxCacheSize(config.getMaxCacheSize()).enableLogging(config.isEnableLogging()).enableMetrics(enable).strictNullAccess(config.isStrictNullAccess()).maxRecursionDepth(config.getMaxRecursionDepth()).build(); return this; }

        // 组件覆盖
        public Builder tokenizer(Tokenizer t) { this.tokenizer = t; return this; }
        public Builder parser(ExpressionParser p) { this.parser = p; return this; }
        public Builder evaluator(ExpressionEvaluator e) { this.evaluator = e; return this; }
        public Builder cache(Cache<String, List<Expression>> c) { this.astCache = c; return this; }
        public Builder functionRegistry(FunctionRegistry r) { this.functionRegistry = r; return this; }
        public Builder logger(KelpLogger l) { this.logger = l; return this; }
        public Builder metrics(MetricsCollector m) { this.metrics = m; return this; }

        /**
         * 向流水线添加中间件。调用此方法将禁用默认中间件。
         */
        public Builder middleware(PipelineMiddleware mw) {
            this.middlewares.add(mw);
            this.useDefaultMiddlewares = false;
            return this;
        }

        /**
         * 向流水线添加多个中间件。
         */
        public Builder middlewares(PipelineMiddleware... mws) {
            this.middlewares.addAll(Arrays.asList(mws));
            this.useDefaultMiddlewares = false;
            return this;
        }

        /**
         * 即使添加自定义中间件也保留默认中间件。
         */
        public Builder withDefaultMiddlewares() {
            this.useDefaultMiddlewares = true;
            return this;
        }

        /**
         * 构建 KelpEngine 实例。
         */
        public KelpEngine build() {
            // 为未设置的组件绑定默认实现
            if (tokenizer == null) tokenizer = new DefaultTokenizer();
            if (parser == null) parser = new DefaultExpressionParser();
            if (evaluator == null) evaluator = new DefaultExpressionEvaluator();
            if (astCache == null) astCache = new LRUCache<>(config.getMaxCacheSize());
            if (functionRegistry == null) functionRegistry = new DefaultFunctionRegistry();
            if (logger == null) logger = config.isEnableLogging() ? new DefaultKelpLogger() : null;
            if (metrics == null) metrics = config.isEnableMetrics() ? new MetricsCollector() : null;

            // 绑定中间件
            List<PipelineMiddleware> finalMiddlewares = new ArrayList<>();
            if (useDefaultMiddlewares) {
                finalMiddlewares.add(new InputValidationMiddleware(config));
                if (config.isEnableLogging() && logger != null) {
                    finalMiddlewares.add(new LoggingMiddleware(logger));
                }
                if (config.isEnableMetrics() && metrics != null) {
                    finalMiddlewares.add(new MetricsMiddleware(metrics));
                }
                finalMiddlewares.add(new ErrorWrappingMiddleware());
            }
            finalMiddlewares.addAll(middlewares);

            return new KelpEngine(config, tokenizer, parser, evaluator, astCache,
                    functionRegistry, logger, metrics, finalMiddlewares);
        }
    }
}
