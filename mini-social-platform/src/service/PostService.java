package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Post;
import model.User;

public class PostService {

    private final Map<String, List<Post>> posts;

    private final Map<String, User> users;

    private final FeedService feedService;

    public PostService(Map<String, List<Post>> posts,
            Map<String, User> users,
            FeedService feedService) {

        this.posts = posts;
        this.users = users;
        this.feedService = feedService;
    }

    public Post createPost(String userId, String content) {

        User user = users.get(userId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Post post = new Post(userId, content);

        posts.computeIfAbsent(userId, k -> new ArrayList<>())
                .add(post);

        feedService.invalidateFeed(userId);

        feedService.invalidateFollowersFeed(userId);

        return post;
    }

    public List<Post> getPostsByUser(String userId) {

        return posts.getOrDefault(userId, List.of());
    }

    public void deletePost(String userId, String postId) {

        List<Post> userPosts = posts.get(userId);

        if (userPosts == null) {
            return;
        }

        userPosts.removeIf(post -> post.getPostId().equals(postId));

        feedService.invalidateFeed(userId);

        feedService.invalidateFollowersFeed(userId);
    }
}