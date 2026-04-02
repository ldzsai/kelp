package com.ldzsai.kelp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.ldzsai.kelp.expression.Environment;

class ComparisonOperatorTest {

    private final KelpEngine engine = KelpEngine.create();

    @Test
    void testLessThan() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);
        assertEquals("true", engine.execute("${a < b}", env));
        assertEquals("false", engine.execute("${b < a}", env));
    }

    @Test
    void testGreaterThan() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);
        assertEquals("false", engine.execute("${a > b}", env));
        assertEquals("true", engine.execute("${b > a}", env));
    }

    @Test
    void testLessThanOrEqual() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 10);
        assertEquals("true", engine.execute("${a <= b}", env));
        assertEquals("true", engine.execute("${a <= 20}", env));
        assertEquals("false", engine.execute("${a <= 5}", env));
    }

    @Test
    void testGreaterThanOrEqual() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 10);
        assertEquals("true", engine.execute("${a >= b}", env));
        assertEquals("false", engine.execute("${a >= 20}", env));
    }

    @Test
    void testNumericEquals() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 10);
        assertEquals("true", engine.execute("${a == b}", env));
        assertEquals("false", engine.execute("${a == 5}", env));
    }

    @Test
    void testNumericNotEquals() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);
        assertEquals("true", engine.execute("${a != b}", env));
        assertEquals("false", engine.execute("${a != 10}", env));
    }

    @Test
    void testStringEquals() {
        Environment env = new Environment();
        env.setVariable("s1", "hello");
        env.setVariable("s2", "hello");
        env.setVariable("s3", "world");
        assertEquals("true", engine.execute("${s1 == s2}", env));
        assertEquals("false", engine.execute("${s1 == s3}", env));
    }

    @Test
    void testStringNotEquals() {
        Environment env = new Environment();
        env.setVariable("s1", "hello");
        env.setVariable("s3", "world");
        assertEquals("true", engine.execute("${s1 != s3}", env));
    }

    @Test
    void testComparisonWithArithmetic() {
        Environment env = new Environment();
        assertEquals("true", engine.execute("${1 + 2 == 3}", env));
    }

    @Test
    void testNullEqualsNull() {
        Environment env = new Environment();
        assertEquals("true", engine.execute("${a == a}", env));
    }

    @Test
    void testNullNotEqualsNull() {
        Environment env = new Environment();
        assertEquals("false", engine.execute("${a != a}", env));
    }
}
