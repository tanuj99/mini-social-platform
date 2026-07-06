package cache;

public interface Cache<K, V> {

    V get(K key);

    void put(K key, V value);

    void invalidate(K key);

    int size();

    boolean containsKey(K key);

    void clear();
}
