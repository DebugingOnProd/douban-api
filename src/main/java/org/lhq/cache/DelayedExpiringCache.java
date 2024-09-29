package org.lhq.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedExpiringCache<K,V> {
    private final Map<K, CacheEntry<V>> cache;
    private final DelayQueue<CacheEntry<V>> queue;

    public DelayedExpiringCache() {
        this.cache = new ConcurrentHashMap<>();
        this.queue = new DelayQueue<>();
    }

    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null && !entry.hasExpired()) {
            return entry.getValue();
        }
        return null;
    }
    public void put(K key, V value, long delay, TimeUnit unit) {
        CacheEntry<V> entry = new CacheEntry<>(key, value, unit.toMillis(delay));
        cache.put(key, entry);
        queue.put(entry);
    }

    public void remove(K key) {
        CacheEntry<V> entry = cache.remove(key);
        if (entry != null) {
            entry.expireNow(); // Mark it as expired so it will be removed from the queue.
        }
    }

    public void clear() {
        cache.clear();
        queue.clear();
    }


    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public boolean hasExpired(K key) {
        boolean hasKey = cache.containsKey(key);
        if (!hasKey) {
            return false;
        }
        return cache.get(key).hasExpired();
    }

    public int size() {
        return cache.size();
    }

    private class CacheEntry<V> implements Delayed {

       private final K key;
       private final V value;
       private final long expireTime;
       private volatile boolean expired = false;

       private CacheEntry(K key, V value, long delay) {
           this.key = key;
           this.value = value;
           this.expireTime = System.currentTimeMillis() + delay;
       }


        public void expireNow() {
            this.expired = true;
        }

        public V getValue() {
            return value;
        }

        public boolean hasExpired() {
            return expired || System.currentTimeMillis() >= expireTime;
        }
       @Override
       public long getDelay(TimeUnit unit) {
           return unit.convert(expireTime - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
       }

       @Override
       public int compareTo(Delayed o) {
           if (this == o) {
               return 0;
           }
           CacheEntry<?> that = (CacheEntry<?>) o;
           long diff = this.expireTime - that.expireTime;
           return (diff == 0) ? 0 : ((diff > 0) ? 1 : -1);
       }
   }
}
