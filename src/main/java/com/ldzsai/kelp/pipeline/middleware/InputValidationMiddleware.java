package com.ldzsai.kelp.pipeline.middleware;

import com.ldzsai.kelp.api.PipelineContext;
import com.ldzsai.kelp.api.PipelineMiddleware;
import com.ldzsai.kelp.config.EngineConfiguration;
import com.ldzsai.kelp.exception.KelpException;

/**
 * 处理前对输入进行校验。
 * 检查空值、空字符串及最大长度限制。
 */
public class InputValidationMiddleware implements PipelineMiddleware {
    private final int maxLength;

    public InputValidationMiddleware(EngineConfiguration config) {
        this.maxLength = config.getMaxExpressionLength();
    }

    public InputValidationMiddleware(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public boolean beforeProcess(PipelineContext context) {
        String input = context.getInput();
        if (input == null) {
            context.addError(new KelpException("Expression cannot be null"));
            return false;
        }
        if (input.isEmpty()) {
            context.addError(new KelpException("Expression cannot be empty"));
            return false;
        }
        if (input.length() > maxLength) {
            context.addError(new KelpException("Expression length " + input.length() +
                " exceeds maximum allowed length of " + maxLength));
            return false;
        }
        return true;
    }

    @Override
    public void afterProcess(PipelineContext context) {
        // 空操作
    }
}
