package com.ldzsai.kelp.expression;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnvironmentTest {

    @Test
    void testSetAndGetVariable() {
        Environment env = new Environment();
        env.setVariable("name", "kelp");

        assertEquals("kelp", env.getVariable("name"));
    }

    @Test
    void testUndefinedVariableReturnsNull() {
        Environment env = new Environment();
        assertNull(env.getVariable("undefined"));
    }

    @Test
    void testSetVariableToNull() {
        Environment env = new Environment();
        env.setVariable("key", null);

        assertTrue(env.hasVariable("key"));
        assertNull(env.getVariable("key"));
    }

    @Test
    void testHasVariable() {
        Environment env = new Environment();
        env.setVariable("key", "value");

        assertTrue(env.hasVariable("key"));
        assertFalse(env.hasVariable("missing"));
    }

    @Test
    void testRemoveVariable() {
        Environment env = new Environment();
        env.setVariable("key", "value");
        env.removeVariable("key");

        assertFalse(env.hasVariable("key"));
        assertNull(env.getVariable("key"));
    }

    @Test
    void testChildInheritsParentVariables() {
        Environment parent = new Environment();
        parent.setVariable("global", "parent_value");

        Environment child = parent.createChild();
        assertEquals("parent_value", child.getVariable("global"));
    }

    @Test
    void testChildDoesNotAffectParent() {
        Environment parent = new Environment();

        Environment child = parent.createChild();
        child.setVariable("local", "child_value");

        assertNull(parent.getVariable("local"));
        assertEquals("child_value", child.getVariable("local"));
    }

    @Test
    void testChildOverridesParentVariable() {
        Environment parent = new Environment();
        parent.setVariable("x", 1);

        Environment child = parent.createChild();
        child.setVariable("x", 2);

        assertEquals(1, parent.getVariable("x"));
        assertEquals(2, child.getVariable("x"));
    }

    @Test
    void testGetParent() {
        Environment parent = new Environment();
        Environment child = parent.createChild();

        assertNull(parent.getParent());
        assertSame(parent, child.getParent());
    }

    @Test
    void testToMap() {
        Environment env = new Environment();
        env.setVariable("a", 1);
        env.setVariable("b", "hello");

        java.util.Map<String, Object> map = env.toMap();
        assertEquals(2, map.size());
        assertEquals(1, map.get("a"));
        assertEquals("hello", map.get("b"));
    }

    @Test
    void testToMapIsUnmodifiable() {
        Environment env = new Environment();
        env.setVariable("a", 1);

        java.util.Map<String, Object> map = env.toMap();
        assertThrows(UnsupportedOperationException.class, () -> map.put("b", 2));
    }
}
