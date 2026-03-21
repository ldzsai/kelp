package com.ldzsai.kelp;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.ldzsai.kelp.expression.Environment;
import com.ldzsai.kelp.ExpressionEngine;

public class ExpressionEngineTest {

    static class StrTest {
        public static String subString(String str, Integer start, Integer end) {
            return str.substring(start, end);
        }
    }

    @Test
    void testSimpleArithmetic() throws Exception {
        Environment env = new Environment();
        String input = "${1+1+1*2}";

        ExpressionEngine engine = new ExpressionEngine(env);
        Object result = engine.execute(input);

        assertEquals("4.0", result);
    }

    @Test
    void testObjectAccess() throws Exception {
        Environment env = new Environment();
        Map<String, Object> obj = new HashMap<>();
        obj.put("a", "kangert");
        env.setVariable("obj", obj);

        String input = "${obj.a}";

        ExpressionEngine engine = new ExpressionEngine(env);
        Object result = engine.execute(input);

        assertEquals("kangert", result);
    }

    @Test
    void testFunctionCall() throws Exception {
        Environment env = new Environment();
        Map<String, Object> obj = new HashMap<>();
        obj.put("a", "kangert");
        env.setVariable("obj", obj);
        env.setVariable("str", StrTest.class);

        String input = "${str.subString(obj.a, 0, 5)}rt";

        ExpressionEngine engine = new ExpressionEngine(env);
        Object result = engine.execute(input);

        assertEquals("kangert", result);
    }

    @Test
    void testArrayAccess() throws Exception {
        Environment env = new Environment();
        Map<String, Object> map = new HashMap<>();
        map.put("a", "kangert");

        Object[] testArray = { 1, map };
        env.setVariable("test", testArray);

        String input = "${test[1]['a']}";

        ExpressionEngine engine = new ExpressionEngine(env);
        Object result = engine.execute(input);

        assertEquals("kangert", result);
    }

    @Test
    void testMixedExpression() throws Exception {
        Environment env = new Environment();
        Map<String, Object> map = new HashMap<>();
        map.put("a", "kangert");

        Object[] testArray = { 1, map };
        env.setVariable("test", testArray);
        env.setVariable("str", StrTest.class);

        String input = "${str.subString(test[1]['a'], 0, 3)}";

        ExpressionEngine engine = new ExpressionEngine(env);
        Object result = engine.execute(input);

        assertEquals("kan", result);
    }

    // ==================== 新增测试用例 ====================

    @Test
    void testModulo() throws Exception {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 3);

        ExpressionEngine engine = new ExpressionEngine(env);

        assertEquals("1.0", engine.execute("${a % b}"));
    }

    @Test
    void testPower() throws Exception {
        Environment env = new Environment();
        env.setVariable("a", 2);
        env.setVariable("b", 3);

        ExpressionEngine engine = new ExpressionEngine(env);

        assertEquals("8.0", engine.execute("${a ** b}"));
    }

    @Test
    void testIntegerDivide() throws Exception {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 3);

        ExpressionEngine engine = new ExpressionEngine(env);

        assertEquals("3.0", engine.execute("${a // b}"));
    }

    @Test
    void testComparisonOperators() throws Exception {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);

        ExpressionEngine engine = new ExpressionEngine(env);

        assertEquals("true", engine.execute("${a < b}"));
        assertEquals("false", engine.execute("${a > b}"));
        assertEquals("true", engine.execute("${a <= b}"));
        assertEquals("false", engine.execute("${a >= b}"));
        assertEquals("false", engine.execute("${a == b}"));
        assertEquals("true", engine.execute("${a != b}"));
    }

    @Test
    void testLogicalOperators() throws Exception {
        Environment env = new Environment();
        env.setVariable("a", 1);
        env.setVariable("b", 0);

        ExpressionEngine engine = new ExpressionEngine(env);

        // 逻辑与 - 两边都为true(非0)才为true
        assertEquals("true", engine.execute("${a && a}"));
        assertEquals("false", engine.execute("${a && b}"));

        // 逻辑或 - 任一边为true(非0)就为true
        assertEquals("true", engine.execute("${a || b}"));
        assertEquals("false", engine.execute("${b || b}"));

        // 逻辑非
        assertEquals("false", engine.execute("${!a}"));
        assertEquals("true", engine.execute("${!b}"));
    }

    @Test
    void testUnaryMinus() throws Exception {
        Environment env = new Environment();
        env.setVariable("a", 10);

        ExpressionEngine engine = new ExpressionEngine(env);

        assertEquals("-10.0", engine.execute("${-a}"));
        assertEquals("10.0", engine.execute("${--a}")); // 双重负号
    }

    @Test
    void testBitwiseOperators() throws Exception {
        Environment env = new Environment();
        env.setVariable("a", 5); // 0101
        env.setVariable("b", 3); // 0011

        ExpressionEngine engine = new ExpressionEngine(env);

        // 位与 0101 & 0011 = 0001 = 1
        assertEquals("1.0", engine.execute("${a & b}"));

        // 位或 0101 | 0011 = 0111 = 7
        assertEquals("7.0", engine.execute("${a | b}"));

        // 位异或 0101 ^ 0011 = 0110 = 6
        assertEquals("6.0", engine.execute("${a ^ b}"));

        // 左移 5 << 3 = 40
        assertEquals("40.0", engine.execute("${a << b}"));

        // 右移 10 >> 1 = 5
        assertEquals("5.0", engine.execute("${10 >> 1}"));
    }

    @Test
    void testTernaryOperator() throws Exception {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);

        ExpressionEngine engine = new ExpressionEngine(env);

        // 条件为true
        assertEquals("yes", engine.execute("${a < b ? 'yes' : 'no'}"));

        // 条件为false
        assertEquals("no", engine.execute("${a > b ? 'yes' : 'no'}"));
    }

    @Test
    void testComplexExpression() throws Exception {
        Environment env = new Environment();
        env.setVariable("a", 10);
        env.setVariable("b", 20);
        env.setVariable("c", 5);

        ExpressionEngine engine = new ExpressionEngine(env);

        // 复杂表达式：先计算幂运算，然后乘除，最后加减
        // 2 ** 3 + 10 / 2 - 5 = 8 + 5 - 5 = 8
        assertEquals("8.0", engine.execute("${2 ** 3 + 10 / 2 - 5}"));

        // 比较和逻辑组合
        // a < b && b > c = 10 < 20 && 20 > 5 = true && true = true
        assertEquals("true", engine.execute("${a < b && b > c}"));

        // 三元表达式嵌套
        // a > b ? (a > c ? 'a' : 'c') : (b > c ? 'b' : 'c')
        // 10 > 20 ? ... : (20 > 5 ? 'b' : 'c') = 'b'
        assertEquals("b", engine.execute("${a > b ? (a > c ? 'a' : 'c') : (b > c ? 'b' : 'c')}"));
    }

    @Test
    void testOperatorPrecedence() throws Exception {
        Environment env = new Environment();

        ExpressionEngine engine = new ExpressionEngine(env);

        // 幂运算优先级最高
        assertEquals("18.0", engine.execute("${2 * 3 ** 2}")); // 2 * 9 = 18

        // 乘除模优先级高于加减
        assertEquals("7.0", engine.execute("${1 + 2 * 3}")); // 1 + 6 = 7

        // 比较运算符优先级低于算术
        assertEquals("true", engine.execute("${1 + 2 == 3}")); // 3 == 3 = true

        // 逻辑与优先级低于比较
        assertEquals("true", engine.execute("${1 < 2 && 2 < 3}")); // true && true = true
    }

    @Test
    void testStringComparison() throws Exception {
        Environment env = new Environment();
        env.setVariable("s1", "hello");
        env.setVariable("s2", "hello");
        env.setVariable("s3", "world");

        ExpressionEngine engine = new ExpressionEngine(env);

        assertEquals("true", engine.execute("${s1 == s2}"));
        assertEquals("false", engine.execute("${s1 == s3}"));
        assertEquals("true", engine.execute("${s1 != s3}"));
    }
}
