package cache;

public interface Cache<K, V> {
    void put(K key, CacheElement<V> cacheElement);
    CacheElement<V> get(K key);
    int getHitCount();
    int getMissCount();
    void dispose();
}
