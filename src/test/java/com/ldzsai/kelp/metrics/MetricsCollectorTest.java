package com.ldzsai.kelp.metrics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MetricsCollectorTest {

    @Test
    void testIncrementCounter() {
        MetricsCollector metrics = new MetricsCollector();
        metrics.incrementCounter("test.counter");
        metrics.incrementCounter("test.counter");
        metrics.incrementCounter("test.counter");

        assertEquals(3, metrics.getCounter("test.counter"));
    }

    @Test
    void testCounterDefaultValue() {
        MetricsCollector metrics = new MetricsCollector();
        assertEquals(0, metrics.getCounter("missing.counter"));
    }

    @Test
    void testRecordHistogram() {
        MetricsCollector metrics = new MetricsCollector();
        metrics.recordHistogram("latency", 10.0);
        metrics.recordHistogram("latency", 20.0);
        metrics.recordHistogram("latency", 30.0);

        MetricsCollector.HistogramData hist = metrics.getHistogram("latency");
        assertNotNull(hist);
        assertEquals(3, hist.getCount());
        assertEquals(20.0, hist.getAvg(), 0.001);
        assertEquals(10.0, hist.getMin(), 0.001);
        assertEquals(30.0, hist.getMax(), 0.001);
    }

    @Test
    void testHistogramDefaultValue() {
        MetricsCollector metrics = new MetricsCollector();
        assertNull(metrics.getHistogram("missing.histogram"));
    }

    @Test
    void testReset() {
        MetricsCollector metrics = new MetricsCollector();
        metrics.incrementCounter("counter");
        metrics.recordHistogram("hist", 10.0);

        metrics.reset();

        assertEquals(0, metrics.getCounter("counter"));
        assertNull(metrics.getHistogram("hist"));
    }

    @Test
    void testMultipleCounters() {
        MetricsCollector metrics = new MetricsCollector();
        metrics.incrementCounter("a");
        metrics.incrementCounter("b");
        metrics.incrementCounter("b");

        assertEquals(1, metrics.getCounter("a"));
        assertEquals(2, metrics.getCounter("b"));
    }

    @Test
    void testMultipleHistograms() {
        MetricsCollector metrics = new MetricsCollector();
        metrics.recordHistogram("h1", 5.0);
        metrics.recordHistogram("h1", 15.0);
        metrics.recordHistogram("h2", 100.0);

        MetricsCollector.HistogramData h1 = metrics.getHistogram("h1");
        MetricsCollector.HistogramData h2 = metrics.getHistogram("h2");

        assertEquals(2, h1.getCount());
        assertEquals(10.0, h1.getAvg(), 0.001);
        assertEquals(1, h2.getCount());
        assertEquals(100.0, h2.getAvg(), 0.001);
    }
}
