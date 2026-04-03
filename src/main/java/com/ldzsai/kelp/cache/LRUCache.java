package com.ldzsai.kelp.cache;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ldzsai.kelp.api.Cache;

/**
 * 线程安全的 LRU 缓存，支持可配置的最大容量。
 * 使用访问有序的 LinkedHashMap 实现淘汰策略。
 */
public class LRUCache<K, V> implements Cache<K, V> {
    private final int maxSize;
    private final Map<K, V> cache;

    public LRUCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Cache max size must be positive, got: " + maxSize);
        }
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<K, V>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.maxSize;
            }
        };
    }

    @Override
    public synchronized V get(K key) {
        return cache.get(key);
    }

    @Override
    public synchronized void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public synchronized void invalidate(K key) {
        cache.remove(key);
    }

    @Override
    public synchronized void clear() {
        cache.clear();
    }

    @Override
    public synchronized int size() {
        return cache.size();
    }

    @Override
    public synchronized boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    @Override
    public synchronized String toString() {
        return "LRUCache{size=" + cache.size() + ", maxSize=" + maxSize + "}";
    }
}
