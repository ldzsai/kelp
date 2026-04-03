package com.ldzsai.kelp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.ldzsai.kelp.expression.Environment;

class LogicalOperatorTest {

    private final KelpEngine engine = KelpEngine.create();

    @Test
    void testLogicalAnd() {
        Environment env = new Environment();
        env.setVariable("a", 1);
        env.setVariable("b", 0);
        assertEquals("true", engine.execute("${a && a}", env));
        assertEquals("false", engine.execute("${a && b}", env));
    }

    @Test
    void testLogicalOr() {
        Environment env = new Environment();
        env.setVariable("a", 1);
        env.setVariable("b", 0);
        assertEquals("true", engine.execute("${a || b}", env));
        assertEquals("false", engine.execute("${b || b}", env));
    }

    @Test
    void testLogicalNot() {
        Environment env = new Environment();
        env.setVariable("a", 1);
        env.setVariable("b", 0);
        assertEquals("false", engine.execute("${!a}", env));
        assertEquals("true", engine.execute("${!b}", env));
    }

    @Test
    void testAndShortCircuit() {
        Environment env = new Environment();
        env.setVariable("a", 0);
        env.setVariable("b", 1);
        assertEquals("false", engine.execute("${a && b}", env));
    }

    @Test
    void testOrShortCircuit() {
        Environment env = new Environment();
        env.setVariable("a", 1);
        env.setVariable("b", 0);
        assertEquals("true", engine.execute("${a || b}", env));
    }

    @Test
    void testLogicalWithComparison() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);
        env.setVariable("c", 5);
        assertEquals("true", engine.execute("${a < b && b > c}", env));
    }

    @Test
    void testLogicalOperatorPrecedence() {
        Environment env = new Environment();
        // 逻辑与优先级低于比较
        assertEquals("true", engine.execute("${1 < 2 && 2 < 3}", env));
    }
}
