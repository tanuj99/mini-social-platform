package tests;

import cache.LRUCache;

public class LRUCacheTest {

    public static void main(String[] args) {

        testPutAndGet();

        testEviction();

        testInvalidate();

        testUpdate();

        System.out.println("All LRU Tests Passed.");
    }

    private static void testPutAndGet() {

        LRUCache<Integer, String> cache = new LRUCache<>(2);

        cache.put(1, "A");

        assert cache.get(1).equals("A");
    }

    private static void testEviction() {

        LRUCache<Integer, String> cache = new LRUCache<>(2);

        cache.put(1, "A");

        cache.put(2, "B");

        cache.get(1);

        cache.put(3, "C");

        assert cache.get(2) == null;

        assert cache.get(1).equals("A");

        assert cache.get(3).equals("C");
    }

    private static void testInvalidate() {

        LRUCache<Integer, String> cache = new LRUCache<>(2);

        cache.put(1, "A");

        cache.invalidate(1);

        assert cache.get(1) == null;
    }

    private static void testUpdate() {

        LRUCache<Integer, String> cache = new LRUCache<>(2);

        cache.put(1, "A");

        cache.put(1, "Updated");

        assert cache.get(1).equals("Updated");
    }
}