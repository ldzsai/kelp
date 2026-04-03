package com.ldzsai.kelp.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 内存中的指标收集器。
 * 追踪计数器和直方图以监控引擎性能。
 */
public class MetricsCollector {
    private final ConcurrentHashMap<String, LongAdder> counters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, HistogramData> histograms = new ConcurrentHashMap<>();

    public void incrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new LongAdder()).increment();
    }

    public void recordHistogram(String name, double value) {
        histograms.computeIfAbsent(name, k -> new HistogramData()).record(value);
    }

    public long getCounter(String name) {
        LongAdder adder = counters.get(name);
        return adder != null ? adder.sum() : 0;
    }

    public HistogramData getHistogram(String name) {
        return histograms.get(name);
    }

    public Map<String, LongAdder> getCounters() {
        return counters;
    }

    public Map<String, HistogramData> getHistograms() {
        return histograms;
    }

    public void reset() {
        counters.clear();
        histograms.clear();
    }

    /**
     * 简单直方图数据：追踪计数、总和、最小值、最大值。
     */
    public static class HistogramData {
        private final AtomicLong count = new AtomicLong();
        private final LongAdder sum = new LongAdder();
        private volatile double min = Double.MAX_VALUE;
        private volatile double max = Double.MIN_VALUE;

        public void record(double value) {
            count.incrementAndGet();
            sum.add((long) value);
            // 无 CAS 的最小/最大值处理——对指标收集可接受
            if (value < min) min = value;
            if (value > max) max = value;
        }

        public long getCount() { return count.get(); }
        public double getSum() { return sum.sum(); }
        public double getMin() { return min == Double.MAX_VALUE ? 0 : min; }
        public double getMax() { return max == Double.MIN_VALUE ? 0 : max; }
        public double getAvg() { long c = count.get(); return c > 0 ? sum.sum() / c : 0; }

        @Override
        public String toString() {
            return "Histogram{count=" + getCount() + ", avg=" + getAvg() +
                   ", min=" + getMin() + ", max=" + getMax() + "}";
        }
    }
}
