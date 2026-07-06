package tests;

import cache.LFUCache;

public class LFUCacheTest {

    public static void main(String[] args) {

        testPutAndGet();

        testFrequency();

        testInvalidate();

        testUpdate();

        System.out.println("All LFU Tests Passed.");
    }

    private static void testPutAndGet() {

        LFUCache<Integer, String> cache = new LFUCache<>(2);

        cache.put(1, "A");

        assert cache.get(1).equals("A");
    }

    private static void testFrequency() {

        LFUCache<Integer, String> cache = new LFUCache<>(2);

        cache.put(1, "A");

        cache.put(2, "B");

        cache.get(1);

        cache.get(1);

        cache.put(3, "C");

        assert cache.get(2) == null;

        assert cache.get(1).equals("A");

        assert cache.get(3).equals("C");
    }

    private static void testInvalidate() {

        LFUCache<Integer, String> cache = new LFUCache<>(2);

        cache.put(1, "A");

        cache.invalidate(1);

        assert cache.get(1) == null;
    }

    private static void testUpdate() {

        LFUCache<Integer, String> cache = new LFUCache<>(2);

        cache.put(1, "A");

        cache.put(1, "Updated");

        assert cache.get(1).equals("Updated");
    }
}