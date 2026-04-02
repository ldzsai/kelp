package com.ldzsai.kelp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.ldzsai.kelp.expression.Environment;

class BitwiseOperatorTest {

    private final KelpEngine engine = KelpEngine.create();

    @Test
    void testBitwiseAnd() {
        Environment env = new Environment();
        env.setVariable("a", 5);
        env.setVariable("b", 3);
        assertEquals("1.0", engine.execute("${a & b}", env));
    }

    @Test
    void testBitwiseOr() {
        Environment env = new Environment();
        env.setVariable("a", 5);
        env.setVariable("b", 3);
        assertEquals("7.0", engine.execute("${a | b}", env));
    }

    @Test
    void testBitwiseXor() {
        Environment env = new Environment();
        env.setVariable("a", 5);
        env.setVariable("b", 3);
        assertEquals("6.0", engine.execute("${a ^ b}", env));
    }

    @Test
    void testLeftShift() {
        Environment env = new Environment();
        env.setVariable("a", 5);
        env.setVariable("b", 3);
        assertEquals("40.0", engine.execute("${a << b}", env));
    }

    @Test
    void testRightShift() {
        Environment env = new Environment();
        assertEquals("5.0", engine.execute("${10 >> 1}", env));
    }

    @Test
    void testUnsignedRightShift() {
        Environment env = new Environment();
        env.setVariable("a", -1);
        String result = engine.execute("${a >>> 1}", env);
        assertEquals("2.147483647E9", result);
    }

    @Test
    void testBitwiseNot() {
        Environment env = new Environment();
        env.setVariable("a", 5);
        String result = engine.execute("${~a}", env);
        assertEquals("-6.0", result);
    }

    @Test
    void testBitwiseOperatorPrecedence() {
        Environment env = new Environment();
        // 位与优先级高于位异或
        assertEquals("4.0", engine.execute("${5 ^ 1 & 7}", env)); // 5 ^ (1 & 7) = 5 ^ 1 = 4
    }
}
