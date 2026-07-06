import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cache.ICache;
import cache.LRUCache;
import model.Post;
import model.User;
import service.FeedService;
import service.PostService;
import service.RequestCoalescer;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        /*
         * In-memory Database
         */
        Map<String, User> users = new HashMap<>();
        Map<String, List<Post>> posts = new HashMap<>();

        /*
         * Cache
         */
        ICache<String, List<Post>> cache = new LRUCache<>(100);

        /*
         * Request Coalescer
         */
        RequestCoalescer<List<Post>> requestCoalescer = new RequestCoalescer<>();

        /*
         * Services
         */
        FeedService feedService = new FeedService(
                cache,
                users,
                posts,
                requestCoalescer);

        PostService postService = new PostService(
                posts,
                users,
                feedService);

        /*
         * Create Users
         */

        User alice = new User("1", "Alice");
        User bob = new User("2", "Bob");
        User charlie = new User("3", "Charlie");

        users.put(alice.getUserId(), alice);
        users.put(bob.getUserId(), bob);
        users.put(charlie.getUserId(), charlie);

        /*
         * Alice follows Bob
         */

        alice.follow(bob.getUserId());
        bob.addFollower(alice.getUserId());

        /*
         * Alice follows Charlie
         */

        alice.follow(charlie.getUserId());
        charlie.addFollower(alice.getUserId());

        /*
         * Create Posts
         */

        postService.createPost(
                bob.getUserId(),
                "Hello from Bob!");

        postService.createPost(
                charlie.getUserId(),
                "Hello from Charlie!");

        System.out.println("\n==============================");
        System.out.println("FIRST FEED REQUEST");
        System.out.println("==============================");

        List<Post> feed = feedService.getFeed(alice.getUserId());

        feed.forEach(System.out::println);

        System.out.println("\n==============================");
        System.out.println("SECOND FEED REQUEST");
        System.out.println("==============================");

        feed = feedService.getFeed(alice.getUserId());

        feed.forEach(System.out::println);

        System.out.println("\n==============================");
        System.out.println("BOB CREATES NEW POST");
        System.out.println("==============================");

        postService.createPost(
                bob.getUserId(),
                "Bob's Second Post!");

        System.out.println("\n==============================");
        System.out.println("AFTER CACHE INVALIDATION");
        System.out.println("==============================");

        feed = feedService.getFeed(alice.getUserId());

        feed.forEach(System.out::println);

        /*
         * Demonstrate Request Coalescing
         */

        System.out.println("\n==============================");
        System.out.println("REQUEST COALESCING");
        System.out.println("==============================");

        cache.invalidate(alice.getUserId());

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 10; i++) {

            int threadId = i;

            executor.submit(() -> {

                List<Post> result = feedService.getFeed(alice.getUserId());

                System.out.println(
                        "Thread "
                                + threadId
                                + " received "
                                + result.size()
                                + " posts");
            });
        }

        executor.shutdown();

        executor.awaitTermination(
                5,
                TimeUnit.SECONDS);

        System.out.println("\n==============================");
        System.out.println("APPLICATION FINISHED");
        System.out.println("==============================");
    }
}