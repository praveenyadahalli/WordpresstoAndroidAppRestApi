package com.simplifiedcodeing;

import java.util.Map;

public class PostModel {
    private int postId;
    private Map<String, Object> title;
    private Map<String, Object> content;

    public PostModel(Map<String, Object> title, Map<String, Object> content, int postId) {
        this.title = title;
        this.content = content;
        this.postId = postId;
    }

    public Map<String, Object> getTitle() {
        return title;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public int getPostId() {
        return postId;
    }
}
