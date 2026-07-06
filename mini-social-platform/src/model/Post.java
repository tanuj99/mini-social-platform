package model;

import java.time.Instant;
import java.util.UUID;

public class Post {

    private final String postId;
    private final String userId;
    private final String content;
    private final long createdAt;

    public Post(String userId, String content) {

        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("Invalid userId");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        this.postId = UUID.randomUUID().toString();
        this.userId = userId;
        this.content = content;
        this.createdAt = Instant.now().toEpochMilli();
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}