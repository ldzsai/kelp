package com.ldzsai.kelp.api;

import com.ldzsai.kelp.exception.KelpException;

/**
 * 结构化错误管理接口。
 */
public interface ErrorHandler {
    /**
     * 处理带上下文信息的处理错误。
     *
     * @param stage   发生错误的流水线阶段
     * @param message 可读的错误描述
     * @param cause   根本原因（可为 null）
     * @return 待抛出的 KelpException 异常（永不返回 null）
     */
    KelpException handleError(String stage, String message, Throwable cause);
}
