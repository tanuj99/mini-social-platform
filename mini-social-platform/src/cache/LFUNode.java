package cache;

public class LFUNode<K, V> {

    K key;
    V value;

    int frequency;

    LFUNode<K, V> prev;
    LFUNode<K, V> next;

    public LFUNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.frequency = 1;
    }
}