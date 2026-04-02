package com.ldzsai.kelp.token;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    void testTokenEquality() {
        Token t1 = new Token(TokenType.INTEGER, 42);
        Token t2 = new Token(TokenType.INTEGER, 42);
        Token t3 = new Token(TokenType.FLOAT, 42.0);

        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testTokenDifferentType() {
        Token t1 = new Token(TokenType.INTEGER, 42);
        Token t2 = new Token(TokenType.STRING, "42");

        assertNotEquals(t1, t2);
    }

    @Test
    void testTokenDifferentValue() {
        Token t1 = new Token(TokenType.INTEGER, 42);
        Token t2 = new Token(TokenType.INTEGER, 99);

        assertNotEquals(t1, t2);
    }

    @Test
    void testTokenToString() {
        Token token = new Token(TokenType.INTEGER, 42);
        String str = token.toString();

        assertTrue(str.contains("INTEGER"));
        assertTrue(str.contains("42"));
    }

    @Test
    void testTokenGetters() {
        Token token = new Token(TokenType.FLOAT, 3.14);

        assertEquals(TokenType.FLOAT, token.getType());
        assertEquals(3.14, token.getValue());
    }

    @Test
    void testTokenNullValue() {
        Token token = new Token(TokenType.EOF, null);

        assertEquals(TokenType.EOF, token.getType());
        assertNull(token.getValue());
    }

    @Test
    void testTokenEqualsNull() {
        Token token = new Token(TokenType.INTEGER, 42);
        assertNotEquals(null, token);
    }

    @Test
    void testTokenEqualsSameInstance() {
        Token token = new Token(TokenType.INTEGER, 42);
        assertEquals(token, token);
    }
}
