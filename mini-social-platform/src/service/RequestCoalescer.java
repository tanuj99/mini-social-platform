package service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class RequestCoalescer<T> {

    private final ConcurrentHashMap<String, CompletableFuture<T>> inFlightRequests = new ConcurrentHashMap<>();

    public T execute(String key, Supplier<T> supplier) {

        CompletableFuture<T> future = inFlightRequests.computeIfAbsent(key, k -> {
            return CompletableFuture.supplyAsync(supplier)
                    .whenComplete((result, throwable) -> {
                        // Remove the key from the map once the future is complete
                        inFlightRequests.remove(k);
                    });
        });

        return future.join();
    }
}