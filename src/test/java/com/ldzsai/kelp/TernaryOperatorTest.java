package com.ldzsai.kelp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.ldzsai.kelp.expression.Environment;

class TernaryOperatorTest {

    private final KelpEngine engine = KelpEngine.create();

    @Test
    void testTernaryTrueBranch() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);
        assertEquals("yes", engine.execute("${a < b ? 'yes' : 'no'}", env));
    }

    @Test
    void testTernaryFalseBranch() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);
        assertEquals("no", engine.execute("${a > b ? 'yes' : 'no'}", env));
    }

    @Test
    void testNestedTernary() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);
        env.setVariable("c", 5);
        assertEquals("b", engine.execute("${a > b ? (a > c ? 'a' : 'c') : (b > c ? 'b' : 'c')}", env));
    }

    @Test
    void testTernaryWithArithmetic() {
        Environment env = new Environment();
        env.setVariable("x", 5);
        assertEquals("10.0", engine.execute("${x > 0 ? x * 2 : -x}", env));
    }

    @Test
    void testTernaryWithNumericResult() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);
        assertEquals("20", engine.execute("${a < b ? b : a}", env));
    }
}
