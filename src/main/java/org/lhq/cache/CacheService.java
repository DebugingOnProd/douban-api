package org.lhq.cache;

import jakarta.enterprise.context.Dependent;

import java.util.concurrent.TimeUnit;

@Dependent
public class CacheService<K, V> {
    private final DelayedExpiringCache<K, V> cache;

    public CacheService() {
        cache = new DelayedExpiringCache<>();
    }

    public void put(K key, V value, long delay, TimeUnit unit) {
        cache.put(key, value, delay, unit);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public boolean containsKeyAndNotExpired (K key) {
        boolean containsKey = cache.containsKey(key);
        boolean hasExpired = cache.hasExpired(key);
        return containsKey && !hasExpired;
    }

    public int size() {
        return cache.size();
    }

    public void clear() {
        cache.clear();
    }
}
