package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class User {

    private final String userId;
    private final String username;

    private final Set<String> followers;
    private final Set<String> following;

    public User(String userId, String username) {

        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("Invalid userId");
        }

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Invalid username");
        }

        this.userId = userId;
        this.username = username;

        this.followers = new HashSet<>();
        this.following = new HashSet<>();
    }

    public void addFollower(String followerId) {
        followers.add(followerId);
    }

    public void follow(String userId) {
        following.add(userId);
    }

    public void removeFollower(String followerId) {
        followers.remove(followerId);
    }

    public void unfollow(String userId) {
        following.remove(userId);
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getFollowers() {
        return Collections.unmodifiableSet(followers);
    }

    public Set<String> getFollowing() {
        return Collections.unmodifiableSet(following);
    }
}