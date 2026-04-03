package com.ldzsai.kelp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.ldzsai.kelp.config.EngineConfiguration;
import com.ldzsai.kelp.exception.KelpException;
import com.ldzsai.kelp.expression.Environment;

class ErrorHandlingTest {

    @Test
    void testNullExpressionThrows() {
        KelpEngine engine = KelpEngine.create();
        Environment env = new Environment();
        assertThrows(KelpException.class, () -> engine.execute(null, env));
    }

    @Test
    void testEmptyExpressionThrows() {
        KelpEngine engine = KelpEngine.create();
        Environment env = new Environment();
        assertThrows(KelpException.class, () -> engine.execute("", env));
    }

    @Test
    void testExpressionTooLongThrows() {
        KelpEngine engine = KelpEngine.builder()
            .maxExpressionLength(10)
            .build();

        Environment env = new Environment();
        assertThrows(KelpException.class, () ->
            engine.execute("${" + "a".repeat(20) + "}", env));
    }

    @Test
    void testInvalidCharacterThrows() {
        KelpEngine engine = KelpEngine.create();
        Environment env = new Environment();
        assertThrows(Exception.class, () -> engine.execute("${@}", env));
    }

    @Test
    void testUnterminatedStringThrows() {
        KelpEngine engine = KelpEngine.create();
        Environment env = new Environment();
        assertThrows(Exception.class, () -> engine.execute("${'hello}", env));
    }

    @Test
    void testMissingOperandThrows() {
        KelpEngine engine = KelpEngine.create();
        Environment env = new Environment();
        assertThrows(Exception.class, () -> engine.execute("${1 +}", env));
    }

    @Test
    void testKelpExceptionHasStage() {
        try {
            KelpEngine engine = KelpEngine.create();
            engine.execute(null, new Environment());
            fail("Expected KelpException");
        } catch (KelpException e) {
            assertNotNull(e.getStage());
        }
    }

    @Test
    void testKelpExceptionMessage() {
        try {
            KelpEngine engine = KelpEngine.create();
            engine.execute(null, new Environment());
            fail("Expected KelpException");
        } catch (KelpException e) {
            assertNotNull(e.getMessage());
            assertFalse(e.getMessage().isEmpty());
        }
    }
}
