package com.ldzsai.kelp.api;

/**
 * 流水线处理的中间件接口。
 * 支持在核心流水线前后挂载处理钩子。
 */
public interface PipelineMiddleware {
    /**
     * 在核心处理阶段之前调用。
     *
     * @param context 流水线上下文
     * @return true 继续处理，false 短路中断
     */
    boolean beforeProcess(PipelineContext context);

    /**
     * 在核心处理阶段之后调用。
     *
     * @param context 流水线上下文
     */
    void afterProcess(PipelineContext context);
}
