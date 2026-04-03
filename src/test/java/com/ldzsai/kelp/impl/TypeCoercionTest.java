package com.ldzsai.kelp.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TypeCoercionTest {

    @Test
    void testToBooleanWithNull() {
        assertFalse(TypeCoercion.toBoolean(null));
    }

    @Test
    void testToBooleanWithBoolean() {
        assertTrue(TypeCoercion.toBoolean(true));
        assertFalse(TypeCoercion.toBoolean(false));
    }

    @Test
    void testToBooleanWithNumber() {
        assertFalse(TypeCoercion.toBoolean(0));
        assertTrue(TypeCoercion.toBoolean(1));
        assertFalse(TypeCoercion.toBoolean(0.0));
        assertTrue(TypeCoercion.toBoolean(3.14));
        assertFalse(TypeCoercion.toBoolean(Double.NaN));
    }

    @Test
    void testToBooleanWithString() {
        assertFalse(TypeCoercion.toBoolean(""));
        assertTrue(TypeCoercion.toBoolean("hello"));
    }

    @Test
    void testToBooleanWithOtherType() {
        assertTrue(TypeCoercion.toBoolean(new Object()));
    }

    @Test
    void testToDoubleWithNumber() {
        assertEquals(3.14, TypeCoercion.toDouble(3.14), 0.001);
        assertEquals(42.0, TypeCoercion.toDouble(42), 0.001);
    }

    @Test
    void testToDoubleWithBoolean() {
        assertEquals(1.0, TypeCoercion.toDouble(true), 0.001);
        assertEquals(0.0, TypeCoercion.toDouble(false), 0.001);
    }

    @Test
    void testToDoubleWithString() {
        assertEquals(99.5, TypeCoercion.toDouble("99.5"), 0.001);
        assertEquals(42.0, TypeCoercion.toDouble("42"), 0.001);
    }

    @Test
    void testToDoubleWithNullThrows() {
        assertThrows(Exception.class, () -> TypeCoercion.toDouble(null));
    }

    @Test
    void testToDoubleWithInvalidStringThrows() {
        assertThrows(Exception.class, () -> TypeCoercion.toDouble("abc"));
    }

    @Test
    void testToInt() {
        assertEquals(3, TypeCoercion.toInt(3.7));
        assertEquals(42, TypeCoercion.toInt(42));
    }

    @Test
    void testEqualsWithNull() {
        assertTrue(TypeCoercion.equals(null, null));
        assertFalse(TypeCoercion.equals(null, "a"));
        assertFalse(TypeCoercion.equals("a", null));
    }

    @Test
    void testEqualsWithValue() {
        assertTrue(TypeCoercion.equals("a", "a"));
        assertFalse(TypeCoercion.equals("a", "b"));
        assertTrue(TypeCoercion.equals(42, 42));
        assertFalse(TypeCoercion.equals(42, 43));
    }
}
