package com.ldzsai.kelp.pipeline.middleware;

import com.ldzsai.kelp.api.PipelineContext;
import com.ldzsai.kelp.api.PipelineMiddleware;
import com.ldzsai.kelp.metrics.MetricsCollector;

/**
 * 记录执行指标（调用次数、延迟）。
 */
public class MetricsMiddleware implements PipelineMiddleware {
    private final MetricsCollector metrics;

    public MetricsMiddleware(MetricsCollector metrics) {
        this.metrics = metrics;
    }

    @Override
    public boolean beforeProcess(PipelineContext context) {
        context.setAttribute("metricsStartTime", System.nanoTime());
        metrics.incrementCounter("kelp.expression.total");
        return true;
    }

    @Override
    public void afterProcess(PipelineContext context) {
        Long startTime = context.getAttribute("metricsStartTime");
        if (startTime != null) {
            double durationMs = (System.nanoTime() - startTime) / 1_000_000.0;
            metrics.recordHistogram("kelp.expression.duration", durationMs);
        }
        if (context.hasErrors()) {
            metrics.incrementCounter("kelp.expression.error");
        }
    }
}
