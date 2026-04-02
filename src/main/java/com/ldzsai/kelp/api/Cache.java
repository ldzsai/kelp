package com.ldzsai.kelp.api;

/**
 * 可插拔的缓存策略接口。
 * 支持 LRU、基于 TTL、有界大小或空操作缓存等实现方式。
 */
public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    void invalidate(K key);
    void clear();
    int size();
    boolean containsKey(K key);
}
