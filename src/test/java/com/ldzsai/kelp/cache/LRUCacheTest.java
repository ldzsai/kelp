package com.ldzsai.kelp.cache;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    void testBasicPutAndGet() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");

        assertEquals(3, cache.size());
        assertEquals("1", cache.get("a"));
        assertEquals("2", cache.get("b"));
        assertEquals("3", cache.get("c"));
    }

    @Test
    void testContainsKey() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("a", "1");

        assertTrue(cache.containsKey("a"));
        assertFalse(cache.containsKey("b"));
    }

    @Test
    void testEviction() {
        LRUCache<String, String> cache = new LRUCache<>(2);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");

        assertEquals(2, cache.size());
        assertNull(cache.get("a"));
        assertEquals("2", cache.get("b"));
        assertEquals("3", cache.get("c"));
    }

    @Test
    void testAccessOrderEviction() {
        LRUCache<String, String> cache = new LRUCache<>(2);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.get("a");
        cache.put("c", "3");

        assertEquals("1", cache.get("a"));
        assertNull(cache.get("b"));
        assertEquals("3", cache.get("c"));
    }

    @Test
    void testInvalidate() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("a", "1");
        cache.invalidate("a");

        assertNull(cache.get("a"));
        assertEquals(0, cache.size());
    }

    @Test
    void testClear() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.clear();

        assertEquals(0, cache.size());
    }

    @Test
    void testOverwriteExistingKey() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("a", "1");
        cache.put("a", "2");

        assertEquals(1, cache.size());
        assertEquals("2", cache.get("a"));
    }

    @Test
    void testInvalidMaxSizeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new LRUCache<>(0));
        assertThrows(IllegalArgumentException.class, () -> new LRUCache<>(-1));
    }

    @Test
    void testToString() {
        LRUCache<String, String> cache = new LRUCache<>(5);
        cache.put("a", "1");
        assertTrue(cache.toString().contains("size=1"));
        assertTrue(cache.toString().contains("maxSize=5"));
    }
}
