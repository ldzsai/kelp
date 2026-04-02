package com.ldzsai.kelp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.ldzsai.kelp.expression.Environment;

class ArithmeticOperatorTest {

    private final KelpEngine engine = KelpEngine.create();

    @Test
    void testAddition() {
        Environment env = new Environment();
        assertEquals("4.0", engine.execute("${1 + 3}", env));
    }

    @Test
    void testSubtraction() {
        Environment env = new Environment();
        assertEquals("2.0", engine.execute("${5 - 3}", env));
    }

    @Test
    void testMultiplication() {
        Environment env = new Environment();
        assertEquals("15.0", engine.execute("${3 * 5}", env));
    }

    @Test
    void testDivision() {
        Environment env = new Environment();
        assertEquals("2.5", engine.execute("${10 / 4}", env));
    }

    @Test
    void testModulo() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 3);
        assertEquals("1.0", engine.execute("${a % b}", env));
    }

    @Test
    void testPower() {
        Environment env = new Environment();
        env.setVariable("a", 2);
        env.setVariable("b", 3);
        assertEquals("8.0", engine.execute("${a ** b}", env));
    }

    @Test
    void testIntegerDivide() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 3);
        assertEquals("3.0", engine.execute("${a // b}", env));
    }

    @Test
    void testOperatorPrecedence() {
        Environment env = new Environment();
        assertEquals("7.0", engine.execute("${1 + 2 * 3}", env));
    }

    @Test
    void testPowerPrecedence() {
        Environment env = new Environment();
        assertEquals("18.0", engine.execute("${2 * 3 ** 2}", env));
    }

    @Test
    void testComplexArithmetic() {
        Environment env = new Environment();
        assertEquals("8.0", engine.execute("${2 ** 3 + 10 / 2 - 5}", env));
    }

    @Test
    void testMixedArithmeticWithVariables() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);
        assertEquals("205.0", engine.execute("${a * b + 5}", env));
    }

    @Test
    void testDivisionByZero() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 0);
        assertThrows(Exception.class, () -> engine.execute("${a / b}", env));
    }

    @Test
    void testIntegerDivisionByZero() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 0);
        assertThrows(Exception.class, () -> engine.execute("${a // b}", env));
    }

    @Test
    void testModuloByZero() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 0);
        assertThrows(Exception.class, () -> engine.execute("${a % b}", env));
    }

    @Test
    void testUnaryMinus() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        assertEquals("-10.0", engine.execute("${-a}", env));
    }

    @Test
    void testDoubleUnaryMinus() {
        Environment env = new Environment();
        env.setVariable("a", 10);
        assertEquals("10.0", engine.execute("${--a}", env));
    }
}
