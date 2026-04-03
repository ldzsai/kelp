package com.ldzsai.kelp.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ldzsai.kelp.exception.KelpException;
import com.ldzsai.kelp.expression.Environment;

/**
 * 在处理流水线中传递的上下文对象。
 * 保存输入、结果、错误以及可扩展的属性信息。
 */
public class PipelineContext {
    private String input;
    private Environment environment;
    private String result;
    private final Map<String, Object> attributes = new HashMap<>();
    private final List<KelpException> errors = new ArrayList<>();
    private long executionTimeNanos;

    public PipelineContext(String input, Environment environment) {
        this.input = input;
        this.environment = environment;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public void setExecutionTimeNanos(long executionTimeNanos) {
        this.executionTimeNanos = executionTimeNanos;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    public void addError(KelpException error) {
        errors.add(error);
    }

    public List<KelpException> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
