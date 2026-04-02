package com.ldzsai.kelp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.ldzsai.kelp.api.Cache;
import com.ldzsai.kelp.cache.LRUCache;
import com.ldzsai.kelp.config.EngineConfiguration;
import com.ldzsai.kelp.expression.Environment;
import com.ldzsai.kelp.metrics.MetricsCollector;

class KelpEngineTest {

    // ==================== 构建器测试 ====================

    @Test
    void testCreateDefaultEngine() {
        KelpEngine engine = KelpEngine.create();
        assertNotNull(engine);
        assertNotNull(engine.getConfig());
        assertNotNull(engine.getCache());
    }

    @Test
    void testBuilderWithCustomConfig() {
        KelpEngine engine = KelpEngine.builder()
            .maxCacheSize(100)
            .maxExpressionLength(5000)
            .build();

        assertNotNull(engine);
        assertEquals(100, engine.getConfig().getMaxCacheSize());
        assertEquals(5000, engine.getConfig().getMaxExpressionLength());
    }

    // ==================== 执行测试 ====================

    @Test
    void testExecuteWithLiteral() {
        KelpEngine engine = KelpEngine.create();
        Environment env = new Environment();
        String result = engine.execute("hello ${1 + 2}", env);
        assertEquals("hello 3.0", result);
    }

    @Test
    void testExecuteWithVariable() {
        KelpEngine engine = KelpEngine.create();
        Environment env = new Environment();
        env.setVariable("x", 42);
        String result = engine.execute("value is ${x}", env);
        assertEquals("value is 42", result);
    }

    // ==================== 配置测试 ====================

    @Test
    void testEngineConfiguration() {
        EngineConfiguration config = EngineConfiguration.builder()
            .maxExpressionLength(5000)
            .maxCacheSize(500)
            .enableLogging(false)
            .enableMetrics(true)
            .build();

        assertEquals(5000, config.getMaxExpressionLength());
        assertEquals(500, config.getMaxCacheSize());
        assertFalse(config.isEnableLogging());
        assertTrue(config.isEnableMetrics());
    }

    @Test
    void testEngineConfigurationDefaults() {
        EngineConfiguration config = EngineConfiguration.defaults();
        assertEquals(10_000, config.getMaxExpressionLength());
        assertEquals(1_000, config.getMaxCacheSize());
        assertFalse(config.isEnableLogging());
        assertFalse(config.isEnableMetrics());
    }

    // ==================== 指标集成测试 ====================

    @Test
    void testEngineWithMetrics() {
        KelpEngine engine = KelpEngine.builder()
            .enableMetrics(true)
            .build();

        Environment env = new Environment();
        env.setVariable("x", 10);
        engine.execute("${x + 1}", env);
        engine.execute("${x * 2}", env);

        assertNotNull(engine.getMetrics());
        assertEquals(2, engine.getMetrics().getCounter("kelp.expression.total"));
    }

    // ==================== 线程安全测试 ====================

    @Test
    void testConcurrentExecution() throws InterruptedException {
        KelpEngine engine = KelpEngine.create();
        int threadCount = 10;
        int iterations = 100;
        Thread[] threads = new Thread[threadCount];
        boolean[] errors = new boolean[threadCount];

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                try {
                    Environment env = new Environment();
                    env.setVariable("x", threadId);
                    for (int i = 0; i < iterations; i++) {
                        String result = engine.execute("result: ${x + 1}", env);
                        if (!result.equals("result: " + (threadId + 1) + ".0")) {
                            errors[threadId] = true;
                        }
                    }
                } catch (Exception e) {
                    errors[threadId] = true;
                }
            });
            threads[t].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (int t = 0; t < threadCount; t++) {
            assertFalse(errors[t], "线程 " + t + " 遇到错误");
        }
    }
}
