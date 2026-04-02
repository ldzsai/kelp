package com.ldzsai.kelp.pipeline.middleware;

import com.ldzsai.kelp.api.PipelineContext;
import com.ldzsai.kelp.api.PipelineMiddleware;
import com.ldzsai.kelp.logging.KelpLogger;

/**
 * 记录表达式执行的开始、结束及耗时。
 */
public class LoggingMiddleware implements PipelineMiddleware {
    private final KelpLogger logger;

    public LoggingMiddleware(KelpLogger logger) {
        this.logger = logger;
    }

    @Override
    public boolean beforeProcess(PipelineContext context) {
        logger.debug("Executing expression: {}", context.getInput());
        context.setAttribute("startTime", System.nanoTime());
        return true;
    }

    @Override
    public void afterProcess(PipelineContext context) {
        Long startTime = context.getAttribute("startTime");
        if (startTime != null) {
            long durationMs = (System.nanoTime() - startTime) / 1_000_000;
            logger.info("Expression executed in {}ms", durationMs);
        }
        if (context.hasErrors()) {
            logger.error("Expression execution had errors: {}", null, context.getErrors());
        }
    }
}
