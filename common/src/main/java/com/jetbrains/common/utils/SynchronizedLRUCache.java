package com.jetbrains.common.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SynchronizedLRUCache<K, V> {
    private final int capacity;
    private final Map<K,V> cache;

    public SynchronizedLRUCache(final int capacity) {
        this.capacity = capacity;
        cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    public V get(K key) {
        return cache.get(key);
    }

    public V put(K key, V value) {
        return cache.put(key, value);
    }

    public int getCapacity() {
        return capacity;
    }
}
