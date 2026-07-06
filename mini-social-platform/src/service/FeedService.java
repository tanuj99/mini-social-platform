package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import cache.Cache;
import model.Post;
import model.User;

public class FeedService {

    private final Cache<String, List<Post>> feedCache;

    private final Map<String, User> users;

    private final Map<String, List<Post>> posts;

    public FeedService(Cache<String, List<Post>> feedCache,
            Map<String, User> users,
            Map<String, List<Post>> posts) {

        this.feedCache = feedCache;
        this.users = users;
        this.posts = posts;
    }

    public List<Post> getFeed(String userId) {

        List<Post> cachedFeed = feedCache.get(userId);

        if (cachedFeed != null) {
            System.out.println("Cache Hit");
            return cachedFeed;
        }

        System.out.println("Cache Miss");

        List<Post> generatedFeed = generateFeed(userId);

        feedCache.put(userId, generatedFeed);

        return generatedFeed;
    }

    private List<Post> generateFeed(String userId) {

        User user = users.get(userId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<Post> feed = new ArrayList<>();

        for (String following : user.getFollowing()) {

            List<Post> userPosts = posts.get(following);

            if (userPosts != null) {
                feed.addAll(userPosts);
            }
        }

        feed.sort(Comparator.comparingLong(Post::getCreatedAt).reversed());

        return feed;
    }

    public void invalidateFeed(String userId) {

        feedCache.invalidate(userId);
    }

    public void invalidateFollowersFeed(String authorId) {

        User author = users.get(authorId);

        if (author == null) {
            return;
        }

        for (String follower : author.getFollowers()) {
            feedCache.invalidate(follower);
        }
    }
}