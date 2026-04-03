package com.ldzsai.kelp.config;

/**
 * 表达式引擎的配置类。
 * 通过构建器模式构造后不可变。
 */
public class EngineConfiguration {
    private final int maxExpressionLength;
    private final int maxCacheSize;
    private final boolean enableLogging;
    private final boolean enableMetrics;
    private final boolean strictNullAccess;
    private final int maxRecursionDepth;

    private EngineConfiguration(Builder builder) {
        this.maxExpressionLength = builder.maxExpressionLength;
        this.maxCacheSize = builder.maxCacheSize;
        this.enableLogging = builder.enableLogging;
        this.enableMetrics = builder.enableMetrics;
        this.strictNullAccess = builder.strictNullAccess;
        this.maxRecursionDepth = builder.maxRecursionDepth;
    }

    public int getMaxExpressionLength() { return maxExpressionLength; }
    public int getMaxCacheSize() { return maxCacheSize; }
    public boolean isEnableLogging() { return enableLogging; }
    public boolean isEnableMetrics() { return enableMetrics; }
    public boolean isStrictNullAccess() { return strictNullAccess; }
    public int getMaxRecursionDepth() { return maxRecursionDepth; }

    public static EngineConfiguration defaults() {
        return new Builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int maxExpressionLength = 10_000;
        private int maxCacheSize = 1_000;
        private boolean enableLogging = false;
        private boolean enableMetrics = false;
        private boolean strictNullAccess = true;
        private int maxRecursionDepth = 100;

        public Builder maxExpressionLength(int len) { this.maxExpressionLength = len; return this; }
        public Builder maxCacheSize(int size) { this.maxCacheSize = size; return this; }
        public Builder enableLogging(boolean enable) { this.enableLogging = enable; return this; }
        public Builder enableMetrics(boolean enable) { this.enableMetrics = enable; return this; }
        public Builder strictNullAccess(boolean strict) { this.strictNullAccess = strict; return this; }
        public Builder maxRecursionDepth(int depth) { this.maxRecursionDepth = depth; return this; }

        public EngineConfiguration build() {
            return new EngineConfiguration(this);
        }
    }
}
