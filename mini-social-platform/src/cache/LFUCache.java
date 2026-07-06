package cache;

import java.util.HashMap;
import java.util.Map;

public class LFUCache<K, V> implements Cache<K, V> {

    private final int capacity;

    private int minFrequency;

    private final Map<K, LFUNode<K, V>> cache;

    private final Map<Integer, DoublyLinkedList<K, V>> frequencyMap;

    public LFUCache(int capacity) {

        this.capacity = capacity;

        this.cache = new HashMap<>();

        this.frequencyMap = new HashMap<>();
    }

    @Override
    public V get(K key) {

        LFUNode<K, V> node = cache.get(key);

        if (node == null) {
            return null;
        }

        updateFrequency(node);

        return node.value;
    }

    @Override
    public void put(K key, V value) {

        if (capacity == 0) {
            return;
        }

        LFUNode<K, V> existing = cache.get(key);

        if (existing != null) {

            existing.value = value;

            updateFrequency(existing);

            return;
        }

        if (cache.size() == capacity) {

            DoublyLinkedList<K, V> list = frequencyMap.get(minFrequency);

            LFUNode<K, V> victim = list.removeLast();

            cache.remove(victim.key);
        }

        LFUNode<K, V> node = new LFUNode<>(key, value);

        cache.put(key, node);

        minFrequency = 1;

        frequencyMap.computeIfAbsent(1, k -> new DoublyLinkedList<>())
                    .addFirst(node);
    }

    private void updateFrequency(LFUNode<K, V> node) {

        int frequency = node.frequency;

        DoublyLinkedList<K, V> current = frequencyMap.get(frequency);

        current.remove(node);

        if (frequency == minFrequency && current.isEmpty()) {
            minFrequency++;
        }

        node.frequency++;

        frequencyMap.computeIfAbsent(node.frequency, k -> new DoublyLinkedList<>())
                    .addFirst(node);
    }

    @Override
    public void invalidate(K key) {

        LFUNode<K, V> node = cache.remove(key);

        if (node == null) {
            return;
        }

        frequencyMap
                .get(node.frequency)
                .remove(node);
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

        frequencyMap.clear();

        minFrequency = 0;
    }

    public void printCache() {

        System.out.println();

        for (Integer frequency : frequencyMap.keySet()) {

            System.out.print("Frequency " + frequency + " : ");

            DoublyLinkedList<K, V> list = frequencyMap.get(frequency);

            LFUNode<K, V> current = list.removeLast();

            while (current != null) {

                System.out.print(current.key + " ");

                current = list.removeLast();
            }

            System.out.println();
        }
    }
}