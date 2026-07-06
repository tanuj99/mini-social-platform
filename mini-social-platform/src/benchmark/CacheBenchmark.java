package benchmark;

import java.util.Random;

import cache.ICache;
import cache.LFUCache;
import cache.LRUCache;

public class CacheBenchmark {

    private static final int CACHE_SIZE = 100;

    private static final int TOTAL_KEYS = 1000;

    private static final int REQUESTS = 100000;

    public static void main(String[] args) {

        System.out.println("========== CACHE BENCHMARK ==========\n");

        benchmark(new LRUCache<>(CACHE_SIZE), "LRU");

        benchmark(new LFUCache<>(CACHE_SIZE), "LFU");
    }

    private static void benchmark(ICache<Integer, Integer> cache,
            String name) {

        Random random = new Random();

        int hits = 0;
        int misses = 0;

        for (int i = 0; i < REQUESTS; i++) {

            int key;

            /*
             * 80% requests go to first 20% keys
             */
            if (random.nextInt(100) < 80) {

                key = random.nextInt(TOTAL_KEYS / 5);

            } else {

                key = random.nextInt(TOTAL_KEYS);
            }

            Integer value = cache.get(key);

            if (value != null) {

                hits++;

            } else {

                misses++;

                cache.put(key, key);
            }
        }

        double hitRatio = (hits * 100.0) / REQUESTS;

        System.out.println("Cache : " + name);

        System.out.println("Hits : " + hits);

        System.out.println("Misses : " + misses);

        System.out.printf("Hit Ratio : %.2f%%\n", hitRatio);

        System.out.println();
    }
}