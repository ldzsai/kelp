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
        
        Object[] testArray = {1, map};
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
        
        Object[] testArray = {1, map};
        env.setVariable("test", testArray);
        env.setVariable("str", StrTest.class);
        
        String input = "${str.subString(test[1]['a'], 0, 3)}";
        
        ExpressionEngine engine = new ExpressionEngine(env);
        Object result = engine.execute(input);
        
        assertEquals("kan", result);
    }
}