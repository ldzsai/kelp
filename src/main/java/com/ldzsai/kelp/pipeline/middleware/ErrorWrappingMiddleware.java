package com.ldzsai.kelp.pipeline.middleware;

import com.ldzsai.kelp.api.PipelineContext;
import com.ldzsai.kelp.api.PipelineMiddleware;
import com.ldzsai.kelp.exception.KelpException;

/**
 * 将原始异常封装为结构化的 KelpException。
 */
public class ErrorWrappingMiddleware implements PipelineMiddleware {
    @Override
    public boolean beforeProcess(PipelineContext context) {
        return true;
    }

    @Override
    public void afterProcess(PipelineContext context) {
        if (context.hasErrors()) {
            KelpException first = context.getErrors().get(0);
            throw first;
        }
    }
}
