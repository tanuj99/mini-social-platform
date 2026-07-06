package cache;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V> {

    private final int capacity;

    private final Map<K, Node<K, V>> cache;

    private final Node<K, V> head;
    private final Node<K, V> tail;

    public LRUCache(int capacity) {

        this.capacity = capacity;
        this.cache = new HashMap<>();

        head = new Node<>(null, null);
        tail = new Node<>(null, null);

        head.next = tail;
        tail.prev = head;
    }

    @Override
    public V get(K key) {

        Node<K, V> node = cache.get(key);

        if (node == null) {
            return null;
        }

        moveToFront(node);

        return node.value;
    }

    @Override
    public void put(K key, V value) {

        Node<K, V> existing = cache.get(key);

        if (existing != null) {

            existing.value = value;

            moveToFront(existing);

            return;
        }

        Node<K, V> node = new Node<>(key, value);

        cache.put(key, node);

        addFirst(node);

        if (cache.size() > capacity) {

            Node<K, V> lru = removeLast();

            cache.remove(lru.key);
        }
    }

    @Override
    public void invalidate(K key) {

        Node<K, V> node = cache.remove(key);

        if (node != null) {
            remove(node);
        }
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    @Override
    public void clear() {

        cache.clear();

        head.next = tail;
        tail.prev = head;
    }

    private void moveToFront(Node<K, V> node) {

        remove(node);

        addFirst(node);
    }

    private void addFirst(Node<K, V> node) {

        node.next = head.next;
        node.prev = head;

        head.next.prev = node;

        head.next = node;
    }

    private void remove(Node<K, V> node) {

        node.prev.next = node.next;

        node.next.prev = node.prev;
    }

    private Node<K, V> removeLast() {

        Node<K, V> node = tail.prev;

        remove(node);

        return node;
    }

    public void printCache() {

        Node<K, V> current = head.next;

        while (current != tail) {

            System.out.print(current.key + " ");

            current = current.next;
        }

        System.out.println();
    }
}